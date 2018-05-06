/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.collection.typesafeenum.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Stress test cases.
 * </p>
 *
 * @author TopCoder
 * @version 1.1
 */
public class StressTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(EnumStressTests.class));
        suite.addTestSuite(TestEnumStress.class);
        return suite;
    }
}