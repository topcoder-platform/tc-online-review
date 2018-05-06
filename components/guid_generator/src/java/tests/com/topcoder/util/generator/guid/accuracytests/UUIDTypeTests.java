/**
 * GUID Generator 1.0
 *
 * UUIDTypeTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import com.topcoder.util.generator.guid.UUIDType;

import junit.framework.TestCase;

/**
 * Accuracy tests for UUIDType Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class UUIDTypeTests extends TestCase {

    /**
     * Tests UUIDType.hashCode() method for accuracy. 
     */
    public void testHashCode() throws Exception {
        assertEquals(UUIDType.TYPE1.hashCode(), 1);
        assertEquals(UUIDType.TYPE4.hashCode(), 4);
        assertEquals(UUIDType.TYPEINT32.hashCode(), 32);
    }
    
    /**
     * Tests UUIDType.equals() method for accuracy. 
     */
    public void testEquals() throws Exception {
        assertTrue(UUIDType.TYPE1.equals(UUIDType.TYPE1));
        assertTrue(UUIDType.TYPE4.equals(UUIDType.TYPE4));
        assertTrue(UUIDType.TYPEINT32.equals(UUIDType.TYPEINT32));
        assertFalse(UUIDType.TYPE1.equals(UUIDType.TYPE4));
        assertFalse(UUIDType.TYPE4.equals(UUIDType.TYPEINT32));
        assertFalse(UUIDType.TYPEINT32.equals(UUIDType.TYPE1));
        assertFalse(UUIDType.TYPE1.equals(null));
        assertFalse(UUIDType.TYPE1.equals(new Object()));
        assertFalse(UUIDType.TYPE1.equals("1"));
    }
    
}
