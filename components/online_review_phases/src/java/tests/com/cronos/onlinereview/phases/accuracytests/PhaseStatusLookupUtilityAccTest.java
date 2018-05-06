package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.PhaseStatusLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the PhaseStatusLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class PhaseStatusLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests PhaseStatusLookupUtility.lookUpId(Connection, String) on "Scheduled" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdScheduled() throws Exception {
        Connection conn = getConnection();
        long id = PhaseStatusLookupUtility.lookUpId(conn, "Scheduled");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests PhaseStatusLookupUtility.lookUpId(Connection, String) on "Open" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdOpen() throws Exception {
        Connection conn = getConnection();
        long id = PhaseStatusLookupUtility.lookUpId(conn, "Open");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests PhaseStatusLookupUtility.lookUpId(Connection, String) on "Closed" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdClosed() throws Exception {
        Connection conn = getConnection();
        long id = PhaseStatusLookupUtility.lookUpId(conn, "Closed");
        assertEquals("Not the expected value from lookUpId()", id, 3);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = PhaseStatusLookupUtility.lookUpId(conn, "Scheduled");
        conn.close();
        id = PhaseStatusLookupUtility.lookUpId(conn, "Scheduled");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
