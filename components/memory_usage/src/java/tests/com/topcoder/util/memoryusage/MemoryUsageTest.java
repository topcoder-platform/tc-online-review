/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.memoryusage.TestsHelper.ClassA;
import com.topcoder.util.memoryusage.TestsHelper.ClassB;
import com.topcoder.util.memoryusage.TestsHelper.ClassC;
import com.topcoder.util.memoryusage.analyzers.IBM14Analyzer;
import com.topcoder.util.memoryusage.analyzers.IBM15Analyzer;
import com.topcoder.util.memoryusage.analyzers.Sun14Analyzer;
import com.topcoder.util.memoryusage.analyzers.Sun15Analyzer;
import java.util.Arrays;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests of MemoryUsage.
 *
 * @author TexWiller
 * @version 2.0
 */
public class MemoryUsageTest extends TestCase {

    /**
     * Stores the default configuration file name, used by most test cases.
     */
    private static final String DEFAULT_XML = "MemoryUsage.xml";

    /**
     * The namespace defined in the alternative configuration file name.
     */
    private static final String ALT_NAMESPACE = "AlternativeNamespace";

    /**
     * Stores the alternative configuration file name, used by a few test cases.
     */
    private static final String ALT_XML = "MemoryUsageAltNamespace.xml";

    /**
     * File name of a malformed configuration file.
     */
    private static final String ERR_1_XML = "MemoryUsageErr1.xml";

    /**
     * File name of a malformed configuration file.
     */
    private static final String ERR_2_XML = "MemoryUsageErr2.xml";

    /**
     * File name of a malformed configuration file.
     */
    private static final String ERR_3_XML = "MemoryUsageErr3.xml";

    /**
     * The number of instances of objects to create in order to properly
     * estimate their size.
     */
    private static final int ESTIMATE_INSTANCES = 20000;

    /**
     * A set of MemoryUsageAnalyzers, used to test the full parameters constructor.
     */
    private static final MemoryUsageAnalyzer[] ANALYZERS = new MemoryUsageAnalyzer[] {
        new Sun14Analyzer(), new Sun15Analyzer(), new IBM14Analyzer(), new IBM15Analyzer()
    };

    /**
     * Saves the namespaces present when loading the configuration,
     * in order to properly remove them afterwards.
     */
    private Set savedNamespaces;

    /**
     * Standard TestCase constructor: creates a new MemoryUsageTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public MemoryUsageTest(String testName) {
        super(testName);
    }

    /**
     * Sets up the fixture. Basically saves the current namespaces, and loads
     * the test configuration file.
     * @throws ConfigManagerException If an error occurs loading the configuration file
     */
    protected void setUp() throws ConfigManagerException {
        savedNamespaces = TestsHelper.getCurrentNamespaces();
        ConfigManager.getInstance().add(DEFAULT_XML);
    }

    /**
     * Tears down the fixture. It unloads all the namespaces loaded during setUp().
     */
    protected void tearDown() {
        TestsHelper.retainNamespaces(savedNamespaces);
    }

    /**
     * Static method expected by JUnit test runners. Returns a Test containing all
     * the testXXX methods in this class.
     *
     * @return A new Test, containing all the testXXX methods in this class.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(MemoryUsageTest.class);
        return suite;
    }

    /**
     * Tests the default constructor.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructor1() throws ConfigurationException {
        new MemoryUsage();
    }

    /**
     * Tests the constructor with <i>namespace</i> parameter.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     * @throws ConfigManagerException If errors occur loading the alternative configuration
     * file; should not occur.
     */
    public void testConstructor2() throws ConfigurationException, ConfigManagerException {
        ConfigManager.getInstance().add(ALT_XML);
        new MemoryUsage(ALT_NAMESPACE);
    }

