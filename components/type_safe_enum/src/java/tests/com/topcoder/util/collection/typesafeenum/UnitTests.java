/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case aggregates all unit test cases.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class UnitTests extends TestCase {

    /**
     * Aggregates all unit test cases.
     *
     * @return the aggregated all unit test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(EnumUnitTests.class);
        suite.addTestSuite(DemoTests.class);
        return suite;
    }
}
