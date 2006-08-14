/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Unit tests on "viewScorecard" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestViewScorecardAction extends BaseTestCase {
    /** Scorecard used in this test. */
    private Scorecard scorecard = null;

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
        UnitTestHelper.getInstance().clearAllScorecards();
        this.initializeScorecard();
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
        this.scorecard = null;
        UnitTestHelper.getInstance().clearAllScorecards();
    }

    /**
     * <p>
     * Initialize a scorecard.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    private void initializeScorecard() throws Exception {
        this.scorecard = ScorecardActionsHelper.buildNewScorecard();
        UnitTestHelper.getInstance().createScorecard(this.scorecard);
    }

    /**
     * <p>
     * Unit test on "viewScorecard" action, this test try to view an exist
     * scorecard.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "viewScorecard"("/viewScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testViewScorecard_ValidSid() throws Exception {
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // view the scorecard
        this.addRequestParameter("actionName", "viewScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("viewScorecard");
        this.verifyForwardPath("/viewScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        assertScorecardsEqual(this.scorecard, scorecardForm.getScorecard(),
                true);
    }

    /**
     * <p>
     * Unit test on "viewScorecard" action, this test try to view scorecard with
     * invalid sid(malformed).
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "global.error.no_such_scorecard". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testViewScorecard_InvalidSid() throws Exception {
        // view the scorecard
        this.addRequestParameter("actionName", "viewScorecard");
        this.addRequestParameter("sid", "invalid");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this
                .verifyActionErrors(new String[] { "global.error.no_such_scorecard" });
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNull("scorecardForm.scorecard is initialized.", scorecardForm
                .getScorecard());
    }

    /**
     * <p>
     * Unit test on "viewScorecard" action, this test try to view scorecard
     * without sid.
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "global.error.no_such_scorecard". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testViewScorecard_NoSid() throws Exception {
        // view the scorecard
        this.addRequestParameter("actionName", "viewScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this
                .verifyActionErrors(new String[] { "global.error.no_such_scorecard" });
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNull("scorecardForm.scorecard is initialized.", scorecardForm
                .getScorecard());
    }

    /**
     * <p>
     * Unit test on "viewScorecard" action, this test try to view scorecard with
     * invalid sid(not exist).
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "global.error.no_such_scorecard". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testViewScorecard_NonExistSid() throws Exception {
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // view the scorecard
        this.addRequestParameter("actionName", "viewScorecard");
        this.addRequestParameter("sid", (sid + 1) + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this
                .verifyActionErrors(new String[] { "global.error.no_such_scorecard" });
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNull("scorecardForm.scorecard is initialized.", scorecardForm
                .getScorecard());
    }

    /**
     * <p>
     * Unit test on "viewScorecard" action, the user is not logged in.
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "global.error.authorization". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testViewScorecard_NotLoggedIn() throws Exception {
        // logout first
        this.logout();
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // view the scorecard
        this.addRequestParameter("actionName", "viewScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this.verifyActionErrors(new String[] { "global.error.authorization" });
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNull("scorecardForm.scorecard is initialized.", scorecardForm
                .getScorecard());
    }

}
