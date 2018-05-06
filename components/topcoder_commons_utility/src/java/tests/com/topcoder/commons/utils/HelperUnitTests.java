/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link Helper} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class HelperUnitTests {
    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HelperUnitTests.class);
    }

    /**
     * <p>
     * Tests accuracy of <code>containNull(Collection&lt;?&gt; collection)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containNull_False() {
        Collection<?> collection = Arrays.asList(new Object[] {1, TestsHelper.EMPTY_STRING});

        boolean res = Helper.containNull(collection);

        assertFalse("'containNull' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containNull(Collection&lt;?&gt; collection)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containNull_True() {
        Collection<?> collection = Arrays.asList(new Object[] {1, TestsHelper.EMPTY_STRING, null});

        boolean res = Helper.containNull(collection);

        assertTrue("'containNull' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_False1() {
        Collection<?> collection = Arrays.asList(new Object[] {1, TestsHelper.EMPTY_STRING});
        boolean trimStrings = false;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertFalse("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_False2() {
        List<Object> list = new ArrayList<Object>();
        list.add(new Object());
        Map<Object, Object> map = new HashMap<Object, Object>();
        map.put(new Object(), new Object());
        Collection<?> collection = Arrays.asList(new Object[] {1, list, map});
        boolean trimStrings = true;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertFalse("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_True1() {
        Collection<?> collection = Arrays.asList(new Object[] {1, ""});
        boolean trimStrings = false;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertTrue("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_True2() {
        Collection<?> collection = Arrays.asList(new Object[] {1, TestsHelper.EMPTY_STRING});
        boolean trimStrings = true;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertTrue("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_True3() {
        Collection<?> collection = Arrays.asList(new Object[] {1, new ArrayList<Object>()});
        boolean trimStrings = false;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertTrue("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>containEmpty(Collection&lt;?&gt; collection, boolean trimStrings)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_containEmpty_True4() {
        Collection<?> collection = Arrays.asList(new Object[] {1, new HashMap<Object, Object>()});
        boolean trimStrings = false;

        boolean res = Helper.containEmpty(collection, trimStrings);

        assertTrue("'containEmpty' should be correct.", res);
    }

    /**
     * <p>
     * Tests accuracy of <code>concat(Object... values)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_concat() {
        String res = Helper.concat("str", 1, "str", "2");

        assertEquals("'concat' should be correct.", "str1str2", res);
    }
}
