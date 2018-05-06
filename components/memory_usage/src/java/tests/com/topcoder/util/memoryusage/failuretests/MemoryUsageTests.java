/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.memoryusage.failuretests;

import java.util.Iterator;

import junit.framework.TestCase;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.memoryusage.ConfigurationException;
import com.topcoder.util.memoryusage.JVMNotSupportedException;
import com.topcoder.util.memoryusage.MemoryUsage;
import com.topcoder.util.memoryusage.MemoryUsageAnalyzer;
import com.topcoder.util.memoryusage.analyzers.Sun12Analyzer;

/**
 * Tests the MemoryUsage class.
 *
 * @author Thinfox
 * @version 2.0
 */
public class MemoryUsageTests extends TestCase {

    /**
     * Represents the configuration file name.
     */
    private static final String CONFIG_FILE = "failure/Config.xml";

    /**
     * Represents the default namespace.
     */
    private static final String NAMESPACE = "com.topcoder.util.memoryusage.MemoryUsage";

    /**
     * Original JVM version.
     */
    private String jvmVersion;

    /**
     * Original Vendor.
     */
    private String vendor;

    /**
     * <p>
     * The default <code>MemoryUsage</code> instance on which to perform tests.
     * </p>
     */
    private MemoryUsage usage = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void setUp() throws Exception {
        jvmVersion = System.getProperty("java.class.version");
        vendor = System.getProperty("java.vendor");

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
        cm.add(CONFIG_FILE);

        usage = new MemoryUsage();
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception propogate exceptions to JUnit
     */
    public void tearDown() throws Exception {
        System.setProperty("java.class.version", jvmVersion);
        System.setProperty("java.vendor", vendor);

        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor of null string.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws ConfigurationException to JUnit.
     */
    public void testConstructorString_Null() throws ConfigurationException {
        try {
            new MemoryUsage(null);
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor of empty string.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws ConfigurationException to JUnit.
     */
    public void testConstructorString_Empty() throws ConfigurationException {
        try {
            new MemoryUsage("  ");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor with UnknownNamespace.
     * </p>
     * <p>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    public void testConstructorString_UnknownNamespace() {
        try {
            new MemoryUsage("Inexistant");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor with null analyzers.
     * </p>
     * <p>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    public void testConstructorString_MissingAnalyzers() {
        try {
            new MemoryUsage(NAMESPACE + ".MissingAnalyzers");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor with null analyzer.
     * </p>
     * <p>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    public void testConstructorString_MissingAnalyzer() {
        try {
            new MemoryUsage(NAMESPACE + ".MissingAnalyzer");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(String)</code> constructor with bad analyzer namespace.
     * </p>
     * <p>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    public void testConstructorString_BadNamespace() {
        try {
            // SpecificationConfigurationException
            new MemoryUsage(NAMESPACE + ".BadNamespace");
            fail("ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(MemoryUsageAnalyzer[], MemoryUsageAnalyzer, boolean)</code>
     * constructor with null array.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testConstructorMemoryUsageAnalyzerArrMemoryUsageAnalyzerBoolean_NullArray() {
        try {
            new MemoryUsage(null, new Sun12Analyzer(), false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(MemoryUsageAnalyzer[], MemoryUsageAnalyzer, boolean)</code>
     * constructor with empty array.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testConstructorMemoryUsageAnalyzerArrMemoryUsageAnalyzerBoolean_EmptyArray() {
        try {
            new MemoryUsage(new MemoryUsageAnalyzer[0], new Sun12Analyzer(), false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>MemoryUsage(MemoryUsageAnalyzer[], MemoryUsageAnalyzer, boolean)</code>
     * constructor with null array item.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    public void testConstructorMemoryUsageAnalyzerArrMemoryUsageAnalyzerBoolean_NullArrayItem() {
        try {
            new MemoryUsage(new MemoryUsageAnalyzer[] {new Sun12Analyzer(), null},
                new Sun12Analyzer(), false);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getShallowMemoryUsage(Object)</code> method.
     * </p>
     * <p>
     * <code>JVMNotSupportedException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetShallowMemoryUsage_JVMNotSupported() throws Exception {
        MemoryUsage memoryUsage = new MemoryUsage(new MemoryUsageAnalyzer[] {new Sun12Analyzer()},
            null, false);
        try {
            memoryUsage.getShallowMemoryUsage(new Object());
            fail("JVMNotSupportedException is expected.");
        } catch (JVMNotSupportedException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getShallowMemoryUsage(Object)</code> method with null object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetShallowMemoryUsage_NullObject() throws Exception {
        try {
            usage.getShallowMemoryUsage(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getDeepMemoryUsage(Object)</code> method.
     * </p>
     * <p>
     * <code>JVMNotSupportedException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetDeepMemoryUsage_JVMNotSupported() throws Exception {
        MemoryUsage memoryUsage = new MemoryUsage(new MemoryUsageAnalyzer[] {new Sun12Analyzer()},
            null, false);
        try {
            memoryUsage.getDeepMemoryUsage(new Object());
            fail("JVMNotSupportedException is expected.");
        } catch (JVMNotSupportedException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getDeepMemoryUsage(Object)</code> method with null object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetDeepMemoryUsage_NullObject() throws Exception {
        try {
            usage.getDeepMemoryUsage(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getDeepMemoryUsage(Object, MemoryUsageListener)</code> method.
     * </p>
     * <p>
     * <code>JVMNotSupportedException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetDeepMemoryUsageListener_JVMNotSupported() throws Exception {
        MemoryUsage memoryUsage = new MemoryUsage(new MemoryUsageAnalyzer[] {new Sun12Analyzer()},
            null, false);
        try {
            memoryUsage.getDeepMemoryUsage(new Object(), new FailureTestMemoryUsageListener());
            fail("JVMNotSupportedException is expected.");
        } catch (JVMNotSupportedException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getDeepMemoryUsage(Object, MemoryUsageListener)</code> method with null object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetDeepMemoryUsageListener_NullObject() throws Exception {
        try {
            usage.getDeepMemoryUsage(null, new FailureTestMemoryUsageListener());
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getDeepMemoryUsage(Object, MemoryUsageListener)</code> method with null listener.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetDeepMemoryUsageListener_NullListener() throws Exception {
        try {
            usage.getDeepMemoryUsage(new Object(), null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * <p>
     * Tests the<code>getEmbeddedObjects(Object)</code> method with null object.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    public void testGetEmbeddedObjects_NullObject() throws Exception {
        try {
            MemoryUsage.getEmbeddedObjects(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }
}
