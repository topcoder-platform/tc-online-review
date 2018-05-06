/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.project.phases.PhaseStatus;

/**
 * A mock class extends <code>PhaseStatus</code>, simply implements setName and getName methods.
 *
 * @author skatou
 * @version 1.0
 */
public class MockPhaseStatus extends PhaseStatus {
    /** Represents the name of the phase status. */
    private String name;

    /**
     * Creates a new MockPhaseStatus object.
     *
     * @param id the phase status id.
     * @param name the name of the phase status.
     */
    public MockPhaseStatus(long id, String name) {
        super(id, name);
        this.name = name;
    }

    /**
     * Sets the name of this phase status.
     *
     * @param name the name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this phase status.
     *
     * @return the name of this phase status.
     */
    public String getName() {
        return name;
    }
}
