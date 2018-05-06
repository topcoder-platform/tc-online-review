/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.accuracytests.impl;

import com.cronos.onlinereview.external.accuracytests.AccuracyHelper;
import com.cronos.onlinereview.external.impl.ExternalObjectImpl;

import junit.framework.TestCase;


/**
 * <p>
 * Tests the ExternalObjectImpl class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public abstract class ExternalObjectImplAccuracyTest extends TestCase {
    /**
     * <p>
     * The default id of the ObjectImpl.
     * </p>
     */
    protected static final long ID = 123;

    /**
     * <p>
     * An ExternalObjectImpl instance for testing.
     * </p>
     */
    protected ExternalObjectImpl externalObject = null;

    /**
     * <p>
     * Tests the accuracy of the Constructor(long).
     * </p>
     *
     * <p>
     * The ExternalObjectImpl instance should be created successfully.
     * </p>
     */
    public void testConstructor_Long() {
        assertTrue("objectImpl should be instance of ExternalObjectImpl.", externalObject instanceof ExternalObjectImpl);
    }

    /**
     * <p>
     * Tests the accuracy of the getter getId().
     * </p>
     */
    public void testGetter_GetId() {
        assertEquals("The id should be got correctly.", new Long(ID),
            AccuracyHelper.getPrivateField(ExternalObjectImpl.class, externalObject, "id"));
    }
}
