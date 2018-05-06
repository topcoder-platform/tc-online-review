/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a simple character validator abstraction which basically checks that a given value conforms with a specific
 * character definition.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine for a specific
 * character value validation. Note that this validator acts as a factory that allows for creation of other
 * specialized <code>CharacterValidator</code> instances that provide a number of convenience methods which validate
 * such aspects as if the character is a digit or a letter or both, if it is a whitespace or if it is upper-case or
 * lower-case. User will need to implement the <code>valid(char value) </code> method to decide what the validator
 * will do with the input character.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class CharacterValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>CharacterValidator</code>.
     * </p>
     */
    public CharacterValidator() {
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public CharacterValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Checks whether the given <code>Object</code> is in fact a <code>Character</code>, or a <code>String</code>
     * representation of a <code>char</code>. If so, it passes the <code>char</code> value to
     * <code>valid(char)</code>. Otherwise, it returns <code>false</code>.
     * </p>
     *
     * @param obj <code>Object</code> to validate.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        // checks if obj is a Number
        if (obj instanceof Character) {
            return valid(((Character) obj).charValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            String str = (String) obj;

            if (str.length() == 1) {
                return valid(str.charAt(0));
            }
        }

        // if no valid(double) was successfully called, return false
        return false;
    }

    /**
     * <p>
     * This validates the given primitive <code>char</code> value. It is an <code>abstract</code> method, so it should
     * be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>char</code> value to validate.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(char value);

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are digits.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isDigit() {
        return new AlphanumCharacterValidator(true);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are letters.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLetter() {
        return new AlphanumCharacterValidator(false);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are letters or digits.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLetterOrDigit() {
        return new CharacterValidatorWrapper(new OrValidator(isLetter(), isDigit()));
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs upper case letters.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isUpperCase() {
        return new CaseCharacterValidator(true);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are lower case letters.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLowerCase() {
        return new CaseCharacterValidator(false);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are whitespace characters.
     * </p>
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isWhitespace() {
        return new WhitespaceCharacterValidator();
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are digits and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isDigit(BundleInfo bundleInfo) {
        return new AlphanumCharacterValidator(true, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are letters and initializes the
     * validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLetter(BundleInfo bundleInfo) {
        return new AlphanumCharacterValidator(false, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are letters or digits and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLetterOrDigit(BundleInfo bundleInfo) {
        return new CharacterValidatorWrapper(new OrValidator(isLetter(bundleInfo), isDigit(bundleInfo)));
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs upper case letters and initializes
     * the validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isUpperCase(BundleInfo bundleInfo) {
        return new CaseCharacterValidator(true, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are lower case letters and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isLowerCase(BundleInfo bundleInfo) {
        return new CaseCharacterValidator(false, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>CharacterValidator</code> which would test whether inputs are whitespace characters and
     * initializes the validator with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     *
     * @return a <code>CharacterValidator</code> that would perform the desired validation.
     */
    public static CharacterValidator isWhitespace(BundleInfo bundleInfo) {
        return new WhitespaceCharacterValidator(bundleInfo);
    }

    /**
     * <p>
     * If the given <code>Object</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(Object obj) {
        // checks if obj is a Number
        if (obj instanceof Character) {
            return getMessage(((Character) obj).charValue());

            // checks if obj is a String
        } else if (obj instanceof String) {
            String str = (String) obj;

            if (str.length() == 1) {
                return getMessage(str.charAt(0));
            }
        }

        // if no getMessage(char) was successfully called, return false
        return "invalid Character";
    }

    /**
     * <p>
     * Gives error information about the given primitive <code>char</code> value. It is an <code>abstract</code>
     * method, so it should be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param value <code>char</code> value to validate.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(char value);

    /**
     * <p>
     * This is something of an adapter class which gives us a simple implementation of the abstract
     * <code>CharacterValidator</code> class. Note that this will wrap around any type of a validator but since this
     * is an internal class only <code>CharacterValidator</code> class is expected. It is thread-safe as it is
     * immutable.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This is an inner class to CharacterValidator.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class CharacterValidatorWrapper extends CharacterValidator {
        /**
         * <p>
         * The underlying validator to use. This is initialized through the constructor and is immutable after that. It
         * is expected to be non-null.
         * </p>
         */
        private final ObjectValidator validator;

        /**
         * <p>
         * Creates a new <code>CharacterValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code>.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public CharacterValidatorWrapper(ObjectValidator validator) {
            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Creates a new <code>CharacterValidatorWrapper</code> that uses <code>validator</code> as the underlying
         * <code>ObjectValidator</code> and is initialized with resource bundle information.
         * </p>
         *
         * @param validator the underlying <code>ObjectValidator</code> to use.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code> or if bundleInfo is null.
         *
         * @since 1.1
         */
        public CharacterValidatorWrapper(ObjectValidator validator, BundleInfo bundleInfo) {
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
         * Validates the given <code>char</code> value.
         * </p>
         *
         * @param value <code>char</code> value to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            return validator.valid(new Character(value));
        }

        /**
         * <p>
         * If the given <code>char</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>char</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(char value) {
            return validator.getMessage(new Character(value));
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
     * This is a specific type of <code>CharacterValidator</code> which ensures either the upper case or lower case
     * aspect of input (i.e. of the character). Thus this validator works in one of two modes:
     *
     * <ol>
     * <li>
     * Test for upper case
     * </li>
     * <li>
     * Test for lower case User will choose which mode it operates in through the constructor flag.
     * </li>
     * </ol>
     *
     * This is an inner class of <code>CharacterValidator</code>
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This is thread-safe since it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class CaseCharacterValidator extends CharacterValidator {
        /**
         * <p>
         * Whether inputs must be upper or lower case letters. This is initialized in the constructor and denotes
         * upper-case validation (true) or lower-case validation (false) Once initialized it is immutable.
         * </p>
         */
        private final boolean isUpper;

        /**
         * <p>
         * Creates a new <code>CaseCharacterValidator</code> that checks whether inputs are upper or lower case
         * letters.
         * </p>
         *
         * @param isUpper <code>true</code> if inputs must be upper case; <code>false</code> if inputs must be lower
         *        case.
         */
        public CaseCharacterValidator(boolean isUpper) {
            this.isUpper = isUpper;
        }

        /**
         * <p>
         * Creates a new <code>CaseCharacterValidator</code> that checks whether inputs are upper or lower case letters
         * and is initialized with resource bundle information.
         * </p>
         *
         * @param isUpper <code>true</code> if inputs must be upper case; <code>false</code> if inputs must be lower
         *        case.
         * @param bundleInfo resource bundle information
         *
         * @since 1.1
         */
        public CaseCharacterValidator(boolean isUpper, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.isUpper = isUpper;
        }

        /**
         * <p>
         * Validates the given <code>char</code> value.
         * </p>
         *
         * @param value <code>char</code> to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            if (isUpper) {
                return Character.isUpperCase(value);
            } else {
                return Character.isLowerCase(value);
            }
        }

        /**
         * <p>
         * If the given <code>char</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>char</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(char value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not " + (isUpper ? "an upper" : "a lower") + " case letter";
        }
    }

    /**
     * <p>
     * This is an extension of the <code>CharacterValidator</code> which specifically deals with validating strings
     * based on whether they are to be consider a whitespace or not. This is an inner class to
     * <code>StringValidator</code>.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class WhitespaceCharacterValidator extends CharacterValidator {
        /**
         * <p>
         * Creates a new <code>CaseCharacterValidator</code> that checks whether inputs are whitespace characters.
         * </p>
         */
        public WhitespaceCharacterValidator() {
        }

        /**
         * <p>
         * Creates a new <code>WhitespaceCharacterValidator</code>  and initializes the validator with a specific
         * resource bundle information.
         * </p>
         *
         * @param bundleInfo resource bundle information
         *
         * @since 1.1
         */
        public WhitespaceCharacterValidator(BundleInfo bundleInfo) {
            super(bundleInfo);
        }

        /**
         * <p>
         * Validates the given <code>char</code> value.
         * </p>
         *
         * @param value <code>char</code> to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            return Character.isWhitespace(value);
        }

        /**
         * <p>
         * If the given <code>char</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>char</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(char value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not a whitespace";
        }
    }

    /**
     * <p>
     * This is a specific type of <code>CharacterValidator</code> which will validate an input character as a digit or
     * a letter (but not both) It can act in one of those modes: 1. Check of the input character is a digit 2. Check
     * if the input character is letter This is determined by the initialization done in the constructor which allows
     * the user to choose either a digit validation (isDigit - true) or letter validation (idDigit - false), Note that
     * this is an inner class of the <code>CharacterValidator</code> and is not part of the public API.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b> This class is thread-safe since it is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class AlphanumCharacterValidator extends CharacterValidator {
        /**
         * <p>
         * Whether inputs must be digits or letters. This is intialized in the constructor and is used during
         * validation to determine the mode of validation (true - must be digit, false - must be letter)
         * </p>
         */
        private final boolean isDigit;

        /**
         * <p>
         * Creates a new <code>AlphanumCharacterValidator</code> that checks whether inputs are letters or digits.
         * </p>
         *
         * @param isDigit <code>true</code> if inputs must be digits; <code>false</code> if inputs must be letters.
         */
        public AlphanumCharacterValidator(boolean isDigit) {
            this.isDigit = isDigit;
        }

        /**
         * <p>
         * Creates a new <code>AlphanumCharacterValidator</code> that checks whether inputs are letters or digits and
         * is set with a specific resource bundle info.
         * </p>
         *
         * @param isDigit <code>true</code> if inputs must be digits; <code>false</code> if inputs must be letters.
         * @param bundleInfo resource bundle information
         *
         * @since 1.1
         */
        public AlphanumCharacterValidator(boolean isDigit, BundleInfo bundleInfo) {
            super(bundleInfo);
            this.isDigit = isDigit;
        }

        /**
         * <p>
         * Validates the given <code>char</code> value.
         * </p>
         *
         * @param value <code>char</code> to be validated.
         *
         * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(char value) {
            if (isDigit) {
                return Character.isDigit(value);
            } else {
                return Character.isLetter(value);
            }
        }

        /**
         * <p>
         * If the given <code>char</code> value is valid, this returns <code>null</code>. Otherwise, it returns an
         * error message.
         * </p>
         *
         * @param value <code>char</code> value to be validated.
         *
         * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(char value) {
            if (valid(value)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "not a " + (isDigit ? "digit" : "letter");
        }

        /**
         * <p>
         * Gives error information about the given object being validated. This method will return possibly a number of
         * messages produced for this object by a number of validators if the validator is composite. Since this is a
         * primitive validator the result will always be equivalent to getting a simple message but as an array.
         * </p>
         *
         * @param object the object to validate
         *
         * @return String[] a non-empty but possibly null array of failure messages
         *
         * @since 1.1
         */
        public String[] getMessages(Object object) {
            String message = getMessage(object);

            return (message == null) ? null : new String[] {message};
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
            return getMessages(object);
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

            return getMessages(object);
        }
    }
}
