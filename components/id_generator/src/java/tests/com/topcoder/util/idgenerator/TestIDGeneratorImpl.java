/**
 * Copyright &copy; 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import junit.framework.TestCase;

import java.math.BigInteger;


/**
 * <p>
 * Test the behaviour of IDGeneratorImpl. This test is outside the ejb  container, so I should create the DataSource
 * myself (Mysql is used here). The follwoing things are worth mentioned:
 *
 * <ul>
 * <li>
 * 1) test constructor with null or non-exist idName will raise exception
 * </li>
 * <li>
 * 2) call getNextID method 1000 times on one instance should return numbers continuous (stable enough)
 * </li>
 * <li>
 * 3) call getNextID method on two instances with the same idName. 4) concurreny test - run 15 threads 1000 iterations
 * on each methods
 * </li>
 * </ul>
 * </p>
 *
 * <p>
 * Because most of functionality keep the same with version 2.0, Only getNextBigID() method is added, so we add some
 * getNextBigID test methods. The same time, we will change the mechnisam about getting connection. It can be tested
 * by checking if getNextID work well, and we will remove all JDNI datasource related codes to ensure no JDNI
 * datasource is not effect now.
 * </p>
 *
 * @author gua
 * @version 3.0
 */
public class TestIDGeneratorImpl extends TestCase {
    /** The id sequence name for testing. */
    private static final String TEST_ID_NAME = "unit_test_id_sequence";

    /** The first exhaust id sequence name for testing. */
    private static final String FIRST_TEST_EXHAUST = "id_exhaust_1";

    /** The second exhaust id sequence name for testing. */
    private static final String SECOND_TEST_EXHAUST = "id_exhaust_2";

    /**
     * Test the constructor with null argument.
     * @throws Exception to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new IDGeneratorImpl(null);
            fail("The specified idName is null");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the constructor if the idName is not exist.
     * @throws Exception to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new IDGeneratorImpl("non-exist");
            fail("The specified idName is not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getNextID. The block size of IDGenerator("foo") is 10, call getNextID 1000 times will
     * cause it read and update the database at least 100 times. A little bit like stress test.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID1Mysql() throws Exception {
        IDGeneratorImpl generator = new IDGeneratorImpl("mysql", TEST_ID_NAME);
        long id = generator.getNextID();

        // the block size is 10
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            long nextid = generator.getNextID();

            assertEquals("Failed to get id: " + nextid + " " + id + " " + i, id + 1 + i, nextid);
        }
    }
    
    /**
     * Test the behaviour of getNextID. The block size of IDGenerator("foo") is 10, call getNextID 1000 times will
     * cause it read and update the database at least 100 times. A little bit like stress test.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID1MSSQL() throws Exception {
        IDGeneratorImpl generator = new IDGeneratorImpl("mssql2005", TEST_ID_NAME);
        long id = generator.getNextID();

        // the block size is 10
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            long nextid = generator.getNextID();

            assertEquals("Failed to get id: " + nextid + " " + id + " " + i, id + 1 + i, nextid);
        }
    }

    /**
     * Test the behaviour of getNextID. Create two IDGenerator with the same idName, both IDGenerator will occupy a new
     * block size of ids.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID2() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(TEST_ID_NAME);
        IDGeneratorImpl gen2 = new IDGeneratorImpl(TEST_ID_NAME);

        // the block size of foo IDGenerator is 10
        final int blockSize = 10;

        long id1 = gen1.getNextID();
        long id2 = gen2.getNextID();
        assertEquals("Adjacent IDGenerator is block_size separate", id1 + blockSize, id2);
    }

    /**
     * Test the getNextID with IDsExhaustedException.  The idName with &quot;exhaust1&quot; will be retrieved. Its
     * exhausted flag is set to true. So exception is  expected from it when call getNextID.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID3() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FIRST_TEST_EXHAUST);

        // exhausted flag is true already
        try {
            gen1.getNextID();
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
    public void testGetNextID4() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(SECOND_TEST_EXHAUST);

        // the ids left is not sufficient
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * Test the getIDName method. The name returned should be the same as the one specified in the ctor.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDName1() throws Exception {
        IDGeneratorImpl generator = new IDGeneratorImpl(TEST_ID_NAME);
        assertEquals("The name should be equal", TEST_ID_NAME, generator.getIDName());
    }

    /**
     * Test the behaviour of getNextBigID. The block size of IDGenerator(TEST_ID_NAME) is 10, call getNextBigID 1000
     * times will cause it read and update the database at least 100 times. A little bit like stress test.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID1() throws Exception {
        IDGeneratorImpl generator = new IDGeneratorImpl(TEST_ID_NAME);
        BigInteger id = generator.getNextBigID();

        // the block size is 10
        final int callLimit = 10;

        for (int i = 0; i < callLimit; ++i) {
            BigInteger nextBigID = generator.getNextBigID();

            if (nextBigID.longValue() != (id.longValue() + 1 + i)) {
                fail("The id generated should be " + nextBigID);
            }
        }
    }

    /**
     * Test the behaviour of getNextBigID. Create two IDGenerator with the same idName, both IDGenerator will occupy a
     * new block size of ids.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID2() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(TEST_ID_NAME);
        IDGeneratorImpl gen2 = new IDGeneratorImpl(TEST_ID_NAME);

        // the block size of foo IDGenerator is 10
        final int blockSize = 10;

        BigInteger id1 = gen1.getNextBigID();
        BigInteger id2 = gen2.getNextBigID();
        assertEquals("Adjacent IDGenerator is block_size separate", id1.longValue() + blockSize, id2.longValue());
    }

    /**
     * Test the getNextBigID with IDsExhaustedException.  The idName with &quot;exhaust1&quot; will be retrieved. Its
     * exhausted flag is set to true. So exception is  expected from it when call getNextBigID.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID3() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FIRST_TEST_EXHAUST);

        // exhausted flag is true already
        try {
            gen1.getNextBigID();
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
    public void testGetNextBigID4() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(SECOND_TEST_EXHAUST);

        // the ids left is not sufficient
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }


    /**
     * Concurrency test of the IDGeneratorImpl.
     *
     * @throws Exception to JUnit
     */
    public void testConcurrencyOfImpl() throws Exception {
        IDGeneratorImpl generator = new IDGeneratorImpl(TEST_ID_NAME);

        // setup multiple threads to test the synchronization
        final int threadCount = 15;
        WorkThread[] threads = new WorkThread[threadCount];

        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new WorkThread(generator, TEST_ID_NAME, i % WorkThread.TOTAL_METHODS);
            threads[i].start();
        }

