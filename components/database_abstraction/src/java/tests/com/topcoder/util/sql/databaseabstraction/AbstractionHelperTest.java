/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import junit.framework.TestCase;

/**
 * Test case for {@link AbstractionHelper}.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class AbstractionHelperTest extends TestCase {

    /**
     * <p>
     * Test checkNull(Object arg, String name)).
     * </p>
     * <p>
     * Verify: When arg is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCheckNull1() {
        try {
            AbstractionHelper.checkNull(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test checkNull(Object arg, String name)).
     * </p>
     * <p>
     * Verify: error message of IllegalArgumentException contains necessary
     * information.
     * </p>
     */
    public void testCheckNull2() {
        try {
            AbstractionHelper.checkNull(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            assertTrue("name is not contained in the error message.", e.getMessage().indexOf("test") >= 0);
        }
    }

    /**
     * <p>
     * Test checkArray(Object[] arg, String name).
     * </p>
     * <p>
     * Verify:When arg is null, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCheckArray1() {
        try {
            AbstractionHelper.checkArray(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test checkArray(Object[] arg, String name).
     * </p>
     * <p>
     * Verify:When arg contains null element, IllegalArgumentException is
     * thrown.
     * </p>
     */
    public void testCheckArray2() {
        try {
            AbstractionHelper.checkArray(new String[] { "test1", null, "test2" }, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test checkArray(Object[] arg, String name).
     * </p>
     * <p>
     * Verify:error message of IllegalArgumentException contains necessary
     * information.
     * </p>
     */
    public void testCheckArray3() {
        try {
            AbstractionHelper.checkArray(null, "test");
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            assertTrue("name is not contained in the error message.", e.getMessage().indexOf("test") >= 0);
        }
    }

    /**
     * <p>
     * Test checkColumnIndex(int column, int maxValue).
     * </p>
     * <p>
     * Verify:when column <=0, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCheckColumnIndex1() {
        try {
            AbstractionHelper.checkColumnIndex(0, 2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test checkColumnIndex(int column, int maxValue).
     * </p>
     * <p>
     * Verify:when column > maxVlaue, IllegalArgumentException is thrown.
     * </p>
     */
    public void testCheckColumnIndex2() {
        try {
            AbstractionHelper.checkColumnIndex(3, 2);
            fail("IllegalArgumentException expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test canConvert(Object value,Class orgClass, Class desiredType,Class<?>[]
     * canConvertTo)).
     * </p>
     * <p>
     * Verify:when value is null,false is returned.
     * </p>
     */
    public void testCanConvert1() {
        assertFalse("canConvert is incorrect.",
                AbstractionHelper.canConvert(null, String.class, Long.class, new Class<?>[] {}));
    }

    /**
     * <p>
     * Test canConvert(Object value,Class orgClass, Class desiredType,Class<?>[]
     * canConvertTo)).
     * </p>
     * <p>
     * Verify:when value is type of orgClass,false is returned.
     * </p>
     */
    public void testCanConvert2() {
        assertFalse("canConvert is incorrect.",
                AbstractionHelper.canConvert(new Long(100), String.class, Long.class, new Class<?>[] {}));
    }

    /**
     * <p>
     * Test canConvert(Object value,Class orgClass, Class desiredType,Class<?>[]
     * canConvertTo)).
     * </p>
     * <p>
     * Verify:when desiredType is not in the list of canConvertTo,false is
     * returned.
     * </p>
     */
    public void testCanConvert3() {
        assertFalse(
                "canConvert is incorrect.",
                AbstractionHelper.canConvert(new Long(100), Long.class, String.class, new Class<?>[] { Long.class,
                    Byte.class }));
    }

    /**
     * <p>
     * Test canConvert(Object value,Class orgClass, Class desiredType,Class<?>[]
     * canConvertTo)).
     * </p>
     * <p>
     * Verify:canConvert is correct.
     * </p>
     */
    public void testCanConvert4() {
        assertTrue(
                "canConvert is incorrect.",
                AbstractionHelper.canConvert(new Long(100), Long.class, String.class, new Class<?>[] { Long.class,
                    String.class }));
    }
}
