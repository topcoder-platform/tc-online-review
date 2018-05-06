/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.io.Serializable;

/**
 * <p>
 * This is a simple enumeration of comparison directions that we would see when comparing entities such as numbers.
 * </p>
 *
 * <p>
 * All the possible directions are enumerated here such as equal, greater-than-or-equal, less-than-or-equal,
 * greater-than, and less-than.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>it is thread-safe since it is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
final class CompareDirection implements Serializable {
    // static enums for comparison directions.

    /**
     * <p>
     * Greater than comparison direction.
     * </p>
     */
    public static final CompareDirection GREATER = new CompareDirection("greater than");

    /**
     * <p>
     * Greater than or equal comparison direction.
     * </p>
     */
    public static final CompareDirection GREATER_OR_EQUAL = new CompareDirection("greater than or equal to");

    /**
     * <p>
     * Less than comparison direction.
     * </p>
     */
    public static final CompareDirection LESS = new CompareDirection("less than");

    /**
     * <p>
     * Less than or equal comparison direction.
     * </p>
     */
    public static final CompareDirection LESS_OR_EQUAL = new CompareDirection("less than or equal to");

    /**
     * <p>
     * Equal comparison direction.
     * </p>
     *
     * @since 1.1
     */
    public static final CompareDirection EQUAL = new CompareDirection("equal to");

    /**
     * <p>
     * <code>String</code> to be returned by <code>toString()</code>. This is initialized in the constructor and is
     * immutable after that. It will simply hold the code value of the enumeration item.
     * </p>
     */
    private final String str;

    /**
     * <p>
     * Creates a new <code>CompareDirection</code>. This takes a <code>String</code> that will be returned by
     * <code>toString()</code>.
     * </p>
     *
     * @param str <code>String</code> that will be returned by <code>toString()</code>.
     */
    private CompareDirection(String str) {
        this.str = str;
    }

    /**
     * <p>
     * Returns the <code>String</code> that was passed to this <code>CompareDirection</code>'s constructor.
     * </p>
     *
     * @return the <code>String</code> passed to the constructor.
     */
    public String toString() {
        return str;
    }
}
