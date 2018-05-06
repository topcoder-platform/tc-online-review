package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.PhaseTypeLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the PhaseTypeLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class PhaseTypeLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Registration" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdRegistration() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Registration");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Submission" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdSubmission() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Submission");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Screening" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdScreening() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Screening");
        assertEquals("Not the expected value from lookUpId()", id, 3);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Review" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdReview() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Review");
        assertEquals("Not the expected value from lookUpId()", id, 4);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Appeals" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdAppeals() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Appeals");
        assertEquals("Not the expected value from lookUpId()", id, 5);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Appeals Response" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdAppealsResponse() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Appeals Response");
        assertEquals("Not the expected value from lookUpId()", id, 6);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Aggregation" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdAggregation() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Aggregation");
        assertEquals("Not the expected value from lookUpId()", id, 7);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Aggregation Review" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdAggregationReview() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Aggregation Review");
        assertEquals("Not the expected value from lookUpId()", id, 8);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Final Fix" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdFinalFix() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Final Fix");
        assertEquals("Not the expected value from lookUpId()", id, 9);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Final Review" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdFinalReview() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Final Review");
        assertEquals("Not the expected value from lookUpId()", id, 10);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on "Approval" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdApproval() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Approval");
        assertEquals("Not the expected value from lookUpId()", id, 11);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = PhaseTypeLookupUtility.lookUpId(conn, "Registration");
        conn.close();
        id = PhaseTypeLookupUtility.lookUpId(conn, "Registration");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
