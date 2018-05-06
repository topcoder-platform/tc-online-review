/*
 * Copyright (c) 2002-2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.Property;

/**
 * An implementation for the Cache interface.
 * This implementation can be configured through a configuration file, which allows the user to set the
 * following aspects of the cache:
 * <ul>
 *        <li> (a) maxCacheByteCapacity
 *        <li> (b) maxCacheSize
 *        <li> (c) Compresssion Handler List and the compression flag
 *        <li> (d) memoryUtilizationHandler
 *        <li> (e) Eviction strategy
 * </ul>
 * We also have the ability to set the CacheEvictionStrategy at run time and we have the following
 * additional removal functionality:
 * <ul>
 *        <li> (a) largest, oldest, class type based, and regular expression based removals
 * </ul>
 * This is a thread-safe implementation.
 *
 * @author  WishingBone, srowen, AleaActaEst, rem
 * @version 2.0
 * @since   1.0
 */
public class SimpleCache implements Cache {

    /**
     * Constant value for no max size for the cache.
     */
    public static final int NO_MAX_SIZE = Integer.MAX_VALUE;

    /**
     * Constant value for no timeout for the cache.
     */
    public static final long NO_TIMEOUT = Long.MAX_VALUE;

    /**
     * Represents the capacity value that signifies no upper limit on the byte size of the cache. This is the
     * default value of maxCacheByteCapacity member variable if no init value is provided by the user.
     */
    public static final long NO_MAX_CAPACITY = Long.MAX_VALUE;

    /**
     * Represents the default compression handler to be used if the compression handlers list is empty and
     * compression is flagged to be used (i.e. compressionFlag == true).
     */
    public static final CompressionHandler DEFAULT_COMPRESSION_HANDLER = new DefaultCompressionHandler();

    /**
     * Represents name of property that contains name for cache eviction strategy that this SimpleCache
     * should use.
     */
    private static final String PROPERTY_CACHE_EVICTION_STRATEGY = "CacheEvictionStrategy";

    /**
     * Represents name of property that contains maximum number of cache entries allowed.
     */
    private static final String PROPERTY_MAX_CACHE_SIZE = "MaxCacheSize";

    /**
     * Represents name of property that contains maximum allowed size of cache in bytes.
     */
    private static final String PROPERTY_MAX_CACHE_BYTE_CAPACITY = "MaxCacheByteCapacity";

    /**
     * Represents name of property that contains time after which cache entries will be removed.
     */
    private static final String PROPERTY_TIMEOUT_MS = "TimeoutMS";

    /**
     * Represents name of property that contains name of class implementing interface
     * MemoryUtilizationHandler. This class will be used to calculate cache capacity.
     */
    private static final String PROPERTY_MEMORY_UTILIZATION_HANDLER = "MemoryUtilizationHandler";

    /**
     * Represents name of property that contains cache's compression flag. This flag is signalling
     * if compression is applied to the entries.
     */
    private static final String PROPERTY_COMPRESSION_FLAG = "CompressionFlag";

    /**
     * Represents name of property that contains list of compression handlers that should be
     * applied to cache entries.
     */
    private static final String PROPERTY_COMPRESSION_HANDLERS = "CompressionHandlers";

    /**
     * Represents name of property that contains name of class which implements interface
     * CompressionHandler.
     */
    private static final String PROPERTY_CLASS = "class";

    /**
     * Represents the maximium allowed capacity of the cache in bytes. This means the collective byte size
     * of all the entries in the cache.
     */
    private long maxCacheByteCapacity;

    /**
     * The maximum number of entries allowed in the cache.
     */
    private long maxCacheSize;

    /**
     * Timeout value for the cache, in milliseconds. Expired objects are deleted by daemon thread.
     */
    private long timeoutMS;

    /**
     * Map from key to Entry objects. Each Entry object contains SoftReference to actual object.
     * Note that this is different from approach described in design, but this change is essential for
     * correct work of this class. This allowes daemon thread to subtract the size of object when it finds
     * cleared SoftReference, which is impossible otherwise.
     */
    private HashMap map = new HashMap();

    /**
     * Eviction strategy for the cache, used when there is no room for new entries.
     */
    private CacheEvictionStrategy evictionStrategy = null;

    /**
     * Represents the list of available compression handlers. Each of the handlers is capable of
     * compressing specific types of objects and thus this list allowes us to store all the necessary compression
     * handlers that we would need to utilize.
     */
    private List compressionHandlerList;

    /**
     * Represents a flag which signifies if compression should be applied to the cache entries or not.
     */
    private boolean compressionFlag = false;

    /**
     * Represents a utility which will be used by this cache to get the size of any object. This will be needed
     * when calculating the memory utilization of the cache.
     */
    private MemoryUtilizationHandler memoryUtilizationHandler;

    /**
     * Represents the current byte size of the cache. This variable is strictly used to prevent wasteful on-demand
     * calcultion of the cache size. Each time we have a cache operation such as put, remove, clear we update
     * this value automatically. This way we always have an uptodate cache size without expensive iteration.
     * This can never be negative.
     */
    private long currentByteSize = 0;

