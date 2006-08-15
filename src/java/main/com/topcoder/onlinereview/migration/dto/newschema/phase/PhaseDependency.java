/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.phase;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of PhaseDependency.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PhaseDependency extends BaseDTO {
    /** Represents the phase_dependency table name. */
    public static final String TABLE_NAME = "phase_dependency";
    private int dependencyPhaseId;
    private int dependentPhaseId;
    private boolean dependencyStart;
    private boolean dependentStart;
    private int lagTime;

    /**
     * Returns the dependencyPhaseId.
     *
     * @return Returns the dependencyPhaseId.
     */
    public int getDependencyPhaseId() {
        return dependencyPhaseId;
    }

    /**
     * Set the dependencyPhaseId.
     *
     * @param dependencyPhaseId The dependencyPhaseId to set.
     */
    public void setDependencyPhaseId(int dependencyPhaseId) {
        this.dependencyPhaseId = dependencyPhaseId;
    }

    /**
     * Returns the dependencyStart.
     *
     * @return Returns the dependencyStart.
     */
    public boolean isDependencyStart() {
        return dependencyStart;
    }

    /**
     * Set the dependencyStart.
     *
     * @param dependencyStart The dependencyStart to set.
     */
    public void setDependencyStart(boolean dependencyStart) {
        this.dependencyStart = dependencyStart;
    }

    /**
     * Returns the dependentPhaseId.
     *
     * @return Returns the dependentPhaseId.
     */
    public int getDependentPhaseId() {
        return dependentPhaseId;
    }

    /**
     * Set the dependentPhaseId.
     *
     * @param dependentPhaseId The dependentPhaseId to set.
     */
    public void setDependentPhaseId(int dependentPhaseId) {
        this.dependentPhaseId = dependentPhaseId;
    }

    /**
     * Returns the dependentStart.
     *
     * @return Returns the dependentStart.
     */
    public boolean isDependentStart() {
        return dependentStart;
    }

    /**
     * Set the dependentStart.
     *
     * @param dependentStart The dependentStart to set.
     */
    public void setDependentStart(boolean dependentStart) {
        this.dependentStart = dependentStart;
    }

    /**
     * Returns the lagTime.
     *
     * @return Returns the lagTime.
     */
    public int getLagTime() {
        return lagTime;
    }

    /**
     * Set the lagTime.
     *
     * @param lagTime The lagTime to set.
     */
    public void setLagTime(int lagTime) {
        this.lagTime = lagTime;
    }
}
