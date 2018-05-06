package com.topcoder.util.cache.accuracytests;

import com.topcoder.util.cache.*;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * This test case tests the cache's behavior when accessed
 * by multiple threads at once. It tests <code>get()</code>,
 * <code>put()</code>, <code>clear()</code> and <code>remove()
 * </code>, while functional tests just test the first two
 *
 * @author haha
 * @version 1.0
 */
public class AccuracyConcurrencyTestCase extends TestCase {

    private static final int TEST_CACHE_OPERATIONS = 25000;

    private Cache cache = new SimpleCache();

    /**
     * verifies the the cache works against numerous threads
     */
    public void testConcurrency() throws Throwable {
        // no exceptions should be thrown

        int numThreads = 16;
        // Kick off some threads... some read, some write.
        // Some use key "foo", others "bar"
        TestThread[] threads = new TestThread[numThreads];
        for (int i = 0; i < numThreads/2; i++) {
            threads[i] = new TestThread(i % 4, "foo");
            threads[i].start();
        }
        for (int i = numThreads/2; i < numThreads; i++) {
            threads[i] = new TestThread(i % 4, "bar");
            threads[i].start();
        }

        for (int i = 0; i < numThreads; i++) {
            threads[i].join();
        }

        for (int i = 0; i < numThreads; i++) {
            Throwable t = threads[i].getThrowable();
            if (t != null) {
                fail("Exception was thrown: " + t.getMessage());
            }
        }
    }

    /**
     * runs the test
     */
    public static Test suite() {
        return new TestSuite(AccuracyConcurrencyTestCase.class);
    }

    /**
     * test thread used to help with currently
     */
    private class TestThread extends Thread {

        private int operation;
        private String key;
        private Throwable throwable;

        public TestThread(int operation, String key) {
            this.operation = operation;
            this.key = key;
        }

        public void run() {
            try {
                for (int i = 0; i < TEST_CACHE_OPERATIONS; i++) {
                    switch(operation){
                    case(0):
                        cache.get(key);
                        break;
                    case(1):
                        cache.put(key,"haha");
                        break;
                    case(2):
                        cache.remove(key);
                        break;
                    case(3):
                        cache.clear();
                        break;
                    }
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }

        public Throwable getThrowable() {
            return this.throwable;
        }
    }
}
