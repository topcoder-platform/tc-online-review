/*-
 * ----------------------------------------------------------------------------
 * FileName: UUIDUtilityTests.java
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


import com.topcoder.util.generator.guid.UUIDUtility;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUID;

import java.util.List;
import java.util.ArrayList;


/**
 * <p>
 * This class contains the unit tests for UUIDUtility.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * <p>Copyright (c) 2004, TopCoder, Inc. All rights reserved</p>
 */
public class UUIDUtilityTests extends TestCase {

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

    /**
     * <p>
     * Creates an instance for the Test.</p>
     *
     * @param name the name of the TestCase.
     */
    public UUIDUtilityTests(String name) {
        super(name);
    } // end UUIDUtilityTests()

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(UUIDUtilityTests.class);
    } // end suite()
    /*------------------------------------------------------------------------*/

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple invocations for TYPE1.</p>
     */
    public void testType1MultipleCount() {
        List uuidList = new ArrayList();
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            UUID uuid = UUIDUtility.getNextUUID(UUIDType.TYPE1);
            String str = uuid.toString();
            assertFalse("UUID should be unique", uuidList.contains(str));
            uuidList.add(str);
        }
        long s = System.currentTimeMillis();
        System.out.println("UUIDUtility.getNextUUID() for TYPE1: " + (s - a));
    } // end testType1MultipleCount()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple invocations for TYPE4.</p>
     */
    public void testType4MultipleCount() {
        List uuidList = new ArrayList();
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            UUID uuid = UUIDUtility.getNextUUID(UUIDType.TYPE4);
            String str = uuid.toString();
            assertFalse("UUID should be unique", uuidList.contains(str));
            uuidList.add(str);
        }
        long s = System.currentTimeMillis();
        System.out.println("UUIDUtility.getNextUUID() for TYPE4: " + (s - a));
    } // end testType4MultipleCount()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple invocations for TYPEINT32.</p>
     */
    public void testTypeInt32MultipleCount() {
        List uuidList = new ArrayList();
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            UUID uuid = UUIDUtility.getNextUUID(UUIDType.TYPEINT32);
            String str = uuid.toString();
            assertFalse("UUID should be unique", uuidList.contains(str));
            uuidList.add(str);
        }
        long s = System.currentTimeMillis();
        System.out.println("UUIDUtility.getNextUUID() for TYPEINT32: " + (s - a));
    } // end testTypeInt32MultipleCount()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple threads for TYPE1.</p>
     */
    public void testType1MultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new GeneratorTestThread(UUIDType.TYPE1);
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
    } // end testType1MultipleThreads()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple threads for TYPE4.</p>
     */
    public void testType4MultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new GeneratorTestThread(UUIDType.TYPE4);
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
    } // end testType4MultipleThreads()

    /**
     * <p>
     * This test tests the getNextUUID() method to return the unique UUID for
     * multiple threads for TYPEINT32.</p>
     */
    public void testTypeInt32MultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new GeneratorTestThread(UUIDType.TYPEINT32);
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
    } // end testTypeInt32MultipleThreads()

    /**
     * <p>
     * Inner class representing a thread for getting multiple UUIDs from the
     * specified generator that is being shared between multiple threads.</p>
     */
    class GeneratorTestThread extends Thread {
        private UUIDType uuidType = null;
        private List uuidList = new ArrayList();

        public GeneratorTestThread(UUIDType uuidType) {
            this.uuidType = uuidType;
        }

        public void run() {
            for (int i = 0; i < SMALL_LOOP_COUNT; i++) {
                uuidList.add(UUIDUtility.getNextUUID(this.uuidType).toString());
            }
        }

        public List getCollectedUUIDs() {
            return this.uuidList;
        }
    };

} // end UUIDUtilityTests
