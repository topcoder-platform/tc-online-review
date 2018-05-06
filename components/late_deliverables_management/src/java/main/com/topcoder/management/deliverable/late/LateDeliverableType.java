/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;


/**
 * <p>
 * This class is a container for information about a single late deliverable type. It is a simple JavaBean (POJO) that
 * provides getters and setters for all private attributes and performs no argument validation in the setters.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 * @since 1.0.6
 */
public class LateDeliverableType {
    /**
     * The ID of the late deliverable type. Can be any value. Has getter and setter.
     */
    private long id;

    /**
     * The name of the late deliverable type. Can be any value. Has getter and setter.
     */
    private String name;

    /**
     * The description of the late deliverable type. Can be any value. Has getter and setter.
     */
    private String description;

    /**
     * Creates an instance of LateDeliverableType.
     */
    public LateDeliverableType() {
        // Empty
    }

    /**
     * Retrieves the ID of the late deliverable type.
     *
     * @return the ID of the late deliverable type.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the ID of the late deliverable type.
     *
     * @param id
     *            the ID of the late deliverable type.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the late deliverable type.
     *
     * @return the name of the late deliverable type.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the late deliverable type.
     *
     * @param name
     *            the name of the late deliverable type.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the description of the late deliverable type.
     *
     * @return the description of the late deliverable type.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the late deliverable type.
     *
     * @param description
     *            the description of the late deliverable type.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns a String representing this object.
     *
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return Helper.concat(this.getClass().getName(), "{",
            "id:", id,
            ", name:", name,
            ", description:", description, "}");
    }
}