    /**
     * An object instance used as a mutex for SimpleCache and CacheCleanupThread instances.
     */
    private Object mutex = new Object();

    /**
     * Cleanup thread which runs every five second to check for and remove timeouted entries.
     * This thread also checks for cleared SoftReferences and changes currentByteSize accordingly.
     */
    private CacheCleanupThread cleanup;

    /**
     * Creates an instance of the SimpleCache with default initialization.
     */
    public SimpleCache() {
        maxCacheSize = NO_MAX_SIZE;
        maxCacheByteCapacity = NO_MAX_CAPACITY;
        timeoutMS = NO_TIMEOUT;
        evictionStrategy = new FIFOCacheEvictionStrategy();
        memoryUtilizationHandler = new SimpleMemoryUtilizationHandler();
        compressionHandlerList = new ArrayList();
        compressionHandlerList.add(DEFAULT_COMPRESSION_HANDLER);
        // always start cleanup thread
        cleanup = new CacheCleanupThread(this, mutex);
        cleanup.start();
    }

    /**
     * Creates an instance of the cache initialized from a configuration file. For example configuration file
     * look at docs/SimpleCacheConfig.xml
     *
     * @param  namespace namespace to be used when reading properties.
     * @throws IllegalArgumentException if maxCacheSize, timeoutMS, maxCacheCapacity are &lt;= 0.
     * @throws CacheInstantiationException if there are any issues with configuration. Any exceptions thrown
     *         from the underlying ConfigManager will be wrapped here.
     * @throws NullPointerException if namespace is null.
     * @throws IllegalArgumentException if namespace is empty.
     * @throws CacheInstantiationException if namespace is invalid.
     * @since  2.0
     */
    public SimpleCache(String namespace) {
        if (namespace == null) {
            throw new NullPointerException("'namespace' can not be null.");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("'namespace' can not be empty.");
        }

        // get ConfigManager instance.
        ConfigManager cm = ConfigManager.getInstance();

        if (!cm.existsNamespace(namespace)) {
            throw new CacheInstantiationException("namespace doesn't exist.");
        }

        try {
            String st;

            // set evictionStrategy.
            st = cm.getString(namespace, PROPERTY_CACHE_EVICTION_STRATEGY);
            if (st == null) {
                evictionStrategy = new FIFOCacheEvictionStrategy();
            } else {
                Class cl = Class.forName(st);
                Constructor ctor = cl.getConstructor(new Class[0]);
                evictionStrategy = (CacheEvictionStrategy) ctor.newInstance(new Object[0]);
            }

            // set maxCacheSize.
            st = cm.getString(namespace, PROPERTY_MAX_CACHE_SIZE);
            if (st == null) {
                maxCacheSize = NO_MAX_SIZE;
            } else {
                maxCacheSize = Long.parseLong(st);
            }

            // set maxCacheByteCapacity.
            st = cm.getString(namespace, PROPERTY_MAX_CACHE_BYTE_CAPACITY);
            if (st == null) {
                maxCacheByteCapacity = NO_MAX_CAPACITY;
            } else {
                maxCacheByteCapacity = Long.parseLong(st);
            }

            // set timeoutMS.
            st = cm.getString(namespace, PROPERTY_TIMEOUT_MS);
            if (st == null) {
                timeoutMS = NO_TIMEOUT;
            } else {
                timeoutMS = Long.parseLong(st);
            }

            // set memoryUtilizationHandler.
            st = cm.getString(namespace, PROPERTY_MEMORY_UTILIZATION_HANDLER);
            if (st == null) {
                memoryUtilizationHandler = new SimpleMemoryUtilizationHandler();
            } else {
                Class cl = Class.forName(st);
                Constructor ctor = cl.getConstructor(new Class[0]);
                memoryUtilizationHandler = (MemoryUtilizationHandler) ctor.newInstance(new Object[0]);
            }

            // set compressionFlag.
            st = cm.getString(namespace, PROPERTY_COMPRESSION_FLAG);
            compressionFlag = Boolean.valueOf(st).booleanValue();

            // setCompressionHandlerList.
            Property propCompressionHandlers = cm.getPropertyObject(namespace, PROPERTY_COMPRESSION_HANDLERS);
            if (propCompressionHandlers == null) {
                compressionHandlerList = null;
            } else {
                compressionHandlerList = new ArrayList();
                // get list of properties representing compression handlers.
                List list = propCompressionHandlers.list();
                for (Iterator it = list.iterator(); it.hasNext(); ) {
                    Property prop = (Property) it.next();
                    CompressionHandler compressionHandler = null;
                    // get name of class which should implement CompressionHandler interface.
                    String className = prop.getValue(PROPERTY_CLASS);
                    Class handlerClass = Class.forName(className);
                    Constructor ctor = handlerClass.getConstructor(new Class[] {Map.class});

                    // handle any class implementing CompressionHandler
                    Map parameters = new HashMap();
                    for (Iterator it2 = prop.list().iterator(); it2.hasNext(); ) {
                        Property p = (Property) it2.next();
                        String key = p.getName();
                        Object value;
                        String[] ss = p.getValues();
                        if (ss.length == 1) {
                            value = ss[0];
                        } else {
                            value = new ArrayList(Arrays.asList(ss));
                        }
                        parameters.put(key, value);
                    }

                    compressionHandler = (CompressionHandler) ctor.newInstance(new Object[] {parameters});
                    compressionHandlerList.add(compressionHandler);
                }
            }
        } catch (Exception ex) {
            throw new CacheInstantiationException("Can't create new SimpleCache instance", ex);
        }
        // check parameters
        if (maxCacheSize <= 0) {
            throw new IllegalArgumentException("'maxCacheSize' should be > 0");
        }
        if (maxCacheByteCapacity <= 0) {
            throw new IllegalArgumentException("'maxCacheCapacity' should be > 0");
        }
        if (timeoutMS <= 0) {
            throw new IllegalArgumentException("'timeoutMS' should be > 0");
        }
        // always start cleanup thread
        cleanup = new CacheCleanupThread(this, mutex);
        cleanup.start();
    }

