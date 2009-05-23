/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.io.Serializable;

/**
 * <p>A simple <code>DTO</code> representing a single project link type.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * @since OR Project Linking assembly
 */
public class ProjectLinkType implements Serializable {

    /**
     * <p>A <code>long</code> providing the ID of this project link type.</p>
     */
    private long id = 0;

    /**
     * <p>A <code>String</code> providing the name of this project link type.</p>
     */
    private String name = null;

    /**
     * <p>Constructs new <code>ProjectLinkType</code> instance. This implementation does nothing.</p>
     */
    public ProjectLinkType() {
    }

    /**
     * <p>Constructs new <code>ProjectLinkType</code> instance with specified ID and name.</p>
     *
     * @param id a <code>long</code> providing the ID of this project link type.
     * @param name a <code>String</code> providing the name of this project link type.
     */
    public ProjectLinkType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * <p>Gets the ID for this project link type.</p>
     *
     * @return a <code>long</code> providing the ID of this project link type.
     */
    public long getId() {
        return this.id;
    }

    /**
     * <p>Sets the ID for this project link type.</p>
     *
     * @param id a <code>long</code> providing the ID of this project link type.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>Gets the name for this project link type.</p>
     *
     * @return a <code>String</code> providing the name of this project link type.
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>Sets the name for this project link type.</p>
     *
     * @param name a <code>String</code> providing the name of this project link type.
     */
    public void setName(String name) {
        this.name = name;
    }
}
