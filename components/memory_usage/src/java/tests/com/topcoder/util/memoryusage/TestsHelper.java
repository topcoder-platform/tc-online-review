/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import junit.framework.TestCase;

/**
 * Helper methods for test cases.
 *
 * @author TexWiller
 * @version 2.0
 */
public class TestsHelper {

    /**
     * The key for retrieving the JVM vendor property from system properties.
     */
    private static final String PROP_VENDOR = "java.vendor";

    /**
     * The key for retrieving the JVM version property from system properties.
     */
    private static final String PROP_VERSION = "java.class.version";

    /**
     * A Stack which holds all the previous vendor and version strings,
     * before they were changed by pushVendorVersion(). The stack contains
     * always a multiple of 2 items, where the first item put on the stack
     * is the vendor, and the second is the version.
     */
    private static Stack vendorVersionStack = new Stack();

    /**
     * Private constructor: this class contains
     * only static methods, and should not be instantiated.
     */
    private TestsHelper() {
    }

    /**
     * Helper method to change the content of the <code>java.vendor</code> and
     * <code>java.class.version</code> system parameters, while maintaining their
     * original content for later recovery.
     * @param vendor The new value for the <code>java.vendor</code> system parameter
     * @param version The new value for the <code>java.class.version</code> system parameter
     * @see TestsHelper#popVendorVersion()
     * @see TestsHelper#isVVStackEmpty()
     */
    public static void pushVendorVersion(String vendor, String version) {
        vendorVersionStack.push(System.getProperty(PROP_VENDOR));
        vendorVersionStack.push(System.getProperty(PROP_VERSION));
        System.setProperty(PROP_VENDOR, vendor);
        System.setProperty(PROP_VERSION, version);
    }

    /**
     * Rolls back the last change to the <code>java.vendor</code> and
     * <code>java.class.version</code> system parameters, recovering their previous
     * values.
     * @throws IllegalStateException If the recovery stack is empty
     * @see TestsHelper#pushVendorVersion(java.lang.String, java.lang.String)
     * @see TestsHelper#isVVStackEmpty()
     */
    public static void popVendorVersion() {
        if (vendorVersionStack.isEmpty()) {
            throw new IllegalStateException("The vendor and version stack is empty");
        }
        System.setProperty(PROP_VERSION, (String) vendorVersionStack.pop());
        System.setProperty(PROP_VENDOR, (String) vendorVersionStack.pop());
    }

    /**
     * Verifies if there are still some system variable recoveries to be issued.
     * @return <code>true</code> if at least one recovery still has to be done
     * @see TestsHelper#popVendorVersion()
     * @see TestsHelper#pushVendorVersion(java.lang.String, java.lang.String)
     */
    public static boolean isVVStackEmpty() {
        return vendorVersionStack.isEmpty();
    }

    /**
     * Compares two lists, checking that they have the same items - although
     * possibly in different order. If the lists are different, a testcase
     * assertion fails. Equality checks are done either with <code>.equals()</code>,
     * or by reference.
     * @param reported The list resulting from tests
     * @param expected The expected list to check <i>reported</i> against
     * @param refEquals Specifies whether <code>==</code> or <code>equals()</code>
     * should be used. If <code>true</code>, reference equality (<code>==</code>) will
     * be checked; otherwise, content equality (<code>equals()</code>).
     */
    public static void listsEqual(List reported, List expected, boolean refEquals) {
        // The algorithm copies the expected list, and removes
        // from it the items in the reported list
        ArrayList list = new ArrayList(expected);
        for (int i = 0; i < reported.size(); i++) {
            Object seek = reported.get(i);
            boolean found = false;
            for (Iterator it = list.iterator(); it.hasNext();) {
                Object o = it.next();
                if ((refEquals && (o == seek)) || (!refEquals && (o.equals(seek)))) {
                    it.remove();
                    found = true;
                    break;
                }
            }
            TestCase.assertTrue("Unexpected item found " + seek, found);
        }
        if (!list.isEmpty()) {
            TestCase.fail("Expected item not found " + list.get(0));
        }
    }

