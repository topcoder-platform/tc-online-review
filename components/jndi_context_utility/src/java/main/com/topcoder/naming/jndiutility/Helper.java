/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 *
 * TCS Project JNDI Context Utility 2.0
 *
 * @ Helper.java
 */
package com.topcoder.naming.jndiutility;

/**
 * Helper class of this component. Checks the arguments of public methods in this component.
 *
 * @author Charizard
 * @version 2.0
 */
public final class Helper {
    /**
     * Private empty constructor.
     */
    private Helper() {
    }

    /**
     * Checks a given object, throw <code>IllegalArgumentException</code> if it is null.
     *
     * @param object the object to be checked
     * @param name name of the object
     *
     * @throws IllegalArgumentException if <code>object</code> is <code>null</code>
     */
    public static void checkObject(Object object, String name) {
        if (object == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    /**
     * Checks a given string, throw <code>IllegalArgumentException</code> if it is null or empty.
     *
     * @param string the string to be checked
     * @param name name of the string
     *
     * @throws IllegalArgumentException if <code>string</code> is <code>null</code> or empty
     */
    public static void checkString(String string, String name) {
        checkObject(string, name);

        if (string.trim().length() == 0) {
            throw new IllegalArgumentException(name + " is empty");
        }
    }
}
