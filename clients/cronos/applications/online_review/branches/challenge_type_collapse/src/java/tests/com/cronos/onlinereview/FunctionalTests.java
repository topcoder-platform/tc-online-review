/*
 * Copyright (C) 2010-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class FunctionalTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(LoginTests.class);
        suite.addTestSuite(ManageProjectTests.class);
        suite.addTestSuite(LinkProjectsTests.class);
        suite.addTestSuite(OpenPhaseTests.class);
        suite.addTestSuite(EditProjectTests.class);
        suite.addTestSuite(ClosePhaseTests.class);
        suite.addTestSuite(ManageProjectWithNoPermissionTests.class);
        suite.addTestSuite(SettingPhaseEndDateTests.class);
        suite.addTestSuite(AddRoleTests.class);
        suite.addTestSuite(FinalFixSubmissionUploadTests.class);
        suite.addTestSuite(SubmissionUploadTests.class);
        suite.addTestSuite(EditProjectFiledsTests.class);
        suite.addTestSuite(CompetitorPrivilegeTests.class);
        suite.addTestSuite(ScoreCardTests.class);
        suite.addTestSuite(DeleteReviewerTests.class);
        suite.addTestSuite(ChangeScorecardTests.class);
        suite.addTestSuite(ReviewerManageScorecardTests.class);
        suite.addTestSuite(ReviewerUploadTestCaseTests.class);
        suite.addTestSuite(ManageAppealTests.class);
        suite.addTestSuite(ScreenPhaseCannotOpenTests.class);
        suite.addTestSuite(UnregisterProjectTests.class);
        suite.addTestSuite(PostmortemReviewerManageScorecardTests.class);
        suite.addTestSuite(ScreenerManageScorecardTests.class);
        suite.addTestSuite(AggregatorAddScorecardTests.class);
        suite.addTestSuite(TimelineAdvancementTests.class);
        
        return suite;
    }
}
