/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator.accuracytests;

import com.topcoder.util.datavalidator.BundleInfo;
import com.topcoder.util.datavalidator.CharacterValidator;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.PrimitiveValidator;

import junit.framework.TestCase;

import java.util.Locale;


/**
 * <p>
 * test the PrimitiveValidator class.
 * <p>
 * @author KLW
 * @version 1.1
 *
 */
public class PrimitiveValidatorAccuracyTests extends TestCase {
    /**
     * <p>
     * PrimitiveValidator instance to test.
     * </p>
     */
    private PrimitiveValidator primitiveValidator;

    /**
     * <p>
     * IntegerValidator instance used to test.
     * </p>
     */
    private IntegerValidator integerValidator;

    /**
     * <p>
     * BundleInfo case used to test.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * setUp().
     * </p>
     */
    protected void setUp() {
        bundleInfo = new BundleInfo();
        bundleInfo.setBundle("accuracy.test", new Locale("en"));
        bundleInfo.setDefaultMessage("accuracy test");
        bundleInfo.setMessageKey("IsOdd_Bundle_Key");
        integerValidator = IntegerValidator.isOdd();
        primitiveValidator = new PrimitiveValidator(integerValidator, this.bundleInfo);
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     */
    public void tearDown() {
        bundleInfo = null;
        integerValidator = null;
        primitiveValidator = null;
    }

    /**
     * <p>
     * test constructor method1.
     * </p>
     */
    public void testCtor1() {
        primitiveValidator = new PrimitiveValidator(integerValidator, bundleInfo);
        assertNotNull(primitiveValidator);
    }

    /**
     * <p>
     * test constructor method2.
     * </p>
     */
    public void testCtor2() {
        assertNotNull(primitiveValidator);
    }

    /**
     * <p>
     * test method valid(char).
     * </p>
     */
    public void testValid7() {
        primitiveValidator = new PrimitiveValidator(CharacterValidator.isDigit());
        assertTrue(primitiveValidator.valid(('1')));
        assertFalse(primitiveValidator.valid('a'));
        assertNull(primitiveValidator.getMessage('1'));
        assertNotNull(primitiveValidator.getMessage('a'));
    }

    /**
     * <p>
     * test method getMessage(object).
     * </p>
     */
    public void testGetMessage1() {
        assertNull(primitiveValidator.getMessage(new Integer(7)));
        assertNotNull(primitiveValidator.getMessage(new Integer(8)));
    }

    /**
     * <p>
     * test method getMessage(byte).
     * </p>
     */
    public void testGetMessage3() {
        assertNull(primitiveValidator.getMessage((byte) 7));
        assertNotNull(primitiveValidator.getMessage((byte) 8));
    }

    /**
     * <p>
     * test method getMessage(short).
     * </p>
     */
    public void testGetMessage4() {
        assertNull(primitiveValidator.getMessage((short) 7));
        assertNotNull(primitiveValidator.getMessage((short) 8));
    }

    /**
     * <p>
     * test method getMessage(int).
     * </p>
     */
    public void testGetMessage5() {
        assertNull(primitiveValidator.getMessage(7));
        assertNotNull(primitiveValidator.getMessage(8));
    }


    /**
     * <p>
     * test method getMessage(long).
     * </p>
     */
    public void testGetMessage6() {
        assertNull(primitiveValidator.getMessage((long) 7));
        assertNotNull(primitiveValidator.getMessage((long) 8));
    }

    /**
     * <p>
     * test method getMessage(float).
     * </p>
     */
    public void testGetMessage7() {
        assertNull(primitiveValidator.getMessage((float) 7));
        assertNotNull(primitiveValidator.getMessage((float) 8));
    }

    /**
     * <p>
     * test method getMessage(double).
     * </p>
     */
    public void testGetMessage8() {
        assertNull(primitiveValidator.getMessage((double) 7));
        assertNotNull(primitiveValidator.getMessage((double) 8));
    }

    /**
     * <p>
     * test method getMessage(char).
     * </p>
     */
    public void testGetMessage9() {
        primitiveValidator = new PrimitiveValidator(CharacterValidator.isDigit());
        assertNull(primitiveValidator.getMessage('1'));
        assertNotNull(primitiveValidator.getMessage('a'));
    }

    /**
     * <p>
     * test method getMessages().
     * </p>
     */
    public void testGetMessages() {
        assertNull(primitiveValidator.getMessages(new Integer(7)));
        assertNotNull(primitiveValidator.getMessage(new Integer(8)));
    }

    /**
     * <p>
     * test method getAllMessages().
     * </p>
     */
    public void testGetAllMessages() {
        assertNull(primitiveValidator.getMessages(new Integer(7)));
        assertNotNull(primitiveValidator.getMessage(new Integer(8)));
    }
}
