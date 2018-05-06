package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.UploadTypeLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the UploadTypeLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class UploadTypeLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests UploadTypeLookupUtility.lookUpId(Connection, String) on "Submission" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdSubmission() throws Exception {
        Connection conn = getConnection();
        long id = UploadTypeLookupUtility.lookUpId(conn, "Submission");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests UploadTypeLookupUtility.lookUpId(Connection, String) on "Test Case" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdTestCase() throws Exception {
        Connection conn = getConnection();
        long id = UploadTypeLookupUtility.lookUpId(conn, "Test Case");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests UploadTypeLookupUtility.lookUpId(Connection, String) on "Final Fix" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdFinalFix() throws Exception {
        Connection conn = getConnection();
        long id = UploadTypeLookupUtility.lookUpId(conn, "Final Fix");
        assertEquals("Not the expected value from lookUpId()", id, 3);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = UploadTypeLookupUtility.lookUpId(conn, "Submission");
        conn.close();
        id = UploadTypeLookupUtility.lookUpId(conn, "Submission");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
