/*
 * TCS Data Validation
 *
 * AbstractAssociativeObjectValidator.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.datavalidator;

import java.util.List;
import java.util.Vector;
import java.util.Iterator;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * This is am abstract class that is used to abstract functionality of validator
 * composition. It is bascially a composition of 0 or more validators. This
 * class gives its descendants the abilty to manipulate the aspects of validator
 * composition. The validators are guaranteed to be placed in a normal list
 * order which means that the first validator added to this composition will
 * be the first that is returned through the iterator, the second will be the
 * second one returned, etc...
 * This class is thread-safe through synchronization of the validators list.
 * @version 1.1
 */
public abstract class AbstractAssociativeObjectValidator
        extends AbstractObjectValidator {

    /** The list of validators to use. This is initialized in the constructor to
     * an empty vector and nce initialized the reference is immutable
     *  his was changed in version 1.1 to support TopCoder coding conventions
     * */
    private final List validators;

    /**
     * Creates a new <code>AbstractAssociativeObjectValidator</code>,
     * with initially no associated validators.
     */
    public AbstractAssociativeObjectValidator() {
      super();
      validators = new Vector();
    }

    /**
     * This constructor will initialize the resource bundle and the remaining
     * variables based on the input parameters.
     * @param bundleName String name of the bundle to use
     * @param locale Locale Locale to utilize when fetching the message resources
     * @param messageKey String the resource byndle message key to fetch the vadliation
     * message with.
     * @param defaultMessage String This is a fail safe message to use in case the
     * bundle cannot be used (failed creation) or the provided key has no associated
     * message in the bundle.
     * @throws IllegalArgumentException if any of the parameters (except defaultMessage)
     * is null or an empty string (for string parameters)
     */
    protected AbstractAssociativeObjectValidator(BundleInfo bundleInfo) throws IllegalArgumentException{
      super(bundleInfo);
      validators = new Vector();
      }

    /**
     * Creates a new <code>AbstractAssociativeObjectValidator</code>,
     * with 2 initial <code>ObjectValdiators</code>.
     *
     * @param   validator1  the first <code>ObjectValidator</code> to use.
     * @param   validator2  the second <code>ObjectValidator</code> to use.
     * @throws  IllegalArgumentException    if <code>validator1</code> or
     *      <code>validator2</code> is <code>null</code>.
     */
    public AbstractAssociativeObjectValidator(ObjectValidator validator1,
            ObjectValidator validator2)
            throws IllegalArgumentException {
        this();

        if (validator1 == null) {
            throw new IllegalArgumentException("validator1 cannot be null");
        }

        if (validator2 == null) {
            throw new IllegalArgumentException("validator2 cannot be null");
        }

        validators.add(validator1);
        validators.add(validator2);
    }

    /**
     * Creates a new <code>AbstractAssociativeObjectValidator</code>,
     * with 2 initial <code>ObjectValdiators</code> and with resource bundle
     * information.
     * @param   validator1  the first <code>ObjectValidator</code> to use.
     * @param   validator2  the second <code>ObjectValidator</code> to use.
     * @param bundleName String name of the bundle to use
     * @param messageKey String the resource byndle message key to fetch the vadliation
     * message with.
     * @param defaultMessage String This is a fail safe message to use in case the
     * bundle cannot be used (failed creation) or the provided key has no associated
     * message in the bundle.
     * @throws IllegalArgumentException if any of the parameters (except defaultMessage)
     * is null or an empty string (for string parameters)
     * @throws  IllegalArgumentException    if <code>validator1</code> or
     *      <code>validator2</code> is <code>null</code>.
     */
    public AbstractAssociativeObjectValidator(ObjectValidator validator1,
            ObjectValidator validator2 , BundleInfo bundleInfo)
            throws IllegalArgumentException {

        this(bundleInfo);

        if (validator1 == null) {
            throw new IllegalArgumentException("validator1 cannot be null");
        }

        if (validator2 == null) {
            throw new IllegalArgumentException("validator2 cannot be null");
        }

        validators.add(validator1);
        validators.add(validator2);
    }

     /**
     * Adds a new <code>ObjectValidator</code> to the list of associated
     * validators. This will be added to the end of the list.
     *
     * @param   validator  <code>ObjectValidator</code> to add.
     * @throws  IllegalArgumentException    if <code>validator</code> is
     * <code>null</code>.
     */
    public void addValidator(ObjectValidator validator) {
        if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
        }

        if (validator == this) {
            throw new IllegalArgumentException("cannot add an "
                    + getClass().getName() + " to itself");
        }

        validators.add(validator);
    }
    /**
     * Removes an existing <code>ObjectValidator</code> from the list of associated
     * validators. Note that removing a non-existing validator will have no effect.
     *
     * @param   validator  <code>ObjectValidator</code> to remove.
     * @throws  IllegalArgumentException    if <code>validator</code> is
     * <code>null</code>.
     * @since 1.1
    */
    public void removeValidator(ObjectValidator validator) {
            if (validator == null) {
                throw new IllegalArgumentException("validator cannot be null");
            }
            validators.remove(validator);
    }

    /**
     * This is a simple iterator of all the validators that make up this
     * associative/composite validator.
     * @return Iterator an iterator of current associated validators
     * @since 1.1
     */
    public Iterator getValidators(){
      return validators.iterator();
    }
}
