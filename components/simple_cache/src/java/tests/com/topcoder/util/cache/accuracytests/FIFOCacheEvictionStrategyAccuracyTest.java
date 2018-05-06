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
 * Accuracy tests for the FIFOCacheEvictionStrategy class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class FIFOCacheEvictionStrategyAccuracyTest extends TestCase {

    /**
     * a FIFOCacheEvictionStrategy instance for test.
     */
    private FIFOCacheEvictionStrategy strategy = null;

    /**
     * Sets up the environment
     */
    public void setUp() {
        strategy = new FIFOCacheEvictionStrategy();
    }

    /**
     * Tears down the environment
     */
    public void tearDown(){
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
        assertEquals("Keys should be returned in FIFO order", "foo",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "baz",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCacheRemove() method
     */
    public void testNotifyOfCacheRemove() {
        strategy.notifyOfCachePut("foo");
        strategy.notifyOfCachePut("bar");
        strategy.notifyOfCachePut("baz");
        // other stuff...
        strategy.notifyOfCacheGet("bar");
        strategy.notifyOfCacheGet("baz");
        // remove stuff
        strategy.notifyOfCacheRemove("foo");
        // check order
        assertEquals("Keys should be returned in FIFO order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "baz",
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
        strategy.notifyOfCacheGet("bar");
        strategy.notifyOfCacheGet("baz");
        // check order
        assertEquals("Keys should be returned in FIFO order", "foo",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "baz",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests init() method
     */
    public void testInit() {
        Map tmpMap = new HashMap();
        String foo = "foo";
        long time = new GregorianCalendar().getTimeInMillis();
        CacheEntryInfo foocei = new CacheEntryInfo(foo.getBytes().length, time,
                time);
        tmpMap.put(foo, foocei);
        
        String bar = "bar";
        CacheEntryInfo barcei = new CacheEntryInfo(bar.getBytes().length, time + 500,
                time + 500);
        tmpMap.put(bar, barcei);
        
        String baz = "baz";
        CacheEntryInfo bazcei = new CacheEntryInfo(baz.getBytes().length, time + 1000,
                time + 1000);
        tmpMap.put(baz, bazcei);

        strategy.init(tmpMap);
        // check order
        assertEquals("Keys should be returned in FIFO order", "foo",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "bar",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order", "baz",
                strategy.getNextKeyToEvict());
    }
}
