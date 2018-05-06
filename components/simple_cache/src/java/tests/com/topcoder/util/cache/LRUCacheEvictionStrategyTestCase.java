/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Map;
import java.util.HashMap;

/**
 * Provides unit test cases for LRUCacheEvictionStrategy class.
 * Tests functionality added in version 2.0 of this component.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class LRUCacheEvictionStrategyTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private LRUCacheEvictionStrategy lru = new LRUCacheEvictionStrategy();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(LRUCacheEvictionStrategyTestCase.class);
    }

    /**
     * Tests constructor.
     * <ul>
     * <li>Make sure it doesn't throw any exception and initially LRUCacheEvictionStrategy doesn't contain
     *     any keys.
     * </ul>
     */
    public void testConstructor() {
        LRUCacheEvictionStrategy obj = new LRUCacheEvictionStrategy();
        assertNull("getNextKeyToEvict() should return null.", obj.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheGet.
     *
     * <ul>
     * <li>Make sure call to this method changes order of eviction.
     * </ul>
     */
    public void testNotifyOfCacheGet() {
        lru.notifyOfCachePut("abc");
        lru.notifyOfCachePut("def");
        lru.notifyOfCacheGet("abc");
        assertEquals("Keys should be returned in LRU order.", "def", lru.getNextKeyToEvict());
        assertEquals("Keys should be returned in LRU order.", "abc", lru.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCachePut(Object key).
     *
     * <ul>
     * <li>Make sure added key will be returned by getNextKeyToEvict().
     * </ul>
     */
    public void testNotifyOfCachePut1() {
        lru.notifyOfCachePut("xyz");
        assertEquals("getNextKeyToEvict() should return added key.", "xyz", lru.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCachePut(Object key, CacheEntryInfo entryMetaData).
     *
     * <ul>
     * <li>Make sure that added key is stored in interal data structures and then can be retrieved by
     * getNextKeyToEvict() method.
     * </ul>
     */
    public void testNotifyOfCachePut2() {
        lru.notifyOfCachePut("123", new CacheEntryInfo(1, 2, 3));
        assertEquals("getNextKeyToEvict() should return added key.", "123", lru.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheRemove.
     *
     * <ul>
     * <li>Make sure call to this method with not-existing key doesn't lead to exception.
     * <li>Make sure removed key will not be returned by getNextKeyToEvict().
     * </ul>
     */
    public void testNotifyOfCacheRemove() {
        lru.notifyOfCacheRemove("abc");
        // nothing should happen

        lru.notifyOfCachePut("def");
        lru.notifyOfCacheRemove("def");
        assertNull("Entry should be removed.", lru.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheClear.
     * <ul>
     * <li>Make sure this method deletes all entries.
     * </ul>
     */
    public void testNotifyOfCacheClear() {
        lru.notifyOfCachePut("abc");
        lru.notifyOfCachePut("def");
        lru.notifyOfCacheClear();
        assertNull("Cache should be cleared.", lru.getNextKeyToEvict());
    }

    /**
     * Tests method getNextKeyToEvict.
     * <ul>
     * <li>Make sure this method returns keys in LRU order.
     * </ul>
     */
    public void testGetNextKeyToEvict() {
        lru.notifyOfCachePut("abc");
        lru.notifyOfCachePut("DEF");
        lru.notifyOfCachePut("ghi");
        lru.notifyOfCachePut("JKL");
        lru.notifyOfCacheGet("DEF");
        lru.notifyOfCacheGet("abc");
        assertEquals("getNextKeyToEvict should return keys in LRU order.", "ghi", lru.getNextKeyToEvict());
        assertEquals("getNextKeyToEvict should return keys in LRU order.", "JKL", lru.getNextKeyToEvict());
        assertEquals("getNextKeyToEvict should return keys in LRU order.", "DEF", lru.getNextKeyToEvict());
        assertEquals("getNextKeyToEvict should return keys in LRU order.", "abc", lru.getNextKeyToEvict());
    }

    /**
     * Tests method init.
     *
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown when entriesMap contains values of incorrect type and
     * check transactional integrity.
     * <li>Make sure that keys existed in cache before init are cleared and new keys are retrieved in
     * correct order (sorted by last access time).
     * <li>Make sure data is cleared only when argument is valid.
     * </ul>
     */
    public void testInit() {
        Map entriesMap = new HashMap();
        entriesMap.put("a", new CacheEntryInfo(1, 2, 3));
        entriesMap.put("b", "incorrect value");
        entriesMap.put("c", new CacheEntryInfo(4, 5, 6));
        try {
            lru.init(entriesMap);
            fail("IllegalArgumentException should be thrown when entriesMap contains values of incorrect type.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNull("No key should be returned.", lru.getNextKeyToEvict());

        for (int i = 0; i < 10; ++i) {
            lru.notifyOfCachePut("test" + i, new CacheEntryInfo(i, i, i));
        }

        entriesMap.clear();
        for (int i = 0; i < 10; ++i) {
            entriesMap.put("newTest" + i, new CacheEntryInfo(123, 456, 9 - i));
        }

        lru.init(entriesMap);
        for (int i = 9; i >= 0; --i) {
            assertEquals("getNextKeyToEvict() should return keys in correct order.",
                "newTest" + i, lru.getNextKeyToEvict());
        }
        assertNull("getNextKeyToEvict() should return null with empty key set.", lru.getNextKeyToEvict());

        entriesMap.clear();
        entriesMap.put("abc", new CacheEntryInfo(1, 2, 3));
        lru.init(entriesMap);
        entriesMap.clear();
        entriesMap.put("def", new Object());
        try {
            lru.init(entriesMap);
            fail("lru.init should throw IAE when some map's value is not type of CacheEntryInfo");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNotNull(lru.getNextKeyToEvict());
    }
}
