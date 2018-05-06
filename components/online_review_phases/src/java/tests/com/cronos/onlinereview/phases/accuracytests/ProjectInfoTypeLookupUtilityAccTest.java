package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.lookup.ProjectInfoTypeLookupUtility;

import java.sql.Connection;


/**
 * Accuracy test cases for the ProjectInfoTypeLookupUtility class
 *
 * @author tuenm
 * @version 1.0
 */
public class ProjectInfoTypeLookupUtilityAccTest extends BaseAccuracyTest {
    /**
     * Tests ProjectInfoTypeLookupUtility.lookUpId(Connection, String) on "External Reference ID" value.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdExternal() throws Exception {
        Connection conn = getConnection();
        long id = ProjectInfoTypeLookupUtility.lookUpId(conn, "External Reference ID");
        assertEquals("Not the expected value from lookUpId()", id, 1);
    }

    /**
     * Tests PhaseTypeLookupUtility.lookUpId(Connection, String) on the caching ability.
     *
     * @throws Exception not under test.
     */
    public void testLookUpIdCached() throws Exception {
        Connection conn = getConnection();
        long id = ProjectInfoTypeLookupUtility.lookUpId(conn, "External Reference ID");
        conn.close();
        id = ProjectInfoTypeLookupUtility.lookUpId(conn, "External Reference ID");

        assertEquals("Not the expected value from lookUpId()", id, 1);
    }
}
