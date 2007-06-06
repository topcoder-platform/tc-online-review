package com.topcoder.onlinereview.fixer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.topcoder.util.log.Level;

/**
 * Online Review Placement and score fixer main class.
 * 
 * @author hohosky
 */
public class OnlineReviewScoreRankFixer {
    /**
     * SQL statement for getting all the completed component (Design or Development)'s project id.
     */
    private static final String GET_ALL_COMPONENT_PROJECT_ID = "SELECT project.project_id id, project.project_category_id type"
                    + ", project_info.value name FROM project, project_info "
                    + "WHERE project_status_id = 7 AND project_category_id in (1,2) "
                    + " AND project_info.project_id = project.project_id "
                    + "AND project_info.project_info_type_id = 6";

    /**
     * SQL statement for retrieving all review scores for a project's submissions. project.
     */
    private static final String GET_REVIEW_SCORES = "SELECT review.score score, submission.submission_id id "
                    + "FROM review, resource, upload, submission, scorecard "
                    + "WHERE review.resource_id = resource.resource_id "
                    + "AND   review.submission_id = submission.submission_id "
                    + "AND   upload.upload_id = submission.upload_id "
                    + "AND   resource.resource_role_id NOT IN (8,9) "
                    + "AND   review.scorecard_id = scorecard.scorecard_id " + " AND scorecard.scorecard_type_id = 2 "
                    + "AND   upload.project_id = ? ORDER BY submission.submission_id";

    /**
     * SQL statement for retrieving a submission's final result (final score and rank) from the
     * table project_result.
     */
    private static final String GET_SUBMISSION_FINAL_RESULT = "SELECT project_result.final_score finalScore, project_result.placed rank "
                    + "FROM project_result, resource, resource_info, resource_submission "
                    + "WHERE project_result.user_id = resource_info.value "
                    + "AND   resource.resource_id = resource_submission.resource_id "
                    + "AND   resource.resource_id = resource_info.resource_id "
                    + "AND   resource_info.resource_info_type_id = 1 "
                    + "AND   resource.project_id = project_result.project_id "
                    + "AND   resource_submission.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's handle.
     */
    private static final String GET_SUBMISSION_HANDLE = "SELECT s.submission_id, ri.value handle from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 2" + " and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's user ID.
     */
    private static final String GET_SUBMISSION_USER_ID = "SELECT s.submission_id, ri.value handle from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 1" + " and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's final score from resource_info table.
     */
    private static final String GET_SUBMISSION_FINAL_SCORE = "SELECT ri.value finalScore from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 11 and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's placement from resource_info table.
     */
    private static final String GET_SUBMISSION_PLACEMENT = "SELECT ri.value rank from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 12 and s.submission_id = ?";

    /**
     * SQL statement for updating the final score of the submission in the resource_info table.
     */
    private static final String UPDATE_RI_FINAL_SCORE = "UPDATE resource_info SET value = ?, modify_user = 'FixerApp', modify_date = CURRENT"
                    + " WHERE resource_info_type_id = 11"
                    + " AND resource_id = (SELECT u.resource_id FROM upload u, submission s"
                    + " WHERE s.upload_id = u.upload_id AND s.submission_id = ?)";

    /**
     * SQL statement for updating the rank of the submission in the resource_info table.
     */
    private static final String UPDATE_RI_PLACEMENT = "UPDATE resource_info SET value = ?, modify_user = 'FixerApp', modify_date = CURRENT"
                    + " WHERE resource_info_type_id = 12"
                    + " AND resource_id = (SELECT u.resource_id FROM upload u, submission s"
                    + " WHERE s.upload_id = u.upload_id AND s.submission_id = ?)";

    /**
     * SQL statement for updating the final score of the submission in project_result table.
     */
    private static final String UPDATE_PR_FINAL_SCORE = "UPDATE project_result SET final_score = ?, modify_date = CURRENT" 
    	+ " where project_id = ? and user_id = ?";

    /**
     * SQL statement for updating the rank of the submission in project_result table.
     */
    private static final String UPDATE_PR_PLACEMENT = "UPDATE project_result SET placed = ?, modify_date = CURRENT"
    	+ " where project_id = ? and user_id = ?";

    /**
     * SQL statement for updating the passed_review_ind in project_result table. (1 for passing, 0
     * for non-passing).
     */
    private static final String UPDATE_PR_PASSED_REVIEW = "UPDATE project_result SET passed_review_ind = ?, modify_date = CURRENT"
    	+ " where project_id = ? and user_id = ?";

