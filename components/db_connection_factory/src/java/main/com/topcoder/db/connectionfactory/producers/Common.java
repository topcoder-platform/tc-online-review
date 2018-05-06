/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.producers;

/**
 * This class is used to avoid the duplicated code in
 * checking the parameters of method. It is just used by
 * producers package.
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
final class Common {
    /**
     * The private constructor is used to
     * avoid being initialized.
     */
    private Common() {
    }

    /**
     * It is used to check the given String type parameter is null or empty.
     * If it is null, NullPointerException will be thrown; if it is empty,
     * IllegalArgumentException will  be thrown.
     *
     * @param paramName the name of parameter
     * @param paramValue the value of parameter
     * @throws NullPointerException if any given paramValue is null.
     * @throws IllegalArgumentException if the given paramValue
     *         is empty string.
     */
    protected static void checkNullOrEmpty(String paramName, String paramValue) {
        if (paramValue == null) {
            throw new NullPointerException("The " + paramName + " should not be null.");
        }
        if (paramValue.trim().length() == 0) {
            throw new IllegalArgumentException(
                    "The " + paramName + " should not be empty.");
        }
    }

    /**
     * It is used to check the given parameter is null or not.
     * If it is null, NullPointerException will be thrown.
     *
     * @param paramName the name of parameter
     * @param paramValue the value of parameter
     *
     * @throws NullPointerException if any given paramValue is null.
     */
    protected static void checkNull(String paramName, Object paramValue) {
        if (paramValue == null) {
            throw new NullPointerException("The " + paramName + " should not be null.");
        }
    }
}
