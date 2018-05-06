/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.accuracytests;
import java.util.List;

import com.topcoder.management.review.scorecalculator.ScorecardMatrix;
import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.management.scorecard.data.Section;
import com.topcoder.util.weightedcalculator.LineItem;
import com.topcoder.util.weightedcalculator.MathMatrix;

import junit.framework.TestCase;
/**
 * Tests for DefaultScorecardMatrixBuilder class.
 * @author qiucx0161
 * @version 1.0
 */
public class TestDefaultScorecardMatrixBuilder extends TestCase {

    /**
     * A Scorecard used for test.
     */
    private Scorecard scorecard = null;

    /**
     * Question instance used for testing.
     */
    private Question question = null;

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        scorecard = new Scorecard(999990, "validator test");

        question = new Question(999991, "Is elegant APIs?", 100.0f);
        question.setDescription("here is the first des.");
        question.setGuideline("here is the first guide");

        Section section = new Section(999992, "structure design party.", 100.0f);
        section.addQuestion(question);
        Group group = new Group(999993, "Method.", 100.0f);
        group.addSection(section);

        scorecard.addGroup(group);
        scorecard.setCategory(999994);
        scorecard.setVersion("2.0");
        scorecard.setMinScore(0);
        scorecard.setMaxScore(100.0f);
        scorecard.setScorecardStatus(new ScorecardStatus(999995, "incompleted."));
        scorecard.setScorecardType(new ScorecardType(999996, "design"));

    }

    /**
     * Setup the test environment.
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests DefaultScorecardMatrixBuilder() method with accuracy state.
     */
    public void testDefaultScorecardMatrixBuilderAccuracy() {
        assertNotNull("Creating DefaultScorecardMatrixBuilder is wrong.");
    }

    /**
     * Tests buildScorecardMatrix(Scorecard scorecard) method with accuracy state.
     * @throws ScorecardStructureException to JUnit
     */
    public void testBuildScorecardMatrixAccuracy() throws ScorecardStructureException {
        ScorecardMatrix matrix = new DefaultScorecardMatrixBuilder().buildScorecardMatrix(scorecard);
        assertNotNull(matrix);
        assertTrue(matrix instanceof ScorecardMatrix);

        // the attribute of question
        assertEquals(question, matrix.getQuestion(999991));

        LineItem lineItem = matrix.getLineItem(999991);
        assertEquals("The description of question is wrong.", "here is the first des.", lineItem.getDescription());
        assertEquals("The weight of question is wrong.", 1.0, lineItem.getWeight(), 1e-9f);
        assertEquals("The max score is wrong.", 1.0, lineItem.getMaximumScore(), 1e-9f);

        MathMatrix m = matrix.getMathMatrix();
        assertNotNull(matrix.getMathMatrix());
        assertEquals("the name of matrix is wrong.", "validator test", m.getDescription());
        assertEquals("The max score of matrix is wrong.", 100.00, m.getMaximumScore(), 1e-9f);

        List items1 = m.getItems();
        assertEquals("The numbe of items1 is wrong.", 1, items1.size());

        com.topcoder.util.weightedcalculator.Section section1 = (com.topcoder.util.weightedcalculator.Section) items1.get(0);

        // The attribute of group.
        assertEquals("The description of section1 is wrong.", "Method.", section1.getDescription());
        assertEquals("The weight of section1 is wrong.", 1.0, section1.getWeight(), 1e-9f);


        // the attribute of section#group
        List items2 = section1.getItems();
        assertEquals("The numbe of items2 is wrong.", 1, items2.size());

        com.topcoder.util.weightedcalculator.Section section2 = (com.topcoder.util.weightedcalculator.Section) items2.get(0);
        assertEquals("The description of section2 is wrong.", "structure design party.", section2.getDescription());
        assertEquals("The weight of section2 is wrong.", 1.0, section2.getWeight(), 1e-9f);

        // the attribute of question##section##group
        List items3 = section2.getItems();
        assertEquals("The description of section3 is wrong.", 1, items3.size());
    }
}
