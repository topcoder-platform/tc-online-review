/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import com.topcoder.web.common.RowNotFoundException;
import com.topcoder.web.ejb.user.UserPreferenceBean;
import org.springframework.jdbc.core.JdbcTemplate;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.topcoder.onlinereview.component.util.CommonUtils.executeSqlWithParam;
import static com.topcoder.onlinereview.component.util.CommonUtils.getString;

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
     * <p>
     * A <code>UserPreferenceBean</code> which is delegated the processing of
     * the calls to methods of this class.
     * </p>
     */
    private final UserPreferenceBean bean;

    /**
     * <p>
     * Constructs new <code>UserPreferenceBean</code> instance.
     * </p>
     */
    public UserPreference() {
        this.bean = new UserPreferenceBean();
    }

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
    public String getValue(long userId, int preferenceId, JdbcTemplate jdbcTemplate)
            throws RowNotFoundException, RemoteException {
        String query = "select value from  user_preference where user_id = ? and preference_id = ? ";
        List<Map<String, Object>> rs = executeSqlWithParam(jdbcTemplate, query, newArrayList(userId, preferenceId));
        if (rs.isEmpty()) {
            throw new RowNotFoundException("no row found for " + query);
        }
        return getString(rs.get(0), "value");
    }
}
