/*
 * TCS Data Validation
 *
 * CharacterValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple character validator abstraction which basically checks that
 * a given value conforms with a specific character definition.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific character value validation. Note that this
 * validator acts as a factory that allows for creation of other specialized
 * CharacterValidator instances that provide a number of convenience methods
 * which validate such aspects as if the character is a digit or a letter or
 * both, if it is a whitespace or if it is upper-case or lower-case.
 * User will need to implement the <code>valid(char value) </code> method to
 * decide what the validator will do with the input character.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class CharacterValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>CharacterValidator</code>.
     */
    public CharacterValidator() {
    }

    /**
     * Creates a new <code>CharacterValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public CharacterValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Checks whether the given <code>Object</code> is in fact a
     * <code>Character</code>, or a <code>String</code> representation of a
     * <code>char</code>. If so, it passes the <code>char</code> value
     * to <code>valid(char)</code>. Otherwise, it returns <code>false</code>.
     *
     * @param   obj <code>Object</code> to validate.
     * @return  <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
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
     * This validates the given primitive <code>char</code> value.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   value   <code>char</code> value to validate.
     * @return  <code>true</code> if <code>value</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(char value);

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are digits.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isDigit() {
        return new AlphanumCharacterValidator(true);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are letters.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLetter() {
        return new AlphanumCharacterValidator(false);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are letters or digits.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLetterOrDigit() {
        return new CharacterValidatorWrapper(new OrValidator(
                isLetter(), isDigit()));
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs upper case letters.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isUpperCase() {
        return new CaseCharacterValidator(true);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are lower case letters.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLowerCase() {
        return new CaseCharacterValidator(false);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are whitespace characters.
     *
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isWhitespace() {
        return new WhitespaceCharacterValidator();
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are digits and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isDigit(BundleInfo bundleInfo) {
        return new AlphanumCharacterValidator(true, bundleInfo);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are letters and initializes the validator with a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLetter(BundleInfo bundleInfo) {
        return new AlphanumCharacterValidator(false, bundleInfo);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are letters or digits and initializes the validator with a
     * specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLetterOrDigit(BundleInfo bundleInfo) {
        return new CharacterValidatorWrapper(new OrValidator(
                isLetter(bundleInfo), isDigit(bundleInfo)));
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs upper case letters and initializes the validator with a specific
     * resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isUpperCase(BundleInfo bundleInfo) {
        return new CaseCharacterValidator(true, bundleInfo);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are lower case letters and initializes the validator with a
     * specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isLowerCase(BundleInfo bundleInfo) {
        return new CaseCharacterValidator(false, bundleInfo);
    }

    /**
     * Creates a new <code>CharacterValidator</code> which would test whether
     * inputs are whitespace characters and initializes the validator with a
     * specific resource bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     * @return  a <code>CharacterValidator</code> that would perform the
     *      desired validation.
     */
    public static CharacterValidator isWhitespace(BundleInfo bundleInfo) {
        return new WhitespaceCharacterValidator(bundleInfo);
    }

    /**
     * If the given <code>Object</code> is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     * @param bundleInfo BundleInfo  resource bundle information
     * @param   obj  <code>Object</code> to be validated.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise an
     *      error message is returned.
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
     * Gives error information about the given primitive <code>char</code>
     * value. It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   value   <code>char</code> value to validate.
     * @return  <code>null</code> if <code>value</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(char value);

}
