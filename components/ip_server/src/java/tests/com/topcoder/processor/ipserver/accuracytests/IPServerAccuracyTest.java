/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.IPServer;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.util.Iterator;


/**
 * Accuracy test of IPServer class.
 *
 * @author air2cold, WishingBone
 * @version 2.0
 */
public class IPServerAccuracyTest extends TestCase {

    /**
     * The namespace for the MessageFactory.
     */
    public static final String NAMESPACE = "com.topcoder.processor.ipserver.message";

    /**
     * The server address.
     */
    private static final String SERVER = "127.0.0.1";

    /**
     * The server port.
     */
    private static int port = 48000;

    /**
     * The number of handlers to register into IPServer.
     */
    private static final int HANDLER_NUM = 10;

    /**
     * The IPServer instance to test.
     */
    private IPServer server = null;

    /**
     * The handler to register into IPServer.
     */
    private AccuracyHandler[] handlers = null;

    /**
     * Start IPServer and register handlers.
     */
    public void setUp() throws Exception {
        clearConfiguration();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add("accuracytests/MessageFactory.xml");

        getPort();
        server = new IPServer(SERVER, port, 10, 100, NAMESPACE);
        handlers = new AccuracyHandler[HANDLER_NUM];

        for (int i = 0; i < handlers.length; ++i) {
            handlers[i] = new AccuracyHandler();
            server.addHandler("handler" + i, handlers[i]);

            // the max handler can process one request each time.
            server.addHandler("maxHandler", new MaxHandler(1));
        }

        server.start();
    }

    /**
     * Close the server.
     *
     * @throws Exception to JUnit.
     */
    public void tearDown() throws Exception {
        server.stop();
        clearConfiguration();
    }

