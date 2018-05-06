/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.model;

import java.io.Serializable;

/**
 * <p>
 * This class defines simple entity of terms of use. It provides model fields and getters/setters for them.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Removed properties memberAgreeable and electronicallySignable.</li>
 * <li>Added agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The class is mutable and not thread safe. The class is used in thread safe manner
 * by this component. Any external application should access it in a thread safe manner.
 * </p>
 *
 * @author faeton, sparemax, saarixx
 * @version 1.1
 */
public class TermsOfUse implements Serializable {
    /**
     * The serial version ID.
     */
    private static final long serialVersionUID = 8822693298224104218L;

    /**
     * <p>
     * The terms of use id.
     * </p>
     *
     * <p>
     * It is accessed by getter and modified by setter. The default value is not set. The legal value is any value.
     * </p>
     */
    private long termsOfUseId;

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
     * The title of terms.
     * </p>
     *
     * <p>
     * It is accessed by getter and modified by setter. The default value is null. The legal value is any value.
     * </p>
     */
    private String title;

    /**
     * <p>
     * The url of terms.
     * </p>
     *
     * <p>
     * It is accessed by getter and modified by setter. The default value is null. The legal value is any value.
     * </p>
     */
    private String url;

    /**
     * <p>
     * The terms of use agreeability type.
     * </p>
     *
     * <p>
     * Can be any value. Has getter and setter.
     * </p>
     *
     * @since 1.1
     */
    private TermsOfUseAgreeabilityType agreeabilityType;

    /**
     * Creates an instance of TermsOfUse.
     */
    public TermsOfUse() {
        // Empty
    }

    /**
     * Gets the terms of use id.
     *
     * @return the terms of use id.
     */
    public long getTermsOfUseId() {
        return termsOfUseId;
    }

    /**
     * Sets the terms of use id.
     *
     * @param termsOfUseId
     *            the terms of use id.
     */
    public void setTermsOfUseId(long termsOfUseId) {
        this.termsOfUseId = termsOfUseId;
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
     * Gets the title of terms.
     *
     * @return the title of terms.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of terms.
     *
     * @param title
     *            the title of terms.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the url of terms.
     *
     * @return the url of terms.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the url of terms.
     *
     * @param url
     *            the url of terms.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Retrieves the terms of use agreeability type.
     *
     * @return the terms of use agreeability type
     *
     * @since 1.1
     */
    public TermsOfUseAgreeabilityType getAgreeabilityType() {
        return agreeabilityType;
    }

    /**
     * Sets the terms of use agreeability type.
     *
     * @param agreeabilityType
     *            the terms of use agreeability type
     *
     * @since 1.1
     */
    public void setAgreeabilityType(TermsOfUseAgreeabilityType agreeabilityType) {
        this.agreeabilityType = agreeabilityType;
    }
}
