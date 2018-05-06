package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case tests the CacheEvictionStrategy implementations.
 * It's more thorough than the functional tests.
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyCacheEvictionStrategyTestCase extends TestCase {

    /**
     * tests the first in first out removal strategy
     */
    public void testFIFO() {
        FIFOCacheEvictionStrategy strategy =
            new FIFOCacheEvictionStrategy();
        
        strategy.notifyOfCachePut("a");
        assertEquals("Keys should be returned in FIFO order",
                     "a",
                     strategy.getNextKeyToEvict());
        
        strategy.notifyOfCachePut("b");
        assertEquals("Keys should be returned in FIFO order",
                     "b",
                     strategy.getNextKeyToEvict());
        
        strategy.notifyOfCachePut("c");
        strategy.notifyOfCachePut("d");
        strategy.notifyOfCachePut("e");
        strategy.notifyOfCacheGet("c");
        assertEquals("Keys should be returned in FIFO order",
                     "c",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order",
                     "d",
                     strategy.getNextKeyToEvict());

        strategy.notifyOfCacheClear();

        strategy.notifyOfCachePut("f");
        strategy.notifyOfCachePut("g");
        strategy.notifyOfCachePut("h");
        strategy.notifyOfCacheGet("g");
        strategy.notifyOfCacheGet("f");
        strategy.notifyOfCacheRemove("g");
        assertEquals("Keys should be returned in FIFO order",
                     "f",
                     strategy.getNextKeyToEvict());
        String h = (String) strategy.getNextKeyToEvict();
        if (!h.equals("h")){
             assertEquals("Keys should be returned in FIFO order",
                          "h",
                          strategy.getNextKeyToEvict());
        }
    }

    /**
     * tests the least recently used removal strategy
     */

    public void testLRU() {
        LRUCacheEvictionStrategy strategy =
            new LRUCacheEvictionStrategy();

        strategy.notifyOfCachePut("a");
        assertEquals("Keys should be returned in LRU order",
                     "a",
                     strategy.getNextKeyToEvict());
        
        strategy.notifyOfCachePut("b");
        assertEquals("Keys should be returned in LRU order",
                     "b",
                     strategy.getNextKeyToEvict());
        
        strategy.notifyOfCachePut("c");
        strategy.notifyOfCachePut("d");
        strategy.notifyOfCachePut("e");
        strategy.notifyOfCacheGet("c");
        assertEquals("Keys should be returned in LRU order",
                     "d",
                     strategy.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order",
                     "e",
                     strategy.getNextKeyToEvict());

        strategy.notifyOfCacheClear();

        strategy.notifyOfCachePut("f");
        strategy.notifyOfCachePut("g");
        strategy.notifyOfCachePut("h");
        strategy.notifyOfCacheGet("g");
        strategy.notifyOfCacheGet("f");
        strategy.notifyOfCacheRemove("g");
        assertEquals("Keys should be returned in LRU order",
                     "h",
                     strategy.getNextKeyToEvict());
        String f = (String) strategy.getNextKeyToEvict();
        if (!f.equals("f")){
             assertEquals("Keys should be returned in LRU order",
                          "f",
                          strategy.getNextKeyToEvict());
        }
    }

    /**
     * runs the tests
     */
    public static Test suite() {
        return new TestSuite(AccuracyCacheEvictionStrategyTestCase.class);
    }
}
