/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a specific type of a composite validator which validates based on a boolean NOT outcome of the associated
 * validator. In other words it reverses the validation outcome of the associated validator. Note that the message
 * returned by this validator in case of failure doesn't take into account the associated validator since it reverses
 * the successful validation outcome of that validator.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This class is thread-safe as it is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public class NotValidator extends AbstractObjectValidator {
    /**
     * <p>
     * The underlying validator to use.
     * </p>
     */
    private final ObjectValidator validator;

    /**
     * <p>
     * Creates a new <code>NotValidator</code> that uses <code>validator</code> as the underlying
     * <code>ObjectValidator</code>.
     * </p>
     *
     * @param validator the underlying <code>ObjectValidator</code> to use.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public NotValidator(ObjectValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
        }

        this.validator = validator;
    }

    /**
     * <p>
     * Creates a new <code>NotValidator</code> that uses <code>validator</code> as the underlying
     * <code>ObjectValidator</code> and is initialized with a specified resource bundle information.
     * </p>
     *
     * @param validator the underlying <code>ObjectValidator</code> to use.
     * @param bundleInfo DOCUMENT ME!
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public NotValidator(ObjectValidator validator, BundleInfo bundleInfo) {
        super(bundleInfo);

        Helper.checkNull(validator, "validator");
        this.validator = validator;
    }

    /**
     * <p>
     * Validates the given <code>Object</code>.
     * </p>
     *
     * @param obj <code>Object</code> to be validated.
     *
     * @return <code>true</code> if <code>obj</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(Object obj) {
        return !validator.valid(obj);
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

        return "NOT failure";
    }
}
