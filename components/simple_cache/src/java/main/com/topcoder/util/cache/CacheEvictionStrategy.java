/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.util.Map;

/**
 * CacheEvictionStrategy decides which entry to evict when cache is full. Such a strategy is synched with
 * the underlying cache through notifications of operations on the actual cache itself. Thus if a cache has
 * a new element inserted then the eviction strategy would be informed of such an operation so that it can
 * keep track of the state of the cache.
 * <p>To keep things simple only the keys are reported to the eviction strategy. Additionally some
 * metadata (CacheEntryInfo) about the entry can also be passed to the strategy. This would contain info
 * like the size of the underlying entry (which the key represents) in bytes which might be useful to the strategy.
 *
 * @author  WishingBone, AleaActaEst, rem
 * @version 2.0
 * @since   1.0
 */
public interface CacheEvictionStrategy {

    /**
     * Notifies that an entry has been put into the cache. Only the key is passed which is used as an alias
     * for the entry.
     * <p>Exception handling:<br>
     * The keys are assumed to be controlled by the cache and thus already valid. No exception handling is needed.
     * @param key key of the object that was put into the cache.
     */
    void notifyOfCachePut(Object key);

    /**
     * Notifies the eviction strategy implementation of a new entry in the cache. Here we provide the key as well
     * as the metadata about the entry (such as size in bytes of the entry in the cache).
     * <p>Exception handling:<br>
     * The keys and the metadata are assumed to be controlled by the cache and thus already valid. No
     * exception handling is needed.
     *
     * @param key key of the cache entry.
     * @param entryMetaData entry metadata information.
     * @since 2.0
     */
    void notifyOfCachePut(Object key, CacheEntryInfo entryMetaData);

    /**
     * Notifies that an entry has been removed from the cache. Only the key is passed which is used as an alias
     * for the entry.
     * <p>Exception handling:<br>
     * The keys are assumed to be controlled by the cache and thus already valid. No exception handling
     * is needed.
     *
     * @param key key of the cache entry.
     */
    void notifyOfCacheRemove(Object key);

    /**
     * Returns the key candidate of the next entry to be removed (evicted) from the cache.
     *
     * @return key of the entry to be evicted or null if set of keys is empty.
     */
    Object getNextKeyToEvict();

    /**
     * Notifies that an entry has been accessed in the cache. Only the key is passed which is used as an alias
     * for the entry.
     * <p>Exception handling:<br>
     * The keys are assumed to be controlled by the cache and thus already valid. No exception handling
     * is needed.
     *
     * @param key key of the entry accessed in the cache.
     */
    void notifyOfCacheGet(Object key);

    /**
     * Notifies that the cache has been cleared.
     */
    void notifyOfCacheClear();

    /**
     * This method can be used to initialize the eviction strategy with current information in the cache.
     * This is useful when strategies are plugged-in into a cache which already has entries.
     * It is expected that the key to this map is the actual key used in the cache. It is expected that the
     * value for this map would be the actual EntryInfo which will give some metadata information about an
     * existing entry.
     *
     * @param  entriesMap contains mapping from keys to CacheEntryInfo objects.
     * @throws IllegalArgumentException if any of the entries are not of type CacheEntryInfo.
     * @throws NullPointerException if the passed in map is null.
     * @since  2.0
     */
    void init(Map entriesMap);
}
