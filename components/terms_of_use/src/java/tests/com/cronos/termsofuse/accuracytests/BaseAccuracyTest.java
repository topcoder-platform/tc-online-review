/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.accuracytests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

/**
 * <p>
 * This is base class for Accuracy tests.
 * </p>
 *
 * <p>
 * Change in 1.1: Update some methods to support the TermsOfUse#agreeabilityType field.
 * </p>
 *
 * @author sokol, fish_ship
 * @version 1.1
 */
public class BaseAccuracyTest extends TestCase {
    /**
     * <p>
     * Represents terms of use configuration name.
     * </p>
     */
    protected static final String TERMS_OF_USE_CONFIGURATION = "termsOfUseDao";

    /**
     * <p>
     * Represents user terms of use configuration name.
     * </p>
     */
    protected static final String USER_TERMS_OF_USE_CONFIGURATION = "userTermsOfUseDao";

    /**
     * <p>
     * Represents project terms of use configuration name.
     * </p>
     */
    protected static final String PROJECT_TERMS_OF_USE_CONFIGURATION = "projectTermsOfUseDao";

    /**
     * <p>
     * Represents query for inserting terms of use.
     * </p>
     *
     * <p>
     * Change in 1.1: Update the insert sql to according to the database changes.
     * </p>
     */
    private static final String INSERT_TERMS_OF_USE_QUERY = "INSERT INTO terms_of_use (terms_of_use_id, terms_text, "
            + "terms_of_use_type_id, title, url, terms_of_use_agreeability_type_id) VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * <p>
     * Represents test files directory name.
     * </p>
     */
    private static final String TEST_FILES = System.getProperty("user.dir") + File.separator + "test_files"
            + File.separator + "accuracytests" + File.separator;

    /**
     * <p>
     * Represents clear database script name.
     * </p>
     */
    private static final String CLEAR_DATABASE_SCRIPT = TEST_FILES + "clear.sql";

    /**
     * <p>
     * Represents initialize database script name.
     * </p>
     */
    private static final String INIT_DATABASE_SCRIPT1 = TEST_FILES + "populate1.sql";

    /**
     * <p>
     * Represents initialize database script name.
     * </p>
     */
    private static final String INIT_DATABASE_SCRIPT2 = TEST_FILES + "populate2.sql";

    /**
     * <p>
     * Represents file name with application configuration.
     * </p>
     */
    private static final String CONFIGURATION_FILE = TEST_FILES + "accuracyConfig.xml";

    /**
     * <p>
     * Represents database configuration name.
     * </p>
     */
    private static final String DB_CONNECTION_FACTORY_CONFIGURATION = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void setUp() throws Exception {
        super.setUp();
        runSql(CLEAR_DATABASE_SCRIPT);
        initDatabase();
    }

    /**
     * <p>
     * Initializes database for testing.
     * </p>
     *
     * <p>
     * Change in 1.1: Add support to the terms of use agreeability type.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    private static void initDatabase() throws Exception {
        // insert test data according to demo
        runSql(INIT_DATABASE_SCRIPT1);
        TermsOfUseAgreeabilityType noAgreeabilityType = createTermsOfUseAgreeabilityType(1, null, null);
        TermsOfUseAgreeabilityType agreeabilityType = createTermsOfUseAgreeabilityType(2, null, null);
        insertTermsOfUse(createTermsOfUse(1L, 1, "t1", "url1", agreeabilityType), "text1");
        insertTermsOfUse(createTermsOfUse(2L, 1, "t2", "url2", noAgreeabilityType), "text2");
        insertTermsOfUse(createTermsOfUse(3L, 1, "t3", "url3", noAgreeabilityType), "text3");
        insertTermsOfUse(createTermsOfUse(4L, 2, "t4", "url4", agreeabilityType), "text4");
        runSql(INIT_DATABASE_SCRIPT2);
    }

    /**
     * <p>
     * Inserts terms of use into database.
     * </p>
     *
     * <p>
     * Change in 1.1: Add support to the terms of use agreeability type.
     * </p>
     *
     * @param termsOfUse
     *            the terms of use to insert
     * @param text
     *            the terms text
     * @throws Exception
     *             if any error occurs
     */
    private static void insertTermsOfUse(TermsOfUse termsOfUse, String text) throws Exception {
        Connection conn = getConnection();
        PreparedStatement ps = null;
        try {
            String query = INSERT_TERMS_OF_USE_QUERY;
            ps = conn.prepareStatement(query);
            ps.setLong(1, termsOfUse.getTermsOfUseId());
            ps.setBytes(2, text.getBytes());
            ps.setInt(3, termsOfUse.getTermsOfUseTypeId());
            ps.setString(4, termsOfUse.getTitle());
            ps.setString(5, termsOfUse.getUrl());
            ps.setInt(6, termsOfUse.getAgreeabilityType().getTermsOfUseAgreeabilityTypeId());
            ps.executeUpdate();
        } finally {
            closeConnection(conn);
            closeStatement(ps);
        }
    }

