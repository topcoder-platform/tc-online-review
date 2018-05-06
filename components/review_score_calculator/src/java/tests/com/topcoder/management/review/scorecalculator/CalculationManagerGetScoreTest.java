/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

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

/**
 * Contains the getScore method unit tests for the CalculationManager class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class CalculationManagerGetScoreTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The calculation manager to use for unit testing.
     */
    private CalculationManager instance = null;

    /**
     * The scorecard to use for unit testing.
     */
    private Scorecard scorecard = null;

    /**
     * The group to use for unit testing.
     */
    private Group group = null;

    /**
     * The section to use for unit testing.
     */
    private Section section = null;

    /**
     * The question to use for unit testing.
     */
    private Question question = null;

    /**
     * The question type to use for unit testing.
     */
    private QuestionType questionType = null;

    /**
     * The item to use for unit testing.
     */
    private Item item = null;

    /**
     * The review to use for unit testing.
     */
    private Review review = null;

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(CalculationManagerGetScoreTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp

    /**
     * Recreates the member fields before each unit test.
     */
    protected void setUp() {
        instance = new CalculationManager(
                new long[] {5, 6},
                new ScoreCalculator[] {new ScaledScoreCalculator(4), new BinaryScoreCalculator()},
                new DefaultScorecardMatrixBuilder(),
                true);

        questionType = new QuestionType();
        questionType.setId(5);

        question = new Question();
        question.setDescription("How are you?");
        question.setId(1);
        question.setWeight(100);
        question.setQuestionType(questionType);

        section = new Section();
        section.setName("Section");
        section.setId(2);
        section.setWeight(75);
        section.addQuestion(question);

        group = new Group();
        group.setName("Group");
        group.setId(3);
        group.setWeight(50);
        group.addSection(section);

        scorecard = new Scorecard();
        scorecard.setName("Scorecard");
        scorecard.setVersion("1.0");
        scorecard.setId(4);
        scorecard.addGroup(group);

        item = new Item();
        item.setAnswer("3/4");
        item.setQuestion(1);

        review = new Review();
        review.addItem(item);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // getScore Tests

    /**
     * Ensures that the getScore method throws an IllegalArgumentException when given a null scorecard.
     *
     * @throws  CalculationException
     *          An unknown error occurred.
     */
    public void testGetScoreThrowsOnNullScorecard() throws CalculationException {
        try {
            instance.getScore(null, review);
            fail("An IllegalArgumentException is expected when given a null scorecard.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the getScore method throws an IllegalArgumentException when given a null review.
     *
     * @throws  CalculationException
     *          An unknown error occurred.
     */
    public void testGetScoreThrowsOnNullReview() throws CalculationException {
        try {
            instance.getScore(null, review);
            fail("An IllegalArgumentException is expected when given a null scorecard.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with a null
     * name.
     */
    public void testGetScoreThrowsOnNullScorecardNameWithCache() {
        scorecard.resetName();
        checkGetScoreThrowsSSE("a scorecard with a null name.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with an empty
     * name.
     */
    public void testGetScoreThrowsOnEmptyScorecardNameWithCache() {
        scorecard.setName("");
        checkGetScoreThrowsSSE("a scorecard with an empty name.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with a blank
     * name.
     */
    public void testGetScoreThrowsOnBlankScorecardNameWithCache() {
        scorecard.setName(" ");
        checkGetScoreThrowsSSE("a scorecard with a blank name.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with a null
     * version.
     */
    public void testGetScoreThrowsOnNullScorecardVersionWithCache() {
        scorecard.resetVersion();
        checkGetScoreThrowsSSE("a scorecard with a null version.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with an empty
     * version.
     */
    public void testGetScoreThrowsOnEmptyScorecardVersionWithCache() {
        scorecard.setVersion("");
        checkGetScoreThrowsSSE("a scorecard with an empty version.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a scorecard with a blank
     * version.
     */
    public void testGetScoreThrowsOnBlankScorecardVersionWithCache() {
        scorecard.setVersion(" ");
        checkGetScoreThrowsSSE("a scorecard with a blank version.");
    }

    /**
     * Ensures that the getScore method throws a ScorecardStructureException when given a review that contains an
     * item with a question type that has no calculator for it.
     */
    public void testGetScoreThrowsOnNoCalculatorForQuestionTypeId() {
        questionType.setId(255);
        checkGetScoreThrowsSSE("a review that has an item with a question whose type id has no calculator.");
    }

    /**
     * Helper method to check that the getScore method throws a ScorecardStructureException when given the current
     * member fields.
     *
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void checkGetScoreThrowsSSE(String message) {
        try {
            instance.getScore(scorecard, review);
            fail("An ScorecardStructureException was expected when given " + message);
        } catch (ScorecardStructureException ex) {
            // Good!
        } catch (CalculationException ex) {
            fail(ex.toString());
        }
    }

    /**
     * Ensures that the getScore method throws a ReviewStructureException when given a review with no items.
     */
    public void testGetScoreThrowsOnReviewHasNoItems() {
        review.clearItems();
        checkGetScoreThrowsRSE("a review with no items.");
    }

    /**
     * Ensures that the getScore method throws a ReviewStructureException when given a review that contains two
     * items for the same question.
     */
    public void testGetScoreThrowsOnReviewItemHasDuplicateQuestion() {
        Item item2 = new Item();
        item2.setAnswer("1/4");
        item2.setQuestion(1);
        review.addItem(item2);

        checkGetScoreThrowsRSE("a review that has two items with the same question ids.");
    }

    /**
     * Ensures that the getScore method throws a ReviewStructureException when given a review that contains an
     * item with an uninitialized question type.
     */
    public void testGetScoreThrowsOnReviewItemHasUninitializedQuestionType() {
        question.resetQuestionType();
        checkGetScoreThrowsRSE("a review that has an item with a question whose type is uninitailized.");
    }

    /**
     * Helper method to check that the getScore method throws a ReviewStructureException when given the current
     * member fields.
     *
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void checkGetScoreThrowsRSE(String message) {
        try {
            instance.getScore(scorecard, review);
            fail("An ReviewStructureException was expected when given " + message);
        } catch (ReviewStructureException ex) {
            // Good!
        } catch (CalculationException ex) {
            fail(ex.toString());
        }
    }

    /**
     * Ensures that a simple getScore method call (with one question!) works properly.
     *
     * @throws  CalculationException
     *          An unknown error occurred.
     */
    public void testGetScoreWorksSimple() throws CalculationException {
        assertEquals(
                "Since there is one question, and the answer was 3/4, the score should be 75.",
                75.0f, instance.getScore(scorecard, review), 1e-9f);
    }

    /**
     * <p>
     * Ensures that the getScore method works for some real world example.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22404237&uid=275640
     * </p>
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void testGetScoreWorksForDesignScreening1() throws CalculationException {
        scorecard = createDesignScreeningScorecard();
        review = createDesignScreeningReview1();

        assertEquals(
                "The answer did not match what the TopCoder website says the answer should be.",
                90.22f, instance.getScore(scorecard, review), 0.01f);
    }

    /**
     * <p>
     * Ensures that the getScore method works for some real world example.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22404237&uid=275071
     * </p>
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void testGetScoreWorksForDesignScreening2() throws CalculationException {
        scorecard = createDesignScreeningScorecard();
        review = createDesignScreeningReview2();

        assertEquals(
                "The answer did not match what the TopCoder website says the answer should be.",
                90.95f, instance.getScore(scorecard, review), 0.01f);
    }

    /**
     * <p>
     * Ensures that the getScore method works for some real world example.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22386965&uid=22025273
     * </p>
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void testGetScoreWorksForDevelopmentScreening1() throws CalculationException {
        scorecard = createDevelopmentScreeningScorecard();
        review = createDevelopmentScreeningReview1();

        assertEquals(
                "The answer did not match what the TopCoder website says the answer should be.",
                46.20f, instance.getScore(scorecard, review), 0.01f);
    }

    /**
     * <p>
     * Ensures that the getScore method works for some real world example.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22386965&uid=14845140
     * </p>
     *
     * @throws  CalculationException
     *          An unknown error occured.
     */
    public void testGetScoreWorksForDevelopmentScreening2() throws CalculationException {
        scorecard = createDevelopmentScreeningScorecard();
        review = createDevelopmentScreeningReview2();

        assertEquals(
                "The answer did not match what the TopCoder website says the answer should be.",
                82.55f, instance.getScore(scorecard, review), 0.01f);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Scorecard Creators

    /**
     * Helper method to create and add a list of questions (an array of string arrys, which contain the id,
     * weight, and description in that order) to the given section.
     *
     * @param   questions
     *          An array of String arrays that represent a Question.
     * @param   section
     *          The section to add the questions to.
     */
    private void createAndAddQuestions(String[][] questions, Section section) {
        for (int i = 0; i < questions.length; ++i) {
            Question q = new Question();
            q.setId(Long.parseLong(questions[i][0]));
            q.setWeight(Float.parseFloat(questions[i][1]));
            q.setDescription(questions[i][2]);
            q.setQuestionType(questionType);

            section.addQuestion(q);
        }
    }

    /**
     * Creates and returns the Class Definition section.
     *
     * @return  The Class Definition section
     */
    private Section createClassDefinitionSection() {
        String[][] questions = {
            {"11", "10", "Provides a descriptive overview of class usage..."},
            {"12", "10", "Where applicable sub-packages have been created..."},
            {"13", "20", "Class scope properly matches class usage..."},
            {"14", "20", "Proper and effective use of inheritance and..."},
            {"15", "20", "Interfaces are used appropriately..."},
            {"16", "10", "Suitable constructors are defined for the component..."},
            {"17", "10", "Class modifiers such as final, static, abstract..."}
        };

        Section result = new Section();
        result.setId(1);
        result.setWeight(30);
        result.setName("Class Definition");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Variable Definition section.
     *
     * @return  The Variable Definition section.
     */
    private Section createVariableDefinitionSection() {
        String[][] questions = {
            {"21", "33", "Variable scope is correctly defined..."},
            {"22", "34", "Type assignments are defined with respect to..."},
            {"23", "33", "Variable details (scope, type, name, description)..."}
        };

        Section result = new Section();
        result.setId(2);
        result.setWeight(10);
        result.setName("Variable Definition");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Method Definition section.
     *
     * @return  The Method Definition section.
     */
    private Section createMethodDefinitionSection() {
        String[][] questions = {
            {"31", "15", "Method scope is correctly defined..."},
            {"32", "15", "All exceptions are properly handled and thrown..."},
            {"33", "14", "Modifiers are properly used where applicable..."},
            {"34", "14", "Return types are appropriately defined..."},
            {"35", "14", "Method arguments are appropriately defined..."},
            {"36", "14", "The required API as stated in the requirements..."},
            {"37", "14", "Method details (scope, type, name, description)..."}
        };

        Section result = new Section();
        result.setId(3);
        result.setWeight(15);
        result.setName("Method Definition");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Use Case Diagrams section.
     *
     * @return  The Use Case Diagrams section.
     */
    private Section createUseCaseDiagramsSection() {
        String[][] questions = {
            {"41", "50", "The use case diagrams reflect the functionality..."}
        };

        Section result = new Section();
        result.setId(4);
        result.setWeight(35);
        result.setName("Use Case Diagrams");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Sequence Diagrams section.
     *
     * @return  The Sequence Diagrams section.
     */
    private Section createSequenceDiagramsSection() {
        String[][] questions = {
            {"51", "50", "At least one sequence diagram exists for each non-trivial..."}
        };

        Section result = new Section();
        result.setId(5);
        result.setWeight(35);
        result.setName("Sequence Diagrams");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Anonymity section.
     *
     * @return  The Anonymity section.
     */
    private Section createDesignAnonymitySection() {
        String[][] questions = {
            {"61", "100", "The submission does not contain any information..."}
        };

        Section result = new Section();
        result.setId(6);
        result.setWeight(10);
        result.setName("Anonymity");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Overall section.
     *
     * @return  The Overall section.
     */
    private Section createDesignOverallSection() {
        String[][] questions = {
            {"71", "100", "This submission is worthy of being reviewed..."}
        };

        Section result = new Section();
        result.setId(7);
        result.setWeight(50);
        result.setName("Overall");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Create and return the Design Screening scorecard.
     *
     * @return  The Design Screening scorecard.
     */
    private Scorecard createDesignScreeningScorecard() {
        // Note that any 0-weighted groups were removed.
        Group g = new Group();
        g.setId(1);
        g.setName("Design Review");
        g.setWeight(100);
        g.addSection(createClassDefinitionSection());
        g.addSection(createVariableDefinitionSection());
        g.addSection(createMethodDefinitionSection());
        g.addSection(createUseCaseDiagramsSection());
        g.addSection(createSequenceDiagramsSection());
        g.addSection(createDesignAnonymitySection());
        g.addSection(createDesignOverallSection());

        Scorecard s = new Scorecard();
        s.setId(1);
        s.setName("Design Screening Scorecard");
        s.setVersion("1.0");
        s.addGroup(g);

        return s;
    }

    /**
     * Creates and returns the Documentation section.
     *
     * @return  The Documentation section.
     */
    private Section createDocumentationSection() {
        String[][] questions = {
            {"11", "34", "All public methods are clearly comment..."},
            {"12", "33", "Required tags are included..."},
            {"13", "33", "Copyright tag is populated..."},
        };

        Section result = new Section();
        result.setId(1);
        result.setWeight(20);
        result.setName("Documentation");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Standards section.
     *
     * @return  The Standards section.
     */
    private Section createStandardsSection() {
        String[][] questions = {
            {"21", "50", "Adheres to coding standards..."},
            {"22", "50", "Code uses a 4 space indentation (not a tab)..."},
        };

        Section result = new Section();
        result.setId(2);
        result.setWeight(10);
        result.setName("Standards");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Design Adaptation section.
     *
     * @return  The Design Adaptation section.
     */
    private Section createDesignAdaptationSection() {
        String[][] questions = {
            {"31", "34", "All class definitions found in the class diagram..."},
            {"32", "33", "All method definitions found in the class diagram..."},
            {"33", "33", "All variable definitions found in the class diagram..."},
        };

        Section result = new Section();
        result.setId(3);
        result.setWeight(10);
        result.setName("Design Adaptation");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Compatability section.
     *
     * @return  The Compatability section.
     */
    private Section createCompatabilitySection() {
        String[][] questions = {
            {"41", "5", "Submission compiles against required targets..."},
        };

        Section result = new Section();
        result.setId(4);
        result.setWeight(5);
        result.setName("Compatability");

        createAndAddQuestions(questions, result);

        QuestionType binaryQuestionType = new QuestionType();
        binaryQuestionType.setId(6);
        result.getAllQuestions()[0].setQuestionType(binaryQuestionType);

        return result;
    }

    /**
     * Creates and returns the Test Cases section.
     *
     * @return  The Test Cases section.
     */
    private Section createTestCasesSection() {
        String[][] questions = {
            {"51", "90", "Unit test cases exists for all public methods in the design..."},
            {"52", "10", "The UnitTests source file calls each unit test..."},
        };

        Section result = new Section();
        result.setId(5);
        result.setWeight(40);
        result.setName("Test Cases");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Test Case Results section.
     *
     * @return  The Test Case Results section.
     */
    private Section createTestCaseResultsSection() {
        String[][] questions = {
            {"61", "100", "The submission passes the unit tests submitted by the developer..."},
        };

        Section result = new Section();
        result.setId(6);
        result.setWeight(35);
        result.setName("Test Case Results");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Anonymity section.
     *
     * @return  The Anonymity section.
     */
    private Section createDevAnonymitySection() {
        String[][] questions = {
            {"71", "1", "The submission does not contain any information identifying the developer..."},
        };

        Section result = new Section();
        result.setId(7);
        result.setWeight(5);
        result.setName("Anonymity");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Creates and returns the Overall section.
     *
     * @return  The Overall section.
     */
    private Section createDevOverallSection() {
        String[][] questions = {
            {"81", "100", "The submission is worthy of being reviewed..."},
        };

        Section result = new Section();
        result.setId(8);
        result.setWeight(75);
        result.setName("Overall");

        createAndAddQuestions(questions, result);

        return result;
    }

    /**
     * Create and return the Development Screening scorecard.
     *
     * @return  The Development Screening scorecard.
     */
    private Scorecard createDevelopmentScreeningScorecard() {
        // Note that any 0-weighted groups were removed.
        Group g = new Group();
        g.setId(1);
        g.setName("Development Review");
        g.setWeight(100);
        g.addSection(createDocumentationSection());
        g.addSection(createStandardsSection());
        g.addSection(createDesignAdaptationSection());
        g.addSection(createCompatabilitySection());
        g.addSection(createTestCasesSection());
        g.addSection(createTestCaseResultsSection());
        g.addSection(createDevAnonymitySection());
        g.addSection(createDevOverallSection());

        Scorecard s = new Scorecard();
        s.setId(1);
        s.setName("Devleopment Screening Scorecard");
        s.setVersion("1.0");
        s.addGroup(g);

        return s;
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Review Creators

    /**
     * Helper method to create and add a list of review items (an array of string arrys, which contain the id,
     * question id, and answer in that order) to the given review.
     *
     * @param   items
     *          An array of String arrays that represent an Item.
     * @param   review
     *          The review to add the items to.
     */
    private void createAndAddItems(String[][] items, Review review) {
        for (int i = 0; i < items.length; ++i) {
            Item itm = new Item();
            itm.setId(Long.parseLong(items[i][0]));
            itm.setQuestion(Long.parseLong(items[i][1]));
            itm.setAnswer(items[i][2]);

            review.addItem(itm);
        }
    }

    /**
     * <p>
     * Creates and returns a review of the screening results.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22404237&uid=275640
     * </p>
     *
     * @return  A review screening of the URL listed above.
     */
    private Review createDesignScreeningReview1() {
        String[][] items = {
            {"1", "11", "4"},
            {"2", "12", "4"},
            {"3", "13", "4"},
            {"4", "14", "3"},
            {"5", "15", "4"},
            {"6", "16", "4"},
            {"7", "17", "4"},
            {"8", "21", "2"},
            {"9", "22", "4"},
            {"10", "23", "3"},
            {"11", "31", "4"},
            {"12", "32", "3"},
            {"13", "33", "3"},
            {"14", "34", "4"},
            {"15", "35", "3"},
            {"16", "36", "4"},
            {"17", "37", "4"},
            {"18", "41", "4"},
            {"19", "51", "4"},
            {"20", "61", "4"},
            {"21", "71", "3"},
        };

        Review result = new Review();
        createAndAddItems(items, result);

        return result;
    }

    /**
     * <p>
     * Creates and returns a review of the screening results.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22404237&uid=275071
     * </p>
     *
     * @return  A review screening of the URL listed above.
     */
    private Review createDesignScreeningReview2() {
        String[][] items = {
            {"1", "11", "4"},
            {"2", "12", "2"},
            {"3", "13", "4"},
            {"4", "14", "4"},
            {"5", "15", "4"},
            {"6", "16", "4"},
            {"7", "17", "4"},
            {"8", "21", "4"},
            {"9", "22", "4"},
            {"10", "23", "2"},
            {"11", "31", "4"},
            {"12", "32", "3"},
            {"13", "33", "3"},
            {"14", "34", "4"},
            {"15", "35", "4"},
            {"16", "36", "4"},
            {"17", "37", "4"},
            {"18", "41", "4"},
            {"19", "51", "4"},
            {"20", "61", "4"},
            {"21", "71", "3"},
        };

        Review result = new Review();
        createAndAddItems(items, result);

        return result;
    }

    /**
     * <p>
     * Creates and returns a review of the screening results.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22386965&uid=22025273
     * </p>
     *
     * @return  A review screening of the URL listed above.
     */
    private Review createDevelopmentScreeningReview1() {
        String[][] items = {
            {"1", "11", "3"},
            {"2", "12", "2"},
            {"3", "13", "1"},
            {"4", "21", "2"},
            {"5", "22", "4"},
            {"6", "31", "4"},
            {"7", "32", "2"},
            {"8", "33", "4"},
            {"9", "41", "Yes"},
            {"10", "51", "2"},
            {"11", "52", "1"},
            {"12", "61", "0/162"},
            {"13", "71", "4"},
            {"14", "81", "2"},
        };

        Review result = new Review();
        createAndAddItems(items, result);

        return result;
    }

    /**
     * <p>
     * Creates and returns a review of the screening results.
     * </p>
     *
     * <p>
     * Using: http://www.topcoder.com/tc?module=ScorecardDetails&pj=22386965&uid=14845140
     * </p>
     *
     * @return  A review screening of the URL listed above.
     */
    private Review createDevelopmentScreeningReview2() {
        String[][] items = {
            {"1", "11", "3"},
            {"2", "12", "2"},
            {"3", "13", "4"},
            {"4", "21", "3"},
            {"5", "22", "4"},
            {"6", "31", "4"},
            {"7", "32", "3"},
            {"8", "33", "4"},
            {"9", "41", "Yes"},
            {"10", "51", "3"},
            {"11", "52", "4"},
            {"12", "61", "415/416"},
            {"13", "71", "4"},
            {"14", "81", "3"},
        };

        Review result = new Review();
        createAndAddItems(items, result);

        return result;
    }
}
