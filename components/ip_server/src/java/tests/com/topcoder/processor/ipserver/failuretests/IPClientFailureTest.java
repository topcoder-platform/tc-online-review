/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.processor.ipserver.failuretests;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.message.Message;

import java.io.IOException;

/**
 * <p>A failure test for <code>IPClient</code> class. Tests the proper handling of
 * invalid input data by the methods. Passes the invalid arguments to the methods and expects the
 * appropriate exception to be thrown.</p>
 *
 * @author isv, brain_cn
 * @version 2.0
 */
public class IPClientFailureTest extends FailureTestCase {

    /**
     * <p>A <code>String</code> providing the sample address to be used for testing.</p>
     */
    public static final String ADDRESS = "127.0.0.1";

    /**
     * <p>An <code>int</code> providing the sample valid port to be used for testing.</p>
     */
    public static final int PORT = 1024;

    /**
     * <p>A <code>String</code> providing the IP address of non-existing server</p>
     */
    public static final String INVALID_SERVER = "";

    /**
     * <p>A <code>Message</code> to be sent to server.</p>
     */
    public static final Message MESSAGE = new MockMessage("handler1", "01");

    /**
     * <p>An instance of <code>IPClient</code> which is tested.</p>
     */
    private IPClient testedInstance = null;

    /**
     * <p>Gets the test suite for <code>IPClient</code> class.</p>
     *
     * @return a <code>TestSuite</code> providing the tests for <code>IPClient</code> class.
     */
    public static Test suite() {
        return new TestSuite(IPClientFailureTest.class);
    }

    /**
     * <p>Sets up the fixture. This method is called before a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        loadNamespaces();
        startServer();
        testedInstance = new IPClient(getProperty("host"), Integer.parseInt(getProperty("server_port")),
                MESSAGE_NAMESPACE);
    }

    /**
     * <p>Tears down the fixture. This method is called after a test is executed.</p>
     *
     * @throws Exception if any error occurs.
     */
    protected void tearDown() throws Exception {
        try {
            if (testedInstance.isConnected()) {
                testedInstance.disconnect();
            }
        } finally {
            testedInstance = null;
            stopServer();
        }
    }

