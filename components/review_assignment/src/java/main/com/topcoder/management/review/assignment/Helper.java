/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Collection;

import com.topcoder.commons.utils.LoggingWrapperUtility;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigurationObjectSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * Helper class for the component. It provides useful common methods for all the classes in this component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class has no state, and thus it is thread safe.
 * </p>
 *
 * @author zhongqiangzhang
 * @version 1.0
 */
public final class Helper {

    /**
     * <p>
     * Represents &quot;loggerName&quot; property key in configuration.
     * </p>
     */
    public static final String LOGGER_NAME_KEY = "loggerName";

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
     * Checks if the given {@link Object} is <code>null</code>.
     *
     * @param logger
     *            the logger to use
     * @param signature
     *            the method name
     * @param obj
     *            the given {@link Object} to check
     * @param varName
     *            the variable name
     * @throws IllegalArgumentException
     *             if the given {@link Object} is <code>null</code>
     */
    public static void checkNullIAE(Log logger, String signature, Object obj, String varName) {
        if (null == obj) {
            throw logException(logger, signature, new IllegalArgumentException("The '" + varName
                + "' should not be null."));
        }
    }

    /**
     * Checks if the given {@link String} is empty.
     *
     * @param logger
     *            the logger to use
     * @param signature
     *            the method name
     * @param str
     *            the given {@link String} to check
     * @param varName
     *            the variable name
     * @throws IllegalArgumentException
     *             if the given {@link String} is empty
     */
    private static void checkEmptyIAE(Log logger, String signature, String str, String varName) {
        if (str.trim().length() == 0) {
            throw logException(logger, signature, new IllegalArgumentException("The variable '" + varName
                + "' should not be empty."));
        }
    }

    /**
     * Checks if the given {@link String} is <code>null</code> or empty.
     *
     * @param logger
     *            the logger to use
     * @param signature
     *            the method name
     * @param str
     *            the given {@link String} to check
     * @param varName
     *            the variable name
     * @throws IllegalArgumentException
     *             if the given {@link String} is <code>null</code> or empty
     */
    public static void checkNullEmptyIAE(Log logger, String signature, String str, String varName) {
        checkNullIAE(logger, signature, str, varName);
        checkEmptyIAE(logger, signature, str, varName);
    }

    /**
     * <p>
     * Validates the value of a list. The value can not be <code>null</code> or contains <code>null</code>.
     * </p>
     *
     * @param <T>
     *            the element type.
     * @param logger
     *            the log
     * @param signature
     *            the method signature used to log.
     * @param value
     *            the value of the variable to be validated.
     * @param name
     *            the name of the variable to be validated.
     *
     * @throws IllegalArgumentException
     *             if value is <code>null</code> or contains <code>null</code>.
     */
    public static <T> void checkListIAE(Log logger, String signature, Collection<T> value, String name) {
        checkNullIAE(logger, signature, value, name);

        if (value.contains(null)) {
            throw logException(logger, signature, new IllegalArgumentException("'" + name
                + "' should not contain null element."));
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
     * Closes the statement.
     *
     * @param log
     *            the logger
     * @param signature
     *            the signature of the method to be logged.
     * @param statement
     *            the statement to close.
     */
    public static void closeStatement(Log log, String signature, Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // Log exception
            LoggingWrapperUtility.logException(log, signature, e);
        }
    }

    /**
     * Closes the ResultSet.
     *
     * @param log
     *            the logger
     * @param signature
     *            the signature of the method to be logged.
     * @param rs
     *            the ResultSet to close.
     */
    public static void closeResultSet(Log log, String signature, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            // Log exception
            LoggingWrapperUtility.logException(log, signature, e);
        }
    }

