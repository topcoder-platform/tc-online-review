/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Defines helper methods used in tests.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public final class UnitTestHelper {

    /**
     * <p>
     * Creates a new instance of <code>UnitTestHelper</code> class. The private constructor prevents the creation of a
     * new instance.
     * </p>
     */
    private UnitTestHelper() {
    }

    /**
     * <p>
     * Clears the config.
     * </p>
     *
     * @throws Exception
     *             unexpected exception.
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator iter = cm.getAllNamespaces(); iter.hasNext();) {
            cm.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Adds the config of given config file.
     * </p>
     *
     * @param configFile
     *            the given config file.
     * @throws Exception
     *             unexpected exception.
     */
    public static void addConfig(String configFile) throws Exception {
        clearConfig();

        ConfigManager configManager = ConfigManager.getInstance();
        configManager.add(configFile);
    }

    /**
     * <p>
     * Gets the value of a private field in the given class. The field has the given name. The value is retrieved from
     * the given instance.
     * </p>
     *
     * @param type
     *            the class which the private field belongs to.
     * @param instance
     *            the instance which the private field belongs to.
     * @param name
     *            the name of the private field to be retrieved.
     * @return the value of the private field or <code>null</code> if any error occurs.
     */
    public static Object getPrivateField(Class type, Object instance, String name) {

        Field field = null;
        Object obj = null;

        try {
            // Get the reflection of the field and get the value
            field = type.getDeclaredField(name);
            field.setAccessible(true);
            obj = field.get(instance);
        } catch (NoSuchFieldException e) {
            // ignore
        } catch (IllegalAccessException e) {
            // ignore
        } finally {
            if (field != null) {
                // Reset the accessibility
                field.setAccessible(false);
            }
        }

        return obj;
    }

    /**
     * <p>
     * Inserts into the comp_versions table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoComponentVersions(Connection conn) throws SQLException {

        cleanupTable(conn, "comp_versions");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn
                .prepareStatement("insert into comp_versions values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Sets and inserts the first record.
        psInsert.setLong(1, 1); // Sets comp_vers_id.
        psInsert.setLong(2, 1001); // Sets component_id.
        psInsert.setLong(3, 1); // Sets version.
        psInsert.setString(4, "Version 1"); // Sets version_text.
        psInsert.setDate(5, currentDate); // Sets create_time.
        psInsert.setLong(6, 113); // Sets phase_id.
        psInsert.setDate(7, currentDate); // Sets phase_time.
        psInsert.setDouble(8, 100.58); // Sets price.
        psInsert.setString(9, "Good"); // Sets comments.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the second record.
        psInsert.setLong(1, 2); // Sets comp_vers_id.
        psInsert.setLong(2, 1002); // Sets component_id.
        psInsert.setLong(3, 2); // Sets version.
        psInsert.setString(4, "Version 2"); // Sets version_text.
        psInsert.setDate(5, currentDate); // Sets create_time.
        psInsert.setLong(6, 112); // Sets phase_id.
        psInsert.setDate(7, currentDate); // Sets phase_time.
        psInsert.setDouble(8, 50.6); // Sets price.
        psInsert.setString(9, "Average"); // Sets comments.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the third record.
        psInsert.setLong(1, 3); // Sets comp_vers_id.
        psInsert.setLong(2, 1003); // Sets component_id.
        psInsert.setLong(3, 1); // Sets version.
        psInsert.setString(4, "Version 1"); // Sets version_text.
        psInsert.setDate(5, currentDate); // Sets create_time.
        psInsert.setLong(6, 113); // Sets phase_id.
        psInsert.setDate(7, currentDate); // Sets phase_time.
        psInsert.setDouble(8, 500.2); // Sets price.
        psInsert.setString(9, "Wonderful"); // Sets comments.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the fourth record.
        psInsert.setLong(1, 4); // Sets comp_vers_id.
        psInsert.setLong(2, 1004); // Sets component_id.
        psInsert.setLong(3, 3); // Sets version.
        psInsert.setString(4, "Version 3"); // Sets version_text.
        psInsert.setDate(5, currentDate); // Sets create_time.
        psInsert.setLong(6, 114); // Sets phase_id.
        psInsert.setDate(7, currentDate); // Sets phase_time.
        psInsert.setDouble(8, 0.9); // Sets price.
        psInsert.setString(9, "Bad"); // Sets comments.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Associates technologies with component by comp_technology table.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void associateComponentTechnologies(Connection conn) throws SQLException {
        cleanupTable(conn, "comp_technology");

        // Inserts rows into the table.
        PreparedStatement psInsert = conn.prepareStatement("insert into comp_technology(comp_tech_id,"
                + " comp_vers_id, technology_type_id) values(?, ?, ?)");

        // Sets and inserts the first record.
        psInsert.setLong(1, 1);
        psInsert.setLong(2, 1);
        psInsert.setLong(3, 1);
        psInsert.execute();

        // Sets and inserts the second record.
        psInsert.setLong(1, 2);
        psInsert.setLong(2, 1);
        psInsert.setLong(3, 2);
        psInsert.execute();

        // Sets and inserts the third record.
        psInsert.setLong(1, 3);
        psInsert.setLong(2, 1);
        psInsert.setLong(3, 3);
        psInsert.execute();

        // Sets and inserts the fourth record.
        psInsert.setLong(1, 4);
        psInsert.setLong(2, 1);
        psInsert.setLong(3, 4);
        psInsert.execute();

        // Sets and inserts the fifth record.
        psInsert.setLong(1, 5);
        psInsert.setLong(2, 2);
        psInsert.setLong(3, 1);
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the technology_types table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoTechnologyTypes(Connection conn) throws SQLException {

        cleanupTable(conn, "technology_types");

        // Inserts rows into the table.
        PreparedStatement psInsert = conn.prepareStatement("insert into technology_types values(?, ?, ?, ?)");

        // Sets and inserts the first record.
        psInsert.setLong(1, 1); // Sets technology_type_id.
        psInsert.setString(2, "Java"); // Sets technology_name.
        psInsert.setString(3, "Java Programming Language"); // Sets description.
        psInsert.setLong(4, 8); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the second record.
        psInsert.setLong(1, 2); // Sets technology_type_id.
        psInsert.setString(2, "J2EE"); // Sets technology_name.
        psInsert.setString(3, "Java 2 Enterprise Edition"); // Sets description.
        psInsert.setLong(4, 8); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the third record.
        psInsert.setLong(1, 3); // Sets technology_type_id.
        psInsert.setString(2, "JavaBean"); // Sets technology_name.
        psInsert.setString(3, "JavaBean"); // Sets description.
        psInsert.setLong(4, 8); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the fourth record.
        psInsert.setLong(1, 4); // Sets technology_type_id.
        psInsert.setString(2, "HTML"); // Sets technology_name.
        psInsert.setString(3, "Hyper-Text Markup Language"); // Sets description.
        psInsert.setLong(4, 8); // Sets status_id.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the comp_forum_xref table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoCompForumXref(Connection conn) throws SQLException {

        cleanupTable(conn, "comp_forum_xref");

        // Inserts rows into the table.
        PreparedStatement psInsert = conn.prepareStatement("insert into comp_forum_xref values(?, ?, ?, ?)");

        // Sets and inserts the first record.
        psInsert.setLong(1, 1000001); // Sets comp_forum_id.
        psInsert.setLong(2, 1); // Sets comp_vers_id.
        psInsert.setLong(3, 100001); // Sets forum_id.
        psInsert.setInt(4, 5); // Sets forum_type.
        psInsert.execute();

        // Sets and inserts the second record.
        psInsert.setLong(1, 1000002); // Sets comp_forum_id.
        psInsert.setLong(2, 2); // Sets comp_vers_id.
        psInsert.setLong(3, 100002); // Sets forum_id.
        psInsert.setInt(4, 5); // Sets forum_type.
        psInsert.execute();

        // Sets and inserts the third record.
        psInsert.setLong(1, 1000003); // Sets comp_forum_id.
        psInsert.setLong(2, 3); // Sets comp_vers_id.
        psInsert.setLong(3, 100003); // Sets forum_id.
        psInsert.setInt(4, 5); // Sets forum_type.
        psInsert.execute();

        // Sets and inserts the fourth record.
        psInsert.setLong(1, 1000004); // Sets comp_forum_id.
        psInsert.setLong(2, 4); // Sets comp_vers_id.
        psInsert.setLong(3, 100004); // Sets forum_id.
        psInsert.setInt(4, 1); // Sets forum_type.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the comp_catalog table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoComponentCataLog(Connection conn) throws SQLException {

        cleanupTable(conn, "comp_catalog");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn
                .prepareStatement("insert into comp_catalog values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Sets and inserts the first record.
        psInsert.setLong(1, 1001); // Sets component_id.
        psInsert.setLong(2, 101); // Sets current_version.
        psInsert.setString(3, "First Project"); // Sets short_desc.
        psInsert.setString(4, "Project A"); // Sets component_name.
        psInsert.setString(5, "First Project A"); // Sets description.
        psInsert.setString(6, "Eating"); // Sets function_desc.
        psInsert.setDate(7, currentDate); // Sets create_time.
        psInsert.setLong(8, 121); // Sets status_id.
        psInsert.setLong(9, 1); // Sets root_category_id.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the second record.
        psInsert.setLong(1, 1002); // Sets component_id.
        psInsert.setLong(2, 102); // Sets current_version.
        psInsert.setNull(3, Types.VARCHAR); // Sets short_desc.
        psInsert.setString(4, "Project B"); // Sets component_name.
        psInsert.setString(5, "Second Project B"); // Sets description.
        psInsert.setNull(6, Types.VARCHAR); // Sets function_desc.
        psInsert.setDate(7, currentDate); // Sets create_time.
        psInsert.setLong(8, 122); // Sets status_id.
        psInsert.setLong(9, 2); // Sets root_category_id.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the third record.
        psInsert.setLong(1, 1003); // Sets component_id.
        psInsert.setLong(2, 103); // Sets current_version.
        psInsert.setString(3, "Third Project"); // Sets short_desc.
        psInsert.setString(4, "Project C"); // Sets component_name.
        psInsert.setString(5, "Third Project C"); // Sets description.
        psInsert.setString(6, "Thinking"); // Sets function_desc.
        psInsert.setDate(7, currentDate); // Sets create_time.
        psInsert.setLong(8, 123); // Sets status_id.
        psInsert.setLong(9, 3); // Sets root_category_id.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        // Sets and inserts the fourth record.
        psInsert.setLong(1, 1004); // Sets component_id.
        psInsert.setLong(2, 104); // Sets current_version.
        psInsert.setString(3, "Fourth Project"); // Sets short_desc.
        psInsert.setString(4, "Project D"); // Sets component_name.
        psInsert.setString(5, "Fourth Project D"); // Sets description.
        psInsert.setString(6, "Working"); // Sets function_desc.
        psInsert.setDate(7, currentDate); // Sets create_time.
        psInsert.setLong(8, 124); // Sets status_id.
        psInsert.setLong(9, 4); // Sets root_category_id.
        psInsert.setDate(10, currentDate); // Sets modify_date.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the email table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoEmail(Connection conn) throws SQLException {

        cleanupTable(conn, "email");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn.prepareStatement("insert into email values(?, ?, ?, ?, ?, ?, ?, ?)");

        // Sets and inserts the 1st email information of the 1st user.
        psInsert.setLong(1, 1001); // Sets user_id.
        psInsert.setLong(2, 5000); // Sets email_id.
        psInsert.setInt(3, 6); // Sets email_type_id.
        psInsert.setString(4, "User1@gmail.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 1); // Sets primary_ind.
        psInsert.setInt(8, 8); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the 2nd email information of the 1st user.
        psInsert.setLong(1, 1001); // Sets user_id.
        psInsert.setLong(2, 5001); // Sets email_id.
        psInsert.setInt(3, 7); // Sets email_type_id.
        psInsert.setString(4, "User1@163.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 0); // Sets primary_ind.
        psInsert.setInt(8, 4); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the 1st email information of the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setLong(2, 2000); // Sets email_id.
        psInsert.setInt(3, 6); // Sets email_type_id.
        psInsert.setString(4, "User2@gmail.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 1); // Sets primary_ind.
        psInsert.setInt(8, 8); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the 2nd email information of the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setLong(2, 2001); // Sets email_id.
        psInsert.setInt(3, 3); // Sets email_type_id.
        psInsert.setString(4, "User2@hotmail.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 0); // Sets primary_ind.
        psInsert.setInt(8, 3); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the 3rd email information of the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setLong(2, 2002); // Sets email_id.
        psInsert.setInt(3, 1); // Sets email_type_id.
        psInsert.setString(4, "User2@yahoo.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 0); // Sets primary_ind.
        psInsert.setInt(8, 5); // Sets status_id.
        psInsert.execute();

        // Sets and inserts the 1st email information of the 3rd user.
        psInsert.setLong(1, 1003); // Sets user_id.
        psInsert.setLong(2, 8000); // Sets email_id.
        psInsert.setInt(3, 6); // Sets email_type_id.
        psInsert.setString(4, "User3@gmail.com"); // Sets address.
        psInsert.setDate(5, currentDate); // Sets create_date.
        psInsert.setDate(6, currentDate); // Sets modify_date.
        psInsert.setInt(7, 1); // Sets primary_ind.
        psInsert.setInt(8, 8); // Sets status_id.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the user table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoUser(Connection conn) throws SQLException {

        cleanupTable(conn, "user");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn
                .prepareStatement("insert into user values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Sets and inserts the 1st user.
        psInsert.setLong(1, 1001); // Sets user_id.
        psInsert.setString(2, "First A"); // Sets first_name.
        psInsert.setString(3, "Last A"); // Sets last_name.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setDate(5, currentDate); // Sets modify_date.
        psInsert.setString(6, "Handle A"); // Sets handle.
        psInsert.setDate(7, currentDate); // Sets last_login.
        psInsert.setString(8, "Active"); // Sets status.
        psInsert.setString(9, "Psw A"); // Sets password.
        psInsert.setString(10, "Activation Code A"); // Sets activation_code.
        psInsert.setString(11, "Middle A"); // Sets middle_name.
        psInsert.setString(12, "handle a"); // Sets handle_lower.
        psInsert.setInt(13, 1); // Sets timezone_id.
        psInsert.execute();

        // Sets and inserts the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setString(2, "First B"); // Sets first_name.
        psInsert.setString(3, "Last B"); // Sets last_name.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setDate(5, currentDate); // Sets modify_date.
        psInsert.setString(6, "Handle B"); // Sets handle.
        psInsert.setDate(7, currentDate); // Sets last_login.
        psInsert.setString(8, "Unactive"); // Sets status.
        psInsert.setString(9, "Psw B"); // Sets password.
        psInsert.setString(10, "Activation Code B"); // Sets activation_code.
        psInsert.setString(11, "Middle B"); // Sets middle_name.
        psInsert.setString(12, "handle b"); // Sets handle_lower.
        psInsert.setInt(13, 2); // Sets timezone_id.
        psInsert.execute();

        // Sets and inserts the 3rd user.
        psInsert.setLong(1, 1003); // Sets user_id.
        psInsert.setString(2, "First C"); // Sets first_name.
        psInsert.setString(3, "Last C"); // Sets last_name.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setDate(5, currentDate); // Sets modify_date.
        psInsert.setString(6, "Handle C"); // Sets handle.
        psInsert.setDate(7, currentDate); // Sets last_login.
        psInsert.setString(8, "Deleted"); // Sets status.
        psInsert.setString(9, "Psw C"); // Sets password.
        psInsert.setString(10, "Activation Code C"); // Sets activation_code.
        psInsert.setString(11, "Middle C"); // Sets middle_name.
        psInsert.setString(12, "handle c"); // Sets handle_lower.
        psInsert.setInt(13, 3); // Sets timezone_id.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the user_rating table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoUserRating(Connection conn) throws SQLException {

        cleanupTable(conn, "user_rating");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn.prepareStatement("insert into user_rating values(?, ?, ?, ?, ?, ?, ?, ?, ?)");

        // Sets and inserts the rating info of the 1st user.
        psInsert.setLong(1, 1001); // Sets user_id.
        psInsert.setLong(2, 1563); // Sets rating.
        psInsert.setInt(3, 112); // Sets phase_id.
        psInsert.setLong(4, 431); // Sets vol.
        psInsert.setLong(5, 1500); // Sets rating_no_vol.
        psInsert.setInt(6, 10); // Sets num_ratings.
        psInsert.setDate(7, currentDate); // Sets mod_date_time.
        psInsert.setDate(8, currentDate); // Sets create_date_time.
        psInsert.setLong(9, 252271); // Sets last_rated_project_id.
        psInsert.execute();

        // Sets and inserts the rating info of the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setLong(2, 2108); // Sets rating.
        psInsert.setInt(3, 113); // Sets phase_id.
        psInsert.setLong(4, 131); // Sets vol.
        psInsert.setLong(5, 2000); // Sets rating_no_vol.
        psInsert.setInt(6, 3); // Sets num_ratings.
        psInsert.setDate(7, currentDate); // Sets mod_date_time.
        psInsert.setDate(8, currentDate); // Sets create_date_time.
        psInsert.setLong(9, 252272); // Sets last_rated_project_id.
        psInsert.execute();

        // Sets and inserts the rating info of the 3rd user.
        psInsert.setLong(1, 1003); // Sets user_id.
        psInsert.setLong(2, 500); // Sets rating.
        psInsert.setInt(3, 113); // Sets phase_id.
        psInsert.setLong(4, 531); // Sets vol.
        psInsert.setLong(5, 300); // Sets rating_no_vol.
        psInsert.setInt(6, 5); // Sets num_ratings.
        psInsert.setDate(7, currentDate); // Sets mod_date_time.
        psInsert.setDate(8, currentDate); // Sets create_date_time.
        psInsert.setLong(9, 252273); // Sets last_rated_project_id.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Inserts into the user_reliability table for testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void insertIntoUserReliability(Connection conn) throws SQLException {

        cleanupTable(conn, "user_reliability");

        // Inserts rows into the table.
        Date currentDate = new Date(System.currentTimeMillis());
        PreparedStatement psInsert = conn.prepareStatement("insert into user_reliability values(?, ?, ?, ?, ?)");

        // Sets and inserts the reliability info of the 1st user.
        psInsert.setLong(1, 1001); // Sets user_id.
        psInsert.setDouble(2, 0.01); // Sets rating.
        psInsert.setDate(3, currentDate); // Sets modify_date.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setLong(5, 112); // Sets phase_id.
        psInsert.execute();

        // Sets and inserts the reliability info of the 2nd user.
        psInsert.setLong(1, 1002); // Sets user_id.
        psInsert.setDouble(2, 0.76543); // Sets rating.
        psInsert.setDate(3, currentDate); // Sets modify_date.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setLong(5, 113); // Sets phase_id.
        psInsert.execute();

        // Sets and inserts the reliability info of the 3rd user.
        psInsert.setLong(1, 1003); // Sets user_id.
        psInsert.setDouble(2, 0.9801); // Sets rating.
        psInsert.setDate(3, currentDate); // Sets modify_date.
        psInsert.setDate(4, currentDate); // Sets create_date.
        psInsert.setLong(5, 113); // Sets phase_id.
        psInsert.execute();

        psInsert.close();
    }

    /**
     * <p>
     * Deletes from the comp_catalog, comp_versions and comp_forum_xref table after testing.
     * </p>
     *
     * @param conn
     *            the connection used.
     * @param tableName
     *            the name of the table which is needed to be cleaned up.
     * @throws SQLException
     *             if any db operation failed.
     */
    public static void cleanupTable(Connection conn, String tableName) throws SQLException {

        // Deletes all the rows in table.
        PreparedStatement psDel = conn.prepareStatement("delete from " + tableName);
        psDel.execute();
        psDel.close();
    }
}