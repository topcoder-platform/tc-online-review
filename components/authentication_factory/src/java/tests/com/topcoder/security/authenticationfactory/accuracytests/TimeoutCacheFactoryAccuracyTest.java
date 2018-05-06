package com.topcoder.security.authenticationfactory.accuracytests;

import com.topcoder.security.authenticationfactory.TimeoutCacheFactory;
import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.SimpleCache;
import junit.framework.TestCase;


/**
 * Accuracy tests for TimeoutCacheFactory class.
 */
public class TimeoutCacheFactoryAccuracyTest extends TestCase {
    /**
     * The configuration namespace to test.
     */
    final String NAMESPACE = "com.topcoder.security.authenticationfactory.TimeoutCacheFactory";

    /**
     * The timeout factory instance to test.
     */
    private TimeoutCacheFactory factory = null;

    /**
     * Setup for each test case.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig.xml");
    }

    /**
     * Clean up for each test cases.
     *
     * @throws Exception to Junit.
     */
    protected void tearDown() throws Exception {
        TestUtil.clearAllNamespace();
    }

    /**
     * Test the constructor that receives long parameter.
     */
    public void testTimeoutCacheFactoryLong() {
        long timeout = 1000;
        factory = new TimeoutCacheFactory(timeout);

        Cache cache = factory.createCache();
        assertNotNull("Cannot get cache.", cache);
    }

    /**
     * Test the constructor that receives string parameter.
     *
     * @throws Exception to Junit.
     */
    public void testTimeoutCacheFactoryString() throws Exception {
        factory = new TimeoutCacheFactory(NAMESPACE);

        Cache cache = factory.createCache();
        assertNotNull("Cannot get cache.", cache);
    }

    /**
     * Test get cache from the factory, the factory is created using constructor with long parameter.
     */
    public void testGetCacheLong() {
        long timeout = 1000;
        factory = new TimeoutCacheFactory(timeout);

        SimpleCache cache = (SimpleCache) factory.createCache();
        assertEquals("Not the expected timeout value.", 1000, cache.getTimeoutMS());
    }

    /**
     * Test get cache from the factory, the factory is created using constructor with string parameter.
     */
    public void testGetCacheConfig() {
        long timeout = 1000;
        factory = new TimeoutCacheFactory(timeout);

        SimpleCache cache = (SimpleCache) factory.createCache();
        assertEquals("Not the expected timeout value.", 1000, cache.getTimeoutMS());
    }

    /**
     * Test the case the of creating no timeout cache.
     *
     * @throws Exception to Junit.
     */
    public void testZeroCache() throws Exception {
        long timeout = 0;
        factory = new TimeoutCacheFactory(timeout);

        SimpleCache cache = (SimpleCache) factory.createCache();
        assertEquals("Not the expected timeout value.", SimpleCache.NO_TIMEOUT, cache.getTimeoutMS());
    }

    /**
     * Test the case the configuration file contains zero for timeout value.
     *
     * @throws Exception to Junit.
     */
    public void testConfigZeroCache() throws Exception {
        TestUtil.loadConfigFile(TestUtil.ACCURACY_TEST_DIR + "testConfig_zero_cache.xml");

        factory = new TimeoutCacheFactory(NAMESPACE);

        SimpleCache cache = (SimpleCache) factory.createCache();
        assertEquals("Not the expected timeout value.", SimpleCache.NO_TIMEOUT, cache.getTimeoutMS());
    }
}
