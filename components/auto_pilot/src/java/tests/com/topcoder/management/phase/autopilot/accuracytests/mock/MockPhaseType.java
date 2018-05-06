/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.project.phases.PhaseType;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockPhaseType extends PhaseType {

    public static final MockPhaseType REVIEW = new MockPhaseType("Review", 2);
    public static final MockPhaseType SUBMISSION = new MockPhaseType("Submission", 1);

    private String name;
    private long id;

    public MockPhaseType(String name, long id) {
        super(id, name);
        this.name = name;
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public long getId() {
        return this.id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }
}
