/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.stresstests;
import java.io.File;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Random;

import com.topcoder.util.config.ConfigManager;
/**
 * <p>
 * Helper class for accuracy test.
 * </p>
 *
 * @author fairytale, victorsam
 * @version 1.1
 */
final class Helper {
    /** The first component_id. */
    public static final long COMPONENT_ID_START = 0;
    /** The first comp_vers_id. */
    public static final long COMP_VERS_ID_START = 1000;
    /** The first user_id. */
    public static final long USER_ID_START = 1000000;
    /** A bundle of components. */
    public static final String[] TEST_COMPONENTS = {
        "Configuration Manager", "Helper Table Manager", "Data Mart", "Auto Pilot",
        "Phase Management Persistence"
    };
    /** A bundle of handles. */
    public static final String[] TEST_HANDLES = {
        "mgmg", "haozangr", "waits", "telly", "ordinary", "PE", "faIryTale"

    };
    /** All config file path. */
    public static final String[] CONFIG_FILE = new String[]{
        "db_connection_factory.xml"
    };

    /**
     * Private constructor.
     */
    private Helper() {
    }

    /**
     * Load all configurations from files.
     *
     * @throws Exception if any errors happened
     */
    public static void loadConfig() throws Exception {
        unloadConfig();
        ConfigManager cm = ConfigManager.getInstance();
        for (int i = 0; i < CONFIG_FILE.length; i++) {
            cm.add(new File("test_files/stresstest/" + CONFIG_FILE[i]).getAbsolutePath());
        }
    }

