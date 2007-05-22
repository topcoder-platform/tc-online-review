package com.topcoder.onlinereview.fixer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Online Review Placement and score fixer main class.
 * 
 * @author hohosky
 */
public class OnlineReviewScoreRankFixer {
    /**
     * SQL statement for getting all the completed component (Design or
     * Development)'s project id.
     */
    private static final String GET_ALL_COMPONENT_PROJECT_ID = "SELECT project.project_id id, project.project_category_id type"
                    + ", project_info.value name FROM project, project_info "
                    + "WHERE project_status_id = 7 AND project_category_id in (1,2) "
                    + " AND project_info.project_id = project.project_id "
                    + "AND project_info.project_info_type_id = 6";

    /**
     * SQL statement for retrieving all review scores for a project's
     * submissions. project.
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
     * SQL statement for retrieving a submission's final result (final score and
     * rank).
     */
    private static final String GET_SUBMISSION_FINAL_RESULT = "SELECT project_result.final_score score, project_result.placed rank "
                    + "FROM project_result, resource, resource_info, resource_submission "
                    + "WHERE project_result.user_id = resource_info.value "
                    + "AND   resource.resource_id = resource_submission.resource_id "
                    + "AND   resource.resource_id = resource_info.resource_id "
                    + "AND   resource_info.resource_info_type_id = 1 "
                    + "AND   resource.project_id = project_result.project_id "
                    + "AND   resource_submission.submission_id = ?";

    /**
     * SQL statement for query a submission's handle.
     */
    private static final String GET_SUBMISSION_HANDLE = "SELECT resource_info.value handle "
                    + "FROM  resource, resource_info, resource_submission "
                    + "WHERE resource.resource_id = resource_submission.resource_id "
                    + "AND   resource.resource_id = resource_info.resource_id "
                    + "AND   resource_info.resource_info_type_id = 2 " + " AND   resource_submission.submission_id = ?";

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

        for (int i = 0, n = projectResults.size(); i < n; ++i) {

            ProjectResult projectResult = (ProjectResult) projectResults.get(i);

            String projectId = projectResult.getProjectId();

            try {
                // get the submission's scores
                getReviewScores = connection.prepareStatement(GET_REVIEW_SCORES);

                getReviewScores.setLong(1, Long.parseLong(projectId));

                ResultSet result = getReviewScores.executeQuery();

                int count = 0;
                SubmitterResult sResult = null;
                String submissionId;

                while (result.next()) {

                    double score = normalizeScore(result.getDouble("score"));
                    submissionId = result.getString("id");

                    if (count == 0) {
                        sResult = new SubmitterResult();
                        sResult.setSubmissionId(submissionId);
                        projectResult.addSubmitterResult(sResult);
                    }

                    sResult.addReviewScore(score);

                    count = (count + 1) % 3;
                }

                for (Iterator itr = projectResult.getSubmitterResults().iterator(); itr.hasNext();) {
                    sResult = (SubmitterResult) itr.next();
                    
                    // get the submission's final score and rank
                    getSubmissionFinalResult = connection.prepareStatement(GET_SUBMISSION_FINAL_RESULT);

                    getSubmissionFinalResult.setString(1, sResult.getSubmissionId());

                    result = getSubmissionFinalResult.executeQuery();

                    while (result.next()) {
                        sResult.setFinalScore(result.getDouble("score"));
                        sResult.setRank(result.getInt("rank"));
                    }
                    
                    getSubmissionHandle = connection.prepareStatement(GET_SUBMISSION_HANDLE);
                    
                    getSubmissionHandle.setString(1, sResult.getSubmissionId());
                    
                    result = getSubmissionHandle.executeQuery();
                    
                    while (result.next()) {
                        sResult.setHandle(result.getString("handle"));
                    }
                }

            } catch (Exception ex) {
                throw new OnlineReviewScoreRankFixerException("Fail to query data from DB.", ex);
            } finally {
                Utility.releaseResource(getReviewScores, null, null);
                Utility.releaseResource(getSubmissionFinalResult, null, null);
                Utility.releaseResource(getSubmissionHandle, null, null);
            }
            
            validateProjectResult(projectResult, false);
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

        for (int i = 0, n = submitterResults.size(); i < n; ++i) {
            SubmitterResult sResult = (SubmitterResult) submitterResults.get(i);
            double[] scores = sResult.getReviewScores();
            double temp = 0;
            
            for (int k = 0; k < scores.length; ++k) {
                temp += scores[k];
            }
            
            temp = normalizeScore(temp / scores.length);

            if (temp != sResult.getFinalScore()) {
                
                if (dataCorrect) {
                    dataCorrect = false;
                    System.out.println(projectResult);
                    System.out.println("ERRORS:");
                }
                
                System.out.println(sResult.getHandle() + ": " + sResult.getFinalScore() + "--->" + temp);
                
                if (sResult.getFinalScore() >= 80 && temp < 80) {
                    System.out.println("change from pass to non-passing.");
                } else if (sResult.getFinalScore() < 80 && temp >= 80) {
                    System.out.println("change from non-passing to passing.");
                }
                
                sResult.setFixedScore(temp);
            } else {
                sResult.setFixedScore(sResult.getFinalScore());
            }
        }
        
        Map ranks = new TreeMap();
        
        for (int i = 0, n = submitterResults.size(); i < n; ++i) {
            SubmitterResult sResult = (SubmitterResult) submitterResults.get(i);
            
            ranks.put(new Double(sResult.getFixedScore()), new Integer(sResult.getRank()));
        }
        
        int rankCount = submitterResults.size();
        
        for (Iterator itr = ranks.values().iterator(); itr.hasNext();) {
            int actualRank = ((Integer) itr.next()).intValue();
            
            if (actualRank != rankCount) {
                System.out.println("rank#" + rankCount + " should change to rank#" + actualRank);
            }
            
            rankCount--;
        }
        
        if (!dataCorrect) {
            System.out.println("\n");
        }

        return dataCorrect;
    }

    /**
     * Round the double value to the 2 decimal double.
     * 
     * @param originalScore
     * @return
     */
    private double normalizeScore(double originalScore) {
        return ((int) ((originalScore + 0.005000000001) * 100)) / (double) 100.0;
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
