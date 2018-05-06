/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a simple <code>boolean</code> validator abstraction which basically checks that a given <code>boolean</code>
 * value conforms with the state of this validator.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine. One such example
 * would be provide a <code>boolean</code> value validation which fails validation of the provided object evaluates to
 * false. User will need to implement the <code>valid(boolean value)</code> method to decide what the validator will
 * do.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b> This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.0
 */
public abstract class BooleanValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>BooleanValidator</code>.
     * </p>
     */
    public BooleanValidator() {
    }

    /**
     * <p>
     * Creates a new <code>BooleanValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public BooleanValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Checks whether the given <code>Object</code> is in fact a <code>Boolean</code>, or a <code>String</code>
     * representation of a <code>boolean</code>. If so, it passes the <code>boolean</code> value to
     * <code>valid(boolean)</code>. Otherwise, it returns <code>false</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        // checks if obj is a Boolean
        if (obj instanceof Boolean) {
            return valid(((Boolean) obj).booleanValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            String str = (String) obj;

            // check if str in fact represents a boolean
            if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) {
                return valid(Boolean.valueOf((String) obj).booleanValue());
            }
        }

        // if no valid(boolean) was successfully called, return false
        return false;
    }

    /**
     * <p>
     * This validates the given primitive <code>boolean</code> value. It is an <code>abstract</code> method, so it
     * should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>boolean</code> value to validate.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(boolean value);

    /**
     * <p>
     * Gives error information about the given <code>Object</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise, an error message is returned.
     */
    public String getMessage(Object obj) {
        // checks if obj is a Boolean
        if (obj instanceof Boolean) {
            return getMessage(((Boolean) obj).booleanValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            String str = (String) obj;

            // check if str in fact represents a boolean
            if (str.equalsIgnoreCase("true") || str.equalsIgnoreCase("false")) {
                return getMessage(Boolean.valueOf((String) obj).booleanValue());
            }
        }

        // if no getMessage(boolean) was successfully called, return error
        return "invalid Boolean";
    }

    /**
     * <p>
     * Gives error information about the given primitive <code>boolean</code> value. It is an <code>abstract</code>
     * method, so it should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>boolean</code> value to validate.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(boolean value);
}
