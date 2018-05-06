/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import java.io.File;
import java.util.Iterator;

import com.topcoder.management.review.data.Item;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.review.scorecalculator.CalculationException;
import com.topcoder.management.review.scorecalculator.CalculationManager;
import com.topcoder.management.review.scorecalculator.ConfigurationException;
import com.topcoder.management.review.scorecalculator.ReviewStructureException;
import com.topcoder.management.review.scorecalculator.ScoreCalculator;
import com.topcoder.management.review.scorecalculator.ScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.calculators.BinaryScoreCalculator;
import com.topcoder.management.review.scorecalculator.calculators.ScaledScoreCalculator;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.QuestionType;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>CalculationManager</code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class FailureTestForCalculationManager extends TestCase {

    /**
     * Represents the config file for testing.
     */
    private static final String CONFIG_FILE = "test_files/failuretests/CalculationManagerConfig.xml";

    /**
     * Represents the CalculationManager instance for test.
     */
    private CalculationManager manager = null;

    /**
     * Set up the enviroment.
     *
     * @throws Exception
     *             to junit.
     */
    public void setUp() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
        File file = new File(CONFIG_FILE);
        cm.add(file.getAbsolutePath());

        manager = new CalculationManager("valid");
    }

    /**
     * Tear down the enviroment. Clear all the namespaces in the config manager.
     *
     * @throws Exception
     *             to junit.
     */
    public void tearDown() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the namespace is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_1() throws Exception {
        try {
            new CalculationManager(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the namespace is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_2() throws Exception {
        try {
            new CalculationManager("");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the namespace is empty,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_3() throws Exception {
        try {
            new CalculationManager("         ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_4() throws Exception {
        String namespace = "valid2";
        try {
            new CalculationManager(namespace);
        } catch (ConfigurationException e) {
            fail("ConfigurationException is expected.");
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the builder property is missing,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_5() throws Exception {
        String namespace = "invalid_1";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the builder property is not correctly
     * configured, ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_6() throws Exception {
        String namespace = "invalid_2";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the builder property is not correctly
     * configured, ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_7() throws Exception {
        String namespace = "invalid_3";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If there is no calculators configured,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_8() throws Exception {
        String namespace = "invalid_4";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the question type is not positive,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_9() throws Exception {
        String namespace = "invalid_5";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the question type is not positive,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_10() throws Exception {
        String namespace = "invalid_6";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the question type is duplicate,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_11() throws Exception {
        String namespace = "invalid_7";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If the question type is too large for
     * long, ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_12() throws Exception {
        String namespace = "invalid_8";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(String namespace) </code>. If class for calculator is not correct,
     * ConfigurationException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testCalculationManagerString_13() throws Exception {
        String namespace = "invalid_9";
        try {
            new CalculationManager(namespace);
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument long is null, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_1() {
        long[] a = new long[1];

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(null, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument ScoreCalculator is null, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_2() {
        long[] a = new long[1];
        a[0] = 1;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, null, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument ScorecardMatrixBuilder is null, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_3() {
        long[] a = new long[1];
        a[0] = 1;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, null, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument long array contains non positive value, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_4() {
        long[] a = new long[1];
        a[0] = 0;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument long array contains non positive value, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_5() {
        long[] a = new long[1];
        a[0] = -1;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument ScoreCalculator[] contains null value, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_6() {
        long[] a = new long[1];
        a[0] = 1;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = null;

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument ScoreCalculator[] lenght is not equal to long array, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_7() {
        long[] a = new long[2];
        a[0] = 1;
        a[1] = 2;

        ScoreCalculator[] s = new ScoreCalculator[1];
        s[0] = new BinaryScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, boolean) </code>.
     * if argument long array contains duplicate value, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderboolean_8() {
        long[] a = new long[2];
        a[0] = 2;
        a[1] = 2;

        ScoreCalculator[] s = new ScoreCalculator[2];
        s[0] = new BinaryScoreCalculator();
        s[1] = new ScaledScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, true);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test constructor <code>CalculationManager(long[], ScoreCalculator[], ScorecardMatrixBuilder, Cache) </code>. If
     * parameter Cache is null, IllegalArgumentException should be thrown.
     */
    public void testCalculationManagerlongArrayScoreCalculatorArrayScorecardMatrixBuilderCache() {
        long[] a = new long[2];
        a[0] = 2;
        a[1] = 1;

        ScoreCalculator[] s = new ScoreCalculator[2];
        s[0] = new BinaryScoreCalculator();
        s[1] = new ScaledScoreCalculator();

        ScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

        try {
            new CalculationManager(a, s, builder, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If scorecard is null, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_1() throws Exception {
        try {
            manager.getScore(null, new Review());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }


    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If review is null, IllegalArgumentException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_2() throws Exception {
        try {
            manager.getScore(new Scorecard(), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If review has no items, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_3() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        try {
            manager.getScore(card, new Review());
            fail("ReviewStructureException is expected.");
        } catch (ReviewStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If review has not had the same number of questions as Scorecard, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_4() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();
        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(2);
        item.setQuestion(2);

        review.addItem(item);

        try {
            manager.getScore(card, review);
            fail("ReviewStructureException is expected.");
        } catch (ReviewStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If the question is associated with id is not exists, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_5() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();
        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(2);
        item.setQuestion(2);

        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("1/2");
        item2.setId(3);
        item2.setQuestion(10);

        review.addItem(item2);

        try {
            manager.getScore(card, review);
            fail("ReviewStructureException is expected.");
        } catch (ReviewStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If the question has not set questiontype, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_6() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();
        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(2);
        item.setQuestion(2);

        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("1/2");
        item2.setId(3);
        item2.setQuestion(2);

        review.addItem(item2);

        try {
            manager.getScore(card, review);
            fail("ReviewStructureException is expected.");
        } catch (ReviewStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * If the question has not set questiontype, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_7() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);
        question2.setQuestionType(new QuestionType(1, "type1"));

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();
        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(2);
        item.setQuestion(2);

        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("1/2");
        item2.setId(3);
        item2.setQuestion(2);

        review.addItem(item2);

        try {
            manager.getScore(card, review);
            fail("CalculationException is expected.");
        } catch (CalculationException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * In this case, the scorecard and review instance are all correct.
     * @throws Exception to junit.
     */
    public void testGetScore_8() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");
        card.setId(11);

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);
        group.setId(111);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);
        section.setId(1111);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);
        question1.setQuestionType(new QuestionType(2, "type2"));

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);
        question2.setQuestionType(new QuestionType(1, "type1"));

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();

        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(10);
        item.setQuestion(1);


        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("Yes");
        item2.setId(1);
        item2.setQuestion(2);

        review.addItem(item2);

        // the correct answer should be 90.
        System.out.println("The score is : " +  manager.getScore(card, review));
    }


    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * In this case, the question id exists for 2 times, ReviewStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_9() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");
        card.setId(11);

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);
        group.setId(111);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);
        section.setId(1111);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);
        question1.setQuestionType(new QuestionType(2, "type2"));

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setId(2);
        question2.setWeight(60);
        question2.setQuestionType(new QuestionType(1, "type1"));

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();

        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(10);
        item.setQuestion(1);


        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("1/4");
        item2.setId(1);
        item2.setQuestion(1);

        review.addItem(item2);

        try {
            manager.getScore(card, review);
            fail("ReviewStructureException is expected.");
        } catch (ReviewStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>float getScore(Scorecard scorecard, Review review) </code>.
     * In this case, the question type is not known, ScorecardStructureException should be thrown.
     * @throws Exception to junit.
     */
    public void testGetScore_11() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");
        card.setId(11);

        Group group = new Group();
        group.setName("name");
        group.setWeight(100);
        group.setId(111);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);
        section.setId(1111);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);
        question1.setQuestionType(new QuestionType(2, "type2"));

        Question question2 = new Question();
        question2.setDescription("question2");
        question2.setWeight(60);
        question2.setId(2);

        QuestionType type = new QuestionType(1, "type1");
        type.setId(10);

        question2.setQuestionType(type);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        Review review = new Review();

        Item item = new Item();
        item.setAnswer("3/4");
        item.setId(10);
        item.setQuestion(1);


        review.addItem(item);

        Item item2 = new Item();
        item2.setAnswer("Yes");
        item2.setId(1);
        item2.setQuestion(2);

        review.addItem(item2);

        try {
            manager.getScore(card, review);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>ScoreCalculator getScoreCalculator(long questionType) </code>.
     * If the questionType is not positive, IllegalArgumentException should be thrown.
     *
     */
    public void testGetScoreCalculator_1() {
        try {
            manager.getScoreCalculator(0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>ScoreCalculator getScoreCalculator(long questionType) </code>.
     * If the questionType is not positive, IllegalArgumentException should be thrown.
     *
     */
    public void testGetScoreCalculator_2() {
        try {
            manager.getScoreCalculator(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>void addScoreCalculator(long questionType, ScoreCalculator scoreCalculator) </code>. If The
     * questionType is not a positive long, or the scoreCalculator is null, IllegalArgumentException should be thrown.
     */
    public void testAddScoreCalculator_1() {
        try {
            manager.addScoreCalculator(0, new BinaryScoreCalculator());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }

    }

    /**
     * Test method <code>void addScoreCalculator(long questionType, ScoreCalculator scoreCalculator) </code>. If The
     * questionType is not a positive long, or the scoreCalculator is null, IllegalArgumentException should be thrown.
     */
    public void testAddScoreCalculator_2() {
        try {
            manager.addScoreCalculator(-1, new BinaryScoreCalculator());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }

    }

    /**
     * Test method <code>void addScoreCalculator(long questionType, ScoreCalculator scoreCalculator) </code>. If The
     * questionType is not a positive long, or the scoreCalculator is null, IllegalArgumentException should be thrown.
     */
    public void testAddScoreCalculator_3() {
        try {
            manager.addScoreCalculator(10, null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }

    }

    /**
     * Test method <code>ScoreCalculator removeScoreCalculator(long questionType) </code>. If the questionType is not
     * positive, IllegalArgumentException should be thrown.
     *
     */
    public void testRemoveScoreCalculator_1() {
        try {
            manager.removeScoreCalculator(0);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Test method <code>ScoreCalculator removeScoreCalculator(long questionType) </code>. If the questionType is not
     * positive, IllegalArgumentException should be thrown.
     *
     */
    public void testRemoveScoreCalculator_2() {
        try {
            manager.removeScoreCalculator(-1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

}
