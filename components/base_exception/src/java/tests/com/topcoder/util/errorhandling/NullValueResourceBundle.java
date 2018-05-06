/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * Represents the ResourceBundle class which is ONLY used for test.
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */

public class NullValueResourceBundle extends ResourceBundle {

    /**
     * Returns an enumeration of the keys.
     *
     * @return an enumeration of the keys
     */
    public Enumeration getKeys() {
        return null;
    }

    /**
     * Gets an object for the given key from this resource bundle.
     *
     * @param key the given key
     * @return the object for the given key
     */
    public Object handleGetObject(String key) {
        return null;
    }
}