    /**
     * This method evaluates the total (deep) memory use of objects
     * from a specified class. The class must have a no-argument constructor.
     * <i>evalSize</i> copies of the class will be instantiated, and
     * the memory size of a single copy will be estimated by dividing the
     * global memory occupied by the number of generated copies. The result
     * is not guaranteed to be accurate.
     * @param c The Class whose memory footprint has to be estimated
     * @param evalSize The number of object instances to create for the evaluation.
     * High figures (>10000) generally help to get better accuracy.
     * @return The total size (in bytes) occupied by an instance of the class,
     * and all its contained objects.
     */
    public static long evaluateMemory(Class c, int evalSize) {
        Object[] arr = new Object[evalSize];
        try {
            // Create one instance out of the evaluation,
            // in order for the class to initialize the static variables
            c.newInstance();
        } catch (Exception ex) {
            // Ignore exceptions
        }
        runGC();
        long memory = usedMemory();
        for (int i = 0; i < arr.length; i++) {
            try {
                arr[i] = c.newInstance();
            } catch (InstantiationException ex) {
                throw new IllegalArgumentException("Exception raised upon creating the object: " + ex.getMessage());
            } catch (IllegalAccessException ex) {
                throw new IllegalArgumentException("Exception raised upon creating the object: " + ex.getMessage());
            }
        }
        runGC();
        memory = usedMemory() - memory;
        long value = (long) Math.round((double) memory / evalSize);
        return (long) Math.round(value / 8.0) * 8;
    }

