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
 * @author dingying131
 * @version 1.0
 */
public class ParameterCheckUtilityAccuracyTests {

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
        return new JUnit4TestAdapter(ParameterCheckUtilityAccuracyTests.class);
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
     * Accuracy test for the method @ ParameterCheckUtility#checkNotNull(Object, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNull() {
        ParameterCheckUtility.checkNotNull("test", name);
    }

    /**
     * <p>
     * Accuracy test for the method @ ParameterCheckUtility#checkNotEmpty(String, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty() {
        ParameterCheckUtility.checkNotEmpty("test", name);
        ParameterCheckUtility.checkNotEmpty((String) null, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@ParameterCheckUtility#checkNotEmptyAfterTrimming(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotEmptyAfterTrimming("test  ", name);
        ParameterCheckUtility.checkNotEmptyAfterTrimming((String) null, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@ParameterCheckUtility#checkNotNullNorEmpty(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty() {
        ParameterCheckUtility.checkNotNullNorEmpty("test", name);
    }

    /**
     * <p>
     * Accuracy test for the method {@ParameterCheckUtility#checkNotNullNorEmptyAfterTrimming(String,
     *  String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmptyAfterTrimming() {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming("test   ", name);
    }

    /**
     * <p>
     * Accuracy test for the method ParameterCheckUtility#checkInstance(Object, Class, String).
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInstance() {
        ParameterCheckUtility.checkInstance("test", String.class, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNullOrInstance(Object, Class, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNullOrInstance() {
        ParameterCheckUtility.checkNullOrInstance(null, String.class, name);
        ParameterCheckUtility.checkNullOrInstance("test", String.class, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkExists(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckExists() {
        ParameterCheckUtility.checkExists(null, name);
        ParameterCheckUtility.checkExists(file, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkIsFile(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckIsFile() {
        ParameterCheckUtility.checkIsFile(null, name);
        ParameterCheckUtility.checkIsFile(file, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkIsDirectory(File, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckIsDirectory() {
        ParameterCheckUtility.checkIsDirectory(null, name);
        ParameterCheckUtility.checkIsDirectory(dir, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotEmpty(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty2() {
        ParameterCheckUtility.checkNotEmpty((Collection<?>) null, name);
        ParameterCheckUtility.checkNotEmpty(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNullNorEmpty(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty2() {
        ParameterCheckUtility.checkNotEmpty(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotEmpty(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmpty3() {
        ParameterCheckUtility.checkNotEmpty((Map<?, ?>) null, name);
        ParameterCheckUtility.checkNotEmpty(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNullNorEmpty(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullNorEmpty3() {
        ParameterCheckUtility.checkNotEmpty(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNullElements(Collection, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullElements() {
        ParameterCheckUtility.checkNotNullElements((Collection<?>) null, name);
        ParameterCheckUtility.checkNotNullElements(list, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotEmptyElements(Collection, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyElements() {
        ParameterCheckUtility.checkNotEmptyElements((Collection<?>) null, true, name);
        ParameterCheckUtility.checkNotEmptyElements(list, true, name);
        list.add("  ");
        ParameterCheckUtility.checkNotEmptyElements(list, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNullKeys(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullKeys() {
        ParameterCheckUtility.checkNotNullKeys((Map<?, ?>) null, name);
        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNullValues(Map, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNullValues() {
        ParameterCheckUtility.checkNotNullKeys((Map<?, ?>) null, name);
        ParameterCheckUtility.checkNotNullKeys(map, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotEmptyKeys(Map, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyKeys() {
        ParameterCheckUtility.checkNotEmptyKeys((Map<?, ?>) null, false, name);
        ParameterCheckUtility.checkNotEmptyKeys(map, true, name);
        map.put("  ", "1");
        ParameterCheckUtility.checkNotEmptyKeys(map, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotEmptyValues(Map, Boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotEmptyValues() {
        ParameterCheckUtility.checkNotEmptyValues((Map<?, ?>) null, false, name);
        ParameterCheckUtility.checkNotEmptyValues(map, true, name);
        map.put("1", "  ");
        ParameterCheckUtility.checkNotEmptyValues(map, false, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNegative(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNegative() {
        ParameterCheckUtility.checkNegative(-1.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkPositive(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckPositive() {
        ParameterCheckUtility.checkPositive(1.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNegative(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNegative() {
        ParameterCheckUtility.checkNotNegative(1.0, name);
        ParameterCheckUtility.checkNotNegative(0.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotPositive(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotPositive() {
        ParameterCheckUtility.checkNotPositive(-1.0, name);
        ParameterCheckUtility.checkNotPositive(0.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotZero(double, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotZero() {
        ParameterCheckUtility.checkNotPositive(-1.0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkGreaterThan(double, double, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckGreaterThan() {
        ParameterCheckUtility.checkGreaterThan(1.0, 0.0, false, name);
        ParameterCheckUtility.checkGreaterThan(1.0, 0.0, true, name);
        ParameterCheckUtility.checkGreaterThan(0.0, 0.0, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkLessThan(double, double, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckLesshan() {
        ParameterCheckUtility.checkLessThan(0.0, 1.0, false, name);
        ParameterCheckUtility.checkLessThan(0.0, 1.0, true, name);
        ParameterCheckUtility.checkLessThan(0.0, 0.0, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method
     * {@link ParameterCheckUtility#checkInRange(double, double, double, boolean, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInRange() {
        ParameterCheckUtility.checkInRange(1.0, 0.0, 2.0, false, false, name);
        ParameterCheckUtility.checkInRange(1.0, 1.0, 2.0, true, false, name);
        ParameterCheckUtility.checkInRange(1.0, 0.0, 1.0, false, true, name);
        ParameterCheckUtility.checkInRange(1.0, 1.0, 1.0, true, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNegative(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNegative2() {
        ParameterCheckUtility.checkNegative(-1, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkPositive(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckPositive2() {
        ParameterCheckUtility.checkPositive(1, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotNegative(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotNegative2() {
        ParameterCheckUtility.checkNotNegative(1, name);
        ParameterCheckUtility.checkNotNegative(0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotPositive(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotPositive2() {
        ParameterCheckUtility.checkNotPositive(-1, name);
        ParameterCheckUtility.checkNotPositive(0, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkNotZero(long, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckNotZero2() {
        ParameterCheckUtility.checkNotPositive(-1, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkGreaterThan(long, long, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckGreaterThan2() {
        ParameterCheckUtility.checkGreaterThan(1, 0, false, name);
        ParameterCheckUtility.checkGreaterThan(1, 0, true, name);
        ParameterCheckUtility.checkGreaterThan(0, 0, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method {@link ParameterCheckUtility#checkLessThan(long, long, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckLesshan2() {
        ParameterCheckUtility.checkLessThan(0, 1, false, name);
        ParameterCheckUtility.checkLessThan(0, 1, true, name);
        ParameterCheckUtility.checkLessThan(0, 0, true, name);
    }

    /**
     * <p>
     * Accuracy test for the method
     * {@link ParameterCheckUtility#checkInRange(long, long, long, boolean, boolean, String)}.
     * </p>
     *
     * <p>
     * The input is correct that validation should pass.
     * </p>
     */
    @Test
    public void testCheckInRange2() {
        ParameterCheckUtility.checkInRange(1, 0, 2, false, false, name);
        ParameterCheckUtility.checkInRange(1, 1, 2, true, false, name);
        ParameterCheckUtility.checkInRange(1, 0, 1, false, true, name);
        ParameterCheckUtility.checkInRange(1, 1, 1, true, true, name);
    }
}
