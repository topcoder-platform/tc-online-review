/**
 *
 * Copyright © 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.stresstests;

import junit.framework.TestCase;

import java.io.File;

import com.topcoder.util.idgenerator.*;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>This test case aggregates all Stress test cases for ID Generator v2.</p>
 *
 * @author XuChuan
 * @version 3.0
 */
public class IDGeneratorFactoryStressTests extends TestCase {
    
    private final static String DB_FACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

	/**
	 * ConfigManager instance used for stress test
	 */
	ConfigManager configManager = null;

    /**
     * Setup the environment
     * @throws Exception if any unexpected exception occurs
     */
    protected void setUp() throws Exception {
        configManager = ConfigManager.getInstance();
        if (configManager.existsNamespace(DB_FACTORY_NAMESPACE)) {
            configManager.removeNamespace(DB_FACTORY_NAMESPACE);
        }
        configManager.add(new File("test_files/stresstest/DBConnectionFactoryImpl.xml").getAbsolutePath());
    }
    
    /**
     * Restore the environment
     * @throws Exception if any unexpected exception occurs
     */
    protected void tearDown() throws Exception {
        if (configManager.existsNamespace(DB_FACTORY_NAMESPACE)) {
            configManager.removeNamespace(DB_FACTORY_NAMESPACE);
        }
    }

    /**
     * Test the getIDGenerator method of the IDGeneratorFactory class
     * @throws Exception if any unexpected exception occurs
     */
    public void testGetIDGenerator() throws Exception {
        // initialization
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("test");  
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            assertEquals("The same instance expected", generator, IDGeneratorFactory.getIDGenerator("test"));
        }
        System.out.println("IDGeneratorFactory#getIDGenerator: 1000 invocation with the same argument takes "
            + (System.currentTimeMillis() - start) + " milliseconds");
    }
}
