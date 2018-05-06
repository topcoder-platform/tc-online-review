/**
 * Copyright © 2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.generator.guid;

import java.util.HashSet;
import java.util.Set;

import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.idgenerator.NoSuchIDSequenceException;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Test the IDGeneratorAdapter class, using the IDGenerator component.
 * Please note that at this moment, those test will fail because IDGenerator doesn't provide the ability
 * to instantiante IDGeneratorAdapter.
 * At the one-time setup, it will verify that the Factory creates an instance of IDGeneratorAdapter.
 * </p>
 * <p>
 * It will also test:
 * </p>
 * <ul>
 * <li>the getIDName method, it should return a fixed class name</li>
 * <li>the testGetNextID method to see if it returns different long values</li>
 * </ul>
 * 
 * @author TCSDEVELOPER
 * @author TCSDESIGNER
 * @version 1.0
 */
public class IDGeneratorTests extends TestCase {
    
    /**
     * Static variable for testing.
     */
    private static IDGenerator generator; 

    /**
     * return a suite of tests.
     * 
     * @return a suite of tests
     */
    public static Test suite() {
        return new TestSetup(new TestSuite(IDGeneratorTests.class)) {
            // one time set up
            public void setUp() {
                try {
                    generator = IDGeneratorFactory.getIDGenerator(
                            "com.topcoder.util.generator.guid.IDGeneratorAdapter");
                } catch (NoSuchIDSequenceException e) {
                    e.printStackTrace();
                } catch (IDGenerationException e) {
                    e.printStackTrace();
                }
                assertTrue("bad instance returned", generator instanceof IDGeneratorAdapter);
            }
        };
        
    }

    /**
     * Test that getIDName is returning the expected class name.
     *  
     */
    public void testGetIDName() {
        assertEquals("bad ID Name", "com.topcoder.util.generator.guid.IDGeneratorAdapter", generator.getIDName());
    }


    /**
     * Test that the getNextID is returning different elements.
     * 
     * @throws IDGenerationException if a problem arises in the id generation
     *  
     */
    public void testGetNextID() throws IDGenerationException {
        Set set = new HashSet();
        int repeated = 0;
        for (int i = 0; i < 100000; i++) {
            if (!set.add(new Long(generator.getNextID()))) {
                repeated++;
            }
        }
        assertEquals("There are some elements repeated. However, it is not impossible that this happens."
                + "Please run the tests again.", 0, repeated);

     }


}