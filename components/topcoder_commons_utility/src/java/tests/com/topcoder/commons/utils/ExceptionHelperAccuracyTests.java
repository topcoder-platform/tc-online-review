/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

/**
 * <p>
 * Unit tests for {@link ExceptionHelper} class.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class ExceptionHelperAccuracyTests {

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ExceptionHelperAccuracyTests.class);
    }

    /**
     * <p>
     * Accuracy test for {@link ExceptionHelper#constructException(Class, String)}.
     * </p>
     *
     * <p>
     * The exception should be constructed correctly.
     * </p>
     */
    @Test
    public void testConstructException1() {
        IllegalArgumentException iae = ExceptionHelper.constructException(IllegalArgumentException.class, "test");
        Assert.assertNotNull("Exception should be constructed correctly.", iae);
        Assert.assertTrue("Exception should be constructed correctly.", iae instanceof IllegalArgumentException);
        Assert.assertEquals("Exception should be constructed correctly.", "test", iae.getMessage());

    }

    /**
     * <p>
     * Accuracy test for {@link ExceptionHelper#constructException(Class, String, Throwable)}.
     * </p>
     *
     * <p>
     * The exception should be constructed correctly.
     * </p>
     */
    @Test
    public void testConstructException2() {
        Throwable t = new Throwable();
        IllegalArgumentException iae = ExceptionHelper.constructException(IllegalArgumentException.class, "test", t);
        Assert.assertNotNull("Exception should be constructed correctly.", iae);
        Assert.assertTrue("Exception should be constructed correctly.", iae instanceof IllegalArgumentException);
        Assert.assertEquals("Exception should be constructed correctly.", "test", iae.getMessage());
        Assert.assertEquals("Exception should be constructed correctly.", t, iae.getCause());
    }
}
