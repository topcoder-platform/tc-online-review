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
 * Provides unit test cases for FIFOCacheEvictionStrategy class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class FIFOCacheEvictionStrategyTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private FIFOCacheEvictionStrategy fifo;

    /**
     * <p>Returns the test suite for this class.</p>
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(FIFOCacheEvictionStrategyTestCase.class);
    }

    /**
     * Set up.
     *
     * @throws Exception if any error occurs.
     */
    protected void setUp() throws Exception {
        super.setUp();
        fifo = new FIFOCacheEvictionStrategy();
    }

    /**
     * Tests constructor.
     * <ul>
     * <li>Make sure it doesn't throw any exception and initially FIFOCacheEvictionStrategy doesn't contain
     *     any keys.
     * </ul>
     */
    public void testConstructor() {
        FIFOCacheEvictionStrategy obj = new FIFOCacheEvictionStrategy();
        assertNull("getNextKeyToEvict() should return null.", obj.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheGet.
     * <ul>
     * <li>Make sure that call to this method doesn't change FIFO eviction order.
     * </ul>
     */
    public void testNotifyOfCacheGet() {
        fifo.notifyOfCachePut("abc");
        fifo.notifyOfCachePut("ghi");
        fifo.notifyOfCachePut("def");
        fifo.notifyOfCacheGet("ghi");
        fifo.notifyOfCacheGet("def");
        fifo.notifyOfCacheGet("abc");
        assertEquals("Call to notifyOfCacheGet shouldn't change FIFO eviction order",
            "abc", fifo.getNextKeyToEvict());
        assertEquals("Call to notifyOfCacheGet shouldn't change FIFO eviction order",
            "ghi", fifo.getNextKeyToEvict());
        assertEquals("Call to notifyOfCacheGet shouldn't change FIFO eviction order",
            "def", fifo.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCachePut(Object key).
     * <ul>
     * <li>Make sure items are evicted in FIFO order.
     * </ul>
     */
    public void testNotifyOfCachePut1() {
        fifo.notifyOfCachePut("1");
        assertEquals("Entries should be evicted in FIFO order", "1", fifo.getNextKeyToEvict());
        fifo.notifyOfCachePut("2");
        fifo.notifyOfCachePut("3");
        fifo.notifyOfCachePut("4");
        assertEquals("Entries should be evicted in FIFO order", "2", fifo.getNextKeyToEvict());
        fifo.notifyOfCachePut("5");
        fifo.notifyOfCachePut("6");
        fifo.notifyOfCachePut("7");
        assertEquals("Entries should be evicted in FIFO order", "3", fifo.getNextKeyToEvict());
        assertEquals("Entries should be evicted in FIFO order", "4", fifo.getNextKeyToEvict());
        assertEquals("Entries should be evicted in FIFO order", "5", fifo.getNextKeyToEvict());
        assertEquals("Entries should be evicted in FIFO order", "6", fifo.getNextKeyToEvict());
        fifo.notifyOfCachePut("8");
        assertEquals("Entries should be evicted in FIFO order", "7", fifo.getNextKeyToEvict());
        assertEquals("Entries should be evicted in FIFO order", "8", fifo.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCachePut(Object key, CacheEntryInfo entryMetaData).
     * <ul>
     * <li>Make sure that added key is stored in internal data structures and then can be retrieved by
     *     getNextKeyToEvict() method.
     * </ul>
     */
    public void testNotifyOfCachePut2() {
        fifo.notifyOfCachePut("123", new CacheEntryInfo(1, 2, 3));
        assertEquals("getNextKeyToEvict() should return added key.", "123", fifo.getNextKeyToEvict());
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
        fifo.notifyOfCacheRemove("abc");
        // nothing should happen

        fifo.notifyOfCachePut("def");
        fifo.notifyOfCacheRemove("def");
        assertNull("Entry should be removed.", fifo.getNextKeyToEvict());
    }

    /**
     * Tests method getNextKeyToEvict.
     *
     * <ul>
     * <li>Make sure this method returns key in FIFO order.
     * </ul>
     */
    public void testGetNextKeyToEvict() {
        fifo.notifyOfCachePut("abc");
        fifo.notifyOfCachePut("DEF");
        assertEquals("Keys should be returned in FIFO order.", "abc", fifo.getNextKeyToEvict());
        fifo.notifyOfCachePut("ghi");
        fifo.notifyOfCachePut("JKL");
        fifo.notifyOfCachePut("mno");
        assertEquals("Keys should be returned in FIFO order.", "DEF", fifo.getNextKeyToEvict());
        fifo.notifyOfCacheRemove("JKL");
        assertEquals("Keys should be returned in FIFO order.", "ghi", fifo.getNextKeyToEvict());
        assertEquals("Keys should be returned in FIFO order.", "mno", fifo.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheClear.
     * <ul>
     * <li>Make sure this method deletes all entries.
     * </ul>
     */
    public void testNotifyOfCacheClear() {
        fifo.notifyOfCachePut("abc");
        fifo.notifyOfCachePut("def");
        fifo.notifyOfCacheClear();
        assertNull("Cache should be cleared.", fifo.getNextKeyToEvict());
    }

    /**
     * Tests method init.
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown when entriesMap contains values of incorrect type and
     *     check transactional integrity.
     * <li>Make sure that keys existed in cache before init are cleared and new keys are retrieved in
     *     correct order (sorted by cache entry time).
     * <li>Make sure data is cleared only when argument is valid.
     * </ul>
     */
    public void testInit() {
        Map entriesMap = new HashMap();
        entriesMap.put("a", new CacheEntryInfo(1, 2, 3));
        entriesMap.put("b", "incorrect value");
        entriesMap.put("c", new CacheEntryInfo(4, 5, 6));
        try {
            fifo.init(entriesMap);
            fail("IllegalArgumentException should be thrown when entriesMap contains values of incorrect type.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNull("No key should be returned.", fifo.getNextKeyToEvict());

        for (int i = 0; i < 10; ++i) {
            fifo.notifyOfCachePut("test" + i, new CacheEntryInfo(i, i, i));
        }

        entriesMap.clear();
        for (int i = 0; i < 10; ++i) {
            entriesMap.put("newTest" + i, new CacheEntryInfo(123, 9 - i, 456));
        }

        fifo.init(entriesMap);
        for (int i = 9; i >= 0; --i) {
            assertEquals("getNextKeyToEvict() should return keys in correct order.",
                "newTest" + i, fifo.getNextKeyToEvict());
        }
        assertNull("getNextKeyToEvict() should return null with empty key set.", fifo.getNextKeyToEvict());

        entriesMap.clear();
        entriesMap.put("abc", new CacheEntryInfo(1, 2, 3));
        fifo.init(entriesMap);
        entriesMap.clear();
        entriesMap.put("def", new Object());
        try {
            fifo.init(entriesMap);
            fail("fifo.init should throw IAE when some map's value is not type of CacheEntryInfo");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNotNull(fifo.getNextKeyToEvict());
    }
}
