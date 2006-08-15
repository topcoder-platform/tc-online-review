/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.newschema.resource;

import com.topcoder.onlinereview.migration.dto.BaseDTO;


/**
 * The test of ResourceInfo.
 *
 * @author brain_cn
 * @version 1.0
 */
public class ResourceInfo extends BaseDTO {
    /** Represents the resource_info table name. */
    public static final String TABLE_NAME = "resource_info";
    public static final int EXTERNAL_REFERENCE_ID = 1;
    public static final int HANDLE = 2;
    public static final int EMAIL = 3;
    public static final int RATING = 4;
    public static final int RELIABILITY = 5;
    public static final int REGISTRATION_DATE = 6;
    public static final int PAYMENT = 7;
    public static final int PAYMENT_STATUS = 8;
    public static final int SCREENING_SCORE = 9;
    public static final int INITIAL_SCORE = 10;
    public static final int FINAL_SCORE = 11;
    public static final int PLACEMENT = 12;
    private int resourceId;
    private int resourceInfoTypeId;
    private String value;

    /**
     * Returns the resourceId.
     *
     * @return Returns the resourceId.
     */
    public int getResourceId() {
        return resourceId;
    }

    /**
     * Set the resourceId.
     *
     * @param resourceId The resourceId to set.
     */
    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * Returns the resourceInfoTypeId.
     *
     * @return Returns the resourceInfoTypeId.
     */
    public int getResourceInfoTypeId() {
        return resourceInfoTypeId;
    }

    /**
     * Set the resourceInfoTypeId.
     *
     * @param resourceInfoTypeId The resourceInfoTypeId to set.
     */
    public void setResourceInfoTypeId(int resourceInfoTypeId) {
        this.resourceInfoTypeId = resourceInfoTypeId;
    }

    /**
     * Returns the value.
     *
     * @return Returns the value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value.
     *
     * @param value The value to set.
     */
    public void setValue(String value) {
        this.value = value;
    }
}
