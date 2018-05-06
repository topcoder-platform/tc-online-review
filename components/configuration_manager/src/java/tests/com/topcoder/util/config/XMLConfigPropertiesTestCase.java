/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * <p>
 * Tests the behavior of XMLConfigProperties.
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
public class XMLConfigPropertiesTestCase extends TestCase {

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
        writer.println("<!DOCTYPE rootElement>");
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
     * Test create XMLConfigProperties with source.
     */
    public void testCreatePropConfigPropertiesSource() {
        assertNotNull("'XMLConfigProperties' should be correct.", instance);
        // checks the properties are really loaded
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        Property b = root.getProperty("b");
        Property abc = root.getProperty("a.b.c");
        assertNotNull("'XMLConfigProperties' should be correct.", a);
        assertTrue("'XMLConfigProperties' should be correct.", a.getValue().equals("value1"));
        assertNotNull("'XMLConfigProperties' should be correct.", b);
        assertTrue("'XMLConfigProperties' should be correct.", b.getValue().equals("value2"));
        assertNotNull("'XMLConfigProperties' should be correct.", abc);
        assertTrue("'XMLConfigProperties' should be correct.", abc.getValues().length == 2);
        assertTrue("'XMLConfigProperties' should be correct.", abc.getValues()[0].equals("value3"));
        assertTrue("'XMLConfigProperties' should be correct.", abc.getValues()[1].equals("value4"));
        // the comments should also be in place
        List<String> comments = a.getComments();
        assertTrue("'XMLConfigProperties' should be correct.", comments.size() == 1);
        assertTrue("'XMLConfigProperties' should be correct.", comments.get(0).equals("comment1"));
        assertNull("'XMLConfigProperties' should be correct.", b.getComments());
        comments = abc.getComments();
        assertTrue("'XMLConfigProperties' should be correct.", comments.size() == 1);
        assertTrue("'XMLConfigProperties' should be correct.", comments.get(0).equals("comment2"));
    }

    /**
     * Tests set/get document.
     */
    public void testSetGetDocument() {
        Document document = instance.getDocument();
        instance.setDocument(null);
        assertNull("'setDocument' should be correct.", instance.getDocument());
        instance.setDocument(document);
        assertTrue("'getDocument' should be correct.", instance.getDocument() == document);
    }

