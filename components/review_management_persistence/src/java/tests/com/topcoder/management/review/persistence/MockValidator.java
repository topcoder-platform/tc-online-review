/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence;

import com.topcoder.util.datavalidator.AbstractObjectValidator;

/**
 * <p>
 * This is mock validator used for testing
 * {@link InformixReviewPersistence#searchReviews(com.topcoder.search.builder.filter.Filter, boolean)}.
 * </p>
 * @author TCSDEVELOPER
 * @version 1.2
 * @since 1.2
 */
@SuppressWarnings("serial")
public class MockValidator extends AbstractObjectValidator {

    /**
     * <p>
     * Creates instance of MockValidator.
     * </p>
     */
    public MockValidator() {
    }

    /**
     * <p>
     * Always return true.
     * </p>
     * @param obj the object to validate
     * @return true
     */
    public boolean valid(Object obj) {
        return true;
    }

    /**
     * <p>
     * Always return null string.
     * </p>
     * @param obj the object to get message
     * @return null
     */
    public String getMessage(Object obj) {
        return null;
    }
}
