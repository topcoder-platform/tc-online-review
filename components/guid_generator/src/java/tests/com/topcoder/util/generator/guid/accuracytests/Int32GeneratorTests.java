/**
 * GUID Generator 1.0
 *
 * Int32GeneratorTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import com.topcoder.util.generator.guid.UUID;
import com.topcoder.util.generator.guid.Int32Generator;
import com.topcoder.util.generator.guid.UUID32Implementation;

import junit.framework.TestCase;

/**
 * Accuracy tests for Int32Generator Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class Int32GeneratorTests extends TestCase {

    /**
     * Tests Int32Generator.getNextUUID() method for accuracy. 
     */
    public void testGetNextUUID() throws Exception {
        Int32Generator gen = new Int32Generator();
        UUID uuid = gen.getNextUUID();
        assertTrue(uuid instanceof UUID32Implementation);
    }
    
}
