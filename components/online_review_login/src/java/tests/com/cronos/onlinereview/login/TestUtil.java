/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */



package com.cronos.onlinereview.login;

import com.topcoder.naming.jndiutility.JNDIUtils;
import com.topcoder.security.NoSuchUserException;
import com.topcoder.security.UserPrincipal;
import com.topcoder.security.admin.PrincipalMgrRemote;
import com.topcoder.security.admin.PrincipalMgrRemoteHome;
import com.topcoder.util.config.ConfigManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import java.lang.reflect.Field;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Iterator;
import java.util.Properties;

import javax.naming.Context;

/**
 * Unit tests utilities.
 * <p>
 * This class defines some common routines to help unit tests. It includes routines to load configurations, to clear
 * configurations, to create user via EJB and get private field value via reflection.
 * </p>
 *
 * @author maone
 * @version 1.0
 */
public class TestUtil {

    /**
     * Creates table file path.
     */
    private static final String CREATE_TABLE_PATH = "test_files/test.prepare/Security_Manager.SQL";

    /**
     * Drops table file path.
     */
    private static final String DROP_TABLE_PATH = "test_files/test.prepare/clear.sql";

    /**
     * Inserts data into table file path.
     */
    private static final String ISNERT_DATA_PATH = "test_files/test.prepare/insert.sql";

    /**
     * Separator for sql statement.
     */
    private static final String SQL_SEPARATOR = ";";

    /**
     * Represents the test_files directory in which all the test files reside in.
     */
    public static final String TEST_DIR = "test_files/";

    /**
     * Empty private constructor.
     */
    private TestUtil() {

        // empty
    }

