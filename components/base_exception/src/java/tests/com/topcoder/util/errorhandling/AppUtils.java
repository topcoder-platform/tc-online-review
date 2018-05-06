/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ResourceBundle;

/**
 * <p>
 * <code>AppUtils</code> is an utility class used to retrieve information from
 * application.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class AppUtils {
    /**
     * Private constructor to prevent this class being instantiated.
     */
    private AppUtils() {

    }

    /**
     * Get the code of the application.
     *
     * @return the application code
     */
    public static String getAppCode() {
        return "appCode";
    }

    /**
     * Get the <code>ResourceBundle</code> instance used for test.
     *
     * @return the loaded resource bundle
     */
    public static ResourceBundle getResourceBundle() {
        return ResourceBundle.getBundle("com.topcoder.util.errorhandling.MyResourceBundle");
    }
}
