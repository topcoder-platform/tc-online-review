/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.resource;


/**
 * A mock subclass for <code>ResourceRole</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockResourceRole extends ResourceRole {

    /**
     * Represents the id.
     */
    private long id;

    /**
     * Represents the name.
     */
    private String name;

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
     * Set the name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name.
     * @return name
     */
    public String getName() {
        return name;
    }
}
