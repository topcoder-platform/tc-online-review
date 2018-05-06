/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

/**
 * <p>
 * The mock class of the ExternalObjectImpl class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class MockExternalObjectImpl extends ExternalObjectImpl {

    /**
     * <p>
     * The ctor delegates to its super.
     * </p>
     *
     * @param id
     *            the unique (among objects of this type) identifier of this object.
     * @throws IllegalArgumentException
     *             if id is negative.
     */
    public MockExternalObjectImpl(long id) {
        super(id);
    }
}
