/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorefixer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.review.ConfigurationException;
import com.topcoder.management.review.DefaultReviewManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Comment;
import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorefixer.logging.LogMessage;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.ScorecardManagerImpl;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Represents a Review Initial Score Fixer tool that will take each committed review in the database and calculate an Initial Score for it
 * if such score hasn't been calculated for that review yet.
 * </p>
 * @author George1
 * @version 1.0
 */
public class InitialScoreFixer {
	
	static {
		try {
			ConfigManager cfg = ConfigManager.getInstance();
			cfg.add(InitialScoreFixer.class.getResource("/ScoreFixer.xml"));
		} catch (ConfigManagerException e) {
			throw new IllegalStateException("fail to load configuration", e);
		}
	}

    /**
     * Represents an SQL statement to query for IDs of all reviews whose Initial Score needs to be
     * computed.
     */
    private static String QUERY_REVIEW_IDS_SQL =
    	" SELECT r.review_id, riRev.value reviewer, riSub.value submitter, u.project_id " +
    	"  FROM review r, scorecard s, submission sub, upload u, resource rsSub, resource_info riSub, resource rsRev, resource_info riRev " +
    	" WHERE r.scorecard_id = s.scorecard_id AND r.submission_id = sub.submission_id AND sub.upload_id = u.upload_id " +
    	"   AND s.scorecard_type_id = 2 " +
    	"   AND r.committed = 1 " +
    	"   AND r.score IS NOT NULL " +
    	"   AND r.initial_score IS NULL " +
    	"   AND r.resource_id = riRev.resource_id and riRev.resource_info_type_id = 1 and rsRev.resource_id = riRev.resource_id and rsRev.resource_role_id in (4,5,6,7) " +
    	"   AND u.resource_id = riSub.resource_id and riSub.resource_info_type_id = 1 and rsSub.resource_id = riSub.resource_id and rsSub.resource_role_id = 1 " +
    	"   AND u.project_id = 9990033 " +
    	" ORDER BY r.create_date desc ";

    /** Logger instance using the class name as category */
    private static final Log logger = LogFactory.getLog(InitialScoreFixer.class.getName());

    /**
     * Constructs a new instance of {@link InitialScoreFixer} class.
     */
    private InitialScoreFixer() {
        // do nothing
    }

    /**
     * A main static method to provide command line functionality to the application. The command
     * line provides run once functionality &ndash; a utility retrieves all reviews and recomputes
     * their initial scores.
     *
     * @param args
     *            the command line arguments.
     * @throws ConfigurationException
     * @throws ScoreFixerException
     * @throws RuntimeException
     *             if runtime exceptions occurs while executing the command line.
     */
    public static void main(String[] args) throws ScoreFixerException {
        if (null == args) {
            throw new IllegalArgumentException("args cannot be null");
        }

        try {
            computeScores();
        } catch (ReviewManagementException rme) {
            logger.log(Level.ERROR, "fail to load one of reviews:\n" + LogMessage.getExceptionStackTrace(rme));
            throw new ScoreFixerException("fail to load one of reviews", rme);
        } catch (com.topcoder.management.scorecard.ConfigurationException ce) {
            logger.log(Level.ERROR, "fail to create scorecard manager instance:\n" +
                    LogMessage.getExceptionStackTrace(ce));
            throw new com.topcoder.management.review.scorefixer.ConfigurationException(
                    "fail to create scorecard manager instance", ce);
        } catch (RuntimeException t) {
            logger.log(Level.ERROR, "fail to build command line cause of illegal switch:\n" + LogMessage.getExceptionStackTrace(t));
            throw t;
        }
    }

