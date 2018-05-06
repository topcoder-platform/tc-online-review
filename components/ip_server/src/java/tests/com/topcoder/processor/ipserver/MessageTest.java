/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for Message.
 * </p>
 *
 * <p>
 * All the manipulation methods are tested against accuracy and failure behaviors described by the design.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 */
public class MessageTest extends TestCase {
    /**
     * <p>
     * The handler id used for testing.
     * </p>
     */
    private static final String HANDLER_ID = "test handler id";

    /**
     * <p>
     * The request id used for testing.
     * </p>
     */
    private static final String REQUEST_ID = "test request id";

    /**
     * <p>
     * Message instance to test on. Will be instantiated in setUp() routine. No tearDown() is provided since it does
     * not require any explicit destruction before reclaimed by GC.
     * </p>
     */
    private Message message = null;

    /**
     * <p>
     * setUp() routine.
     * </p>
     *
     * <p>
     * A new instance of Message is created for each test. It is populated with handlerId, requestId.
     * </p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        this.message = new Message(MessageTest.HANDLER_ID, MessageTest.REQUEST_ID);
    }

    /**
     * Test of constructor with no arguments.  Notes: this constructor only be used for deserialization.
     */
    public void testConstructorNoArgs() {
        assertNotNull("Fails to create instance.", new Message());
    }

    /**
     * Test of constructor with valid arguments, instance created in setUp method.
     */
    public void testConstructorArgs() {
        assertNotNull("Fails to create instance.", this.message);
    }

    /**
     * Test of constructor with null handlerId, NPE is expected.
     */
    public void testConstructorArgsWithNullHanlderId() {
        try {
            new Message(null, MessageTest.REQUEST_ID);
            fail("The given handlerId is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of constructor with null requestId, NPE is expected.
     */
    public void testConstructorArgsWithNullRequestId() {
        try {
            new Message(null, MessageTest.REQUEST_ID);
            fail("The given handlerId is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of getHandlerId method, Verifies if the correct handlerId is returned.
     */
    public void testGetHandlerId() {
        assertEquals("Fails to get handlerId.", MessageTest.HANDLER_ID, this.message.getHandlerId());
    }

    /**
     * Test of getRequestId method, Verifies if the correct requestId is returned.
     */
    public void testGetRequestId() {
        assertEquals("Fails to get requestId.", MessageTest.REQUEST_ID, this.message.getRequestId());
    }
}
