/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import com.topcoder.util.collection.typesafeenum.Enum;

/**
 * <p>
 * this is an enum for the valid UUID types this component deals with. While java
 * doesn't support the enum type, we can get a close resemblance by using the type safe enum component. This
 * enum simply defines the types for the given generators in this component. The enum provides
 * implementations of the hashCode() and equals() functions to allow it to be the key of the generators map
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe because it's immutable
 * </p>
 *  
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUIDType extends Enum {

    /**
     * <p>
     * represents the enum type 1 generator.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a non-null UUIDType
     * </p>
     *  
     */
    public static final UUIDType TYPE1 = new UUIDType(1);

    /**
     * <p>
     * represents the enum type 4 generator.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a non-null UUIDType
     * </p>
     *
     */
    public static final UUIDType TYPE4 = new UUIDType(4);

    /**
     * <p>
     * represents the enum type for the int32 generator.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a non-null UUIDType
     * </p>
     *
     */
    public static final UUIDType TYPEINT32 = new UUIDType(32);

    /**
     * <p>
     * represents the version number of this type.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a valid integer
     * </p>
     *
     */
    private int version;

     /**
     * <p>
     * this private constructor simply saves the passed argument.
     * </p>
     *
     * @param version the version of UUID to use
     */
    private UUIDType(int version) {
        this.version = version;
    }

    /**
     * <p>
     * implementation of the hashCode to provide uniqueness between types.
     * </p>
     *
     * @return the hash code for this instance
     */
    public int hashCode() {
        return version;
    }

    /**
     * <p>
     * this implemenation of the equals method simply determines if the type is equal to
     * another type based on the verison number.
     * </p>
     *
     * @param o
     *            a possibly null object
     * @return true if the objects are the same version, false otherwise
     */
    public boolean equals(Object o) {
        return (o instanceof UUIDType) && (((UUIDType) o).version == version);
    }
}

