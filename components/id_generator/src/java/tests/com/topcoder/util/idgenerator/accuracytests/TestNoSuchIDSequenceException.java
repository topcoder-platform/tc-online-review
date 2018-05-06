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
 * This tests the situation that results in NoSuchIDSequenceException thrown</p>
 *
 * @author garyk
 * @version 2.0
 */
public class TestNoSuchIDSequenceException extends TestCase {
    /* The id name */
    private static final String ID_NAME = "accuracytests_no_id_ex";

    public void testIDGeneratorFactory() throws Exception {

        try {
            IDGeneratorFactory.getIDGenerator(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            IDGeneratorFactory.getIDGenerator(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
    }

    public void testIDGeneratorImpl() throws Exception {

        try {
            new IDGeneratorImpl(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            new IDGeneratorImpl(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
    }

    public void testIDGeneratorEJB() throws Exception {
        
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

        try {
            gen.getNextID(ID_NAME);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }

        try {
            gen.getNextID(null);

            // bad, exception not thrown
            fail("Should have thrown NoSuchIDSequenceException");
        } catch (NoSuchIDSequenceException e) {
            // good, caught as expected            
        }
        MockContextFactory.revertSetAsInitial();
    }

}