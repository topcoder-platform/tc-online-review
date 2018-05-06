/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.file.transfer.message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This is used to test BytesMessageType for correctness.
 * @author FireIce
 * @version 1.0
 */
public class BytesMessageTypeTestCase extends TestCase {

    /**
     * Test Constructor, if the argument is null, throw NullPointerException.
     */
    public void testCtorNPE() {
        try {
            new BytesMessageType(null);
            fail("if the argument is null, throw NullPointerException");
        } catch (NullPointerException e) {
            // good
        }
    }

    /**
     * Test Constructor, if the argument is empty String, throw IllegalArgumentException.
     */
    public void testCtorIAE() {
        try {
            new BytesMessageType(" ");
            fail("if the argument is empty, throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test Constructor, if the argument is valid, created successfully with no exception.
     */
    public void testCtorSuccess() {
        BytesMessageType messageType = new BytesMessageType("TYPE");
        assertEquals("the type value should be TYPE", messageType.getType(), "TYPE");
    }

    /**
     * Test inheritence.
     */
    public void testInheritence() {
        assertTrue("should extends MessageType class", new BytesMessageType("newTypeName") instanceof MessageType);
    }

    /**
     * <p>
     * Aggragates all tests in this class.
     * </p>
     * @return test suite aggragating all tests.
     */
    public static Test suite() {
        return new TestSuite(BytesMessageTypeTestCase.class);
    }
}
