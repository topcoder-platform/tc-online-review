/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.link;

import java.io.Serializable;

/**
 * <p>
 * Project link type entity. It represents the project link type.
 * </p>
 * <p>
 * Change log for version 1.1: Added allow_overlap property with respective constructor and accessor/mutator methods.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 * @since 1.0
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
     * A <code>boolean</code> flag indicating whether the links of specified type allow to overlap the schedules for the
     * respective dependent projects or not.
     * </p>
     *
     * @since 1.1
     */
    private boolean allowOverlap;

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
     * @param id
     *            the id
     * @param name
     *            the name
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public ProjectLinkType(long id, String name) {
        Helper.assertStringNotNullNorEmpty(name, "name");

        this.id = id;
        this.name = name;
        this.allowOverlap = false;
    }

    /**
     * <p>
     * The constructor.
     * </p>
     *
     * @param id
     *            the id.
     * @param name
     *            the name.
     * @param allowOverlap
     *            the allow_overlap flag.
     * @throws IllegalArgumentException
     *             if name is null or empty.
     * @since 1.1
     */
    public ProjectLinkType(long id, String name, boolean allowOverlap) {
        this(id, name);
        this.allowOverlap = allowOverlap;
    }

    /**
     * <p>
     * Sets the <code>id</code> field value.
     * </p>
     *
     * @param id
     *            the value to set
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
     * @param name
     *            the value to set
     * @throws IllegalArgumentException
     *             if name is null or empty
     */
    public void setName(String name) {
        Helper.assertStringNotNullNorEmpty(name, "name");
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

    /**
     * <p>
     * Gets the flag indicating whether the links of specified type allow to overlap the schedules for the respective
     * dependent projects or not.
     * </p>
     *
     * @return an <code>allowOverlap</code> field value.
     * @since 1.1
     */
    public boolean isAllowOverlap() {
        return this.allowOverlap;
    }

    /**
     * <p>
     * Gets the flag indicating whether the links of specified type allow to overlap the schedules for the respective
     * dependent projects or not.
     * </p>
     *
     * @param allowOverlap
     *            an <code>allowOverlap</code> field value.
     * @since 1.1
     */
    public void setAllowOverlap(boolean allowOverlap) {
        this.allowOverlap = allowOverlap;
    }
}
