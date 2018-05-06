/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a simple integer validator abstraction which basically checks that a given value conforms with a specific
 * integer definition.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine for a specific
 * integer value validation. Note that this validator acts as a factory that allows for creation of other specialized
 * <code>IntegerValidator</code> instances that provide a number of convenience methods which validate such aspects as
 * range (from , to, and could also be exclusive of extremes) or general comparison of values such as greater-than,
 * less-than, etc... We can also create validators which validate if a number is positive or negative, or even if it
 * is odd or even (i.e. parity comparison) User will need to implement the <code>valid(int value) </code> method to
 * decide what the validator will do with the input integer.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class IntegerValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>IntegerValidator</code>.
     * </p>
     */
    public IntegerValidator() {
    }

    /**
     * <p>
     * Creates a new <code>IntegerValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public IntegerValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Checks whether the given <code>Object</code> is in fact an <code>Integer</code>, or a <code>String</code>
     * representation of an <code>int</code>. If so, it passes the <code>int</code> value to <code>valid(int)</code>.
     * Otherwise, it returns <code>false</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
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
            } catch (Exception exception) {
                // Ignore the exception
            }
        }

        // if no valid(int) was successfully called, return false
        return false;
    }

    /**
     * <p>
     * This validates the given primitive <code>int</code> value. It is an <code>abstract</code> method, so it should
     * be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>int</code> value to validate.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(int value);

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are greater than <code>value</code>.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator greaterThan(int value) {
        IntegerValidator validator = new CompareIntegerValidator(value, CompareDirection.GREATER);

        return validator;
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator greaterThanOrEqualTo(int value) {
        return new CompareIntegerValidator(value, CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are equal to <code>value</code>.
     * </p>
     *
     * @param value value to ensure equality for.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator equalTo(int value) {
        return new CompareIntegerValidator(value, CompareDirection.EQUAL);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are less than <code>value</code>.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator lessThan(int value) {
        return new CompareIntegerValidator(value, CompareDirection.LESS);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator lessThanOrEqualTo(int value) {
        return new CompareIntegerValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static IntegerValidator inRange(int lower, int upper) {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static IntegerValidator inExclusiveRange(int lower, int upper) {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal " + "to lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(greaterThan(lower), lessThan(upper)));
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are positive.
     * </p>
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isPositive() {
        return new CompareIntegerValidator(0, CompareDirection.GREATER);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are negative.
     * </p>
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isNegative() {
        return new CompareIntegerValidator(0, CompareDirection.LESS);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are odd numbers.
     * </p>
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isOdd() {
        return new ParityIntegerValidator(true);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are even numbers.
     * </p>
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isEven() {
        return new ParityIntegerValidator(false);
    }

    /**
     * <p>
     * Gives error information about the given <code>Object</code>.
     * </p>
     *
     * @param value <code>Object</code> to validate.
     * @param bundleInfo DOCUMENT ME!
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise, an error message is returned.
     */
    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are greater than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator greaterThan(int value, BundleInfo bundleInfo) {
        IntegerValidator validator = new CompareIntegerValidator(value, CompareDirection.GREATER, bundleInfo);

        return validator;
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code> and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator greaterThanOrEqualTo(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value, CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are equal to <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value value to ensure equality for.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator equalTo(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value, CompareDirection.EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are less than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator lessThan(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code> and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator lessThanOrEqualTo(int value, BundleInfo bundleInfo) {
        return new CompareIntegerValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static IntegerValidator inRange(int lower, int upper, BundleInfo bundleInfo) {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(greaterThanOrEqualTo(lower, bundleInfo),
                lessThanOrEqualTo(upper, bundleInfo)));
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static IntegerValidator inExclusiveRange(int lower, int upper, BundleInfo bundleInfo) {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal " + "to lower");
        }

        return new IntegerValidatorWrapper(new AndValidator(greaterThan(lower, bundleInfo),
                lessThan(upper, bundleInfo)));
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are positive and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isPositive(BundleInfo bundleInfo) {
        return new CompareIntegerValidator(0, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are negative and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isNegative(BundleInfo bundleInfo) {
        return new CompareIntegerValidator(0, CompareDirection.LESS, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are odd numbers and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isOdd(BundleInfo bundleInfo) {
        return new ParityIntegerValidator(true, bundleInfo);
    }

    /**
     * <p>
     * Creates an <code>IntegerValidator</code> which would test whether inputs are even numbers and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return an <code>IntegerValidator</code> that would perform the desired validation.
     */
    public static IntegerValidator isEven(BundleInfo bundleInfo) {
        return new ParityIntegerValidator(false, bundleInfo);
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
            return getMessage(((Number) obj).intValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            try {
                // attempts to parse obj into an int
                return getMessage(Integer.parseInt((String) obj));
            } catch (Exception exception) {
                // Ignore the exception
            }
        }

        // if no getMessage(int) was successfully called, return error message
        return "invalid Integer";
    }

    /**
     * <p>
     * Gives error information about the given primitive <code>int</code> value. It is an <code>abstract</code> method,
     * so it should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>int</code> value to validate.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(int value);

    /**
     * <p>
     * This is something of an adapter class which gives us a simple implementation of the abstract IntegerValidator
     * class. Note that this will wrap around any type of a validator but since this is an internal class only
     * IntegerValidator class is expected.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This is an inner class to IntegerValidator. It is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class IntegerValidatorWrapper extends IntegerValidator {
        /**
         * <p>
         * The underlying validator to use. This is initialized through the constructor and is immutable after that. It
         * is expected to be non-null.
         * </p>
         */
        private final ObjectValidator validator;

        /**
         * <p>
         * Creates a new <code>IntegerValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public IntegerValidatorWrapper(ObjectValidator validator) {
            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Creates a new <code>IntegerValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>  and initializes the validator with a specific resource bundle information.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public IntegerValidatorWrapper(ObjectValidator validator, BundleInfo bundleInfo) {
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
         * Validates the given <code>int</code> value.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(int value) {
            return validator.valid(new Integer(value));
        }

        /**
         * <p>
         * If the given <code>int</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(int value) {
            return validator.getMessage(new Integer(value));
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
     * This is an extension of the IntegerValidator which specifically deals with validating comparisons based criteria
     * such equal, greater-than-or-equal, less-than, less-than-or-equal, greater-than. Thus we will have a validation
     * based on a specific initilization value (that is set with the validator) and then have this validator compare
     * the input value with the set value using one of the specific comparison directions. This is an inner class to
     * IntegerValidator.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b> This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class CompareIntegerValidator extends IntegerValidator {
        /**
         * <p>
         * The value to compare inputs with. This is the validator initilization value which is set in the constructor
         * and once set is immutable. This cannot be null and represents the value against which the input values will
         * be compared.
         * </p>
         */
        private final int value;

        /**
         * <p>
         * The direction for comparing inputs. This is initialized in the constructor and is immutable therafter. It
         * cannot be null.
         * </p>
         */
        private final CompareDirection direction;

        /**
         * <p>
         * Creates a new <code>CompareIntegerValidator</code> that compares inputs to <code>value</code>, in the
         * direction determined by <code>direction</code>.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         */
        public CompareIntegerValidator(int value, CompareDirection direction) {
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Creates a new <code>CompareIntegerValidator</code> that compares inputs to <code>value</code>, in the
         * direction determined by <code>direction</code> and initializes the validator with a specific resource
         * bundle information.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         * @param bundleInfo resource bundle information
         */
        public CompareIntegerValidator(int value, CompareDirection direction, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Validates the given <code>int</code> value.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(int value) {
            if (direction == CompareDirection.GREATER) {
                return value > this.value;
            } else if (direction == CompareDirection.GREATER_OR_EQUAL) {
                return value >= this.value;
            } else if (direction == CompareDirection.LESS) {
                return value < this.value;
            } else if (direction == CompareDirection.LESS_OR_EQUAL) {
                return value <= this.value;
            } else if (direction == CompareDirection.EQUAL) {
                return value == this.value;
            }

            // should not reach here because all cases are handled
            return false;
        }

        /**
         * <p>
         * If the given <code>int</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(int value) {
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
     * This is an extension of the IntegerValidator which specifically deals with validating numbers based on their
     * parity (i.e. if the number is odd or even) Which parity should be validated against is set as a flag when
     * creating/initializing this validator and is basically odd (true) or even (false) As an example, if we create an
     * instance of this validator set with a parity of odd (true) which would then accpet only integers that are odd.
     * This is an inner class to IntegerValidator.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class ParityIntegerValidator extends IntegerValidator {
        /**
         * <p>
         * This represnts whether inputs must be odd or even. This is the validator initilization value which is set in
         * the constructor and once set is immutable.
         * </p>
         */
        private final boolean isOdd;

        /**
         * <p>
         * Creates a new <code>ParityIntegerValidator</code> that checks whether inputs are odd or even.
         * </p>
         *
         * @param isOdd <code>true</code> if inputs must be odd; <code>false</code> if inputs must be even.
         */
        public ParityIntegerValidator(boolean isOdd) {
            this.isOdd = isOdd;
        }

        /**
         * <p>
         * Creates a new <code>ParityIntegerValidator</code> that checks whether inputs are odd or even and initializes
         * the validator with a specific resource bundle information.
         * </p>
         *
         * @param isOdd <code>true</code> if inputs must be odd; <code>false</code> if inputs must be even.
         * @param bundleInfo resource bundle information
         */
        public ParityIntegerValidator(boolean isOdd, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.isOdd = isOdd;
        }

        /**
         * <p>
         * Validates the given <code>int</code> value.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(int value) {
            return ((value % 2) == 0) != isOdd;
        }

        /**
         * <p>
         * If the given <code>int</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param value <code>int</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(int value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not " + (isOdd ? "odd" : "even");
        }
    }
}
