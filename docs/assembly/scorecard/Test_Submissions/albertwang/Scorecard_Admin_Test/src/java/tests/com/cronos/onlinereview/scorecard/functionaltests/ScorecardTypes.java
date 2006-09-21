/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

/**
 * The scorecard types used in the application. They are looked up from configuration.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class ScorecardTypes {

    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(ScorecardTypes.class.getName());

    /**
     * Private constructor.
     */
    private ScorecardTypes() {
    }

    /**
     * <p>
     * Return the scorecard type name by id.
     * </p>
     * @param id id
     * @return the type name
     */
    public static String getScorecardTypeName(long id) {
        return config.getProperty(id + "");
    }
}