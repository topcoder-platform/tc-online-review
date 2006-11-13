/*
 * Copyright (C) 2006 TopCoder, Inc. All Rights Reserved.
 */
package com.cronos.onlinereview.scorecard.functionaltests;

/**
 * The project categories used in the application. They are looked up from configuration.
 *
 * @author TCSTESTER
 * @version 1.0
 */
public class ProjectCategories {

    /**
     * The configuration interface.
     */
    private static Configuration config = new Configuration(ProjectCategories.class.getName());

    /**
     * Private constructor.
     */
    private ProjectCategories() {
    }

    /**
     * <p>
     * Return the project category name by id.
     * </p>
     * @param id the project category id
     * @return the category name
     */
    public static String getProjectCategory(long id) {
        return config.getProperty(id + "");
    }
    /**
     * <p>
     * Return the project type name by category id.
     * </p>
     * @param categoryId category id
     * @return the project type name
     */
    public static String getProjectType(long categoryId) {
    	return config.getProperty(categoryId + ".project_type");
    }
}