/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.keepalive.KeepAliveIPClient;

import com.topcoder.util.heartbeat.HeartBeat;
import com.topcoder.util.heartbeat.HeartBeatManager;

import java.io.IOException;

import java.lang.reflect.Field;


/**
 * <p>
 * Unit test cases for KeepAliveIPClient. This unit test extends from IPClientTest.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class KeepAliveIPClientTest extends IPClientTest {
    /** The delay time used for testing. */
    private final int delay = 10000;

    /** The heartBeatManager Field used to test connect, disconnect method. */
    private Field heartBeatManagerField = null;

    /**
     * Use KeepAliveClient instead of IPClient instance for testing.
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.initExceptClient();

        if (heartBeatManagerField == null) {
            heartBeatManagerField = KeepAliveIPClient.class.getDeclaredField("heartBeatManager");
            heartBeatManagerField.setAccessible(true);
        }

        client = new KeepAliveIPClient(
            address, port, delay, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
        client.connect();
        assertTrue("Fails to connect to server.", client.isConnected());
    }

    /**
     * @see IPClientTest#tearDown(), Just invoke super.tearDown to disconnect the client from server and stop server.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        // Only invoking super.tearDown to disconnnect with server and stop the server,
        super.tearDown();
    }

    /**
     * Test of connect, will add KeepAliveIPClient to heartBeatManager.
     */
    public void testConnect() {
        // Already connect with server in setUp method
        try {
            HeartBeatManager manager = (HeartBeatManager) heartBeatManagerField.get(client);
            assertTrue("Fails to add heartBeat to manaager.", manager.contains((HeartBeat) client));
        } catch (IllegalArgumentException e) {
            // Never happen
        } catch (IllegalAccessException e) {
            // Never happen
        }
    }

    /**
     * Test of connect, will remove KeepAliveIPClient from heartBeatManager.
     *
     * @throws IOException to JUnit
     */
    public void testDisconnect() throws IOException {
        // Disconnect with server
        if (client.isConnected()) {
            client.disconnect();
        }

        try {
            HeartBeatManager manager = (HeartBeatManager) heartBeatManagerField.get(client);
            assertFalse("Fails to remove heartBeat from manaager.", manager.contains((HeartBeat) client));
        } catch (IllegalArgumentException e) {
            // Never happen
        } catch (IllegalAccessException e) {
            // Never happen
        }
    }

    /**
     * Test of constructor with address, port, delay arguments.
     */
    public void testConstructor() {
        assertNotNull("Fails to create KeepAliveIPClient instance with correct arguments.", client);
    }

    /**
     * Test of constructor with zero delay, IAE should thrown.
     * @throws Exception to JUnit
     */
    public void testConstructorWithZeroDelay() throws Exception {
        try {
            new KeepAliveIPClient(address, port, 0, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given delay is zero");
        } catch (IllegalArgumentException npe) {
            // good
        }
    }

    /**
     * Test of constructor with negative delay, IAE should thrown.
     * @throws Exception to JUnit
     */
    public void testConstructorWithNegativeDelay() throws Exception {
        try {
            new KeepAliveIPClient(address, port, -1, ConfigHelper.MESSAGE_FACTORY_NAMESPACE);
            fail("The given delay is negative");
        } catch (IllegalArgumentException npe) {
            // good
        }
    }

    /**
     * Test of constructor with empty namespace, IAE should thrown.
     * @throws Exception to JUnit
     */
    public void testConstructorWithEmptyNamespace() throws Exception {
        try {
            new KeepAliveIPClient(address, port, 12, "   ");
            fail("The given namespace is empty");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of constructor with null namespace, NPE should thrown.
     * @throws Exception to JUnit
     */
    public void testConstructorWithNullNamespace() throws Exception {
        try {
            new KeepAliveIPClient(address, port, 12, null);
            fail("The given namespace is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of keepAlive, this method will not by invoking by other method except heartBeatManager.
     */
    public void testKeepAlive() {
        // Now server is started
        // No any exception will be thrown, it means the keepAlive method runs well
        ((KeepAliveIPClient) client).keepAlive();
        assertNull("Fails to exectute keepAlive.", ((KeepAliveIPClient) client).getLastException());
    }

    /**
     * Test of getLastException, initially it will return null.
     * @throws Exception to JUnit
     */
    public void testGetLastException() throws Exception {
        assertNull("Fails to initial to null.",
            (new KeepAliveIPClient(
            address, port, delay, ConfigHelper.MESSAGE_FACTORY_NAMESPACE)).getLastException());
    }
}
