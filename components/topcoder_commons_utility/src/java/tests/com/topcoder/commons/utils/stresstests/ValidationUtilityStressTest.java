/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.stresstests;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.topcoder.commons.utils.ValidationUtility;

/**
 * <p>
 * Stress test case of {@link ValidationUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class ValidationUtilityStressTest extends BaseStressTest {
    private String name = "test";

    /**
     * <p>
     * Sets up test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     * 
     * @throws Exception
     *             to jUnit.
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     * 
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(
                ValidationUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method ValidationUtility.
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getStringProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            ValidationUtility.checkGreaterThan(4, 3, true, name, IllegalArgumentException.class);
            ValidationUtility.checkGreaterThan(4.0, 3.0, true, name, IllegalArgumentException.class);
            ValidationUtility.checkLessThan(4, 30, true, name, IllegalArgumentException.class);
            ValidationUtility.checkLessThan(4.0, 30.0, true, name, IllegalArgumentException.class);
            ValidationUtility.checkInRange(4.0, 3.0, 5.0, true, true, name, IllegalArgumentException.class);
            ValidationUtility.checkInRange(4, 3, 5, true, true, name, IllegalArgumentException.class);
            ValidationUtility.checkExists(new File("test_files/stress/config.properties"), name, IllegalArgumentException.class);
            ValidationUtility.checkInstance("name", String.class, name, IllegalArgumentException.class);
            ValidationUtility.checkIsDirectory(new File("test_files/stress/"), name, IllegalArgumentException.class);
            ValidationUtility.checkIsFile(new File("test_files/stress/config.properties"), name, IllegalArgumentException.class);
            ValidationUtility.checkNegative(-1, name, IllegalArgumentException.class);
            ValidationUtility.checkNegative(-1.0, name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmpty(Arrays.asList("a"), name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmpty(" ", name, IllegalArgumentException.class);
            ValidationUtility.checkNotPositive(0, name, IllegalArgumentException.class);
            ValidationUtility.checkPositive(1, name, IllegalArgumentException.class);
            ValidationUtility.checkPositive(1.0, name, IllegalArgumentException.class);
            ValidationUtility.checkNullOrInstance(null, String.class, name, IllegalArgumentException.class);
            ValidationUtility.checkNullOrInstance("name", String.class, name, IllegalArgumentException.class);
            ValidationUtility.checkNotZero(1, name, IllegalArgumentException.class);
            ValidationUtility.checkNotZero(1.0, name, IllegalArgumentException.class);
            ValidationUtility.checkNotNullNorEmptyAfterTrimming("name", name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmptyAfterTrimming(null, name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmptyAfterTrimming("name", name, IllegalArgumentException.class);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("key", "value");
            ValidationUtility.checkNotNullValues(map, name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmpty(map, name, IllegalArgumentException.class);
            boolean trimStrings = true;
            ValidationUtility.checkNotEmptyKeys(map, trimStrings , name, IllegalArgumentException.class);
            ValidationUtility.checkNotEmptyValues(map, trimStrings , name, IllegalArgumentException.class);
            ValidationUtility.checkNotNullKeys(map, name, IllegalArgumentException.class);
            ValidationUtility.checkNotNullValues(map, name, IllegalArgumentException.class);
            ValidationUtility.checkNotNullNorEmpty(map, name, IllegalArgumentException.class);
            ValidationUtility.checkNotNullNorEmpty(map, name, IllegalArgumentException.class);
            Class<IllegalArgumentException> exceptionClass = IllegalArgumentException.class;
            ValidationUtility.checkNotNullNorEmptyAfterTrimming("value", name, exceptionClass);
            ValidationUtility.checkNotNullNorEmpty("name", name, exceptionClass);
            List<String> collection = Arrays.asList("a");
            ValidationUtility.checkNotNullNorEmpty(collection , name, exceptionClass);
            ValidationUtility.checkNotEmptyElements(collection, true, name, exceptionClass);
            ValidationUtility.checkNotNullElements(collection, name, exceptionClass);
        }
        
        System.out.println("Run ValidationUtility for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

    
    
    
    
    
    
    
    
    
    
    
    
}
