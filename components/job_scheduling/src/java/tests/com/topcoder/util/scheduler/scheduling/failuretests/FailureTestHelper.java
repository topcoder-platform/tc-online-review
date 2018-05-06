/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.util.config.ConfigManager;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;

/**
 * <p>
 * A helper class to perform those common operations which are helpful for the
 * test.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class FailureTestHelper {
    /**
     * <p>
     * Represents the path of config file for Logging Wrapper component.
     * </p>
     */
    private static final String config_file = "test_files/failure/failure.xml";

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private FailureTestHelper() {
    }

    /**
     * <p>
     * Uses the given file to config the configuration manager.
     * </p>
     *
     * @throws Exception
     *             when any exception occurs
     */
    public static void loadXMLConfig() throws Exception {
        loadConfig(config_file);
    }

    /**
     * <p>
     * Uses the given file to config the configuration manager.
     * </p>
     *
     * @param file
     *            the configurable file.
     *
     * @throws Exception
     *             when any exception occurs
     */
    public static void loadConfig(String file) throws Exception {
        clearConfig();

        // set up environment
        ConfigManager config = ConfigManager.getInstance();

        config.add(new File(file).getAbsolutePath());
    }

    /**
     * <p>
     * Clears all the namespaces from the configuration manager.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator i = cm.getAllNamespaces(); i.hasNext();) {
            cm.removeNamespace((String) i.next());
        }
    }

    /**
     * <p>
     * Inserts some data to the tables for testing purpose.
     * </p>
     *
     * @throws Exception
     *             if exception occurs during database operation
     */
    public static void insertData() throws Exception {
        String[] sqlStatements = new String[]{
            "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (999990, "
                + "'jobName007', CURRENT, 3000000, CURRENT, 'com.topcoder.util.scheduler.scheduling.Week', "
                + "NULL, NULL, NULL, 2, 5, 'y', 'JOB_TYPE_EXTERNAL', 'dir', NULL, NULL, NULL)",

            "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (10002, "
                + "'newJobName01', NULL, NULL, NULL, 'com.topcoder.util.scheduler.scheduling.Week', "
                + "NULL, NULL, NULL, 2, 4, 'y', 'JOB_TYPE_JAVA_CLASS', "
                + "'com.topcoder.util.scheduler.scheduling.Job', 'jobName007', 'SUCCESSFUL', 10000)",

            "INSERT INTO message (messageid, ownerid, name, "
                + "fromaddress, subject, templatetext, recipients) VALUES (999990, 10002, 'SUCCESSFUL', "
                + "'admin@topcoder.com', 'Notification', "
                + "'This email notifies you that the job %JobName% has the status %JobStatus% now...', "
                + "'rec1@topcoder.com,rec2@topcoder.com')",

            "INSERT INTO group (groupid, name) VALUES (888880, 'group_1')",
            "INSERT INTO groupjob (groupid, jobid) VALUES (888880, 999990)",
            "INSERT INTO groupjob (groupid, jobid) VALUES (888880, 10002)" };

        Statement stmt = null;
        Connection connection = null;

        try {
            connection = getConnection();
            stmt = connection.createStatement();

            for (int i = 0; i < sqlStatements.length; i++) {
                stmt.executeUpdate(sqlStatements[i]);
            }

            connection.commit();
        } finally {
            closeStatement(stmt);
            closeConnection(connection);
        }
    }

    /**
     * <p>
     * Deletes data from all the tables used by this component.
     * </p>
     *
     * @throws Exception
     *             if exception occurs during database operation
     */
    public static void clearTables() throws Exception {
        Statement stmt = null;
        Connection connection = null;

        try {
            connection = getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM groupjob");
            stmt.executeUpdate("DELETE FROM message");
            stmt.executeUpdate("DELETE FROM job");
            stmt.executeUpdate("DELETE FROM group");
            connection.commit();
        } finally {
            closeStatement(stmt);
            closeConnection(connection);
        }
    }

    /**
     * <p>
     * Returns a new connection to be used for persistence.
     * </p>
     *
     * @return the connection instance for database operation
     *
     * @throws Exception
     *             If unable to obtain a Connection
     */
    private static Connection getConnection() throws Exception {
        Connection connection = new DBConnectionFactoryImpl(
            "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl").createConnection();
        connection.setAutoCommit(false);

        return connection;
    }

    /**
     * <p>
     * Closes the given Connection instance.
     * </p>
     *
     * @param con
     *            the given Connection instance to close.
     */
    private static void closeConnection(Connection con) {
        try {
            if ((con != null) && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }

    /**
     * <p>
     * Closes the given PreparedStatement instance.
     * </p>
     *
     * @param state
     *            the given Statement instance to close.
     */
    private static void closeStatement(Statement state) {
        try {
            if (state != null) {
                state.close();
            }
        } catch (SQLException e) {
            // Ignore
        }
    }
}
