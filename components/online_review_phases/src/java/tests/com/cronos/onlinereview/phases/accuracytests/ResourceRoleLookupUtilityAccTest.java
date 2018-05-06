package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.ResourceRoleLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the ResourceRoleLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class ResourceRoleLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests ResourceRoleLookupUtility.lookUpId(Connection, String) on "Submitter" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdSubmitter() throws Exception {
        Connection conn = getConnection();
        long id = ResourceRoleLookupUtility.lookUpId(conn, "Submitter");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests ResourceRoleLookupUtility.lookUpId(Connection, String) on "Primary Screener" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdPrimaryScreener() throws Exception {
        Connection conn = getConnection();
        long id = ResourceRoleLookupUtility.lookUpId(conn, "Primary Screener");
        assertEquals("Not the expected value from lookUpId()", id, 2);
    }

    /**
     * Tests ResourceRoleLookupUtility.lookUpId(Connection, String) on "Screener" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdScreener() throws Exception {
        Connection conn = getConnection();
        long id = ResourceRoleLookupUtility.lookUpId(conn, "Screener");
        assertEquals("Not the expected value from lookUpId()", id, 3);
    }

    /**
     * Tests ResourceRoleLookupUtility.lookUpId(Connection, String) on "Reviewer" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdReviewer() throws Exception {
        Connection conn = getConnection();
        long id = ResourceRoleLookupUtility.lookUpId(conn, "Reviewer");
        assertEquals("Not the expected value from lookUpId()", id, 4);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = ResourceRoleLookupUtility.lookUpId(conn, "Submitter");
        conn.close();
        id = ResourceRoleLookupUtility.lookUpId(conn, "Submitter");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
