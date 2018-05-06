/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;


/**
 * <p>
 * this abstract class should be used as the base class for all UUID generators.
 * This class provides storage of the bytes and provides default implementations of two of the UUID interface
 * methods
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is not thread safe because, for example, the byte array passed
 * as a parameter could be changed at the same time that a UUID is being parsed.  However, those kind of
 * situations shouldn't arise because it doesn't makes lot of sense for the user to do that; and it was
 * chosen to improve speed avoiding copying than providing thread safety where is not really important.
 * </p>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public abstract class AbstractUUID implements UUID {
    /**
     * <p>
     * represents the bytes that make up the UUID that was generated
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a non-null array of 1 to many bytes (typically 4 or 16 in length)
     * </p>
     *
     */
    private byte[] bytes = null;

    /**
     * <p>
     * this protected constructor creates the Abstract UUID from the given bytes.
     * </p>
     *
     *
     * @param bytes
     *            a non-null byte array
     * @throws NullPointerException if the array is null
     *
     */
    protected AbstractUUID(byte[] bytes)  {
        if (bytes == null) {
            throw new NullPointerException("bytes can't be null");
        }
        this.bytes = bytes;
    }

    /**
     * <p>
     * returns the number of bits defined in this UUID as per the UUID interface contract.
     * </p>
     *
     * @return the number of bits defined in this UUID
     */
    public int getBitCount() {
        return bytes.length * 8;
    }

    /**
     * <p>
     * simply returns the bytes defined in this UUID.
     * </p>
     *
     * @return the bytes defined in this UUID
     */
    public byte[] toByteArray() {
        return bytes;
    }

    /**
     * <p>
     * helper utility to generate a UUID based on the string representation.
     * Currently only the 32-bit and 128 bit UUID strings are possible.
     * </p>
     *
     * @param id
     *            a 8 or 36 byte length string
     * @return a non-null UUID implementation
     * @throws NullPointerException
     *             if the string is null
     * @throws IllegalArgumentException
     *             if the string is not 8 or 36 in length or it could not be decoded
     */
    public static final UUID parse(String id) {
        if (id == null) {
            throw new NullPointerException("id can't be null");
        }

        if (id.length() == 8) {
            // directly convert the string to a byte array and create a 32 bit representation
            return new UUID32Implementation(toByteArray(id));
        } else if (id.length() == 36) {

            // Check that there are '-' in the expected places
            if ((id.charAt(8) != '-') || (id.charAt(13) != '-') || (id.charAt(18) != '-') || (id.charAt(8) != '-')) {
                throw new IllegalArgumentException("The string does not represent a 128 bits UUID");
            }
            // make a string without the '-' , convert this string to a byte array and create a 128 bit representation
            return new UUID128Implementation(toByteArray(id.substring(0, 8) + id.substring(9, 13)
                    + id.substring(14, 18) + id.substring(19, 23) + id.substring(24, 36)));
        } else {
            throw new IllegalArgumentException("The string has an invalid length");
        }
    }

    /**
     * Helper method that converts a string of hexadecimal numbers into an array of bytes.
     * If an odd number of characters is passed, the last one will not be taken into account.
     *
     * @param id the string to convert
     *
     * @return an array of bytes. The size is half of the String size, because 2 characters represent a byte.
     * @throws NumberFormatException if the string contains invalid characters.
     */
    private static byte[] toByteArray(String id) {
        int n = id.length() / 2;
        byte[] bytes = new byte[n];

        for (int i = 0; i < n; i++) {
            // decode method throw a NumberFormatException if the string is not a valid hexadecimal value.
            // NumberFormatException inherits from IllegalArgumentException
            bytes[i] = (byte) Integer.parseInt(id.substring(i * 2, i * 2 + 2), 16);
        }

        return bytes;
    }

}

