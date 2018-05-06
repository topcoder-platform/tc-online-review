/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.naming.Context;

import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.jndi.MockContextFactory;

import com.topcoder.util.idgenerator.*;
import com.topcoder.util.idgenerator.ejb.IDGeneratorBean;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocal;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocalHome;


/**
 * This tests the situation that results in IDsExhaustedException thrown</p>
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDsExhaustedException extends TestCase {
    /* The id name 1 */
    private static final String ID_NAME1 = "accuracytests_exh_ex1";

    /* The id name 2 */
    private static final String ID_NAME2 = "accuracytests_exh_ex2";

    /* The id name 3 */
    private static final String ID_NAME3 = "accuracytests_exh_ex3";

    /* The id name 4 */
    private static final String ID_NAME4 = "accuracytests_exh_ex4";

    /* The id name 5 */
    private static final String ID_NAME5 = "accuracytests_exh_ex5";

    /* The id name 6 */
    private static final String ID_NAME6 = "accuracytests_exh_ex6";

    /* The block size */
    private static final long BLOCK_SIZE_NUM = 3;

    /* The number to start from */
    private static final long START_NUM = Long.MAX_VALUE - 2;

    public void testIDGeneratorFactory() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        IDGenerator gen = IDGeneratorFactory.getIDGenerator(ID_NAME1);

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }


        gen = IDGeneratorFactory.getIDGenerator(ID_NAME2);

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
    }

    public void testIDGeneratorImpl() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        IDGenerator gen = IDGeneratorFactory.getIDGenerator(ID_NAME3);

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }

        gen = IDGeneratorFactory.getIDGenerator(ID_NAME4);

        try {
            nextID = gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
    }

    public void testIDGeneratorEJB() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        MockContextFactory.setAsInitial();
        Context ctx = new InitialContext();
        MockContainer mockContainer = new MockContainer(ctx);
        SessionBeanDescriptor sampleServiceDescriptor =
            new SessionBeanDescriptor("IDGeneratorHome",
                    IDGeneratorLocalHome.class, IDGeneratorLocal.class,
                    new IDGeneratorBean());
        mockContainer.deploy(sampleServiceDescriptor);
        Object obj = ctx.lookup("IDGeneratorHome");
        IDGeneratorLocalHome home = (IDGeneratorLocalHome)
            PortableRemoteObject.narrow(obj, IDGeneratorLocalHome.class);
      
        com.topcoder.util.idgenerator.ejb.IDGeneratorLocal gen = home.create();

        /* Get some ids */
        for (long i = 0; i < BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID(ID_NAME5);

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }

        try {
            nextID = gen.getNextID(ID_NAME5);

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }

        try {
            nextID = gen.getNextID(ID_NAME6);

            // bad, exception not thrown
            fail("Should have thrown IDsExhaustedException");
        } catch (IDsExhaustedException e) {
            // good, caught as expected
        }
        MockContextFactory.revertSetAsInitial();
    }

}