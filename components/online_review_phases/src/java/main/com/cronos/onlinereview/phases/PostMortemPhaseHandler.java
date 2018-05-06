/*
 * Copyright (C) 2009-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.cronos.termsofuse.dao.ProjectTermsOfUseDao;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.UserTermsOfUseDao;
import com.cronos.termsofuse.model.TermsOfUse;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handle the
 * Post-Mortem phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The Post-Mortem phase can start as soon as the dependencies are met and can stop when the following conditions
 * met: - The dependencies are met; - The post-mortem scorecards are committed. - At least the required number of
 * Post-Mortem Reviewer resources have filled in a scorecard
 * </p>
 * <p>
 * Version 1.2 change: add the logic to send out email within enhanced template and role-options.
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #allPostMortemReviewsDone(Phase)} method to use appropriate logic for searching for review
 * scorecards tied to project but not to phase type.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 (Members Post-Mortem Reviews Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #perform(Phase, String)} method to add submitters and reviewers as Post-Mortem reviewers for
 * project.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>The return changes from boolean to OperationCheckResult.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * @author argolite, waits, saarixx, isv, FireIce, microsky
 * @version 1.8.5
 */
public class PostMortemPhaseHandler extends AbstractPhaseHandler {
    /**
     * The default namespace of PostMortemPhaseHandler.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.PostMortemPhaseHandler";

    /**
     * <p>
     * A <code>String</code> array listing the names for roles for resources which are candidates for adding to
     * project as Post-Mortem reviewers when Post-Mortem phase starts.
     * </p>
     * @since 1.4
     */
    private static final String[] POST_MORTEM_REVIEWER_CANDIDATE_ROLE_NAMES = new String[] {
        Constants.ROLE_SUBMITTER, Constants.ROLE_REVIEWER,
        Constants.ROLE_ACCURACY_REVIEWER, Constants.ROLE_FAILURE_REVIEWER,
        Constants.ROLE_STRESS_REVIEWER, Constants.ROLE_COPILOT };

    /**
     * <p>
     * A <code>String</code> array listing the name for Post-Mortem Reviewer role.
     * </p>
     * @since 1.4
     */
    private static final String[] POST_MORTEM_REVIEWER_FILTER = new String[] {Constants.ROLE_POST_MORTEM_REVIEWER };

    /**
     * <p>
     * A <code>String</code> providing the name for email property of the resource.
     * </p>
     * @since 1.4
     */
    private static final String EMAIL = "Email";

    /**
     * <p>
     * A <code>String</code> providing the name for payment status property of the resource.
     * </p>
     * @since 1.4
     */
    private static final String PAYMENT_STATUS = "Payment Status";

    /**
     * <p>
     * A <code>String</code> providing the name for registration date property of the resource.
     * </p>
     * @since 1.4
     */
    private static final String REGISTRATION_DATE = "Registration Date";

    /**
     * <p>
     * A <code>String</code> providing the name for rating property of the resource.
     * </p>
     * @since 1.4
     */
    private static final String RATING = "Rating";

    /**
     * <p>
     * A <code>Log</code> used for logging the events.
     * </p>
     * @since 1.4
     */
    private static final Log log = LogManager.getLog(PostMortemPhaseHandler.class.getName());

