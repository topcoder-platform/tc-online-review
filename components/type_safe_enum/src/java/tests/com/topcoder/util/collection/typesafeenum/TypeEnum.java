/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.collection.typesafeenum;

/**
 * Represents the Enum class which is ONLY used in demo.
 *
 * @author TCSDEVELOPER
 * @version 1.1
 */
public abstract class TypeEnum extends Enum {

    /**
     * NUMBER enum for TypeEnum. Introduced in version 1.1, enum constant with constant-specific class body.
     */
    public static final TypeEnum NUMBER = new TypeEnum("Number") {
        public boolean checkValue(Object val) {
            return val instanceof Number;
        }
    };

    /**
     * STRING enum for TypeEnum. Introduced in version 1.1, enum constant with constant-specific class body.
     */
    public static final TypeEnum STRING = new TypeEnum("String") {
        public boolean checkValue(Object val) {
            return val instanceof String;
        }
    };

    /**
     * Represents a custom property for enum.
     */
    private final String name;

    /**
     * Creates a new <code>TypeEnum</code> enum.
     *
     * @param name
     *            the value for custom property of this enum
     */
    private TypeEnum(String name) {
        // Introduced in version 1.1, this is required to support internal subclassing.
        super(TypeEnum.class);
        if (name == null) {
            throw new IllegalArgumentException("name shouldn't be null.");
        }
        this.name = name;
    }

    /**
     * Checks the value.
     *
     * @param val
     *            the value to check
     * @return whether the value is passed the check
     */
    public abstract boolean checkValue(Object val);

    /**
     * Gets the custom property for enum.
     *
     * @return the custom property for enum
     */
    public String getName() {
        return this.name;
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
