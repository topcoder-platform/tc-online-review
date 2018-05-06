/**
 * Copyright &copy; 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import com.topcoder.util.idgenerator.ejb.IDGeneratorBean;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocal;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocalHome;

import junit.framework.TestCase;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.Properties;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.jndi.MockContextFactory;


/**
 * <p>
 * Test the behaviour of IDGenerator ejb. There is only one method in the interface : <code>getNextID</code>. The
 * following things are worth mentioned: 1) call it with null idName or non-exist idName, exception will throw 2)
 * because the IDGenerator is pooled in the ejb container, so the id  returned maybe not continuous.
 * </p>
 *
 * <p>
 * IDGeneratorBean class only add getNextBigID method. its test is same as getNextID because it just wrapp long id that
 * retreieved from getNextID in current release.
 * </p>
 *
 * @author gua
 * @version 3.0
 */
public class TestIDGeneratorBean extends TestCase {
    /** The IDGeneratorHome load from config file. */
    static String idGeneratorHome;

    /** The constant of test properties file name. */
    private static final String CONFIG_FILE = "test_files/unittest.properties";

        /** The first id sequence name for bean testing. */
    private static final String FIRST_TEST_BEAN = "unit_test_bean";

    /** The second id sequence name for bean testing. */
    private static final String SECOND_TEST_BEAN = "unit_test_bean_2";

    /** The first instance of IDGenerator used to getNextID. */
    private IDGeneratorLocal gen1;

    /** The second instance of IDGenerator used to getNextID. */
    private IDGeneratorLocal gen2;


