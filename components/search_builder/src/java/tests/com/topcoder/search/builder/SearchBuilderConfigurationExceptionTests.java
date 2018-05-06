/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import junit.framework.TestCase;


/**
 * <p>
 * Unit test cases for SearchBuilderConfigurationExceptionTest.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public class SearchBuilderConfigurationExceptionTests extends TestCase {
    /** The test message. */
    private static final String ERROR_MESSAGE = "test exception message";
    /**
     * the cause Exception.
     */
    private final Exception cause = new NullPointerException();

    /**
     * test the excption constructor with ERROR_MESSAGE.
     */
    public void testSearchBuilderConfigurationException1() {
        SearchBuilderConfigurationException de = new SearchBuilderConfigurationException(ERROR_MESSAGE);
        assertNotNull("Unable to instantiate SearchBuilderConfigurationException.", de);

        assertEquals("Error message is not properly propagated to super class.", ERROR_MESSAGE, de.getMessage());
        assertTrue("The error message should match.", de.getMessage().indexOf(ERROR_MESSAGE) >= 0);
    }

    /**
     * test the excption constructor with ERROR_MESSAGE and throwable.
     */
    public void testSearchBuilderConfigurationException2() {
        SearchBuilderConfigurationException de = new SearchBuilderConfigurationException(ERROR_MESSAGE, cause);

        assertNotNull("Unable to instantiate SearchBuilderConfigurationException.", de);

        assertEquals("Cause is not properly propagated to super class.", cause, de.getCause());
    }

    /**
     * Inheritance test.
     */
    public void testSearchBuilderConfigurationException3() {
        assertTrue("SearchBuilderConfigurationException does not subclass SearchBuilderException.",
            new SearchBuilderConfigurationException(ERROR_MESSAGE) instanceof SearchBuilderException);

        assertTrue("SearchBuilderConfigurationException does not subclass SearchBuilderException.",
            new SearchBuilderConfigurationException(ERROR_MESSAGE, cause) instanceof SearchBuilderException);
    }

}
