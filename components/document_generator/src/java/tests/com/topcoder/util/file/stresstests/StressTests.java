/*
 * Copyright (C) 2007 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Stress test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class StressTests extends TestCase {
    /**
     * <p>
     * This test case aggregates all Stress test cases.
     * </p>
     *
     * @return all Stress test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTestSuite(MultithreadedTests.class);
        suite.addTestSuite(TemplateAndDataStressTests.class);
        suite.addTestSuite(BigFileStressTest.class);
        suite.addTestSuite(DocumentGeneratorFactoryStressTest.class);

        return suite;
    }
}
