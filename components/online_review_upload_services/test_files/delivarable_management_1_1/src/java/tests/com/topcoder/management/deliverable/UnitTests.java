/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.logging.LogMessageTests;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingExceptionTest;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceExceptionTest;
import com.topcoder.management.deliverable.persistence.UploadPersistenceExceptionTest;
import com.topcoder.management.deliverable.search.DeliverableFilterBuilderTests;
import com.topcoder.management.deliverable.search.SubmissionFilterBuilderTests;
import com.topcoder.management.deliverable.search.UploadFilterBuilderTests;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class UnitTests extends TestCase {

    /**
     * <p>Creates test suite.</p>
     *
     * @return test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(LogMessageTests.class);

        suite.addTestSuite(AuditedDeliverableStructureTests.class);
        suite.addTestSuite(DeliverableTests.class);
        suite.addTestSuite(IdAlreadySetExceptionTests.class);
        suite.addTestSuite(NamedDeliverableStructureTests.class);
        suite.addTestSuite(PersistenceDeliverableManagerTests.class);
        suite.addTestSuite(PersistenceUploadManagerTests.class);
        suite.addTestSuite(SubmissionStatusTests.class);
        suite.addTestSuite(SubmissionTests.class);
        suite.addTestSuite(UploadStatusTests.class);
        suite.addTestSuite(UploadTests.class);
        suite.addTestSuite(UploadTypeTests.class);
        suite.addTestSuite(SubmissionTypeTests.class);

        suite.addTestSuite(DeliverableCheckingExceptionTest.class);
        suite.addTestSuite(DeliverablePersistenceExceptionTest.class);
        suite.addTestSuite(UploadPersistenceExceptionTest.class);
        suite.addTestSuite(DeliverableFilterBuilderTests.class);
        suite.addTestSuite(SubmissionFilterBuilderTests.class);
        suite.addTestSuite(UploadFilterBuilderTests.class);

        suite.addTestSuite(Demo.class);
        return suite;
    }
}
