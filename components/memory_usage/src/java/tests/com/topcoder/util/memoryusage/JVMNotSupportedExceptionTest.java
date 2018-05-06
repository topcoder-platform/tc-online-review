/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests for JVMNotSupportedException. Simply applies the GenericExceptionTest,
 * avoiding the tests with causing exception.
 *
 * @author TexWiller
 * @version 2.0
 */
public class JVMNotSupportedExceptionTest extends GenericExceptionTest {

    /**
     * Standard TestCase constructor: creates a new JVMNotSupportedExceptionTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public JVMNotSupportedExceptionTest(String testName) {
        super(JVMNotSupportedException.class, testName, false);
    }


    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(JVMNotSupportedExceptionTest.class);
        return suite;
    }

}
