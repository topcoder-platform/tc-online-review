/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.failure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Properties;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;

/**
 * Base class of all failure tests.
 *
 * @author mumujava
 * @version 1.0
 */
public class BaseFailureTest extends TestCase {
	//represents a large text
	public final static String LARGE_TEXT;
	static {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 3000; i++) {
			sb.append("a large text");
		}
		LARGE_TEXT = sb.toString();
	}
	
    /**
     * Clear the db.
     *
     * @throws Exception
     *             to Junit
     */
    public static void clearDB() throws Exception {
        Connection conn = getConnection();
        try {
        	runSql(conn, "test_files/failure/clear.sql");
        } finally {
            conn.close();
        }
    }

    /**
     * Init the db.
     *
     * @throws Exception
     *             to JUnit
     */
    public static void initDB() throws Exception {
        Connection conn = getConnection();
        try {
        	runSql(conn, "test_files/failure/init.sql");
        } finally {
            conn.close();
        }
    }

    /**
     * Executes the sql file.
     *
     * @param connection
     *            the db connection
     * @param file
     *            the sql file
     * @throws Exception
     *             to JUnit
     */
    private static void runSql(Connection connection, String file) throws Exception {
        String sql = getFileContent(file);
        StringTokenizer st = new StringTokenizer(sql, ";");
        PreparedStatement preparedStatement = null;
        try {
            for (int count = 1; st.hasMoreTokens(); count++) {
                String statement = st.nextToken().trim();
                if (statement.length() > 0) {
                    preparedStatement = connection.prepareStatement(statement);
                    preparedStatement.executeUpdate();
                }
            }
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    /**
     * <p>
     * Create the database connection.
     * </p>
     *
     * @return the connection.
     * @throws Exception
     *             to JUnit.
     */
    private static Connection getConnection() throws Exception {
        Properties config = new Properties();
        FileInputStream fio = new FileInputStream("test_files/failure/database.properties");
        try {
            config.load(fio);
        } finally {
            fio.close();
        }
        String url = config.getProperty("url");
        String driver = config.getProperty("driver");
        Class.forName(driver);
        String user = config.getProperty("username");
        String password = config.getProperty("password");

        return DriverManager.getConnection(url, user, password);

    }

    /**
     * <p>
     * Gets the sql file content.
     * </p>
     *
     * @param file
     *            The sql file to get its content.
     * @return The content of sql file.
     * @throws Exception
     *             to JUnit
     */
    private static String getFileContent(String file) throws Exception {
        StringBuilder sql = new StringBuilder();

        BufferedReader in = new BufferedReader(new FileReader(file));

        try {
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                int commentIndex = s.indexOf("--");

                if (commentIndex >= 0) { // Remove any SQL comment
                    s = s.substring(0, commentIndex);
                }

                sql.append(s).append(' '); // The BufferedReader drops newlines so insert whitespace
            }
        } finally {
            in.close();
        }

        return sql.toString();
    }
    

    /**
     * <p>
     * Gets the configuration object.
     * </p>
     *
     * @param configFile
     *            The config file name
     * @param configname
     *            The config name
     * @return The configuration object.
     * @throws Exception
     *             to JUnit
     */
    public ConfigurationObject getConfigurationObject(String configFile,
			String configname) throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();
        // Get configuration
        ConfigurationObject obj = persistence.loadFile(configname, new File(configFile));

        return obj.getChild(configname);
	}

}
