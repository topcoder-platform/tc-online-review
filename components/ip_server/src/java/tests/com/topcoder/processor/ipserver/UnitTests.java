/**
 *
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.processor.ipserver;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TopCoder
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class UnitTests extends TestCase {

    /**
     * Aggreegates all unit test cases.
     * @return the unit test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(MessageCreationExceptionTest.class));
        suite.addTest(new TestSuite(MessageSerializationExceptionTest.class));
        suite.addTest(new TestSuite(UnknownMessageExceptionTest.class));
        suite.addTest(new TestSuite(SimpleSerializableMessageTest.class));
        suite.addTest(new TestSuite(SerializableMessageSerializerTest.class));
        suite.addTest(new TestSuite(DefaultMessageFactoryTest.class));

        suite.addTest(new TestSuite(IPServerManagerTest.class));
        suite.addTest(new TestSuite(ConfigurationExceptionTest.class));
        suite.addTest(new TestSuite(ConnectionTest.class));
        suite.addTest(new TestSuite(HandlerTest.class));
        suite.addTest(new TestSuite(IPClientTest.class));
        suite.addTest(new TestSuite(KeepAliveHandlerTest.class));
        suite.addTest(new TestSuite(KeepAliveIPClientTest.class));
        suite.addTest(new TestSuite(ProcessingExceptionTest.class));
        suite.addTest(new TestSuite(IPServerTest.class));
        suite.addTest(new TestSuite(IPServerDemo.class));
        return suite;
    }

}
