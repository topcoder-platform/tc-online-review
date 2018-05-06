/**
 * Copyright (C) 2005, TopCoder, Inc. All Rights Reserved
 */
package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import junit.framework.TestCase;

/**
 * Accuracy tests for the LRUCacheEvictionStrategy class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class LRUCacheEvictionStrategyAccuracyTest extends TestCase {

    /**
     * a LRUCacheEvictionStrategy instance for test.
     */
    private LRUCacheEvictionStrategy strategy = null;

    /**
     * Sets up the environment
     */
    public void setUp() {
        strategy = new LRUCacheEvictionStrategy();
    }

    /**
     * Tears down the environment
     */
    public void tearDown() {
        strategy = null;
    }

    /**
     * Tests notifyOfCachePut() method
     */
    public void testNotifyOfCachePutObject() {
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // check order
        assertEquals("Keys should be returned in LRU order", "foo",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "baz",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCacheRemove() method
     */
    public void testNotifyOfCacheRemove() {
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // remove stuff
        strategy.notifyOfCacheRemove("foo");
        // check order
        assertEquals("Keys should be returned in LRU order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "baz",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCacheGet() method
     */
    public void testNotifyOfCacheGet() {
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // get stuff...
        strategy.notifyOfCacheGet("baz");
        strategy.notifyOfCacheGet("bar");
        // check order
        assertEquals("Keys should be returned in LRU order", "foo",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "baz",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "bar",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests init() method
     */
    public void testInit() {
        Map tmpMap = new HashMap();
        String foo = "foo";
        long time = new GregorianCalendar().getTimeInMillis();
        
        String bar = "bar";
        CacheEntryInfo barcei = new CacheEntryInfo(foo.getBytes().length, time,
                time + 500);
        tmpMap.put(bar, barcei);
        
        String baz = "baz";
        CacheEntryInfo bazcei = new CacheEntryInfo(foo.getBytes().length, time,
                time + 1000);
        tmpMap.put(baz, bazcei);

        CacheEntryInfo foocei = new CacheEntryInfo(foo.getBytes().length, time,
                time + 5000 );
        tmpMap.put(foo, foocei);
        strategy.init(tmpMap);
        // check order
        assertEquals("Keys should be returned in LRU order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "baz",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order", "foo",
                strategy.getNextKeyToEvict());

    }
}
