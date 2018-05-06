/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.topcoder.commons.utils.JDBCUtility;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationPersistenceException;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewApplicationStatus;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionType;
import com.topcoder.util.config.ConfigManager;

/**
 * The {@code Helper} class provides static methods used to facilitate component testing.
 *
 * @author KennyAlive
 * @version 1.0
 */
class Helper {
    /**
     * Constant for configuration filename for testing.
     */
    private static final String CONFIGURATION_FILENAME = "test_files/accuracy/Kenny/test.properties";

    /**
     * Represents the line separator.
     */
    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * <p>
     * Represents the connection string.
     * </p>
     */
    private static final String CONNECTION_STRING;

    /**
     * Represents the driver class name.
     */
    private static final String DRIVER_CLASS;

    /**
     * Represents the username.
     */
    private static final String USERNAME;

    /**
     * Represents the password.
     */
    private static final String PASSWORD;

    /**
     * Do not allow this class to be instantiated.
     */
    private Helper() {
        // Empty
    }

    /**
     * Initialize database parameters.
     */
    static {
        Properties config = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream("test_files/accuracy/Kenny/db.properties");
            config.load(stream);
        } catch (IOException e) {
            // Ignore
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                // Ignore
            }
        }

        DRIVER_CLASS = config.getProperty("driverClassName");
        CONNECTION_STRING = config.getProperty("connectionString");
        USERNAME = config.getProperty("username");
        PASSWORD = config.getProperty("password");
    }

    /**
     * Helper method for getting the configuration object from the specified namespace.
     *
     * @param namespace
     *          the configuration namespace
     *
     * @return the configuration object for the given namespace
     *
     * @throws IOException
     *              if some IO error occurred
     * @throws ConfigurationPersistenceException
     *              if some error occurred in configuration persistence component
     * @throws ConfigurationAccessException
     *              if failed to access configuration data
     */
    public static ConfigurationObject getConfiguration(String namespace)
        throws IOException, ConfigurationPersistenceException, ConfigurationAccessException {
        ConfigurationFileManager configurationFileManager = new ConfigurationFileManager(CONFIGURATION_FILENAME);
        ConfigurationObject configuration = configurationFileManager.getConfiguration(namespace);
        return configuration.getChild(namespace);
    }

    /**
     * Adds the necessary configurations.
     *
     * @throws Exception
     *             unexpected exception.
     */
    public static void preloadConfiguration() throws Exception {
//        ConfigManager cm = ConfigManager.getInstance();
//        for (Iterator<?> iter = cm.getAllNamespaces(); iter.hasNext();) {
//            cm.removeNamespace((String) iter.next());
//        }
//        ConfigManager configManager = ConfigManager.getInstance();
//        configManager.add("DBConnectionFactory.xml");
//        configManager.add("SearchBundleManager.xml");
//        configManager.add("DBUserRetrieval.xml");
//        configManager.add("PhaseManager.xml");
//        configManager.add("ProjectManager.xml");
    }

    /**
     * Creates {@code ReviewApplicationRole} instance for testing.
     *
     * @return the {@code ReviewApplicationRole} instance
     */
    public static ReviewApplicationRole createReviewApplicationRole(long positions) {
        return createReviewApplicationRole(1L, "Test Role", positions);
    }

    /**
     * Creates {@code ReviewApplicationRole} instance.
     *
     * @param id
     *            the application role id
     * @param name
     *            the application role name
     * @param position
     *            the number of positions for this role
     *
     * @return the {@code ReviewApplicationRole} instance
     */
    public static ReviewApplicationRole createReviewApplicationRole(long id, String name, long positions) {
        List<ReviewApplicationResourceRole> resourceRoles = new ArrayList<ReviewApplicationResourceRole>();
        resourceRoles.add(new ReviewApplicationResourceRole(1L, false));
        ReviewApplicationRole role = new ReviewApplicationRole(id, name, positions, resourceRoles);
        return role;
    }

    /**
     * Creates {@code ReviewAuctionCategory} instance for testing.
     *
     * @return the {@code ReviewAuctionCategory} instance
     */
    public static ReviewAuctionCategory createReviewAuctionCategory() {
        ReviewAuctionCategory category = new ReviewAuctionCategory(1L, "Contest Review");
        return category;
    }

    //------------------------------- ReviewAuctionType ---------------------------------------
    /**
     * Creates {@code ReviewAuctionType} instance for testing with no application roles defined.
     *
     * @return the {@code ReviewAuctionType} instance
     */
    public static ReviewAuctionType createReviewAuctionType_noRoles() {
        ReviewAuctionCategory category = createReviewAuctionCategory();

        ReviewAuctionType type = new ReviewAuctionType(1L, "Regular Contest Review", category,
                new ArrayList<ReviewApplicationRole>());
        return type;
    }

    /**
     * Creates {@code ReviewAuctionType} instance for testing with a single review application role.
     *
     * @return the {@code ReviewAuctionType} instance
     */
    public static ReviewAuctionType createReviewAuctionType_singleRole(long rolePositions) {
        ReviewAuctionCategory category = createReviewAuctionCategory();

        List<ReviewApplicationRole> roles = new ArrayList<ReviewApplicationRole>();
        roles.add(createReviewApplicationRole(rolePositions));

        ReviewAuctionType type = new ReviewAuctionType(1L, "Regular Contest Review", category, roles);
        return type;
    }

    /**
     * Creates {@code ReviewAuctionType} instance for testing with two review application roles.
     *
     * @param role1Positions
     *            role1 open positions
     * @param role2Positions
     *            role2 open positions
     *
     * @return the {@code ReviewAuctionType} instance
     */
    public static ReviewAuctionType createReviewAuctionType_twoRoles(long role1Positions, long role2Positions) {
        ReviewAuctionCategory category = createReviewAuctionCategory();

        List<ReviewApplicationRole> roles = new ArrayList<ReviewApplicationRole>();
        roles.add(createReviewApplicationRole(1L, "Test Role 1", role1Positions));
        roles.add(createReviewApplicationRole(2L, "Test Role 2", role2Positions));

        ReviewAuctionType type = new ReviewAuctionType(1L, "Regular Contest Review", category, roles);
        return type;
    }

    //-------------------------------- ReviewAuction ------------------------------------------
    /**
     * Creates {@code ReviewAuction} instance with default configuration.
     *
     * @return the {@code ReviewAuction} instance
     */
    private static ReviewAuction createDefaultReviewAuction() {
        ReviewAuction reviewAuction = new ReviewAuction();
        reviewAuction.setId(1L);
        reviewAuction.setProjectId(1L);
        reviewAuction.setAuctionType(createReviewAuctionType_noRoles());
        reviewAuction.setOpen(true);
        reviewAuction.setOpenPositions(new ArrayList<Long>());
        reviewAuction.setAssignmentDate(new Date(new Date().getTime() + 1000L * 3600L * 24L));
        return reviewAuction;
    }

    /**
     * Creates {@code ReviewAuction} instance for testing with auction type without roles defined.
     *
     * @return the {@code ReviewAuction} instance
     */
    public static ReviewAuction createReviewAuction_noRoles() {
        ReviewAuction reviewAuction = createDefaultReviewAuction();
        return reviewAuction;
    }

    /**
     * Creates {@code ReviewAuction} instance for testing with a single role.
     *
     * @param rolePositions
     *            the role open positions
     *
     * @return the {@code ReviewAuction} instance
     */
    public static ReviewAuction createReviewAuction_singleRole(long rolePositions) {
        ReviewAuction reviewAuction = createDefaultReviewAuction();
        reviewAuction.setAuctionType(createReviewAuctionType_singleRole(rolePositions));
        List<Long> openPositions = new ArrayList<Long>();
        for (ReviewApplicationRole role : reviewAuction.getAuctionType().getApplicationRoles()) {
            openPositions.add(role.getPositions());
        }
        reviewAuction.setOpenPositions(openPositions);
        return reviewAuction;
    }

    /**
     * Creates {@code ReviewAuction} instance for testing with two roles.
     *
     * @param role1Positions
     *            role1 open positions
     * @param role2Positions
     *            role2 open positions
     *
     * @return the {@code ReviewAuction} instance
     */
    public static ReviewAuction createReviewAuction_twoRoles(long role1Positions, long role2Positions) {
        ReviewAuction reviewAuction = createDefaultReviewAuction();
        reviewAuction.setAuctionType(createReviewAuctionType_twoRoles(role1Positions, role2Positions));
        List<Long> openPositions = new ArrayList<Long>();
        for (ReviewApplicationRole role : reviewAuction.getAuctionType().getApplicationRoles()) {
            openPositions.add(role.getPositions());
        }
        reviewAuction.setOpenPositions(openPositions);
        return reviewAuction;
    }

    //-------------------------------- ReviewApplication ----------------------------------------
    /**
     * Creates {@code ReviewApplication} instance with default configuration.
     *
     * @return the {@code ReviewAuction} instance
     */
    private static ReviewApplication createDefaultReviewApplication() {
        ReviewApplication application = new ReviewApplication();
        application.setId(1L);
        application.setUserId(1L);
        application.setAuctionId(1L);
        application.setApplicationRoleId(1L);

        ReviewApplicationStatus status = new ReviewApplicationStatus(1L, "Pending");
        application.setStatus(status);

        application.setCreateDate(new Date());
        return application;
    }

    /**
     * Creates {@code ReviewApplication} instance.
     *
     * @return the {@code ReviewAuction} instance
     */
    public static ReviewApplication createReviewApplication() {
        ReviewApplication application = createDefaultReviewApplication();
        return application;
    }

    /**
     * Creates {@code ReviewApplication} instance.
     *
     * @param applicationId
     *            the application id
     * @param userId
     *            the user id
     * @param applicationRoleId
     *            the application role id
     *
     * @return the {@code ReviewAuction} instance
     */
    public static ReviewApplication createReviewApplication(long applicationId, long userId, long applicationRoleId) {
        ReviewApplication application = createDefaultReviewApplication();
        application.setId(applicationId);
        application.setUserId(userId);
        application.setApplicationRoleId(applicationRoleId);
        return application;
    }

    //------------------------------- Reflection helpers ---------------------------------------
    /**
     * Returns the value of the field with the given name in the given object. The method
     * has access to all the fields, despite theirs access level. This is achieved through reflection.
     *
     * @param object
     *              the object that holds the field
     * @param fieldName
     *              the field's name
     *
     * @return the value of the field or null if the field was not found
     */
    public static Object getField(Object object, String fieldName) {
        return getField(object, fieldName, false);
    }

    /**
     * Returns the value of the field with the given name in the super type object. The method
     * has access to all the fields, despite theirs access level. This is achieved through reflection.
     *
     * @param object
     *              the object that holds the field
     * @param fieldName
     *              the field's name
     *
     * @return the value of the field or null if the field was not found
     */
    public static Object getSuperField(Object object, String fieldName) {
        return getField(object, fieldName, true);
    }

    /**
     * Sets the value of the field with the given name in the given object. The method has access
     * to all the fields, despite theirs access level. This is achieved through reflection.
     *
     * @param object
     *              the object that holds the field
     * @param fieldName
     *              the field's name
     * @param value
     *              the field's value
     */
    public static void setField(Object object, String fieldName, Object value) {
        setField(object, fieldName, value, false);
    }

    /**
     * Helper method for retrieving object fields via reflection.
     *
     * @param object
     *              the object that holds the field
     * @param fieldName
     *              the field's name
     * @param useSuperClass
     *          indicates whether to get field from base class or from this class
     *
     * @throws RuntimeException
     *          if fieldName field is absent in the given object. Used primarily during debugging
     *          to fix typos in fields names
     *
     * @return the value of the field or null if the field was not found
     */
    private static Object getField(Object object, String fieldName, boolean useSuperClass) {
        Object value = null;
        try {
            Class<?> cls = object.getClass();
            if (useSuperClass) {
                cls = cls.getSuperclass();
            }
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            try {
                value = field.get(object);
            } finally {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("There is a typo in field name '" + fieldName + "'");
        } catch (IllegalAccessException e) {
            // Ignore
        }
        return value;
    }

    /**
     * The helper method used to implement corresponding public methods.
     * Sets the value of the field with the given name in the given object if <code>useSuperClass</code> is false,
     * or in the super type sub-object of the given object if <code>useSuperClass</code> is true.
     * The method has access to all the fields, despite theirs access level. This is achieved through reflection.
     *
     * @param object
     *              the object that holds the field or whose super type sub-object holds the field.
     * @param fieldName
     *              the field's name
     * @param value
     *              the field's value
     * @param useSuperClass
     *              indicates whether to get field from base class or from this class.
     */
    private static void setField(Object object, String fieldName, Object value, boolean useSuperClass) {
        try {
            Class<?> cls = object.getClass();
            if (useSuperClass) {
                cls = cls.getSuperclass();
            }
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            try {
                field.set(object, value);
            } finally {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        }
    }

    /**
     * Reads the content of a given file.
     *
     * @param fileName
     *            the name of the file to read
     *
     * @return a string with content of the given file
     *
     * @throws IOException
     *             if any error occurs during reading
     */
    private static String readFile(String fileName) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line).append(LINE_SEPARATOR);
                line = reader.readLine();
            }
            return sb.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Gets a connection.
     *
     * @return the connection
     *
     * @throws Exception
     *             to JUnit
     */
    static Connection getConnection() throws Exception {
        Class.forName(DRIVER_CLASS);
        return DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
    }

    /**
     * Closes the given connection.
     *
     * @param connection
     *            the given connection
     */
    static void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * Closes the given statement.
     *
     * @param statement
     *            the given statement
     */
    static void closeStatement(Statement statement) throws Exception {
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * Run the given SQL file.
     *
     * @param filePath
     *            SQL file path
     *
     * @throws Exception
     *             to JUnit
     */
    public static void executeSQL(String filePath) throws Exception {
        Connection connection = getConnection();
        Statement statement = null;

        connection.setAutoCommit(true);
        try {
            String content = readFile(filePath);
            String[] contents = content.split(";");
            for (int i = 0; i < contents.length; i++) {
                String st = contents[i].trim();
                if (st.length() == 0 || st.startsWith("--")) {
                    continue;
                }
                statement = connection.createStatement();
                statement.execute(st);
            }
        } finally {
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    /**
     * Clears the database.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void clearDB() throws Exception {
        executeSQL("test_files/accuracy/sql/db_clear.sql");
    }

    /**
     * Inserts test records into the database.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void insertDB() throws Exception {
//        executeSQL("test_files/accuracy/sql/db_insert.sql");
    }

    /**
     * Gets the number of resources associated with the given project.
     *
     * @throws Exception
     *             to JUnit
     */
    public static long getProjectResourcesCount(long projectId) throws Exception {
        Connection connection = getConnection();
        connection.setAutoCommit(true);
        try {
            Object[][] result = JDBCUtility.executeQuery(connection,
                    "SELECT count(*) FROM resource WHERE project_id=?",
                    new int[] { Types.DECIMAL }, new Object[] { projectId },
                    new Class<?>[] { Long.class }, Exception.class);
            return (Long) result[0][0];
        } finally {
            closeConnection(connection);
        }

    }
}
