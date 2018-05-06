/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageFactory;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

import junit.framework.TestCase;

import java.io.IOException;

import java.util.Set;
import java.util.Map;

/**
 * <p>
 * Component demonstration for IP Server.
 * </p>
 *
 * <p>
 * This component implements a server ¨C client communication model over TCP/IP. The server is multithreaded and uses
 * the New Java I/O API for maximum performance. The server uses only one thread to handle listening for connections,
 * accepting and reading requests. The idea of the component is to provide TCP/IP communication services to
 * applications without exposing them to the Java socket API or to java.nio
 * </p>
 *
 * <p>
 * This demo is separated into the following parts.
 * </p>
 *
 * <p>
 * Demonstration for managing the message factory.
 * </p>
 *
 * <p>
 * Demonstration for the component Server side usage.
 * </p>
 *
 * <p>
 * Demonstration for the component Client side usage.
 * </p>
 *
 * <p>
 * Demonstration for how one client sends a message to multiple handlers/single handler.
 * </p>
 *
 * <p>
 * Demonstration for how multiple clients send message to multiple handlers.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPServerDemo extends TestCase {
    /**
     * The number of handlers to register into IPServer.
     */
    private static final int HANDLER_NUM = 10;

    /** The ConfigHelper singleton instance used for testing. */
    private ConfigHelper helper;

    /** The ipServer used for testing. */
    private IPServer ipServer;

    /** The handler used for testing. */
    private Handler handler1 = new KeepAliveHandler();

    /** The handlerName used for testing. */
    private String handlerId1 = "handler_demo";

    /**
     * The handler to register into IPServer.
     */
    private Handler[] handlers = new Handler[HANDLER_NUM];

    /** The message factory to create messages. */
    private MessageFactory messageFactory;

    /**
     * SetUp rountine, Make sure that the test server is started for testing.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();

        helper = ConfigHelper.getInstance();
        ipServer = helper.getIPServer();

        for (int i = 0; i < handlers.length; ++i) {
            handlers[i] = new KeepAliveHandler();
            ipServer.addHandler("handler" + i, handlers[i]);
        }
        ipServer.addHandler(handlerId1, handler1);
        ConfigHelper.startServer(ipServer);

        messageFactory = new DefaultMessageFactory(ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
    }

    /**
     * Make sure that the test server is stopped after testing.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigHelper.stopServer(ipServer);
        ipServer.clearHandlers();
        ConfigHelper.releaseNamespaces();
    }

    /**
     * This part demonstrates usage of message factory.
     *
     * @throws Exception to JUnit
     */
    public void testDemoMessageFactory() throws Exception {
        // get message
        Message message = messageFactory.getMessage("simple", "handler id", "request id");
        assertEquals("handler id", message.getHandlerId());
        assertEquals("request id", message.getRequestId());
        assertEquals("com.topcoder.processor.ipserver.message.serializable.SerializableMessageSerializer",
            message.getSerializerType());

        // serialize message
        byte[] data = messageFactory.serializeMessage(message);
        // deserialize message
        Message msg = messageFactory.deserializeMessage(data);
        // msg and message are same
        assertEquals(msg.getHandlerId(), message.getHandlerId());
        assertEquals(msg.getRequestId(), message.getRequestId());
        assertEquals(msg.getSerializerType(), message.getSerializerType());

        // add message type
        ((DefaultMessageFactory) messageFactory).add("new type", CustomMessage.class);

        // remove message type
        ((DefaultMessageFactory) messageFactory).remove("new type");

        // get message type
        Class cls = ((DefaultMessageFactory) messageFactory).get("simple");

        // test if there's a certain message type
        boolean res = ((DefaultMessageFactory) messageFactory).contains("new type");

        // get the mapping of message type names and message types
        Map all = ((DefaultMessageFactory) messageFactory).getMessageTypes();

        // clear all message types
        ((DefaultMessageFactory) messageFactory).clear();

        // shows the usage of CustomSerializableMessage and CustomSerializableMessageSerializable

        // add message type
        ((DefaultMessageFactory) messageFactory).add("custom", CustomSerializableMessage.class);

        message = messageFactory.getMessage("custom", "handler id", "request id");
        ((CustomSerializableMessage) message).setData((byte) 12);
        // serialize message
        data = messageFactory.serializeMessage(message);
        // deserialize message
        msg = messageFactory.deserializeMessage(data);
        // msg and message are same
        assertEquals(msg.getHandlerId(), message.getHandlerId());
        assertEquals(msg.getRequestId(), message.getRequestId());
        assertEquals(msg.getSerializerType(), message.getSerializerType());
        assertEquals(((CustomSerializableMessage) msg).getData(),
            ((CustomSerializableMessage) message).getData());
    }

    /**
     * This part demonstrates how the component Server side usage.
     *
     * @throws Exception to JUnit
     */
    public void testDemoServerSide() throws Exception {
        /////////////////////////////////////////////////////////////////////////////////////
        // Manage IP servers.
        // Get manager instance.
        IPServerManager manager = IPServerManager.getInstance();

        // Add an IP server.
        manager.addIPServer("serv1", new IPServer(null, 8080, 0, 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE));

        // Remove an IP server.
        manager.removeIPServer("serv1");

        // Clear all IP servers.
        manager.clearIPServers();

        // Check if an IP server is registered.
        boolean contained = manager.containsIPServer("serv1");

        // ContainsIPServer should return false after invoking clearIPServers.
        assertFalse("Fails to clear all IPServer.", contained);

        // Get the names of all registered IP servers.
        Set serverNames = manager.getIPServerNames();

        assertNotSame("Fails to return set copy of ipServerNames.", serverNames, manager.getIPServerNames());

        // Get an IP server by name.
        IPServer server = manager.getIPServer("serv1");

        /////////////////////////////////////////////////////////////////////////////////////
        // Create and configure IP servers.
        // Create an IP server.
        server = new IPServer(null, 8081, 10, 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        // Get its address.
        String address = server.getAddress();

        // Get its port.
        int port = server.getPort();

        // Get the maximum number of connections.
        int maxConn = server.getMaxConnections();

        // Set bind address.
        server.setAddress(address);

        // Set listening port.
        server.setPort(port);

        // Set maximum number of connections.
        server.setMaxConnections(maxConn);

        /////////////////////////////////////////////////////////////////////////////////////
        // Manage handlers
        // Add the custom handler with 10 simultaneous requests
        server.addHandler("h1", new MyHandler(10));

        // Add a keep alive handler
        server.addHandler(KeepAliveHandler.KEEP_ALIVE_ID, new KeepAliveHandler());

        // Remove a handler
        server.removeHandler("h1");

        // Clear all handlers
        server.clearHandlers();

        // Check if a handler is contained
        contained = server.containsHandler("h1");

        // Get the ids of all handlers
        Set handlerIds = server.getHandlerIds();
        assertNotSame("Fails to return set copy of handlerIds.", handlerIds, server.getHandlerIds());

        // Get a handler by id
        Handler handler = server.getHandler("h1");
        assertNull("Fails to remove handler with the given handlerId.", handler);

        /////////////////////////////////////////////////////////////////////////////////////
        // Start and stop a server
        // Start the server
        server.start();

        // Check the status of the server
        boolean started = server.isStarted();
        assertTrue("Fails to start the server.", started);

        // Stop the server
        server.stop();
    }

    /**
     * This part demonstrates how the component Client side usage.
     *
     * @throws Exception to JUnit
     */
    public void testDemoClientSide() throws Exception {
        /////////////////////////////////////////////////////////////////////////////////////
        // Create and configure client
        // Create a client
        IPClient client = new IPClient(helper.getAddress(), helper.getPort(), ConfigHelper.MESSAGE_FACTORY_NAMESPACE);

        /////////////////////////////////////////////////////////////////////////////////////
        // Connect and disconnect client
        // Connect to server
        client.connect();

        // Check connected status
        assertTrue("Fails to connect to the test server.", client.isConnected());

        /////////////////////////////////////////////////////////////////////////////////////
        // Send requests and receive responses
        // Send three requests with different request ids
        String requestId1 = "req1112";
        String requestId2 = "req1115";
        String requestId3 = "req1145";

        client.sendRequest(messageFactory.getMessage("simple", this.handlerId1, requestId1));
        client.sendRequest(messageFactory.getMessage("simple", this.handlerId1, requestId2));
        client.sendRequest(messageFactory.getMessage("simple", this.handlerId1, requestId3));

        // Get the response for request requestId1 (blocks until it arrives)
        Message response1 = client.receiveResponse(requestId1, true);
        assertNotNull("Fails to get response with block way.", response1);

        // Get the response for request req1112 (returns null if not immediately available)
        Message response2 = client.receiveResponse(requestId2, false);
        if (response2 == null) {
            System.out.println("Return null while not immediately avaiable with non-block way.");
        } else {
            System.out.println("Return immediately while response avaiable with non-block way.");
        }

        // Get any response (blocks until one arrives)
        Message response3 = client.receiveResponse(true);
        System.out.println("Return response for request: " + response3);

        // Get any reponse (returns null if not immediately available)
        Message response4 = client.receiveResponse(false);
        if (response4 == null) {
            System.out.println("Return null while not immediately avaiable with non-block way.");
        } else {
            System.out.println("Return immediately while response avaiable with non-block way for request: "
                    + response4.getRequestId());
        }

        // Disconnect from server
        client.disconnect();
        assertFalse("Fails to disconnect to the test server.", client.isConnected());
    }
    /**
     * Each IPClient will send a message to the same handler and a repsonse
     * will be retrieved immediately.
     *
     * @throws Exception to JUnit.
     */
    public void testMultipleClients() throws Exception {
        IPClient[] clients = new IPClient[HANDLER_NUM];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(helper.getAddress(), helper.getPort(), ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            clients[i].connect();

            // send a request to server
            Message src = messageFactory.getMessage("simple", "handler0", "request" + i);
            clients[i].sendRequest(src);
            assertNotNull("Fails to recieve response.", clients[i].receiveResponse(true));
            clients[i].disconnect();
        }
    }

    /**
     * Send message to different handlers from a single client,  and then
     * retrieve the message by request id.
     *
     * @throws Exception to JUnit.
     */
    public void testSingleClientMulitpleHandlers() throws Exception {
        IPClient client = new IPClient(helper.getAddress(), helper.getPort(), ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        client.connect();

        Message[] msgs = new Message[HANDLER_NUM];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = messageFactory.getMessage("simple", "handler" + i, "request" + i);
            client.sendRequest(msgs[i]);
        }

        // retrieve all the messages (from the last one to the first one).
        for (int i = 0; i < msgs.length; ++i) {
            int id = msgs.length - 1 - i;
            Message ret = client.receiveResponse("request" + id, true);
            assertEquals("Fails to receive response", msgs[id].getRequestId(), ret.getRequestId());
        }

        client.disconnect();
    }
    /////////////////////////////////////////////////////////////////////////////////////
    // Define a custom handler.
    // Custom handler implementation.
    /**
     * Mock handler implementation class, Only used for testing.
     */
    class MyHandler extends Handler {
        /**
         * <p>
         * Constructor with maxRequests. Should be called by subclasses. If a handler does not wish to have maximum
         * request limitation it should simply pass 0 as argument.
         * </p>
         *
         * @param maxRequests maximum number of requests
         */
        MyHandler(int maxRequests) {
            super(maxRequests);
        }

        /**
         * <p>
         * Notification method that is called when a connection is established with a client.
         * </p>
         *
         * @param conn the new connection
         */
        protected void onConnect(Connection conn) {
            System.out.println(conn.getClientNameAddress() + " " + conn.getClientIPAddress() + " "
                    + conn.getClientPort());
        }

        /**
         * <p>
         * Notification method that is called when a new client request arrives. The subclasses should implement this
         * method. They should do whatever processing is required and then use connection.getIPServer().sendResponse
         * (zero to many times as needed) to send the response back to the client.
         * </p>
         *
         * @param conn the connection on which the request arrived
         * @param request the request message
         * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should
         * be reported to the user by wrapping them in the response message, only fatal exceptions that should
         * terminate the server should throw this exception)
         * @throws IOException if a socket exception occurs while sending the response to the client
         */
        protected void onRequest(Connection conn, Message request)
            throws ProcessingException, IOException {
            super.onRequest(conn, request);
            // Process request ...
            System.out.println("OnRequest for:" + request.getRequestId());
            try {
                conn.getIPServer().sendResponse(conn.getId(), request);
            } catch (MessageSerializationException e) {
                throw new ProcessingException("The message factory fails to serialize the message.", e);
            }
        }
    }
}
