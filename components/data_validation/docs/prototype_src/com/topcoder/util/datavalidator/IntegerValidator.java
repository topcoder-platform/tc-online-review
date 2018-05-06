/*
 * TCS Data Validation
 *
 * IntegerValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple integer validator abstraction which basically checks that
 * a given value conforms with a specific integer definition.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific integer value validation. Note that this
 * validator acts as a factory that allows for creation of other specialized
 * IntegerValidator instances that provide a number of convenience methods
 * which validate such aspects as range (from , to, and could also be exclusive
 * of extremes) or general comparison of values such as greater-than,
 * less-than, etc... We can also create validators which validate if a number
 * is positive or negative, or even if it is odd or even (i.e. parity comparison)
 * User will need to implement the <code>valid(int value) </code> method to
 * decide what the validator will do with the input integer.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class IntegerValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>IntegerValidator</code>.
     */
    public IntegerValidator() {
    }

    /**
     * Creates a new <code>IntegerValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public IntegerValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Checks whether the given <code>Object</code> is in fact an
     * <code>Integer</code>, or a <code>String</code> representation of an
     * <code>int</code>. If so, it passes the <code>int</code> value
     * to <code>valid(int)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).intValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into an int
                return valid(Integer.parseInt((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(int) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>int</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>int</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(int value);

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator greaterThan(int value) {
        IntegerValidator validator = new CompareIntegerValidator(value, CompareDirection.GREATER);
        return validator;

    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator greaterThanOrEqualTo(int value) {
        return new CompareIntegerValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are equal to <code>value</code>.
     *
     * @param   value   value to ensure equality for.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator equalTo(int value) {
            return new CompareIntegerValidator(value,
                    CompareDirection.EQUAL);
    }
    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator lessThan(int value) {
        return new CompareIntegerValidator(value, CompareDirection.LESS);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator lessThanOrEqualTo(int value) {
        return new CompareIntegerValidator(value,
                CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static IntegerValidator inRange(int lower, int upper)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static IntegerValidator inExclusiveRange(int lower, int upper)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(
                greaterThan(lower), lessThan(upper)));
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are positive.
     *
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isPositive() {
        return new CompareIntegerValidator(0, CompareDirection.GREATER);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are negative.
     *
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isNegative() {
        return new CompareIntegerValidator(0, CompareDirection.LESS);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are odd numbers.
     *
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isOdd() {
        return new ParityIntegerValidator(true);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are even numbers.
     *
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isEven() {
        return new ParityIntegerValidator(false);
    }

    /**
     * Gives error information about the given <code>Object</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise,
     *      an error message is returned.
     */





    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are greater than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive lower bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator greaterThan(int value, BundleInfo bundleInfo) {
        IntegerValidator validator = new CompareIntegerValidator(value, CompareDirection.GREATER,  bundleInfo);
        return validator;

    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code> and initializes
     * the validator with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   inclusive lower bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator greaterThanOrEqualTo(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value,
                CompareDirection.GREATER_OR_EQUAL,  bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are equal to <code>value</code> and initializes the validator with
     * a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   value to ensure equality for.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator equalTo(int value, BundleInfo bundleInfo) {
            return new CompareIntegerValidator(value,
                    CompareDirection.EQUAL,  bundleInfo);
    }
    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are less than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator lessThan(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value, CompareDirection.LESS,  bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code> and initializes the
     * validator with a specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   inclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator lessThanOrEqualTo(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value,
                CompareDirection.LESS_OR_EQUAL,  bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive
     * and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static IntegerValidator inRange(int lower, int upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower, bundleInfo), lessThanOrEqualTo(upper, bundleInfo)));
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive
     * and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static IntegerValidator inExclusiveRange(int lower, int upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(
                greaterThan(lower, bundleInfo), lessThan(upper, bundleInfo)));
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are positive and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isPositive(BundleInfo bundleInfo) {
        return new CompareIntegerValidator(0, CompareDirection.GREATER,  bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are negative and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isNegative(BundleInfo bundleInfo) {
        return new CompareIntegerValidator(0, CompareDirection.LESS,  bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are odd numbers and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isOdd(BundleInfo bundleInfo) {
        return new ParityIntegerValidator(true, bundleInfo);
    }

    /**
     * Creates an <code>IntegerValidator</code> which would test whether
     * inputs are even numbers and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  an <code>IntegerValidator</code> that would perform the
     *      desired validation.
     */
    public static IntegerValidator isEven(BundleInfo bundleInfo) {
        return new ParityIntegerValidator(false, bundleInfo);
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
            return getMessage(((Number) obj).intValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into an int
                return getMessage(Integer.parseInt((String) obj));
            } catch (Exception exception) { }
        }

        // if no getMessage(int) was successfully called, return error message
        return "invalid Integer";
    }

    /**
     * Gives error information about the given primitive <code>int</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>int</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(int value);
}
