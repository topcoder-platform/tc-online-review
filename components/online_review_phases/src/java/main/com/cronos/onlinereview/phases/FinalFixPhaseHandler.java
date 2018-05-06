/*
 * Copyright (C) 2009-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilder;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.data.Review;

import com.topcoder.project.phases.Phase;

import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;

import java.util.Arrays;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handle the
 * aggregation phase. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The final fix phase can start as soon as the dependencies are met and can stop when the following conditions
 * met: the dependencies are met and The final fix has been uploaded.
 * </p>
 * <p>
 * The additional logic for executing this phase is: When Final Fix is starting and Final Review worksheet is not
 * created, it should be created; otherwise it should be marked uncommitted. Previous final fix upload will be
 * deleted.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.0.1 (Checkpoint Support Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #checkFinalReviewWorksheet(Phase, String)} method to filter submissions for submission type.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>The return changes from boolean to OperationCheckResult.</li>
 * <li>A safety check was added to prevent creating duplicated final reviews.</li>
 * </ul>
 * </p>
 * @author tuenm, bose_java, isv, saarixx, microsky, VolodymyrK
 * @version 1.7.10
 */
public class FinalFixPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.FinalFixPhaseHandler";

    /**
     * array of comment types to be copied from aggregation scorecard to new
     * final review worksheet.
     */
    private static final String[] COMMENT_TYPES_TO_COPY = new String[] {
        Constants.COMMENT_TYPE_COMMENT, Constants.COMMENT_TYPE_REQUIRED,
        Constants.COMMENT_TYPE_RECOMMENDED, Constants.COMMENT_TYPE_APPEAL,
        Constants.COMMENT_TYPE_APPEAL_RESPONSE, Constants.COMMENT_TYPE_AGGREGATION_COMMENT,
        Constants.COMMENT_TYPE_SUBMITTER_COMMENT, Constants.COMMENT_TYPE_MANAGER_COMMENT,
        Constants.COMMENT_TYPE_FINAL_FIX_COMMENT, Constants.COMMENT_TYPE_FINAL_REVIEW_COMMENT};

    /**
     * Create a new instance of FinalFixPhaseHandler using the default namespace
     * for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public FinalFixPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of FinalFixPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public FinalFixPhaseHandler(String namespace) throws ConfigurationException {
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
     * conditions: The dependencies are met
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions: the dependencies are met and The final fix has been uploaded.
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
     *             "Final Fix" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_FINAL_FIX);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached.
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            // Get nearest Final Review phase
            Phase finalReviewPhase = PhasesHelper.locatePhase(phase,
                            Constants.PHASE_FINAL_REVIEW, true, false);
            if (finalReviewPhase == null) {
                return new OperationCheckResult("Final Review phase cannot be located");
            }

            Resource[] finalReviewer = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                new String[] {Constants.ROLE_FINAL_REVIEWER }, finalReviewPhase.getId());

            // return true if there is a final reviewer
            if (finalReviewer.length == 1) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("There must be one Final Reviewer assigned for the phase");
            }
        } else {
            // return true if all dependencies have stopped and final fix exists.
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }
            if (getFinalFix(phase) != null) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("Final fix is not yet uploaded");
            }
        }
    }

    /**
     * <p>
     * Provides additional logic to execute a phase. This method will be called by start() and end() methods of
     * PhaseManager implementations in Phase Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be send on start phase or end phase
     * base on configuration settings.
     * </p>
     * <p>
     * If the input phase status is Scheduled, then it will perform the following additional logic to start the
     * phase: if Final Review worksheet is not created, it should be created; otherwise it should be marked
     * uncommitted. Previous final fix upload will be deleted.
     * </p>
     * <p>
     * If the input phase status is Open, then it will do nothing.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             "Final Fix" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_FINAL_FIX);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            checkFinalReviewWorksheet(phase, operator);
        }

        sendPhaseEmails(phase);
    }

    /**
     * This method is called from perform method when phase is starting. If
     * Final Review worksheet is not created, it is created; otherwise it is
     * marked uncommitted. Previous final fix upload is deleted.
     * @param phase phase instance.
     * @param operator operator name.
     * @throws PhaseHandlingException if an error occurs when retrieving/saving data.
     */
    private void checkFinalReviewWorksheet(Phase phase, String operator) throws PhaseHandlingException {
        // Get nearest Final Review phase
        Phase finalReviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_FINAL_REVIEW, true, true);
        Phase previousFinalReviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_FINAL_REVIEW, false, false);
        Phase previousApprovalPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_APPROVAL, false, false);

        try {
            // Sometimes phase management framework fails to open the final fix phase (e.g. due to a DB error)
            // at a later stage after the final review worksheet is created. In those rare cases the final review
            // worksheet would be created again on the next iteration of the autopilot. To prevent the duplication
            // this method checks if the final review worksheet already exists for this phase and returns if it does.
            if (PhasesHelper.getWorksheet(getManagerHelper(), finalReviewPhase.getId()) != null) {
                return;
            }

            // Search for id of the Final Reviewer
            Resource[] resources = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
                new String[] {Constants.ROLE_FINAL_REVIEWER }, finalReviewPhase.getId());

            if (resources.length == 0) {
                throw new PhaseHandlingException(
                                "No Final Reviewer found for phase: " + finalReviewPhase.getId());
            }

            Review finalWorksheet = null;
            if (previousApprovalPhase != null) {
                Review[] approvalWorksheets = PhasesHelper.searchReviewsForPhase(getManagerHelper(),
                        previousApprovalPhase.getId(), null);

                if (approvalWorksheets == null || approvalWorksheets.length == 0) {
                    throw new PhaseHandlingException("Approval worksheets do not exist.");
                }

                finalWorksheet = new Review();

                // Copy the review items.
                for (Review approvalWorksheet : approvalWorksheets) {
                    finalWorksheet.setScorecard(approvalWorksheet.getScorecard());
                    finalWorksheet.setSubmission(approvalWorksheet.getSubmission());

                    // No review comments to copy from approval, only the review item comments.
                    PhasesHelper.copyReviewItems(approvalWorksheet, finalWorksheet, COMMENT_TYPES_TO_COPY);
                }

            } else if (previousFinalReviewPhase != null) {
                finalWorksheet = PhasesHelper.getWorksheet(
                    getManagerHelper(), previousFinalReviewPhase.getId());
            }

            if (finalWorksheet == null) {
                // create the final review worksheet...

                // create review object
                finalWorksheet = new Review();
                finalWorksheet.setProjectPhase(finalReviewPhase.getId());
                finalWorksheet.setAuthor(resources[0].getId());
                finalWorksheet.setCommitted(false);

                // Locate the nearest backward Aggregation phase
                Phase aggPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_AGGREGATION, false, false);
                if (aggPhase != null) {
                    // Create final review from aggregation worksheet

                    // Search the aggregated review scorecard
                    Review aggWorksheet = PhasesHelper.getWorksheet(getManagerHelper(), aggPhase.getId());

                    if (aggWorksheet == null) {
                        throw new PhaseHandlingException("aggregation worksheet does not exist.");
                    }

                    // copy the comments and review items
                    PhasesHelper.copyComments(aggWorksheet, finalWorksheet, COMMENT_TYPES_TO_COPY, null);
                    PhasesHelper.copyFinalReviewItems(aggWorksheet, finalWorksheet);

                    // set the ID of a scorecard which the review scorecard
                    // corresponds to
                    finalWorksheet.setScorecard(aggWorksheet.getScorecard());
                    finalWorksheet.setSubmission(aggWorksheet.getSubmission());
                } else {
                    Phase reviewPhase = PhasesHelper.locatePhase(phase, Constants.PHASE_REVIEW, false, true);

                    // find winning submitter.
                    Resource winningSubmitter = PhasesHelper.getWinningSubmitter(
                        getManagerHelper(), phase.getProject().getId());
                    if (winningSubmitter == null) {
                        throw new PhaseHandlingException(
                                        "No winner for project with id" + phase.getProject().getId());
                    }

                    // find the winning submission
                    UploadManager uploadManager = getManagerHelper().getUploadManager();
                    Filter winnerFilter = SubmissionFilterBuilder.createResourceIdFilter(winningSubmitter.getId());

                    long activeStatusId = LookupHelper.getSubmissionStatus(uploadManager, Constants.SUBMISSION_STATUS_ACTIVE).getId();
                    Filter submissionStatusFilter = SubmissionFilterBuilder.createSubmissionStatusIdFilter(activeStatusId);

                    long submissionTypeId = LookupHelper.getSubmissionType(uploadManager,
                        Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION).getId();
                    Filter submissionTypeFilter = SubmissionFilterBuilder.createSubmissionTypeIdFilter(submissionTypeId);

                    Filter filter = new AndFilter(Arrays.asList(winnerFilter, submissionTypeFilter,
                                                                submissionStatusFilter));

                    // change in version 1.4
                    Submission[] submissions = uploadManager.searchSubmissions(filter);
                    if (submissions == null || submissions.length != 1) {
                        throw new PhaseHandlingException("No winning submission for project with id"
                            + phase.getProject().getId());
                    }
                    Long winningSubmissionId = submissions[0].getId();

                    // Search all review scorecard from review phase for the
                    // winning submitter
                    Review[] reviews = PhasesHelper.searchReviewsForPhase(
                        getManagerHelper(), reviewPhase.getId(), winningSubmissionId);

                    for (Review review : reviews) {
                        finalWorksheet.setScorecard(review.getScorecard());
                        finalWorksheet.setSubmission(review.getSubmission());
                        PhasesHelper.copyComments(review, finalWorksheet, COMMENT_TYPES_TO_COPY, null);
                        PhasesHelper.copyReviewItems(review, finalWorksheet, COMMENT_TYPES_TO_COPY);
                    }
                }

                // persist the review
                getManagerHelper().getReviewManager().createReview(finalWorksheet, operator);
            } else {
                // Mark Final Review worksheet uncommitted
                finalWorksheet.setAuthor(resources[0].getId());
                finalWorksheet.setProjectPhase(finalReviewPhase.getId());
                finalWorksheet.setCommitted(false);

                // persist the copy
                finalWorksheet = PhasesHelper.cloneReview(finalWorksheet);
                getManagerHelper().getReviewManager().createReview(finalWorksheet, operator);
            }

        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Problem when persisting review", e);
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem when persisting upload", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem when retrieving winning submission.", e);
        }
    }

    /**
     * This retrieves the final fix upload. It returns null if none exist.
     * @param phase phase instance.
     * @return the final fix upload, null if none exist.
     * @throws PhaseHandlingException if error occurs when retrieving, or if multiple uploads exist.
     */
    private Upload getFinalFix(Phase phase) throws PhaseHandlingException {
        UploadManager uploadManager = getManagerHelper().getUploadManager();

        try {
            // get final fix upload based on the project phase ID, "Final Fix" type
            // and winning submitter resource id filters.
            long uploadTypeId = LookupHelper.getUploadType(uploadManager, Constants.UPLOAD_TYPE_FINAL_FIX).getId();
            Filter uploadTypeFilter = UploadFilterBuilder.createUploadTypeIdFilter(uploadTypeId);
            Filter uploadStatusFilter = UploadFilterBuilder.createProjectPhaseIdFilter(phase.getId());
            Resource winningSubmitter = PhasesHelper.getWinningSubmitter(
                getManagerHelper(),  phase.getProject().getId());
            Filter resourceIdFilter = UploadFilterBuilder.createResourceIdFilter(winningSubmitter.getId());

            Filter fullFilter = new AndFilter(Arrays.asList(uploadTypeFilter, uploadStatusFilter, resourceIdFilter));
            Upload[] uploads = uploadManager.searchUploads(fullFilter);

            if (uploads.length == 0) {
                return null;
            } else if (uploads.length == 1) {
                return uploads[0];
            } else {
                throw new PhaseHandlingException("There cannot be multiple final fix uploads");
            }
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem when retrieving upload", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }
    }
}
