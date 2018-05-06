/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases;


/**
 * Mock subclass of PhaseStatus.
 *
 * @author assistant
 * @version 1.0
 */
public class MockPhaseStatus extends PhaseStatus {


    /**
     * Represents the id.
     */
    private long id;

    /**
     * Represents the name.
     */
    private String name;

    /**
     * Default Constructor.
     *
     * @param id id
     * @param name name
     */
    public MockPhaseStatus(long id, String name) {
        super(id, name);
    }

    /**
     * Get the id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get the name.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }
}
