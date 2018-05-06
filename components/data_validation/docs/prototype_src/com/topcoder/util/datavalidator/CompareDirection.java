/*
 * TCS Data Validation
 *
 * CompareDirection.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple enumeration of comparison directions that we would see when
 * comparing entities such as numbers. All the possible directions are
 * enumerated here such as equal, greater-than-or-equal, less-than-or-equal,
 * greater-than, and less-than
 * As an enumeration it is thread-safe since it is immutable.
 * @version 1.1
 */
final class CompareDirection {

    // static enums for comparison directions.

    /**
     * Greater than comparison direction
     */
    public static final CompareDirection
            GREATER = new CompareDirection("greater than");
    /**
     * Greater than or equal comparison direction
     */
    public static final CompareDirection
            GREATER_OR_EQUAL = new CompareDirection("greater than or equal to");

    /**
     * Less than comparison direction
     */
    public static final CompareDirection
            LESS = new CompareDirection("less than");

    /**
     * Less than or equal comparison direction
     */
   public static final CompareDirection
            LESS_OR_EQUAL = new CompareDirection("less than or equal to");
    /**
     * Equal comparison direction
     */
    public static final CompareDirection
            EQUAL = new CompareDirection("equal to");

    /** <code>String</code> to be returned by <code>toString()</code>.
     * This is initialized in the constructor and is immutable after that. It
     * will simply hold the code value of the enumeration item.
     */
    private final String str;

    /**
     * Creates a new <code>CompareDirection</code>. This takes a
     * <code>String</code> that will be returned by <code>toString()</code>.
     *
     * @param   str <code>String</code> that will be returned by
     *      <code>toString()</code>.
     */
    private CompareDirection(String str) {
        this.str = str;
    }

    /**
     * Returns the <code>String</code> that was passed to this
     * <code>CompareDirection</code>'s constructor.
     *
     * @return  the <code>String</code> passed to the constructor.
     */
    public String toString() {
        return str;
    }

}
