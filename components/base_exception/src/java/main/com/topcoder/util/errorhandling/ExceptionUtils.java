/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.errorhandling;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * <p>
 * Utility class that provides static access to actions performed frequently
 * when dealing with exceptions.
 * </p>
 * <p>
 * This includes a way to retrieve a localized message using a resource bundle,
 * and two common parameter checking methods (for null objects or empty
 * strings), which also utilize messages from resource bundles.
 * </p>
 * <p>
 * It is the intention that any popular check methods within TCS components can
 * go within this class, so they are not required to be rewritten for each
 * component.
 * </p>
 * <p>
 * <b>Thread safety</b>: These methods are all provided statically, and are
 * stateless to ensure thread safety - the class has no members.
 * </p>
 *
 * @author sql_lall, TCSDEVELOPER
 * @version 2.0
 */
public class ExceptionUtils {
    /**
     * <p>
     * Default no-arg constructor.
     * </p>
     */
    protected ExceptionUtils() {
        // does nothing
    }

    /**
     * <p>
     * Returns a localized message string, given a <code>bundle</code> and the
     * message's <code>key</code>. If no message can be found within the
     * bundle, the <code>defaultMessage</code> is used instead.
     * </p>
     * <p>
     * This method should not throw exceptions, if there is a problem the
     * default message string will be returned.
     * </p>
     *
     * @param bundle The localized resource bundle used to obtain the message
     *            from
     * @param key The key of the message from within the bundle
     * @param defaultMessage The default message to use if one can't be found in
     *            the bundle
     * @return The localized message, or the default if none could be found
     */
    public static String getMessage(ResourceBundle bundle, String key, String defaultMessage) {
        // return default message string if bundle or key is null
        if (bundle == null || key == null) {
            return defaultMessage;
        }
        try {
            // return a localized message string
            return bundle.getString(key);
        } catch (MissingResourceException mre) {
            // omit
        } catch (ClassCastException cce) {
            // omit
        }
        // return default message if exception is thrown
        return defaultMessage;

    }

    /**
     * <p>
     * Creates an exception message automatically from the exception's codes.
     * </p>
     * <p>
     * This static method builds a key by concatenating the codes together,
     * split by "-", and use the key to retrieve message, the returned result
     * will be in the form like "key: message" generally.
     * </p>
     *
     * @param bundle The localized resource bundle used to obtain the message
     *            from
     * @param appCode The application code for the exception
     * @param moduleCode The module code for the exception
     * @param errorCode The error code for the exception
     * @return The obtained error message, or the appCode-modCode-errorCode key
     *         if none is found.
     */
    public static String getMessage(ResourceBundle bundle, String appCode, String moduleCode, String errorCode) {
        // the int variable indicates which string is the last non null/empty one
        // if errorCode is non null/empty, lastNonEmptyNull = 2;
        // else if moduleCode is non null/empty, lastNonEmptyNull = 1;
        // else lastNonEmptyNull = 0;
        int lastNonEmptyNull = 0;
        if (errorCode != null && errorCode.length() != 0) {
            lastNonEmptyNull = 2;
        } else if (moduleCode != null && moduleCode.length() != 0) {
            lastNonEmptyNull = 1;
        }
        // get the key from the exception's codes
        StringBuffer keyBuffer = new StringBuffer();
        if (appCode != null && appCode.length() != 0) {
            keyBuffer.append(appCode);
            if (lastNonEmptyNull > 0) {
                keyBuffer.append("-");
            }
        }
        if (moduleCode != null && moduleCode.length() != 0) {
            keyBuffer.append(moduleCode);
            if (lastNonEmptyNull > 1) {
                keyBuffer.append("-");
            }
        }
        if (errorCode != null && errorCode.length() != 0) {
            keyBuffer.append(errorCode);
        }
        String key = keyBuffer.toString();
        // use the key to retrieve message
        String message = getMessage(bundle, key, key);

        // get result;
        // if key is equals to message, result = key;
        // else if key is null/empty && message is null/empty, result = ""
        // else if key is null/empty, result = message
        // else if message is null/empty, result = key
        // else result result = "key: message"
        // Note: key and message will never be null
        StringBuffer resultBuffer = new StringBuffer();
        if (key.length() != 0) {
            resultBuffer.append(key);
            if (message.length() != 0) {
                resultBuffer.append(": ");
            }
        }
        if (message.length() != 0) {
            resultBuffer.append(message);
        }

        return (key.equals(message)) ? key : resultBuffer.toString();
    }

    /**
     * <p>
     * Utility parameter checker that throws an <code>IllegalArgumentException</code>
     * if the <code>item</code> parameter is null.
     * </p>
     * <p>
     * If null, the message to use for the exception is obtained from the given
     * resource bundle.
     * </p>
     *
     * @param item The object to compare to null
     * @param bundle The resource bundle to get the exception message from
     * @param key The key of the message within the bundle.
     * @param defaultMessage The default message if no localized one can be found
     * @throws IllegalArgumentException if the <code>item</code> parameter is
     *             null
     */
    public static void checkNull(Object item, ResourceBundle bundle, String key, String defaultMessage) {
        if (item != null) {
            // non-null, return directly
            return;
        }
        // throw IAE if item is null
        // the message for the exception is obtained from the given bundle
        throw new IllegalArgumentException(getMessage(bundle, key, defaultMessage));
    }

    /**
     * <p>
     * Utility parameter checker that throws an <code>IllegalArgumentException</code>
     * if the <code>item</code> parameter is a null or empty string.
     * </p>
     * <p>
     * If parameter is null/empty, the message to use for the exception is obtained from
     * the given resource bundle.
     * </p>
     *
     * @param item The string to check for null or empty.
     * @param bundle The resource bundle to get the exception message from
     * @param key The key of the message within the bundle.
     * @param defaultMessage The default message if no localized one can be found
     * @throws IllegalArgumentException if the <code>item</code> parameter is
     *             null or an empty string
     */
    public static void checkNullOrEmpty(String item, ResourceBundle bundle, String key, String defaultMessage) {
        if (item != null && item.trim().length() > 0) {
            // non null/empty, return directly
            return;
        }
        // throw IAE if item is null/empty
        // the message for the exception is obtained from the given bundle
        throw new IllegalArgumentException(getMessage(bundle, key, defaultMessage));
    }
}
