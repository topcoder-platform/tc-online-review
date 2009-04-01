/* Copyright (C) 2007 TopCoder Inc., All Rights Reserved. */

package com.cronos.onlinereview.tests.projects;

import com.cronos.onlinereview.tests.projects.aggregation.AllAggregationTestsTestSuite;
import com.cronos.onlinereview.tests.projects.appeals.AllAppealsTestsTestSuite;
import com.cronos.onlinereview.tests.projects.approval.AllApprovalTestsTestSuite;
import com.cronos.onlinereview.tests.projects.endphase.AllEndPhaseTestsTestSuite;
import com.cronos.onlinereview.tests.projects.finalfix.AllFinalFixTestsTestSuite;
import com.cronos.onlinereview.tests.projects.finalreview.AllFinalReviewTestsTestSuite;
import com.cronos.onlinereview.tests.projects.review.AllReviewTestsTestSuite;
import com.cronos.onlinereview.tests.projects.screening.AllScreeningTestsTestSuite;
import com.cronos.onlinereview.tests.projects.submissions.AllSubmissionTestsTestSuite;
import junit.framework.*;

public class AllProjectTestsTestSuite extends TestSuite
{
    public AllProjectTestsTestSuite(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        TestSuite suite = new AllProjectTestsTestSuite("Project Tests");

        suite.addTestSuite(CreateProjectTest.class);
        suite.addTestSuite(EditProjectTest.class);
        suite.addTestSuite(ViewProjectTest.class);
        suite.addTestSuite(ViewPaymentTest.class);
        suite.addTestSuite(ContactProjectManagersTest.class);
        suite.addTestSuite(ViewRegistrationsTest.class);
        
        suite.addTest(AllSubmissionTestsTestSuite.suite());
        suite.addTest(AllScreeningTestsTestSuite.suite());
        suite.addTest(AllReviewTestsTestSuite.suite());
        suite.addTest(AllAppealsTestsTestSuite.suite());
        suite.addTest(AllAggregationTestsTestSuite.suite());
        suite.addTest(AllFinalFixTestsTestSuite.suite());
        suite.addTest(AllFinalReviewTestsTestSuite.suite());

        suite.addTestSuite(SubmitCommentTest.class);
        
        suite.addTest(AllApprovalTestsTestSuite.suite());
        
        suite.addTestSuite(EditAnyScorecardTest.class);
        
        suite.addTest(AllEndPhaseTestsTestSuite.suite());
        
        suite.addTestSuite(AdvanceSubmissionTest.class);
        suite.addTestSuite(PostDeliverablesTest.class);
        suite.addTestSuite(DownloadTestCasesTest.class);
        suite.addTestSuite(CompositeReviewTest.class);
        
        return suite;
    }
    
}
