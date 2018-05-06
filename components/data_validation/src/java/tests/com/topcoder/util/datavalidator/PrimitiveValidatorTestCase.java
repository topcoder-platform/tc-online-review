/*
 * TCS Data Validation
 *
 * PrimitiveValidatorTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import com.topcoder.util.datavalidator.*;
import junit.framework.TestCase;

/**
 * This tests PrimitiveValidator.
 *
 * @author WishingBone
 * @author zimmy
 * @version 1.0
 */
public class PrimitiveValidatorTestCase extends AbstractObjectValidatorTestCases {

    /**
     * Tests PrimitveValidator.
     */
    public void testPrimitiveValidator() {
        PrimitiveValidator validator = new PrimitiveValidator(
                IntegerValidator.isOdd());
        assertTrue(validator.valid(7));
        assertFalse(validator.valid(8));
        assertTrue(validator.valid((long) 7));
        assertFalse(validator.valid((long) 8));
        assertTrue(validator.valid((byte) 7));
        assertFalse(validator.valid((byte) 8));
        assertTrue(validator.valid((short) 7));
        assertFalse(validator.valid((short) 8));
        assertTrue(validator.valid((float) 7));
        assertFalse(validator.valid((float) 8));
        assertTrue(validator.valid((double) 7));
        assertFalse(validator.valid((double) 8));
        assertTrue(validator.valid("7"));
        assertFalse(validator.valid("8"));

        assertNull(validator.getMessage(7));
        assertEquals(validator.getMessage(8), "not odd");
        assertNull(validator.getMessage((long) 7));
        assertEquals(validator.getMessage((long) 8), "not odd");
        assertNull(validator.getMessage((byte) 7));
        assertEquals(validator.getMessage((byte) 8), "not odd");
        assertNull(validator.getMessage((short) 7));
        assertEquals(validator.getMessage((short) 8), "not odd");
        assertNull(validator.getMessage((float) 7));
        assertEquals(validator.getMessage((float) 8), "not odd");
        assertNull(validator.getMessage((double) 7));
        assertEquals(validator.getMessage((double) 8), "not odd");
        assertNull(validator.getMessage("7"));
        assertEquals(validator.getMessage("8"), "not odd");

        validator = new PrimitiveValidator(CharacterValidator.isDigit());
        assertTrue(validator.valid('1'));
        assertFalse(validator.valid('a'));

        assertNull(validator.getMessage('1'));
        assertEquals(validator.getMessage('a'), "not a digit");

        validator = new PrimitiveValidator(new BooleanValidator() {
            public boolean valid(boolean value) { return value; }
            public String getMessage(boolean value) { return value ? null
                    : "boolean error"; } }
        );

        assertTrue(validator.valid(true));
        assertFalse(validator.valid(false));

        assertNull(validator.getMessage(true));
        assertEquals(validator.getMessage(false), "boolean error");
    }

    /**
     * Test method for 'AbstractObjectValidator.AbstractObjectValidator(String)'
     */
    public void testAbstractObjectValidatorString() {
        // corresponding constructor not supported on PrimitiveValidator
    }

    /**
     * Returns the BooleanValidator under test
     *
     * @return the BooleanValidator
     */
    public AbstractObjectValidator getObjectValidator() {
	return new PrimitiveValidator(IntegerValidator.isOdd());
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>  This version always causes a
     * test failure, as PrimitiveValidator does not provide a corresponding constructor.
     * </p>
     *
     * @param validationMessage This is the validation message to use for the underlying validator.
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(String validationMessage) {
	fail("constructor not supported");

	return null;  // never reached
    }

    /**
     * <p>
     * Creates an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator() {
        return new PrimitiveValidator(IntegerValidator.isOdd());
    }

    /**
     * <p>
     * Create an instance of <code>AbstractObjectValidator.</code>
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @return the <code>AbstractObjectValidator</code> instance.
     */
    public AbstractObjectValidator createObjectValidator(BundleInfo bundleInfo) {
        return new PrimitiveValidator(IntegerValidator.isOdd(), bundleInfo);
    }

}
