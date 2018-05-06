/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.io.Serializable;

/**
 * <p>
 * This class represents a project property type from the persistence. Each project property must associated with a
 * project property type. Project property type are stored in 'project_info_type_lu' table, project property in
 * 'project_info' table. A project property type instance contains id, name and description. This class is used in
 * ProjectManager#getAllProjectPropertyTypes method to return project property types from the persistence. This class
 * implements Serializable interface to support serialization.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 * <p>
 * Version 1.2.1
 * <ul>
 * <li>Added key ALLOW_STOCK_ART_KEY, VIEWABLE_SUBMISSIONS_FLAG_KEY_STRING, VIEWABLE_SUBMITTERS_KEY.</li>
 * </ul>
 * </p>
 *
 * @author tuenm, iamajia, flytoj2ee, TCSDEVELOPER
 * @version 1.2.1
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ProjectPropertyType implements Serializable {
    /**
     * Represents key for Maximum Allowed Submissions property.
     *
     * @since 1.2
     */
    public static final String MAXIMUM_SUBMISSIONS_KEY = "Maximum Submissions";

    /**
     * Represents key for Allow Stock Art property.
     *
     * @since 1.2.1
     */
    public static final String ALLOW_STOCK_ART_KEY = "Allow Stock Art";

    /**
     * Represents key for Viewable Submissions Flag property.
     *
     * @since 1.2.1
     */
    public static final String VIEWABLE_SUBMISSIONS_FLAG_KEY_STRING = "Viewable Submissions Flag";

    /**
     * Represents key for Viewable Submitters property.
     *
     * @since 1.2.1
     */
    public static final String VIEWABLE_SUBMITTERS_KEY = "Viewable Submitters";

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
     * Create a new ProjectPropertyType instance with the given id and name. The two fields are required for a this
     * instance to be persisted.
     *
     * @param id
     *            The project property type id.
     * @param name
     *            The project property type name.
     * @throws IllegalArgumentException
     *             If id is less than or equals to zero, or name is null or empty string.
     */
    public ProjectPropertyType(long id, String name) {
        this(id, name, "");
    }

    /**
     * Create a new ProjectPropertyType instance with the given id, name and description. The two first fields are
     * required for a this instance to be persisted.
     *
     * @param id
     *            The project property type id.
     * @param name
     *            The project property type name.
     * @param description
     *            The project property type description.
     * @throws IllegalArgumentException
     *             If any id is less than or equals to zero, or name is null or empty string, or description is null.
     */
    public ProjectPropertyType(long id, String name, String description) {
        setId(id);
        setName(name);
        setDescription(description);
    }

    /**
     * Sets the id for this project property type instance. Only positive values are allowed.
     *
     * @param id
     *            The id of this project property type instance.
     * @throws IllegalArgumentException
     *             If project property type id is less than or equals to zero.
     */
    public void setId(long id) {
        Helper.checkNumberPositive(id, "id");
        this.id = id;
    }

    /**
     * Gets the id of this project property type instance.
     *
     * @return the id of this project property type instance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the name for this project property type instance. Null or empty values are not allowed.
     *
     * @param name
     *            The name of this project property type instance.
     * @throws IllegalArgumentException
     *             If project property type name is null or empty string.
     */
    public void setName(String name) {
        Helper.checkStringNotNullOrEmpty(name, "name");
        this.name = name;
    }

    /**
     * Gets the name of this project property type instance.
     *
     * @return the name of this project property type instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description for this project property type instance. Null value are not allowed.
     *
     * @param description
     *            The description of this project property type instance.
     * @throws IllegalArgumentException
     *             If project property type description is null.
     */
    public void setDescription(String description) {
        Helper.checkObjectNotNull(description, description);
        this.description = description;
    }

    /**
     * Gets the description of this project property type instance.
     *
     * @return the description of this project property type instance.
     */
    public String getDescription() {
        return description;
    }
}
