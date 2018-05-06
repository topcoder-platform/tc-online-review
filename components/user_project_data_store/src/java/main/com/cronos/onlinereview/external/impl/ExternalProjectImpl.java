/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.util.HashSet;
import java.util.Set;

import com.cronos.onlinereview.external.ExternalProject;
import com.cronos.onlinereview.external.UserProjectDataStoreHelper;

/**
 * <p>
 * Basic implementation of the <code>{@link ExternalProject}</code> interface.
 * </p>
 * <p>
 * If any field is already set and the user attempts to set them again, an exception will be thrown. The unique id
 * (project id) is described by the super class.
 * </p>
 * <p>
 * <b>Added in 1.1</b>:<br>
 * adds short/functional description plus technologies members, and their related get/set methods.
 * </p>
 * <p>
 * <b>Thread Safety</b>: This class is not thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author sql_lall, FireIce
 * @version 1.1
 * @since 1.0
 */
public class ExternalProjectImpl extends ExternalObjectImpl implements ExternalProject {

    /**
     * <p>
     * The version number of project as set in the constructor and accessed by getVersionId.
     * </p>
     * <p>
     * Will always be positive.
     * </p>
     */
    private final long versionId;

    /**
     * <p>
     * The text of the version of project as set in the constructor and accessed by getVersion.
     * </p>
     * <p>
     * Will never be null, but may be empty after trim.
     * </p>
     */
    private final String version;

    /**
     * <p>
     * The id of the component that project is for as set in setComponentId and accessed by getComponentId.
     * </p>
     * <p>
     * May be -1 if no component is assigned.
     * </p>
     */
    private long componentId = -1;

    /**
     * <p>
     * The id of the forum for project as set in setForumId and accessed by getForumId.
     * </p>
     * <p>
     * May be -1 if no forum is configured.
     * </p>
     */
    private long forumId = -1;

    /**
     * <p>
     * The id of the catalog of the component of project as set in setCatalogId and accessed by getCatalogId.
     * </p>
     * <p>
     * May be -1 if there is no component.
     * </p>
     */
    private long catalogId = -1;

    /**
     * <p>
     * The name of component of this project as set by setName and accessed by getName.
     * </p>
     * <p>
     * May be null if there is no component, and may be empty even after trim if set.
     * </p>
     */
    private String name = null;

    /**
     * <p>
     * The description of the component of project as set setDescription and accessed by getDescription.
     * </p>
     * <p>
     * May be null.
     * </p>
     */
    private String description = null;

    /**
     * <p>
     * The comments associated with this project as set by setComments and accessed by getComments.
     * </p>
     * <p>
     * May be null.
     * </p>
     */
    private String comments = null;

    /**
     * <p>
     * The optional shortened description of the project as set in setShortDescription and accessed by getDescription.
     * </p>
     * <p>
     * It is initially null, can be set once to a non-null string, and when null, the empty string is returned from
     * getShortDescription().
     * </p>
     */
    private String shortDescription = null;

    /**
     * <p>
     * The optional functional description of the project as set in setFunctionalDescription and accessed by
     * getFunctionalDescription.
     * </p>
     * <p>
     * Initially null, can be set once to a non-null string, and when null, the empty string is returned from
     * getFunctionalDescription().
     * </p>
     */
    private String functionalDescription = null;

    /**
     * <p>
     * The set of unique technologies for this project.
     * </p>
     * <p>
     * Entries will always be non-null Strings. Modified by addTechnology and retrieved by getTechnologies. The
     * underlying HashSet instance is immutable, and never null.
     * </p>
     */
    private final Set technologies = new HashSet();

    /**
     * <p>
     * Constructs this object with the given parameters by copying to the appropriate fields.
     * </p>
     *
     * @param id
     *            the identifier of this project.
     * @param versionId
     *            the version id of this project.
     * @param version
     *            the version text of this project. This value may be empty, even after trim, but may never be null.
     * @throws IllegalArgumentException
     *             if id or versionId is negative, or if version is null.
     */
    public ExternalProjectImpl(long id, long versionId, String version) {
        super(id);

        UserProjectDataStoreHelper.validateNegative(versionId, "versionId");
        UserProjectDataStoreHelper.validateNull(version, "version");

        this.versionId = versionId;
        this.version = version;
    }

    /**
     * <p>
     * Returns the catalog id of the component of this project.
     * </p>
     *
     * @return the catalog id of the component of this project. May be -1 if there is no component.
     */
    public long getCatalogId() {
        return this.catalogId;
    }

    /**
     * <p>
     * Returns the comments of the component of this project.
     * </p>
     *
     * @return the comments of the component of this project. May be null.
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * <p>
     * Returns the id of the component of this project.
     * </p>
     *
     * @return the id of the component of this project. May be -1 if there is no component.
     */
    public long getComponentId() {
        return this.componentId;
    }

    /**
     * <p>
     * Returns the description of the component of this project.
     * <p>
     *
     * @return the description of the component of this project. May be null if there is no component or no description.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * <p>
     * Returns the id of the forum of this project.
     * </p>
     *
     * @return the id of the forum of this project. May be -1 if there is no forum.
     */
    public long getForumId() {
        return this.forumId;
    }

    /**
     * <p>
     * Returns the functional description for this project.
     * </p>
     * <p>
     * If the current value for the functionalDescription is null, then the empty string should be returned instead.
     * </p>
     *
     * @return The functional description for the project - will not be null, can be the empty string
     * @since 1.1
     */
    public String getFunctionalDescription() {
        return functionalDescription == null ? "" : functionalDescription;
    }