    /**
     * Test save(). The new property tree is saved to the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSave_1() throws Exception {
        // construct a new property tree
        Property root = new Property();
        root.setProperty("a", "value1");
        root.setProperty("b", "value2");
        root.setProperty("a.b.c", "newvalue");
        root.getProperty("a").addComment("newcomment");
        root.getProperty("a.b.c").addComment("comment2");
        instance.setRoot(root);
        // now save it and verify the underlying file
        instance.save();

        String content = TestsHelper.readFile(file.getAbsolutePath());

        assertTrue("'Save' should be correct.", content.contains("<?xml version=\"1.0\"?>"));
        assertTrue("'Save' should be correct.", content.contains("<CMConfig>"));
        assertTrue("'Save' should be correct.", content.contains("    <!--newcomment-->"));
        assertTrue("'Save' should be correct.", content.contains("    <Property name=\"a\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>value1</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <!--comment2-->"));
        assertTrue("'Save' should be correct.", content.contains("    <Property name=\"c\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>newvalue</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <Property name=\"b\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>value2</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("</CMConfig>"));
    }

    /**
     * Test save(). The new property tree is saved to the file.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testSave_2() throws Exception {
        // construct a new property tree
        Property root = new Property();
        root.setProperty("a", "value1");
        root.setProperty("b", "value2");
        root.setProperty("a.b.c<>&\"\r\n", "newvalue");
        root.getProperty("a").addComment("newcomment");
        root.getProperty("a.b.c<>&\"\r\n").addComment("comment2");
        instance.setRoot(root);
        instance.setListDelimiter('_');
        instance.setRefreshable(true);
        // now save it and verify the underlying file
        instance.save();

        String content = TestsHelper.readFile(file.getAbsolutePath());

        assertTrue("'Save' should be correct.", content.contains("<?xml version=\"1.0\"?>"));
        assertTrue("'Save' should be correct.", content.contains("<CMConfig>"));
        assertTrue("'Save' should be correct.", content.contains("    <!--newcomment-->"));
        assertTrue("'Save' should be correct.", content.contains("    <Property name=\"a\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>value1</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <!--comment2-->"));
        assertTrue("'Save' should be correct.",
            content.contains("    <Property name=\"c&lt;&gt;&amp;&quot;&#13;&#10;\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>newvalue</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <Property name=\"b\">"));
        assertTrue("'Save' should be correct.", content.contains("        <Value>value2</Value>"));
        assertTrue("'Save' should be correct.", content.contains("    </Property>"));
        assertTrue("'Save' should be correct.", content.contains("    <ListDelimiter>_</ListDelimiter>"));
        assertTrue("'Save' should be correct.", content.contains("    <IsRefreshable>true</IsRefreshable>"));
        assertTrue("'Save' should be correct.", content.contains("</CMConfig>"));
    }

    /**
     * Test load(). When the file is changed, load should be able to fetch a new copy of the properties.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoad_1() throws Exception {
        // modify the file
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <!--newcomment-->");
        writer.println("    <Property name=\"a\">");
        writer.println("        <Value>value1</Value>");
        writer.println("    </Property>");
        writer.println("    <!--comment2-->");
        writer.println("    <Property name=\"a.b.c\">");
        writer.println("        <Value>newvalue</Value>");
        writer.println("    </Property>");
        writer.println("    <Property name=\"b\">");
        writer.println("        <Value>value2</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        // now load and verify the changes
        instance.load();
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        List<String> comments = a.getComments();
        assertTrue("'load' should be correct.", comments.size() == 1);
        assertTrue("'load' should be correct.", comments.get(0).equals("newcomment"));
        Property abc = root.getProperty("a.b.c");
        assertTrue("'load' should be correct.", abc.getValue().equals("newvalue"));

        assertEquals("'load' should be correct.", ';', instance.getListDelimiter());
        assertNull("'load' should be correct.", instance.isRefreshable());
    }

    /**
     * Test load(). When the file is changed, load should be able to fetch a new copy of the properties.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad_2() throws Exception {
        // modify the file
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <ListDelimiter>_</ListDelimiter>");
        writer.println("    <IsRefreshable>true</IsRefreshable>");
        writer.println("    <!--newcomment-->");
        writer.println("    <Property name=\"a\">");
        writer.println("        <Value>value1</Value>");
        writer.println("    </Property>");
        writer.println("    <!--comment2-->");
        writer.println("    <Property name=\"a.b.c\">");
        writer.println("        <Value>newvalue</Value>");
        writer.println("    </Property>");
        writer.println("    <Property name=\"b\">");
        writer.println("        <Value>value2</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        // now load and verify the changes
        instance.load();
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        List<String> comments = a.getComments();
        assertTrue("'load' should be correct.", comments.size() == 1);
        assertTrue("'load' should be correct.", comments.get(0).equals("newcomment"));
        Property abc = root.getProperty("a.b.c");
        assertTrue("'load' should be correct.", abc.getValue().equals("newvalue"));

        assertEquals("'load' should be correct.", '_', instance.getListDelimiter());
        assertTrue("'load' should be correct.", instance.isRefreshable());
    }

    /**
     * Test list delimiter. List delimiter should be handled.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadListDelimiter() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <ListDelimiter>%</ListDelimiter>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value1</Value>");
        writer.println("        <Value>value2%value3</Value>");
        writer.println("        <Value>value;4</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        instance.load();
        // verify
        Property root = instance.getRoot().getProperty("prop");
        assertTrue("'load' should be correct.", root.getValues().length == 4);
        assertTrue("'load' should be correct.", root.getValues()[0].equals("value1"));
        assertTrue("'load' should be correct.", root.getValues()[1].equals("value2"));
        assertTrue("'load' should be correct.", root.getValues()[2].equals("value3"));
        // default delimeter is overriden!
        assertTrue("'load' should be correct.", root.getValues()[3].equals("value;4"));
    }

    /**
     * Tests failure situation - no file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadFailureNoFile() throws Exception {
        file.delete();
        file = null;
        try {
            instance.load();
            fail("Should have thrown IOException");
        } catch (IOException ioe) {
            // Good
        }
    }

    /**
     * Tests failure situation - incorrect format.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadFailureIncorrectFormat() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <Foo name=\"a\">");
        writer.println("        <Bar>value1</Bar>");
        writer.println("    </Foo>");
        writer.println("</CMConfig>");
        writer.close();
        try {
            instance.load();
            fail("Should have thrown ConfigParserException");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * Tests failure situation - duplicate property name.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadFailureDuplicatePropertyName() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value1</Value>");
        writer.println("    </Property>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value2</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        try {
            instance.load();
            fail("Should have thrown ConfigParserException");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>load()</code> with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad_Error1() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <ListDelimiter>%;</ListDelimiter>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value1</Value>");
        writer.println("        <Value>value2%value3</Value>");
        writer.println("        <Value>value;4</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        try {
            instance.load();

            fail("ConfigManagerException is expected.");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>load()</code> with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad_Error2() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <ListDelimiter>%<a>;</a></ListDelimiter>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value1</Value>");
        writer.println("        <Value>value2%value3</Value>");
        writer.println("        <Value>value;4</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();
        try {
            instance.load();

            fail("ConfigManagerException is expected.");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test for the method <code>load()</code> with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad_Error3() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("<?xml version=\"1.0\"?>");
        writer.println("<CMConfig>");
        writer.println("    <ListDelimiter><a>%</a></ListDelimiter>");
        writer.println("    <Property name=\"prop\">");
        writer.println("        <Value>value1</Value>");
        writer.println("        <Value>value2%value3</Value>");
        writer.println("        <Value>value;4</Value>");
        writer.println("    </Property>");
        writer.println("</CMConfig>");
        writer.close();

        try {
            instance.load();

            fail("ConfigManagerException is expected.");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * Tests clone().
     */
    public void testClone() {
        instance.setRefreshable(true);

        XMLConfigProperties copy = (XMLConfigProperties) instance.clone();
        // verify the clone
        Property root = copy.getRoot();
        Property a = root.getProperty("a");
        Property b = root.getProperty("b");
        Property abc = root.getProperty("a.b.c");
        assertNotNull("'clone' should be correct.", a);
        assertTrue("'clone' should be correct.", a.getValue().equals("value1"));
        assertNotNull("'clone' should be correct.", b);
        assertTrue(b.getValue().equals("value2"));
        assertNotNull("'clone' should be correct.", abc);
        assertTrue("'clone' should be correct.", abc.getValues().length == 2);
        assertTrue("'clone' should be correct.", abc.getValues()[0].equals("value3"));
        assertTrue("'clone' should be correct.", abc.getValues()[1].equals("value4"));
        // the comments should also be in place
        List<String> comments = a.getComments();
        assertTrue("'clone' should be correct.", comments.size() == 1);
        assertTrue("'clone' should be correct.", comments.get(0).equals("comment1"));
        assertNull("'clone' should be correct.", b.getComments());
        comments = abc.getComments();
        assertTrue("'clone' should be correct.", comments.size() == 1);
        assertTrue("'clone' should be correct.", comments.get(0).equals("comment2"));
        // the clone should not be a shallow copy
        root = instance.getRoot();
        assertTrue("'clone' should be correct.", root.getProperty("a") != a);
        assertTrue("'clone' should be correct.", root.getProperty("b") != b);
        assertTrue("'clone' should be correct.", root.getProperty("a.b.c") != abc);

        assertTrue("'clone' should be correct.", copy.isRefreshable());
    }

    /**
     * Tests the handler methods. These method should wrap the incoming exception and rethrow. Messages are logged to
     * standard error.
     */
    public void testHandlerMethods() {
        SAXParseException saxpe = new SAXParseException("unit test - this should appear on stderr", "a", "b", 1, 2);
        try {
            instance.warning(saxpe);
        } catch (SAXException saxe) {
            fail("No exception should be thrown.");
        }
        try {
            instance.error(saxpe);
            fail("Should have thrown SAXException");
        } catch (SAXException saxe) {
            assertTrue("'error' should be correct.", saxe.getMessage().indexOf(
                "unit test - this should appear on stderr") != -1);
        }
        try {
            instance.fatalError(saxpe);
            fail("Should have thrown SAXException");
        } catch (SAXException saxe) {
            assertTrue("'fatalError' should be correct.", saxe.getMessage().indexOf(
                "unit test - this should appear on stderr") != -1);
        }
    }

}
