/**
 * GUID Generator 1.0
 *
 * UUIDVersion1GeneratorTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import com.topcoder.util.generator.guid.UUID;
import com.topcoder.util.generator.guid.UUID128Implementation;
import com.topcoder.util.generator.guid.UUIDVersion1Generator;

import junit.framework.TestCase;

/**
 * Accuracy tests for UUIDVersion1Generator Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class UUIDVersion1GeneratorTests extends TestCase {

    /**
     * Tests UUIDVersion1Generator.getNextUUID() method for accuracy. 
     */
    public void testGetNextUUID() throws Exception {
        UUIDVersion1Generator gen = new UUIDVersion1Generator();
        UUID uuid = gen.getNextUUID();
        assertTrue(uuid instanceof UUID128Implementation);
        assertEquals(0x10, uuid.toByteArray()[6] & 0xF0);//version
        assertEquals(0x80, uuid.toByteArray()[8] & 0xC0);//variant
    }
    
}