        // wait util all the threads stopped
        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) {
                // ignore
            }
        }

        // check if there is any exception thrown when they are running
        for (int i = 0; i < threads.length; ++i) {
            if (threads[i].getThrowable() != null) {
                fail("Thread (" + i + ") throws exception: " + threads[i].getThrowable());
            }
        }
    }

    /**
     * Helper class to test the concurrency of the IDGeneratorImpl. getIDName and getNextID will be both tested, they
     * will be tested for RUN_ITERATION round to ensure concurrently access is safe and will not generate any
     * inconsistency.
     */
    private static class WorkThread extends Thread {
        /** The maximum round of the method will be runned. */
        private static final int RUN_ITERATION = 1000;

        /** The count of methods to be tested. */
        private static final int TOTAL_METHODS = 2;

        /** The instance of IDGeneratorImpl to be tested. */
        private IDGeneratorImpl generator;

        /** The idName of the IDGeneratorImpl. */
        private String genName;

        /** Determine which method will be runned. */
        private int opcode;

        /** The exception thrown when running the method. */
        private Throwable throwable;

        /**
         * Constructor of WorkThread.
         * @param generator test generator
         * @param genName test genName
         * @param opcode test opcode
         */
        public WorkThread(IDGeneratorImpl generator, String genName, int opcode) {
            this.generator = generator;
            this.genName = genName;

            this.opcode = opcode;
        }

        /**
         * Return the exception thrown during running the specified method.
         *
         * @return the exception thrown during running the specified method.
         */
        public Throwable getThrowable() {
            return throwable;
        }

        /**
         * The core job is done here. The specified method will be runned for RUN_ITERATION round to ensure the
         * concurrency access will  not generate any inconsistence.
         */
        public void run() {
            try {
                for (int i = 0; i < RUN_ITERATION; ++i) {
                    switch (opcode) {
                    case 0:
                        generator.getNextID();

                        break;

                    case 1:

                        if (generator.getIDName() != genName) {
                            throw new RuntimeException("Different name.");
                        }

                        break;
                    default:
                        break;
                    }
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }
    }
}
