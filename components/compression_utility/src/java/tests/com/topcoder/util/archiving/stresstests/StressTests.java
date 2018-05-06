package com.topcoder.util.archiving.stresstests;


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
        suite.addTestSuite(ZipExtractionStressTest.class);
        suite.addTestSuite(ZipCreationStressTest.class);
        return suite;
    }

}








