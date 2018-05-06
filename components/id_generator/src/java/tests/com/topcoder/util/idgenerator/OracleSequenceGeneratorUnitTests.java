/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import junit.framework.TestCase;

import java.math.BigInteger;


/**
 * <p>
 * Test the behaviour of OracleSequenceGenerator. . The follwoing things are worth mentioned: 1) test constructor with
 * null or nonexist idName will raise exception 2) call getNextID method 1000 times on one instance should return
 * numbers continuous (stable enough) 3) call getNextID method on two instances with the same idName. 4) concurreny
 * test - run 15 threads 1000 iterations on each methods 5) the same way to test getNextBigID().
 * </p>
 *
 * <p>
 * The most difference between Oracle sequence impl and default IDGeneratorImpl is: Default impl keep a block of ids in
 * instance. but Oracle sequence impl won't hold any block ids, it will retrieve nextval from oracle persistence all
 * the time. So the id generator instances with same id name in several jvms will have the same behaviour.
 * </p>
 *
 * @author gua
 * @version 3.0
 */
public class OracleSequenceGeneratorUnitTests extends TestCase {
    /** The id sequence name for testing. */
    private static final String TEST_ID_NAME = "ID_SEQUENCE";

    /** The exhaust id sequence name for testing. */
    private static final String TEST_EXHAUST = "ORACLE_EXHAUST";

    /** The exhaust id sequence name for testing which contains less id. */
    private static final String TEST_LESS_EXHAUST = "ORACLE_EXHAUST_LESS";

