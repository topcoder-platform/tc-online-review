/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 *
 */
package com.topcoder.management.review.scorecalculator.stresstests;

import java.io.File;
import java.util.Iterator;
import java.util.Random;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Stress Tests for <code>ScorecardCalculator</code> component.
 *
 * @author crackme
 * @version 1.0
 */
public class ScorecardCalculatorStressTests extends TestCase {

    /**
     * The number of iterations for each thread to go.
     */
    private static int NUM = 1000;

    /**
     * The number of threads to simulate.
     */
    private static int THREADS = 20;

    /** An instance of ScoreCalculator. */
    private ScoreCalculator calculator1 = null;

    /** An instance of ScoreCalculator. */
    private ScoreCalculator calculator2 = null;

    /** An instance of ScorecardMatrixBuilder. */
    private ScorecardMatrixBuilder builder = null;

    /** An instance of  scorecard. */
    private Scorecard scorecard = null;

    /** An instance of CalculationManager. */
    private CalculationManager manager = null;

    /** An instance of ConfigManager. */
    private ConfigManager cm = ConfigManager.getInstance();

    /** An instance of review. */
    private Review review = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        // Set up the namespaces.
        clearNamespaces();
        cm.add(new File("test_files/stress/stress.xml").getAbsolutePath());

        // Creates the calculators.
        calculator1 = new BinaryScoreCalculator("BinaryScoreCalculator.Stress");
        calculator2 = new ScaledScoreCalculator("ScaledScoreCalculator.Stress");
        builder = new DefaultScorecardMatrixBuilder();

        // Initialize the manager.
        manager = new CalculationManager();
        // Initialize the scorecard instance.
        scorecard = createScorecard();

