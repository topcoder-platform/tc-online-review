/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.link;

import java.io.Serializable;

/**
 * <p>
 * Project link type.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectLinkType implements Serializable {
    /**
     * <p>
     * Generated Serialized UID.
     * </p>
     */
    private static final long serialVersionUID = 7098255451121192100L;

    /**
     * <p>
     * The default constructor.
     * </p>
     */
    public ProjectLinkType() {
        // do nothing
    }

    /**
     * <p>
     * The constructor.
     * </p>
     *
     * @param id the id
     * @param name the name
     */
    public ProjectLinkType(long id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * <p>
     * The id field.
     * </p>
     */
    private long id;

    /**
     * <p>
     * The name field.
     * </p>
     */
    private String name;

    /**
     * <p>
     * Sets the <code>id</code> field value.
     * </p>
     *
     * @param id the value to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>
     * Gets the <code>id</code> field value.
     * </p>
     *
     * @return the <code>id</code> field value
     */
    public long getId() {
        return this.id;
    }

    /**
     * <p>
     * Sets the <code>name</code> field value.
     * </p>
     *
     * @param name the value to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Gets the <code>name</code> field value.
     * </p>
     *
     * @return the <code>name</code> field value
     */
    public String getName() {
        return this.name;
    }
}
