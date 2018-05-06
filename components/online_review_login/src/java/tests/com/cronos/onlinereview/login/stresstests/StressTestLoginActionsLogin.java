/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.login.stresstests;

import junit.framework.TestCase;

/**
 * Stress test for class <code>LoginActions#login </code>.
 *
 * @author Chenhong
 * @version 1.0
 */
public class StressTestLoginActionsLogin extends TestCase {
    /**
     * Stress test for login for 10 users.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLogin_With10User() throws Exception {
        Thread[] thread = new Thread[10];
        long start = System.currentTimeMillis();
        for (int i = 0; i < thread.length; i++) {
            thread[i] = new LoginThread();
            thread[i].start();
        }
        long end = System.currentTimeMillis();
        System.out.println("Running login for 10 user, cost " + (end - start) / 1000.0 + " seconds.");
    }

    /**
     * Stress test for login for 100 threads.
     *
     * @throws Exception
     *             to junit.
     *
     */
    public void testLogin_With100User() throws Exception {
        Thread[] thread = new Thread[100];
        long start = System.currentTimeMillis();
        for (int i = 0; i < thread.length; i++) {
            Thread.sleep(300);
            thread[i] = new LoginThread();

            thread[i].start();

        }

        long end = System.currentTimeMillis();
        System.out.println("Running login for 100 user, cost " + (end - start - 30000) / 1000.0 + " seconds.");
    }

    /**
     * A Login Thread for login process
     */
    private class LoginThread extends Thread {
        /**
         * Represents a Login test instance for running login.
         */
        private LoginTest login = null;

        /**
         * Create a LoginThread instance for test.
         *
         * @throws Exception
         *             to junit.
         */
        LoginThread() throws Exception {
            login = new LoginTest();
        }

        /**
         * Run the thread.
         */
        public void run() {
            try {
                login.testLogin();
            } catch (Exception e) {
                fail("Run method login failed.");
            }
        }
    }
}
