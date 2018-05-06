/*
 * Copyright (C) 2006, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.sql.databaseabstraction;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.text.SimpleDateFormat;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * Helper class for unit test. This class performs some common methods.
 *
 * @author justforplay, suhugo
 * @version 2.0
 * @since 1.1
 */
public class UnitTestHelper {

    /**
     * <p>
     * File Directory where test files locate.
     * </p>
     */
    public static final String INPUT_FILE_DIR = "test_files" + File.separator;

    /**
     * Instance of CustomResultSetMetaData.
     */
    private static CustomResultSetMetaData crsmd = null;

    /**
     * Instance of ResultSetMetaData.
     */
    private static ResultSetMetaData rsmd = null;

    /**
     * <p>
     * This private constructor prevents to create a new instance.
     * </p>
     */
    private UnitTestHelper() {
    }

    /**
     * Return Instance of CustomResultMetaData of Access database present in
     * version 1.0.This is mainly used by On-Demand Converters test cases as
     * parameter. If it fail to get CustomResultSetMetaData, null value is
     * returned.But it will warn the user what is the exception thrown by
     * printing out the error messages, which is mostly caused by the incorrect
     * configuration of client environment.
     *
     * @return Instance of CustomResultMetaData of Access database present in
     *         version 1.0.
     */
    public static CustomResultSetMetaData getCustomRMD() {
        if (crsmd == null) {
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Connection connection = DriverManager.getConnection("jdbc:odbc:Tester");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from tests");
                rsmd = rs.getMetaData();
                crsmd = new CustomResultSetMetaData(getResultMD());
                rs.close();
                statement.close();
                connection.close();

            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("NOTE:Environment is not Pre-config, Unable to get database connection.");
            }

        }
        return crsmd;
    }

    /**
     * Return Instance of ResultMetaData of Access database present in version
     * 1.0.This is mainly used by On-Demand Converters test cases as parameter.
     * If it fail to get ResultMetaData, null value is returned.But it will warn
     * the user what is the exception thrown by printing out the error messages,
     * which is mostly caused by the incorrect configuration of client
     * environment.
     *
     * @return Instance of ResultMetaData of Access database present in version
     *         1.0.
     */
    public static ResultSetMetaData getResultMD() {
        if (rsmd == null) {
            try {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                Connection connection = DriverManager.getConnection("jdbc:odbc:Tester");
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery("select * from tests");
                rsmd = rs.getMetaData();
                rs.close();
                statement.close();
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("NOTE:Environment is not Pre-config, Unable to get database connection.");
            }

        }
        return rsmd;
    }

    /**
     * Close the specified database connection. If it fail to close connection,
     * null value is returned.But it will warn the user what is the exception
     * thrown by printing out the error messages, which is mostly caused by the
     * incorrect configuration of client environment.
     *
     * @param conn
     *            Database connection.
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
            ConfigManager cm = ConfigManager.getInstance();
            if (cm.existsNamespace("testConnection")) {
                cm.removeNamespace("testConnection");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NOTE:Environment is not Pre-config, Unable to create table or release namespace.");
        }

    }

    /**
     * Get database connection configured in test_files/dbfactory.xml, which is
     * configured to Oracle database. If it fail to get connection, null value
     * is returned.But it will warn the user what is the exception thrown by
     * printing out the error messages, which is mostly caused by the incorrect
     * configuration of client environment.
     *
     * @return database connection.
     */
    public static Connection getDatabaseConnection() {
        try {
            ConfigManager cm = ConfigManager.getInstance();
            if (cm.existsNamespace("testConnection")) {
                cm.removeNamespace("testConnection");
            }
            cm.add("dbfactory.xml");
            DBConnectionFactory connFactory = new DBConnectionFactoryImpl("testConnection");
            Connection conn = connFactory.createConnection();
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(
                    "NOTE:Environment is not Pre-config, Unable to get connection or load file dbfactory.xml.");
            return null;
        }
    }

    /**
     * Get Blob object which is used by BlobConverter. If it fail to get object,
     * null value is returned.But it will warn the user what is the exception
     * thrown by printing out the error messages, which is mostly caused by the
     * incorrect configuration of client environment.<br>
     * Note: If no Blob exists in database, it will create 2 values into
     * database. Please refer to insertData method.
     *
     * @param conn
     *            Database connection.
     * @return instance of Blob.
     */
    public static Blob getBlobObj(Connection conn) {
        try {

            Statement stmt = conn.createStatement();
            String sql = "SELECT BLOB_T FROM ABSTRACTION_TABLE WHERE ID=100";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getBlob("BLOB_T");
            } else {
                insertData(conn);
                return getBlobObj(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NOTE:Environment is not Pre-config, Unable to select value.");
            return null;
        }

    }

    /**
     * Get Clob object which is used by ClobConverter. If it fail to get object,
     * null value is returned.But it will warn the user what is the exception
     * thrown by printing out the error messages, which is mostly caused by the
     * incorrect configuration of client environment.<br>
     * Note: If no Blob exists in database, it will create 2 values into
     * database. Please refer to insertData method.
     *
     * @param conn
     *            Database connection.
     * @return instance of Clob.
     */
    public static Clob getClobObj(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT CLOB_T FROM ABSTRACTION_TABLE WHERE ID=100";
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getClob("CLOB_T");
            } else {
                insertData(conn);
                return getClobObj(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NOTE:Environment is not Pre-config, Unable to select value.");
            return null;
        }
    }

    /**
     * Get ResultSet instance from Oracle Database. If it fail to get ResultSet,
     * null value is returned.But it will warn the user what is the exception
     * thrown by printing out the error messages, which is mostly caused by the
     * incorrect configuration of client environment.<br>
     * Note: If no Blob exists in database, it will create 3 values into
     * database. Please refer to insertData method.
     *
     * @param conn
     *            Database connection.
     * @return instance of Blob.
     */
    public static ResultSet getResultSet(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            String sql = "SELECT count(*) FROM ABSTRACTION_TABLE WHERE ID=100 or ID=50 or ID=150";
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            System.out.println(rs.getInt(1));
            if (rs.getInt(1) < 3) {
                insertData(conn);
            }
            sql = "SELECT * FROM ABSTRACTION_TABLE WHERE ID=100 or ID=50 or ID=150";
            rs = stmt.executeQuery(sql);
            return rs;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NOTE:Environment is not Pre-config, Unable to select value.");
            return null;
        }
    }

    /**
     * This function is used to initialize user database when no records is
     * found in the database. The database is assumed to be empty in order to
     * run the test case. After retrieve Blob object, Clob object or ResultSet
     * object, It will invoke this method to insert 2 records into the database.
     *
     * @param conn
     *            database connection.
     */
    private static void insertData(Connection conn) {
        // Modified by WishingBone - use generic SQL so that it supports more
        // DB's
        try {
            String sql = "INSERT INTO ABSTRACTION_TABLE (ID, NAME, AGE, BLOB_T, CLOB_T, DATE_T, URL)"
                    + " values(?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, 50);
            pstmt.setString(2, "TOPCODER");
            pstmt.setLong(3, 10);
            pstmt.setBytes(4, new byte[] { 1 });
            pstmt.setBytes(5, new byte[] { 1 });
            pstmt.setDate(6, new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2006-06-18").getTime()));
            pstmt.setString(7, "http://www.topcoder.com");
            pstmt.executeUpdate();

            File file = new File(INPUT_FILE_DIR + "tempFile.txt");
            FileInputStream fin = new FileInputStream(file);
            byte[] data = new byte[1024];
            int count = fin.read(data);
            fin.close();
            byte[] tmp = new byte[count];
            System.arraycopy(data, 0, tmp, 0, count);

            pstmt.setLong(1, 100);
            pstmt.setString(2, "TEST");
            pstmt.setLong(3, 20);
            pstmt.setBytes(4, tmp);
            pstmt.setBytes(5, tmp);
            pstmt.setDate(6, new Date(new SimpleDateFormat("yyyy-MM-dd").parse("2006-06-19").getTime()));
            pstmt.setString(7, "http://test.com");
            pstmt.executeUpdate();
            pstmt.close();

            // for the NullColumnValueException test
            sql = "INSERT INTO ABSTRACTION_TABLE (ID) values(?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, 150);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("NOTE:Environment is not Pre-config, Unable to insert value.");
        }
    }
}