    /**
     * Converts an array of objects to a string representation.
     * @param array The array of objects to be converted; cannot be <code>null</code>.
     * @return A String representation of the array.
     */
    public static String arrayToString(Object[] array) {
        StringBuffer sb = new StringBuffer();
        sb.append("[");
        for (int i = 0; i < array.length; i++) {
            sb.append(array[i].toString());
            if (i < array.length - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * Retrieves all the namespaces currently loaded in ConfigManager.
     * This method can be used before loading a configuration
     * file with some namespaces, in order to find out which
     * namespaces the configuration file has added.
     * Later, the added namespaces can be removed using retainNamespaces().
     * @return A (possibly empty) Set of String values, containing all the
     * namespaces currently loaded in ConfigManager.
     */
    public static Set getCurrentNamespaces() {
        ConfigManager cManager = ConfigManager.getInstance();
        HashSet namespaces = new HashSet();
        Iterator it = cManager.getAllNamespaces();
        while (it.hasNext()) {
            namespaces.add(it.next().toString());
        }
        return namespaces;
    }

    /**
     * Removes from ConfigManager all the namespaces NOT present
     * in the <i>namespaces</i> set. In conjunction with getCurrentNamespaces(),
     * this method can be used to restore ConfigManager state to a point
     * previous reading a new configuration file.
     * @param namespaces A Set of String, specifying all the namespaces
     * which should NOT be removed. If the Set is <code>null</code> or
     * empty, all the namespaces will be removed.
     */
    public static void retainNamespaces(Set namespaces) {
        ConfigManager cManager = ConfigManager.getInstance();
        if (namespaces == null) {
            namespaces = new HashSet();
        }
        Iterator it = cManager.getAllNamespaces();
        while (it.hasNext()) {
            String ns = (String) it.next();
            if (!namespaces.contains(ns)) {
                try {
                    cManager.removeNamespace(ns);
                } catch (UnknownNamespaceException ex) {
                    // Ignored; should not happen anyway
                }
            }
        }
    }

    /**
     * This method runs the garbage collection, until the amount
     * of used memory appears to be stable.
     */
    private static void runGC() {
        int curPriority = Thread.currentThread().getPriority();
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        for (int j = 0; j < 5; j++) {
            long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
            for (int i = 0; (usedMem1 < usedMem2) && (i < 500); i++) {
                System.runFinalization();
                System.gc();
                System.gc();
                Thread.yield();

                usedMem2 = usedMem1;
                usedMem1 = usedMemory();
            }
        }
        Thread.currentThread().setPriority(curPriority);
    }

    /**
     * Returns the amount of used memory.
     * @return The total amount of currently used memory (in bytes).
     */
    private static long usedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * Counts the number of items in a list belonging to a specific class.
     * No inheritance is considered, only the exact same class.
     *
     * @param cls The Class looked for
     * @param items The List of items looked at
     * @return The number of items in the list belonging to the <i>cls</i> class.
     */
    public static int countClass(Class cls, List items) {
        int count = 0;
        for (Iterator it = items.iterator(); it.hasNext();) {
            Object item = it.next();
            if (item != null) {
                if (cls.equals(item.getClass())) {
                    count++;
                }
            }
        }
        return count;
    }

    /**
     * Basic test class. It contains a static field, several primitive type fiels
     * with different access levels, and an Object field. All the fields inside the
     * object have clearly no meaning, their only purpouse is to test the size
     * estimation routines.
     *
     */
    public static class ClassA {
        /**
         * Dummy static field - should not be included by memory
         * use calculation routines.
         */
        public static final int STATIC = 0;

        /** Dummy field. */
        public int intA1;
        /** Dummy field. */
        double doubleA2;
        /** Dummy field. */
        protected float floatA3;
        /** Dummy field. */
        private byte byteA4;
        /** Dummy field. */
        protected Object objA5 = new Object();

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a shallow list, meaning that only the
         * objects contained in the fields of this object will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedShallow() {
            return Arrays.asList(new Object[] {objA5});
        }

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a deep list, meaning that all the objects contained
         * by this object, and all the objects contained by them, will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedDeep() {
            return getEmbeddedShallow();
        }

        /**
         * This methods returns a List of Field objects, containing
         * all non-static fields declared by this class.
         * @return A non-<code>null</code> List of field objects
         */
        public static List getFields() {
            try {
                return Arrays.asList(new Field[] {
                    ClassA.class.getDeclaredField("intA1"),
                    ClassA.class.getDeclaredField("doubleA2"),
                    ClassA.class.getDeclaredField("floatA3"),
                    ClassA.class.getDeclaredField("byteA4"),
                    ClassA.class.getDeclaredField("objA5")});
            } catch (SecurityException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            } catch (NoSuchFieldException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            }
        }
    }

    /**
     * Intermediate test class. It inherits from the basic class, adds a few
     * Object fields with various access levels, and an array.
     */
    public static class ClassB extends ClassA {
        /** Dummy field. */
        public Object objB1 = new Object();
        /** Dummy field. */
        protected Object objB2 = new Object();
        /** Dummy field. */
        private Object objB3 = new Object();
        /** Dummy field. */
        private Object[] arrB4 = new Object[5];

        /**
         * Initializes the array contents. Every item in the array
         * is different.
         */
        public ClassB() {
            for (int i = 0; i < arrB4.length; i++) {
                arrB4[i] = new Object();
            }
        }

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a shallow list, meaning that only the
         * objects contained in the fields of this object will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedShallow() {
            List list = new ArrayList(Arrays.asList(new Object[] {
                objB1, objB2, objB3, arrB4
            }));
            list.addAll(super.getEmbeddedShallow());
            return list;
        }

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a deep list, meaning that all the objects contained
         * by this object, and all the objects contained by them, will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedDeep() {
            List list = new ArrayList(super.getEmbeddedDeep());
            list.addAll(Arrays.asList(arrB4));
            return list;
        }

        /**
         * This methods returns a List of Field objects, containing
         * all non-static fields declared by this class.
         * @return A non-<code>null</code> List of field objects
         */
        public static List getFields() {
            try {
                List fields = new ArrayList(Arrays.asList(new Field[] {
                    ClassB.class.getDeclaredField("objB1"),
                    ClassB.class.getDeclaredField("objB2"),
                    ClassB.class.getDeclaredField("objB3"),
                    ClassB.class.getDeclaredField("arrB4")}));
                fields.addAll(ClassA.getFields());
                return fields;
            } catch (SecurityException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            } catch (NoSuchFieldException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            }
        }
    }

    /**
     * Complex class. It inherits from the intermediate class, and contains
     * another array with <code>null</code> values, duplicate objects, and self-references.
     */
    public static class ClassC extends ClassB {
        /** Dummy field. */
        private Object[] arrC1 = new Object[7];

        /**
         * Initializes the array with <code>null</code> values, duplicate
         * objects, and self-references.
         */
        public ClassC() {
            arrC1[0] = new Object();
            arrC1[1] = objA5;
            arrC1[2] = null;
            arrC1[3] = arrC1[0];
            arrC1[4] = this;
            arrC1[5] = new Integer(5);
            arrC1[6] = new Integer(5);
        }

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a shallow list, meaning that only the
         * objects contained in the fields of this object will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedShallow() {
            List list = new ArrayList();
            list.add(arrC1);
            list.addAll(super.getEmbeddedShallow());
            return list;
        }

        /**
         * Returns a List of all the Objects embedded in this
         * object. This is a deep list, meaning that all the objects contained
         * by this object, and all the objects contained by them, will be returned.
         * @return A non-<code>null</code> List of Object items
         */
        public List getEmbeddedDeep() {
            List list = new ArrayList(super.getEmbeddedDeep());
            list.add(arrC1[0]);
            list.add(arrC1[5]);
            list.add(arrC1[6]);
            return list;
        }

        /**
         * This methods returns a List of Field objects, containing
         * all non-static fields declared by this class.
         * @return A non-<code>null</code> List of field objects
         */
        public static List getFields() {
            try {
                List fields = new ArrayList(Arrays.asList(new Field[] {
                    ClassB.class.getDeclaredField("arrC1")}));
                fields.addAll(ClassB.getFields());
                return fields;
            } catch (SecurityException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            } catch (NoSuchFieldException ex) {
                // Should not happen
                throw new RuntimeException("Unexpected exception", ex);
            }
        }
    }
}
