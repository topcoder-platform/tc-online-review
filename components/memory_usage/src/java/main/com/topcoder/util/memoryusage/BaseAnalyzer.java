/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage;

import com.topcoder.util.memoryusage.MemoryUsageHelper.FieldsListener;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <p>This class provides common methods necessary to implement the MemoryUsageAnalyzer
 * interface. The class implements the traversal algorithm, both in shallow and
 * deep variants, and the basic memory usage calculation mechanisms.</p>
 * <p>The memory usage calculation algorithm is based upon the protected <code>getXXX()</code>
 * methods, which return the basic numbers for the calculation. <br>
 * First, the <code>getFieldSize()</code> method returns the amount of space
 * occupied by a field. Then, the <code>getObjectSize()</code> value returns the
 * space specifies the amount of basic space taken by an object, independently of
 * its fields. The total space occupied by an object will then be rounded to the closest
 * (higher) multiple of <code>getObjectAlign()</code>. Arrays have similar methods
 * to perform this calculation, and a <code>getArraySize()</code> method to calculate
 * the space actually taken by the array data. Finally, the <code>getFieldComparator</code>
 * returns a Comparator used to sort the fields in a particular order before
 * calculating their memory usage.</p>
 * <p>Overriding classes must implement the <code>matchesJVM()</code> method,
 * and can override any method not returning the correct value.</p>
 * <p><b>Thread safety:</b> This class is thread safe since it is immutable. Subclasses
 * should be thread safe too.</p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public abstract class BaseAnalyzer implements MemoryUsageAnalyzer {

    /**
     * Overhead for any object.
     */
    private static final int OBJECT_BASE = 8;

    /**
     * Overhead for any array.
     */
    private static final int ARRAY_BASE = 12;

    /**
     * The size of an object is padded with bytes to become multiple of OBJECT_ALIGN bytes.
     */
    private static final int OBJECT_ALIGN = 8;

    /**
     * The size of an array is padded with bytes to become multiple of ARRAY_ALIGN bytes.
     */
    private static final int ARRAY_ALIGN = 8;

    /**
     * The size to be returned for <code>null</code> objects.
     */
    private static final int NULL_SIZE = 4;

    /**
     * Represents the java.vendor property that this analyzer matches. Initialized
     * in the ctor. Usually something meaningful such as "IBM Corporation"
     */
    private String vendor;

    /**
     * Represents the java.vm.version property that this analyzer matches. Initialized
     * in the ctor. Shoudl be something meaningful such as "J2SDK v1.4"
     */
    private String vmVersion;

    /**
     * The default Comparator for sorting fields.
     */
    private final Comparator defaultFieldComparator = new DefaultFieldComparator();

    /**
     * Base constructor which accepts the vandor and supported VM information.
     * The two values are currently only used inside the <code>toString()</code>
     * method.
     *
     * @param vendor JVM vendor supported by this MemoryUsageAnalyzer
     * @param vmVersion JVM version supported by this MemoryUsageAnalyzer
     */
    protected BaseAnalyzer(String vendor, String vmVersion) {
        this.vendor = vendor;
        this.vmVersion = vmVersion;
    }

    /**
     * Detects if this MemoryUsageAnalyzer implementation applies to the running JVM.
     *
     * @return <code>true</code> if this implementation applies to the running JVM,
     * <code>false</code> otherwise.
     */
    public abstract boolean matchesJVM();

    /**
     * Retrieve the shallow memory usage for an object (not including embedded
     * objects memory usage, but counting the memory consumed by the object to keep
     * references to the embedded objects).
     *
     * @param obj The object to get the memory usage for; can be <code>null</code>
     * @return The shallow memory usage result of the specified object
     * @throws MemoryUsageException If exceptions occurred while traversing the object
     */
    public MemoryUsageResult getShallowMemoryUsage(Object obj) throws MemoryUsageException {
        return analyze(obj, false, null);
    }

    /**
     * Get the deep memory usage for an object (including embedded objects memory
     * usage recursively). The user can specify a MemoryUsageListener in order to
     * prevent some of the embedded objects to be analyzed. The object can be <code>null</code>.
     *
     * @param obj The object to get the memory usage for; can be <code>null</code>
     * @param listener The MemoryUsageListener to specify which objects should
     * be included in the memory usage sum, and which should not. Can be <code>null</code>,
     * meaning that all the objects will be included.
     * @return The deep memory usage result of the specified object
     * @throws MemoryUsageException If exceptions occurred while traversing the object
     */
    public MemoryUsageResult getDeepMemoryUsage(Object obj, MemoryUsageListener listener)
        throws MemoryUsageException {
        return analyze(obj, true, listener);
    }

    /**
     * This method implements the main traversal algorithm to analyze memory usage
     * for an object (with or without the embedded objects). It uses a BFS (Breadth
     * First Search) in order to walk the object graph.
     *
     * @param obj The object to get the memory usage for; can be <code>null</code>
     * @param listener The MemoryUsageListener to specify which objects should
     * be included in the memory usage sum, and which should not. Can be <code>null</code>,
     * meaning that all the objects will be included.
     * @param goDeep <code>true</code> if embedded objects should be recursively traversed
     * (deep memory usage), <code>false</code> otherwise (shallow memory usage)
     * @return The memory usage result of the specified object
     * @throws MemoryUsageException If exceptions occurred while traversing the object
     */
    protected MemoryUsageResult analyze(Object obj, boolean goDeep, MemoryUsageListener listener)
        throws MemoryUsageException {
        // Implementation notes.
        // The algorithm maintains two structures to perform the traversal
        // The queue stores all the objects which must be visited; new objects
        // are added at bottom, and next object to visit is taken at top, in order
        // to implement a BFS.
        // The IdentityHashMap visited stores all the objects which have already been
        // reached and addded to the queue (but not necessarily counted). An IdentityHashMap
        // is used, in order to distinguish between two equals() instances of an object.
        // The algorithm uses addEmbeddedObjects() to add new objects to the queue and
        // to the visited map.

        if (obj == null) {
            MemoryUsageResult result = new MemoryUsageResult();
            result.cumulate(1, NULL_SIZE);
            result.setDetails(Collections.EMPTY_MAP);
            return result;
        }

        Map classToDetails = new HashMap();
        MemoryUsageResult result = new MemoryUsageResult();
        IdentityHashMap visited = new IdentityHashMap();
        List queue = new LinkedList();
        addEmbeddedObjects(new Object[] {obj }, null, queue, visited);

        // BFS to go through all fields and embedded objects
        while (!queue.isEmpty()) {
            long size = 0;
            // Get the object at top of the queue
            Object currentObject = queue.remove(0);

            if (currentObject.getClass().isArray()) {
                // Calculate overhead
                size = getArrayBase() + getArraySize(currentObject);

                if (goDeep) {
                    // Add all the contained objects. Arrays have no fields, so FieldsListener is not necessary.
                    Object[] embedded = MemoryUsageHelper.getEmbeddedObjects(currentObject, null);
                    addEmbeddedObjects(embedded, listener, queue, visited);
                }

                // Align array size to multiple of ARRAY_ALIGN
                size = align(size, getArrayAlign());
            } else {
                // Calculate overhead
                size = getObjectBase();
                final List fields = new ArrayList();

                // Get the embedded objects, and store all the fields of the current object
                Object[] embedded = MemoryUsageHelper.getEmbeddedObjects(currentObject, new FieldsListener() {
                    public void fieldReached(Field field) {
                        if (!MemoryUsageHelper.isStatic(field)) {
                            fields.add(field);
                        }
                    }
                });

                Collections.sort(fields, getFieldComparator());

                // Add the space of the fields to the object size
                for (Iterator it = fields.iterator(); it.hasNext();) {
                    int fieldSize = getFieldSize(((Field) it.next()).getType());
                    size = align(size, fieldSize); // Each field is aligned on its own size boundary
                    size += fieldSize;
                }

                // Add the embedded objects to the queue, if necessary
                if (goDeep) {
                    addEmbeddedObjects(embedded, listener, queue, visited);
                }

                // Align object size to multiple of OBJECT_ALIGN
                size = align(size, getObjectAlign());
            }

            // Cumulate this object's size to overall total
            result.cumulate(1, size);

            // Cumulate this object's size to its class details
            MemoryUsageDetail detail = (MemoryUsageDetail) classToDetails.get(currentObject.getClass());
            if (detail == null) {
                detail = new MemoryUsageDetail(currentObject.getClass());
                classToDetails.put(currentObject.getClass(), detail);
            }
            detail.cumulate(1, size);
        }
        // End BFS through objects

        result.setDetails(classToDetails);

        return result;
    }

    /**
     * Return the space occupied by a class field of the given class type.
     * This implementation follows the sizes in JVMs 1.3 and earlier: each type
     * uses 4 bytes, apart from <code>double</code> and <code>long</code> which
     * use 8 bytes.
     *
     * @param cls The type of the field - cannot be <code>null</code>
     * @return Size occupied by the field, in bytes
     * @throws IllegalArgumentException If the <i>cls</i> parameter is <code>null</code>
     */
    protected int getFieldSize(Class cls) {
        MemoryUsageHelper.checkObjectNotNull(cls, "cls");

        if (cls.isPrimitive() && (cls == Double.TYPE || cls == Long.TYPE)) {
            return 8;
        }

        return 4;
    }

    /**
     * Get the size of the contents of an array (shallow). Returns the number of bytes
     * occupied by each array item (obtained by getFieldSize()), multiplied by the
     * total number of items in the array (the array length). The array overhead
     * is not considered.
     *
     * @param arobj The array object whose size must be calculated - cannot be <code>null</code>
     * @return The size, in bytes, of the array
     * @throws IllegalArgumentException If the object is not an array, or if it is <code>null</code>
     * @see BaseAnalyzer#getFieldSize(java.lang.Class)
     */
    protected int getArraySize(Object arobj) {
        MemoryUsageHelper.checkObjectNotNull(arobj, "obj");
        if (!arobj.getClass().isArray()) {
            throw new IllegalArgumentException("The specified object is not an array");
        }

        Class array = arobj.getClass();
        int elementSize = MemoryUsageHelper.getFieldSize(array.getComponentType());
        return elementSize * Array.getLength(arobj);
    }

    /**
     * Get the overhead for an object. Each allocated object uses a certain
     * amount of bytes, to be added to the bytes used by its contents.
     * This implementation returns 8, which is the value for JVMs up to 1.4.
     *
     * @return The object overhead, in bytes
     */
    protected int getObjectBase() {
        return OBJECT_BASE;
    }

    /**
     * Get the overhead for an array. Each allocated array uses a certain
     * amount of bytes, to be added to the bytes used by its contents.
     * This implementation returns 12, which is the value for JVMs up to 1.5.
     *
     * @return The array overhead, in bytes
     */
    protected int getArrayBase() {
        return ARRAY_BASE;
    }

    /**
     * Get the alignment for the size of an object: the size of an object is
     * padded with bytes to become multiple of x bytes.
     *
     * @return The object align value; this implementation returns 8.
     */
    protected int getObjectAlign() {
        return OBJECT_ALIGN;
    }

    /**
     * Get the alignment for the size of an array: the size of an array is
     * padded with bytes to become multiple of x bytes.
     *
     * @return The array align value; this implementation returns 8.
     */
    protected int getArrayAlign() {
        return ARRAY_ALIGN;
    }

    /**
     * Returns the Comparator which will be used to sort the fields
     * before calculating their occupied space. This method must be overridden
     * if a different sorting of the fields is necessary. The returned comparator
     * must be able to compare objects of Field type. It will not receive
     * <code>null</code> values.
     * @return The default Comparator, which sorts fields according to their
     * occupied size. The size is calculated through this object <code>getFieldSize</code>
     * method.
     */
    protected Comparator getFieldComparator() {
        return new DefaultFieldComparator();
    }

    /**
     * Returns a textual representation of this object.
     *
     * @return A string representation of this object
     */
    public String toString() {
        return "Vendor:=" + vendor + " vm version:=" + vmVersion;
    }

    ///////////////////////////////
    // Helpers for analyze method
    //

    /**
     * Aligns a value upon a multiple of another value. If the first value
     * is not multiple of <i>alignSize</i>, it is incremented until it becomes a
     * multiple of <i>alignSize</i>.
     * @param value The value to be aligned
     * @param alignSize The size of the alignment
     * @return The aligned value
     */
    private static long align(long value, int alignSize) {
        if (value % alignSize == 0) {
            return value;
        } else {
            return ((value / alignSize) + 1) * alignSize;
        }
    }

    /**
     * Helper method to add an array of embedded objects to the queue of objects
     * to be visited, and to the visited set. The function checks each object
     * has not already been visited, and asks to the MemoryUsageListener if it
     * should be traversed. If both checks succeed, the object is added to the
     * queue, and to the visited set.
     *
     * @param embedded The array of embedded objects to test. Can be <code>null</code>: nothing
     * will happen.
     * @param listener A MemoryUsageListener to check if we should analyze the embedded object
     * @param queue The queue of objects to be analyzed; cannot be <code>null</code>
     * @param visited The IdentityHashMap of objects already queued to be visited; cannot be <code>null</code>
     * @throws IllegalArgumentException If either <i>queue</i> or <i>visited</i> is <code>null</code>
     */
    private void addEmbeddedObjects(Object[] embedded, MemoryUsageListener listener,
            List queue, IdentityHashMap visited) {
        MemoryUsageHelper.checkObjectNotNull(queue, "queue");
        MemoryUsageHelper.checkObjectNotNull(visited, "visited");
        if (embedded == null) {
            return;
        }

        for (int i = 0; i < embedded.length; i++) {
            // If the object is not null, and not visited yet...
            if ((embedded[i] != null) && (!visited.containsKey(embedded[i]))) {
                // If the listener is missing, or consents to the traversal...
                if ((listener == null) || listener.objectReached(embedded[i])) {
                    // Add the object to the queue, and to the visited set
                    visited.put(embedded[i], embedded[i]);
                    queue.add(embedded[i]);
                }
            }
        }
    }

    /**
     * This class implements the Comparator interface, in order to sort
     * the fields in the proper order before estimating their occupied size.
     * The comparator expects two Field objects. The sorting will return all
     * non-primitive types first, and then the primitive types, in decreasing
     * size order.
     */
    private class DefaultFieldComparator implements Comparator {

        /**
         * Compares two Field objects.
         * @param o1 The first object to compare
         * @param o2 The second object to compare
         * @return -1, 0, +1 if the first object is less than, equal, greater than
         * the second
         * @throws NullPointerException If either of the two objects is <code>null</code>
         * @throws ClassCastException If either of the two objects is not
         * of class Field
         */
        public int compare(Object o1, Object o2) {
            Field f1 = (Field) o1;
            Field f2 = (Field) o2;
            return getFieldSize(f2.getType()) - getFieldSize(f1.getType());
        }
    }
}