    /**
     * <p>
     * Closes given SQL connection.
     * </p>
     *
     * @param conn
     *            the connection to close
     * @throws SQLException
     *             if any error occurs
     */
    private static void closeConnection(Connection conn) throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * <p>
     * Creates TermsOfUse instance with given values.
     * </p>
     *
     * <p>
     * Change in 1.1: Add support to the terms of use agreeability type.
     * </p>
     *
     * @param id
     *            the id
     * @param typeId
     *            the type id
     * @param title
     *            the title
     * @param url
     *            the url
     * @param agreeabilityType
     *            the terms of useAgreeability type
     * @return created TermsOfUse instance with given values
     */
    protected static TermsOfUse createTermsOfUse(long id, int typeId, String title, String url,
            TermsOfUseAgreeabilityType agreeabilityType) {
        TermsOfUse termsOfUse = new TermsOfUse();
        termsOfUse.setTermsOfUseId(id);
        termsOfUse.setTermsOfUseTypeId(typeId);
        termsOfUse.setTitle(title);
        termsOfUse.setUrl(url);
        termsOfUse.setAgreeabilityType(agreeabilityType);
        return termsOfUse;
    }

    /**
     * <p>
     * Runs SQL scripts from given file.
     * </p>
     *
     * @param filePath
     *            the file path of SQL scripts
     * @throws Exception
     *             if any error occurs
     */
    private static void runSql(String filePath) throws Exception {
        String[] scripts = getScriptsFromFile(filePath);
        Connection connection = getConnection();
        PreparedStatement statement = null;
        try {
            connection.setAutoCommit(true);
            for (String sql : scripts) {
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();
            }
        } finally {
            closeConnection(connection);
            closeStatement(statement);
        }
    }

