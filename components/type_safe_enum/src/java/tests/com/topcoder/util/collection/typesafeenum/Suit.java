/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * <p>
 * Example class for Typesafe Enum pattern implementation.
 * </p>
 * <p>
 * This class should be compileable and public methods for it will be tested in <code>EnumUnitTests</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public class Suit extends Enum {
    /**
     * CLUBS element for Suit enum.
     */
    public static final Suit CLUBS = new Suit("Clubs", SuitColor.BLACK);

    /**
     * DIAMONDS element for Suit enum.
     */
    public static final Suit DIAMONDS = new Suit("Diamonds", SuitColor.RED);

    /**
     * HEARTS element for Suit enum.
     */
    public static final Suit HEARTS = new Suit("Hearts", SuitColor.RED);

    /**
     * SPADES element for Suit enum.
     */
    public static final Suit SPADES = new Suit("Spades", SuitColor.BLACK);

    /**
     * Represents the name of the suit.
     */
    private final String name;

    /**
     * Represents the color of the suit.
     */
    private final SuitColor color;

    /**
     * Creates a new <code>Suit</code> enum.
     *
     * @param name
     *            the name of the suit.
     * @param color
     *            the color of the suit.
     */
    private Suit(String name, SuitColor color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Gets the name of the suit.
     *
     * @return the name of the suit
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the color of the suit.
     *
     * @return the color of the suit
     */
    public SuitColor getColor() {
        return color;
    }

    /**
     * Gets the string representation of the suit.
     *
     * @return the string representation of the suit
     */
    public String toString() {
        return getName();
    }
}
