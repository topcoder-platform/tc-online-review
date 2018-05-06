/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

/**
 * <p>
 * defines the contract that UUID implementations must follow. This contract allows
 * the application to determine the number of bits the UUID represents, to return those bits and to return a string
 * encoded verison of those bits.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>classes implementing this interface should be thread safe.
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public interface UUID {
    /**
     * <p>
     * returns the number of bits defined in this UUID.
     * </p>
     * 
     * @return the number of bits defined in this UUID
     */
    public int getBitCount();

    /**
     * <p>
     * returns the bytes defined in this UUID.
     * </p>
     * 
     * @return the bytes defined in this UUID
     */
    public byte[] toByteArray();

    /**
     * <p>
     * returns a string encoded version of the UUID. How the string is encoded
     * depends on the implementation but will typically be some hex encoded value.
     * </p>
     * 
     * @return a string encoded version of the UUID
     */
    public String toString();

}

