/*
 * TestIllegalHandlerException.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.classassociations;

import com.topcoder.util.classassociations.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>Test IllegalHandlerException constructor and usage.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0  3-30-2004
 */
public class TestIllegalHandlerException extends TestCase {

    public static Test suite() {
        return new TestSuite(TestIllegalHandlerException.class);
    }

    public void testIllegalHandlerExceptionWithValidString() {
        // Verify that the String specified with the exception
        // is stored and persisted properly through creation and throwing
        // of the exception.
        String message = "test String argument";
        IllegalHandlerException ihe = new IllegalHandlerException (message);

        try {
            throw ihe;
        } catch (IllegalHandlerException ex) {
            assertTrue(ex.getMessage().equals(message));
        }
    }

    public void testIllegalHandlerExceptionWithNullString() {
        // Verify that the String specified with in the exception constructor
        // will not be accepted if null.
        try {
            IllegalHandlerException ihe = new IllegalHandlerException (null);
            fail("IllegalArgumentException should be thrown for null argument.");
        } catch (IllegalArgumentException ex) {
            // this is the correct response
        }
    }

}