    /**
     * Creates an instance of SimpleCache initialized with various inputs.
     * Any of the reference parameters can be a null since there are default values for them in the SimpleCache.
     * <p>The default values for parameters are the following.
     * <ul>
     * <li>evictionStrategy - new FIFOCacheEvictionStrategy()
     * <li>memoryUtilizationHandler - new SimpleMemoryUtilizationHandler()
     * </ul>
     * <p>If compressionFlag is false then compressionHandlerList parameter is ignored.
     * If compressionFlag is true and compressionHandlerList is null or empty, then it is set
     * to list containing one element - new DefaultCompressionHandler.
     * If compressionFlag is true and compressionHandlerList contains any elements then these
     * handlers are used.
     *
     * @param  maxCacheSize maximum cache size in terms of number of entries.
     * @param  timeoutMS timeout in milliseconds.
     * @param  evictionStrategy EvictionStrategy to be used.
     * @param  maxCacheCapacity maximum cache size in terms of bytes of memory.
     * @param  memoryUtilizationHandler utility to be used to calculate object size.
     * @param  compressionHandlerList list of compression handlers to be used.
     * @param  compressionFlag defines if compression is on or off.
     * @throws IllegalArgumentException if maxCacheSize, timeoutMS, maxCacheCapacity are &lt;= 0.
     * @throws IllegalArgumentException if compressionHandlerList contains null elements or objects that are not
     *         instances of CompressionHandler
     *
     * @since  2.0
     */
    public SimpleCache(long maxCacheSize, long timeoutMS, CacheEvictionStrategy evictionStrategy,
            long maxCacheCapacity, MemoryUtilizationHandler memoryUtilizationHandler,
            List compressionHandlerList, boolean compressionFlag) {
        // check parameters.
        if (maxCacheSize <= 0) {
            throw new IllegalArgumentException("'maxCacheSize' should be > 0");
        }
        if (timeoutMS <= 0) {
            throw new IllegalArgumentException("'timeoutMS' should be > 0");
        }
        if (maxCacheCapacity <= 0) {
            throw new IllegalArgumentException("'maxCacheCapacity' should be > 0");
        }

        this.maxCacheSize = maxCacheSize;
        this.timeoutMS = timeoutMS;
        if (evictionStrategy == null) {
            this.evictionStrategy = new FIFOCacheEvictionStrategy();
        }
        else {
            this.evictionStrategy = evictionStrategy;
        }
        this.maxCacheByteCapacity = maxCacheCapacity;
        if (memoryUtilizationHandler == null) {
            this.memoryUtilizationHandler = new SimpleMemoryUtilizationHandler();
        } else {
            this.memoryUtilizationHandler = memoryUtilizationHandler;
        }
        // set compressionFlag and compressionHandlerList
        this.compressionFlag = compressionFlag;
        this.compressionHandlerList = new ArrayList();
        if (compressionHandlerList != null) {
            for (Iterator it = compressionHandlerList.iterator(); it.hasNext(); ) {
                Object obj = it.next();
                if (obj == null || !(obj instanceof CompressionHandler)) {
                    throw new IllegalArgumentException("compressionHandlerList should contain non-null " +
                            "CompressionHandler instances only.");
                }
                this.compressionHandlerList.add((CompressionHandler) obj);
            }
        }
        if (this.compressionHandlerList.size() == 0) {
            this.compressionHandlerList.add(DEFAULT_COMPRESSION_HANDLER);
        }
        // always start cleanup thread
        cleanup = new CacheCleanupThread(this, mutex);
        cleanup.start();
    }

