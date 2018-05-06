/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.IOException;
import java.nio.channels.Selector;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.processor.ipserver.keepalive.KeepAliveIPClient;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageFactory;

/**
 * <p>
 * Unit test cases for IPServer.
 * </p>
 * @author zsudraco, FireIce
 * @version 2.0.1
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPServerTest extends TestCase {
    /** The ConfigHelper instance used for testing. */
    private ConfigHelper helper;

    /** The IPServer instance used for testing. */
    private IPServer server;

    /** The Handler instance used for testing. */
    private Handler handler = new Server1Handler1(100);

    /** The address used for testing. */
    private String address;

    /** The port used for testing. */
    private int port;

    /** The maxConnections used for testing. */
    private int maxConnections;

    /** The backlog used for testing. */
    private int backlog = 100;

    /** The response used for testing. */
    private Message response;

    /** The message factory to create messages. */
    private MessageFactory messageFactory;

    /**
     * SetUp rountine, Make sure that the test server is started for testing.
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();

        helper = ConfigHelper.getInstance();
        server = helper.getIPServer();
        address = helper.getAddress();
        port = helper.getPort();
        maxConnections = helper.getMaxConnections();
        ConfigHelper.startServer(this.server);

        messageFactory = new DefaultMessageFactory(ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        response = messageFactory.getMessage("simple", "handlerId", "requestId");
    }

    /**
     * Make sure that the test server is stopped after testing.
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigHelper.stopServer(this.server);
        server.clearHandlers();
        ConfigHelper.releaseNamespaces();
    }

    /**
     * <p>
     * Test the public void run() method.
     * </p>
     * <p>
     * No client connected yet.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testRunNoClients() throws Exception {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        // add handler.
        server.addHandler(KeepAliveHandler.KEEP_ALIVE_ID, new KeepAliveHandler());

        // start server
        ConfigHelper.startServer(this.server);

        Map channels = (Map) ConfigHelper.getFieldValue(server, "connectionIdToChannel");

        assertTrue("channels is empty now.", channels.isEmpty());
    }

    /**
     * <p>
     * Test the public void run() method.
     * </p>
     * <p>
     * one client connected yet.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testRunOneClientConnected() throws Exception {

        // create client
        KeepAliveIPClient client = new KeepAliveIPClient(helper.getAddress(), helper.getPort(), 1000,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        client.connect();

        client.keepAlive();

        Thread.sleep(1000);

        Map channels = (Map) ConfigHelper.getFieldValue(server, "connectionIdToChannel");
        assertTrue("channels should contains 1 now.", channels.size() == 1);

        client.disconnect();
    }

    /**
     * <p>
     * Test the public void run() method.
     * </p>
     * <p>
     * one client connected yet.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testRunOneClientDisconnected() throws Exception {

        // create client
        KeepAliveIPClient client = new KeepAliveIPClient(helper.getAddress(), helper.getPort(), 1000,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        client.connect();
        client.keepAlive();
        Thread.sleep(1000);
        client.disconnect();
        Thread.sleep(10000);
        Map channels = (Map) ConfigHelper.getFieldValue(server, "connectionIdToChannel");
        assertTrue("channels should be empty now.", channels.isEmpty());
    }

    /**
     * <p>
     * Test the public void run() method.
     * </p>
     * <p>
     * one client connected yet.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testRunFiveClientConnected() throws Exception {

        // create clients
        KeepAliveIPClient[] client = new KeepAliveIPClient[10];

        for (int i = 0; i < 10; i++) {
            client[i] = new KeepAliveIPClient(helper.getAddress(), helper.getPort(), 1000,
                    ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            client[i].connect();
            client[i].keepAlive();
        }
        Thread.sleep(10000);
        Map channels = (Map) ConfigHelper.getFieldValue(server, "connectionIdToChannel");
        assertTrue("channels should contains 10 now.", channels.size() == 10);
        Thread.sleep(10000);
        for (int i = 0; i < 10; i++) {
            client[i].disconnect();
        }

    }

    /**
     * <p>
     * Test the public void run() method.
     * </p>
     * <p>
     * one client connected yet.
     * </p>
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    public void testRunFiveClientDisconnected() throws Exception {
        // create clients
        KeepAliveIPClient[] client = new KeepAliveIPClient[10];

        for (int i = 0; i < 10; i++) {
            client[i] = new KeepAliveIPClient(helper.getAddress(), helper.getPort(), 1000,
                    ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            client[i].connect();
            client[i].keepAlive();
        }

        Thread.sleep(10000);

        // close connections.
        for (int i = 0; i < 10; i++) {
            client[i].disconnect();
        }

        Thread.sleep(10000);
        Map channels = (Map) ConfigHelper.getFieldValue(server, "connectionIdToChannel");
        assertTrue("channels should be empty now.", channels.isEmpty());
    }

    /**
     * Test of Constructor with correct address, port, maxConnections.
     */
    public void testConstructor() {
        assertNotNull("Fails to create IPServer instance with correct address, port, maxConnections.", this.server);
    }

    /**
     * Test of Constructor with negative maxConnections, IAE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorWithNegativeMaxConnections() throws Exception {
        try {
            new IPServer(this.address, this.port, -1, this.backlog, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given maxConnections is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of Constructor with null namespace, NPE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorWithNullNamespace() throws Exception {
        try {
            new IPServer(this.address, this.port, 10, this.backlog, null);
            fail("The namespace is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of Constructor with empty namespace, IAE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorWithEmptyNamespace() throws Exception {
        try {
            new IPServer(this.address, this.port, 10, this.backlog, "   ");
            fail("The namespace is empty.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of Constructor with negative port, IAE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorWithNegativePort() throws Exception {
        try {
            new IPServer(this.address, -1, this.maxConnections, this.backlog, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given port is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of Constructor with large port that larger than 65535, IAE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testConstructorWithLargePort() throws Exception {
        try {
            new IPServer(this.address, 65536, this.maxConnections, this.backlog, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given port is larger than 65535.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of getAddress will retrieve the address that set in Constructor.
     * @throws Exception
     *             to JUnit
     */
    public void testGetAddressSetInConstructor() throws Exception {
        String testAddress = "testAddress";

        // Set address
        IPServer server1 = new IPServer(testAddress, this.port, 0, this.backlog, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        assertEquals("Fails to get address.", testAddress, server1.getAddress());
    }

    /**
     * Test of getAddress will retrieve the address that set in Setter method.
     * @throws Exception
     *             to JUnit
     */
    public void testGetAddressSetInSetter() throws Exception {
        String testAddress = "testAddress";
        IPServer server1 = new IPServer(this.address, this.port, 0, this.backlog,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // Set address
        server1.setAddress(testAddress);
        assertEquals("Fails to get address.", testAddress, server1.getAddress());
    }

    /**
     * Test of setAddress will set the address that will be properly retrieve by getAddress.
     * @throws Exception
     *             to JUnit
     */
    public void testSetAddress() throws Exception {
        String testAddress = "testAddress";
        IPServer server1 = new IPServer(this.address, this.port, 0, this.backlog,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // Set address
        server1.setAddress(testAddress);
        assertEquals("Fails to get address.", testAddress, server1.getAddress());
    }

    /**
     * Test of setAddress while server is started, IllegalStateException is expected.
     */
    public void testSetAddressWithStarted() {
        try {
            this.server.setAddress(this.address);
            fail("The server is started");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of getPort will retrieve the port that set in Constructor.
     * @throws Exception
     *             to JUnit
     */
    public void testGetPortSetInConstructor() throws Exception {
        int testPort = 1234;
        IPServer server1 = new IPServer(this.address, testPort, 0, this.backlog, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        assertEquals("Fails to get port.", testPort, server1.getPort());
    }

    /**
     * Test of getPort will retrieve the port that set in Setter method.
     * @throws Exception
     *             to JUnit
     */
    public void testGetPortSetInSetter() throws Exception {
        int testPort = 1234;
        IPServer server1 = new IPServer(this.address, this.port, 0, this.backlog,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // Set port
        server1.setPort(testPort);
        assertEquals("Fails to get port.", testPort, server1.getPort());
    }

    /**
     * Test of setPort will set the port that will be retrieved by getPort method.
     * @throws Exception
     *             to JUnit
     */
    public void testSetPort() throws Exception {
        int testPort = 1234;
        IPServer server1 = new IPServer(this.address, this.port, 0, this.backlog,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // Set port
        server1.setPort(testPort);
        assertEquals("Fails to get port.", testPort, server1.getPort());
    }

    /**
     * Test of setPort with negative port, IAE is expected.
     */
    public void testSetPortWithNegativePort() {
        try {
            this.server.setPort(-1);
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
            this.server.setPort(65536);
            fail("The given port is larger than 65535.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of setPort while server is started, IllegalStateException is expected.
     */
    public void testSetPortWithStarted() {
        try {
            this.server.setPort(this.port);
            fail("The server is started");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of getMaxConnections will retrieve the maxConnections that set in Constructor.
     * @throws Exception
     *             to JUnit
     */
    public void testGetMaxConnections() throws Exception {
        int testMaxConnections = 1234;
        IPServer server1 = new IPServer(this.address, this.port, testMaxConnections, this.backlog,
                ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        assertEquals("Fails to get maxConnections.", testMaxConnections, server1.getMaxConnections());
    }

    /**
     * Test of setMaxConnections with negative maxConnections, IAE is expected.
     */
    public void testSetMaxConnectionsWithNegativeMaxConnections() {
        try {
            this.server.setMaxConnections(-1);
            fail("The given maxConnections is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of setMaxConnections while server is started, IllegalStateException is expected.
     */
    public void testSetMaxConnectionsWithStarted() {
        try {
            this.server.setMaxConnections(this.maxConnections);
            fail("The server is started.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of addHandler will return true if add an non-existing handlerId.
     */
    public void testAddHandlerNonExistingHandlerId() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handlerId";

        // Make sure the test handler does not exist
        this.server.removeHandler(handlerId);

        assertTrue("Fails to add the handler.", this.server.addHandler(handlerId, new Server1Handler1(1)));
    }

    /**
     * Test of addHandler will return false if adding an existing handlerId, and don't change the existing handler.
     */
    public void testAddHandlerExistingHandlerId() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handlerId";

        // Make sure the test handler exists in server
        Handler handler1 = new Server1Handler1(1);
        this.server.removeHandler(handlerId);
        this.server.addHandler(handlerId, handler1);

        assertFalse("Fails to return false to add existing handlerId.", this.server.addHandler(handlerId,
                new Server1Handler1(1)));

        assertEquals("Fails to keep the existing handler.", handler1, this.server.getHandler(handlerId));
    }

    /**
     * Test of addHandler with null handler id, NPE is expected.
     */
    public void testAddHandlerWithNullHandlerId() {
        try {
            this.server.addHandler(null, this.handler);
            fail("The given handler id is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of addHandler with null handler, NPE is expected.
     */
    public void testAddHandlerWithNullHandler() {
        try {
            this.server.addHandler(this.response.getHandlerId(), null);
            fail("The given handler id is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of addHandler while server is started, IllegalStateException is expected.
     */
    public void testAddHandlerWithStartedServer() {
        try {
            this.server.addHandler(this.response.getHandlerId(), this.handler);
            fail("The server is started.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of removeHandler will return true if remove the existing handler with the given handlerId.
     */
    public void testRemoveHandlerExistingHander() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handlerId";
        this.server.addHandler(handlerId, new Server1Handler1(1));
        assertTrue("Fails to remove the existing handler.", this.server.removeHandler(handlerId));
    }

    /**
     * Test of removeHandler will return false if try to remove a non-existing handler with the given handlerId.
     */
    public void testRemoveHandlerNonExistingHandler() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handlerId";

        // Make sure the handler does not existing
        this.server.removeHandler(handlerId);
        assertFalse("Fails to return false for removing the non-existing handler.", this.server
                .removeHandler(handlerId));
    }

    /**
     * Test of removeHandler with null handler id, NPE is expected.
     */
    public void testRemoveHandlerWithNullHandlerId() {
        try {
            this.server.removeHandler(null);
            fail("The given handler id is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of removeHandler while server is started, IllegalStateException is expected.
     */
    public void testRemoveHandlerWithStarted() {
        try {
            this.server.removeHandler(this.response.getHandlerId());
            fail("The server is started.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of clearHandlers will clear all handlers from the test server, by testing the size of return handlerIds set
     * is 0.
     */
    public void testClearHandlers() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        // Add handlers for testing
        this.server.addHandler("handlerId1", new Server1Handler1(1));
        this.server.addHandler("handlerId2", new Server1Handler1(1));
        this.server.addHandler("handlerId3", new Server1Handler1(1));

        // clear all handlers
        this.server.clearHandlers();
        assertEquals("Fails to clear all handlers from the test server.", this.server.getHandlerIds().size(), 0);
    }

    /**
     * Test of clearHandlers while server is started, IllegalStateException is expected.
     */
    public void testClearHandlersWithStarted() {
        try {
            this.server.clearHandlers();
            fail("The server is started.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of containsHandler will return true if the handler exist with the given handlerId.
     */
    public void testContainsHandlerExistingHandler() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handler id";
        this.server.addHandler(handlerId, new Server1Handler1(1));
        assertTrue("Fails to return true for exist handler.", this.server.containsHandler(handlerId));
    }

    /**
     * Test of containsHandler will return false if the handler does not exist with the given handlerId.
     */
    public void testContainsHandlerNonExistingHandler() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId = "handler id";
        this.server.removeHandler(handlerId);
        assertFalse("Fails to return false for non-exist handler.", this.server.containsHandler(handlerId));
    }

    /**
     * Test of containsHandler with null handler id, NPE is expected.
     */
    public void testContainsHandlerWithNullHandlerId() {
        try {
            this.server.containsHandler(null);
            fail("The given handler id is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of getHandlersIds will return all handlerIds in the test server, and the return set is a copy.
     */
    public void testGetHandlerIds() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String handlerId1 = "handlerId1";
        String handlerId2 = "handlerId2";
        String handlerId3 = "handlerId3";

        // Add three handlers for testing
        this.server.addHandler(handlerId1, new Server1Handler1(1));
        this.server.addHandler(handlerId2, new Server1Handler1(2));
        this.server.addHandler(handlerId3, new Server1Handler1(3));

        // Get the handlerIds set
        Set set = this.server.getHandlerIds();
        assertTrue("Fails to contain handlerId1.", set.contains(handlerId1));
        assertTrue("Fails to contain handlerId2.", set.contains(handlerId2));
        assertTrue("Fails to contain handlerId3.", set.contains(handlerId3));
        set.clear();

        // clear the return set will not affect the handlerIds of the test server
        Set set1 = this.server.getHandlerIds();
        assertTrue("Fails to return a handlerIds set copy.", set1.contains(handlerId1));
        assertTrue("Fails to return a handlerIds set copy.", set1.contains(handlerId2));
        assertTrue("Fails to return a handlerIds set copy.", set1.contains(handlerId3));
    }

    /**
     * Test of getHandler will return the handler with given handlerId.
     */
    public void testGetHandlerWithExistHandler() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        Handler handler1 = new Server1Handler1(100);
        String testHandlerId = "test handler";

        // Make the handler does not exist for the given handler id
        this.server.removeHandler(testHandlerId);

        // add the handler for testing
        this.server.addHandler(testHandlerId, handler1);
        assertEquals("Fails to get handler with the given handler id.", handler1, this.server.getHandler(testHandlerId));
    }

    /**
     * Test of getHandler will return null handler with non-exist given handlerId.
     */
    public void testGetHandlerWithNonExistHandler() {
        // Make sure the test server is stopped for testing
        ConfigHelper.stopServer(this.server);

        String testHandlerId = "test handler";

        // Make the handler does not exist for the given handler id
        this.server.removeHandler(testHandlerId);
        assertNull("Fails to get null with non-exist given handler id.", this.server.getHandler(testHandlerId));
    }

    /**
     * Test of getHandler with null handler id, NPE is expected.
     */
    public void testGetHandlerWithNullHandlerId() {
        try {
            this.server.getHandler(null);
            fail("The given handler id is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of start will start the test server by testing if the isStarted will return true.
     * @throws IOException
     *             to JUnit
     */
    public void testStart() throws IOException {
        // Stop the test server first
        ConfigHelper.stopServer(this.server);

        // Start the server for testing
        this.server.start();
        assertTrue("Fails to start the test server.", this.server.isStarted());
    }

    /**
     * Test of start while server is started, IllegalStateException is expected.
     * @throws IOException
     *             to JUnit
     */
    public void testStartWithStarted() throws IOException {
        try {
            this.server.start();
            fail("The server is started.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of stop will stop the server by testing if isStarted method will return false in a short while.
     */
    public void testStop() {
        this.server.stop();

        // Wait a short time for the server stopping
        synchronized (this) {
            try {
                wait(1100);
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
        }

        // The server is stopped
        assertFalse("Fails to stop server.", this.server.isStarted());
    }

    /**
     * Test of stop while server is stopped, IllegalStateException is expected.
     */
    public void testStopWithStopped() {
        // Ensure the server is stopped
        ConfigHelper.stopServer(this.server);

        try {
            this.server.stop();
            fail("The server is stopped.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * Test of isStarted will return true while the server is started.
     */
    public void testIsStartedWhileStarted() {
        assertTrue("Fails to get started status.", this.server.isStarted());
    }

    /**
     * Test of isStarted will return false while the server is stopped.
     */
    public void testIsStartedWhileStopped() {
        ConfigHelper.stopServer(this.server);
        assertFalse("Fails to get started status.", this.server.isStarted());
    }

    /**
     * Test of sendResponse with null connectionId, NPE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testSendResponsWithNullConnectionId() throws Exception {
        try {
            this.server.sendResponse(null, this.response);
            fail("The given connectionId is null");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of sendResponse with null response, NPE is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testSendResponsWithNullResponse() throws Exception {
        try {
            this.server.sendResponse(ConfigHelper.CONNECTION_ID, null);
            fail("The given response is null");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of sendResponse while server is stopped, IllegalStateException is expected.
     * @throws Exception
     *             to JUnit
     */
    public void testSendResponsWithStopped() throws Exception {
        // Make sure the test server is started
        ConfigHelper.stopServer(this.server);

        try {
            this.server.sendResponse(ConfigHelper.CONNECTION_ID, this.response);
            fail("The server is stopped.");
        } catch (IllegalStateException e) {
            // good
        }
    }
}
