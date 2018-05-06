/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

/**
 * This is a MetaData bean for a cache entry. This is a simple, immutable, data transfer object which
 * holds information about an entry in the cache.
 *
 * @author  AleaActaEst, rem
 * @version 2.0
 * @since   2.0
 */
public class CacheEntryInfo {

    /**
     * Represents the size of this entry (value) in bytes. Set in the constuctor.
     * <p>Access Details
     * - Initilaized in the ctor.
     * - Once initialized it is never modifed.
     * - Accessed only through the getSize() method.
     * <p>Valid input Range
     * - size must be >= 0
     */
    private long size;

    /**
     * Represents the time (in ms) when this entry was put into the cache.
     * <p>Access Details
     * - Initilaized in the ctor.
     * - Once initialized it is never modifed.
     * - Accessed only through the getCacheEntryTime() method.
     * <p>Valid input Range
     * - Can not be < 0
     */
    private long cacheEntryTime;

    /**
     * Represents the last time that this entry was accessed. This means that a get(...) operation was done on
     * the cache on this entry at the time of lastAccessTime. This includes only user actions.
     * This will have to be updated for every get done on the cache.
     * <p>Access Details
     * - Initilaized in the ctor.
     * - Accessed only through the getLastAccessTime() method.
     * <p>Valid input Range
     * - Can not be < 0
     */
    private long lastAccessTime;

    /**
     * Creates an instance of CacheEntryInfo initialized with the input data.
     *
     * @param  size size of the entry in bytes.
     * @param  createTime creation time (cache entry) of this value.
     * @param  lastAccessTime last time this entry was accessed through cache API.
     * @throws IllegalArgumentException if any input parameter is < 0
     */
    public CacheEntryInfo(long size, long createTime, long lastAccessTime) {
        if (size < 0) {
            throw new IllegalArgumentException("'size' can not be < 0");
        }

        if (createTime < 0) {
            throw new IllegalArgumentException("'createTime' can not be < 0");
        }

        if (lastAccessTime < 0) {
            throw new IllegalArgumentException("'lastAccessTime' can not be < 0");
        }

        this.size = size;
        this.cacheEntryTime = createTime;
        this.lastAccessTime = lastAccessTime;
    }

    /**
     * Returns the size metadata which represents the size of the entry in bytes.
     *
     * @return size of the entry.
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the created timestamp metadata which represents the time when this entry was placed in
     * the cache.
     *
     * @return creation time of the entry.
     */
    public long getCacheEntryTime() {
        return cacheEntryTime;
    }

    /**
     * Returns the last access timestamp metadata which represents the time when this entry last accessed
     * (through the public cache API).
     *
     * @return time when this entry was last accessed.
     */
    public long getLastAccessTime() {
        return lastAccessTime;
    }
}