    /**
     * <p>
     * Test the constructor with null argument.
     * </p>
     *
     * <p>
     * NoSuchIDSequenceException should be thrown
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConstructor1() throws Exception {
        try {
            new OracleSequenceGenerator(null);
            fail("The specified idName is null");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the constructor if the idName is not exist.
     * </p>
     *
     * <p>
     * NoSuchIDSequenceException should be thrown
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testConstructor2() throws Exception {
        try {
            new OracleSequenceGenerator(null);
            fail("The specified idName is not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the getIDName method.
     * </p>
     *
     * <p>
     * The name returned should be the same as the one specified in the ctor.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetIDName1() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        assertEquals("The name should be equal", TEST_ID_NAME, generator.getIDName());
    }

    /**
     * <p>
     * Test the behaviour of getNextID.
     * </p>
     *
     * <p>
     * The block size of IDGenerator is 10, call getNextID 1000 times will cause it read and update the database at
     * least 100 times. A little bit like stress test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID1() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        long id = generator.getNextID();

        // the block size is 10
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            assertEquals("The id generated should be ", generator.getNextID(), id + 1 + i);
        }
    }

    /**
     * <p>
     * Test the behaviour of getNextBigID.
     * </p>
     *
     * <p>
     * The block size of IDGenerator(TEST_ID_NAME) is 10, call getNextBigID 1000 times will cause it read and update
     * the database at least 100 times. A little bit like stress test.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID1() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        BigInteger id = generator.getNextBigID();

        // the block size is 10
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            assertEquals("The id generated should be ", generator.getNextBigID().longValue(), id.longValue() + 1 + i);
        }
    }

    /**
     * <p>
     * Test the behaviour of getNextID.
     * </p>
     *
     * <p>
     * Create two IDGenerator with the same idName, both IDGenerator will occupy a new block size of ids.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID2() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_ID_NAME);
        OracleSequenceGenerator gen2 = new OracleSequenceGenerator(TEST_ID_NAME);

        // the block size of foo IDGenerator is 10
        final int blocksize = 20;

        long id1 = gen1.getNextID();
        long id2 = gen2.getNextID();
        assertEquals("Adjacent IDGenerator is blocksize", id1 + blocksize, id2);
    }

    /**
     * <p>
     * Test the behaviour of getNextBigID.
     * </p>
     *
     * <p>
     * Create two IDGenerator with the same idName, both IDGenerator will occupy a new block size of ids.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID2() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_ID_NAME);
        OracleSequenceGenerator gen2 = new OracleSequenceGenerator(TEST_ID_NAME);

        // the block size of foo IDGenerator is 10
        final int blocksize = 20;

        BigInteger id1 = gen1.getNextBigID();
        BigInteger id2 = gen2.getNextBigID();
        assertEquals("Adjacent IDGenerator is blocksizeseparate", (id1.longValue() + blocksize), id2.longValue());
    }

    /**
     * <p>
     * Test the getNextID with IDsExhaustedException.
     * </p>
     *
     * <p>
     * The idName with &quot;exhaust1&quot; will be retrieved. Its exhausted flag is set to true. So exception is
     * expected from it when call getNextID.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID3() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_EXHAUST);

        // exhausted flag is true already
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the getNextBigID with IDsExhaustedException.
     * </p>
     *
     * <p>
     * The idName with &quot;exhaust1&quot; will be retrieved. Its exhausted flag is set to true. So exception is
     * expected from it when call getNextBigID.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID3() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_EXHAUST);

        // exhausted flag is true already
        try {
            gen1.getNextBigID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the getNextID with IDsExhaustedException.
     * </p>
     *
     * <p>
     * The idName with &quot;exhaust2&quot; will be retrieved. It does not have sufficient ids left to make a full
     * block.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID4() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_LESS_EXHAUST);

        // the ids left is not sufficient
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the getNextBigID with IDsExhaustedException.
     * </p>
     *
     * <p>
     * The idName with &quot;exhaust2&quot; will be retrieved. It does not have sufficient ids left to make a full
     * block.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID4() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(TEST_LESS_EXHAUST);

        // the ids left is not sufficient
        try {
            gen1.getNextBigID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * <p>
     * Test the setSequenceName(String) method with null sequence name.
     * </p>
     *
     * <p>
     * NullPointerException should be thrown
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSequenceName_NullName() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        try {
            generator.setSequenceName(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // ignore
        }
    }

    /**
     * <p>
     * Test the setSequenceName(String) method with correct sequence name.
     * </p>
     *
     * <p>
     * Verify that the seqName will be retrieved properly by getSequenceName method.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSetSequenceName() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        String seqName = "sequence_name";
        generator.setSequenceName(seqName);
        assertEquals("Failed to set sequence name.", seqName, generator.getSequenceName());
    }

    /**
     * <p>
     * Test the getSequenceName() method that sequence name set in constructor.
     * </p>
     *
     * <p>
     * Verify that the seqName will be retrieved properly that set with setSequenceName.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetSequenceName1() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        String seqName = TEST_ID_NAME + "_SEQ";
        assertEquals("Failed to set sequence name.", seqName, generator.getSequenceName());
    }

    /**
     * <p>
     * Test the getSequenceName() method that sequence name set in setter.
     * </p>
     *
     * <p>
     * Verify that the seqName will be retrieved properly that set with setSequenceName.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testGetSequenceName2() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);
        String seqName = "sequence_name";
        generator.setSequenceName(seqName);
        assertEquals("Failed to set sequence name.", seqName, generator.getSequenceName());
    }

    /**
     * <p>
     * Concurrency test of the OracleSequenceGenerator.
     * </p>
     *
     * <p></p>
     *
     * @throws Exception to JUnit
     */
    public void testConcurrencyOfImpl() throws Exception {
        OracleSequenceGenerator generator = new OracleSequenceGenerator(TEST_ID_NAME);

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
                // Ignore
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
     * <p>
     * Helper class to test the concurrency of the OracleSequenceGenerator. getIDName and getNextID will be both
     * tested, they will be tested for RUN_ITERATION round to ensure concurrently access is safe and will not generate
     * any inconsistency.
     * </p>
     *
     * <p></p>
     */
    private static class WorkThread extends Thread {
        /** The maximum round of the method will be runned. */
        private static final int RUN_ITERATION = 10;

        /** The count of methods to be tested. */
        private static final int TOTAL_METHODS = 2;

        /** The instance of OracleSequenceGenerator to be tested. */
        private OracleSequenceGenerator generator;

        /** The idName of the OracleSequenceGenerator. */
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
        public WorkThread(OracleSequenceGenerator generator, String genName, int opcode) {
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
         * concurrency access will not generate any inconsistence.
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
