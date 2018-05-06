/**
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.TestResult;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TopCoder
 * @version 1.0
 */
public class StressTests extends TestCase {

    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(UUID32ImplementationTests.suite());
        suite.addTest(UUID128ImplementationTests.suite());

        suite.addTest(Int32GeneratorTests.suite());
        suite.addTest(UUIDVersion1GeneratorTests.suite());
        suite.addTest(UUIDVersion4GeneratorTests.suite());

        suite.addTest(UUIDUtilityTests.suite());

        return suite;
    }
}
