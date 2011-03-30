/**
 *
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.cronos.onlinereview;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all functional test cases.</p>
 *
 * @author TopCoder
 * @version 1.1
 * @since 1.0
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
        suite.addTestSuite(DeleteSubmissionTests.class);
        suite.addTestSuite(CompetitorPrivilegeTests.class);
        suite.addTestSuite(ScoreCardTests.class);
        suite.addTestSuite(DeleteReviewerTests.class);
        suite.addTestSuite(ChangeScorecardTests.class);
        
        return suite;
    }
}
