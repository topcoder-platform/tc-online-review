/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.processor.ipserver.keepalive.KeepAliveHandler;


/**
 * <p>
 * Unit test cases for KeepAliveHandler. In this test case, we focus on the specific issue: maxRequest will be always.
 * This unit test extends from HandlerTest, so this unit test will ensure that all tests of HandlerTest will be ok
 * while the handler is KeepAliveHandler type. 0.
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class KeepAliveHandlerTest extends HandlerTest {
    /**
     * <p>
     * setUp rountine that call super.setUp and use KeepAliveHandler instance instead of handler instance in
     * HandlerTest for testing.
     * </p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();

        /**
         * Use KeepAlivaHandler instance instead of the handler instance in HandlerTest for testing.
         */
        if (!(this.handler instanceof KeepAliveHandler)) {
            this.handler = new KeepAliveHandler();
        }
    }

    /**
     * Make sure the super.tearDown can be invoking to remove the test site.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * OnRequest of KeepAliveHandler by invoking super.onRequest for argument validation, and will invoke
     * server.sendResponse with the given argument to keep alive with client.
     * </p>
     */
    public void testOnRequest() {
        /**
         * This method cannot be invoking by user directly, It should be called by server for server will create the
         * connection instance and keep this instance for call-back. So we will test onRequest in Demo test which is
         * for all functionality integration testing.
         */
    }

    /**
     * Test of handlerRequestcall by invoking super.handleRequest for argument validation,  will invoke
     * KeepAliveHandler.onRequest to handler this request, the test logic like the testOnRequest method.
     */
    public void testHandleRequest() {
        /**
         * This method cannot be invoking by user directly, the reason is the same as the above test method, so we will
         * test handlerRequest in Demo test which is for all functionality integration testing.
         */
    }

    /**
     * <p>
     * Test of constructor with no-argument.
     * </p>
     */
    public void testConstructor() {
        assertNotNull("Fails to create KeepAliveHanlder instance with no argument.", this.handler);
    }

    /**
     * Test of getMaxConnections will retrieve 0 for maxConnnections of KeepAliveHandler instance.
     */
    public void testGetMaxConnections() {
        assertEquals("Fails to get maxConnections (0 always be) for KeepAliveHandler.", 0,
            this.handler.getMaxConnections());
    }
}