    /**
     * <p>Tests the <code>IPClient(String, int)</code> constructor for proper handling invalid input data. Passes the
     * <code>null</code> address and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_1() {
        try {
            new IPClient(null, PORT, MESSAGE_NAMESPACE);
            fail("NullPointerException should be thrown in response to NULL address");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPClient(String, int)</code> constructor for proper handling invalid input data. Passes the
     * negative port and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_2() {
        try {
            new IPClient(ADDRESS, -1, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to negative port");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>IPClient(String, int)</code> constructor for proper handling invalid input data. Passes the
     * port out of valid range and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testConstructor_String_int_3() {
        try {
            new IPClient(ADDRESS, 65536, MESSAGE_NAMESPACE);
            fail("IllegalArgumentException should be thrown in response to port not in range from 0 to 65535");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : " + e);
        }
    }

    /**
     * <p>Tests the <code>setAddress(String)</code> method for proper handling invalid input data. Passes the <code>null
     * </code> address and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testSetAddress_String_1() {
        try {
            testedInstance.setAddress(null);
            fail("NullPointerException should be thrown in response to NULL address");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setAddress(String)</code> method for proper behavior when being used inappropriately. Attempts
     * to set the address after the client is connected to a server and expects the <code>IllegalStateException</code>
     * to be thrown.</p>
     *
     * @throws IOException if the tested client can not connect to a server. Such an error should not be interpreted as
     *         test failure.
     */
    public void testSetAddress_String_2() throws IOException {
        testedInstance.connect();
        try {
            testedInstance.setAddress("127.0.0.1");
            fail("IllegalStateException should be thrown since the client is already connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setPort(int)</code> method for proper handling invalid input data. Passes the negative port
     * and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testSetPort_int_1() {
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
     * <p>Tests the <code>setPort(int)</code> method for proper handling invalid input data. Passes the port which is
     * out of valid range and expects the <code>IllegalArgumentException</code> to be thrown.</p>
     */
    public void testSetPort_int_2() {
        try {
            testedInstance.setPort(65536);
            fail("IllegalArgumentException should be thrown in response to port not in range from 0 to 65535");
        } catch (IllegalArgumentException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalArgumentException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>setPort(int)</code> method for proper behavior when being used inappropriately. Attempts
     * to set the port after the client is connected to a server and expects the <code>IllegalStateException</code>
     * to be thrown.</p>
     *
     * @throws IOException if the tested client can not connect to a server. Such an error should not be interpreted as
     *         test failure.
     */
    public void testSetPort_int_3() throws IOException {
        testedInstance.connect();
        try {
            testedInstance.setPort(8080);
            fail("IllegalStateException should be thrown since the client is already connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>connect()</code> method for proper behavior when being used inappropriately. Attempts
     * to conenct after the client is connected to a server and expects the <code>IllegalStateException</code>
     * to be thrown.</p>
     *
     * @throws IOException if the tested client can not connect to a server. Such an error should not be interpreted as
     *         test failure.
     */
    public void testConnect_1() throws IOException {
        testedInstance.connect();
        try {
            testedInstance.connect();
            fail("IllegalStateException should be thrown since the client is already connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>connect()</code> method for proper behavior when being used inappropriately. Attempts
     * to connect to a non-serviced port and expects the <code>IOException</code> to be thrown.</p>
     */
    public void testConnect_2() {
        try {
            testedInstance.setPort(Integer.parseInt(getProperty("unserviced_port")));
            testedInstance.connect();
            fail("IOException should be thrown since a network error occurs while connecting to the server");
        } catch (IOException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IOException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>disconnect()</code> method for proper behavior when being used inappropriately. Attempts
     * to disconnect before the client is connected and expects the <code>IllegalStateException</code> to be thrown.</p>
     */
    public void testDisconnect_1() {
        try {
            testedInstance.disconnect();
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendRequest(Message)</code> method for proper handling invalid input data. Passes the <code>
     * null</code> message and expects the <code>NullPointerException</code> to be thrown.</p>
     */
    public void testSendRequest_Message_1() {
        try {
            testedInstance.sendRequest(null);
            fail("NullPointerException should be thrown in response to NULL request");
        } catch (NullPointerException e) {
            // expected behavior
        } catch (Exception e) {
            fail("NullPointerException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>sendRequest(Message)</code> method for proper behavior when being used inappropriately.
     * Attempts to send a request before the client is connected and expects the <code>IllegalStateException</code> to
     * be thrown.</p>
     */
    public void testSendRequest_Message_2() {
        try {
            testedInstance.sendRequest(MESSAGE);
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>receiveResponse(boolean)</code> method for proper behavior when being used inappropriately.
     * Attempts to receive a response before the client is connected and expects the <code>IllegalStateException</code>
     * to be thrown. Uses blocking mode.</p>
     */
    public void testReceiveResponse_boolean_1() {
        try {
            testedInstance.receiveResponse(true);
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>receiveResponse(boolean)</code> method for proper behavior when being used inappropriately.
     * Attempts to receive a response before the client is connected and expects the <code>IllegalStateException</code>
     * to be thrown. Uses non-blocking mode.</p>
     */
    public void testReceiveResponse_boolean_2() {
        try {
            testedInstance.receiveResponse(false);
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>receiveResponse(String, boolean)</code> method for proper behavior when being used
     * inappropriately. Attempts to receive a response before the client is connected and expects the <code>
     * IllegalStateException</code> to be thrown. Uses blocking mode.</p>
     */
    public void testReceiveResponse_String_boolean_1() {
        try {
            testedInstance.receiveResponse(MESSAGE.getRequestId(), true);
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }

    /**
     * <p>Tests the <code>receiveResponse(String, boolean)</code> method for proper behavior when being used
     * inappropriately. Attempts to receive a response before the client is connected and expects the <code>
     * IllegalStateException</code> to be thrown. Uses non-blocking mode.</p>
     */
    public void testReceiveResponse_String_boolean_2() {
        try {
            testedInstance.receiveResponse(MESSAGE.getRequestId(), false);
            fail("IllegalStateException should be thrown since the client is not connected to the server");
        } catch (IllegalStateException e) {
            // expected behavior
        } catch (Exception e) {
            fail("IllegalStateException was expected but the original exception is : "+ e);
        }
    }
}
