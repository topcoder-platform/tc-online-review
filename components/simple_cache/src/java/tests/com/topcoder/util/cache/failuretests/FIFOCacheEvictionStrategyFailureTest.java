/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import java.util.Hashtable;

import com.topcoder.util.cache.FIFOCacheEvictionStrategy;

import junit.framework.TestCase;

/**
 * Failure test for FIFOCacheEvictionStrategy class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class FIFOCacheEvictionStrategyFailureTest extends TestCase {
    /**
     * Test the method <code>init</code>.
     * Test the case the entriesMap is invalid, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testInitEntriesMapInvalid() throws Exception {
        try {
            Hashtable entriesMap = new Hashtable();
            entriesMap.put("key", "value");
            new FIFOCacheEvictionStrategy().init(entriesMap);
            fail("Should throw IllegalArgumentException if entriesMap is invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
