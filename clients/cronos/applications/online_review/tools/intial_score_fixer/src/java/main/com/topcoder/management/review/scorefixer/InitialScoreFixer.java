/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorefixer;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.BetweenFilter;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;
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

    /**
     * Specifies a year to start searching the reviews from.
     */
    private static int YEAR_TO_START = 1995;

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
        	ConfigManager cfg = ConfigManager.getInstance();
        	cfg.add(InitialScoreFixer.class.getResource("/conf/ScoreFixer.xml"));

            computeScores();
        } catch (ConfigManagerException e) {
        	logger.log(Level.ERROR, "fail to load configuration:\n" +
                    LogMessage.getExceptionStackTrace(e));
		} catch (ReviewManagementException rme) {
            logger.log(Level.ERROR, "fail to load one of reviews:\n" + LogMessage.getExceptionStackTrace(rme));
            throw new ScoreFixerException("fail to load one of reviews", rme);
        } catch (com.topcoder.management.scorecard.ConfigurationException ce) {
            logger.log(Level.ERROR, "fail to create scorecard manager instance:\n" +
                    LogMessage.getExceptionStackTrace(ce));
            throw new com.topcoder.management.review.scorefixer.ConfigurationException(
                    "fail to create scorecard manager instance", ce);
        } catch (PersistenceException pe) {
            logger.log(Level.ERROR, "fail to load one of scorecard templates:\n" + LogMessage.getExceptionStackTrace(pe));
            throw new ScoreFixerException("fail to load one of scorecard templates", pe);
        } catch (CalculationException ce) {
            logger.log(Level.ERROR, "fail to calculate a score:\n" + LogMessage.getExceptionStackTrace(ce));
            throw new ScoreFixerException("fail to calculate a score", ce);
        } catch (RuntimeException t) {
        	logger.log(Level.ERROR, "fail to build command line cause of illegal switch:"
        			+ "\n" + LogMessage.getExceptionStackTrace(t));
            throw t;
        }
    }

    /**
     * Recomputes initial scorecards' scores based on the values stored in the Appeal's Response
     * extra info.
     *
     * @throws ReviewManagementException
     *             if an error occurred retrieving or updating a review.
     * @throws PersistenceException
     *             if an error occurred retrieving a scorecard template for one of the reviews.
     * @throws CalculationException
     *             if an error occurred while recalculating the score.
     * @throws com.topcoder.management.scorecard.ConfigurationException
     *             if an error occurred creating a Scorecard Manager instance.
     */
    private static void computeScores() throws ReviewManagementException, PersistenceException, CalculationException,
            com.topcoder.management.scorecard.ConfigurationException {
        final Calendar today = new GregorianCalendar();
        Calendar gc = new GregorianCalendar(YEAR_TO_START, 1, 1);

        ReviewManager reviewMgr = createReviewManager();
        ScorecardManager scrMgr = createScorecardManager();

        ScorecardType[] allTypes = scrMgr.getAllScorecardTypes();
        ScorecardType reviewType = null;

        for (int i = 0; i < allTypes.length; ++i) {
            if (allTypes[i].getName().equalsIgnoreCase("Review")) {
                reviewType = allTypes[i];
                break;
            }
        }

        if (reviewType == null) {
            return;
        }

        while (gc.before(today)) {
            Calendar anotherDate = (Calendar) gc.clone();
            anotherDate.add(Calendar.MONTH, 1);

            computeScoresForDateRange(scrMgr, reviewMgr, gc.getTime(), anotherDate.getTime(), reviewType);

            gc = anotherDate;
        }
    }

    /**
     * Retrieves and updates (recomputes their initial scores) the reviews created in the specified
     * date range. Date range is used to lighten memory requirements for this tool.
     *
     * @param scrMgr
     *            an instance of Scorecard Manager. Used to load scorecard templates for the
     *            reviews.
     * @param revMgr
     *            an instance of Review Manager. Used to load reviews from the database and update
     *            them back there.
     * @param start
     *            Specifies a start date reviews should be retrieved from. Only reviews created
     *            after the specified date will be retrieved.
     * @param end
     *            Specifies an end date reviews should be retrieved to. Only reviews created before
     *            the specified date will be retrieved.
     * @param reviewType
     *            Specifies a type of the review to retrieve and update. Only reviews of type
     *            &quot;Review&quot; need to be updated.
     * @throws ReviewManagementException
     *             if an error occurred retrieving or updating review(s).
     * @throws PersistenceException
     *             if an error occurred retrieving a scorecard template for one of the reviews.
     * @throws CalculationException
     *             if an error occurred while recalculating the score.
     */
    private static void computeScoresForDateRange(ScorecardManager scrMgr, ReviewManager revMgr, Date start, Date end, ScorecardType reviewType)
            throws ReviewManagementException, PersistenceException, CalculationException {
        Filter dateFilter = new BetweenFilter("creation_date", new java.sql.Date(end.getTime()), new java.sql.Date(start.getTime()));
        Filter typeFilter = new EqualToFilter("scorecardType", new Long(reviewType.getId()));
        Filter committedFilter = new EqualToFilter("committed", new Integer(1));
        Filter scoreFilter = new NotFilter(new NullFilter("score"));
        Filter initialScoreFilter = new NullFilter("initial_score");
        Filter combinedFilter = new AndFilter(Arrays.asList(new Filter[] {
                dateFilter, typeFilter, committedFilter, scoreFilter, initialScoreFilter }));

        Review[] reviews = revMgr.searchReviews(combinedFilter, true);

        for (int i = 0; i < reviews.length; ++i) {
            computeScoreForReview(scrMgr, revMgr, reviews[i]);
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
     * @throws ReviewManagementException
     *             if an error occurred updating the review.
     * @throws PersistenceException
     *             if an error occurred retrieving a scorecard template for one of the reviews.
     * @throws CalculationException
     *             if an error occurred while recalculating the score.
     */
    private static void computeScoreForReview(ScorecardManager scrMgr, ReviewManager revMgr, Review review)
            throws PersistenceException, CalculationException, ReviewManagementException {
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

        System.out.println("Recomputing score for review ID: " + review.getId());

        // Obtain an instance of CalculationManager
        CalculationManager scoreCalculator = new CalculationManager();
        // Compute scorecard's score
        float newScore = scoreCalculator.getScore(scorecardTemplate, anotherCopy);

        review.setInitialScore(new Float(newScore));
        System.out.println("New score is: " + newScore + "; updating...");
        revMgr.updateReview(review, "InitialScoreFixer");
        System.out.println("===== Updated!");
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
    public static ScorecardManager createScorecardManager()
        throws com.topcoder.management.scorecard.ConfigurationException {
        // Return the newly-created Scorecard Manager object
        return new ScorecardManagerImpl();
    }
}
