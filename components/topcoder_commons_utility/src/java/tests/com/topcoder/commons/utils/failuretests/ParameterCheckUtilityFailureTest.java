/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils.failuretests;

import java.io.File;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import com.topcoder.commons.utils.ParameterCheckUtility;

/**
 * <p>
 * Failure tests for ParameterCheckUtility.
 * </p>
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class ParameterCheckUtilityFailureTest extends TestCase {

    /**
     * <p>
     * Setup the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit
     */
    protected void setUp() throws Exception {
    }

    /**
     * <p>
     * Clean up the environment.
     * </p>
     * 
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
    }
    /**
     * Tests for checkNotNull() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNull() throws Exception {
        try {
            ParameterCheckUtility.checkNotNull(null, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmpty() throws Exception {
        try {
            ParameterCheckUtility.checkNotEmpty("", "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotEmptyAfterTrimming() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmptyAfterTrimming() throws Exception {
        try {
            ParameterCheckUtility.checkNotEmptyAfterTrimming(" \n\t\r", "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNullNorEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullNorEmpty() throws Exception {
        try {
            ParameterCheckUtility.checkNotNullNorEmpty((String)null, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotNullNorEmpty("", "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNullNorEmptyAfterTrimming() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullNorEmptyAfterTrimming() throws Exception {
        try {
            ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming((String)null, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming("", "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkInstance() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkInstance() throws Exception {
        try {
            ParameterCheckUtility.checkInstance(null, Date.class, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkInstance("A string", Date.class, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNullOrInstance() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNullOrInstance() throws Exception {
        try {
            ParameterCheckUtility.checkNullOrInstance("A string", Date.class, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkExists() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkExists() throws Exception {
        try {
            ParameterCheckUtility.checkExists(new File("should not exist"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkIsFile() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkIsFile() throws Exception {
        try {
            ParameterCheckUtility.checkIsFile(new File("should not exist"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkIsFile(new File("test_files"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkIsDirectory() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkIsDirectory() throws Exception {
        try {
            ParameterCheckUtility.checkIsDirectory(new File("should not exist"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkIsDirectory(new File("build.xml"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmpty2() throws Exception {
        try {
            ParameterCheckUtility.checkNotEmpty(new ArrayList<String>(), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNullNorEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullNorEmpty2() throws Exception {
        try {
            ParameterCheckUtility.checkNotNullNorEmpty((ArrayList<String>)null, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotNullNorEmpty(new ArrayList<String>(), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Tests for checkNotEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmpty3() throws Exception {
        try {
            ParameterCheckUtility.checkNotEmpty(new HashMap<String,String>(), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNullNorEmpty() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullNorEmpty3() throws Exception {
        try {
            ParameterCheckUtility.checkNotNullNorEmpty((HashMap<String,String>)null, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotNullNorEmpty(new HashMap<String,String>(), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Tests for checkNotNullElements() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullElements() throws Exception {
        try {
            ParameterCheckUtility.checkNotNullElements(Arrays.asList(null,"string"), "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Tests for checkNotEmptyElements() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    @SuppressWarnings("unchecked")
    public void test_checkNotEmptyElements() throws Exception {
        try {
            ParameterCheckUtility.checkNotEmptyElements(Arrays.asList(" "), true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotEmptyElements(Arrays.asList(""), false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotEmptyElements(Arrays.asList(new ArrayList<String>()), true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNotEmptyElements(Arrays.asList(new HashMap<String, String>()), false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    
    /**
     * Tests for checkNotNullKeys() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullKeys() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put(null, "string");
        try {
            ParameterCheckUtility.checkNotNullKeys(map, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNullValues() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNullValues() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("string", null);
        try {
            ParameterCheckUtility.checkNotNullValues(map, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotEmptyKeys() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmptyKeys() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("   ", "N");
        try {
            ParameterCheckUtility.checkNotEmptyKeys(map, true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotEmptyValues() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotEmptyValues() throws Exception {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        map.put("key", new ArrayList<String>());
        try {
            ParameterCheckUtility.checkNotEmptyValues(map, true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNegative() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNegative() throws Exception {
        try {
            ParameterCheckUtility.checkNegative(0, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkNegative(1, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkPositive() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkPositive() throws Exception {
        try {
            ParameterCheckUtility.checkPositive(0, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkPositive(-1, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotNegative() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotNegative() throws Exception {
        try {
            ParameterCheckUtility.checkNotNegative(-1, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotPositive() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotPositive() throws Exception {
        try {
            ParameterCheckUtility.checkNotPositive(1, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkNotZero() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkNotZero() throws Exception {
        try {
            ParameterCheckUtility.checkNotZero(0, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkGreaterThan() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkGreaterThan() throws Exception {
        try {
            ParameterCheckUtility.checkGreaterThan(2,3,true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkGreaterThan(3,3,false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkLessThan() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkLessThan() throws Exception {
        try {
            ParameterCheckUtility.checkLessThan(3,2,true, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkLessThan(3,3,false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
    /**
     * Tests for checkInRange() with illegal input.
     * 
     * 
     * @throws Exception
     *             to junit
     */
    public void test_checkInRange() throws Exception {
        try {
            ParameterCheckUtility.checkInRange(2,2,4,false, false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkInRange(4,2,4,false, false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkInRange(1,2,4,false, false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
        try {
            ParameterCheckUtility.checkInRange(5,2,4,false, false, "name");
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}
