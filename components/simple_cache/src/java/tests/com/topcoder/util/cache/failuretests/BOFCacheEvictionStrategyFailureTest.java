/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import java.util.Hashtable;

import com.topcoder.util.cache.BOFCacheEvictionStrategy;

import junit.framework.TestCase;

/**
 * Failure test for BOFCacheEvictionStrategy class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class BOFCacheEvictionStrategyFailureTest extends TestCase {
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
            new BOFCacheEvictionStrategy().init(entriesMap);
            fail("Should throw IllegalArgumentException if entriesMap is invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>notifyOfCachePut</code>.
     * An UnsupportedOperationException should be thrown always.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testNotifyOfCachePut() throws Exception {
        try {
            new BOFCacheEvictionStrategy().notifyOfCachePut("key");
            fail("Should throw UnsupportedOperationException always.");
        } catch (UnsupportedOperationException e) {
            // success
        }
    }
}
