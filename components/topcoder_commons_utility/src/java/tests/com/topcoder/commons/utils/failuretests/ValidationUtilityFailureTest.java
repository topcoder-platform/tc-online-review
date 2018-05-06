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

import com.topcoder.commons.utils.ValidationUtility;

/**
 * <p>
 * Failure tests for ValidationUtility.
 * </p>
 * 
 * @author Beijing2008
 * @version 1.0
 */
public class ValidationUtilityFailureTest extends TestCase {

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
            ValidationUtility.checkNotNull(null, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmpty("", "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmptyAfterTrimming(" \n\t\r", "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullNorEmpty((String)null, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotNullNorEmpty("", "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullNorEmptyAfterTrimming((String)null, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotNullNorEmptyAfterTrimming("", "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkInstance(null, Date.class, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkInstance("A string", Date.class, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNullOrInstance("A string", Date.class, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkExists(new File("should not exist"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkIsFile(new File("should not exist"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkIsFile(new File("test_files"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkIsDirectory(new File("should not exist"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkIsDirectory(new File("build.xml"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmpty(new ArrayList<String>(), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullNorEmpty((ArrayList<String>)null, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotNullNorEmpty(new ArrayList<String>(), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmpty(new HashMap<String,String>(), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullNorEmpty((HashMap<String,String>)null, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotNullNorEmpty(new HashMap<String,String>(), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullElements(Arrays.asList(null,"string"), "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmptyElements(Arrays.asList(" "), true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotEmptyElements(Arrays.asList(""), false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotEmptyElements(Arrays.asList(new ArrayList<String>()), true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNotEmptyElements(Arrays.asList(new HashMap<String, String>()), false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullKeys(map, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNullValues(map, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmptyKeys(map, true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotEmptyValues(map, true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNegative(0, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkNegative(1, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkPositive(0, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkPositive(-1, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotNegative(-1, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotPositive(1, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkNotZero(0, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkGreaterThan(2,3,true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkGreaterThan(3,3,false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkLessThan(3,2,true, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkLessThan(3,3,false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
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
            ValidationUtility.checkInRange(2,2,4,false, false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkInRange(4,2,4,false, false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkInRange(1,2,4,false, false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
        try {
            ValidationUtility.checkInRange(5,2,4,false, false, "name", FailureReviewerException.class);
            fail("Expects FailureReviewerException");
        } catch (FailureReviewerException e) {
            // pass
        }
    }
}
