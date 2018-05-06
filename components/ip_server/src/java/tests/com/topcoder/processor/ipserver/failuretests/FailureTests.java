/**
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.processor.ipserver.failuretests;

import com.topcoder.processor.ipserver.failuretests.keepalive.KeepAliveHandlerFailureTest;
import com.topcoder.processor.ipserver.failuretests.keepalive.KeepAliveIPClientFailureTest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * <p>
 * This test case aggregates all Failure test cases.
 * </p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class FailureTests extends TestCase {
    /**
     * Return all failure test cases.
     *
     * @return the failure test cases
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // since 1.0
        suite.addTest(ConnectionFailureTest.suite());
        suite.addTest(IPServerManagerFailureTest.suite());
        suite.addTest(KeepAliveIPClientFailureTest.suite());
        suite.addTest(KeepAliveHandlerFailureTest.suite());
        suite.addTest(HandlerFailureTest.suite());
        suite.addTest(IPClientFailureTest.suite());
        suite.addTest(IPServerFailureTest.suite());

        // since 2.0
        suite.addTest(DefaultMessageFactoryFailureTests.suite());
        suite.addTest(SerializableMessageSerializerFailureTests.suite());
        suite.addTest(SimpleSerializableMessageFailureTests.suite());
        
        return suite;
    }
}
