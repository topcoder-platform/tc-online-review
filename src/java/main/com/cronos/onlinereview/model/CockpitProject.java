/*
 * Copyright (C) 2009 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import java.io.Serializable;

/**
 * <p>
 * A simple bean class that holds cockpit project id and name.
 *
 * This class has been created, in case client project retrieval shifts to webservice we might use DII (dynamic
 * invocation interface). In that class all other code except the one which is retrieving the client projects would
 * remain unchanged.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class CockpitProject implements Serializable {
    /**
     * A default serial version id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Id of the the client project.
     */
    private long id;

    /**
     * Name of the client project.
     */
    private String name;

    /**
     * A default empty constructor.
     */
    public CockpitProject() {
        // empty constructor.
    }

    /**
     * <p>
     * Gets the id of the cockpit project.
     * </p>
     *
     * @return the id of the cockpit project.
     */
    public long getId() {
        return id;
    }

    /**
     * <p>
     * Sets the id of the cockpit project.
     * </p>
     *
     * @param id
     *            the id of the cockpit project.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>
     * Gets the name of the cockpit project.
     * </p>
     *
     * @return the name of the cockpit project.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Sets the name of the cockpit project.
     * </p>
     *
     * @param name
     *            the name of the cockpit project.
     */
    public void setName(String name) {
        this.name = name;
    }
}
