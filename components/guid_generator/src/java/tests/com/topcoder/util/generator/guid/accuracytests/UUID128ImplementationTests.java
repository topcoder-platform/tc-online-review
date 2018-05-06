/**
 * GUID Generator 1.0
 *
 * UUID128ImplementationTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import java.util.Arrays;

import com.topcoder.util.generator.guid.AbstractUUID;
import com.topcoder.util.generator.guid.UUID;
import com.topcoder.util.generator.guid.UUID128Implementation;

import junit.framework.TestCase;

/**
 * Accuracy tests for UUID128Implementation Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class UUID128ImplementationTests extends TestCase {

    /**
     * Tests UUID128Implementation.getBitCount() method for accuracy. 
     */
    public void testGetBitCount() throws Exception {
        UUID128Implementation uuid = new UUID128Implementation(new byte[16]);
        assertEquals(uuid.getBitCount(), 128);
    }
    
    /**
     * Tests UUID128Implementation.toByteArray() method for accuracy. 
     */
    public void testToByteArray() throws Exception {
        byte[] bytes = new byte[] {11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59};
        UUID128Implementation uuid = new UUID128Implementation(bytes);
        byte[] bytes2 = uuid.toByteArray();
        assertTrue(Arrays.equals(bytes, bytes2));
    }
    
    /**
     * Tests UUID128Implementation.toString() method for accuracy. 
     */
    public void testToString() throws Exception {
        UUID128Implementation uuid = new UUID128Implementation(new byte[] {11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59});
        assertEquals("0b232f3b-0b23-2f3b-0b23-2f3b0b232f3b", uuid.toString().toLowerCase());

        uuid = new UUID128Implementation(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0});
        assertEquals("00000000-0000-0000-0000-000000000000", uuid.toString().toLowerCase());

        uuid = new UUID128Implementation(new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1});
        assertEquals("ffffffff-ffff-ffff-ffff-ffffffffffff", uuid.toString().toLowerCase());
    }
    
    /**
     * Tests UUID128Implementation.parse() method for accuracy. 
     */
    public void testParse() throws Exception {
        UUID uuid = AbstractUUID.parse("0b232f3b-0b23-2f3b-0b23-2f3b0b232f3b");
        assertTrue(uuid instanceof UUID128Implementation);
        assertTrue(Arrays.equals(new byte[] {11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59, 11, 35, 47, 59}, uuid.toByteArray()));

        uuid = AbstractUUID.parse("00000000-0000-0000-0000-000000000000");
        assertTrue(uuid instanceof UUID128Implementation);
        assertTrue(Arrays.equals(new byte[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, uuid.toByteArray()));

        uuid = AbstractUUID.parse("FFFFFFFF-FFFF-FFFF-FFFF-FFFFFFFFFFFF");
        assertTrue(uuid instanceof UUID128Implementation);
        assertTrue(Arrays.equals(new byte[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1}, uuid.toByteArray()));
    }
    
}
