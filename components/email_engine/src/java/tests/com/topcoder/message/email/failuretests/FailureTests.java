/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.message.email.failuretests;

import com.topcoder.util.config.ConfigManager;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>This test case aggregates all Failure test cases.</p>
 *
 * @author mgmg
 * @version 3.0
 */
public class FailureTests extends TestCase {
    /**
     * Aggregate all the tests.
     *
     * @return
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        suite.addTest(TCSEmailMessageFailureTest.suite());
        suite.addTest(EmailEngineFailureTest.suite());
        suite.addTest(EmailEngineV32FailureTest.suite());

        return suite;
    }

    /**
     * Clean up the ConfigManager.
     *
     * @throws Exception the exception to JUnit.
     */
    static void cleanConfiguration() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        List nameSpaces = new ArrayList();

        // iterate through all the namespaces and delete them.
        while (it.hasNext()) {
            nameSpaces.add(it.next());
        }

        for (int i = 0; i < nameSpaces.size(); i++) {
            cm.removeNamespace((String) nameSpaces.get(i));
        }
    }

    /**
     * Load the config file for the specified namespace.
     *
     * @param namespace the namespace to be loaded.
     * @param filename the config filename.
     */
    public static void loadConfig(String namespace, String filename) throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        if (cm.existsNamespace(namespace)) {
            cm.removeNamespace(namespace);
        }

        cm.add(namespace, filename, ConfigManager.CONFIG_XML_FORMAT);
    }
}
