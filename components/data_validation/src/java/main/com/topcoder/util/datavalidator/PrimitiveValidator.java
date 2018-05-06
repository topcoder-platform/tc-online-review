/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is a specific wrapper for all validators that deal with primitive type value validation such a Integer,
 * Boolean, Long, Double, Float, Byte, or Short this is nothing more than a convenience wrapper.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is thread-safe since it is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 */
public class PrimitiveValidator extends AbstractObjectValidator {
    /**
     * <p>
     * The underlying validator to use. This is initialized through the constructor and is immutable after that. It is
     * expected to be non-null.
     * </p>
     */
    private ObjectValidator validator = null;

    /**
     * <p>
     * Creates a new <code>PrimitiveValidator</code> that uses <code>validator</code> as the underlying
     * <code>ObjectValidator</code>.
     * </p>
     *
     * @param validator the underlying <code>ObjectValidator</code> to use.
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public PrimitiveValidator(ObjectValidator validator) {
        Helper.checkNull(validator, "validator");

        this.validator = validator;
    }

    /**
     * <p>
     * Creates a new <code>PrimitiveValidator</code> that uses <code>validator</code> as the underlying
     * <code>ObjectValidator</code> with a specific resource bundle information.
     * </p>
     *
     * @param validator the underlying <code>ObjectValidator</code> to use.
     * @param bundleInfo resource bundle information
     *
     * @throws IllegalArgumentException if <code>validator</code> is <code>null</code>.
     */
    public PrimitiveValidator(ObjectValidator validator, BundleInfo bundleInfo) {
        super(bundleInfo);

        if (validator == null) {
            throw new IllegalArgumentException("validator cannot be null");
        }

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
        return validator.valid(obj);
    }

    /**
     * <p>
     * Validates the given <code>boolean</code> value.
     * </p>
     *
     * @param value <code>boolean</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(boolean value) {
        return valid(new Boolean(value));
    }

    /**
     * <p>
     * Validates the given <code>byte</code> value.
     * </p>
     *
     * @param value <code>byte</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(byte value) {
        return valid(new Byte(value));
    }

    /**
     * <p>
     * Validates the given <code>short</code> value.
     * </p>
     *
     * @param value <code>short</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(short value) {
        return valid(new Short(value));
    }

    /**
     * <p>
     * Validates the given <code>int</code> value.
     * </p>
     *
     * @param value <code>int</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(int value) {
        return valid(new Integer(value));
    }

    /**
     * <p>
     * Validates the given <code>long</code> value.
     * </p>
     *
     * @param value <code>long</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(long value) {
        return valid(new Long(value));
    }

    /**
     * <p>
     * Validates the given <code>float</code> value.
     * </p>
     *
     * @param value <code>float</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(float value) {
        return valid(new Float(value));
    }

    /**
     * <p>
     * Validates the given <code>double</code> value.
     * </p>
     *
     * @param value <code>double</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(double value) {
        return valid(new Double(value));
    }

    /**
     * <p>
     * Validates the given <code>char</code> value.
     * </p>
     *
     * @param value <code>char</code> value to be validated.
     *
     * @return <code>true</code> if <code>value</code> is valid; <code>false</code> otherwise.
     */
    public boolean valid(char value) {
        return valid(new Character(value));
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
        return validator.getMessage(obj);
    }

    /**
     * <p>
     * If the given <code>boolean</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>boolean</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(boolean value) {
        return getMessage(new Boolean(value));
    }

    /**
     * <p>
     * If the given <code>byte</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>byte</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(byte value) {
        return getMessage(new Byte(value));
    }

    /**
     * <p>
     * If the given <code>short</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>short</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(short value) {
        return getMessage(new Short(value));
    }

    /**
     * <p>
     * If the given <code>int</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>int</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(int value) {
        return getMessage(new Integer(value));
    }

    /**
     * <p>
     * If the given <code>long</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>long</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(long value) {
        return getMessage(new Long(value));
    }

    /**
     * <p>
     * If the given <code>float</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>float</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(float value) {
        return getMessage(new Float(value));
    }

    /**
     * <p>
     * If the given <code>double</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>double</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(double value) {
        return getMessage(new Double(value));
    }

    /**
     * <p>
     * If the given <code>char</code> value is valid, this returns <code>null</code>. Otherwise, it returns an error
     * message.
     * </p>
     *
     * @param value <code>char</code> value to be validated.
     *
     * @return <code>null</code> if <code>value</code> is valid. Otherwise an error message is returned.
     */
    public String getMessage(char value) {
        return getMessage(new Character(value));
    }

    /**
     * <p>
     * Gives error information about the given object being validated. This method will return possibly a number of
     * messages produced for this object by a number of validators if the validator is composite.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     *
     * @since 1.1
     */
    public String[] getMessages(Object object) {
        return validator.getMessages(object);
    }

    /**
     * <p>
     * This is the same method concept as the <code>getMessage()</code> except that this method will evaluate the whole
     * validator tree and return all the messages from any validators that failed the object.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     *
     * @since 1.1
     */
    public String[] getAllMessages(Object object) {
        return validator.getAllMessages(object);
    }

    /**
     * <p>
     * This is another version of the <code>getAllMessages()</code> method which accepts a limit on how many messages
     * at most will be requested. This means that the traversal of the validator tree will stop the moment
     * messageLimit has been met. This is provided so that the user can limit the number of messages coming back and
     * thus limit the traversal time but still get a reasonable number of messages back to the user.
     * </p>
     *
     * @param object the object to validate
     * @param messageLimit the max number of messages. This must be greater-than-or-equal to 0.
     *
     * @return String[] a non-empty but possibly null array of failure messages with a most messageLimit messages.
     *
     * @throws IllegalArgumentException if messageLimit is not positive
     *
     * @since 1.1
     */
    public String[] getAllMessages(Object object, int messageLimit) {
        if (messageLimit <= 0) {
            throw new IllegalArgumentException("The argument 'messageLimit' must be greater than 0");
        }

        return validator.getAllMessages(object, messageLimit);
    }
}