    /**
     * Remove all the config files from the Configuration Manager.
     *
     * @throws Exception to JUnit.
     */
    private void clearConfiguration() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * Each IPClient will send a message to a different handler, and a repsonse
     * will be retrieved later.
     *
     * @throws Exception to JUnit.
     */
    public void testMultipleClients1() throws Exception {
        IPClient[] clients = new IPClient[HANDLER_NUM];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(SERVER, port, NAMESPACE);
            clients[i].connect();

            // send a request to server
            AccuracyMessage src = new AccuracyMessage("handler" + i,
                    "request" + i, "msg" + i);
            clients[i].sendRequest(src);

            // the original message is sent back
            AccuracyMessage ret = (AccuracyMessage) clients[i].receiveResponse(true);
            assertMessage(src, ret);

            // make sure the handler is called 
            Thread.sleep(300);
            assertMessage(ret, (AccuracyMessage) handlers[i].getRequest());
            assertNotNull("Handler's onRequest is called.",
                handlers[i].getOnRequest());
            assertNotNull("handler's onConnect is called.",
                handlers[i].getOnConnect());

            clients[i].disconnect();
        }
    }

    /**
     * Each IPClient will send a message to the same handler and a repsonse
     * will be retrieved immediately.
     *
     * @throws Exception to JUnit.
     */
    public void testMultipleClients2() throws Exception {
        IPClient[] clients = new IPClient[HANDLER_NUM];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(SERVER, port, NAMESPACE);
            clients[i].connect();

            // send a request to server
            AccuracyMessage src = new AccuracyMessage("handler0",
                    "request" + i, "msg" + i);
            clients[i].sendRequest(src);


            AccuracyMessage ret = (AccuracyMessage) clients[i].receiveResponse(true);
            assertMessage(src, ret);

            clients[i].disconnect();
        }
    }

    /**
     * Each IPClient will send a message to each handler,  the message will be
     * retrieved immediately.
     *
     * @throws Exception to JUnit.
     */
    public void testMultipleClients3() throws Exception {
        IPClient[] clients = new IPClient[HANDLER_NUM];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(SERVER, port, NAMESPACE);
            clients[i].connect();

            // send a request to server
            for (int j = 0; j < HANDLER_NUM; ++j) {
                AccuracyMessage src = new AccuracyMessage("handler" + j,
                        "request" + j, "msg" + i);
                clients[i].sendRequest(src);

                AccuracyMessage ret = (AccuracyMessage) clients[i]
                    .receiveResponse(true);
                assertMessage(src, ret);
            }

            clients[i].disconnect();
        }
    }

    /**
     * Send message to multiple handlers, the responses will be retrieved by
     * request id later.
     *
     * @throws Exception to JUnit.
     */
    public void testMultipleClient4() throws Exception {
        IPClient[] clients = new IPClient[HANDLER_NUM];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(SERVER, port, NAMESPACE);
            clients[i].connect();

            // send a request to server
            for (int j = 0; j < HANDLER_NUM; ++j) {
                AccuracyMessage src = new AccuracyMessage("handler" + j,
                        "request" + j, "msg" + i);
                clients[i].sendRequest(src);
            }
        }

        // retrieve response now
        for (int i = 0; i < clients.length; ++i) {
            for (int j = 0; j < HANDLER_NUM; ++j) {
                AccuracyMessage src = new AccuracyMessage("handler" + j,
                        "request" + j, "msg" + i);
                AccuracyMessage ret = (AccuracyMessage) clients[i]
                    .receiveResponse("request" + j, true);
                assertMessage(src, ret);
            }

            clients[i].disconnect();
        }
    }

    /**
     * The IPServer could only accept 10 connection.
     *
     * @throws Exception to JUnit.
     */
    public void testMaxConnection() throws Exception {
        IPClient[] clients = new IPClient[10];

        for (int i = 0; i < clients.length; ++i) {
            clients[i] = new IPClient(SERVER, port, NAMESPACE);
            clients[i].connect();

            // send a request
            AccuracyMessage src = new AccuracyMessage("handler" + i,
                    "request" + i, "msg" + i);
            clients[i].sendRequest(src);

            AccuracyMessage ret = (AccuracyMessage) clients[i].receiveResponse(
                    "request" + i, true);
            assertMessage(src, ret);
        }

        // disconnect later
        for (int i = 0; i < clients.length; ++i) {
            clients[i].disconnect();
        }
    }

    /**
     * Send message to different handlers from a single client, and then
     * retrieve response immediately.
     *
     * @throws Exception to JUnit.
     */
    public void testSingleClient1() throws Exception {
        IPClient client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();

        AccuracyMessage[] msgs = new AccuracyMessage[HANDLER_NUM];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = new AccuracyMessage("handler" + i, "request" + i,
                    "msg" + i);
            client.sendRequest(msgs[i]);

            AccuracyMessage ret = (AccuracyMessage) client.receiveResponse(true);
            assertMessage(msgs[i], ret);
        }

        client.disconnect();
    }

    /**
     * Send message to different handlers from a single client,  and then
     * retrieve the message by request id.
     *
     * @throws Exception to JUnit.
     */
    public void testSingleClient2() throws Exception {
        IPClient client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();

        AccuracyMessage[] msgs = new AccuracyMessage[HANDLER_NUM];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = new AccuracyMessage("handler" + i, "request" + i,
                    "msg" + i);
            client.sendRequest(msgs[i]);
        }

        // retrieve all the messages (from the last one to the first one).
        for (int i = 0; i < msgs.length; ++i) {
            int id = msgs.length - 1 - i;
            AccuracyMessage ret = (AccuracyMessage) client.receiveResponse(
                    "request" + id, true);
            assertMessage(msgs[id], ret);
        }

        client.disconnect();
    }

    /**
     * Send message to the same handler from a single client,  and then
     * retrieve the response immediately.
     *
     * @throws Exception to JUnit.
     */
    public void testSingleClient3() throws Exception {
        IPClient client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();

        AccuracyMessage[] msgs = new AccuracyMessage[HANDLER_NUM];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = new AccuracyMessage("handler" + 0, "request" + i,
                    "msg" + i);
            client.sendRequest(msgs[i]);

            AccuracyMessage ret = (AccuracyMessage) client.receiveResponse(true);
            assertMessage(msgs[i], ret);
        }

        client.disconnect();
    }

    /**
     * Send message to the same handler from a single client,  and then
     * retrieve responses by request id.
     *
     * @throws Exception to JUnit.
     */
    public void testSingleClient4() throws Exception {
        IPClient client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();

        AccuracyMessage[] msgs = new AccuracyMessage[HANDLER_NUM];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = new AccuracyMessage("handler" + 0, "request" + i,
                    "msg" + i);
            client.sendRequest(msgs[i]);
        }

        // retrieve response
        for (int i = 0; i < msgs.length; ++i) {
            int id = msgs.length - 1 - i;
            AccuracyMessage ret = (AccuracyMessage) client.receiveResponse(
                    "request" + id, true);
            assertMessage(msgs[id], ret);
        }

        client.disconnect();
    }

    /**
     * Test retrieve response from the MaxHandler registered into IPServer.
     *
     * @throws Exception to JUnit.
     */
    public void testMaxHandler() throws Exception {
        IPClient client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();

        AccuracyMessage[] msgs = new AccuracyMessage[10];

        for (int i = 0; i < msgs.length; ++i) {
            msgs[i] = new AccuracyMessage("maxHandler", "request" + i, "msg"
                    + i);
            client.sendRequest(msgs[i]);
        }

        // retrieve response
        for (int i = 0; i < msgs.length; ++i) {
            int id = msgs.length - 1 - i;
            AccuracyMessage ret = (AccuracyMessage) client.receiveResponse(
                    "request" + id, true);
            assertMessage(msgs[id], ret);
        }

        client.disconnect();
    }

    /**
     * Compare the expected message with the actual one.
     *
     * @param expected the expected message.
     * @param actual the actual message.
     */
    private void assertMessage(AccuracyMessage expected, AccuracyMessage actual) {
        assertEquals("The handlerId doesn't match", expected.getHandlerId(),
            actual.getHandlerId());
        assertEquals("The requestId doesn't match", expected.getRequestId(),
            actual.getRequestId());
        assertEquals("The name doesn't match", expected.getName(),
            actual.getName());
    }

    /**
     * Return an unused port to create the server.
     *
     * @return the unused port.
     */
    private int getPort() {
        return port++;
    }
}
