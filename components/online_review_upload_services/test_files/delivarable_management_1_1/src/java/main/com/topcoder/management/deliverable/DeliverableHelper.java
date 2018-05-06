/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable;

import com.topcoder.management.deliverable.logging.LogMessage;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;
import com.topcoder.util.sql.databaseabstraction.InvalidCursorStateException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>Helper class for the whole component.</p>
 *
 * <p><strong>Thread Safety:</strong> This class is immutable and thread safe</p>
 *
 * @author singlewood, TCSDEVELOPER
 * @version 1.1
 */
public final class DeliverableHelper {

    /**
     * <p> Identifier of deliverable search bundle. </p>
     */
    public static final int DELIVERABLE_SEARCH_BUNDLE = 1;

    /**
     * <p> Identifier of deliverable with submission search bundle. </p>
     */
    public static final int DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE = 2;

    /**
     * <p> Identifier of upload search bundle. </p>
     */
    public static final int UPLOAD_SEARCH_BUNDLE = 3;

    /**
     * <p> Identifier of submission search bundle. </p>
     */
    public static final int SUBMISSION_SEARCH_BUNDLE = 4;

    /**
     * <p> Logger instance using the class name as category. </p>
     */
    private static final Log LOGGER = LogFactory.getLog(DeliverableHelper.class.getName());

    /**
     * <p> Private empty constructor. </p>
     */
    private DeliverableHelper() {
    }

    /**
     * <p>Check if the object is null.</p>
     *
     * @param obj  the object to check.
     * @param name the object name
     *
     * @return true if object is not null.
     *
     * @throws IllegalArgumentException if the object is null.
     */
    public static boolean checkObjectNotNull(Object obj, String name) {
        if (obj == null) {
            throw new IllegalArgumentException("the object " + name + " should not be null.");
        }
        return true;
    }

    /**
     * <p>Check if the object is null.</p>
     *
     * @param obj  the object to check
     * @param str1 the key name
     * @param str2 the container object name
     *
     * @throws IllegalArgumentException if the object is null.
     */
    public static void checkObjectNotNull(Object obj, String str1, String str2) {
        if (obj == null) {
            throw new IllegalArgumentException("failed to get " + str1 + " from " + str2);
        }
    }

