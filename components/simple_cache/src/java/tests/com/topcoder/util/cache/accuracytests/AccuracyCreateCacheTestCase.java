package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case tests the SimpleCache constructors. It tests the
 * strategy parameter while functional tests don't.
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyCreateCacheTestCase extends TestCase {

    /** 
     * test constructor arguments
     */    
    public void testCreateCache() {
        SimpleCache simpleCache = new SimpleCache();
        assertEquals("Max size should default to no max",
                     SimpleCache.NO_MAX_SIZE,
                     simpleCache.getMaxCacheSize());
        assertEquals("Timeout should default to no timeout",
                     SimpleCache.NO_TIMEOUT,
                     simpleCache.getTimeoutMS());

        simpleCache =
            new SimpleCache(2, 365*24*60*60*1000, new FIFOCacheEvictionStrategy(), 5, null, null, false);
        assertEquals("Max size should match given size",
                     2,
                     simpleCache.getMaxCacheSize());
        assertEquals("Timeout should match given timeout",
                     365*24*60*60*1000,
                     simpleCache.getTimeoutMS());
                     
        //does it use FIFOCacheEvictionStrategy?.
        simpleCache.put("a","aa");
        simpleCache.put("b","bb");
        simpleCache.get("a");
        simpleCache.put("c","cc");
        assertNull("FIFO Strategy should be used.",
                   simpleCache.get("a") );

        simpleCache =
            new SimpleCache(2, 365*24*60*60*1000, new LRUCacheEvictionStrategy(), 5, null, null, false);
                     
        //does it use LRUCacheEvictionStrategy?.
        simpleCache.put("a","aa");
        simpleCache.put("b","bb");
        simpleCache.get("a");
        simpleCache.put("c","cc");
        assertNull("LRU Strategy should be used.",
                   simpleCache.get("b") );
    }

    /** 
     * Suite to run tests
     */    
    public static Test suite() {
        return new TestSuite(AccuracyCreateCacheTestCase.class);
    }
}
