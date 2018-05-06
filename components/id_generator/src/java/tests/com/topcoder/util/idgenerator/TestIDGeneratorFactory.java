/**
 * Copyright &copy; 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import junit.framework.TestCase;


/**
 * Test the behaviour of IDGeneratorFactory. This test is outside the ejb  container, so I should create the DataSource
 * myself (Mysql is used here). The follwoing things are worth mentioned: 1) test getIDGenerator with null or
 * non-exist idName will raise exception 2) call getIDGenerator with the same idName will return the same object 3)
 * call getNextID on the IDGenerator returned by getIDGenerator about 1000  times (stress test) 4) concurrency test -
 * issue 15 threads 1000 iteration on each method
 *
 * @author gua
 * @version 2.0
 */
public class TestIDGeneratorFactory extends TestCase {
    /** The id sequence name for testing. */
    private static final String TEST_ID_NAME = "unit_test_id_sequence";

    /** The second id sequence name for testing. */
    private static final String TEST_ID_NAME2 = "unit_test_id_sequence_2";

    /** The first exhaust id sequence name for testing. */
    private static final String TEST_EXHAUST_1 = "id_exhaust_1";

    /** The second exhaust id sequence name for testing. */
    private static final String TEST_EXHAUST_2 = "id_exhaust_2";

    /** The instance of IDGenerator get from the IDGeneratorFactory. */
    private IDGenerator gen1;

    /**
     * Bind the JNDI name with a DataSource.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        gen1 = IDGeneratorFactory.getIDGenerator(TEST_ID_NAME);
    }

    /**
     * Test the behaviour of getIDGenerator. the given idName does not exist in the database, NoSuchIDSequenceException
     * is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator1() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator("non-exist");
            fail("The specified idName does not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. the given idName is null, NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator2() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(null);
            fail("The specified idName is null.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. We will call getIDGenerator  twice with the same idName, the returned
     * IDGenerator should be same because it is cached after the first call.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator3() throws Exception {
        IDGenerator gen2 = IDGeneratorFactory.getIDGenerator(TEST_ID_NAME);
        assertSame("These two instance should be the same", gen1, gen2);

        // call the getNextID on IDGenerator
        assertEquals("The id returned should be continuous", gen1.getNextID() + 1, gen2.getNextID());
    }

    /**
     * Test the behaviour of getIDGenerator. The block size of IDGenerator is 10, call getNextID 1000 times will cause
     * it read and update the database at least 100 times. A little bit like stress test.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator4() throws Exception {
        // the block size is 10
        long id = gen1.getNextID();

        final int callLimit = 1000;

        // call it callLimit times
        for (int i = 0; i < callLimit; ++i) {
            long nextid = gen1.getNextID();

            if (nextid != (id + i + 1)) {
                fail("The returned id (" + nextid + ") is wrong indeed");
            }
        }
    }

    /**
     * Test the getNextID with IDsExhaustedException.  The idName with &quot;exhaust1&quot; will be retrieved. Its
     * exhausted flag is set to true. So exception is  expected from it when call getNextID.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator5() throws Exception {
        IDGenerator gen = IDGeneratorFactory.getIDGenerator(TEST_EXHAUST_1);

        // exhausted flag is true already
        try {
            gen.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * Test the getNextID with IDsExhaustedException. The idName with &quot;exhaust2&quot; will be retrieved. It does
     * not have sufficient ids left to make a full block.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator6() throws Exception {
        IDGenerator gen = IDGeneratorFactory.getIDGenerator(TEST_EXHAUST_2);

        // the ids left is not sufficient
        try {
            gen.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. the given idName is non-existing, NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator7() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(null, "com.topcoder.util.idgenerator.OracleSequenceGenerator");
            fail("NoSuchIDSequenceException should be throw.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. the given implClassName cannot be found. ClassNotFoundException is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator8() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(TEST_ID_NAME, "com.topcoder.util.idgenerator.non-implClass");
            fail("ClassNotFoundException should be throw.");
        } catch (ClassNotFoundException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. the given implClassName is correct. Verify that correct IDGenerator should
     * be created
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator9() throws Exception {
        assertNotNull("Failed to create IDGenerator",
            IDGeneratorFactory.getIDGenerator("Oracle", "com.topcoder.util.idgenerator.OracleSequenceGenerator"));
    }

    /**
     * Concurrency test of IDGeneratorFactory. The IDGeneratorFactory will be tested against 15 threads, and each
     * threads will run the method 300 iterations. No exception is expected from the running.
     */
    public void testConcurrencyOfFactory() {
        final String[] idNames = {TEST_ID_NAME, TEST_ID_NAME2};
        final int maxThread = 15;

        WorkThread[] threads = new WorkThread[maxThread];

        // run the thread test
        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new WorkThread(idNames[i % idNames.length]);
            threads[i].start();
        }

        // wait util all the threads are stopped
        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) {
                // Ignore
            }
        }

        // check the exception
        for (int i = 0; i < threads.length; ++i) {
            if (threads[i].getThrowable() != null) {
                fail("Exception thrown when running the thread(" + i + "): " + threads[i].getThrowable());
            }
        }
    }

    /**
     * A helper class for testing the concurrency of IDGeneratorFactory. Each method in IDGeneratorFactory will be
     * called 300 iteration, I have test it against 1000 iteration, it works well also, but will take up too much time
     * (30 s for 15 threads).
     */
    private static class WorkThread extends Thread {
        /** The maximum round of the method will be runned. */
        private static final int RUN_ITERATION = 300;

        /** The exception thrown when running the method. */
        private Throwable throwable;

        /** The idName of the IDGenerator in the factory. */
        private String idName;

        /**
         * Construct of WorkThread.
         *
         * @param idName test idName
         */
        public WorkThread(String idName) {
            this.idName = idName;
        }

        /**
         * Return the exception thrown when running the method.
         *
         * @return the exception thrown during running the specified method.
         */
        public Throwable getThrowable() {
            return throwable;
        }

        /**
         * The core function that does the job. The method will be tested RUN_ITERATION rounds to ensure the
         * thread-safety.  After get the IDGenerator from the factory, we will call getNextID on it, so this test runs
         * much slower than that of TestIDGeneratorImpl.
         */
        public void run() {
            try {
                for (int i = 0; i < RUN_ITERATION; ++i) {
                    // get the IDGenerator from the factory
                    IDGenerator generator = IDGeneratorFactory.getIDGenerator(idName);

                    // call methods on it, add the id the vector
                    generator.getNextID();
                    generator.getIDName();
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }
    }
}