    /**
     * unload all configurations from files.
     *
     * @throws Exception if any errors happened
     */
    public static void unloadConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator it = cm.getAllNamespaces(); it.hasNext();) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Returns the value of the given field in the given Object using Reflection.
     * </p>
     *
     * @param obj the given Object instance to get the field value.
     * @param fieldName the name of the filed to get value from the obj.
     * @return the field value in the obj.
     */
    public static Object getObjectFieldValue(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            return field.get(obj);
        } catch (SecurityException e) {
            //ignore the exception and return null.
        } catch (NoSuchFieldException e) {
            //ignore the exception and return null.
        } catch (IllegalArgumentException e) {
            //ignore the exception and return null.
        } catch (IllegalAccessException e) {
            //ignore the exception and return null.
        }

        return null;
    }

    /**
     * <p> clear all the data in specified table. </p>
     * @param conn the connection to db.
     * @param tableName the name of the table which is needed to be cleaned up.
     * @throws Exception if any exception occurs.
     */
    public static void clearTable(Connection conn, String tableName) throws Exception {
        Statement stat = conn.createStatement();
        stat.execute("delete from " + tableName);
        stat.close();
    }

    /**
     * <p>Inserts into the user table for testing.</p>
     *
     * @param conn the connection used.
     * @param count the number of data items inserted.
     * @throws Exception if any exception occurs.
     */
    public static void insertUsers(Connection conn, int count) throws Exception {
        clearTable(conn, "user");
        PreparedStatement ps = conn.prepareStatement(
            "insert into user values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int i = 0;
        Random rand = new Random();
        for (; i < count - 1; i++) {

            Date currentDate = new Date(System.currentTimeMillis());

            String handle = Helper.getRandomHandle(rand);
            ps.setLong(1, USER_ID_START + i);
            ps.setString(2, "first_name");
            ps.setString(3, "last_name");
            ps.setDate(4, currentDate);
            ps.setDate(5, currentDate);
            ps.setString(6, handle);
            ps.setDate(7, currentDate);
            ps.setString(8, "A");
            ps.setString(9, "password");
            ps.setString(10, "B");
            ps.setString(11, "middle");
            ps.setString(12, handle.toLowerCase());
            ps.setInt(13, 1);
            ps.execute();
        }
        Date currentDate = new Date(System.currentTimeMillis());

        ps.setLong(1, USER_ID_START + i);
        ps.setString(2, "Hong");
        ps.setString(3, "jinhu");
        ps.setDate(4, currentDate);
        ps.setDate(5, currentDate);
        ps.setString(6, "faIryTale");
        ps.setDate(7, currentDate);
        ps.setString(8, "A");
        ps.setString(9, "password");
        ps.setString(10, "B");
        ps.setString(11, "and");
        ps.setString(12, "fairytale");
        ps.setInt(13, 1);
        ps.execute();
    }

    /**
     * <p>Inserts into the email table for testing.</p>
     *
     * @param conn the connection used.
     * @param count the number of data items inserted.
     * @throws Exception if any exception occurs.
     */
    public static void insertEmail(Connection conn, int count) throws Exception {
        clearTable(conn, "email");

        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement ps = conn.prepareStatement(
                "insert into email values(?, ?, ?, ?, ?, ?, ?, ?)");
        for (int i = 0; i < count; i++) {

            ps.setLong(1, USER_ID_START + i);
            ps.setLong(2, i);
            ps.setInt(3, 6);
            ps.setString(4, "primaryEmail@hotmail.com");
            ps.setDate(5, currentDate);
            ps.setDate(6, currentDate);
            ps.setInt(7, 1);
            ps.setInt(8, 8);
            ps.execute();


            ps.setLong(1, USER_ID_START + i);
            ps.setLong(2, USER_ID_START + i);
            ps.setInt(3, 7);
            ps.setString(4, "aother@sohu.com");
            ps.setDate(5, currentDate);
            ps.setDate(6, currentDate);
            ps.setInt(7, 0);
            ps.setInt(8, 4);
            ps.execute();
        }
    }

    /**
     * <p>Inserts into the comp_catalog table for testing.</p>
     *
     * @param conn the connection used.
     * @param count the number of data items inserted.
     * @throws Exception if any exception occurs.
     */
    public static void intsertComponentCataLog(Connection conn, int count) throws Exception {
        clearTable(conn, "comp_catalog");
        PreparedStatement ps = conn.prepareStatement(
            "insert into comp_catalog values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        int i = 0;
        Random rand = new Random();
        for (; i < count - 1; i++) {
            Date currentDate = new Date(System.currentTimeMillis());
            ps.setLong(1, COMPONENT_ID_START + i);
            ps.setLong(2, i);
            ps.setString(3, null);
            ps.setString(4, getRandomComponentName(rand));
            ps.setString(5, "description.");
            ps.setString(6,  "function_desc.");
            ps.setDate(7, currentDate);
            ps.setLong(8, i);
            ps.setLong(9, i);
            ps.setDate(10, currentDate);
            ps.execute();
        }
        Date currentDate = new Date(System.currentTimeMillis());
        ps.setLong(1, COMPONENT_ID_START + i);
        ps.setLong(2, i);
        ps.setString(3, null);
        ps.setString(4, TEST_COMPONENTS[0]);
        ps.setString(5, "description.");
        ps.setString(6,  "function_desc.");
        ps.setDate(7, currentDate);
        ps.setLong(8, i);
        ps.setLong(9, i);
        ps.setDate(10, currentDate);
        ps.execute();
        ps.close();
    }
    /**
     * <p>Inserts into the comp_forum_xref table for testing.</p>
     *
     * @param conn the connection used.
     * @param count the number of data items inserted.
     * @throws Exception if any exception occurs.
     */
    public static void insertCompForumXref(Connection conn, int count)
        throws Exception {

        clearTable(conn, "comp_forum_xref");

        PreparedStatement ps = conn.prepareStatement(
                "insert into comp_forum_xref values(?, ?, ?, ?)");

        for (int i = 0; i < count; i++) {

            ps.setLong(1, 22462008 + i);
            ps.setLong(2, COMP_VERS_ID_START + i);
            ps.setLong(3, 100);
            ps.setInt(4, 2);
            ps.execute();
        }

        ps.close();
    }

    /**
     * <p>Inserts into the comp_versions table for testing.</p>
     *
     * @param conn the connection used.
     * @param count the number of data items inserted.
     * @throws Exception if any exception occurs.
     */
    public static void insertComponentVersions(Connection conn, int count)
        throws Exception {

        clearTable(conn, "comp_versions");
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement ps = conn.prepareStatement(
                "insert into comp_versions values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        for (int i = 0; i < count; i++) {

            ps.setLong(1, COMP_VERS_ID_START + i);
            ps.setLong(2, COMPONENT_ID_START + i);
            ps.setLong(3, i);
            ps.setString(4, "1.0");
            ps.setDate(5, currentDate);
            ps.setLong(6, 113);
            ps.setDate(7, currentDate);
            ps.setDouble(8, 500.00 + i);
            ps.setString(9, "Comment at " + COMPONENT_ID_START + i);
            ps.setDate(10, currentDate);
            ps.execute();

        }
        ps.close();
    }

    /**
     * <p>Gets the random handle from TEST_HANDLES.</p>
     * @param rand the rand to random handles.
     * @return the handle
     */
    public static String getRandomHandle(Random rand) {
        return TEST_HANDLES[Math.abs(rand.nextInt()) % TEST_HANDLES.length];
    }
    /**
     * <p>Gets the random component name from TEST_COMPONENTS. </p>
     * @param rand the rand to random component names.
     * @return the component name.
     */
    public static String getRandomComponentName(Random rand) {
        return TEST_COMPONENTS[Math.abs(rand.nextInt()) % TEST_COMPONENTS.length];
    }
}
