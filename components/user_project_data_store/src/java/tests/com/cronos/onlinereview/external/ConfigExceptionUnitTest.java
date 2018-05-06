/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the ConfigException class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ConfigExceptionUnitTest extends TestCase {

    /**
     * <p>
     * The Default Exception Message.
     * </p>
     */
    private static final String DEFUALT_EXCEPTION_MESSAGE = "DefaultExceptionMessage";

    /**
     * <p>
     * The Default Throwable Message.
     * </p>
     */
    private static final String DEFAULT_THROWABLE_MESSAGE = "DefaultThrowableMessage";

    /**
     * <p>
     * An ConfigException instance for testing.
     * </p>
     */
    private ConfigException defaultException = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        defaultException = new ConfigException(DEFUALT_EXCEPTION_MESSAGE);
    }

    /**
     * <p>
     * Set defaultException to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        defaultException = null;
        super.tearDown();
    }

    /**
     * <p>
     * Tests the ctor(String).
     * </p>
     * <p>
     * The ConfigException instance should be created successfully.
     * </p>
     */
    public void testCtor_String() {
        assertNotNull("ConfigException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of ConfigException.",
                defaultException instanceof ConfigException);
        assertTrue("ConfigException should be accurately created with the same Exception message.", defaultException
                .getMessage().indexOf(DEFUALT_EXCEPTION_MESSAGE) >= 0);
    }

    /**
     * <p>
     * Tests the ctor(String, Throwable).
     * </p>
     * <p>
     * The ConfigException instance should be created successfully.
     * </p>
     */
    public void testCtor_StringThrowable() {
        defaultException = new ConfigException(DEFUALT_EXCEPTION_MESSAGE, new Throwable(DEFAULT_THROWABLE_MESSAGE));

        assertNotNull("ConfigException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of ConfigException.",
                defaultException instanceof ConfigException);
        assertTrue("ConfigException should be accurately created with the same Exception message.", defaultException
                .getMessage().indexOf(DEFUALT_EXCEPTION_MESSAGE) >= 0);
        assertTrue("ConfigException should be accurately created with the same Throwable message.", defaultException
                .getCause().toString().indexOf(DEFAULT_THROWABLE_MESSAGE) >= 0);
    }
}