    /**
     * SQL statement for updating submission_status. (1 for winner, 3 from non-passed review, 4 for
     * passed review without win)
     */
    private static final String UPDATE_SUBMISSION_STATUS = "UPDATE submission SET submission_status_id = ?, modify_user = 'FixerApp', modify_date = CURRENT"
    	+ " WHERE submission_id = ?";

    /**
     * SQL statement for updating the project_info for the given project.
     */
    private static final String UPDATE_PROJECT_INFO = "UPDATE project_info SET value = ?, modify_user = 'FixerApp', modify_date = CURRENT"
    	+ " WHERE project_id = ? AND project_info_type_id = ?";

    /**
     * The minimum score to pass the review.
     */
    private static final double MINIMUN_PASSING_SCORE = 75;

    /**
     * Passed Review Flag.
     */
    private static final int PASSED_REVIEW = 1;

    /**
     * Non-passed review flag.
     */
    private static final int NON_PASSED_REVIEW = 0;

    /**
     * Winner submission status.
     */
    private static final int WINNER_SUBMISSION = 1;

    /**
     * Failed review status.
     */
    private static final int FAILED_REVIEW_SUBMISSION = 3;

    /**
     * Passed review without win status.
     */
    private static final int PASSED_WITHOUT_WIN_SUBMISSION = 4;

    /**
     * The set of project id for fix.
     */
    private Set<String> updateProjects = new HashSet<String>();

    /**
     * private constructor.
     */
    private OnlineReviewScoreRankFixer() {

    }

    /**
     * Builds the Projects specified by the projectResults list
     * 
     * 
     * @param projectResults
     */
    private void buildProjectResults(List projectResults) {
        // get all the component project list
        Connection connection = Utility.getConnection();

        PreparedStatement getReviewScores = null;
        PreparedStatement getSubmissionFinalResult = null;
        PreparedStatement getSubmissionHandle = null;
        PreparedStatement getSubmissionFinalScore = null;
        PreparedStatement getSubmissionRank = null;

        try {
        	connection.setAutoCommit(false);
            getReviewScores = connection.prepareStatement(GET_REVIEW_SCORES);
            getSubmissionFinalResult = connection.prepareStatement(GET_SUBMISSION_FINAL_RESULT);
            getSubmissionHandle = connection.prepareStatement(GET_SUBMISSION_HANDLE);
            getSubmissionFinalScore = connection.prepareStatement(GET_SUBMISSION_FINAL_SCORE);
            getSubmissionRank = connection.prepareStatement(GET_SUBMISSION_PLACEMENT);

            for (int i = 0, n = projectResults.size(); i < n; ++i) {

                ProjectResult projectResult = (ProjectResult) projectResults.get(i);

                String projectId = projectResult.getProjectId();

                // get all the submissions' review scores
                getReviewScores.setLong(1, Long.parseLong(projectId));
                ResultSet result = getReviewScores.executeQuery();

                SubmitterResult sResult = null;
                String submissionId;
                String lastSubmissionId = null;

                while (result.next()) {
                    double score = result.getDouble("score");
                    submissionId = result.getString("id");

                    if (lastSubmissionId == null || !lastSubmissionId.equals(submissionId)) {
                        sResult = new SubmitterResult();
                        sResult.setSubmissionId(submissionId);
                        projectResult.addSubmitterResult(sResult);
                        lastSubmissionId = submissionId;
                    }

                    sResult.addReviewScore(score);
                }

                // get all the submissions' final review score and rank, and the handles
                for (Iterator itr = projectResult.getSubmitterResults().iterator(); itr.hasNext();) {
                    sResult = (SubmitterResult) itr.next();
                    String id = sResult.getSubmissionId();

                    // get the submission's final score from resource_info table
                    getSubmissionFinalScore.setString(1, id);
                    result = getSubmissionFinalScore.executeQuery();

                    while (result.next()) {
                        sResult.setFinalScore(result.getDouble("finalScore"));
                    }

                    // get the submission's rank from resource_info table
                    getSubmissionRank.setString(1, id);
                    result = getSubmissionRank.executeQuery();

                    while (result.next()) {
                        sResult.setRank(result.getInt("rank"));
                    }

                    // retrieve the submitter's handle
                    getSubmissionHandle.setString(1, id);
                    result = getSubmissionHandle.executeQuery();

                    while (result.next()) {
                        sResult.setHandle(result.getString("handle"));
                    }
                }

                boolean success = validateProjectResult(connection, projectResult, false);

                if (!success) {
                    Utility.log(Level.INFO, "-------------------");
                }
            }
            connection.commit();
        } catch (Exception ex) {
        	if (connection != null) {
        		try {
					connection.rollback();
				} catch (SQLException e) {
					throw new OnlineReviewScoreRankFixerException("error during rollback", e);
				}
        	}
            throw new OnlineReviewScoreRankFixerException("Fail to query data from DB.", ex);
        } finally {
            Utility.releaseResource(getReviewScores, null, null);
            Utility.releaseResource(getSubmissionFinalResult, null, null);
            Utility.releaseResource(getSubmissionHandle, null, null);
        }
    }