    /**
     * <p>
     * Creates SQL connection to database using properties from configuration file.
     * </p>
     *
     * @return SQL connection to database
     * @throws Exception
     *             if any error occurs
     */
    private static Connection getConnection() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(getConfiguration(
                TERMS_OF_USE_CONFIGURATION).getChild(DB_CONNECTION_FACTORY_CONFIGURATION));
        return dbConnectionFactory.createConnection();
    }

    /**
     * <p>
     * Retrieves SQL scripts from file.
     * </p>
     *
     * @param filePath
     *            the file path of SQL scripts
     * @return retrieved SQL scripts from file
     * @throws Exception
     *             if any error occurs
     */
    private static String[] getScriptsFromFile(String filePath) throws Exception {
        BufferedReader reader = null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                if (line.trim().length() > 0) {
                    sb.append(line.trim());
                    if (!line.endsWith(";")) {
                        sb.append(" ");
                    }
                }
                line = reader.readLine();
            }
            return sb.toString().split(";");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * <p>
     * Retrieves row count after executing query.
     * </p>
     *
     * @param query
     *            the query to get row count
     * @return row count after executing query
     * @throws Exception
     *             if any error occurs
     */
    protected static int getQueryRowCount(String query) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int result = 0;
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result++;
            }
            return result;
        } finally {
            closeResultSet(resultSet);
            closeStatement(statement);
            closeConnection(connection);
        }
    }

    /**
     * <p>
     * Closes given result set.
     * </p>
     *
     * @param resultSet
     *            the result set to close
     * @throws SQLException
     *             if any exception occurs
     */
    private static void closeResultSet(ResultSet resultSet) throws SQLException {
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * <p>
     * Closes given result set.
     * </p>
     *
     * @param statement
     *            the statement to close
     * @throws SQLException
     *             if any exception occurs
     */
    private static void closeStatement(PreparedStatement statement) throws SQLException {
        if (statement != null) {
            statement.close();
        }
    }

    /**
     * <p>
     * Retrieves configuration from configuration file.
     * </p>
     *
     * @param name
     *            the configuration name.
     * @return the configuration object from configuration file.
     * @throws Exception
     *             if any error occurs
     */
    protected static ConfigurationObject getConfiguration(String name) throws Exception {
        // Get configuration
        ConfigurationObject obj = new XMLFilePersistence().loadFile(name, new File(CONFIGURATION_FILE));
        return obj.getChild(name);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void tearDown() throws Exception {
        super.tearDown();
        runSql(CLEAR_DATABASE_SCRIPT);
    }

    /**
     * <p>
     * Creates TermsOfUseType instance with given values.
     * </p>
     *
     * @param id
     *            the id
     * @param description
     *            the description
     * @return created TermsOfUseType instance with given values
     */
    protected static TermsOfUseType createTermsType(int id, String description) {
        TermsOfUseType type = new TermsOfUseType();
        type.setTermsOfUseTypeId(id);
        type.setDescription(description);
        return type;
    }

    /**
     * <p>
     * Creates TermsOfUseAgreeabilityType instance with given values.
     * </p>
     *
     * @param id
     *            the id
     * @param description
     *            the description
     * @param name
     *            the name
     * @return created TermsOfUseAgreeabilityType instance with given values
     *
     * @since 1.1
     */
    protected static TermsOfUseAgreeabilityType createTermsOfUseAgreeabilityType(int id, String name,
            String description) {
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(id);
        type.setName(name);
        type.setDescription(description);
        return type;
    }

    /**
     * <p>
     * Compares given TermsOfUse instances.
     * </p>
     *
     * <p>
     * Change in 1.1: Add support to TermsOfUse#AgreeabilityType field.
     * </p>
     *
     * @param termsOfUse
     *            the terms of use to compare
     * @param otherTermsOfUse
     *            the terms of use to compare
     * @return true if given TermsOfUse instances have equal fields, false otherwise
     */
    protected static boolean compareTermsOfUse(TermsOfUse termsOfUse, TermsOfUse otherTermsOfUse) {
        TermsOfUseAgreeabilityType type = termsOfUse.getAgreeabilityType();
        TermsOfUseAgreeabilityType otherType = otherTermsOfUse.getAgreeabilityType();
        return termsOfUse.getTermsOfUseId() == otherTermsOfUse.getTermsOfUseId()
                && termsOfUse.getTermsOfUseTypeId() == otherTermsOfUse.getTermsOfUseTypeId()
                && termsOfUse.getTitle().equals(otherTermsOfUse.getTitle())
                && termsOfUse.getUrl().equals(otherTermsOfUse.getUrl())
                && type.getTermsOfUseAgreeabilityTypeId() == otherType.getTermsOfUseAgreeabilityTypeId()
                && type.getName().equals(otherType.getName())
                && type.getDescription().equals(otherType.getDescription());
    }

    /**
     * <p>
     * Compares given TermsOfUseType instances.
     * </p>
     *
     * @param type1
     *            the terms of use type to compare
     * @param type2
     *            the terms of use type to compare
     * @return true if given TermsOfUseType instances have equal fields, false otherwise
     */
    protected static boolean compareTermsOfUseType(TermsOfUseType type1, TermsOfUseType type2) {
        return type1.getDescription().equals(type2.getDescription())
                && type1.getTermsOfUseTypeId() == type2.getTermsOfUseTypeId();
    }
}
