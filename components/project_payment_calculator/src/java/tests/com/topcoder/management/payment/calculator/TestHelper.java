/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.payment.calculator.impl.DefaultProjectPaymentCalculator;


/**
 * <p>
 * A class providing helper methods for testing.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public final class TestHelper {
    /**
     * <p>
     * Represents the configuration file used for testing.
     * </p>
     */
    public static final String CONFIG_FILE = "test_files" + File.separator + "config.properties";

    /**
     * <p>
     * Private modifier prevents the creation of new instance.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * <p>
     * Gets the field value of given object by using reflection. If field with given name is not found in the obj'
     * class then the field is searched in obj' superclass.
     * </p>
     *
     * @param obj
     *            the object to get its field value.
     * @param fieldName
     *            the field name.
     * @return the field value.
     * @throws Exception
     *             if any error occurs while retrieving field value.
     */
    public static Object getFieldValue(Object obj, String fieldName) throws Exception {
        Field field = null;
        try {
            field = getField(obj, fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            if (field != null) {
                field.setAccessible(false);
            }
        }
    }

    /**
     * <p>
     * Gets the Field instance of given obj by using reflection. If field with given name is not found in the obj'
     * class then the field is searched in obj' superclass.
     * </p>
     *
     * @param obj
     *            the object to get
     * @param fieldName
     *            the field name
     * @return a Field instance
     * @throws NoSuchFieldException
     *             if the field cannot be found in superclass
     */
    private static Field getField(Object obj, String fieldName) throws NoSuchFieldException {
        // get the class
        Class<?> cls = obj.getClass();
        try {
            // get the field
            return cls.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // field is not found
            // check the superclass
            return cls.getSuperclass().getDeclaredField(fieldName);
        }
    }

    /**
     * <p>
     * Inserts necessary records to database for testing.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public static void initDB() throws Exception {
        executeUpdate("test_files" + File.separator + "data.sql");
    }

    /**
     * <p>
     * Removes previously inserted records from database after test.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public static void cleanDB() throws Exception {
        executeUpdate("test_files" + File.separator + "clean.sql");
    }

    /**
     * Executes list of INSERT/DELETE queries in given sqlFile.
     *
     * @param sqlFile
     *            the file containing SQL queries to execute.
     * @throws Exception
     *             if any error occurs.
     */
    private static void executeUpdate(String sqlFile) throws Exception {
        String[] queries = readFile(sqlFile).split(";");
        Connection conn = null;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            for (int i = 0; i < queries.length; i++) {
                String query = queries[i].trim();
                if (query.length() == 0) {
                    continue;
                }
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.executeUpdate();
                pstmt.close();
            }
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    // ignore
                }
            }
            e.printStackTrace();
            throw e;
        } finally {
            close(conn);
        }
    }

    /**
     * <p>
     * Read the content of a file.
     * </p>
     *
     * @param file
     *            the file to read.
     * @return the file's content as string.
     * @throws IOException
     *             if any error occurs.
     */
    private static String readFile(String file) throws IOException {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[8192];
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            int read = 0;
            while ((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // ignored
                }
            }
        }
        return builder.toString();
    }

    /**
     * <p>
     * Create a connection to database.
     * </p>
     *
     * @return database connection.
     * @throws Exception
     *             if any error occurs.
     */
    private static Connection getConnection() throws Exception {
        ConfigurationFileManager cfm = new ConfigurationFileManager(TestHelper.CONFIG_FILE);

        ConfigurationObject root = cfm.getConfiguration(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject config = root.getChild(DefaultProjectPaymentCalculator.class.getName());
        ConfigurationObject dbConfig = config.getChild("db_connection_factory_config");
        DBConnectionFactory connectionFactory = new DBConnectionFactoryImpl(dbConfig);
        return connectionFactory.createConnection();
    }

    /**
     * <p>
     * Close database connection.
     * </p>
     *
     * @param conn
     *            the connection to close.
     */
    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }
}
