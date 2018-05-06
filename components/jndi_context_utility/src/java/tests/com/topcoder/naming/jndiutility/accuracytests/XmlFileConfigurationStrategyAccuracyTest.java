/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.naming.jndiutility.accuracytests;

import java.io.File;
import java.util.Date;

import com.topcoder.naming.jndiutility.ConfigurationStrategy;
import com.topcoder.naming.jndiutility.configstrategy.XmlFileConfigurationStrategy;

import junit.framework.TestCase;

/**
 * Accuracy test for XmlFileConfigurationStrategy.
 *
 * @author KKD
 * @version 2.0
 */
public class XmlFileConfigurationStrategyAccuracyTest extends TestCase {
    /**
     * Represents separator of file.
     */
    private static final String FS = File.separator;

    /**
     * Name of configuration file used in tests.
     */
    private static final String XMLFILE = "test_files" + FS + "accuracytests"
            + FS + "XmlFileConfigurationStrategyAccuracyTest.xml";

    /**
     * tear down of the test.
     * @throws Exception exception to JUnit.
     */
    protected void tearDown() throws Exception {
        AccuracyTestHelper.clearConfig();
    }

    /**
     * test the Constructor method.
     * @throws Exception exception to JUnit.
     */
    public void testConstructorAccuracy() throws Exception {
        assertNotNull("instance should be created.",
                new XmlFileConfigurationStrategy(AccuracyTestHelper
                        .getFile(XMLFILE)));
        assertNotNull("instance should be created.",
                new XmlFileConfigurationStrategy(AccuracyTestHelper
                        .getInputStream(XMLFILE)));
    }

    /**
     * test the getProperty method.
     * @throws Exception exception to JUnit.
     */
    public void testgetPropertyAccuracy() throws Exception {
        ConfigurationStrategy test = new XmlFileConfigurationStrategy(
                AccuracyTestHelper.getFile(XMLFILE));
        assertEquals("invalid property value.", "value 1", test
                .getProperty("property1"));
        assertEquals("invalid property value.", "  ", test
                .getProperty("property2"));
        test = new XmlFileConfigurationStrategy(AccuracyTestHelper
                .getInputStream(XMLFILE));
        assertEquals("invalid property value.", "value 1", test
                .getProperty("property1"));
        assertEquals("invalid property value.", "  ", test
                .getProperty("property2"));
    }

    /**
     * test the setProperty method.
     * @throws Exception exception to JUnit.
     */
    public void testsetPropertyAccuracy() throws Exception {
        ConfigurationStrategy test = new XmlFileConfigurationStrategy(
                AccuracyTestHelper.getFile(XMLFILE));
        String newValue = new Date().toString();
        test.setProperty("property1", newValue);

        assertEquals("temporary property value should be return.", newValue,
                test.getProperty("property1"));

        test.setProperty("property4", newValue);
        assertEquals("temporary property value should be return.", newValue,
                test.getProperty("property4"));
    }

    /**
     * test the commitChanges method.
     * @throws Exception exception to JUnit.
     */
    public void testcommitChangesAccuracy() throws Exception {
        ConfigurationStrategy test = new XmlFileConfigurationStrategy(
                AccuracyTestHelper.getFile(XMLFILE));
        String newValue = new Date().toString();
        test.setProperty("property3", newValue);
        test.setProperty("property4", newValue);
        test.commitChanges();

        assertEquals("invalid property value.", newValue, test
                .getProperty("property4"));
        assertEquals("invalid property value.", newValue, test
                .getProperty("property3"));
    }
}
