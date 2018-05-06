/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.cache.failuretests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.topcoder.util.cache.CacheInstantiationException;
import com.topcoder.util.cache.SimpleCache;

import junit.framework.TestCase;

/**
 * Failure test for SimpleCache class.
 *
 * @author semi_sleep
 * @version 2.0
 */
public class SimpleCacheFailureTest extends TestCase {
    /**
     * Instance of SimpleCache used in test.
     */
    private SimpleCache instance = new SimpleCache();
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that nameSpace is null,
     * a NullPointerException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor1NullNameSpace() throws Exception {
        try {
            new SimpleCache(null);
            fail("Should throw NullPointerException when nameSpace is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that nameSpace is emtpy string,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor1NameSpaceEmpty() throws Exception {
        try {
            new SimpleCache("");
            fail("Should throw IllegalArgumentException when nameSpace is empty string.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that nameSpace is invalid,
     * an CacheInstantiationException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor1NameSpaceInvalid() throws Exception {
        try {
            new SimpleCache("abc");
            fail("Should throw CacheInstantiationException when nameSpace is invalid.");
        } catch (CacheInstantiationException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that maxCacheSize is smaller than zero,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2MaxCacheSizeNegative() throws Exception {
        try {
            new SimpleCache(0, 1, null, 1, null, null, true);
            fail("Should throw IllegalArgumentException when maxCacheSize is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that timeoutMS is smaller than zero,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2TimeoutMSNegative() throws Exception {
        try {
            new SimpleCache(1, 0, null, 1, null, null, true);
            fail("Should throw IllegalArgumentException when timeoutMS is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that maxCacheCapacity is smaller than zero,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2MaxCacheCapacityNegative() throws Exception {
        try {
            new SimpleCache(1, 1, null, 0, null, null, true);
            fail("Should throw IllegalArgumentException when maxCacheCapacity is smaller than zero.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the constructor of <code>SimpleCache</code>.
     * This method test the case that compressionHandlerList contains invalid element,
     * an IllegalArgumentException should be thrown in this case.
     * 
     * @throws Exception if any error occurs during creation
     */
    public void testCtor2CompressionHandlerListInvalid() throws Exception {
        try {
            ArrayList compressionHandlerList = new ArrayList();
            compressionHandlerList.add("abc");
            new SimpleCache(1, 1, null, 1, null, compressionHandlerList, true);
            fail("Should throw IllegalArgumentException when compressionHandlerList contains invalid element.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>get</code>.
     * Test the case the key is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testGetKeyNull() throws Exception {
        try {
            instance.get(null);
            fail("Should throw NullPointerException if key is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>put</code>.
     * Test the case the key is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testPutKeyNull() throws Exception {
        try {
            instance.put(null, null);
            fail("Should throw NullPointerException if key is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>put</code>.
     * Test the case the cacheEntry contains null key, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testPutCacheEntryInvalid() throws Exception {
        try {
            HashMap cacheEntry = new HashMap();
            cacheEntry.put(null, "abc");
            instance.put(cacheEntry);
            fail("Should throw IllegalArgumentException if cacheEntry contains null key.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>remove</code>.
     * Test the case the key is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveKeyNull() throws Exception {
        try {
            instance.remove(null);
            fail("Should throw NullPointerException if key is null.");
        } catch (NullPointerException e) {
            // success
        }
    }

    /**
     * Test the method <code>removeSet</code>.
     * Test the case the keys contains null key, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveSetEntryInvalid() throws Exception {
        try {
            HashSet keys = new HashSet();
            keys.add(null);
            instance.removeSet(keys);
            fail("Should throw IllegalArgumentException if keys contains null key.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test the method <code>setEvictionStrategy</code>.
     * Test the case the strategy is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testSetEvictionStrategyStrategyNull() throws Exception {
        try {
            instance.setEvictionStrategy(null);
            fail("Should throw NullPointerException if strategy is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeLarger</code>.
     * Test the case the value is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveLargerValueNull() throws Exception {
        try {
            instance.removeLarger(null);
            fail("Should throw NullPointerException if value is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeOlder</code>.
     * Test the case the key is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveOlderKeyNull() throws Exception {
        try {
            instance.removeOlder(null);
            fail("Should throw NullPointerException if key is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeLike</code>.
     * Test the case the object is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveLikeObjectNull() throws Exception {
        try {
            instance.removeLike(null);
            fail("Should throw NullPointerException if object is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeByPattern</code>.
     * Test the case the regex is null, a NullPointerException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveByPatternRegexNull() throws Exception {
        try {
            instance.removeByPattern(null);
            fail("Should throw NullPointerException if regex is null.");
        } catch (NullPointerException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeByPattern</code>.
     * Test the case the regex is empty string, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveByPatternRegexEmpty() throws Exception {
        try {
            instance.removeByPattern("");
            fail("Should throw IllegalArgumentException if regex is empty string.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
    
    /**
     * Test the method <code>removeByPattern</code>.
     * Test the case the regex is invalid, an IllegalArgumentException should be thrown.
     *
     * @throws Exception if any error occurs during testing
     */
    public void testRemoveByPatternRegexInvalid() throws Exception {
        try {
            instance.removeByPattern("[ab");
            fail("Should throw IllegalArgumentException if regex is empty invalid.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
