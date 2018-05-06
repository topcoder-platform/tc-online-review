/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.commons.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * Unit tests for {@link JDBCUtility} class.
 * </p>
 *
 * @author sparemax
 * @version 1.0
 */
public class JDBCUtilityUnitTests {
    /**
     * <p>
     * Represents the Connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Represents the Connection used in tests.
     * </p>
     */
    private Connection conn;

    /**
     * <p>
     * Represents the exception class.
     * </p>
     */
    private Class<IssueTrackingException> exceptionClass;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(JDBCUtilityUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);

        conn = TestsHelper.getConnection();
        conn.setAutoCommit(false);
        exceptionClass = IssueTrackingException.class;
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        try {
            TestsHelper.clearDB(connection);
        } finally {
            TestsHelper.closeConnection(connection);
            TestsHelper.closeConnection(conn);
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_1() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn, "SELECT name FROM issue_types WHERE name=?",
            new int[] {Types.VARCHAR}, new Object[] {"issue1"}, new Class<?>[] {String.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 1, res.length);
        assertEquals("'executeQuery' should be correct.", "issue1", res[0][0]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_2() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn, "SELECT id FROM issue_types WHERE id=? AND name=?",
            new int[] {Types.BIGINT, Types.VARCHAR}, new Object[] {1001, "issue1"},
            new Class<?>[] {Integer.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 1, res.length);
        assertEquals("'executeQuery' should be correct.", 1001, res[0][0]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_3() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Integer.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertEquals("'executeQuery' should be correct.", 1, row[1]);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertEquals("'executeQuery' should be correct.", 0, row[1]);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_4() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Long.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertEquals("'executeQuery' should be correct.", 1L, row[1]);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertEquals("'executeQuery' should be correct.", 0L, row[1]);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_5() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Float.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertEquals("'executeQuery' should be correct.", 1F, (Float) row[1], 0.01);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertEquals("'executeQuery' should be correct.", 0F, (Float) row[1], 0.01);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_6() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Double.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertEquals("'executeQuery' should be correct.", 1D, (Double) row[1], 0.01);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertEquals("'executeQuery' should be correct.", 0D, (Double) row[1], 0.01);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_7() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Boolean.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertTrue("'executeQuery' should be correct.", (Boolean) row[1]);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertFalse("'executeQuery' should be correct.", (Boolean) row[1]);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_8() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT user_id, priority FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Integer.class, Object.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 101, row[0]);
        assertEquals("'executeQuery' should be correct.", 1, row[1]);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertEquals("'executeQuery' should be correct.", 0, row[1]);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 2, row.length);
        assertEquals("'executeQuery' should be correct.", 102, row[0]);
        assertNull("'executeQuery' should be correct.", row[1]);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeQuery_9() throws Exception {
        Object[][] res = JDBCUtility.executeQuery(conn,
            "SELECT start_date FROM issues WHERE issue_type_id=? ORDER BY user_id",
            new int[] {Types.BIGINT}, new Object[] {"1002"},
            new Class<?>[] {Date.class}, exceptionClass);

        assertEquals("'executeQuery' should be correct.", 3, res.length);
        Object[] row = res[0];
        assertNotNull("'executeQuery' should be correct.", row[0]);
        row = res[1];
        assertEquals("'executeQuery' should be correct.", 1, row.length);
        assertNotNull("'executeQuery' should be correct.", row[0]);
        row = res[2];
        assertEquals("'executeQuery' should be correct.", 1, row.length);
        assertNull("'executeQuery' should be correct.", row[0]);
    }

    /**
     * <p>
     * Failure test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>
     * with column types length does not match the result set column count.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_executeQuery_columnTypesInvalid() throws Exception {
        JDBCUtility.executeQuery(conn, "SELECT name FROM issue_types WHERE name=?",
            new int[] {Types.VARCHAR}, new Object[] {"issue1"}, new Class<?>[] {String.class, Integer.class},
            exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>
     * with unsupported column type is used.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_executeQuery_ColumnTypeUnsupported() throws Exception {
        JDBCUtility.executeQuery(conn, "SELECT name FROM issue_types WHERE name=?",
            new int[] {Types.VARCHAR}, new Object[] {"issue1"}, new Class<?>[] {exceptionClass},
            exceptionClass);
    }

    /**
     * <p>
     * Failure test for the method <code>executeQuery(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>
     * with an error occurred.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_executeQuery_Error() throws Exception {
        conn.close();

        JDBCUtility.executeQuery(conn, "SELECT name FROM issue_types WHERE name=?",
            new int[] {Types.VARCHAR}, new Object[] {"issue1"}, new Class<?>[] {String.class}, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeUpdate(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeUpdate_1() throws Exception {
        int res = JDBCUtility.executeUpdate(conn,
            "UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?",
            new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER}, new Object[] {2, 1001, 101}, exceptionClass);
        conn.commit();

        assertEquals("'executeUpdate' should be correct.", 1, res);
        assertEquals("'executeUpdate' should be correct.", 2, getPriority(connection));
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeUpdate(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeUpdate_2() throws Exception {
        int res = JDBCUtility.executeUpdate(conn,
            "UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?",
            new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER}, new Object[] {2, 1002, 102}, exceptionClass);
        conn.commit();

        assertEquals("'executeUpdate' should be correct.", 2, res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>executeUpdate(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeUpdate_3() throws Exception {
        int res = JDBCUtility.executeUpdate(conn,
            "UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?",
            new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER}, new Object[] {2, 1002, 999}, exceptionClass);
        conn.commit();

        assertEquals("'executeUpdate' should be correct.", 0, res);
    }

    /**
     * <p>
     * Failure test for the method <code>executeUpdate(Connection connection, String sql, int[] argumentTypes,
     * Object[] argumentValues, ClassClass&lt;?&gt;[] columnTypes, ClassClass&lt;T&gt; exceptionClass)</code>
     * with an error occurred.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_executeUpdate_Error() throws Exception {
        conn.close();

        JDBCUtility.executeUpdate(conn, "UPDATE issues SET priority = ? WHERE issue_type_id = ? AND user_id = ?",
            new int[] {Types.INTEGER, Types.BIGINT, Types.INTEGER}, new Object[] {2, 1001, 101}, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>commitTransaction(Connection connection,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_commitTransaction() throws Exception {
        setPriority(conn, 2);
        JDBCUtility.commitTransaction(conn, exceptionClass);

        assertEquals("'commitTransaction' should be correct.", 2, getPriority(connection));
    }

    /**
     * <p>
     * Failure test for the method <code>commitTransaction(Connection connection,
     * Class&lt;T&gt; exceptionClass)</code> with an error occurred.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_commitTransaction_Error() throws Exception {
        conn.close();

        JDBCUtility.commitTransaction(conn, exceptionClass);
    }

    /**
     * <p>
     * Accuracy test for the method <code>rollbackTransaction(Connection connection,
     * Class&lt;T&gt; exceptionClass)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_rollbackTransaction() throws Exception {
        setPriority(conn, 2);
        JDBCUtility.rollbackTransaction(conn, exceptionClass);

        assertEquals("'rollbackTransaction' should be correct.", 1, getPriority(connection));
    }

    /**
     * <p>
     * Accuracy test for the method <code>rollbackTransaction(Connection connection,
     * Class&lt;T&gt; exceptionClass)</code> with connection is <code>null</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_rollbackTransaction_connectionNull() throws Exception {
        JDBCUtility.rollbackTransaction(null, exceptionClass);

        // Good
    }

    /**
     * <p>
     * Failure test for the method <code>rollbackTransaction(Connection connection,
     * Class&lt;T&gt; exceptionClass)</code> with an error occurred.<br>
     * <code>IssueTrackingException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IssueTrackingException.class)
    public void test_rollbackTransaction_Error() throws Exception {
        conn.close();

        JDBCUtility.rollbackTransaction(conn, exceptionClass);
    }

    /**
     * <p>
     * Sets the priority.
     * </p>
     *
     * @param connection
     *            the connection.
     * @param priority
     *            the priority.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static void setPriority(Connection connection, int priority) throws Exception {
        PreparedStatement statement =
            connection.prepareStatement("UPDATE issues SET priority=? WHERE issue_type_id=? AND user_id=?");
        try {
            statement.setInt(1, priority);
            statement.setInt(2, 1001);
            statement.setInt(3, 101);

            statement.executeUpdate();
        } finally {
            statement.close();
        }
    }

    /**
     * <p>
     * Gets the priority.
     * </p>
     *
     * @param connection
     *            the connection.
     *
     * @return the priority.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static int getPriority(Connection connection) throws Exception {
        PreparedStatement statement =
            connection.prepareStatement("SELECT priority FROM issues WHERE issue_type_id=? AND user_id=?");
        try {
            statement.setInt(1, 1001);
            statement.setInt(2, 101);

            ResultSet rs = statement.executeQuery();
            rs.next();
            return rs.getInt(1);
        } finally {
            statement.close();
        }
    }
}
