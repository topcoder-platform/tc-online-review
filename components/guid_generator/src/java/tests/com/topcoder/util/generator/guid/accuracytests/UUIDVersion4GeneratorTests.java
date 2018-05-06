/**
 * GUID Generator 1.0
 *
 * UUIDVersion4GeneratorTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import com.topcoder.util.generator.guid.UUID;
import com.topcoder.util.generator.guid.UUID128Implementation;
import com.topcoder.util.generator.guid.UUIDVersion4Generator;

import junit.framework.TestCase;

/**
 * Accuracy tests for UUIDVersion4Generator Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class UUIDVersion4GeneratorTests extends TestCase {

    /**
     * Tests UUIDVersion4Generator.getNextUUID() method for accuracy. 
     */
    public void testGetNextUUID() throws Exception {
        UUIDVersion4Generator gen = new UUIDVersion4Generator();
        UUID uuid = gen.getNextUUID();
        assertTrue(uuid instanceof UUID128Implementation);
        assertEquals(0x40, uuid.toByteArray()[6] & 0xF0);//version
        assertEquals(0x80, uuid.toByteArray()[8] & 0xC0);//variant
    }
    
}
