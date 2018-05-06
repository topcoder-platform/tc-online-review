/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.util.Date;

import org.junit.Test;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

/**
 * <p>
 * Unit tests for {@link LoggingUtilityHelper} class.
 * </p>
 *
 * @author dingying131
 * @version 1.0
 */
public class LoggingUtilityHelperAccuracyTests {

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LoggingUtilityHelperAccuracyTests.class);
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingUtilityHelper#getExceptionMessage(String, Throwable)}.
     * </p>
     *
     * <p>
     * The exception message should be generated correctly.
     * </p>
     */
    @Test
    public void testGetExceptionMessage() {
        String message = LoggingUtilityHelper.getExceptionMessage("signature", new IllegalArgumentException(
                "test message"));
        Assert.assertTrue("Exception message should be correct.", message
                .startsWith("Error in method [signature], details: test message"));
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingUtilityHelper#getOutputValueMessage(Object)}.
     * </p>
     *
     * <p>
     * The output parameter should be generated correctly.
     * </p>
     */
    @Test
    public void testGetOutputValueMessage() {
        Assert.assertEquals("output parameter should be correct.", "Output parameter: test", LoggingUtilityHelper
                .getOutputValueMessage("test"));
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingUtilityHelper#getMethodExitMessage(String, Date)}.
     * </p>
     *
     * <p>
     * The method exit message should be correct.
     * </p>
     */
    @Test
    public void testGetMethodExitMessage() {
        String message = LoggingUtilityHelper.getMethodExitMessage("test", new Date());
        Assert.assertTrue("Message should be correct.", message
                .startsWith("Exiting method [test], time spent in the method: "));
        Assert.assertTrue("Message should be correct.", message.endsWith(" milliseconds."));
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingUtilityHelper#getInputParametersMessage(String[], Object[])}.
     * </p>
     *
     * <p>
     * The input parameter should be generated correctly.
     * </p>
     */
    @Test
    public void testGetInputParametersMessage() {
        String message = LoggingUtilityHelper.getInputParametersMessage(new String[] {"name", "age"},
                new Object[] {"allen", 20});
        Assert.assertEquals("Input parameter message should be correct.", "Input parameters [name:allen, age:20]",
                message);
    }

    /**
     * <p>
     * Accuracy test for {@link LoggingUtilityHelper#getMethodEntranceMessage(String)}.
     * </p>
     *
     * <p>
     * The Method entrance message should be generated correctly.
     * </p>
     */
    @Test
    public void testGetMethodEntranceMessage() {
        Assert.assertEquals("Method entrance method should be correct.", "Entering method [test].",
                LoggingUtilityHelper.getMethodEntranceMessage("test"));
    }
}
