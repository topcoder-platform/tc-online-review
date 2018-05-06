/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling.stresstests;

import java.util.ListResourceBundle;


/**
 * This class is a helper class. Used to get an instance for <code>ResourceBundle</code>.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MockResourceBundle extends ListResourceBundle {
    /** Create the static <code>Object</code> array. */
    static final Object[][] CONTENTS = {
            // localize this
        {"name", "xixi"},
        {"app-module-error", "Item missing" },
        {"key", new Object()},
        // end of material to locallize
    };

    /**
     * <p>
     * Implements <code>java.util.ListResourceBundle.getContents</code>.
     * </p>
     *
     * @return CONTENTS
     */
    public Object[][] getContents() {
        return CONTENTS;
    }
}
