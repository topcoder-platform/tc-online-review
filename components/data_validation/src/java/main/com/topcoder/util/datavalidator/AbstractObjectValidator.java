/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

/**
 * <p>
 * This is an abstract <code>ObjectValidator</code> implementation which gives the ability for resource bundled message
 * fetching for the validation messages.
 * </p>
 *
 * <p>
 * The user will be able to set up which bundle should be used and what key should be used to fetch the message. We
 * also have the ability to specify the locale. But if one is not provided then the current locale will be used. Note
 * that the bundle functionality is fail safe in the sense the user must provide a default message in case the bundle
 * functionality fails. This way the validator is always guaranteed to be able to produce a message. The user is not
 * required though to utilize the resource bundle functionality and can directly supply just the message that will be
 * then used by the implementing validator.
 * </p>
 *
 * <p>
 * <b>Thread Safety:</b>This is a thread-safe implementation since it is immutable.
 * </p>
 *
 * @author WishingBone, zimmy, AleaActaEst, telly12
 * @version 1.1
 *
 * @since 1.1
 */
public abstract class AbstractObjectValidator implements ObjectValidator {
    /**
     * <p>
     * This is the id of this validator and is used to uniquely identify this validator amongst other validators in a
     * composite validator for example. This is set through a dedicated setter and is mutable. We can also read it
     * through a dedicated getter. Access to this must be synchronized for thread-safety. It's initialized as an empty
     * <code>String</code> which means that the id has not been set.
     * </p>
     */
    private String id = "";

    /**
     * <p>
     * This represents the resource bundle information used by this validator. It is set in one of the constructors to
     * either null (not required) or to a specific named bundle. This is then used to fetch the specific message based
     * on a resource key (which is also initialized in the same constructor) It is transient since it is not
     * serializable. It can be changed upon serialization and subsequent de-serialization.
     * </p>
     */
    private BundleInfo bundleInfo;

    /**
     * <p>
     * This is a simple constructor which initializes the <code>defaultMessage</code> to the input string.
     * </p>
     *
     * @param validationMessage String This is the validation message to use for the underlying validator.
     *
     * @throws IllegalArgumentException if the input parameter is null or empty
     */
    protected AbstractObjectValidator(String validationMessage) {
        this();
        Helper.checkString(validationMessage, "validationMessage");
        bundleInfo.setDefaultMessage(validationMessage);
    }

