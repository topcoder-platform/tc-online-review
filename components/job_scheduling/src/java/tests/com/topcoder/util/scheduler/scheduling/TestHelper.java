/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
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
 * A helper class to perform those common operations which are helpful for the
 * test.
 * </p>
 * <p>
 * Changes in version 3.1: A new method <code>getPrivateFieldValue</code> is
 * added. And the methods to create new job are also added.
 * </p>
 * @author TCSDEVELOPER
 * @version 3.1
 * @since 3.0
 */
public class TestHelper {
    /**
     * <p>
     * Represents the namespace for Logging Wrapper component.
     * </p>
     */
    public static final String LOG_NAMESPACE = "com.topcoder.util.log";

    /**
     * <p>
     * Represents the path of config file for Logging Wrapper component.
     * </P>
     */
    public static final String LOG_CONFIGFILE = "test_files" + File.separator
            + "Logging.xml";

    /**
     * <p>
     * Represents the namespace for DB Connection Factory component.
     * </p>
     */
    public static final String DB_FACTORY_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Use the given file to config the given namespace the format of the config
     * file is ConfigManager.CONFIG_XML_FORMAT.
     * </p>
     * @param namespace use the namespace to load config information to
     *            ConfigManager
     * @param fileName config file to set up environment
     * @throws Exception when any exception occurs
     */
    public static void loadSingleXMLConfig(String namespace, String fileName)
        throws Exception {
        // set up environment
        ConfigManager config = ConfigManager.getInstance();
        File file = new File(fileName);

        // config namespace
        if (config.existsNamespace(namespace)) {
            config.removeNamespace(namespace);
        }

        config.add(namespace, file.getCanonicalPath(),
                ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * <p>
     * Remove the given namespace in the ConfigManager.
     * </p>
     * @param namespace namespace use to remove the config information in
     *            ConfigManager
     * @throws Exception when any exception occurs
     */
    public static void clearConfigFile(String namespace) throws Exception {
        ConfigManager config = ConfigManager.getInstance();

        // clear the environment
        if (config.existsNamespace(namespace)) {
            config.removeNamespace(namespace);
        }
    }

    /**
     * <p>
     * Uses the given file to config the configuration manager.
     * </p>
     * @param fileName config file to set up environment
     * @throws Exception when any exception occurs
     */
    public static void loadXMLConfig(String fileName) throws Exception {
        // set up environment
        ConfigManager config = ConfigManager.getInstance();
        File file = new File(fileName);

        config.add(file.getCanonicalPath());
    }

    /**
     * <p>
     * Clears all the namespaces from the configuration manager.
     * </p>
     * @throws Exception to JUnit.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator i = cm.getAllNamespaces(); i.hasNext();) {
            cm.removeNamespace((String) i.next());
        }
    }

    /**
     * <p>
     * This method recovers the <b>sample_config_persistence.xml</b> file from
     * the archive directory.
     * </p>
     */
    public static void recoverArchiveFile() {
        // copy the config file from archive directory to test_files directory
        InputStream inStream = null;
        FileOutputStream fs = null;
        try {
            int byteread = 0;
            // copy the config file from archive directory
            File oldFile = new File("test_files" + File.separator + "archive",
                    "sample_config_persistence.xml");
            inStream = new FileInputStream(oldFile);
            fs = new FileOutputStream("test_files" + File.separator
                    + "sample_config_persistence.xml");
            byte[] buffer = new byte[1024];
            while ((byteread = inStream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
            }
        } catch (IOException ioe) {
            // ignore
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
            if (fs != null) {
                try {
                    fs.close();
                } catch (IOException ioe) {
                    // ignore
                }
            }
        }

    }

    /**
     * <p>
     * Inserts some data to the tables which this component depends on.
     * </p>
     * <p>
     * This is used to simplify the testing.
     * </p>
     * @throws Exception to JUnit
     */
    public static void setUpDataBase() throws Exception {
        Connection connection = null;
        try {
            connection = getConnection();
            clearTables(connection);
            insertBasicDataToDB(connection);
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
     * Clears all the data from the tables using by this component.
     * </p>
     * <p>
     * This is used to simplify the testing.
     * </p>
     * @throws Exception to JUnit
     */
    public static void tearDownDataBase() throws Exception {
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
     * Read file into a Template.
     * @param fileName the file to read
     * @return the template
     * @throws Exception if any error occur when reading files.
     */
    public static Template readFileToTemplate(String fileName) throws Exception {
        Template template = new XsltTemplate();
        BufferedReader is = null;
        try {
            is = new BufferedReader(new FileReader(fileName));
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
     * Inserts some data to the tables for testing purpose.
     * </p>
     * @param connection Connection instance to access the database
     * @throws SQLException if exception occurs during database operation
     */
    private static void insertBasicDataToDB(Connection connection) throws SQLException {
        String[] sqlStatements = new String[]{
            "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                        + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                        + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (10001, "
                        + "'jobName07', CURRENT, 3000000, CURRENT, 'com.topcoder.util.scheduler.scheduling.Week', "
                        + "NULL, NULL, NULL, 2, 5, 'y', 'JOB_TYPE_EXTERNAL', 'dir', NULL, NULL, NULL)",
            "INSERT INTO job (jobid, name, startdate, starttime, enddate, dateunit, dateunitdays, "
                        + "dateunitweek, dateunitmonth, interval, recurrence, active, jobtype, jobcommand, "
                        + "dependencejobname, dependencejobstatus, dependencejobdelay) VALUES (10002, "
                        + "'newJobName01', NULL, NULL, NULL, 'com.topcoder.util.scheduler.scheduling.Week', "
                        + "NULL, NULL, NULL, 2, 4, 'y', 'JOB_TYPE_JAVA_CLASS', "
                        + "'com.topcoder.util.scheduler.scheduling.MyJob', 'jobName07', 'SUCCESSFUL', 10000)",
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
     * Deletes data from all the tables used by this component.
     * </p>
     * @param connection Connection instance to access the database
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
     * @return the connection instance for database operation
     * @throws Exception If unable to obtain a Connection
     */
    private static Connection getConnection() throws Exception {
        Connection connection = new DBConnectionFactoryImpl(
                DB_FACTORY_NAMESPACE).createConnection();
        connection.setAutoCommit(false);
        return connection;
    }

    /**
     * <p>
     * Closes the given Connection instance.
     * </p>
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

    /**
     * Gets the private member variable value for the given object.
     * @param obj the object to get the private member variable.
     * @param clz the <code>Class</code> type of the target private member
     *            variable.
     * @param name the name of the private member variable.
     * @return the value of the private member variable.
     * @throws Exception if fails to get the value of the private member
     *             variable.
     * @since 3.1
     */
    public static Object getPrivateFieldValue(Object obj, Class clz, String name)
        throws Exception {
        Class type;
        if (clz == null) {
            type = obj.getClass();
        } else {
            type = clz;
        }
        Field field = type.getDeclaredField(name);
        field.setAccessible(true);
        return field.get(obj);
    }
}
