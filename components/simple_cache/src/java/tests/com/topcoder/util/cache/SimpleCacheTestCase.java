/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Set;
import java.util.Iterator;
import java.util.Random;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Tests methods of SimpleCache.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleCacheTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private SimpleCache cache = new SimpleCache();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SimpleCacheTestCase.class);
    }

    /**
     * Tests the following methods: get, put, remove, clear, getMaxCacheSize(), getMaxCacheCapacity,
     * getTimeoutMS(), getSize(), getByteSize().
     */
    public void testMethods() {
        // check sizes and timeout.
        assertEquals("getTimeoutMS() should return NO_TIMEOUT", SimpleCache.NO_TIMEOUT, cache.getTimeoutMS());
        assertEquals("getMaxCacheSize() should return NO_MAX_SIZE", SimpleCache.NO_MAX_SIZE,
                cache.getMaxCacheSize());
        assertEquals("getMaxCacheCapacity() should return NO_MAX_CAPACITY", SimpleCache.NO_MAX_CAPACITY,
                cache.getMaxCacheCapacity());
        assertEquals("getSize() should return 0", 0, cache.getSize());
        assertEquals("getByteSize() should return 0", 0, cache.getByteSize());

        // put some objects in cache.
        List list = new ArrayList();
        long sizes[] = new long[101];
        for (int i = 1; i <= 100; ++i) {
            Object key, value;
            key = new Integer(i);
            value = key.toString();
            // add values to list to ensure that Garbage Collector will not delete them.
            list.add(value);
            // put object and remember it's byte size.
            long byteSizeBefore = cache.getByteSize();
            cache.put(key, value);
            long byteSizeAfter = cache.getByteSize();
            sizes[i] = byteSizeAfter - byteSizeBefore;
            // check getSize() method
            assertEquals("getSize() should return correct value.", i, cache.getSize());
        }

        // remove some objects
        long byteSize = cache.getByteSize();
        for (int i = 1; i <= 50; ++i) {
            Object key, value;
            key = new Integer(i);
            value = key.toString();
            // check get
            assertEquals("get(key) should return correct value.", value, cache.get(key));
            // try both remove(key) and put(key, null) methods.
            if (i % 2 == 1) {
                cache.remove(key);
            } else {
                cache.put(key, null);
            }
            byteSize -= sizes[i];
            // check getSize and getByteSize
            assertEquals("getSize() should return correct value.", 100 - i, cache.getSize());
            assertEquals("getByteSize() should return current value.", byteSize, cache.getByteSize());
        }

        // clear cache
        cache.clear();
        assertEquals("getSize() should return correct value.", 0, cache.getSize());
        assertEquals("getByteSize() should return current value.", 0, cache.getByteSize());
    }

    /**
     * Tests methods keySet and values.
     * <ul>
     * <li>Make sure keySet returns correct value.
     * <li>Make sure values returns current value.
     * </ul>
     */
    public void testKeySetAndValues() {
        String[] keys = {"ab", "cd", "ef", "gh"};
        String[] values = {"ij", "kl", "mn", "op"};

        Set setOfKeys, setOfValues;
        setOfKeys = new HashSet(Arrays.asList(keys));
        setOfValues = new HashSet(Arrays.asList(values));

        for (int i = 0; i < keys.length; ++i) {
            cache.put(keys[i], values[i]);
        }

        assertTrue("keySet() should return correct value", setsAreEqual(cache.keySet(), setOfKeys));
        Set set = new HashSet();
        for (Iterator it = cache.values(); it.hasNext();) {
            Object obj = it.next();
            assertFalse("Iterator should not return same value twice.", set.contains(obj));
            set.add(obj);
        }
        assertTrue("value() should return correct value", setsAreEqual(set, setOfValues));
    }

    /**
     * Tests method removeSet.
     */
    public void testRemoveSet() {
        cache.put("1", "a");
        cache.put("2", "b");
        cache.put("3", "c");
        cache.removeSet(new HashSet(Arrays.asList(new String[] {"1", "3", "xyz"})));
        assertTrue("removeSet should work correctly",
            setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new String[] {"2"}))));
    }

    /**
     * Tests method removeLarger.
     */
    public void testRemoveLarger() {
        cache.put("2", "b");
        cache.put("4", new byte[100000]);
        cache.put("5", new byte[1000000]);
        cache.removeLarger(new byte[1000]);
        assertTrue("removeLarger should work correctly",
            setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new String[] {"2"}))));
    }

    /**
     * Tests method removeOlder.
     *
     * @throws Exception if any error occurs.
     */
    public void testRemoveOlder() throws Exception {
        cache.put("2", "b");
        cache.put("6", "d");
        Thread.sleep(30);
        cache.put("7", "e");
        cache.put("8", "f");
        cache.removeOlder("7");

        assertTrue("removeOlder should work correctly",
            setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new String[] {"7", "8"}))));
    }

    /**
     * Tests method removeLike.
     */
    public void testRemoveLike() {
        cache.put("7", "e");
        cache.put("8", "f");
        cache.put(new Integer(9), new Integer(9));
        cache.put(new Long(10), new Long(10));
        cache.put("11", new Object());
        cache.removeLike(new Integer(11));
        assertTrue("removeLike should work correctly",
                setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new Object[] {
                    "7", "8", new Long(10), "11"}))));
        cache.removeLike(new Object());
        assertTrue("removeLike should work correctly", cache.keySet().size() == 0);
    }

    /**
     * Tests method removeByPattern.
     */
    public void testRemoveByPattern() {
        cache.put("1", new Object());
        cache.put("12", new Object());
        cache.put("123", new Object());
        cache.put("777", new Object());
        cache.put(new Integer(1), new Object());
        cache.put(new Integer(12), new Object());
        cache.put(new Integer(123), new Object());
        cache.put(new Integer(777), new Object());
        cache.removeByPattern("12.*");
        assertTrue("removeByPattern should work correctly",
                setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new Object[] {
                    "1", "777", new Integer(1), new Integer(777)}))));
    }

    /**
     * Tests method setEvictionStrategy.
     * <ul>
     * <li>Make sure that changing eviction strategy will cause change of object eviction order.
     * </ul>
     */
    public void testSetEvictionStrategy() {
        cache = new SimpleCache(3, SimpleCache.NO_TIMEOUT, null, SimpleCache.NO_MAX_CAPACITY,
                null, null, false);
        cache.put("a", "1");
        cache.put("b", "2");
        cache.put("c", "3");
        cache.put("d", "4");
        assertTrue("Entries should be evicted in FIFO order.",
                setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new String[] {"b", "c", "d"}))));

        cache.setEvictionStrategy(new LRUCacheEvictionStrategy());
        cache.get("c");
        cache.get("b");
        cache.get("d");
        cache.put("e", "5");
        assertTrue("Entries should be evicted in LRU order.",
                setsAreEqual(cache.keySet(), new HashSet(Arrays.asList(new String[] {"b", "d", "e"}))));
    }

    /**
     * Tests methods getCompressionFlag and setCompressionFlag.
     * <ul>
     * <li>Make sure that objects are actually compressed by comparing byte sizes.
     * </ul>
     */
    public void testCompression() {
        assertFalse("compreesionFlag should be false by default.", cache.getCompressionFlag());
        cache.setCompressionFlag(true);
        assertTrue("compressionFlag should change to true.", cache.getCompressionFlag());

        Random rand = new Random();
        int[] array = new int[100000];

        // try to put array in cache in compression mode and save the byte size of cache.
        cache.put(new Object(), array);
        long size1 = cache.getByteSize();

        cache.clear();
        cache.setCompressionFlag(false);
        assertFalse("compressionFlag should change to false.", cache.getCompressionFlag());

        // try to put array in cache when compressionFlag is false and save the byte size of cache.
        cache.put(new Object(), array);
        long size2 = cache.getByteSize();

        // compare size1 and size2.
        assertTrue("size1 < size2", size1 < size2);
    }

    /**
     * Utility method. Checks equality of two sets.
     *
     * @param  set1 first set.
     * @param  set2 second set.
     * @return true iif set1 and set2 contain same elements.
     */
    private boolean setsAreEqual(Set set1, Set set2) {
        if (set1.size() != set2.size()) {
            return false;
        }
        for (Iterator it = set1.iterator(); it.hasNext();) {
            if (!set2.contains(it.next())) {
                return false;
            }
        }
        return true;
    }
}

