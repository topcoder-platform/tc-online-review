/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import com.topcoder.util.cache.SimpleMemoryUtilizationHandler;

import junit.framework.TestCase;

/**
 * Failure test for SimpleMemoryUtilizationHandler class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class SimpleMemoryUtilizationHandlerFailureTest extends TestCase {
    /**
     * Test the method <code>getObjectSize</code>.
     * Test the case the object is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testGetObjectSizeObjectNull() throws Exception {
        try {
            new SimpleMemoryUtilizationHandler().getObjectSize(null);
            fail("Should throw NullPointerException if object is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
}
