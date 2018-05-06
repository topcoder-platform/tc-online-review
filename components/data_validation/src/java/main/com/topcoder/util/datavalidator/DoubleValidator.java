/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a simple double validator abstraction which basically checks that a given value conforms with a specific
 * double definition. There is also an option to use this validator with an error of comparison delta which is termed
 * here as EPSILON which means that comparisons could be done to within this particular EPSILON.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine for a specific
 * double value validation. Note that this validator acts as a factory that allows for creation of other specialized
 * <code>DoubleValidator</code> instances that provide a number of convenience methods methods which validate such
 * aspects as range (from , to and could also be exclusive of extremes) or provide general comparison of values such
 * as greater-than, less-than, etc... We can also create validators that validate for negative or positive values or
 * test if the double can be considered to be an integer (i.e. no loss of precision when converting or with specified
 * and acceptable loss of precision) User will need to implement the <code>valid(double value) </code> method to
 * decide what the validator will do with the input byte.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class DoubleValidator extends AbstractObjectValidator {
    /**
     * <p>
     * The default epsilon value to use (= 1e-12).
     * </p>
     */
    public static final double EPSILON = 1e-12;

    /**
     * <p>
     * The epsilon value to be used in this validator. This is initialized in the constructor (optional) and must be a
     * number that is positive or zero. This value is immutable but cannot be made final since it is statically
     * initialized to EPSILON which acts as a default. The value cannot be NaN as well.
     * </p>
     */
    private double eps = EPSILON;

    /**
     * <p>
     * Creates a new <code>DoubleValidator</code>.
     * </p>
     */
    public DoubleValidator() {
    }

    /**
     * <p>
     * Creates a new <code>DoubleValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public DoubleValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Sets the epsilon value to be used with this validator.
     * </p>
     *
     * @param eps epsilon value to use.
     *
     * @throws IllegalArgumentException if <code>eps</code> is NaN.
     */
    public void setEpsilon(double eps) {
        if (Double.isNaN(eps)) {
            throw new IllegalArgumentException("eps cannot be NaN");
        }

        this.eps = eps;
    }

    /**
     * <p>
     * Checks whether the given <code>Object</code> is in fact a <code>Double</code>, or a <code>String</code>
     * representation of a <code>double</code>. If so, it passes the <code>double</code> value to
     * <code>valid(double)</code>. Otherwise, it returns <code>false</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
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
            } catch (Exception exception) {
                // Ignore the exception
            }
        }

        // if no valid(double) was successfully called, return false
        return false;
    }

    /**
     * <p>
     * This validates the given primitive <code>double</code> value. It is an <code>abstract</code> method, so it
     * should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>double</code> value to validate.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(double value);

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are greater than <code>value</code>.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator greaterThan(double value) {
        return new CompareDoubleValidator(value, CompareDirection.GREATER);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are greater than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator greaterThan(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator greaterThanOrEqualTo(double value) {
        return new CompareDoubleValidator(value, CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code> and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator greaterThanOrEqualTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are less than <code>value</code>.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator lessThan(double value) {
        return new CompareDoubleValidator(value, CompareDirection.LESS);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are less than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator lessThan(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator lessThanOrEqualTo(double value) {
        return new CompareDoubleValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code> and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator lessThanOrEqualTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are equal to <code>value</code>.
     * </p>
     *
     * @param value the value to be tested for equality
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator equalTo(double value) {
        return new CompareDoubleValidator(value, CompareDirection.EQUAL);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are equal to <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value the value to be tested for equality
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator equalTo(double value, BundleInfo bundleInfo) {
        return new CompareDoubleValidator(value, CompareDirection.EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static DoubleValidator inRange(double lower, double upper) {
        return new RangeDoubleValidator(lower, upper, false);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static DoubleValidator inRange(double lower, double upper, BundleInfo bundleInfo) {
        return new RangeDoubleValidator(lower, upper, false, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static DoubleValidator inExclusiveRange(double lower, double upper) {
        return new RangeDoubleValidator(lower, upper, true);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static DoubleValidator inExclusiveRange(double lower, double upper, BundleInfo bundleInfo) {
        return new RangeDoubleValidator(lower, upper, true, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are positive.
     * </p>
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isPositive() {
        return new CompareDoubleValidator(0, CompareDirection.GREATER);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are positive and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isPositive(BundleInfo bundleInfo) {
        return new CompareDoubleValidator(0, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are negative.
     * </p>
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isNegative() {
        return new CompareDoubleValidator(0, CompareDirection.LESS);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are negative and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isNegative(BundleInfo bundleInfo) {
        return new CompareDoubleValidator(0, CompareDirection.LESS, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are mathematical integers.
     * </p>
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isInteger() {
        return new IntegralDoubleValidator();
    }

    /**
     * <p>
     * Creates a <code>DoubleValidator</code> which would test whether inputs are mathematical integers and initializes
     * the validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>DoubleValidator</code> that would perform the desired validation.
     */
    public static DoubleValidator isInteger(BundleInfo bundleInfo) {
        return new IntegralDoubleValidator(bundleInfo);
    }

    /**
     * <p>
     * This is a simple getter for the epsilon value assocaited with this validator.
     * </p>
     *
     * @return the epsilon associated with this validator
     */
    public double getEpsilon() {
        return eps;
    }

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
        // checks if obj is a Number
        if (obj instanceof Number) {
            return getMessage(((Number) obj).doubleValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            try {
                // attempts to parse obj into a double
                return getMessage(Double.parseDouble((String) obj));
            } catch (Exception exception) {
                // Ignore the exception
            }
        }

        // if getMessage(double) was not called, return error message
        return "invalid Double";
    }

    /**
     * <p>
     * Gives error information about the given primitive <code>double</code> value. It is an <code>abstract</code>
     * method, so it should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>double</code> value to validate.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(double value);

    /**
     * <p>
     * This is something of an adapter class which gives us a simple implementation of the abstract
     * <code>DoubleValidator</code> class. Note that this will wrap around any type of a validator but since this is
     * an internal class only <code>DoubleValidator</code> class is expected. This is an inner class to
     * <code>DoubleValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>It is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class DoubleValidatorWrapper extends DoubleValidator {
        /**
         * <p>
         * The underlying validator to use. This is initialized through the constructor and is immutable after that. It
         * is expected to be non-null.
         * </p>
         */
        private final ObjectValidator validator;

        /**
         * <p>
         * Creates a new <code>DoubleValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public DoubleValidatorWrapper(ObjectValidator validator) {
            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Creates a new <code>DoubleValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         * @param bundleInfo DOCUMENT ME!
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         *
         * @since 1.1
         */
        public DoubleValidatorWrapper(ObjectValidator validator, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Validates the given <code>Object</code>.
         * </p>
         *
         * @param obj <code>Object</code> to be validated.
         *
         * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(Object obj) {
            return validator.valid(obj);
        }

        /**
         * <p>
         * Validates the given <code>double</code> value.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(double value) {
            return validator.valid(new Double(value));
        }

        /**
         * <p>
         * If the given <code>double</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(double value) {
            return validator.getMessage(new Double(value));
        }

        /**
         * <p>
         * Gives error information about the given object being validated. This method will return possibly a number of
         * messages produced for this object by a number of validators if the validator is composite.
         * </p>
         *
         * @param object the object to validate
         *
         * @return String[] a non-empty but possibly null array of failure messages
         *
         * @since 1.1
         */
        public String[] getMessages(Object object) {
            return validator.getMessages(object);
        }

        /**
         * <p>
         * This is the same method concept as the <code>getMessage()</code> except that this method will evaluate the
         * whole validator tree and return all the messages from any validators that failed the object. In other words
         * this is a non-short-circuited version of the method.
         * </p>
         *
         * @param object the object to validate
         *
         * @return String[] a non-empty but possibly null array of failure messages
         *
         * @since 1.1
         */
        public String[] getAllMessages(Object object) {
            return validator.getAllMessages(object);
        }

        /**
         * <p>
         * This is another version of the <code>getAllMessages()</code> method which accepts a limit on how many
         * messages at most will be requested. This means that the traversal of the validator tree will stop the
         * moment messageLimit has been met. This is provided so that the user can limit the number of messages coming
         * back and thus limit the traversal time but still get a reasonable number of messages back to the user.
         * </p>
         *
         * @param object the object to validate
         * @param messageLimit the max number of messages. This must be greater-than-or-equal to 0.
         *
         * @return String[] a non-empty but possibly null array of failure messages with a most messageLimit messages.
         *
         * @throws IllegalArgumentException if messageLimit is not positive
         *
         * @since 1.1
         */
        public String[] getAllMessages(Object object, int messageLimit) {
            if (messageLimit <= 0) {
                throw new IllegalArgumentException("The argument 'messageLimit' must be greater than 0");
            }

            return validator.getAllMessages(object, messageLimit);
        }
    }

    /**
     * <p>
     * This is an extension of the <code>DoubleValidator</code> which specifically deals with validating comparisons
     * based criteria such equal, greater-than-or-equal, less-than, less-than-or-equal, greater-than. Thus we will
     * have a validation based on a specific initilization value (that is set with the validator) and then have this
     * validator compare the input value with the set value using one of the specific comparison directions. This is
     * an inner class to <code>DoubleValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class CompareDoubleValidator extends DoubleValidator {
        /**
         * <p>
         * The value to compare inputs with. This is the validator initilization value which is set in the constructor
         * and once set is immutable. This cannot be null and represents the value against which the input values will
         * be compared.
         * </p>
         */
        private final double value;

        /**
         * <p>
         * The direction for comparing inputs. This is initialized in the constructor and is immutable therafter. It
         * cannot be null.
         * </p>
         */
        private final CompareDirection direction;

        /**
         * <p>
         * Creates a new <code>CompareDoubleValidator</code> that compares inputs to <code>value</code>, in the
         * direction determined by <code>direction</code>.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         */
        public CompareDoubleValidator(double value, CompareDirection direction) {
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Creates a new <code>CompareDoubleValidator</code> that compares inputs to <code>value</code>, in the
         * direction determined by <code>direction</code> and with a specified resource bundle information.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         * @param bundleInfo resource bundle information
         *
         * @since 1.1
         */
        public CompareDoubleValidator(double value, CompareDirection direction, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Validates the given <code>double</code> value.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(double value) {
            double pole = 0;

            if (getEpsilon() > 0.0) {
                // Using epsilon
                pole = getEpsilon();
            }

            double delta = value - this.value;

            if (direction == CompareDirection.GREATER) {
                return delta > pole;
            } else if (direction == CompareDirection.GREATER_OR_EQUAL) {
                return delta >= -pole;
            } else if (direction == CompareDirection.LESS) {
                return delta < -pole;
            } else if (direction == CompareDirection.LESS_OR_EQUAL) {
                return delta <= pole;
            } else if (direction == CompareDirection.EQUAL) {
                return Math.abs(delta) <= pole;
            }

            // should not reach here because all cases are handled
            return false;
        }

        /**
         * <p>
         * If the given <code>double</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(double value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not " + direction + " " + this.value;
        }
    }

    /**
     * <p>
     * This is an extension of the <code>DoubleValidator</code> which specifically deals with validating a range. In
     * other words we are creating a validator that will validate if a given input number is in range (inclusive of
     * the extremes as an option) Note that an acceptable comparison error can be set as well (i.e. the epsilon) which
     * would be taken into account when deciding if the in-range criteria has been met. As an example, if we create an
     * instance of this validator set with a range of 2.5 - 7.88 with an epsilon of 1e-12 then a number like 5.0 would
     * be in range and even a number like 7.88000000045 would be in range but a number like 7.89 would not be valid.
     * This is an inner class to <code>DoubleValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class RangeDoubleValidator extends DoubleValidator {
        /**
         * <p>
         * The underlying lower validator to use. This is initialized through the constructor and is immutable after
         * that. It is expected to be non-null.
         * </p>
         */
        private final DoubleValidator lowerValidator;

        /**
         * <p>
         * The underlying upper validator to use. This is initialized through the constructor and is immutable after
         * that. It is expected to be non-null.
         * </p>
         */
        private final DoubleValidator upperValidator;

        /**
         * <p>
         * The underlying composite validator which will combine the upper and lower validators. This is initialized in
         * the the constructor and is immutable after that. it cannot be null but since it is not initialized by the
         * user there is no danger of that. This will always be an <code>AndValidator</code> which will combine the
         * upper and lower validators.
         * </p>
         */
        private ObjectValidator validator;

        /**
         * <p>
         * Creates a new <code>RangeDoubleValidator</code> that creates a range comparator which ensures that input is
         * with the specified range and if the exclusive flag is true then we exclude the extremes.
         * </p>
         *
         * @param lower lower bound of valid values.
         * @param upper upper bound of valid values.
         * @param exclusive whether the bounds are exclusive.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException when <code>exclusive</code> is false, this is thrown if upper is less than
         *         lower; when <code>exclusive</code> is true, this is thrown if upper is less than or equal to lower.
         *
         * @since 1.1
         */
        public RangeDoubleValidator(double lower, double upper, boolean exclusive, BundleInfo bundleInfo) {
            super(bundleInfo);

            if (!exclusive && (upper < lower)) {
                throw new IllegalArgumentException("upper is less than lower");
            }

            if (exclusive && (upper <= lower)) {
                throw new IllegalArgumentException("upper is less than or " + "equal to lower");
            }

            if (exclusive) {
                lowerValidator = DoubleValidator.greaterThan(lower, bundleInfo);
                upperValidator = DoubleValidator.lessThan(upper, bundleInfo);
            } else {
                lowerValidator = DoubleValidator.greaterThanOrEqualTo(lower, bundleInfo);
                upperValidator = DoubleValidator.lessThanOrEqualTo(upper, bundleInfo);
            }

            validator = new AndValidator(lowerValidator, upperValidator);
        }

        /**
         * <p>
         * Creates a new <code>RangeDoubleValidator</code> that creates a range comparator which ensures that input is
         * with the specified range and if the exclusive flag is true then we exclude the extremes. This is also
         * initialized with a specific resource bundle information used to fetch error messages.
         * </p>
         *
         * @param lower lower bound of valid values.
         * @param upper upper bound of valid values.
         * @param exclusive whether the bounds are exclusive.
         *
         * @throws IllegalArgumentException when <code>exclusive</code> is false, this is thrown if upper is less than
         *         lower; when <code>exclusive</code> is true, this is thrown if upper is less than or equal to lower.
         */
        public RangeDoubleValidator(double lower, double upper, boolean exclusive) {
            if (!exclusive && (upper < lower)) {
                throw new IllegalArgumentException("upper is less than lower");
            }

            if (exclusive && (upper <= lower)) {
                throw new IllegalArgumentException("upper is less than or " + "equal to lower");
            }

            if (exclusive) {
                lowerValidator = DoubleValidator.greaterThan(lower);
                upperValidator = DoubleValidator.lessThan(upper);
            } else {
                lowerValidator = DoubleValidator.greaterThanOrEqualTo(lower);
                upperValidator = DoubleValidator.lessThanOrEqualTo(upper);
            }

            validator = new AndValidator(lowerValidator, upperValidator);
        }

        /**
         * <p>
         * Sets the epsilon value to be used with this validator.
         * </p>
         *
         * @param eps epsilon value to use.
         *
         * @throws IllegalArgumentException if <code>eps</code> is NaN.
         */
        public void setEpsilon(double eps) {
            super.setEpsilon(eps);

            lowerValidator.setEpsilon(eps);
            upperValidator.setEpsilon(eps);
        }

        /**
         * <p>
         * Validates the given <code>double</code> value.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(double value) {
            return validator.valid(new Double(value));
        }

        /**
         * <p>
         * If the given <code>double</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(double value) {
            return validator.getMessage(new Double(value));
        }

        /**
         * <p>
         * Gives error information about the given object being validated. This method will return possibly a number of
         * messages produced for this object by a number of validators if the validator is composite.
         * </p>
         *
         * @param object the object to validate
         *
         * @return String[] a non-empty but possibly null array of failure messages
         *
         * @since 1.1
         */
        public String[] getMessages(Object object) {
            return validator.getMessages(object);
        }

        /**
         * <p>
         * This is the same method concept as the <code>getMessage()</code> except that this method will evaluate the
         * whole validator tree and return all the messages from any validators that failed the object. In other words
         * this is a non-short-circuited version of the method. Since this is a primitive validator the result will
         * always be equivalent to getting a single message but as an array.
         * </p>
         *
         * @param object the object to validate
         *
         * @return String[] a non-empty but possibly null array of failure messages
         *
         * @since 1.1
         */
        public String[] getAllMessages(Object object) {
            return validator.getAllMessages(object);
        }

        /**
         * <p>
         * This is another version of the <code>getAllMessages()</code> method which accepts a limit on how many
         * messages at most will be requested. This means that the traversal of the validator tree will stop the
         * moment messageLimit has been met. This is provided so that the user can limit the number of messages coming
         * back and thus limit the traversal time but still get a reasonable number of messages back to the user.
         * </p>
         *
         * @param object the object to validate
         * @param messageLimit the max number of messages. This must be greater-than-or-equal to 0.
         *
         * @return String[] a non-empty but possibly null array of failure messages with a most messageLimit messages.
         *
         * @throws IllegalArgumentException if messageLimit is not positive
         *
         * @since 1.1
         */
        public String[] getAllMessages(Object object, int messageLimit) {
            if (messageLimit <= 0) {
                throw new IllegalArgumentException("The argument 'messageLimit' must be greater than 0");
            }

            return validator.getAllMessages(object, messageLimit);
        }
    }

    /**
     * <p>
     * This is an extension of the <code>DoubleValidator</code> which specifically deals with validating that double
     * values are actually converibtible to integers without loss of precision (or with acceptable epsilon loss of
     * precision) In other words this validates if the input is a mathematical integer. Note that if epsilon is set it
     * will be used in the comparison which means that we will be accepting an approximation of the integer. This is
     * an inner class to <code>DoubleValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class IntegralDoubleValidator extends DoubleValidator {
        /**
         * <p>
         * Creates a new <code>IntegralDoubleValidator</code> that checks whether inputs are mathematical integers.
         * </p>
         */
        public IntegralDoubleValidator() {
        }

        /**
         * <p>
         * Creates a new <code>IntegralDoubleValidator</code>  and initializes the validator with a specific resource
         * bundle information.
         * </p>
         *
         * @param bundleInfo resource bundle information
         */
        public IntegralDoubleValidator(BundleInfo bundleInfo) {
            super(bundleInfo);
        }

        /**
         * <p>
         * Validates the given <code>double</code> value.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(double value) {
            // rint is the closest mathematical integer to value
            double rint = Math.rint(value);

            // using epsilon...
            if (getEpsilon() > 0.0d) {
                double delta = value - rint;

                return (delta <= getEpsilon()) && (delta >= -getEpsilon());

                // not using epsilon...
            } else {
                return value == rint;
            }
        }

        /**
         * <p>
         * If the given <code>double</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>double</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(double value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not an integer";
        }
    }
}
