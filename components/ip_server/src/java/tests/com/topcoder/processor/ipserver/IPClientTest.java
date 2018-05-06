/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import junit.framework.TestCase;

import java.io.IOException;

import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;

/**
 * <p>
 * Unit test cases for IPClient.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPClientTest extends TestCase {
    /** The ConfigHelper instance used for testing. */
    protected ConfigHelper helper;

    /** The address used for testing. */
    protected String address;

    /** The port used for testing. */
    protected int port;

    /** The IPServer used for testing. */
    protected IPServer server;

    /** The IPClient instance used for testing. */
    protected IPClient client = null;

    /** The handler Id used for test. */
    private final String handlerId = KeepAliveHandler.KEEP_ALIVE_ID;

    /** The request used for testing. */
    private Message request;

    /**
     * Initialize the fields except client.
     * @throws Exception to JUnit
     */
    protected void initExceptClient() throws Exception {
        ConfigHelper.loadNamespaces();

        this.helper = ConfigHelper.getInstance();
        this.address = helper.getAddress();
        this.port = helper.getPort();
        this.server = helper.getIPServer();
        ConfigHelper.startServer(this.server);

        this.request = new DefaultMessageFactory(
            ConfigHelper.MESSAGE_FACTORY_NAMESPACE).getMessage("simple", handlerId, "requestId");
    }

    /**
     * SetUp rountine, create IPClient instance for testing.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        initExceptClient();

        this.client = new IPClient(this.address, this.port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        this.client.connect();
        assertTrue("Fails to connect to server.", client.isConnected());
    }

    /**
     * Make sure the test client is disconnect with server then set to null and stop the test server.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        // Make sure the test client is disconnect with server and set to null
        try {
            if (this.client.isConnected()) {
                this.client.disconnect();
            }
        } catch (Exception e) {
            //Ignore
        }

        this.client = null;

        // Stop the test server
        ConfigHelper.stopServer(this.server);
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Test of Constructor with correct address, port.
     */
    public void testConstructor() {
        assertNotNull("Fails to create IPClient instance with correct address, port.", this.client);
    }

    /**
     * Test of Constructor with null address, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorWithNullAddress() throws Exception {
        try {
            new IPClient(null, this.port, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given address is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of Constructor with null namespace, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorWithNullNamespace() throws Exception {
        try {
            new IPClient("127.0.0.1", this.port, null);
            fail("The given namespace is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of Constructor with empty namespace, IAE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorWithEmptyNamespace() throws Exception {
        try {
            new IPClient("127.0.0.1", this.port, "    ");
            fail("The given namespace is empty.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of Constructor with negative port, IAE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorWithNegativePort() throws Exception {
        try {
            new IPClient(this.address, -1, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given port is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of Constructor with large port that larger than 65535, IAE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorWithLargePort() throws Exception {
        try {
            new IPClient(this.address, 65536, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given port is larger than 65535.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of getAddress will retrieve address that set in constructor.
     */
    public void testGetAddressSetInConstructor() {
        assertEquals("Fails to get address.", this.address, this.client.getAddress());
    }

    /**
     * Test of getAddress will retrieve address that set by setAddress method.
     * @throws IOException to JUnit
     */
    public void testGetAddressSetInSetter() throws IOException {
        String anotherAddress = "AnotherAddress";

        // Make client disconnect with server for below testing
        this.client.disconnect();

        this.client.setAddress(anotherAddress);
        assertEquals("Fails to get address.", anotherAddress, this.client.getAddress());
    }

    /**
     * Test of setAddress will set address that can be correctly retrieved by getAddress method.
     * @throws IOException to JUnit
     */
    public void testSetAddress() throws IOException {
        String anotherAddress = "AnotherAddress";

        // Make client disconnect with server for below testing
        this.client.disconnect();

        this.client.setAddress(anotherAddress);
        assertEquals("Fails to get address.", anotherAddress, this.client.getAddress());
    }

    /**
     * Test of setAddress with null address, NPE is expected.
     */
    public void testSetAddressWithNullAddress() {
        try {
            this.client.setAddress(null);
            fail("The given address is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of setAddress while client is connected with server, IllegalStateException is expected.
     */
    public void testSetAddressWithConnected() {
        try {
            this.client.setAddress(this.address);
            fail("The client is already connected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of getPort will retrieve port that set in constructor.
     */
    public void testGetPortSetInConstructor() {
        assertEquals("Fails to get port.", this.port, this.client.getPort());
    }

    /**
     * Test of getPort will retrieve port that set in constructor.
     * @throws IOException to JUnit
     */
    public void testGetPortSetInSetter() throws IOException {
        int anotherPort = 2000;

        // Make client disconnect with server for below testing
        this.client.disconnect();

        this.client.setPort(anotherPort);
        assertEquals("Fails to get port.", anotherPort, this.client.getPort());
    }

    /**
     * Test of setPort will set port that will be properly retrieved by getPort.
     * @throws IOException to JUnit
     */
    public void testSetPort() throws IOException {
        // Make sure the client is disconnected with server for testing
        this.client.disconnect();

        final int anotherPort = 2000;
        this.client.setPort(anotherPort);
        assertEquals("Fails to set port.", anotherPort, this.client.getPort());
    }

    /**
     * Test of setPort with negative port, IAE is expected.
     */
    public void testSetPortWithNegativePort() {
        try {
            this.client.setPort(-1);
            fail("The given port is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of setPort with large port that larger than 65535, IAE is expected.
     */
    public void testSetPortWithLargePort() {
        try {
            this.client.setPort(65536);
            fail("The given port is larger than 65535.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of setPort while client is connected with server, IllegalStateException is expected.
     */
    public void testSetPortWithConnected() {
        try {
            this.client.setPort(this.port);
            fail("The client is already connected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of connect will make the client is connected with server, isConnected will return false after this
     * invoking.
     */
    public void testConnect() {
        // This client already be connected with server in setUp rountine
        assertTrue("Fails to connect with server.", this.client.isConnected());
    }

    /**
     * Test of connect while client is connected with server, IllegalStateException is expected.
     *
     * @throws IOException to JUnit
     */
    public void testConnectWithConnected() throws IOException {
        try {
            this.client.connect();
            fail("The client is already connected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of disconnect will make the client disconnect with the server, isconnected will return false after
     * invoking.
     *
     * @throws IOException to JUnit
     */
    public void testDisconnect() throws IOException {
        // Make client disconnect with server for below testing
        this.client.disconnect();
        assertFalse("Fails to disconnect this client.", this.client.isConnected());
    }

    /**
     * Test of disconnect while client is disconnected with server, IllegalStateException is expected.
     *
     * @throws IOException to JUnit
     */
    public void testDisconnectWithDisconnected() throws IOException {
        // Make client disconnect with server for below testing
        this.client.disconnect();

        try {
            this.client.disconnect();
            fail("The client is already disconnected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of isConnected will retrieve true while client is connected with server.
     */
    public void testIsConnectedWithConnected() {
        assertTrue(this.client.isConnected());
    }

    /**
     * Test of isConnected will retrieve false while client is disconnected with server.
     *
     * @throws IOException to JUnit
     */
    public void testIsConnectedWithDisconnected() throws IOException {
        // Make client disconnect with server for below testing
        this.client.disconnect();

        assertFalse(this.client.isConnected());
    }

    /**
     * Test of sendRequest will sendRequest to server and will getResponse by recieveReponse method.
     * @throws Exception to JUnit
     */
    public void testSendRequest() throws Exception {
        this.client.sendRequest(request);
        Message response = this.client.receiveResponse(request.getRequestId(), true);
        assertNotNull("Fails to get response.", response);
        assertEquals("Fails to get correct response", request.getRequestId(), response.getRequestId());
    }

    /**
     * Test of sendRequest with null request, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testSendRequestWithNullRequest() throws Exception {
        try {
            this.client.sendRequest(null);
            fail("The given request is null");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of sendRequest while client is disconnected with server, IllegalStateException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testSendRequestWithDisconnected() throws Exception {
        // Make client disconnect with server for below testing
        this.client.disconnect();

        try {
            this.client.sendRequest(this.request);
            fail("The client is disconnected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of receiveResponse will receive response from server with non-blocking mode.
     *
     * Maybe we cannot recieve response immediately after send request. But we can receieve after a while.
     *
     * @throws Exception to JUnit
     */
    public void testReceiveResponseWithNonBlockingMode() throws Exception {
        this.client.sendRequest(request);
        Message response = this.client.receiveResponse(false);

        // It's normal not to recieve response immediately after send request.
        if (response == null) {
            Thread.sleep(1000);
            response = this.client.receiveResponse(false);
        }

        assertNotNull("Fails to get response in non-blocking mode.", response);
        assertEquals("Fails to get correct response", request.getRequestId(), response.getRequestId());
    }

    /**
     * Test of receiveResponse will receive response from server with blocking mode.
     *
     * @throws Exception to JUnit
     */
    public void testReceiveResponseWithBlockingMode() throws Exception {
        this.client.sendRequest(request);
        Message response = this.client.receiveResponse(true);

        assertNotNull("Fails to get response in blocking mode.", response);
        assertEquals("Fails to get correct response", request.getRequestId(), response.getRequestId());
    }

    /**
     * Test of receiveResponse while client is disconnected with server, IllegalStateException is expected.
     *
     * @throws IOException to JUnit
     */
    public void testReceiveResponseOneArgsWithDisconnected()
        throws IOException {
        // Make client disconnect with server for below testing
        this.client.disconnect();

        try {
            this.client.receiveResponse(false);
            fail("The client is disconnected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of receiveResponse will receive response from server with non-blocking mode.
     *
     * Maybe we cannot recieve response immediately after send request. But we can receieve after a while.
     *
     * @throws Exception to JUnit
     */
    public void testReceiveResponseWithRequestIdNonBlockingMode() throws Exception {
        this.client.sendRequest(request);
        Message response = this.client.receiveResponse(request.getRequestId(), false);

        // It's normal not to recieve response immediately after send request.
        if (response == null) {
            Thread.sleep(1000);
            response = this.client.receiveResponse(request.getRequestId(), false);
        }

        assertNotNull("Fails to get response with requestId in non-blocking mode.", response);
        assertEquals("Fails to get correct response with requestId", request.getRequestId(), response.getRequestId());
    }

    /**
     * Test of receiveResponse will receive response from server with blocking mode.
     *
     * @throws Exception to JUnit
     */
    public void testReceiveResponseWithRequestIdBlockingMode() throws Exception {
        this.client.sendRequest(request);
        Message response = this.client.receiveResponse(request.getRequestId(), true);

        assertNotNull("Fails to get response with requestId in blocking mode.", response);
        assertEquals("Fails to get correct response with requestId", request.getRequestId(), response.getRequestId());
    }

    /**
     * Test of receiveResponse with null requestId, NPE is expected.
     *
     * @throws IOException to JUnit
     */
    public void testReceiveResponseTwoArgsWithNullRequestId()
        throws IOException {
        try {
            this.client.receiveResponse(null, false);
            fail("The given requestId is null");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of receiveResponse while client is disconnected with server, IllegalStateException is expected.
     *
     * @throws IOException to JUnit
     */
    public void testReceiveResponseTwoArgsWithDisconnected()
        throws IOException {
        // Invoking disconnect while client is connected with server
        this.client.disconnect();

        try {
            this.client.receiveResponse(this.request.getRequestId(), false);
            fail("The client is disconnected with server");
        } catch (IllegalStateException e) {
            // good
        }
    }
}
