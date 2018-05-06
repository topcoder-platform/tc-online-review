/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

/**
 * Mock subclass of Upload.
 *
 * @author assistant
 * @version 1.0
 */
public class MockUpload extends Upload {

    /**
     * Represents the id.
     */
    private long id;
    /**
     * Represents the owner.
     */
    private long owner;

    /**
     * Get the id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the owner.
     * @return the owner
     */
    public long getOwner() {
        return owner;
    }

    /**
     * Set the owner.
     * @param owner the owner
     */
    public void setOwner(long owner) {
        this.owner = owner;
    }
}
