/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ejblibrary;

import org.springframework.jdbc.core.JdbcTemplate;

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
    public String getValue(long userId, int preferenceId, JdbcTemplate jdbcTemplate) {
        String query = "select value from  user_preference where user_id = ? and preference_id = ? ";
        List<Map<String, Object>> rs = executeSqlWithParam(jdbcTemplate, query, newArrayList(userId, preferenceId));
        if (rs.isEmpty()) {
            return "false";
        }
        return getString(rs.get(0), "value");
    }
}
