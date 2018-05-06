/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;

import com.cronos.termsofuse.dao.TermsOfUseDao;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This class provides methods for testing this component.
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class TestsHelper {
    /**
     * <p>
     * Represents the path of test files.
     * </p>
     */
    public static final String TEST_FILES = "test_files" + File.separator;

    /**
     * <p>
     * Represents the configuration name for TermsOfUseDao used in tests.
     * </p>
     */
    public static final String CONFIG_TERMS = "termsOfUseDao";

    /**
     * <p>
     * Represents the configuration name for UserTermsOfUseDao used in tests.
     * </p>
     */
    public static final String CONFIG_USER_TERMS = "userTermsOfUseDao";

    /**
     * <p>
     * Represents the configuration name for projectTermsOfUseDao used in tests.
     * </p>
     */
    public static final String CONFIG_PROJECT_TERMS = "projectTermsOfUseDao";

    /**
     * <p>
     * Represents the invalid configuration name used in tests.
     * </p>
     */
    public static final String CONFIG_INVALID = "invalidConfig";

    /**
     * <p>
     * Represents the configuration file used in tests.
     * </p>
     */
    private static final String CONFIG_FILE = TEST_FILES + "config.xml";

    /**
     * <p>
     * Private constructor to prevent this class being instantiated.
     * </p>
     */
    private TestsHelper() {
        // empty
    }

    /**
     * <p>
     * Gets the configuration object used for tests.
     * </p>
     *
     * @param name
     *            the configuration name.
     *
     * @return the configuration object.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getConfig(String name) throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(name, new File(CONFIG_FILE));

        return obj.getChild(name);
    }

    /**
     * Checks the terms of use entity.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed steps for properties memberAgreeable and electronicallySignable.</li>
     * <li>Added steps for agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param method
     *            the method.
     * @param termsOfUse
     *            the terms of use entity.
     * @param values
     *            the values.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void checkTermsOfUse(String method, TermsOfUse termsOfUse, Object[] values)
        throws Exception {
        int index = 0;
        assertEquals("'" + method + "' should be correct.",
            ((Long) values[index++]).longValue(), termsOfUse.getTermsOfUseId());
        assertEquals("'" + method + "' should be correct.",
            ((Integer) values[index++]).intValue(), termsOfUse.getTermsOfUseTypeId());
        assertEquals("'" + method + "' should be correct.", (String) values[index++], termsOfUse.getTitle());
        assertEquals("'" + method + "' should be correct.", (String) values[index++], termsOfUse.getUrl());

        TermsOfUseAgreeabilityType agreeabilityType = termsOfUse.getAgreeabilityType();
        assertEquals("'" + method + "' should be correct.",
            ((Long) values[index++]).longValue(), agreeabilityType.getTermsOfUseAgreeabilityTypeId());
        assertEquals("'" + method + "' should be correct.",
            (String) values[index++], agreeabilityType.getName());
        assertEquals("'" + method + "' should be correct.",
            (String) values[index], agreeabilityType.getDescription());
    }

    /**
     * <p>
     * Gets a connection.
     * </p>
     *
     * @return the connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static Connection getConnection() throws Exception {
        ConfigurationObject config = getConfig(TestsHelper.CONFIG_TERMS);

        // Get configuration of DB Connection Factory
        ConfigurationObject dbConnectionFactoryConfig = config.getChild("dbConnectionFactoryConfig");

        // Create database connection factory using the extracted configuration
        DBConnectionFactoryImpl dbConnectionFactory = new DBConnectionFactoryImpl(dbConnectionFactoryConfig);

        return dbConnectionFactory.createConnection();
    }

    /**
     * <p>
     * Closes the given connection.
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void closeConnection(Connection connection) throws Exception {
        if ((connection != null) && (!connection.isClosed())) {
            connection.close();
        }
    }

    /**
     * <p>
     * Clears the database.
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearDB(Connection connection) throws Exception {
        Field generatorsField = IDGeneratorFactory.class.getDeclaredField("generators");
        generatorsField.setAccessible(true);
        Map<?, ?> generators = (Map<?, ?>) generatorsField.get(null);
        if (generators != null) {
            // Clear the generators
            generators.clear();
        }
        generatorsField.setAccessible(false);

        executeSQL(connection, TEST_FILES + "DBClear.sql");
    }

    /**
     * <p>
     * Loads data into the database.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed steps for memberAgreeable and electronicallySignable properties.</li>
     * <li>Added steps for agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param connection
     *            the given connection.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void loadDB(Connection connection) throws Exception {
        executeSQL(connection, TEST_FILES + "DBData1.sql");

        ConfigurationObject configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_TERMS);

        TermsOfUseDao termsOfUseDao = new TermsOfUseDaoImpl(configurationObject);
        termsOfUseDao.createTermsOfUse(getTermsOfUse(1, "t1", "url1", 2), "text1");
        termsOfUseDao.createTermsOfUse(getTermsOfUse(1, "t2", "url2", 1), "text2");
        termsOfUseDao.createTermsOfUse(getTermsOfUse(1, "t3", "url3", 1), "text3");
        termsOfUseDao.createTermsOfUse(getTermsOfUse(2, "t4", "url4", 3), "text4");

        executeSQL(connection, TEST_FILES + "DBData2.sql");
    }

    /**
     * <p>
     * Gets value for field of given object.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     *
     * @return the field value.
     */
    public static Object getField(Object obj, String field) {
        Object value = null;
        try {
            Field declaredField = null;
            try {
                declaredField = obj.getClass().getDeclaredField(field);
            } catch (NoSuchFieldException e) {
                // Ignore
            }
            if (declaredField == null) {
                // From the superclass
                declaredField = obj.getClass().getSuperclass().getDeclaredField(field);
            }

            declaredField.setAccessible(true);

            value = declaredField.get(obj);

            declaredField.setAccessible(false);
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        }

        return value;
    }

    /**
     * <p>
     * Executes the SQL statements in the file. Lines that are empty or starts with '#' will be ignore.
     * </p>
     *
     * @param connection
     *            the connection.
     * @param file
     *            the file.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static void executeSQL(Connection connection, String file) throws Exception {
        Statement stmt = null;
        try {
            stmt = connection.createStatement();

            String[] values = readFile(file).split(";");

            for (int i = 0; i < values.length; i++) {
                String sql = values[i].trim();
                if ((sql.length() != 0) && (!sql.startsWith("#"))) {
                    stmt.executeUpdate(sql);
                }
            }
        } finally {
            stmt.close();
        }
    }

    /**
     * <p>
     * Reads the content of a given file.
     * </p>
     *
     * @param fileName
     *            the name of the file to read.
     *
     * @return a string represents the content.
     *
     * @throws IOException
     *             if any error occurs during reading.
     */
    private static String readFile(String fileName) throws IOException {
        Reader reader = new FileReader(fileName);

        try {
            // Create a StringBuilder instance
            StringBuilder sb = new StringBuilder();

            // Buffer for reading
            char[] buffer = new char[1024];

            // Number of read chars
            int k = 0;

            // Read characters and append to string builder
            while ((k = reader.read(buffer)) != -1) {
                sb.append(buffer, 0, k);
            }

            // Return read content
            return sb.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException ioe) {
                // Ignore
            }
        }
    }

    /**
     * Creates a TermsOfUse instance.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Removed memberAgreeable and electronicallySignable properties.</li>
     * <li>Added agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUseTypeId
     *            the terms of use type id.
     * @param title
     *            the title of terms.
     * @param url
     *            the url of terms.
     * @param agreeabilityTypeId
     *            the agreeability type id.
     *
     * @return the TermsOfUse instance.
     */
    private static TermsOfUse getTermsOfUse(int termsOfUseTypeId, String title, String url, int agreeabilityTypeId) {
        TermsOfUse terms = new TermsOfUse();

        terms.setTermsOfUseTypeId(termsOfUseTypeId);
        terms.setTitle(title);
        terms.setUrl(url);

        TermsOfUseAgreeabilityType agreeabilityType = new TermsOfUseAgreeabilityType();
        agreeabilityType.setTermsOfUseAgreeabilityTypeId(agreeabilityTypeId);

        terms.setAgreeabilityType(agreeabilityType);

        return terms;
    }
}
