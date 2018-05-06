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

import com.topcoder.commons.utils.ParameterCheckUtility;

/**
 * <p>
 * Stress test case of {@link ParameterCheckUtility}.
 * </p>
 * 
 * @author mumujava
 * @version 1.0
 */
public class ParameterCheckUtilityStressTest extends BaseStressTest {
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
                ParameterCheckUtilityStressTest.class);
        return suite;
    }

    /**
     * <p>
     * Stress test for method ParameterCheckUtility.
     * 
     * </p>
     * @throws Throwable to junit
     */
    public void test_getStringProperty() throws Throwable {
        for (int i = 0; i < testCount; i++) {
            ParameterCheckUtility.checkGreaterThan(4, 3, true, name);
            ParameterCheckUtility.checkGreaterThan(4.0, 3.0, true, name);
            ParameterCheckUtility.checkLessThan(4, 30, true, name);
            ParameterCheckUtility.checkLessThan(4.0, 30.0, true, name);
            ParameterCheckUtility.checkInRange(4.0, 3.0, 5.0, true, true, name);
            ParameterCheckUtility.checkInRange(4, 3, 5, true, true, name);
            ParameterCheckUtility.checkExists(new File("test_files/stress/config.properties"), name);
            ParameterCheckUtility.checkInstance("name", String.class, name);
            ParameterCheckUtility.checkIsDirectory(new File("test_files/stress/"), name);
            ParameterCheckUtility.checkIsFile(new File("test_files/stress/config.properties"), name);
            ParameterCheckUtility.checkNegative(-1, name );
            ParameterCheckUtility.checkNegative(-1.0, name);
            ParameterCheckUtility.checkNotEmpty(Arrays.asList("a"), name);
            ParameterCheckUtility.checkNotEmpty(" ", name);
            ParameterCheckUtility.checkNotPositive(0, name);
            ParameterCheckUtility.checkPositive(1, name);
            ParameterCheckUtility.checkPositive(1.0, name);
            ParameterCheckUtility.checkNullOrInstance(null, String.class, name);
            ParameterCheckUtility.checkNullOrInstance("name", String.class, name);
            ParameterCheckUtility.checkNotZero(1, name);
            ParameterCheckUtility.checkNotZero(1.0, name);
            ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming("name", name);
            ParameterCheckUtility.checkNotEmptyAfterTrimming(null, name);
            ParameterCheckUtility.checkNotEmptyAfterTrimming("name", name);
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("key", "value");
            ParameterCheckUtility.checkNotNullValues(map, name);
            ParameterCheckUtility.checkNotEmpty(map, name);
            boolean trimStrings = true;
            ParameterCheckUtility.checkNotEmptyKeys(map, trimStrings , name);
            ParameterCheckUtility.checkNotEmptyValues(map, trimStrings , name);
            ParameterCheckUtility.checkNotNullKeys(map, name);
            ParameterCheckUtility.checkNotNullValues(map, name);
            ParameterCheckUtility.checkNotNullNorEmpty(map, name);
            ParameterCheckUtility.checkNotNullNorEmpty(map, name);
            ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming("value", name);
            ParameterCheckUtility.checkNotNullNorEmpty("name", name);
            List<String> collection = Arrays.asList("a");
            ParameterCheckUtility.checkNotNullNorEmpty(collection , name);
            ParameterCheckUtility.checkNotEmptyElements(collection, true, name);
            ParameterCheckUtility.checkNotNullElements(collection, name);
        }
        
        System.out.println("Run ParameterCheckUtility for " + testCount + " times takes "
                + (new Date().getTime() - start) + "ms");
    }

}
