/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * <p>
 * This is a utility class that provides static methods for checking whether some parameter value meets specific
 * criteria (not null, not empty, positive, negative, etc). If criteria is not met, this utility throws
 * IllegalArgumentException.
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
public class ParameterCheckUtility {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private ParameterCheckUtility() {
        // Empty
    }

    /**
     * <p>
     * Checks whether the given value is not null. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is null.
     */
    public static void checkNotNull(Object value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be null."));
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (without trimming). And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is not thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is empty.
     */
    public static void checkNotEmpty(String value, String name) {
        if (value != null) {
            checkNotEmptyString(value, name);
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (after trimming). And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is not thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is empty.
     */
    public static void checkNotEmptyAfterTrimming(String value, String name) {
        if (value != null) {
            checkNotEmptyStringAfterTrimming(value, name);
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not null, nor empty (without trimming). And if this condition is not
     * met, the specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is null or empty.
     */
    public static void checkNotNullNorEmpty(String value, String name) {
        checkNotNull(value, name);
        checkNotEmptyString(value, name);
    }

    /**
     * <p>
     * Checks whether the given string value is not null, nor empty (after trimming). And if this condition is not
     * met, the specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is null or empty.
     */
    public static void checkNotNullNorEmptyAfterTrimming(String value, String name) {
        checkNotNull(value, name);
        checkNotEmptyStringAfterTrimming(value, name);
    }

    /**
     * <p>
     * Checks whether the given value is an instance of the specified type. And if this condition is not met, the
     * specified exception is thrown. Note that if value is null, exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param expectedType
     *            the expected type of the value.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not an instance of the expected type.
     */
    public static void checkInstance(Object value, Class<?> expectedType, String name) {
        if (!expectedType.isInstance(value)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be an instance of ",
                expectedType.getName(), "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is null or an instance of the specified type. And if this condition is not met,
     * the specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param expectedType
     *            the expected type of the value.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not null and not an instance of the expected type.
     */
    public static void checkNullOrInstance(Object value, Class<?> expectedType, String name) {
        if ((value != null) && (!expectedType.isInstance(value))) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be null or an instance of ",
                expectedType.getName(), "."));
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing file or directory. And if this condition is not
     * met, the specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param file
     *            the value of File parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given file value represents a not existing file or directory.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the file or directory.
     */
    public static void checkExists(File file, String name) {
        if ((file != null) && (!file.exists())) {
            throw new IllegalArgumentException(getParamValueMessage(name)
                + " should point to an existing file or directory.");
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing file. And if this condition is not met, the
     * specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param file
     *            the value of File parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given file value represents a not existing file.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the file.
     */
    public static void checkIsFile(File file, String name) {
        if ((file != null) && (!file.isFile())) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should point to an existing file."));
        }
    }

    /**
     * <p>
     * Checks whether the given File instance points to an existing directory. And if this condition is not met, the
     * specified exception is thrown. Note that if file is null, exception is not thrown.
     * </p>
     *
     * @param file
     *            the value of File parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given file value represents a not existing directory.
     * @throws SecurityException
     *             if a security manager exists and its <code>{@link
     *          java.lang.SecurityManager#checkRead(java.lang.String)}</code>
     *             method denies read access to the directory.
     */
    public static void checkIsDirectory(File file, String name) {
        if ((file != null) && (!file.isDirectory())) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should point to an existing directory."));
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if collection is null, exception is not thrown.
     * </p>
     *
     * @param collection
     *            the value of collection parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given collection is empty.
     */
    public static void checkNotEmpty(Collection<?> collection, String name) {
        if (collection != null) {
            checkNotEmptyCollection(collection, name);
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not null, nor empty. And if this condition is not met, the specified
     * exception is thrown.
     * </p>
     *
     * @param collection
     *            the value of collection parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given collection is null or empty.
     */
    public static void checkNotNullNorEmpty(Collection<?> collection, String name) {
        checkNotNull(collection, name);
        checkNotEmptyCollection(collection, name);
    }

    /**
     * <p>
     * Checks whether the given map value is not empty. And if this condition is not met, the specified exception is
     * thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param map
     *            the value of map parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map is empty.
     */
    public static void checkNotEmpty(Map<?, ?> map, String name) {
        if (map != null) {
            checkNotEmptyMap(map, name);
        }
    }

    /**
     * <p>
     * Checks whether the given map is not null, nor empty. And if this condition is not met, the specified exception
     * is thrown.
     * </p>
     *
     * @param map
     *            the value of map parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map is null or empty.
     */
    public static void checkNotNullNorEmpty(Map<?, ?> map, String name) {
        checkNotNull(map, name);
        checkNotEmptyMap(map, name);
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain null elements. And if this condition is not met, the
     * specified exception is thrown. Note that if collection is null, exception is not thrown.
     * </p>
     *
     * @param collection
     *            the value of collection parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given collection contains null element.
     */
    public static void checkNotNullElements(Collection<?> collection, String name) {
        if ((collection != null) && Helper.containNull(collection)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain null."));
        }
    }

    /**
     * <p>
     * Checks whether the given collection doesn't contain empty elements (strings, collections, maps). And if this
     * condition is not met, the specified exception is thrown. Note that if collection is null, exception is not
     * thrown.
     * </p>
     *
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param collection
     *            the value of collection parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given collection contains an empty element (string, collection or map).
     */
    public static void checkNotEmptyElements(Collection<?> collection, boolean trimStrings, String name) {
        if ((collection != null) && Helper.containEmpty(collection, trimStrings)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain empty element."));
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain a null key. And if this condition is not met, the specified
     * exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param map
     *            the value to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map contains null key.
     */
    public static void checkNotNullKeys(Map<?, ?> map, String name) {
        if ((map != null) && Helper.containNull(map.keySet())) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain null key."));
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain a null value. And if this condition is not met, the specified
     * exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param map
     *            the value of map parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map contains null value.
     */
    public static void checkNotNullValues(Map<?, ?> map, String name) {
        if ((map != null) && Helper.containNull(map.values())) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain null value."));
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain empty keys (strings, collection, maps). And if this condition is
     * not met, the specified exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param map
     *            the value of map parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map contains an empty key (string, collection or map).
     */
    public static void checkNotEmptyKeys(Map<?, ?> map, boolean trimStrings, String name) {
        if ((map != null) && Helper.containEmpty(map.keySet(), trimStrings)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain empty key."));
        }
    }

    /**
     * <p>
     * Checks whether the given map doesn't contain empty values (strings, collection, maps). And if this condition is
     * not met, the specified exception is thrown. Note that if map is null, exception is not thrown.
     * </p>
     *
     * @param trimStrings
     *            true if strings should be trimmed before emptiness check, false otherwise.
     * @param map
     *            the value of map parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map contains an empty value (string, collection or map).
     */
    public static void checkNotEmptyValues(Map<?, ?> map, boolean trimStrings, String name) {
        if ((map != null) && Helper.containEmpty(map.values(), trimStrings)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not contain empty value."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not negative.
     */
    public static void checkNegative(double value, String name) {
        if (value >= 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be negative."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not positive.
     */
    public static void checkPositive(double value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be positive."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is not negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is negative.
     */
    public static void checkNotNegative(double value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be not negative."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is not positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is positive.
     */
    public static void checkNotPositive(double value, String name) {
        if (value > 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be not positive."));
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
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is equal to 0.
     */
    public static void checkNotZero(double value, String name) {
        if (value == 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be equal to 0."));
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
     * @param value
     *            the value of parameter to be checked.
     * @param inclusive
     *            true if "greater than or equal to" check should be performed, false if "greater than" check should
     *            be performed.
     * @param name
     *            the parameter name.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws IllegalArgumentException
     *             if the given value is not greater than (not greater than and not equal to, if inclusive is true)
     *             than the specified number.
     */
    public static void checkGreaterThan(double value, double number, boolean inclusive, String name) {
        if (inclusive ? (value < number) : (value <= number)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be greater than ",
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
     * @param value
     *            the value of parameter to be checked.
     * @param inclusive
     *            true if "less than or equal to" check should be performed, false if "less than" check should be
     *            performed.
     * @param name
     *            the parameter name.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws IllegalArgumentException
     *             if the given value is not less than (not less than and not equal to, if inclusive is true) than the
     *             specified number.
     */
    public static void checkLessThan(double value, double number, boolean inclusive, String name) {
        if (inclusive ? (value > number) : (value >= number)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be less than ",
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
     * @param to
     *            the end value of the range.
     * @param value
     *            the value of parameter to be checked.
     * @param toInclusive
     *            true if end value is included into the range, false otherwise.
     * @param fromInclusive
     *            true if start value is included into the range, false otherwise.
     * @param from
     *            the start value of the range.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is out of the specified range.
     */
    public static void checkInRange(double value, double from, double to, boolean fromInclusive, boolean toInclusive,
        String name) {
        if ((fromInclusive ? (value < from) : (value <= from)) || (toInclusive ? (value > to) : (value >= to))) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be in the range ",
                (fromInclusive ? "[" : "("), from, ", ", to, (toInclusive ? "]" : ").")));
        }
    }

    /**
     * <p>
     * Checks whether the given value is negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not negative.
     */
    public static void checkNegative(long value, String name) {
        if (value >= 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be negative."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is not positive.
     */
    public static void checkPositive(long value, String name) {
        if (value <= 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be positive."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is not negative. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is negative.
     */
    public static void checkNotNegative(long value, String name) {
        if (value < 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be not negative."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is not positive. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is positive.
     */
    public static void checkNotPositive(long value, String name) {
        if (value > 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be not positive."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is not equal to zero. And if this condition is not met, the specified exception
     * is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is equal to 0.
     */
    public static void checkNotZero(long value, String name) {
        if (value == 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be equal to 0."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is greater than (greater than or equal to, if inclusive is true) than the
     * specified number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param inclusive
     *            true if "greater than or equal to" check should be performed, false if "greater than" check should
     *            be performed.
     * @param name
     *            the parameter name.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws IllegalArgumentException
     *             if the given value is not greater than (not greater than and not equal to, if inclusive is true)
     *             than the specified number.
     */
    public static void checkGreaterThan(long value, long number, boolean inclusive, String name) {
        if (inclusive ? (value < number) : (value <= number)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be greater than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is less than (less than or equal to, if inclusive is true) than the specified
     * number. And if this condition is not met, the specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked.
     * @param inclusive
     *            true if "less than or equal to" check should be performed, false if "less than" check should be
     *            performed.
     * @param name
     *            the parameter name.
     * @param number
     *            the number the value should be compared to.
     *
     * @throws IllegalArgumentException
     *             if the given value is not less than (not less than and not equal to, if inclusive is true) than the
     *             specified number.
     */
    public static void checkLessThan(long value, long number, boolean inclusive, String name) {
        if (inclusive ? (value > number) : (value >= number)) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be less than ",
                (inclusive ? "or equal to " : ""), number, "."));
        }
    }

    /**
     * <p>
     * Checks whether the given value is in the specified range. And if this condition is not met, the specified
     * exception is thrown.
     * </p>
     *
     * @param to
     *            the end value of the range.
     * @param value
     *            the value of parameter to be checked.
     * @param toInclusive
     *            true if end value is included into the range, false otherwise.
     * @param fromInclusive
     *            true if start value is included into the range, false otherwise.
     * @param from
     *            the start value of the range.
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is out of the specified range.
     */
    public static void checkInRange(long value, long from, long to, boolean fromInclusive, boolean toInclusive,
        String name) {
        if ((fromInclusive ? (value < from) : (value <= from)) || (toInclusive ? (value > to) : (value >= to))) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should be in the range ",
                (fromInclusive ? "[" : "("), from, ", ", to, (toInclusive ? "]" : ").")));
        }
    }

    /**
     * <p>
     * Checks whether the given map value is not empty. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param map
     *            the value of map parameter to be checked (not <code>null</code>).
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given map is empty.
     */
    private static void checkNotEmptyMap(Map<?, ?> map, String name) {
        if (map.isEmpty()) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be empty."));
        }
    }

    /**
     * <p>
     * Checks whether the given collection is not empty. And if this condition is not met, the specified exception is
     * thrown.
     * </p>
     *
     * @param collection
     *            the value of collection parameter to be checked (not <code>null</code>).
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given collection is empty.
     */
    private static void checkNotEmptyCollection(Collection<?> collection, String name) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be empty."));
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (after trimming). And if this condition is not met, the
     * specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked (not <code>null</code>).
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is empty.
     */
    private static void checkNotEmptyStringAfterTrimming(String value, String name) {
        if (value.trim().length() == 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be empty (after trimming)."));
        }
    }

    /**
     * <p>
     * Checks whether the given string value is not empty (without trimming). And if this condition is not met, the
     * specified exception is thrown.
     * </p>
     *
     * @param value
     *            the value of parameter to be checked (not <code>null</code>).
     * @param name
     *            the parameter name.
     *
     * @throws IllegalArgumentException
     *             if the given value is empty.
     */
    private static void checkNotEmptyString(String value, String name) {
        if (value.length() == 0) {
            throw new IllegalArgumentException(getParamValueMessage(name, " should not be empty (without trimming)."));
        }
    }

    /**
     * <p>
     * Retrieves the parameter value message from the specified parameter name.
     * </p>
     *
     * @param paramName
     *            the parameter name.
     * @param values
     *            the message values.
     *
     * @return the constructed parameter value name.
     */
    private static String getParamValueMessage(String paramName, Object... values) {
        StringBuilder sb = new StringBuilder();

        sb.append("The '").append(paramName).append("' parameter");

        for (Object value : values) {
            sb.append(value);
        }
        return sb.toString();
    }
}
