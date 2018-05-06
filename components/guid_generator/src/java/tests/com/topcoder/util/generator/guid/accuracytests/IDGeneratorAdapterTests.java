/**
 * GUID Generator 1.0
 *
 * IDGeneratorAdapterTests.java
 *
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */

package com.topcoder.util.generator.guid.accuracytests;

import java.util.HashSet;
import java.util.Set;

import com.topcoder.util.generator.guid.IDGeneratorAdapter;

import junit.framework.TestCase;

/**
 * Accuracy tests for IDGeneratorAdapter Class 
 *
 * @author valeriy
 * @version 1.0
 */

public class IDGeneratorAdapterTests extends TestCase {

    /**
     * Tests IDGeneratorAdapter.getIDName() method for accuracy. 
     */
    public void testGetIDName() throws Exception {
        IDGeneratorAdapter gen = new IDGeneratorAdapter();
        assertEquals(IDGeneratorAdapter.class.getName(), gen.getIDName());
    }
    
    /**
     * Tests IDGeneratorAdapter.getNextID() method for accuracy. 
     */
    public void testGetNextID() throws Exception {
        IDGeneratorAdapter gen = new IDGeneratorAdapter();
        Set ids = new HashSet();
        for (int i = 0; i < 1000; i++) {
            ids.add(new Long(gen.getNextID()));
        }
        assertEquals(1000, ids.size());
    }
    
}
