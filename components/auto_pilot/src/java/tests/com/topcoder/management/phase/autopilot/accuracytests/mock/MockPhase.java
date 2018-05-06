/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.mock;

import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * <p>A mock implementation of {@link Phase} class to be used for testing.
 * Overrides the protected methods declared by a super-class. The overridden methods are declared with package private access
 * so only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author  isv
 * @version 1.0
 */
public class MockPhase extends Phase {

    private long id;
    private Project project;
    private PhaseType type;
    private PhaseStatus status;
    private List deps = new ArrayList();

    public MockPhase(Project project, long length) {
        super(project, length);
    }

    public MockPhase(long id, Project project) {
        super(project, id);
        this.id = id;
        this.project = project;
    }

    /**
     * @see Phase#setId(long)
     */
    public void setId(long long0) {
        this.id = long0;
    }

    /**
     * @see Phase#getId()
     */
    public long getId() {
        return this.id;
    }

    /**
     * @see Phase#getProject()
     */
    public Project getProject() {
        if (this.project == null) {
           return super.getProject();
        }
        return this.project;
    }

    /**
     * @see Phase#setPhaseType(PhaseType)
     */
    public void setPhaseType(PhaseType phaseType0) {
        this.type = phaseType0;
    }

    /**
     * @see Phase#getPhaseType()
     */
    public PhaseType getPhaseType() {
        return this.type;
    }

    /**
     * @see Phase#setPhaseStatus(PhaseStatus)
     */
    public void setPhaseStatus(PhaseStatus phaseStatus0) {
        this.status = phaseStatus0;
    }

    /**
     * @see Phase#getPhaseStatus()
     */
    public PhaseStatus getPhaseStatus() {
        return this.status;
    }

    /**
     * @see Phase#setFixedStartDate(Date)
     */
    public void setFixedStartDate(Date date0) {
    }

    /**
     * @see Phase#getFixedStartDate()
     */
    public Date getFixedStartDate() {
        return null;
    }

    /**
     * @see Phase#setScheduledStartDate(Date)
     */
    public void setScheduledStartDate(Date date0) {
    }

    /**
     * @see Phase#getScheduledStartDate()
     */
    public Date getScheduledStartDate() {
        return null;
    }

    /**
     * @see Phase#setScheduledEndDate(Date)
     */
    public void setScheduledEndDate(Date date0) {
    }

    /**
     * @see Phase#getScheduledEndDate()
     */
    public Date getScheduledEndDate() {
        return null;
    }

    /**
     * @see Phase#setLength(long)
     */
    public void setLength(long long0) {
    }

    /**
     * @see Phase#getLength()
     */
    public long getLength() {
        return 0;
    }

    /**
     * @see Phase#setActualStartDate(Date)
     */
    public void setActualStartDate(Date date0) {
    }

    /**
     * @see Phase#getActualStartDate()
     */
    public Date getActualStartDate() {
        return null;
    }

    /**
     * @see Phase#setActualEndDate(Date)
     */
    public void setActualEndDate(Date date0) {
    }

    /**
     * @see Phase#getActualEndDate()
     */
    public Date getActualEndDate() {
        return null;
    }

    /**
     * @see Phase#addDependency(Dependency)
     */
    public void addDependency(Dependency dependency0) {
    }

    /**
     * @see Phase#removeDependency(Dependency)
     */
    public void removeDependency(Dependency dependency0) {
    }

    /**
     * @see Phase#getAllDependencies()
     */
    public Dependency[] getAllDependencies() {
        return null;
    }

    /**
     * @see Phase#calcStartDate()
     */
    public Date calcStartDate() {
        return null;
    }

    /**
     * @see Phase#calcEndDate()
     */
    public Date calcEndDate() {
        return null;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final MockPhase mockPhase = (MockPhase) o;

        if (id != mockPhase.id) return false;

        return true;
    }

    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
