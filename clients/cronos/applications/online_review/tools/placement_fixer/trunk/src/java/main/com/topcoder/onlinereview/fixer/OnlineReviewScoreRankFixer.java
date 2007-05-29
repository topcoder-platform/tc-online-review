package com.topcoder.onlinereview.fixer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
    		+ " and ri.resource_info_type_id = 2"
    		+ " and s.submission_id = ?";	
    /**
     * SQL statement for retrieving a submission's final score from resource_info table.
     */
    private static final String GET_SUBMISSION_FINAL_SCORE = "SELECT ri.value finalScore from submission s, upload u, resource_info ri"
		+ " where s.upload_id = u.upload_id"
		+ " and u.resource_id = ri.resource_id"
		+ " and ri.resource_info_type_id = 11"
		+ " and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's placement from resource_info table.
     */
    private static final String GET_SUBMISSION_PLACEMENT = "SELECT ri.value rank from submission s, upload u, resource_info ri"
		+ " where s.upload_id = u.upload_id"
		+ " and u.resource_id = ri.resource_id"
		+ " and ri.resource_info_type_id = 12"
		+ " and s.submission_id = ?";

	private static final double MINIMUN_PASSING_SCORE = 75;
    
    /**
     * SQL statement for updating the final score of the submission in project_result table.
     */
    
    /**
     * SQL statement for updating the final score of the submission in the resource_info table.
     */
    
    /**
     * SQL statement for updating the rank of the submission in project_result table.
     */
    
    /**
     * SQL statement for updating the rank of the submission in the resource_info table.
     */
    
    /**
     * SQL statement for updating the passed_review_ind in project_result table.
     */
    
    /**
     * SQL statement for updating submission_status.
     */
    
    /**
     * SQL statement for updating the project_info for certain project.
     */

    /**
     * private constructor.
     */
    private OnlineReviewScoreRankFixer() {

    }

    private void buildProjectResults(List projectResults) {
        // get all the component project list
        Connection connection = Utility.getConnection();

        PreparedStatement getReviewScores = null;
        PreparedStatement getSubmissionFinalResult = null;
        PreparedStatement getSubmissionHandle = null;
        PreparedStatement getSubmissionFinalScore = null;
        PreparedStatement getSubmissionRank = null;

        try {
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

                boolean success = validateProjectResult(projectResult, false);
                
                if (!success) {
                    Utility.log(Level.INFO, "-------------------");
                }
            }
        } catch (Exception ex) {
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

        List projectResults = new ArrayList();

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

    private boolean validateProjectResult(ProjectResult projectResult, boolean doUpdate) {
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

                if (sResult.getFinalScore() >= MINIMUN_PASSING_SCORE && temp < MINIMUN_PASSING_SCORE) {
                    Utility.log(Level.ERROR, sResult.getHandle() + ": passing to non-passing.");
                } else if (sResult.getFinalScore() < MINIMUN_PASSING_SCORE && temp >= MINIMUN_PASSING_SCORE) {
                    Utility.log(Level.ERROR, sResult.getHandle() + ": non-passing to passing.");
                }

                sResult.setFixedScore(temp);

            } else {
                sResult.setFixedScore(sResult.getFinalScore());
            }
        }
        
        
        // validates all the submissions' rank
        Map ranks = new TreeMap();
        Map handleData = new HashMap();

        for (int i = 0, n = submitterResults.size(); i < n; ++i) {
            SubmitterResult sResult = (SubmitterResult) submitterResults.get(i);
            ranks.put(new Double(sResult.getFixedScore()), new Integer(sResult.getRank()));
            handleData.put(new Integer(sResult.getRank()), sResult.getHandle());
        }

        int newRank = submitterResults.size();

        for (Iterator itr = ranks.values().iterator(); itr.hasNext();) {
            int oldRank = ((Integer) itr.next()).intValue();

            if (oldRank != newRank) {
                String handle = (String) handleData.get(new Integer(oldRank));
                
                if (newRank == 1 || newRank == 2) {
                    Utility.log(Level.ERROR, handle + ": rank#" + oldRank + "--> rank#" + newRank);
                } else {
                    Utility.log(Level.INFO,handle + ": rank#" + oldRank + "--> rank#" + newRank);
                }
            }

            newRank--;
        }

        return dataCorrect;
    }

    /**
     * Entry point of the online review score and rank fixer app.
     * 
     * @param args the arguments passed in.
     */
    public static void main(String[] args) {
        OnlineReviewScoreRankFixer fixer = new OnlineReviewScoreRankFixer();
        List projectResults = fixer.getAllProjectResults();
        fixer.buildProjectResults(projectResults);
    }
}
