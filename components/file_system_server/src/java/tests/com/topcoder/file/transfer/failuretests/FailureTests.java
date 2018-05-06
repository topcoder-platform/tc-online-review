/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all failure test cases.
 * </p>
 *
 * @author fairytale, hfx
 * @version 1.1
 * @since 1.0
 */
public class FailureTests extends TestCase {
    /**
     * Returns the test suite for all failure tests.
     *
     * @return Test the test suite for all failure tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // version 1.0
        suite.addTest(new TestSuite(FailureFileSystemClientTest.class));
        suite.addTest(new TestSuite(FailureFileSystemHandlerTest.class));
        suite.addTest(new TestSuite(FailureUploadRequestValidatorTest.class));
        suite.addTest(new TestSuite(FailureFileIdGroupSearcherTest.class));
        suite.addTest(new TestSuite(FailureRegexFileSearcherTest.class));
        suite.addTest(new TestSuite(FailureRegexGroupSearcherTest.class));
        suite.addTest(new TestSuite(FailureSearchManagerTest.class));
        suite.addTest(new TestSuite(FailureFileSystemXmlRegistryTest.class));
        suite.addTest(new TestSuite(FailureFileSystemPersistenceTest.class));
        suite.addTest(new TestSuite(FailureInputStreamBytesIteratorTest.class));
        suite.addTest(new TestSuite(FailureBytesMessageTypeTest.class));
        suite.addTest(new TestSuite(FailureMessageTypeTest.class));
        suite.addTest(new TestSuite(FailureRequestMessageTest.class));
        suite.addTest(new TestSuite(FailureResponseMessageTest.class));

        // version 1.1
        suite.addTest(new TestSuite(FailureFileSystemClientTest_V1_1.class));
        suite.addTest(new TestSuite(FailureSimpleLockingFileSystemPersistenceTest_V1_1.class));

        return suite;
    }
}
