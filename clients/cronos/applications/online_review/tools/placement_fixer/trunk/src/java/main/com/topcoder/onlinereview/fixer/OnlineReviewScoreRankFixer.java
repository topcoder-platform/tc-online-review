package com.topcoder.onlinereview.fixer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.topcoder.util.log.Level;

/**
 * Online Review Placement and score fixer main class.
 * 
 * @author hohosky
 */
public class OnlineReviewScoreRankFixer {
    
	/** SQL to insert a new resource_info */
	private static final String INSERT_RESOURCE_INFO = "INSERT INTO resource_info" +
			" (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date)" +
			" values (?, ?, ?, 'FixerApp', CURRENT,  'FixerApp', CURRENT)";

	/**
     * SQL statement for getting all the completed component (Design or Development)'s project id.
     */
    private static final String GET_ALL_COMPONENT_PROJECT_ID = "SELECT p.project_id id, p.project_category_id type, piName.value name, piPrice.value price"
    	+ " FROM project p, outer project_info piName, outer project_info piPrice"
    	+ " WHERE p.project_category_id in (1,2)"
    	+ " AND piName.project_id = p.project_id"
    	+ " AND piPrice.project_id = p.project_id"
    	+ " AND piName.project_info_type_id = 6"
    	+ " AND piPrice.project_info_type_id = 16"
    	+ " AND p.project_status_id = 7";

    /**
     * SQL statement for retrieving all review scores for a project's submissions. project.
     */
    private static final String GET_REVIEW_SCORES = "SELECT review.score score, submission.submission_id id "
                    + "FROM review, resource, upload, submission, scorecard "
                    + "WHERE review.resource_id = resource.resource_id "
                    + "AND   review.submission_id = submission.submission_id "
                    + "AND   upload.upload_id = submission.upload_id "
                    + "AND   resource.resource_role_id NOT IN (8,9) "
                    + "AND   review.scorecard_id = scorecard.scorecard_id " 
                    + "AND scorecard.scorecard_type_id = 2 "
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
    private static final String GET_SUBMISSION_DATA = "SELECT s.submission_id, ri.value handle, ri.resource_id," 
    				+ " s.final_score finalScore, s.placement rank, s.create_date createDate" 
    				+ " from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 2" + " and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's user ID.
     */
    private static final String GET_SUBMISSION_USER_ID = "SELECT s.submission_id, ri.value user_id from submission s, upload u, resource_info ri"
                    + " where s.upload_id = u.upload_id"
                    + " and u.resource_id = ri.resource_id"
                    + " and ri.resource_info_type_id = 1 and s.submission_id = ?";

    /**
     * SQL statement for retrieving a submission's final score from submission table.
     *
    private static final String GET_SUBMISSION_FINAL_SCORE = "SELECT final_score finalScore from submission where submission_id = ?";
   	*/
    
    /**
     * SQL statement for retrieving a submission's placement from submission table.
     *
    private static final String GET_SUBMISSION_PLACEMENT = "SELECT placement rank from submission where submission_id = ?";
	*/
    
    /**
     * SQL statement for updating the final score of the submission in the submission table.
     */
    private static final String UPDATE_SUBMISSION_FINAL_SCORE = "UPDATE submission SET final_score = ?, modify_user = 'FixerApp', modify_date = CURRENT"
                    + " WHERE submission_id = ?";

    /**
     * SQL statement for updating the rank of the submission in the submission table.
     */
    private static final String UPDATE_SUBMISSION_PLACEMENT = "UPDATE submission SET placement = ?, modify_user = 'FixerApp', modify_date = CURRENT"
    	            + " WHERE submission_id = ?";
    
    /**
     * SQL statement for updating the payment of the submission in the resource_info table.
     */
    private static final String UPDATE_RI_PAYMENT = "UPDATE resource_info SET value = ?, modify_user = 'FixerApp', modify_date = CURRENT"
                    + " WHERE resource_info_type_id = 7"
                    + " AND resource_id = (SELECT u.resource_id FROM upload u, submission s"
                    + " WHERE s.upload_id = u.upload_id AND s.submission_id = ?)";

    /**
     * SQL statement for updating the final score of the submission in project_result table.
     */
    private static final String UPDATE_PR_FINAL_SCORE = "UPDATE project_result SET final_score = ?" 
    	+ " where project_id = ? and user_id = ?";

    /**
     * SQL statement for updating the rank of the submission in project_result table.
     */
    private static final String UPDATE_PR_PLACEMENT = "UPDATE project_result SET placed = ?, payment = ?"
    	+ " where project_id = ? and user_id = ?";

