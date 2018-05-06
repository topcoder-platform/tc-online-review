/*
 * Copyright (C) 2009-2015 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.deliverable.search.UploadFilterBuilder;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection and email sending. This class handles the
 * Review  and Checkpoint Review phases. If the input is of other phase types, PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The (checkpoint) review phase can start as soon as
 * <ul>
 * <li>The dependencies are met.</li>
 * <li>Phase start date/time is reached (if specified).</li>
 * <li>There is at least one active Contest Submission or Checkpoint Submission.</li>
 * </ul>
 * and can stop when the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>All active submissions have one review scorecard for the phase.</li>
 * </ul>
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * @author tuenm, bose_java, argolite, waits, saarixx, myxgyy, FireIce, microsky, flexme, VolodymyrK, Vovka
 * @version 1.8.6
 */
abstract class BaseReviewPhaseHandler extends AbstractPhaseHandler {

    /**
     * Indicates whether the handler is for the "Checkpoint Review" phase or for the "Review" phase.
     */
    final private boolean isCheckpoint;

    /**
     * Stores the phase type name.
     */
    final private String phaseTypeName;

    /**
     * Stores the submission type name.
     */
    final private String submissionTypeName;

    /**
     * Stores the reviewer role names.
     */
    final private String[] reviewerRoleNames;

    /**
     * Represents the min required number of peer reviews.
     */
    final private Long minPeerReviews;

    /**
     * Represents the REST URL called to aggregate peer review scores.
     */
    final private String peerReviewAggregationURLTemplate;