    /**
     * Recomputes initial scorecards' scores based on the values stored in the Appeal's Response
     * extra info.
     *
     * @throws com.topcoder.management.scorecard.ConfigurationException
     *             if an error occurred creating a Scorecard Manager instance.
     * @throws ConfigurationException
     *             if an error occurred creating a Review Manager instance.
     * @throws ScoreFixerException
     *             if any other unexpected error occurred.
     */
    private static void computeScores() throws ScoreFixerException,
            com.topcoder.management.scorecard.ConfigurationException, ConfigurationException {
        List<ReviewData> reviewIds = getAllReviewsIds();

        logger.log(Level.INFO, "There are " + reviewIds.size() + " Review(s) to compute Initial Scores for.");

        ScorecardManager scrMgr = createScorecardManager();
        ReviewManager reviewMgr = createReviewManager();
        Connection conOldDB = null;
        try {
        	conOldDB = createConnection("oldDB");
	        for (ReviewData rData: reviewIds) {
	        	Review review = null;
	            try {
	            	review = reviewMgr.getReview(rData.getReviewId());
	            	logger.log(Level.INFO, "start review: " + review.getId());
	            	if (checkIsMigratedFromOldOR(conOldDB, rData.getProjectId())) {
	            		computeScoreForReviewUsingOldDB(conOldDB, rData, review);
	            	} else {
	            		computeScoreForReview(scrMgr, reviewMgr, review);
	            	}
	            	reviewMgr.updateReview(review, "InitialScoreFixer");
	                logger.log(Level.INFO, "====== Updated!");
	            } catch (ReviewManagementException rme) {
					logger.log(Level.ERROR, "An error occurred while " + ((review != null) ? "updating" : "retrieving")
							+ " the review with ID " + rData.getReviewId() + "\n" + LogMessage.getExceptionStackTrace(rme));
				} catch (PersistenceException pe) {
					logger.log(Level.ERROR, "An error occurred while retrieving scorecard tempalte for review with ID "
							+ rData.getReviewId() + ". Scorecard's template ID: "
							+ (review == null ? "n/a" : review.getScorecard()) + "\n"
							+ LogMessage.getExceptionStackTrace(pe));
				} catch (CalculationException ce) {
					logger.log(Level.ERROR, "An error occurred while calculating Initial Score for review with ID "
							+ rData.getReviewId() + "\n" + LogMessage.getExceptionStackTrace(ce));
				} catch (SQLException e) {
					logger.log(Level.ERROR, "review id: " + rData.getReviewId() + "\n" + LogMessage.getExceptionStackTrace(e));
				}
	            logger.log(Level.INFO, "finish review: " + (review == null? "n/a": review.getId()));
	        }
        } finally {
        	closeResources(conOldDB, null, null);
        }
    }

    private static void computeScoreForReviewUsingOldDB(Connection conOldDB, ReviewData data, Review review) throws SQLException {
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	try {
    		ps = conOldDB.prepareStatement("select s.score " +
    				"from scorecard s, user sub, user rev, submission su " +
    				"where s.submission_id = su.submission_id " +
    				"and su.submitter_id = sub.user_id and s.author_id = rev.user_id and s.scorecard_type = 2 " +
    				"and su.cur_version = 1 and s.is_completed = 1 " +
    				"and s.scorecard_v_id = (select MIN(s2.scorecard_v_id) from scorecard s2 " +
    				"    where s2.scorecard_id = s.scorecard_id and s2.cur_version = 0 " +
    				"        and s2.is_completed = 1 and s2.score > 0) " +
    		"and s.project_id = ? and sub.user_id = ? and rev.user_id = ?");

    		ps.setLong(1, data.getProjectId());
    		ps.setLong(2, data.getSubmitterId());
    		ps.setLong(3, data.getReviewerId());

    		rs = ps.executeQuery();
    		if (rs.next()) {
    			review.setInitialScore(rs.getFloat("score"));
    		} else {
    			logger.log(Level.INFO, "Can't find the initial score in the old db for review: " + review.getId() 
    					+ " using final score");
    			review.setInitialScore(review.getScore());
    		}
		} finally {
			closeResources(null, ps, rs);
		}
	}

