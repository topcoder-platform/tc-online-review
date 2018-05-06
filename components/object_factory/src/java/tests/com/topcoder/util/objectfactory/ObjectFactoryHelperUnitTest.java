/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.objectfactory;

import junit.framework.TestCase;

/**
 * Unit test for {@link ObjectFactoryHelper}.
 *
 * @author TCSDEVELOPER
 * @version 2.2
 * @since 2.2
 */
public class ObjectFactoryHelperUnitTest extends TestCase {
    /**
     * <p>
     * Accuracy test for the method
     * <code>checkObjectNotNull(Object obj, String name)</code> with obj is
     * valid. No exception would occur.
     * </p>
     */
    public void testCheckObjectNotNull() {
        ObjectFactoryHelper.checkObjectNotNull("test", "test");
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkObjectNotNull(Object obj, String name)</code> with obj is
     * null. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckObjectNotNull2() {
        try {
            ObjectFactoryHelper.checkObjectNotNull(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkStringNotEmpty(String str, String name)</code> with str is
     * valid. No exception would occur.
     * </p>
     */
    public void testCheckStringNotEmpty() {
        ObjectFactoryHelper.checkStringNotEmpty("test", "test");
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkStringNotEmpty(String str, String name)</code> with str is
     * null. No exception would occur.
     * </p>
     */
    public void testCheckStringNotEmpty2() {
        ObjectFactoryHelper.checkStringNotEmpty(null, "test");
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkStringNotEmpty(String str, String name)</code> with str is
     * empty. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckStringNotEmpty3() {
        try {
            ObjectFactoryHelper.checkStringNotEmpty(" ", "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkStringNotNullOrEmpty(String str, String name)</code> with str
     * is valid. No exception would occur.
     * </p>
     */
    public void testCheckStringNotNullOrEmpty() {
        ObjectFactoryHelper.checkStringNotNullOrEmpty("test", "test");
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkStringNotNullOrEmpty(String str, String name)</code> with str
     * is null. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckStringNotNullOrEmpty2() {
        try {
            ObjectFactoryHelper.checkStringNotNullOrEmpty(" ", "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkStringNotNullOrEmpty(String str, String name)</code> with str
     * is empty. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckStringNotNullOrEmpty3() {
        try {
            ObjectFactoryHelper.checkStringNotNullOrEmpty(" ", "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkClassValid(String jar, String className)</code> with className
     * is valid. No exception would occur.
     * </p>
     */
    public void testCheckClassValid() {
        ObjectFactoryHelper.checkClassValid(null, "java.lang.String");
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkClassValid(String jar, String className)</code> with className
     * is wrong. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckClassValid2() {
        try {
            ObjectFactoryHelper.checkClassValid(null, "java.lang.String1");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkClassValid(String jar, String className)</code> with jar is
     * wrong. IllegalArgumentException is expected.
     * </p>
     */
    public void testCheckClassValid3() {
        try {
            ObjectFactoryHelper.checkClassValid("jar://test.jar", "com.test.TestComplex");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>getDimensionArrayFromArray(Object param)</code> with param is one
     * dimensional array. No exception would occur.
     * </p>
     */
    public void testGetDimensionArrayFromArray() {
        int[] dims = ObjectFactoryHelper.getDimensionArrayFromArray(new String[] { "1", "2", "3" });
        assertEquals(3, dims[0]);
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>getDimensionArrayFromArray(Object param)</code> with param is two
     * dimensional array. No exception would occur.
     * </p>
     */
    public void testGetDimensionArrayFromArray2() {
        int[] dims = ObjectFactoryHelper.getDimensionArrayFromArray(new Integer[][] { { 1, 1 }, { 2, 2 }, { 3, 3 } });
        assertEquals(3, dims[0]);
        assertEquals(2, dims[1]);
    }

    /**
     * <p>
     * Failure test for the method
     * <code>getDimensionArrayFromArray(Object param)</code> with param is
     * wrong. IllegalArgumentException is expected.
     * </p>
     */
    public void testGetDimensionArrayFromArray3() {
        try {
            ObjectFactoryHelper.getDimensionArrayFromArray(new Integer[][] { { 1, 1 }, { 2 } });
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkArrayDimension(Object[] param, int dim)</code> with expected
     * dimension. No exception would occur.
     * </p>
     */
    public void testCheckArrayDimension() {
        ObjectFactoryHelper.checkArrayDimension(new String[] { "1", "2", "3" }, 1);
    }

    /**
     * <p>
     * Failure test for the method
     * <code>checkArrayDimension(Object[] param, int dim)</code> with unexpected
     * dimension. IllegalArgumentException is expected.
     * </p>
     */
    public void checkArrayDimension2() {
        try {
            ObjectFactoryHelper.checkArrayDimension(new String[] { "1", "2", "3" }, 2);
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkSimpleType(String type)</code>
     * with simple type. True is expected.
     * </p>
     */
    public void testCheckSimpleType() {
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.BOOLEAN));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.BYTE));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.CHAR));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.DOUBLE));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.FLOAT));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.INT));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.LONG));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.SHORT));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.STRING));
        assertEquals(true, ObjectFactoryHelper.checkSimpleType(ObjectSpecification.STRING_FULL));
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkSimpleType(String type)</code>
     * with not simple type. False is expected.
     * </p>
     */
    public void testCheckSimpleType2() {
        assertEquals(false, ObjectFactoryHelper.checkSimpleType("java.lang.Object"));
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkStringSimpleType(String type)</code> with string type. True is
     * expected.
     * </p>
     */
    public void testCheckStringSimpleType() {
        assertEquals(true, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.STRING));
        assertEquals(true, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.STRING_FULL));
    }

    /**
     * <p>
     * Accuracy test for the method
     * <code>checkStringSimpleType(String type)</code> with not string type.
     * False is expected.
     * </p>
     */
    public void testCheckStringSimpleType2() {
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.BOOLEAN));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.BYTE));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.CHAR));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.DOUBLE));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.FLOAT));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.INT));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.LONG));
        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType(ObjectSpecification.SHORT));

        assertEquals(false, ObjectFactoryHelper.checkStringSimpleType("java.lang.Object"));
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNullInArray(Object[] objs)</code>
     * with objs contains null. True is expected.
     * </p>
     */
    public void testCheckNullInArray() {
        assertEquals(true, ObjectFactoryHelper.checkNullInArray(new String[] { null }));
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNullInArray(Object[] objs)</code>
     * with objs not contains null. False is expected.
     * </p>
     */
    public void testCheckNullInArray2() {
        assertEquals(false, ObjectFactoryHelper.checkNullInArray(new String[] { "test" }));
    }
}
