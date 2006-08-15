/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.project;

/**
 * The CompCatalog DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class CompCatalog {
    /** Represents the comp_catalog table name. */
    public static final String TABLE_NAME = "comp_catalog";

    /** Represents component_id field name. */
    public static final String COMPONENT_ID_NAME = "component_id";

    /** Represents root_category_id field name. */
    public static final String ROOT_CATEGORY_ID_NAME = "root_category_id";

    /** Represents component_name field name. */
    public static final String COMPONENT_NAME_NAME = "component_name";
    private int rootCategoryId;
    private String componentName;

    /**
     * Returns the componentName.
     *
     * @return Returns the componentName.
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * Set the componentName.
     *
     * @param componentName The componentName to set.
     */
    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    /**
     * Returns the rootCategoryId.
     *
     * @return Returns the rootCategoryId.
     */
    public int getRootCategoryId() {
        return rootCategoryId;
    }

    /**
     * Set the rootCategoryId.
     *
     * @param rootCategoryId The rootCategoryId to set.
     */
    public void setRootCategoryId(int rootCategoryId) {
        this.rootCategoryId = rootCategoryId;
    }
}
