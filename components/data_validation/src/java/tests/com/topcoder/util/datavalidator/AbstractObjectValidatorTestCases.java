/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import junit.framework.TestCase;
import junit.framework.AssertionFailedError;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Locale;


/**
 * <p>
 * Test the functionality of class <code>AbstractObjectValidator</code>.
 * </p>
 *
 * @author telly12
 * @version 1.1
 */
public abstract class AbstractObjectValidatorTestCases extends TestCase {

    /**
     * <p>
     * An instance of <code>BundleInfo</code> for testing.
     * </p>
     */
    protected BundleInfo bundleInfo = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("property", Locale.ENGLISH);
        bundleInfo.setDefaultMessage("defaultMessage");
        bundleInfo.setMessageKey("key");
    }

    /**
     * <p>
     * Failure test case for method 'setId()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     */
    public void testSetId_String_Null1() {
        try {
            getObjectValidator().setId(null);
            fail("Test failure for setId() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Failure test case for method 'setId()'.<br>
     * The empty <code>String</code> is acceptable.
     * </p>
     */
    public void testSetId_String_Empty1() {
        try {
            getObjectValidator().setId("   ");
            // Success
        } catch (IllegalArgumentException iae) {
            fail("Test failure for method setId() failed, IllegalArgumentException should be thrown.");
        }
    }

    /**
     * <p>
     * Accuracy test case for methods 'setId()' and 'getId()'.<br>
     * The field 'id' should be set properly.
     * </p>
     */
    public void testSetId_String_Accuracy() {
        String id = new String();
        AbstractObjectValidator validator = getObjectValidator();

        validator.setId(id);

        String value = (String) validator.getId();
        assertEquals("Test accuracy for method setId() failed, the value should be set properly.", id, value);
    }

    /**
     * <p>
     * Failure test case for method 'setResourceBundleInfo()'.<br>
     * The argument is null, <code>IllegalArgumentException</code> should be thrown.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testSetResourceBundleInfo_BundleInfo_Null1()
        throws Exception {
        try {
            getObjectValidator().setResourceBundleInfo(null);
            fail("Test failure for setResourceBundleInfo() failed, IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException iae) {
            // Success
        }
    }

    /**
     * <p>
     * Accuracy test case for method 'setResourceBundleInfo()'.<br>
     * The field 'resourceBundleInfo' should be set properly.
     * </p>
     */
    public void testSetResourceBundleInfo_BundleInfo_Accuracy() {
        AbstractObjectValidator validator = getObjectValidator();

        validator.setResourceBundleInfo(bundleInfo);

        BundleInfo value = validator.getBundleInfo();
        assertEquals("Test accuracy for methodis setResourceBundleInfo() / getResourceBundleInfo() failed, "
                + "the Locale should be set properly.",
            bundleInfo.getLocale(), value.getLocale());
        assertEquals("Test accuracy for methodis setResourceBundleInfo() / getResourceBundleInfo() failed, "
                + "the bundle name should be set properly.",
            bundleInfo.getBundleName(), value.getBundleName());
        assertEquals("Test accuracy for methodis setResourceBundleInfo() / getResourceBundleInfo() failed, "
                + "the message key should be set properly.",
            bundleInfo.getMessageKey(), value.getMessageKey());
        assertEquals("Test accuracy for methodis setResourceBundleInfo() / getResourceBundleInfo() failed, "
                + "the default message should be set properly.",
            bundleInfo.getDefaultMessage(), value.getDefaultMessage());
    }

    /**
     * Tests that the validator can successfully be serialized and deserialized (but does not test the fidelity
     * of the serialization / deserialization cycle).
     */
    public void testAbstractObjectValidator_isSerializable() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        ObjectInputStream in;
        AbstractObjectValidator validator;

        /*
         * Test with the base validator
         */
        out.writeObject(createObjectValidator());
        out.flush();
        out.close();

        in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        validator = (AbstractObjectValidator) in.readObject();
        in.close();

        /*
         * Test with the prepared BundleInfo
         */
        try {
            bytes.reset();
            out = new ObjectOutputStream(bytes);
            out.writeObject(createObjectValidator(bundleInfo));
            out.flush();
            out.close();

            in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
            validator = (AbstractObjectValidator) in.readObject();
            in.close();
        } catch (AssertionFailedError afe) {
            // happens for some validators that don't have a FooValidator(BundleInfo) constructor
            // this part of the test is irrelevant for such classes
        }
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        String testMessage = "message";
        AbstractObjectValidator validator = createObjectValidator(testMessage);

        assertNotNull("No validator created", validator);
        assertEquals("Wrong default message", testMessage, validator.getValidationMessage());
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator()'
     */
    public void testAbstractObjectValidator() {
        assertNotNull("No validator created", createObjectValidator());
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(BundleInfo)'
     */
    public void testAbstractObjectValidatorBundleInfo() {
        assertNotNull("No validator created", createObjectValidator(bundleInfo));
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This abstract method should be implemented by the
     * sub-class to retrieve the specified <code>AbstractObjectValidator</code> instance.
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public abstract AbstractObjectValidator createObjectValidator();

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This abstract method should be implemented by the
     * sub-class to retrieve the specified <code>AbstractObjectValidator</code> instance.
     * </p>
     *
     * @param validationMessage This is the validation message to use for the underlying validator.
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public abstract AbstractObjectValidator createObjectValidator(String validationMessage);

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This abstract method should be implemented by the
     * sub-class to retrieve the specified <code>AbstractObjectValidator</code> instance.
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public abstract AbstractObjectValidator createObjectValidator(BundleInfo bundleInfo);

    /**
     * <p>
     * Get the <code>AbstractObjectValidator</code> instance.<br>
     * This abstract method should be implemented by the sub-class to retrieve the specified
     * <code>AbstractObjectValidator</code> instance.
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public abstract AbstractObjectValidator getObjectValidator();
}
