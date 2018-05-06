/*
 * Copyright (C) 2005-2011 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.security.authenticationfactory;

import com.topcoder.util.cache.Cache;
import com.topcoder.util.cache.LRUCacheEvictionStrategy;
import com.topcoder.util.cache.SimpleCache;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;

/**
 * <p>
 * TimeoutCacheFactory class implements the CacheFactory interface, which will return a timeout
 * simple cache.  The createCache method will return a SimpleCache instance from the Simple Cache
 * component with the specified time out, SimpleCache.NO_MAX_SIZE, and LRUCacheEvictStrategy. If
 * the time out is 0, SimpleCache.NO_TIMEOUT will be used instead, indicating the entries in the
 * cache will never timeout.
 * </p>
 *
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 2.0
 */
public class TimeoutCacheFactory implements CacheFactory {
    /**
     * <p>
     * The required property.
     * </p>
     */
    private static final String TIMEOUT = "timeout";

    /**
     * <p>
     * Represents the timeout value for the cache, in milliseconds. The timeout should be
     * non-negative.
     * </p>
     */
    private long timeout = 0;

    /**
     * <p>
     * Create a TimeoutCacheFactory with the timeout.
     * </p>
     *
     * @param timeout the timeout value for the cache, in milliseconds.
     * @throws IllegalArgumentException if the timeout is negative.
     */
    public TimeoutCacheFactory(long timeout) {
        construct(timeout);
    }

    /**
     * <p>
     * Create a TimeoutCacheFactory instance from the given namespace.This construct will get value
     * from 'timeout' property of config file.
     * </p>
     *
     * @param namespace the namespace from which to load the timeout value.
     *
     * @throws NullPointerException if the namespace is null.
     * @throws IllegalArgumentException if the namespace is empty string.
     * @throws ConfigurationException if fail to load the timeout value, or the timeout value is invalid.
     */
    public TimeoutCacheFactory(String namespace) throws ConfigurationException {
        if (namespace == null) {
            throw new NullPointerException("namespace is null");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("namespace is empty string");
        }
        try {
            ConfigManager cm = ConfigManager.getInstance();
            String value = cm.getString(namespace, TIMEOUT);

            if (value == null) {
                throw new ConfigurationException("required value {timeout} of property missing");
            }

            // use value to construct TimeoutCacheFactory
            // will throw NumberFormatException if format is wrong
            // throw IllgalArgumentException if the value of timeout is negative
            construct(Long.parseLong(value));
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException("namespace " + namespace + " is known", une);
        } catch (NumberFormatException nfe) {
            throw new ConfigurationException("the value of timeout is not valid", nfe);
        } catch (IllegalArgumentException iae) {
            throw new ConfigurationException("the value of timeout should be >= 0", iae);
        }
    }

    /**
     * <p>
     * Return the created Cache instance. If the timeout variable is 0, SimpleCache.NO_TIMEOUT will
     * be used instead.
     * </p>
     *
     * @return the Cache instance of the specified timeout.
     */
    public Cache createCache() {
        long time = timeout;
        if (time == 0) {
            time = SimpleCache.NO_TIMEOUT;
        }

        return new SimpleCache(SimpleCache.NO_MAX_SIZE, time, new LRUCacheEvictionStrategy(),
            SimpleCache.NO_MAX_CAPACITY, null, null, false);
    }

    /**
     * <p>
     * Initialize <tt>this.timeout</tt>.
     * </p>
     * @param timeout the timeout value for the cache, in milliseconds.
     * @throws IllegalArgumentException if timeout is negative.
     */
    private void construct(long timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout " + timeout + " is negative.");
        }
        this.timeout = timeout;
    }
}