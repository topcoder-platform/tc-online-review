/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests.keepalive;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.failuretests.ConnectionFailureTest;
import com.topcoder.processor.ipserver.failuretests.FailureTestCase;
import com.topcoder.processor.ipserver.failuretests.MockMessage;

/**
 * <p>A failure test for <code>KeepAliveHandler</code> class. Tests the proper handling of invalid input data by the
 * methods. Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class KeepAliveHandlerFailureTest extends FailureTestCase {

    /**
     * <p>A <code>Connection  </code> providing a sample connection to be used for testing.</p>
     */
    public static final Connection CONNECTION = createConnection(ConnectionFailureTest.ID,
                                                                 ConnectionFailureTest.IP_SERVER,
                                                                 ConnectionFailureTest.SOCKET);

    /**
     * <p>A <code>Message</code> providing a sample message to be used for testing.</p>
     */
    public static final Message MESSAGE = new MockMessage();

    /**
     * <p>An instance of <code>KeepAliveHandler</code> which is tested.</p>
     */
    private KeepAliveHandlerSubclass testedInstance = null;

    /**
     * <p>Gets the test suite for <code>KeepAliveHandler</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>KeepAliveHandler</code> class.
     */
    public static Test suite() {
        return new TestSuite(KeepAliveHandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        testedInstance = new KeepAliveHandlerSubclass();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        testedInstance = null;
    }

    /**
     * <p>Tests the <code>onRequest(Connection, Message)</code> method for proper handling invalid input data. Passes
     * the <code>null</code> connection and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testOnRequest_Connection_Message_1() {
        try {
            testedInstance.onRequest(null, MESSAGE);
            fail("NullPointerException should be thrown in response to NULL connection");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>onRequest(Connection, Message)</code> method for proper handling invalid input data. Passes
     * the <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testOnRequest_Connection_Message_2() {
        try {
            testedInstance.onRequest(CONNECTION, null);
            fail("NullPointerException should be thrown in response to NULL message");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>onRequest(Connection, Message)</code> method for proper handling invalid input data. Passes
     * the <code>Connection</code> referencing the server which is not currently running and expects the <code>
     * IllegalStateException</code> to be thrown.</p>
     */
    public void testOnRequest_Connection_Message_3() {
        try {
            testedInstance.onRequest(CONNECTION, MESSAGE);
            fail("IllegalStateException should be thrown since the server is not running");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

}