    /**
     * Closes the connection.
     *
     * @param log
     *            the logger
     * @param signature
     *            the signature of the method to be logged.
     * @param connection
     *            the connection to close.
     */
    public static void closeConnection(Log log, String signature, Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // Log exception
            LoggingWrapperUtility.logException(log, signature, e);
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
     * @throws ReviewAssignmentConfigurationException
     *             if the property is missing when required is <code>true</code>, not a string, an empty
     *             string or some error occurred.
     */
    public static String getPropertyValue(ConfigurationObject config, String key, boolean required) {
        try {
            if (!config.containsProperty(key)) {
                if (required) {
                    throw new ReviewAssignmentConfigurationException("The property '" + key
                        + "' is required.");
                }

                // Return the default value
                return null;
            }

            Object valueObj = config.getPropertyValue(key);

            if (!(valueObj instanceof String)) {
                throw new ReviewAssignmentConfigurationException("The property '" + key
                    + "' should be a String.");
            }

            String valueStr = ((String) valueObj).trim();

            if (valueStr.length() == 0) {
                // The value is empty
                throw new ReviewAssignmentConfigurationException("The property '" + key
                    + "' can not be empty.");
            }

            return valueStr;
        } catch (ConfigurationAccessException e) {
            throw new ReviewAssignmentConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * <p>
     * Gets list of string property values from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the property key.
     *
     * @return the retrieved property values.
     *
     * @throws ReviewAssignmentConfigurationException
     *             if the property is missing, not a String[], empty or some error occurred.
     */
    public static String[] getPropertyValues(ConfigurationObject config, String key) {
        try {
            if (!config.containsProperty(key)) {
                throw new ReviewAssignmentConfigurationException("The property '" + key + "' is required.");
            }

            Object[] valuesObj = config.getPropertyValues(key);
            int valuesSize = valuesObj.length;

            if (valuesSize == 0) {
                // The value is empty
                throw new ReviewAssignmentConfigurationException("The property '" + key
                    + "' can not be empty.");
            }

            String[] valuesStr = new String[valuesSize];

            for (int i = 0; i < valuesSize; i++) {
                Object value = valuesObj[i];
                if (!(value instanceof String)) {
                    throw new ReviewAssignmentConfigurationException("The property '" + key
                        + "' should be a String[].");
                }

                valuesStr[i] = (String) value;
            }

            return valuesStr;
        } catch (ConfigurationAccessException e) {
            throw new ReviewAssignmentConfigurationException(
                "An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * <p>
     * Gets Long type property values from given configuration object.
     * </p>
     *
     * @param config
     *            the given configuration object.
     * @param key
     *            the property key.
     *
     * @return the retrieved property values.
     *
     * @throws ReviewAssignmentConfigurationException
     *             if the property is missing, not a Long[], empty or some error occurred.
     */
    public static Long[] getLongPropertyValues(ConfigurationObject config, String key) {
        try {
            if (!config.containsProperty(key)) {
                throw new ReviewAssignmentConfigurationException("The property '" + key + "' is required.");
            }

            Object[] valuesObj = config.getPropertyValues(key);
            int valuesSize = valuesObj.length;

            if (valuesSize == 0) {
                // The value is empty
                throw new ReviewAssignmentConfigurationException("The property '" + key
                    + "' can not be empty.");
            }

            Long[] valuesLong = new Long[valuesSize];

            for (int i = 0; i < valuesSize; i++) {
                Object value = valuesObj[i];
                try {
                    valuesLong[i] = Long.valueOf(value.toString());
                } catch (NumberFormatException e) {
                    throw new ReviewAssignmentConfigurationException("The property '" + key
                        + "' should be a Long[].");
                }
            }
            return valuesLong;
        } catch (ConfigurationAccessException e) {
            throw new ReviewAssignmentConfigurationException(
                "An error occurred while accessing the configuration.", e);
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
     * @throws ReviewAssignmentConfigurationException
     *             if any error occurs while accessing <code>ConfigurationObject</code>, or if the child with
     *             given key is <code>null</code>.
     */
    public static ConfigurationObject getChildConfig(ConfigurationObject config, String key) {
        try {
            ConfigurationObject child = config.getChild(key);
            if (child == null) {
                throw new ReviewAssignmentConfigurationException("The [" + key
                    + "] child configuration in configuration object [" + config.getName() + "] is required.");
            }

            return child;
        } catch (ConfigurationAccessException e) {
            throw new ReviewAssignmentConfigurationException(
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
     * @throws ReviewAssignmentConfigurationException
     *             if any error occurs while creating <code>ObjectFactory</code> instance.
     */
    public static ObjectFactory createObjectFactory(ConfigurationObject config) {
        try {
            ConfigurationObjectSpecificationFactory specFactory = new ConfigurationObjectSpecificationFactory(
                getChildConfig(config, OBJECT_FACTORY_CONFIG));

            return new ObjectFactory(specFactory);
        } catch (IllegalReferenceException e) {
            throw new ReviewAssignmentConfigurationException(
                "Fails on creating ObjectFactory instance based on configuration object.", e);
        } catch (SpecificationConfigurationException e) {
            throw new ReviewAssignmentConfigurationException(
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
     * @throws ReviewAssignmentConfigurationException
     *             if there is any error occurs.
     */
    public static <T> T createObject(ConfigurationObject config, ObjectFactory objectFactory, String key,
        Class<T> type) {
        try {
            // Creates the Object and validates it is of given type.
            Object object = objectFactory.createObject(getPropertyValue(config, key, true));

            return type.cast(object);
        } catch (InvalidClassSpecificationException e) {
            throw new ReviewAssignmentConfigurationException(
                "Fails on creating Object using ObjectFactory with key '" + key + "'.", e);
        } catch (ClassCastException e) {
            throw new ReviewAssignmentConfigurationException("The Object configured under key '" + key
                + "' in ObjectFactory configuration should be an instance of '" + type.getName() + "'.", e);
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

        String exitMessage = MessageFormat.format(MESSAGE_EXIT,
            new Object[] { signature, System.currentTimeMillis() - timestamp });
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

        String errorMessage = MessageFormat.format(MESSAGE_ERROR,
            new Object[] { signature, exception.getMessage() });

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
        StringBuilder sb = new StringBuilder("Input parameters: {");

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
     * Logs given message at <code>WARN</code> level.
     * </p>
     *
     * @param log
     *            The log object.
     * @param message
     *            The message to log.
     */
    public static void logWarn(Log log, String message) {
        if (log == null) {
            return;
        }

        log.log(Level.WARN, message);
    }

    /**
     * <p>
     * Logs for entrance into public methods at <code>INFO</code> level, parameters at <code>DEBUG</code>
     * level.
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

        String entranceMessage = MessageFormat.format(MESSAGE_ENTRANCE, new Object[] { signature });
        log.log(Level.DEBUG, entranceMessage);

        if (paramNames != null) {
            logParameters(log, paramNames, params);
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
     * @return the Log instance or <code>null</code> if the logger name is not specified.
     *
     * @throws ReviewAssignmentConfigurationException
     *             if 'loggerName' is not a string, an empty string or some error occurred.
     */
    public static Log getLog(ConfigurationObject config) {
        String loggerName = getPropertyValue(config, LOGGER_NAME_KEY, false);
        return (loggerName == null) ? null : LogManager.getLog(loggerName);

    }

    /**
     * <p>
     * convert the Long objects to long values.
     *
     * @param longs
     *            the list of Long values to convert.
     * @return the convert list of long type values.
     */
    public static long[] convertLong(Collection<Long> longs) {
        long[] convert = new long[longs.size()];
        int i = 0;
        for (Long k : longs) {
            convert[i] = k;
            i++;
        }

        return convert;
    }

    /**
     * <p>
     * Finds ReviewApplicationRole by its ID.
     *
     * @param reviewAuction
     *            the review auction.
     * @param reviewApplicationRoleID
     *            ID of the review application role.
     * @return ReviewApplicationRole or null if not found.
     */
    public static ReviewApplicationRole getReviewApplicationRoleByID(ReviewAuction reviewAuction,
                                                                     long reviewApplicationRoleID) {
        for (ReviewApplicationRole role : reviewAuction.getAuctionType().getApplicationRoles()) {
            if (role.getId() == reviewApplicationRoleID) {
                return role;
            }
        }
        return null;
    }
}