    /**
     * Look up the for value with specified key in the cache.
     * @param   key     the specified key.
     * @return          value if found in cache, null otherwise.
     * @throws  NullPointerException    when key is null.
     */
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException("'key' can not be null.");
        }
        synchronized (mutex) {
            Entry entry = (Entry) map.get(key);
            // if object with specified key doesn't exist return null.
            if (entry == null) return null;
            Object result = ((Reference) entry.getValue()).get();
            // result object might be null if JVM reclaims it, then we remove the key
            // and notify eviction strategy with "remove" and return null
            if (result == null) {
                currentByteSize -= entry.getSize();
                map.remove(key);
                evictionStrategy.notifyOfCacheRemove(key);
                return null;
            }
            // notify with "get"
            evictionStrategy.notifyOfCacheGet(key);
            // decompress value if entry is compressed.
            CompressionHandler ch = entry.getCompressionHandler();
            if (ch != null) {
                try {
                    result = ch.decompress((byte[]) result);
                } catch (Exception ex) {
                    throw new CacheException("Can't decompress object.", ex);
                }
            }
            return result;
        }
    }

    /**
     * Puts value with its key into the cache. If value is null,
     * it acts as if remove value with specified key from the cache.
     * <p>If size of value is greater then maximum allowed byte capacity of cache
     * then this object will not be added. This is the only case when put operation
     * fails. No exception is thrown in this situation.
     * <p>If value can be added to the cache but after this operation amount of
     * objects in cache or their total size will exceed limit then cache uses
     * CacheEvictionStrategy to remove some of existing objects until put operation
     * can be made.
     *
     * @param   key     the key for the value.
     * @param   value   the value.
     * @throws  NullPointerException    when key is null.
     * @throws  CacheException if any internal error occurs. Any exception caught will be chained to
     *         this one.
     */
    public void put(Object key, Object value) {
        synchronized (mutex) {
            // if value is null, just call remove
            if (value == null){
                remove(key);
                return;
            }
            if (key == null) {
                throw new NullPointerException("'key' can not be null.");
            }
            // compress value if needed.
            Object compressedValue = null;
            CompressionHandler chUsed = null;
            if (compressionFlag) {
                // try every compression handler until we find one that can compress object.
                for (int i = 0; i < compressionHandlerList.size(); ++i) {
                    chUsed = (CompressionHandler) compressionHandlerList.get(i);
                    try {
                        compressedValue = chUsed.compress(value);
                    } catch (CompressionException ex) {
                        throw new CacheException("compresion handler was unable to compress object.", ex);
                    } catch (Exception ex) {
                        compressedValue = null;
                    }
                    if (compressedValue != null) break;
                }
            }
            if (compressedValue == null) {
                chUsed = null;
            }
            // find size of new item.
            // For compressed value it's just size of byte array.
            // For not compressed value use memoryUtilizationHandler.
            Long size;
            if (compressedValue != null) {
                size = new Long(((byte[]) compressedValue).length);
            } else {
                try {
                    size = new Long(memoryUtilizationHandler.getObjectSize(value));
                } catch (MemoryUtilizationException ex) {
                    throw new CacheException("memoryUtilizationHandler has thrown exception.", ex);
                }
            }
            // if size of item is greater than maxCacheByteSize then we can't add it.
            if (size.longValue() > maxCacheByteCapacity) {
                return;
            }
            // delete objects until we can add new one
            while(map.size() > 0) {
                boolean continueDeleting = false;
                // check amount of objects in cache.
                if (map.size() + (map.containsKey(key)?0:1) > maxCacheSize) {
                    continueDeleting = true;
                }
                // check size of objects.
                if (currentByteSize + size.longValue() > maxCacheByteCapacity) {
                    continueDeleting = true;
                }
                if (!continueDeleting) {
                    break;
                }
                Object evictKey = evictionStrategy.getNextKeyToEvict();
                // if eviction strategy fails to determine next key to evict
                // then take any key.
                if (evictKey == null || !map.containsKey(evictKey)) {
                    evictKey = map.keySet().iterator().next();
                }
                Entry entry = (Entry) map.get(evictKey);
                currentByteSize -= entry.getSize();
                map.remove(evictKey);
                evictionStrategy.notifyOfCacheRemove(evictKey);
            }
            // just defensive programming.
            if (map.size() == 0) {
                currentByteSize = 0;
            }
            // calculate timestamp, check for overflow
            // put a SoftReference to the value and timestamp into the map
            // it might overwrite old value
            // notify with "put"
            long timesOutAt = NO_TIMEOUT - System.currentTimeMillis() - timeoutMS;
            if (timesOutAt <= 0) {
                timesOutAt = NO_TIMEOUT;
            }
            else {
                timesOutAt = NO_TIMEOUT - timesOutAt;
            }
            Entry entry = new Entry(new SoftReference((compressedValue == null) ? value : compressedValue),
                    timesOutAt, size.intValue(), chUsed, value.getClass().getName());
            map.put(key, entry);
            currentByteSize += entry.getSize();
            evictionStrategy.notifyOfCachePut(key, entry.getMetaData());
        }
    }

    /**
     * A bulk operation of entering key:value pairs into the cache. Each entry in the input map will be added
     * to the cache.
     * <p>It is allowed for the cacheEntry to be null. It this case cache instance will do nothing.
     *
     * @param  cacheEntryMap a Map of key:value pairs to be put into the cache.
     * @throws NullPointerException if any of the keys are null.
     * @throws CacheException if any internal error occurs. Any exception caught will be chained to
     *         this one.
     * @since  2.0
     */
    public void put(Map cacheEntryMap) {
        for (Iterator it = cacheEntryMap.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry mapEntry;
            mapEntry = (Map.Entry) it.next();
            Object key, value;
            key = mapEntry.getKey();
            value = mapEntry.getValue();
            if (key == null) {
                throw new IllegalArgumentException("'cacheEntryMap' should not contain null keys.");
            }
            if (value != null) put(key, value);
        }
    }

    /**
     * Removes value with specified key from the cache.
     * @param   key     the specified key.
     * @return          value if found in cache, null otherwise.
     * @throws  NullPointerException    when key is null.
     */
    public Object remove(Object key) {
        if (key == null) {
            throw new NullPointerException("'key' can not be null.");
        }
        synchronized (mutex) {
            Entry entry = (Entry) map.get(key);
            // key might not exist in the map
            if (entry == null) {
                return null;
            }
            // remove the key and notify eviction strategy with "remove"
            map.remove(key);
            evictionStrategy.notifyOfCacheRemove(key);
            // decrease currentByteSize
            currentByteSize -= entry.getSize();
            Object result = ((SoftReference) entry.getValue()).get();
            // object might be null if JVM reclaims it, just return null
            if (result == null) {
                return null;
            }
            // decompress value if entry is compressed.
            CompressionHandler ch = entry.getCompressionHandler();
            if (ch != null) {
                try {
                    result = ch.decompress((byte[]) result);
                } catch (Exception ex) {
                    throw new CacheException("Can't decompress object.", ex);
                }
            }
            return result;
        }
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        synchronized (mutex) {
            map.clear();
            currentByteSize = 0;
            // notify with "clear"
            evictionStrategy.notifyOfCacheClear();
        }
    }

    /**
     * Returns an unmodifiable Set containing all keys currently in the cache.
     * Updates to the cache do not affect this Set.
     * Note that a key's value may be reclaimed at any time, so subsequent calls to
     * get() with keys in this Set may return null.
     *
     * @return unmodifiable Set containing all keys in the cache
     * @since  1.0.1
     */
    public Set keySet() {
        synchronized (mutex) {
            Set keySet;
            keySet = map.keySet();
            if (keySet == null || keySet.isEmpty()) {
                return Collections.EMPTY_SET;
            }
            Set keySetCopy = new HashSet((int)(keySet.size() / 0.75) + 1);
            // initial capacity is just large enough to avoid rehashes
            keySetCopy.addAll(keySet);
            return Collections.unmodifiableSet(keySetCopy);
        }
    }

    /**
     * Gets maxCacheSize of the cache.
     * @return          max number of entries for this cache.
     */
    public long getMaxCacheSize() {
        return maxCacheSize;
    }

    /**
     * Gets timeoutMS of the cache.
     * @return          timeout value for the cache.
     */
    public long getTimeoutMS() {
        return timeoutMS;
    }

    /**
     * Finalize method.
     */
    public void finalize() {
        cleanup.halt();
    }

    /**
     * This method returns an Iterator over all the values/entries present in the cache. The entries are
     * returned to the user in the very same manner that they were put into the cache. This means that if the
     * entry is compressed in the cache it will be uncompressed when returned from the Iterator's next() method.
     * <p>The iterator returned is not thread-safe.
     *
     * @return Iterator over collection of values in cache.
     * @since  2.0
     */
    public Iterator values() {
        return new CacheIterator(keySet());
    }

    /**
     * Returns the number of entries currently in the cache.
     * Note that actually this value can be a bit smaller, because some of the objects might have been
     * deleted by Garbage Collector.
     *
     * @return current number of entries in the cache.
     * @since  2.0
     */
    public long getSize() {
        synchronized (mutex) {
            return map.size();
        }
    }

    /**
     * Removes all entries in the cache that are specified by the keys Set. This is basically a bulk remove
     * operation.
     *
     * @param  keys Set of keys for entries to be removed.
     * @throws NullPointerException if keys is null or contains null value.
     * @since  2.0
     */
    public void removeSet(Set keys) {
        if (keys == null) {
            throw new NullPointerException("'keys' can not be null.");
        }
        synchronized (mutex) {
            for (Iterator it = keys.iterator(); it.hasNext(); ) {
                Object key = it.next();
                if (key == null) {
                    throw new IllegalArgumentException("'keys' can not contain null value.");
                }
                Entry entry = (Entry) map.get(key);
                // key might not exist in the map
                if (entry == null) continue;
                // remove the key and notify eviction strategy with "remove"
                map.remove(key);
                evictionStrategy.notifyOfCacheRemove(key);
                // decrease currentByteSize
                currentByteSize -= entry.getSize();
            }
        }
    }

    /**
     * Represents total byte size of all the cache entries. Note that actually this value can be smaller,
     * because some of the objects might have been deleted by Garbage Collector.
     *
     * @return the size of the cache in bytes.
     * @since  2.0
     */
    public long getByteSize() {
        synchronized (mutex) {
            return currentByteSize;
        }
    }

    /**
     * Changes cache eviction strategy.
     *
     * @param  strategy new eviction strategy to be plugged in.
     * @throws NullPointerException if the input is null.
     * @since  2.0
     */
    public void setEvictionStrategy(CacheEvictionStrategy strategy) {
        if (strategy == null) {
            throw new NullPointerException("'strategy' can not be null.");
        }
        synchronized (mutex) {
            Map mapKeys2CacheEntryInfo = new HashMap();
            for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry mapEntry = (Map.Entry) it.next();
                mapKeys2CacheEntryInfo.put(mapEntry.getKey(), ((Entry) mapEntry.getValue()).getMetaData());
            }
            strategy.init(Collections.unmodifiableMap(mapKeys2CacheEntryInfo));
            evictionStrategy = strategy;
        }
    }

    /**
     * Returns the maximum number of bytes that this cache can occupy.
     *
     * @return the maximum capacity of the cache in bytes.
     * @since  2.0
     */
    public long getMaxCacheCapacity() {
        return maxCacheByteCapacity;
    }

    /**
     * Returns the map object.
     * @return the map
     */
    public HashMap getMap() {
        return map;
    }

    /**
     * Returns the CacheEvictionStrategy object.
     * @return the CacheEvictionStrategy
     */
    public CacheEvictionStrategy getEvictionStrategy() {
        return evictionStrategy;
    }

    /**
     * Sets the currentByteSize.
     * @param currentByteSize size which can't be negative
     */
    public void setByteSize(long currentByteSize) {
        if (currentByteSize < 0) {
            throw new IllegalArgumentException("currentByteSize can never be negative.");
        }
        synchronized (mutex) {
            this.currentByteSize = currentByteSize;
        }
    }

    /**
     * Removes all the entries in the cache that are larger in size than the input entry.
     *
     * @param  value the value object whose size will be used as removal criteria.
     * @throws NullPointerException if the value is null.
     * @throws CacheException if any internal error occurs. Any exception caught will be chained to this one.
     * @since  2.0
     */
    public void removeLarger(Object value) {
        if (value == null) {
            throw new NullPointerException("'value' can not be null.");
        }
        synchronized (mutex) {
            try {
                long size = memoryUtilizationHandler.getObjectSize(value);
                // iterate over set of map entries and delete values larger than size.
                for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry mapEntry = (Map.Entry) it.next();
                    Entry entry = (Entry) mapEntry.getValue();
                    if (entry.getSize() > size) {
                        currentByteSize -= entry.getSize();
                        evictionStrategy.notifyOfCacheRemove(mapEntry.getKey());
                        it.remove();
                    }
                }
            } catch (Exception ex) {
                throw new CacheException("Exception caught in removeLarger.", ex);
            }
        }
    }

    /**
     * Removes any entry in the cache that have been in the cache longer than the object specified by the
     * key input object. If key doesn't exist in cache then this method does nothing.
     *
     * @param  key key to the cache entry.
     * @throws NullPointerException if the key is null.
     * @throws CacheException if any internal error occurs. Any exception caught will be chained to
     *         this one.
     * @since  2.0
     */
    public void removeOlder(Object key) {
        if (key == null) {
            throw new NullPointerException("'key' can not be null.");
        }
        synchronized (mutex) {
            try {
                // get entry time of the entry associated with key. If value doesn't exist or has been deleted then exit.
                Entry entry = (Entry) map.get(key);
                if (entry == null) return;
                long time = entry.getMetaData().getCacheEntryTime();
                // iterate over set of map entries and delete values with creation time < time
                for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry mapEntry = (Map.Entry) it.next();
                    entry = (Entry) mapEntry.getValue();
                    if (entry.getMetaData().getCacheEntryTime() < time) {
                        currentByteSize -= entry.getSize();
                        evictionStrategy.notifyOfCacheRemove(mapEntry.getKey());
                        it.remove();
                    }
                }
            } catch (Exception ex) {
                throw new CacheException("Exception caught in removeOlder", ex);
            }
        }
    }

    /**
     * Removes any entry in the cache that is of the same class as the object specified (or it's descendant).
     *
     * @param  object object whose class type will be used in the comparison.
     * @throws NullPointerException if the object is null.
     * @throws CacheException if any internal error occurs. Any exception caught will be chained to
     *         this one.
     * @since  2.0
     */
    public void removeLike(Object object) {
        if (object == null) {
            throw new NullPointerException("'object' can not be null.");
        }
        synchronized (mutex) {
            Class objectClass = object.getClass();
            try {
                // iterate over set of map entries and delete values which are instances of objectClass.
                for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry mapEntry = (Map.Entry) it.next();
                    Entry entry = (Entry) mapEntry.getValue();
                    Class entryClass = Class.forName(entry.getClassType());
                    if (objectClass.isAssignableFrom(entryClass)) {
                        currentByteSize -= entry.getSize();
                        evictionStrategy.notifyOfCacheRemove(mapEntry.getKey());
                        it.remove();
                    }
                }
            } catch (Exception ex) {
                throw new CacheException("Exception caught in removeLike", ex);
            }
        }
    }

    /**
     * Removes any entry in the cache whose key matches given regular expression. If key is not String then
     * method toString() is used to get it's string representation.
     *
     * @param  regex String regular expression pattern to be used in matching.
     * @throws NullPointerException if regex is null.
     * @throws IllegalArgumentException if regex is empty string or invalid regular expression.
     * @throws CacheException if any internal error occurs. Any exception caught will be chained to this one.
     * @since  2.0
     */
    public void removeByPattern(String regex) {
        if (regex == null) {
            throw new NullPointerException("'regex' can not be null.");
        }
        if (regex.trim().length() == 0) {
            throw new IllegalArgumentException("'regex' can not be empty string.");
        }
        synchronized (mutex) {
            Pattern pattern = null;
            try {
                pattern = Pattern.compile(regex);
            } catch (PatternSyntaxException ex) {
                throw new IllegalArgumentException("'regex' should contain valid regular expression");
            }
            
            try {
                // iterate over set of map entries and delete values whose keys match pattern.
                for (Iterator it = map.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry mapEntry = (Map.Entry) it.next();
                    Object key = mapEntry.getKey();
                    Entry entry = (Entry) mapEntry.getValue();
                    Matcher matcher = pattern.matcher(key.toString());
                    if (matcher.matches()) {
                        currentByteSize -= entry.getSize();
                        evictionStrategy.notifyOfCacheRemove(mapEntry.getKey());
                        it.remove();
                    }
                }
            } catch (Exception ex) {
                throw new CacheException("Exception caught in removeByPattern", ex);
            }
        }
    }

    /**
     * Gets the compression flag.
     *
     * @return compression flag.
     * @since  2.0
     */
    public boolean getCompressionFlag() {
        synchronized (mutex) {
            return compressionFlag;
        }
    }

    /**
     * Sets the compression flag.
     *
     * @param compression compression flag.
     * @since 2.0
     */
    public void setCompressionFlag(boolean compression) {
        synchronized (mutex) {
            compressionFlag = compression;
        }
    }


    /**
     * This is an inner class of the SimpleCache and represents an instance of an Iterator over the values
     * in the cache. Each value returned by the next() method represents a value in the cache.
     * This is an external iterator in the sense that we give this instance out to the requesting user who can then
     * manipulate the entries of the cache as specified by the Iterator interface contract.
     * Note that since these are external calls each remove() and hasNext() will actually trigger EvictionStrategy
     * notification.
     * Expected Behavior
     * <p>This Iterator is not a *fast-fail* Iterator and as such if entries are actually removed from the underlying
     * collection of values the iterator will still function and will not fail. This implementation actually 'shadows'
     * the key Iterator and as such any changes to the cache entries will affect this Iterator.
     * <p>Thread-Safety<br>
     * An Iterator should not be utilized by more than one thread at a time since the behavior becomes
     * unpredictable. In such a sense the Iterator is NOT thread-safe
     */
    private class CacheIterator implements Iterator {

        /**
         * Iterator over key set of the cache.
         */
        private final Iterator keySetIterator;

        /**
         * Represents the current entry that we are on. This is the entry that was given out by the
         * iterator on the last next() method call. Call to remove() would remove this entry.
         */
        private Object curEntry = null;

        /**
         * Represents the key to the current entry that we are on. This is the entry that was given out by the
         * iterator on the last next() method call. Call to remove() would remove this entry and this key would be
         * actually used to remove it from the cache.
         */
        private Object curKey = null;

        /**
         * Represents the next entry in the collection that the iterator will serve if the call to next() is made.
         */
        private Object nextEntry = null;

        /**
         * Represents the  key to the next entry in the collection that the iterator will serve if the call to
         * next() is made.
         */
        private Object nextKey = null;

        /**
         * This will create a new instance of the Iterator with the passed on keyset as the view of the underlying
         * cache keys which will be used to extract the values when next() is called.
         *
         * @param the current view of set of keys in the cache.
         */
        public CacheIterator(Set keySet) {
            keySetIterator = keySet.iterator();
            while (keySetIterator.hasNext()) {
                Object key, value;
                key = keySetIterator.next();
                value = get(key);
                if (value != null) {
                    nextKey = key;
                    nextEntry = value;
                    break;
                }
            }
        }

        /**
         * Determines if there is another entry in the iterator. If not then we have already reached the last element
         * with the last next() and there are no more elements left.
         *
         * @return true if there is another element and false otherwise.
         */
        public boolean hasNext() {
            return nextKey != null;
        }

        /**
         * Returns the next element from the underlying collection of values in the cache. Note that in reality
         * we are returning an Iterator cached value.
         *
         * @throws NoSuchElementException iteration has no more elements.
         */
        public Object next() {
            curKey = nextKey;
            curEntry = nextEntry;
            if (curKey == null) {
                throw new NoSuchElementException("iteration has no more elements.");
            }
            nextKey = nextEntry = null;
            while (keySetIterator.hasNext()) {
                Object key, value;
                key = keySetIterator.next();
                value = get(key);
                if (value != null) {
                    nextKey = key;
                    nextEntry = value;
                    break;
                }
            }
            return curEntry;
        }

        /**
         * Removes the last accessed value from the underlying cache collection. This should only be called once
         * for each visited entry. More than one call on the same entry will result in an exception.
         *
         * @throws IllegalStateException if the next method has not yet been called, or the remove method has
         *         already been called after the last call to the next method.
         */
        public void remove() {
            if (curKey == null) {
                throw new IllegalStateException("no element to remove.");
            }
            SimpleCache.this.remove(curKey);
            curKey = curEntry = null;
        }
    }
}
/**
 * Represents the entry in a cache. This entry can be either original uncompressed value object
 * or an actual compressed value in which case there must be an associated compression handler in this entry.
 * An entry has a size which is the number of bytes that the value takes up in memory in its present state (i.e.
 * either original or compressed). This size is precomputed and assigned to an entry for efficiency
 * considerations since computing the size of any object is possibly an intense process.
 * This is an inner class and thus an assumption is made that all input values to the entry are valid and correct.
 */
