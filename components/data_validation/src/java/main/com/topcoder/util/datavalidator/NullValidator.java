/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a specific type of a simple validator which validates based on a boolean whether the input object is null or
 * not.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public class NullValidator extends AbstractObjectValidator {
    /**
     * <p>
     * Creates a new <code>NullValidator</code>.
     * </p>
     */
    public NullValidator() {
    }

    /**
     * <p>
     * Creates a new <code>NullValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param bundleInfo resource bundle information
     */
    public NullValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Validates the given <code>Object</code>.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>true</code> if <code>obj</code> is <code>null</code>; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        return obj == null;
    }

    /**
     * <p>
     * If the given <code>Object</code> is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>null</code> if <code>obj</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(Object obj) {
        if (valid(obj)) {
            return null;
        }

        // get the message from resource bundle if it exists.
        String message = this.getValidationMessage();

        if (message != null) {
            return message;
        }

        return "not null";
    }
}