    /**
     * Creates a PostMortemPhaseHandler with default namespace.
     * @throws ConfigurationException if there is configuration error.
     */
    public PostMortemPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Creates a PostMortemPhaseHandler with the given namespace.
     * @param namespace the namespace of PostMortemPhaseHandler.
     * @throws ConfigurationException if there is configuration error.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PostMortemPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * <p>
     * Check if the input phase can be executed or not. This method will check the phase status to see what will be
     * executed. This method will be called by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: The dependencies are met.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>All post-mortem scorecards are committed.</li>
     * <li>At least the required number of Post-Mortem Reviewer resources have filled in a scorecard (use the
     * Reviewer Number phase criteria).</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException if the input phase type is not
     *             "Post-Mortem Review" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_POST_MORTEM);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            // return true if all dependencies have stopped
            // and start time has been reached.
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            // Check phase dependencies
            OperationCheckResult result = PhasesHelper.checkPhaseDependenciesMet(phase, false);

            // Return true is dependencies are met and minimum number of reviews committed in time before phase
            // deadline.
            if (!result.isSuccess()) {
                return result;
            }
            if (!PhasesHelper.reachedPhaseEndTime(phase)) {
                return new OperationCheckResult("Phase end time is not yet reached");
            }
            if (!allPostMortemReviewsDone(phase)) {
                return new OperationCheckResult("Not enough post-mortem scorecards are committed");
            }
            return OperationCheckResult.SUCCESS;
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. If the phase starts then all submitters who have submitted for
     * project and all reviewers are assigned <code>Post-Mortem Reviewer</code> role for project (if not already)
     * and emails are sent on phase state transition.
     * </p>
     * <p>
     * Version 1.6 Change notes:
     * <ol>
     * <li>Updated to use the cached ProjectRoleTermsOfUse and UserTermsOfUse instance.</li>
     * </ol>
     * </p>
     * @param phase
     *            the input phase.
     * @param operator
     *            the operator name.
     * @throws PhaseHandlingException
     *             if there is any error occurs.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        // perform parameters checking
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_POST_MORTEM);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        // send email now if it is going to start
        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            ManagerHelper managerHelper = getManagerHelper();
            ResourceManager resourceManager = managerHelper.getResourceManager();
            DateFormat dateFormatter = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

            try {
                // Locate the role for Post-Mortem Reviewer
                ResourceRole postMortemReviewerRole = LookupHelper.getResourceRole(resourceManager, Constants.ROLE_POST_MORTEM_REVIEWER);

                long projectId = phase.getProject().getId();

                // Get the list of existing Post-Mortem reviewers assigned to project so far
                Resource[] existingPostMortemResources = PhasesHelper.searchProjectResourcesForRoleNames(
                    managerHelper, POST_MORTEM_REVIEWER_FILTER, projectId);
                // Add Submitters (who have submitted), reviewers as Post-Mortem Reviewers but only if they are not
                // assigned to project as Post-Mortem reviewers already and have all necessary terms of use
                // accepted
                Resource[] candidatePostMortemResources = PhasesHelper.searchProjectResourcesForRoleNames(
                    managerHelper, POST_MORTEM_REVIEWER_CANDIDATE_ROLE_NAMES, projectId);
                for (Resource resource : candidatePostMortemResources) {
                    boolean valid = false;
                    String roleName = resource.getResourceRole().getName();
                    if (Constants.ROLE_SUBMITTER.equalsIgnoreCase(roleName)) {
                        Long[] submissionIds = resource.getSubmissions();
                        if ((submissionIds != null) && (submissionIds.length > 0)) {
                            valid = true;
                        }
                    } else {
                        valid = true;
                    }
                    if (valid) {
                        Long candidateId = resource.getUserId();
                        boolean alreadyPostMortemReviewer = false;
                        for (Resource existingPostMortemReviewer : existingPostMortemResources) {
                            Long existingId = existingPostMortemReviewer.getUserId();
                            if (existingId != null && existingId.equals(candidateId)) {
                                alreadyPostMortemReviewer = true;
                                break;
                            }
                        }
                        if (!alreadyPostMortemReviewer) {
                            String candidateUserHandle = (String) resource.getProperty(PhasesHelper.HANDLE);
                            boolean hasPendingTerms = hasPendingTermsOfUse(projectId, candidateId,
                                                                           postMortemReviewerRole.getId());
                            if (!hasPendingTerms) {
                                Resource newPostMortemReviewer = new Resource();
                                newPostMortemReviewer.setResourceRole(postMortemReviewerRole);
                                newPostMortemReviewer.setProject(projectId);
                                newPostMortemReviewer.setPhase(phase.getId());
                                newPostMortemReviewer.setUserId(candidateId);
                                newPostMortemReviewer.setProperty("External Reference ID", String.valueOf(candidateId));
                                newPostMortemReviewer.setProperty(PhasesHelper.HANDLE, candidateUserHandle);
                                newPostMortemReviewer.setProperty(EMAIL, resource.getProperty(EMAIL));
                                newPostMortemReviewer.setProperty(RATING, resource.getProperty(RATING));
                                newPostMortemReviewer.setProperty(PAYMENT_STATUS, "N/A");
                                newPostMortemReviewer.setProperty(REGISTRATION_DATE,
                                    dateFormatter.format(new Date()));

                                resourceManager.updateResource(newPostMortemReviewer, operator);
                            } else {
                                log.log(Level.WARN, "Can not assign Post-Mortem Reviewer role to user "
                                        + candidateUserHandle + "(ID: " + candidateId + ") "
                                        + "for project " + projectId
                                        + " as user does not have accepted "
                                        + "all necessary terms of use for project");
                            }
                        }
                    }
                }

                Resource[] postMortemResources = PhasesHelper.searchProjectResourcesForRoleNames(managerHelper,
                    POST_MORTEM_REVIEWER_FILTER, projectId);

                // according to discussion here http://forums.topcoder.com/?module=Thread&threadID=659556&start=0
                // if the attribute is not set, default value would be 0
                int requiredRN = 0;
                if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
                    requiredRN = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
                }
                values.put("N_REQUIRED_POST_MORTEM_REVIEWERS", requiredRN);
                values.put("N_POST_MORTEM_REVIEWERS", postMortemResources.length);
                values.put("NEED_POST_MORTEM_REVIEWERS", requiredRN <= postMortemResources.length ? 0 : 1);
            } catch (ResourcePersistenceException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            } catch (TermsOfUsePersistenceException e) {
                throw new PhaseHandlingException("Failed to add new Post-Mortem Reviewer resource", e);
            }

            // Send the review feedback email only if the Review phase has closed.
            Phase reviewPhase = PhasesHelper.getReviewPhase(phase.getProject());
            if (reviewPhase != null && PhasesHelper.isPhaseClosed(reviewPhase.getPhaseStatus())) {
                sendReviewFeedbackEmails(phase, values);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * Checks whether all post-mortem reviews are committed, and whether the
     * reviews number meets the requirement.
     * @param phase the phase to check.
     * @return true if all post-mortem reviews are committed and meet
     *         requirement, false otherwise.
     * @throws PhaseHandlingException if there was an error retrieving data.
     */
    private boolean allPostMortemReviewsDone(Phase phase) throws PhaseHandlingException {
        // Search all post-mortem review scorecards for the current phase
        Review[] reviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), null);

        // Count number of committed reviews
        int committedReviewsCount = 0;
        for (Review review : reviews) {
            if (review.isCommitted()) {
                committedReviewsCount++;
            }
        }

        // Deny to close phase if minimum number of reviews is not committed
        if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) != null) {
            int reviewerNum = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
            if (committedReviewsCount < reviewerNum) {
                return false;
            }
        }

        return true;
    }

    /**
     * <p>
     * Validates that specified user which is going to be assigned specified role for specified project has
     * accepted all terms of use set for specified role in context of the specified project.
     * </p>
     * @param projectId a <code>long</code> providing the project ID.
     * @param userId a <code>long</code> providing the user ID.
     * @param roleId a <code>long</code> providing the role ID.
     * @return a <code>true</code> if there are terms of user which are not yet accepted by the specified user or
     *         <code>false</code> if all necessary terms of use are accepted.
     * @throws TermsOfUsePersistenceException if any errors occur while accessing terms of use persistence.
     * @since 1.4
     */
    private boolean hasPendingTermsOfUse(long projectId, long userId, long roleId)
        throws TermsOfUsePersistenceException {
        // get remote services
        ManagerHelper managerHelper = getManagerHelper();
        ProjectTermsOfUseDao projectTermsOfUseDao = managerHelper.getProjectTermsOfUseDao();
        UserTermsOfUseDao userTermsOfUseDao = managerHelper.getUserTermsOfUseDao();

        Map<Integer, List<TermsOfUse>> necessaryTerms =
            projectTermsOfUseDao.getTermsOfUse((int) projectId, (int) roleId, null);

        if (necessaryTerms != null && !necessaryTerms.isEmpty()) {
            boolean hasGroupWithAllTermsAccepted = false;
            for (Integer groupId : necessaryTerms.keySet()) {
                boolean hasNonAcceptedTermsForGroup = false;
                List<TermsOfUse> groupTermsOfUse = necessaryTerms.get(groupId);
                for (TermsOfUse terms : groupTermsOfUse) {
                    long termsId = terms.getTermsOfUseId();
                    // check if the user has this terms
                    if (!userTermsOfUseDao.hasTermsOfUse(userId, termsId)) {
                        hasNonAcceptedTermsForGroup = true;
                    }
                }
                if (!hasNonAcceptedTermsForGroup) {
                    hasGroupWithAllTermsAccepted = true;
                    break; // User has at least one terms group with all terms from that group accepted
                          // so there is no need to check other terms of use groups
                }
            }
            if (!hasGroupWithAllTermsAccepted) {
                return true;
            }
        }

        return false;
    }}