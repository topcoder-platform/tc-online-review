/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

/**
 * <p>
 * The field class is part of the API for programmatically configuring field
 * values. It corresponds to a field in the template and exposes methods for
 * getting the name, a description (containing comments, sample data) and
 * getting/setting the value.
 * </p>
 *
 * <p>
 * Changed in Version 2.1 : {@link #isReadOnly()} method was added to conveniently determine
 * whether a <code>Field</code> is read only.
 * </p>
 *
 * <p>
 * Thread Safety: This class is not thread-safe.  The name, description, and readOnly
 * fields are immutable. However, the value may be changed (if this is not made readOnly).
 * It is recommended that for writing purposes, multiple threads work on their own instances
 * of the class.
 * </p>
 *
 * @author adic, roma
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.0
 */
public class Field implements Node {
    /**
     * <p>
     * The name of the field.
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: getName
     * </p>
     * <p>
     * Modified In: Not modified
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Non-Null, non-empty Strings
     * </p>
     *
     * @since 2.0
     */
    private final String name;

    /**
     * <p>
     * The value of the field.
     * </p>
     * <p>
     * Initialized In: Constructor via default value,  or in setValue method
     * </p>
     * <p>
     * Accessed In: getValue
     * </p>
     * <p>
     * Modified In: setValue
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Possibly null (if not initialized),  possibly empty Strings
     * </p>
     *
     * @since 2.0
     */
    private String value;

    /**
     * <p>
     * The description of the field (comments, sample data).
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: getDescription
     * </p>
     * <p>
     * Modified In: Not modified
     * </p>
     * <p>
     * Utilized In: Not utilized in this class
     * </p>
     * <p>
     * Valid Values: Possibly Null, posisbly empty Strings
     * </p>
     *
     * @since 2.0
     */
    private final String description;

    /**
     * <p>
     * Indicates whether the instance is readonly (the value cannot be
     * changed).
     * </p>
     * <p>
     * Initialized In: Constructor
     * </p>
     * <p>
     * Accessed In: isReadOnly
     * </p>
     * <p>
     * Modified In: Not modified
     * </p>
     * <p>
     * Utilized In: Set value
     * </p>
     * <p>
     * Valid Values: true or false
     * </p>
     *
     * @since 2.0
     */
    private final boolean readOnly;

    /**
     * <p>
     * Constructor.
     * </p>
     *
     * @param name the name of the field
     * @param defaultValue the default value of the field (can be
     * <code>null</code>)
     * @param description the description of the field (can be
     * <code>null</code>)
     * @param readOnly is the field read-only or not
     *
     * @throws IllegalArgumentException if name is <code>null</code> or empty
     *
     * @since 2.0
     */
    public Field(String name, String defaultValue, String description, boolean readOnly) {
        // Check for null and empty string for name.
        if ((name == null) || (name.length() == 0)) {
            throw new IllegalArgumentException("The name is null or empty.");
        }

        // Set up properties.
        this.name = name;
        this.value = defaultValue;
        this.description = description;
        this.readOnly = readOnly;
    }

    /**
     * Gets the name of the field.
     *
     * @return the name of the field
     *
     * @since 2.0
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the field.
     *
     * @param value the new value <ul><li>Valid args: non-null argument when for non read-only instance</li>
     * <li>Invalid args: <code>null</code> argument or read-only instance</li> </ul>
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws IllegalStateException if the instance is readonly
     *
     * @since 2.0
     */
    public void setValue(String value) {
        // Check for null first of all.
        if (value == null) {
            throw new IllegalArgumentException("The value is null.");
        }
        // Check whether object is read-only.
        if (readOnly) {
            throw new IllegalStateException("The object is read-only.");
        }
        // Set new value.
        this.value = value;
    }

    /**
     * Gets the value of the field.
     *
     * @return the value of the field
     *
     * @since 2.0
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the description of the field.
     *
     * @return the description of the field
     *
     * @since 2.0
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * This indicates whether the field is read-only or not.
     * </p>
     *
     * <p>
     * A read-only <code>Field</code> cannot be modified
     * ({@link #setValue(String)}} will throw <code>IllegalStateException</code>).
     * </p>
     *
     * @return whether the <code>Field</code> is read-only or not.
     *
     * @since 2.1
     */
    public boolean isReadOnly() {
        return readOnly;
    }
}