    /**
     * Create a new instance of BaseReviewPhaseHandler using the given namespace for loading configuration settings.
     * @param namespace
     *            the namespace to load configuration settings from.
     * @param isCheckpoint
     *            True if the phase type is "Checkpoint Review" and false for "Review" phase type
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public BaseReviewPhaseHandler(String namespace, boolean isCheckpoint) throws ConfigurationException {
        super(namespace);
        this.isCheckpoint = isCheckpoint;

        if (isCheckpoint) {
            phaseTypeName = Constants.PHASE_CHECKPOINT_REVIEW;
            submissionTypeName = Constants.SUBMISSION_TYPE_CHECKPOINT_SUBMISSION;
            reviewerRoleNames = new String[] {Constants.ROLE_CHECKPOINT_REVIEWER };
        } else {
            phaseTypeName = Constants.PHASE_REVIEW;
            submissionTypeName = Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION;
            reviewerRoleNames = PhasesHelper.REVIEWER_ROLE_NAMES;
        }

        // Get the min number of required peer reviews
        minPeerReviews = Long.parseLong(PhasesHelper.getPropertyValue(ReviewPhaseHandler.class.getName(),
                "MinPeerReviews", true));

        // get the template for REST API URL to aggregate peer review scores for challenge
        peerReviewAggregationURLTemplate = PhasesHelper.getPropertyValue(ReviewPhaseHandler.class.getName(),
                "PeerReviewScoreAggregationAPI", true);

    }

    /**
     * Check if the input phase can be executed or not. This method will check the phase status to see what will be
     * executed. This method will be called by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * <li>Phase start date/time is reached (if specified).</li>
     * <li>There is at least one active Contest Submission or Checkpoint Submission.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met.</li>
     * <li>All active submissions have enough review scorecards for the phase.</li>
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
     * @param phase
     *            The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Review" or "Checkpoint Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, phaseTypeName);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        OperationCheckResult result;
        if (toStart) {
            // return true if all dependencies have stopped and start time has been reached.
            result = PhasesHelper.checkPhaseCanStart(phase);
            if (!result.isSuccess()) {
                return result;
            }

            // Search all "Active" submissions for current project with contest submission type
            Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                    phase.getProject().getId(), submissionTypeName);
            if (subs.length > 0) {
                return OperationCheckResult.SUCCESS;
            } else {
                return new OperationCheckResult("No submissions that passed screening");
            }
        } else {
            result = PhasesHelper.checkPhaseDependenciesMet(phase, false);
            if (!result.isSuccess()) {
                return result;
            }

            if (PhasesHelper.isPeerReview(getManagerHelper(), phase)) {
                if (!PhasesHelper.reachedPhaseEndTime(phase)) {
                    return new OperationCheckResult("Phase end time is not yet reached");
                }

                result = enoughPeerReviews(phase);
                if (!result.isSuccess()) {
                    return new OperationCheckResult("Not enough peer reviews");
                }
            } else {
                result = allReviewsDone(phase);
                if (!result.isSuccess()) {
                    return result;
                }
                if (!isCheckpoint) {
                    result = allTestCasesUploaded(phase);
                }
            }

            return result;
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
     * If the input phase status is Open, then it will perform the following additional logic to stop the phase:
     * Scores, statuses and prizes for all submissions that passed screening will be calculated and saved.
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Update for version 1.2, for start, put the reviewer/submission info for stop, put the result info.
     * </p>
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Review" or "Checkpoint Review" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, phaseTypeName);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();
        if (toStart) {
            // for start, puts the reviewer/submission info
            putPhaseStartInfoValues(phase, values);
        } else {
            if (PhasesHelper.isPeerReview(getManagerHelper(), phase)) {
                // call the REST API to aggregate peer reviews
                aggregatePeerReviews(phase, operator, values);
                values.put("PEER_REVIEW", 1);
            } else {
                // update the submission's initial score after review and set in the values map
                updateSubmissionScores(phase, operator, values);
                values.put("PEER_REVIEW", 0);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * <p>
     * Puts the start review phase information about submissions and reviewer info to the value map.
     * </p>
     * @param phase
     *            the current Phase, not null
     * @param values
     *            the values map
     * @throws PhaseHandlingException
     *             if any error occurs
     * @since 1.2
     */
    private void putPhaseStartInfoValues(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        // Search all "Active" submissions for current project with contest submission type
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), submissionTypeName);
        values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(subs,
            getManagerHelper().getResourceManager(), false));
        // Search the reviewIds
        Resource[] reviewers = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            reviewerRoleNames, phase.getId());

        if (isCheckpoint) {
            values.put("NEED_CHECKPOINT_REVIEWER", reviewers.length == 0 ? 1 : 0);
        } else {
            if (PhasesHelper.isPeerReview(getManagerHelper(), phase)) {
                values.put("PEER_REVIEW", 1);
            } else {
                values.put("PEER_REVIEW", 0);

                if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) == null) {
                    throw new PhaseHandlingException(
                            "Review Number phase criteria is not set for project : " + phase.getProject().getId());
                }

                int requiredReviewers = PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER);
                values.put("N_REQUIRED_REVIEWERS", requiredReviewers);
                values.put("N_REVIEWERS", reviewers.length);
                values.put("NEED_REVIEWER", reviewers.length >= requiredReviewers ? 0 : 1);
            }
        }
    }

    /**
     * This method calculates scores, statuses and prizes of all submissions that passed screening and saves them.
     * It is called from perform method when phase is stopping.
     * <p>
     * Update for version 1.2, for stop, put the result info.
     * </p>
     * <p>
     * Changes in version 1.6: For studio competitions, there will be no Appeal, Appeal Response, Aggregation, and
     * Aggregation Review Phase, so the ranking and score will be calculated here, and no change later.
     * </p>
     * @param phase
     *            phase instance.
     * @param operator
     *            the operator name.
     * @param values
     *            Map of email fields. The method can optionally add new fields as necessary.
     * @throws PhaseHandlingException
     *             in case of error when retrieving/updating data.
     */
    private void updateSubmissionScores(Phase phase, String operator, Map<String, Object> values)
        throws PhaseHandlingException {

        try {
            Project project = getManagerHelper().getProjectManager().getProject(phase.getProject().getId());
            boolean isStudioProject = PhasesHelper.isStudio(project);
            Phase appealsResponsePhase = PhasesHelper.locatePhase(phase, Constants.PHASE_APPEALS_RESPONSE, true, false);

            Submission[] subs = PhasesHelper.updateSubmissionsResults(getManagerHelper(), phase, operator,
                true, isCheckpoint || isStudioProject || appealsResponsePhase == null);

            // add the submission result to the values map
            DecimalFormat df = new DecimalFormat("#.##");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            for (Submission sub : subs) {
                Map<String, Object> infos = new HashMap<String, Object>();
                Resource submitter = getManagerHelper().getResourceManager().getResource(sub.getUpload().getOwner());
                infos.put("SUBMITTER_HANDLE", PhasesHelper.notNullValue(submitter.getProperty(PhasesHelper.HANDLE)));
                infos.put("SUBMITTER_SCORE", df.format(sub.getInitialScore()));
                result.add(infos);
            }
            values.put("SUBMITTER", result);

            boolean allSubmissionsFailed = false;
            if (!isCheckpoint) {
                Submission[] activeSubs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                        phase.getProject().getId(), Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);
                allSubmissionsFailed = activeSubs == null || activeSubs.length == 0;
            }

            values.put("NO_REVIEW_PASS", allSubmissionsFailed ? 1 : 0);
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        } catch (PersistenceException e) {
            throw new PhaseHandlingException("Fail to retrieve the corresponding project.", e);
        }
    }

    /**
     * This method calls the REST API to aggregate peer reviews and update submissions.
     * @param phase
     *            phase instance.
     * @param operator
     *            the operator name.
     * @param values
     *            Map of email fields. The method can optionally add new fields as necessary.
     * @throws PhaseHandlingException
     *             in case of error when retrieving/updating data.
     */
    private void aggregatePeerReviews(Phase phase, String operator, Map<String, Object> values)
            throws PhaseHandlingException {
        HttpClient client = new DefaultHttpClient();

        String restURL = peerReviewAggregationURLTemplate;
        restURL = restURL.replace("{challengeId}", String.valueOf(phase.getProject().getId()));

        try {
            HttpPut put = new HttpPut(restURL);

            put.setHeader("Content-type", "application/json");

            HttpResponse response = client.execute(put);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(result.toString());

            JsonNode success = jsonNode.get("result").get("success");
            JsonNode status = jsonNode.get("result").get("status");

            if(!success.asText().equalsIgnoreCase("true") && !status.asText().equals("200")) {
                throw new PhaseHandlingException(String.format("Calling API %s failed, success: %s, status:%s",
                        success.asText(), status.asText()));
            }


        } catch (PhaseHandlingException phex) {
            throw phex;
        } catch (Exception ex) {
            throw new PhaseHandlingException("Failed to call peer review score aggregation API", ex);
        }



        try {
            Submission[] subs = PhasesHelper.getProjectSubmissions(getManagerHelper().getUploadManager(),
                    phase.getProject().getId(),
                    Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION,
                    new String[]{
                            Constants.SUBMISSION_STATUS_ACTIVE,
                            Constants.SUBMISSION_STATUS_FAILED_REVIEW,
                            Constants.SUBMISSION_STATUS_FAILED_CHECKPOINT_REVIEW,
                            Constants.SUBMISSION_STATUS_COMPLETED_WITHOUT_WIN});

            // add the submission result to the values map
            DecimalFormat df = new DecimalFormat("#.##");
            List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
            Set<Long> subSet = new HashSet<Long>();
            for (Submission sub : subs) {
                if(subSet.contains(sub.getId())) {
                    continue;
                }
                Map<String, Object> infos = new HashMap<String, Object>();
                Resource submitter = getManagerHelper().getResourceManager().getResource(sub.getUpload().getOwner());
                infos.put("SUBMITTER_HANDLE", PhasesHelper.notNullValue(submitter.getProperty(PhasesHelper.HANDLE)));
                infos.put("SUBMITTER_SCORE", df.format(sub.getFinalScore()));
                result.add(infos);
                subSet.add(sub.getId());
            }
            values.put("SUBMITTER", result);

        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Problem when looking up resource for the submission.", e);
        }
    }

    /**
     * This method checks if all active submissions have one review scorecard from each reviewer for the phase and
     * returns true if conditions are met, false otherwise.
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase
     *            the phase instance.
     * @return the validation result indicating whether all the reviews are done, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseHandlingException
     *             if there was an error retrieving data.
     */
    private OperationCheckResult allReviewsDone(Phase phase) throws PhaseHandlingException {
        // Search all "Active" submissions for current project
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), submissionTypeName);

        // Search the reviewIds
        Resource[] reviewers = PhasesHelper.searchResourcesForRoleNames(getManagerHelper(),
            reviewerRoleNames, phase.getId());

        if (isCheckpoint) {
            if (reviewers.length != 1) {
                 return new OperationCheckResult("There should be exactly one checkpoint reviewer.");
             }
        } else {
            if (phase.getAttribute(Constants.PHASE_CRITERIA_REVIEWER_NUMBER) == null) {
                throw new PhaseHandlingException(
                    "Review Number phase criteria is not set for project : " + phase.getProject().getId());
            }

            if (reviewers.length < PhasesHelper.getIntegerAttribute(phase, Constants.PHASE_CRITERIA_REVIEWER_NUMBER)) {
                return new OperationCheckResult("Not enough reviewers for project: " + phase.getProject().getId());
            }
        }

        // Search all review scorecard for the current phase
        Review[] reviews = PhasesHelper.searchReviewsForResources(getManagerHelper(), reviewers, phase.getId(), null);

        // for each submission
        for (Submission submission : subs) {
            long subId = submission.getId();
            int numberOfReviews = 0;

            // Match the submission with its reviews
            for (Review review : reviews) {
                // check if review is committed
                if (!review.isCommitted()) {
                    return new OperationCheckResult("There are uncommitted reviews.");
                }
                if (subId == review.getSubmission()) {
                    numberOfReviews++;
                }
            }

            // if number of reviews does not match number of reviewers
            if (numberOfReviews < reviewers.length) {
                return new OperationCheckResult("Not all reviewers submitted their reviews for submission " + subId);
            }

            if (numberOfReviews > reviewers.length) {
                throw new PhaseHandlingException("There are extra reviews for submission " + subId);
            }
        }

        return OperationCheckResult.SUCCESS;
    }

    /**
     * This method checks if all active submissions have enough committed peer review scorecards and
     * returns true if conditions are met, false otherwise.
     * @param phase
     *            the phase instance.
     * @return the validation result indicating whether there ae enough peer reviews, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseHandlingException
     *             if there was an error retrieving data.
     */
    private OperationCheckResult enoughPeerReviews(Phase phase) throws PhaseHandlingException {
        // Search all "Active" submissions for current project
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), submissionTypeName);

        // Search all review scorecard for the current phase
        Review[] reviews = PhasesHelper.searchReviewsForPhase(getManagerHelper(), phase.getId(), null);

        // for each submission
        for (Submission submission : subs) {
            long subId = submission.getId();
            int numberOfReviews = 0;

            // Match the submission with its reviews
            for (Review review : reviews) {
                // check if review is committed
                if (!review.isCommitted()) {
                    continue;
                }
                if (subId == review.getSubmission()) {
                    numberOfReviews++;
                }
            }

            // if not enough committed peer reviews
            if (numberOfReviews < minPeerReviews) {
                return new OperationCheckResult("Not enough committed peer reviews for submission " + subId);
            }
        }

        return OperationCheckResult.SUCCESS;
    }

    /**
     * This method checks if all test case reviewers have one test case uploaded.
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase
     *            the phase instance.
     * @return the validation result indicating whether all the test cases are uploaded, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseHandlingException
     *             if any error occurred when retrieving data.
     */
    private OperationCheckResult allTestCasesUploaded(Phase phase) throws PhaseHandlingException {
        // get test case reviewers for phase id
        Resource[] reviewers = getDevelopmentReviewers(phase);

        // if there are no test case reviewers, no need to check if all uploads have been uploaded
        if (reviewers.length == 0) {
            return OperationCheckResult.SUCCESS;
        }

        try {
            UploadManager uploadManager = getManagerHelper().getUploadManager();            
            long uploadTypeId = LookupHelper.getUploadType(uploadManager, Constants.UPLOAD_TYPE_TEST_CASE).getId();
            Filter uploadTypeFilter = UploadFilterBuilder.createUploadTypeIdFilter(uploadTypeId);
            
            long uploadStatusId = LookupHelper.getUploadStatus(uploadManager, Constants.UPLOAD_STATUS_ACTIVE).getId();
            Filter uploadStatusFilter = UploadFilterBuilder.createUploadStatusIdFilter(uploadStatusId);
            
            Filter phaseFilter = UploadFilterBuilder.createProjectPhaseIdFilter(phase.getId());

            Upload[] uploads = uploadManager.searchUploads(
                    new AndFilter(Arrays.asList(uploadTypeFilter, uploadStatusFilter, phaseFilter)));

            for (Resource reviewer : reviewers) {
                // match reviewer with test case upload
                boolean found = false;

                for (Upload upload : uploads) {
                    if (reviewer.getId() == upload.getOwner()) {
                        found = true;
                        break;
                    }
                }

                // if a test case upload is not found, return false
                if (!found) {
                    return new OperationCheckResult("Not all test cases are uploaded (see reviewer with "
                            + reviewer.getResourceRole().getName() + " role)");
                }
            }
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Problem retrieving uploads", e);
        } catch (SearchBuilderException e) {
            throw new PhaseHandlingException("Problem with search builder", e);
        }

        return OperationCheckResult.SUCCESS;
    }

    /**
     * This method retrieves the reviewer ids for Accuracy, Failure and Stress reviews for the given phase id.
     * @param phase
     *            the phase instance.
     * @return reviewers matching search criteria.
     * @throws PhaseHandlingException
     *             if any error occurred when retrieving data.
     */
    private Resource[] getDevelopmentReviewers(Phase phase) throws PhaseHandlingException {
        String[] roleNames = new String[] {
            Constants.ROLE_ACCURACY_REVIEWER,
            Constants.ROLE_FAILURE_REVIEWER,
            Constants.ROLE_STRESS_REVIEWER};

        return PhasesHelper.searchResourcesForRoleNames(getManagerHelper(), roleNames, phase.getId());
    }
}