    /**
     * SQL statement for updating the passed_review_ind in project_result table. (1 for passing, 0
     * for non-passing).
     */
    private static final String UPDATE_PR_PASSED_REVIEW = "UPDATE project_result SET passed_review_ind = ?"
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
    private void buildProjectResults(List<ProjectResult> projectResults) {
        // get all the component project list
        Connection connection = Utility.getConnection();

        PreparedStatement psReviewScores = null;
        PreparedStatement psSubmissionFinalResult = null;
        PreparedStatement psSubmissionData = null;

        try {
        	if (!getUpdateProjects().isEmpty()) {
        		connection.setAutoCommit(false);
        	}
        	
            psReviewScores = connection.prepareStatement(GET_REVIEW_SCORES);
            psSubmissionFinalResult = connection.prepareStatement(GET_SUBMISSION_FINAL_RESULT);
            psSubmissionData = connection.prepareStatement(GET_SUBMISSION_DATA);

            for (ProjectResult projectResult: projectResults) {
                String projectId = projectResult.getProjectId();

                // get all the submissions' review scores
                psReviewScores.setLong(1, Long.parseLong(projectId));
                ResultSet result = psReviewScores.executeQuery();

                SubmitterResult sResult = null;
                String submissionId;
                String lastSubmissionId = null;

                while (result.next()) {
                    double score = result.getDouble("score");
                    submissionId = result.getString("id");

                    if (lastSubmissionId == null || !lastSubmissionId.equals(submissionId)) {
                        sResult = new SubmitterResult(submissionId);
                        projectResult.addSubmitterResult(sResult);
                        lastSubmissionId = submissionId;
                        
                        // get all the submissions' final review score and rank, and the handles
                        // retrieve the submitter's handle
                        psSubmissionData.setString(1, submissionId);
                        result = psSubmissionData.executeQuery();

                        while (result.next()) {
                            sResult.setHandle(result.getString("handle"));
                            sResult.setResourceId(result.getLong("resource_id"));
                            sResult.setFinalScore(result.getDouble("finalScore"));
                            sResult.setRank(result.getInt("rank"));
                            sResult.setCreationDate(result.getDate("createDate"));
                        }
                        sResult.setUserId(getUserId(connection, sResult.getSubmissionId()));
                    }

                    sResult.addReviewScore(score);
                }

                boolean success = validateProjectResult(connection, projectResult);

                if (!success) {
                    Utility.log(Level.INFO, "-------------------");
                }
            }
            if (!getUpdateProjects().isEmpty()) {
            	connection.commit();
            }
        } catch (Exception ex) {
        	if (connection != null && !getUpdateProjects().isEmpty()) {
        		try {
					connection.rollback();
				} catch (SQLException e) {
					throw new OnlineReviewScoreRankFixerException("error during rollback", e);
				}
        	}
            throw new OnlineReviewScoreRankFixerException("Fail to query data from DB.", ex);
        } finally {
            Utility.releaseResource(psReviewScores, null, null);
            Utility.releaseResource(psSubmissionFinalResult, null, null);
            Utility.releaseResource(psSubmissionData, null, connection);
        }
    }

    /**
     * Gets the all the component's projectId from the Online Review Database.
     * 
     * @return a list of projectResult.
     */
    private List<ProjectResult> getAllProjectResults() {
        // get all the component project list
        Connection connection = Utility.getConnection();
        Statement statement = null;

        List<ProjectResult> projectResults = new ArrayList<ProjectResult>();
        String projectId = null;
        try {
            statement = connection.createStatement();

            ResultSet result = statement.executeQuery(GET_ALL_COMPONENT_PROJECT_ID);

            while (result.next()) {
            	projectId = result.getString("id");
            	if (getUpdateProjects().isEmpty() || getUpdateProjects().contains(projectId)) {
            		String name = result.getString("name");
            		if (name == null) {
            			Utility.log(Level.WARN, "ignoring projectId: " + projectId + " has a null name");
            		} else {
            			ProjectResult pResult = new ProjectResult();

            			pResult.setProjectId(projectId);
            			pResult.setProjectName(name);
            			pResult.setProjectType(result.getInt("type") == 1 ? "Design" : "Development");
            			pResult.setPayment(result.getString("price"));
            			projectResults.add(pResult);
            		}
            	}
            }

        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("Fail to query data from DB. projectId: " + projectId, ex);
        } finally {
            Utility.releaseResource(statement, null, connection);
        }

        return projectResults;
    }

