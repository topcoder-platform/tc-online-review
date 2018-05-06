/*
 * TCS Data Validation
 *
 * StringValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

/**
 * This is a simple String validator abstraction which basically checks that
 * a given value conforms with a specific string definition.
 * This validator is abstract and will need to be extended to actually provide the
 * validation routine for a specific string value validation. Note that this
 * validator comes with a number of convenience factory methods which create
 * specific valiators which can tests/validate such aspects as if a string
 * starts with or end with some string. We also have the ability to create
 * validators that will test for string containment (i.e. a string is valid if
 * it contains a certain substring) or validators that test for length based
 * comparisins such as exact length or a range. We also have the ability to
 * create validators that test an input string against  specific regular
 * expression.
 * User will need to implement the <code>valid(String value) </code> method to
 * decide what the validator will do with the input String.
 * This is thread-safe as the implementation is immutable.
 * @version 1.1
 */
public abstract class StringValidator extends AbstractObjectValidator {

    /**
     * Creates a new <code>StringValidator</code>.
     */
    public StringValidator() {
    }

    /**
     * Creates a new <code>StringValidator</code> wuith a specific resource
     * bundle information.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public StringValidator(BundleInfo bundleInfo) {
      super(bundleInfo);
    }


    /**
     * Converts the given <code>Object</code> to a <code>String</code> by
     * calling its <code>toString()</code> method, and passes the
     * <code>String</code> to <code>valid(String)</code>.
     *
     * @param  obj <code>Object</code> to be validated.
     * @return <code>true</code> if <code>obj</code> is valid;
     *      <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        if (!(obj instanceof String)) {
            return false;
        }

        return valid((String) obj);
    }

    /**
     * This validates the given <code>String</code>.
     * It is an <code>abstract</code> method, so it should be overridden in
     * subclasses to implement the specific validation routines.
     *
     * @param   str <code>String</code> value to validate.
     * @return  <code>true</code> if <code>str</code> is valid;
     *      <code>false</code> otherwise.
     */
    public abstract boolean valid(String str);

    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs start with <code>str</code>.
     *
     * @param   str check that inputs start with this <code>String</code>.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>str</code> is
     *      <code>null</code>.
     */
    public static StringValidator startsWith(String str)
            throws IllegalArgumentException {
        return new StartsWithStringValidator(str);
    }

    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs start with <code>str</code> using a specified resource bundle
     * information.
     *
     * @param   str check that inputs start with this <code>String</code>.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>str</code> is
     *      <code>null</code>.
     * @param bundleInfo BundleInfo  resource bundle information
     */
    public static StringValidator startsWith(String str, BundleInfo bundleInfo )
        throws IllegalArgumentException {
      return new StartsWithStringValidator(str, bundleInfo);
    }


    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs conforms to the specified <code>regexpStr</code>.
     *
     * @param   str check that inputs start with this <code>String</code>.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>regexpStr</code> is
     *      <code>null</code> or an empty string.
     */
    public static StringValidator matchesRegexp(String regexpStr)
            throws IllegalArgumentException {
        return new RegexpStringValidator(regexpStr);
    }


    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs end with <code>str</code>.
     *
     * @param   str check that inputs end with this <code>String</code>.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>str</code> is
     *      <code>null</code>.
     */
    public static StringValidator endsWith(String str)
            throws IllegalArgumentException {
        return new EndsWithStringValidator(str);
    }

    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs contain <code>str</code> as a substring.
     *
     * @param   str check that inputs contain this <code>String</code>.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>str</code> is
     *      <code>null</code>.
     */
    public static StringValidator containsSubstring(String str)
            throws IllegalArgumentException {
        return new ContainsStringValidator(str);
    }

    /**
     * Creates a new <code>StringValidator</code> which would test whether
     * inputs have a length that the given <code>IntergerValidator</code>
     * accepts as valid.
     *
     * @param   validator   <code>IntegerValidator</code> that will be used to
     *      validate the length of inputs.
     * @return  a <code>StringValidator</code> that would perform the
     *      desired validation.
     * @throws  IllegalArgumentException    if <code>validator</code> is
     *      <code>null</code>.
     */
    public static StringValidator hasLength(IntegerValidator validator)
            throws IllegalArgumentException {
        return new LengthStringValidator(validator);
    }

    /**
     * If the given <code>Object</code> is valid, this returns
     * <code>null</code>. Otherwise, it returns an error message.
     *
     * @param   obj  <code>Object</code> to be validated.
     * @return  <code>null</code> if <code>obj</code> is valid. Otherwise an
     *      error message is returned.
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
     * Gives error information about the given <code>String</code>.
     * It is an <code>abstract</code> method, so it should be
     * overridden in subclasses to implement the specific validation routines.
     *
     * @param   str <code>String</code> to validate.
     * @return  <code>null</code> if <code>str</code> is valid. Otherwise,
     *      an error message is returned.
     */
    public abstract String getMessage(String str);



}
