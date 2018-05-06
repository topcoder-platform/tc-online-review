/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.util.ListResourceBundle;


/**
 * <p>
 * The mock implementation of <code>ResourceBundle</code> for testing.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public class MockResourcesBundle extends ListResourceBundle {
    /**
     * <p>
     * Represents the contents of the resources.
     * </p>
     */
    static final Object[][] CONTENTS = {
            // LOCALIZE THIS
        {"OkKey", "OK"},
        {"CancelKey", "Cancel"},
        // END OF MATERIAL TO LOCALIZE
    };

    /**
     * <p>
     * the contents of the resources bundle.
     * </p>
     *
     * @return the resources bundle contents
     */
    public Object[][] getContents() {
        return CONTENTS;
    }
}
