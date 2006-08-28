/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.scorecard;

import com.topcoder.onlinereview.migration.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The test of ScorecardSection.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardSectionNew extends BaseDTO {
    /** Represents the scorecard_section table name. */
    public static String TABLE_NAME = "scorecard_section";
    private Collection questions = new ArrayList();
    private int scorecardSectionId;
    private int scorecardGroupId;
    private String name;
    private float weight;
    private int sort;

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
     * Returns the scorecardSectionId.
     *
     * @return Returns the scorecardSectionId.
     */
    public int getScorecardSectionId() {
        return scorecardSectionId;
    }

    /**
     * Set the scorecardSectionId.
     *
     * @param scorecardSectionId The scorecardSectionId to set.
     */
    public void setScorecardSectionId(int scorecardSectionId) {
        this.scorecardSectionId = scorecardSectionId;
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
     * Returns the questions.
     *
     * @return Returns the questions.
     */
    public Collection getQuestions() {
        return questions;
    }

    /**
     * Set the questions.
     *
     * @param questions The questions to set.
     */
    public void setQuestions(Collection questions) {
        this.questions = questions;
    }
}