    /**
     * <p>
     * This is a simple constructor which initializes all the internal variables to their default values.
     * </p>
     */
    protected AbstractObjectValidator() {
        bundleInfo = new BundleInfo();
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
     */
    protected AbstractObjectValidator(BundleInfo bundleInfo) {
        checkBundleInfo(bundleInfo);
        this.bundleInfo = copyBundleInfo(bundleInfo);
    }

    /**
     * <p>
     * This is a simple method which returns the currently set validation message for this validator. This will be
     * either the message obtained from resource bundle or a default message. Please note that this could return null.
     * </p>
     *
     * @return the validation message, possibly null
     */
    public String getValidationMessage() {
        return getValidationMessage(bundleInfo.getMessageKey(), bundleInfo.getDefaultMessage());
    }

    /**
     * <p>
     * This is a simple method which returns the currently set validation message for this validator. This will be
     * either the message obtained from resource bundle or a default message. Please note that this could return null.
     * </p>
     *
     * @param messageKey The message key of the resource bundle
     * @param defaultMessage the default message for the resource bundle
     *
     * @return the validation message, possibly null
     */
    public String getValidationMessage(String messageKey, String defaultMessage) {
        synchronized (this) {
            try {
                return bundleInfo.getResourceBundle().getString(messageKey);
            } catch (Exception e) {
                // we choke it off
            }

            return defaultMessage;
        }
    }

    /**
     * <p>
     * A unique identifier of this validator. This can be used to identify validators with specific resources that they
     * validate.
     * </p>
     *
     * @return the id of validator
     */
    public String getId() {
        synchronized (this.id) {
            return id;
        }
    }

    /**
     * <p>
     * This is a setter for the validator's id. This will be used to set a specific id for the validator so that later
     * on we can map the validator for a message for example. The value of id can be anything except null.
     * </p>
     *
     * @param id the id of validator
     *
     * @throws IllegalArgumentException if the <code>id</code> is null
     */
    public void setId(String id) {
        Helper.checkNull(id, "id");

        synchronized (this.id) {
            this.id = id;
        }
    }

    /**
     * <p>
     * A setter of resource bundle information used by this validator.
     * </p>
     *
     * @param bundleInfo the resource bundle information used by this validator
     *
     * @throws IllegalArgumentException if the <code>bundleInfo</code> is null, or if the <code>bundleName</code>,
     *         <code>defaultMessage</code>,  <code>locale</code> or <code>resourceBundle</code> of
     *         <code>bundleInfo</code> is null, or empty for <code>String</code> values.
     */
    public void setResourceBundleInfo(BundleInfo bundleInfo) {
        checkBundleInfo(bundleInfo);

        synchronized (this) {
            this.bundleInfo = copyBundleInfo(bundleInfo);
        }
    }

    /**
     * <p>
     * This is a getter for the bundle information contained in this validator.
     * </p>
     *
     * <p>
     * This method should be thread-safe
     * </p>
     *
     * @return current bundle info, possibly null.
     */
    public BundleInfo getBundleInfo() {
        synchronized (this) {
            return copyBundleInfo(bundleInfo);
        }
    }

    /**
     * <p>
     * Helper method to make a copy of the <code>BundleInfo</code>.
     * </p>
     *
     * @param bundleInfo the BundleInfo instance to be copied
     *
     * @return BundleInfo the copy instance of BundleInfo
     */
    private BundleInfo copyBundleInfo(BundleInfo bundleInfo) {
        if (bundleInfo == null) {
            return null;
        }

        BundleInfo copy = new BundleInfo();

        // Only set the value if it's not null or empty
        if ((bundleInfo.getBundleName() != null) && (bundleInfo.getBundleName().trim().length() > 0)) {
            if (bundleInfo.getLocale() != null) {
                copy.setBundle(bundleInfo.getBundleName(), bundleInfo.getLocale());
            } else {
                // Use default Local
                copy.setBundle(bundleInfo.getBundleName());
            }
        }

        if ((bundleInfo.getDefaultMessage() != null) && (bundleInfo.getDefaultMessage().trim().length() > 0)) {
            copy.setDefaultMessage(bundleInfo.getDefaultMessage());
        }

        if ((bundleInfo.getMessageKey() != null) && (bundleInfo.getMessageKey().trim().length() > 0)) {
            copy.setMessageKey(bundleInfo.getMessageKey());
        }

        return copy;
    }

    /**
     * <p>
     * Checking the <code>BundleInfo</code> to ensure that enough information was provided.
     * </p>
     *
     * @param bundleInfo the <code>BundleInfo</code> instance to be checked
     *
     * @throws IllegalArgumentException if the <code>bundleInfo</code> is null, or if the <code>bundleName</code>,
     *         <code>defaultMessage</code>,  <code>locale</code> or <code>resourceBundle</code> of
     *         <code>bundleInfo</code> is null, or empty for <code>String</code> values.
     */
    private void checkBundleInfo(BundleInfo bundleInfo) {
        Helper.checkNull(bundleInfo, "bundleInfo");
        Helper.checkNull(bundleInfo.getLocale(), "bundleInfo.getLocale()");
        Helper.checkNull(bundleInfo.getResourceBundle(), "bundleInfo.getResourceBundle()");
        Helper.checkString(bundleInfo.getBundleName(), "bundleInfo.getBundleName()");
        Helper.checkString(bundleInfo.getMessageKey(), "bundleInfo.getMessageKey()");
    }

    /**
     * <p>
     * Gives error information about the given object being validated. This method will return possibly a number of
     * messages produced for this object by a number of validators if the validator is composite. Since this is a
     * primitive validator the result will always be equivalent to getting a simple message but as an array.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     *
     * @since 1.1
     */
    public String[] getMessages(Object object) {
        String message = getMessage(object);

        return (message == null) ? null : new String[] {message};
    }

    /**
     * <p>
     * This is the same method concept as the <code>getMessage()</code> except that this method will evaluate the whole
     * validator tree and return all the messages from any validators that failed the object. In other words this is a
     * non-short-circuited version of the method. Since this is a primitive validator the result will always be
     * equivalent to getting a single message but as an array.
     * </p>
     *
     * @param object the object to validate
     *
     * @return String[] a non-empty but possibly null array of failure messages
     *
     * @since 1.1
     */
    public String[] getAllMessages(Object object) {
        return getAllMessages(object, Integer.MAX_VALUE);
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

        return getMessages(object);
    }
}
