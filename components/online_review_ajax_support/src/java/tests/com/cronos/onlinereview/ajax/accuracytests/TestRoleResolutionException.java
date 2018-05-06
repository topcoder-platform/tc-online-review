/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.accuracytests;

import com.cronos.onlinereview.ajax.handlers.ResourceException;

import com.topcoder.util.errorhandling.BaseException;

import junit.framework.TestCase;


/**
 * Tests for RoleResolutionException class.
 *
 * @author assistant
 * @version 1.0
 */
public class TestRoleResolutionException extends TestCase {
    /**
     * Tests RoleResolutionException() method with accuracy state.
     */
    public void testRoleResolutionException1Accuracy() {
        ResourceException ce = new ResourceException();
        assertNotNull("creting RoleResolutionException fails.", ce);
        assertTrue(ce instanceof BaseException);
    }

    /**
     * Tests RoleResolutionException(String message) method with accuracy state.
     */
    public void testRoleResolutionException2Accuracy() {
        ResourceException ce = new ResourceException("msg");
        assertNotNull("creting RoleResolutionException fails.", ce);
        assertTrue("creting RoleResolutionException fails.", ce instanceof BaseException);
        assertEquals("creting RoleResolutionException fails.", "msg", ce.getMessage());
    }

    /**
     * Tests RoleResolutionException(Throwable cause) method with accuracy state.
     */
    public void testRoleResolutionException3Accuracy() {
        Exception e = new IllegalArgumentException("msg");
        ResourceException ce = new ResourceException(e);
        assertNotNull("creting RoleResolutionException fails.", ce);
        assertTrue("creting RoleResolutionException fails.", ce instanceof BaseException);
        assertEquals("creting RoleResolutionException fails.", e, ce.getCause());
    }

    /**
     * Tests RoleResolutionException(String message, Throwable cause) method with accuracy state.
     */
    public void testRoleResolutionException4Accuracy() {
        Exception e = new IllegalArgumentException("msg2");
        ResourceException ce = new ResourceException("msg", e);
        assertNotNull("creting RoleResolutionException fails.", ce);
        assertTrue("creting RoleResolutionException fails.", ce instanceof BaseException);
        assertEquals("creting RoleResolutionException fails.", "msg", ce.getMessage());
        assertEquals("creting RoleResolutionException fails.", e, ce.getCause());
    }
}
