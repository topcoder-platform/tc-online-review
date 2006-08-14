/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardType;

/**
 * <p>
 * Unit tests on "listScorecards" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class UnitTestListScorecardsAction extends BaseTestCase {
    /** Scorecards array used in this test. */
    private Scorecard[] scorecards = null;

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
        this.scorecards = null;
        UnitTestHelper.getInstance().clearAllScorecards();
    }

    /**
     * <p>
     * Initialize the scorecards for given project type.
     * </p>
     * 
     * @param projectTypeId
     *            the project id
     * @throws Exception
     *             to JUnit
     */
    private void initializeScorecards(int projectTypeId) throws Exception {
        ScorecardActionsHelper helper = ScorecardActionsHelper.getInstance();
        ProjectCategory[] categories = helper
                .getProjectCategories(projectTypeId);
        ScorecardType[] types = helper.getScorecardTypes();
        int n = categories.length * types.length;
        this.scorecards = new Scorecard[n];
        int count = 0;
        for (int i = 0; i < categories.length; i++) {
            for (int j = 0; j < types.length; j++) {
                this.scorecards[count] = ScorecardActionsHelper
                        .buildNewScorecard();
                this.scorecards[count].setName(categories[i].getProjectType()
                        .getName()
                        + " "
                        + categories[i].getName()
                        + " "
                        + types[j].getName() + " Scorecard");
                this.scorecards[count].setCategory(categories[i].getId());
                this.scorecards[count].setScorecardType(types[j]);
                UnitTestHelper.getInstance().createScorecard(
                        this.scorecards[count]);
                count++;
            }
        }
    }

    /**
     * <p>
     * Verify the scorecard list request attribute.
     * </p>
     */
    private void verifyScorecardList() {
        ScorecardListBean list = (ScorecardListBean) this.getRequest()
                .getAttribute(Constants.ATTR_KEY_SCORECARD_LIST);
        assertNotNull("scorecardList attribute doesn't exist.", list);
        int idx = 0;
        ScorecardGroupBean[] groups = list.getScorecardGroups();
        for (int i = 0; i < groups.length; i++) {
            Scorecard[] scs = groups[i].getScorecards();
            for (int j = 0; j < scs.length; j++) {
                assertScorecardsEqual(this.scorecards[idx++], scs[j], false);
            }
        }
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, this test try to list scorecards
     * for "Component" projects.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "listScorecards"("/listScorecards.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testListScorecards_ComponentProjects() throws Exception {
        // init scorecards
        this.initializeScorecards(1);
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.addRequestParameter("projectTypeId", "1");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("listScorecards");
        this.verifyForwardPath("/listScorecards.jsp");
        this.verifyNoActionErrors();
        // verify the "scorecardList" request attribute
        this.verifyScorecardList();
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, this test try to list scorecards
     * for "Component" projects(no "projectTypeId" is provided, defaults to 1).
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "listScorecards"("/listScorecards.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testListScorecards_DefaultProjects() throws Exception {
        // init scorecards
        this.initializeScorecards(1);
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("listScorecards");
        this.verifyForwardPath("/listScorecards.jsp");
        this.verifyNoActionErrors();
        // verify the "scorecardList" request attribute
        this.verifyScorecardList();
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, this test try to list scorecards
     * for "Application" projects.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "listScorecards"("/listScorecards.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testListScorecards_ApplicationProjects() throws Exception {
        // init scorecards
        this.initializeScorecards(2);
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.addRequestParameter("projectTypeId", "2");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("listScorecards");
        this.verifyForwardPath("/listScorecards.jsp");
        this.verifyNoActionErrors();
        // verify the "scorecardList" request attribute
        this.verifyScorecardList();
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, this test try to list scorecards
     * for unknown project type.
     * </p>
     * <p>
     * Expected Result: 1. There should be one action error
     * "global.error.general". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     */
    public void testListScorecards_UnknownProjectTypeId() {
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.addRequestParameter("projectTypeId", "3");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this.verifyActionErrors(new String[] { "global.error.general" });
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, this test try to list scorecards
     * for invalid project type, the default projectTypeId 1 will be used.
     * </p>
     * <p>
     * Expected Result: 1. There should be no action errors. 2. The result
     * forward should be "listScorecards"("/listScorecards.jsp").
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    public void testListScorecards_InvalidProjectTypeId() throws Exception {
        // init scorecards
        this.initializeScorecards(1);
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.addRequestParameter("projectTypeId", "invalid");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the forward
        this.verifyForward("listScorecards");
        this.verifyForwardPath("/listScorecards.jsp");
        this.verifyNoActionErrors();
        // verify the "scorecardList" request attribute
        this.verifyScorecardList();
    }

    /**
     * <p>
     * Unit test on "listScorecards" action, the user is not logged in.
     * </p>
     * <p>
     * Expected Result: 1. There should be one action error
     * "global.error.authorization". 2. The result forward should be
     * "failure"("/error.jsp").
     * </p>
     */
    public void testListScorecards_NotLoggedIn() {
        // logout first
        this.logout();
        // list the scorecards
        this.addRequestParameter("actionName", "listScorecards");
        this.addRequestParameter("projectTypeId", "1");
        this.setRequestPathInfo("/scorecardAdmin");
        this.actionPerform();
        // verify the "scorecardList" request attribute
        this.verifyForward("failure");
        this.verifyForwardPath("/error.jsp");
        this.verifyActionErrors(new String[] { "global.error.authorization" });
    }
}
