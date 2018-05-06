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

import com.topcoder.util.idgenerator.ejb.*;

/**
 * Tests public methods of IDGeneratorImpl class
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDGeneratorEJB extends TestCase {
    /* The id name */
    private static final String ID_NAME = "accuracytests_ejb";

    /* The number to start from */
    private static final long START_NUM = 400;

    /* The block size */
    private static final long BLOCK_SIZE_NUM = 50;

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
      
        IDGeneratorLocal gen = home.create();

        assertNotNull("The generator should not be null", gen);

        /* Get some ids */
        for (long i = 0; i < 2 * BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID(ID_NAME);

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }
        MockContextFactory.revertSetAsInitial();
    }


}
