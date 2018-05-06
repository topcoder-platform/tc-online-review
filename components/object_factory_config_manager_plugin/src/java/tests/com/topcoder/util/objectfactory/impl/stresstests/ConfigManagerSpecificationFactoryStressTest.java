/*
 * Copyright (c) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory.impl.stresstests;

import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * Stress test cases for <code>ConfigManagerSpecificationFactory</code> class.
 * </p>
 *
 * @author Wendell
 * @version 2.0
 */
public class ConfigManagerSpecificationFactoryStressTest extends TestCase {
    /**
     * Sets up the test environment.
     */
    protected void setUp() throws Exception {
        StressTestHelper.clearConfig();
        StressTestHelper.loadConfig(StressTestHelper.CONFIG);
    }

    /**
     * Clears the configuration from ConfigManager.
     */
    protected void tearDown() {
        StressTestHelper.clearConfig();
    }

    /**
     * Tests constructor.
     */
    public void testConfigManagerSpecificationFactory() throws Exception {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < StressTestHelper.RUN_TIMES; i++) {
            ConfigManagerSpecificationFactory factory =
                new ConfigManagerSpecificationFactory(StressTestHelper.NAMESPACE);
            assertNotNull("failed to instantiate ConfigManagerSpecificationFactory.", factory);
        }

        long endTime = System.currentTimeMillis();
        System.out.println("Creating " + StressTestHelper.RUN_TIMES
            + " ConfigManagerSpecificationFactory instances takes " + (endTime - startTime) + "ms");
    }

    /**
     * Returns the test suite of this test case.
     *
     * @return the test suite of this test case.
     */
    public static Test suite() {
        return new TestSuite(ConfigManagerSpecificationFactoryStressTest.class);
    }
}
