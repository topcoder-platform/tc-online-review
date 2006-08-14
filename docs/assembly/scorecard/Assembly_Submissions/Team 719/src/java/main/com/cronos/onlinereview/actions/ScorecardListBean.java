/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

/**
 * <p>
 * <code>ScorecardListBean</code> holds a list of scorecards information, the
 * scorecards are grouped by project category.
 * </p>
 * <p>
 * It is primarily used in "list scorecards" action.
 * </p>
 * 
 * @version 1.0
 * @author TCSDEVELOPER
 */
public class ScorecardListBean {
    /**
     * A list of grouped scorecards which share the same project category.
     */
    private ScorecardGroupBean[] scorecardGroups;

    /**
     * The name of project type of the list of scorecards.
     */
    private String projectTypeName;

    /**
     * <p>
     * Empty constructor.
     * </p>
     */
    public ScorecardListBean() {
    }

    /**
     * <p>
     * Return the project type name.
     * </p>
     * 
     * @return the project type name
     */
    public String getProjectTypeName() {
        return projectTypeName;
    }

    /**
     * <p>
     * Set the project type name.
     * </p>
     * 
     * @param projectTypeName
     *            the project type name.
     */
    public void setProjectTypeName(String projectTypeName) {
        this.projectTypeName = projectTypeName;
    }

    /**
     * <p>
     * Set the scorecard groups.
     * </p>
     * 
     * @return the scorecard groups.
     */
    public ScorecardGroupBean[] getScorecardGroups() {
        return scorecardGroups;
    }

    /**
     * <p>
     * Return a group of scorecards which share the same project category.
     * </p>
     * 
     * @param scorecardGroups
     *            a group of scorecards which share the same project category
     */
    public void setScorecardGroups(ScorecardGroupBean[] scorecardGroups) {
        this.scorecardGroups = scorecardGroups;
    }
}
