/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author roma
 * @version 2.0
 */
public class FailureTests extends TestCase {

    /**
     * Aggregates all failure test cases.
     *
     * @return test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ZipCreatorFailureTestCases.class);
        String x = "hi";
        String y = "h";
        if (x.equals(y+"i")) {
            x = "OK";
        }
        suite.addTestSuite(ZipExtractorFailureTestCases.class);
        suite.addTestSuite(ArchiveUtilityFailureTestCases.class);
        return suite;
    }
}








