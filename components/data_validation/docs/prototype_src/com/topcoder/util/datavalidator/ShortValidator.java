/*
 * TCS Data Validation
 *
 * ShortValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple short integer validator abstraction which basically checks that
 * a given value conforms with a specific short integer definition.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific short integer value validation. Note that this
 * validator acts as a factory that allows for creation of other specialized
 * ShortValidator instances that provide a number of convenience methods which
 * validate such aspects as range (from , to and could also be exclusive of
 * extremes) or general comparison of values such as greater-than, less-than,
 * etc...
 * Users will need to implement the <code>valid(short value) </code> method to
 * decide what the validator will do with the input short integer.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class ShortValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>ShortValidator</code>.
     */
    public ShortValidator() {
    }

    /**
     * Creates a new <code>ShortValidator</code> with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public ShortValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Number</code>, or a <code>String</code> representation of a
     * <code>short</code>. If so, it passes the <code>short</code> value
     * to <code>valid(short)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).shortValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a short
                return valid(Short.parseShort((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(short) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>short</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>short</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(short value);

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     */
    public static ShortValidator greaterThan(short value) {
        return new CompareShortValidator(value, CompareDirection.GREATER);
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     */
    public static ShortValidator greaterThanOrEqualTo(short value) {
        return new CompareShortValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     */
    public static ShortValidator lessThan(short value) {
        return new CompareShortValidator(value, CompareDirection.LESS);
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     */
    public static ShortValidator lessThanOrEqualTo(short value) {
        return new CompareShortValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs equal to <code>value</code>.
     *
     * @param   value   the value to test for equality
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     */
    public static ShortValidator equalTo(short value) {
        return new CompareShortValidator(value, CompareDirection.EQUAL);
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static ShortValidator inRange(short lower, short upper)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new ShortValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * Creates an <code>ShortValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  an <code>ShortValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static ShortValidator inExclusiveRange(short lower, short upper)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new ShortValidatorWrapper(new AndValidator(
                greaterThan(lower), lessThan(upper)));
    }

    /**
     * Gives error information about the given <code>Object</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public String getMessage(Object obj) {
        // checks if obj is a Number
        if (obj instanceof Number) {
            return getMessage(((Number) obj).shortValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into an short
                return getMessage(Short.parseShort((String) obj));
            } catch (Exception exception) { }
        }

        // if no getMessage(short) was successfully called, return error message
        return "invalid Short";
    }

    /**
     * Gives error information about the given primitive <code>short</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>short</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(short value);
}
