/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.scorecard;

import java.util.ArrayList;
import java.util.Collection;


/**
 * The ScorecardTemplate data object.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ScorecardTemplate {
    /** Represents the scorecard_template table name. */
    public static final String TABLE_NAME = "scorecard_template";

    /** Represents template_id field name. */
    public static final String TEMPLATE_ID_NAME = "template_id";

    /** Represents status_id field name. */
    public static final String STATUS_ID_NAME = "status_id";

    /** Represents scorecard_type field name. */
    public static final String SCORECARD_TYPE_NAME = "scorecard_type";

    /** Represents project_type field name. */
    public static final String PROJECT_TYPE_NAME = "project_type";

    /** Represents templateName field name. */
    public static final String TEMPLATE_NAME_NAME = "template_name";
    private Collection groups = new ArrayList();
    private int templateId;
    private int statusId;
    private int scorecardType;
    private int projectType;
    private String templateName;

    /**
     * Empty constructor.
     */
    public ScorecardTemplate() {
    }

    /**
     * Returns the projectType.
     *
     * @return Returns the projectType.
     */
    public int getProjectType() {
        return projectType;
    }

    /**
     * Set the projectType.
     *
     * @param projectType The projectType to set.
     */
    public void setProjectType(int projectType) {
        this.projectType = projectType;
    }

    /**
     * Returns the scorecardType.
     *
     * @return Returns the scorecardType.
     */
    public int getScorecardType() {
        return scorecardType;
    }

    /**
     * Set the scorecardType.
     *
     * @param scorecardType The scorecardType to set.
     */
    public void setScorecardType(int scorecardType) {
        this.scorecardType = scorecardType;
    }

    /**
     * Returns the statusId.
     *
     * @return Returns the statusId.
     */
    public int getStatusId() {
        return statusId;
    }

    /**
     * Set the statusId.
     *
     * @param statusId The statusId to set.
     */
    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    /**
     * Returns the templateId.
     *
     * @return Returns the templateId.
     */
    public int getTemplateId() {
        return templateId;
    }

    /**
     * Set the templateId.
     *
     * @param templateId The templateId to set.
     */
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    /**
     * Returns the templateName.
     *
     * @return Returns the templateName.
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Set the templateName.
     *
     * @param templateName The templateName to set.
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
     * Set groups.
     *
     * @param groups The groups to set.
     */
    public void setGroups(Collection groups) {
        this.groups = groups;
    }
}
