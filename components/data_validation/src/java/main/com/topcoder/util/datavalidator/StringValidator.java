/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * <p>
 * This is a simple String validator abstraction which basically checks that a given value conforms with a specific
 * string definition.
 * </p>
 *
 * <p>
 * This validator is abstract and will need to be extended to actually provide the validation routine for a specific
 * string value validation. Note that this validator comes with a number of convenience factory methods which create
 * specific validators which can tests/validate such aspects as if a string starts with or end with some string. We
 * also have the ability to create validators that will test for string containment (i.e. a string is valid if it
 * contains a certain substring) or validators that test for length based comparisons such as exact length or a range.
 * We also have the ability to create validators that test an input string against  specific regular expression. User
 * will need to implement the <code>valid(String value) </code> method to decide what the validator will do with the
 * input String.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class StringValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>StringValidator</code>.
     * </p>
     */
    public StringValidator() {
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public StringValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Converts the given <code>Object</code> to a <code>String</code> by calling its <code>toString()</code> method,
     * and passes the <code>String</code> to <code>valid(String)</code>.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }

        return valid((String) obj);
    }

    /**
     * <p>
     * This validates the given <code>String</code>. It is an <code>abstract</code> method, so it should be overridden
     * in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param str <code>String</code> value to validate.
     *
     * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
     */
    public abstract boolean valid(String str);

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs start with <code>str</code>.
     * </p>
     *
     * @param str check that inputs start with this <code>String</code>.
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator startsWith(String str) {
        return new StartsWithStringValidator(str);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs start with <code>str</code> using a
     * specified resource bundle information.
     * </p>
     *
     * @param str check that inputs start with this <code>String</code>.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator startsWith(String str, BundleInfo bundleInfo) {
        return new StartsWithStringValidator(str, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs conforms to the specified
     * <code>regexpStr</code>.
     * </p>
     *
     * @param regexpStr check that inputs start with this<code>String</code>.
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>regexpStr</code> is <code>null</code> or an empty string.
     */
    public static StringValidator matchesRegexp(String regexpStr) {
        return new RegexpStringValidator(regexpStr);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs conforms to the specified
     * <code>regexpStr</code> using a specified resource bundle information.
     * </p>
     *
     * @param regexpStr check that inputs start with this<code>String</code>.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>regexpStr</code> is <code>null</code> or an empty string.
     */
    public static StringValidator matchesRegexp(String regexpStr, BundleInfo bundleInfo) {
        return new RegexpStringValidator(regexpStr, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs end with <code>str</code>.
     * </p>
     *
     * @param str check that inputs end with this <code>String</code>.
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator endsWith(String str) {
        return new EndsWithStringValidator(str);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs end with <code>str</code> using a
     * specified resource bundle information.
     * </p>
     *
     * @param str check that inputs end with this <code>String</code>.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator endsWith(String str, BundleInfo bundleInfo) {
        return new EndsWithStringValidator(str, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs contain <code>str</code> as a
     * substring.
     * </p>
     *
     * @param str check that inputs contain this <code>String</code>.
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator containsSubstring(String str) {
        return new ContainsStringValidator(str);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs contain <code>str</code> as a
     * substring using a specified resource bundle information.
     * </p>
     *
     * @param str check that inputs contain this <code>String</code>.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
     */
    public static StringValidator containsSubstring(String str, BundleInfo bundleInfo) {
        return new ContainsStringValidator(str, bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs have a length that the given
     * <code>IntergerValidator</code> accepts as valid.
     * </p>
     *
     * @param validator <code>IntegerValidator</code> that will be used to validate the length of inputs.
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public static StringValidator hasLength(IntegerValidator validator) {
        return new LengthStringValidator(validator);
    }

    /**
     * <p>
     * Creates a new <code>StringValidator</code> which would test whether inputs have a length that the given
     * <code>IntergerValidator</code> accepts as valid using a specified resource bundle information.
     * </p>
     *
     * @param validator <code>IntegerValidator</code> that will be used to validate the length of inputs.
     * @param bundleInfo resource bundle information
     *
     * @return a <code>StringValidator</code> that would perform the desired validation.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public static StringValidator hasLength(IntegerValidator validator, BundleInfo bundleInfo) {
        return new LengthStringValidator(validator, bundleInfo);
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
        if (obj == null) {
            return "object is null";
        }

        if (!(obj instanceof String)) {
            return "not instance of " + String.class.getName();
        }

        return getMessage((String) obj);
    }

    /**
     * <p>
     * Gives error information about the given <code>String</code>. It is an <code>abstract</code> method, so it should
     * be overridden in subclasses to implement the specific validation routines.
     * </p>
     *
     * @param str <code>String</code> to validate.
     *
     * @return <code>null</code> if <code>str</code> is valid. Otherwise, an error message is returned.
     */
    public abstract String getMessage(String str);

    /**
     * <p>
     * This is an extension of the StringValidator which specifically deals with validating strings based on testing
     * that strings start with a specific string. As an example, if we create an instance of this validator set with a
     * string of "hello" then any user input that would start with this string (case-insensitive) would be considered
     * valid. This is an inner class to StringValidator. This class is thread-safe as it is immutable.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class StartsWithStringValidator extends StringValidator {
        /**
         * <p>
         * Inputs must start with this string. This is initialized in the constructor and is immutable once set. it
         * cannot be null but could be an empty string
         * </p>
         */
        private final String str;

        /**
         * <p>
         * Creates a new <code>StartsWithStringValidator</code> that checks whether inputs start with <code>str</code>.
         * </p>
         *
         * @param str inputs must start with this.
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public StartsWithStringValidator(String str) {
            Helper.checkNull(str, "str");

            this.str = str;
        }

        /**
         * <p>
         * Creates a new <code>StartsWithStringValidator</code> that checks whether inputs start with <code>str</code>
         * with a specific resource bundle information.
         * </p>
         *
         * @param str inputs must start with this.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public StartsWithStringValidator(String str, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkNull(str, "str");

            this.str = str;
        }

        /**
         * <p>
         * Validates the given <code>String</code>.
         * </p>
         *
         * @param str <code>String</code> to be validated.
         *
         * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(String str) {
            return (str == null) ? false : str.startsWith(this.str);
        }

        /**
         * <p>
         * If the given <code>String</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param str <code>String</code> value to be validated.
         *
         * @return <code>null</code> if <code>str</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(String str) {
            if (valid(str)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "does not start with \"" + this.str + "\"";
        }
    }

    /**
     * <p>
     * This is an extension of the StringValidator which specifically deals with validating strings based on testing
     * that strings end with a specific string. As an example, if we create an instance of this validator set with a
     * string of "world" then any user input that would end with this string (case-insensitive) would be considered
     * valid. This is an inner class to StringValidator. This class is thread-safe as it is immutable.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class EndsWithStringValidator extends StringValidator {
        /**
         * <p>
         * Inputs must end with this string. This is initialized in the constructor and is immutable once set. it
         * cannot be null but could be an empty string
         * </p>
         */
        private final String str;

        /**
         * <p>
         * Creates a new <code>EndsWithStringValidator</code> that checks whether inputs end with <code>str</code>.
         * </p>
         *
         * @param str inputs must end with this.
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public EndsWithStringValidator(String str) {
            Helper.checkNull(str, "str");
            this.str = str;
        }

        /**
         * <p>
         * Creates a new <code>EndsWithStringValidator</code> that checks whether inputs start with <code>str</code>
         * with a specific resource bundle information.
         * </p>
         *
         * @param str inputs must start with this.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public EndsWithStringValidator(String str, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkNull(str, "str");

            this.str = str;
        }

        /**
         * <p>
         * Validates the given <code>String</code>.
         * </p>
         *
         * @param str <code>String</code> to be validated.
         *
         * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(String str) {
            return str.endsWith(this.str);
        }

        /**
         * <p>
         * If the given <code>String</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param str <code>String</code> value to be validated.
         *
         * @return <code>null</code> if <code>str</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(String str) {
            if (valid(str)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "does not end with \"" + this.str + "\"";
        }
    }

    /**
     * <p>
     * This is an extension of the StringValidator which specifically deals with validating strings based on substring
     * containment. This means that we will be validatng user input as bing valid it is contains the string that this
     * validator has been initialized with. As an example, if we create an instance of this validator set with a
     * string of "hello" then any user input that would contain this string (case-insensitive) would be considered
     * valid. This is an inner class to StringValidator. This class is thread-safe as it is immutable.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class ContainsStringValidator extends StringValidator {
        /**
         * <p>
         * Inputs must contain this as a substring. This is initialized in the constructor and is immutable once set.
         * it cannot be null but could be an empty string
         * </p>
         */
        private final String str;

        /**
         * <p>
         * Creates a new <code>ContainsStringValidator</code> that checks whether inputs contain <code>str</code>.
         * </p>
         *
         * @param str inputs must contain this.
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public ContainsStringValidator(String str) {
            Helper.checkNull(str, "str");

            this.str = str;
        }

        /**
         * <p>
         * Creates a new <code>ContainsStringValidator</code> that checks whether inputs contain <code>str</code> and
         * initializes the validator with a specific resource bundle information.
         * </p>
         *
         * @param str inputs must contain this.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code>.
         */
        public ContainsStringValidator(String str, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkNull(str, "str");

            this.str = str;
        }

        /**
         * <p>
         * Validates the given <code>String</code>.
         * </p>
         *
         * @param str <code>String</code> to be validated.
         *
         * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(String str) {
            return str.indexOf(this.str) >= 0;
        }

        /**
         * <p>
         * If the given <code>String</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param str <code>String</code> value to be validated.
         *
         * @return <code>null</code> if <code>str</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(String str) {
            if (valid(str)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "does not contain \"" + this.str + "\"";
        }
    }

    /**
     * <p>
     * This is an extension of the StringValidator which specifically deals with validating strings based on their
     * length. note that the actual validation is performed based on an IntegerValidator which means that we could
     * have this validator do all the integer validations including range validation of the length. As an example, if
     * we create an instance of this validator set with an IntegerValidator which checks that a number is in range of
     * 2..10 inclusive then any user input that would have its length in that range would be considered valid. This is
     * an inner class to StringValidator.
     * </p>
     *
     * <p>
     * <b>Thread Safety:</b>This is thread-safe as the implementation is immutable.
     * </p>
     *
     * @author WishingBone, zimmy, AleaActaEst, telly12
     * @version 1.1
     */
    static class LengthStringValidator extends StringValidator {
        /**
         * <p>
         * The underlying <code>IntegerValidator</code> to use. This is the validator initilization value which is set
         * in the constructor and once set is immutable. This cannot be null and represents the validator doing the
         * actual validation of the length.
         * </p>
         */
        private final IntegerValidator validator;

        /**
         * <p>
         * Creates a new <code>LengthStringValidator</code> that uses the given <code>IntegerValidator</code> to check
         * whether the length of inputs are valid.
         * </p>
         *
         * @param validator the underlying <code>IntegerValidator</code> to use.
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public LengthStringValidator(IntegerValidator validator) {
            Helper.checkNull(validator, "validator");

            this.validator = validator;
        }

        /**
         * <p>
         * Creates a new <code>LengthStringValidator</code> that uses the given <code>IntegerValidator</code> to check
         * whether the length of inputs are valid and initializes the validator with a specific resource bundle
         * information.
         * </p>
         *
         * @param validator the underlying <code>IntegerValidator</code> to use.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
         */
        public LengthStringValidator(IntegerValidator validator, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkNull(validator, "validator");
            this.validator = validator;
        }

        /**
         * <p>
         * Validates the given <code>String</code>.
         * </p>
         *
         * @param str <code>String</code> to be validated.
         *
         * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(String str) {
            return validator.valid(str.length());
        }

        /**
         * <p>
         * If the given <code>String</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
         * message.
         * </p>
         *
         * @param str <code>String</code> value to be validated.
         *
         * @return <code>null</code> if <code>str</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(String str) {
            String msg = validator.getMessage(str.length());

            if (msg == null) {
                return null;
            }

            return "string length " + msg;
        }
    }

    /**
     * <p>
     * This is an extension of the <code>StringValidator</code> which specifically deals with validating strings based
     * on a regular expression. This is an inner class to <code>StringValidator</code>. This class is thread-safe as
     * it is immutable.
     * </p>
     *
     * @author AleaActaEst, telly12
     * @version 1.1
     */
    static class RegexpStringValidator extends StringValidator {
        /**
         * <p>
         * Inputs must conform to this regexp string. This is initialized in the constructor and is used to initialize
         * the regexp pattern (below) It cannot be null and cannot be an empty string.
         * </p>
         */
        private final String regexpStr;

        /**
         * <p>
         * The regular expression as a pattern. This is initialized in the constructor. It cannot be null. It is
         * initialized from the regexpStr that the user inputs.
         * </p>
         */
        private final Pattern regexpPattern;

        /**
         * <p>
         * Creates a new <code>StartsWithStringValidator</code> that checks whether inputs start with <code>str</code>.
         * </p>
         *
         * @param regexpStr inputs must conform to this regexp.
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code> or is an empty string. It will
         *         also be thrown if there are issues with creating the regular expression pattern.
         */
        public RegexpStringValidator(String regexpStr) {
            Helper.checkString(regexpStr, "regexpStr");

            this.regexpStr = regexpStr;
            this.regexpPattern = Pattern.compile(regexpStr);
        }

        /**
         * <p>
         * Creates a new <code>StartsWithStringValidator</code> that checks whether inputs start with <code>str</code>
         * with a specific resource bundle information.
         * </p>
         *
         * @param regexpStr inputs must conform to this regexp.
         * @param bundleInfo resource bundle information
         *
         * @throws IllegalArgumentException if <code>str</code> is <code>null</code> or is an empty string. It will
         *         also be thrown if there are issues with creating the regular expression pattern.
         */
        public RegexpStringValidator(String regexpStr, BundleInfo bundleInfo) {
            super(bundleInfo);

            Helper.checkString(regexpStr, "regexpStr");
            this.regexpStr = regexpStr;

            this.regexpPattern = Pattern.compile(regexpStr);
        }

        /**
         * <p>
         * Validates the given <code>String</code> to see if it matches the regular expression that this validator has
         * been initialized with.
         * </p>
         *
         * @param str <code>String</code> to be validated.
         *
         * @return <code>true</code> if <code>str</code> is valid; <code>false</code> otherwise.
         */
        public boolean valid(String str) {
            Matcher matcher = regexpPattern.matcher(str);

            return matcher.matches();
        }

        /**
         * <p>
         * If the given <code>String</code> is valid , this returns <code>null</code>. Otherwise, it returns an error
         * message. To test validity this method will test to see if it matches the regular expression that this
         * validator has been initialized with.
         * </p>
         *
         * @param str <code>String</code> value to be validated.
         *
         * @return <code>null</code> if <code>str</code> is valid. Otherwise an error message is returned.
         */
        public String getMessage(String str) {
            if (valid(str)) {
                return null;
            }

            // get the message from resource bundle if it exists.
            String message = this.getValidationMessage();

            if (message != null) {
                return message;
            }

            return "does not match the following regular expression \"" + this.regexpStr + "\"";
        }
    }
}
