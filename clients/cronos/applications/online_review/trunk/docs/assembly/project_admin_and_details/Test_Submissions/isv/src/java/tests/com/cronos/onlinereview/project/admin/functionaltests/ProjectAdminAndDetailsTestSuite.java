/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.project.admin.functionaltests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * @author TCSTester
 * @version 1.0
 */
public class ProjectAdminAndDetailsTestSuite extends TestCase {
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTestSuite(AdvanceSubmissionFunctionalTest.class);
        suite.addTestSuite(ContactProjectManagersFunctionalTest.class);
        suite.addTestSuite(CreateProjectFunctionalTest.class);
        suite.addTestSuite(DownloadFinalFixFunctionalTest.class);
        suite.addTestSuite(DownloadTestCasesFunctionalTest.class);
        suite.addTestSuite(EditProjectDetailsFunctionalTest.class);
        suite.addTestSuite(EndPhaseFunctionalTest.class);
        suite.addTestSuite(PerformSubmissionFunctionalTest.class);
        suite.addTestSuite(PerformFinalFixFunctionalTest.class);
        suite.addTestSuite(PostDeliverablesFunctionalTest.class);
        suite.addTestSuite(RemoveSubmissionsFunctionalTest.class);
        suite.addTestSuite(SetTimelineNotificationsFunctionalTest.class);
        suite.addTestSuite(UploadTestCasesFunctionalTest.class);
        suite.addTestSuite(ViewAllPaymentInformationFunctionalTest.class);
        suite.addTestSuite(ViewAllSubmissionsFunctionalTest.class);
        suite.addTestSuite(ViewMostRecentSubmissionsFunctionalTest.class);
        suite.addTestSuite(ViewMostRecentSubmissionsafterAppealsResponseFunctionalTest.class);
        suite.addTestSuite(ViewMyPaymentInformationFunctionalTest.class);
        suite.addTestSuite(ViewMyProjectFunctionalTest.class);
        suite.addTestSuite(ViewMySubmissionsFunctionalTest.class);
        suite.addTestSuite(ViewProjectDetailFunctionalTest.class);
        suite.addTestSuite(ViewProjectResourcesFunctionalTest.class);
        suite.addTestSuite(ViewProjectsFunctionalTest.class);
        suite.addTestSuite(ViewRegistrationsFunctionalTest.class);
        suite.addTestSuite(ViewSVNLinkFunctionalTest.class);
        suite.addTestSuite(ViewScreenerSubmissionsFunctionalTest.class);
        suite.addTestSuite(ViewWinningSubmissionsFunctionalTest.class);
        return suite;
    }
}
