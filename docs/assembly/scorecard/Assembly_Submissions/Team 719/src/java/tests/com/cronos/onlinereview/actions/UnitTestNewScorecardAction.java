/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Unit tests on "newScorecard" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestNewScorecardAction extends BaseTestCase {
    /**
     * <p>
     * Set up environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
    }

    /**
     * <p>
     * Clear the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Unit test on "newScorecard" action, this test try to create a new
     * scorecard without project type id(1 will be used as the default one).
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testNewScorecard_NoProjectTypeId() {
        // new a scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        assertScorecardsEqual(ScorecardActionsHelper.buildNewScorecard(),
                scorecardForm.getScorecard(), true);
    }

    /**
     * <p>
     * Unit test on "newScorecard" action, this test try to create a new
     * scorecard with project type id 1.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testNewScorecard_ComponetProject() {
        // new a scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.addRequestParameter("projectTypeId", "1");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        assertScorecardsEqual(ScorecardActionsHelper.buildNewScorecard(),
                scorecardForm.getScorecard(), true);
    }

    /**
     * <p>
     * Unit test on "newScorecard" action, this test try to create a new
     * scorecard with project type id 2.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testNewScorecard_ApplicationProject() {
        // new a scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.addRequestParameter("projectTypeId", "2");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        Scorecard scorecard = ScorecardActionsHelper.buildNewScorecard();
        scorecard.setCategory(ScorecardActionsHelper.getInstance()
                .getProjectCategories(2)[0].getId());
        assertScorecardsEqual(scorecard, scorecardForm.getScorecard(), true);
    }

    /**
     * <p>
     * Unit test on "newScorecard" action, this test try to create a new
     * scorecard with unknown project type id(1 will be used as default value).
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testNewScorecard_UnknownProjectTypeId() {
        // new a scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.addRequestParameter("projectTypeId", "3");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        assertScorecardsEqual(ScorecardActionsHelper.buildNewScorecard(),
                scorecardForm.getScorecard(), true);
    }

    /**
     * <p>
     * Unit test on "newScorecard" action, the user is not logged in.
     * </p>
     * <p>
     * Expected Result: 1. There should be one action error
     * "global.error.authorization". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     */
    public void testNewScorecard_NotLoggedIn() {
        // logout first
        this.logout();
        // list the scorecards
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the "scorecardList" request attribute
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this.verifyActionErrors(new String[] { "global.error.authorization" });
    }
}
