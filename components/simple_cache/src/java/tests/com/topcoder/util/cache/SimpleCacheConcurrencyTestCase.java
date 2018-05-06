/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.cache;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.Random;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.HashSet;

/**
 * Tests simultaneous access to methods of SimpleCache from multiple threads.
 *
 * @author  rem
 * @version 2.0
 * @since   2.0
 */
public class SimpleCacheConcurrencyTestCase extends TestCase {

    /**
     * Instance to test.
     */
    private SimpleCache cache = new SimpleCache();

    /**
     * Random number generator.
     */
    private Random rand = new Random();

    /**
     * Returns the test suite for this class.
     *
     * @return a new TestSuite representing this class.
     */
    public static Test suite() {
        return new TestSuite(SimpleCacheConcurrencyTestCase.class);
    }

    /**
     * Synchronized method that returns new random integer value.
     *
     * @return new random number.
     */
    private int nextInt() {
        synchronized (rand) {
            return rand.nextInt();
        }
    }

    /**
     * Run 8 threads, waits until they finish and checks that none of them has caught any exception.
     *
     * @throws Exception if any error occurs.
     */
    public void testConcurrency() throws Exception {
        TestThread[] threads = new TestThread[8];
        for (int i = 0; i < 8; ++i) {
            threads[i] = new TestThread();
            threads[i].start();
        }
        for (int i = 0; i < 8; ++i) {
            threads[i].join();
        }
        for (int i = 0; i < 8; ++i) {
            Throwable t = threads[i].getThrowable();
            if (t != null) {
                fail("Exception was thrown + " + t.getMessage());
            }
        }
    }

    /**
     * Thread class.
     */
    private class TestThread extends Thread {

        private Throwable throwable = null;

        public void run() {
            try {
                for (int iteration = 0; iteration < 10000; ++iteration) {
                    cache.getSize();
                    cache.getByteSize();
                    cache.getMaxCacheCapacity();
                    cache.getMaxCacheSize();
                    cache.getTimeoutMS();
                    int num = nextInt() % 100;
                    if (num == 0) {
                        cache.clear();
                    } else if (num == 1) {
                        cache.removeByPattern("1.*");
                    } else if (num == 2) {
                        cache.removeLarger(new String());
                    } else if (num == 3) {
                        cache.removeLike(new String());
                    } else if (num == 4) {
                        cache.removeOlder(new Integer(nextInt() % 100));
                    } else if (num == 5) {
                        Set set = new HashSet();
                        for (int i = 0; i < 10; ++i) {
                            set.add((nextInt() % 100) + "");
                        }
                        cache.removeSet(set);
                    } else if (num == 6) {
                        cache.setCompressionFlag(!cache.getCompressionFlag());
                    } else if (num == 7) {
                        cache.setEvictionStrategy(new FIFOCacheEvictionStrategy());
                    } else if (num == 8) {
                        cache.setEvictionStrategy(new BOFCacheEvictionStrategy());
                    } else if (num == 9) {
                        Iterator it = cache.values();
                        while (it.hasNext()) {
                            if (it.next() == null) {
                                throw new Exception();
                            }
                        }
                    } else if (num == 10) {
                        Set set = cache.keySet();
                        for (Iterator it = set.iterator(); it.hasNext();) {
                            String key = (String) it.next();
                            String value = (String) cache.get(key);
                            if (value != null && !value.equals(key)) {
                                throw new Exception();
                            }
                        }
                    } else if (num == 11) {
                        Map map = new HashMap();
                        for (int i = 0; i < 10; ++i) {
                            int k = nextInt() % 100;
                            map.put(k + "", k + "");
                        }
                        cache.put(map);
                    } else if (num < 50) {
                        cache.remove(new Integer(nextInt() % 100));
                    } else {
                        int k = nextInt() % 100;
                        cache.put(k + "", k + "");
                    }
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }

        public Throwable getThrowable() {
            return throwable;
        }
    }
}
