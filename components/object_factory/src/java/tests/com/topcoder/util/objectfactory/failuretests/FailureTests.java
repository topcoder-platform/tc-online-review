package com.topcoder.util.objectfactory.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all failure test cases.</p>
 *
 * @author TopCoder Software
 * @version 1.0
 */
public class FailureTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(ObjectFactoryFailureTests.class);
        suite.addTestSuite(ObjectSpecificationFailureTests.class);

        return suite;
    }
}
