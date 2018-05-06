/*
 * Copyright (C) 2010-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;

import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.search.builder.filter.AbstractAssociativeFilter;
import com.topcoder.search.builder.filter.AbstractSimpleFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.util.errorhandling.BaseCriticalException;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Helper class for the component. It provides useful common methods for all the classes in this component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author sparemax
 * @version 1.0.7
 */
public final class Helper {
    /**
     * <p>
     * Represents the dot.
     * </p>
     */
    public static final String DOT = ".";

    /**
     * <p>
     * Represents the comma.
     * </p>
     */
    private static final String COMMA = ", ";

    /**
     * <p>
     * Represents the property key 'loggerName'.
     * </p>
     */
    private static final String KEY_LOGGER_NAME = "loggerName";

    /**
     * <p>
     * Represents the entrance message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the method signature.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_ENTRANCE = "Entering method [%1$s].";

    /**
     * <p>
     * Represents the exit message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the method signature.</li>
     * <li>the error message.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_EXIT = "Exiting method [%1$s], time spent in the method: %2$s milliseconds.";

    /**
     * <p>
     * Represents the error message.
     * </p>
     *
     * <p>
     * Arguments:
     * <ol>
     * <li>the method signature.</li>
     * <li>the call duration time.</li>
     * </ol>
     * </p>
     */
    private static final String MESSAGE_ERROR = "Error in method [%1$s], details: %2$s";

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
     * Validates the value of a variable. The value can not be <code>null</code>.
     * </p>
     *
     * @param value
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     *
     * @throws IllegalArgumentException
     *             if the value of the variable is <code>null</code>.
     */
    public static void checkNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException("'" + name + "' should not be null.");
        }
    }

    /**
     * <p>
     * Validates the value of a variable. The value should be positive.
     * </p>
     *
     * @param value
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     *
     * @throws IllegalArgumentException
     *             if value is not positive.
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException("'" + name + "' should be positive.");
        }
    }

    /**
     * <p>
     * Gets a Log instance with the configuration object.
     * </p>
     *
     * @param config
     *            the configuration object.
     *
     *@return the Log instance or <code>null</code> if the logger name is not specified.
     *
     * @throws LateDeliverableManagementConfigurationException
     *             if 'loggerName' is not a string, an empty string or some error occurred.
     */
    public static Log getLog(ConfigurationObject config) {
        String loggerName = getProperty(config, KEY_LOGGER_NAME, false);
        return (loggerName == null) ? null : LogManager.getLog(loggerName);

    }

    /**
     * <p>
     * Gets a child ConfigurationObject from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the child name.
     *
     * @return the retrieved child ConfigurationObject.
     *
     * @throws LateDeliverableManagementConfigurationException
     *             if the child ConfigurationObject is missing or some error occurred.
     */
    public static ConfigurationObject getChildConfig(ConfigurationObject config, String key) {
        try {
            ConfigurationObject child = config.getChild(key);

            if (child == null) {
                throw new LateDeliverableManagementConfigurationException("The child '" + key + "' of '"
                    + config.getName() + "' is required.");
            }

            return child;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverableManagementConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * <p>
     * Gets a property value from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the property key.
     * @param required
     *            the flag indicates whether the property is required.
     *
     * @return the retrieved property value or <code>null</code> if the optional property is not present.
     *
     * @throws LateDeliverableManagementConfigurationException
     *             if the property is missing when required is <code>true</code>, not a string, an empty string or
     *             some error occurred.
     */
    public static String getProperty(ConfigurationObject config, String key, boolean required) {
        try {
            if (!config.containsProperty(key)) {
                if (required) {
                    throw new LateDeliverableManagementConfigurationException("The property '" + key
                        + "' is required.");
                }

                // Return the default value
                return null;
            }

            Object valueObj = config.getPropertyValue(key);

            if (!(valueObj instanceof String)) {
                throw new LateDeliverableManagementConfigurationException("The property '" + key
                    + "' should be a String.");
            }

            String valueStr = ((String) valueObj).trim();

            if (valueStr.length() == 0) {
                // The value is empty
                throw new LateDeliverableManagementConfigurationException("The property '" + key
                    + "' can not be empty.");
            }

            return valueStr;
        } catch (ConfigurationAccessException e) {
            throw new LateDeliverableManagementConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * <p>
     * Concatenates the given values to a string.
     * </p>
     *
     * @param values
     *            the values.
     *
     * @return the result.
     */
    public static String concat(Object... values) {
        StringBuilder sb = new StringBuilder();

        for (Object value : values) {
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     * <p>
     * Logs for entrance into public method and parameters at <code>DEBUG</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * @param log
     *            the logger.
     * @param signature
     *            the signature of the method to log.
     * @param paramNames
     *            the names of parameters to log.
     * @param paramValues
     *            the values of parameters to log.
     */
    public static void logEntrance(Log log, String signature, String[] paramNames, Object[] paramValues) {
        if (log == null) {
            // No logging
            return;
        }

        log.log(Level.DEBUG, String.format(MESSAGE_ENTRANCE, signature));

        if (paramNames != null) {
            // Log parameters
            StringBuilder sb = new StringBuilder("Input parameters[");
            int paramNamesLen = paramNames.length;
            for (int i = 0; i < paramNamesLen; i++) {
                if (i != 0) {
                    // Append a comma
                    sb.append(COMMA);
                }
                Object paramValue = paramValues[i];
                sb.append(paramNames[i]).append(":").append(
                    (paramValue instanceof Filter) ? toString((Filter) paramValue) : paramValue);
            }
            sb.append("]");

            log.log(Level.DEBUG, sb.toString());
        }
    }

    /**
     * <p>
     * Logs for exit from public method and return value at <code>DEBUG</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * @param log
     *            the logger.
     * @param signature
     *            the signature of the method to log.
     * @param value
     *            the return value to log.
     * @param enterTimestamp
     *            the timestamp while entering the method.
     */
    public static void logExit(Log log, String signature, Object[] value, Date enterTimestamp) {
        if (log == null) {
            // No logging
            return;
        }

        log.log(Level.DEBUG, String.format(MESSAGE_EXIT, signature, System.currentTimeMillis()
            - enterTimestamp.getTime()));

        if (value != null) {
            // Log return value
            log.log(Level.DEBUG, "Output parameter: " + value[0]);
        }
    }

    /**
     * <p>
     * Logs the given exception and message at <code>ERROR</code> level.
     * </p>
     *
     * <p>
     * <em>NOTE:</em> Logging is NOT performed if log is <code>null</code>.
     * </p>
     *
     * @param <T>
     *            the exception type.
     * @param log
     *            the logger.
     * @param signature
     *            the signature of the method to log.
     * @param e
     *            the exception to log.
     * @param message
     *            the message to log.
     *
     * @return the passed in exception.
     */
    public static <T extends Throwable> T logException(Log log, String signature, T e, String message) {
        if (log != null) {
            // Log exception at ERROR level
            log.log(Level.ERROR, String.format(MESSAGE_ERROR, signature, message) + getStackTrace(e));

            if (e instanceof BaseCriticalException) {
                ((BaseCriticalException) e).setLogged(true);
            }
        }

        return e;
    }

    /**
     * <p>
     * Converts the Filter object to a string.
     * </p>
     *
     * @param obj
     *            the Filter object (not <code>null</code>).
     *
     * @return the converted string.
     */
    @SuppressWarnings("unchecked")
    private static String toString(Filter obj) {
        StringBuilder sb = new StringBuilder();

        sb.append(obj.getClass().getSimpleName()).append("{");

        if (obj instanceof NotFilter) {
            NotFilter notFilter = (NotFilter) obj;

            sb.append("filterToNegate:").append(toString(notFilter.getFilter()));

        } else if (obj instanceof InFilter) {
            InFilter inFilter = (InFilter) obj;

            sb.append("name:").append(inFilter.getName()).append(COMMA)
                .append("values:").append(inFilter.getList());

        } else if (obj instanceof LikeFilter) {
            LikeFilter likeFilter = (LikeFilter) obj;

            sb.append("name:").append(likeFilter.getName()).append(COMMA)
                .append("value:").append(likeFilter.getValue()).append(COMMA)
                .append("escapeCharacter:").append(likeFilter.getEscapeCharacter());

        } else if (obj instanceof AbstractSimpleFilter) {
            AbstractSimpleFilter simpleFilter = (AbstractSimpleFilter) obj;

            sb.append("name:").append(simpleFilter.getName()).append(COMMA)
                .append("value:").append(simpleFilter.getValue());
        } else if (obj instanceof AbstractAssociativeFilter) {
            AbstractAssociativeFilter associativeFilter = (AbstractAssociativeFilter) obj;

            List filters = associativeFilter.getFilters();
            sb.append("filters:").append("[");
            for (int i = 0; i < filters.size(); i++) {
                if (i != 0) {
                    // Append a comma
                    sb.append(COMMA);
                }
                sb.append(toString((Filter) filters.get(i)));
            }
            sb.append("]");

        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * <p>
     * Returns the exception stack trace string.
     * </p>
     *
     * @param cause
     *            the exception to be recorded.
     *
     * @return the exception stack trace string.
     */
    private static String getStackTrace(Throwable cause) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        // Print a new line
        ps.println();
        cause.printStackTrace(ps);

        return out.toString();
    }
}
