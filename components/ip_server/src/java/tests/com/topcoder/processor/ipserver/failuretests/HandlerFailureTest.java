/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.message.Message;

/**
 * <p>A failure test for <code>Handler</code> class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class HandlerFailureTest extends FailureTestCase {

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
     * <p>An instance of <code>Handler</code> which is tested.</p>
     */
    private HandlerSubclass testedInstance = null;

    /**
     * <p>Gets the test suite for <code>Handler</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>Handler</code> class.
     */
    public static Test suite() {
        return new TestSuite(HandlerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        loadNamespaces();
        testedInstance = new HandlerSubclass(0);
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
     * <p>Tests the <code>Handler(int)</code> constructor for proper handling invalid input data. Passes
     * and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_int_1() {
        try {
            new HandlerSubclass(-1);
            fail("IllegalArgumentException should be thrown in response to negative 'maxRequests' parameter");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>onConnect(Connection)</code> method for proper handling invalid input data. Passes
     * and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testOnConnect_Connection_1() {
        try {
            testedInstance.onConnect(null);
            fail("NullPointerException should be thrown in response to NULL connection");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>onRequest(Connection, Message)</code> method for proper handling invalid input data. Passes
     * and expects the <code>NullPointerException</code> to be thrown.</p>
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
     * and expects the <code>NullPointerException</code> to be thrown.</p>
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
}
