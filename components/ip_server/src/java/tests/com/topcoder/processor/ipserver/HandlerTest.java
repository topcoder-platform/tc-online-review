/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test cases for Handler.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class HandlerTest extends TestCase {
    /** The max requests constant used for testing. */
    private static final int MAX_REQUESTS = 100;

    /** The Handler used for testing. */
    protected Handler handler = new Server1Handler1(HandlerTest.MAX_REQUESTS);

    /** The request used for testing. */
    private Message request;

    /** The ConfigHelper instance used for testing. */
    private ConfigHelper helper;

    /** The IPServer instance used for testing. */
    private IPServer server;

    /**
     * <p>
     * setUp rountine that ensure the ipServer of connection is started.
     * </p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();

        helper = ConfigHelper.getInstance();
        server = helper.getIPServer();

        // handlerRequest, onConnect, onRequest require a started ipServer
        ConfigHelper.startServer(server);

        request = new DefaultMessageFactory(
            ConfigHelper.MESSAGE_FACTORY_NAMESPACE).getMessage("simple", "handlerId", "requestId");
    }

    /**
     * Make sure the test IPServer instance is stopped and clear test environment.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        ConfigHelper.stopServer(this.server);
        ConfigHelper.releaseNamespaces();
    }

    /**
     * Test of Constructor with correct max connections, which create in setUp method.
     */
    public void testConstructor() {
        assertNotNull("Fails to create Handler instance with correct maxConnections.", this.handler);
    }

    /**
     * Test of Constructor with negative maxConnections, IAE is expected.
     */
    public void testConstructorWithNegativeMaxConnections() {
        try {
            new Server1Handler1(-1);
            fail("The given maxConnections is negative.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test of getMaxConnections will retrieve the max connections that set in constructor.
     */
    public void testGetMaxConnections() {
        assertEquals("Fails to get maxConnections.", helper.getMaxConnections(), this.handler.getMaxConnections());
    }

    /**
     * Test of handlerRequest, Except argument validation, No more process in this default implementation. So it can
     * pass correct arguments then all are ok.
     *
     * @throws Exception to JUnit
     */
    public void testHandleRequest() throws Exception {
        // Make sure the connection is already in server
        this.handler.handleRequest(helper.getConnection(), request);

        // Pass argument validation
    }

    /**
     * Test of handleRequest with null connection, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testHandleRequestWithNullConnection() throws Exception {
        try {
            this.handler.handleRequest(null, this.request);
            fail("The given connection is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of handleRequest with null request, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testHandleRequestWithNullRequest() throws Exception {
        try {
            this.handler.handleRequest(helper.getConnection(), null);
            fail("The given request is null.");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test of handleRequest with connection which ipServer is stopped, IllegalStateException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testHandleRequestWithStoppedServer() throws Exception {
        // Stop the test server for below testing
        ConfigHelper.stopServer(server);

        try {
            this.handler.handleRequest(helper.getConnection(), this.request);
            fail("The ipServer of given connection is stopped.");
        } catch (IllegalStateException e) {
            // good
        }
    }

    /**
     * <p>
     * Test of onConnect method, only arguments validation will be done in default handler onConnect.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testOnConnect() throws Exception {
        this.handler.onConnect(helper.getConnection());

        // Argument validation pass
    }

    /**
     * Test of onRequest with null connection, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testOnConnectWithNullConnection() throws Exception {
        try {
            this.handler.onConnect(null);
            fail("The given connection is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * <p>
     * Test of onRequest method, only arguments validation will be done in default handler onRequest.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testOnRequest() throws Exception {
        this.handler.onRequest(helper.getConnection(), this.request);

        // Argument validation pass
    }

    /**
     * Test of onRequest with null connection, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testOnRequestWithNullConnection() throws Exception {
        try {
            this.handler.onRequest(null, this.request);
            fail("The given connection is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of onRequest with null request, NPE is expected.
     *
     * @throws Exception to JUnit
     */
    public void testOnRequestWithNullRequest() throws Exception {
        try {
            this.handler.onRequest(helper.getConnection(), null);
            fail("The given request is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of onRequest with connection which ipServer is stopped, IllegalStateException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testOnRequestWithStoppedServer() throws Exception {
        // Stop the test server for below testing
        ConfigHelper.stopServer(server);

        try {
            this.handler.onRequest(helper.getConnection(), this.request);
            fail("The ipServer of given connection is stopped.");
        } catch (IllegalStateException e) {
            // good
        }
    }
}
