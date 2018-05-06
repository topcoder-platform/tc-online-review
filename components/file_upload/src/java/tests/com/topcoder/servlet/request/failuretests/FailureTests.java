/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.servlet.request.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 * @author TopCoder
 * @version 2.0
 */
public class FailureTests extends TestCase {

    /**
     * Aggregates all Failure tests in this component.
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(FileUploadResultFailureTests.suite());
        suite.addTest(FileUploadFailureTests.suite());
        suite.addTest(MemoryFileUploadFailureTests.suite());
        suite.addTest(LocalFileUploadFailureTests.suite());
        suite.addTest(RemoteFileUploadFailureTests.suite());
        suite.addTest(HttpRequestParserFailureTests.suite());
        return suite;
    }

}
