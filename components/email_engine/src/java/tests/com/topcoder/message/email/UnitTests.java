/*
 * Copyright (C) 2005-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author  TopCoder
 * @version 3.2
 */
public class UnitTests extends TestCase {

    /**
     * Returns suite containing all Unit test cases.
     *
     * @return the suite to run the Unit test.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(AddressExceptionTest.suite());
        suite.addTest(SendingExceptionTest.suite());

        suite.addTest(PriorityLevelTest.suite());

        suite.addTest(TCSEmailMessageTestV3Normal.suite());
        suite.addTest(TCSEmailMessageTestV3Priority.suite());

        suite.addTest(EmailEngineTestV3.suite());

        suite.addTest(ComponentDemo.suite());
        return suite;
    }

}
