/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import java.util.Date;

/**
 * The Helper class for test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class TestHelper {
    /**
     * <p>Represents the test namespace.</p>
     */
    private static final String TEST_NAMESPACE = "com.cronos.onlinereview";
    
    /**
     * <p>Represents browser speed to run tests</p>
     */
    private static final String TEST_BROWSER_SPEED = "browser_speed";

    /**
     * <p>Represents Domain type.</p>
     */
    private static final String DOMAIN = "domain";

    /**
     * <p>Represents Browser type.</p>
     */
    private static final String BROWSER = "browser";

    /**
     * <p>Represents base Index URL.</p>
     */
    private static final String BASE_URL = "base_url";

    /**
     * <p>Represents the project link.</p>
     */
    private static final String PROJECT_URL = "project_link";

    /**
     * <p>Represents the selenium port.</p>
     */
    private static final String PORT = "port";

    /**
     * <p>Represents the timeout.</p>
     */
    private static final String TIMEOUT = "timeout";

    /**
     * <p>Represents the username.</p>
     */
    private static final String USERNAME = "username";

    /**
     * <p>Represents the password.</p>
     */
    private static final String PASSWORD = "password";

    /**
     * <p>Represents the distribution location.</p>
     */
    private static final String DISTRIBUTION = "distribution_location";

    /**
     * <p>Represents text shown on form when validation errors exist</p>
     */
    public static final String VALIDATION_ERROR = "There were validation errors.";
    
    /**
     * <p>Represents text shown on form when errors exist</p>
     */
    public static final String ERROR_TEXT = "An error occurred";
    
    /**
     * <p>
     * Default constructor.
     * </p>
     */
    private TestHelper() {
    }

    /**
     * To get the base index URL.
     *
     * @return base index url
     *
     * @throws Exception if any error occurred
     */
    static String getDomain() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, DOMAIN).toString();
    }

    /**
     * To get selenium port.
     *
     * @return selenium port
     *
     * @throws Exception if any error occurred
     */
    static int getSeleniumPort() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return new Integer(cm.getProperty(TEST_NAMESPACE, PORT).toString());
    }

    /**
     * To get the base index URL.
     *
     * @return base index url
     *
     * @throws Exception if any error occurred
     */
    static String getBrowser() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, BROWSER).toString();
    }

    /**
     * To get the base index URL.
     *
     * @return base index url
     *
     * @throws Exception if any error occurred
     */
    static String getBaseURL() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, BASE_URL).toString();
    }

    /**
     * To get the project URL.
     *
     * @return the project url
     *
     * @throws Exception if any error occurred
     */
    static String getProjectURL() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, PROJECT_URL).toString();
    }

    /**
     * Get the login Page.
     *
     * @return the index page.
     *
     * @throws Exception if any error occurred
     */
    static Selenium getLoginPage() throws Exception {
        Selenium browser = new DefaultSelenium("localhost", getSeleniumPort(), getBrowser(), getDomain());
        browser.start();
        
        browser.open(getBaseURL());
        return browser;
    }

    /**
     * To get the timeout of loading a page.
     *
     * @return timeout
     *
     * @throws Exception if any error occurred
     */
    static String getTimeout() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, TIMEOUT).toString();
    }

    /**
     * To get the username.
     *
     * @return username
     *
     * @throws Exception if any error occurred
     */
    static String getUsername() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, USERNAME).toString();
    }

    /**
     * To get the password.
     *
     * @return password
     *
     * @throws Exception if any error occurred
     */
    static String getPassword() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, PASSWORD).toString();
    }

    /**
     * Create the project with generated id.
     *
     * @param projectId the project id.
     * @param projectId2 the 2nd project id
     * @param registrationClosed indicate if the registration is closed
     * @param allPhase indicates if all phases are included for this project
     *
     * @throws Exception if any error
     */
    static void createProject(long projectId, long projectId2, boolean registrationClosed, boolean allPhase)
        throws Exception {
        String componentName1 = "Integration Test Case 1 (project 1) - " + (new Date());

        DBConnectionFactory factory = new DBConnectionFactoryImpl(
                "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        Connection con = factory.createConnection();
        // Data for project
        insertData(con,
            "INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '1', '1', '132456', CURRENT, '132456', CURRENT);");
        // Data for project_info
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '1', '1003', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '2', '1001', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '3', '1', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '4', '0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '5', '10001776', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '6', '" + componentName1 + "', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '7', '1.0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '9', 'Off', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '10', 'On', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '11', 'On', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '12', 'Yes', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '13', 'Yes', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '14', 'Open', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '16', '0.0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '26', 'On', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '29', 'On', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '41', 'true', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '43', 'true', '132456', CURRENT, '132456', CURRENT)");

        // Data for project_phase
        if (registrationClosed) {
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                getProjectPhaseId() + ", " + projectId +
                ", '1', '3', CURRENT, CURRENT, CURRENT+3 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        } else {
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                getProjectPhaseId() + ", " + projectId +
                ", '1', '1', CURRENT, CURRENT, CURRENT+3 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        }

        if (allPhase) {
            // the specification and approval phase
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                getProjectPhaseId() + ", " + projectId +
                ", '13', '1', CURRENT-2 UNITS day, CURRENT-2 UNITS day, CURRENT-1 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                getProjectPhaseId() + ", " + projectId +
                ", '14', '1', CURRENT-1 UNITS day, CURRENT-1 UNITS day, CURRENT, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                getProjectPhaseId() + ", " + projectId +
                ", '11', '1', CURRENT+23 UNITS day, CURRENT+23 UNITS day, CURRENT+28 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
        }

        long phase1 = getProjectPhaseId();
        long phase2 = getProjectPhaseId();
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase1 + ", " + projectId +
            ", '2', '1', CURRENT, CURRENT, CURRENT+7 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase2 + ", " + projectId +
            ", '3', '1', CURRENT+7 UNITS day, CURRENT+7 UNITS day, CURRENT+8 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '4', '1', CURRENT+8 UNITS day, CURRENT+8 UNITS day, CURRENT+14 UNITS day, NULL, NULL, '345600000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '5', '1', CURRENT+14 UNITS day, CURRENT+14 UNITS day, CURRENT+15 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '6', '1', CURRENT+15 UNITS day, CURRENT+15 UNITS day, CURRENT+16 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '7', '1', CURRENT+16 UNITS day, CURRENT+16 UNITS day, CURRENT+16 UNITS day, NULL, NULL, '43200000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '8', '1', CURRENT+16 UNITS day, CURRENT+16 UNITS day, CURRENT+17 UNITS day, NULL, NULL, '43200000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '9', '1', CURRENT+17 UNITS day, CURRENT+17 UNITS day, CURRENT+22 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            getProjectPhaseId() + ", " + projectId +
            ", '10', '1', CURRENT+22 UNITS day, CURRENT+22 UNITS day, CURRENT+23 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");

        // Data for comp_catalog
        long componentId = getComponentId();
        insertData(con,
            "INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc, create_time, status_id, root_category_id, modify_date, public_ind) VALUES (" +
            componentId + ", '1', '" + componentName1 + "', '" + componentName1 + "', '" + componentName1 +
            "', 'Component', CURRENT, '102', '10001776', CURRENT, '0')");

        // Data for comp_versions
        long versionId = getComponentVersionsId();
        insertData(con,
            "INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time, price, comments, modify_date, suspended_ind, browse, location, issue_tracker_path, revision) VALUES (" +
            versionId + ", " + componentId +
            ", '12', '1.0', '2010-05-21 07:20:14.48', '112', CURRENT, '10000.00', 'Cool', CURRENT, '0', NULL, NULL, NULL, NULL)");
        // Data for comp_categories
        insertData(con,
            "INSERT INTO comp_categories (comp_categories_id, component_id, category_id) VALUES (" +
            getComponentCategoryId() + ", " + componentId + ", '12')");
        // Data for comp_technology
        insertData(con,
            "INSERT INTO comp_technology (comp_tech_id, comp_vers_id, technology_type_id) VALUES (" +
            getComponentTechId() + ", " + versionId + ", '2')");
        // Data for phase_criteria
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase1 + ", '2', '0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase1 + ", '5', 'No', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase2 + ", '3', '0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase2 + ", '5', 'No', '132456', CURRENT, '132456', CURRENT)");
        // Data for phase_dependency
        insertData(con,
            "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
            phase1 + ", " + phase2 + ", '1', '1', '0', '132456', CURRENT, '132456', CURRENT)");

        if (projectId2 != -1) {
            String componentName2 = "Integration Test Case 1 (project 2)- " + (new Date());
            // Data for project
            insertData(con,
                "INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '1', '1', '132456', CURRENT, '132456', CURRENT);");
            // Data for project_info
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '1', '1003', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '2', '1001', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '3', '1', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '4', '0', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '5', '10001776', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '6', '" + componentName2 + "', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '7', '1.0', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '9', 'Off', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '10', 'On', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '11', 'On', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '12', 'Yes', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '13', 'Yes', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '14', 'Open', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '16', '0.0', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '26', 'On', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '29', 'On', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '41', 'true', '132456', CURRENT, '132456', CURRENT)");
            insertData(con,
                "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                projectId2 + ", '43', 'true', '132456', CURRENT, '132456', CURRENT)");
        }
    }

    /**
     * Insert data into the database.
     *
     * @param con the connection instance
     * @param sql the sql to be executed
     *
     * @throws SQLException if any sql error
     */
    private static void insertData(Connection con, String sql)
        throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.executeUpdate();
    }

    /**
     * To get the distribution location.
     *
     * @return the distribution location.
     *
     * @throws Exception if any error occurred
     */
    static String getDistributionLocation() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, DISTRIBUTION).toString();
    }

    /**
     * Login the user.
     *
     * @param browser the selenium browser
     *
     * @throws Exception if any error
     */
    static void loginUser(Selenium browser) throws Exception {
        browser.type("userName", TestHelper.getUsername());
        browser.type("password", TestHelper.getPassword());
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(getTimeout());
    }

    /**
     * Delete the created project.
     *
     * @param browser the browser to mark project deleted
     * @param projectId project id to delete
     *
     * @throws Exception if any error
     */
    static void deleteProject(Selenium browser, long projectId)
        throws Exception {
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + projectId);
        browser.click("//img[@alt='Edit Project']");
        browser.waitForPageToLoad(getTimeout());
        browser.select("//select[@name='status']", "label=Deleted");
        browser.type("explanation", "Removing project used for testing");
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(getTimeout());
    }

    /**
     * Get the project id from the sequence.
     *
     * @return the project id
     *
     * @throws Exception if any error
     */
    static long getProjectId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("project_id_seq");

        return generator.getNextID();
    }

    /**
     * Get the project phase id from the sequence.
     *
     * @return the project phase id
     *
     * @throws Exception if any error
     */
    static long getProjectPhaseId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("project_phase_id_seq");

        return generator.getNextID();
    }

    /**
     * Get the component id from the sequence.
     *
     * @return the component id
     *
     * @throws Exception if any error
     */
    static long getComponentId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("COMPONENT_SEQ");

        return generator.getNextID();
    }

    /**
     * Get the component version id from the sequence.
     *
     * @return the component version id
     *
     * @throws Exception if any error
     */
    static long getComponentVersionsId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("COMPVERSION_SEQ");

        return generator.getNextID();
    }

    /**
     * Get the component category id from the sequence.
     *
     * @return the component category id
     *
     * @throws Exception if any error
     */
    static long getComponentCategoryId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("COMPCATEGORY_SEQ");

        return generator.getNextID();
    }

    /**
     * Get the component technology id from the sequence.
     *
     * @return the component technology id
     *
     * @throws Exception if any error
     */
    static long getComponentTechId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("COMPTECH_SEQ");

        return generator.getNextID();
    }
    
    /**
     * Get the browser speed from configuration
     * 
     * @return the browser speed.
     * 
     * @throws Exception if any error occurred.
     */
    static String getBrowserSpeed() throws Exception {
    	return ConfigManager.getInstance().getProperty( 
    			TEST_NAMESPACE, TEST_BROWSER_SPEED).toString();
    }
 }
