/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator.failuretests;

import com.topcoder.util.idgenerator.IDGeneratorImpl;
import com.topcoder.util.idgenerator.IDsExhaustedException;
import com.topcoder.util.idgenerator.NoSuchIDSequenceException;

import junit.framework.TestCase;


/**
 * <p>
 * A failure test for <code>IDGeneratorImpl</code> class.
 * </p>
 *
 * @author cosherx
 * @version 3.0
 */
public class IDGeneratorImplFailureTest extends TestCase {
    /**
     * Test the constructor with null argument.
     *
     * @throws Exception to JUnit
     */
    public void testConstructor_NullIDName() throws Exception {
        try {
            new IDGeneratorImpl(null);
            fail("The specified idName is null");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
    }

    /**
     * Test the constructor if the idName is not exist.
     *
     * @throws Exception to JUnit
     */
    public void testConstructor_NonExistingIdName() throws Exception {
        try {
            new IDGeneratorImpl("non-exist");
            fail("The specified idName is not exist.");
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
    public void testGetNextID_Exhaust_1() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FailureTestHelper.default_exhaust_1);

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
    public void testGetNextID_Exhaust_2() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FailureTestHelper.default_exhaust_2);

        // the ids left is not sufficient
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }

    /**
     * Test the getNextBigID with IDsExhaustedException.  The idName with &quot;exhaust1&quot; will be retrieved. Its
     * exhausted flag is set to true. So exception is  expected from it when call getNextBigID.
     *
     * @throws Exception to JUnit
     */
    public void testGetNextBigID_Exhaust_1() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FailureTestHelper.default_exhaust_1);

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
    public void testGetNextBigID_Exhaust_2() throws Exception {
        IDGeneratorImpl gen1 = new IDGeneratorImpl(FailureTestHelper.default_exhaust_2);

        // the ids left is not sufficient
        try {
            gen1.getNextID();
            fail("The ids have been exhausted.");
        } catch (IDsExhaustedException e) {
            // good
        }
    }
}
