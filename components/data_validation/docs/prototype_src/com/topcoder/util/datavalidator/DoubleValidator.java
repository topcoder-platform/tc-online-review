/*
 * TCS Data Validation
 *
 * DoubleValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

 /**
 * This is a simple double validator abstraction which basically checks that
 * a given value conforms with a specific double definition. There is also an
 * option to use this validator with an error of comparison delta which is
 * termed here as epsilon which means that comparisons coudl be done to within
 * this particular epsilon.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific double value validation. Note that this
 * validator acts as a factory that allows for creation of other specialized
 * DoubleValidator instances that provide a number of convenience methods
 * methods which validate such aspects as range (from , to and could also be
 * exclusive of extremes) or provide general comparison of values such as
 * greater-than, less-than, etc... We can also create validators that validate
 * for negative or positive values or test if the double can be considered to
 * be an integer (i.e. no loss of precision when converting or with specified
 * and acceptable loss of precision)
 * User will need to implement the <code>valid(double value) </code> method to
 * decide what the validator will do with the input byte.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class DoubleValidator extends AbstractObjectValidator {

    /** The default epsilon value to use (= 1e-12). */
    public static final double EPSILON = 1e-12;

    /**
     * The epsilon value to be used in this validator. This is initialized
     * in the constructor (optional) and must be a number that is positive
     * or zero. This value is immutable but cannot be made final since it is
     * statically initialized to EPSILON which acts as a default.
     * The value cannot be NaN as well.
     */
    private double eps = EPSILON;

    /**
     * Creates a new <code>DoubleValidator</code>.
     */
    public DoubleValidator() {
    }


    /**
     * Creates a new <code>DoubleValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public DoubleValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Sets the epsilon value to be used with this validator.
     *
     * @param   eps epsilon value to use.
     * @throws  IllegalArgumentException    if <code>eps</code> is NaN.
     */
    public void setEpsilon(double eps)
            throws IllegalArgumentException {
        if (Double.isNaN(eps)) {
            throw new IllegalArgumentException("eps cannot be NaN");
        }

        this.eps = eps;
    }

    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Double</code>, or a <code>String</code> representation of a
     * <code>double</code>. If so, it passes the <code>double</code> value
     * to <code>valid(double)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {

        // checks if obj is a Number
        if (obj instanceof Number) {
            return valid(((Number) obj).doubleValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a double
                return valid(Double.parseDouble((String) obj));
            } catch (Exception exception) { }
        }

        // if no valid(double) was successfully called, return false
        return false;
    }

    /**
     * This validates the given primitive <code>double</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>double</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(double value);

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are greater than <code>value</code>.
     *
     * @param   value   exclusive lower bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator greaterThan(double value) {
        return new CompareDoubleValidator(value, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are greater than <code>value</code> and initializes the validator
     * with a specific resource bundle information.
     *
     * @param   value   exclusive lower bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator greaterThan(double value, BundleInfo bundleInfo) {
      return new CompareDoubleValidator(value, CompareDirection.GREATER,bundleInfo );
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code>.
     *
     * @param   value   inclusive lower bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator greaterThanOrEqualTo(double value) {
        return new CompareDoubleValidator(value,
                CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are greater than or equal to <code>value</code> and initializes
     * the validator with a specific resource bundle information.
     *
     * @param   value   inclusive lower bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator greaterThanOrEqualTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value,
                CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are less than <code>value</code>.
     *
     * @param   value   exclusive upper bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator lessThan(double value) {
        return new CompareDoubleValidator(value, CompareDirection.LESS);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are less than <code>value</code> and initializes
     * the validator with a specific resource bundle information.
     *
     * @param   value   exclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator lessThan(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code>.
     *
     * @param   value   inclusive upper bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator lessThanOrEqualTo(double value) {
        return new CompareDoubleValidator(value,
                CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are less than or equal to <code>value</code> and initializes
     * the validator with a specific resource bundle information.
     *
     * @param   value   inclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator lessThanOrEqualTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value,
                CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are equal to <code>value</code>.
     *
     * @param   value   the value to be tested for equality
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator equalTo(double value) {
        return new CompareDoubleValidator(value,
                CompareDirection.EQUAL);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are equal to <code>value</code> and initializes
     * the validator with a specific resource bundle information
     *
     * @param   value   the value to be tested for equality
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator equalTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value,
                CompareDirection.EQUAL, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive.
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static DoubleValidator inRange(double lower, double upper)
            throws IllegalArgumentException {
        return new RangeDoubleValidator(lower, upper, false);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> inclusive
     * and initializes the validator with a specific resource bundle
     * information
     *
     * @param   lower   inclusive lower bound of valid values.
     * @param   upper   inclusive upper bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     * @param bundleInfo BundleInfo  resource bundle information
     * @throws  IllegalArgumentException    if upper is less than lower.
     */
    public static DoubleValidator inRange(double lower, double upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        return new RangeDoubleValidator(lower, upper, false, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive.
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static DoubleValidator inExclusiveRange(double lower, double upper)
            throws IllegalArgumentException {
        return new RangeDoubleValidator(lower, upper, true);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are between <code>lower</code> and <code>upper</code> exclusive
     * and initializes the validator with a specific resource bundle
     * information
     *
     * @param   lower   exclusive lower bound of valid values.
     * @param   upper   exclusive upper bound of valid values.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if upper is less than or equal to
     *      lower.
     */
    public static DoubleValidator inExclusiveRange(double lower, double upper, BundleInfo bundleInfo)
            throws IllegalArgumentException {
        return new RangeDoubleValidator(lower, upper, true, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are positive.
     *
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator isPositive() {
        return new CompareDoubleValidator(0, CompareDirection.GREATER);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are positive and initializes the validator with a specific
     * resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator isPositive(BundleInfo bundleInfo) {
        return new CompareDoubleValidator(0, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are negative.
     *
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator isNegative() {
        return new CompareDoubleValidator(0, CompareDirection.LESS);
    }

    /**
         * Creates a <code>DoubleValidator</code> which would test whether
         * inputs are negative and initializes the validator with a specific
         * resource bundle information.
         * @param bundleInfo BundleInfo  resource bundle information
         * @return  a <code>DoubleValidator</code> that would perform the
         *      desired validation.
         */
        public static DoubleValidator isNegative(BundleInfo bundleInfo) {
            return new CompareDoubleValidator(0, CompareDirection.LESS, bundleInfo);
    }

    /**
     * Creates a <code>DoubleValidator</code> which would test whether
     * inputs are mathematical integers.
     *
     * @return  a <code>DoubleValidator</code> that would perform the
     *      desired validation.
     */
    public static DoubleValidator isInteger() {
        return new IntegralDoubleValidator();
    }


    /**
      * Creates a <code>DoubleValidator</code> which would test whether
      * inputs are mathematical integers and initializes the validator with a
      * specific resource bundle information.
      * @param bundleInfo BundleInfo  resource bundle information
      * @return  a <code>DoubleValidator</code> that would perform the
      *      desired validation.
      */
     public static DoubleValidator isInteger(BundleInfo bundleInfo) {
         return new IntegralDoubleValidator(bundleInfo);
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
            return getMessage(((Number) obj).doubleValue());

        // checks if obj is a String
        } else if (obj instanceof String) {
            try {

                // attempts to parse obj into a double
                return getMessage(Double.parseDouble((String) obj));
            } catch (Exception exception) { }
        }

        // if getMessage(double) was not called, return error message
        return "invalid Double";
    }

    /**
     * Gives error information about the given primitive <code>double</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>double</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(double value);
}
