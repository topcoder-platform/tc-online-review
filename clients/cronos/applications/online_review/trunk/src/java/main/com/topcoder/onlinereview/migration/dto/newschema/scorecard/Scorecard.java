/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.scorecard;

import com.topcoder.onlinereview.migration.dto.BaseDTO;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The Scorecard data object.
 *
 * @author brain_cn
 * @version 1.0
 */
public class Scorecard extends BaseDTO {
    /** Represents the Scorecard table name. */
    public static String TABLE_NAME = "scorecard";

    /** Represents scorecard_id field name. */
    public static final String SCORECARD_ID_NAME = "scorecard_id";

    /** Represents scorecard_status_id field name. */
    public static final String SCORECARD_STATUS_ID_NAME = "scorecard_status_id";

    /** Represents scorecard_type_id field name. */
    public static final String SCORECARD_TYPE_ID_NAME = "scorecard_type_id";

    /** Represents project_category_id field name. */
    public static final String PROJECT_CATEGORY_ID_NAME = "project_category_id";

    /** Represents name field name. */
    public static final String NAME_NAME = "name";

    /** Represents version field name. */
    public static final String VERSION_NAME = "version";

    /** Represents min_score field name. */
    public static final String MIN_SCORE_NAME = "min_score";

    /** Represents max_score field name. */
    public static final String MAX_SCORE_NAME = "max_score";
    private Collection groups = new ArrayList();
    private int scorecardId;
    private int scorecardStatusId;
    private int scorecardTypeId;
    private int projectCategoryId;
    private String name;

    /** The format is major.minor. */
    private String version;
    private float minScore;
    private float maxScore;

    /**
     * Empty constructor.
     */
    public Scorecard() {
    }

    /**
     * Returns the maxScore.
     *
     * @return Returns the maxScore.
     */
    public float getMaxScore() {
        return maxScore;
    }

    /**
     * Set the maxScore.
     *
     * @param maxScore The maxScore to set.
     */
    public void setMaxScore(float maxScore) {
        this.maxScore = maxScore;
    }

    /**
     * Returns the minScore.
     *
     * @return Returns the minScore.
     */
    public float getMinScore() {
        return minScore;
    }

    /**
     * Set the minScore.
     *
     * @param minScore The minScore to set.
     */
    public void setMinScore(float minScore) {
        this.minScore = minScore;
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
     * Returns the projectCategoryId.
     *
     * @return Returns the projectCategoryId.
     */
    public int getProjectCategoryId() {
        return projectCategoryId;
    }

    /**
     * Set the projectCategoryId.
     *
     * @param projectCategoryId The projectCategoryId to set.
     */
    public void setProjectCategoryId(int projectCategoryId) {
        this.projectCategoryId = projectCategoryId;
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
     * Returns the scorecardStatusId.
     *
     * @return Returns the scorecardStatusId.
     */
    public int getScorecardStatusId() {
        return scorecardStatusId;
    }

    /**
     * Set the scorecardStatusId.
     *
     * @param scorecardStatusId The scorecardStatusId to set.
     */
    public void setScorecardStatusId(int scorecardStatusId) {
        this.scorecardStatusId = scorecardStatusId;
    }

    /**
     * Returns the scorecardTypeId.
     *
     * @return Returns the scorecardTypeId.
     */
    public int getScorecardTypeId() {
        return scorecardTypeId;
    }

    /**
     * Set the scorecardTypeId.
     *
     * @param scorecardTypeId The scorecardTypeId to set.
     */
    public void setScorecardTypeId(int scorecardTypeId) {
        this.scorecardTypeId = scorecardTypeId;
    }

    /**
     * Returns the version.
     *
     * @return Returns the version.
     */
    public String getVersion() {
        return version;
    }

    /**
     * Set the version.
     *
     * @param version The version to set.
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Returns the groups.
     *
     * @return Returns the groups.
     */
    public Collection getGroups() {
        return groups;
    }

    /**
     * Set the groups.
     *
     * @param groups The groups to set.
     */
    public void setGroups(Collection groups) {
        this.groups = groups;
    }
}
