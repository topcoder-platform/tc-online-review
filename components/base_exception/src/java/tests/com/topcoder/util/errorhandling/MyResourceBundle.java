/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.ListResourceBundle;

/**
 * <code>MyResourceBundle</code> extends <code>ListResourceBundle</code>.
 * It is used to provide resource.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class MyResourceBundle extends ListResourceBundle {
    /**
     * Represents the resource content.
     *
     * @see ListResourceBundle
     */
    private static final Object[][] CONTENTS = {
        {"key", "value"}, {"doubleKey", new Double(0.0)}, {"emptyKey", ""}, {"trimemptyKey", "  "},
        {"sameKey", "sameKey"}, {"app-module-error", "value1"}, {"badname", "bad"}, {"nospace", "space space"},
        {"appCode-SmartEx-e617", "message"}};

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
