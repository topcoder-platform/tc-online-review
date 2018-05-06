/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.stresstests;

import java.util.Date;

import junit.framework.TestCase;


/**
 * <p>
 * BaseStressTest class for the stress tests.
 * </p>
 * <p>
 * Thread safe: This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author gjw99
 * @version 1.0
 */
public class BaseStressTest extends TestCase {
    /** The test count. */
    protected static int testCount = 100;

    /** time started to test. */
    protected long start = 0;

    /** The last error occurred. */
    protected Throwable lastError;

    /**
     * Initialize variables.
     *
     * @throws Exception
     *             if anything goes wrong
     */
    public void setUp() throws Exception {
        start = new Date().getTime();
        lastError = null;
    }

    /**
     * The thread that is used in the test.
     *
     * @author gjw99
     * @version 1.0
     */
    class TestThread extends Thread {
        /** The index. */
        private int index;

        /**
         * Create the test thread.
         *
         * @param index
         *            the index.
         */
        public TestThread(int index) {
            super();
            this.index = index;
        }

        /**
         * Get the index.
         *
         * @return the index
         */
        public int getIndex() {
            return index;
        }
    }
}
