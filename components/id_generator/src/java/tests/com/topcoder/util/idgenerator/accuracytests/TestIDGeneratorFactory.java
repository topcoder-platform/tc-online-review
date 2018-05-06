/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import com.topcoder.util.idgenerator.*;

/**
 * Tests public methods of IDGeneratorFactory class
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDGeneratorFactory extends TestCase {
    /** The id name */
    private static final String ID_NAME = "accuracytests_factory";
    
    /** Dummy ID */
    private static final String DUMMY_ID = "my_dummy_implementation";
    
    /** Dummy implementation */
    private static final String DUMMY_CLASSNAME = "com.topcoder.util.idgenerator.accuracytests.MyIDGeneratorImpl";

    /* The number to start from */
    private static final long START_NUM = 0;

    /* The block size */
    private static final long BLOCK_SIZE_NUM = 10;

    public void testIDGeneratorFactory_String() throws Exception {
        long expectedID = START_NUM;
        long nextID;

        IDGenerator gen = IDGeneratorFactory.getIDGenerator(ID_NAME);

        assertNotNull("The generator should not be null", gen);
        assertEquals("The ID name should be " + ID_NAME, 
            ID_NAME, gen.getIDName());

        /* Get the generator by the same name again */
        IDGenerator gen2 = IDGeneratorFactory.getIDGenerator(ID_NAME);

        assertSame("The two generators are the same", gen, gen2);

        /* Get some ids */
        for (long i = 0; i < 10 * BLOCK_SIZE_NUM; i++, expectedID++) {
            nextID = gen.getNextID();

            assertEquals("The next id should be " + expectedID, 
                expectedID, nextID);
        }
    }
    
    public void testIDGeneratorFactory_String_String() throws Exception {
        IDGenerator gen = IDGeneratorFactory.getIDGenerator(DUMMY_ID, DUMMY_CLASSNAME);
        
        assertEquals("ID name should be " + DUMMY_ID,
                     gen.getIDName(),
                     DUMMY_ID);
                     
        IDGenerator gen2 = IDGeneratorFactory.getIDGenerator(DUMMY_ID);
        
        assertSame("Two dummy impletation should be the same.",
                   gen,
                   gen2);
        
    }
    

}