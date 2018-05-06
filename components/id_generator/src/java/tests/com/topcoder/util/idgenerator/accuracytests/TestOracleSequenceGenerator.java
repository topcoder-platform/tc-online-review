/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.idgenerator.*;
import java.math.BigInteger;

/**
 * Tests public methods of OracleSequenceGenerator class.
 *
 * @author garyk, RoyItaqi
 * @version 3.0
 */
public class TestOracleSequenceGenerator extends TestCase {
    
    /** The id name used for normal generation. */
    private static final String ID_NAME_NORMAL = "accuracytests_TestOracleSequenceGenerator_normal";

    /** The id name used for big integer generation. */
    private static final String ID_NAME_BIG = "accuracytests_TestOracleSequenceGenerator_big";

    /** The sequence name for normal generation. */
    private static final String SEQ_NAME_NORMAL = "ACCURACY_ORACLE_NORMAL";
   
    /** The sequence name for big integer generation. */
    private static final String SEQ_NAME_BIG = "ACCURACY_ORACLE_BIG";
  
    /** The first number in the sequence. */
    private static final long FIRST_NUM_NORMAL = 10;

    /** The last number in the sequence. */
    private static final long LAST_NUM_NORMAL = 100;

    /** The first number in the sequence. */
    private static final BigInteger FIRST_NUM_BIG = new BigInteger(Long.toString(100));

    /** The last number in the sequence. */
    private static final BigInteger LAST_NUM_BIG = new BigInteger(Long.toString(200));
    

    public void testSequenceGeneration_normal() throws Exception {
        OracleSequenceGenerator gen = new OracleSequenceGenerator(ID_NAME_NORMAL);

        assertNotNull("The generator should not be null", gen);
        assertEquals("The ID name should be " + ID_NAME_NORMAL, 
                     ID_NAME_NORMAL, gen.getIDName());

        gen.setSequenceName(SEQ_NAME_NORMAL);
        
        
        // Test sequence generation.
        for (long expectedID = FIRST_NUM_NORMAL; expectedID <= LAST_NUM_NORMAL; expectedID++) {
            long nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                         expectedID, nextID);
        }
    }

    public void testSequenceGeneration_big() throws Exception {
        OracleSequenceGenerator gen = new OracleSequenceGenerator(ID_NAME_BIG);

        assertNotNull("The generator should not be null", gen);
        assertEquals("The ID name should be " + ID_NAME_BIG, 
                     ID_NAME_BIG, gen.getIDName());

        gen.setSequenceName(SEQ_NAME_BIG);
        
        
        // Test sequence generation.
        for (BigInteger expectedID = FIRST_NUM_BIG; expectedID.compareTo(LAST_NUM_BIG) != 1;
                expectedID = expectedID.add(BigInteger.ONE)) {
            BigInteger nextID = gen.getNextBigID();

            assertEquals("The next id should be " + expectedID, 
                         expectedID, nextID);
        }
    }



}
