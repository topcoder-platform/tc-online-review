/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.project.phases.PhaseStatus;

/**
 * <p></p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class MockPhaseStatus extends PhaseStatus {

    public static final MockPhaseStatus CLOSED = new MockPhaseStatus("Closed", 1);
    public static final MockPhaseStatus OPEN = new MockPhaseStatus("Open", 2);
    public static final MockPhaseStatus SCHEDULED = new MockPhaseStatus("Scheduled", 3);

    private String name;
    private long id;

    public MockPhaseStatus(String name, long id) {
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
