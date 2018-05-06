/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Random;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the Int32Generator class.
 * </p>
 * <p>
 * Because this testCase extends UUIDGeneratorTests, all the tests provided on that class will be executed
 * over the Int32Generator class.  Please see to UUIDGeneratorTests. 
 * </p>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class Int32GeneratorTests extends UUIDGeneratorTests {

    /**
     * The instance of Int32Generator that will be used for testing.
     * The UUIDGeneratorTests class will ask for it using getGenerator() method.
     */
    private static AbstractGenerator generator = new Int32Generator();

    /**
     * get the int 32-bits generator.
     * 
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator() {
        return generator;
    } 

    /**
     * get the int 32-bits generator.
     * 
     * @param random object for generating random numbers
     * @return an instance of a kind of AbstractGenerator
     */
    protected AbstractGenerator getGenerator(Random random) {
        return new Int32Generator(random);
    } 

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(Int32GeneratorTests.class);
    }

}