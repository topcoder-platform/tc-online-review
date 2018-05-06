/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.scorecard.data;

/**
 * Mock subclass of ScorecardStatus.
 *
 * @author assistant
 * @version 1.0
 */
public class MockScorecardStatus extends ScorecardStatus {

    /**
     * Represents the id.
     */
    private long id;

    /**
     * Represents the name.
     */
    private String name;

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
