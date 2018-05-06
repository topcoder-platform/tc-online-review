/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.archiving;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all unit test cases.</p>
 *
 * @author visualage
 * @version 2.0
 */
public class UnitTests extends TestCase {

    /**
     * <p>
     * Return a new <code>TestSuite</code> containing all test cases in this class.
     * </p>
     *
     * @return a new <code>TestSuite</code> containing all test cases in this class
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // Test ArchiveUtility
        suite.addTest(ArchiveUtilityTestCase.suite());

        // Test ZipArchive
        suite.addTest(ZipArchiverTestCase.suite());

        // Demo tests
        suite.addTest(DemoTestCase.suite());
        return suite;
    }

}








