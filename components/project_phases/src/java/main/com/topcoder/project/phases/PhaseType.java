/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases;

import java.io.Serializable;

/**
 * <p>
 * This class represents the phase type. A phase type consists of a numeric identifier and a name. This class is
 * serializable.
 * </p>
 * <p>
 * Thread Safety: This class is mutable. so it's not thread safe.
 * </p>
 *
 * @author oldbig, littlebull
 * @version 2.0
 */
public class PhaseType implements Serializable {
    /**
     * Represents the phase type id. Initialized in the constructor and could be accessed via getter and setter method.
     * The value could not be negative .
     */
    private long id;

    /**
     * Represents the phase type name. Initialized in the constructor and could be accessed via getter and setter
     * method. The value could not be null .
     */
    private String name;

    /**
     * The constructor with the phase type id and name.
     *
     * @param id
     *            the phase type id
     * @param name
     *            the phase type name
     * @throws IllegalArgumentException
     *             if <code>id</code> is negative or <code>name</code> is null
     */
    public PhaseType(long id, String name) {
        setId(id);
        setName(name);
    }

    /**
     * Gets the phase type id.
     *
     * @return the phase type id
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the phase type id.
     *
     * @param id
     *            the phase type id
     * @throws IllegalArgumentException
     *             if <code>id</code> is negative
     */
    public void setId(long id) {
        ProjectPhaseHelper.checkLongNotNegative(id, "id");

        this.id = id;
    }

    /**
     * Gets the phase type name.
     *
     * @return the phase type name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the phase type name.
     *
     * @param name
     *            the phase type name.
     * @throws IllegalArgumentException
     *             if <code>name</code> is null
     */
    public void setName(String name) {
        ProjectPhaseHelper.checkObjectNotNull(name, "name");

        this.name = name;
    }
}
