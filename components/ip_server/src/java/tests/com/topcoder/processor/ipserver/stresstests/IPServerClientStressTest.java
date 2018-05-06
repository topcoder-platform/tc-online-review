/**
 * Copyright (c) 2004-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.processor.ipserver.stresstests;

import com.topcoder.processor.ipserver.Connection;
import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.IPClient;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.MessageCreationException;
import com.topcoder.processor.ipserver.message.MessageFactory;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidClassException;

import java.util.Properties;


/**
 * Stress test for IPServer component. Basically, The performance will rely on how the developer use jdk's nio
 *
 * @author alanSunny, marijnk
 * @version 2.0
 */
public class IPServerClientStressTest extends TestCase {
    /** The file from which contains properties for the testing environment. */
    private static final String TEST_FILE = "test_files/stress/ipserver_stresstest.properties";

    /** The namespace in which to load the message factor configuration. */
    private static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message.MessageFactory";

    /** The file to read the message factory configuration from. */
    private static final String MESSAGE_FACTORY_FILENAME = "stress/messagefactory.xml";

    /** The name of the message to use in testing. */
    private static final String MESSAGE_NAME = "message";

    /** The port used for testing. read from properties file. */
    private int port = -1;

    /** The address used for testing. read from properties file. */
    private String address = null;

    /** The server used for testing. */
    private IPServer ipServer = null;

    /** The handlerId used for testing. */
    private static String testHandlerId = "stress_handler_id";

    /** The client used for testing. */
    private IPClient client = null;

    /** The message factory used for testing. */
    private MessageFactory messageFactory = null;

    private static Properties prop = null;

