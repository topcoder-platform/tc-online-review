/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;
import java.util.zip.CRC32;

/**
 * <p>
 * this implementation of the Generator interface will generate 32-bit UUID's.
 * It is based on the algorithm found at http://www.mail-archive.com/ejb-interest@java.sun.com/msg01757.html
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe by having getNextUUID synchronized
 * </p>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class Int32Generator extends AbstractGenerator {

    /**
     * <p>
     * represents a simple sequential number that is implemented on each access.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a valid integer
     * </p>
     *
     */
    private int seq = 0;

    /**
     * <p>
     * create the int32generator using a SecureRandom generator.
     * </p>
     *
     *
     */
    public Int32Generator() {
        super();
        seq = getRandom().nextInt();
    }

    /**
     * <p>
     * create the int32generator using the specified generator.
     * </p>
     *
     * @param random
     *            a non-null Random implementation
     * @throws NullPointerException
     *             if the random is null
     */
    public Int32Generator(Random random) {
        // super will throw a NullPointerException if random is null
        super(random);
        seq = getRandom().nextInt();
    }

    /**
     * <p>
     * generates the next UUID based on the int32 algorithm.
     * This algorithm is described in
     * http://www.mail-archive.com/ejb-interest@java.sun.com/msg01757.html
     * </p>
     *
     * @return a non-null UUID32Implemenation
     */
    public synchronized UUID getNextUUID() {
        byte[] bytes = new byte[4]; // store the 32 bits result
        long currentTime = System.currentTimeMillis();

        // divide the current time in two ints
        int ct1 = (int) (currentTime >> 32);
        int ct2 = (int) (currentTime & 0xFFFFFFFF);

        seq++;

        // Calculate a crc using the sequence number and both halves of the current time
        CRC32 crc = new CRC32();
        crc.update(getBytes(seq), 0, 4);
        crc.update(getBytes(ct1), 0, 4);
        crc.update(getBytes(ct2), 0, 4);

        // get the 32 least significant bits of the CRC.
        // this intermediate variable it is used just for making code clearer.
        int result = (int) (crc.getValue() & 0xFFFFFFFF);

        return new UUID32Implementation(getBytes(result));
    }

    /**
     * Helper method to extract the 4 bytes that form an integer.
     *
     * @param n the int value to be divided
     * @return an array of four bytes, where bytes[0] is the most significant byte and bytes[3] the least.
     */
    private byte[] getBytes(int n) {
        byte[] result = new byte[4];

        for (int i = 3; i >= 0; i--) {
            result [i] = (byte) (n & 0xFF);
            n >>>= 8;
        }
        return result;
    }

}

