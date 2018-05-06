/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the behavior of XMLConfigProperties with multiple namespaces.
 * </p>
 *
 * <p>
 * <em>Changes in 2.2:</em>
 * <ol>
 * <li>Updated test cases.</li>
 * </ol>
 * </p>
 *
 * @author WishingBone, sparemax
 * @version 2.2
 */
public class XMLConfigPropertiesMultipleTestCase extends TestCase {
    /**
     * The xml config file.
     */
    private File file = null;

    /**
     * The XMLConfigProperties to test on.
     */
    private XMLConfigProperties instance = null;

    /**
     * Set up testing environment. Creates a sample xml config file and constructs the XMLConfigProperties instance.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("unittest", ".xml", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <Config name=\"component a\">");
        writer.println("        <Property name=\"a\">");
        writer.println("            <Value>a</Value>");
        writer.println("        </Property>");
        writer.println("    </Config>");
        writer.println("    <Config name=\"component b\">");
        writer.println("        <ListDelimiter>_</ListDelimiter>");
        writer.println("        <IsRefreshable>true</IsRefreshable>");
        writer.println("        <Property name=\"b\">");
        writer.println("            <Value>b</Value>");
        writer.println("        </Property>");
        writer.println("        <Property name=\"b.c\">");
        writer.println("            <Value>c</Value>");
        writer.println("        </Property>");
        writer.println("    </Config>");
        writer.println("</CMConfig>");
        writer.close();
        instance = new XMLConfigProperties(file.toURL(), "component b");
    }

    /**
     * Tear down testing environment. Remove the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        instance = null;
        if (file != null) {
            file.delete();
            file = null;
        }
    }

    /**
     * Test create XMLConfigProperties with source and namespace.
     */
    public void testCreatePropConfigPropertiesSourceNamespace() {
        assertNotNull("'XMLConfigProperties' should be correct.", instance);
        // checks the properties are really loaded
        Property root = instance.getRoot();
        Property b = root.getProperty("b");
        Property bc = root.getProperty("b.c");
        assertNotNull("'XMLConfigProperties' should be correct.", b);
        assertTrue("'XMLConfigProperties' should be correct.", b.getValue().equals("b"));
        assertNotNull("'XMLConfigProperties' should be correct.", bc);
        assertTrue("'XMLConfigProperties' should be correct.", bc.getValue().equals("c"));
    }

    /**
     * Test load(). When the file is changed, load should be able to fetch a new copy of the properties.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad() throws Exception {
        instance.load();

        Property root = instance.getRoot();
        Property b = root.getProperty("b");
        assertTrue("'load' should be correct.", b.getValue().equals("b"));
        Property abc = root.getProperty("b.c");
        assertTrue("'load' should be correct.", abc.getValue().equals("c"));

        assertEquals("'load' should be correct.", '_', instance.getListDelimiter());
        assertTrue("'load' should be correct.", instance.isRefreshable());
    }

    /**
     * Test save(). The new property tree is saved to the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSave() throws Exception {
        // construct a new property tree
        Property root = new Property();
        root.setProperty("a", "value1");
        root.setProperty("b", "value2");
        root.setProperty("a.b.c", "newvalue");
        root.getProperty("a").addComment("newcomment");
        root.getProperty("a.b.c").addComment("comment2");
        instance.setRoot(root);

        instance.setListDelimiter('_');
        instance.setRefreshable(true);

        // now save it and verify the underlying file
        instance.save();

        String content = TestsHelper.readFile(file.getAbsolutePath());

        assertTrue("'Save' should be correct.", content.contains("    <Config name=\"component b\">"));
        assertTrue("'Save' should be correct.", content.contains("        <!--newcomment-->"));
        assertTrue("'Save' should be correct.", content.contains("        <Property name=\"a\">"));
        assertTrue("'Save' should be correct.", content.contains("            <Value>value1</Value>"));
        assertTrue("'Save' should be correct.", content.contains("            <Property name=\"b\">"));
        assertTrue("'Save' should be correct.", content.contains("        <!--comment2-->"));
        assertTrue("'Save' should be correct.", content.contains("                <Property name=\"c\">"));
        assertTrue("'Save' should be correct.", content.contains("                    <Value>newvalue</Value>"));
        assertTrue("'Save' should be correct.", content.contains("                </Property>"));
        assertTrue("'Save' should be correct.", content.contains("            </Property>"));
        assertTrue("'Save' should be correct.", content.contains("        </Property>"));
        assertTrue("'Save' should be correct.", content.contains("        <Property name=\"b\">"));
        assertTrue("'Save' should be correct.", content.contains("            <Value>value2</Value>"));
        assertTrue("'Save' should be correct.", content.contains("        </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <ListDelimiter>_</ListDelimiter>"));
        assertTrue("'Save' should be correct.", content.contains("    <IsRefreshable>true</IsRefreshable>"));
    }
}