	/**
     * If a project was migrated from the old OR
     * 
     * @param conOldDB connection to the old OR db
     * @param projectId  project id  
     * @return true if the project was migrated from the old OR
     * @throws SQLException
     */
    private static boolean checkIsMigratedFromOldOR(Connection conOldDB, long projectId) throws SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conOldDB.prepareStatement("select project_id from project where project_id = ? and cur_version = 1");
			ps.setLong(1, projectId);
			rs = ps.executeQuery();
			return rs.next();
		} finally {
			closeResources(null, ps, rs);
		}
	}

	/**
     * Computes and stores intial score for a review.
     *
     * @param scrMgr
     *            an instance of Scorecard Manager. Used to load scorecard templates for the
     *            reviews.
     * @param revMgr
     *            an instance of Review Manager. Used to load reviews from the database and update
     *            them back there.
     * @param review
     *            a review to compute initial score for.
     * @throws PersistenceException
     *             if an error occurred retrieving a scorecard template for one of the reviews.
     * @throws CalculationException
     *             if an error occurred while recalculating the score.
     */
    private static void computeScoreForReview(ScorecardManager scrMgr, ReviewManager revMgr, Review review)
            throws PersistenceException, CalculationException {
        Review anotherCopy = new Review();
        Item[] originalItems = review.getAllItems();
        Item[] dupeItems = new Item[originalItems.length];

        for (int i = 0; i < originalItems.length; ++i) {
            Comment appealResponse = null;
            Comment[] allComments = originalItems[i].getAllComments();

            for (int j = 0; j < allComments.length; ++j) {
                if (allComments[j].getCommentType().getName().equalsIgnoreCase("Appeal Response")) {
                    appealResponse = allComments[j];
                    break;
                }
            }

            dupeItems[i] = new Item();
            dupeItems[i].setQuestion(originalItems[i].getQuestion());

            String originalAnswer = (String) originalItems[i].getAnswer();

            if (appealResponse != null && appealResponse.getExtraInfo() != null && appealResponse.getExtraInfo() instanceof String) {
                originalAnswer = (String) appealResponse.getExtraInfo();
            }

            dupeItems[i].setAnswer(originalAnswer);
        }
        anotherCopy.addItems(dupeItems);
        anotherCopy.setAuthor(review.getAuthor());
        anotherCopy.setScorecard(review.getScorecard());
        anotherCopy.setSubmission(review.getSubmission());

        // Retrieve a scorecard template for the review
        Scorecard scorecardTemplate = scrMgr.getScorecard(review.getScorecard());

        logger.log(Level.INFO, "Recomputing score for review ID: " + review.getId() );

        // Obtain an instance of CalculationManager
        CalculationManager scoreCalculator = new CalculationManager();
        // Compute scorecard's score
        float newScore = scoreCalculator.getScore(scorecardTemplate, anotherCopy);

        review.setInitialScore(new Float(newScore));
        logger.log(Level.INFO, "Initial score is: " + newScore + " final_score: " + review.getScore()
						+ "; updating...");
    }

    /**
     * Returns IDs of all reviews whose Initial Score needs to be computed.
     *
     * @return an array of IDs.
     * @throws ScoreFixerException
     *             if any error occurs while accessing the database.
     */
    private static List<ReviewData> getAllReviewsIds() throws ScoreFixerException {
        Connection conn = createConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(QUERY_REVIEW_IDS_SQL);
            rs = pstmt.executeQuery();

            List<ReviewData> reviews = new LinkedList<ReviewData>();

            while (rs.next()) {
            	ReviewData rData = new ReviewData();
            	// r.review_id, riRev.value reviewer, riSub.value submitter, u.project_id
            	rData.setProjectId(rs.getLong("project_id"));
            	rData.setReviewId(rs.getLong("review_id"));
            	rData.setReviewerId(new Long(rs.getString("reviewer")));
            	rData.setSubmitterId(new Long(rs.getString("submitter")));
                reviews.add(rData);
            }

            return reviews;
        } catch (SQLException e) {
            throw new ScoreFixerException("An error occurred while retrieving IDs of the reviews." , e);
        } finally {
        	closeResources(conn, pstmt, rs);
        }
    }

    /**
     * This static method helps to create an object of the <code>ReviewManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws IllegalArgumentException
     *             if <code>request</code> parameter is <code>null</code>.
     * @throws ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    private static ReviewManager createReviewManager() throws ConfigurationException {
        // Return the newly-created Review Manager object
        return new DefaultReviewManager();
    }

    /**
     * This static method helps to create an object of the <code>ScorecardManager</code> class.
     *
     * @return a newly created instance of the class.
     * @throws com.topcoder.management.scorecard.ConfigurationException
     *             if error occurs while loading configuration settings, or any of the required
     *             configuration parameters are missing.
     */
    private static ScorecardManager createScorecardManager()
        throws com.topcoder.management.scorecard.ConfigurationException {
        // Return the newly-created Scorecard Manager object
        return new ScorecardManagerImpl();
    }

    /**
     * Creates an SQL connection.
     *
     * @return the connection created.
     * @throws ScoreFixerException
     *             if any error occurs while creating the connection.
     */
    private static Connection createConnection() throws ScoreFixerException {
        return createConnection(null);
    }

    /**
     * Creates an SQL connection.
     *
     * @return the connection created.
     * @throws ScoreFixerException
     *             if any error occurs while creating the connection.
     */
    private static Connection createConnection(String name) throws ScoreFixerException {
        try {
            DBConnectionFactory dbFactory = new DBConnectionFactoryImpl(
                    "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
            if (name == null) {
            	return dbFactory.createConnection();
            }
            return dbFactory.createConnection(name);
        } catch (UnknownConnectionException uce) {
            throw new com.topcoder.management.review.scorefixer.ConfigurationException(
                    "An error occurs while creating connection factory or connection.", uce);
        } catch (com.topcoder.db.connectionfactory.ConfigurationException ce) {
            throw new com.topcoder.management.review.scorefixer.ConfigurationException(
                    "An error occurs while creating connection factory or connection.", ce);
        } catch (DBConnectionException dce) {
            throw new com.topcoder.management.review.scorefixer.ConfigurationException(
                    "An error occurs while creating connection.", dce);
        }
    }
    
    /**
     * Closes passed in SQL resources as necessary. If a resource does not need to be closed, the
     * appropriate parameter should be <code>null</code>. This method closes resources in the
     * following order (provided none of them are <code>null</code>): result set is closed first,
     * then prepared statement, and connection is closed the last. No exceptions leave the
     * boundaries of this method.
     *
     * @param conn
     *            a connection to close. If it is not <code>null</code> and is not already closed,
     *            it will be closed in the very end, after all other resources have been closed.
     * @param pstmt
     *            a prepared statement to close. If it is not <code>null</code>, it will be
     *            closed after result set has been closed, but before connection.
     * @param rs
     *            a result set to close. It it is not <code>null</code>, it will be closed in the
     *            first place.
     */
    private static void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // eat
            }
        }

        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                // eat
            }
        }

        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // eat
        }
    }
}
