/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUIDUtility class.
 * </p>
 * <p>
 * It will test:
 * </p>
 * <ul>
 * <li> the getGenerator and getNextUUID methods with a null parameter, expecting to receive a NullPointerException</li>
 * <li> the getGenerator and getNextUUID methods with the 3 different UUID types to check that the rigth
 *      type is returned each time</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUIDUtilityTests extends TestCase {

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUIDUtilityTests.class);
    }

    /**
     * Test if getGenerator with a null parameter throws NullPointerException.
     */
    public void testGetGeneratorNull() {
        try {
            UUIDUtility.getGenerator(null);
            fail("getGenerator with null must throw NullPointerException");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }

    /**
     * Test if getNextUUID with a null parameter throws NullPointerException.
     */
    public void testGetNextUUID() {
        try {
            UUIDUtility.getNextUUID(null);
            fail("getNextUUID with null must throw NullPointerException");
        } catch (NullPointerException e) {
            // expected behavior
        }
    }
    /**
     * test that the getGenerator method with UUIDType.TYPE1 returns an instance of UUIDVersion1Generator.
     */
    public void testGetGeneratorType1() {
        assertTrue("bad generator type", UUIDUtility.getGenerator(UUIDType.TYPE1) instanceof UUIDVersion1Generator);
    }

    /**
     * test that the getGenerator method with UUIDType.TYPE4 returns an instance of UUIDVersion4Generator.
     */
    public void testGetGeneratorType4() {
        assertTrue("bad generator type", UUIDUtility.getGenerator(UUIDType.TYPE4) instanceof UUIDVersion4Generator);
    }

    /**
     * test that the getGenerator method with UUIDType.TYPEINT32 returns an instance of Int32Generator.
     */
    public void testGetGeneratorTypeInt32() {
        assertTrue("bad generator type", UUIDUtility.getGenerator(UUIDType.TYPEINT32) instanceof Int32Generator);
    }

    /**
     * test that the getNextUUID methods with UUIDType.TYPE1 returns a 128 bits UUID.
     */
    public void testGetNextUUIDType1() {
        assertEquals("bad getNextUUID return value", 128, UUIDUtility.getNextUUID(UUIDType.TYPE1).getBitCount());
    }

    /**
     * test that the getNextUUID methods with UUIDType.TYPE4 returns a 128 bits UUID.
     */
    public void testGetNextUUIDType4() {
        assertEquals("bad getNextUUID return value", 128, UUIDUtility.getNextUUID(UUIDType.TYPE4).getBitCount());
    }

    /**
     * test that the getNextUUID methods with UUIDType.TYPEINT32 returns a 32 bits UUID.
     */
    public void testGetNextUUIDTypeInt32() {
        assertEquals("bad getNextUUID return value", 32, UUIDUtility.getNextUUID(UUIDType.TYPEINT32).getBitCount());
    }
}
