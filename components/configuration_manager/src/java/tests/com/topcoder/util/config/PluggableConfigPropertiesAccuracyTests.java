/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import com.topcoder.util.config.PluggableConfigProperties;

/**
 * <p>
 * Accuracy Unit test cases for PluggableConfigProperties.
 * </p>
 *
 * @author victorsam
 * @version 2.2
 */
public class PluggableConfigPropertiesAccuracyTests extends TestCase {
    /**
     * The config file.
     */
    private File file = null;

    /**
     * <p>PluggableConfigProperties instance for testing.</p>
     */
    private PluggableConfigProperties instance;

    /**
     * <p>Setup test environment.</p>
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("accuracytest", ".config", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("classname=com.topcoder.util.config.AccuracyPluggableConfigSource");
        writer.close();
        instance = new PluggableConfigProperties(file.toURL());
    }

    /**
     * <p>Tears down test environment.</p>
     *
     */
    protected void tearDown() {
        instance = null;
        if (file != null) {
            file.delete();
            file = null;
        }
        AccuracyPluggableConfigSource.clear();
    }

    /**
     * <p>Return all tests.</p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(PluggableConfigPropertiesAccuracyTests.class);
    }

    /**
     * <p>Tests PluggableConfigProperties#load() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testLoad() throws Exception {
        AccuracyPluggableConfigSource.setRefreshable(AccuracyPluggableConfigSource.IS_REFRESHABLE);
        Property root = new Property();
        instance.setRoot(root);
        instance.load();
        assertTrue("Failed to load correctly.", instance.isRefreshable());
    }

    /**
     * <p>Tests PluggableConfigProperties#save() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testSave() throws Exception {
        Property root = new Property();
        instance.setRoot(root);
        instance.setRefreshable(true);
        instance.save();
        assertEquals("Failed to save correctly.", "true", AccuracyPluggableConfigSource.getRoot().getProperty(
            "IsRefreshable").getValue());
    }

}