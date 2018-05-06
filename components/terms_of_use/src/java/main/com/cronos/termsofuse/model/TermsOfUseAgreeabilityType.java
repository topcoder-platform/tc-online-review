/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.model;

import java.io.Serializable;

/**
 * <p>
 * This class defines simple entity of terms of use agreeability type. It provides model fields and getters/setters
 * for them.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The class is mutable and not thread - safe. The class is used in thread - safe
 * manner by this component. Any external application should access it in a thread - safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.1
 * @since 1.1
 */
public class TermsOfUseAgreeabilityType implements Serializable {
    /**
     * The serial version ID.
     */
    private static final long serialVersionUID = -6141010593127800719L;

    /**
     * <p>
     * The ID of the terms of use agreeability type.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private int termsOfUseAgreeabilityTypeId;

    /**
     * <p>
     * The name of the terms of use agreeability type.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private String name;

    /**
     * <p>
     * The description of the terms of use agreeability type.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     */
    private String description;

    /**
     * Creates an instance of TermsOfUseAgreeabilityType.
     */
    public TermsOfUseAgreeabilityType() {
        // Empty
    }

    /**
     * Retrieves the ID of the terms of use agreeability type.
     *
     * @return the ID of the terms of use agreeability type
     */
    public int getTermsOfUseAgreeabilityTypeId() {
        return termsOfUseAgreeabilityTypeId;
    }

    /**
     * Sets the ID of the terms of use agreeability type.
     *
     * @param termsOfUseAgreeabilityTypeId
     *            the ID of the terms of use agreeability type
     */
    public void setTermsOfUseAgreeabilityTypeId(int termsOfUseAgreeabilityTypeId) {
        this.termsOfUseAgreeabilityTypeId = termsOfUseAgreeabilityTypeId;
    }

    /**
     * Retrieves the name of the terms of use agreeability type.
     *
     * @return the name of the terms of use agreeability type
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the terms of use agreeability type.
     *
     * @param name
     *            the name of the terms of use agreeability type
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the description of the terms of use agreeability type.
     *
     * @return the description of the terms of use agreeability type
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the terms of use agreeability type.
     *
     * @param description
     *            the description of the terms of use agreeability type
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
