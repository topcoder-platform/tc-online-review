/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 * @author FireIce
 * @version 2.0
 */
public class FailureTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ExceptionUtilsFailureTests.class);
        return suite;
    }

}
