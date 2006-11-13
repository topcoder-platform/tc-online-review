/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

/**
 * The scorecard statuses used in the application. They are looked up from configuration.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class ScorecardStatuses {

    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(ScorecardStatuses.class.getName());

    /**
     * Private constructor.
     */
    private ScorecardStatuses() {
    }

    /**
     * <p>
     * Return the scorecard status name by id
     * </p>
     * @param id the id
     * @return the status name
     */
    public static String getScorecardStatusName(long id) {
        return config.getProperty(id + "");
    }
}