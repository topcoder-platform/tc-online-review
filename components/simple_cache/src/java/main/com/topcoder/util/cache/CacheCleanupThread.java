package com.topcoder.util.cache;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Inner thread which cleans up entries in the cache that have expired.
 * This thread also removes entries whose SoftReference-s were cleared by
 * Garbage Collector.
 * @author  WishingBone, srowen, AleaActaEst, rem
 * @version 2.0
 * @since   1.0
 */
class CacheCleanupThread extends Thread {

    /**
     * Represents the default wakeup interval for the thread which is 5 seconds.
     */
    private long CLEANUP_INTERVAL_MS = 5000;

    /**
     * WeakReference instance used to keep a reference to SimpleCache.
     */
    private WeakReference cache;

    /**
     * An object instance used as a mutex for SimpleCache and CacheCleanupThread instances.
     */
    private Object mutex;

    /**
     * Create an instance, set as deamon thread.
     */
    public CacheCleanupThread(SimpleCache cache, Object mutex) {
        this.cache = new WeakReference(cache);
        this.mutex = mutex;
        setDaemon(true);
    }

    /**
     * Thread Body. This is where the iterations are going to happen.
     */
    public void run() {
        while (!Thread.interrupted()) {
            // run every five seconds
            try {
                sleep(CLEANUP_INTERVAL_MS);
            } catch (InterruptedException e) {
                break;
            }
            // get current time
            // entries timestamped before this would be removed
            long current = System.currentTimeMillis();
            synchronized (mutex) {
                Set entrySet = ((SimpleCache)this.cache.get()).getMap().entrySet();
                Iterator i = entrySet.iterator();
                // iterate through the map
                while (i.hasNext()) {
                    Map.Entry mapEntry = (Map.Entry)i.next();
                    Entry entry = (Entry) mapEntry.getValue();
                    // remove value if it has been cleared by Garbage Collector or it has expired.
                    if (((Reference) entry.getValue()).get() == null ||
                        entry.getTimesOutAt() <= current) {
                        ((SimpleCache)this.cache.get()).setByteSize(((SimpleCache)this.cache.get()).getByteSize() - entry.getSize());
                        i.remove();
                        ((SimpleCache)this.cache.get()).getEvictionStrategy().notifyOfCacheRemove(mapEntry.getKey());
                    }
                }
            }
        }
    }

    /**
     * Stops the thread. Set as user thread.
     */
    public void halt() {
        this.interrupt();
    }

}