class Entry {

    /**
     * Represents the value for this entry. Will always store SoftReference object.
     */
    private final Object value;

    /**
     * Represents the time when this entry will time out and would become elligable for removal by the
     * CacheCleanupThread.
     */
    private final long timesOutAt;

    /**
     * Represents the size of this entry (value) in bytes. This is used to store the size of this value
     * since object size computation is relatively expensive.
     */
    private final long size;

    /**
     * Represents the compression handler that 'knows' how to decompress this value if it is compressed.
     * If compressionHandler != null then we have a compressed value and it needs to be decompressed. Otherwise
     * the  value is not compressed and will be retrieved as is.
     * This is done for reasons of speed so that we can get the correct compression handler in O(1) time.
     */
    private final CompressionHandler compressionHandler;

    /**
     * Represents the time (in ms) when this entry was put into the cache.
     */
    private final long cacheEntryTime;

    /**
     * Represents the last time when this entry was accessed. This means that a get(...) operation was done on
     * the cache on this entry at the time of lastOperationtime. This includes only user actions.
     * This will have to be updated for every get done on the cache.
     */
    private long lastAccessTime;

    /**
     * Represents the class type of the entry. This is specifically important when the entry is compressed and the
     * only way to get the class type info was to decompress it. This metadata information saves time by not having
     * to decompress the value to find this info.
     */
    private final String classType;

