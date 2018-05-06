/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.util.datavalidator.AbstractObjectValidator;

/**
 * <p>
 * A mock implementation of ObjectValidator. Used for testing.
 * </p>
 *
 * @author yuanyeyuanye
 * @version 1.0
 */
public class MockedValidator extends AbstractObjectValidator {
    /**
     * the serial version uid.
     */
    private static final long serialVersionUID = 4476906760075942604L;

    /**
     * <p>
     * Creates an instance of MockValidator.
     * </p>
     */
    public MockedValidator() {
        // Empty
    }

    /**
     * <p>
     * Always returns null, since this validator considers any object to be valid.
     * </p>
     *
     * @param obj
     *            The object to validate (this parameter is ignored)
     *
     * @return <code>null</code> always.
     */
    public String getMessage(Object obj) {
        return null;
    }

    /**
     * <p>
     * Always returns true, since this validator considers any object to be valid.
     * </p>
     *
     * @param obj
     *            The object to validate (this parameter is ignored)
     *
     * @return <code>true</code> always.
     */
    public boolean valid(Object obj) {
        return true;
    }
}
