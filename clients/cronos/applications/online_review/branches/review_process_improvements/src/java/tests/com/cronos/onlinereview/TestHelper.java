/*
 * Copyright (C) 2010 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * The Helper class for test.
 *
 * <p>
 * Version 1.1 (Online Review Update Review Management Process assembly 2) Change notes:
 * Add some methods to create resources for the new updated review system.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.1
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
     * <p>Represents user id used when writing data into database.</p>
     */
    public static final String TESTS_USER_ID = "132456";

    /**
     * <p>Represents the application review scorecard id.</p>
     */
    public static final String APPLICATION_SCORECARD_ID = "30000415";

    /**
     * <p>Represents the scorecard question id.</p>
     */
    public static final String SCORECARD_QUESTION_ID = "30001005";

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
     * Create a project with the new updated review system.
     * 
     * @param projectId the project id.
     * @param registrationClosed whether to close registration phase
     * @param allPhase whether to contain all the phases
     * @return the project phase ids
     * @throws Exception if any error occurs
     */
    static Map<String, Long> createProjectWithNewReviewSystem(long projectId, boolean registrationClosed, boolean allPhase) 
        throws Exception {
        String componentName1 = "Integration Test Case 2 (project 2) - " + (new Date());
        
        Map<String, Long> phaseIds = new HashMap<String, Long>();
        
        Connection con = getConnection();
        // Data for project
        insertData(con,
            "INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '1', '14', '132456', CURRENT, '132456', CURRENT);");
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
        phaseIds.put("registration_phase", getProjectPhaseId());
        if (registrationClosed) {
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("registration_phase") + ", " + projectId +
                ", '1', '3', CURRENT, CURRENT, CURRENT+3 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        } else {
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("registration_phase") + ", " + projectId +
                ", '1', '1', CURRENT, CURRENT, CURRENT+3 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        }
        if (allPhase) {
            // the specification and approval phase
            phaseIds.put("specific_submission_phase", getProjectPhaseId());
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("specific_submission_phase") + ", " + projectId +
                ", '13', '1', CURRENT-2 UNITS day, CURRENT-2 UNITS day, CURRENT-1 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
            phaseIds.put("specific_review_phase", getProjectPhaseId());
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("specific_review_phase") + ", " + projectId +
                ", '14', '1', CURRENT-1 UNITS day, CURRENT-1 UNITS day, CURRENT, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
            phaseIds.put("approval_phase", getProjectPhaseId());
            insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("approval_phase") + ", " + projectId +
                ", '11', '1', CURRENT+23 UNITS day, CURRENT+23 UNITS day, CURRENT+28 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
        }
        
        phaseIds.put("submission_phase", getProjectPhaseId());
        phaseIds.put("screening_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("submission_phase") + ", " + projectId +
            ", '2', '1', CURRENT, CURRENT, CURRENT+7 UNITS day, NULL, NULL, '604800000', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening_phase") + ", " + projectId +
            ", '3', '1', CURRENT+7 UNITS day, CURRENT+7 UNITS day, CURRENT+8 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        // Secondary Reviewer Review
        phaseIds.put("secondary_reviewer_review_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("secondary_reviewer_review_phase") + ", " + projectId +
            ", '15', '1', CURRENT+8 UNITS day, CURRENT+8 UNITS day, CURRENT+14 UNITS day, NULL, NULL, '345600000', '132456', CURRENT, '132456', CURRENT)");
        // Primary Review Evaluation
        phaseIds.put("primary_review_evaluation_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("primary_review_evaluation_phase") + ", " + projectId +
            ", '16', '1', CURRENT+14 UNITS day, CURRENT+14 UNITS day, CURRENT+15 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        // New Appeals
        phaseIds.put("new_appeals_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("new_appeals_phase") + ", " + projectId +
            ", '17', '1', CURRENT+15 UNITS day, CURRENT+15 UNITS day, CURRENT+16 UNITS day, NULL, NULL, '86400000', '132456', CURRENT, '132456', CURRENT)");
        // Primary Review Appeals Response
        phaseIds.put("primary_review_appeals_response_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("primary_review_appeals_response_phase") + ", " + projectId +
            ", '19', '1', CURRENT+16 UNITS day, CURRENT+16 UNITS day, CURRENT+17 UNITS day, NULL, NULL, '43200000', '132456', CURRENT, '132456', CURRENT)");
        // Aggregation
        phaseIds.put("aggregation_phase", getProjectPhaseId());
        insertData(con,
                "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("aggregation_phase") + ", " + projectId +
                ", '7', '1', CURRENT+17 UNITS day, CURRENT+17 UNITS day, CURRENT+18 UNITS day, NULL, NULL, '43200000', '132456', CURRENT, '132456', CURRENT)");
        phaseIds.put("aggregation_review_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("aggregation_review_phase") + ", " + projectId +
            ", '8', '1', CURRENT+18 UNITS day, CURRENT+18 UNITS day, CURRENT+19 UNITS day, NULL, NULL, '43200000', '132456', CURRENT, '132456', CURRENT)");
        phaseIds.put("final_fix_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("final_fix_phase") + ", " + projectId +
            ", '9', '1', CURRENT+19 UNITS day, CURRENT+19 UNITS day, CURRENT+22 UNITS day, NULL, NULL, '259200000', '132456', CURRENT, '132456', CURRENT)");
        phaseIds.put("final_review_phase", getProjectPhaseId());
        insertData(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("final_review_phase") + ", " + projectId +
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
            phaseIds.get("submission_phase") + ", '2', '0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("submission_phase") + ", '5', 'No', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening_phase") + ", '3', '0', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening_phase") + ", '5', 'No', '132456', CURRENT, '132456', CURRENT)");
        insertData(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("secondary_reviewer_review_phase") + ", '1', '" + APPLICATION_SCORECARD_ID + "', '132456', CURRENT, '132456', CURRENT)");
        if (allPhase) {
            insertData(con,
                    "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
                    phaseIds.get("approval_phase") + ", '6', '1', '132456', CURRENT, '132456', CURRENT)");
        }

        // Data for phase_dependency
        insertData(con,
            "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("submission_phase") + ", " + phaseIds.get("screening_phase") + ", '1', '1', '0', '132456', CURRENT, '132456', CURRENT)");
        
        return phaseIds;
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

    /**
     * Method to create db connection.
     * 
     * @return created connection.
     * 
     * @throws Exception if any error occurred.
     */
    static Connection getConnection() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl(
        "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
    
        return factory.createConnection();
    }

    /**
     * This method set status to open of specified phase. 
     * 
     * @param phaseId phase id to update.
     * 
     * @param con connection to use.
     * 
     * @throws Exception if any error occurred.
     */
    static void openPhase(long phaseId, Connection con) throws Exception {
        insertData(con, 
                "UPDATE project_phase SET phase_status_id = 2 WHERE project_phase_id = " + phaseId);
    }

    /**
     * This method set status to close of specified phase.
     *
     * @param phaseId phase id to update.
     *
     * @param con connection to use.
     *
     * @throws Exception if any error occurred.
     */
    static void closePhase(long phaseId, Connection con) throws Exception {
        insertData(con,
                "UPDATE project_phase SET phase_status_id = 3 WHERE project_phase_id = " + phaseId);
    }

    /**
     * Adds resource to the specified project. 
     * 
     * @param projectId project to add resource.
     * 
     * @param roleId resource role to use.
     * 
     * @param projectPhaseId phase to assign resource to.
     * 
     * @param userId user id to add.
     * 
     * @param resourceHandle user handle to add. 
     * 
     * @param con connection to use.
     * 
     * @return id of created resource.
     * 
     * @throws Exception if ant error occurred.
     */
    static long AddResource(long projectId, String roleId, long projectPhaseId, long userId, String resourceHandle, Connection con) throws Exception {
        long resourceId = getNextResourceId();
        
        insertData(con, 
                "INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", " + roleId + ", " + projectId + ", " + (projectPhaseId<=0? "NULL": projectPhaseId)+ ", '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        
        insertData(con, 
                "INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", 1, '" + userId + "', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        insertData(con, 
                "INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", 2, '" + resourceHandle + "', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        insertData(con, 
                "INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", 8, 'N/A', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        return resourceId;
    }

    /**
     * Adds submission to the specified project. 
     * 
     * @param projectId project id to add submission.
     * 
     * @param resourceId submitter's id. 
     * 
     * @param submissionTypeId submission type (spec submission of contest submission)
     * 
     * @param con connection to use.
     *
     * @return id of submission. 
     * 
     * @throws Exception if any error occurred. 
     */
    static long addSubmission (long projectId, long resourceId, long submissionTypeId, Connection con) throws Exception {
        long uploadId = getNextUploadId();
        insertData(con, 
                "INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
                uploadId + ", " + projectId + ", " + resourceId + ", 1, 1, 'no_file.txt', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        
        long submissionId = getNextSubmissionId();
        insertData(con,
                "INSERT INTO submission (submission_id, upload_id, submission_status_id, screening_score, initial_score, final_score, placement, submission_type_id, create_user, create_date, modify_user, modify_date) VALUES (" +
                submissionId + ", " + uploadId + ", 1, NULL, NULL, NULL, NULL, " + submissionTypeId + ", '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        
        insertData(con,
                "INSERT INTO resource_submission (resource_id, submission_id, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", " + submissionId + ", '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        return submissionId;
    }

    /**
     * Adds review to the specified project.
     *
     * @param resourceId submitter's id.
     *
     * @param submissionId submission id
     *
     * @param con connection to use.
     *
     * @return id of submission.
     *
     * @throws Exception if any error occurred.
     */
    static long[] AddReview ( long resourceId, long submissionId, Connection con) throws Exception {
        long reviewId = getNextId("review_id_seq");
        insertData(con,
                "INSERT INTO review (review_id, resource_id, submission_id, scorecard_id, committed, score, initial_score, create_user, create_date, modify_user, modify_date) VALUES (" +
                reviewId + ", " + resourceId + ", " + submissionId +","+ APPLICATION_SCORECARD_ID +", 1 , 100.00 , 100.00 ,'"+TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );

        long reviewItemId = getNextId("review_item_id_seq");
        insertData(con,
                "INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, upload_id, answer, sort, create_user, create_date, modify_user, modify_date) VALUES (" +
                reviewItemId + ", " + reviewId + "," + SCORECARD_QUESTION_ID + ", NULL, '4/4', 0 ,'" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        long reviewItemCommentId = getNextId("review_item_comment_id_seq");
        insertData(con,
                "INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, extra_info, sort, create_user, create_date, modify_user, modify_date, evaluation_type_id) VALUES (" +
                reviewItemCommentId + ", " + resourceId + ","+ reviewItemId +",1, 'OK', NULL, 0, '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT, 1)" );
        return new long[] {reviewId, reviewItemId};
    }

    /**
     * Adds an appeal to a specific review item.
     *
     * @param resourceId the resource id
     * @param reviewItemId the specific review item id
     * @param con connection to use.
     * @return id of appeal item comment.
     * @throws Exception if any error occurred.
     */
    static long AddAppeal(long resourceId, long reviewItemId, Connection con) throws Exception {
        long reviewItemCommentId = getNextId("review_item_comment_id_seq");
        insertData(con,
                "INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, extra_info, sort, create_user, create_date, modify_user, modify_date, evaluation_type_id) VALUES (" +
                reviewItemCommentId + ", " + resourceId + ","+ reviewItemId +",4, 'appeal content', NULL, 0, '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT, NULL)" );
        return reviewItemCommentId;
    }
    /**
     * Get the resource id from the sequence.
     *
     * @return the resource id
     *
     * @throws Exception if any error
     */
    static long getNextResourceId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("resource_id_seq");

        return generator.getNextID();
    }

    /**
     * Get the upload id from the sequence.
     *
     * @return the upload id
     *
     * @throws Exception if any error
     */
    static long getNextUploadId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("upload_id_seq");

        return generator.getNextID();
    }

    /**
     * Get the submission id from the sequence.
     *
     * @return the submission id
     *
     * @throws Exception if any error
     */
    static long getNextSubmissionId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("submission_id_seq");

        return generator.getNextID();
    }

    /**
     * Get the id of the specific type.
     * @param type represent type of the id.
     * @return id of the type
     *
     * @throws Exception if any error
     */
    static long getNextId(String type) throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator(type);

        return generator.getNextID();
    }

    /**
     * Log in as normal competitor.
     *
     * @param browser the selenium browser
     * @param username the user name
     * @param password the password
     * @throws Exception if any error
     */
    static void loginAsCompetitor(Selenium browser, String username, String password) throws Exception {
        // login with normal username
        browser.type("userName", username);
        browser.type("password", password);
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(getTimeout());
    }
    
    /**
     * Mark a resource have complete eary appeals.
     * 
     * @param resouceId the resource id
     * @param con the connection
     * @throws Exception if any error occurs
     */
    static void earylyCompleteApeal(long resouceId, Connection con) throws Exception {
        insertData(con, "insert into resource (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES ("
                + resouceId + ", 13, 'Yes', '13','"+TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    }
}