    /**
     * Creates a new instance of an Entry initialized with the input parameters.
     *
     * @param value value of this entry.
     * @param timesOutAt when this entry will timeout.
     * @param size byte size of this entry.
     */
    public Entry(Object value, long timesOutAt, long size) {
        this(value, timesOutAt, size, null, null);
    }

    /**
     * Creates a new instance of an Entry initilaized with the input parameters.
     *
     * @param value value of this entry.
     * @param timesOutAt when this entry will timeout.
     * @param size size of this entry in bytes.
     * @param compressionHandler compression handler used to decompress this entry.
     * @param class type information for this entry.
     */
    public Entry(Object value, long timesOutAt, long size, CompressionHandler compressionHandler,
            String classType) {
        this.value = value;
        this.timesOutAt = timesOutAt;
        this.size = size;
        this.compressionHandler = compressionHandler;
        this.classType = classType;
        cacheEntryTime = lastAccessTime = System.currentTimeMillis();
    }

    /**
     * Represent the time when this entry will time out and become elligable for removal by the clean up
     * thread.
     *
     * @return time of expiry for this entry.
     */
    public long getTimesOutAt() {
        return timesOutAt;
    }

    /**
     * Represents the value of the object as represented in this entry.
     * Currently this means that the value represents one of these:
     * ver 2.0 changes
     * - We need to update the the lastAccessTime to current time.</p>
     *
     * @return value of this entry.
     */
    public synchronized Object getValue() {
        lastAccessTime = System.currentTimeMillis();
        return value;
    }

    /**
     * Returns the precomputed size of entry's value in bytes.
     *
     * @return size in bytes.
     */
    public long getSize() {
        return size;
    }

    /**
     * Returns the reference to the current compression handler used for this entry. This can be used by
     * the caller to decompress the entry if necessary. If the handler is null that means that no compression was
     * applied and the value in the entry is the original value as input to the cache API.
     *
     * @return the current compression handler used to compress the value.
     */
    public CompressionHandler getCompressionHandler() {
        return compressionHandler;
    }

    /**
     * Returns the class type of the value of this entry.  This will simply return the stored value.
     * It is possible that the returned value will be null or an empty string.
     * Otherwise it will be a full qualified class name as in: "java.util.Map" for example.
     *
     * @return class type of the value of the entry.
     */
    public String getClassType() {
        return classType;
    }

    /**
     * Returns the metadata for this entry.
     *
     * @return metadata info bean.
     */
    public CacheEntryInfo getMetaData() {
        return new CacheEntryInfo(size, cacheEntryTime, lastAccessTime);
    }
}
