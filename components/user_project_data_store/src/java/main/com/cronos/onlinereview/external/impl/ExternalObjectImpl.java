/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import com.cronos.onlinereview.external.ExternalObject;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;

/**
 * <p>
 * Basic implementation of the <code>{@link ExternalObject}</code> interface; merely maintains the id field as a final
 * long.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is thread-safe, but subclasses are not guaranteed to be.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public abstract class ExternalObjectImpl implements ExternalObject {

    /**
     * <p>
     * The unique (among objects of this type) identifier of this object as set in the constructor and accessed by
     * getId. Will never be negative.
     * </p>
     */
    private final long id;

    /**
     * <p>
     * Constructs this object with the given identifier.
     * </p>
     *
     * @param id
     *            the unique (among objects of this type) identifier of this object.
     * @throws IllegalArgumentException
     *             if id is negative.
     */
    protected ExternalObjectImpl(long id) {
        UserProjectDataStoreHelper.validateNegative(id, "id");

        this.id = id;
    }

    /**
     * <p>
     * Returns the unique identifier of this object as set in the constructor.
     * </p>
     *
     * @return the unique identifier of this object. Will never be negative.
     */
    public long getId() {
        return this.id;
    }
}
