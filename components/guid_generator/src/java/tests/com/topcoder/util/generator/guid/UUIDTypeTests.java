/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the UUIDTypeTests class.
 * </p>
 * <p>
 * It will test:
 * </p>
 * <ul>
 * <li>The hashcodes of the 3 types</li>
 * <li>The equals method with null and a non-UUIDType object, expecting to receive false</li>
 * <li>The equals method with different combinations</li>
 * </ul>
 *
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class UUIDTypeTests extends TestCase {

    /**
     * return a suite of tests.
     *
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSuite(UUIDTypeTests.class);
    }

    /**
     * Test that UUID version 1 hashcode is 1.
     */
    public void testHashCode1() {
        assertEquals("Invalid hashcode", 1, UUIDType.TYPE1.hashCode());
    }

    /**
     * Test that UUID version 4 hashcode is 4.
     */
    public void testHashCode4() {
        assertEquals("Invalid hashcode", 4, UUIDType.TYPE4.hashCode());
    }

    /**
     * Test that int 32 hashcode is 1.
     */
    public void testHashCode32() {
        assertEquals("Invalid hashcode", 32, UUIDType.TYPEINT32.hashCode());
    }
    
    /**
     * Test that the equals method returns false with a null parameter.
     */
    public void testEqualsNull() {
        assertFalse("equals is not working fine",  UUIDType.TYPE1.equals(null));
    }

    /**
     * Test that the equals method returns false with a parameter that is not a UUIDType.
     */
    public void testEqualsNotUUIDType() {
        assertFalse("equals is not working fine",  UUIDType.TYPE1.equals("this is a string"));
    }

    /**
     * Test the equals method with a non equal UUIDType.
     */
    public void testEquals1() {
        assertFalse("equals is not working fine",  UUIDType.TYPE1.equals(UUIDType.TYPE4));
    }

    /**
     * Test the equals method with a non equal UUIDType.
     */
    public void testEquals2() {
        assertFalse("equals is not working fine",  UUIDType.TYPEINT32.equals(UUIDType.TYPE1));
    }

    /**
     * Test the equals method with an equal UUIDType.
     */
    public void testEquals3() {
        assertTrue("equals is not working fine",  UUIDType.TYPE1.equals(UUIDType.TYPE1));
    }
    /**
     * Test the equals method with an equal UUIDType.
     */
    public void testEquals4() {
        assertTrue("equals is not working fine",  UUIDType.TYPEINT32.equals(UUIDType.TYPEINT32));
    }

}