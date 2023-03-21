/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.onlinereview.component.grpcclient.GrpcHelper;
import com.topcoder.onlinereview.component.grpcclient.actionshelper.ActionsHelperServiceRpc;

/**
 * <code>User Preference EJB</code>.</p>
 *
 * <p><b>Thread safety:</b> This class is thread-safe.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UserPreference {
    /**
     * Get user preference value.
     * 
     * @param userId
     *            the user id
     * @param preferenceId
     *            the preference id
     * @param jdbcTemplate
     *            the data source
     * @return the value
     */
    public String getValue(long userId, int preferenceId) {
        ActionsHelperServiceRpc actionsHelperServiceRpc = GrpcHelper.getActionsHelperServiceRpc();
        String value = actionsHelperServiceRpc.getUserPreferenceValue(userId, preferenceId);
        if (value == null) {
            return "false";
        }
        return value;
    }
}
