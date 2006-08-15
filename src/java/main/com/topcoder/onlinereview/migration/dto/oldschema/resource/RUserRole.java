/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration.dto.oldschema.resource;

/**
 * The test of RUserRole.
 *
 * @author brain_cn
 * @version 1.0
 */
public class RUserRole {
    /** Represents the r_user_role table name. */
    public static final String TABLE_NAME = "r_user_role";

    /** Represents r_user_role_id field name. */
    public static final String R_USER_ROLE_ID_NAME = "r_user_role_id";

    /** Represents r_role_id field name. */
    public static final String R_ROLE_ID_NAME = "r_role_id";

    /** Represents r_resp_id field name. */
    public static final String R_RESP_ID_NAME = "r_resp_id";

    /** Represents project_id field name. */
    public static final String PROJECT_ID_NAME = "project_id";

    /** Represents login_id field name. */
    public static final String LOGIN_ID_NAME = "login_id";
    private int rUserRoleId;
    private int rRoleId;
    private int rRespId;
    private int projectId;
    private int loginId;
    private PaymentInfo paymentInfo;

    /**
     * Returns the loginId.
     *
     * @return Returns the loginId.
     */
    public int getLoginId() {
        return loginId;
    }

    /**
     * Set the loginId.
     *
     * @param loginId The loginId to set.
     */
    public void setLoginId(int loginId) {
        this.loginId = loginId;
    }

    /**
     * Returns the projectId.
     *
     * @return Returns the projectId.
     */
    public int getProjectId() {
        return projectId;
    }

    /**
     * Set the projectId.
     *
     * @param projectId The projectId to set.
     */
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    /**
     * Returns the respId.
     *
     * @return Returns the rRespId.
     */
    public int getRRespId() {
        return rRespId;
    }

    /**
     * Set the respId.
     *
     * @param respId The rRespId to set.
     */
    public void setRRespId(int respId) {
        rRespId = respId;
    }

    /**
     * Returns the roleId.
     *
     * @return Returns the rRoleId.
     */
    public int getRRoleId() {
        return rRoleId;
    }

    /**
     * Set the roleId.
     *
     * @param roleId The rRoleId to set.
     */
    public void setRRoleId(int roleId) {
        rRoleId = roleId;
    }

    /**
     * Returns the userRoleId.
     *
     * @return Returns the rUserRoleId.
     */
    public int getRUserRoleId() {
        return rUserRoleId;
    }

    /**
     * Set the userRoleId.
     *
     * @param userRoleId The rUserRoleId to set.
     */
    public void setRUserRoleId(int userRoleId) {
        rUserRoleId = userRoleId;
    }

    /**
     * DOCUMENT ME!
     *
     * @return Returns the paymentInfo.
     */
    public PaymentInfo getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paymentInfo The paymentInfo to set.
     */
    public void setPaymentInfo(PaymentInfo paymentInfo) {
        this.paymentInfo = paymentInfo;
    }
}
