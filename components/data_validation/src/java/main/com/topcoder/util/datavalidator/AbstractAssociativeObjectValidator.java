/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * <p>
 * This is an abstract class that is used to abstract functionality of validator composition. It is basically a
 * composition of 0 or more validators.
 * </p>
 *
 * <p>
 * This class gives its descendants the ability to manipulate the aspects of validator composition. The validators are
 * guaranteed to be placed in a normal list order which means that the first validator added to this composition will
 * be the first that is returned through the iterator, the second will be the second one returned, etc...
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b> This class is thread-safe through synchronization of the validators list.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public abstract class AbstractAssociativeObjectValidator extends AbstractObjectValidator {
    /**
     * <p>
     * The list of validators to use. This is initialized in the constructor to an empty <code>ArrayList</code>, the
     * reference is immutable. this was changed in version 1.1 to support TopCoder coding conventions
     * </p>
     */
    private final List validators = new ArrayList();

    /**
     * <p>
     * Creates a new <code>AbstractAssociativeObjectValidator</code>, with initially no associated validators.
     * </p>
     */
    public AbstractAssociativeObjectValidator() {
        super();
    }

    /**
     * <p>
     * This constructor will initialize the resource bundle and the remaining variables based on the input parameters.
     * </p>
     *
     * @param bundleInfo name of the bundle to use
     *
     * @throws IllegalArgumentException if the <code>bundleInfo</code> is null, or if the <code>bundleName</code>,
     *         <code>defaultMessage</code>,  <code>locale</code> or <code>resourceBundle</code> of
     *         <code>bundleInfo</code> is null, or empty for <code>String</code> values.
     *
     * @since 1.1
     */
    protected AbstractAssociativeObjectValidator(BundleInfo bundleInfo) {
        super(bundleInfo);
    }

    /**
     * <p>
     * Creates a new <code>AbstractAssociativeObjectValidator</code>, with 2 initial <code>ObjectValdiators</code>.
     * </p>
     *
     * @param validator1 the first <code>ObjectValidator</code> to use.
     * @param validator2 the second <code>ObjectValidator</code> to use.
     *
     * @throws IllegalArgumentException if <code>validator1</code> or <code>validator2</code> is <code>null</code>.
     */
    public AbstractAssociativeObjectValidator(ObjectValidator validator1, ObjectValidator validator2) {
        this();
        initialValidators(validator1, validator2);
    }

    /**
     * <p>
     * Creates a new <code>AbstractAssociativeObjectValidator</code> with 2 initial <code>ObjectValdiators</code> and
     * with resource bundle information.
     * </p>
     *
     * @param validator1 the first <code>ObjectValidator</code> to use.
     * @param validator2 the second <code>ObjectValidator</code> to use.
     * @param bundleInfo the name of bundle to use
     *
     * @throws IllegalArgumentException if either <code>validator1</code> or <code>validator2</code> is
     *         <code>null</code>.
     * @throws IllegalArgumentException if the <code>bundleInfo</code> is null, or if the <code>bundleName</code>,
     *         <code>defaultMessage</code>,  <code>locale</code> or <code>resourceBundle</code> of
     *         <code>bundleInfo</code> is null, or empty for <code>String</code> values.
     *
     * @since 1.1
     */
    public AbstractAssociativeObjectValidator(ObjectValidator validator1, ObjectValidator validator2,
        BundleInfo bundleInfo) {
        super(bundleInfo);
        initialValidators(validator1, validator2);
    }

    /**
     * <p>
     * Adds a new <code>ObjectValidator</code> to the list of associated validators. This will be added to the end of
     * the list.
     * </p>
     *
     * @param validator the <code>ObjectValidator</code> to be added.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code> or identical to the caller
     *         instance.
     *
     * @since 1.1
     */
    public void addValidator(ObjectValidator validator) {
        Helper.checkNull(validator, "validator");

        if (validator == this) {
            throw new IllegalArgumentException("Cannot add an " + getClass().getName() + " to itself");
        }

        validators.add(validator);
    }

    /**
     * <p>
     * Removes an existing <code>ObjectValidator</code> from the list of associated validators. Note that removing a
     * non-existing validator will have no effect.
     * </p>
     *
     * @param validator the <code>ObjectValidator</code> to be removed.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     *
     * @since 1.1
     */
    public void removeValidator(ObjectValidator validator) {
        Helper.checkNull(validator, "validator");

        validators.remove(validator);
    }

    /**
     * <p>
     * This is a simple iterator of all the validators that make up this associative/composite validator.
     * </p>
     *
     * @return an iterator of current associated validators
     *
     * @since 1.1
     */
    public Iterator getValidators() {
        return validators.iterator();
    }

    /**
     * <p>
     * Initial the <code>ObjectValdiators</code> with the given instances when creating a new
     * <code>AbstractAssociativeObjectValidator</code>.
     * </p>
     *
     * @param validator1 the first <code>ObjectValidator</code> to use.
     * @param validator2 the second <code>ObjectValidator</code> to use.
     *
     * @throws IllegalArgumentException if either <code>validator1</code> or <code>validator2</code> is
     *         <code>null</code>.
     */
    private void initialValidators(ObjectValidator validator1, ObjectValidator validator2) {
        Helper.checkNull(validator1, "validator1");
        Helper.checkNull(validator2, "validator2");
        validators.add(validator1);
        validators.add(validator2);
    }
}