    /**
     * <p>
     * Returns the name of the component of this project.
     * </p>
     *
     * @return the name of the component of this project. May be null if there is no component or no name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * <p>
     * Returns the short description for this project.
     * </p>
     * <p>
     * If the current value for the shortDescription is null, then the empty string should be returned instead.
     * </p>
     *
     * @return The short description for the project - will not be null, can be the empty string
     * @since 1.1
     */
    public String getShortDescription() {
        return shortDescription == null ? "" : shortDescription;
    }

    /**
     * <p>
     * Returns the technologies associated to the project.
     * </p>
     * <p>
     * The returned array will not be null, but may have zero length. Also, the strings it contains will not be null,
     * but may be the empty string.
     * </p>
     *
     * @return the associated technologies. Will never be null and will never have null elements, but the length of the
     *         returned array may be zero to indicate no technologies associated.
     * @since 1.1
     */
    public String[] getTechnologies() {
        return (String[]) this.technologies.toArray(new String[technologies.size()]);
    }

    /**
     * <p>
     * Returns the version text of this project.
     * </p>
     *
     * @return the version text of this project. Will never be null, but may be empty.
     */
    public String getVersion() {
        return this.version;
    }

    /**
     * <p>
     * Returns the version number of this project.
     * </p>
     *
     * @return the version id of this project. Will never be negative.
     */
    public long getVersionId() {
        return this.versionId;
    }

    /**
     * <p>
     * Sets the catalog id of the component of this project.
     * </p>
     *
     * @param catalogId
     *            The id of the catalog of the component.
     * @throws IllegalArgumentException
     *             if the catalogId parameter is negative or the catalog id is already set.
     */
    public void setCatalogId(long catalogId) {
        UserProjectDataStoreHelper.validateNegative(catalogId, "catalogId");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.catalogId, "catalogId");

        this.catalogId = catalogId;
    }

    /**
     * <p>
     * Sets the comments associated with this project.
     * </p>
     *
     * @param comments
     *            The comments of this project. Note that this may be null or empty, even after trim.
     * @throws IllegalArgumentException
     *             if the comments parameter is <code>null</code> or the comments is already set.
     */
    public void setComments(String comments) {
        UserProjectDataStoreHelper.validateNull(comments, "comments");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.comments, "comments");

        this.comments = comments;
    }

    /**
     * <p>
     * Sets the id of the component of this project.
     * </p>
     *
     * @param componentId
     *            The id of the component of this project.
     * @throws IllegalArgumentException
     *             if the componentId parameter is negative or the component id is already set.
     */
    public void setComponentId(long componentId) {
        UserProjectDataStoreHelper.validateNegative(componentId, "componentId");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.componentId, "componentId");

        this.componentId = componentId;
    }

    /**
     * <p>
     * Sets the description of the component of this project.
     * </p>
     *
     * @param description
     *            The description of the component of this project.
     * @throws IllegalArgumentException
     *             if the description parameter is <code>null</code> or the description is already set.
     */
    public void setDescription(String description) {
        UserProjectDataStoreHelper.validateNull(description, "description");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.description, "description");

        this.description = description;
    }

    /**
     * <p>
     * Sets the id of the forum of this project.
     * </p>
     *
     * @param forumId
     *            The id of the forum of the component.
     * @throws IllegalArgumentException
     *             if the forumId parameter is negative or the forum id is already set.
     */
    public void setForumId(long forumId) {
        UserProjectDataStoreHelper.validateNegative(forumId, "forumId");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.forumId, "forumId");

        this.forumId = forumId;
    }

    /**
     * <p>
     * Sets the functional description associated with this project.
     * </p>
     *
     * @param functionalDescription
     *            The functional description to use for the project. This may be empty, but not null
     * @throws IllegalArgumentException
     *             if the functionalDescription is <code>null</code>, or already set (not null)
     * @since 1.1
     */
    public void setFunctionalDescription(String functionalDescription) {
        UserProjectDataStoreHelper.validateNull(functionalDescription, "functionalDescription");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.functionalDescription, "functionalDescription");

        this.functionalDescription = functionalDescription;
    }

    /**
     * <p>
     * Sets the name of the component of this project by copying to the 'name' field.
     * </p>
     *
     * @param name
     *            The name of the component of this project.
     * @throws IllegalArgumentException
     *             if the name parameter is <code>null</code> or the name is already set (not null).
     */
    public void setName(String name) {
        UserProjectDataStoreHelper.validateNull(name, "name");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.name, "name");

        this.name = name;
    }

    /**
     * <p>
     * Sets the short description associated with this project.
     * </p>
     *
     * @param shortDescription
     *            The short description to use for the project. This may be empty, but not null
     * @throws IllegalArgumentException
     *             if shortDescription is <code>null</code>, or already set (not null).
     * @since 1.1
     */
    public void setShortDescription(String shortDescription) {
        UserProjectDataStoreHelper.validateNull(shortDescription, "shortDescription");
        UserProjectDataStoreHelper.validateFieldAlreadySet(this.shortDescription, "shortDescription");

        this.shortDescription = shortDescription;
    }

    /**
     * <p>
     * Adds the given technology to the set of related technologies for this project.
     * </p>
     *
     * @param technology
     *            the technology to add to this project's set of related technologies
     * @throws IllegalArgumentException
     *             if technology parameter is <code>null</code>
     * @since 1.1
     */
    public void addTechnology(String technology) {
        UserProjectDataStoreHelper.validateNull(technology, "technology");

        this.technologies.add(technology);
    }
}
