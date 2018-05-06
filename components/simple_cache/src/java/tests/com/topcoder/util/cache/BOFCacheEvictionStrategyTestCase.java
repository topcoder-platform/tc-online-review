/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

/**
 * Provides unit test cases for BOFCacheEvictionStrategy class.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class BOFCacheEvictionStrategyTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private BOFCacheEvictionStrategy bof;

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(BOFCacheEvictionStrategyTestCase.class);
    }

    /**
     * Set up.
     *
     * @throws Exception if some error occurs during setup.
     */
    protected void setUp() throws Exception {
        super.setUp();
        bof = new BOFCacheEvictionStrategy();
    }

    /**
     * Tests constructor.
     * <ul>
     * <li>Make sure it doesn't throw any exception and initially BOFCacheEvictionStrategy doesn't contain
     *     any keys.
     * </ul>
     */
    public void testConstructor() {
        BOFCacheEvictionStrategy obj = new BOFCacheEvictionStrategy();
        assertNull("getNextKeyToEvict() should return null.", obj.getNextKeyToEvict());
    }

    /**
     * Tests notifyOfCachePut(Object key).
     * <ul>
     * <li>Make sure this method throws UnsupportedOperationException.
     * </ul>
     */
    public void testNotifyOfCachePut1() {
        try {
            bof.notifyOfCachePut("123");
            fail("notifyOfCachePut(Object key) should throw UnsupportedOperationException.");
        } catch (UnsupportedOperationException ex) {
            // exptected exception.
        }
    }

    /**
     * Tests notifyOfCachePut(Object key, CacheEntryInfo entryMetaData).
     * <ul>
     * <li>Make sure that added key is stored in internal data structures and then can be retrieved by
     *     getNextKeyToEvict() method.
     * </ul>
     */
    public void testNotifyOfCachePut2() {
        bof.notifyOfCachePut("123", new CacheEntryInfo(1, 2, 3));
        assertEquals("getNextKeyToEvict() should return added key.", "123", bof.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheRemove.
     * <ul>
     * <li>Make sure that removing not-existing item does nothing.
     * <li>Make sure that after removing the key will not be returned by getNextKeyToEvict().
     * </ul>
     */
    public void testNotifyOfCacheRemove() {
        bof.notifyOfCacheRemove("123");
        // shouldn't throw any exception

        bof.notifyOfCachePut("123", new CacheEntryInfo(1, 2, 3));
        bof.notifyOfCacheRemove("123");
        assertNull("Key should be removed.", bof.getNextKeyToEvict());
    }

    /**
     * Tests method getNextKeyToEvict.
     * <ul>
     * <li>Make sure it returns null if key set is empty.
     * <li>Make sure it returns keys in correct order (from biggest to smallest size).
     * <li>Try random sequence of notifyOfCachePut and getNextKeyToEvict operations.
     * </ul>
     */
    public void testGetNextKeyToEvict() {
        assertNull("getNextKeyToEvict() should return null with empty key set.", bof.getNextKeyToEvict());

        bof.notifyOfCachePut("abc", new CacheEntryInfo(1, 6, 7));
        bof.notifyOfCachePut("def", new CacheEntryInfo(2, 0, 1));
        bof.notifyOfCachePut("ghi", new CacheEntryInfo(4, 5, 2));
        bof.notifyOfCachePut("jkl", new CacheEntryInfo(3, 4, 3));

        assertEquals("ghi - 4", "ghi", bof.getNextKeyToEvict());
        assertEquals("jkl - 3", "jkl", bof.getNextKeyToEvict());
        assertEquals("def - 2", "def", bof.getNextKeyToEvict());
        assertEquals("abc - 1", "abc", bof.getNextKeyToEvict());
        assertNull("getNextKeyToEvict() should return null with empty key set.", bof.getNextKeyToEvict());

        Random rand = new Random();
        HashSet[] sets = new HashSet[10]; // sizes will be in range 0..9
        for (int i = 0; i < 10; ++i) {
            sets[i] = new HashSet();
        }
        for (int test = 0; test < 10000; ++test) {
            if (test < 5 || rand.nextInt(2) == 1) {
                // notifyOfCachePut
                int size = rand.nextInt(10);
                String key = "test" + test;
                bof.notifyOfCachePut(key, new CacheEntryInfo(size, rand.nextInt(1000000), rand.nextInt(1000000)));
                sets[size].add(key);
            } else {
                // getNextKeyToEvict
                String key = (String) bof.getNextKeyToEvict();
                if (key == null) {
                    // check that key set is empty.
                    for (int i = 0; i < 10; ++i) {
                        assertTrue(sets[i].isEmpty());
                    }
                } else {
                    // check that key is correct.
                    int i = 9;
                    while (i >= 0 && sets[i].isEmpty()) {
                        --i;
                    }
                    assertTrue(i >= 0 && sets[i].remove(key));
                }
            }
        }
    }

    /**
     * Tests method notifyOfCacheGet.
     * <ul>
     * <li>Make sure calling this method doesn't affect the order of keys retrieving.
     * </ul>
     */
    public void testNotifyOfCacheGet() {
        bof.notifyOfCachePut("abc", new CacheEntryInfo(1, 6, 7));
        bof.notifyOfCachePut("def", new CacheEntryInfo(2, 0, 1));
        bof.notifyOfCachePut("ghi", new CacheEntryInfo(4, 5, 2));
        bof.notifyOfCachePut("jkl", new CacheEntryInfo(3, 4, 3));

        // notifyOfCacheGet should do nothing.
        bof.notifyOfCacheGet("mno");
        bof.notifyOfCacheGet("jkl");
        bof.notifyOfCacheGet("ghi");
        bof.notifyOfCacheGet("def");
        bof.notifyOfCacheGet("abc");

        assertEquals("ghi - 4", "ghi", bof.getNextKeyToEvict());
        assertEquals("jkl - 3", "jkl", bof.getNextKeyToEvict());
        assertEquals("def - 2", "def", bof.getNextKeyToEvict());
        assertEquals("abc - 1", "abc", bof.getNextKeyToEvict());
        assertNull("getNextKeyToEvict() should return null with empty key set.", bof.getNextKeyToEvict());
    }

    /**
     * Tests method notifyOfCacheClear.
     * <ul>
     * <li>Make sure getNextKeyToEvict() returns null after call to this method.
     * </ul>
     */
    public void testNotifyOfCacheClear() {
        for (int i = 0; i < 10; ++i) {
            bof.notifyOfCachePut("test" + i, new CacheEntryInfo(3, 2, 1));
        }
        bof.notifyOfCacheClear();
        assertNull("getNextKeyToEvict() should return null after call to notifyOfCacheClear()",
                bof.getNextKeyToEvict());
    }

    /**
     * Tests method init.
     * <ul>
     * <li>Make sure IllegalArgumentException is thrown when entriesMap contains values of incorrect type and
     *     check transactional integrity.
     * <li>Make sure that keys existed in cache before init are cleared and new keys are retrieved in
     *     correct order (from biggest to smallest size).
     * <li>Make sure data is cleared only when argument is valid.
     * </ul>
     */
    public void testInit() {
        Map entriesMap = new HashMap();
        entriesMap.put("a", new CacheEntryInfo(1, 2, 3));
        entriesMap.put("b", "incorrect value");
        entriesMap.put("c", new CacheEntryInfo(4, 5, 6));
        try {
            bof.init(entriesMap);
            fail("IllegalArgumentException should be thrown when entriesMap contains values of incorrect type.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNull("No key should be returned.", bof.getNextKeyToEvict());

        for (int i = 0; i < 10; ++i) {
            bof.notifyOfCachePut("test" + i, new CacheEntryInfo(i, i, i));
        }

        entriesMap.clear();
        for (int i = 0; i < 10; ++i) {
            entriesMap.put("newTest" + i, new CacheEntryInfo(i, 123, 456));
        }

        bof.init(entriesMap);
        for (int i = 9; i >= 0; --i) {
            assertEquals("getNextKeyToEvict() should return keys in correct order.",
                    "newTest" + i, bof.getNextKeyToEvict());
        }
        assertNull("getNextKeyToEvict() should return null with empty key set.", bof.getNextKeyToEvict());

        entriesMap.clear();
        entriesMap.put("abc", new CacheEntryInfo(1, 2, 3));
        bof.init(entriesMap);
        entriesMap.clear();
        entriesMap.put("def", new Object());
        try {
            bof.init(entriesMap);
            fail("bof.init should throw IAE when some map's value is not type of CacheEntryInfo");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
        assertNotNull(bof.getNextKeyToEvict());
    }
}
