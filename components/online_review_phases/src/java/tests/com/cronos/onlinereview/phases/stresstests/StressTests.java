/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all stress test cases.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class StressTests extends TestCase {

    /**
     * Aggregates all the stress tests.
     * @return the test suite
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        return suite;
    }
}
