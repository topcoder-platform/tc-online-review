/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.scorecard.data.Scorecard;

/**
 * <p>
 * <code>ScorecardGroupBean</code> holds a list of scorecards which share the
 * same project category.
 * </p>
 * <p>
 * It is primarily used in "list scorecards" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class ScorecardGroupBean {
    /**
     * The project category shared by the scorecards.
     */
    private ProjectCategory projectCategory;

    /**
     * The list of scorecards.
     */
    private Scorecard[] scorecards;

    /**
     * <p>
     * Empty constructor.
     * </p>
     */
    public ScorecardGroupBean() {
    }

    /**
     * <p>
     * Return the project category.
     * </p>
     * 
     * @return the project category
     */
    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    /**
     * <p>
     * Set the project category.
     * </p>
     * 
     * @param projectCategory
     *            the project category
     */
    public void setProjectCategory(ProjectCategory projectCategory) {
        this.projectCategory = projectCategory;
    }

    /**
     * <p>
     * Return the list of scorecards.
     * </p>
     * 
     * @return the list of scorecards
     */
    public Scorecard[] getScorecards() {
        return scorecards;
    }

    /**
     * <p>
     * Set the list of scorecards.
     * </p>
     * 
     * @param scorecards
     *            the list of scorecards
     */
    public void setScorecards(Scorecard[] scorecards) {
        this.scorecards = scorecards;
    }
}
