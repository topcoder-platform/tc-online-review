/*-
 * ----------------------------------------------------------------------------
 * FileName: Int32GeneratorTests.java
 * Version: 1.0
 * Date: Aug 31, 2004
 *
 * <p>Copyright (c) 2004, TopCoder, Inc. All rights reserved</p>
 * ----------------------------------------------------------------------------
 */


package com.topcoder.util.generator.guid.stresstests;


import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.generator.guid.Int32Generator;
import com.topcoder.util.generator.guid.UUID;

import java.util.List;
import java.util.ArrayList;


/**
 * <p>
 * This class contains the unit tests for Int32Generator.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * <p>Copyright (c) 2004, TopCoder, Inc. All rights reserved</p>
 */
public class Int32GeneratorTests extends TestCase {

    //--- private static constnts ---
    /**
     * <p>
     * Represents the number of times method is to be invoked for stress testing.</p>
     */
    private static final int LOOP_COUNT = 1000;

    /**
     * <p>
     * Represents the number of threads to be used for stress testing.</p>
     */
    private static final int NUM_THREADS = 100;

    /**
     * <p>
     * Represents the number of times method is to be invoked for stress testing
     * in threads.</p>
     */
    private static final int SMALL_LOOP_COUNT = 10;
    /*------------------------------------------------------------------------*/

    //--- private instance variables ---
    /**
     * <p>
     * Represents the instance of Int32Generator to be used for testing.</p>
     */
    private Int32Generator generator = null;
    /*------------------------------------------------------------------------*/

    /**
     * <p>
     * Creates an instance for the Test.</p>
     *
     * @param name the name of the TestCase.
     */
    public Int32GeneratorTests(String name) {
        super(name);
    } // end Int32GeneratorTests()

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(Int32GeneratorTests.class);
    } // end suite()

    /**
     * <p>
     * Sets up the fixture.</p>
     */
    public void setUp() {
        this.generator = new Int32Generator();
    } // end setUp()
    /*------------------------------------------------------------------------*/

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple invocations.</p>
     */
    public void testGetNextUUIDMultipleCount() {
        List uuidList = new ArrayList();
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            UUID uuid = generator.getNextUUID();
            String str = uuid.toString();
            assertFalse("UUID should be unique", uuidList.contains(str));
            uuidList.add(str);
        }
        long s = System.currentTimeMillis();
        System.out.println("Int32Generator.getNextUUID(): " + (s - a));
    } // end testGetNextUUIDMultipleCount()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple threads.</p>
     */
    public void testGetNextUUIDMultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new GeneratorTestThread();
            threads.add(t);
        }

        // start the threads
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = (Thread) threads.get(i);
            t.start();
        }

        // wait for threads to finish
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = (Thread) threads.get(i);
            try {
                t.join();
            } catch (Exception e) {
                fail();
            }
        }

        // test generated UUIDs to be unique
        List uuidList = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            GeneratorTestThread t = (GeneratorTestThread) threads.get(i);
            List collectedUUIDs = t.getCollectedUUIDs();
            for (int j = 0; j < collectedUUIDs.size(); j++) {
                String uuidStr = (String) collectedUUIDs.get(j);
                assertFalse("UUIDs generated should be unique when invoked concurrently",
                            uuidList.contains(uuidStr));
                uuidList.add(uuidStr);
            }
        }
    } // end testGetNextUUIDMultipleThreads()

    /**
     * <p>
     * Inner class representing a thread for getting multiple UUIDs from the
     * specified generator that is being shared between multiple threads.</p>
     */
    class GeneratorTestThread extends Thread {
        private List uuidList = new ArrayList();

        public void run() {
            for (int i = 0; i < SMALL_LOOP_COUNT; i++) {
                uuidList.add(generator.getNextUUID().toString());
            }
        }

        public List getCollectedUUIDs() {
            return this.uuidList;
        }
    };

} // end Int32GeneratorTests
