/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.datavalidator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * <p>
 * This is a simple bean which represents the resource bundle information for validators.
 * </p>
 * 
 * <p>
 * <b>Thread Safety:</b><br>
 * This a serializable bean and is not thread-safe. This doesn't affect the thread-safety of the component since it
 * will be utilized in a thread-safe manner.
 * </p>
 *
 * @author AleaActaEst, telly12
 * @version 1.1
 *
 * @since 1.1
 */
public class BundleInfo implements Serializable {
    /**
     * <p>
     * This represents the resource bundle used by this validator. It is set in one of the constructors to either null
     * (not required) or to a specific named bundle. This is then used to fetch the specific message based on a
     * resource key (which is also initialized in the same constructor). It is transient since it is not serializable.
     * It can be changed upon serialization and subsequent de-serialization.
     * </p>
     */
    private transient ResourceBundle resourceBundle;

    /**
     * <p>
     * This is a name of a resource bundle to load up the resource message from. This is initialized in the setter but
     * since it is not required it can be null. This is used to create a named resource bundle in one of the
     * constructors. It can be null but cannot be an empty string.
     * </p>
     */
    private String bundleName;

    /**
     * <p>
     * This is the locale used to fetch the resource data. It identifies the locale for the message key that will be
     * used to fetch the message for the validator. It will be initialized through a setter and is mutable.
     * </p>
     */
    private Locale bundleLocale;

    /**
     * <p>
     * This is a flag used to remember if the user set up this validator with a current locale. THis will be used by
     * serialization to also use the current locale when being serialized. It will be initialized through a setter and
     * is mutable. By default it is false.
     * </p>
     */
    private boolean currentLocale = false;

    /**
     * <p>
     * This is a message key used to look up and fetch a message from a resource bundle. It will be initialized through
     * a setter and is mutable. it is an optional variable and this it can be left uninitialized (i.e. as null)
     * </p>
     */
    private String messageKey;

    /**
     * <p>
     * This is a default message that can be used by any validator in case the resource bundle is not available or
     * simply not set. This will also apply to a situation where the bundle doesn't contain the requested message.
     * This is basically a fail safe message. It will be initialized through a setter and is mutable. It can be a null
     * or an empty message. There are no restrictions.
     * </p>
     */
    private String defaultMessage;

    /**
     * <p>
     * Empty constructor, create an instance of <code>BundleInfo</code>.
     * </p>
     */
    public BundleInfo() {
    }

    /**
     * <p>
     * Getter for the bundleName property.
     * </p>
     *
     * @return the name of bundle
     */
    public String getBundleName() {
        return bundleName;
    }

    /**
     * <p>
     * Getter for the locale property which is used to fetch the resource data.
     * </p>
     *
     * @return the locale used to fetch the resource data.
     */
    public Locale getLocale() {
        return bundleLocale;
    }

    /**
     * <p>
     * Getter for the defaultMessage property which is used by the validator.
     * </p>
     *
     * @return the defaultMessage used by the validator
     */
    public String getDefaultMessage() {
        return defaultMessage;
    }

    /**
     * <p>
     * Getter for the messageKey property. The messageKey is used to look up and fetch a message from a resource
     * bundle.
     * </p>
     *
     * @return the message key used to look up and fetch a message from a resource bundle
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * <p>
     * Setter for the messageKey property. The messageKey is used to look up and fetch a message from a resource
     * bundle.
     * </p>
     *
     * @param messageKey the message key used to look up and fetch a message from a resource bundle
     *
     * @throws IllegalArgumentException if the messageKey is null or empty
     */
    public void setMessageKey(String messageKey) {
        Helper.checkString(messageKey, "messageKey");
        this.messageKey = messageKey;
    }

    /**
     * <p>
     * Setter for the defaultMessage property which is used by the validator.
     * </p>
     *
     * @param defaultMessage the defaultMessage used by the validator
     *
     * @throws IllegalArgumentException if the defaultMessage is null or empty
     */
    public void setDefaultMessage(String defaultMessage) {
        Helper.checkString(defaultMessage, "defaultMessage");
        this.defaultMessage = defaultMessage;
    }

    /**
     * <p>
     * Setter for the bundle property.
     * </p>
     *
     * @param bundleName the name of the bundle
     * @param locale the locale used to fetch the resource data.
     *
     * @throws IllegalArgumentException if the bundleName is null or empty, or if the locale is null
     */
    public void setBundle(String bundleName, Locale locale) {
        Helper.checkString(bundleName, "bundleName");
        Helper.checkNull(locale, "locale");
        this.bundleName = bundleName;
        this.bundleLocale = locale;
        this.resourceBundle = ResourceBundle.getBundle(bundleName, locale);
    }

    /**
     * <p>
     * Setter for the bundle property with default locale.
     * </p>
     *
     * @param bundleName the name of the bundle
     *
     * @throws IllegalArgumentException if the bundleName is null or empty
     */
    public void setBundle(String bundleName) {
        setBundle(bundleName, Locale.getDefault());
        this.currentLocale = true;
    }

    /**
     * <p>
     * Getter for the currentLocale property.
     * </p>
     *
     * @return the current locale
     */
    public boolean getCurrentLocale() {
        return currentLocale;
    }

    /**
     * <p>
     * Getter for the resourceBundle property.
     * </p>
     *
     * @return the resource bundle
     */
    public ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }

    /**
     * <p>
     * Specific implementation for de-serialization. Here we will allow for the resource bundle to be read back in on
     * the other end.
     * </p>
     *
     * @param stream the serialization stream
     *
     * @throws IOException if there were any issues with reading the stream
     * @throws ClassNotFoundException if there were any issues with JVM trying to find the specific class via class
     *         loader.
     */
    private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
        stream.defaultReadObject();

        // load up the bundle
        if (bundleName != null) {
            if (currentLocale) {
                bundleLocale = Locale.getDefault();
            }

            resourceBundle = ResourceBundle.getBundle(bundleName, bundleLocale);
        }
    }

    /**
     * <p>
     * Specific implementation for serialization. This is provided mainly as needed by the protocol since we did
     * override the readObject. There is nothing for us to do here since reourceBundle is declared as transient and
     * will thus not be written to the stream.
     * </p>
     *
     * @param stream ObjectOutputStream
     *
     * @throws IOException if there are any IO issues in the stream
     */
    private void writeObject(ObjectOutputStream stream)
        throws IOException {
        stream.defaultWriteObject();
    }
}
