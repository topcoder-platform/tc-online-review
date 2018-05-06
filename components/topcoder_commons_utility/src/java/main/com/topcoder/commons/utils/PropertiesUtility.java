/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

/**
 * <p>
 * This is a utility class that provides static methods for retrieving properties of different types (String, Integer,
 * Long, Double, Date, Class) fromO Properties instance. This utility throws an exception specified by the caller if
 * required property is missing or cannot be parsed properly. getSubConfiguration() method allows to extract inner
 * configuration from Properties instance (when "childConfigName.childPropertyName" format is used for property keys).
 * </p>
 *
 * <p>
 * <em>API Usage: </em> A part of a sample IssuesTracker#updateIssue() method that uses most of the utilities defined
 * in this component. Note that only some methods of this utility class are demonstrated since their usage is very
 * similar.
 * <pre>
 * private final Log log = LogManager.getLog(getClass().getName());
 *
 * public boolean updateIssue(String issueName, int priority, File paramFile) throws IssueTrackingException {
 *     // Save method entrance timestamp
 *     Date enterTimestamp = new Date();
 *     // Prepare method signature that will appear in logged messages
 *     String signature = &quot;IssuesTracker#updateIssue(String issueName, int priority, File paramFile)&quot;;
 *
 *     // Log method entrance
 *     LoggingWrapperUtility.logEntrance(log, signature,
 *         new String[] {&quot;issueName&quot;, &quot;priority&quot;, &quot;paramFile&quot;},
 *         new Object[] {issueName, priority, paramFile});
 *
 *     try {
 *         // Check input arguments
 *         ParameterCheckUtility.checkNotNullNorEmpty(issueName, &quot;issueName&quot;);
 *         ParameterCheckUtility.checkPositive(priority, &quot;priority&quot;);
 *         ParameterCheckUtility.checkNotNull(paramFile, &quot;paramFile&quot;);
 *         ParameterCheckUtility.checkIsFile(paramFile, &quot;paramFile&quot;);
 *
 *         // Load additional parameters
 *         Properties properties = loadProperties(paramFile);
 *
 *         // Get user ID from the properties
 *         Long userId = PropertiesUtility.getLongProperty(properties, &quot;userId&quot;, true,
 *             IssueTrackingException.class);
 *         // Check whether user ID is positive
 *         ValidationUtility.checkPositive(userId, &quot;The property 'userId'&quot;, IssueTrackingException.class);
 *
 *         boolean result;
 *
 *         // Get cached DB connection
 *         Connection connection = getConnection();
 *
 *         try {
 *             // Retrieve issue type ID from DB
 *             Object[][] queryResult = JDBCUtility.executeQuery(connection,
 *                 &quot;SELECT id FROM issue_types WHERE name = ?&quot;,
 *                 new int[] {Types.VARCHAR},
 *                 new Object[] {issueName},
 *                 new Class&lt;?&gt;[] {Long.class},
 *                 IssueTrackingException.class);
 *
 *             result = (queryResult.length &gt; 0);
 *
 *             if (result) {
 *                 long issueTypeId = (Long) queryResult[0][0];
 *
 *                 // Update issues in DB
 *                 int updatedNum = JDBCUtility.executeUpdate(connection,
 *                     &quot;UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?&quot;,
 *                     new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER},
 *                     new Object[] {priority, issueTypeId, userId},
 *                     IssueTrackingException.class);
 *
 *                 // Commit transaction
 *                 JDBCUtility.commitTransaction(connection, IssueTrackingException.class);
 *
 *                 result = (updatedNum &gt; 0);
 *             }
 *         } catch (IssueTrackingException e) {
 *             try {
 *                 // Rollback transaction
 *                 JDBCUtility.rollbackTransaction(connection, IssueTrackingException.class);
 *             } catch (IssueTrackingException e2) {
 *                 // Log exception at WARN level
 *                 LoggingWrapperUtility.logException(log, signature, e2, false, Level.WARN);
 *
 *                 // Ignore this exception
 *             }
 *             throw e;
 *         } finally {
 *             releaseConnection(connection);
 *         }
 *
 *         // Log method exit
 *         LoggingWrapperUtility.logExit(log, signature, new Object[] {result}, enterTimestamp);
 *
 *         return result;
 *     } catch (IllegalArgumentException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     } catch (IssueTrackingException e) {
 *         // Log and re-throw the exception
 *         throw LoggingWrapperUtility.logException(log, signature, e);
 *     }
 * }
 * </pre>
 *
 * </p>
 *
 * <p>
 * <em>Sub-Configuration Usage:</em><br>
 * Assume some Properties instance contains the following key/value pairs:
 * <pre>
 * retrieverClassName=com.topcoder.commons.utils.CustomRetriever
 * retriever.param1=12345
 * retriever.param2=ABC
 * </pre>
 *
 * Assume that this Properties instance represents configuration of some class that creates an instance of Retriever
 * (this is some sample interface) implementation and passes the retriever configuration to the created instance. The
 * following code can be used to achieve this:
 * <pre>
 * // Get main configuration
 * Properties properties = TestsHelper.loadProperties(new File(TestsHelper.CONFIG_FILE));
 *
 * // Get retriever class by its full name from configuration
 * Class&lt;?&gt; retrieverClass = PropertiesUtility.getClassProperty(properties, &quot;retrieverClassName&quot;,
 *     true, Exception.class);
 *
 * // Get constructor that accepts Properties instance
 * Constructor&lt;?&gt; constructor = retrieverClass.getConstructor(Properties.class);
 *
 * // Get inner retriever configuration
 * Properties retrieverConfig = PropertiesUtility.getSubConfiguration(properties, &quot;retriever&quot;);
 *
 * // retrieverConfig should contain the following key/value pairs:
 * // param1=12345
 * // param2=ABC
 *
 * // Create retriever with use of reflection
 * Retriever retriever = (Retriever) constructor.newInstance(retrieverConfig);
 * </pre>
 *
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe when properties parameters passed to it
 * are used by the caller in thread safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
public class PropertiesUtility {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private PropertiesUtility() {
        // Empty
    }

    /**
     * <p>
     * Retrieves the string property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     *
     * @return the retrieved string property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property is required, but missing.
     */
    public static <T extends Throwable> String getStringProperty(Properties properties, String key, boolean required,
        Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (required && (value == null)) {
            throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
        }

        return value;
    }

    /**
     * <p>
     * Retrieves the delimited strings property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param delimiter
     *            the delimiter regular expression pattern.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     *
     * @return the retrieved strings values (null if property is optional and missing).
     *
     * @throws T
     *             if the property is required, but missing.
     */
    public static <T extends Throwable> String[] getStringsProperty(Properties properties, String key,
        String delimiter, boolean required, Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        return value.split(delimiter, -1);
    }

    /**
     * <p>
     * Retrieves the integer property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     *
     * @return the retrieved integer property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property value has invalid format or is required, but missing.
     */
    public static <T extends Throwable> Integer getIntegerProperty(Properties properties, String key,
        boolean required, Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            throw ExceptionHelper.constructException(exceptionClass,
                getPropertyMessage(key, " should be a valid integer."), e);
        }
    }

    /**
     * <p>
     * Retrieves the long integer property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     * @return the retrieved long integer property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property value has invalid format or is required, but missing.
     */
    public static <T extends Throwable> Long getLongProperty(Properties properties, String key, boolean required,
        Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            throw ExceptionHelper.constructException(exceptionClass,
                getPropertyMessage(key, " should be a valid long integer."), e);
        }
    }

    /**
     * <p>
     * Retrieves the double property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     *
     * @return the retrieved double property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property value has invalid format or is required, but missing.
     */
    public static <T extends Throwable> Double getDoubleProperty(Properties properties, String key, boolean required,
        Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        ParsePosition parsePosition = new ParsePosition(0);

        Number result = NumberFormat.getInstance(Locale.US).parse(value, parsePosition);
        if (parsePosition.getIndex() != value.length()) {
            throw ExceptionHelper.constructException(exceptionClass,
                getPropertyMessage(key, " should be a valid double."));
        }
        return result.doubleValue();
    }

    /**
     * <p>
     * Retrieves the date/time property from the given Properties instance.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     * @param format
     *            the expected date/time format string.
     *
     * @return the retrieved date/time property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property value has invalid format or is required, but missing.
     */
    public static <T extends Throwable> Date getDateProperty(Properties properties, String key, String format,
        boolean required, Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        ParsePosition parsePosition = new ParsePosition(0);
        Date result = new SimpleDateFormat(format, Locale.US).parse(value, parsePosition);
        if ((result == null) || (parsePosition.getIndex() != value.length())) {
            throw ExceptionHelper.constructException(exceptionClass,
                getPropertyMessage(key, " should be in format [", format, "]."));
        }
        return result;
    }

    /**
     * <p>
     * Retrieves the class property from the given Properties instance. Property value is expected to contain a full
     * class name.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown if some error occurred.
     * @param key
     *            the key of the property to be retrieved.
     * @param required
     *            true if property is required, false otherwise (if property is required, but missing, an exception is
     *            thrown).
     * @param exceptionClass
     *            the type of the exception to be thrown if some error occurs.
     * @param properties
     *            the properties container.
     *
     * @return the retrieved class property value (null if property is optional and missing).
     *
     * @throws T
     *             if the property value has invalid format or is required, but missing.
     */
    public static <T extends Throwable> Class<?> getClassProperty(Properties properties, String key,
        boolean required, Class<T> exceptionClass) throws T {
        String value = properties.getProperty(key);
        if (value == null) {
            if (required) {
                throw ExceptionHelper.constructException(exceptionClass, getPropertyMessage(key, " is required."));
            }
            return null;
        }

        try {
            return Class.forName(value);
        } catch (ClassNotFoundException e) {
            throw ExceptionHelper.constructException(exceptionClass,
                getPropertyMessage(key, " contains invalid full class name (", value, ")."), e);
        }
    }

    /**
     * <p>
     * Retrieves the inner configuration from the configuration stored in Properties container.
     * </p>
     *
     * @param properties
     *            the properties with the main configuration.
     * @param configName
     *            the name of the inner configuration.
     *
     * @return the Properties container with the extracted inner configuration (not null).
     */
    public static Properties getSubConfiguration(Properties properties, String configName) {
        String prefix = configName + ".";
        int prefixLen = prefix.length();

        // Create Properties instance for inner configuration:
        Properties result = new Properties();
        for (Object obj : properties.keySet()) {
            if (obj instanceof String) {
                String key = (String) obj;
                if (key.startsWith(prefix)) {
                    // Put key/value pair to the inner configuration:
                    result.put(key.substring(prefixLen), properties.getProperty(key));
                }
            }
        }
        return result;
    }

    /**
     * <p>
     * Retrieves the property message to be used in exception message.
     * </p>
     *
     * @param key
     *            the property key.
     * @param values
     *            the message values.
     *
     * @return the constructed property message.
     */
    private static String getPropertyMessage(String key, String... values) {
        StringBuilder sb = new StringBuilder();

        sb.append("The property '").append(key).append("'");

        for (String value : values) {
            sb.append(value);
        }
        return sb.toString();
    }
}
