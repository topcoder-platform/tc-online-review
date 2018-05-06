/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * This is a utility class that provides static methods for checking whether some arbitrary value meets specific
 * criteria (not null, not empty, positive, negative, etc). If criteria is not met, this utility throws an exception
 * of type specified by the caller.
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
 * <strong>Thread Safety: </strong> This class is immutable and thread safe when collection parameters passed to it
 * are used by the caller in thread safe manner.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0
 */
public class ValidationUtility {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private ValidationUtility() {
        // Empty
    }

    /**
     * <p>
     * Checks whether the given value is not null. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is null.
     */
    public static <T extends Throwable> void checkNotNull(Object value, String name, Class<T> exceptionClass)
        throws T {
        if (value == null) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be null.");
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (without trimming). And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is empty.
     */
    public static <T extends Throwable> void checkNotEmpty(String value, String name, Class<T> exceptionClass)
        throws T {
        if (value != null) {
            checkNotEmptyString(value, name, exceptionClass);
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (after trimming). And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is empty.
     */
    public static <T extends Throwable> void checkNotEmptyAfterTrimming(String value, String name,
        Class<T> exceptionClass) throws T {
        if (value != null) {
            checkNotEmptyStringAfterTrimming(value, name, exceptionClass);
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not null, nor empty (without trimming). And if this condition is not
     * met, the specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is null or empty.
     */
    public static <T extends Throwable> void checkNotNullNorEmpty(String value, String name, Class<T> exceptionClass)
        throws T {
        checkNotNull(value, name, exceptionClass);
        checkNotEmptyString(value, name, exceptionClass);
    }

    /**
     * <p>
     * Checks whether the given string value is not null, nor empty (after trimming). And if this condition is not
     * met, the specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is null or empty.
     */
    public static <T extends Throwable> void checkNotNullNorEmptyAfterTrimming(String value, String name,
        Class<T> exceptionClass) throws T {
        checkNotNull(value, name, exceptionClass);
        checkNotEmptyStringAfterTrimming(value, name, exceptionClass);
    }

    /**
     * <p>
     * Checks whether the given value is an instance of the specified type. And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param expectedType
     *            the expected type of the value.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not an instance of the expected type.
     */
    public static <T extends Throwable> void checkInstance(Object value, Class<?> expectedType, String name,
        Class<T> exceptionClass) throws T {
        if (!expectedType.isInstance(value)) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name,
                " should be an instance of ", expectedType.getName(), "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is null or an instance of the specified type. And if this condition is not met,
     * the specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param expectedType
     *            the expected type of the value.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not null and not an instance of the expected type.
     */
    public static <T extends Throwable> void checkNullOrInstance(Object value, Class<?> expectedType, String name,
        Class<T> exceptionClass) throws T {
        if ((value != null) && (!expectedType.isInstance(value))) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name,
                " should be null or an instance of ", expectedType.getName(), "."));
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing file or directory. And if this condition is not
     * met, the specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param file
     *            the File instance to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given file value represents a not existing file or directory.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the file or directory.
     */
    public static <T extends Throwable> void checkExists(File file, String name, Class<T> exceptionClass) throws T {
        if ((file != null) && (!file.exists())) {
            throw ExceptionHelper.constructException(exceptionClass, name
                + " should point to an existing file or directory.");
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing file. And if this condition is not met, the
     * specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param file
     *            the File instance to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given file value represents a not existing file.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the file.
     */
    public static <T extends Throwable> void checkIsFile(File file, String name, Class<T> exceptionClass) throws T {
        if ((file != null) && (!file.isFile())) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should point to an existing file.");
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing directory. And if this condition is not met, the
     * specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param file
     *            the File instance to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given file value represents a not existing directory.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the directory.
     */
    public static <T extends Throwable> void checkIsDirectory(File file, String name, Class<T> exceptionClass)
        throws T {
        if ((file != null) && (!file.isDirectory())) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should point to an existing directory.");
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if collection is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param collection
     *            the collection to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given collection is empty.
     */
    public static <T extends Throwable> void checkNotEmpty(Collection<?> collection, String name,
        Class<T> exceptionClass) throws T {
        if (collection != null) {
            checkNotEmptyCollection(collection, name, exceptionClass);
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not null, nor empty. And if this condition is not met, the specified
     * exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param collection
     *            the collection to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given collection is null or empty.
     */
    public static <T extends Throwable> void checkNotNullNorEmpty(Collection<?> collection, String name,
        Class<T> exceptionClass) throws T {
        checkNotNull(collection, name, exceptionClass);
        checkNotEmptyCollection(collection, name, exceptionClass);
    }

    /**
     * <p>
     * Checks whether the given map value is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param map
     *            the map to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map is empty.
     */
    public static <T extends Throwable> void checkNotEmpty(Map<?, ?> map, String name, Class<T> exceptionClass)
        throws T {
        if (map != null) {
            checkNotEmptyMap(map, name, exceptionClass);
        }
    }

    /**
     * <p>
     * Checks whether the given map is not null, nor empty. And if this condition is not met, the specified exception
     * is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param map
     *            the map to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map is null or empty.
     */
    public static <T extends Throwable> void checkNotNullNorEmpty(Map<?, ?> map, String name, Class<T> exceptionClass)
        throws T {
        checkNotNull(map, name, exceptionClass);
        checkNotEmptyMap(map, name, exceptionClass);
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain null elements. And if this condition is not met, the
     * specified exception is thrown. Note that if collection is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param collection
     *            the collection to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given collection contains null element.
     */
    public static <T extends Throwable> void checkNotNullElements(Collection<?> collection, String name,
        Class<T> exceptionClass) throws T {
        if ((collection != null) && Helper.containNull(collection)) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain null.");
        }
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain empty elements (strings, collections, maps). And if this
     * condition is not met, the specified exception is thrown. Note that if collection is null, exception is not
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param collection
     *            the collection to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given collection contains an empty element (string, collection or map).
     */
    public static <T extends Throwable> void checkNotEmptyElements(Collection<?> collection, boolean trimStrings,
        String name, Class<T> exceptionClass) throws T {
        if ((collection != null) && Helper.containEmpty(collection, trimStrings)) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain empty element.");
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain a null key. And if this condition is not met, the specified
     * exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param map
     *            the value to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map contains null key.
     */
    public static <T extends Throwable> void checkNotNullKeys(Map<?, ?> map, String name, Class<T> exceptionClass)
        throws T {
        if ((map != null) && Helper.containNull(map.keySet())) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain null key.");
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain a null value. And if this condition is not met, the specified
     * exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param map
     *            the map to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map contains null value.
     */
    public static <T extends Throwable> void checkNotNullValues(Map<?, ?> map, String name, Class<T> exceptionClass)
        throws T {
        if ((map != null) && Helper.containNull(map.values())) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain null value.");
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain empty keys (strings, collection, maps). And if this condition is
     * not met, the specified exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param map
     *            the map to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map contains an empty key (string, collection or map).
     */
    public static <T extends Throwable> void checkNotEmptyKeys(Map<?, ?> map, boolean trimStrings, String name,
        Class<T> exceptionClass) throws T {
        if ((map != null) && Helper.containEmpty(map.keySet(), trimStrings)) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain empty key.");
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain empty values (strings, collection, maps). And if this condition is
     * not met, the specified exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param map
     *            the map to be checked.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map contains an empty value (string, collection or map).
     */
    public static <T extends Throwable> void checkNotEmptyValues(Map<?, ?> map, boolean trimStrings, String name,
        Class<T> exceptionClass) throws T {
        if ((map != null) && Helper.containEmpty(map.values(), trimStrings)) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not contain empty value.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not negative.
     */
    public static <T extends Throwable> void checkNegative(double value, String name, Class<T> exceptionClass)
        throws T {
        if (value >= 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not positive.
     */
    public static <T extends Throwable> void checkPositive(double value, String name, Class<T> exceptionClass)
        throws T {
        if (value <= 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be positive.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is negative.
     */
    public static <T extends Throwable> void checkNotNegative(double value, String name, Class<T> exceptionClass)
        throws T {
        if (value < 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be not negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is positive.
     */
    public static <T extends Throwable> void checkNotPositive(double value, String name, Class<T> exceptionClass)
        throws T {
        if (value > 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be not positive.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not equal to zero. And if this condition is not met, the specified exception
     * is thrown.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Don't forget about "Floating-Point Accuracy/Comparison" problems when checking floating point
     * numbers.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is equal to 0.
     */
    public static <T extends Throwable> void checkNotZero(double value, String name, Class<T> exceptionClass)
        throws T {
        if (value == 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be equal to 0.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is greater than (greater than or equal to, if inclusive is true) than the
     * specified number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Don't forget about "Floating-Point Accuracy/Comparison" problems when checking floating point
     * numbers. Inclusive comparison is recommended to be used with integral types only.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param inclusive
     *            true if "greater than or equal to" check should be performed, false if "greater than" check should
     *            be performed.
     * @param name
     *            the name associated with the value.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws T
     *             if the given value is not greater than (not greater than and not equal to, if inclusive is true)
     *             than the specified number.
     */
    public static <T extends Throwable> void checkGreaterThan(double value, double number, boolean inclusive,
        String name, Class<T> exceptionClass) throws T {
        if (inclusive ? (value < number) : (value <= number)) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be greater than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is less than (less than or equal to, if inclusive is true) than the specified
     * number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Don't forget about "Floating-Point Accuracy/Comparison" problems when checking floating point
     * numbers. Inclusive comparison is recommended to be used with integral types only.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param inclusive
     *            true if "less than or equal to" check should be performed, false if "less than" check should be
     *            performed.
     * @param name
     *            the name associated with the value.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws T
     *             if the given value is not less than (not less than and not equal to, if inclusive is true) than the
     *             specified number.
     */
    public static <T extends Throwable> void checkLessThan(double value, double number, boolean inclusive,
        String name, Class<T> exceptionClass) throws T {
        if (inclusive ? (value > number) : (value >= number)) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be less than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is in the specified range. And if this condition is not met, the specified
     * exception is thrown.
     * </p>
     *
     * <p>
     * <em>NOTE: </em> Don't forget about "Floating-Point Accuracy/Comparison" problems when checking floating point
     * numbers. Inclusive comparison is recommended to be used with integral types only.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param to
     *            the end value of the range.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param toInclusive
     *            true if end value is included into the range, false otherwise.
     * @param fromInclusive
     *            true if start value is included into the range, false otherwise.
     * @param from
     *            the start value of the range.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is out of the specified range.
     */
    public static <T extends Throwable> void checkInRange(double value, double from, double to,
        boolean fromInclusive, boolean toInclusive, String name, Class<T> exceptionClass) throws T {
        if ((fromInclusive ? (value < from) : (value <= from)) || (toInclusive ? (value > to) : (value >= to))) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be in the range ",
                (fromInclusive ? "[" : "("), from, ", ", to, (toInclusive ? "]" : ").")));
        }
    }

    /**
     * <p>
     * Checks whether the given value is negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not negative.
     */
    public static <T extends Throwable> void checkNegative(long value, String name, Class<T> exceptionClass) throws T {
        if (value >= 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is not positive.
     */
    public static <T extends Throwable> void checkPositive(long value, String name, Class<T> exceptionClass) throws T {
        if (value <= 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be positive.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is negative.
     */
    public static <T extends Throwable> void checkNotNegative(long value, String name, Class<T> exceptionClass)
        throws T {
        if (value < 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be not negative.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is positive.
     */
    public static <T extends Throwable> void checkNotPositive(long value, String name, Class<T> exceptionClass)
        throws T {
        if (value > 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should be not positive.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is not equal to zero. And if this condition is not met, the specified exception
     * is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is equal to 0.
     */
    public static <T extends Throwable> void checkNotZero(long value, String name, Class<T> exceptionClass) throws T {
        if (value == 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be equal to 0.");
        }
    }

    /**
     * <p>
     * Checks whether the given value is greater than (greater than or equal to, if inclusive is true) than the
     * specified number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param inclusive
     *            true if "greater than or equal to" check should be performed, false if "greater than" check should
     *            be performed.
     * @param name
     *            the name associated with the value.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws T
     *             if the given value is not greater than (not greater than and not equal to, if inclusive is true)
     *             than the specified number.
     */
    public static <T extends Throwable> void checkGreaterThan(long value, long number, boolean inclusive,
        String name, Class<T> exceptionClass) throws T {
        if (inclusive ? (value < number) : (value <= number)) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be greater than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is less than (less than or equal to, if inclusive is true) than the specified
     * number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param inclusive
     *            true if "less than or equal to" check should be performed, false if "less than" check should be
     *            performed.
     * @param name
     *            the name associated with the value.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws T
     *             if the given value is not less than (not less than and not equal to, if inclusive is true) than the
     *             specified number.
     */
    public static <T extends Throwable> void checkLessThan(long value, long number, boolean inclusive, String name,
        Class<T> exceptionClass) throws T {
        if (inclusive ? (value > number) : (value >= number)) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be less than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is in the specified range. And if this condition is not met, the specified
     * exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param to
     *            the end value of the range.
     * @param value
     *            the value to be checked.
     * @param exceptionClass
     *            the exception class.
     * @param toInclusive
     *            true if end value is included into the range, false otherwise.
     * @param fromInclusive
     *            true if start value is included into the range, false otherwise.
     * @param from
     *            the start value of the range.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is out of the specified range.
     */
    public static <T extends Throwable> void checkInRange(long value, long from, long to, boolean fromInclusive,
        boolean toInclusive, String name, Class<T> exceptionClass) throws T {
        if ((fromInclusive ? (value < from) : (value <= from)) || (toInclusive ? (value > to) : (value >= to))) {
            throw ExceptionHelper.constructException(exceptionClass, Helper.concat(name, " should be in the range ",
                (fromInclusive ? "[" : "("), from, ", ", to, (toInclusive ? "]" : ").")));
        }
    }

    /**
     * <p>
     * Checks whether the given map value is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param map
     *            the map to be checked (not <code>null</code>).
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given map is empty.
     */
    private static <T extends Throwable> void checkNotEmptyMap(Map<?, ?> map, String name, Class<T> exceptionClass)
        throws T {
        if (map.isEmpty()) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if collection is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param exceptionClass
     *            the exception class.
     * @param collection
     *            the collection to be checked (not <code>null</code>).
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given collection is empty.
     */
    private static <T extends Throwable> void checkNotEmptyCollection(Collection<?> collection, String name,
        Class<T> exceptionClass) throws T {
        if (collection.isEmpty()) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be empty.");
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (after trimming). And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is not thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked (not <code>null</code>).
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is empty.
     */
    private static <T extends Throwable> void checkNotEmptyStringAfterTrimming(String value, String name,
        Class<T> exceptionClass) throws T {
        if (value.trim().length() == 0) {
            throw ExceptionHelper.constructException(exceptionClass, name + " should not be empty (after trimming).");
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (without trimming). And if this condition is not met, the
     * specified exception is thrown.
     * </p>
     *
     * @param <T>
     *            the type of the exception to be thrown when validation fails.
     * @param value
     *            the value to be checked (not <code>null</code>).
     * @param exceptionClass
     *            the exception class.
     * @param name
     *            the name associated with the value.
     *
     * @throws T
     *             if the given value is empty.
     */
    private static <T extends Throwable> void checkNotEmptyString(String value, String name, Class<T> exceptionClass)
        throws T {
        if (value.length() == 0) {
            throw ExceptionHelper.constructException(exceptionClass, name
                + " should not be empty (without trimming).");
        }
    }
}
