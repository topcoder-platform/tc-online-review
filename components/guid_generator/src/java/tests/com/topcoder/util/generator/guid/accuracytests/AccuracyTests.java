/**
 * GUID Generator 1.0
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 * 
 * @author TopCoder
 * @version 1.0
 */

public class AccuracyTests extends TestCase {

    public static Test suite() {

        final TestSuite suite = new TestSuite();

        suite.addTestSuite(UUIDTypeTests.class);
        suite.addTestSuite(UUID32ImplementationTests.class);
        suite.addTestSuite(UUID128ImplementationTests.class);
        suite.addTestSuite(IDGeneratorAdapterTests.class);
        suite.addTestSuite(UUIDUtilityTests.class);
        suite.addTestSuite(Int32GeneratorTests.class);
        suite.addTestSuite(UUIDVersion1GeneratorTests.class);
        suite.addTestSuite(UUIDVersion4GeneratorTests.class);

        return suite;
    }
}