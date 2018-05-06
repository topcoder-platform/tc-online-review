/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ListResourceBundle;

/**
 * Represents the ResourceBundle class which is ONLY used for test.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class EmptyKeyResourceBundle extends ListResourceBundle {
    /**
     * Represents the resource content.
     *
     * @see ListResourceBundle
     */
    private static final Object[][] CONTENTS = {{"", "value"}};

    /**
     * This method is the implementation of
     * <code>ListResourceBundle.getContents()</code>.
     *
     * @return the resource content
     * @see ListResourceBundle
     */
    public Object[][] getContents() {
        return CONTENTS;
    }

}
