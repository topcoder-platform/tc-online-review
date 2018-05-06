/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.deliverables.accuracytests;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

/**
 * Set of help functions.
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class AccuracyTestHelper {

    /**
     * Storage for all configurations tests.
     */
    private static final String CONFIG_TESTS_FILE = "accuracytests/configurations.xml";

    /**
     * Storage for sql statements for inserting values in defaultusers table.
     */
    private static final String PRECONDITIONS_SQL_FILE =
        "test_files/accuracytests/preconditions.sql";

    /**
     * empty construction.
     */
    private AccuracyTestHelper() {
    // empty
    }

    /**
     * Adding data from sql file for testing purposes.
     * @param connection connection to database
     * @throws Exception wrap all exceptions
     */
    public static void addTestData(Connection connection) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(PRECONDITIONS_SQL_FILE));
        String sql;
        while ((sql = reader.readLine()) != null) {
            // ececute each sql insert statement,
            if (sql.length() > 2) { try
            {
                connection.createStatement().executeUpdate(sql);
            }
            catch (Exception e)
            {
				System.out.println("e : " + sql);
				throw e;
            }
            }
        }
    }

    /**
     * Deleting all data from specified table.
     * @param connection connection to database
     * @throws Exception wrap all exceptions
     */
    public static void clearTables(Connection connection) throws Exception {
        String[] tables =
            new String[] { "review_comment", "review", "submission", "upload", "resource", "project_phase",
                "project", "scorecard", "project_category_lu", "project_type_lu" , "project_status_lu", 
			    "resource_role_lu", "phase_status_lu", "phase_type_lu", "upload_status_lu", "upload_type_lu", "submission_status_lu", 
			    "scorecard_type_lu", "scorecard_status_lu", "comment_type_lu"};
        for (int i = 0; i < tables.length; i++) {
            String query = "DELETE FROM " + tables[i];
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
        }
    }

    /**
     * Loading test configurations.
     * @throws Exception wrap all exceptions
     */
    public static void loadAllConfig() throws Exception {
        clearTestConfig();
        ConfigManager config = ConfigManager.getInstance();
        config.add(CONFIG_TESTS_FILE);
    }

    /**
     * Clearing all configurations.
     * @throws Exception wrap all exceptions
     */
    public static void clearTestConfig() throws Exception {
        ConfigManager config = ConfigManager.getInstance();
        Iterator iter = config.getAllNamespaces();
        while (iter.hasNext()) {
            String namespace = (String) iter.next();
            config.removeNamespace(namespace);
        }
    }

    /**
     * Getting the value of the field through introspection.
     * @param object object
     * @param fieldName name of field
     * @return value
     * @throws Exception wrap all exceptions
     */
    public static Object getDeclaredField(Object object, String fieldName) throws Exception {
        Class clazz = object.getClass();
        while (clazz != null) {
            if (clazz.getSuperclass() == Object.class) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        Field connectionFactoryField = clazz.getDeclaredField(fieldName);
        connectionFactoryField.setAccessible(true);
        return connectionFactoryField.get(object);
    }
}