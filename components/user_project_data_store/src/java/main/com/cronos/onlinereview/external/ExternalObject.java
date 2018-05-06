/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.io.Serializable;

/**
 * <p>
 * This simple interface merely captures common information (the id) for the other two entities in the <b>User Project
 * Data Store</b> component, namely <code>{@link ExternalUser}</code> and <code>{@link ExternalProject}</code>.
 * </p>
 * <p>
 * It also implements <code>{@link Serializable}</code> so that when subclasses are used in a web application, they
 * can be serialized for session replication.
 * </p>
 * <p>
 * <b>Thread Safety</b>: Implementations of this interface would just be thread-safe.
 * </p>
 *
 * @author dplass, oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public interface ExternalObject extends Serializable {

    /**
     * <p>
     * Returns the unique id of this object.
     * </p>
     * <p>
     * Note that uniqueness only applies across objects of the same base type. In other words, the same id may be
     * re-used for a user as for a project, but since they are stored separately, it is not an issue.
     * </p>
     *
     * @return the unique id of this object. Will never be negative.
     */
    long getId();
}
