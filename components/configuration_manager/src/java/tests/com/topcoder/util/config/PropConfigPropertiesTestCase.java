/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the behavior of PropConfigProperties.
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
public class PropConfigPropertiesTestCase extends TestCase {
    /**
     * The file associated with the PropConfiProperties.
     */
    private File file = null;

    /**
     * The PropConfigProperties instance.
     */
    private PropConfigProperties instance = null;

    /**
     * Set up testing environment. Creates a temporary file and populate it with data. Then create PropConfigProperties
     * and load data.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("unittest", ".properties", new File("test_files"));
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
     * Test create PropConfigProperties with source.
     */
    public void testCreatePropConfigPropertiesSource() {
        assertNotNull("'PropConfigProperties' should be correct.", instance);
        // checks the properties are really loaded
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        Property b = root.getProperty("b");
        Property abc = root.getProperty("a.b.c");
        assertNotNull("'PropConfigProperties' should be correct.", a);
        assertTrue(a.getValue().equals("value1"));
        assertNotNull("'PropConfigProperties' should be correct.", b);
        assertTrue("'PropConfigProperties' should be correct.", b.getValue().equals("value2"));
        assertNotNull("'PropConfigProperties' should be correct.", abc);
        assertTrue("'PropConfigProperties' should be correct.", abc.getValues().length == 2);
        assertTrue("'PropConfigProperties' should be correct.", abc.getValues()[0].equals("value3"));
        assertTrue("'PropConfigProperties' should be correct.", abc.getValues()[1].equals("value4"));
        // the comments should also be in place
        List<String> comments = a.getComments();
        assertTrue("'PropConfigProperties' should be correct.", comments.size() == 1);
        assertTrue("'PropConfigProperties' should be correct.", comments.get(0).equals("#comment1"));
        assertNull("'PropConfigProperties' should be correct.", b.getComments());
        comments = abc.getComments();
        assertTrue("'PropConfigProperties' should be correct.", comments.size() == 1);
        assertTrue("'PropConfigProperties' should be correct.", comments.get(0).equals("!comment2"));
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
        root.getProperty("a").addComment("#newcomment");
        root.getProperty("a.b.c").addComment("!comment2");
        instance.setRoot(root);
        // now save it and verify the underlying file
        instance.save();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        assertTrue("'save' should be correct.", reader.readLine().equals("#newcomment"));
        assertTrue("'save' should be correct.", reader.readLine().equals("a=value1"));
        assertTrue("'save' should be correct.", reader.readLine().equals("!comment2"));
        assertTrue("'save' should be correct.", reader.readLine().equals("a.b.c=newvalue"));
        assertTrue("'save' should be correct.", reader.readLine().equals("b=value2"));
        assertNull("'save' should be correct.", reader.readLine());
        reader.close();
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
        root.setProperty("a.b.c", "newvalue");
        root.getProperty("a").addComment("#newcomment");
        root.getProperty("a.b.c").addComment("!comment2");
        instance.setRoot(root);
        instance.setListDelimiter('_');
        instance.setRefreshable(true);
        // now save it and verify the underlying file
        instance.save();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        assertTrue("'save' should be correct.", reader.readLine().equals("ListDelimiter=_"));
        assertTrue("'save' should be correct.", reader.readLine().equals("IsRefreshable=true"));
        assertTrue("'save' should be correct.", reader.readLine().equals("#newcomment"));
        assertTrue("'save' should be correct.", reader.readLine().equals("a=value1"));
        assertTrue("'save' should be correct.", reader.readLine().equals("!comment2"));
        assertTrue("'save' should be correct.", reader.readLine().equals("a.b.c=newvalue"));
        assertTrue("'save' should be correct.", reader.readLine().equals("b=value2"));
        assertNull("'save' should be correct.", reader.readLine());
        reader.close();
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
        writer.println("#newcomment");
        writer.println("a=value1");
        writer.println("!comment2");
        writer.println("a.b.c=newvalue");
        writer.println("b=value2");
        writer.close();
        // now load and verify the changes
        instance.load();
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        List<String> comments = a.getComments();
        assertTrue("'load' should be correct.", comments.size() == 1);
        assertTrue("'load' should be correct.", comments.get(0).equals("#newcomment"));
        Property abc = root.getProperty("a.b.c");
        assertTrue("'load' should be correct.", abc.getValue().equals("newvalue"));
    }

    /**
     * Test load(). When the file is changed, load should be able to fetch a new copy of the properties.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoad_2() throws Exception {
        // modify the file
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("#newcomment");
        writer.println("a=value1");
        writer.println("!comment2");
        writer.println("a.b.c=newvalue");
        writer.println("b=value2");
        writer.println("ListDelimiter=_");
        writer.println("IsRefreshable=true");
        writer.close();
        // now load and verify the changes
        instance.load();
        Property root = instance.getRoot();
        Property a = root.getProperty("a");
        List<String> comments = a.getComments();
        assertTrue("'load' should be correct.", comments.size() == 1);
        assertTrue("'load' should be correct.", comments.get(0).equals("#newcomment"));
        Property abc = root.getProperty("a.b.c");
        assertTrue("'load' should be correct.", abc.getValue().equals("newvalue"));

        assertEquals("'load' should be correct.", '_', instance.getListDelimiter());
        assertTrue("'load' should be correct.", instance.isRefreshable());
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
     * Tests failure situation - empty property name.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadFailureEmptyPropertyName() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("   =value");
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
        writer.println("prop=value1");
        writer.println("prop=value2");
        writer.close();
        try {
            instance.load();
            fail("Should have thrown ConfigParserException");
        } catch (ConfigParserException cpe) {
            // Good
        }
    }

    /**
     * Tests clone().
     */
    public void testClone() {
        instance.setRefreshable(true);

        PropConfigProperties copy = (PropConfigProperties) instance.clone();
        // verify the clone
        Property root = copy.getRoot();
        Property a = root.getProperty("a");
        Property b = root.getProperty("b");
        Property abc = root.getProperty("a.b.c");
        assertNotNull("'clone' should be correct.", a);
        assertTrue("'clone' should be correct.", a.getValue().equals("value1"));
        assertNotNull("'clone' should be correct.", b);
        assertTrue("'clone' should be correct.", b.getValue().equals("value2"));
        assertNotNull("'clone' should be correct.", abc);
        assertTrue("'clone' should be correct.", abc.getValues().length == 2);
        assertTrue("'clone' should be correct.", abc.getValues()[0].equals("value3"));
        assertTrue("'clone' should be correct.", abc.getValues()[1].equals("value4"));
        // the comments should also be in place
        List<String> comments = a.getComments();
        assertTrue("'clone' should be correct.", comments.size() == 1);
        assertTrue("'clone' should be correct.", comments.get(0).equals("#comment1"));
        assertNull("'clone' should be correct.", b.getComments());
        comments = abc.getComments();
        assertTrue("'clone' should be correct.", comments.size() == 1);
        assertTrue("'clone' should be correct.", comments.get(0).equals("!comment2"));
        // the clone should not be a shallow copy
        root = instance.getRoot();
        assertTrue("'clone' should be correct.", root.getProperty("a") != a);
        assertTrue("'clone' should be correct.", root.getProperty("b") != b);
        assertTrue("'clone' should be correct.", root.getProperty("a.b.c") != abc);

        assertTrue("'clone' should be correct.", copy.isRefreshable());
    }

}
