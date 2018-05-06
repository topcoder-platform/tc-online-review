/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

/**
 * <p>
 * Helper class for accuracy tests.
 * </p>
 *
 * @author mittu
 * @version 3.0
 */
public class AccuracyHelper {

    /**
     * <p>
     * Represents the accuracy tests files directory.
     * </p>
     */
    private static final String ACC_DIR =
        System.getProperty("user.dir") + File.separator + "test_files" + File.separator + "accuracytests"
            + File.separator;

    /**
     * <p>
     * Represents the email template used for testing.
     * </p>
     */
    private static final String EMAIL_TEMPLATE = ACC_DIR + "email_template.txt";

    /**
     * <p>
     * Represents the configuration file used for accuracy tests.
     * </p>
     */
    private static final String CONFIG = ACC_DIR + "config.xml";

    /**
     * <p>
     * Represents the namespace for DB Connection Factory component.
     * </p>
     */
    public static final String DB_FACTORY_NAMESPACE =
        "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * <p>
     * Prevents new instance creation.
     * </p>
     */
    private AccuracyHelper() {
    }

    /**
     * <p>
     * Loads the configuration required for testing.
     * </p>
     *
     * @throws Exception to junit.
     */
    public static void loadConfig() throws Exception {
        releaseConfig();
        ConfigManager manager = ConfigManager.getInstance();
        manager.add(CONFIG);
    }

    /**
     * <p>
     * Releases the configurations from memory.
     * </p>
     *
     * @throws Exception to junit.
     */
    public static void releaseConfig() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();

        for (Iterator i = manager.getAllNamespaces(); i.hasNext();) {
            manager.removeNamespace((String) i.next());
        }
    }

    /**
     * <p>
     * Gets the Template for sending the email.
     * </p>
     *
     * @return the Document Template to be used.
     * @throws Exception to junit.
     */
    public static Template getEmailTemplate() throws Exception {
        Template template = new XsltTemplate();
        BufferedReader is = null;
        try {
            is = new BufferedReader(new FileReader(EMAIL_TEMPLATE));
            StringBuffer buffer = new StringBuffer();
            String tmp;
            while ((tmp = is.readLine()) != null) {
                buffer.append(tmp);
                buffer.append(System.getProperty("line.separator"));
            }

            template.setTemplate(buffer.toString());
            return template;
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * <p>
     * Inserts some data to the tables which this component depends on.
     * </p>
     *
     * <p>
     * This is used to simplify the testing.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public static void setUpDataBase() throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            clearTables(connection);
            insertTestData(connection);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * <p>
     * Inserts some data to the tables for testing purpose.
     * </p>
     *
     * @param connection Connection instance to access the database
     *
     * @throws SQLException if exception occurs during database operation
     */
    private static void insertTestData(Connection connection) throws SQLException {
        String[] sqlStatements =
            new String[] {
                "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                    + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                    + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (10002, "
                    + "'newJobName01', NULL, NULL, NULL, 'com.topcoder.util.scheduler.scheduling.Week', "
                    + "NULL, NULL, NULL, 2, 4, 'y', 'JOB_TYPE_JAVA_CLASS', "
                    + "'com.topcoder.util.scheduler.scheduling.MyJob', 'jobName07', 'SUCCESSFUL', 10000)",
                "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                    + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                    + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (10001, "
                    + "'jobName07', CURRENT, 3000000, CURRENT, 'com.topcoder.util.scheduler.scheduling.Week', "
                    + "NULL, NULL, NULL, 2, 5, 'y', 'JOB_TYPE_EXTERNAL', 'dir', NULL, NULL, NULL)",
                "INSERT INTO message (messageid, ownerid, name, "
                    + "fromaddress, subject, templatetext, recipients) VALUES (10001, 10002, 'SUCCESSFUL', "
                    + "'admin@topcoder.com', 'Notification', "
                    + "'This email notifies you that the job %JobName% has the status %JobStatus% now...', "
                    + "'rec1@topcoder.com,rec2@topcoder.com')",
                "INSERT INTO group (groupid, name) VALUES (100001, 'group_1')",
                "INSERT INTO groupjob (groupid, jobid) VALUES (100001, 10001)",
                "INSERT INTO groupjob (groupid, jobid) VALUES (100001, 10002)"};

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < sqlStatements.length; i++) {
                stmt.executeUpdate(sqlStatements[i]);
            }
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * <p>
     * Clears all the data from the tables using by this component.
     * </p>
     *
     * <p>
     * This is used to simplify the testing.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public static void deleteTestData() throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            clearTables(connection);
            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            closeConnection(connection);
        }
    }

    /**
     * <p>
     * Deletes data from all the tables used by this component.
     * </p>
     *
     * @param connection Connection instance to access the database
     *
     * @throws SQLException if exception occurs during database operation
     */
    private static void clearTables(Connection connection) throws SQLException {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM groupjob");
            stmt.executeUpdate("DELETE FROM message");
            stmt.executeUpdate("DELETE FROM job");
            stmt.executeUpdate("DELETE FROM group");
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * <p>
     * Returns a new connection to be used for persistence.
     * </p>
     *
     * @return the connection instance for database operation
     *
     * @throws Exception If unable to obtain a Connection
     */
    private static Connection getConnection() throws Exception {
        Connection connection = new DBConnectionFactoryImpl(DB_FACTORY_NAMESPACE).createConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * <p>
     * Closes the given Connection instance.
     * </p>
     *
     * @param con the given Connection instance to close.
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
     * @param state the given Statement instance to close.
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
