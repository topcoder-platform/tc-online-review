/*
 * TCS LDAP SDK Interface 1.0
 *
 * Values.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

import java.util.*;

/**
 * <p>This class is used to store the values of an Entry.<br />The values can be text, stored in String, or binary,
 * stored in byte [].<br />There are zero or more text values and zero or more binary values </p>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class Values {

    /**
     * List of the text values contained by the object</p>
     * <p>This variable must be instantiated in the constructor and may be populated there</p>
     * <p>Then, the methods in this class will manipulate the collection for setting,adding, deleting and retrieving
     * data.</p>
     */
    private List textValues = null; // of type String

    /**
     * List of the binary values contained by the object</p>
     * <p>This variable must be instantiated in the constructor and may be populated there</p>
     * <p>Then, the methods in this class will manipulate the collection for setting,adding, deleting and retrieving data
     */
    private List binaryValues = null; // of type byte[]


    /**
     * Create an instance without values.
     */
    public Values() {
        textValues = new ArrayList();
        binaryValues = new ArrayList();
    }

    /**
     * Create an instance with one Text value </p>
     *
     * @param  value the text value.
     * @throws NullPointerException if value is null. Empty strings are accepted.
     */
    public Values(String value) {
        this();
        add(value);
    }

    /**
     * Create an instance with many Text values </p>
     *
     * @param  values the text values
     * @throws NullPointerException if values is null or contains at least one null argument.Empty strings are accepted.
     */
    public Values(String[] values) {
        this();
        add(values);
    }

    /**
     * Create an instance with one Binary value </p>
     *
     * @param  value the binary value
     * @throws NullPointerException if value is null. Empty arrays are accepted.
     */
    public Values(byte[] value) {
        this();
        add(value);
    }

    /**
     * Create an instance with many Binary values </p>
     *
     * @param  values the binary values
     * @throws NullPointerException if values is null or contain at least one null element. Empty arrays are accepted.
     */
    public Values(byte[][] values) {
        this();
        add(values);
    }

    /**
     * Adds one text value to the object.</p>
     *
     * @param  value the text value.
     * @throws NullPointerException if value is null. Empty strings are accepted.
     */
    public void add(String value) {
        checkValues(value);
        textValues.add(value);
    }

    /**
     * Adds many text value to the object </p>
     *
     * @param values the text values
     * @throws IllegalArgumentException if specified array contains at least one null element.
     * @throws NullPointerException if values is null. Empty strings are accepted.
     *
     */
    public void add(String[] values) {
        checkValue(values);
        for (int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    /**
     * Adds one binary value to the object.
     *
     * @param  value the binary value.
     * @throws NullPointerException if value is null. Empty arrays are accepted.
     */
    public void add(byte[] value) {
        checkValues(value);
        binaryValues.add(value);
    }

    /**
     * Adds many binary values to the object.</p>
     *
     * @param  values the binary values.
     * @throws NullPointerException if values is null. Empty arrays are accepted.
     *
     */
    public void add(byte[][] values) {
        checkValues(values);
        for(int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    /**
     * Delete a text value from the object.</p>
     *
     * @param  value the text value to be deleted.
     * @return true if the value has been found and deleted, false if the value has not been found.
     * @throws NullPointerException if value is null. Empty strings are accepted.
     */
    public boolean delete(String value) {
        checkValues(value);
        return textValues.remove(value);
    }

    /**
     * Delete a binary value from the object. </p>
     *
     * @param value the binary value to be deleted.
     * @return true if the value has been found and deleted, false if the value has not been found.
     * @throws NullPointerException if value is null. Empty arrays are accepted.
     */
    public boolean delete(byte[] value) {
        checkValues(value);
        boolean removed = false;
        byte[] element = null;

        for(Iterator iterator = binaryValues.iterator(); iterator.hasNext();) {
            element = (byte[]) iterator.next();
            if(Arrays.equals(element, value)) {
                removed = true;
                iterator.remove();
            }
        }

        return removed;
    }

    /**
     * Obtain all the text values
     *
     *
     * @return a List of String containing all the text values
     */
    public List getTextValues() {
        return new ArrayList(textValues);
    }

    /**
     * Obtain all the binary values
     *
     * @return a List of byte[] containing all the binary values
     */
    public List getBinaryValues() {
        return new ArrayList(binaryValues);
    }

    /**
     * Set all the text values, deleting previous text values </p>
     * @param  values the text values
     * @throws NullPointerException if values is null. Empty strings are accepted.
     */
    public void setTextValues(String[] values) {
        checkValue(values);
        textValues.clear();
        for(int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    /**
     * Set all the binary values, deleting previous binary values.
     *
     * @param  values the binary values.
     * @throws NullPointerException if values is null. Empty arrays are accepted.
     */
    public void setBinaryValues(byte[][] values) {
        checkValues(values);
        binaryValues.clear();
        for(int i = 0; i < values.length; i++) {
            add(values[i]);
        }
    }

    /**
     * Clear both the binary and text values.
     */
    public void clear() {
        textValues.clear();
        binaryValues.clear();
    }

    /**
     * A helper "guard" method rejecting the invalid binary values throwing the appropriate runtime exception (namely
     * <code>NulPointerException</code>).
     *
     * @param  value a <code>byte</code> array with binary values to check.
     * @throws NullPointerException if specified array is <code>null</code>.
     */
    private void checkValues(byte[] value) {
        if (value == null) {
            throw new NullPointerException("Null array of binary values is prohibited.");
        }
    }

    /**
     * A helper "guard" method rejecting the invalid binary values throwing the appropriate runtime exception (namely
     * <code>NulPointerException</code>).
     *
     * @param  values a <code>byte</code> array with binary values to check.
     * @throws NullPointerException if specified array is <code>null</code> or contains at least one <code>null</code>
     *         element.
     */
    private void checkValues(byte[][] values) {
        if (values == null) {
            throw new NullPointerException("Null array of binary values is prohibited.");
        }

        for (int i = 0; i < values.length; i++) {
            checkValues(values[i]);
        }
    }

    /**
     * A helper "guard" method rejecting the invalid text values throwing the appropriate runtime exception (namely
     * <code>NulPointerException</code>).
     *
     * @param  value a <code>String</code> representing the text values to check.
     * @throws NullPointerException if specified argument is <code>null</code>.
     */
    private void checkValues(String value) {
        if (value == null) {
            throw new NullPointerException("Null text value is prohibited.");
        }
    }

    /**
     * A helper "guard" method rejecting the invalid text values throwing the appropriate runtime exception (namely
     * <code>NulPointerException</code>).
     *
     * @param  values a <code>String</code> array representing the text values to check.
     * @throws NullPointerException if specified array is <code>null</code> or contains at least one <code>null</code>
     *         element.
     */
    private void checkValue(String[] values) {
        if (values == null) {
            throw new NullPointerException("Null array of text values is prohibited.");
        }

        for (int i = 0; i < values.length; i++) {
            checkValues(values[i]);
        }
    }
}