    /**
     * <p>Check if the object is null.</p>
     *
     * @param obj     the object to check.
     * @param message the error message
     *
     * @throws IllegalArgumentException if the object is null.
     */
    public static void checkObjectNotNullFullDesp(Object obj, String message) {
        if (obj == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * <p>Check if the number is greater than zero.</p>
     *
     * @param number the number to check.
     * @param name   the number name
     *
     * @throws IllegalArgumentException if the number <= 0.
     */
    public static void checkGreaterThanZero(long number, String name) {
        if (number <= 0) {
            throw new IllegalArgumentException(name + " should be greater than zero.");
        }
    }

    /**
     * <p>Check if deliverableCheckers is a Map of non-null String - DeliverableChecker.</p>
     *
     * @param deliverableCheckers the map to check
     *
     * @throws IllegalArgumentException If deliverableCheckers is not a Map of non-null String - DeliverableChecker
     */
    public static void checkDeliverableCheckers(Map deliverableCheckers) {
        for (Iterator iter = deliverableCheckers.entrySet().iterator(); iter.hasNext();) {
            Entry element = (Entry) iter.next();

            if (element.getKey() == null) {
                throw new IllegalArgumentException("deliverableCheckers contains null String key.");
            } else {
                if (!(element.getKey() instanceof String)) {
                    throw new IllegalArgumentException("deliverableCheckers contains non-String key.");
                }
            }

            if (!(element.getValue() instanceof DeliverableChecker)) {
                throw new IllegalArgumentException(
                        "deliverableCheckers contains non-DeliverableChecker Objects");
            }
        }

    }

    /**
     * <p>Check if the object is a CustomResultSet. If true, then check if the CustomResultSet's record count equals
     * count. If all ok, it will return a long[][] array that retrieved from CustomResultSet.</p>
     *
     * @param obj       the object to check
     * @param maxColumn the max column number
     *
     * @return long[][] from CustomResultSet
     *
     * @throws SearchBuilderConfigurationException
     *          if obj is not a CustomResultSet, or it's record count is not correct.
     */
    public static long[][] checkAndGetCustomResultSetValidDeliverable(Object obj, int maxColumn)
            throws SearchBuilderConfigurationException {

        if (!(obj instanceof CustomResultSet)) {
            LOGGER.log(Level.ERROR, "the object get from SearchBundle is not a CustomResultSet");
            throw new SearchBuilderConfigurationException("the object get from SearchBundle"
                    + " is not a CustomResultSet");
        }

        CustomResultSet customerResult = (CustomResultSet) obj;

        LOGGER.log(Level.INFO, "CustomResultSet Records: " + customerResult.getRecordCount());

        // Create a long array for temporary storage.
        long[][] res = new long[maxColumn][customerResult.getRecordCount()];

        try {
            // Retrieved long[][] from CustomResultSet. If the expected data is not present,
            // according to the implementation of CustomerResult.getInt(), ClassCastException
            // will be thrown. Note that all columns and rows of CustomResultSet are 1-indexed.
            for (int i = 0; customerResult.next(); i++) {
                for (int j = 0; j < maxColumn; ++j) {
                    res[j][i] = customerResult.getLong(j + 1);
                }
            }
        } catch (ClassCastException e) {
            LOGGER.log(Level.ERROR, "the CustomResultSet get from SearchBundle"
                    + " contains non-long values, or the expected data is missing.\n"
                    + LogMessage.getExceptionStackTrace(e));
            throw new SearchBuilderConfigurationException("the CustomResultSet get from SearchBundle"
                    + " contains non-long values, or the expected data is missing.", e);
        } catch (InvalidCursorStateException e) {
            LOGGER.log(Level.ERROR, "error occurs when fetching the current row. \n"
                    + LogMessage.getExceptionStackTrace(e));
            throw new SearchBuilderConfigurationException("error occurs when fetching the current row.", e);
        }

        return res;
    }

    /**
     * <p>Check if the object is a CustomResultSet. If true, then check if the CustomResultSet's record count equals
     * count. If all ok, it will return a long[][] array that retrieved from CustomResultSet.</p>
     *
     * @param obj       the object to check
     * @param maxColumn the max column number
     *
     * @return long[][] from CustomResultSet
     *
     * @throws SearchBuilderConfigurationException
     *          if obj is not a CustomResultSet, or it's record count is not correct.
     */
    public static long[][] checkAndGetCustomResultSetValid(Object obj, int maxColumn)
            throws SearchBuilderConfigurationException {

        if (!(obj instanceof CustomResultSet)) {
            LOGGER.log(Level.ERROR, "the object get from SearchBundle is not a CustomResultSet");
            throw new SearchBuilderConfigurationException("the object get from SearchBundle"
                    + " is not a CustomResultSet");
        }

        CustomResultSet customerResult = (CustomResultSet) obj;

        // Create a long array for temporary storage.
        long[][] res = new long[2][customerResult.getRecordCount()];

        try {
            // Retrieved long[][] from CustomResultSet. If the expected data is not present,
            // according to the implementation of CustomerResult.getInt(), ClassCastException
            // will be thrown. Note that all columns and rows of CustomResultSet are 1-indexed.
            for (int i = 0; customerResult.next(); i++) {
                res[0][i] = customerResult.getInt(1);

                // if maxColumn == 2, get data from the second column.
                if (maxColumn == 2) {
                    res[1][i] = customerResult.getInt(2);
                }
            }

            // Check if CustomResultSet has too much columns.
            if (customerResult.getRecordCount() != 0) {
                // Reset the cursor state.
                customerResult.first();

                Object testColumn = null;
                if (maxColumn == 1) {
                    // Try to get the second column.
                    testColumn = customerResult.getObject(2);
                } else {
                    // Try to get the third column.
                    testColumn = customerResult.getObject(3);
                }

                // If testColumn is not null, it shows that there are extra columns, throw
                // SearchBuilderConfigurationException.
                if (testColumn != null) {
                    LOGGER.log(Level.ERROR, "the CustomResultSet get from SearchBundle"
                            + "has too many columns, " + maxColumn + " column(s) allowed.");
                    throw new SearchBuilderConfigurationException("the CustomResultSet get from SearchBundle"
                            + "has too many columns, " + maxColumn + " column(s) allowed.");
                }
            }

        } catch (ClassCastException e) {
            LOGGER.log(Level.ERROR, "the CustomResultSet get from SearchBundle"
                    + " contains non-long values, or the expected data is missing. \n"
                    + LogMessage.getExceptionStackTrace(e));
            throw new SearchBuilderConfigurationException("the CustomResultSet get from SearchBundle"
                    + " contains non-long values, or the expected data is missing.", e);
        } catch (InvalidCursorStateException e) {
            LOGGER.log(Level.ERROR, "error occurs when fetching the current row. \n"
                    + LogMessage.getExceptionStackTrace(e));
            throw new SearchBuilderConfigurationException("error occurs when fetching the current row.", e);
        }

        return res;
    }

    /**
     * <p>Set the searchable fields of given SearchBundle.</p>
     *
     * @param searchBundle the SearchBundle to set
     * @param key          the identifier of SearchBundle
     */
    public static void setSearchableFields(SearchBundle searchBundle, int key) {
        Map fields = new HashMap();

        // Set up an IntegerValidator for latter use.
        IntegerValidator greaterThanZeroValidator = IntegerValidator.greaterThan(0);

        // Set the fields with different validator.
        switch (key) {
            case DELIVERABLE_WITH_SUBMISSIONS_SEARCH_BUNDLE:
                fields.put("submission_id", greaterThanZeroValidator);
                // Falls through.

            case DELIVERABLE_SEARCH_BUNDLE:
                fields.put("deliverable_id", greaterThanZeroValidator);
                fields.put("phase_id", greaterThanZeroValidator);
                fields.put("name", StringValidator.hasLength(greaterThanZeroValidator));
                fields.put("required", IntegerValidator.inRange(Integer.MIN_VALUE, Integer.MAX_VALUE));
                break;

            case UPLOAD_SEARCH_BUNDLE:
                fields.put("upload_id", greaterThanZeroValidator);
                fields.put("upload_type_id", greaterThanZeroValidator);
                fields.put("upload_status_id", greaterThanZeroValidator);
                break;

            case SUBMISSION_SEARCH_BUNDLE:
                fields.put("upload_id", greaterThanZeroValidator);
                fields.put("submission_id", greaterThanZeroValidator);
                fields.put("submission_status_id", greaterThanZeroValidator);

                // added in version 1.1 - for searching SubmissionType
                fields.put("submission_type_id", greaterThanZeroValidator);
                break;

            default:
                break;
        }

        // Set common searchable fields for those search bundle.
        fields.put("project_id", greaterThanZeroValidator);
        fields.put("resource_id", greaterThanZeroValidator);

        searchBundle.setSearchableFields(fields);
    }
}
