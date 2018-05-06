/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
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

import java.io.File;

import java.util.Iterator;


/**
 * Tests for CalculationManager class.
 *
 * @author qiucx0161
 * @version 1.0
 */
public class TestCalculationManager extends TestCase {
    /** ConfigManager instance used for testing. */
    private ConfigManager cm = ConfigManager.getInstance();

    /** CalculationManager instance used for testing. */
    private CalculationManager manager = null;

    /** ScoreCalculator instance used for testing. */
    private ScoreCalculator calc1 = null;

    /** ScoreCalculator instance used for testing. */
    private ScoreCalculator calc2 = null;

    /** ScorecardMatrixBuilder instance used for testing. */
    private ScorecardMatrixBuilder builder = null;

    /** The scorecard used for testing. */
    private Scorecard scorecard = null;

    /** The group used for testing. */
    private Group group = null;

    /** The section used for testing. */
    private Section section = null;

    /** The question used for testing. */
    private Question question = null;

    /** The question type used for testing. */
    private QuestionType questionType = null;

    /** The item used for unit testing. */
    private Item item = null;

    /** The review used for unit testing. */
    private Review review = null;

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        clearAllNamespaces();
        cm.add(new File("test_files/accuracy/Config.xml").getAbsolutePath());

        calc1 = new BinaryScoreCalculator("Yes", "No");
        calc2 = new ScaledScoreCalculator(4);
        builder = new DefaultScorecardMatrixBuilder();
        manager = new CalculationManager(new long[] { 888881 },
                new ScoreCalculator[] { calc1 }, builder, new SimpleCache());

        questionType = new QuestionType(888881, "type");
        question = new Question(666662, "1+2=3?", 100.0f);
        question.setQuestionType(questionType);
        question.setDescription("easy question.");

        section = new Section(666662, "document", 100.0f);
        section.addQuestion(question);

        group = new Group(666663, "all document", 100.00f);
        group.addSection(section);

        scorecard = new Scorecard(666664, "dev card");
        scorecard.setVersion("2.0");
        scorecard.addGroup(group);

        item = new Item(666665, 666662, "Yes");

        review = new Review(666666, "ivern");
        review.addItem(item);
    }

    /**
     * Setup the test environment.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        clearAllNamespaces();
        manager = null;
    }

    /**
     * Clear all the namespaces in this component
     */
    private void clearAllNamespaces() {
        try {
            for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
                cm.removeNamespace((String) iter.next());
            }
        } catch (Exception e) {
            // ignore.
        }
    }

    /**
     * Tests CalculationManager() method with accuracy state.
     *
     * @throws ConfigurationException to Junit
     */
    public void testCalculationManagerAccuracy1() throws ConfigurationException {
        manager = new CalculationManager();
        assertTrue("creating CalculationManager fails",
            manager.getScoreCalculator(99990) instanceof BinaryScoreCalculator);
        assertTrue("creating CalculationManager fails",
            manager.getScoreCalculator(99991) instanceof ScaledScoreCalculator);
    }

    /**
     * Tests CalculationManager(String namespace) method with accuracy state.
     *
     * @throws ConfigurationException to JUnit.
     */
    public void testCalculationManagerAccuracy2() throws ConfigurationException {
        manager = new CalculationManager("CalculationManager.accuracy.test");
        assertTrue("creating CalculationManager fails",
            manager.getScoreCalculator(999992) instanceof BinaryScoreCalculator);
    }

    /**
     * Tests CalculationManager(long[] questionTypes, ScoreCalculator[] calculators,
     * ScorecardMatrixBuilder builder, boolean useCaching) method with accuracy state.
     */
    public void testCalculationManagerAccuracy3() {
        manager = new CalculationManager(new long[] { 888883 },
                new ScoreCalculator[] { calc1 }, builder, false);
        assertNotNull("creating CalculationManager fails", manager);
        assertTrue("creating CalculationManager fails",
            manager.getScoreCalculator(888883) instanceof BinaryScoreCalculator);
    }

    /**
     * Tests CalculationManager(long[] questionTypes, ScoreCalculator[] calculators,
     * ScorecardMatrixBuilder builder, Cache cache) method with accuracy state.
     */
    public void testCalculationManagerAccuracy4() {
        assertNotNull("creating CalculationManager fails", manager);
        assertTrue("creating CalculationManager fails",
            manager.getScoreCalculator(888881) instanceof BinaryScoreCalculator);
    }

    /**
     * Tests getScore(Scorecard scorecard, Review review) method with accuracy state.
     *
     * @throws CalculationException to JUnit.
     */
    public void testGetScoreAccuracy1() throws CalculationException {
        assertEquals("getScore is not correct.", 100.0f,
            manager.getScore(scorecard, review), 1e-9f);
    }

    /**
     * Tests getScore(Scorecard scorecard, Review review) method with accuracy state.
     *
     * @throws CalculationException to JUnit.
     */
    public void testGetScoreAccuracy2() throws CalculationException {
        manager = new CalculationManager(new long[] { 888881 },
                new ScoreCalculator[] { calc2 }, builder, new SimpleCache());
        item = new Item(666665, 666662, "3");
        review = new Review(666666, "ivern");
        review.addItem(item);
        assertEquals("getScore is not correct.", 75.0f,
            manager.getScore(scorecard, review), 1e-9f);
    }

    /**
     * Tests addScoreCalculator(long questionType, ScoreCalculator scoreCalculator)
     * method with accuracy state.
     */
    public void testAddScoreCalculatorAccuracy() {
        assertNull("getScoreCalculator is not correct.", manager.getScoreCalculator(12345));
        manager.addScoreCalculator(12345, calc2);
        assertSame("addScoreCalculator is not correct.", calc2,
            manager.getScoreCalculator(12345));
    }

    /**
     * Tests removeScoreCalculator(long questionType) method with accuracy state.
     */
    public void testRemoveScoreCalculatorAccuracy() {
        assertSame("getScoreCalculator is not correct.", calc1,
            manager.getScoreCalculator(888881));
        manager.removeScoreCalculator(888881);
        assertNull("removeScoreCalculator is not correct.",
            manager.getScoreCalculator(888881));
    }

    /**
     * Tests clearScoreCalculators() method with accuracy state.
     */
    public void testClearScoreCalculatorsAccuracy() {
        manager.addScoreCalculator(12345, calc2);
        assertSame("addScoreCalculator is not correct.", calc2,
            manager.getScoreCalculator(12345));
        assertSame("addScoreCalculator is not correct.", calc1,
            manager.getScoreCalculator(888881));

        manager.clearScoreCalculators();

        assertNull("removeScoreCalculator is not correct.",
            manager.getScoreCalculator(12345));
        assertNull("removeScoreCalculator is not correct.",
            manager.getScoreCalculator(888881));
    }
}
