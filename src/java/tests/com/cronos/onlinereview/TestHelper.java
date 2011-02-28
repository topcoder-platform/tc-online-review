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
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * <p>Represents the review link.</p>
     */
    private static final String REVIEW_URL = "review_link";

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
     * <p>Represents the upload file path.</p>
     */
    private static final String UPLOAD_FILE_PATH = "upload_file_path";

    /**
     * <p>Represents the RS location.</p>
     */
    private static final String RS = "rs";

    /**
     * <p>Represents the DOCUMENT 1 location.</p>
     */
    private static final String DOCUMENT1 = "doc1";

    /**
     * <p>Represents the DOCUMENT 2 location.</p>
     */
    private static final String DOCUMENT2 = "doc2";

    /**
     * <p>Represents the DOCUMENT 3 location.</p>
     */
    private static final String DOCUMENT3 = "doc3";

    /**
     * <p>Represents text shown on form when validation errors exist</p>
     */
    public static final String VALIDATION_ERROR = "There were validation errors.";
    
    /**
     * <p>Represents text shown on form when errors exist</p>
     */
    public static final String ERROR_TEXT = "An error occurred";

    /**
     * <p>Represents the competitor username.</p>
     */
    private static final String COMPETITOR_USERNAME = "competitor_username";

    /**
     * <p>Represents the competitor password.</p>
     */
    private static final String COMPETITOR_PASSWORD = "competitor_password";
    
    /**
     * <p>Represents the competitor user id.</p>
     */
    private static final String COMPETITOR_USER_ID= "competitor_user_id";
    
    /**
     * <p>Represents user id used when writing data into database.</p>
     */
    public static final String TESTS_USER_ID = "132456";

    /**
     * <p>Represents default design review scorecard id used when writing data into database.</p>
     */
    public static final long DESIGN_REVIEW_SCORECARD_ID = 30000411;

     /**
     * <p>Represents review scorecard question id used when writing data into database.</p>
     */
    public static final long SCORECARD_QUESTION_ID = 30001001;
    
    /**
     * <p>Represents date format to use in project name.</p>
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(" MMM-dd-yyyy 'at' HH'h' mm'm' ss's'");
    
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
     * To get the review URL.
     *
     * @return the reivew url
     *
     * @throws Exception if any error occurred
     */
    static String getReviewURL() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, REVIEW_URL).toString();
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
     * Create the project with generated id.
     *
     * @param projectId the project id.
     * @param projectId2 the 2nd project id
     * @param registrationClosed indicate if the registration is closed
     * @param allPhase indicates if all phases are included for this project
     *
     * @throws Exception if any error
     */
    static void createProject(long projectId, String testName, Map<String, Long> phaseIds)
        throws Exception {
    	Connection con = getConnection();
    	try {
	        String componentName = "Test Case " + testName + DATE_FORMAT.format(new Date());

	        createProject(projectId, componentName, con);
	        
	        generatePhaseIds(phaseIds);
	        
	        createProjectPhases(projectId, phaseIds, con);
	       	
	        createPhaseDependencies(phaseIds, con);
	            
	       	createPhaseCriterias(phaseIds, con);
    	} 
    	finally {
    		con.close();
    	}
    }
    
    /**
     * Creates project with generated id. Delegates all work to overloaded method.
     * 
     * @param projectId project id to use.
     * 
     * @param testName test name to use.
     * 
     * @throws Exception if any error occurred.
     */
    static void createProject(long projectId, String testName) throws Exception {
    	createProject(projectId, testName, new HashMap<String, Long>());
    }
    
    /**
     * Creates project with component data. 
     * 
     * @param projectId project id to use.
     * 
     * @param componentName component name to use. 
     *  
     * @param con connection to use. 
     * 
     * @throws Exception if any error occurred.
     */
    static void createProject(long projectId, String componentName, Connection con) throws Exception {
    	// Data for project
        executeStatement(con,
            "INSERT INTO project (project_id, project_status_id, project_category_id, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '1', '1', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT);");
        
        // Data for comp_catalog
        long componentId = getNextComponentId();
        executeStatement(con,
            "INSERT INTO comp_catalog (component_id, current_version, short_desc, component_name, description, function_desc, create_time, status_id, root_category_id, modify_date, public_ind) VALUES (" +
            componentId + ", '1', '" + componentName + "', '" + componentName + "', '" + componentName +
            "', 'Component', CURRENT, '102', '5801776', CURRENT, '0')");
        
        // Data for comp_versions
        long versionId = getNextComponentVersionsId();
        executeStatement(con,
            "INSERT INTO comp_versions (comp_vers_id, component_id, version, version_text, create_time, phase_id, phase_time, price, comments, modify_date, suspended_ind, browse, location, issue_tracker_path, revision) VALUES (" +
            versionId + ", " + componentId + ", '1', '1.0', CURRENT, '112', CURRENT, '10000.00', '"+ componentName + "', CURRENT, '0', NULL, NULL, NULL, NULL)");
        // Data for comp_categories
        executeStatement(con,
            "INSERT INTO comp_categories (comp_categories_id, component_id, category_id) VALUES (" +
            getNextComponentCategoryId() + ", " + componentId + ", '11')");
        // Data for comp_technology
        executeStatement(con,
            "INSERT INTO comp_technology (comp_tech_id, comp_vers_id, technology_type_id) VALUES (" +
            getNextComponentTechId() + ", " + versionId + ", '2')"); 
        
        // Data for project_info
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '1', '"+ versionId + "', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '2', '"+ componentId +"', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '3', '1', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '4', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '5', '5801776', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '6', '" + componentName + "', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '7', '1.0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '9', 'Off', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '10', 'On', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '11', 'On', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '12', 'Yes', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '13', 'Yes', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '14', 'Open', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '16', '1000.0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '26', 'On', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '29', 'On', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '30', '450', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '41', 'true', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '43', 'true', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '44', 'true', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '45', 'true', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_info (project_id, project_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
            projectId + ", '46', 'true', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        
    }
    
    /**
     * Crates phase criterias
     * 
     * @param phaseIds phase ids to use. 
     * 
     * @param con connection to use. 
     */
    static void createPhaseCriterias(Map<String, Long> phaseIds, Connection con) throws Exception {
    	// Data for phase_criteria
        executeStatement(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("registration") + ", '2', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("registration") + ", '5', 'No', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening") + ", '3', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening") + ", '5', 'No', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("approval") + ", '6', '1', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
   
    }
    
    /**
     * Creates phases for specified project 
     * 
     * @param projectId project id to use.
     * 
     * @param phaseIds map which holds ids of all phases to create.
     * 
     * @param con connection to use. 
     * 
     * @throws Exception if any error occurred. 
     */
    static void createProjectPhases (long projectId, Map<String, Long> phaseIds, Connection con) throws Exception {
    	// insert project phases.
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("spec_submission") + ", " + projectId +
            ", '13', '1', CURRENT-2 UNITS day, CURRENT-2 UNITS day, CURRENT-1 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("spec_review") + ", " + projectId +
            ", '14', '1', NULL, CURRENT-1 UNITS day, CURRENT, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("registration")+ ", " + projectId +
            ", '1', '1', NULL, CURRENT, CURRENT+2 UNITS day, NULL, NULL, '172800000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("submission") + ", " + projectId +
            ", '2', '1', NULL, CURRENT, CURRENT+7 UNITS day, NULL, NULL, '604800000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("screening") + ", " + projectId +
            ", '3', '1', NULL, CURRENT+7 UNITS day, CURRENT+8 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("review") + ", " + projectId +
            ", '4', '1', NULL, CURRENT+8 UNITS day, CURRENT+14 UNITS day, NULL, NULL, '518400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("appeals") + ", " + projectId +
            ", '5', '1', NULL, CURRENT+14 UNITS day, CURRENT+15 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("appeals_response") + ", " + projectId +
            ", '6', '1', NULL, CURRENT+15 UNITS day, CURRENT+16 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("aggregation") + ", " + projectId +
            ", '7', '1', NULL, CURRENT+16 UNITS day, CURRENT+17 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("aggregation_review") + ", " + projectId +
            ", '8', '1', NULL, CURRENT+17 UNITS day, CURRENT+18 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("final_fix") + ", " + projectId +
            ", '9', '1', NULL, CURRENT+18 UNITS day, CURRENT+22 UNITS day, NULL, NULL, '345600000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
            "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
            phaseIds.get("final_review") + ", " + projectId +
            ", '10', '1', NULL, CURRENT+22 UNITS day, CURRENT+23 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
        executeStatement(con,
	        "INSERT INTO project_phase (project_phase_id, project_id, phase_type_id, phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) VALUES (" +
	        phaseIds.get("approval") + ", " + projectId +
	        ", '11', '1', NULL, CURRENT+23 UNITS day, CURRENT+24 UNITS day, NULL, NULL, '86400000', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");
    }
    
    /**
     *  Creates phase dependencies between specified phases.
     *  
     * @param phaseIds phase ids to use
     * 
     * @param con connection to use
     * 
     * @throws Exception if any error occurred.
     */
    static void createPhaseDependencies (Map<String, Long> phaseIds, Connection con) throws Exception {
    	// Data for phase_dependency
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("spec_submission") + ", " + phaseIds.get("spec_review") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("spec_review") + ", " + phaseIds.get("registration") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
	            "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
	            phaseIds.get("registration") + ", " + phaseIds.get("submission") + ", '1', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("submission") + ", " + phaseIds.get("screening") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("screening") + ", " + phaseIds.get("review") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("review") + ", " + phaseIds.get("appeals") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("appeals") + ", " + phaseIds.get("appeals_response") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("appeals_response") + ", " + phaseIds.get("aggregation") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("aggregation") + ", " + phaseIds.get("aggregation_review") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("aggregation_review") + ", " + phaseIds.get("final_fix") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("final_fix") + ", " + phaseIds.get("final_review") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
        executeStatement(con,
                "INSERT INTO phase_dependency (dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time, create_user, create_date, modify_user, modify_date) VALUES (" +
                phaseIds.get("final_review") + ", " + phaseIds.get("approval") + ", '0', '1', '0', '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)");   
    
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
    static void OpenPhase(long phaseId, Connection con) throws Exception {
    	executeStatement(con, 
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
    static void ClosePhase(long phaseId, Connection con) throws Exception {
    	executeStatement(con,
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
    static long AddResource(long projectId, long roleId, long projectPhaseId, long userId, String resourceHandle, Connection con) throws Exception {
    	long resourceId = getNextResourceId();
    	
    	executeStatement(con, 
    			"INSERT INTO resource (resource_id, resource_role_id, project_id, project_phase_id, create_user, create_date, modify_user, modify_date) VALUES (" +
                resourceId + ", " + roleId + ", " + projectId + ", " + (projectPhaseId<=0? "NULL": projectPhaseId)+ ", '" + TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    	
    	executeStatement(con, 
    			"INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
    			resourceId + ", 1, '" + userId + "', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    	executeStatement(con, 
    			"INSERT INTO resource_info (resource_id, resource_info_type_id, value, create_user, create_date, modify_user, modify_date) VALUES (" +
    			resourceId + ", 2, '" + resourceHandle + "', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    	executeStatement(con, 
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
    static long AddSubmission (long projectId, long resourceId, long submissionTypeId, Connection con) throws Exception {
    	long uploadId = getNextUploadId();
    	executeStatement(con, 
    			"INSERT INTO upload (upload_id, project_id, resource_id, upload_type_id, upload_status_id, parameter, create_user, create_date, modify_user, modify_date) VALUES (" +
    			uploadId + ", " + projectId + ", " + resourceId + ", 1, 1, 'no_file.txt', '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    	
    	long submissionId = getNextSubmissionId();
    	executeStatement(con,
    			"INSERT INTO submission (submission_id, upload_id, submission_status_id, screening_score, initial_score, final_score, placement, submission_type_id, create_user, create_date, modify_user, modify_date) VALUES (" +
    			submissionId + ", " + uploadId + ", 1, NULL, NULL, NULL, NULL, " + submissionTypeId + ", '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    	
    	executeStatement(con,
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
    static long AddReview ( long resourceId, long submissionId, Connection con) throws Exception {
    	long reviewId = getNextId("review_id_seq");
    	executeStatement(con,
    			"INSERT INTO review (review_id, resource_id, submission_id, scorecard_id, committed, score, initial_score, create_user, create_date, modify_user, modify_date) VALUES (" +
    			reviewId + ", " + resourceId + ", " + submissionId +","+ DESIGN_REVIEW_SCORECARD_ID +", 1 , 100.00 , 100.00 ,'"+TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );

    	long reviewItemId = getNextId("review_item_id_seq");
    	executeStatement(con,
    			"INSERT INTO review_item (review_item_id, review_id, scorecard_question_id, upload_id, answer, sort, create_user, create_date, modify_user, modify_date) VALUES (" +
    			reviewItemId + ", " + reviewId + "," + SCORECARD_QUESTION_ID + ", NULL, '4/4', 0 ,'" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        long reviewItemCommentId = getNextId("review_item_comment_id_seq");
    	executeStatement(con,
    			"INSERT INTO review_item_comment (review_item_comment_id, resource_id, review_item_id, comment_type_id, content, extra_info, sort, create_user, create_date, modify_user, modify_date) VALUES (" +
    			reviewItemCommentId + ", " + resourceId + ","+ reviewItemId +",1, 'OK', NULL, 0, '" +TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        return reviewId;
    }
    
    /**
     * Insert data into the database.
     *
     * @param con the connection instance
     * @param sql the sql to be executed
     *
     * @throws SQLException if any sql error
     */
    static void executeStatement(Connection con, String sql)
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
     * To get the upload file path.
     *
     * @return the upload file path.
     *
     * @throws Exception if any error occurred
     */
    static String getUploadFilePath() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, UPLOAD_FILE_PATH).toString();
    }

    /**
     * To get the RS location.
     *
     * @return the distribution location.
     *
     * @throws Exception if any error occurred
     */
    static String getRSLocation() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, RS).toString();
    }

    /**
     * To get the RS location.
     *
     * @return the distribution location.
     *
     * @throws Exception if any error occurred
     */
    static String getDocument1() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, DOCUMENT1).toString();
    }

    /**
     * To get the RS location.
     *
     * @return the distribution location.
     *
     * @throws Exception if any error occurred
     */
    static String getDocument2() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, DOCUMENT2).toString();
    }

    /**
     * To get the RS location.
     *
     * @return the distribution location.
     *
     * @throws Exception if any error occurred
     */
    static String getDocument3() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        return cm.getProperty(TEST_NAMESPACE, DOCUMENT3).toString();
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
     * Log in as normal competitor.
     *
     * @param browser the selenium browser
     *
     * @throws Exception if any error
     */
    static void loginAsCompetitor(Selenium browser) throws Exception {
        // login with normal username
        browser.type("userName", getCompetitorUsername());
        browser.type("password", getCompetitorPassword());
        browser.click("//input[@name='']");
        browser.waitForPageToLoad(getTimeout());
	}

    /**
     * To get the competitor username.
     *
     * @return username
     *
     * @throws Exception if any error occurred
     */
    static String getCompetitorUsername() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, COMPETITOR_USERNAME).toString();
    }

    /**
     * To get the competitor password.
     *
     * @return password
     *
     * @throws Exception if any error occurred
     */
    static String getCompetitorPassword() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, COMPETITOR_PASSWORD).toString();
    }
    
    /**
     * To get the competitor user id.
     *
     * @return user id.
     *
     * @throws Exception if any error occurred
     */
    static String getCompetitiorUserId() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        return cm.getProperty(TEST_NAMESPACE, COMPETITOR_USER_ID).toString();
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
    static long getNextProjectId() throws Exception {
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
    static long getNextProjectPhaseId() throws Exception {
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
    static long getNextComponentId() throws Exception {
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
    static long getNextComponentVersionsId() throws Exception {
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
    static long getNextComponentCategoryId() throws Exception {
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
    static long getNextComponentTechId() throws Exception {
        IDGenerator generator = IDGeneratorFactory.getIDGenerator("COMPTECH_SEQ");

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
     * Generate ids of all project phases.
     * 
     * @param phaseIds map to write ids into.
     * 
     * @throws Exception if any error occurred. 
     */
    static void generatePhaseIds (Map<String, Long> phaseIds) throws Exception {
    	phaseIds.put("spec_submission", getNextProjectPhaseId());
    	phaseIds.put("spec_review", getNextProjectPhaseId());
    	phaseIds.put("registration", getNextProjectPhaseId());
    	phaseIds.put("submission", getNextProjectPhaseId());
    	phaseIds.put("screening", getNextProjectPhaseId());
    	phaseIds.put("review", getNextProjectPhaseId());
    	phaseIds.put("appeals", getNextProjectPhaseId());
    	phaseIds.put("appeals_response", getNextProjectPhaseId());
    	phaseIds.put("aggregation", getNextProjectPhaseId());
    	phaseIds.put("aggregation_review", getNextProjectPhaseId());
    	phaseIds.put("final_fix", getNextProjectPhaseId());
    	phaseIds.put("final_review", getNextProjectPhaseId());
    	phaseIds.put("approval", getNextProjectPhaseId());
    }



    /**
     * Let the competitor to unregister in the project.
     * @param browser the selenium browser
     * @param ProjectId represent the project which will be registered by the competitor.
     *
     * @throws Exception if any error occurred.
     */
    static void unregisterProject(Selenium browser, long ProjectId) throws Exception{
        //login as competitor
        loginAsCompetitor(browser);
        // open the project details page
        browser.open(TestHelper.getBaseURL() + TestHelper.getProjectURL() + ProjectId);

         // Click the 'Unregister' Link
        browser.click("//table[@id='table12']/tbody/tr[1]/td[4]/a[1]");
        browser.waitForPageToLoad(getTimeout());
        // confirm unregister
        browser.click("//img[@alt='Confirm']");
        browser.waitForPageToLoad(getTimeout());

        // logout the user
        browser.click("link=Logout");
        browser.waitForPageToLoad(getTimeout());


    }

    /**
     * Add a new spec review score card.
     *
     * @throws Exception if any error occurred.
     */

    static long addSpecReviewScoreCard(Connection con) throws Exception{
        long scorecardId = getNextId("scorecard_id_seq");
    	executeStatement(con,
    			"INSERT INTO scorecard (scorecard_id, scorecard_status_id, scorecard_type_id, project_category_id, name, version, min_score, max_score, create_user, create_date, modify_user, modify_date) VALUES ("  +
    			scorecardId + ",1, 5, 28, 'Default Spec Review Scorecard', '2.0', 75.0, 100.0, '"+TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
        return scorecardId;
    }

    /**
     * Delete a  spec review score card.
     *
     * @throws Exception if any error occurred.
     */

    static void deleteSpecReviewScoreCard(Connection con, long scorecardId) throws Exception{
        if(scorecardId != -1) {
    	    executeStatement(con,
    			"DELETE FROM scorecard where scorecard_id=" + scorecardId);
        }
    }
  
    /**
     * Sets scorecard for specified phase. 
     * 
     * @param con connection to use. 
     * 
     * @param phaseId phase to set scorecard.
     * 
     * @param scorecardId scorecard id to use.
     * 
     * @throws Exception if any error occurred.
     */
    static void setPhaseScorecard(Connection con, long phaseId, long scorecardId ) throws Exception {
    	executeStatement(con, 
    			"INSERT INTO phase_criteria (project_phase_id, phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date)VALUES ("+
    			phaseId+", "+ 1 +", '" + scorecardId+ "', '"+TESTS_USER_ID + "', CURRENT, '" + TESTS_USER_ID + "', CURRENT)" );
    }

    /**
     * relogin as manager to delete the project.
     * @param browser the selenium browser
     * @param projectId represent the project which will be registered by the competitor.
     *
     * @throws Exception if any error occurred.
     */
    static void reloginAndDeleteProject(Selenium browser, long projectId) throws Exception{
        if (projectId != -1) {
    		browser.click("link=Logout");
            browser.waitForPageToLoad(TestHelper.getTimeout());
    		// login as manager to delete the project
    		TestHelper.loginUser(browser);
    		TestHelper.deleteProject(browser, projectId);
    	}
    }

 }
