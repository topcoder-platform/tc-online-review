/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import com.topcoder.util.config.ConfigManager;
import java.util.List;
import java.util.ArrayList;

/**
 * Provides unit tests for constructors of SimpleCache.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleCacheConstructorsTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private SimpleCache cache;

    /**
     * Configuration Manager instance.
     */
    private ConfigManager cm = ConfigManager.getInstance();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SimpleCacheConstructorsTestCase.class);
    }

    /**
     * Set up.
     *
     * @throws Exception if any error occurs during setup.
     */
    protected void setUp() throws Exception {
        super.setUp();
        cache = new SimpleCache();
        cm.add("ConfigFile1.xml");
        cm.add("ConfigFile2.xml");
        cm.add("ConfigFile3.xml");
        cm.add("ConfigFile4.xml");
        cm.add("ConfigFile5.xml");
    }

    /**
     * Tear down.
     *
     * @throws Exception if any error occurs during teardown.
     */
    protected void tearDown() throws Exception {
        cm.removeNamespace("com.topcoder.util.cache.ConfigFile1");
        cm.removeNamespace("com.topcoder.util.cache.ConfigFile2");
        cm.removeNamespace("com.topcoder.util.cache.ConfigFile3");
        cm.removeNamespace("com.topcoder.util.cache.ConfigFile4");
        cm.removeNamespace("com.topcoder.util.cache.ConfigFile5");
        super.tearDown();
    }

    /**
     * Tests no-arg constructor.
     * <ul>
     * <li>Make sure that it successfully creates new instance.
     * <li>Check that methods getSize(), getByteSize(), getMaxCacheSize(), getMaxCacheCapacity() return
     *     expected values.
     * </ul>
     */
    public void testConstructor1() {
        cache = new SimpleCache();
        assertEquals("getSize() should return 0", 0, cache.getSize());
        assertEquals("getByteSize() should return 0", 0, cache.getByteSize());
        assertEquals("getMaxCacheSize() should return NO_MAX_SIZE",
                SimpleCache.NO_MAX_SIZE, cache.getMaxCacheSize());
        assertEquals("getMaxCacheCapacity() should return NO_MAX_CAPACITY",
                SimpleCache.NO_MAX_CAPACITY, cache.getMaxCacheCapacity());
    }

    /**
     * Tests constructor with namespace parameter.
     * <ul>
     * <li>Try to use ConfigFile1.xml as configuration file and make sure ctor doesn't throw any exceptions.
     * <li>Try to use ConfigFile2.xml (which contains empty namespace) as configuration file and make sure
     *     ctor doesn't throw any exception.
     * <li>Make sure NullPointerException is thrown when namespace is null.
     * <li>Make sure IllegalArgumentException is thrown when namespace is empty.
     * <li>Make sure IllegalArgumentException is thrown when parameters maxCacheSize, timeoutMS,
     *     maxCacheCapacity are &lt;= 0 (in ConfigFile3.xml).
     * <li>Make sure CacheInstantiationException is thrown when parameter CacheEvictionStrategy is invalid
     *     (in ConfigFile4.xml).
     * <li>Make sure that SimpleCache correctly reads configuration of custom CompressionHandler (in ConfigFile5).
     * </ul>
     */
    public void testConstructor2() throws Exception {
        new SimpleCache("com.topcoder.util.cache.ConfigFile1");

        new SimpleCache("com.topcoder.util.cache.ConfigFile2");

        try {
            new SimpleCache(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException ex) {
            // expected exception.
        }

        try {
            new SimpleCache("   ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        try {
            new SimpleCache("com.topcoder.util.cache.ConfigFile3");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        try {
            new SimpleCache("com.topcoder.util.cache.ConfigFile4");
            fail("CacheInstantiationException should be thrown.");
        } catch (CacheInstantiationException ex) {
            // expected exception.
        }

        new SimpleCache("com.topcoder.util.cache.ConfigFile5");
        assertTrue("Constructor TestCompressionHandler should be called",
                TestCompressionHandler.constructorInvoked());
    }

    /**
     * Tests third constructor of SimpleCache.
     * <ul>
     * <li>Make sure no exception is thrown if all reference parameters are null.
     * <li>Make sure getMaxCacheSize(), getTimeoutMS(), getMaxCacheCapacity() return values that were provided
     *     in constructor.
     * <li>Make sure IllegalArgumentException is thrown when maxCacheSize is &lt;= 0
     * <li>Make sure IllegalArgumentException is thrown when maxCacheCapacity is &lt;= 0
     * <li>Make sure IllegalArgumentException is thrown when timeoutMS is &lt;= 0
     * <li>Make sure CacheInstantiationException is thrown when compressionHandlerList contains
     *     element that is not CompressionHandler.
     * </ul>
     */
    public void testConstructor3() {
        cache = new SimpleCache(1, 2, null, 3, null, null, false);

        assertEquals("getMaxCacheSize() should return value provided in ctor.", 1, cache.getMaxCacheSize());
        assertEquals("getTimeoutMS() should return value provided in ctor.", 2, cache.getTimeoutMS());
        assertEquals("getMaxCacheCapacity() should return value provided in ctor.", 3, cache.getMaxCacheCapacity());

        try {
            new SimpleCache(-1, 1, null, 1, null, null, false);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        try {
            new SimpleCache(1, -1, null, 1, null, null, false);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        try {
            new SimpleCache(1, 1, null, -1, null, null, false);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }

        List list = new ArrayList();
        list.add(new Object());
        try {
            new SimpleCache(1, 1, null, 1, null, list, true);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException ex) {
            // expected exception.
        }
    }
}

