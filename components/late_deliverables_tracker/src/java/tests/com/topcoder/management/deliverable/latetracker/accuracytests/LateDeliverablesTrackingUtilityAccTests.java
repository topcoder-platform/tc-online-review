/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;

/**
 * Accuracy tests for LateDeliverablesTrackingUtility.
 * @author mumujava, KLW
 * @version 1.2
 */
public class LateDeliverablesTrackingUtilityAccTests extends AccuracyHelper {
    /**
     * The guard file path.
     */
    private String guardFileName = "test_files/accuracy/guard.txt";
    /**
     * <p>Sets up the unit tests.</p>
     */
    public void setUp() throws Exception {
        super.setUp();

        new File(guardFileName).delete();
    }

    /**
     * <p>Cleans up the unit tests.</p>
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        new File(guardFileName).delete();
    }

    /**
     * Accuracy test for method main.
     *
     *
     * @throws Exception to junit
     */
    public void test_main1() throws Exception {
        final PipedOutputStream out = new PipedOutputStream();
        InputStream in = new PipedInputStream(out);
        InputStream org = System.in;
        System.setIn(in);
        try {
            new Thread() {
                public void run() {
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //write a char
                    try {
                        out.write(1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();

            LateDeliverablesTrackingUtility.main(new String[] {});

            //no exception is thrown
        } finally {
            System.setIn(org);
        }
    }
    /**
     * Accuracy test for method main.
     *
     *
     * @throws Exception to junit
     * @since 1.2
     */
    public void test_main2() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");
        runMain(new String[] {"-trackingInterval=20", "-notificationInterval=30", "-guardFile=" + guardFileName,
            "-background=true"});
        // check the email
    }
    /**
     * Runs the main method with given arguments.
     *
     * @param args
     *            the arguments.
     * @throws Exception to JUnit.
     */
    private void runMain(String[] args) throws Exception {
        final PipedOutputStream out = new PipedOutputStream();
        InputStream in = new PipedInputStream(out);
        InputStream org = System.in;
        System.setIn(in);

        try {
            class MainThread extends Thread {
                private String[] args;
                public MainThread(String[] args) {
                    this.args = args;
                }
                public void run() {
                    LateDeliverablesTrackingUtility.main(args);
                    try {
                        Thread.sleep(10 * 1000);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                    // write a char
                    try {
                        out.write(1);
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }
            Thread thread = new MainThread(args);
            thread.start();

            Thread.sleep(1000);
        } finally {
            System.setIn(org);
        }
    }
}