    /**
     * Gets the all the component's projectId from the Online Review Database.
     * 
     * @return a list of projectResult.
     */
    private List getAllProjectResults() {
        // get all the component project list
        Connection connection = Utility.getConnection();

        Statement statement = null;

        List<ProjectResult> projectResults = new ArrayList<ProjectResult>();

        try {
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery(GET_ALL_COMPONENT_PROJECT_ID);

            while (result.next()) {
                ProjectResult pResult = new ProjectResult();
                pResult.setProjectId(result.getString("id"));
                pResult.setProjectName(result.getString("name"));
                pResult.setProjectType(result.getInt("type") == 1 ? "Design" : "Development");
                projectResults.add(pResult);
            }

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to query data from DB.", ex);
        } finally {
            Utility.releaseResource(statement, null, null);
        }

        return projectResults;
    }

    private boolean validateProjectResult(Connection connection, ProjectResult projectResult, boolean doUpdate) {
        List submitterResults = projectResult.getSubmitterResults();

        boolean dataCorrect = true;

        Utility.log(Level.DEBUG, projectResult);

        // validate all the submission's final score
        for (int i = 0, n = submitterResults.size(); i < n; ++i) {

            SubmitterResult sResult = (SubmitterResult) submitterResults.get(i);
            double[] scores = sResult.getReviewScores();
            double temp = 0;

            // Logs a warning message if the submission has more or less than 3 review scores
            if (sResult.getReviewScoresLength() != 3) {
                Utility.log(Level.ERROR, "WARNING:" + projectResult.getProjectInfo() + " has "
                                + sResult.getReviewScoresLength() + " review scores.");
            }

            for (int k = 0; k < sResult.getReviewScoresLength(); ++k) {
                temp += scores[k];
            }

            temp /= sResult.getReviewScoresLength();

            boolean missingData = false;

            // if there is no final score for the submission
            if (sResult.getFinalScore() == 0) {
                Utility.log(Level.ERROR, projectResult.getProjectInfo());
                Utility.log(Level.ERROR, "No final score for " + sResult.getHandle());
                missingData = true;
            }

            // if there is no rank info for the submission
            if (sResult.getRank() == 0) {
                Utility.log(Level.ERROR, projectResult.getProjectInfo());
                Utility.log(Level.ERROR, "No rank info for " + sResult.getHandle());
                missingData = true;
            }

            // ignore this project due to the missing data.
            if (missingData) {
                return false;
            }

            if (Math.abs(temp - sResult.getFinalScore()) > 0.006) {

                if (dataCorrect) {
                    dataCorrect = false;
                    Utility.log(Level.ERROR, projectResult.getProjectInfo());
                    Utility.log(Level.INFO, projectResult.getSubmissionsResult());
                    Utility.log(Level.ERROR, "ERRORS:");
                }

                Utility.log(Level.INFO, sResult.getHandle() + ": " + sResult.getFinalScore() + "--->" + temp);

                // update the final score
                if (getUpdateProjects().contains(projectResult.getProjectId())) {         
                    updateFinalScore(connection, projectResult.getProjectId(), sResult, temp);
                }

                if (sResult.getFinalScore() >= MINIMUN_PASSING_SCORE && temp < MINIMUN_PASSING_SCORE) {
                    Utility.log(Level.ERROR, sResult.getHandle() + ": passing to non-passing.");

                    if (getUpdateProjects().contains(projectResult.getProjectId())) {
                        // update the passed review flag to 0
                        updatePassedReview(connection, projectResult.getProjectId(), sResult, NON_PASSED_REVIEW);

                        // update the submission status
                        updateSubmissionStatus(connection, sResult.getSubmissionId(), FAILED_REVIEW_SUBMISSION);
                    }

                } else if (sResult.getFinalScore() < MINIMUN_PASSING_SCORE && temp >= MINIMUN_PASSING_SCORE) {
                    Utility.log(Level.ERROR, sResult.getHandle() + ": non-passing to passing.");

                    if (getUpdateProjects().contains(projectResult.getProjectId())) {
                        // update the passed review flag to 1
                        updatePassedReview(connection, projectResult.getProjectId(), sResult, PASSED_REVIEW);

                        // update the submission status
                        if (sResult.getRank() == 1) {
                            updateSubmissionStatus(connection, sResult.getSubmissionId(), WINNER_SUBMISSION);
                        } else {
                            updateSubmissionStatus(connection, sResult.getSubmissionId(), PASSED_WITHOUT_WIN_SUBMISSION);
                        }

                    }
                }

                sResult.setFixedScore(temp);

            } else {
                sResult.setFixedScore(sResult.getFinalScore());
            }
        }

        // validates all the submissions' rank
        Map<Double, Integer> ranks = new TreeMap<Double, Integer>();
        Map<Integer, SubmitterResult> handleData = new HashMap<Integer, SubmitterResult>();

        for (int i = 0, n = submitterResults.size(); i < n; ++i) {
            SubmitterResult sResult = (SubmitterResult) submitterResults.get(i);
            ranks.put(sResult.getFixedScore(), sResult.getRank());
            handleData.put(sResult.getRank(), sResult);
        }

        int newRank = submitterResults.size();

        for (Iterator itr = ranks.values().iterator(); itr.hasNext();) {
            int oldRank = ((Integer) itr.next()).intValue();

            if (oldRank != newRank) {
            	SubmitterResult submitter = handleData.get(oldRank);
                String handle = submitter.getHandle();
                String submissionId = submitter.getSubmissionId();

                if (newRank == 1 || newRank == 2) {
                    Utility.log(Level.ERROR, handle + ": rank#" + oldRank + "--> rank#" + newRank);

                } else {
                    Utility.log(Level.INFO, handle + ": rank#" + oldRank + "--> rank#" + newRank);
                }

                if (getUpdateProjects().contains(projectResult.getProjectId())) {
                    // update the placement first
                    this.updatePlacement(connection, projectResult.getProjectId(), submitter, newRank);
                    long userId = getUserId(submissionId);

                    if (newRank == 1) {
                        updateSubmissionStatus(connection, submissionId, WINNER_SUBMISSION);
                        updateProjectInfo(connection, projectResult.getProjectId(), 23, userId + "");
                    }

                    if (newRank == 2) {
                        updateProjectInfo(connection, projectResult.getProjectId(), 24, userId + "");
                    }

                    if (oldRank == 1) {
                        updateSubmissionStatus(connection, submissionId, PASSED_WITHOUT_WIN_SUBMISSION);
                    }
                }
            }

            newRank--;
        }

        return dataCorrect;
    }

