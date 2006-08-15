/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.phase;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of PhaseCriteria.
 *
 * @author brain_cn
 * @version 1.0
 */
public class PhaseCriteria extends BaseDTO {
    /** Represents the phase_criteria table name. */
    public static final String TABLE_NAME = "phase_criteria";
    public static final int SCORECARD_ID = 1;
    public static final int REGISTRATION_NUMBER = 2;
    public static final int SUBMISSION_NUMBER = 3;
    public static final int VIEW_RESPONSE_DURING_APPEALS = 4;
    public static final int MANUAL_SCREENING = 5;
    private int phaseId;
    private int phaseCriteriaTypeId;
    private String parameter;

    /**
     * Returns the parameter.
     *
     * @return Returns the parameter.
     */
    public String getParameter() {
        return parameter;
    }

    /**
     * Set the parameter.
     *
     * @param parameter The parameter to set.
     */
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    /**
     * Returns the phaseCriteriaTypeId.
     *
     * @return Returns the phaseCriteriaTypeId.
     */
    public int getPhaseCriteriaTypeId() {
        return phaseCriteriaTypeId;
    }

    /**
     * Set the phaseCriteriaTypeId.
     *
     * @param phaseCriteriaTypeId The phaseCriteriaTypeId to set.
     */
    public void setPhaseCriteriaTypeId(int phaseCriteriaTypeId) {
        this.phaseCriteriaTypeId = phaseCriteriaTypeId;
    }

    /**
     * Returns the phaseId.
     *
     * @return Returns the phaseId.
     */
    public int getPhaseId() {
        return phaseId;
    }

    /**
     * Set the phaseId.
     *
     * @param phaseId The phaseId to set.
     */
    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }
}
