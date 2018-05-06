/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.io.Serializable;

/**
 * <p>
 * This class represents a project status from the persistence. Project statuses are stored in 'project_status_lu'
 * table. A project status instance contains id, name and description. This class is used in Project class to specify
 * the project status of a project. This class implements Serializable interface to support serialization.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author tuenm, iamajia
 * @version 1.0
 */
@SuppressWarnings("serial")
public class ProjectStatus implements Serializable {

    /**
     * Represents the id of this instance. Only values greater than zero is allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private long id = 0;

    /**
     * Represents the name of this instance. Null or empty values are not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private String name = null;

    /**
     * Represents the description of this instance. Null value is not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private String description = null;

    /**
     * Create a new ProjectStatus instance with the given id and name. The two fields are required for a this instance
     * to be persisted.
     *
     * @param id
     *            The project status id.
     * @param name
     *            The project status name.
     * @throws IllegalArgumentException
     *             If id is less than or equals to zero, or name is null or empty string.
     */
    public ProjectStatus(long id, String name) {
        this(id, name, "");
    }

    /**
     * Create a new ProjectStatus instance with the given id, name and description. The two first fields are required
     * for a this instance to be persisted.
     *
     * @param id
     *            The project status id.
     * @param name
     *            The project status name.
     * @param description
     *            The project status description.
     * @throws IllegalArgumentException
     *             If id is less than or equals to zero, or name is null or empty string, or description is null.
     */
    public ProjectStatus(long id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    /**
     * Sets the id for this project status instance. Only positive values are allowed.
     *
     * @param id
     *            The id of this project status instance.
     * @throws IllegalArgumentException
     *             If project status id is less than or equals to zero.
     */
    public void setId(long id) {
        Helper.checkNumberPositive(id, "id");
        this.id = id;
    }

    /**
     * Gets the id of this project status instance.
     *
     * @return the id of this project status instance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the name for this project status instance. Null or empty values are not allowed.
     *
     * @param name
     *            The name of this project status instance.
     * @throws IllegalArgumentException
     *             If project status name is null or empty string.
     */
    public void setName(String name) {
        Helper.checkStringNotNullOrEmpty(name, "name");
        this.name = name;
    }

    /**
     * Gets the name of this project status instance.
     *
     * @return the name of this project status instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description for this project status instance. Null value are not allowed.
     *
     * @param description
     *            The description of this project status instance.
     * @throws IllegalArgumentException
     *             If project status description is null.
     */
    public void setDescription(String description) {
        Helper.checkObjectNotNull(description, "description");
        this.description = description;
    }

    /**
     * Gets the description of this project status instance.
     *
     * @return the description of this project status instance.
     */
    public String getDescription() {
        return description;
    }
}
