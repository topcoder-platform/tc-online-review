/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.review.scorecalculator;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * Contains utility testing methods.
 *
 * @author      UFP2161
 * @copyright   Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 * @version     1.0
 */
public final class TestUtil {

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Constructors

    /**
     * Create a new TestUtil.
     */
    private TestUtil() {
        // Do nothing.
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Static Methods

    /**
     * Clear the given namespace from the Configuration Manager component (if the namespace was loaded).
     *
     * @param   namespace
     *          The namespace to remove.
     */
    public static void clearNamespace(String namespace) {
        if (ConfigManager.getInstance().existsNamespace(namespace)) {
            try {
                ConfigManager.getInstance().removeNamespace(namespace);
            } catch (UnknownNamespaceException ex) {
                // This should never happen; so we ignore it.
            }
        }
    }
}
