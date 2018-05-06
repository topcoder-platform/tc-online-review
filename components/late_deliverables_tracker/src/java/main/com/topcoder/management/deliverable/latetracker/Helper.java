/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Set;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * Helper class for the component. It provides useful common constants and methods for all the classes in this
 * component.
 * </p>
 *
 * <p>
 * <em>Change in 1.1:</em>
 * <ol>
 * <li>parseLong: added parameter 'minValue'.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Added delayToString() and some constants.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added checkLateDeliverableTypes() method.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author myxgyy, sparemax
 * @version 1.3
 */
public final class Helper {
    /**
     * <p>
     * Represents comma.
     * </p>
     */
    public static final String COMMA = ",";

    /**
     * <p>
     * Represents &quot;loggerName&quot; property key in configuration.
     * </p>
     */
    public static final String LOGGER_NAME_KEY = "loggerName";

    /**
     * <p>
     * Represents one minute time in long.
     * </p>
     *
     * @since 1.2
     */
    public static final long MINUTE = 60000;

    /**
     * <p>
     * Represents constant value 60.
     * </p>
     *
     * @since 1.2
     */
    public static final int SIXTY = 60;

    /**
     * <p>
     * Represents one second in long value.
     * </p>
     */
    public static final long THOUSAND = 1000;

    /**
     * <p>
     * Represents constant value 24.
     * </p>
     *
     * @since 1.2
     */
    private static final int TWENTY_FOUR = 24;

    /**
     * <p>
     * Represents &quot;objectFactoryConfig&quot; child configuration name in configuration.
     * </p>
     */
    private static final String OBJECT_FACTORY_CONFIG = "objectFactoryConfig";

    /**
     * <p>
     * Represents the entrance message.
     * </p>
     */
    private static final String MESSAGE_ENTRANCE = "Entering the method [{0}].";

    /**
     * <p>
     * Represents the exit message.
     * </p>
     */
    private static final String MESSAGE_EXIT = "Exiting the method [{0}], time spent in the method: {1} milliseconds.";

    /**
     * <p>
     * Represents the error message.
     * </p>
     */
    private static final String MESSAGE_ERROR = "Error in the method [{0}], Details: {1}";

    /**
     * <p>
     * Represents the return value message.
     * </p>
     */
    private static final String MESSAGE_RETURN = "Return value: {0}.";

    /**
     * <p>
     * Prevents to create a new instance.
     * </p>
     */
    private Helper() {
        // empty
    }

    /**
     * <p>
     * Converts the given delay in microseconds to a string. The delay string has format
     * "[[X day[s] ]Y hour[s] ]Z minute[s]". The minimum delay is assumed to be equal to 1 minute.
     * </P>
     *
     * @param delay
     *            the delay in microseconds (null if not available)
     *
     * @return the constructed delay string (not null).
     *
     * @since 1.2
     */
    public static String delayToString(Long delay) {
        if (delay == null) {
            return "N/A";
        }

        delay /= MINUTE; // delay in minutes;

        if (delay == 0) {
            delay = 1L;
        }

        long delayMinutes = delay % SIXTY;
        delay /= SIXTY; // delay in hours

        long delayHours = delay % TWENTY_FOUR;
        delay /= TWENTY_FOUR; // delay in days

        long delayDays = delay;
        StringBuilder sb = new StringBuilder();

        if (delayDays != 0) {
            sb.append(delayDays).append(' ').append("day");

            if (delayDays > 1) {
                sb.append('s');
            }

            sb.append(' ');
        }

        if ((delayDays != 0) || (delayHours != 0)) {
            sb.append(delayHours).append(' ').append("hour");

            if (delayHours > 1) {
                sb.append('s');
            }

            sb.append(' ');
        }

        sb.append(delayMinutes).append(' ').append("minute");

        if (delayMinutes > 1) {
            sb.append('s');
        }

        return sb.toString().trim();
    }

    /**
     * <p>
     * Validates the value of a variable. The value can not be empty or contain <code>null</code>.
     * </p>
     *
     * @param types
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     * @param log
     *            The Log object.
     * @param signature
     *            the signature of the method to be logged.
     *
     * @throws IllegalArgumentException
     *             if the value of the variable is empty or contains <code>null</code>.
     *
     * @since 1.3
     */
    public static void checkLateDeliverableTypes(Set<LateDeliverableType> types, String name, Log log,
        String signature) {
        if (types == null) {
            return;
        }

        if (types.isEmpty()) {
            throw Helper.logException(log, signature, new IllegalArgumentException("The parameter '" + name
                + "' should not be empty."));
        }
        for (LateDeliverableType type : types) {
            if (type == null) {
                throw Helper.logException(log, signature, new IllegalArgumentException("The parameter '" + name
                    + "' should not contain null element."));
            }
        }
    }

