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
 * @author dingying131
 * @version 1.0
 */
public class ValidationUtilityAccuracyTests {

    /**
     * <p>
     * Represents the test file.
     * </p>
     */
    private File file = new File("test_files/accuracy.properties");

    /**
     * <p>
     * Represents the test directory.
     * </p>
     */
    private File dir = new File("test_files");

    /**
     * <p>
     * Represents the name for parameter.
     * </p>
     */
    private String name = "name";

    /**
     * <p>
     * Represents the list for tests.
     * </p>
     */
    private List<String> list;

    /**
     * <p>
     * Represents the map for tests.
     * </p>
     */
    private Map<String, String> map;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ValidationUtilityAccuracyTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     */
    @Before
    public void before() {
        list = new ArrayList<String>();
        list.add("value");
        map = new HashMap<String, String>();
        map.put("key", "value");
    }

    /**
     * <p>
     * Accuracy test for the method {@ValidationUtility#checkNotNull(Object,
     * String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNull() {
        ValidationUtility.checkNotNull("test", name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method @ ValidationUtility#checkNotEmpty(String, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty() {
        ValidationUtility.checkNotEmpty("test", name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmpty((String) null, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@ValidationUtility#checkNotEmptyAfterTrimming(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyAfterTrimming() {
        ValidationUtility.checkNotEmptyAfterTrimming("test  ", name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmptyAfterTrimming((String) null, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@ValidationUtility#checkNotNullNorEmpty(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty() {
        ValidationUtility.checkNotNullNorEmpty("test", name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@ValidationUtility#checkNotNullNorEmptyAfterTrimming(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmptyAfterTrimming() {
        ValidationUtility.checkNotNullNorEmptyAfterTrimming("test   ", name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method ValidationUtility#checkInstance(Object, Class, String).
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInstance() {
        ValidationUtility.checkInstance("test", String.class, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNullOrInstance(Object, Class, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNullOrInstance() {
        ValidationUtility.checkNullOrInstance(null, String.class, name, IllegalArgumentException.class);
        ValidationUtility.checkNullOrInstance("test", String.class, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkExists(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckExists() {
        ValidationUtility.checkExists(null, name, IllegalArgumentException.class);
        ValidationUtility.checkExists(file, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkIsFile(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckIsFile() {
        ValidationUtility.checkIsFile(null, name, IllegalArgumentException.class);
        ValidationUtility.checkIsFile(file, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkIsDirectory(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckIsDirectory() {
        ValidationUtility.checkIsDirectory(null, name, IllegalArgumentException.class);
        ValidationUtility.checkIsDirectory(dir, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotEmpty(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty2() {
        ValidationUtility.checkNotEmpty((Collection<?>) null, name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmpty(list, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNullNorEmpty(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty2() {
        ValidationUtility.checkNotEmpty(list, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotEmpty(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty3() {
        ValidationUtility.checkNotEmpty((Map<?, ?>) null, name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmpty(map, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNullNorEmpty(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty3() {
        ValidationUtility.checkNotEmpty(map, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNullElements(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullElements() {
        ValidationUtility.checkNotNullElements((Collection<?>) null, name, IllegalArgumentException.class);
        ValidationUtility.checkNotNullElements(list, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotEmptyElements(Collection, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyElements() {
        ValidationUtility.checkNotEmptyElements((Collection<?>) null, true, name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmptyElements(list, true, name, IllegalArgumentException.class);
        list.add("  ");
        ValidationUtility.checkNotEmptyElements(list, false, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNullKeys(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullKeys() {
        ValidationUtility.checkNotNullKeys((Map<?, ?>) null, name, IllegalArgumentException.class);
        ValidationUtility.checkNotNullKeys(map, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNullValues(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullValues() {
        ValidationUtility.checkNotNullKeys((Map<?, ?>) null, name, IllegalArgumentException.class);
        ValidationUtility.checkNotNullKeys(map, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotEmptyKeys(Map, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyKeys() {
        ValidationUtility.checkNotEmptyKeys((Map<?, ?>) null, false, name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmptyKeys(map, true, name, IllegalArgumentException.class);
        map.put("  ", "1");
        ValidationUtility.checkNotEmptyKeys(map, false, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotEmptyValues(Map, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyValues() {
        ValidationUtility.checkNotEmptyValues((Map<?, ?>) null, false, name, IllegalArgumentException.class);
        ValidationUtility.checkNotEmptyValues(map, true, name, IllegalArgumentException.class);
        map.put("1", "  ");
        ValidationUtility.checkNotEmptyValues(map, false, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNegative(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNegative() {
        ValidationUtility.checkNegative(-1.0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkPositive(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckPositive() {
        ValidationUtility.checkPositive(1.0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNegative(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNegative() {
        ValidationUtility.checkNotNegative(1.0, name, IllegalArgumentException.class);
        ValidationUtility.checkNotNegative(0.0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotPositive(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotPositive() {
        ValidationUtility.checkNotPositive(-1.0, name, IllegalArgumentException.class);
        ValidationUtility.checkNotPositive(0.0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotZero(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotZero() {
        ValidationUtility.checkNotPositive(-1.0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkGreaterThan(double, double, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckGreaterThan() {
        ValidationUtility.checkGreaterThan(1.0, 0.0, false, name, IllegalArgumentException.class);
        ValidationUtility.checkGreaterThan(1.0, 0.0, true, name, IllegalArgumentException.class);
        ValidationUtility.checkGreaterThan(0.0, 0.0, true, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkLessThan(double, double, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckLesshan() {
        ValidationUtility.checkLessThan(0.0, 1.0, false, name, IllegalArgumentException.class);
        ValidationUtility.checkLessThan(0.0, 1.0, true, name, IllegalArgumentException.class);
        ValidationUtility.checkLessThan(0.0, 0.0, true, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method
     * {@link ValidationUtility#checkInRange(double, double, double, boolean, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInRange() {
        ValidationUtility.checkInRange(1.0, 0.0, 2.0, false, false, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1.0, 1.0, 2.0, true, false, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1.0, 0.0, 1.0, false, true, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1.0, 1.0, 1.0, true, true, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNegative(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNegative2() {
        ValidationUtility.checkNegative(-1, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkPositive(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckPositive2() {
        ValidationUtility.checkPositive(1, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotNegative(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNegative2() {
        ValidationUtility.checkNotNegative(1, name, IllegalArgumentException.class);
        ValidationUtility.checkNotNegative(0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotPositive(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotPositive2() {
        ValidationUtility.checkNotPositive(-1, name, IllegalArgumentException.class);
        ValidationUtility.checkNotPositive(0, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkNotZero(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotZero2() {
        ValidationUtility.checkNotPositive(-1, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkGreaterThan(long, long, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckGreaterThan2() {
        ValidationUtility.checkGreaterThan(1, 0, false, name, IllegalArgumentException.class);
        ValidationUtility.checkGreaterThan(1, 0, true, name, IllegalArgumentException.class);
        ValidationUtility.checkGreaterThan(0, 0, true, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkLessThan(long, long, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckLesshan2() {
        ValidationUtility.checkLessThan(0, 1, false, name, IllegalArgumentException.class);
        ValidationUtility.checkLessThan(0, 1, true, name, IllegalArgumentException.class);
        ValidationUtility.checkLessThan(0, 0, true, name, IllegalArgumentException.class);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ValidationUtility#checkInRange(long, long, long, boolean, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInRange2() {
        ValidationUtility.checkInRange(1, 0, 2, false, false, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1, 1, 2, true, false, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1, 0, 1, false, true, name, IllegalArgumentException.class);
        ValidationUtility.checkInRange(1, 1, 1, true, true, name, IllegalArgumentException.class);
    }
}
