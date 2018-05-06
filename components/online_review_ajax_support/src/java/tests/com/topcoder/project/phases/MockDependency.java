/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases;

/**
 * A mock subclass for <code>Dependency</code>.
 *
 * @author assistant
 * @version 1.0
 */
public class MockDependency extends Dependency {

    public MockDependency(Phase arg0, Phase arg1, boolean arg2, boolean arg3, long arg4) {
        super(arg0, arg1, arg2, arg3, arg4);
    }
    /**
     * The dependency.
     */
    private Phase dependency;

    /**
     * The dependent.
     */
    private Phase dependent;

    /**
     * The lag time.
     */
    private long lagTime;

    /**
     * Get dependency.
     * @return dependency.
     */
    public Phase getDependency() {
        return dependency;
    }

    /**
     * Set dependency.
     * @param dependency the dependency.
     */
    public void setDependency(Phase dependency) {
        this.dependency = dependency;
    }

    /**
     * Get dependent.
     * @return dependent
     */
    public Phase getDependent() {
        return dependent;
    }

    /**
     * Set dependent.
     * @param dependent the dependent
     */
    public void setDependent(Phase dependent) {
        this.dependent = dependent;
    }

    /**
     * Get lag time.
     * @return the lag time
     */
    public long getLagTime() {
        return lagTime;
    }

    /**
     * Set lag time.
     * @param lagTime lag time
     */
    public void setLagTime(long lagTime) {
        this.lagTime = lagTime;
    }

    /**
     * Whether dependency started.
     * @return always true
     */
    public boolean isDependencyStart() {
        return true;
    }
    /**
     * Whether dependent started.
     * @return always true
     */
    public boolean isDependentStart() {
        return true;
    }
}