    /**
     * Update the final score for specified submission. This method will update two tables,
     * resource_info and project_result.
     * 
     * @param submissionId the id of the submission.
     * @param newScore the new score of the submission.
     */
    private void updateFinalScore(Connection connection, String projectId, SubmitterResult sResult, double newScore) {
        PreparedStatement updateRI = null;
        PreparedStatement updatePR = null;

        try {
            updateRI = connection.prepareStatement(UPDATE_RI_FINAL_SCORE);
            updatePR = connection.prepareStatement(UPDATE_PR_FINAL_SCORE);

            // update resource_info
            updateRI.setDouble(1, newScore);
            updateRI.setString(2, sResult.getSubmissionId());
            Utility.log(Level.ERROR, "update Resource_Info final score submissionId: " + sResult.getSubmissionId() + ", score: " + newScore);
            updateRI.executeUpdate();

            // update project_result
            updatePR.setDouble(1, newScore);
            updatePR.setString(2, projectId);
            updatePR.setString(3, sResult.getUserId());
            Utility.log(Level.ERROR, "update Project_Result final score projectId: " + projectId + ", userId: " + sResult.getUserId() + ", score: " + newScore);
            updatePR.executeUpdate();

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("update final score failed.", ex);
        } finally {
            Utility.releaseResource(updateRI, null, null);
            Utility.releaseResource(updatePR, null, null);
        }
    }

    /**
     * Update the placement for specified submission. This method will update two tables,
     * resource_info and project_result.
     * 
     * @param submissionId the id of the submission.
     * @param newPlacement the new placement of the submission.
     */
    private void updatePlacement(Connection connection, String projectId, SubmitterResult sResult, int newPlacement) {

        PreparedStatement updateRI = null;
        PreparedStatement updatePR = null;

        try {
            updateRI = connection.prepareStatement(UPDATE_RI_PLACEMENT);
            updatePR = connection.prepareStatement(UPDATE_PR_PLACEMENT);

            // update resource_info
            updateRI.setDouble(1, newPlacement);
            updateRI.setString(2, sResult.getSubmissionId());

            updateRI.executeUpdate();

            // update project_result
            updatePR.setDouble(1, newPlacement);
            updatePR.setString(2, projectId);
            updatePR.setString(3, sResult.getUserId());

            updatePR.executeUpdate();

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("update placement failed.", ex);
        } finally {
            Utility.releaseResource(updateRI, null, null);
            Utility.releaseResource(updatePR, null, null);
        }
    }

