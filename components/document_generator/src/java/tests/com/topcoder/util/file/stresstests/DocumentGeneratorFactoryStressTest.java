/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.stresstests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.DocumentGeneratorConfigurationException;
import com.topcoder.util.file.DocumentGeneratorFactory;

/**
 * The Class DocumentGeneratorFactoryStressTest.
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DocumentGeneratorFactoryStressTest extends TestCase {

    /** The configuration. */
    private ConfigurationObject configuration;

    /**
     * Suite.
     *
     * @return the test suite
     */
    public static Test suite() {

        return new TestSuite(DocumentGeneratorFactoryStressTest.class);
    }

    /**
     * Sets up the environment for the TestCase.
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigurationFileManager cfManager = new ConfigurationFileManager("DocumentManager.xml");
        configuration = cfManager.getConfiguration("myconfig").getChild("myconfig");
    }

    /**
     * Tears down the environment for the TestCase.
     *
     * @throws Exception
     *             throw exception to JUnit.
     */
    protected void tearDown() throws Exception {
        configuration = null;
    }

    /**
     * Test get document generator.
     *
     * @throws Exception
     *             to junit
     */
    public void testGetDocumentGenerator() throws Exception {

        ThreadGroup threadGroup = new ThreadGroup("group1");
        long time1 = System.currentTimeMillis();
        for (int i = 0; i < 5; i++) {
            new Thread(threadGroup, runGetDocumentGenerator()).start();

        }
        long time2 = 0;
        while (true) {

            int activeThreadNum = threadGroup.activeCount();
            if (activeThreadNum == 0) {
                time2 = System.currentTimeMillis();
                threadGroup.destroy();
                break;
            }
        }

        System.out.println("The DocumentGeneratorFactory::getDocumentGenerator() stress test:"
            + " the 5 threads run. each thread run 100 times. the total time " + (time2 - time1) + "s");
    }

    /**
     * Run get document generator.
     *
     * @return the runnable
     */
    private Runnable runGetDocumentGenerator() {
        return new Runnable() {
            public void run() {
                for (int i = 0; i < 100; i++) {
                    try {
                        DocumentGenerator generator = DocumentGeneratorFactory
                            .getDocumentGenerator(configuration);
                        assertNotNull("the object is null.", generator);
                    } catch (DocumentGeneratorConfigurationException e) {
                        fail("The stress is failed.");
                    }
                }
            }
        };
    }

}