    /**
     * Load all the configurations required to do testing.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void loadAllConfigurations() throws Exception {
        clearAllConfigurations();

        ConfigManager cm = ConfigManager.getInstance();

        cm.add(new File(TEST_DIR + "OnlineReviewLogin.xml").getAbsolutePath());
        cm.add(new File(TEST_DIR + "OnlineReviewLogin_invalid.xml").getAbsolutePath());
        cm.add("com.topcoder.naming.jndiutility", new File(TEST_DIR + "JNDIUtils.properties").getAbsolutePath(),
               ConfigManager.CONFIG_PROPERTIES_FORMAT);
    }

    /**
     * Clear all the configurations in ConfigManager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearAllConfigurations() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext(); ) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * Get field value via reflection.
     *
     * @param obj
     *            the object to retrieve value from
     * @param name
     *            the name of the field
     * @return the value of the field
     * @throws Exception
     *             to JUnit.
     */
    public static Object getFieldValue(Object obj, String name) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);

        field.setAccessible(true);

        return field.get(obj);
    }

    /**
     * Get field value via reflection.
     *
     * @param obj
     *            the object to set value to
     * @param name
     *            the name of the field
     * @param value
     *            the value to set for the field
     * @throws Exception
     *             to JUnit.
     */
    public static void setFieldValue(Object obj, String name, Object value) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);

        field.setAccessible(true);

        field.set(obj, value);
    }

    /**
     * Create a user with given name and password. If the user does already exist, the password will be updated.
     *
     * @param name
     *            the name of the user
     * @param password
     *            the passowrd of the user
     * @return the generated id of the user.
     * @throws Exception
     *             to JUnit.
     */
    public static UserPrincipal createUser(String name, String password) throws Exception {

        // retrieve remote principal manager from configuration
        String contextName = Util.getRequiredPropertyString(TestUtil.class.getName(), "context_name");
        Context context = JNDIUtils.getContext(contextName);
        String principalBean = Util.getRequiredPropertyString(TestUtil.class.getName(), "principal_bean_name");
        PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) context.lookup(principalBean);
        PrincipalMgrRemote mgr = home.create();

        // create or update the user
        UserPrincipal principal = null;

        try {
            principal = mgr.getUser(name);
            mgr.editPassword(principal, password, null);
        } catch (NoSuchUserException e) {
            principal = mgr.createUser("myname", "mypw", null);
        }

        return principal;
    }

    /**
     * Remove the specified user from remote principal manager.
     *
     * @param principal
     *            the principla to remove
     * @throws Exception
     *             to JUnit.
     */
    public static void removeUser(UserPrincipal principal) throws Exception {

        // retrieve remote principal manager from configuration
        String contextName = Util.getRequiredPropertyString(TestUtil.class.getName(), "context_name");
        Context context = JNDIUtils.getContext(contextName);
        String principalBean = Util.getRequiredPropertyString(TestUtil.class.getName(), "principal_bean_name");
        PrincipalMgrRemoteHome home = (PrincipalMgrRemoteHome) context.lookup(principalBean);
        PrincipalMgrRemote mgr = home.create();

        mgr.removeUser(principal, null);
    }

    /**
     * <p>
     * Reads given file.
     * </p>
     *
     * @param path
     *            the path to the file to read
     * @return the contents of the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private static String readFile(String path) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));

            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb.toString();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {

                    // ignore
                }
            }
        }
    }

    /**
     * Sets up the database.
     *
     * @param con
     *            the connection to use.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    public static void setUpDataBase(Connection con) throws SQLException, IOException {
        executeSqlFile(con, CREATE_TABLE_PATH);

        executeSqlFile(con, ISNERT_DATA_PATH);
    }

    /**
     * Clears the database.
     *
     * @param con
     *            the connection to use.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    public static void clearDataBase(Connection con) throws SQLException, IOException {
        executeSqlFile(con, DROP_TABLE_PATH);
    }

    /**
     * Executes the sql file.
     *
     * @param con
     *            the connection to use.
     * @param sqlPath
     *            the sql file path.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    public static void executeSqlFile(Connection con, String sqlPath) throws SQLException, IOException {
        Statement st = con.createStatement();

        String[] sqls = readFile(sqlPath).split(SQL_SEPARATOR);

        for (String sql : sqls) {
            if (!(sql.trim().trim().length() == 0)) {
                st.addBatch(sql);
            }
        }

        st.executeBatch();
    }

    /**
     * Loads the configuration file given by configFile into a Properties object.
     *
     * @param configFile
     *            the path of the configuration file.
     * @return the loaded Properties object
     * @throws Exception
     *             to JUnit
     */
    public static Properties loadProperties(String configFile) throws Exception {

        // Create an empty Properties
        Properties prop = new Properties();

        InputStream in = null;

        try {

            // Create an input stream from the given configFile
            in = new FileInputStream(configFile);

            // Load the properties
            prop.load(in);
        } finally {

            // Close the input stream
            if (in != null) {
                in.close();
            }
        }

        return prop;
    }

    /**
     * Create a new db connection to return.
     *
     * @param properties
     *            the properties file to use
     * @return the db connection.
     * @throws Exception
     *             to JUnit
     */
    public static Connection createConnection(Properties properties) throws Exception {
        String dbUrl = properties.getProperty("dburl");
        String dbUser = properties.getProperty("dbuser");
        String dbPassword = properties.getProperty("dbpassword");
        String dbClass = properties.getProperty("dbdriver");

        Class.forName(dbClass);

        if ((dbUser != null) && (!(dbUser.trim().length() == 0))) {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } else {
            return DriverManager.getConnection(dbUrl);
        }
    }

    /**
     * Closes the database connection.
     *
     * @param connection
     *            the database connection to close
     * @throws Exception
     *             to JUnit
     */
    public static void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Executes the specified sql query.
     *
     * @param con
     *            the connection to use
     * @param sql
     *            the query string
     * @return the calling of the result set's next method
     * @throws Exception
     *             to JUnit
     */
    static boolean executeSql(Connection con, String sql) throws Exception {
        Statement stmt = null;

        try {
            stmt = con.createStatement();

            return stmt.executeQuery(sql).next();
        } finally {
            stmt.close();
        }
    }
}
