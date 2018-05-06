/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the UserProjectDataStoreException class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class UserProjectDataStoreExceptionUnitTest extends TestCase {

    /**
     * <p>
     * The Default Exception Message.
     * </p>
     */
    private static final String DEFAULT_EXCEPTION_MESSAGE = "DefaultExceptionMessage";

    /**
     * <p>
     * The Default Throwable Message.
     * </p>
     */
    private static final String DEFAUL_THROWABLE_MESSAGE = "DefaultThrowableMessage";

    /**
     * <p>
     * An UserProjectDataStoreException instance for testing.
     * </p>
     */
    private UserProjectDataStoreException defaultException = null;

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
        defaultException = new UserProjectDataStoreException();
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
     * Tests the ctor().
     * </p>
     * <p>
     * The UserProjectDataStoreException instance should be created successfully.
     * </p>
     */
    public void testCtor() {
        assertNotNull("UserProjectDataStoreException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of UserProjectDataStoreException.",
                defaultException instanceof UserProjectDataStoreException);
    }

    /**
     * <p>
     * Tests the ctor(String).
     * </p>
     * <p>
     * The UserProjectDataStoreException instance should be created successfully.
     * </p>
     */
    public void testCtor_String() {
        defaultException = new UserProjectDataStoreException(DEFAULT_EXCEPTION_MESSAGE);

        assertNotNull("UserProjectDataStoreException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of UserProjectDataStoreException.",
                defaultException instanceof UserProjectDataStoreException);
        assertTrue("UserProjectDataStoreException should be accurately created with the same Exception " + "message.",
                defaultException.getMessage().indexOf(DEFAULT_EXCEPTION_MESSAGE) >= 0);
    }

    /**
     * <p>
     * Tests the ctor(String, Throwable).
     * </p>
     * <p>
     * The UserProjectDataStoreException instance should be created successfully.
     * </p>
     */
    public void testCtor_StringThrowable() {
        defaultException = new UserProjectDataStoreException(DEFAULT_EXCEPTION_MESSAGE, new Throwable(
                DEFAUL_THROWABLE_MESSAGE));

        assertNotNull("UserProjectDataStoreException should be accurately created.", defaultException);
        assertTrue("defaultException should be instance of UserProjectDataStoreException.",
                defaultException instanceof UserProjectDataStoreException);
        assertTrue("UserProjectDataStoreException should be accurately created with the same Exception " + "message.",
                defaultException.getMessage().indexOf(DEFAULT_EXCEPTION_MESSAGE) >= 0);
        assertTrue("UserProjectDataStoreException should be accurately created with the same Throwable " + "message.",
                defaultException.getCause().toString().indexOf(DEFAUL_THROWABLE_MESSAGE) >= 0);
    }
}
