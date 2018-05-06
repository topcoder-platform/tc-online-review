/*
 * Copyright (C) 2003, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the behavior of PluggableConfigProperties. The test cases use custom implementation of PluggableConfigSource
 * for testing.
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
public class PluggableConfigPropertiesTestCase extends TestCase {
    /**
     * The config file.
     */
    private File file = null;

    /**
     * The URL of the config file.
     */
    private URL url = null;

    /**
     * The PluggableConfigProperties instance to test.
     */
    private PluggableConfigProperties instance = null;

    /**
     * Set up testing environment. Make up a sample config file and construct PluggableConfigProperties with it.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        file = File.createTempFile("unittest", ".config", new File("test_files"));
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("prop1=value1");
        writer.println("classname=com.topcoder.util.config.CustomPluggableConfigSource");
        writer.println("prop2=value2");
        writer.close();
        url = file.toURL();
        instance = new PluggableConfigProperties(url);
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
        CustomPluggableConfigSource.clear();
    }

    /**
     * Tests create PluggableConfigProperties with file.
     */
    public void testCreatePluggableConfigPropertiesFile() {
        assertNotNull("'PluggableConfigProperties' should be correct.", instance);
        // verify the source has been configured
        Properties props = CustomPluggableConfigSource.getProps();
        assertNotNull("'PluggableConfigProperties' should be correct.", props);
        assertTrue("'PluggableConfigProperties' should be correct.",
            props.size() == 3);
        assertTrue("'PluggableConfigProperties' should be correct.",
            props.getProperty("prop1").equals("value1"));
        assertTrue("'PluggableConfigProperties' should be correct.",
            props.getProperty("prop2").equals("value2"));
        assertTrue("'PluggableConfigProperties' should be correct.",
            props.getProperty("classname").equals("com.topcoder.util.config.CustomPluggableConfigSource"));
        // verify the property tree is loaded
        Property root = instance.getRoot();
        assertTrue("'PluggableConfigProperties' should be correct.", root.list().size() == 3);
        assertTrue("'PluggableConfigProperties' should be correct.", root.getValue("prop1").equals("value1"));
        assertTrue("'PluggableConfigProperties' should be correct.", root.getValue("prop2").equals("value2"));
        assertTrue("'PluggableConfigProperties' should be correct.", root.getValue("prop3").equals("value3"));
    }

    /**
     * Tests create failure sitaution - no file.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateFailureNoFile() throws Exception {
        file.delete();
        try {
            instance = new PluggableConfigProperties(url);
            fail("Should have thrown IOExceptoin");
        } catch (IOException ioe) {
            // Good
        } finally {
            file = null;
        }
    }

    /**
     * Tests create failure situation - no class name.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateFailureNoClassName() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("prop1=value1");
        writer.println("prop2=value2");
        writer.close();
        try {
            instance = new PluggableConfigProperties(url);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException cme) {
            // Good
        }
    }

    /**
     * Tests create failure situation - class not found.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateFailureClassNotFound() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("classname=com.nonexist.ConfigSource");
        writer.close();
        try {
            instance = new PluggableConfigProperties(url);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException cme) {
            // Good
        }
    }

    /**
     * Tests create failure situation - incorrect class.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCreateFailureIncorrectClass() throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.println("classname=java.util.HashMap");
        writer.close();
        try {
            instance = new PluggableConfigProperties(url);
            fail("Should have thrown ConfigManagerException");
        } catch (ConfigManagerException cme) {
            // Good
        }
    }

    /**
     * Tests create failure situation - underlying exception. Underlying exception is propogated.
     */
    public void testCreateFailureUnderlyingException() {
        CustomPluggableConfigSource.setExceptional(true);
        try {
            instance = new PluggableConfigProperties(url);
            fail("Should have thrown IOException");
        } catch (IOException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests save(). Tests whether the property tree is saved to the underlying source.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testSave_1() throws Exception {
        Property root = new Property();
        instance.setRoot(root);

        instance.setRefreshable(null);

        instance.save();

        Property res = CustomPluggableConfigSource.getRoot();

        assertNull("'save' should be correct.", res.getProperty(Helper.KEY_REFRESHABLE));
        assertNull("'save' should be correct.", root.getProperty(Helper.KEY_REFRESHABLE));
    }

    /**
     * <p>
     * Tests save(). Tests whether the property tree is saved to the underlying source.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testSave_2() throws Exception {
        Property root = new Property();
        instance.setRoot(root);
        instance.setRefreshable(true);
        instance.save();

        Property res = CustomPluggableConfigSource.getRoot();

        assertEquals("'save' should be correct.", "true", res.getProperty(Helper.KEY_REFRESHABLE).getValue());
        assertNull("'save' should be correct.", root.getProperty(Helper.KEY_REFRESHABLE));

        instance.setRefreshable(false);
        instance.save();
        res = CustomPluggableConfigSource.getRoot();

        assertEquals("'save' should be correct.", "false", res.getProperty(Helper.KEY_REFRESHABLE).getValue());
        assertNull("'save' should be correct.", root.getProperty(Helper.KEY_REFRESHABLE));
    }

    /**
     * Tests save(). Tests failure situation - underlying exception.
     */
    public void testSaveFailureUnderlyingException() {
        CustomPluggableConfigSource.setExceptional(true);
        try {
            instance.save();
            fail("Should have thrown IOException");
        } catch (IOException ioe) {
            // Good
        }
    }

    /**
     * Tests load(). Tests whether the properties is loaded.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 2.2
     */
    public void testLoad() throws Exception {
        instance.load();

        assertTrue("'load' should be correct.", instance.isRefreshable());
    }

    /**
     * Tests load(). Tests if exception is propagated.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testLoadFailureUnderlyingException() throws Exception {
        CustomPluggableConfigSource.setExceptional(true);
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

        PluggableConfigProperties copy = (PluggableConfigProperties) instance.clone();
        // verify the clone
        Property root = copy.getRoot();
        Property prop1 = root.getProperty("prop1");
        Property prop2 = root.getProperty("prop2");
        Property prop3 = root.getProperty("prop3");
        assertNotNull("'clone' should be correct.", prop1);
        assertTrue("'clone' should be correct.", prop1.getValue().equals("value1"));
        assertNotNull("'clone' should be correct.", prop2);
        assertTrue("'clone' should be correct.", prop2.getValue().equals("value2"));
        assertNotNull("'clone' should be correct.", prop3);
        assertTrue("'clone' should be correct.", prop3.getValue().equals("value3"));
        // the clone should not be a shallow copy
        root = instance.getRoot();
        assertTrue("'clone' should be correct.", root.getProperty("prop1") != prop1);
        assertTrue("'clone' should be correct.", root.getProperty("prop2") != prop2);
        assertTrue("'clone' should be correct.", root.getProperty("prop3") != prop3);

        assertTrue("'clone' should be correct.", copy.isRefreshable());
    }
}

/**
 * Custom implementation of PluggableConfigSource for testing purpose only.
 *
 * @author WishingBone
 * @version 2.1
 */
class CustomPluggableConfigSource implements PluggableConfigSource {

    /**
     * Whether to throw exception on method invocation.
     */
    private static boolean exceptional = false;

    /**
     * The configure properties.
     */
    private static Properties props = null;

    /**
     * The saved root of the property tree.
     */
    private static Property root = null;

    /**
     * Set whether to throw exception on method invocation.
     *
     * @param exceptional
     *            whether to throw exception on method invocation.
     */
    public static void setExceptional(boolean exceptional) {
        CustomPluggableConfigSource.exceptional = exceptional;
    }

    /**
     * Get the configure properties.
     *
     * @return the configure properties.
     */
    public static Properties getProps() {
        return props;
    }

    /**
     * Get the saved root of the property tree.
     *
     * @return the saved root of the property tree.
     */
    public static Property getRoot() {
        return root;
    }

    /**
     * Set fields to default.
     */
    public static void clear() {
        exceptional = false;
        props = null;
        root = null;
    }

    /**
     * Configure the source.
     *
     * @param props
     *            the properties to configure the source.
     * @throws ConfigParserException
     *             if it is unable to parse config.
     */
    public void configure(Properties props) throws ConfigParserException {
        if (exceptional) {
            throw new ConfigParserException();
        }
        CustomPluggableConfigSource.props = props;
    }

    /**
     * Save the property tree to underlying source.
     *
     * @param root
     *            the root of the property tree.
     * @throws IOException
     *             if underlying source is unable to save.
     */
    public void save(Property root) throws IOException {
        if (exceptional) {
            throw new IOException();
        }
        CustomPluggableConfigSource.root = (Property) root.clone();
    }

    /**
     * Get a list of properties from the underlying source.
     *
     * @return the properties.
     *
     * @throws ConfigParserException
     *             if any error occurs.
     */
    public List<Property> getProperties() throws ConfigParserException {
        if (exceptional) {
            throw new ConfigParserException();
        }
        List<Property> list = new ArrayList<Property>();
        list.add(new Property("prop1", "value1"));
        list.add(new Property("prop2", "value2"));
        list.add(new Property("prop3", "value3"));
        list.add(new Property(Helper.KEY_REFRESHABLE, "true"));
        return list;
    }
}
