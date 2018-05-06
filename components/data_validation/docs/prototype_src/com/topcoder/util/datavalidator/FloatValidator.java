/*
 * TCS Data Validation
 *
 * FloatValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple float validator abstraction which basically checks that
 * a given value conforms with a specific float definition. There is also an
 * option to use this validator with an error of comparison delta which is
 * termed here as epsilon which means that comparisons could be done to within
 * this particular epsilon.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific double value validation. Note that this
 * validator acts as a factory that allows for creation of other specialized
 * FloatValidator instances that provide a number of convenience methods
 * which validate such aspects as range (from , to and could also be exclusive
 * of extremes) or provide validation of general comparison of values such as
 * greater-than, less-than, etc... We can also create validators that validate
 * for negative or positive values or test if the float can be considered to
 * be an integer (i.e. no loss of precision when converting or with specific
 * acceptable loss of precision)
 * User will need to implement the <code>valid(float value) </code> method to
 * decide what the validator will do with the input byte.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class FloatValidator extends AbstractObjectValidator {

   /** The default epsilon value to use (= 1e-8). */
    public static final float EPSILON = (float) 1e-8;

   /**
     * The epsilon value to be used in this validator. This is initialized
     * in the constructor (optional) and must be a number that is positive
     * or zero. This value is immutable but cannot be made final since it is
     * statically initialized to EPSILON which acts as a default.
     * The value cannot be NaN as well.
     */
    private float eps = EPSILON;

    /**
     * Creates a new <code>FloatValidator</code>.
     */
    public FloatValidator() {
    }


    /**
     * Creates a new <code>FloatValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public FloatValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }

    /**
     * Sets the epsilon value to be used with this validator.
     *
     * @param   eps epsilon value to use.
     * @throws  IllegalArgumentException    if <code>eps</code> is NaN.
     */
    public void setEpsilon(float eps)
            throws IllegalArgumentException {
        if (Float.isNaN(eps)) {
            throw new IllegalArgumentException("eps cannot be NaN");
        }

        this.eps = eps;
    }

    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Float</code>, or a <code>String</code> representation of a
     * <code>float</code>. If so, it passes the <code>float</code> value
     * to <code>valid(float)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).floatValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a float
                return valid(Float.parseFloat((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(float) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>float</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>float</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(float value);

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator greaterThan(float value) {
        return new CompareFloatValidator(value, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are greater than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     *
     * @param   value   exclusive lower bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator greaterThan(float value, BundleInfo bundleInfo) {
        return new CompareFloatValidator(value, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator greaterThanOrEqualTo(float value) {
        return new CompareFloatValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code> and initializes
     * the validator with a specific resource bundle information.
     *
     * @param   value   inclusive lower bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator greaterThanOrEqualTo(float value, BundleInfo bundleInfo) {
        return new CompareFloatValidator(value,
                CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }



    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator lessThan(float value) {
        return new CompareFloatValidator(value, CompareDirection.LESS);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are less than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     *
     * @param   value   exclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator lessThan(float value, BundleInfo bundleInfo) {
        return new CompareFloatValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator lessThanOrEqualTo(float value) {
        return new CompareFloatValidator(value, CompareDirection.LESS_OR_EQUAL);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code> and initializes the
     * validator with a specific resource bundle information.
     *
     * @param   value   inclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator lessThanOrEqualTo(float value, BundleInfo bundleInfo) {
        return new CompareFloatValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are equal to <code>value</code>.
     *
     * @param   value   the value to be tested for equality
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator equalTo(float value) {
        return new CompareFloatValidator(value, CompareDirection.EQUAL);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are equal to <code>value</code> and initializes the validator with
     * a specific resource bundle information.
     *
     * @param   value   the value to be tested for equality
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator equalTo(float value, BundleInfo bundleInfo) {
        return new CompareFloatValidator(value, CompareDirection.EQUAL, bundleInfo);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static FloatValidator inRange(float lower, float upper)
            throws IllegalArgumentException {
        return new RangeFloatValidator(lower, upper, false);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive
     * and initializes the validator with a specific resource
     * bundle information.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static FloatValidator inRange(float lower, float upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        return new RangeFloatValidator(lower, upper, false, bundleInfo);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static FloatValidator inExclusiveRange(float lower, float upper)
            throws IllegalArgumentException {
        return new RangeFloatValidator(lower, upper, true);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive
     * and initializes the validator with a specific resource
     * bundle information.
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static FloatValidator inExclusiveRange(float lower, float upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        return new RangeFloatValidator(lower, upper, true, bundleInfo);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are positive.
     *
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isPositive() {
        return new CompareFloatValidator(0, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are positive and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isPositive(BundleInfo bundleInfo) {
        return new CompareFloatValidator(0, CompareDirection.GREATER, bundleInfo);
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are negative.
     *
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isNegative() {
        return new CompareFloatValidator(0, CompareDirection.LESS);
    }

    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are negative and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isNegative(BundleInfo bundleInfo) {
        return new CompareFloatValidator(0, CompareDirection.LESS, bundleInfo);
    }



    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are mathematical integers.
     *
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isInteger() {
        return new IntegralFloatValidator();
    }


    /**
     * Creates a <code>FloatValidator</code> which would test whether
     * inputs are mathematical integers and initializes the validator with a
     * specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>FloatValidator</code> that would perform the
     *      desired validation.
     */
    public static FloatValidator isInteger(BundleInfo bundleInfo) {
        return new IntegralFloatValidator(bundleInfo);
    }

    /**
     * This is a simple getter for the epsilon value assocaited with this
     * validator
     *
     * @return  the epsilon associated with this validator
     */
    public double getEpsilon() {
                return eps;
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
            return getMessage(((Number) obj).floatValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a float
                return getMessage(Float.parseFloat((String) obj));
            } catch (Exception exception) { }
        }

        // if no getMessage(float) was successfully called, return error message
        return "invalid Float";
    }

    /**
     * Gives error information about the given primitive <code>float</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>float</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(float value);
}
