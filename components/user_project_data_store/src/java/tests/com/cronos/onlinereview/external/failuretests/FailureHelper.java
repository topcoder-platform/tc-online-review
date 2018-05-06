/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.failuretests;

import com.topcoder.util.config.ConfigManager;

import java.util.Iterator;

/**
 * Help class for testintg.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class FailureHelper {
    /**
     * <p/>
     * Creates a new instance of <code>FailureHelper</code> class. The private
     * constructor prevents the creation of a new instance.
     * </p>
     */
    private FailureHelper() {
    }

    /**
     * <p/>
     * Clears the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }
}
