/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.persistence.accuracytests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * A helper class for accuracy tests.
 * </p>
 * @author Thinfox, Beijing2008
 * @version 1.0
 */
final class AccuracyTestHelper {
    /**
     * Removes all namespaces from Config Manager.
     * @throws Exception to JUnit.
     */
    static void clearConfiguration() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<String> it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Closes the sql statement.
     * </p>
     * @param statement the statement to be closed.
     */
    private static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Closes the sql connection.
     * </p>
     * @param connection the connection to be closed.
     */
    private static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * <p>
     * Executes the given sql string.
     * </p>
     * @param conn the database connection
     * @param sql the sql string to execute
     * @param args the sql argument objects
     * @throws SQLException when error occurs during execution.
     */
    public static void execute(Connection conn, String sql, Object[] args) throws SQLException {
        PreparedStatement stmt = null;

        try {
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < args.length;) {
                Object obj = args[i++];

                if (obj instanceof Long) {
                    stmt.setLong(i, ((Long) obj).longValue());
                } else if (obj instanceof Double) {
                    stmt.setDouble(i, ((Double) obj).doubleValue());
                } else if (obj instanceof Boolean) {
                    stmt.setBoolean(i, ((Boolean) obj).booleanValue());
                } else if (obj instanceof String) {
                    stmt.setString(i, (String) obj);
                }
            }

            stmt.executeUpdate();
        } finally {
            close(stmt);
        }
    }

    /**
     * <p>
     * Clear all the data in database.
     * </p>
     * @throws Exception if fails
     */
    @SuppressWarnings("deprecation")
    static void clearTables() throws Exception {
        Connection conn = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName())
            .createConnection();
        try {
            execute(conn, "DELETE FROM review_item_comment", new Object[0]);
            execute(conn, "DELETE FROM review_item", new Object[0]);
            execute(conn, "DELETE FROM review_comment", new Object[0]);
            execute(conn, "DELETE FROM review", new Object[0]);
            execute(conn, "DELETE FROM comment_type_lu", new Object[0]);
            execute(conn, "DELETE FROM resource", new Object[0]);
            execute(conn, "DELETE FROM submission", new Object[0]);
            execute(conn, "DELETE FROM upload", new Object[0]);
            execute(conn, "DELETE FROM scorecard", new Object[0]);
            execute(conn, "DELETE FROM scorecard_type_lu", new Object[0]);
            execute(conn, "DELETE FROM scorecard_question", new Object[0]);
            execute(conn, "DELETE FROM id_sequences", new Object[0]);
        } finally {
            close(conn);
        }
    }

    /**
     * <p>
     * Initializes test data in the database tables.
     * </p>
     * @throws Exception if fails
     */
    @SuppressWarnings("deprecation")
    public static void initTables() throws Exception {
        clearTables();

        Connection conn = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName())
            .createConnection();
        conn.setAutoCommit(true);
        try {
            // insert data into id_sequences table
            execute(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_id_seq', 1, 1, 0)", new Object[0]);
            execute(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_item_id_seq', 1, 1, 0);", new Object[0]);
            execute(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_comment_id_seq', 1, 1, 0);", new Object[0]);
            execute(conn, "INSERT INTO id_sequences(name, next_block_start, "
                + "block_size, exhausted) VALUES('review_item_comment_id_seq', 1, 1, 0); ", new Object[0]);

            // insert data into scorecard_question table
            for (int i = 1; i <= 30; i++) {
                //execute(conn, "INSERT INTO upload (upload_id) values (?)", new Object[] {new Long(i)});
                execute(conn, "INSERT INTO submission (submission_id) values (?)", new Object[] {new Long(i)});
                execute(conn, "INSERT INTO scorecard_question (scorecard_question_id) values (?)",
                    new Object[] {new Long(i)});
                execute(conn, "INSERT INTO resource (resource_id, project_id) values (?, ?)",
                    new Object[] {new Long(i), new Long(i)});
                execute(conn, "INSERT INTO comment_type_lu (comment_type_id, name, description, create_user,"
                    + " create_date, modify_user, modify_date) values (?, ?, ?, 'topcoder', CURRENT,"
                    + " 'topcoder', CURRENT)", new Object[] {new Long(i), "type" + i, "comment type " + i});
                execute(conn, "INSERT INTO scorecard_type_lu (scorecard_type_id) values (?)",
                    new Object[] {new Long(i)});
                execute(conn, "INSERT INTO scorecard (scorecard_id, scorecard_type_id) values (?, ?)",
                    new Object[] {new Long(i), new Long(i)});
            }

        } finally {
            close(conn);
        }
    }
    /**
     * <p>
     * Executes the given sql string and returns the result count.
     * </p>
     * @param sql the sql string to execute
     * @return the count
     * @throws Exception when error occurs during execution.
     */
    @SuppressWarnings("deprecation")
    public static int queryCount(String sql) throws Exception {
        Connection conn = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName())
        .createConnection();
        try {
            Statement stmt = null;
            ResultSet rs = null;
            try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
                rs.next();
                return rs.getInt(1);
            } finally {
                close(stmt);
                close(rs);
            }

        } finally {
            close(conn);
        }
        
    }
    /**
     * <p>
     * Closes the rs.
     * </p>
     * @param rs the rs to be closed.
     */
    private static void close(ResultSet rs) throws SQLException {
        if (rs != null) {
            rs.close();
        }
    }
    /**
     * <p>
     * Executes the given sql string.
     * </p>
     * @param sql the sql string to execute
     * @param args the sql argument objects
     * @throws Exception when error occurs during execution.
     */
    @SuppressWarnings("deprecation")
    public static void execute(String sql, Object[] args) throws Exception {
        PreparedStatement stmt = null;
        Connection conn = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName())
        .createConnection();

        try {
            stmt = conn.prepareStatement(sql);

            for (int i = 0; i < args.length;) {
                Object obj = args[i++];

                if (obj instanceof Long) {
                    stmt.setLong(i, ((Long) obj).longValue());
                } else if (obj instanceof Double) {
                    stmt.setDouble(i, ((Double) obj).doubleValue());
                } else if (obj instanceof Boolean) {
                    stmt.setBoolean(i, ((Boolean) obj).booleanValue());
                } else if (obj instanceof String) {
                    stmt.setString(i, (String) obj);
                }
            }

            stmt.executeUpdate();
        } finally {
            close(stmt);
        }
    }
}