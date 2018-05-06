/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.mockrunner.mock.jdbc.MockConnection;

import junit.framework.TestCase;

/**
 * <p>
 * Unit test for <code>{@link UserProjectDataStoreHelper}</code> class.
 * </p>
 *
 * @author FireIce
 * @version 1.1
 * @since 1.1
 */
public class UserProjectDataStoreHelperUnitTest extends TestCase {

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNull(Object, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the object to validate is null.
     * </p>
     */
    public void testValidateNull_NullObject() {
        try {
            UserProjectDataStoreHelper.validateNull(null, "null");
            fail("expect throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNull(Object, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw any exception if the object is not null.
     * </p>
     */
    public void testValidateNull_NotNullObject() {
        try {
            UserProjectDataStoreHelper.validateNull(new Object(), "NotNull");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateStringEmptyNull(String, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the string is null.
     * </p>
     */
    public void testValidateStringEmptyNull_NullString() {
        String arg = null;
        try {
            UserProjectDataStoreHelper.validateStringEmptyNull(arg, "arg");
            fail("expect throw IllegalArgumentException if the string is null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateStringEmptyNull(String, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the string is empty.
     * </p>
     */
    public void testValidateStringEmptyNull_EmptyString() {
        String arg = "";
        try {
            UserProjectDataStoreHelper.validateStringEmptyNull(arg, "arg");
            fail("expect throw IllegalArgumentException if the string is empty.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateStringEmptyNull(String, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the string is trimmed empty.
     * </p>
     */
    public void testValidateStringEmptyNull_TrimmedEmptyString() {
        String arg = "  ";
        try {
            UserProjectDataStoreHelper.validateStringEmptyNull(arg, "arg");
            fail("expect throw IllegalArgumentException if the string is trimmed empty.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateStringEmptyNull(String, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the string is valid(not null or empty).
     * </p>
     */
    public void testValidateStringEmptyNull_ValidString() {
        String arg = "Valid";
        try {
            UserProjectDataStoreHelper.validateStringEmptyNull(arg, "arg");
        } catch (IllegalArgumentException e) {
            fail("not expect throw IllegalArgumentException if the string is valid.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(int, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the integer is negative.
     * </p>
     */
    public void testValidateNegative_int_Negative() {
        int num = -1;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
            fail("Expect throw IllegalArgumentException if the integer is negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(int, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the integer is non-negative.
     * </p>
     */
    public void testValidateNegative_int_NoneNegative() {
        int num = 0;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the integer is non-negative.");
        }

        num = 10000;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the integer is non-negative.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(long, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long is negative.
     * </p>
     */
    public void testValidateNegative_long_Negative() {
        long num = -1;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
            fail("Expect throw IllegalArgumentException if the long is negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(long, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the long is non-negative.
     * </p>
     */
    public void testValidateNegative_long_NoneNegative() {
        long num = 0;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the long is non-negative.");
        }

        num = 10000;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the long is non-negative.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(double, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the double is negative.
     * </p>
     */
    public void testValidateNegative_double_Negative() {
        double num = -1;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
            fail("Expect throw IllegalArgumentException if the double is negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNegative(double, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the double is non-negative.
     * </p>
     */
    public void testValidateNegative_double_NoneNegative() {
        double num = 0;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the double is non-negative.");
        }

        num = 10000;
        try {
            UserProjectDataStoreHelper.validateNegative(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the double is non-negative.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(int, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the integer is negative.
     * </p>
     */
    public void testValidateNotPositive_int_Negative() {
        int num = -100;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
            fail("Expect throw IllegalArgumentException if the integer is negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(int, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the integer is zero.
     * </p>
     */
    public void testValidateNotPositive_int_Zero() {
        int num = 0;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
            fail("Expect throw IllegalArgumentException if the integer is zero.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(int, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the integer is positive.
     * </p>
     */
    public void testValidateNotPositive_int_Positive() {
        int num = 10000;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the integer is positive.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(long, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long is negative.
     * </p>
     */
    public void testValidateNotPositive_long_Negative() {
        long num = -100;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
            fail("Expect throw IllegalArgumentException if the long is negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(long, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long is zero.
     * </p>
     */
    public void testValidateNotPositive_long_Zero() {
        long num = 0;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
            fail("Expect throw IllegalArgumentException if the long is zero.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateNotPositive(long, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the long is positive.
     * </p>
     */
    public void testValidateNotPositive_long_Positive() {
        long num = 10000;
        try {
            UserProjectDataStoreHelper.validateNotPositive(num, "num");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the long is positive.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateFieldAlreadySet(long, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the long is -1 which indicates not set.
     * </p>
     */
    public void testValidateFieldAlreadySet_long_NotSet() {
        try {
            UserProjectDataStoreHelper.validateFieldAlreadySet(-1, "notSet");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the long is -1 which indicates not set.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateFieldAlreadySet(long, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long is not -1 which indicates already set.
     * </p>
     */
    public void testValidateFieldAlreadySet_long_AlreadySet() {
        try {
            UserProjectDataStoreHelper.validateFieldAlreadySet(1, "AlreadySet");
            fail("Expect throw IllegalArgumentException if the long is not -1 which indicates already set.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateFieldAlreadySet(String, String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the string is null which indicates not set.
     * </p>
     */
    public void testValidateFieldAlreadySet_String_NotSet() {
        try {
            UserProjectDataStoreHelper.validateFieldAlreadySet(null, "notSet");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the string is null which indicates not set.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateFieldAlreadySet(String, String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the string is not null which indicates already set.
     * </p>
     */
    public void testValidateFieldAlreadySet_String_AlreadySet() {
        try {
            UserProjectDataStoreHelper.validateFieldAlreadySet("", "AlreadySet");
            fail("Expect throw IllegalArgumentException if the string is not null which indicates already set.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(long[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long array is null.
     * </p>
     */
    public void testValidateArray_long_NullArray() {
        long[] longArr = null;

        try {
            UserProjectDataStoreHelper.validateArray(longArr, "longArr");
            fail("Expect throw IllegalArgumentException if the long array is null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(long[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long array contains zero.
     * </p>
     */
    public void testValidateArray_long_ArrayContainsZero() {
        long[] longArr = new long[] {0};

        try {
            UserProjectDataStoreHelper.validateArray(longArr, "longArr");
            fail("Expect throw IllegalArgumentException if the long array contains zero.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(long[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the long array contains negative.
     * </p>
     */
    public void testValidateArray_long_ArrayContainsNegative() {
        long[] longArr = new long[] {-1};

        try {
            UserProjectDataStoreHelper.validateArray(longArr, "longArr");
            fail("Expect throw IllegalArgumentException if the long array contains negative.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(long[], String)}</code> method.
     * </p>
     * <p>
     * Not Expect throw IllegalArgumentException if the long array is valid.
     * </p>
     */
    public void testValidateArray_long_ValidArray() {
        long[] longArr = new long[] {1};

        try {
            UserProjectDataStoreHelper.validateArray(longArr, "longArr");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the long array is valid.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(String[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the String array is null.
     * </p>
     */
    public void testValidateArray_String_NullArray() {
        String[] strArr = null;

        try {
            UserProjectDataStoreHelper.validateArray(strArr, "strArr");
            fail("Expect throw IllegalArgumentException if the string array is null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(String[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the String array contains null.
     * </p>
     */
    public void testValidateArray_String_ArrayContainsNull() {
        String[] strArr = new String[] {null};

        try {
            UserProjectDataStoreHelper.validateArray(strArr, "strArr");
            fail("Expect throw IllegalArgumentException if the string array contains null.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(String[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the String array contains empty string.
     * </p>
     */
    public void testValidateArray_String_ArrayContainsEmpty() {
        String[] strArr = new String[] {""};

        try {
            UserProjectDataStoreHelper.validateArray(strArr, "strArr");
            fail("Expect throw IllegalArgumentException if the string array contains empty string.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(String[], String)}</code> method.
     * </p>
     * <p>
     * Expect throw IllegalArgumentException if the String array contains trimmed empty string.
     * </p>
     */
    public void testValidateArray_String_ArrayContainsTrimmedEmpty() {
        String[] strArr = new String[] {"  "};

        try {
            UserProjectDataStoreHelper.validateArray(strArr, "strArr");
            fail("Expect throw IllegalArgumentException if the string array contains trimmed empty string.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#validateArray(String[], String)}</code> method.
     * </p>
     * <p>
     * Not expect throw IllegalArgumentException if the String array is valid.
     * </p>
     */
    public void testValidateArray_String_ValidArray() {
        String[] strArr = new String[] {"Hello", "World"};

        try {
            UserProjectDataStoreHelper.validateArray(strArr, "strArr");
        } catch (IllegalArgumentException e) {
            fail("Not expect throw IllegalArgumentException if the string array is valid.");
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#retFirstObject(ExternalObject[])}</code> method.
     * </p>
     * <p>
     * If the array is empty, should return null.
     * </p>
     */
    public void testRetFirstObject_EmptyArray() {
        assertNull("If the array is empty, should return null.", UserProjectDataStoreHelper
                .retFirstObject(new ExternalObject[0]));
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#retFirstObject(ExternalObject[])}</code> method.
     * </p>
     * <p>
     * If the array is not empty, should return the first element.
     * </p>
     */
    public void testRetFirstObject_NonEmptyArray() {
        ExternalObject[] externalObjects = new ExternalObject[10];
        for (int i = 0; i < externalObjects.length; i++) {
            externalObjects[i] = new ExternalObject() {
                public long getId() {
                    return 0;
                }
            };
        }
        assertSame("If the array is not empty, should return the first element.", externalObjects[0],
                UserProjectDataStoreHelper.retFirstObject(externalObjects));
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#createStatement(Connection, String, String)}</code>
     * method.
     * </p>
     * <p>
     * If SQLException is thrown, should wrapped as RetrievalException.
     * </p>
     */
    public void testCreateStatement_SQLException() {
        Connection conn = new MockConnection() {
            public PreparedStatement prepareStatement(String arg0) throws SQLException {
                throw new SQLException("dummy exception only for testing purpose.");
            }
        };

        try {
            UserProjectDataStoreHelper.createStatement(conn, "select count(*) from user", "UserCount");
            fail("If SQLException is thrown, should wrapped as RetrievalException.");
        } catch (RetrievalException e) {
            // expected
        }
    }

    /**
     * <p>
     * Unit test for <code>{@link UserProjectDataStoreHelper#generateQuestionMarks(int)}</code> method.
     * </p>
     * <p>
     * the generated question marks string should be accurate.
     * </p>
     */
    public void testGenerateQuestionMarksAccuracy() {
        String result = UserProjectDataStoreHelper.generateQuestionMarks(5);

        assertEquals("the generated question marks string should be accurate.", "(?,?,?,?,?)", result);
    }

}
