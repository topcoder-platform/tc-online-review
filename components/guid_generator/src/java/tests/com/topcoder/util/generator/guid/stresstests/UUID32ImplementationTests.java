/*-
 * ----------------------------------------------------------------------------
 * FileName: UUID32ImplementationTests.java
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

import com.topcoder.util.generator.guid.UUID32Implementation;

import java.util.List;
import java.util.ArrayList;


/**
 * <p>
 * This class contains the unit tests for UUID32Implementation.</p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * <p>Copyright (c) 2004, TopCoder, Inc. All rights reserved</p>
 */
public class UUID32ImplementationTests extends TestCase {

    //--- private static constants ---
    /**
     * <p>
     * Represents the number of times method is to be invoked for stress testing.</p>
     */
    private static final int LOOP_COUNT = 10000;

    /**
     * <p>
     * Represents the number of threads to be used for stress testing.</p>
     */
    private static final int NUM_THREADS = 500;

    /**
     * <p>
     * Represents the number of times method is to be invoked for stress testing
     * in threads.</p>
     */
    private static final int SMALL_LOOP_COUNT = 100;
    /*------------------------------------------------------------------------*/

    //--- private instance variables ---
    /**
     * <p>
     * Represents the byte array to be used for testing.</p>
     */
    private byte[] BYTES = {0x29, 0x07, 0x05, 0x02};

    /**
     * <p>
     * Represents the string representation of the byte array as per the UUID
     * specifications.</p>
     */
    private String STRING_UUID = "29070502";

    /**
     * <p>
     * Represents the instance of the UUID to be used for testing.</p>
     */
    private UUID32Implementation uuid = null;
    /*------------------------------------------------------------------------*/

    /**
     * <p>
     * Creates an instance for the Test.</p>
     *
     * @param name the name of the TestCase.
     */
    public UUID32ImplementationTests(String name) {
        super(name);
    } // end UUID32ImplementationTests()

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.</p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(UUID32ImplementationTests.class);
    } // end suite()

    /**
     * <p>
     * Sets up the fixture.</p>
     */
    public void setUp() {
        this.uuid = new UUID32Implementation(BYTES);
    } // end setUp()
    /*------------------------------------------------------------------------*/

    /**
     * <p>
     * This test tests the toString() method to return the correct and the same
     * string instance for multiple invocations of toString() method on the UUID
     * instance.</p>
     */
    public void testToStringMultipleCount() {
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            assertEquals("String UUID not as expected", STRING_UUID, this.uuid.toString());
        }
        long s = System.currentTimeMillis();
        System.out.println("UUID32Implementation.toString(): " + (s - a));
    } // end testToStringMultipleCount()

    /**
     * <p>
     * This test tests the toByteArray() method to return the correct and the same
     * byte array for multiple invocations of toByteArray() method on the UUID
     * instance.</p>
     */
    public void testToByteArrayMultipleCount() {
        long a = System.currentTimeMillis();
        for (int i = 0; i < LOOP_COUNT; i++) {
            byte[] bytes = this.uuid.toByteArray();
            for (int j = 0; j < bytes.length; j++) {
                assertEquals("byte array for UUID not as expected", BYTES[j], bytes[j]);
            }
        }
        long s = System.currentTimeMillis();
        System.out.println("UUID32Implementation.toByteArray(): " + (s - a));
    } // end testToByteArrayMultipleCount()


    /**
     * <p>
     * This test tests the toString() method to return the correct and the same
     * string for multiple threads.</p>
     */
    public void testToStringMultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new Thread() {
                public void run() {
                    for (int j = 0; j < SMALL_LOOP_COUNT; j++) {
                        assertEquals("String UUID not as expected", STRING_UUID, uuid.toString());
                    }
                }
            };
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
    } // end testToStringMultipleThreads()

    /**
     * <p>
     * This test tests the toByteArray() method to return the correct and the same
     * byte array for multiple threads.</p>
     */
    public void testToByteArrayMultipleThreads() {
        List threads = new ArrayList();
        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new Thread() {
                public void run() {
                    for (int i = 0; i < LOOP_COUNT; i++) {
                        byte[] bytes = uuid.toByteArray();
                        for (int j = 0; j < bytes.length; j++) {
                            assertEquals("byte array for UUID not as expected", BYTES[j], bytes[j]);
                        }
                    }
                }
            };
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
    } // end testToByteArrayMultipleThreads()

} // end UUID32ImplementationTests