    /**
     * <p>
     * Validates the value of a variable. The value can not be <code>null</code>.
     * </p>
     *
     * @param value
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     * @param log
     *            The Log object.
     * @param signature
     *            The signature of the method to be logged.
     * @throws IllegalStateException
     *             if the value of the variable is <code>null</code>.
     */
    public static void checkState(Object value, String name, Log log, String signature) {
        if (value == null) {
            throw logException(log, signature, new IllegalStateException("The " + name + " should be set."));
        }
    }

    /**
     * <p>
     * Gets a property value of <code>String</code> type with given key from given <code>ConfigurationObject</code>,
     * or null if not found when the property is not required.
     * </p>
     *
     * @param config
     *            the ConfigurationObject in which the property contains.
     * @param key
     *            the key of the property.
     * @param isRequired
     *            the flag to indicate this property is required or not.
     * @param canBeEmpty
     *            the flag to indicate this property value can be empty or not.
     * @return the property value of String type with given key, or null if not found when the <code>isRequired</code>
     *         is false.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs while accessing ConfigurationObject, or if the property with given key is not
     *             <code>String</code> type, or it is null when the <code>isRequired</code> is true or it is empty.
     */
    public static String getPropertyValue(ConfigurationObject config, String key, boolean isRequired,
        boolean canBeEmpty) {
        try {
            Object value = config.getPropertyValue(key);

            if (value == null) {
                if (isRequired) {
                    throw new LateDeliverablesTrackerConfigurationException("The [" + key
                        + "] property value in configuration object [" + config.getName() + "] is required.");
                } else {
                    return null;
                }
            }

            if (!(value instanceof String)) {
                throw new LateDeliverablesTrackerConfigurationException("The '" + key
                    + "' property value in configuration object '" + config.getName()
                    + "' should be an instance of '" + String.class.getName() + "', but found '"
                    + value.getClass().getName() + "'.");
            }

            if (!canBeEmpty && ((String) value).trim().length() == 0) {
                throw new LateDeliverablesTrackerConfigurationException("The '" + key
                    + "' property value in configuration object '" + config.getName() + "' should not be empty.");
            }

            return (String) value;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException("Fails on accessing configuration object.", e);
        }
    }

    /**
     * <p>
     * Gets a child configuration object with given key from given <code>ConfigurationObject</code>.
     * </p>
     *
     * @param config
     *            the <code>ConfigurationObject</code> in which the child contains.
     * @param key
     *            the key of the child.
     * @return the child configuration object with given key.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs while accessing <code>ConfigurationObject</code>, or if the child with given
     *             key is <code>null</code>.
     */
    public static ConfigurationObject getChildConfig(ConfigurationObject config, String key) {
        try {
            ConfigurationObject child = config.getChild(key);

            if (child == null) {
                throw new LateDeliverablesTrackerConfigurationException("The [" + key
                    + "] child configuration in configuration object [" + config.getName() + "] is required.");
            }

            return child;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails on accessing configuration object when getting child configuration [" + key + "].", e);
        }
    }

    /**
     * <p>
     * Creates an <code>ObjectFactory</code> instance from given <code>ConfigurationObject</code>.
     * </p>
     *
     * @param config
     *            the configuration object used to create <code>ObjectFactory</code>.
     * @return the created <code>ObjectFactory</code> instance.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if any error occurs while creating <code>ObjectFactory</code> instance.
     */
    public static ObjectFactory createObjectFactory(ConfigurationObject config) {
        try {
            ConfigurationObjectSpecificationFactory specFactory = new ConfigurationObjectSpecificationFactory(
                getChildConfig(config, OBJECT_FACTORY_CONFIG));

            return new ObjectFactory(specFactory);
        } catch (IllegalReferenceException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails on creating ObjectFactory instance based on configuration object.", e);
        } catch (SpecificationConfigurationException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails on creating ObjectFactory instance based on configuration object.", e);
        }
    }

    /**
     * <p>
     * Creates an <code>Object</code> from object factory of given type.
     * </p>
     *
     * @param <T>
     *            the generic type of the created object.
     * @param config
     *            the configuration object to get key.
     * @param objectFactory
     *            <code>ObjectFactory</code> to use.
     * @param key
     *            the key to retrieve object key in <code>ObjectFactory</code> configuration through
     *            <code>ConfigurationObject</code>.
     * @param type
     *            the expected type of created object.
     * @return the created object of given type.
     * @throws LateDeliverablesTrackerConfigurationException
     *             if there is any error occurs.
     */
    public static <T> T createObject(ConfigurationObject config, ObjectFactory objectFactory, String key,
        Class<T> type) {
        try {
            // Creates the Object and validates it is of given type.
            Object object = objectFactory.createObject(getPropertyValue(config, key, true, false));

            return type.cast(object);
        } catch (InvalidClassSpecificationException e) {
            throw new LateDeliverablesTrackerConfigurationException(
                "Fails on creating Object using ObjectFactory with key '" + key + "'.", e);
        } catch (ClassCastException e) {
            throw new LateDeliverablesTrackerConfigurationException("The Object configured under key '" + key
                + "' in ObjectFactory configuration should be an instance of '" + type.getName() + "'.", e);
        }
    }

