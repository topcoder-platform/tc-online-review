/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.filter;

import com.topcoder.search.builder.ValidationResult;

import com.topcoder.util.datavalidator.ObjectValidator;

import java.util.Iterator;
import java.util.Map;


/**
 * <p>
 * This class is added in version 1.2,
 * this filter searches a textual field for a specified substring that is contained in it.
 * The substring can appear anywhere, including the beginning, the end and any other places.
 * The filter works for database and LDAP. And also it supports wildcard searching.
 * </p>
 *
 * <p>
 * Four types of value are supported, which are CONTAINS, START_WITH, END_WITH and WITH_CONTENT,
 * all of them are achieved by adding wildcard in proper position.
 * </p>
 *
 * <p>
 * It is thread safe since it is immutable,
 * value will not be changed during the life time of the instance
 * </p>
 *
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 */
public class LikeFilter implements Filter {
    /**
     * A public static final String member, denote the String 'SS:'.
     */
    public static final String CONTAIN_TAGS = "SS:";

    /**
     * A public static final String member, denote the String 'SW:'.
     */
    public static final String START_WITH_TAG = "SW:";

    /**
     * A public static final String member, denote the String 'EW:'.
     */
    public static final String END_WITH_TAG = "EW:";

    /**
     * A public static final String member, denote the String 'WC:'.
     */
    public static final String WITH_CONTENT = "WC:";
    /**
     * The default escape character for the LikeFilter.
     */
    private static final char DEFAULTESCAPECHAR = '\\';

    /**
     * The char array which contains the invalid escape Character.
     */
    private static final char[] INVALID_ESCAPE = {'%', '_', '*'};


    /**
     * <p>
     * Represents escape character to escape the wildcard in SQL ('%' and '_') and LDAP ( '*').
     * It is initialzed in constructor and never changed later.  It can be accessed by the getter.
     * Its default value is '\'.
     * <p>
     *
     * </p>
     * It cann't be some special chars, all the invalid chars are defined in INVALID_ESCAPE array.
     * </p>
     *
     */
    private final char escapeCharacter;

    /**
     *
     * It will hold the filed name of the search criterion,
     * which might be the real name, also might be the alias of the criterion.
     *
     */
    private final String fieldName;

    /**
     * It holds the type of the value(the prefix actually) and the real content.
     * The value should be started with 'SS:', 'SW:', 'EW:', 'WC:'.
     */
    private final String value;

    /**
     * <p>Create a new instance with the given name and value.</p>
     *
     *
     *
     * @param name the filed name of the search criterion
     * @param value the searching value
     * @throws IllegalArgumentException if any parameter is null or empty,
     * or value does not begin with 'SS:', 'SW:', 'EW:', 'WC:',
     * or value is zero length without the prefix
     */
    public LikeFilter(String name, String value) {
        this(name, value, DEFAULTESCAPECHAR);
    }

    /**
     * <p>Create a new instance with the given name, value and escape character.</p>
     *
     * <p>Note: the escape character can not be '%', '_' and '*'.</p>
     *
     * @param name the filed name of the search criterion
     * @param value the searching value
     * @param escapeCharacter the escape character
     * @throws IllegalArgumentException if any parameter is null or empty,
     * or value does not begin with 'SS:', 'SW:', 'EW:', 'WC:',
     * or value is zero length without the prefix,
     * or escapeCharacter is of some special invalid chars.
     */
    public LikeFilter(String name, String value, char escapeCharacter) {
        if (name == null) {
            throw new IllegalArgumentException("The name should not be null.");
        }

        if (name.trim().length() == 0) {
            throw new IllegalArgumentException("The name should not be empty.");
        }

        if (value == null) {
            throw new IllegalArgumentException("The value should not be null.");
        }

        //check the value should be start with one of the four patterns
        if (!(value.startsWith(CONTAIN_TAGS) || value.startsWith(START_WITH_TAG)
                || value.startsWith(END_WITH_TAG) || value.startsWith(WITH_CONTENT))) {
            throw new IllegalArgumentException(
                "The value should start with 'SS:', 'SW:', 'EW:' or 'WC:'.");
        }

        //the value with out prefix should with length > 0
        if (value.length() == CONTAIN_TAGS.length()) {
            throw new IllegalArgumentException(
                "The value without prefix should not be of zero length.");
        }

        for (int i = 0; i < INVALID_ESCAPE.length; i++) {
            if (INVALID_ESCAPE[i] == escapeCharacter) {
                throw new IllegalArgumentException("The escapeCharacter '"
                    + escapeCharacter + "' is invalid.");
            }
        }

        this.fieldName = name;
        this.value = value;
        this.escapeCharacter = escapeCharacter;
    }

