/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all accuracy test cases.
 * </p>
 *
 * @author fairytale, KLW
 * @version 3.1
 * @since 3.0
 */
public class AccuracyTests extends TestCase {
    /**
     * Returns the test suite for all accuracy tests.
     *
     * @return Test the test suite for all accuracy tests.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(new TestSuite(AccuracyPriorityLevelTest.class));
        suite.addTest(new TestSuite(AccuracyEmailEngineTest.class));
        suite.addTest(new TestSuite(AccuracyTCSEmailMessageTest.class));
        suite.addTest(new TestSuite(AccuracySetPriorityTest.class));
        suite.addTest(new TestSuite(AccuracyTCSEmailMessageTestV31.class));

        // added in version 3.2
        suite.addTest(new TestSuite(EmailEngineAccuracyTestV32.class));
        return suite;
    }
}
