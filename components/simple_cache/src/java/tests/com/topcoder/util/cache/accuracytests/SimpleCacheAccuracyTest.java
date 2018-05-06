/**
 * Copyright (C) 2005, TopCoder, Inc. All Rights Reserved
 */
package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;

/**
 * Accuracy tests for the SimpleCache class.
 * 
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class SimpleCacheAccuracyTest extends TestCase {

    /**
     * A SimpleCache instance to test.
     */
    private SimpleCache sc = null;

    /**
     * valid namespace used to construct a SimpleCache instance.
     */
    private final String validNameSpace = "com.topcoder.util.cache.SimpleCache";

    /**
     * invalid namespace used to test the construct of SimpleCache.
     */
    private final String invalidNameSpace = "com.topcoder.util.cache";

    /**
     * valid MaxCacheSize used to construct a SimpleCache instance.
     */
    private final int validMaxCacheSize = 1000;

    /**
     * invalid MaxCacheSize used to test the construct of SimpleCache.
     */
    private final int invalidMaxCacheSize = -1000;

    /**
     * valid MaxCacheCapacity used to construct a SimpleCache instance.
     */
    private final long validMaxCacheCapacity = 10000000;

    /**
     * invalid MaxCacheCapacity used to test the construct of SimpleCache.
     */
    private final long invalidMaxCacheCapacity = -10000000;

    /**
     * valid TimeoutMS used to construct a SimpleCache instance.
     */
    private final long validTimeoutMS = 3600000;

    /**
     * invalid TimeoutMS used to test the construct of SimpleCache.
     */
    private final long invalidTimeoutMS = -3600000;

    /**
     * a MemoryUtilizationHandler instace used for test.
     */
    private final MemoryUtilizationHandler muh = new SimpleMemoryUtilizationHandler();

    /**
     * a CacheEvictionStrategy instace used for test.
     */
    private final CacheEvictionStrategy ces = new LRUCacheEvictionStrategy();

    /**
     * a compressionHandlerList used for test.
     */
    private final List compressionHandlerList = new ArrayList();

    /**
     * the configuration manager
     */
    private ConfigManager cm;

    /**
     * Sets up the environment
     */
    public void setUp() {
        // nothing to set up here.
    }

    /**
     * Tears down the environment
     */
    public void tearDown() {
        // nothing to tear down here
    }

    /**
     * Tests getting and  retrieving objects.
     */
    public void testPutAndGet() {
        sc = new SimpleCache(2, SimpleCache.NO_TIMEOUT,
                new FIFOCacheEvictionStrategy(), SimpleCache.NO_MAX_CAPACITY,
                muh, compressionHandlerList, false);
        assertNull("Cache should return null for non-existent keys", sc.get("foo1"));
        sc.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added", "bar1",
            sc.get("foo1"));
  }

    /**
     * Verifies the maximum size of the cache using FIFO cache strategy.
     */
    public void testMaxSizeWithFIFO() {
        sc = new SimpleCache(2, SimpleCache.NO_TIMEOUT,
                new FIFOCacheEvictionStrategy(), SimpleCache.NO_MAX_CAPACITY,
                muh, compressionHandlerList, false);
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));
        sc.put("foo3", "bar3");
        assertNull("Cache should evict objects in FIFO order by default",
                sc.get("foo1"));
        assertEquals("Cache should return objects that are added", "bar2",
                sc.get("foo2"));
    }

    /**
     * Verifies the maximum size of the cache using LRU cache strategy.
     */
    public void testMaxSizeWithLRU() {
        sc = new SimpleCache(2, SimpleCache.NO_TIMEOUT,
                new LRUCacheEvictionStrategy(), SimpleCache.NO_MAX_CAPACITY,
                muh, compressionHandlerList, false);
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));
        sc.put("foo3", "bar3");
        assertNull("Cache should evict objects in LRU order by default",
                sc.get("foo2"));
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));
    }

    /**
     * Verifies the maximum size of the cache using BOF cache strategy.
     */
    public void testMaxSizeWithBOF() {
        sc = new SimpleCache(2, SimpleCache.NO_TIMEOUT,
                new BOFCacheEvictionStrategy(), SimpleCache.NO_MAX_CAPACITY,
                muh, compressionHandlerList, false);
        sc.put("foo1", "bar21111");
        sc.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added", "bar21111",
                sc.get("foo1"));
        sc.put("foo3", "bar3");
        assertNull("Cache should evict objects in BOF order by default",
                sc.get("foo1"));
        assertEquals("Cache should return objects that are added", "bar2",
                sc.get("foo2"));
    }

    /**
     * Tests removing items from the cache.
     */
    public void testRemove() {
        sc = new SimpleCache();

        sc.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));
        sc.remove("foo1");
        assertNull("Removing keys should cause gets to return null", sc
                .get("foo1"));

        // shouldn't throw any exception
        sc.remove("foo1");

        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));
        assertEquals("Cache should return objects that are added", "bar2",
                sc.get("foo2"));
        sc.remove("foo1");
        assertEquals("Removing one key should not affect another", "bar2",
                sc.get("foo2"));
    }

    /**
     * Tests clearing out all the entries in cache.
     */
    public void testClear() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.clear();
        assertNull("Clear cache should cause all gets to return null", sc
                .get("foo1"));
        assertNull("Clear cache should cause all gets to return null", sc
                .get("foo2"));
        assertNull("Clear cache should cause all gets to return null", sc
                .get("foo3"));
    }

    /**
     * Verifies that objects are removed that have timed out.
     */
    public void testTimeout() {
        sc = new SimpleCache(SimpleCache.NO_MAX_SIZE, 1000,
                new FIFOCacheEvictionStrategy(), SimpleCache.NO_MAX_CAPACITY,
                muh, compressionHandlerList, false);
        sc.put("foo1", "bar1");
        assertEquals("Cache should return objects that are added", "bar1",
                sc.get("foo1"));

        try {
            Thread.sleep(1000 + 5000 + 500); // thread cleans up timed out
                                                // entries every 5 seconds
        } catch (InterruptedException ie) {
            // continue - shouldn't happen anyway
        }

        sc.put("foo2", "bar2");
        assertNull("Cache should timeout entries", sc.get("foo1"));
        assertEquals("Cache should return objects that are added", "bar2",
                sc.get("foo2"));
    }

    /**
     * Tests keySet() method.
     */
    public void testKeySet() {
        sc = new SimpleCache();

        Set keySet = sc.keySet();
        assertNotNull("Key set should never be null", keySet);
        assertTrue("Key set should be empty", keySet.isEmpty());

        try {
            keySet.add("something");
            fail("Key set should be unmodifiable");
        } catch (UnsupportedOperationException uoe) {
                        // good
        }

        sc.put("foo", "bar");
        assertTrue("Previous key set should not reflect cache changes", keySet.isEmpty());

        keySet = sc.keySet();
        assertTrue("Key set should have new key", keySet.size() == 1);
        assertTrue("Key set should have new key", keySet.contains("foo"));

        sc.put("baz", "boo");
        keySet = sc.keySet();
        assertTrue("Key set should contain both keys", keySet.size() == 2);
        assertTrue("Key set should contain both keys", keySet.contains("foo") && keySet.contains("baz"));

        sc.clear();
        keySet = sc.keySet();
        assertTrue("Key set should be empty after clear", keySet.isEmpty());
    }

    /**
     * Verifies that max size the cache should have.
     */
    public void testGetMaxCacheSize() {
        sc = new SimpleCache(validMaxCacheSize, validTimeoutMS,
                    ces, validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        assertEquals(validMaxCacheSize, sc.getMaxCacheSize());
    }

    /**
     * Verifies that timeout(in MS) the cache should have.
     */
    public void testGetTimeoutMS() {
        sc = new SimpleCache(validMaxCacheSize, validTimeoutMS,
                    ces, validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        assertEquals(validTimeoutMS, sc.getTimeoutMS());
    }

    /**
     * Test Values() method.Verifies that values the cache return.
     */
    public void testValues() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.put("foo4", "bar4");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6");
        
        Set toVerify = new HashSet();
        for (Iterator iter = sc.values(); iter.hasNext();) {
            String toadd = (String) iter.next();
            toVerify.add(toadd);
        }
        int has = 0;
        for (int i = 1; i < 7; i++) {
            if (toVerify.contains("bar" + i)) {
                has++;
            }
        }
        assertEquals(6, has);
    }

    /**
     * Verifies that size of the cache.
     */
    public void testGetSize() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.put("foo4", "bar4");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6");
        assertEquals(6, sc.getSize());
    }

    /**
     * Test RemoveSet() method.
     */
    public void testRemoveSet() {
        sc = new SimpleCache();
        for (int i = 0; i < 20; i++) {
            sc.put("foo" + i, "bar" + i);
        }
        assertEquals(20, sc.getSize());
        
        Set toRemove = new HashSet();
        for (int i = 0; i < 18; i++) {
            toRemove.add("foo" + i);
        }
        
        sc.removeSet(toRemove);
        assertEquals(2, sc.getSize());
        assertEquals("bar18", sc.get("foo18"));
        assertEquals("bar19", sc.get("foo19"));
    }

    /**
     * Test GetByteSize() method.Verifies that bytes size of the cache.
     */
    public void testGetByteSize() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.remove("foo1");
        
        assertEquals(muh.getObjectSize("bar2") + muh.getObjectSize("bar3"), sc.getByteSize());
    }

    /**
     * Test SetEvictionStrategy() method.Verifies that eviction strategy of the cache.
     */
    public void testSetEvictionStrategy() {
        sc = new SimpleCache(2, validTimeoutMS,
                    new FIFOCacheEvictionStrategy(), validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        sc.put("foo1", "bar1");
        sc.put("foo3", "bar3");
        sc.put("foo4", "bar4");
        assertNull("Cache should evict objects in FIFO order by default",
                sc.get("foo1"));
        CacheEvictionStrategy toSet = new LRUCacheEvictionStrategy();
        sc.setEvictionStrategy(toSet);
        sc.put("foo5", "bar5");
        sc.put("foo4", "bar4");
        //sc.get("foo4");
        sc.put("foo6", "bar6");
        assertNull("Clear cache should cause all gets to return null", sc
                .get("foo5"));
        assertEquals("bar4", sc.get("foo4"));
    }

    /**
     * Test GetMaxCacheCapacity() method.Verifies the max capacity of the cache in bytes.
     */
    public void testGetMaxCacheCapacity() {
        sc = new SimpleCache(validMaxCacheSize, validTimeoutMS,
                    ces, validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        assertEquals(validMaxCacheCapacity, sc.getMaxCacheCapacity());
    }

    /**
     * Test RemoveLarger() method.Verifies the result.
     */
    public void testRemoveLarger() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "123456");
        sc.put("foo3", "bar346678");
        sc.put("foo4", "bar4dfasffdsfasdfasdfddsfasdf");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6asfdsfasd");
        assertEquals(6, sc.getSize());
        sc.removeLarger("bar346678");
        assertEquals(4, sc.getSize());
    }

    /**
     * Test RemoveOlder() method.Verifies the result.
     */
    public void testRemoveOlder(){
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            // there will not be interruption.
        }
        sc.put("foo4", "bar4");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6");
        sc.removeOlder("foo4");
        assertNull("remove should cause the return is null", sc
                .get("foo1"));
        assertNull("remove should cause the return is null", sc
                .get("foo2"));
        assertNull("remove should cause the return is null", sc
                .get("foo3"));
        assertEquals("bar4", sc.get("foo4"));
        assertEquals("bar5", sc.get("foo5"));
        assertEquals("bar6", sc.get("foo6"));
    }

    /**
     * Test RemoveLike() method.Verifies the result.
     */
    public void testRemoveLike() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.put("foo4", "bar4");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6");
        assertEquals(6, sc.getSize());
        sc.removeLike("foo4");
        assertEquals(0, sc.getSize());
    }

    /**
     * Test RemoveByPattern() method.Verifies the result.
     */
    public void testRemoveByPattern() {
        sc = new SimpleCache();
        sc.put("foo1", "bar1");
        sc.put("foo2", "bar2");
        sc.put("foo3", "bar3");
        sc.put("foo4", "bar4");
        sc.put("foo5", "bar5");
        sc.put("foo6", "bar6");
        assertEquals(6, sc.getSize());
        sc.removeLike("foo*");
        assertEquals(0, sc.getSize());
    }

    /**
     * Test GetCompressionFlag() method.Verifies the result.
     */
    public void testGetCompressionFlag() {
        sc = new SimpleCache(validMaxCacheSize, validTimeoutMS,
                    ces, validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        assertEquals(false, sc.getCompressionFlag());
    }

    /**
     * Test SetCompressionFlag() method.Verifies the compressionflag of the cache.
     */
    public void testSetCompressionFlag() {
        sc = new SimpleCache(validMaxCacheSize, validTimeoutMS,
                    ces, validMaxCacheCapacity, muh,
                    compressionHandlerList, false);
        sc.setCompressionFlag(true);
        assertEquals(true, sc.getCompressionFlag());
    }

    /**
     * Test PutMap() method.Verifies the result.
     */
    public void testPutMap() {
        sc = new SimpleCache();
        Map toSet = new HashMap();
        toSet.put("foo1", "bar1");
        toSet.put("foo2", "bar2");
        sc.put(toSet);
        assertEquals("bar1", sc.get("foo1"));
        assertEquals("bar2", sc.get("foo2"));
    }
}
