/**
 *
 * Copyright ? 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class FailureTests extends TestCase {
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        //suite.addTest(XXX.suite());
        suite.addTestSuite(FunctionalTest.class);
        suite.addTestSuite(GeneratorsFailureTest.class);
        suite.addTestSuite(UUIDFailureTest.class);
        suite.addTestSuite(UUIDUtilityFailureTest.class);

        // simple accuracy test 
        suite.addTestSuite(UUIDVersion4GeneratorTest.class);
        suite.addTestSuite(UUIDVersion1GeneratorTest.class);
        suite.addTestSuite(Int32GeneratorTest.class);

        return suite;
    }
}
