package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.SubmissionStatusLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the SubmissionStatusLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class SubmissionStatusLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests SubmissionStatusLookupUtility.lookUpId(Connection, String) on "Active" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdActive() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Active");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests SubmissionStatusLookupUtility.lookUpId(Connection, String) on "Failed Screening" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdFailedScreening() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Failed Screening");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests SubmissionStatusLookupUtility.lookUpId(Connection, String) on "Failed Review" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdFailedReview() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Failed Review");
        assertEquals("Not the expected value from lookUpId()", id, 3);
    }

    /**
     * Tests SubmissionStatusLookupUtility.lookUpId(Connection, String) on "Completed Without Win" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCompletedWithoutWin() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Completed Without Win");
        assertEquals("Not the expected value from lookUpId()", id, 4);
    }

    /**
     * Tests SubmissionStatusLookupUtility.lookUpId(Connection, String) on "Deleted" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdDeleted() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Deleted");
        assertEquals("Not the expected value from lookUpId()", id, 5);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = SubmissionStatusLookupUtility.lookUpId(conn, "Active");
        conn.close();
        id = SubmissionStatusLookupUtility.lookUpId(conn, "Active");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
