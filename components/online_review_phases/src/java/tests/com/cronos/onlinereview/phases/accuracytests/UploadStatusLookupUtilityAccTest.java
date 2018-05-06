package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.UploadStatusLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the UploadStatusLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class UploadStatusLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests UploadStatusLookupUtility.lookUpId(Connection, String) on "Active" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdActive() throws Exception {
        Connection conn = getConnection();
        long id = UploadStatusLookupUtility.lookUpId(conn, "Active");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests UploadStatusLookupUtility.lookUpId(Connection, String) on "Deleted" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdDeleted() throws Exception {
        Connection conn = getConnection();
        long id = UploadStatusLookupUtility.lookUpId(conn, "Deleted");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = UploadStatusLookupUtility.lookUpId(conn, "Active");
        conn.close();
        id = UploadStatusLookupUtility.lookUpId(conn, "Active");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
