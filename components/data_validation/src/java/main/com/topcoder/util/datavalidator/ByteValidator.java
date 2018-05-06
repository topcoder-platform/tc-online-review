/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a simple byte validator abstraction which basically checks that a given value conforms with a specific byte
 * definition.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine for a specific
 * byte value validation. Note that this validator acts as a factory that allows for creation of other specialized
 * <code>ByteValidator</code> instances that provide a number of convenience methods which validate such aspects as
 * range (from , to and could also be exclusive of extremes) or provide general comparison of values such as
 * greater-than, less-than, etc... User will need to implement the <code>valid(byte value) </code> method to decide
 * what the validator will do with the input byte.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class ByteValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>ByteValidator</code>.
     * </p>
     */
    public ByteValidator() {
        super();
    }

    /**
     * <p>
     * Creates a new <code>ByteValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public ByteValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Checks whether the given <code>Object</code> is in fact a <code>Number</code>, or a <code>String</code>
     * representation of a <code>byte</code>. If so, it passes the <code>byte</code> value to
     * <code>valid(byte)</code>. Otherwise, it returns <code>false</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
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
            } catch (Exception exception) {
                // ignore the exception
            }
        }

        // if no valid(byte) was successfully called, return false
        return false;
    }

    /**
     * <p>
     * This validates the given primitive <code>byte</code> value. It is an <code>abstract</code> method, so it should
     * be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>byte</code> value to validate.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(byte value);

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are greater than <code>value</code>.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator greaterThan(byte value) {
        return new CompareByteValidator(value, CompareDirection.GREATER);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator greaterThanOrEqualTo(byte value) {
        return new CompareByteValidator(value, CompareDirection.GREATER_OR_EQUAL);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are less than <code>value</code>.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator lessThan(byte value) {
        return new CompareByteValidator(value, CompareDirection.LESS);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code>.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator lessThanOrEqualTo(byte value) {
        return new CompareByteValidator(value, CompareDirection.LESS_OR_EQUAL);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are equal to <code>value</code>.
     * </p>
     *
     * @param value specific value for equality.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator equal(byte value) {
        return new CompareByteValidator(value, CompareDirection.EQUAL);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static ByteValidator inRange(byte lower, byte upper) {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new ByteValidatorWrapper(new AndValidator(greaterThanOrEqualTo(lower), lessThanOrEqualTo(upper)));
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static ByteValidator inExclusiveRange(byte lower, byte upper) {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal " + "to lower");
        }

        return new ByteValidatorWrapper(new AndValidator(greaterThan(lower), lessThan(upper)));
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are greater than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator greaterThan(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.GREATER, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are greater than or equal to
     * <code>value</code> and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive lower bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator greaterThanOrEqualTo(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.GREATER_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are less than <code>value</code> and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator lessThan(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.LESS, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are less than or equal to
     * <code>value</code>. and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param value inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator lessThanOrEqualTo(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.LESS_OR_EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are equal to <code>value</code>. and
     * initializes the validator with a specific resource
     * </p>
     *
     * @param value specific value for equality.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     */
    public static ByteValidator equal(byte value, BundleInfo bundleInfo) {
        return new CompareByteValidator(value, CompareDirection.EQUAL, bundleInfo);
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> inclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower inclusive lower bound of valid values.
     * @param upper inclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than lower.
     */
    public static ByteValidator inRange(byte lower, byte upper, BundleInfo bundleInfo) {
        if (upper < lower) {
            throw new IllegalArgumentException("upper is less than lower");
        }

        return new ByteValidatorWrapper(new AndValidator(greaterThanOrEqualTo(lower, bundleInfo),
                lessThanOrEqualTo(upper, bundleInfo)));
    }

    /**
     * <p>
     * Creates a <code>ByteValidator</code> which would test whether inputs are between <code>lower</code> and
     * <code>upper</code> exclusive and initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param lower exclusive lower bound of valid values.
     * @param upper exclusive upper bound of valid values.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>ByteValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if upper is less than or equal to lower.
     */
    public static ByteValidator inExclusiveRange(byte lower, byte upper, BundleInfo bundleInfo) {
        if (upper <= lower) {
            throw new IllegalArgumentException("upper is less than or equal " + "to lower");
        }

        return new ByteValidatorWrapper(new AndValidator(greaterThan(lower, bundleInfo), lessThan(upper, bundleInfo)));
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
            return getMessage(((Number) obj).byteValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            try {
                // attempts to parse obj into a byte
                return getMessage(Byte.parseByte((String) obj));
            } catch (Exception exception) {
                // ignore the exception
            }
        }

        // if no getMessage(byte) was successfully called, return error message
        return "invalid Byte";
    }

    /**
     * <p>
     * Gives error information about the given primitive <code>byte</code> value. It is an <code>abstract</code>
     * method, so it should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>byte</code> value to validate.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(byte value);

    /**
     * <p>
     * This is something of an adapter class which gives us a simple implementation of the abstract
     * <code>ByteValidator</code> class. Note that this will wrap around any type of a validator but since this is an
     * internal class only <code>ByteValidator</code> class is expected. This is an inner class to
     * <code>ByteValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>It is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class ByteValidatorWrapper extends ByteValidator {
        /**
         * <p>
         * The underlying validator to use. This is initialized through the constructor and is immutable after that. It
         * is expected to be non-null.
         * </p>
         */
        private final ObjectValidator validator;

        /**
         * <p>
         * Creates a new <code>ByteValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public ByteValidatorWrapper(ObjectValidator validator) {
            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Creates a new <code>ByteValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code> and is initialized with resource bundle information.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code> or if bundleInfo is null.
         */
        public ByteValidatorWrapper(ObjectValidator validator, BundleInfo bundleInfo) {
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
         * Validates the given <code>byte</code> value.
         * </p>
         *
         * @param value <code>byte</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(byte value) {
            return validator.valid(new Byte(value));
        }

        /**
         * <p>
         * If the given <code>byte</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>byte</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(byte value) {
            return validator.getMessage(new Byte(value));
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
     * This is an extension of the <code>ByteValidator</code> which specifically deals with validating comparisons
     * based criteria such equal, greater-than-or-equal, less-than, less-than-or-equal, greater-than. Thus we will
     * have a validation based on a specific initialization value (that is set with the validator) and then have this
     * validator compare the input value with the set value using one of the specific comparison directions. This is
     * an inner class to <code>ByteValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b> This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class CompareByteValidator extends ByteValidator {
        /**
         * <p>
         * The value to compare inputs with. This is the validator initilization value which is set in the constructor
         * and once set is immutable. This cannot be null and represents the value against which the input values will
         * be compared.
         * </p>
         */
        private final byte value;

        /**
         * <p>
         * The direction for comparing inputs. This is initialized in the constructor and is immutable therafter. It
         * cannot be null.
         * </p>
         */
        private final CompareDirection direction;

        /**
         * <p>
         * Creates a new <code>CompareByteValidator</code> that compares inputs to <code>value</code>, in the direction
         * determined by <code>direction</code>.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         */
        public CompareByteValidator(byte value, CompareDirection direction) {
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Creates a new <code>CompareByteValidator</code> that compares inputs to <code>value</code>, in the direction
         * determined by <code>direction</code> and with a resource bundle information.
         * </p>
         *
         * @param value inputs are compared to this.
         * @param direction the comparison direction.
         * @param bundleInfo resource bundle information
         */
        public CompareByteValidator(byte value, CompareDirection direction, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.value = value;
            this.direction = direction;
        }

        /**
         * <p>
         * Validates the given <code>byte</code> value.
         * </p>
         *
         * @param value <code>byte</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(byte value) {
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
         * If the given <code>byte</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>byte</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(byte value) {
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
}