    private boolean validateProjectResult(Connection connection, ProjectResult projectResult) {
        boolean dataCorrect = true;
        Utility.log(Level.DEBUG, projectResult);

        // validate all the submission's final score
        for (SubmitterResult sResult: projectResult.getSubmitterResults()) {
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
        Set<SubmitterResult> results = new TreeSet<SubmitterResult>(projectResult.getSubmitterResults());
        int newRank = 1;
        for (Iterator<SubmitterResult> i = results.iterator(); i.hasNext(); newRank++) {
			SubmitterResult submitter = i.next();
            int oldRank = submitter.getRank();
            if (oldRank != newRank) {
                String handle = submitter.getHandle();
                String submissionId = submitter.getSubmissionId();
                
                if (dataCorrect) {
                    dataCorrect = false;
                    Utility.log(Level.ERROR, projectResult.getProjectInfo());
                    Utility.log(Level.INFO, projectResult.getSubmissionsResult());
                    Utility.log(Level.ERROR, "ERRORS:");
                }
                
                if (newRank == 1 || newRank == 2) {
                    Utility.log(Level.ERROR, handle + ": rank#" + oldRank + "--> rank#" + newRank);
                } else {
                    Utility.log(Level.INFO, handle + ": rank#" + oldRank + "--> rank#" + newRank);
                }

                if (getUpdateProjects().contains(projectResult.getProjectId())) {
                    // update the placement first
                    this.updatePlacement(connection, projectResult, submitter, newRank);
                    String userId = submitter.getUserId();

                    if (newRank == 1) {
                    	setAllActiveSubmissionToPassingWithoutWinning(connection, projectResult.getProjectId());
                        updateSubmissionStatus(connection, submissionId, WINNER_SUBMISSION);
                        updateProjectInfo(connection, projectResult.getProjectId(), 23, userId);
                    } else if (newRank == 2) {
                        updateProjectInfo(connection, projectResult.getProjectId(), 24, userId);
                    }

                    if (oldRank == 1) {
                        updateSubmissionStatus(connection, submissionId, PASSED_WITHOUT_WIN_SUBMISSION);
                    }
                }
            }
        }

        return dataCorrect;
    }

    private void setAllActiveSubmissionToPassingWithoutWinning(Connection connection, String projectId) {
		try {
			connection.createStatement().executeUpdate("UPDATE submission SET submission_status_id = 4 WHERE submission_status_id = 1 " + 
					"AND upload_id IN (SELECT u.upload_id FROM upload u where u.project_id = " + projectId);
		} catch (SQLException e) {
			throw new OnlineReviewScoreRankFixerException("Fail to change submissions status", e);
		}
	}

	private static DecimalFormat SCORE_FORMATTER = null;
    
    private DecimalFormat getScoreFormatter() {
    	if (SCORE_FORMATTER == null) {
    		SCORE_FORMATTER = new DecimalFormat("#.##");
    		SCORE_FORMATTER.setDecimalSeparatorAlwaysShown(false);
        	DecimalFormatSymbols formatSymbols = SCORE_FORMATTER.getDecimalFormatSymbols();
        	formatSymbols.setDecimalSeparator('.');
        	SCORE_FORMATTER.setDecimalFormatSymbols(formatSymbols);	
    	}
    	
    	return SCORE_FORMATTER;
    }
    
    /**
     * Update the final score for specified submission. This method will update two tables,
     * submission and project_result.
     * 
     * @param submissionId the id of the submission.
     * @param newScore the new score of the submission.
     */
    private void updateFinalScore(Connection connection, String projectId, SubmitterResult sResult, double newScore) {
        PreparedStatement updateRI = null;
        PreparedStatement updatePR = null;

        try {
            updateRI = connection.prepareStatement(UPDATE_SUBMISSION_FINAL_SCORE);
            updatePR = connection.prepareStatement(UPDATE_PR_FINAL_SCORE);

            // update resource_info
            updateRI.setString(1, getScoreFormatter().format(newScore));
            updateRI.setString(2, sResult.getSubmissionId());
            Utility.log(Level.ERROR, "update submission final_score submissionId: " + sResult.getSubmissionId() + ", score: " + newScore);
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
     * submission, resource_info and project_result.
     * 
     * @param submissionId the id of the submission.
     * @param newPlacement the new placement of the submission.
     */
    private void updatePlacement(Connection connection, ProjectResult projectResult, SubmitterResult sResult, int newPlacement) {

        PreparedStatement updateRIplacement = null;
        PreparedStatement updateRIpayment = null;
        PreparedStatement updatePR = null;

        try {
            updateRIplacement = connection.prepareStatement(UPDATE_SUBMISSION_PLACEMENT);
            updateRIpayment = connection.prepareStatement(UPDATE_RI_PAYMENT);
            updatePR = connection.prepareStatement(UPDATE_PR_PLACEMENT);

            // update submission placement
            updateRIplacement.setInt(1, newPlacement);
            updateRIplacement.setString(2, sResult.getSubmissionId());
            Utility.log(Level.ERROR, "update placement in submission for submission: " + sResult.getSubmissionId() + ", new place: " + newPlacement);
            updateRIplacement.executeUpdate();
            //update resource_info payment
            double payment = 0; 
            if (newPlacement <= 2) {
            	payment = projectResult.getPaymentForPlace(newPlacement);
            	updatePR.setDouble(2, projectResult.getPaymentForPlace(newPlacement));
            } else {
            	updatePR.setNull(2, Types.DOUBLE);
            }
            updateRIpayment.setDouble(1, payment);
            updateRIpayment.setString(2, sResult.getSubmissionId());
            Utility.log(Level.ERROR, "update payment in resource_info for submission: " + sResult.getSubmissionId() + ", new payment: " + projectResult.getPaymentForPlace(newPlacement));
            if (updateRIpayment.executeUpdate() == 0 && payment > 0) {
            	DecimalFormat fmt = new DecimalFormat("0.##");
            	DecimalFormatSymbols dfs = fmt.getDecimalFormatSymbols();
            	dfs.setDecimalSeparator('.');
            	fmt.setDecimalFormatSymbols(dfs);
            	insertResourceInfo(connection, sResult.getResourceId(), 7, fmt.format(payment));
            }

            // update project_result
            updatePR.setInt(1, newPlacement);
            updatePR.setString(3, projectResult.getProjectId());
            updatePR.setString(4, sResult.getUserId());
            Utility.log(Level.ERROR, "update placement & payment in project_result for projectId: " + projectResult.getProjectId() 
            		+ ", userId: " + sResult.getUserId() 
            		+ ", new place: " + newPlacement
            		+ ", new payment: " + projectResult.getPaymentForPlace(newPlacement));
            updatePR.executeUpdate();
        } catch (Exception ex) {
            throw new OnlineReviewScoreRankFixerException("update placement failed.", ex);
        } finally {
        	Utility.releaseResource(updateRIplacement, null, null);
            Utility.releaseResource(updateRIpayment, null, null);
            Utility.releaseResource(updatePR, null, null);
        }
    }

    private void insertResourceInfo(Connection connection, long resourceId, int resourceInfoTypeId, String value) {
    	PreparedStatement pstm = null;
    	try {
			pstm = connection.prepareStatement(INSERT_RESOURCE_INFO);
			int i = 1;
			pstm.setLong(i++, resourceId);
			pstm.setInt(i++, resourceInfoTypeId);
			pstm.setString(i++, value);
			pstm.executeUpdate();
		} catch (SQLException e) {
			throw new OnlineReviewScoreRankFixerException("cannot insert into resource_info: resourceId: " + resourceId + 
					", type: " + resourceInfoTypeId + ", value:" + value, e);
		} finally {
			Utility.releaseResource(pstm, null, null);
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
            Utility.log(Level.ERROR, "update passed_review_id in project_result for projectId: " + projectId + ", userId: " + sResult.getUserId() + ", value: " + isPassed);
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
            Utility.log(Level.ERROR, "update submission status for submissionId: " + submissionId + ", status: " + status);
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
            Utility.log(Level.ERROR, "update placement in project_result for projectId: " + projectId 
            		+ ", project_info_type_id: " + projectInfoTypeId 
            		+ ", value: " + value);
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
     * @param submissionId the id of the submitter.
     * @return the user id.
     */
    private String getUserId(Connection connection, String submissionId) {
        PreparedStatement getUserId = null;

        try {
            getUserId = connection.prepareStatement(GET_SUBMISSION_USER_ID);
            getUserId.setString(1, submissionId);
            ResultSet result = getUserId.executeQuery();

            while (result.next()) {
                return result.getString("user_id");
            }

            return null;
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
    	Utility.log(Level.ALL, "Start : " + new Date());
		OnlineReviewScoreRankFixer fixer = new OnlineReviewScoreRankFixer();

        if (args.length > 0) {
            for (int i = 0; i < args.length; i++) {
                String stringId = args[i];
                try {
                    Long.parseLong(stringId);
                    fixer.addProjectIdForUpdate(stringId);
                } catch (NumberFormatException e) {
                    printHelp();
                    return;
                }
            }
        }
        if (!fixer.getUpdateProjects().isEmpty()) {
        	Utility.log(Level.INFO, "Projects for fixing: " + fixer.getUpdateProjects());
        }
        List<ProjectResult> projectResults = fixer.getAllProjectResults();
        fixer.buildProjectResults(projectResults);
        Utility.log(Level.ALL, "End : " + new Date());
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
