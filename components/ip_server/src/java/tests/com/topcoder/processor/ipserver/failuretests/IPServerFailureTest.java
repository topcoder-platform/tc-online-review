/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */

package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;

import java.io.IOException;

/**
 * <p>A failure test for <code>IPServer</code> class. Tests the proper handling of invalid input data by the methods.
 * Passes the invalid arguments to the methods and expects the appropriate exception to be thrown.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class IPServerFailureTest extends FailureTestCase {

    /**
     * <p>A <code>String</code> providing the sample address to be used for testing.</p>
     */
    public static final String ADDRESS = "127.0.0.1";

    /**
     * <p>An <code>int</code> providing the sample valid port to be used for testing.</p>
     */
    public static final int PORT = 1024;

    /**
     * <p>An <code>int</code> providing the sample valid number of max connections to be used  for testing.</p>
     */
    public static final int MAX_CONNECTIONS = 10;

    /**
     * <p>A <code>String</code> providing the valid ID of a handler to be used for testing.</p>
     */
    public static final String HANDLER_ID = "10";

    /**
     * <p>A <code>Handler</code> to be used for testing.</p>
     */
    public static final Handler HANDLER = new KeepAliveHandler();

    /**
     * <p>A <code>Message</code> providing a sample message to be used for testing.</p>
     */
    public static final Message MESSAGE = new MockMessage();

    /**
     * <p>A <code>String</code> providing the valid ID of a connection to be used for testing.</p>
     */
    public static final String CONNECTION_ID = "10";

    /**
     * <p>A <code>String</code> providing the ID of a connection  non-existing within server to be used for testing.</p>
     */
    public static final String NON_EXISTING_CONNECTION_ID = "NON_EXISTING_ID";

    /**
     * <p>An instance of <code>IPServer</code> which is tested.</p>
     */
    private IPServer testedInstance = null;

    /**
     * Create IPServerFailureTest instance with test name.
     *
     * @param name the test name
     */
    public IPServerFailureTest(String name) {
        setName(name);
    }

    /**
     * <p>Gets the test suite for <code>IPServer</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>IPServer</code> class.
     */
    public static Test suite() {
        return new TestSuite(IPServerFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        loadNamespaces();
        testedInstance = getServer();
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        if (testedInstance.isStarted()) {
            stopServer();
        }

        testedInstance = null;
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the negative port number and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_int_1() {
        try {
            new IPServer(null, -1, MAX_CONNECTIONS, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to negative port");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the negative port number and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_int_2() {
        try {
            new IPServer(ADDRESS, -1, MAX_CONNECTIONS, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to negative port");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the port number out of valid range and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_int_3() {
        try {
            new IPServer(null, 65536, MAX_CONNECTIONS, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to port out of valid range");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the port number out of valid range and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_int_4() {
        try {
            new IPServer(ADDRESS, 65536, MAX_CONNECTIONS, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to port out of valid range");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the negative maximum number of connections and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_int_int_5() {
        try {
            new IPServer(null, PORT, -1, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to negative maximum connections number");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPServer(String, int, int, int)</code> constructor for proper handling invalid input data.
     * Passes the negative maximum number of connections and expects the <code>IllegalArgumentException</code> to be
     * thrown.</p>
     */
    public void testConstructor_String_int_int_6() {
        try {
            new IPServer(ADDRESS, PORT, -1, MAX_CONNECTIONS, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to negative maximum connections number");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>setAddress(String)</code> method for proper behavior when being used inapproprietly. Attempts
     * to set the new address after the server is started and expects the <code>IllegalStateException</code> to be
     * thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testSetAddress_String_1() throws IOException {
        startServer();
        try {
            testedInstance.setAddress("127.0.0.1");
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setPort(int)</code> method for proper handling invalid input data. Passes the negative port
     * number and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testSetPort_int_2() {
        try {
            testedInstance.setPort(-1);
            fail("IllegalArgumentException should be thrown in response to negative port");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setPort(int)</code> method for proper handling invalid input data. Passes the port number out
     * of valid range and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testSetPort_int_3() {
        try {
            testedInstance.setPort(65536);
            fail("IllegalArgumentException should be thrown in response to port out of valid range");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setPort(int)</code> method for proper behavior when being used inapproprietly. Attempts
     * to set the new address after the server is started and expects the <code>IllegalStateException</code> to be
     * thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testSetPort_int_1() throws IOException {
        startServer();

        try {
            testedInstance.setPort(8080);
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setMaxConnections(int)</code> method for proper handling invalid input data. Passes the
     * negatve maximum number of connections and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testSetMaxConnections_int_2() {
        try {
            testedInstance.setMaxConnections(-1);
            fail("IllegalArgumentException should be thrown in response to negative maximum connections number");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setMaxConnections(int)</code> method for proper behavior when being used inapproprietly.
     * Attempts to set the new address after the server is started and expects the <code>IllegalStateException</code> to
     * be thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testSetMaxConnections_int_1() throws IOException {
        startServer();

        try {
            testedInstance.setMaxConnections(10);
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>addHandler(String, Handler)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> handler ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddHandler_String_Handler_1() {
        try {
            testedInstance.addHandler(null, HANDLER);
            fail("NullPointerException should be thrown in response to NULL handler ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>addHandler(String, Handler)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> handler and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testAddHandler_String_Handler_2() {
        try {
            testedInstance.addHandler(HANDLER_ID, null);
            fail("NullPointerException should be thrown in response to NULL handler");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>addHandler(String, Handler)</code> method for proper behavior when being used inapproprietly.
     * Attempts to set the new address after the server is started and expects the <code>IllegalStateException</code> to
     * be thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testAddHandler_String_Handler_3() throws IOException {
        startServer();

        try {
            testedInstance.addHandler("new handler", new KeepAliveHandler());
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>removeHandler(String)</code> method for proper handling invalid input data. Passes the <code>
     * null</code> handler ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testRemoveHandler_String_1() {
        try {
            testedInstance.removeHandler(null);
            fail("NullPointerException should be thrown in response to NULL handler ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>removeHandler(String)</code> method for proper behavior when being used inapproprietly.
     * Attempts to set the new address after the server is started and expects the <code>IllegalStateException</code> to
     * be thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testRemoveHandler_String_2() throws IOException {
        startServer();

        try {
            testedInstance.removeHandler("new handler");
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>clearHandlers()</code> method for proper behavior when being used inapproprietly. Attempts to
     * set the new address after the server is started and expects the <code>IllegalStateException</code> to be thrown.
     * </p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testClearHandlers_1() throws IOException {
        startServer();

        try {
            testedInstance.clearHandlers();
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>containsHandler(String)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> handler ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testContainsHandler_String_1() {
        try {
            testedInstance.containsHandler(null);
            fail("NullPointerException should be thrown in response to NULL handler ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>getHandler(String)</code> method for proper handling invalid input data. Passes the <code>null
     * </code> handler ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testGetHandler_String_1() {
        try {
            testedInstance.getHandler(null);
            fail("NullPointerException should be thrown in response to NULL handler ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>start()</code> method for proper behavior when being used inapproprietly. Attempts to start
     * the server after the server is started and expects the <code>IllegalStateException</code> to be thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testStart_1() throws IOException {
        startServer();

        try {
            testedInstance.start();
            fail("IllegalStateException should be thrown since the server is already started");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>start()</code> method for proper behavior when being used inapproprietly. Attempts to bind
     * the server to a port which is already bound and expects the <code>IOException</code> to be thrown.</p>
     */
    public void testStart_2() {
        testedInstance.setPort(Integer.parseInt(getProperty("busy_port")));

        try {
            testedInstance.start();
        } catch (IOException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IOException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>stop()</code> method for proper behavior when being used inapproprietly. Attempts to stop
     * the server before the server is started and expects the <code>IllegalStateException</code> to be thrown.</p>
     */
    public void testStop_1() {
        try {
            testedInstance.stop();
            fail("IllegalStateException should be thrown since the server is not started yet");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendResponse(String, Message)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> connection ID and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testSendResponse_String_Message_1() {
        try {
            testedInstance.sendResponse(null, MESSAGE);
            fail("NullPointerException should be thrown in response to NULL connection ID");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendResponse(String, Message)</code> method for proper handling invalid input data. Passes the
     * <code>null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testSendResponse_String_Message_2() {
        try {
            testedInstance.sendResponse(CONNECTION_ID, null);
            fail("NullPointerException should be thrown in response to NULL response messsage");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendResponse(String, Message)</code> method for proper handling invalid input data. Passes the
     * ID of non-existing connection and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     *
     * @throws IOException if a tested server fails to start up. Such an error should not be interpreted as test
     *         failure.
     */
    public void testSendResponse_String_Message_3() throws IOException {
        startServer();

        try {
            testedInstance.sendResponse(NON_EXISTING_CONNECTION_ID, MESSAGE);
            fail("IllegalArgumentException should be thrown in response to non-existing connection ID");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendResponse(String, Message)</code> method for proper behavior when being used
     * inapproprietly. Attempts to send a response before the server is started and expects the <code>
     * IllegalStateException</code> to be thrown.</p>
     */
    public void testSendResponse_String_Message_4() {
        try {
            testedInstance.sendResponse(CONNECTION_ID, MESSAGE);
            fail("IllegalStateException should be thrown since the server is not started yet");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }
}
