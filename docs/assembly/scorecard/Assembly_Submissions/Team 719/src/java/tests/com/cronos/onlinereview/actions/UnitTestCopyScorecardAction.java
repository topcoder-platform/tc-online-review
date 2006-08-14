/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Unit tests on "copyScorecard" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestCopyScorecardAction extends BaseTestCase {
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
     * Unit test on "copyScorecard" action, this test try to copy an exist
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
    public void testCopyScorecard_ValidSid() throws Exception {
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // copy the scorecard
        this.addRequestParameter("actionName", "copyScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        assertTrue("The scorecard name is not editable.", scorecardForm
                .isScorecardNameEditable());
        assertTrue("The scorecard version is not editable.", scorecardForm
                .isScorecardVersionEditable());
        assertTrue("The scorecard is not a newly created one.", scorecardForm
                .isNewlyCreated());
        Scorecard sc = scorecardForm.getScorecard();
        assertNotNull("scorecardForm.scorecard is not initialized.", sc);
        // increase original scorecard's major version
        String version = this.scorecard.getVersion();
        int dotIdx = version.indexOf(".");
        long major;
        if (dotIdx == -1) {
            major = Long.parseLong(version) + 1;
            this.scorecard.setVersion(major + "");
        } else {
            major = Long.parseLong(version.substring(0, dotIdx)) + 1;
            this.scorecard.setVersion(major + version.substring(dotIdx));
        }
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "copyScorecard" action, this test try to copy scorecard with
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
    public void testCopyScorecard_InvalidSid() throws Exception {
        // copy the scorecard
        this.addRequestParameter("actionName", "copyScorecard");
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
     * Unit test on "copyScorecard" action, this test try to copy scorecard
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
    public void testCopyScorecard_NoSid() throws Exception {
        // copy the scorecard
        this.addRequestParameter("actionName", "copyScorecard");
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
     * Unit test on "copyScorecard" action, this test try to copy scorecard with
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
    public void testCopyScorecard_NonExistSid() throws Exception {
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // copy the scorecard
        this.addRequestParameter("actionName", "copyScorecard");
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
     * Unit test on "copyScorecard" action, the user is not logged in.
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
    public void testCopyScorecard_NotLoggedIn() throws Exception {
        // logout first
        this.logout();
        // retrieve the sid
        long sid = UnitTestHelper.getInstance().getScorecardByNameAndVersion(
                this.scorecard.getName(), this.scorecard.getVersion()).getId();
        // copy the scorecard
        this.addRequestParameter("actionName", "copyScorecard");
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
