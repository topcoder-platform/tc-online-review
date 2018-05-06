package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>This test case tests the cache's <code>get()</code> method,
 * as well as other cache functionality:</p>
 * <ul>
 *  <li>maximum size limit</li>
 *  <li>object timeout</li>
 *  <li>memory management</li>
 * </ul>
 *
 * <p>See the separate <code>AccuracyConcurrencyTestCase</code> for tests
 * of multi-threaded behavior.</p>
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyGetObjectFromCacheTestCase extends TestCase {

    /** 
     * tests simple get function
     */
    public void testGet() {
        SimpleCache cache = new SimpleCache();
        assertNull("Cache should return null for non-existent keys",
                   cache.get("foo1"));
        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        cache.put("foo3", "bar3");
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
    }

    /** 
     * tests the get function when the cache is at max max size
     */
    public void testMaxSize() {
        SimpleCache cache =
            new SimpleCache(2, SimpleCache.NO_TIMEOUT, new FIFOCacheEvictionStrategy(), 96 , null, null, false);
        cache.put("foo1", "bar1");
        cache.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));
        cache.put("foo3", "bar3");
        assertNull("Cache should evict objects in FIFO order by default",
                   cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
    }

    /** 
     * tests the timeout for a cache
     */
    public void testTimeout() {
        SimpleCache cache =
            new SimpleCache(SimpleCache.NO_MAX_SIZE, 1000, new FIFOCacheEvictionStrategy(), 96 , null, null, false);
        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));

        try {
            Thread.sleep(1000 + 5000 + 500); // thread cleans up timed out entries every 5 seconds
        } catch(InterruptedException ie) {
            // continue - shouldn't happen anyway
        }

        cache.put("foo2", "bar2");
        assertNull("Cache should timeout entries",
                   cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));

        cache =
            new SimpleCache(SimpleCache.NO_MAX_SIZE, 60*1000, new FIFOCacheEvictionStrategy(), 96 , null, null, false);
        cache.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added",
                     "bar1",
                     cache.get("foo1"));

        try {
            Thread.sleep(5000+500); // thread cleans up timed out entries every 5 seconds
        } catch(InterruptedException ie) {
            // continue - shouldn't happen anyway
        }

        cache.put("foo2", "bar2");
        assertEquals("Cache should not timeout entries",
                   "bar1",
                   cache.get("foo1"));
        assertEquals("Cache should return objects that are added",
                     "bar2",
                     cache.get("foo2"));
    }

    /**
     * runs these tests
     */
    public static Test suite() {
        return new TestSuite(AccuracyGetObjectFromCacheTestCase.class);
    }
}
