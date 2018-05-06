/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test any class extending AbstractGenerator.
 * To use those tests, inherit this class and implement the abstract methods in order to make the tests for
 * the desired generator.
 * </p>
 * <p>
 * It will test:
 * </p>
 * <ul>
 * <li>Generating a lot of UUID's and storing in a set to test uniqueness</li>
 * <li>the parse method</li>
 * <li>the constructor with a null parameter</li>
 * <li>the getRandom method in conjunction with the constructor</li>
 * </ul>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public abstract class UUIDGeneratorTests extends TestCase {

    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUIDVersion1GeneratorTests.class);
    }

    /**
     * This method should be implemented in inheriting classes to provide a generator to test.
     * 
     * @return an instance of a kind of AbstractGenerator
     */
    protected abstract AbstractGenerator getGenerator();

    /**
     * This method should be implemented in inheriting classes to provide a generator to test.
     * 
     * @param random object for generating random numbers
     * @return an instance of a kind of AbstractGenerator
     */
    protected abstract AbstractGenerator getGenerator(Random random);

    /**
     * Test generating a burst of ID's to see if they're different.
     *  
     */
    public void testUnique1() {
        Generator gen = getGenerator();
        Set set = new HashSet();
        int repeated = 0;
        for (int i = 0; i < 100000; i++) {
            if (!set.add(gen.getNextUUID())) {
                repeated++;
            }
        }
        assertEquals("There are some elements repeated. However, it is not impossible that this happens."
                + "Please run the tests again.", 0, repeated);
    }

    /**
     * Test generating bursts of ID's in different times to see if they're different.
     *  
     */
    public void testUnique2() {
        Generator gen = getGenerator();
        Set set = new HashSet();
        int repeated = 0;
        long lastTime = 0;

        for (int i = 0; i < 20; i++) {
            while (lastTime == System.currentTimeMillis()) {
                // just wait for the system time to change
            }
            lastTime = System.currentTimeMillis();
            for (int j = 0; j < 10000; j++) {
                if (!set.add(gen.getNextUUID())) {
                    repeated++;
                }
            }
        }
        assertEquals("There are some elements repeated.  However, it is not impossible that this happens."
                + "Please run the tests again.", 0, repeated);
    }

    /**
     * Test that the parse method is working fine with different UUIDs.
     * For that purpouse, an UUID is generated, then it's converted to string and back to its byte representation
     * using the parse method.  The original byte array should be the same as the byte array of the parsed uuid.
     * This is repeated 10,000 times, each time being a different random UUID generated.
     */
    public void testParse() {
        Generator gen = getGenerator();

        for (int i = 0; i < 10000; i++) {
            UUID uuid = gen.getNextUUID();
            byte[] originalBytes = uuid.toByteArray();
            byte[] parsedBytes = AbstractUUID.parse(uuid.toString()).toByteArray();
            assertTrue("parsing give a bad result with uuid " + uuid, Arrays.equals(originalBytes, parsedBytes));
        }
    }

    /**
     * Test if the constructor for the generator class throws NullPointerException when called with a null Random.
     */
    public void testConstructorNull() {
        try {
            getGenerator(null);
            fail("the constructor must throw NullPointerException when called with null");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Test the getRandom method and the constructor.  The getRandom method must return the Random
     * provided in the constructor.
     */
    public void testGetRandom() {
        Random random = new Random();
        AbstractGenerator gen = getGenerator(random);
        assertSame("getRandom() is not returning the expected random", random, gen.getRandom());
    }
}