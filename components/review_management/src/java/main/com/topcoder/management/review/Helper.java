/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review;


/**
 * <p>
 * Package private helper class to simplify the argument checking and string parsing.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Moved checkString/createObject/getProperty/getValue methods to DefaultReviewManager class.</li>
 * <li>Changed visibility of helper methods to package private.</li>
 * <li>Added thread safety information.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author icyriver, saarixx, sparemax
 * @version 1.2
 */
final class Helper {
    /**
     * <p>
     * Private constructor to prevent instantiation of this class.
     * </p>
     */
    private Helper() {
        // Empty
    }

    /**
     * <p>
     * Check the given <code>obj</code> for null.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Changed to package private.</li>
     * </ol>
     * </p>
     *
     * @param obj
     *            the object to check.
     * @param paramName
     *            the paramName of the argument.
     *
     * @throws IllegalArgumentException
     *             if obj is null.
     */
    static void checkNull(Object obj, String paramName) {
        if (obj == null) {
            throw new IllegalArgumentException("Parameter argument: '" + paramName + "' can not be null!");
        }
    }
}
