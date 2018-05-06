/*
 * TCS Data Validation
 *
 * ByteValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

    /**
     * This is a simple byte validator abstraction which basically checks that
     * a given value conforms with a specific byte definition.
     * This validator is abstract and will need to be extended to actually provide the
     * validation routine for a specific byte value validation. Note that this
     * validator acts as a factory that allows for creation of other specialized
     * ByteValidator instances that provide a number of convenience methods
     * which validate such aspects as range (from , to and could also be
     * exclusive of extremes) or provide general comparison of values such as
     * greater-than, less-than, etc...
     * User will need to implement the <code>valid(byte value) </code> method to
     * decide what the validator will do with the input byte.
     * This is thread-safe as the implementation is immutable.
     * @version 1.1
     */
     public abstract class ByteValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>ByteValidator</code>.
     */
    public ByteValidator() {
      super();
    }

    /**
     * Creates a new <code>ByteValidator</code> with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public ByteValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Number</code>, or a <code>String</code> representation of a
     * <code>byte</code>. If so, it passes the <code>byte</code> value
     * to <code>valid(byte)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).byteValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a byte
                return valid(Byte.parseByte((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(byte) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>byte</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>byte</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(byte value);

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator greaterThan(byte value) {
        return new CompareByteValidator(value, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator greaterThanOrEqualTo(byte value) {
        return new CompareByteValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator lessThan(byte value) {
        return new CompareByteValidator(value, CompareDirection.LESS);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator lessThanOrEqualTo(byte value) {
        return new CompareByteValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are equal to <code>value</code>.
     *
     * @param   value   specific value for equality.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator equal(byte value) {
            return new CompareByteValidator(value, CompareDirection.EQUAL);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static ByteValidator inRange(byte lower, byte upper)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new ByteValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static ByteValidator inExclusiveRange(byte lower, byte upper)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new ByteValidatorWrapper(new AndValidator(
                greaterThan(lower), lessThan(upper)));
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are greater than <code>value</code>
     * and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator greaterThan(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>
     * and initializes the validator with a specific resource
     * bundle information.
     *
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator greaterThanOrEqualTo(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value,
                CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are less than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator lessThan(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     * and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator lessThanOrEqualTo(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are equal to <code>value</code>.
     * and initializes the validator with a specific resource
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   specific value for equality.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     */
    public static ByteValidator equal(byte value, BundleInfo bundleInfo) {
            return new CompareByteValidator(value, CompareDirection.EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive
     * and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static ByteValidator inRange(byte lower, byte upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new ByteValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower, bundleInfo), lessThanOrEqualTo(upper, bundleInfo)));
    }

    /**
     * Creates a <code>ByteValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive
     * and initializes the validator with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>ByteValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static ByteValidator inExclusiveRange(byte lower, byte upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new ByteValidatorWrapper(new AndValidator(
                greaterThan(lower,bundleInfo), lessThan(upper,bundleInfo)));
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
            return getMessage(((Number) obj).byteValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a byte
                return getMessage(Byte.parseByte((String) obj));
            } catch (Exception exception) { }
        }

        // if no getMessage(byte) was successfully called, return error message
        return "invalid Byte";
    }

    /**
     * Gives error information about the given primitive <code>byte</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>byte</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(byte value);


    /*
     * The following are inner classes
     */

}
