/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator.failuretests;

import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.idgenerator.IDsExhaustedException;
import com.topcoder.util.idgenerator.NoSuchIDSequenceException;

import junit.framework.TestCase;


/**
 * <p>
 * A failure test for <code>IDGeneratorFactory</code> class.
 * </p>
 *
 * @author cosherx
 * @version 3.0
 */
public class IDGeneratorFactoryFailureTest extends TestCase {
    /**
     * Test the behaviour of getIDGenerator. the given idName does not exist in the database, NoSuchIDSequenceException
     * is expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator_NonExisting() throws Exception {
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
    public void testGetIDGenerator_NullName() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(null);
            fail("The specified idName is null.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the getNextID with IDsExhaustedException.  The idName with &quot;exhaust1&quot; will be retrieved. Its
     * exhausted flag is set to true. So exception is  expected from it when call getNextID.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator_Exhaust_1() throws Exception {
        IDGenerator gen = IDGeneratorFactory.getIDGenerator(FailureTestHelper.default_exhaust_1);

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
    public void testGetIDGenerator_Insufficient() throws Exception {
        IDGenerator gen = IDGeneratorFactory.getIDGenerator(FailureTestHelper.default_exhaust_2);

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
    public void testGetIDGenerator_Nonexisting() throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(null, "com.topcoder.util.idgenerator.OracleSequenceGenerator");
            fail("NoSuchIDSequenceException should be throw.");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the behaviour of getIDGenerator. the given implClassName cannot be found, ClassNotFoundException is
     * expected.
     *
     * @throws Exception to JUnit
     */
    public void testGetIDGenerator_NonExistingImplClass()
        throws Exception {
        try {
            IDGeneratorFactory.getIDGenerator(FailureTestHelper.default_sequence, "NotExisting");
            fail("ClassNotFoundException should be throw.");
        } catch (ClassNotFoundException e) {
            // good
        }
    }
}
