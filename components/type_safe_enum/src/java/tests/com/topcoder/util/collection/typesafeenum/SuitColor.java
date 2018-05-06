/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * <p>
 * Example class for Typesafe Enum pattern implementation.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class SuitColor extends Enum {
    /**
     * RED element for SuitColor enum.
     */
    public static final SuitColor RED = new SuitColor("red");

    /**
     * BLACK element for SuitColor enum.
     */
    public static final SuitColor BLACK = new SuitColor("black");

    /**
     * Represents the color of the SuitColor.
     */
    private final String color;

    /**
     * Creates a new <code>SuitColor</code> enum.
     *
     * @param color
     *            the color of the SuitColor
     */
    private SuitColor(String color) {
        this.color = color;
    }

    /**
     * Gets the color of the SuitColor.
     *
     * @return the color of the SuitColor
     */
    public String getColor() {
        return color;
    }

    /**
     * Gets the string representation of the suit.
     *
     * @return the string representation of the suit
     */
    public String toString() {
        return getColor();
    }
}