    /**
     * Update the passed_review_ind flag in project_result table.
     * 
     * @param submissionId the id of the submission.
     * @param isPassed 1 for passed review, 0 for non-passed review.
     */
    private void updatePassedReview(Connection connection, String projectId, SubmitterResult sResult, int isPassed) {
        if (isPassed != 0 && isPassed != 1) {
            throw new IllegalArgumentException("isPassed should be either 0 or 1.");
        }

        PreparedStatement updatePassedReview = null;

        try {
            updatePassedReview = connection.prepareStatement(UPDATE_PR_PASSED_REVIEW);

            updatePassedReview.setInt(1, isPassed);
            updatePassedReview.setString(2, projectId);
            updatePassedReview.setString(3, sResult.getUserId());

            updatePassedReview.executeUpdate();

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to update passed review flag.", ex);
        } finally {
            Utility.releaseResource(updatePassedReview, null, null);
        }
    }

    /**
     * Update the submission_status of the submission.
     * 
     * @param submissionId the id of the submission.
     * @param status the status of the submission.
     */
    private void updateSubmissionStatus(Connection connection, String submissionId, int status) {
        if (status != 1 && status != 3 && status != 4) {
            throw new IllegalArgumentException("status should be 1 or 3 or 4");
        }

        PreparedStatement updateSubmissionStatus = null;

        try {
            updateSubmissionStatus = connection.prepareStatement(UPDATE_SUBMISSION_STATUS);

            updateSubmissionStatus.setInt(1, status);
            updateSubmissionStatus.setString(2, submissionId);

            updateSubmissionStatus.executeUpdate();

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to update submission status.", ex);
        } finally {
            Utility.releaseResource(updateSubmissionStatus, null, null);
        }
    }

    /**
     * Update the projectInfo of the specified project.
     * 
     * @param projectId the id of the project.
     * @param projectInfoTypeId the project info type id.
     * @param value the value of project info.
     */
    private void updateProjectInfo(Connection connection, String projectId, int projectInfoTypeId, String value) {

        PreparedStatement updateProjectInfo = null;

        try {
            updateProjectInfo = connection.prepareStatement(UPDATE_PROJECT_INFO);

            updateProjectInfo.setString(1, value);
            updateProjectInfo.setString(2, projectId);
            updateProjectInfo.setInt(3, projectInfoTypeId);

            updateProjectInfo.executeUpdate();

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to update passed review flag.", ex);
        } finally {
            Utility.releaseResource(updateProjectInfo, null, null);
        }
    }

    /**
     * Gets the user id of the given submitter.
     * 
     * @param submitterId the id of the submitter.
     * @return the user id.
     */
    private long getUserId(String submitterId) {
        Connection connection = Utility.getConnection();

        PreparedStatement getUserId = null;

        try {
            getUserId = connection.prepareStatement(GET_SUBMISSION_USER_ID);

            getUserId.setString(1, submitterId);

            ResultSet result = getUserId.executeQuery();

            while (result.next()) {
                return result.getLong(1);
            }

            return -1;

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to get user id.", ex);
        } finally {
            Utility.releaseResource(getUserId, null, null);
        }
    }

    /**
     * Entry point of the online review score and rank fixer app.
     * 
     * @param args the arguments passed in.
     */
    public static void main(String[] args) {
        OnlineReviewScoreRankFixer fixer = new OnlineReviewScoreRankFixer();

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String stringId = args[i];
                try {
                    Long projectId = Long.parseLong(stringId);
                    fixer.addProjectIdForUpdate(stringId);
                } catch (NumberFormatException e) {
                    printHelp();
                    return;
                }
            }
        }

        List projectResults = fixer.getAllProjectResults();
        fixer.buildProjectResults(projectResults);
    }

    private void addProjectIdForUpdate(String projectId) {
        getUpdateProjects().add(projectId);
    }

    private Set<String> getUpdateProjects() {
        return updateProjects;
    }

    /**
     * Print the command help message.
     */
    private static void printHelp() {
        System.err.println("====Usage notes====");
        System.err.println("- no parameters for statistics");
        System.err.println("- List of projects ids for fixing its data.");
    }
}