        // Initialize the review instance.
        Item item1 = new Item(777771, 222221, "Yes");
        Item item2 = new Item(777772, 222222, "1");
        Item item3 = new Item(777773, 222223, "2/4");
        Item item4 = new Item(777774, 222224, "3/4");
        Item item5 = new Item(777775, 222225, "No");
        review = new Review(123453, "crackme");
        review.addItem(item1);
        review.addItem(item2);
        review.addItem(item3);
        review.addItem(item4);
        review.addItem(item5);
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        clearNamespaces();
    }

    /**
     * Create the scorecard used to test.
     * @return the created scorecard.
     */
    private Scorecard createScorecard() {
        // Creates the questions.
        QuestionType type1 = new QuestionType(111111, "type1");
        QuestionType type2 = new QuestionType(111112, "type2");
        Question question1 = new Question(222221, "who are you?", 50.00f);
        Question question2 = new Question(222222, "How old are you?", 50.00f);
        Question question3 = new Question(222223, "Where are you from?", 50.00f);
        Question question4 = new Question(222224, "Where are you from1?", 50.00f);
        Question question5 = new Question(222225, "Where are you from2?", 100.00f);
        question1.setQuestionType(type1);
        question1.setDescription("First question.");
        question2.setQuestionType(type2);
        question2.setDescription("Second question.");
        question3.setQuestionType(type2);
        question3.setDescription("Third question.");
        question4.setQuestionType(type2);
        question4.setDescription("Four question.");
        question5.setQuestionType(type1);
        question5.setDescription("Five question.");

        // Creates the sections.
        Section section1 = new Section(333331, "section1", 50.00f);
        Section section2 = new Section(333332, "section2", 50.00f);
        Section section3 = new Section(333333, "section3", 100.00f);

        section1.addQuestion(question1);
        section1.addQuestion(question2);
        section2.addQuestion(question3);
        section2.addQuestion(question4);
        section3.addQuestion(question5);

        // Create the groups.
        Group group1 = new Group(444441, "group1", 50.00f);
        Group group2 = new Group(444442, "group2", 50.00f);
        group1.addSection(section1);
        group1.addSection(section2);

        group2.addSection(section3);

        // Create the scorecard.
        scorecard = new Scorecard(666664, "Design Reviewcard");
        scorecard.setVersion("2.0");
        scorecard.addGroup(group1);
        scorecard.addGroup(group2);
        return scorecard;
    }

    /**
     * Remove the namespaces in this class.
     */
    private void clearNamespaces() {
        try {
            for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
                cm.removeNamespace((String) iter.next());
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    /**
     * Tests retrieve the correct score.
     * @throws CalculationException to JUnit.
     */
    public void testGetScoreBenchmark() throws CalculationException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < NUM; ++i) {
            assertEquals("getScore is not correct.", 31.25f, manager.getScore(scorecard, review), 1e-9f);
        }
        long end = System.currentTimeMillis();
        System.out.println("Getting the score with " + NUM + " times took " + (end - start) + "ms");
    }

    /**
     * Tests cocurrency the methods of Calculation manager, like
     * addScoreCalculator, removeScoreCalculator, getScoreCalculator and clearScoreCalculators.
     *
     * @throws Exception to JUnit
     */
    public void testCocurrencyOfManager() throws Exception {
        Thread[] threads = new Thread[THREADS];
        for (int i = 1; i < THREADS; ++i) {
            threads[i] = new WorkerThread(i);
            threads[i].start();
        }
        for (int i = 1; i < THREADS; ++i) {
            threads[i].join();
        }
    }

    /**
     * Tests cocurrency of the cache.
     *
     * @throws Exception to JUnit
     */
    public void testCocurrencyOfCache() throws Exception {
        Thread[] threads = new Thread[THREADS];
        for (int i = 0; i < THREADS; ++i) {
            threads[i] = new WorkerThread2();
            threads[i].start();
        }
        for (int i = 0; i < THREADS; ++i) {
            threads[i].join();
        }
    }

    /**
     * Worker thread.
     */
    private class WorkerThread extends Thread {

        /**
         * <p>
         * An instance of CalculationManager
         * </p>
         */
        private CalculationManager calManager = null;

        /**
         * The index of the thread.
         */
        private final long index;

        /**
         * The random generator to determine operation.
         */
        private Random random = new Random();

        /**
         * Constructor.
         *
         * @param index the index of the thread.
         */
        public WorkerThread(long index) {
            this.index = index;
            calManager = new CalculationManager(new long[] { 111111, 111112 },
                    new ScoreCalculator[] { calculator1, calculator2 }, builder, new SimpleCache());
        }

        /**
         * <p>
         * Run one of methods of calculation Manager according to the index
         * </p>
         * @throws Exception
         */
        public void run() {

            try {
                ScaledScoreCalculator ssc = new ScaledScoreCalculator(index);
                for (int i = 0; i < NUM; ++i) {
                    switch (random.nextInt() % 5) {
                        case 0: {
                            this.calManager.addScoreCalculator(index, ssc);
                            break;
                        }
                        case 1: {
                            this.calManager.getScoreCalculator(index);
                            break;
                        }
                        case 2: {
                            this.calManager.removeScoreCalculator(index);
                            break;
                        }
                        case 3: {
                            this.calManager.clearScoreCalculators();
                            break;
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                fail(exception.getMessage());
            }
        }
    }

    /**
     * Worker thread2.
     */
    private class WorkerThread2 extends Thread {

        /**
         * <p>
         * An instance of CalculationManager
         * </p>
         */
        private CalculationManager calManager = null;

        /**
         * Constructor.
         */
        public WorkerThread2() {
            calManager = new CalculationManager(new long[] { 111111, 111112 },
                    new ScoreCalculator[] { calculator1, calculator2 }, builder, new SimpleCache());
        }

        /**
         * <p>
         * Run one of methods of calculation Manager according to the index
         * </p>
         */
        public void run() {

            try {
                for (int i = 0; i < NUM; ++i) {
                    calManager.getScore(scorecard, review);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                fail(exception.getMessage());
            }
        }
    }
}
