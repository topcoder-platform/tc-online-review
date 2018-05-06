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
 * Unit tests for {@link ValidationUtility} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class ValidationUtilityUnitTests {
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
     * Represents the exception class.
     * </p>
     */
    private Class<ConfigurationException> exceptionClass = ConfigurationException.class;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ValidationUtilityUnitTests.class);
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
        ValidationUtility.checkNotNull(valueObj, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNull(Object value, String name)</code> with <code>value</code> is
     * <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNull_valueNull() {
        ValidationUtility.checkNotNull(null, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty(valueStr, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty(TestsHelper.EMPTY_STRING, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty((String) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(String value, String name)</code> with <code>value</code>
     * is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmpty_String_valueEmpty() {
        ValidationUtility.checkNotEmpty("", name, exceptionClass);
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
        ValidationUtility.checkNotEmptyAfterTrimming(valueStr, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyAfterTrimming(null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyAfterTrimming_valueEmpty() {
        ValidationUtility.checkNotEmptyAfterTrimming("", name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyAfterTrimming_valueEmptyAfterTrimming() {
        ValidationUtility.checkNotEmptyAfterTrimming(TestsHelper.EMPTY_STRING, name, exceptionClass);
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
        ValidationUtility.checkNotNullNorEmpty(valueStr, name, exceptionClass);
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
        ValidationUtility.checkNotNullNorEmpty(TestsHelper.EMPTY_STRING, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_String_valueNull() {
        ValidationUtility.checkNotNullNorEmpty((String) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_String_valueEmpty() {
        ValidationUtility.checkNotNullNorEmpty("", name, exceptionClass);
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
        ValidationUtility.checkNotNullNorEmptyAfterTrimming(valueStr, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueNull() {
        ValidationUtility.checkNotNullNorEmptyAfterTrimming(null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueEmpty() {
        ValidationUtility.checkNotNullNorEmptyAfterTrimming("", name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmptyAfterTrimming(String value, String name)</code> with
     * <code>value</code> is empty (without trimming).<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmptyAfterTrimming_valueEmptyAfterTrimming() {
        ValidationUtility.checkNotNullNorEmptyAfterTrimming(TestsHelper.EMPTY_STRING, name, exceptionClass);
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
        ValidationUtility.checkInstance(valueStr, expectedType, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInstance(Object value, Class&lt;?&gt; expectedType, String name)</code>
     * with <code>value</code> is <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInstance_valueNull() {

        ValidationUtility.checkInstance(null, expectedType, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInstance(Object value, Class&lt;?&gt; expectedType, String name)</code>
     * with <code>value</code> is not an instance of String.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInstance_valueInvalid() {
        ValidationUtility.checkInstance(valueObj, expectedType, name, exceptionClass);
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
        ValidationUtility.checkNullOrInstance(valueStr, expectedType, name, exceptionClass);
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
        ValidationUtility.checkNullOrInstance(null, expectedType, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNullOrInstance(Object value, Class&lt;?&gt; expectedType,
     * String name)</code> with <code>value</code> is not an instance of String.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNullOrInstance_valueInvalid() {
        ValidationUtility.checkNullOrInstance(valueObj, expectedType, name, exceptionClass);
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
        ValidationUtility.checkExists(new File(TestsHelper.CONFIG_FILE), name, exceptionClass);
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
        ValidationUtility.checkExists(new File(TestsHelper.TEST_FILES), name, exceptionClass);
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
        ValidationUtility.checkExists(null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkExists(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkExists_NotExist() {
        ValidationUtility.checkExists(new File(TestsHelper.NOT_EXIST_FILE), name, exceptionClass);
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
        ValidationUtility.checkIsFile(new File(TestsHelper.CONFIG_FILE), name, exceptionClass);
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
        ValidationUtility.checkIsFile(null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkIsFile_NotExist() {
        ValidationUtility.checkIsFile(new File(TestsHelper.NOT_EXIST_FILE), name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsFile(File file, String name)</code> with <code>file</code> is a
     * directory.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkIsFile_Directory() {
        ValidationUtility.checkIsFile(new File(TestsHelper.TEST_FILES), name, exceptionClass);
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
        ValidationUtility.checkIsDirectory(new File(TestsHelper.TEST_FILES), name, exceptionClass);
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
        ValidationUtility.checkIsDirectory(null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> does
     * not point to an existing file or directory.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkIsDirectory_NotExist() {
        ValidationUtility.checkIsDirectory(new File(TestsHelper.NOT_EXIST_FILE), name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkIsDirectory(File file, String name)</code> with <code>file</code> is a
     * file.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkIsDirectory_File() {
        ValidationUtility.checkIsDirectory(new File(TestsHelper.CONFIG_FILE), name, exceptionClass);
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
        ValidationUtility.checkNotEmpty(list, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty((Collection<?>) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmpty_Collection_collectionEmpty() {
        list.clear();

        ValidationUtility.checkNotEmpty(list, name, exceptionClass);
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
        ValidationUtility.checkNotNullNorEmpty(list, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_Collection_collectionNull() {
        ValidationUtility.checkNotNullNorEmpty((Collection<?>) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> is empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_Collection_collectionEmpty() {
        list.clear();

        ValidationUtility.checkNotNullNorEmpty(list, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty(map, name, exceptionClass);
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
        ValidationUtility.checkNotEmpty((Map<?, ?>) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmpty_Map_mapEmpty() {
        map.clear();

        ValidationUtility.checkNotEmpty(map, name, exceptionClass);
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
        ValidationUtility.checkNotNullNorEmpty(map, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is <code>null</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_Map_mapNull() {
        ValidationUtility.checkNotNullNorEmpty((Map<?, ?>) null, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullNorEmpty(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> is empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullNorEmpty_Map_mapEmpty() {
        map.clear();

        ValidationUtility.checkNotNullNorEmpty(map, name, exceptionClass);
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
        ValidationUtility.checkNotNullElements(list, name, exceptionClass);
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
        ValidationUtility.checkNotNullElements(null, name, exceptionClass);
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

        ValidationUtility.checkNotNullElements(list, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullElements(Collection&lt;?&gt; collection, String name)</code> with
     * <code>collection</code> contains null elements.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullElements_collectionContainsNull() {
        list.add(null);

        ValidationUtility.checkNotNullElements(list, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyElements(list, false, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyElements(list, true, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyElements(null, true, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyElements(list, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty1() {
        list.add("");

        ValidationUtility.checkNotEmptyElements(list, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty2() {
        list.add(TestsHelper.EMPTY_STRING);

        ValidationUtility.checkNotEmptyElements(list, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty3() {
        list.add(new ArrayList<Object>());

        ValidationUtility.checkNotEmptyElements(list, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyElements(Collection&lt;?&gt; collection, boolean trimStrings,
     * String name)</code> with <code>collection</code> contains empty.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyElements_collectionContainsEmpty4() {
        list.add(new HashMap<Object, Object>());

        ValidationUtility.checkNotEmptyElements(list, true, name, exceptionClass);
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
        ValidationUtility.checkNotNullKeys(map, name, exceptionClass);
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
        ValidationUtility.checkNotNullKeys(null, name, exceptionClass);
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

        ValidationUtility.checkNotNullKeys(map, name, exceptionClass);
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

        ValidationUtility.checkNotNullKeys(map, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullKeys(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> key.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullKeys_mapContainsNullKey() {
        map.put(null, "value");

        ValidationUtility.checkNotNullKeys(map, name, exceptionClass);
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
        map.put("key", "value");

        ValidationUtility.checkNotNullValues(map, name, exceptionClass);
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
        ValidationUtility.checkNotNullValues(null, name, exceptionClass);
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

        ValidationUtility.checkNotNullValues(map, name, exceptionClass);
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

        ValidationUtility.checkNotNullValues(map, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNullValues(Map&lt;?, ?&gt; map, String name)</code> with
     * <code>map</code> contains <code>null</code> value.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNullValues_mapContainsNullValue() {
        map.put("key", null);

        ValidationUtility.checkNotNullValues(map, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyKeys(map, false, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyKeys(map, true, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyKeys(null, true, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyKeys(map, true, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyKeys(map, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey1() {
        map.put("", "value");

        ValidationUtility.checkNotEmptyKeys(map, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey2() {
        map.put(TestsHelper.EMPTY_STRING, "value");

        ValidationUtility.checkNotEmptyKeys(map, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey3() {
        map.put(new ArrayList<Object>(), "value");

        ValidationUtility.checkNotEmptyKeys(map, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyKeys(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty key.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyKeys_mapContainsEmptyKey4() {
        map.put(new HashMap<Object, Object>(), "value");

        ValidationUtility.checkNotEmptyKeys(map, false, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyValues(map, false, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyValues(map, true, name, exceptionClass);
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
        ValidationUtility.checkNotEmptyValues(null, true, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyValues(map, true, name, exceptionClass);
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

        ValidationUtility.checkNotEmptyValues(map, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue1() {
        map.put("key", "");

        ValidationUtility.checkNotEmptyValues(map, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue2() {
        map.put("key", TestsHelper.EMPTY_STRING);

        ValidationUtility.checkNotEmptyValues(map, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue3() {
        map.put("key", new ArrayList<Object>());

        ValidationUtility.checkNotEmptyValues(map, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotEmptyValues(Map&lt;?, ?&gt; map, boolean trimStrings,
     * String name)</code> with <code>map</code> contains empty value.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotEmptyValues_mapContainsEmptyValue4() {
        map.put("key", new HashMap<Object, Object>());

        ValidationUtility.checkNotEmptyValues(map, false, name, exceptionClass);
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
        ValidationUtility.checkNegative(-0.1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(double value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNegative_Double_valueNotNegative1() {
        ValidationUtility.checkNegative(0.0, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(double value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNegative_Double_valueNotNegative2() {
        ValidationUtility.checkNegative(0.1, name, exceptionClass);
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
        ValidationUtility.checkPositive(0.1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(double value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkPositive_Double_valueNotPositive1() {
        ValidationUtility.checkPositive(0.0, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(double value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkPositive_Double_valueNotPositive2() {
        ValidationUtility.checkPositive(-0.1, name, exceptionClass);
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
        ValidationUtility.checkNotNegative(0.0, name, exceptionClass);
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
        ValidationUtility.checkNotNegative(0.1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNegative(double value, String name)</code> with <code>value</code>
     * is negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNegative_Double_valueNegative() {
        ValidationUtility.checkNotNegative(-0.1, name, exceptionClass);
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
        ValidationUtility.checkNotPositive(0.0, name, exceptionClass);
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
        ValidationUtility.checkNotPositive(-0.1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotPositive(double value, String name)</code> with <code>value</code>
     * is positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotPositive_Double_valuePositive() {
        ValidationUtility.checkNotPositive(0.1, name, exceptionClass);
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
        ValidationUtility.checkNotZero(0.1, name, exceptionClass);
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
        ValidationUtility.checkNotZero(-0.1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotZero(double value, String name)</code> with <code>value</code>
     * is zero.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotZero_Double_valueZero() {
        ValidationUtility.checkNotZero(0.0, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(0.1, 0.1, true, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(0.11, 0.1, true, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(0.11, 0.1, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is less than <code>number</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkGreaterThan_Double_valueLessThan() {
        ValidationUtility.checkGreaterThan(0.1, 0.11, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkGreaterThan_Double_valueEqualTo() {
        ValidationUtility.checkGreaterThan(0.1, 0.1, false, name, exceptionClass);
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
        ValidationUtility.checkLessThan(0.1, 0.1, true, name, exceptionClass);
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
        ValidationUtility.checkLessThan(0.1, 0.11, true, name, exceptionClass);
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
        ValidationUtility.checkLessThan(0.1, 0.11, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is greater than <code>number</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkLessThan_Double_valueGreaterThan() {
        ValidationUtility.checkLessThan(0.11, 0.1, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(double value, double number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkLessThan_Double_valueEqualTo() {
        ValidationUtility.checkLessThan(0.1, 0.1, false, name, exceptionClass);
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
        ValidationUtility.checkInRange(0.1, 0.1, 0.2, true, true, name, exceptionClass);
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
        ValidationUtility.checkInRange(0.2, 0.1, 0.2, true, true, name, exceptionClass);
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
        ValidationUtility.checkInRange(0.15, 0.1, 0.2, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Double_valueNotInRange1() {
        ValidationUtility.checkInRange(0.1, 0.1, 0.2, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Double_valueNotInRange2() {
        ValidationUtility.checkInRange(0.2, 0.1, 0.2, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Double_valueNotInRange3() {
        ValidationUtility.checkInRange(0.09, 0.1, 0.2, true, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(double value, double from, double to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Double_valueNotInRange4() {
        ValidationUtility.checkInRange(0.21, 0.1, 0.2, true, true, name, exceptionClass);
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
        ValidationUtility.checkNegative(-1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(long value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNegative_Long_valueNotNegative1() {
        ValidationUtility.checkNegative(0, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNegative(long value, String name)</code> with <code>value</code> is
     * not negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNegative_Long_valueNotNegative2() {
        ValidationUtility.checkNegative(1, name, exceptionClass);
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
        ValidationUtility.checkPositive(1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(long value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkPositive_Long_valueNotPositive1() {
        ValidationUtility.checkPositive(0, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkPositive(long value, String name)</code> with <code>value</code> is
     * not positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkPositive_Long_valueNotPositive2() {
        ValidationUtility.checkPositive(-1, name, exceptionClass);
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
        ValidationUtility.checkNotNegative(0, name, exceptionClass);
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
        ValidationUtility.checkNotNegative(1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotNegative(long value, String name)</code> with <code>value</code>
     * is negative.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotNegative_Long_valueNegative() {
        ValidationUtility.checkNotNegative(-1, name, exceptionClass);
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
        ValidationUtility.checkNotPositive(0, name, exceptionClass);
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
        ValidationUtility.checkNotPositive(-1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotPositive(long value, String name)</code> with <code>value</code>
     * is positive.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotPositive_Long_valuePositive() {
        ValidationUtility.checkNotPositive(1, name, exceptionClass);
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
        ValidationUtility.checkNotZero(1, name, exceptionClass);
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
        ValidationUtility.checkNotZero(-1, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkNotZero(long value, String name)</code> with <code>value</code>
     * is zero.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkNotZero_Long_valueZero() {
        ValidationUtility.checkNotZero(0, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(1, 1, true, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(2, 1, true, name, exceptionClass);
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
        ValidationUtility.checkGreaterThan(2, 1, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is less than <code>number</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkGreaterThan_Long_valueLessThan() {
        ValidationUtility.checkGreaterThan(1, 2, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkGreaterThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkGreaterThan_Long_valueEqualTo() {
        ValidationUtility.checkGreaterThan(1, 1, false, name, exceptionClass);
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
        ValidationUtility.checkLessThan(1, 1, true, name, exceptionClass);
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
        ValidationUtility.checkLessThan(1, 2, true, name, exceptionClass);
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
        ValidationUtility.checkLessThan(1, 2, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is greater than <code>number</code>.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkLessThan_Long_valueGreaterThan() {
        ValidationUtility.checkLessThan(2, 1, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkLessThan(long value, long number, boolean inclusive,
     * String name)</code> with <code>value</code> is equal to <code>number</code> when inclusive is false.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkLessThan_Long_valueEqualTo() {
        ValidationUtility.checkLessThan(1, 1, false, name, exceptionClass);
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
        ValidationUtility.checkInRange(1, 1, 3, true, true, name, exceptionClass);
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
        ValidationUtility.checkInRange(3, 1, 3, true, true, name, exceptionClass);
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
        ValidationUtility.checkInRange(2, 1, 3, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Long_valueNotInRange1() {
        ValidationUtility.checkInRange(1, 1, 3, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Long_valueNotInRange2() {
        ValidationUtility.checkInRange(3, 1, 3, false, false, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Long_valueNotInRange3() {
        ValidationUtility.checkInRange(0, 1, 3, true, true, name, exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>checkInRange(long value, long from, long to, boolean fromInclusive,
     * boolean toInclusive, String name)</code> with <code>value</code> is not in the range.<br>
     * <code>ConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = ConfigurationException.class)
    public void test_checkInRange_Long_valueNotInRange4() {
        ValidationUtility.checkInRange(4, 1, 3, true, true, name, exceptionClass);
    }
}
