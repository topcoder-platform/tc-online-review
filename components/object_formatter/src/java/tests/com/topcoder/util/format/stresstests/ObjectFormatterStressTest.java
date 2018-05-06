/*
 * ObjectFormatterStressTest.java
 *
 * Copyright © 2002, TopCoder, Inc. All rights reserved
 *
 */

package com.topcoder.util.format.stresstests;

import com.topcoder.util.format.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import java.util.*;

/**
 * A test class for <code>ObjectFormatter</code>.<p>
 * Tests getFormatMethodForObject() method in situation when object could be
 * formatted by more than one method.
 *
 * @author valeriy
 * @version 1.0
 **/
public class ObjectFormatterStressTest extends TestCase { 

    // Increase this value if fastest test tooks less than 1 sec
    private int loops = 30000;

    // object for formatting
    // java.util.Stack extends 4 classes and implements 5 interfaces
    private Stack stack = new Stack();
    
    public void testOneObjectFormatMethod() {
        ObjectFormatter of = ObjectFormatterFactory.getEmptyFormatter();
        of.setFormatMethodForClass(Object.class, new AnyObjectFormatMethod("Object:"), true);
        long start = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            of.getFormatMethodForObject(stack);
        }
        System.out.println("One object format method:   " + ((double)loops / (System.currentTimeMillis() - start)) + " calls per ms");
    }
    
    public void testFiveObjectFormatMethods() {
        ObjectFormatter of = ObjectFormatterFactory.getEmptyFormatter();
        of.setFormatMethodForClass(Object.class, new AnyObjectFormatMethod("Object:"), true);
        of.setFormatMethodForClass(AbstractCollection.class, new AnyObjectFormatMethod("AbstractCollection:"), true);
        of.setFormatMethodForClass(AbstractList.class, new AnyObjectFormatMethod("AbstractList:"), true);
        of.setFormatMethodForClass(Vector.class, new AnyObjectFormatMethod("Vector:"), true);
        of.setFormatMethodForClass(Cloneable.class, new AnyObjectFormatMethod("Cloneable:"), true);
        long start = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            of.getFormatMethodForObject(stack);
        }
        System.out.println("Five object format methods: " + ((double)loops / (System.currentTimeMillis() - start)) + " calls per ms");
    }
    
    public void testNineObjectFormatMethods() {
        ObjectFormatter of = ObjectFormatterFactory.getEmptyFormatter();
        of.setFormatMethodForClass(Object.class, new AnyObjectFormatMethod("Object:"), true);
        of.setFormatMethodForClass(AbstractCollection.class, new AnyObjectFormatMethod("AbstractCollection:"), true);
        of.setFormatMethodForClass(AbstractList.class, new AnyObjectFormatMethod("AbstractList:"), true);
        of.setFormatMethodForClass(Vector.class, new AnyObjectFormatMethod("Vector:"), true);
        of.setFormatMethodForClass(Cloneable.class, new AnyObjectFormatMethod("Cloneable:"), true);
        of.setFormatMethodForClass(Collection.class, new AnyObjectFormatMethod("Collection:"), true);
        of.setFormatMethodForClass(List.class, new AnyObjectFormatMethod("List:"), true);
        of.setFormatMethodForClass(RandomAccess.class, new AnyObjectFormatMethod("RandomAccess:"), true);
        of.setFormatMethodForClass(java.io.Serializable.class, new AnyObjectFormatMethod("Serializable:"), true);
        long start = System.currentTimeMillis();
        for (int i = 0; i < loops; i++) {
            of.getFormatMethodForObject(stack);
        }
        System.out.println("Nine object format methods: " + ((double)loops / (System.currentTimeMillis() - start)) + " calls per ms");
    }
    
    /**
     * Assembles and returns a test suite for
     * all the test methods of this test case.
     *
     * @return A non-null test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ObjectFormatterStressTest.class);
        return suite;
    }
    
    private class AnyObjectFormatMethod implements ObjectFormatMethod {
        
        private String prefix;
        
        AnyObjectFormatMethod(String prefix) {
            this.prefix = prefix;
        }
        
        public String format(Object obj) throws java.lang.IllegalArgumentException {
            return prefix + obj.toString();
        }
    }
}
