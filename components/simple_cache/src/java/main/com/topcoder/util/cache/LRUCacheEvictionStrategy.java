/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

/**
 * An implementation of CacheEvictionStrategy interface with
 * the LRU (Least Recently Used) strategy.
 *
 * @author  WishingBone, AleaActaEst, rem
 * @version 2.0
 * @since   1.0
 */
public class LRUCacheEvictionStrategy implements CacheEvictionStrategy {

    /**
     * Map from key to its id.
     */
    private HashMap mapKey2ID;

    /**
     * Map from id to its key.
     */
    private TreeMap mapID2Key;

    /**
     * The last id used.
     */
    private int lastID;

    /**
     * Constructor.
     */
    public LRUCacheEvictionStrategy() {
        mapKey2ID = new HashMap();
        mapID2Key = new TreeMap();
        lastID = 0;
    }

    /**
     * Notify of cache when "get" method happened on the specified key.
     * @param   key     the specified key on which "get" method happened.
     */
    public synchronized void notifyOfCacheGet(Object key) {
        Integer id = (Integer) mapKey2ID.get(key);
        if (id == null) {
            return;
        }
        mapKey2ID.remove(key);
        mapID2Key.remove(id);
        id = new Integer(lastID++);
        mapKey2ID.put(key, id);
        mapID2Key.put(id, key);
    }

    /** Notify of cache when "put" method happened on the specified key.
     * @param   key     the specified key on which "put" method happened.
     */
    public synchronized void notifyOfCachePut(Object key) {
        Integer id = new Integer(lastID++);
        // obtain a new id, put into both maps
        mapKey2ID.put(key, id);
        mapID2Key.put(id, key);
    }

    /**
     * The size of the entry is not relevant to this algorithm and thus this method simply delegates to
     * notifyOfCachePut(key) and disregard the size of the entry.
     *
     * @param key key of the cache entry.
     * @param entryMetaData entry metadata information.
     * @since 2.0
     */
    public void notifyOfCachePut(Object key, CacheEntryInfo entryMetaData) {
        notifyOfCachePut(key);
    }

    /**
     * Notify of cache when "remove" method happened on the specified key.
     * @param   key     the specified key on which "remove" method happened.
     */
    public synchronized void notifyOfCacheRemove(Object key) {
        Integer id;
        // remove from both maps
        id = (Integer) mapKey2ID.get(key);
        if (id != null) {
            mapKey2ID.remove(key);
            mapID2Key.remove(id);
        }
    }

    /**
     * Notify of cache when "clear" method happened.
     */
    public synchronized void notifyOfCacheClear() {
        // clear both maps
        mapKey2ID.clear();
        mapID2Key.clear();
        lastID = 0;
    }

    /**
     * Decides the next key to evict when the cache is full.
     * @return the key to evict.
     */
    public synchronized Object getNextKeyToEvict() {
        Integer id;
        Object key;
        // find the key with the smallest id
        // remove from both maps
        if (mapID2Key.isEmpty()) {
            return null;
        }
        id = (Integer) mapID2Key.firstKey();
        key = mapID2Key.get(id);
        mapID2Key.remove(id);
        mapKey2ID.remove(key);
        return key;
    }

    /**
     * Allows for this eviction strategy to be initialized. This is called when the strategy is being plugged-in
     * into a cache which already has entries.
     * <p>This implementation preserves transactional integrity. If any of the entries has incorrect type
     * then none of them will be added.
     *
     * @throws IllegalArgumentException if any of the values are not type of CacheEntryInfo.
     *
     * @param entriesMap a map containing the key:entry-meta-data collection.
     * @since 2.0
     */
    public synchronized void init(Map entriesMap) {
        // create mapping from last access times to keys.
        TreeMap mapAccessTime2KeySet = new TreeMap();
        for (Iterator it = entriesMap.keySet().iterator(); it.hasNext();) {
            Object key, info;
            key = it.next();
            info = entriesMap.get(key);
            // if info has incorrect type then throw exception. All data is cleared.
            if (!(info instanceof CacheEntryInfo)) {
                throw new IllegalArgumentException("values of entriesMap should have type CacheEntryInfo.");
            }
            Long time = new Long(((CacheEntryInfo) info).getLastAccessTime());
            // get keySet corresponding to given time and add key to this set.
            Set keySet = (Set) mapAccessTime2KeySet.get(time);
            if (keySet == null) {
                keySet = new HashSet();
                mapAccessTime2KeySet.put(time, keySet);
            }
            keySet.add(key);
        }

        // clear data
        mapKey2ID.clear();
        mapID2Key.clear();
        lastID = 0;

        // iterate through last access times in ascending order.
        for (Iterator timeIter = mapAccessTime2KeySet.keySet().iterator(); timeIter.hasNext();) {
            // get key set corresponding to currect time.
            Set keySet = (Set) mapAccessTime2KeySet.get(timeIter.next());
            // iterate through the set of keys and add each key.
            for (Iterator keyIter = keySet.iterator(); keyIter.hasNext();) {
                Object key = keyIter.next();
                Integer id = new Integer(lastID++);
                // obtain a new id, put into both maps
                mapKey2ID.put(key, id);
                mapID2Key.put(id, key);
            }
        }
    }
}
