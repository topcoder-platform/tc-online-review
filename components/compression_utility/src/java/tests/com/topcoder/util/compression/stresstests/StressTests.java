package com.topcoder.util.compression.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all stress test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(RandomDataTest.class);
        suite.addTestSuite(SourceCodeTest.class);
        return suite;
    }

}








