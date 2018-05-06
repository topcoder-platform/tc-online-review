/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.scorecalculator.failuretests;

import com.topcoder.management.review.scorecalculator.ScorecardStructureException;
import com.topcoder.management.review.scorecalculator.builders.DefaultScorecardMatrixBuilder;
import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;

import junit.framework.TestCase;

/**
 * Failure test cases for class <code>DefaultScorecardMatrixBuilder </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class FailureTestForDefaultScorecardMatrixBuilder extends TestCase {

    /**
     * Represents the DefaultScorecardMatrixBuilder instance for test.
     */
    private static final DefaultScorecardMatrixBuilder builder = new DefaultScorecardMatrixBuilder();

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is null,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_1() throws Exception {
        try {
            builder.buildScorecardMatrix(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard's name is null,
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_2() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");
        card.resetName();

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (description for question is empty)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_3() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription("");
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (description for question is null)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_4() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);

        Question question1 = new Question();
        question1.setDescription("question1");
        question1.setId(1);
        question1.setWeight(40);

        Question question2 = new Question();
        question2.setDescription(null);
        question2.setId(2);
        question2.setWeight(60);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (group name is null)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_5() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
        group.setWeight(100);
        group.resetName();

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
        question2.setWeight(100);

        section.addQuestion(question1);
        section.addQuestion(question2);

        group.addSection(section);

        card.addGroup(group);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (section with no question)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_6() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
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
        question2.setWeight(100);

        group.addSection(section);

        card.addGroup(group);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (group with no sections)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_8() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
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
        question2.setWeight(100);

        card.addGroup(group);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (scorecard with no group)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_9() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
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
        question2.setWeight(100);

        try {
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (section name is empty)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_10() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
        group.setWeight(100);

        Section section = new Section();
        section.setName("");
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
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (group name is empty)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_11() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("");
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
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }


    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (group name is null)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_12() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("name");
        group.resetName();
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
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

    /**
     * Failure test for method <code>buildScorecardMatrix(Scorecard scorecard) </code>. If Scorecard is not valid,
     * (section name is null)
     * ScorecardStructureException should be thrown.
     *
     * @throws Exception
     *             to junit.
     */
    public void testBuildScorecardMatrix_13() throws Exception {
        Scorecard card = new Scorecard();
        card.setName("card");

        Group group = new Group();
        group.setName("group");
        group.setWeight(100);

        Section section = new Section();
        section.setName("section");
        section.setWeight(100);
        section.resetName();

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
            builder.buildScorecardMatrix(card);
            fail("ScorecardStructureException is expected.");
        } catch (ScorecardStructureException e) {
            // Ok.
        }
    }

}
