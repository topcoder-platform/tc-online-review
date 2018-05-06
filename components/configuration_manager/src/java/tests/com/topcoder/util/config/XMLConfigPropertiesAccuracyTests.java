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
import com.topcoder.util.config.XMLConfigProperties;

/**
 * <p>
 * Accuracy Unit test cases for XMLConfigProperties.
 * </p>
 *
 * @author victorsam
 * @version 2.2
 */
public class XMLConfigPropertiesAccuracyTests extends TestCase {
    /**
     * The xml config file.
     */
    private File file = null;

    /**
     * <p>XMLConfigProperties instance for testing.</p>
     */
    private XMLConfigProperties instance;

    /**
     * <p>Setup test environment.</p>
     * @throws Exception to JUnit
     *
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("accuracytest", ".xml", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <!--comment1-->");
        writer.println("    <Property name=\"a\">");
        writer.println("        <Value>value1</Value>");
        writer.println("    </Property>");
        writer.println("    <!--comment2-->");
        writer.println("    <Property name=\"a.b.c\">");
        writer.println("        <Value>value3</Value>");
        writer.println("        <Value>value4</Value>");
        writer.println("    </Property>");
        writer.println("    <Property name=\"b\">");
        writer.println("        <Value>value2</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        instance = new XMLConfigProperties(file.toURL());
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
        return new TestSuite(XMLConfigPropertiesAccuracyTests.class);
    }

    /**
     * <p>Tests XMLConfigProperties#load() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testLoad() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("<IsRefreshable>true</IsRefreshable>");
        writer.println("</CMConfig>");
        writer.close();
        instance.load();
        assertTrue("Failed to load correctly.", instance.isRefreshable());
    }

    /**
     * <p>Tests XMLConfigProperties#save() for accuracy.</p>
     * @throws Exception to JUnit
     */
    public void testSave() throws Exception {
        // construct a new property tree
        Property root = new Property();
        instance.setRefreshable(true);
        instance.setRoot(root);
        instance.save();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        assertEquals("<?xml version=\"1.0\"?>", reader.readLine());
        assertEquals("<CMConfig>", reader.readLine());
        assertEquals("    <IsRefreshable>true</IsRefreshable>", reader.readLine());
        assertEquals("</CMConfig>", reader.readLine());
        assertNull(reader.readLine());
        reader.close();

    }

}