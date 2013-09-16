/*
 * Copyright (C) 2009-2010 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.model;

import java.io.Serializable;

/**
 * <p>
 * A simple bean class that holds client/billing project id and name.
 * 
 * This class has been created, in case client project retrieval shifts to webservice we might use DII (dynamic
 * invocation interface). In that class all other code except the one which is retrieving the client projects would
 * remain unchanged.
 * </p>
 * 
 * @author TCSASSEMBLER
 * @since Online Review Update - Add Project Dropdown v1.0
 */
public class ClientProject implements Serializable {
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
    public ClientProject() {
        // empty constructor.
    }

    /**
     * <p>
     * Gets the id of the client project.
     * </p>
     * 
     * @return the id of the client project.
     */
    public long getId() {
        return id;
    }

    /**
     * <p>
     * Sets the id of the client project.
     * </p>
     * 
     * @param id
     *            the id of the client project.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * <p>
     * Gets the name of the client project.
     * </p>
     * 
     * @return the name of the client project.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Sets the name of the client project.
     * </p>
     * 
     * @param name
     *            the name of the client project.
     */
    public void setName(String name) {
        this.name = name;
    }
}
