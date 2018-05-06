/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import junit.framework.TestCase;

import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * <p>
 * Unit test cases for Connection.
 * </p>
 *
 * <p>
 * The Connection implementation is simple, Only need to foucs on constructor method, other methods provided is for
 * verify the properties being set in construtor.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class ConnectionTest extends TestCase {
    /**
     * <p>
     * Connection instance to test on. Will be instantiated in setUp() routine. No tearDown() is provided since it does
     * not require any explicit destruction before reclaimed by GC.
     * </p>
     */
    private Connection connection = null;

    /**
     * <p>
     * The IPServer instance used for testing.
     * </p>
     */
    private IPServer ipServer = null;

    /**
     * <p>
     * The Socket instance used for testing.
     * </p>
     */
    private Socket socket = null;

    /**
     * <p>
     * The ConfigHelper instance used for testing.
     * </p>
     */
    private ConfigHelper helper;

    /**
     * <p>
     * setUp() routine.
     * </p>
     *
     * <p>
     * Get socket, ipServer, connection instance for testing from ConfigHelper.
     * </p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();

        helper = ConfigHelper.getInstance();

        // These instance never be changed after get, So only need to get once
        if (this.socket == null) {
            // In ConfigHelper instance, socket, ipServer set in Connection construtor as arguments
            this.socket = helper.getSocket();
            this.ipServer = helper.getIPServer();
            this.connection = helper.getConnection();
        }
    }

    /**
     * Clear the environment.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Test of constructor with valid arguments, instance created in setUp method.
     */
    public void testConstructor() {
        assertNotNull("Fails to create instance.", this.connection);
    }

    /**
     * Test of constructor with null connectionId, NPE is expected.
     */
    public void testConstructorWithNullConnectionId() {
        try {
            new Connection(null, this.ipServer, this.socket);
            fail("The given connectionId is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of constructor with null ipServer, NPE is expected.
     */
    public void testConstructorWithNullIPServer() {
        try {
            new Connection(ConfigHelper.CONNECTION_ID, null, this.socket);
            fail("The given ipServer is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of constructor with null socket, NPE is expected.
     */
    public void testConstructorWithNullSocket() {
        try {
            new Connection(ConfigHelper.CONNECTION_ID, this.ipServer, null);
            fail("The given socket is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of getId method, Verifies if the correct connectionId is returned.
     */
    public void testGetId() {
        assertEquals("Fails to get connectionId.", ConfigHelper.CONNECTION_ID, this.connection.getId());
    }

    /**
     * Test of getIPServer method, Verifies if the correct ipServer is returned.
     */
    public void testGetIPServer() {
        assertEquals("Fails to get ipServer.", this.ipServer, this.connection.getIPServer());
    }

    /**
     * Test of getSocket method, Verifies if the correct ipServer is returned.
     */
    public void testGetSocket() {
        assertEquals("Fails to get socket.", this.socket, this.connection.getSocket());
    }

    /**
     * Test of getClientNameAddress method, Verifies if the correct clientNameAddress is returned.
     */
    public void testGetClientNameAddress() {
        assertEquals("Fails to get clientNameAddress.",
            ((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress().getHostName(),
            this.connection.getClientNameAddress());
    }

    /**
     * Test of getClientIPAddress method, Verifies if the correct clientIPAddress is returned.
     */
    public void testGetClientIPAddress() {
        assertEquals("Fails to get clientIPAddress.",
            ((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress().getHostAddress(),
            this.connection.getClientIPAddress());
    }

    /**
     * Test of getClientPort method, Verifies if the correct clientPort is returned.
     */
    public void testGetClientPort() {
        assertEquals("Fails to get clientPort.", ((InetSocketAddress) socket.getRemoteSocketAddress()).getPort(),
            this.connection.getClientPort());
    }
}
