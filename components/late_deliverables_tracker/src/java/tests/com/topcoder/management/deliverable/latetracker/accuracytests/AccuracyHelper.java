/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.util.config.ConfigManager;

/**
 * Helper class for the accuracy test.
 * <p>
 * <b>Thread safety:</b> This class is immutable and so thread safe.
 *
 * @author mumujava, KLW, gjw99
 * @version 1.3
 */
public class AccuracyHelper extends TestCase {

    /**
     * the dependency configurations.
     */
    private static final String[] DEPENDENCIES_CONFIGS = new String[] {"accuracy/config/Project_Management.xml",
        "accuracy/config/Phase_Management.xml", "accuracy/config/Upload_Resource_Search.xml",
        "accuracy/config/SearchBuilderCommon.xml", "accuracy/config/DB_Factory.xml",
        "accuracy/config/Logging_Wrapper.xml",
        "accuracy/config/InformixPhasePersistence.xml",
        "accuracy/config/SearchBundleManager.xml",
        "accuracy/config/LateDeliverableManagerImpl.xml"};

    /**
     * The db connection.
     */
    protected static Connection connection;

    static {
        try {
            buildConfig();
            DBConnectionFactoryImpl dbFactory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
            connection = dbFactory.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the configuration object from the given file with the given namespace.
     *
     * @param file
     *            the configuration file.
     * @param namespace
     *            the namespace.
     * @return the configuration object.
     * @throws Exception to junit
     */
    public static ConfigurationObject getConfigurationObject(String file, String namespace) throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager();

        manager.loadFile("root", file);

        ConfigurationObject config = manager.getConfiguration("root");

        return config.getChild(namespace);
    }

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to junit
     */
    protected void setUp() throws Exception {
        buildConfig();
        executeSqlFile("test_files/accuracy/clear.sql");
        executeSqlFile("test_files/accuracy/prepare.sql");
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception to junit
     */
    protected void tearDown() throws Exception {
        clearConfig();
        executeSqlFile("test_files/accuracy/clear.sql");
    }

    /**
     * Builds the configuration.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void buildConfig() throws Exception {
        clearConfig();

        ConfigManager configManager = ConfigManager.getInstance();

        // add all dependencies config
        for (String config : DEPENDENCIES_CONFIGS) {
            configManager.add(config);
        }
    }

    /**
     * Clears all configurations.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearConfig() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator<?> iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Executes the sql scripts in the given sql file.
     * </p>
     *
     * @param sqlPath the path of the sql file to execute
     *
     * @throws Exception if exception occurs during database operation
     */
    public static void executeSqlFile(String sqlPath) throws Exception {
        String[] sqlStatements = getFileAsString(sqlPath).split(";");

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            for (int i = 0; i < sqlStatements.length; i++) {
                if (sqlStatements[i].trim().length() != 0 && !sqlStatements[i].trim().startsWith("--")) {
                    stmt.executeUpdate(sqlStatements[i]);
                }
            }
        } finally {
            if (stmt != null) {
                stmt.close();
            }
        }
    }

    /**
     * Gets the file content as string.
     *
     * @param filePath the file path
     * @return The content of file
     * @throws Exception to JUnit
     */
    public static String getFileAsString(String filePath) throws Exception {
        StringBuilder buf = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));
        try {
            String s;
            while ((s = in.readLine()) != null) {
                if (!s.trim().startsWith("--")) {
                    buf.append(s);
                }
            }
            return buf.toString();
        } finally {
            in.close();
        }
    }

        /**
         * <p>
         * update value to a column in the given table.
         * </p>
         *
         * @param table
         *            the table name.
         * @param column
         *            the column name.
         * @param value
         *            the value.
         *
         * @throws Exception
         *             to JUnit
         */
    public static void updateColumn(String table, String column, Object value) throws Exception {
        PreparedStatement stmt = null;

        try {
            stmt = connection.prepareStatement("UPDATE  " + table + " SET " + column + "=?");
            stmt.setObject(1, value);
            stmt.executeUpdate();
        } finally {
            stmt.close();
            // connection.close();
        }
    }
    /**
     * get value of field for check.
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     * @return the field value.
     */
    public static Object getField(Object obj, String field) {
        Object value = null;
        try {
            Field declaredField = obj.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);

            value = declaredField.get(obj);
            declaredField.setAccessible(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * The LateDeliverableType set to be used for retrieving.
     * @return the types set
     * @since 1.3
     */
    static Set<LateDeliverableType> getTypes() {
    	Set<LateDeliverableType> types = new HashSet<LateDeliverableType>();
    	types.add(LateDeliverableType.MISSED_DEADLINE);
    	types.add(LateDeliverableType.REJECTED_FINAL_FIX);
    	return types;
	}
}