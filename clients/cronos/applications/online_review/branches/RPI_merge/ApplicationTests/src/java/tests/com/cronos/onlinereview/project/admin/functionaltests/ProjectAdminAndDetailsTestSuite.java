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
        suite.addTestSuite(ContactProjectManagersFunctionalTest.class); // Fails
        suite.addTestSuite(CreateProjectFunctionalTest.class); // Fails
        suite.addTestSuite(DownloadFinalFixFunctionalTest.class); // Fails
        suite.addTestSuite(DownloadTestCasesFunctionalTest.class); // Fails
        suite.addTestSuite(EditProjectDetailsFunctionalTest.class); // re-run
        suite.addTestSuite(EndPhaseFunctionalTest.class);
        suite.addTestSuite(PerformSubmissionFunctionalTest.class); // Fails
        suite.addTestSuite(PerformFinalFixFunctionalTest.class); // Fails
        suite.addTestSuite(PostDeliverablesFunctionalTest.class); // Passes
        suite.addTestSuite(RemoveSubmissionsFunctionalTest.class); // Fails
        suite.addTestSuite(SetTimelineNotificationsFunctionalTest.class); // Fails
        suite.addTestSuite(UploadTestCasesFunctionalTest.class); // Passes
        suite.addTestSuite(ViewAllPaymentInformationFunctionalTest.class); // Fails
        suite.addTestSuite(ViewAllSubmissionsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewMostRecentSubmissionsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewMostRecentSubmissionsafterAppealsResponseFunctionalTest.class); // Fails
        suite.addTestSuite(ViewMyPaymentInformationFunctionalTest.class); // Passes
        suite.addTestSuite(ViewMyProjectFunctionalTest.class); // Passes
        suite.addTestSuite(ViewMySubmissionsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewProjectDetailFunctionalTest.class); // Passes
        suite.addTestSuite(ViewProjectResourcesFunctionalTest.class); // Passes
        suite.addTestSuite(ViewProjectsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewRegistrationsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewSVNLinkFunctionalTest.class); // Passes
        suite.addTestSuite(ViewScreenerSubmissionsFunctionalTest.class); // Fails
        suite.addTestSuite(ViewWinningSubmissionsFunctionalTest.class); // Fails
        return suite;
    }
}
