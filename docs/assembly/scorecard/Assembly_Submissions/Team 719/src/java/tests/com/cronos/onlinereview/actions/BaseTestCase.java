/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Group;
import com.topcoder.management.scorecard.data.Question;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.Section;

import servletunit.struts.MockStrutsTestCase;

/**
 * <p>
 * This is the base test case which provides common operations among the unit
 * test cases.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public abstract class BaseTestCase extends MockStrutsTestCase {
    /**
     * <p>
     * Set up environment.
     * </p>
     */
    public void setUp() throws Exception {
        super.setUp();
        this.login();
    }

    /**
     * <p>
     * Login to the web application.
     * </p>
     */
    protected void login() {
        this.addRequestParameter("userIdText", "1234");
        this.setRequestPathInfo("/login");
        this.actionPerform();
    }

    /**
     * <p>
     * Logout from the web application.
     * </p>
     */
    protected void logout() {
        this.setRequestPathInfo("/logout");
        this.actionPerform();
    }

    /**
     * <p>
     * Asserts two scorecards are "equal".
     * </p>
     * 
     * @param expected
     *            the expected one
     * @param actual
     *            the actual one
     * @param complete
     *            if perform a complete verification
     */
    public static void assertScorecardsEqual(Scorecard expected,
            Scorecard actual, boolean complete) {
        if (complete) {
            assertScorecardsEqual(expected, actual, false);
            assertEquals("Group numbers are different.", expected
                    .getNumberOfGroups(), actual.getNumberOfGroups());
            int n = expected.getNumberOfGroups();
            for (int i = 0; i < n; i++) {
                assertGroupsEqual(expected.getGroup(i), actual.getGroup(i),
                        true);
            }
        } else {
            assertEquals("Scorecard names are different.", expected.getName(),
                    actual.getName());
            assertEquals("Scorecard versions are different.", expected
                    .getVersion(), actual.getVersion());
            assertEquals("Project categories are different.", expected
                    .getCategory(), actual.getCategory());
            assertEquals("Scorecard types are different.", expected
                    .getScorecardType().getName(), actual.getScorecardType()
                    .getName());
            assertEquals("Scorecard statuses are different.", expected
                    .getScorecardStatus().getName(), actual
                    .getScorecardStatus().getName());
            assertEquals("Max scores are different.", expected.getMaxScore(),
                    actual.getMaxScore(), 1e-9);
            assertEquals("Min scores are different.", expected.getMinScore(),
                    actual.getMinScore(), 1e-9);
            assertEquals("IsInUse are different.", expected.isInUse(), actual
                    .isInUse());
        }
    }

    /**
     * <p>
     * Asserts two groups are "equal".
     * </p>
     * 
     * @param expected
     *            the expected one
     * @param actual
     *            the actual one
     * @param complete
     *            if perform a complete verification
     */
    public static void assertGroupsEqual(Group expected, Group actual,
            boolean complete) {
        if (complete) {
            assertGroupsEqual(expected, actual, false);
            assertEquals("Section numbers are different.", expected
                    .getNumberOfSections(), actual.getNumberOfSections());
            int n = expected.getNumberOfSections();
            for (int i = 0; i < n; i++) {
                assertSectionsEqual(expected.getSection(i), actual
                        .getSection(i), true);
            }
        } else {
            assertEquals("Group names are different.", expected.getName(),
                    actual.getName());
            assertEquals("Group weights are different.", expected.getWeight(),
                    actual.getWeight(), 1e-9);
        }
    }

    /**
     * <p>
     * Asserts two sections are "equal".
     * </p>
     * 
     * @param expected
     *            the expected one
     * @param actual
     *            the actual one
     * @param complete
     *            if perform a complete verification
     */
    public static void assertSectionsEqual(Section expected, Section actual,
            boolean complete) {
        if (complete) {
            assertSectionsEqual(expected, actual, false);
            assertEquals("Question numbers are different.", expected
                    .getNumberOfQuestions(), actual.getNumberOfQuestions());
            int n = expected.getNumberOfQuestions();
            for (int i = 0; i < n; i++) {
                assertQuestionsEqual(expected.getQuestion(i), actual
                        .getQuestion(i));
            }
        } else {
            assertEquals("Section names are different.", expected.getName(),
                    actual.getName());
            assertEquals("Section weights are different.",
                    expected.getWeight(), actual.getWeight(), 1e-9);
        }
    }

    /**
     * <p>
     * Asserts two question are "equal".
     * </p>
     * 
     * @param expected
     *            the expected one
     * @param actual
     *            the actual one
     */
    public static void assertQuestionsEqual(Question expected, Question actual) {
        assertEquals("Question names are different.", expected.getName(),
                actual.getName());
        assertEquals("Question descriptions are different.", expected
                .getDescription(), actual.getDescription());
        assertEquals("Question guidelines are different.", expected
                .getGuideline(), actual.getGuideline());
        assertEquals("Question types are different.", expected
                .getQuestionType().getName(), actual.getQuestionType()
                .getName());
        assertEquals("Question weights are different.", expected.getWeight(),
                actual.getWeight(), 1e-9);
        assertEquals("IsUploadDocuments are different.", expected
                .isUploadDocument(), actual.isUploadDocument());
        assertEquals("IsUploadRequired's are different.", expected
                .isUploadRequired(), actual.isUploadRequired());
    }
}