/**
 * CompressionHandler implementation for testing.
 */
class TestCompressionHandler implements CompressionHandler {

    /**
     * Contains true iif constructor was invoked at least once.
     */
    private static boolean ctorInvoked = false;

    /**
     * Checks that SimpleCache object correctly reads configuration from file ConfigFile5.xml.
     * If map passed to this constructor is not correct then it throws exception.
     *
     * @param  map map that contains configuration parameters.
     * @throws Exception if any error occurs.
     */
    public TestCompressionHandler(Map map) throws Exception {
        ctorInvoked = true;
        boolean throwException = false;
        if (map.size() != 3) {
            throwException = true;
        }
        if (!map.get("class").equals("com.topcoder.util.cache.TestCompressionHandler")) {
            throwException = true;
        }
        if (!map.get("StringParameter").equals("One_Value")) {
            throwException = true;
        }
        List list = (List) map.get("ListParameter");
        if (list.size() != 2) {
            throwException = true;
        }
        if (!list.get(0).equals("First_Value") || !list.get(1).equals("Second_Value")) {
            throwException = true;
        }
        if (throwException) {
            throw new Exception();
        }
    }

    /**
     * Checks that constructor of this class was called at least once.
     *
     * @return true iif constructor was called at least once
     */
    public static boolean constructorInvoked() {
        return ctorInvoked;
    }

    /**
     * Stub for compress method.
     *
     * @param  value object to compress.
     * @return compressed value.
     */
    public byte[] compress(Object value) {
        return new byte[0];
    }

    /**
     * Stud for decompress method.
     *
     * @param  compressedValue value to decompress.
     * @return decompressed value.
     */
    public Object decompress(byte[] compressedValue) {
        return new Object();
    }
}
