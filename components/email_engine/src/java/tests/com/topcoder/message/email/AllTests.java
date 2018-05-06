/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 *
 * @(#)AllTests.java
 */
package com.topcoder.message.email;

import com.topcoder.message.email.failuretests.FailureTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p><code>AllTests</code></p>
 *
 * @author  TopCoder
 * @version 3.0
 */
public class AllTests {

    /**
     * Suite to run the tests.
     *
     * @return suite to run the tests
     */
    public static Test suite() {
        TestSuite testSuite = new TestSuite("AllTests");
        
        // Version 3.0
        testSuite.addTest(UnitTests.suite());
        testSuite.addTest(FailureTests.suite());
        testSuite.addTest(com.topcoder.message.email.accuracytests.AccuracyTests.suite());
        testSuite.addTest(com.topcoder.message.email.stresstests.StressTests.suite());


        // Previous version.
        testSuite.addTest(TCSEmailMessageTest.suite());
        testSuite.addTest(EmailEngineTest.suite());
        testSuite.addTest(EmailEngineAttachmentDataSourceTests.suite());
        testSuite.addTest(AccuracyTests.suite());
        testSuite.addTest(StressTest.suite());
        testSuite.addTest(EmailEngineFailureTests.suite());
        return testSuite;
    }

}
