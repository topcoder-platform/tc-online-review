/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.resource;

import java.util.Date;


/**
 * The RboardApplication DTO.
 *
 * @author brain_cn
 * @version 1.0
 */
public class RboardApplication {
    /** Represents the rboard_application table name. */
    public static final String TABLE_NAME = "rboard_application";

    /** Represents user_id field name. */
    public static final String USER_ID_NAME = "user_id";

    /** Represents create_date field name. */
    public static final String CREATE_DATE_NAME = "create_date";

    /** Represents phase_id field name. */
    public static final String PHASE_ID_NAME = "phase_id";
    private Date createDate;
    
    /** TODO Not sure if it's same as login_id of r_user_role table. */
    private int userId;
    
    /** 112: design  113: develop */
    private int phaseId;

    /**
     * Returns the createDate.
     *
     * @return Returns the createDate.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Set the createDate.
     *
     * @param createDate The createDate to set.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    /**
     * Returns the userId.
     *
     * @return Returns the userId.
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Set the userId.
     *
     * @param userId The userId to set.
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}
