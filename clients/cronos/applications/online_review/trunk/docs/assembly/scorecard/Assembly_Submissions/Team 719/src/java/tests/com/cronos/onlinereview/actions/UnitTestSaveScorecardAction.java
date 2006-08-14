/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * Unit tests on "saveScorecard" action.
 * </p>
 * <p>
 * This is the most complicated test case since "save scorecard" action involves
 * form validation and many other core business operations.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestSaveScorecardAction extends BaseTestCase {
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
    private void initializeScorecard() throws Exception {
        this.scorecard = ScorecardActionsHelper.buildNewScorecard();
        UnitTestHelper.getInstance().createScorecard(this.scorecard);
    }

    /**
     * <p>
     * Unit test on "doAddGroup" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. A new group
     * with one section and one question will be added to the right position.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoAddGroup() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doAddGroup"
        scorecardForm.setOperation("doAddGroup");
        // set position
        int gIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.insertGroup(ScorecardActionsHelper.buildNewGroup(),
                gIdx + 1);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doRemoveGroup" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The group at
     * the given position should be removed.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoRemoveGroup() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doRemoveGroup"
        scorecardForm.setOperation("doRemoveGroup");
        // set position
        int gIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.removeGroup(gIdx);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doAddSection" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. A new section
     * with one question will be added to the right position.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoAddSection() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doAddSection"
        scorecardForm.setOperation("doAddSection");
        // set position
        int gIdx = 0, sIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        scorecardForm.setSectionIndex(sIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.getGroup(gIdx).insertSection(
                ScorecardActionsHelper.buildNewSection(), sIdx + 1);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doRemoveSection" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The section
     * at the given position should be removed.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoRemoveSection() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doRemoveSection"
        scorecardForm.setOperation("doRemoveSection");
        // set position
        int gIdx = 0, sIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        scorecardForm.setSectionIndex(sIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.getGroup(gIdx).removeSection(sIdx);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doAddQuestion" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. A new
     * question will be added to the right position.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoAddQuestion() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doAddQuestion"
        scorecardForm.setOperation("doAddQuestion");
        // set position
        int gIdx = 0, sIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        scorecardForm.setSectionIndex(sIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.getGroup(gIdx).getSection(sIdx).addQuestion(
                ScorecardActionsHelper.buildNewQuestion());
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doRemoveQuestion" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The section
     * at the given position should be removed.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoRemoveQuestion() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doRemoveQuestion"
        scorecardForm.setOperation("doRemoveQuestion");
        // set position
        int gIdx = 0, sIdx = 0, qIdx = 0;
        scorecardForm.setGroupIndex(gIdx);
        scorecardForm.setSectionIndex(sIdx);
        scorecardForm.setQuestionIndex(qIdx);
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // verify the scorecardForm
        scorecardForm = (ScorecardForm) this.getActionForm();
        Scorecard sc = scorecardForm.getScorecard();
        this.scorecard.getGroup(gIdx).getSection(sIdx).removeQuestion(qIdx);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doFinish" operation.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "listScorecards"("/listScorecards.jsp"). 3. The
     * scorecard should be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the scorecard name
        String name = "new name";
        scorecardForm.getScorecard().setName(name);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("listScorecards");
        this.verifyForwardPath("/listScorecards.jsp");
        this.verifyNoActionErrors();
        // verify the saved scorecard
        this.scorecard.setName(name);
        this.increaseMinorVersion(this.scorecard);
        Scorecard sc = UnitTestHelper.getInstance().getScorecard(sid);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard name in the action form
     * is empty.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_name.required"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptyScorecardName() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the scorecard name
        String name = "";
        scorecardForm.getScorecard().setName(name);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_name.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not updated
        Scorecard sc = UnitTestHelper.getInstance().getScorecard(sid);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard name in the action form
     * is longer than 64.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_name.length"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_ScorecardNameTooLong() throws Exception {
        this.initializeScorecard();
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
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the scorecard name
        String name = "";
        for (int i = 0; i < 65; i++) {
            name += "a";
        }
        scorecardForm.getScorecard().setName(name);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_name.length" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not updated
        Scorecard sc = UnitTestHelper.getInstance().getScorecard(sid);
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard version in the action
     * form is empty.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_name.required"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptyVersion() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the scorecard version
        String version = "";
        scorecardForm.getScorecard().setVersion(version);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_version.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard version in the action
     * form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_version.malformed"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_MalformedVersion() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the scorecard version
        String version = "1.x";
        scorecardForm.getScorecard().setVersion(version);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_version.malformed" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The name-version combination is not
     * unique.
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "editScorecard.error.scorecard_name_version.not_unique". 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_NameVersionNotUnique() throws Exception {
        this.initializeScorecard();
        // create the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_name_version.not_unique" });
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertEquals("The scorecard is created.", 1, scs.length);
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The project type & category
     * combination in the action form is invalid.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.project_category.invalid"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_InvalidProjectTypeCategory() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // there's no category Application Design
        scorecardForm.setProjectCategoryName("Design");
        scorecardForm.setProjectTypeName("Application");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.project_category.invalid" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard status in the action
     * form is invalid.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_status.invalid"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_InvalidScorecardStatus() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // unknown scorecard status
        scorecardForm.getScorecard().getScorecardStatus().setName("Unknown");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_status.invalid" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard type in the action form
     * is invalid.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard_type.invalid"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_InvalidScorecardType() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // unknown scorecard type
        scorecardForm.getScorecard().getScorecardType().setName("Unknown");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_type.invalid" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard min score in the action
     * form is negative.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.min_score.negative"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_NegativeMinScore() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // negative min score
        scorecardForm.setMinScoreText("-1");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.min_score.negative" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard min score in the action
     * form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.min_score.malformed"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_MalformedMinScore() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // malformed min score
        scorecardForm.setMinScoreText("xx");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.min_score.malformed" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard max score in the action
     * form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.max_score.malformed"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_MalformedMaxScore() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // malformed max score
        scorecardForm.setMaxScoreText("xx");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.max_score.malformed" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard min score is larger than
     * max score in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.min_score.larger_than_max_score"). 2. The
     * result forward should be "editScorecard"("/editScorecard.jsp"). 3. The
     * scorecard should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_MinScoreLargerThanMaxScore() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set max & min score
        scorecardForm.setMaxScoreText("10");
        scorecardForm.setMinScoreText("11");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.min_score.larger_than_max_score" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The group weights sum up to more than
     * 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard.weight"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_GroupWeightsMoreThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a new group
        scorecardForm.setOperation("doAddGroup");
        scorecardForm.setGroupIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();

        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The group weights sum up to less than
     * 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.scorecard.weight"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_GroupWeightsLessThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a new group
        scorecardForm.setOperation("doAddGroup");
        scorecardForm.setGroupIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();

        // adjust group weights
        scorecardForm.getScorecard().getGroup(0).setWeight(10);
        scorecardForm.getScorecard().getGroup(1).setWeight(89);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The group name is empty in the action
     * form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.group_name.required"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptyGroupName() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set group name
        scorecardForm.getScorecard().getGroup(0).setName("   ");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.group_name.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The group name in the action form is
     * longer than 64.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.group_name.length"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_GroupNameTooLong() throws Exception {
        // create the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // modify the group name
        String name = "";
        for (int i = 0; i < 65; i++) {
            name += "a";
        }
        scorecardForm.getScorecard().getGroup(0).setName(name);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.group_name.length" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The section weights sum up to more
     * than 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.group.weight"). 2. The result forward should
     * be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should not be
     * created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_SectionWeightsMoreThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a section
        scorecardForm.setOperation("doAddSection");
        scorecardForm.setGroupIndex(0);
        scorecardForm.setSectionIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();

        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.group.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The section weights sum up to less
     * than 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.group.weight"). 2. The result forward should
     * be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should not be
     * created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_SectionWeightsLessThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a section
        scorecardForm.setOperation("doAddSection");
        scorecardForm.setGroupIndex(0);
        scorecardForm.setSectionIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();

        // adjust the section weights
        scorecardForm.getScorecard().getGroup(0).getSection(0).setWeight(10);
        scorecardForm.getScorecard().getGroup(0).getSection(1).setWeight(89);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.group.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The section name is empty in the
     * action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.section_name.required"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptySectionName() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set name
        scorecardForm.getScorecard().getGroup(0).getSection(0).setName("  ");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.section_name.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The section name is longer than 64 in
     * the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.section_name.length"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_SectionNameTooLong() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set name
        String name = "";
        for (int i = 0; i < 65; i++) {
            name += ".";
        }
        scorecardForm.getScorecard().getGroup(0).getSection(0).setName(name);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.section_name.length" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The question weights sum up to more
     * than 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.section.weight"). 2. The result forward should
     * be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should not be
     * created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_QuestionWeightsMoreThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a question
        scorecardForm.setOperation("doAddQuestion");
        scorecardForm.setGroupIndex(0);
        scorecardForm.setSectionIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();

        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.section.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The question weights sum up to less
     * than 100 in the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.section.weight"). 2. The result forward should
     * be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should not be
     * created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_QuestionWeightsLessThan100() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // add a question
        scorecardForm.setOperation("doAddQuestion");
        scorecardForm.setGroupIndex(0);
        scorecardForm.setSectionIndex(0);
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // adjust question weights
        scorecardForm.getScorecard().getGroup(0).getSection(0).getQuestion(0)
                .setWeight(10);
        scorecardForm.getScorecard().getGroup(0).getSection(0).getQuestion(1)
                .setWeight(89);
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.section.weight" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The question description is empty in
     * the action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.question_description.required"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptyQuestionDescription() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set description
        scorecardForm.getScorecard().getGroup(0).getSection(0).getQuestion(0)
                .setDescription("  ");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.question_description.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The question guideline is empty in the
     * action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.question_guideline.required"). 2. The result
     * forward should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard
     * should not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_EmptyQuestionGuideline() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set guideline
        scorecardForm.getScorecard().getGroup(0).getSection(0).getQuestion(0)
                .setGuideline("  ");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.question_guideline.required" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The question type is invalid in the
     * action form is malformed.
     * </p>
     * <p>
     * Expected Result: 1. There should be action
     * error("editScorecard.error.question_type.invalid"). 2. The result forward
     * should be "editScorecard"("/editScorecard.jsp"). 3. The scorecard should
     * not be created.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_InvalidQuestionType() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set question type
        scorecardForm.getScorecard().getGroup(0).getSection(0).getQuestion(0)
                .getQuestionType().setName("Unknown");
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this
                .verifyActionErrors(new String[] { "editScorecard.error.question_type.invalid" });
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Unit test on "doFinish" operation. The scorecard is active and can not be
     * updated.
     * </p>
     * <p>
     * Expected Result: 1. There should be action error
     * "editScorecard.error.scorecard_in_use". 2. The result forward should be
     * "failure"("/error.jsp"). 3. The scorecard should be saved.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testDoFinish_ScorecardActive() throws Exception {
        this.initializeScorecard();
        // retrieve the saved scorecard
        Scorecard sc = UnitTestHelper.getInstance()
                .getScorecardByNameAndVersion(this.scorecard.getName(),
                        this.scorecard.getVersion());
        // retrieve the sid
        long sid = sc.getId();
        // edit the scorecard
        this.addRequestParameter("actionName", "editScorecard");
        this.addRequestParameter("sid", sid + "");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // change the scorecard status to "Active"
        sc.setScorecardStatus(ScorecardActionsHelper.getInstance()
                .getScorecardStatus("Active"));
        UnitTestHelper.getInstance().updateScorecard(sc);

        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this
                .verifyActionErrors(new String[] { "editScorecard.error.scorecard_is_active" });
        // verify the scorecard is not updated
        sc = UnitTestHelper.getInstance().getScorecard(sid);
        this.increaseMinorVersion(this.scorecard);
        this.scorecard.setScorecardStatus(ScorecardActionsHelper.getInstance()
                .getScorecardStatus("Active"));
        assertScorecardsEqual(this.scorecard, sc, true);
    }

    /**
     * <p>
     * Unit test on "saveScorecard" action, the user is not logged in.
     * </p>
     * <p>
     * Expected Result: 1. There should be one action error
     * "global.error.authorization". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     */
    public void testSaveScorecard_NotLoggedIn() throws Exception {
        // new the scorecard
        this.addRequestParameter("actionName", "newScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("editScorecard");
        this.verifyForwardPath("/editScorecard.jsp");
        this.verifyNoActionErrors();
        // get the scorecardForm
        ScorecardForm scorecardForm = (ScorecardForm) this.getActionForm();
        // set operation to "doFinish"
        scorecardForm.setOperation("doFinish");
        // logout
        this.logout();
        // perform save scorecard action
        this.addRequestParameter("actionName", "saveScorecard");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyActionErrors(new String[] { "global.error.authorization" });
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        // verify the scorecard is not created
        Scorecard[] scs = UnitTestHelper.getInstance().getScorecardsByName(
                scorecardForm.getScorecard().getName());
        assertTrue("The scorecard is created.",
                (scs == null || scs.length == 0));
    }

    /**
     * <p>
     * Increase the minor version of the given scorecard.
     * </p>
     * 
     * @param s
     *            the scorecard
     */
    private void increaseMinorVersion(Scorecard s) {
        String version = s.getVersion();
        int idx = version.lastIndexOf(".");
        if (idx < 0) {
            s.setVersion(version + ".1");
        } else {
            int minor = Integer.parseInt(version.substring(idx + 1));
            minor++;
            s.setVersion(version.substring(0, idx) + "." + minor);
        }
    }
}
