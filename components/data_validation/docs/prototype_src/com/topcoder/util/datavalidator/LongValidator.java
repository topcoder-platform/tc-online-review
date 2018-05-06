/*
 * TCS Data Validation
 *
 * LongValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple long integer validator abstraction which basically checks
 * that a given value conforms with a specific long integer definition.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific long integer value validation. Note that
 * this validator acts as a factory that allows for creation of other specialized
 * LongValidator instances that provide a number of convenience methods which
 * validate such aspects as range (from , to, and could also be exclusive of
 * extremes) or general comparison of values such as greater-than, less-than,
 * etc... We can also create validators that validate if a number is positive
 * or negative, or even if it is odd or even (i.e. parity comparison)
 * User will need to implement the <code>valid(long value) </code> method to
 * decide what the validator will do with the input byte.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class LongValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>LongValidator</code>.
     */
    public LongValidator() {
    }

    /**
     * Creates a new <code>LongValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public LongValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Long</code>, or a <code>String</code> representation of a
     * <code>long</code>. If so, it passes the <code>long</code> value
     * to <code>valid(long)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).longValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a long
                return valid(Long.parseLong((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(long) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>long</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>long</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(long value);

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator greaterThan(long value) {
        return new CompareLongValidator(value, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator greaterThanOrEqualTo(long value) {
        return new CompareLongValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator lessThan(long value) {
        return new CompareLongValidator(value, CompareDirection.LESS);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator lessThanOrEqualTo(long value) {
        return new CompareLongValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static LongValidator inRange(long lower, long upper)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new LongValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static LongValidator inExclusiveRange(long lower, long upper)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new LongValidatorWrapper(new AndValidator(greaterThan(lower),
                lessThan(upper)));
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are positive.
     *
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isPositive() {
        return new CompareLongValidator(0, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are negative.
     *
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isNegative() {
        return new CompareLongValidator(0, CompareDirection.LESS);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are odd numbers.
     *
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isOdd() {
        return new ParityLongValidator(true);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are even numbers.
     *
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isEven() {
        return new ParityLongValidator(false);
    }







    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are greater than <code>value</code>  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator greaterThan(long value, BundleInfo bundleInfo) {
        return new CompareLongValidator(value, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>  and initializes the validator with a specific resource
     * bundle information.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator greaterThanOrEqualTo(long value, BundleInfo bundleInfo) {
        return new CompareLongValidator(value,
                CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are less than <code>value</code>  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator lessThan(long value, BundleInfo bundleInfo) {
        return new CompareLongValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator lessThanOrEqualTo(long value, BundleInfo bundleInfo) {
        return new CompareLongValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static LongValidator inRange(long lower, long upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new LongValidatorWrapper(new AndValidator(
                greaterThanOrEqualTo(lower, bundleInfo), lessThanOrEqualTo(upper, bundleInfo)));
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static LongValidator inExclusiveRange(long lower, long upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal "
                    + "to lower");
        }

        return new LongValidatorWrapper(new AndValidator(greaterThan(lower, bundleInfo),
                lessThan(upper, bundleInfo)));
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are positive  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isPositive(BundleInfo bundleInfo) {
        return new CompareLongValidator(0, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are negative  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isNegative(BundleInfo bundleInfo) {
        return new CompareLongValidator(0, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are odd numbers  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isOdd(BundleInfo bundleInfo) {
        return new ParityLongValidator(true, bundleInfo);
    }

    /**
     * Creates a <code>LongValidator</code> which would test whether
     * inputs are even numbers  and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>LongValidator</code> that would perform the
     *      desired validation.
     */
    public static LongValidator isEven(BundleInfo bundleInfo) {
        return new ParityLongValidator(false, bundleInfo);
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
            return getMessage(((Number) obj).longValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a long
                return getMessage(Long.parseLong((String) obj));
            } catch (Exception exception) { }
        }

        // if no getMessage(long) was successfully called, return error message
        return "invalid Long";
    }

    /**
     * Gives error information about the given primitive <code>long</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>long</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(long value);
}
