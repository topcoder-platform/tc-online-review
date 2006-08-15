/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.scorecard;

import com.topcoder.onlinereview.migration.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The ScorecardGroup data object.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardGroup extends BaseDTO {
    /** Represents the ScorecardGroup table name. */
    public static final String TABLE_NAME = "scorecard_group";
    private Collection sections = new ArrayList();
    private int scorecardGroupId;
    private int scorecardId;
    private String name;
    private float weight;
    private int sort;

    /**
     * Empty constructor.
     */
    public ScorecardGroup() {
    }

    /**
     * Returns the name.
     *
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name.
     *
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the scorecardGroupId.
     *
     * @return Returns the scorecardGroupId.
     */
    public int getScorecardGroupId() {
        return scorecardGroupId;
    }

    /**
     * Set the scorecardGroupId.
     *
     * @param scorecardGroupId The scorecardGroupId to set.
     */
    public void setScorecardGroupId(int scorecardGroupId) {
        this.scorecardGroupId = scorecardGroupId;
    }

    /**
     * Returns the scorecardId.
     *
     * @return Returns the scorecardId.
     */
    public int getScorecardId() {
        return scorecardId;
    }

    /**
     * Set the scorecardId.
     *
     * @param scorecardId The scorecardId to set.
     */
    public void setScorecardId(int scorecardId) {
        this.scorecardId = scorecardId;
    }

    /**
     * Returns the sort.
     *
     * @return Returns the sort.
     */
    public int getSort() {
        return sort;
    }

    /**
     * Set the sort.
     *
     * @param sort The sort to set.
     */
    public void setSort(int sort) {
        this.sort = sort;
    }

    /**
     * Returns the weight.
     *
     * @return Returns the weight.
     */
    public float getWeight() {
        return weight;
    }

    /**
     * Set the weight.
     *
     * @param weight The weight to set.
     */
    public void setWeight(float weight) {
        this.weight = weight;
    }

    /**
     * Returns the sections.
     *
     * @return Returns the sections.
     */
    public Collection getSections() {
        return sections;
    }

    /**
     * Set the sections.
     *
     * @param sections The sections to set.
     */
    public void setSections(Collection sections) {
        this.sections = sections;
    }
}
