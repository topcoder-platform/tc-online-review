/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.security.authenticationfactory;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

/**
 * <p>
 * The class contain testcase for test <code>TimeoutCacheFactory</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class TimeoutCacheFactoryTest extends TestCase {

    /**
     * <p>The filename to load config.</p>
     */
    private static final String LOCATION = "timeout.xml";

    /**
     * <p>The TimeoutCacheFactory instance.</p>
     */
    private TimeoutCacheFactory factory = null;

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(TimeoutCacheFactoryTest.class);
    }


    /**
     * <p>
     * test for void TimeoutCacheFactory(long) with negative timeout, will throw
     * IllegalArgumentException.</p>
     */
    public void testTimeoutCacheFactorylongIAE() {
        try {
            factory = new TimeoutCacheFactory(-1);
            fail("negative is not acceptable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * <p>
     * test for void TimeoutCacheFactory(long).</p>
     */
    public void testTimeoutCacheFactorylong() {
        long timeout = 100;
        // construct it with valid timeout, should not throw any exception.
        factory = new TimeoutCacheFactory(timeout);
        SimpleCache cache = (SimpleCache) factory.createCache();
        assertEquals("the timeout value of cache equals " + timeout, timeout, cache.getTimeoutMS());
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with null parameter,
     * should throw NullPointerException.</p>
     */
    public void testTimeoutCacheFactoryStringNPE() {
        try {
            factory = new TimeoutCacheFactory(null);
            fail("null parameter is not acceptable, should throw NullPointerException");
        } catch (NullPointerException npe) {
            // pass
        } catch (Exception e) {
            fail("should throw NullPointerException");
        }
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with null parameter,
     * should throw IllegalArgumentException.</p>
     */
    public void testTimeoutCacheFactoryStringIAE() {
        try {
            factory = new TimeoutCacheFactory(" ");
            fail("empty string is not accetable, should throw IllegalArgumentException");
        } catch (IllegalArgumentException iae) {
            // pass
        } catch (Exception e) {
            fail("should throw IllegalArgumentException");
        }
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with error config file.</p>
     */
    public void testTimeoutCacheFactoryStringCE() {
        try {
            final String namespace = "com.topcoder.security.authenticationfactory.ErrorTimeoutCacheFactory";
            ConfigManager cm = ConfigManager.getInstance();
            if (cm.existsNamespace(namespace)) {
                cm.removeNamespace(namespace);
            }
            // load a config with non-digit 'timeout', not acceptable
            cm.add(namespace, LOCATION, ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
            factory = new TimeoutCacheFactory(namespace);
            fail("timeouot must be integer, should throw ConfigurationException");
        } catch (ConfigurationException e) {
            // pass
        } catch (Exception e) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with error config file.</p>
     */
    public void testTimeoutCacheFactoryStringCE2() {
        try {
            final String namespace = "com.topcoder.security.authenticationfactory.Error2TimeoutCacheFactory";
            ConfigManager cm = ConfigManager.getInstance();
            if (cm.existsNamespace(namespace)) {
                cm.removeNamespace(namespace);
            }

            // load a config file that don't have the 'timeout' property
            cm.add(namespace, LOCATION, ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);
            factory = new TimeoutCacheFactory(namespace);
            fail("should throw ConfigurationException");
        } catch (ConfigurationException e) {
            // pass
        } catch (Exception e) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with non-exist namespace.</p>
     */
    public void testTimeoutCacheFactoryStringCE3() {
        try {
            // construct with non-exist namespace
            factory = new TimeoutCacheFactory("com.topcoder.security.authenticationfactory.blahblah");
            fail("should throw ConfigurationException");
        } catch (ConfigurationException e) {
            // pass
        } catch (Exception e) {
            fail("should throw ConfigurationException");
        }
    }

    /**
     * <p>test for void TimeoutCacheFactory(String) with right config file.</p>
     * @throws ConfigManagerException to JUnit
     * @throws ConfigurationException to JUnit
     */
    public void testTimeoutCacheFactoryString() throws ConfigManagerException, ConfigurationException {
        final String namespace = "com.topcoder.security.authenticationfactory.TimeoutCacheFactory";
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.existsNamespace(namespace)) {
            cm.removeNamespace(namespace);
        }
        cm.add(namespace, LOCATION, ConfigManager.CONFIG_MULTIPLE_XML_FORMAT);

        // should not throw any exception
        factory = new TimeoutCacheFactory(namespace);
        SimpleCache cache = (SimpleCache) factory.createCache();
        long timeout = 1000;
        // the timeout value in config is 1000
        assertEquals("the timeout value of cache equals " + timeout, timeout, cache.getTimeoutMS());

    }

    /**
     * <p>Test createCache() accuracy functionality.
     */
    public void testCreateCache() {
        int timeout = 1000;
        factory = new TimeoutCacheFactory(timeout);
        Cache cache = factory.createCache();

        assertNotNull("cache is created", cache);
    }
}