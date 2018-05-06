/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

/**
 * <p>
 * This interface describes a project within the <b>User Project Data Store</b> component.
 * </p>
 * <p>
 * The component id, version id, forum id and catalog id are all included, as well as textual descriptions of the
 * project and the component itself. The unique id (project id) is described by the super interface.
 * </p>
 * <p>
 * In version 2.0, other information like short description, functional description, and a collection of technologies
 * related to the project are added.
 * </p>
 * <p>
 * <b>Thread Safety</b>: Implementations of this interface are not required to be thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author sql_lall, FireIce
 * @version 1.1
 * @since 1.0
 */
public interface ExternalProject extends ExternalObject {

    /**
     * <p>
     * Returns the id of the component that this project is for.
     * </p>
     *
     * @return the id of the component that this project is for. May be -1 if no component is assigned.
     */
    long getComponentId();

    /**
     * <p>
     * Returns the version number of this project.
     * </p>
     *
     * @return the version number of this project. Will always be positive.
     */
    long getVersionId();

    /**
     * <p>
     * Returns the id of the current forum for this project.
     * </p>
     *
     * @return the id of the current forum for this project. May be -1 if no forum is configured.
     */
    long getForumId();

    /**
     * <p>
     * Returns the catalog id of the component that this project is for.
     * </p>
     *
     * @return the catalog id of the component of this project. May be -1 if there is no component.
     */
    long getCatalogId();

    /**
     * <p>
     * Returns the name of the component of this project.
     * </p>
     *
     * @return the name of the component of this project. May be null or empty after trim if there is no component.
     */
    String getName();

    /**
     * <p>
     * Returns a String representation of the version of the component that this project is for.
     * </p>
     *
     * @return the version of this project. Will never be null but may be empty after trim.
     */
    String getVersion();

    /**
     * <p>
     * Returns the description of the component that this project is for.
     * </p>
     *
     * @return the description of the component of this project. May be null or empty after trim.
     */
    String getDescription();

    /**
     * <p>
     * Returns the comments of this project.
     * </p>
     *
     * @return the comments of this project. May be null or empty after trim.
     */
    String getComments();

    /**
     * <p>
     * Returns the short description of the component that this project is for.
     * </p>
     *
     * @return the short description of the component of this project. May be empty but not null.
     * @since 1.1
     */
    String getShortDescription();

    /**
     * <p>
     * Returns the functional description of the component that this project is for.
     * </p>
     *
     * @return the functional description of the component of this project. May be empty but not null.
     * @since 1.1
     */
    String getFunctionalDescription();

    /**
     * <p>
     * Returns the technologies associated with the project.
     * </p>
     *
     * @return the array of technologies - will not be null but may be zero length, and will not contain null strings.
     * @since 1.1
     */
    String[] getTechnologies();
}
