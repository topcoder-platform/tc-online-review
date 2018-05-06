/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the ExternalObjectImpl class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class ExternalObjectImplUnitTest extends TestCase {

    /**
     * <p>
     * The default id of the ObjectImpl.
     * </p>
     */
    private static final long DEFAULTID = 123;

    /**
     * <p>
     * An ExternalObjectImpl instance for testing.
     * </p>
     */
    private MockExternalObjectImpl objectImpl = null;

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
        objectImpl = new MockExternalObjectImpl(DEFAULTID);
    }

    /**
     * <p>
     * Set objectImpl to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        objectImpl = null;

        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(long).
     * </p>
     * <p>
     * The ExternalObjectImpl instance should be created successfully.
     * </p>
     */
    public void testCtor_Long() {
        assertNotNull("ExternalObjectImpl should be accurately created.", objectImpl);
        assertTrue("objectImpl should be instance of ExternalObjectImpl.", objectImpl instanceof ExternalObjectImpl);
    }

    /**
     * <p>
     * Tests the failure of the ctor(long).
     * </p>
     * <p>
     * If the given id is negative, IllegalArgumentException should be thrown.
     * </p>
     */
    public void testGetRatingType_Int_Negative() {
        try {
            new MockExternalObjectImpl(-1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void testGetter_GetId() {
        assertEquals("The id should be got correctly.", DEFAULTID, objectImpl.getId());
    }
}