    /**
     * static initial process, load address, port properties for test, create IPServer instance and add test handler.
     */
    static {
        try {
            // load the ipserver config from file
            prop = new Properties();
            prop.load(new FileInputStream(TEST_FILE));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * SetUp rountine, Make sure that the test server is started for testing.
     */
    protected void setUp() throws Exception {
        address = prop.getProperty("address");
        port = Integer.parseInt(prop.getProperty("port"));

        // load the message factory configuration
        ConfigManager manager = ConfigManager.getInstance();
        manager.add(MESSAGE_FACTORY_NAMESPACE, MESSAGE_FACTORY_FILENAME, ConfigManager.CONFIG_XML_FORMAT);

        try {
            // create ipServer instance with address, port properties
            ipServer = new IPServer(address, port, 0, 0, MESSAGE_FACTORY_NAMESPACE);
            messageFactory = ipServer.getMessageFactory();

            // add test handler to server
            ipServer.addHandler(testHandlerId, new StressHandler(100));

            ipServer.start();

            client = new IPClient(address, port, MESSAGE_FACTORY_NAMESPACE);
            client.connect();
        } catch (Exception e) {
            manager.removeNamespace(MESSAGE_FACTORY_NAMESPACE);
            throw e;
        }
    }

    /**
     * Make sure that the test server is stopped after testing
     */
    protected void tearDown() throws Exception {
        // Unload the message factory configuration
        ConfigManager manager = ConfigManager.getInstance();
        if (manager.existsNamespace(MESSAGE_FACTORY_NAMESPACE)) {
            manager.removeNamespace(MESSAGE_FACTORY_NAMESPACE);
        }

        client.disconnect();

        if (ipServer.isStarted()) {
            ipServer.stop();
        }

        // Try to stop the server
        while (ipServer.isStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                Thread.interrupted();
            }
        }
    }

    /**
     * Test send 500 requests to IPServer and recieve 500 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking500() throws IOException, MessageCreationException {
        sendReceiveBlocking(500);
    }

    /**
     * Test send 1000 requests to IPServer and recieve 1000 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking1000() throws IOException, MessageCreationException {
       sendReceiveBlocking(1000);
    }

    /**
     * Test send 2000 requests to IPServer and recieve 2000 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking2000() throws IOException, MessageCreationException {
       sendReceiveBlocking(2000);
    }

    /**
     * Test send 5000 requests to IPServer and recieve 5000 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking5000() throws IOException, MessageCreationException {
       sendReceiveBlocking(5000);
    }

    /**
     * Test send 10000 requests to IPServer and recieve 10000 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking10000() throws IOException, MessageCreationException {
        sendReceiveBlocking(10000);
    }

    /**
     * Test send 20000 requests to IPServer and recieve 20000 responses from with blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveBlocking20000() throws IOException, MessageCreationException {
        sendReceiveBlocking(20000);
    }

    /**
     * Test send 500 requests to IPServer and recieve 500 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking500() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(500);
    }

    /**
     * Test send 1000 requests to IPServer and recieve 1000 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking1000() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(1000);
    }

    /**
     * Test send 2000 requests to IPServer and recieve 2000 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking2000() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(2000);
    }

    /**
     * Test send 5000 requests to IPServer and recieve 5000 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking5000() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(5000);
    }

    /**
     * Test send 10000 requests to IPServer and recieve 10000 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking10000() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(10000);
    }

    /**
     * Test send 20000 requests to IPServer and recieve 20000 responses from with non-blocking mode.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    public void testSendReceiveNonBlocking20000() throws IOException, MessageCreationException {
        sendReceiveNonBlocking(20000);
    }

    /**
     * Send the given num requests to IPServer and recieve with blocking mode.
     *
     * @param num The num request will be sent and recieved.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    private void sendReceiveBlocking(int num) throws IOException, MessageCreationException {
        // add specified number words
        long start = System.currentTimeMillis();

        int successNum = 0;

        for (int i = 0; i < num; i++) {
            client.sendRequest(messageFactory.getMessage(MESSAGE_NAME, testHandlerId, "Blocking-" + num
                                    + "-request-" + i));
            if (client.receiveResponse("Blocking-" + num + "-request-" + i, true) != null) {
                successNum++;
            }
        }

        long cost = System.currentTimeMillis() - start;
        System.out.println("Blocking Mode: Send " + num + " requests ; receive " + successNum + " responses take " +
            cost + " ms.");
    }

    /**
     * Send the given num requests to IPServer and recieve with non-blocking mode.
     *
     * @param num The num request will be sent and recieved.
     *
     * @throws IOException to JUnit
     * @throws MessageCreationException to JUnit
     */
    private void sendReceiveNonBlocking(final int num) throws IOException, MessageCreationException {
        // add specified number words
        long start = System.currentTimeMillis();

        int successNum = 0;
        
        for (int i = 0; i < num; i++) {
            try {
                client.sendRequest(messageFactory.getMessage(MESSAGE_NAME, testHandlerId, "NonBlocking-" + num
                                        + "-request-" + i));
                Thread.yield();
                if (i % 100 == 99) {
                    while (client.receiveResponse(false) != null) {
                        successNum++;
                    }
                }
            } catch (MessageCreationException e) {
                ipServer.getLastException().printStackTrace();
                throw e;
            } catch (IOException e) {
                    e.printStackTrace();
                ipServer.getLastException().printStackTrace();
                throw e;
            }
        }

        int lateNum = 0;
        try {
            Thread.sleep(100);
            while (client.receiveResponse(false) != null) {
                successNum++;
                lateNum++;
            }
            } catch (IOException e) {
                    e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long cost = System.currentTimeMillis() - start;
        System.out.println("NonBlocking Mode: Send " + num + " requests ; receive " + successNum
                        + " responses (" + lateNum + " late) take " + cost + " ms.");
    }

    /////////////////////////////////////////////////////////////////////////////////////
    // Define a custom handler.
    // Custom handler implementation.
    class StressHandler extends Handler {
        StressHandler(int maxRequests) {
            super(maxRequests);
        }

        // On request send back a response
        protected void onRequest(Connection conn, Message request)
            throws IOException {
            try {
                Message response = messageFactory.getMessage(MESSAGE_NAME, request.getHandlerId(),
                                request.getRequestId());
                conn.getIPServer().sendResponse(conn.getId(), response);
            } catch (MessageCreationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}
