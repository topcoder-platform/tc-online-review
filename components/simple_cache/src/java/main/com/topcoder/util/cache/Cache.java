/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.util.Map;
import java.util.Set;
import java.util.Iterator;

/**
 * This is the basic contract for a cache. This contract deals with the basics of cache manipulation.
 *
 * @author  WishingBone, srowen, AleaActaEst, rem
 * @version 2.0
 * @since   1.0
 */
public interface Cache {

    /**
     * Look up the for value with specified key in the cache.
     *
     * @param  key the specified key.
     * @return value if found in cache, null otherwise.
     * @throws NullPointerException if the input key is null.
     * @throws CacheException if any internal error occurs. Any exception caught should be chained to
     *         this one.
     */
    Object get(Object key);

    /**
     * Puts value with its key into the cache. If value is null, it acts as if remove value with
     * specified key from the cache was invoked.
     *
     * @param  key the key for the value.
     * @param  value the value.
     * @throws NullPOinterException if the input key is null.
     * @throws CacheException if any internal error occurs. Any exception caught should be chained to
     *         this one.
     */
    void put(Object key, Object value);

    /**
     * A bulk operation of entering key:value pairs into the cache. Each entry in the input map will be added
     * to the cache.
     * <p>It is allowed for the cacheEntry to be null. It this case cache instance will do nothing.
     *
     * @param  cacheEntry a Map of key:value pairs to be put into the cache.
     * @throws NullPointerException if any of the keys are null.
     * @throws CacheException if any internal error occurs. Any exception caught should be chained to
     *         this one.
     * @since  2.0
     */
    void put(Map cacheEntry);

    /**
     * Removes value with specified key from the cache.
     *
     * @param  key the specified key.
     * @return value if found in cache, null otherwise.
     * @throws NullPointerException if the input key is null.
     * @throws CacheException if any internal error occurs. Any exception caught should be chained to
     *         this one.
     */
    Object remove(Object key);

    /**
     * Clears the cache.
     */
    void clear();

    /**
     * Returns an unmodifiable Set containing all keys currently in the cache.
     * Updates to the cache do not affect this Set.
     * Note that a key's value may be reclaimed at any time, so subsequent calls to
     * get() with keys in this Set may return null.
     *
     * @return unmodifiable Set containing all keys in the cache.
     * @since  1.0.1
     */
    Set keySet();

    /**
     * Returns the iterator over all the cache values.
     *
     * @return iterator over collection of values in cache.
     * @since  2.0
     */
    public Iterator values();

    /**
     * Returns the number of items currently in the cache.
     *
     * @return number of entries in the cache.
     * @since  2.0
     */
    long getSize();

    /**
     * Removes all entries in the cache that are specified by the keys Set. This is basically a bulk remove
     * operation.
     * <p>It is allowed for the cacheEntry to be null. It this case cache instance will do nothing.
     *
     * @param  keys set representing the keys to be used in removing entries from the cache.
     * @throws NullPointerException if any of the keys are null.
     * @throws CacheException if any internal exception occurs. Any exception caught should be chained to
     *         this one.
     * @since  2.0
     */
    void removeSet(Set keys);

    /**
     * Represents the current total byte size of all the cache entries.
     *
     * @return current byte size of the cache.
     * @since  2.0
     */
    long getByteSize();
}
