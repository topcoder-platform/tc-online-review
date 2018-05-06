/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Contains a demonstration of how to use the Review Score Calculator component.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class Demo extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Fields

    /**
     * The name of the configuration file containing all the configuration for this class.
     */
    private static final String CONFIG_FILENAME = "Demo/DemoConfig.xml";

    /**
     * The default configuration namespace.
     */
    private static final String DEFAULT_NAMESPACE =
        "com.topcoder.management.review.scorecalculator.CalculationManager";

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(Demo.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp/TearDown

    /**
     * Recreates the instances used during unit testing, and reloads the configuration file.
     *
     * @throws  Exception
     *          An unknown error occured during setup.
     */
    protected void setUp() throws Exception {
        clearNamespaces();
        ConfigManager.getInstance().add(CONFIG_FILENAME);
    }

    /**
     * Clears the configuration manager of our namespaces.
     */
    protected void tearDown() {
        clearNamespaces();
    }

    /**
     * Clears the configuration manager of our namespaces.
     */
    private void clearNamespaces() {
        TestUtil.clearNamespace(DEFAULT_NAMESPACE);
        TestUtil.clearNamespace("com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator");
        TestUtil.clearNamespace("com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator1");
        TestUtil.clearNamespace("com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator2");
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Demonstrations

    /**
     * Demonstrates the component usage by initializing using the API.
     *
     * @throws  CalculationException
     *          An unknown error occurred.
     */
    public void testDemoUsingAPI() throws CalculationException {
        // Create a new ScorecardMatrixBuilder.
        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        // Create score calculators.
        ScoreCalculator binaryCalculator = new BinaryScoreCalculator("Yes", "No");
        ScoreCalculator scale4Calculator = new ScaledScoreCalculator(4);
        ScoreCalculator scale10Calculator = new ScaledScoreCalculator(10);

        // Create a new Cache.
        Cache cache = new SimpleCache();

        // Create a new CalculationManager using the API.
        long[] questionTypes = new long[] {1, 2, 3};
        ScoreCalculator[] calculators = new ScoreCalculator[] {
            binaryCalculator, scale4Calculator, scale10Calculator
        };

        CalculationManager manager = new CalculationManager(questionTypes, calculators, builder, cache);

        // Add a new calculator that we "forgot" about.
        ScoreCalculator dynamicCalculator = new ScaledScoreCalculator();
        manager.addScoreCalculator(4, dynamicCalculator);

        // Get the scorecard and review from somewhere.
        Scorecard scorecard = createScorecard();
        Review review = createReview();

        // Calculate the score and check it.
        assertEquals(
                "The score did not match the expected score.",
                59.1, manager.getScore(scorecard, review), 0.001);
    }

    /**
     * Demonstrates the component usage by initializing using a configuration file.
     *
     * @throws  CalculationException
     *          An unknown error occurred.
     */
    public void testDemoUsingConfig() throws CalculationException {
        // Create a new CalculationManager using the default configuration namespace.
        CalculationManager manager = new CalculationManager();

        // Get the scorecard and review from somewhere.
        Scorecard scorecard = createScorecard();
        Review review = createReview();

        // Calculate the score and check it.
        assertEquals(
                "The score did not match the expected score.",
                59.1, manager.getScore(scorecard, review), 0.001);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Review / Scorecard Creators

    /**
     * Creates and returns the review as specified in the Component Specifications document.
     *
     * @return  The review as specified in the Component Specifications document.
     */
    private Review createReview() {
        Item item1 = new Item();
        item1.setAnswer("Yes");
        item1.setQuestion(1);

        Item item2 = new Item();
        item2.setAnswer("No");
        item2.setQuestion(2);

        Item item3 = new Item();
        item3.setAnswer("2");
        item3.setQuestion(3);

        Item item4 = new Item();
        item4.setAnswer("60/100");
        item4.setQuestion(4);

        Item item5 = new Item();
        item5.setAnswer("3");
        item5.setQuestion(5);

        Item item6 = new Item();
        item6.setAnswer("10");
        item6.setQuestion(6);

        Review result = new Review();
        result.addItem(item1);
        result.addItem(item2);
        result.addItem(item3);
        result.addItem(item4);
        result.addItem(item5);
        result.addItem(item6);

        return result;
    }

    /**
     * Creates and returns section A of the scorecard as specified in the Component Specifications document.
     *
     * @return  Section A of the scorecard as specified in the Component Specifications document.
     */
    private Section createSectionA() {
        QuestionType questionType1 = new QuestionType();
        questionType1.setId(1);

        Question question1 = new Question();
        question1.setId(1);
        question1.setDescription("Question 1");
        question1.setQuestionType(questionType1);
        question1.setWeight(0.5f);

        Question question2 = new Question();
        question2.setId(2);
        question2.setDescription("Question 2");
        question2.setQuestionType(questionType1);
        question2.setWeight(0.5f);

        Section result = new Section();
        result.addQuestion(question1);
        result.addQuestion(question2);
        result.setId(1);
        result.setName("Section A");
        result.setWeight(0.4f);

        return result;
    }

    /**
     * Creates and returns section B of the scorecard as specified in the Component Specifications document.
     *
     * @return  Section B of the scorecard as specified in the Component Specifications document.
     */
    private Section createSectionB() {
        QuestionType questionType2 = new QuestionType();
        questionType2.setId(2);

        Question question3 = new Question();
        question3.setId(3);
        question3.setDescription("Question 3");
        question3.setQuestionType(questionType2);
        question3.setWeight(1.0f);

        Section result = new Section();
        result.addQuestion(question3);
        result.setId(2);
        result.setName("Section B");
        result.setWeight(0.6f);

        return result;
    }

    /**
     * Creates and returns section C of the scorecard as specified in the Component Specifications document.
     *
     * @return  Section C of the scorecard as specified in the Component Specifications document.
     */
    private Section createSectionC() {
        QuestionType questionType3 = new QuestionType();
        questionType3.setId(3);

        QuestionType questionType4 = new QuestionType();
        questionType4.setId(4);

        Question question4 = new Question();
        question4.setId(4);
        question4.setDescription("Question 4");
        question4.setQuestionType(questionType4);
        question4.setWeight(0.4f);

        Question question5 = new Question();
        question5.setId(5);
        question5.setDescription("Question 4");
        question5.setQuestionType(questionType3);
        question5.setWeight(0.3f);

        Question question6 = new Question();
        question6.setId(6);
        question6.setDescription("Question 6");
        question6.setQuestionType(questionType3);
        question6.setWeight(0.3f);

        Section result = new Section();
        result.addQuestion(question4);
        result.addQuestion(question5);
        result.addQuestion(question6);
        result.setId(3);
        result.setName("Section C");
        result.setWeight(1.0f);

        return result;
    }

    /**
     * Creates and returns the Design group of the scorecard as specified in the Component Specifications document.
     *
     * @return  The Design group of the scorecard as specified in the Component Specifications document.
     */
    private Group createDesignGroup() {
        Group result = new Group();
        result.addSection(createSectionA());
        result.addSection(createSectionB());
        result.setId(1);
        result.setName("Design");
        result.setWeight(0.3f);

        return result;
    }

    /**
     * Creates and returns the Documentation group of the scorecard as specified in the Component Specifications
     * document.
     *
     * @return  The Documentation group of the scorecard as specified in the Component Specifications document.
     */
    private Group createDocumentationGroup() {
        Group result = new Group();
        result.addSection(createSectionC());
        result.setId(2);
        result.setName("Documentation");
        result.setWeight(0.7f);

        return result;
    }

    /**
     * Creates and returns the scorecard as specified in the Component Specifications document.
     *
     * @return  The scorecard as specified in the Component Specifications document.
     */
    private Scorecard createScorecard() {
        Scorecard result = new Scorecard();
        result.addGroup(createDesignGroup());
        result.addGroup(createDocumentationGroup());
        result.setId(1);
        result.setName("Demo");
        result.setVersion("1.0");

        return result;
    }
}
