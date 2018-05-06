/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.idgenerator;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;

/**
 * Test for the OracleSequenceGenerator fix. 
 * 1) User don't need to own the sequence in order to access them.
 * 2) The sql statement used should be configurable via ConfigManager.
 *
 * @author Standlove
 * @version 3.0
 */
public class OracleSequenceGeneratorFixTest extends TestCase {
    /**
     * The namespace for OracleSequenceGenerator.
     */
    private static final String OSG_NS = "com.topcoder.util.idgenerator.OracleSequenceGenerator";

    /**
     * The namespace for DBConnectionFactoryImpl;
     */
    private static final String DF_NS = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";
    
    /**
     * The test directory.
     */
    private static final String TEST_DIR = "fixtest/";

    /**
     * The valid sequence name.
     */
    private static final String VALID_SEQ_NAME = "ID_SEQUENCE";

    /**
     * The valid db connection config file.
     */
    private static final String VALID_DF_CONFIG_FILE = "DBConnectionFactoryImpl.xml";
    
    /**
     * Valid oracle sequence config file.
     */
    private static final String VALID_OSG_CONFIG_FILE = "oracle.xml";

    /**
     * Clear config file before running test.
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        removeConfigFile(DF_NS);
        removeConfigFile(OSG_NS);
    }
    
    /**
     * Clear config file after test.
     *
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        removeConfigFile(DF_NS);
        removeConfigFile(OSG_NS);
        ConfigManager.getInstance().
            add("./com/topcoder/db/connectionfactory/DBConnectionFactoryImpl.xml");
    }

// Commented by kkkk9
// Since the constructor just set some string value, it will throw UnknownConfiguration Exception
// So these case are useless.

//    /**
//     * Test to load invalid db connection info, IDGenerationException is expected.
//     *
//     * @throws Exception to JUnit
//     */
//    public void testInvalidDBConnectionConfig() throws Exception {
//        loadConfigFile("invalid-connection-factory.xml");
//        try {
//            new OracleSequenceGenerator("any-name");
//            fail("the db connection factory configuration is invalid.");
//        } catch (IDGenerationException ex) {
//            // good
//        }
//    }
//    
//    /**
//     * Test to load invalid oracle sequence config file, IDGenerationException is expected.
//     * The SQL_SELECT_BLOCK_SIZE is invalid.
//     *
//     * @throws Exception to JUnit
//     */
//    public void testInvalidOracleConfig1() throws Exception {        
//        loadConfigFile(VALID_DF_CONFIG_FILE);
//        loadConfigFile("invalid-oracle1.xml");
//        try {
//            new OracleSequenceGenerator(VALID_SEQ_NAME);
//            fail("the oracle sequence configuration is invalid.");
//        } catch (IDGenerationException ex) {
//            // good
//        }
//    }
//    
//    /**
//     * Test to load invalid oracle sequence config file, IDGenerationException is expected.
//     * The SQL_NEXT_SEQUENCE_ID is invalid.
//     *
//     * @throws Exception to JUnit
//     */
//    public void testInvalidOracleConfig2() throws Exception {
//        loadConfigFile(VALID_DF_CONFIG_FILE);
//        loadConfigFile("invalid-oracle2.xml");
//        try {
//            new OracleSequenceGenerator(VALID_SEQ_NAME);
//            fail("the oracle sequence configuration is invalid.");
//        } catch (IDGenerationException ex) {
//            // good
//        }
//    }
    
    /**
     * Test with good config files. Sql statement loaded from the config file is used.
     *
     * @throws Exception to JUnit
     */
    public void testGoodOracleConfig() throws Exception {
        loadConfigFile(VALID_DF_CONFIG_FILE);
        loadConfigFile(VALID_OSG_CONFIG_FILE);
        IDGenerator gen = new OracleSequenceGenerator(VALID_SEQ_NAME);
        long id = gen.getNextID();
        System.out.println(id);
    }
    
    /**
     * Load config file.
     *
     * @param the config file name.
     *
     * @throws Exception to JUnit
     */
    private void loadConfigFile(String fileName) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        cm.add(TEST_DIR + fileName);
    }
    
    /**
     * Remove the config file.
     *
     * @param the namespace to remove.
     *
     * @throws Exception to JUnit
     */
    private static void removeConfigFile(String namespace) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        if (cm.existsNamespace(namespace)) {
            cm.removeNamespace(namespace);
        }
    }
}