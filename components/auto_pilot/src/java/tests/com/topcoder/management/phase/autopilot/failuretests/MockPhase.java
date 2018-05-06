/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;


/**
 * A mock class extends <code>Phase</code>, simply overrides getPhase, getPhaseType, getProject and getAllDependencies
 * methods.
 *
 * @author skatou
 * @version 1.0
 */
public class MockPhase extends Phase {
    /** Represents the phase status of this phase. */
    private MockPhaseStatus phaseStatus = new MockPhaseStatus(0, "");

    /** Represents the phase type of this phase. */
    private PhaseType phaseType = new PhaseType(1, "sample");

    /** Represents the project of this phase. */
    private Project project = null;

    /**
     * Creates a new MockPhase object.
     *
     * @param project the project this phase belong to.
     * @param length the length in milliseconds of the phase.
     */
    public MockPhase(Project project, long length) {
        super(project, length);
        this.project = project;
    }

    /**
     * Gets the phase status of this phase.
     *
     * @return the phase status of this phase.
     */
    public PhaseStatus getPhaseStatus() {
        return phaseStatus;
    }

    /**
     * Gets the phase type of this phase.
     *
     * @return the phase type of this phase.
     */
    public PhaseType getPhaseType() {
        return phaseType;
    }

    /**
     * Gets the project this phase belongs to.
     *
     * @return the project this phase belongs to.
     */
    public Project getProject() {
        if (project == null) {
          return super.getProject();
        }
        return project;
    }

    /**
     * Gets the dependencies of this phase.
     *
     * @return an array of dependency of this phase.
     */
    public Dependency[] getAllDependencies() {
        return new Dependency[0];
    }
}
