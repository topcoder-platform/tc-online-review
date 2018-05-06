/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * A Helper class provides the argument validation.
 * </p>
 *
 * @author telly12
 * @version 1.0
 */
final class Helper {
    /**
     * Private constructor to prevent this class to be instantiated.
     */
    private Helper() {
        // empty
    }

    /**
     * Checks whether the given Object is null.
     *
     * @param arg the argument to be checked
     * @param name the name of the argument
     *
     * @throws IllegalArgumentException if the given Object is null
     */
    static void checkNull(Object arg, String name) {
        if (arg == null) {
            throw new IllegalArgumentException("The parameter \'" + name + "\' should not be null.");
        }
    }

    /**
     * Checks whether the given <code>String</code> is null or empty.
     *
     * @param str the <code>String</code> to be checked
     * @param name the name of the String
     *
     * @throws IllegalArgumentException if the argument is the given string is null or empty
     */
    static void checkString(String str, String name) {
        checkNull(str, name);

        if (str.trim().length() == 0) {
            throw new IllegalArgumentException("The parameter \'" + name + "\' should not be empty");
        }
    }
}
