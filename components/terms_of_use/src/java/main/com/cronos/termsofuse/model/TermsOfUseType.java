/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.model;

import java.io.Serializable;

/**
 * <p>
 * This class defines simple entity of terms of use type. It provides model fields and getters/setters for them.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The class is mutable and not thread safe. The class is used in thread safe manner
 * by this component. Any external application should access it in a thread safe manner.
 * </p>
 *
 * @author faeton, sparemax
 * @version 1.0
 */
public class TermsOfUseType implements Serializable {
    /**
     * The serial version ID.
     */
    private static final long serialVersionUID = 7032175398678755047L;

    /**
     * <p>
     * The terms of use type id.
     * </p>
     *
     * <p>
     * It is accessed by getter and modified by setter. The default value is not set. The legal value is any value.
     * </p>
     */
    private int termsOfUseTypeId;

    /**
     * <p>
     * The description of terms of use type.
     * </p>
     *
     * <p>
     * It is accessed by getter and modified by setter. The default value is null. The legal value is any value.
     * </p>
     */
    private String description;

    /**
     * Creates an instance of TermsOfUseType.
     */
    public TermsOfUseType() {
        // Empty
    }

    /**
     * Gets the terms of use type id.
     *
     * @return the terms of use type id.
     */
    public int getTermsOfUseTypeId() {
        return termsOfUseTypeId;
    }

    /**
     * Sets the terms of use type id.
     *
     * @param termsOfUseTypeId
     *            the terms of use type id.
     */
    public void setTermsOfUseTypeId(int termsOfUseTypeId) {
        this.termsOfUseTypeId = termsOfUseTypeId;
    }

    /**
     * Gets the description of terms of use type.
     *
     * @return the description of terms of use type.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of terms of use type.
     *
     * @param description
     *            the description of terms of use type.
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