    /**
     * Tests the constructor with full parameters.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructor3() throws ConfigurationException {
        new MemoryUsage(ANALYZERS, null, true);
    }

    /**
     * Tests the constructor with wrong <i>namespace</i> parameter.
     * Expected exception: ConfigurationException.
     */
    public void testConstructorErr1() {
        try {
            new MemoryUsage("UNKNOWN");
            fail("The MemoryUsage constructor did not throw ConfigurationException"
                    + " with a wrong namespace parameter");
        } catch (ConfigurationException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor with <code>null</code> <i>namespace</i> parameter.
     * Expected exception: IllegalArgumentException.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructorErr2() throws ConfigurationException {
        try {
            new MemoryUsage(null);
            fail("The MemoryUsage constructor did not throw IllegalArgumentException"
                    + " with a null namespace parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor with empty <i>namespace</i> parameter.
     * Expected exception: IllegalArgumentException.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructorErr3() throws ConfigurationException {
        try {
            new MemoryUsage("  ");
            fail("The MemoryUsage constructor did not throw IllegalArgumentException"
                    + " with an empty namespace parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor with <code>null</code> <i>analyzers</i> parameter.
     * Expected exception: IllegalArgumentException.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructorErr4() throws ConfigurationException {
        try {
            new MemoryUsage(null, new Sun14Analyzer(), true);
            fail("The MemoryUsage constructor did not throw IllegalArgumentException"
                    + " with a null analyzers parameter");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor with <code>null</code> <i>analyzers</i> parameter.
     * Expected exception: IllegalArgumentException.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testConstructorErr5() throws ConfigurationException {
        MemoryUsageAnalyzer[] copy = (MemoryUsageAnalyzer[]) ANALYZERS.clone();
        copy[1] = null;
        try {
            new MemoryUsage(copy, new Sun14Analyzer(), true);
            fail("The MemoryUsage constructor did not throw IllegalArgumentException"
                    + " with an analyzers array containing null values");
        } catch (IllegalArgumentException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor fails with a configuration file missing the
     * <i>analyzers</i> property.
     * Expected exception: ConfigurationException.
     * @throws ConfigManagerException If errors occur loading the erroneous configuration
     * file; should not occur.
     */
    public void testConfigErr1() throws ConfigManagerException {
        ConfigManager.getInstance().add(ERR_1_XML);
        try {
            new MemoryUsage(ALT_NAMESPACE);
            fail("The MemoryUsage constructor accepted a namespace with no anyzers property");
        } catch (ConfigurationException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor fails with a configuration file with an empty
     * <i>analyzers</i> property.
     * Expected exception: ConfigurationException.
     * @throws ConfigManagerException If errors occur loading the erroneous configuration
     * file; should not occur.
     */
    public void testConfigErr2() throws ConfigManagerException {
        ConfigManager.getInstance().add(ERR_2_XML);
        try {
            new MemoryUsage(ALT_NAMESPACE);
            fail("The MemoryUsage constructor accepted a namespace with an empty analyzers property");
        } catch (ConfigurationException ex) {
            // Test passed
        }
    }

    /**
     * Tests the constructor fails with a configuration file with an empty
     * <i>analyzers</i> property.
     * Expected exception: ConfigurationException.
     * @throws ConfigManagerException If errors occur loading the erroneous configuration
     * file; should not occur.
     */
    public void testConfigErr3() throws ConfigManagerException {
        ConfigManager.getInstance().add(ERR_3_XML);
        try {
            new MemoryUsage(ALT_NAMESPACE);
            fail("The MemoryUsage constructor accepted a namespace with a non-working ObjectFactory namespace");
        } catch (ConfigurationException ex) {
            // Test passed
        }
    }

    /**
     * Test of getShallowMemoryUsage() method.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     */
    public void testGetShallowMemoryUsage() throws MemoryUsageException,
            JVMNotSupportedException, ConfigurationException {
        MemoryUsage usage = new MemoryUsage();
        ClassA item = new ClassA();
        assertEquals("Shallow memory usage for ClassA reported wrong value",
                TestsHelper.evaluateMemory(ClassA.class, ESTIMATE_INSTANCES),
                usage.getShallowMemoryUsage(item).getUsedMemory()
                + usage.getShallowMemoryUsage(item.objA5).getUsedMemory());
    }

    /**
     * Test of getDeepMemoryUsage method on a simple class, ClassA.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws InstantiationException If an error occurred instantiating an object of the class
     * @throws IllegalAccessException If the specified class does not have a public no-parameter constructor
     */
    public void testGetDeepMemoryUsage1() throws ConfigurationException,
            MemoryUsageException, JVMNotSupportedException, InstantiationException, IllegalAccessException {
        testGetDeepMemoryUsage(ClassA.class);
    }

    /**
     * Test of getDeepMemoryUsage method on a medium complexity class, ClassB.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws InstantiationException If an error occurred instantiating an object of the class
     * @throws IllegalAccessException If the specified class does not have a public no-parameter constructor
     */
    public void testGetDeepMemoryUsage2() throws ConfigurationException,
            MemoryUsageException, JVMNotSupportedException, InstantiationException, IllegalAccessException {
        testGetDeepMemoryUsage(ClassB.class);
    }

    /**
     * Test of getDeepMemoryUsage method on a complex class, ClassC.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws InstantiationException If an error occurred instantiating an object of the class
     * @throws IllegalAccessException If the specified class does not have a public no-parameter constructor
     */
    public void testGetDeepMemoryUsage3() throws ConfigurationException,
            MemoryUsageException, JVMNotSupportedException, InstantiationException, IllegalAccessException {
        testGetDeepMemoryUsage(ClassC.class);
    }

    /**
     * Generic test for getDeepMemoryUsage(). Takes a Class as parameter,
     * which must be a subclass of ClassA, and tests the getDeepMemoryUsage
     * method on an instance of that class.
     * @param cls The class whose memory usage has to be calculated
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws InstantiationException If an error occurred instantiating an object of the class
     * @throws IllegalAccessException If the specified class does not have a public no-parameter constructor
     */
    private void testGetDeepMemoryUsage(Class cls) throws ConfigurationException,
            MemoryUsageException, JVMNotSupportedException, InstantiationException, IllegalAccessException {
        MemoryUsage memoryUsage = new MemoryUsage();
        ClassA item = (ClassA) cls.newInstance();
        MemoryUsageResult result = memoryUsage.getDeepMemoryUsage(item);
        assertEquals("Wrong memory usage calculated",
                TestsHelper.evaluateMemory(cls, ESTIMATE_INSTANCES),
                result.getUsedMemory());
        assertEquals("Wrong count of Object items",
                TestsHelper.countClass(Object.class, item.getEmbeddedDeep()),
                result.getDetail(Object.class).getObjectCount());
    }

    /**
     * Test of getEmbeddedObjects method on TestsHelper.ClassA.
     * @throws MemoryUsageException If an error occurs analyzing ClassA fields;
     * should not happen
     */
    public void testGetEmbeddedObjects1() throws MemoryUsageException {
        ClassA item = new ClassA();
        TestsHelper.listsEqual(Arrays.asList(MemoryUsage.getEmbeddedObjects(item)),
                item.getEmbeddedShallow(), true);
    }

    /**
     * Test of getEmbeddedObjects method on TestsHelper.ClassA.
     * @throws MemoryUsageException If an error occurs analyzing ClassA fields;
     * should not happen
     */
    public void testGetEmbeddedObjects2() throws MemoryUsageException {
        ClassB item = new ClassB();
        TestsHelper.listsEqual(Arrays.asList(MemoryUsage.getEmbeddedObjects(item)),
                item.getEmbeddedShallow(), true);
    }

    /**
     * Test of getEmbeddedObjects method on TestsHelper.ClassA.
     * @throws MemoryUsageException If an error occurs analyzing ClassA fields;
     * should not happen
     */
    public void testGetEmbeddedObjects3() throws MemoryUsageException {
        ClassC item = new ClassC();
        TestsHelper.listsEqual(Arrays.asList(MemoryUsage.getEmbeddedObjects(item)),
                item.getEmbeddedShallow(), true);
    }

    /**
     * Test of getFallBackAnalyzer method, where it has been specified by
     * the configuration file.
     * @throws ConfigurationException If there are problems with the configuration;
     * should not occur.
     */
    public void testGetFallBackAnalyzer1() throws ConfigurationException {
        MemoryUsage usage = new MemoryUsage();
        assertEquals("The fallback analyzer is not the expected one",
                Sun15Analyzer.class, usage.getFallBackAnalyzer().getClass());
    }

    /**
     * Test of getFallBackAnalyzer method, where it has been specified by
     * the constructor to use the default analyzer.
     */
    public void testGetFallBackAnalyzer2() {
        MemoryUsage usage = new MemoryUsage(ANALYZERS, null, true);
        assertEquals("The fallback analyzer is not the expected one",
                Sun14Analyzer.class, usage.getFallBackAnalyzer().getClass());
    }
}
