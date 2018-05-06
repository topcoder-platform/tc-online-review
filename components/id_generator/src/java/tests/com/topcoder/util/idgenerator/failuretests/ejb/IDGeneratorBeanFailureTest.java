/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator.failuretests.ejb;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import org.mockejb.MockContainer;
import org.mockejb.SessionBeanDescriptor;
import org.mockejb.jndi.MockContextFactory;

import junit.framework.TestCase;


import com.topcoder.util.idgenerator.ejb.IDGeneratorBean;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocal;
import com.topcoder.util.idgenerator.ejb.IDGeneratorLocalHome;
import com.topcoder.util.idgenerator.failuretests.FailureTestHelper;
import com.topcoder.util.idgenerator.NoSuchIDSequenceException;

/**
 * <p>A failure test for <code>IDGeneratorBean</code> class.</p>
 *
 * @author cosherx
 * @version 3.0
 */
public class IDGeneratorBeanFailureTest extends TestCase {
    /** The first instance of IDGenerator used to getNextID. */
    private IDGeneratorLocal gen;

    /**
     * Get a IDGenerator instance from JNDI.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        MockContextFactory.setAsInitial();
        Context ctx = new InitialContext();
        MockContainer mockContainer = new MockContainer(ctx);
        SessionBeanDescriptor sampleServiceDescriptor =
            new SessionBeanDescriptor(FailureTestHelper.idGeneratorHome,
                    IDGeneratorLocalHome.class, IDGeneratorLocal.class,
                    new IDGeneratorBean());
        mockContainer.deploy(sampleServiceDescriptor);
        
        Object obj = ctx.lookup(FailureTestHelper.idGeneratorHome);
        IDGeneratorLocalHome home = (IDGeneratorLocalHome) PortableRemoteObject.narrow(obj, IDGeneratorLocalHome.class);

        gen = home.create();
    }

    /**
     * Test the behaviour of getNextID, the specified idName is null,  a NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextID1() throws Exception {
        try {
            gen.getNextID(null);
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
            gen.getNextID("non-exist");
            fail("The specified idName does not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }


    /**
     * Test the behaviour of getNextBigID, the specified idName is null,  a NoSuchIDSequenceException is expected.
     *
     * @throws Exception to JUnit
     */
    public void testgetNextBigID1() throws Exception {
        try {
            gen.getNextBigID(null);
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
            gen.getNextBigID("non-exist");
            fail("The specified idName does not exist.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }
}
