/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.memoryusage.TestsHelper.ClassB;
import com.topcoder.util.memoryusage.TestsHelper.ClassC;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Demo test class. Two tests are implemented. {@link DemoTest@testDemo()} has no other
 * practical purpose, other than showing how the class should be used.
 * {@link DemoTest@testStorageSizes()} shows how this class can be used for a
 * practical development decision, faced in this component. It is necessary to
 * implement a Set based upon reference equality. The IdentityHashMap can be used,
 * or a regular HashSet containing wrapped objects which check reference equality
 * inside their <code>equals()</code> methods. The tests shows the memory usage
 * of both solutions, employing a MemoryUsageListener to exclude from the usage count
 * the size of the actual objects inside the maps.
 *
 * @author TexWiller
 * @version 2.0
 */
public class DemoTest extends TestCase {

    /**
     * Stores the default configuration file name, used by most test cases.
     */
    private static final String DEFAULT_XML = "MemoryUsage.xml";

    /**
     * Saves the namespaces present when loading the configuration,
     * in order to properly remove them afterwards.
     */
    private Set savedNamespaces;

    /**
     * Standard TestCase constructor: creates a new DemoTest object.
     *
     * @param testName The name to be given to this TestCase.
     */
    public DemoTest(String testName) {
        super(testName);
    }

    /**
     * Sets up the fixture. Basically saves the current namespaces, and loads
     * the test configuration file.
     * @throws ConfigManagerException If an error occurs loading the configuration file
     */
    protected void setUp() throws ConfigManagerException {
        savedNamespaces = TestsHelper.getCurrentNamespaces();
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
        TestSuite suite = new TestSuite(DemoTest.class);
        return suite;
    }

    /**
     * Simple demo of the component usage.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws ConfigManagerException If an errors occur reading the configuration file
     */
    public void testDemo() throws ConfigurationException,
            ConfigManagerException, MemoryUsageException, JVMNotSupportedException {
        // Load the configuration file
        ConfigManager.getInstance().add(DEFAULT_XML);
        // Create a new MemoryUsage instance, configured through the
        // configuration file
        MemoryUsage memoryUsage = new MemoryUsage();

        // Create some objects...
        ClassX testItem = new ClassX();

        // Get an object memory usage
        MemoryUsageResult result = memoryUsage.getDeepMemoryUsage(testItem);
        // Get the total memory usage of the object
        System.out.println("The total memory used by " + testItem
                + " is " + result.getUsedMemory() + " bytes");

        // Get the number of contained objects belonging to a specific class
        System.out.println("The object " + testItem + " contains "
                + result.getDetail(ClassC.class).getObjectCount() + " objects belonging to ClassC");

        // It is possible to exclude part of the contained objects
        // from the memory calculation:
        MemoryUsageResult partialResult = memoryUsage.getDeepMemoryUsage(testItem,
                new MemoryUsageListener() {
            public boolean objectReached(Object obj) {
                return !(obj.getClass().equals(ClassC.class));
            }
        });
        System.out.println("The total memory used by " + testItem
                + " excluding ClassC objects is " + partialResult.getUsedMemory() + " bytes");

        assertNull("Found some unexpected ClassC objects",
                partialResult.getDetail(ClassC.class));
    }

    /**
     * This demo tests the memory usage of an IdentityHashMap with respect
     * to a regular HashSet with wrapped objects to achieve the same result.
     * It creates both structures, and fills them with 1000 different Dummy
     * objects. It then evaluates the deep memory usage, excluding from the
     * count the Dummy objects through an appropriate MemoryUsageListener.
     * @throws MemoryUsageException If an error reading the test object occurs
     * @throws JVMNotSupportedException If the current JVM is not supported
     * @throws ConfigurationException If there is an error in the configuration
     * @throws ConfigManagerException If an errors occur reading the configuration file
     */
    public void testStorageSizes() throws ConfigManagerException,
            ConfigurationException, MemoryUsageException, JVMNotSupportedException {
        ConfigManager.getInstance().add(DEFAULT_XML);
        // Create a new MemoryUsage instance, configured through the
        // configuration file
        MemoryUsage memoryUsage = new MemoryUsage();

        // Create and fill the two structures
        IdentityHashMap idMap = new IdentityHashMap();
        Set idWrap = new HashSet();
        for (int i = 0; i < 1000; i++) {
            Dummy o = new Dummy();
            idMap.put(o, o);
            idWrap.add(new Wrapper(o));
        }

        // Set up the MemoryUsageListener used to avoid counting the
        // contained objects sizes.
        MemoryUsageListener dummyExcluder = new MemoryUsageListener() {
            public boolean objectReached(Object obj) {
                return !obj.getClass().equals(Dummy.class);
            }
        };

        MemoryUsageResult idMapResult = memoryUsage.getDeepMemoryUsage(idMap, dummyExcluder);
        MemoryUsageResult idWrapResult = memoryUsage.getDeepMemoryUsage(idWrap, dummyExcluder);

        // Print and check the two memory usages
        System.out.println("IdMap size: " + idMapResult.getUsedMemory());
        System.out.println("IdWrap size: " + idWrapResult.getUsedMemory());

        assertTrue("IdentityHashMap has a higher size than a Set with Wrappers",
                idMapResult.getUsedMemory() < idWrapResult.getUsedMemory());

        // A simpler approach to removing the Dummy objects is the following
        MemoryUsageResult idMapFullResult = memoryUsage.getDeepMemoryUsage(idMap);
        assertEquals("Dummy objects size removal produces different results",
                idMapResult.getUsedMemory(),
                idMapFullResult.getUsedMemory()
                - idMapFullResult.getDetail(Dummy.class).getUsedMemory());
    }

    /**
     * A simple empty class to make it easy to remove contents
     * occupation from the container occupation in testStorageSizes().
     */
    public static class Dummy {
    }

    /**
     * This class wraps an object, in order to override the
     * <code>equals()</code> method to make it retur reference equality.
     */
    private static class Wrapper {
        /** The wrapped object. */
        private Object o;

        /**
         * Builds a new Wrapper, with a contained object.
         * @param o The wrapped object
         */
        public Wrapper(Object o) {
            this.o = o;
        }

        /**
         * Returns the hashcode of the contained object.
         * @return The hashcode of the contained object.
         */
        public int hashCode() {
            return o.hashCode();
        }

        /**
         * Test for reference equality: returns <code>true</code> only
         * if the wrapped object inside this Wrapper is exactly the same as
         * the wrapper object inside the passed Wrapper.
         * @param o The other Wrapper to test for equality
         * @return <code>true</code> if the contained object are the same;
         * <code>false</code> otherwise
         */
        public boolean equals(Object o) {
            return ((Wrapper) o).o == this.o;
        }
    }

    /**
     * Dummy class used in the demo.
     */
    public static class ClassX {
        /** A dummy items array. */
        ClassC[] dummyArray = new ClassC[10];
        /** A dummy variable. */
        private ClassB dummyItem = new ClassB();

        /**
         * Default constructor.
         */
        public ClassX() {
            for (int i = 0; i < dummyArray.length; i++) {
                dummyArray[i] = new ClassC();
            }
        }
    }
}
