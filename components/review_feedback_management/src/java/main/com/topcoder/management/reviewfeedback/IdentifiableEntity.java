/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

/**
 * <p>
 * This is a base class for identifiable entities. It's a simply POJO, containing an Id property (with public getter and
 * setter with no validation) and default empty constructor.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, sparemax
 * @version 2.0
 * @since 2.0
 */
public abstract class IdentifiableEntity {
    /**
     * Identity of this entity. It's fully mutable, has public getter and setter. Can be any value.
     */
    private long id;

    /**
     * Creates an instance of IdentifiableEntity.
     */
    protected IdentifiableEntity() {
        // Empty
    }

    /**
     * Gets the identity of this entity.
     *
     * @return the identity of this entity.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the identity of this entity.
     *
     * @param id
     *            the identity of this entity.
     */
    public void setId(long id) {
        this.id = id;
    }
}
