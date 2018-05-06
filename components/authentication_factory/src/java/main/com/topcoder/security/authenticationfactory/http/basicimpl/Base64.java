/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory.http.basicimpl;

/**
 * This class provide only one static method to encode the byte array
 * into the Base64 format string. Please look into the method for more
 * detail.
 *
 * (See RFC 1521: MIME Part One - Base64 Encoding)
 * @author  TCSDEVELOPER 1.1
 * @version 1.0 18/09/2004
 */
final class Base64 {

   /**
    * Private constructor to prevent instantiation.
    */
    private Base64() {

    }
   /**
     * Encodes the byte array into Base64 format.
     *
     * @param  bytes a <code>byte[]</code> containing the data to be encoded.
     * @return a <code>String</code> that contains the Base64 Representation
     * of the byte array.
     */

    static String encode(byte[] bytes) {
        StringBuffer output = new StringBuffer();
        int current = 0; // The current position of byte being processed
        int bytesEncoded = 0; // Number of bytes encoded

        // Process the entire byte Array
        while (current < bytes.length) {
            char[] encodedBlock; // A set of 4 characters encoded in Base64

            // calculate the number of bytes remaining
            int left = bytes.length - current;

            // perform the appropriate action, depending on whether there are
            // 'dangling' bytes or not.
            if (left >= 3) {
                encodedBlock = encode(bytes[current++], bytes[current++],
                    bytes[current++]);
                output.append(encodedBlock);
            }

            if (left  ==  2) {
                encodedBlock = encode(bytes[current++], bytes[current++]);
                output.append(encodedBlock);
            }

            if (left  ==  1) {
                encodedBlock = encode(bytes[current++]);
                output.append(encodedBlock);
            }

            // update the number of bytes encoded so far
            bytesEncoded += 4;

            // add a line break if current line already has 76 characters
            if ((bytesEncoded % 76)  ==  0) {
                output.append("\n");
            }
        }

        return output.toString();
    }

    /**
     * <p>
     * Standard encoding with no dangling bytes.
     * </p>
     *
     * @param  first the first <code>byte</code> to be encoded.
     * @param  second the second <code>byte</code> to be encoded.
     * @param  third the third <code>byte</code> to be encoded.
     * @return a <code>char[]</code> that contains a quarted Base64
     * representation of the 8-bit triplet.
     */
    private static char[] encode(byte first, byte second, byte third) {
        char[] output = new char[4]; // holds the character output
        int holder = 0; // holds the concatenated data of the bytes

        // first aggregate the data in the holder
        holder = holder | ((first & 0xff) << 16);
        holder = holder | ((second & 0xff) << 8);
        holder = holder | (third & 0xff);

        // now get the character representations of the 6-bit characters.
        output[0] = getBase64Char((holder >> 18) & 0x3f);
        output[1] = getBase64Char((holder >> 12) & 0x3f);
        output[2] = getBase64Char((holder >> 6) & 0x3f);
        output[3] = getBase64Char(holder & 0x3f);

        return output;
    }

    /**
     * <p>
     * Standard encoding with one missing byte.
     * </p.
     *
     * @param  first the first <code>byte</code> to be encoded.
     * @param  second the second <code>byte</code> to be encoded.
     * @return a <code>char[]</code> that contains a quarted Base64
     * representation of the 8-bit triplet.
     */
    private static char[] encode(byte first, byte second) {
        char[] output = new char[4];
        int holder = 0;

        // Compensate for negative values.
        if (first < 0) {
            first += 256;
        }

        if (second < 0) {
            second += 256;
        }

        // Same as method above
        holder = holder | ((first & 0xff) << 16);
        holder = holder | ((second & 0xff) << 8);

        output[0] = getBase64Char((holder >> 18) & 0x3f);
        output[1] = getBase64Char((holder >> 12) & 0x3f);
        output[2] = getBase64Char((holder >> 6) & 0x3f);

        // Add special padding character for the missing byte(s)
        output[3] = '=';

        return output;
    }

    /**
     * <p>
     * Standard encoding with two missing bytes.
     * </p>
     *
     * @param  first the first <code>byte</code> to be encoded.
     * @return a <code>char[]</code> that contains a quarted Base64
     * representation of the 8-bit triplet.
     */
    private static char[] encode(byte first) {
        char[] output = new char[4];
        int holder = 0;

        // Compensate for negative values.
        if (first < 0) {
            first += 256;
        }

        // Same as method above
        holder = holder | ((first & 0xff) << 16);

        output[0] = getBase64Char((holder >> 18) & 0x3f);
        output[1] = getBase64Char((holder >> 12) & 0x3f);
        output[2] = '=';
        output[3] = '=';

        return output;
    }

    /**
     * Returns the appropriate character from the Base64 Table.
     *
     * @param  i the index of the character to lookup.
     * @return a <code>char</code> of the specified Base64 character.
     */

    private static char getBase64Char(int i) {
        if ((i >= 0) && (i <= 25)) {
            return (char) ('A' + i);
        }

        if ((i >= 26) && (i <= 51)) {
            return (char) ('a' + i - 26);
        }

        if ((i >= 52) && (i <= 61)) {
            return (char) ('0' + i - 52);
        }

        if (i  ==  62) {
            return '+';
        }

        if (i  ==  63) {
            return '/';
        }

        //Something went wrong...
        return '\n';
    }
}
