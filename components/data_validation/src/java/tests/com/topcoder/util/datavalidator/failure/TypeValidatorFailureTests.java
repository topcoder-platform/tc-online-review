/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.failure;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.ObjectValidator;
import com.topcoder.util.datavalidator.TypeValidator;


import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>TypeValidator</code>.
 * </p>
 * 
 * <p>
 * This test suite contains multiple failure test cases that addressed different aspects for each public methods and constructors.<br>
 * Various real data is used to ensure that the invalid inputs are handled properly as defined in the
 * documentation.<br>
 * </p>
 *
 * @author lyt
 * @version 1.0
 */
public class TypeValidatorFailureTests extends AbstractObjectValidatorFailureTests {
    /**
     * <p>
     * Represents an instance of <code>ObjectValidator</code> for testing.<br>
     * It is initialized in <code>setUp()</code> method and released in <code>tearDown()</code> method.
     * </p>
     */
    private ObjectValidator objectValidator = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * 
     * <p>
     * The test instance is initialized and all the need configuration are loaded.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        objectValidator = IntegerValidator.isEven();
        validator = new TypeValidator(objectValidator, String.class);
    }

    /**
     * <p>
     * Tear down the test environment.
     * </p>
     * 
     * <p>
     * The test instance is released and the configuration is clear.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        objectValidator = null;
        bundleInfo = null;
        validator = null;
        super.tearDown();
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_Class_Null1() {
        try {
            new TypeValidator(null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_Class_failure() {
        try {
            new TypeValidator(long.class);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ClassBundleInfo_Null1() {
        try {
            new TypeValidator(null, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ClassBundleInfo_Null2() {
        try {
            new TypeValidator(String.class, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClass_Null1() {
        try {
            new TypeValidator(null, String.class);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClass_Null2() {
        try {
            new TypeValidator(objectValidator, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null1() {
        try {
            new TypeValidator(null, Integer.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null2() {
        try {
            new TypeValidator(objectValidator, null, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_Null3() {
        try {
            new TypeValidator(objectValidator, Integer.class, null);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_failure1() {
        bundleInfo = new BundleInfo();

        // BUNDLE_NAME was null
        // bundleInfo.setBundle(BUNDLE_NAME, Locale.ENGLISH);
        bundleInfo.setDefaultMessage("validation fails");
        bundleInfo.setMessageKey("validation");

        try {
            new TypeValidator(objectValidator, Integer.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

 
    /**
     * <p>
     * Failure test case for method 'TypeValidator()'.<br>
     * The argument is invalid, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testTypeValidator_ObjectValidatorClassBundleInfo_failure2() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle(BUNDLE_NAME, Locale.ENGLISH);
        bundleInfo.setDefaultMessage("validation fails");

        // MessageKey was null
        // bundleInfo.setMessageKey("validation");
        try {
            new TypeValidator(objectValidator, Integer.class, bundleInfo);
            fail("Test failure for TypeValidator() failed, IllegalArgumentException expected.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

   
}
