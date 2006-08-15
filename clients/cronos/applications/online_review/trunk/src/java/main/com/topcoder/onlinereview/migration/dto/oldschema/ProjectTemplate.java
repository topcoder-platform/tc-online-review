/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema;

/**
 * The test of ProjectTemplate.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ProjectTemplate {
    /** Represents the project_template table name. */
    public static final String TABLE_NAME = "project_template";

    /** Represents template_id field name. */
    public static final String TEMPLATE_ID_NAME = "template_id";

    /** Used in phase pacakge for scorecard to use during the phase. */
    private int templateId;

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
}
