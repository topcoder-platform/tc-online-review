/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.idgenerator.*;
import java.math.BigInteger;

/**
 * Tests public methods of IDGeneratorImpl class.
 *
 * @author garyk, RoyItaqi
 * @version 3.0
 */
public class TestIDGeneratorImpl extends TestCase {

    /** The id name used for normal generation. */
    private static final String ID_NAME_NORMAL = "accuracytests_TestIDGeneratorImpl_normal";

    /** The id name used for big integer generation. */
    private static final String ID_NAME_BIG = "accuracytests_TestIDGeneratorImpl_big";

    /** The first number in the sequence. */
    private static final long FIRST_NUM_NORMAL = 0;

    /** The last number in the sequence. */
    private static final long LAST_NUM_NORMAL = 99;

    /** The first number in the sequence. */
    private static final BigInteger FIRST_NUM_BIG = new BigInteger(Long.toString(FIRST_NUM_NORMAL));

    /** The last number in the sequence. */
    private static final BigInteger LAST_NUM_BIG = new BigInteger(Long.toString(LAST_NUM_NORMAL));
    

    public void testSequanceGeneration_normal() throws Exception {
        IDGenerator gen = new IDGeneratorImpl(ID_NAME_NORMAL);

        assertNotNull("The generator should not be null", gen);
        assertEquals("The ID name should be " + ID_NAME_NORMAL, 
                     ID_NAME_NORMAL, gen.getIDName());

        
        // Test sequence generation.
        for (long expectedID = FIRST_NUM_NORMAL; expectedID <= LAST_NUM_NORMAL; expectedID++) {
            long nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                         expectedID, nextID);
        }
    }

    public void testSequanceGeneration_big() throws Exception {
        IDGenerator gen = new IDGeneratorImpl(ID_NAME_BIG);

        assertNotNull("The generator should not be null", gen);
        assertEquals("The ID name should be " + ID_NAME_BIG, 
                     ID_NAME_BIG, gen.getIDName());

        
        // Test sequence generation.
        for (BigInteger expectedID = FIRST_NUM_BIG; expectedID.compareTo(LAST_NUM_BIG) != 1;
        expectedID = expectedID.add(BigInteger.ONE)) {
            BigInteger nextID = gen.getNextBigID();

            assertEquals("The next id should be " + expectedID, 
                         expectedID, nextID);
        }
    }


}
