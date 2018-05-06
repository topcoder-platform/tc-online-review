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
 * Accuracy tests for the BOFCacheEvictionStrategy class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class BOFCacheEvictionStrategyAccuracyTest extends TestCase {

    /**
     * a BOFCacheEvictionStrategy instance for test.
     */
    private BOFCacheEvictionStrategy strategy = null;

    /**
     * stuff used to test the strategy
     */
    String football;
    long fbTime;
    CacheEntryInfo fbcei;
    String bar;
    long barTime;
    CacheEntryInfo barcei;
    String barbeq;
    long barbeqTime;
    CacheEntryInfo barbeqcei;
    
    /**
     * Sets up the environment
     */
    public void setUp() {
        strategy = new BOFCacheEvictionStrategy();
        football = "football";
        fbTime = new GregorianCalendar().getTimeInMillis();
        fbcei = new CacheEntryInfo(football.getBytes().length,
                fbTime, fbTime);
        // to get different time
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        bar = "bar";
        barTime = new GregorianCalendar().getTimeInMillis();
        barcei = new CacheEntryInfo(bar.getBytes().length, barTime,
                barTime);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        barbeq = "barbeq";
        barbeqTime = new GregorianCalendar().getTimeInMillis();
        barbeqcei = new CacheEntryInfo(barbeq.getBytes().length,
                barbeqTime, barbeqTime);
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
    public void testNotifyOfCachePutObjectCacheEntryInfo() {
        strategy.notifyOfCachePut(football, fbcei);
        strategy.notifyOfCachePut(bar, barcei);
        strategy.notifyOfCachePut(barbeq, barbeqcei);
        // check order
        assertEquals("Keys should be returned in BOF order", "football",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "barbeq",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "bar",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCacheRemove() method
     */
    public void testNotifyOfCacheRemove() {
        strategy.notifyOfCachePut(football, fbcei);
        strategy.notifyOfCachePut(bar, barcei);
        strategy.notifyOfCachePut(barbeq, barbeqcei);
        strategy.notifyOfCacheRemove(football);
        // check order
        assertEquals("Keys should be returned in BOF order", "barbeq",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "bar",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCacheGet() method
     */
    public void testNotifyOfCacheGet() {
        strategy.notifyOfCachePut(football, fbcei);
        strategy.notifyOfCachePut(bar, barcei);
        strategy.notifyOfCachePut(barbeq, barbeqcei);
        strategy.notifyOfCacheGet(barbeq);
        strategy.notifyOfCacheGet(bar);
        // check order
        assertEquals("Keys should be returned in BOF order", "football",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "barbeq",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "bar",
                strategy.getNextKeyToEvict());
    }

    /**
     * Tests init() method
     */
    public void testInit() {
        Map tmpMap = new HashMap();
        tmpMap.put(football, fbcei);
        tmpMap.put(bar, barcei);
        tmpMap.put(barbeq, barbeqcei);

        strategy.init(tmpMap);
        // check order
        assertEquals("Keys should be returned in BOF order", "football",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "barbeq",
                strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in BOF order", "bar",
                strategy.getNextKeyToEvict());
    }
}
