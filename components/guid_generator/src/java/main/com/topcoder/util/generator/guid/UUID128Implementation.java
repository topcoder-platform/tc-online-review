/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

/**
 * <p>
 * this class represents the 128-bit UUID implementation of the UUID interface.
 * This UUID implementation will be generated from the UUIDVersion1 and UUIDVersion4 generators.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe by being immutable
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUID128Implementation extends AbstractUUID {


    /**
     * <p>
     * constructs the UUID128Implementation from the 128-bits represented in the byte array.
     * </p>
     *
     * @param bytes
     *            a 16 length byte array representing the 128 bits
     * @throws NullPointerException
     *             if the array is null
     * @throws IllegalArgumentException
     *             if the array is not 16 in length
     */
    public UUID128Implementation(byte[] bytes) {
        super(bytes);
        if (bytes.length != 16) {
            throw new IllegalArgumentException("bytes should have 16 bytes length");
        }
    }

    /**
     * <p>
     * encodes the byte array into a string according to the UUID draft specifications (section 3.5)
     * for example:
     * f81d4fae-7dec-11d0-a765-00a0c91e6bf6
     * </p>
     *
     * @return a 36 byte encoded string
     */
    public String toString() {
        // encoded string
        StringBuffer sb = new StringBuffer(36);

        // The byte array representation of this UUID
        byte[] bytes = toByteArray();

        // the unsigned value for each byte
        int unsignedValue;

        for (int i = 0; i < bytes.length; i++) {
            // get the value of byte i in an int variable such that
            // it preserves the same bit representation, and it will always be positive.
            unsignedValue = bytes[i] & 0xFF;
            
            // Append the byte in its two-digits hexadecimal representation
            sb.append((unsignedValue < 16 ? "0" : "") + Integer.toHexString(unsignedValue));
        }

        // Put a '-' in the required places
        sb.insert(8, '-');
        sb.insert(13, '-');
        sb.insert(18, '-');
        sb.insert(23, '-');
        return sb.toString();
    }
}

