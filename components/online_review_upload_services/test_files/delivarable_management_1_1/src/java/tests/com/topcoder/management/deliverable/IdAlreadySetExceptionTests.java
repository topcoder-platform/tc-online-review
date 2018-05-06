/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import junit.framework.TestCase;


/**
 * Unit tests for IdAlreadySetException.
 *
 * @author singlewood
 * @version 1.1
 */
public class IdAlreadySetExceptionTests extends TestCase {
    /**
     * Tests the constructor with one message parameter. The object should be created correctly.
     */
    public void testConstructor1() {
        IdAlreadySetException e = new IdAlreadySetException("message");

        assertNotNull("The object should not be null", e);
    }

}
