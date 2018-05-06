/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link ParameterCheckUtility} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class ParameterCheckUtilityUnitTests {
    /**
     * <p>
     * Represents the name.
     * </p>
     */
    private String name = "name";

    /**
     * <p>
     * Represents the expected type.
     * </p>
     */
    private Class<?> expectedType = String.class;

    /**
     * <p>
     * Represents the list.
     * </p>
     */
    private List<Object> list;

    /**
     * <p>
     * Represents the map.
     * </p>
     */
    private Map<Object, Object> map;

    /**
     * <p>
     * Represents the object value.
     * </p>
     */
    private Object valueObj = new Object();

    /**
     * <p>
     * Represents the string value.
     * </p>
     */
    private String valueStr = "abc";

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ParameterCheckUtilityUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void setUp() {
        list = new ArrayList<Object>();
        list.add(new Object());
        list.add("value");

        map = new HashMap<Object, Object>();
        map.put(new Object(), new Object());
        map.put("key", "value");
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNull(Object value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNull() {
        ParameterCheckUtility.checkNotNull(valueObj, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNull(Object value, String name)</code> with <code>value</code> is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNull_valueNull() {
        ParameterCheckUtility.checkNotNull(null, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(String value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_String() {
        ParameterCheckUtility.checkNotEmpty(valueStr, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(String value, String name)</code> with <code>value</code>
     * is empty (after trimming).<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_String_valueEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotEmpty(TestsHelper.EMPTY_STRING, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(String value, String name)</code> with <code>value</code>
     * is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_String_valueNull() {
        ParameterCheckUtility.checkNotEmpty((String) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(String value, String name)</code> with <code>value</code>
     * is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmpty_String_valueEmpty() {
        ParameterCheckUtility.checkNotEmpty("", name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotEmptyAfterTrimming(valueStr, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyAfterTrimming_valueNull() {
        ParameterCheckUtility.checkNotEmptyAfterTrimming(null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyAfterTrimming_valueEmpty() {
        ParameterCheckUtility.checkNotEmptyAfterTrimming("", name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyAfterTrimming_valueEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotEmptyAfterTrimming(TestsHelper.EMPTY_STRING, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullNorEmpty_String() {
        ParameterCheckUtility.checkNotNullNorEmpty(valueStr, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is empty (after trimming).<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullNorEmpty_String_valueEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotNullNorEmpty(TestsHelper.EMPTY_STRING, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_String_valueNull() {
        ParameterCheckUtility.checkNotNullNorEmpty((String) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_String_valueEmpty() {
        ParameterCheckUtility.checkNotNullNorEmpty("", name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullNorEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming(valueStr, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueNull() {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming(null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueEmpty() {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming("", name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming(TestsHelper.EMPTY_STRING, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInstance(Object value, Class&lt;?&gt; expectedType, String name)</code>
     * with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInstance() {
        ParameterCheckUtility.checkInstance(valueStr, expectedType, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInstance(Object value, Class&lt;?&gt; expectedType, String name)</code>
     * with <code>value</code> is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInstance_valueNull() {
        ParameterCheckUtility.checkInstance(null, expectedType, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInstance(Object value, Class&lt;?&gt; expectedType, String name)</code>
     * with <code>value</code> is not an instance of String.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInstance_valueInvalid() {
        ParameterCheckUtility.checkInstance(valueObj, expectedType, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNullOrInstance(Object value, Class&lt;?&gt; expectedType,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNullOrInstance() {
        ParameterCheckUtility.checkNullOrInstance(valueStr, expectedType, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNullOrInstance(Object value, Class&lt;?&gt; expectedType,
     * String name)</code> with <code>value</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNullOrInstance_valueNull() {
        ParameterCheckUtility.checkNullOrInstance(null, expectedType, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNullOrInstance(Object value, Class&lt;?&gt; expectedType,
     * String name)</code> with <code>value</code> is not an instance of String.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNullOrInstance_valueInvalid() {
        ParameterCheckUtility.checkNullOrInstance(valueObj, expectedType, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkExists(File file, String name)</code> with <code>file</code> is a
     * file.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkExists_File() {
        ParameterCheckUtility.checkExists(new File(TestsHelper.CONFIG_FILE), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkExists(File file, String name)</code> with <code>file</code> is a
     * directory.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkExists_Directory() {
        ParameterCheckUtility.checkExists(new File(TestsHelper.TEST_FILES), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkExists(File file, String name)</code> with <code>file</code> is
     * <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkExists_fileNull() {
        ParameterCheckUtility.checkExists(null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkExists(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkExists_NotExist() {

        ParameterCheckUtility.checkExists(new File(TestsHelper.NOT_EXIST_FILE), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> is a
     * file.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkIsFile_File() {
        ParameterCheckUtility.checkIsFile(new File(TestsHelper.CONFIG_FILE), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> is
     * <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkIsFile_fileNull() {
        ParameterCheckUtility.checkIsFile(null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkIsFile_NotExist() {
        ParameterCheckUtility.checkIsFile(new File(TestsHelper.NOT_EXIST_FILE), name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> is a
     * directory.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkIsFile_Directory() {
        ParameterCheckUtility.checkIsFile(new File(TestsHelper.TEST_FILES), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> is a
     * directory.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkIsDirectory_Directory() {
        ParameterCheckUtility.checkIsDirectory(new File(TestsHelper.TEST_FILES), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> is
     * <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkIsDirectory_fileNull() {
        ParameterCheckUtility.checkIsDirectory(null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkIsDirectory_NotExist() {
        ParameterCheckUtility.checkIsDirectory(new File(TestsHelper.NOT_EXIST_FILE), name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> is a
     * file.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkIsDirectory_File() {
        ParameterCheckUtility.checkIsDirectory(new File(TestsHelper.CONFIG_FILE), name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_Collection() {
        ParameterCheckUtility.checkNotEmpty(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_Collection_collectionNull() {
        ParameterCheckUtility.checkNotEmpty((Collection<?>) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmpty_Collection_collectionEmpty() {
        list.clear();

        ParameterCheckUtility.checkNotEmpty(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullNorEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullNorEmpty_Collection() {
        ParameterCheckUtility.checkNotNullNorEmpty(list, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_Collection_collectionNull() {
        ParameterCheckUtility.checkNotNullNorEmpty((Collection<?>) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_Collection_collectionEmpty() {
        list.clear();

        ParameterCheckUtility.checkNotNullNorEmpty(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_Map() {
        ParameterCheckUtility.checkNotEmpty(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmpty_Map_mapNull() {
        ParameterCheckUtility.checkNotEmpty((Map<?, ?>) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmpty_Map_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotEmpty(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullNorEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullNorEmpty_Map() {
        ParameterCheckUtility.checkNotNullNorEmpty(map, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_Map_mapNull() {
        ParameterCheckUtility.checkNotNullNorEmpty((Map<?, ?>) null, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullNorEmpty_Map_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotNullNorEmpty(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullElements(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullElements() {
        ParameterCheckUtility.checkNotNullElements(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullElements(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullElements_collectionNull() {
        ParameterCheckUtility.checkNotNullElements(null, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullElements(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is empty.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullElements_collectionEmpty() {
        list.clear();

        ParameterCheckUtility.checkNotNullElements(list, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullElements(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> contains null elements.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullElements_collectionContainsNull() {
        list.add(null);

        ParameterCheckUtility.checkNotNullElements(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyElements() {
        list.add(TestsHelper.EMPTY_STRING);

        ParameterCheckUtility.checkNotEmptyElements(list, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyElements_Trim() {
        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyElements_collectionNull() {
        ParameterCheckUtility.checkNotEmptyElements(null, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyElements_collectionContainsNull() {
        list.add(null);

        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty1() {
        list.add("");

        ParameterCheckUtility.checkNotEmptyElements(list, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty2() {
        list.add(TestsHelper.EMPTY_STRING);

        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty3() {
        list.add(new ArrayList<Object>());

        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty4() {
        list.add(new HashMap<Object, Object>());

        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullKeys() {
        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullKeys_mapNull() {
        ParameterCheckUtility.checkNotNullKeys(null, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullKeys_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> value.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullKeys_mapContainsNullValue() {
        map.put("key", null);

        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> key.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullKeys_mapContainsNullKey() {
        map.put(null, "value");

        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullValues() {
        ParameterCheckUtility.checkNotNullValues(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullValues_mapNull() {
        ParameterCheckUtility.checkNotNullValues(null, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullValues_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotNullValues(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> key.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNullValues_mapContainsNullKey() {
        map.put(null, "value");

        ParameterCheckUtility.checkNotNullValues(map, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> value.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNullValues_mapContainsNullValue() {
        map.put("key", null);

        ParameterCheckUtility.checkNotNullValues(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyKeys() {
        map.put(TestsHelper.EMPTY_STRING, "value");

        ParameterCheckUtility.checkNotEmptyKeys(map, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyKeys_Trim() {
        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyKeys_mapNull() {
        ParameterCheckUtility.checkNotEmptyKeys(null, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is empty.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyKeys_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyKeys_mapContainsEmptyValue() {
        map.put("key", "");

        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey1() {
        map.put("", "value");

        ParameterCheckUtility.checkNotEmptyKeys(map, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey2() {
        map.put(TestsHelper.EMPTY_STRING, "value");

        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey3() {
        map.put(new ArrayList<Object>(), "value");

        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey4() {
        map.put(new HashMap<Object, Object>(), "value");

        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyValues() {
        map.put("key", TestsHelper.EMPTY_STRING);

        ParameterCheckUtility.checkNotEmptyValues(map, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyValues_Trim() {
        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is <code>null</code>.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyValues_mapNull() {
        ParameterCheckUtility.checkNotEmptyValues(null, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> is empty.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyValues_mapEmpty() {
        map.clear();

        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotEmptyValues_mapContainsEmptyKey() {
        map.put("", "value");

        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue1() {
        map.put("key", "");

        ParameterCheckUtility.checkNotEmptyValues(map, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue2() {
        map.put("key", TestsHelper.EMPTY_STRING);

        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue3() {
        map.put("key", new ArrayList<Object>());

        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue4() {
        map.put("key", new HashMap<Object, Object>());

        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNegative(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNegative_Double() {
        ParameterCheckUtility.checkNegative(-0.1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(double value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNegative_Double_valueNotNegative1() {
        ParameterCheckUtility.checkNegative(0.0, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(double value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNegative_Double_valueNotNegative2() {
        ParameterCheckUtility.checkNegative(0.1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkPositive(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkPositive_Double() {
        ParameterCheckUtility.checkPositive(0.1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(double value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkPositive_Double_valueNotPositive1() {
        ParameterCheckUtility.checkPositive(0.0, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(double value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkPositive_Double_valueNotPositive2() {
        ParameterCheckUtility.checkPositive(-0.1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNegative(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNegative_Double_1() {
        ParameterCheckUtility.checkNotNegative(0.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNegative(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNegative_Double_2() {
        ParameterCheckUtility.checkNotNegative(0.1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNegative(double value, String name)</code> with <code>value</code>
     * is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNegative_Double_valueNegative() {
        ParameterCheckUtility.checkNotNegative(-0.1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotPositive(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotPositive_Double_1() {
        ParameterCheckUtility.checkNotPositive(0.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotPositive(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotPositive_Double_2() {
        ParameterCheckUtility.checkNotPositive(-0.1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotPositive(double value, String name)</code> with <code>value</code>
     * is positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotPositive_Double_valuePositive() {
        ParameterCheckUtility.checkNotPositive(0.1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotZero(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotZero_Double_1() {
        ParameterCheckUtility.checkNotZero(0.1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotZero(double value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotZero_Double_2() {
        ParameterCheckUtility.checkNotZero(-0.1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotZero(double value, String name)</code> with <code>value</code>
     * is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotZero_Double_valueZero() {
        ParameterCheckUtility.checkNotZero(0.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Double_1() {
        ParameterCheckUtility.checkGreaterThan(0.1, 0.1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Double_2() {
        ParameterCheckUtility.checkGreaterThan(0.11, 0.1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Double_3() {
        ParameterCheckUtility.checkGreaterThan(0.11, 0.1, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is less than <code>number</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkGreaterThan_Double_valueLessThan() {
        ParameterCheckUtility.checkGreaterThan(0.1, 0.11, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkGreaterThan_Double_valueEqualTo() {
        ParameterCheckUtility.checkGreaterThan(0.1, 0.1, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Double_1() {
        ParameterCheckUtility.checkLessThan(0.1, 0.1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Double_2() {
        ParameterCheckUtility.checkLessThan(0.1, 0.11, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Double_3() {
        ParameterCheckUtility.checkLessThan(0.1, 0.11, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is greater than <code>number</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkLessThan_Double_valueGreaterThan() {
        ParameterCheckUtility.checkLessThan(0.11, 0.1, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkLessThan_Double_valueEqualTo() {
        ParameterCheckUtility.checkLessThan(0.1, 0.1, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Double_1() {
        ParameterCheckUtility.checkInRange(0.1, 0.1, 0.2, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Double_2() {
        ParameterCheckUtility.checkInRange(0.2, 0.1, 0.2, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Double_3() {
        ParameterCheckUtility.checkInRange(0.15, 0.1, 0.2, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Double_valueNotInRange1() {
        ParameterCheckUtility.checkInRange(0.1, 0.1, 0.2, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Double_valueNotInRange2() {
        ParameterCheckUtility.checkInRange(0.2, 0.1, 0.2, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Double_valueNotInRange3() {
        ParameterCheckUtility.checkInRange(0.09, 0.1, 0.2, true, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Double_valueNotInRange4() {
        ParameterCheckUtility.checkInRange(0.21, 0.1, 0.2, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNegative(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNegative_Long() {
        ParameterCheckUtility.checkNegative(-1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(long value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNegative_Long_valueNotNegative1() {
        ParameterCheckUtility.checkNegative(0, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(long value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNegative_Long_valueNotNegative2() {
        ParameterCheckUtility.checkNegative(1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkPositive(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkPositive_Long() {
        ParameterCheckUtility.checkPositive(1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(long value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkPositive_Long_valueNotPositive1() {
        ParameterCheckUtility.checkPositive(0, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(long value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkPositive_Long_valueNotPositive2() {
        ParameterCheckUtility.checkPositive(-1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNegative(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNegative_Long_1() {
        ParameterCheckUtility.checkNotNegative(0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotNegative(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotNegative_Long_2() {
        ParameterCheckUtility.checkNotNegative(1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNegative(long value, String name)</code> with <code>value</code>
     * is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotNegative_Long_valueNegative() {
        ParameterCheckUtility.checkNotNegative(-1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotPositive(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotPositive_Long_1() {
        ParameterCheckUtility.checkNotPositive(0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotPositive(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotPositive_Long_2() {
        ParameterCheckUtility.checkNotPositive(-1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotPositive(long value, String name)</code> with <code>value</code>
     * is positive.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotPositive_Long_valuePositive() {
        ParameterCheckUtility.checkNotPositive(1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotZero(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotZero_Long_1() {
        ParameterCheckUtility.checkNotZero(1, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkNotZero(long value, String name)</code> with <code>value</code>
     * is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkNotZero_Long_2() {
        ParameterCheckUtility.checkNotZero(-1, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotZero(long value, String name)</code> with <code>value</code>
     * is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNotZero_Long_valueZero() {
        ParameterCheckUtility.checkNotZero(0, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Long_1() {
        ParameterCheckUtility.checkGreaterThan(1, 1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Long_2() {
        ParameterCheckUtility.checkGreaterThan(2, 1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkGreaterThan_Long_3() {
        ParameterCheckUtility.checkGreaterThan(2, 1, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is less than <code>number</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkGreaterThan_Long_valueLessThan() {
        ParameterCheckUtility.checkGreaterThan(1, 2, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkGreaterThan_Long_valueEqualTo() {
        ParameterCheckUtility.checkGreaterThan(1, 1, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Long_1() {
        ParameterCheckUtility.checkLessThan(1, 1, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Long_2() {
        ParameterCheckUtility.checkLessThan(1, 2, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkLessThan_Long_3() {
        ParameterCheckUtility.checkLessThan(1, 2, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is greater than <code>number</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkLessThan_Long_valueGreaterThan() {
        ParameterCheckUtility.checkLessThan(2, 1, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkLessThan_Long_valueEqualTo() {
        ParameterCheckUtility.checkLessThan(1, 1, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Long_1() {
        ParameterCheckUtility.checkInRange(1, 1, 3, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Long_2() {
        ParameterCheckUtility.checkInRange(3, 1, 3, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is valid.<br>
     * No exception would occur.
     * </p>
     */
    @Test
    public void test_checkInRange_Long_3() {
        ParameterCheckUtility.checkInRange(2, 1, 3, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Long_valueNotInRange1() {
        ParameterCheckUtility.checkInRange(1, 1, 3, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Long_valueNotInRange2() {
        ParameterCheckUtility.checkInRange(3, 1, 3, false, false, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Long_valueNotInRange3() {
        ParameterCheckUtility.checkInRange(0, 1, 3, true, true, name);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkInRange_Long_valueNotInRange4() {
        ParameterCheckUtility.checkInRange(4, 1, 3, true, true, name);
    }
}
