/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.lookup;

import java.sql.Connection;
import java.sql.SQLException;

import com.cronos.onlinereview.phases.BaseTest;

/**
 * Declares test cases for UploadStatusLookupUtility class.
 *
 * @author bose_java
 * @version 1.0
 */
public class UploadStatusLookupUtilityTest extends BaseTest {

    /**
     * Tests the UploadStatusLookupUtility.lookUpId(Connection, String) with
     * null connection.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdWithNullConnection() throws Exception {
        Connection conn = null;
        String value = "Scheduled";
        try {
            UploadStatusLookupUtility.lookUpId(conn, value);
            fail("lookUpId() did not throw IAE for null connection.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the UploadStatusLookupUtility.lookUpId(Connection, String) with
     * null value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdWithNullValue() throws Exception {
        Connection conn = getConnection();
        String value = null;
        try {
            UploadStatusLookupUtility.lookUpId(conn, value);
            fail("lookUpId() did not throw IAE for null value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests the UploadStatusLookupUtility.lookUpId(Connection, String) with
     * empty value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdWithEmptyValue() throws Exception {
        Connection conn = getConnection();
        String value = "   ";
        try {
            UploadStatusLookupUtility.lookUpId(conn, value);
            fail("lookUpId() did not throw IAE for empty value.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * Tests whether UploadStatusLookupUtility.lookUpId(Connection, String)
     * returns correct value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpId() throws Exception {
        Connection conn = getConnection();
        String value = "Deleted";
        long expected = 2;
        long id = UploadStatusLookupUtility.lookUpId(conn, value);
        assertEquals("lookupId did not return correct value", id, expected);
    }

    /**
     * Tests the UploadStatusLookupUtility.lookUpId(Connection, String) with a
     * value that is not mapped to any record in the database.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdWithNonMappedValue() throws Exception {
        Connection conn = getConnection();
        String value = "NON-EXISTING-JUNK";
        try {
            UploadStatusLookupUtility.lookUpId(conn, value);
            fail("lookUpId() did not throw SQLException for value not mapped in table.");
        } catch (SQLException e) {
            // expected
        }
    }
}
