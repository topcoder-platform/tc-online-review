/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.lookup;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import com.cronos.onlinereview.phases.BaseTest;

/**
 * Defines accuracy and failure test cases for LookupHelper class.
 *
 * @author bose_java
 * @version 1.0
 */
public class LookupHelperTest extends BaseTest {

    /**
     * Tests if LookupHelper.checkNull(Object, String) throws
     * IllegalArgumentException for null argument.
     */
    public void testCheckNull() {
        try {
            LookupHelper.checkNull(null, "argName");
            fail("LookupHelper.checkNull() should throw IAE for null argument.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests if LookupHelper.checkString(String, String) throws
     * IllegalArgumentException for null argument.
     */
    public void testCheckStringNull() {
        try {
            LookupHelper.checkString(null, "argName");
            fail("LookupHelper.checkNull() should throw IAE for null argument.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests if LookupHelper.checkString(String, String) throws
     * IllegalArgumentException for empty argument.
     */
    public void testCheckStringEmpty() {
        try {
            LookupHelper.checkString("   ", "argName");
            fail("LookupHelper.checkNull() should throw IAE for empty argument.");
        } catch (IllegalArgumentException ex) {
            // expected
        }
    }

    /**
     * Tests LookupHelper.lookUpId(Map, String, Connection, String) with valid
     * values and tests if correct value is returned.
     *
     * @throws Exception not during test.
     */
    public void testLookUpId() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        String value = "Open";
        long expectedId = 2;
        String sql = "SELECT phase_status_id FROM phase_status_lu WHERE name = ?";
        Connection conn = getConnection();

        long id = LookupHelper.lookUpId(map, value, conn, sql);
        assertEquals("lookup() did not return correct value", id, expectedId);
    }

    /**
     * Tests LookupHelper.lookUpId(Map, String, Connection, String) with valid
     * values and tests if the value returned is inserted in the passed map for
     * caching or not.
     *
     * @throws Exception not during test.
     */
    public void testLookUpIdCache() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        String value = "Open";
        long expectedId = 2;
        String sql = "SELECT phase_status_id FROM phase_status_lu WHERE name = ?";
        Connection conn = getConnection();

        long id = LookupHelper.lookUpId(map, value, conn, sql);
        assertEquals("lookup() did not return correct value", id, expectedId);
        assertEquals("Lookup method did not cache the value after retrieving",
                        new Long(id), map.get(value));
    }

    /**
     * Tests LookupHelper.lookUpId(Map, String, Connection, String) with valid
     * values and asserts that the connection was not closed.
     *
     * @throws Exception not during test.
     */
    public void testLookUpIdConnection() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        String value = "Open";
        String sql = "SELECT phase_status_id FROM phase_status_lu WHERE name = ?";
        Connection conn = getConnection();

        LookupHelper.lookUpId(map, value, conn, sql);
        assertFalse("Must not close connection", conn.isClosed());
    }
}
