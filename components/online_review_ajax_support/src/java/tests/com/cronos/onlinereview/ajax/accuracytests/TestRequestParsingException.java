/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.RequestParsingException;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * Tests for RequestParsingException class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestRequestParsingException extends TestCase {
    /**
     * Tests RequestParsingException() method with accuracy state.
     */
    public void testRequestParsingException1Accuracy() {
        RequestParsingException ce = new RequestParsingException();
        assertNotNull("creting RequestParsingException fails.", ce);
        assertTrue(ce instanceof BaseException);
    }

    /**
     * Tests RequestParsingException(String message) method with accuracy state.
     */
    public void testRequestParsingException2Accuracy() {
        RequestParsingException ce = new RequestParsingException("msg");
        assertNotNull("creting RequestParsingException fails.", ce);
        assertTrue("creting RequestParsingException fails.", ce instanceof BaseException);
        assertEquals("creting RequestParsingException fails.", "msg", ce.getMessage());
    }

    /**
     * Tests RequestParsingException(Throwable cause) method with accuracy state.
     */
    public void testRequestParsingException3Accuracy() {
        Exception e = new IllegalArgumentException("msg");
        RequestParsingException ce = new RequestParsingException(e);
        assertNotNull("creting RequestParsingException fails.", ce);
        assertTrue("creting RequestParsingException fails.", ce instanceof BaseException);
        assertEquals("creting RequestParsingException fails.", e, ce.getCause());
    }

    /**
     * Tests RequestParsingException(String message, Throwable cause) method with accuracy state.
     */
    public void testRequestParsingException4Accuracy() {
        Exception e = new IllegalArgumentException("msg2");
        RequestParsingException ce = new RequestParsingException("msg", e);
        assertNotNull("creting RequestParsingException fails.", ce);
        assertTrue("creting RequestParsingException fails.", ce instanceof BaseException);
        assertEquals("creting RequestParsingException fails.", "msg", ce.getMessage());
        assertEquals("creting RequestParsingException fails.", e, ce.getCause());
    }
}
