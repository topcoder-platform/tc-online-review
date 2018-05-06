/*
 * Copyright (C) 2005, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator.failuretests;

import com.topcoder.util.idgenerator.IDsExhaustedException;
import com.topcoder.util.idgenerator.NoSuchIDSequenceException;
import com.topcoder.util.idgenerator.OracleSequenceGenerator;

import junit.framework.TestCase;


/**
 * <p>
 * A failure test for OracleSequenceGenerator class.
 * </p>
 *
 * @author cosherx
 * @version 3.0
 */
public class OracleSequenceGeneratorFailureTest extends TestCase {
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
    public void testConstructor_NullName() throws Exception {
        try {
            new OracleSequenceGenerator(null);
            fail("The specified idName is null");
        } catch (NoSuchIDSequenceException e) {
            // good
        }
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
    public void testGetNextID_Exhaust_1() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(FailureTestHelper.oracle_exhaust_1);

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
    public void testGetNextBigID_Exhaust_1() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(FailureTestHelper.oracle_exhaust_1);

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
    public void testGetNextID_Exhaust_2() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(FailureTestHelper.oracle_exhaust_2);

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
    public void testGetNextBigID_Exhaust_2() throws Exception {
        OracleSequenceGenerator gen1 = new OracleSequenceGenerator(FailureTestHelper.oracle_exhaust_2);

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
        OracleSequenceGenerator generator = new OracleSequenceGenerator(FailureTestHelper.oracle_sequence);

        try {
            generator.setSequenceName(null);
            fail("NullPointerException should be thrown.");
        } catch (NullPointerException e) {
            // ignore
        }
    }
}
