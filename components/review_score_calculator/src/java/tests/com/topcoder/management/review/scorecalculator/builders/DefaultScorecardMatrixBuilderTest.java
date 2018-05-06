/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator.builders;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.ScorecardMatrixBuilder;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

/**
 * Contains the unit tests for the DefaultScorecardMatrixBuilder class.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public class DefaultScorecardMatrixBuilderTest extends TestCase {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Member Fields

    /**
     * The instance to use for each unit test; doesn't need to be recreated because there is no state.
     */
    private final ScorecardMatrixBuilder instance = new DefaultScorecardMatrixBuilder();

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

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Creates a test suite for the tests in this test case.
     *
     * @return  A TestSuite for this test case.
     */
    public static Test suite() {
        return new TestSuite(DefaultScorecardMatrixBuilderTest.class);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // SetUp

    /**
     * Recreates the member fields before each unit test.
     */
    protected void setUp() {
        question = new Question();
        question.setDescription("How are you?");
        question.setId(1);
        question.setWeight(100);

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
        scorecard.setId(4);
        scorecard.addGroup(group);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // buildScorecardMatrix Tests

    /**
     * Ensures that the buildScorecardMatrix method throws an IllegalArgumentException when given a null
     * scorecard.
     *
     * @throws  ScorecardStructureException
     *          An unknown error occurred.
     */
    public void testBuildThrowsOnNullScorecard() throws ScorecardStructureException {
        try {
            instance.buildScorecardMatrix(null);
            fail("An IllegalArgumentException is expected when the scorecard is null.");
        } catch (IllegalArgumentException ex) {
            // Good!
        }
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a null name.
     */
    public void testBuildThrowsOnNullScorecardName() {
        scorecard.resetName();
        checkBuildThrowsSSE("the scorecard's name is null.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with an empty name.
     */
    public void testBuildThrowsOnEmptyScorecardName() {
        scorecard.setName("");
        checkBuildThrowsSSE("the scorecard's name is an empty string.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with an empty groups list.
     */
    public void testBuildThrowsOnEmptyGroupsList() {
        scorecard.clearGroups();
        checkBuildThrowsSSE("the scorecard has no groups.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with an empty sections list.
     */
    public void testBuildThrowsOnEmptySectionsList() {
        group.clearSections();
        checkBuildThrowsSSE("the scorecard has a group with no sections.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with an empty questions list.
     */
    public void testBuildThrowsOnEmptyQuestionsList() {
        section.clearQuestions();
        checkBuildThrowsSSE("the scorecard has a group with a section with no questions.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a group with a null name.
     */
    public void testBuildThrowsOnNullGroupName() {
        group.resetName();
        checkBuildThrowsSSE("the scorecard has a group with a null name.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a group with an empty name.
     */
    public void testBuildThrowsOnEmptyGroupName() {
        group.setName("");
        checkBuildThrowsSSE("the scorecard has a group with an empty name.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a section with a null name.
     */
    public void testBuildThrowsOnNullSectionName() {
        section.resetName();
        checkBuildThrowsSSE("the scorecard has a group with a section with a null name.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a section with an empty name.
     */
    public void testBuildThrowsOnEmptySectionName() {
        section.setName("");
        checkBuildThrowsSSE("the scorecard has a group with a section with an empty name.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with two questions with the same id.
     */
    public void testBuildThrowsOnDuplicateQuestionId() {
        Question question2 = new Question();
        question2.setDescription(question.getDescription());
        question2.setId(question.getId());
        question2.setWeight(question.getWeight());

        section.addQuestion(question2);

        checkBuildThrowsSSE("the scorecard has two questions with the same id.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a question with a null name.
     */
    public void testBuildThrowsOnNullQuestionDescription() {
        question.resetDescription();
        checkBuildThrowsSSE("the scorecard has a group with a section with a question with a null description.");
    }

    /**
     * Ensures that the buildScorecardMatrix method throws a ScorecardStructureException when given a scorecard
     * with a question with an empty name.
     */
    public void testBuildThrowsOnEmptyQuestionDescription() {
        question.setDescription("");
        checkBuildThrowsSSE("the scorecard has a group with a section with a question with an empty description.");
    }

    /**
     * Helper method to check that the buildScorecardMatrix throws a ScorecardStructureException with the current
     * member fields.
     *
     * @param   message
     *          The message to use in the error message if the unit test fails.
     */
    private void checkBuildThrowsSSE(String message) {
        try {
            instance.buildScorecardMatrix(scorecard);
            fail("A ScorecardStructureException is expected when " + message);
        } catch (ScorecardStructureException ex) {
            // Good!
        }
    }

    /**
     * <p>
     * Ensures that the buildScorecardMatrix builds the matrix the way we want it.
     * </p>
     *
     * <p>
     * There is no point in breaking this test case down into smaller test cases because each one would rely on
     * all the others having passed first.
     * </p>
     *
     * @throws  ScorecardStructureException
     *          An unknown error occurred.
     */
    public void testBuildAddedMatrix() throws ScorecardStructureException {
        // Build the scorecard matrix.
        ScorecardMatrix scMatrix = instance.buildScorecardMatrix(scorecard);

        // Check the MathMatrix.
        MathMatrix matrix = scMatrix.getMathMatrix();
        assertEquals("The matrix's description is incorrect.", scorecard.getName(), matrix.getDescription());
        assertEquals("The matrix's maximum score is incorrect.", 100.0, matrix.getMaximumScore(), 1e-9f);

        // Check the groups.
        List scorecardItems = instance.buildScorecardMatrix(scorecard).getMathMatrix().getItems();
        com.topcoder.util.weightedcalculator.Section groupSection =
            (com.topcoder.util.weightedcalculator.Section) scorecardItems.get(0);

        assertEquals("There should only be one item underneath the scorecard.", 1, scorecardItems.size());
        assertEquals("The group item's description is incorrect.", group.getName(), groupSection.getDescription());
        assertEquals("The group item's weight is incorrect.", 1.0, groupSection.getWeight(), 1e-9f);

        // Check the sections.
        List groupItems = groupSection.getItems();
        com.topcoder.util.weightedcalculator.Section sectionSection =
            (com.topcoder.util.weightedcalculator.Section) groupItems.get(0);

        assertEquals("There should only be one item underneath the group.", 1, groupItems.size());
        assertEquals("The section item's description is incorrect.",
                section.getName(), sectionSection.getDescription());
        assertEquals("The section item's weight is incorrect.", 1.0, sectionSection.getWeight(), 1e-9f);

        // Check the questions.
        List sectionItems = sectionSection.getItems();
        LineItem questionLineItem = (LineItem) sectionItems.get(0);

        assertEquals("There should only be one item underneath the section.", 1, sectionItems.size());
        assertEquals("The question item's description is incorrect.",
                question.getDescription(), questionLineItem.getDescription());
        assertEquals("The question item's weight is incorrect.", 1.0, questionLineItem.getWeight(), 1e-9f);
        assertEquals("The question item's maximum score is incorrect.",
                1.0, questionLineItem.getMaximumScore(), 1e-9f);
    }

    /**
     * Ensures that the buildScorecardMatrix adds a line item for the question id.
     *
     * @throws  ScorecardStructureException
     *          An unknown error occurred.
     */
    public void testBuildAddedLineItemEntry() throws ScorecardStructureException {
        ScorecardMatrix scMatrix = instance.buildScorecardMatrix(scorecard);
        LineItem lineItem = scMatrix.getLineItem(question.getId());

        assertNotNull("The line item was not added.", lineItem);
        assertEquals("The line item's description is incorrect.",
                question.getDescription(), lineItem.getDescription());
        assertEquals("The line item's weight is incorrect.", 1.0, lineItem.getWeight(), 1e-9f);
        assertEquals("The line item's maximum score is incorrect.", 1.0, lineItem.getMaximumScore(), 1e-9f);
    }

    /**
     * Ensures that the buildScorecardMatrix adds a questin for the question id.
     *
     * @throws  ScorecardStructureException
     *          An unknown error occurred.
     */
    public void testBuildAddedQuestionEntry() throws ScorecardStructureException {
        assertSame(
                "The question was not added.",
                question, instance.buildScorecardMatrix(scorecard).getQuestion(question.getId()));
    }
}
