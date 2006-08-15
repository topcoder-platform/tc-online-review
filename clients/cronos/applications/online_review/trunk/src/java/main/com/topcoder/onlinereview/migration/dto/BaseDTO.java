/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto;

import java.util.Date;

/**
 * The BaseDTO data object.
 *
 * @author brain_cn
 * @version 1.0
 */
public class BaseDTO {
    /**
     * Represents create_user field name.
     */
    public static final String CREATE_USER_NAME = "create_user";

    /**
     * Represents create_date field name.
     */
    public static final String CREATE_DATE_NAME = "create_date";

    /**
     * Represents modify_user field name.
     */
    public static final String MODIFY_USER_NAME = "modify_user";

    /**
     * Represents modify_date field name.
     */
    public static final String MODIFY_DATE_NAME = "modify_date";
    private String createUser;
    private Date createDate;
    private String modifyUser;
    private Date modifyDate;
    
    /**
     * Empty constructor.
     *
     */
    public BaseDTO() {    	
    }

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
     * Returns the createUser.
     *
     * @return Returns the createUser.
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * Set the createUser.
     *
     * @param createUser The createUser to set.
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * Returns the modifyDate.
     *
     * @return Returns the modifyDate.
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * Set the modifyDate.
     *
     * @param modifyDate The modifyDate to set.
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    /**
     * Returns the modifyUser.
     *
     * @return Returns the modifyUser.
     */
    public String getModifyUser() {
        return modifyUser;
    }

    /**
     * Set the modifyUser.
     *
     * @param modifyUser The modifyUser to set.
     */
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

}
