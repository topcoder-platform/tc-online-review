/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.memoryusage.analyzers;

import com.topcoder.util.memoryusage.BaseAnalyzer;
import com.topcoder.util.memoryusage.MemoryUsageHelper;
import java.lang.reflect.Field;
import java.util.Comparator;

/**
 * <p>
 * Memory usage analyzer implementation for the Sun JVMs 1.5.
 * Compared to the base memory model, the 1.5 memory model does not pad each
 * field to 4 bytes, but uses its native size. Moreover, the fields are sorted
 * differently: first the non-primitive types, then the primitive types in
 * decreasing size order.
 * </p>
 * <p><b>Thread safety:</b> This class is thread safe since it has no state. </p>
 *
 * @author AleaActaEst, TexWiller
 * @version 2.0
 * @since 2.0
 */
public class Sun15Analyzer extends BaseAnalyzer {

    /**
     * An instance of the field Comparator used by this class.
     */
    private final Comparator sun15Comparator = new Sun15FieldComparator();

    /**
     * Creates a new instance of this analyzer.
     */
    public Sun15Analyzer() {
        super("Sun Microsystems, Inc.", "JDK 1.5.x");
    }

    /**
     * Detects the running JVM is the Sun 1.5 JVM.
     *
     * @return <code>true</code> if the running JVM is the Sun 1.5 JVM; <code>false</code>otherwise.
     */
    public boolean matchesJVM() {
        return MemoryUsageHelper.checkVendorVersion("Sun Microsystems", "49.");
    }

    /**
     * Get the size of a class field of the given class type. In Sun JVM 1.5,
     * this is the actual size of the fields, instead of its padding to 4 bytes.
     *
     * @param cls The type of the field
     * @return Size occupied by the field, in bytes
     * @throws IllegalArgumentException If the <i>cls</i> parameter is <code>null</code>
     */
    public int getFieldSize(Class cls) {
        return MemoryUsageHelper.getFieldSize(cls);
    }

    /**
     * Overrides the basic <code>getFieldComparator()</code> method, in order
     * to specify the different sorting of the fields which is used in JVM 1.5.
     * @return A Comparator which sorts the field according to JVM 1.5 order:
     * first non-primitive types, then primitive types in decreasing size order.
     */
    public Comparator getFieldComparator() {
        return sun15Comparator;
    }


    /**
     * This class implements the Comparator interface, in order to sort
     * the fields in the proper order before estimating their occupied size.
     * The comparator expects two Field objects. The sorting will return all
     * non-primitive types first, and then the primitive types, in decreasing
     * size order.
     */
    private class Sun15FieldComparator implements Comparator {

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
            int p1 = (f1.getType().isPrimitive() ? 1 : 0);
            int p2 = (f2.getType().isPrimitive() ? 1 : 0);

            if (p1 != p2) {
                return p1 - p2;
            } else {
                return getFieldSize(f2.getType()) - getFieldSize(f1.getType());
            }
        }
    }
}