    /**
     * Load config properties for testing.
     */
    static {
        try {
            // load the test config from file
            Properties prop = new Properties();
            prop.load(new FileInputStream(CONFIG_FILE));
            idGeneratorHome = prop.getProperty("IDGeneratorHome");
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    /**
     * Get a IDGenerator instance from JNDI.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        MockContextFactory.setAsInitial();
        Context context = new InitialContext();
        MockContainer mockContainer = new MockContainer(context);
        SessionBeanDescriptor sampleServiceDescriptor =
            new SessionBeanDescriptor(idGeneratorHome,
                    IDGeneratorLocalHome.class, IDGeneratorLocal.class,
                    new IDGeneratorBean());
        mockContainer.deploy(sampleServiceDescriptor);


        Object obj = context.lookup(idGeneratorHome);
        IDGeneratorLocalHome home = (IDGeneratorLocalHome) obj; 

        gen1 = home.create();
        System.out.println("gen1" + gen1);
        gen2 = home.create();
    }

    /**
     * Clean Environment.
     *
     * @throws Exception to JUnit.
     */
    public void tearDown() throws Exception {
        MockContextFactory.revertSetAsInitial();
    }

    /**
     * Test the behaviour of getNextID, the specified idName is null,  a NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID1() throws Exception {
        try {
            gen1.getNextID(null);
            fail("The specified idName is null.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getNextID, the specified idName does not exist,  a NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID2() throws Exception {
        try {
            gen1.getNextID("non-exist");
            fail("The specified idName does not exist.");
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
    public void testGetNextID3() throws Exception {
        // fortunatedly, the id is different from the other IDNames
        long id = gen1.getNextID(FIRST_TEST_BEAN);

        // test times
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            long nextid = gen1.getNextID(FIRST_TEST_BEAN);

            if (nextid != (id + 1 + i)) {
                fail("The id generated is - " + nextid + " which should be - " + (id + 1 + i));
            }
        }
    }


    /**
     * Test the behaviour of getNextBigID, the specified idName is null,  a NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testgetNextBigID1() throws Exception {
        try {
            gen1.getNextBigID(null);
            fail("The specified idName is null.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getNextBigID, the specified idName does not exist,  a NoSuchIDSequenceException is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testgetNextBigID2() throws Exception {
        try {
            gen1.getNextBigID("non-exist");
            fail("The specified idName does not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getNextBigID. The block size of IDGenerator("foo") is 10, call getNextBigID 1000 times will
     * cause it read and update the database at least 100 times. A little bit like stress test.
     *
     * @throws Exception to JUnit
     */
    public void testgetNextBigID3() throws Exception {
        // fortunatedly, the id is different from the other IDNames
        long id = gen1.getNextBigID(FIRST_TEST_BEAN).longValue();

        // test times
        final int callLimit = 1000;

        for (int i = 0; i < callLimit; ++i) {
            long nextid = gen1.getNextBigID(FIRST_TEST_BEAN).longValue();

            if (nextid != (id + 1 + i)) {
                fail("The id generated is - " + nextid + " which should be - " + (id + 1 + i));
            }
        }
    }

    /**
     * Test two session bean in multi-thread client. 15 threads running 300 iteration on two session bean calling the
     * getNextBigID on a single row (the same idName).
     *
     * @throws Exception to JUnit
     */
    public void testConcurrencyOfBean() throws Exception {
        final int maxBean = 2;
        IDGeneratorLocal[] gens = {gen1, gen2};

        // create 15 threads to test the concurrency
        final int maxThread = 15;
        WorkThread[] threads = new WorkThread[maxThread];

        for (int i = 0; i < threads.length; ++i) {
            threads[i] = new WorkThread(gens[i % maxBean], SECOND_TEST_BEAN);
            threads[i].start();
        }

        // wait for the thread to terminate
        for (int i = 0; i < threads.length; ++i) {
            try {
                threads[i].join();
            } catch (InterruptedException ignored) {
                // Ignore
            }
        }

        // check whether all the threads functions well
        for (int i = 0; i < threads.length; ++i) {
            if (threads[i].getThrowable() != null) {
                fail("Exception thrown while running the thread -> " + threads[i].getThrowable());
            }
        }

        // all the ids are unique
        assertTrue("No duplicate should occur", WorkThread.checkUnique());
    }

    /**
     * Helper class used to test the concurrency of SessionBean. No exception should be thrown while call the getNextID
     * on valid IDNames. And 300 iteration is also a small stress test of the stable of the SessionBean running at the
     * server side.
     */
    private static class WorkThread extends Thread {
        /** The iteration to call the getNextID on the session bean. */
        private static final int RUN_ITERATION = 300;

        /** A vector to store all the ids generated. */
        private static Vector idsGenerated = new Vector();

        /** The EJB object used to call the getNextID. */
        private IDGeneratorLocal gen;

        /** The idName on which the getNextID is called. */
        private String idName;

        /** The exception thrown while running. */
        private Throwable throwable;

        /**
         * Constructor of the WorkThread.
         *
         * @param gen the IDGenerator to call getNextID
         * @param idName the name of the id sequence
         */
        public WorkThread(IDGeneratorLocal gen, String idName) {
            this.gen = gen;
            this.idName = idName;
        }

        /**
         * Check if the elements generated in all threads are unique.
         *
         * @return true if duplicate occurs, false if all ids are unique
         */
        public static boolean checkUnique() {
            // single element
            if (idsGenerated.size() < 2) {
                return true;
            }

            // sort first to detect the duplication
            Collections.sort(idsGenerated);

            for (int i = 1; i < idsGenerated.size(); ++i) {
                if (idsGenerated.get(i).equals(idsGenerated.get(i - 1))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Return the exception thrown while running.
         *
         * @return the exception thrown while running
         */
        public Throwable getThrowable() {
            return throwable;
        }

        /**
         * Call the getNextID on the IDGenerator RUN_ITERATION times. No exception is expected while running it.
         */
        public void run() {
            try {
                for (int i = 0; i < RUN_ITERATION; ++i) {
                    idsGenerated.add(new Long(gen.getNextID(idName)));
                }
            } catch (Throwable t) {
                throwable = t;
            }
        }
    }
}