    /**
     * Parses given value to long value.
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Added parameter 'minValue'.</li>
     * </ol>
     * </p>
     *
     * @param value
     *            the value to parse.
     * @param name
     *            the property name.
     * @param minValue
     *            the minimum value (inclusive).
     *
     * @return the long value.
     *
     * @throws LateDeliverablesTrackerConfigurationException
     *             if given value can not parse to long, or the value is less than minValue.
     */
    public static long parseLong(String value, String name, long minValue) {
        try {
            long ret = Long.parseLong(value);
            if (ret < minValue) {
                throw new LateDeliverablesTrackerConfigurationException("The property[" + name
                    + "] value should not be less than '" + minValue + "'.");
            }
            return ret;
        } catch (NumberFormatException e) {
            throw new LateDeliverablesTrackerConfigurationException("Fails to parse value[" + value + " to long.", e);
        }
    }

    /**
     * <p>
     * Logs for entrance into public methods at <code>INFO</code> level, parameters at <code>DEBUG</code> level.
     * </p>
     *
     * @param log
     *            The logger object.
     * @param signature
     *            The signature of the method to be logged.
     * @param paramNames
     *            The names of parameters to log.
     * @param params
     *            The values of parameters to log.
     */
    public static void logEntrance(Log log, String signature, String[] paramNames, Object[] params) {
        if (log == null) {
            return;
        }

        String entranceMessage = MessageFormat.format(MESSAGE_ENTRANCE, new Object[] {signature});
        log.log(Level.DEBUG, entranceMessage);

        if (paramNames != null) {
            logParameters(log, paramNames, params);
        }
    }

    /**
     * <p>
     * Logs for exit from public methods at <code>INFO</code> level, return value at <code>DEBUG</code> level.
     * </p>
     *
     * @param log
     *            The logger object.
     * @param signature
     *            The signature of the method to be logged.
     * @param value
     *            The return value to log (not <code>null</code>).
     * @param timestamp
     *            The timestamp while entering the method.
     */
    public static void logExit(Log log, String signature, Object value, long timestamp) {
        if (log == null) {
            return;
        }

        if (value != null) {
            log.log(Level.DEBUG, MessageFormat.format(MESSAGE_RETURN, String.valueOf(value)));
        }

        String exitMessage = MessageFormat.format(MESSAGE_EXIT, new Object[] {signature,
                System.currentTimeMillis() - timestamp});
        log.log(Level.DEBUG, exitMessage);
    }

    /**
     * <p>
     * Logs the given exception and message at <code>ERROR</code> level.
     * </p>
     *
     * @param <T>
     *            the generic type of exception.
     * @param log
     *            The logger object.
     * @param signature
     *            The signature of the method to be logged.
     * @param exception
     *            The exception to log.
     * @return The passed in exception.
     */
    public static <T extends Throwable> T logException(Log log, String signature, T exception) {
        if (log == null) {
            return exception;
        }

        String errorMessage = MessageFormat.format(MESSAGE_ERROR, new Object[] {signature, exception.getMessage()});

        // error message
        log.log(Level.ERROR, errorMessage);

        // convert the exception stack trace into string
        // note: closing the stringWriter has no effect
        StringWriter buffer = new StringWriter();
        exception.printStackTrace(new PrintWriter(buffer));
        log.log(Level.ERROR, buffer.toString());

        return exception;
    }

    /**
     * <p>
     * Logs the parameters at <code>DEBUG</code> level.
     * </p>
     *
     * @param log
     *            The log object (not <code>null</code>).
     * @param paramNames
     *            The names of parameters to log (not <code>null</code>).
     * @param params
     *            The values of parameters to log (not <code>null</code>).
     */
    private static void logParameters(Log log, String[] paramNames, Object[] params) {
        StringBuffer sb = new StringBuffer("Input parameters: {");

        for (int i = 0; i < params.length; i++) {

            if (i > 0) {
                // Append a comma
                sb.append(", ");
            }

            sb.append(paramNames[i] + " : " + String.valueOf(params[i]));
        }
        sb.append("}.");

        log.log(Level.DEBUG, sb.toString());
    }

    /**
     * <p>
     * Logs given message at <code>INFO</code> level.
     * </p>
     *
     * @param log
     *            The log object.
     * @param message
     *            The message to log.
     */
    public static void logInfo(Log log, String message) {
        if (log == null) {
            return;
        }

        log.log(Level.INFO, message);
    }

    /**
     * <p>
     * Logs given message at <code>ERROR</code> level.
     * </p>
     *
     * @param log
     *            The log object.
     * @param message
     *            The message to log.
     */
    public static void logError(Log log, String message) {
        if (log == null) {
            return;
        }

        log.log(Level.ERROR, message);
    }
}