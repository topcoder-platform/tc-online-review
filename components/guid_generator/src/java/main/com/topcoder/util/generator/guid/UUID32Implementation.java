/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;


/**
 * <p>
 * this class represents the 32-bit UUID implementation of the UUID interface.
 * This UUID implementation will be generated from the Int32Generator.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe by being immutable
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUID32Implementation extends AbstractUUID {

    /**
     * <p>
     * constructs the UUID32Implementation from the 32-bits represented in the byte array.
     * </p>
     *
     * @param bytes
     *            a 4 length byte array representing the 32 bits
     * @throws IllegalArgumentException
     *             if the passed array is not 4 bytes in length
     * @throws NullPointerException
     *             if the passed array is null
     */
    public UUID32Implementation(byte[] bytes) {
        super(bytes);

        if (bytes.length != 4) {
            throw new IllegalArgumentException("bytes should have 4 bytes length");
        }
    }

    /**
     * <p>
     * encodes the byte array into a string.
     * </p>
     *
     * @return a 8 digit hex encoded string.
     */
    public String toString() {
        //  encoded string
        StringBuffer sb = new StringBuffer(8);

        // The byte array representation of this UUID
        byte[] bytes = toByteArray();

        // the unsigned value for each byte
        int unsignedValue;

        for (int i = 0; i < bytes.length; i++) {
            // get the value of byte i in an int variable such that
            // it preserves the same bit representation, and it will always be positive.
            unsignedValue = (byte) bytes[i] & 0xFF;

            // Append the byte in its two-digits hexadecimal representation
            sb.append((unsignedValue < 16 ? "0" : "") + Integer.toHexString(unsignedValue));
        }

        return sb.toString();

    }
}

