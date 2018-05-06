/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.junit.Test;
import org.w3c.dom.Document;

/**
 * <p>
 * Tests the behavior of Helper.
 * </p>
 *
 * @author sparemax
 * @version 2.2
 * @since 2.2
 */
public class HelperTestCase extends TestCase {
    /**
     * <p>
     * Tests accuracy of <code>closeObj(Closeable obj)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_closeObj_1() throws Exception {
        FileInputStream obj = new FileInputStream(TestsHelper.TEST_FILES + "config1.properties");

        Helper.closeObj(obj);

        try {
            obj.read();

            fail("IOException is expected.");
        } catch (IOException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>closeObj(Closeable obj)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_closeObj_2() {
        Helper.closeObj(null);

        // Good
    }

    /**
     * <p>
     * Tests failure of <code>loadProperties(URL url)</code> method with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_loadProperties_Error() throws Exception {
        URL url = new URL("http://www.\n.com");

        try {
            Helper.loadProperties(url);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>loadProperties(URL url)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_loadProperties() throws Exception {
        URL url = (new File(TestsHelper.TEST_FILES + "config1.properties")).toURL();

        Properties res = Helper.loadProperties(url);

        assertNotNull("'loadProperties' should be correct.", res);
    }

    /**
     * <p>
     * Tests failure of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method
     * with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj_Error1() throws Exception {
        Class<PluggableConfigSource> targetClass = PluggableConfigSource.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, "invalid_class");

        try {
            Helper.createObj(targetClass, properties, key);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method
     * with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj_Error2() throws Exception {
        Class<PluggableConfigSource> targetClass = PluggableConfigSource.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, String.class.getName());

        try {
            Helper.createObj(targetClass, properties, key);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method
     * with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj_Error3() throws Exception {
        Class<PluggableConfigSource> targetClass = PluggableConfigSource.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, MockPluggableConfigSource1.class.getName());

        try {
            Helper.createObj(targetClass, properties, key);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method
     * with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj_Error4() throws Exception {
        Class<PluggableConfigSource> targetClass = PluggableConfigSource.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, MockPluggableConfigSource2.class.getName());

        try {
            Helper.createObj(targetClass, properties, key);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method
     * with an error occurred.<br>
     * <code>ConfigManagerException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj_Error5() throws Exception {
        Class<PluggableConfigSource> targetClass = PluggableConfigSource.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, MockPluggableConfigSource3.class.getName());

        try {
            Helper.createObj(targetClass, properties, key);

            fail("ConfigManagerException is expected.");
        } catch (ConfigManagerException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>createObj(Class&lt;T&gt; targetClass, Properties properties, String key)</code> method.
     * <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_createObj() throws Exception {
        Class<ConfigManager> targetClass = ConfigManager.class;
        Properties properties = new Properties();
        String key = "key";

        properties.setProperty(key, DefaultConfigManager.class.getName());

        ConfigManager res = Helper.createObj(targetClass, properties, key);

        assertNotNull("'createObj' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>decodeURL(String url)</code> method. <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_decodeURL() throws Exception {
        String url = URLEncoder.encode("a bc", "UTF-8");

        String res = Helper.decodeURL(url);

        assertEquals("'decodeURL' should be correct.", "a bc", res);
    }

    /**
     * <p>
     * Tests failure of <code>getDocument(URL url)</code> method with an error occurred.<br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getDocument_Error() throws Exception {
        URL url = (new File(TestsHelper.TEST_FILES + "config1.properties")).toURL();

        try {
            Helper.getDocument(url);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>getDocument(URL url)</code> method. <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getDocument() throws Exception {
        URL url = (new File(TestsHelper.TEST_FILES + "SampleMultipleConfig.xml")).toURL();

        Document res = Helper.getDocument(url);

        assertNotNull("'getDocument' should be correct.", res);
    }

    /**
     * <p>
     * Tests failure of <code>getNamespaces(URL url)</code> method with an error occurred.<br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getNamespaces_Error() throws Exception {
        URL url = (new File(TestsHelper.TEST_FILES + "config1.properties")).toURL();

        try {
            Helper.getNamespaces(url);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>getNamespaces(URL url)</code> method. <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_getNamespaces() throws Exception {
        URL url = (new File(TestsHelper.TEST_FILES + "SampleMultipleConfig.xml")).toURL();

        Enumeration<String> res = Helper.getNamespaces(url);

        assertNotNull("'getNamespaces' should be correct.", res);
    }

    /**
     * <p>
     * Tests failure of <code>parseValueString(String value, char listDelimiter)</code> method with an error occurred.
     * <br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseValueString_Error1() throws Exception {
        String s = "abc\\";
        char listDelimiter = ';';

        try {
            Helper.parseValueString(s, listDelimiter);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>parseValueString(String value, char listDelimiter)</code> method with an error occurred.
     * <br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseValueString_Error2() throws Exception {
        String s = "abc\\u";
        char listDelimiter = ';';

        try {
            Helper.parseValueString(s, listDelimiter);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>parseValueString(String value, char listDelimiter)</code> method. <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseValueString() throws Exception {
        String s = "ab\nc;123";
        char listDelimiter = ';';

        List<String> res = Helper.parseValueString(s, listDelimiter);

        assertEquals("'parseValueString' should be correct.", 2, res.size());
    }

    /**
     * <p>
     * Tests failure of <code>parseString(String s)</code> method with an error occurred.<br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseString_Error1() throws Exception {
        String s = "abc\\";

        try {
            Helper.parseString(s);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests failure of <code>parseString(String s)</code> method with an error occurred.<br>
     * <code>ConfigParserException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseString_Error2() throws Exception {
        String s = "abc\\u";

        try {
            Helper.parseString(s);

            fail("ConfigParserException is expected.");
        } catch (ConfigParserException ioe) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>parseString(String s)</code> method. <br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_parseString() throws Exception {
        String s = "ab\nc";

        String res = Helper.parseString(s);

        assertEquals("'parseString' should be correct.", "ab\nc", res);
    }
}
