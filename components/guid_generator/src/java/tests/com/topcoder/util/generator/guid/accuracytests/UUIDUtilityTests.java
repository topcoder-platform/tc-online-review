/**
 * GUID Generator 1.0
 *
 * UUIDUtilityTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import com.topcoder.util.generator.guid.Int32Generator;
import com.topcoder.util.generator.guid.UUID;
import com.topcoder.util.generator.guid.UUID128Implementation;
import com.topcoder.util.generator.guid.UUID32Implementation;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;
import com.topcoder.util.generator.guid.UUIDVersion1Generator;
import com.topcoder.util.generator.guid.UUIDVersion4Generator;

import junit.framework.TestCase;

/**
 * Accuracy tests for UUIDUtility Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class UUIDUtilityTests extends TestCase {

    /**
     * Tests UUIDUtility.getGenerator() method for accuracy. 
     */
    public void testGetGenerator() throws Exception {
        assertTrue(UUIDUtility.getGenerator(UUIDType.TYPE1) instanceof UUIDVersion1Generator);
        assertTrue(UUIDUtility.getGenerator(UUIDType.TYPE4) instanceof UUIDVersion4Generator);
        assertTrue(UUIDUtility.getGenerator(UUIDType.TYPEINT32) instanceof Int32Generator);
    }
    
    /**
     * Tests UUIDUtility.getNextUUID() method for accuracy. 
     */
    public void testGetNextUUID() throws Exception {
        assertTrue(UUIDUtility.getNextUUID(UUIDType.TYPEINT32) instanceof UUID32Implementation);

        UUID uuid = UUIDUtility.getNextUUID(UUIDType.TYPE1);
        assertTrue(uuid instanceof UUID128Implementation);
        assertEquals(0x10, uuid.toByteArray()[6] & 0xF0);//version
        assertEquals(0x80, uuid.toByteArray()[8] & 0xC0);//variant

        uuid = UUIDUtility.getNextUUID(UUIDType.TYPE4);
        assertTrue(uuid instanceof UUID128Implementation);
        assertEquals(0x40, uuid.toByteArray()[6] & 0xF0);//version
        assertEquals(0x80, uuid.toByteArray()[8] & 0xC0);//variant
    }
    
}
