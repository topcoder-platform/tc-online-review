/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import junit.framework.TestCase;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageSerializationException;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.ProcessingException;
import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.Handler;
import java.io.IOException;
import java.util.Iterator;

/**
 * Accuracy test cases for IPServer (this only addresses functionality introduced in version 2.0.
 *
 * @author WishingBone
 * @version 2.0
 */
public class IPServerAccuracyTestsV2 extends TestCase {

    /**
     * The namespace to instantiate from.
     */
    private static final String NAMESPACE = "com.topcoder.processor.ipserver.message";

    /**
     * The server address.
     */
    private static final String SERVER = "127.0.0.1";

    /**
     * The server port.
     */
    private static int port = 48100;

    /**
     * This could either track the incoming message or outgoing message.
     */
    private Message message = null;

    /**
     * The IPServer to test with.
     */
    private IPServer server = null;

    /**
     * The IPClient to test with.
     */
    private IPClient client = null;

    /**
     * Load configuration files.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        clearConfiguration();
        ConfigManager cm = ConfigManager.getInstance();
        cm.add("accuracytests/MessageFactory.xml");

        port++;

        server = new IPServer(SERVER, port, 10, 100, NAMESPACE);
        server.addHandler("accuracy", new AccuracyHandler());
        server.start();

        client = new IPClient(SERVER, port, NAMESPACE);
        client.connect();
    }

    /**
     * Unload all the configuration files.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        client.disconnect();
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
     * This case tests client can properly send custom message to the server.
     *
     * @throws Exception to JUnit.
     */
    public void testSendMessage() throws Exception {
        String handlerId = "accuracy";
        String requestId = "i0-s@";
        Message message = new AccuracyCustomMessage(handlerId, requestId);

        client.sendRequest(message);
        Thread.sleep(200);

        assertNotNull("Request is not received.", this.message);
        assertTrue("Incorrect message type instantiated.", this.message instanceof AccuracyCustomMessage);

        assertEquals("Message data is corrupted.", handlerId, this.message.getHandlerId());
        assertEquals("Message data is corrupted.", requestId, this.message.getRequestId());
    }

    /**
     * This case tests client can properly receive custom message from the server.
     *
     * @throws Exception to JUnit.
     */
    public void testReceiveMessage() throws Exception {
        String handlerId = "accuracy";
        String requestId = "i0-s@";
        Message message = new AccuracyCustomMessage(handlerId, requestId);

        handlerId = "hello";
        requestId = "@s-0i";
        this.message = new AccuracyCustomMessage(handlerId, requestId);

        client.sendRequest(message);
        message = client.receiveResponse(true);

        assertNotNull("Request is not received.", message);
        assertTrue("Incorrect message type instantiated.", message instanceof AccuracyCustomMessage);

        assertEquals("Message data is corrupted.", handlerId, message.getHandlerId());
        assertEquals("Message data is corrupted.", requestId, message.getRequestId());
    }

    /**
     * The inner class which acts as a customer handler.
     */
    private class AccuracyHandler extends Handler {

        /**
         * Default constructor.
         */
        public AccuracyHandler() {
            super(1);
        }

        /**
         * Request handler. If message is not set it tracks the incoming message, otherwise it sends it as
         * the outgoing message.
         *
         * @param connection the connection.
         * @param request the request.
         */
        protected void onRequest(Connection connection, Message request)
                throws ProcessingException, IOException {
            super.onRequest(connection, request);
            if (message == null) {
                message = request;
            } else {
                try {
                    connection.getIPServer().sendResponse(connection.getId(), message);
                } catch (MessageSerializationException mse) {
                    throw new ProcessingException("Message can not be serialized.", mse);
                }
            }
        }

    }

}
