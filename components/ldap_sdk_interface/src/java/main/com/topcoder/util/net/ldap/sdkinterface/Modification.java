/*
 * TCS LDAP SDK Interface 1.0
 *
 * Modification.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * Represents a modification in an entry.</p>
 * <p>A modification consist of a type of modification, for whom there are constants defined in this class, the name of
 * the attribute to be modified and optionally the values for the attribute</p>
 * <p>This class is package because the user of the package doesn't need it, it's only an intermediate class to store
 * one modification</p>
 * <p>The type, attributeName and values can only be set in the constructor. At the moment, it has no use to change
 * them after, because only Update class will build modifications. In the case this needs to be changed, this class can
 * be inherited and the necessary methods implemented. This is why the attributes are declared protected. </p>
 * <p>The different types of modification are detailed in the constants
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class Modification {

    /**
     * Add values to an attribute or add a new attribute , depending on wheter the attribute name exist or not
     */
    public static final int ADD = 1;

    /**
     * Delete one or more values from an attribute
     */
    public static final int DELETE_VALUE = 2;

    /**
     * Delete an attribute.
     */
    public static final int DELETE_ATTRIBUTE = 3;

    /**
     * Replace the attribute values with the specified new values
     */
    public static final int REPLACE = 4;

    /**
     * Type of modification, as defined by the constants in this class. </p>
     * <p>Its initial value is always set on the constructor, and it is not possible to modify this value later
     */
    protected int type = 0;

    /**
     * The name of the attribute to be modified.</p>
     * <p>Its initial value is always set on the constructor, and it is not possible to modify this value later.
     */
    protected String attributeName = null;

    /**
     * An optional <code>Values</code> object holding the values of attribute affected by this modification.
     */
    protected Values values = null;

    /**
     * Create a Modification without values</p>
     * <p>This constructor should be used (at this moment) only for DELETE_ATTRIBUTE types, beacause is the only that
     * doesn't require values.</p>
     *
     * @param  type type of modification.
     * @param  attributeName name of the attribute.
     * @throws IllegalArgumentException if given <code>type</code> is not one of the defined constants or given <code>
     *         String</code> is empty.
     * @throws NullPointerException if given <code>String</code> is <code>null</code>.
     */
    Modification(int type, String attributeName) {
        if ((type != ADD) && (type != REPLACE) && (type != DELETE_ATTRIBUTE) && (type != DELETE_VALUE)) {
            throw new IllegalArgumentException("Invalid type of modification.");
        }

        if (attributeName == null) {
            throw new NullPointerException("Null attribute name is prohibited.");
        }

        if (attributeName.trim().length() == 0) {
            throw new IllegalArgumentException("Empty attribute name is prohibited.");
        }

        this.type = type;
        this.attributeName = attributeName;
    }

    /**
     * Creates a Modification.</p>
     * <p>This constructor should be used (at this moment) for ADD, DELETE_VALUE and REPLACE types, beacause those
     * types require attribute values.</p>
     *
     * @param  type type of modification.
     * @param  attributeName name of the attribute.
     * @param  values values for the attribute.
     * @throws IllegalArgumentException if given <code>type</code> is not one of the defined constants or given <code>
     *         String</code> is empty.
     * @throws NullPointerException if given <code>String</code> is <code>null</code>.
     */
    Modification(int type, String attributeName, Values values) {
        this(type, attributeName);
        this.values = values;
    }

    /**
     * Get the type of modification.
     *
     * @return the type of modification.
     */
    public int getType() {
        return type;
    }

    /**
     * Get the values for this modification.
     *
     * @return the values for this modification.
     */
    public Values getValues() {
        return values;
    }

    /**
     * Get the attribute name for this modification.
     *
     * @return the attribute name for this modification.
     */
    public String getAttributeName() {
        return attributeName;
    }
}
