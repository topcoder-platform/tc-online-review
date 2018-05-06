/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;

/**
 * <p>
 * creates a UUID draft version 4 generator that generates UUID based fully off a strong
 * random number generator.  This class will, by default, be created using the SecureRandom
 * implementation.Application can choose to supply their own random implementation instead.
 * </p>
 * <p>
 * If the application provides their own random implementation, it should use a cryptographic strength random 
 * number generator.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe by being immutable.
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUIDVersion4Generator extends AbstractGenerator {

    /**
     * creates this generator using the SecureRandom generator.
     */
    public UUIDVersion4Generator() {
        super();
    } 

    /**
     * create the UUIDVersion4Generator using the specified generator.
     * 
     * @param random
     *            a non-null Random implementation
     * @throws NullPointerException
     *             if random is null
     */
    public UUIDVersion4Generator(Random random) {
        // super will throw NullPointerException if random is null
        super(random);
    } 

    /**
     * <p>
     * this implements the version 4 generation of random UUID as specified in the UUID draft specification:
     * <ol>
     * <li>Fill a 16 byte array with random numbers</li>
     * <li> Set the variant field (bits 7 and 6 of byte 8) to 10 binary</li>
     * <li> Set the version field (high nible of byte 6) to 0100 binary </li>
     * <li> Return a new UUID128Implementation based on the resulting bytes</li>
     * </ol>
     * </p>
     * 
     * @return a non-null UUID128Implemenation
     */
    public UUID getNextUUID() {
        byte[] bytes = new byte[16];
        
        // Get 16 random bytes (128 bits)
        getRandom().nextBytes(bytes);

        // Set the variant field (bits 7 and 6 of byte 8) to 10 binary  
        bytes [8] = (byte) ((bytes [8] & 0x3F) | 0x80); 
        
        // Set the version field (high nible  of byte 6) to 0100 binary 
        bytes [6] = (byte) ((bytes [6] & 0x0F) | 0x40); 
        
        return new UUID128Implementation(bytes);
    } 
} 

