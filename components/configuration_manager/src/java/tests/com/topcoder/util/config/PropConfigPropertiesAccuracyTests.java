/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestSuite;
import junit.framework.Test;
import junit.framework.TestCase;
import com.topcoder.util.config.PropConfigProperties;

/**
 * <p>
 * Accuracy Unit test cases for PropConfigProperties.
 * </p>
 *
 * @author victorsam
 * @version 2.2
 */
public class PropConfigPropertiesAccuracyTests extends TestCase {
    /**
     * The file associated with the PropConfiProperties.
     */
    private File file = null;

    /**
     * <p>PropConfigProperties instance for testing.</p>
     */
    private PropConfigProperties instance;

    /**
     * <p>Setup test environment.</p>
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("accuracytest", ".properties", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("#comment1");
        writer.println("a=value1");
        writer.println("!comment2");
        writer.println("a.b.c=value3;value4");
        writer.println("b=value2");
        writer.close();
        instance = new PropConfigProperties(file.toURL());
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
    }

    /**
     * <p>Return all tests.</p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(PropConfigPropertiesAccuracyTests.class);
    }

    /**
     * <p>Tests PropConfigProperties#load() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testLoad() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("IsRefreshable=true");
        writer.close();
        instance.load();
        assertTrue("Failed to load correctly.", instance.isRefreshable());
    }

    /**
     * <p>Tests PropConfigProperties#save() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testSave() throws Exception {
        Property root = new Property();
        instance.setRoot(root);
        instance.setRefreshable(true);
        instance.save();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        assertTrue("Failed to save correctly.", reader.readLine().equals("IsRefreshable=true"));
        assertNull("Failed to save correctly.", reader.readLine());
        reader.close();
    }

}