/**
 * Copyright (C) 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.security.SecureRandom;
import java.util.Random;

/**
 * <p>
 * this abstract implementation of the Generator interface simply provides helper storage
 * and functions to allow the generator's to be built easier.  Currently, the abstract class only provides
 * creation, storage and generation of a random generator.
 * </p>
 * <p>
 * <strong>Thread Safety: </strong>this class is thread safe from being immutable.
 * </p>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public abstract class AbstractGenerator implements Generator {

    /**
     * <p>
     * represents the random number generator being used by this generator.
     * </p>
     * <p>
     * <strong>Valid Values: </strong>a non-null Random implementation.
     * </p>
     *
     */
    private Random random = null;

    /**
     * <p>
     * creates the abstract generator by creating a new SecureRandom generator.
     * </p>
     */
    protected AbstractGenerator() {
        this(new SecureRandom());
    }

    /**
     * <p>
     * creates the abstract generator from the specified Random number generator.
     * </p>
     *
     * @param random
     *            a non-null Random implementation
     * @throws NullPointerException
     *             if the random is null
     */
    protected AbstractGenerator(Random random) {
        if (random == null) {
            throw new NullPointerException("random can't be null");
        }

        this.random = random;
    }

    /**
     * <p>
     * this protected getter method returns the random number generator. This
     * method is marked protected to prevent the application from accessing it.
     * </p>
     * @return a non-null Random number generator
     */
    protected Random getRandom() {
        return random;
    }

}

