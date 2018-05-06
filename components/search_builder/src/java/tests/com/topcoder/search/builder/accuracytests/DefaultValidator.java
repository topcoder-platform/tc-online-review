/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.accuracytests;

import com.topcoder.util.datavalidator.ObjectValidator;

/**
 * <p>A default validator.</p>
 *
 * @author  isv
 * @version 1.0
 * @since 1.3
 */
public class DefaultValidator implements ObjectValidator {

    /**
     * <p>Default constructor.</p>
     */
    public DefaultValidator() {
    }

    /**
     * Determines if the given <code>Object</code> is valid.
     *
     * @param obj <code>Object</code> to validate.
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        return true;
    }

    /**
     * Gives error information about the given <code>Object</code>.
     *
     * @param obj <code>Object</code> to validate.
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise, an error message is returned.
     */
    public String getMessage(Object obj) {
        return null;
    }
}
