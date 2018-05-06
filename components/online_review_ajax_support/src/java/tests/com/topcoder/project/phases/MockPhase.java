/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases;

import java.util.Date;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;

/**
 * A mock subclass for <code>Phase</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockPhase extends Phase {

    /**
     * Represents the calculated start date.
     */
    public Date calcStartDate;

    /**
     * Represents the calculated end date.
     */
    public Date calcEndDate;


    /**
     * The phase type.
     */
    private PhaseType phaseType;

    /**
     * The phase status.
     */
    private PhaseStatus phaseStatus;

    /**
     * Represents the fixed start date.
     */
    private Date fixedStartDate;

    /**
     * Represents the length.
     */
    private long length;

    /**
     * The id.
     */
    private long id;

    /**
     * The constructor.
     */
    public MockPhase() {
        super(new Project(new Date(), new DefaultWorkdays()), 1);
        this.phaseStatus = new MockPhaseStatus(1, "Appeal");
        this.phaseStatus.setId(1);
    }

    /**
     * Get the calculated end date.
     *
     * @return the calculated end date
     */
    public Date calcEndDate() {
        return calcEndDate;
    }

    /**
     * Get the calculated start date.
     * @return the calculated start date
     */
    public Date calcStartDate() {
        return calcStartDate;
    }

    /**
     * Get the fixed start date.
     * @return the fixed start date.
     */
    public Date getFixedStartDate() {
        return fixedStartDate;
    }

    /**
     * Set the fixed start date.
     * @param fixedStartDate the fixed start date
     */
    public void setFixedStartDate(Date fixedStartDate) {
        this.fixedStartDate = fixedStartDate;
    }

    /**
     * Get the length.
     *
     * @return the length
     */
    public long getLength() {
        return length;
    }

    /**
     * Set the length.
     * @param length the length
     */
    public void setLength(long length) {
        this.length = length;
    }

    /**
     * Get the phase type.
     * @return the phase type.
     */
    public PhaseType getPhaseType() {
        return this.phaseType;
    }

    /**
     * Get all dependencies.
     * @return all dependencies.
     */
    public Dependency[] getAllDependencies() {
        Phase dependency = new MockPhase();
        dependency.setId(31);

        Phase dependent = new MockPhase();
        dependent.setId(32);

        Dependency dep = new MockDependency(dependency, dependent, false, false, 100);
        dep.setDependency(dependency);
        dep.setDependent(dependent);
        dep.setLagTime(100);

        return new Dependency[] {dep};
    }

    /**
     * Get id.
     * @return id.
     */
    public long getId() {
        return id;
    }

    /**
     * Set id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Set phase type.
     * @param phaseType the phase type
     */
    public void setPhaseType(PhaseType phaseType) {
        this.phaseType = phaseType;
    }

    /**
     * @return status
     */
    public PhaseStatus getPhaseStatus() {
        return phaseStatus;
    }

    /**
     * Set phase status.
     * @param phastStatus the phase status
     */
    public void setPhaseStatus(PhaseStatus phastStatus) {
        this.phaseStatus = phastStatus;
    }
}
