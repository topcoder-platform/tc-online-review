/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * An implementation of CacheEvictionStrategy which uses BOF strategy for evictions.
 * This means that method getNextKeyToEvict() always returns the biggest object
 * existing in cache.
 * <p>This class is thread-safe.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class BOFCacheEvictionStrategy implements CacheEvictionStrategy {

    /**
     * Represents the map of all available keys and the associated size information for each entry that the
     * key represents. Basically it is a key:entry-size pair.
     * Each key will simply be the key used in the cache and the value will be the Long object
     * representing the size of the cache entry in bytes.
     * <p>Valid input Range
     * - keys can be any non null object reference.
     * - values must be Longs representing a >=0 number. Can not be null.
     */
    private final Map mapKey2Size = new HashMap();

    /**
     * Represents a maping of sizes to set of keys that represent these sizes. This allows the strategy to
     * keep track of all the keys that point to entries of the same size.
     * Each key will simply be the the Long object representing the size of the cache entry in bytes.
     */
    private final TreeMap mapSize2KeySet = new TreeMap();

    /**
     * Creates a new BOFCacheEvictionStrategy instance.
     */
    public BOFCacheEvictionStrategy() {
    }

    /**
     * Since the Biggest Object First strategy can not work without size information this implementation will
     * throw an UnsupportedOperationException.
     *
     * @param  key key to the cache entry.
     * @throws UnsupportedOperationException always.
     */
    public void notifyOfCachePut(Object key) {
        throw new UnsupportedOperationException("use notifyOfCachePut(Object key, CacheEntryInfo entryMetaData)"
                + " instead.");
    }

    /**
     * Notifies the eviction strategy implementation of a new entry in the cache. Here we provide the key as well
     * as the metadata about the entry (such as size in bytes of the entry in the cache).
     * <p>This implementation uses only size information. It ignores cacheEntryTime and lastAccessTime metadata.
     *
     * @param key key to the cache entry.
     * @param entryMetaData metadata of the entry represented by the key.
     */
    public synchronized void notifyOfCachePut(Object key, CacheEntryInfo entryMetaData) {
        Long size = new Long(entryMetaData.getSize());
        // put key-size in mapKey2Size.
        mapKey2Size.put(key, size);
        // put size-key in mapSize2KeySet.
        Set keySet = (Set) mapSize2KeySet.get(size);
        if (keySet == null) {
            // create set if it doesn't exist.
            keySet = new HashSet();
            mapSize2KeySet.put(size, keySet);
        }
        keySet.add(key);
    }

    /**
     * Notifies that an entry has been removed from the cache. Only the key is passed which is used as an alias
     * for the entry.
     *
     * @param key key to remove.
     */
    public synchronized void notifyOfCacheRemove(Object key) {
        Long size;
        // remove key-size from mapKey2Size.
        size = (Long) mapKey2Size.get(key);
        // if this key doesn't exist then exit
        if (size == null) {
            return;
        }
        mapKey2Size.remove(key);
        // remove size-key from mapSize2KeySet.
        Set keySet = (Set) mapSize2KeySet.get(size);
        if (keySet != null) {
            keySet.remove(key);
            // remove set when it becames empty.
            if (keySet.isEmpty()) {
                mapSize2KeySet.remove(size);
            }
        }
    }

    /**
     * This will return the key candidate of the next entry to be removed (evicted) from the cache.
     * It will return the key corresponding to the object with biggest size.
     *
     * @return the key of the entry to be evicted or null if set of keys is empty.
     */
    public synchronized Object getNextKeyToEvict() {
        // value to return.
        Object keyToEvict = null;
        // find keyToEvict.
        if (!mapSize2KeySet.isEmpty()) {
            Long biggestSize = (Long) mapSize2KeySet.lastKey();
            Set keys = (Set) mapSize2KeySet.get(biggestSize);
            Iterator it = keys.iterator();
            if (it.hasNext()) {
                keyToEvict = it.next();
            }
        }
        if (keyToEvict == null) {
            return null;
        }

        // if found key is not null then remove it.
        Long size;
        size = (Long) mapKey2Size.get(keyToEvict);
        mapKey2Size.remove(keyToEvict);
        Set keySet = (Set) mapSize2KeySet.get(size);
        keySet.remove(keyToEvict);
        // remove set when it becames empty.
        if (keySet.isEmpty()) {
            mapSize2KeySet.remove(size);
        }

        // return found key.
        return keyToEvict;
    }

    /**
     * Notifies that an entry has been accessed in the cache.
     * Does nothing, since BOF strategy does not use this information.
     *
     * @param key key of the entry to get.
     */
    public void notifyOfCacheGet(Object key) {
        // do nothing.
    }

    /**
     * Notifies that the cache has been cleared.
     */
    public synchronized void notifyOfCacheClear() {
        mapKey2Size.clear();
        mapSize2KeySet.clear();
    }

    /**
     * Initializes the state of the strategy based on input data which represents the current cache state.
     * <p>This implementation preserves transactional integrity. If any of the entries has incorrect type
     * then none of them will be added.
     *
     * @throws IllegalArgumentException if any of the entries in the input map are not of type CacheEntryInfo.
     *
     * @param  entriesMap a map containing the key:entry-meta-data collection.
     */
    public synchronized void init(Map entriesMap) {
        // this maps store new contents of mapKey2Size and mapSize2Key
        Map newMapKey2Size = new HashMap();
        Map newMapSize2KeySet = new TreeMap();

        // iterate through the set of keys.
        for (Iterator it = entriesMap.keySet().iterator(); it.hasNext();) {
            Object key = it.next();
            Object info = entriesMap.get(key);
            if (!(info instanceof CacheEntryInfo)) {
                // throw IllegalArgumentException.
                throw new IllegalArgumentException("values of entriesMap should have type CacheEntryInfo.");
            }
            Long size = new Long(((CacheEntryInfo) info).getSize());
            // add key-size pair to maps.
            // put key-size in mapKey2Size.
            newMapKey2Size.put(key, size);
            // put size-key in mapSize2KeySet.
            Set set = (Set) newMapSize2KeySet.get(size);
            if (set == null) {
                // create set if it doesn't exist.
                set = new HashSet();
                newMapSize2KeySet.put(size, set);
            }
            set.add(key);
        }

        // if all data is valid then we clear old maps and put new values to them
        mapKey2Size.clear();
        mapKey2Size.putAll(newMapKey2Size);
        mapSize2KeySet.clear();
        mapSize2KeySet.putAll(newMapSize2KeySet);
    }
}
