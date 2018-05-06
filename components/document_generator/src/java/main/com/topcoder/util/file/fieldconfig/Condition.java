/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.fieldconfig;

import com.topcoder.util.file.Util;

/**
 * <p>
 * This is a data class that represents a conditional statement.
 * </p>
 *
 * <p>
 * It simply represents a condition that must be satisfied.
 * </p>
 *
 * <p>
 * Thread Safety: This class is not thread-safe. The name, description, readOnly
 * and conditionalStatement fields are immutable. However, the value may be changed
 * (if this is not made readOnly). It is recommended that for writing purposes,
 * multiple threads work on their own instances of the class.
 * </p>
 *
 * @author ShindouHikaru, biotrail
 * @version 2.1
 * @since 2.1
 */
public class Condition implements Node {
    /**
     * <p>
     * This is the conditional statement that is used to determine whether the template text
     * delimited by the %if% tag should be displayed or not.
     * </p>
     *
     * <p>
     * It is exposed in the API for informational purposes.
     * </p>
     *
     * <p>
     * It is set in the constructor and never changed afterward.
     * </p>
     *
     * <p>
     * It can be accessed by {@link #getConditionalStatement()} method.
     * </p>
     *
     * <p>
     * It will never be null or empty (untrimmed).
     * </p>
     */
    private final String conditionalStatement;

    /**
     * <p>
     * Represents the value of the <code>Condition</code>.
     * </p>
     *
     * <p>
     * It is set in the constructor and {@link #setValue(String)} and can be retrieved by
     * {@link #getValue()}.
     * </p>
     *
     * <p>
     * It will never be null but can be empty.
     * </p>
     */
    private String value;

    /**
     * <p>
     * Represents the name of the <code>Condition</code>.
     * </p>
     *
     * <p>
     * It is set in the constructor and never changed afterward.
     * </p>
     *
     * <p>
     * It can be accessed by {@link #getName()} method.
     * </p>
     *
     * <p>
     * It will never be null or empty (untrimmed).
     * </p>
     */
    private final String name;

    /**
     * <p>
     * Represents the description of the <code>Condition</code> (comments, sample data).
     * </p>
     *
     * <p>
     * It is set in the constructor and never changed afterward.
     * </p>
     *
     * <p>
     * It can be accessed by {@link #getDescription()} method.
     * </p>
     *
     * <p>
     * It is possibly null or empty (untrimmed).
     * </p>
     */
    private final String description;

    /**
     * <p>
     * Indicates whether the instance is read-only (the value cannot be
     * changed).
     * </p>
     *
     * <p>
     * It is set in the constructor and never changed afterward.
     * </p>
     *
     * <p>
     * It can be accessed by {@link #isReadOnly()} method.
     * </p>
     */
    private final boolean readOnly;

    /**
     * <p>
     * Represents the child nodes of this <code>Condition</code> node.
     * </p>
     *
     * <p>
     * It is set in the constructor and never changed afterward.
     * </p>
     *
     * <p>
     * It can be accessed by {@link #getSubNodes()} method.
     * </p>
     */
    private final NodeList subNodes;

    /**
     * <p>
     * Creates a new <code>Condition</code> instance with the given name,
     * default value, description, readOnly status and conditional Statement.
     * </p>
     *
     * @param name The name of the field.
     * @param subNodes The child nodes of this condition node.
     * @param description The description of the field.
     * @param readOnly Whether the field is readOnly or not.
     * @param conditionalStatement The conditional statement used to determine whether the template
     * text is displayed.
     *
     * @throws IllegalArgumentException if either name or conditionalStatement is <code>null</code> or
     * empty (untrimmed), or subNodes is null
     */
    public Condition(String name, NodeList subNodes, String description, boolean readOnly,
        String conditionalStatement) {
        Util.checkString(name, "name");
        Util.checkString(conditionalStatement, "conditionStatement");
        Util.checkNull(subNodes, "subNodes");

        this.name = name;
        this.subNodes = subNodes;
        this.value = "";
        this.description = description;
        this.conditionalStatement = conditionalStatement;
        this.readOnly = readOnly;
    }

    /**
     * <p>
     * Retrieves the conditional statement that is used to determine whether the template text
     * delimited by the %if% tag should be displayed or not.
     * </p>
     *
     * @return the conditional statement that is used.
     */
    public String getConditionalStatement() {
        return conditionalStatement;
    }

    /**
     * <p>
     * Retrieves the child nodes of this <code>Condition</code> node.
     * </p>
     *
     * @return the child nodes of this <code>Condition</code> node.
     */
    public NodeList getSubNodes() {
        return subNodes;
    }

    /**
     * <p>
     * Gets the name of the Conditional node.
     * </p>
     *
     * @return the name of the Conditional node.
     */
    public String getName() {
        return name;
    }

    /**
     * <p>
     * Sets the value of the Conditional node.
     * </p>
     *
     * @param value the new value to set
     *
     * @throws IllegalArgumentException if the argument is <code>null</code>
     * @throws IllegalStateException if the instance is read-only
     */
    public void setValue(String value) {
        Util.checkNull(value, "value");

        // Check whether object is read-only.
        if (readOnly) {
            throw new IllegalStateException("The object is read-only.");
        }

        // Set new value.
        this.value = value;
    }

    /**
     * <p>
     * Gets the value of the Conditional node.
     * </p>
     *
     * @return the value of the field
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>
     * Gets the description of the Conditional node.
     * </p>
     *
     * @return the description of the field
     */
    public String getDescription() {
        return description;
    }

    /**
     * <p>
     * This indicates whether the Conditional node is read-only or not.
     * </p>
     *
     * <p>
     * A read-only <code>Condition</code> cannot be modified
     * ({@link #setValue(String)}} will throw <code>IllegalStateException</code>).
     * </p>
     *
     * @return whether the <code>Condition</code> is read-only or not.
     */
    public boolean isReadOnly() {
        return readOnly;
    }
}