    /**
     * <p>
     * Check whether the filter is valid according to the rules presented in the validators.
     * </p>
     *
     *
     * @param validators a map containing <code>ObjectValidator</code> Objects
     * @param alias a Map containing mapping between alias names and real names
     * @return the <code>ValidationResult</code> object denote whether the validation is successful or not
     * @throws IllegalArgumentException if any parameter is Null, or the key of any parameter is not String,
     * or the value of validators is not ObjectValidator, or the value of alias is not String,
     * or no rule can be found via the filed name of this filter.
     */
    public ValidationResult isValid(Map validators, Map alias) {
        checkMap(validators, ObjectValidator.class, "validators");
        checkMap(alias, String.class, "alias");

        //firstly, try to validator with real fieldName
        ObjectValidator rule = (ObjectValidator) validators.get(fieldName);

        if (rule == null) {
            //if no ObjectValidator can be retrieved via the real name, get the ObjectValidator via the alias
            rule = (ObjectValidator) validators.get(alias.get(fieldName));
        }

        if (rule == null) {
            throw new IllegalArgumentException(
                "No ObjectValidator can be retrieved via the fieldName '" + fieldName + "'.");
        }

        String message = rule.getMessage(value);

        //if validate failed, return InvalidResult
        if (message != null) {
            return ValidationResult.createInvalidResult("Validate failed for "
                + message + ".", this);
        }

        //validate successfully, return ValidResult
        return ValidationResult.createValidResult();
    }

    /**
     * <p>
     * Check the map which used to validate the LikeFilter.
     * </p>
     *
     * @param map the Map to be checked.
     * @param valueClass the expected class of the values in the map.
     * @param name the name of the map.
     * @throws IllegalArgumentException if the map is null, or keys are not all of String,
     * or values are not all with the expected class.
     */
    private void checkMap(Map map, Class valueClass, String name) {
        if (map == null) {
            throw new IllegalArgumentException("The " + name
                + " should not be null.");
        }

        for (Iterator it = map.entrySet().iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();

            //check the Key, it shold be a String
            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("The key of the map "
                    + name + " contains null_String.");
            }

            //check the value whether it is a proper Object
            if (!valueClass.isInstance(entry.getValue())) {
                throw new IllegalArgumentException("The value of the map "
                    + name + " contains invalid value.");
            }
        }
    }

    /**
     * <p>Get the type of the Filter.</p>
     *
     *
     *
     * @return a integer representing the type of the LikeFilter
     * @deprecated This method has been deprecated.
     */
    public final int getFilterType() {
        return Filter.LIKE_FILTER;
    }

    /**
     * <p>Get the escapeCharacter.</p>
     *
     *
     * @return the escapeCharacter in this LikeFilter
     */
    public char getEscapeCharacter() {
        return escapeCharacter;
    }

    /**
     * <p>Get the name of the field.</p>
     *
     *
     * @return the name of the LikeFilter
     */
    public String getName() {
        return fieldName;
    }

    /**
     * <p>Get the value of the filter.</p>
     *
     *
     *
     * @return the value of the LikeFilter
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Get a clone object of this filter.</p>
     *
     *
     * @return a clone of the object
     */
    public Object clone() {
        return new LikeFilter(fieldName, value, escapeCharacter);
    }
}
