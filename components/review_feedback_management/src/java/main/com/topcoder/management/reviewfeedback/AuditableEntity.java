/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.Date;

/**
 * <p>
 * This is a base class for auditable entities. It's a simply POJO, containing several properties (each with public
 * getter and setter with no validation) and default empty constructor.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is mutable and not thread-safe.
 * </p>
 *
 * @author gevak, sparemax
 * @version 2.0
 * @since 2.0
 */
public abstract class AuditableEntity extends IdentifiableEntity {
    /**
     * User who created this entity. It's fully mutable, has public getter and setter. Can be any value.
     */
    private String createUser;

    /**
     * Date and time when this entity was created. It's fully mutable, has public getter and setter. Can be any value.
     */
    private Date createDate;

    /**
     * User who modified this entity last time. It's fully mutable, has public getter and setter. Can be any value.
     */
    private String modifyUser;

    /**
     * Date and time when this entity was modified last time. It's fully mutable, has public getter and setter. Can be
     * any value.
     */
    private Date modifyDate;

    /**
     * Creates an instance of AuditableEntity.
     */
    protected AuditableEntity() {
        // Empty
    }

    /**
     * Gets the user who created this entity.
     *
     * @return the user who created this entity.
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * Sets the user who created this entity.
     *
     * @param createUser
     *            the user who created this entity.
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    /**
     * Gets the date and time when this entity was created.
     *
     * @return the date and time when this entity was created.
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * Sets the date and time when this entity was created.
     *
     * @param createDate
     *            the date and time when this entity was created.
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets the user who modified this entity last time.
     *
     * @return the user who modified this entity last time.
     */
    public String getModifyUser() {
        return modifyUser;
    }

    /**
     * Sets the user who modified this entity last time.
     *
     * @param modifyUser
     *            the user who modified this entity last time.
     */
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }

    /**
     * Gets the date and time when this entity was modified last time.
     *
     * @return the date and time when this entity was modified last time.
     */
    public Date getModifyDate() {
        return modifyDate;
    }

    /**
     * Sets the date and time when this entity was modified last time.
     *
     * @param modifyDate
     *            the date and time when this entity was modified last time.
     */
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
