/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Unit tests on "editScorecard" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestEditScorecardAction extends BaseTestCase {
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
    private void initializeScorecard(boolean active, boolean inUse)
            throws Exception {
        this.scorecard = ScorecardActionsHelper.buildNewScorecard();
        if (active) {
            this.scorecard.setScorecardStatus(ScorecardActionsHelper
                    .getInstance().getScorecardStatus("Active"));
        }
        this.scorecard.setInUse(inUse);
        UnitTestHelper.getInstance().createScorecard(this.scorecard);
    }

    /**
     * <p>
     * Unit test on "editScorecard" action, this test try to edit an exist
     * scorecard.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testEditScorecard_ValidSidCanEdit() throws Exception {
        // init the scorecard
        this.initializeScorecard(false, false);
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertFalse("The scorecard name is editable.", scorecardForm
                .isScorecardNameEditable());
        assertFalse("The scorecard version is editable.", scorecardForm
                .isScorecardVersionEditable());
        assertFalse("The scorecard is a newly created one.", scorecardForm
                .isNewlyCreated());
        assertFalse("The scorecard is copied from another one.", scorecardForm
                .isCopy());
        assertNotNull("scorecardForm.scorecard is not initialized.",
                scorecardForm.getScorecard());
        assertScorecardsEqual(this.scorecard, scorecardForm.getScorecard(),
                true);
    }

    /**
     * <p>
     * Unit test on "editScorecard" action, this test try to edit an exist
     * scorecard, the scorecard is active.
     * </p>
     * <p>
     * Expected Result: 1. There should be action errors
     * "editScorecard.error.scorecard_is_active". 2. The result forward should
     * be "failure"("/error.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testEditScorecard_ValidSidActive() throws Exception {
        // init the scorecard
        this.initializeScorecard(true, false);
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_is_active" });
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertNull("scorecardForm.scorecard is initialized.", scorecardForm
                .getScorecard());
    }

    /**
     * <p>
     * Unit test on "editScorecard" action, this test try to edit scorecard with
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
    public void testEditScorecard_InvalidSid() throws Exception {
        // init the scorecard
        this.initializeScorecard(false, false);
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
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
     * Unit test on "editScorecard" action, this test try to edit scorecard
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
    public void testEditScorecard_NoSid() throws Exception {
        // init the scorecard
        this.initializeScorecard(false, false);
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
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
     * Unit test on "editScorecard" action, this test try to edit scorecard with
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
    public void testEditScorecard_NonExistSid() throws Exception {
        // init the scorecard
        this.initializeScorecard(false, false);
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
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
     * Unit test on "editScorecard" action, the user is not logged in.
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
    public void testEditScorecard_NotLoggedIn() throws Exception {
        // init the scorecard
        this.initializeScorecard(false, false);
        // logout first
        this.logout();
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
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
