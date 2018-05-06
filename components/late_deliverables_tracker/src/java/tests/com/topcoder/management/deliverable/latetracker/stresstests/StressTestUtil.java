/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.stresstests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.Properties;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.util.config.ConfigManager;

/**
 * Stress tests utilities.
 * <p>
 * This class defines some common routines to help unit tests.
 * </p>
 *
 * @author morehappiness
 * @version 1.0
 */
final class StressTestUtil {
    /**
     * The configuration file for the LateDeliverablesRetriever implementation.
     */
    private static final String RETRIEVER_CONFIG_FILE = "test_files/stress/LateDeliverablesRetrieverImpl.xml";
    /**
     * The configuration file for the LateDeliverableProcessor implementation.
     */
    private static final String PROCESSOR_CONFIG_FILE = "test_files/stress/LateDeliverableProcessorImpl.xml";

    /**
     * The configuration file path for the database properties.
     */
    public static final String DB_PROPERTIES_FILE = "test_files/stress/db.properties";

    /**
     * Drops table file path.
     */
    private static final String DROP_TABLE_PATH = "test_files/stress/drop.sql";

    /**
     * Inserts data into table file path.
     */
    private static final String ISNERT_DATA_PATH = "test_files/stress/insert.sql";

    /**
     * Separator for sql statement.
     */
    private static final String SQL_SEPARATOR = ";";
    /**
     * The configuration file for scheduling the job.
     */
    private static final String TRACER_CONFIG_FILE = "test_files/stress/scheduler.xml";
    /**
     * The resource info values template.
     */
    private static final String[] RESOURCE_INFO_VALUES_TEMPLATE = {"", "handle", "handle", "1400", "100%",
        "2004-01-01 20:14:24.000", "", "", "", "", "", "", ""};

    /**
     * Mili-seconds in a day.
     */
    private static final int MILI_SECONDS_IN_A_DAY = 86400000;

    /**
     * Empty private constructor.
     */
    private StressTestUtil() {
        // empty
    }

    /**
     * Get field value via reflection.
     *
     * @param obj
     *            the object to retrieve value from
     * @param name
     *            the name of the field
     * @return the value of the field
     * @throws Exception
     *             to JUnit.
     */
    static Object getFieldValue(Object obj, String name) throws Exception {
        Field field = obj.getClass().getDeclaredField(name);

        field.setAccessible(true);

        return field.get(obj);
    }

    /**
     * <p>
     * Reads given file.
     * </p>
     *
     * @param path
     *            the path to the file to read
     * @return the contents of the file
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    static StringBuffer readFile(String path) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(path));

            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            return sb;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

    /**
     * Sets up the database.
     *
     * @param con
     *            the connection to use.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    static void setUpDataBase(Connection con) throws SQLException, IOException {
        executeSqlFile(con, ISNERT_DATA_PATH);
    }

    /**
     * Clears the database.
     *
     * @param con
     *            the connection to use.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    static void clearDataBase(Connection con) throws SQLException, IOException {
        executeSqlFile(con, DROP_TABLE_PATH);
    }

    /**
     * <p>
     * Executes the sql file.
     * </p>
     *
     * @param con
     *            the connection to use.
     * @param sqlPath
     *            the sql file path.
     * @throws SQLException
     *             to JUnit
     * @throws IOException
     *             to JUnit
     */
    static void executeSqlFile(Connection con, String sqlPath) throws SQLException, IOException {
        con.setAutoCommit(false);
        Statement st = con.createStatement();

        String[] sqls = readFile(sqlPath).toString().split(SQL_SEPARATOR);

        for (String sql : sqls) {
            if (!(sql.trim().trim().length() == 0)) {
                st.execute(sql);
                con.commit();
            }
        }
    }

    /**
     * <p>
     * Loads the configuration file given by configFile into a Properties object.
     * </p>
     *
     * @param configFile
     *            the path of the configuration file.
     * @return the loaded Properties object
     * @throws Exception
     *             to JUnit
     */
    static Properties loadProperties(String configFile) throws Exception {
        // Create an empty Properties
        Properties prop = new Properties();

        InputStream in = null;

        try {
            // Create an input stream from the given configFile
            in = new FileInputStream(configFile);

            // Load the properties
            prop.load(in);
        } finally {
            // Close the input stream
            if (in != null) {
                in.close();
            }
        }

        return prop;
    }

    /**
     * <p>
     * Create a new db connection to return.
     * </p>
     *
     * @param properties
     *            the properties file to use
     * @return the db connection.
     * @throws Exception
     *             to JUnit
     */
    static Connection createConnection(Properties properties) throws Exception {
        String dbUrl = properties.getProperty("dburl");
        String dbUser = properties.getProperty("dbuser");
        String dbPassword = properties.getProperty("dbpassword");
        String dbClass = properties.getProperty("dbdriver");

        Class.forName(dbClass);

        if ((dbUser != null) && (!(dbUser.trim().length() == 0))) {
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } else {
            return DriverManager.getConnection(dbUrl);
        }
    }

    /**
     * <p>
     * Closes the database connection.
     * </p>
     *
     * @param connection
     *            the database connection to close
     * @throws Exception
     *             to JUnit
     */
    static void closeConnection(Connection connection) throws Exception {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    /**
     * <p>
     * Clears all the configuration loaded.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    static void clearConfigurations() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator<?> iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>
     * Adds all the configuration used for tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    static void addStressTestConfigurations() throws Exception {
        clearConfigurations();

        ConfigManager configManager = ConfigManager.getInstance();

        String[] configFiles = new String[]{"test_files/stress/DB_Factory.xml",
            "test_files/stress/Logging_Wrapper.xml", "test_files/config/Project_Management.xml",
            "test_files/config/Phase_Management.xml", "test_files/config/Upload_Resource_Search.xml",
            "test_files/config/SearchBuilderCommon.xml", "test_files/stress/User_Config.xml"};

        for (String configFile : configFiles) {
            configManager.add(new File(configFile).getAbsolutePath());
        }
    }

    /**
     * <p>
     * Gets configuration object for the specified class name which is the namespace.
     * </p>
     *
     * @param clzz
     *            the specified class name which is the namespace
     * @return the configuration object loaded
     */
    static ConfigurationObject getConfig(Class<?> clzz) throws Exception {
        ConfigurationFileManager configurationFileManager = new ConfigurationFileManager();

        if (clzz.getName().equals(LateDeliverablesRetrieverImpl.class.getName())) {
            configurationFileManager.loadFile("root", RETRIEVER_CONFIG_FILE);
        } else if (clzz.getName().equals(LateDeliverableProcessorImpl.class.getName())) {
            configurationFileManager.loadFile("root", PROCESSOR_CONFIG_FILE);
        } else {
            configurationFileManager.loadFile("root", TRACER_CONFIG_FILE);
        }

        return configurationFileManager.getConfiguration("root").getChild(clzz.getName());
    }

    /**
     * <p>
     * Closes the database Statement.
     * </p>
     *
     * @param stmt
     *            the Statement to close
     */
    static void closeStatement(Statement stmt) {
        if (null != stmt) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // ignore
            }
        }
        stmt = null;
    }

    /**
     * <p>
     * Deletes data from the tables.
     * </p>
     *
     * @param tables
     *            the tables to clear
     * @param con
     *            the database connection
     * @throws Exception
     *             to JUnit
     */
    static void deleteFromTable(String[] tables, Connection con) throws Exception {
        PreparedStatement preStmt = null;

        for (int i = 0; i < tables.length; i++) {
            String insertProjectSql = "DELETE FROM " + tables[i];

            try {
                preStmt = con.prepareStatement(insertProjectSql);

                preStmt.executeUpdate();
            } finally {
                StressTestUtil.closeStatement(preStmt);
            }
        }
    }

    /**
     * <p>
     * Prepares the project data for the stress tests.
     * </p>
     * <p>
     * There will be projectsCount/2 projects active. And with projectsCount/4 resource(specified submissions)
     * </p>
     *
     * @param projectsCount
     *            the count of project to insert into the database
     * @param subCount
     *            the count of submission to insert inot the database
     * @param con
     *            the connection to use
     * @throws Exception
     *             to JUnit
     */
    static void prepareProjectData(long projectsCount, int subCount, Connection con) throws Exception {
        for (int k = 0; k < projectsCount; k++) {
            int projectId = k + 1;
            con.setAutoCommit(false);
            prepareProject(projectId, con);

            prepareProjectInfo(projectId, con);

            prepareProjectPhase(projectId, con); // the project phase id of the late phase is (projectId * 3 + 2)

            prepareResource(projectId, subCount, con);
            con.commit();
        }
    }

    /**
     * <p>
     * Prepares the upload table data.
     * </p>
     *
     * @param projectId
     *            the project id to set
     * @param resourceId
     *            the resource id to set
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareUpload(int projectId, int resourceId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;

        String insertSql = "INSERT INTO upload VALUES(?, ?, ?, 1, 1, 'param', 'System', ?, 'System', ?)";
        // upload type is 'submission'
        // upload status is 'Active'
        //
        try {
            preStmt = con.prepareStatement(insertSql);

            // make one upload for each submitter
            int uploadId = resourceId;

            preStmt.setLong(1, uploadId);
            preStmt.setLong(2, projectId);
            preStmt.setLong(3, resourceId); // each upload with the resource id to uploadId
            preStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preStmt.executeUpdate();
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the resource related data.
     * </p>
     *
     * @param projectId
     *            the project id to set
     * @param subCount
     *            the submission count to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareResource(int projectId, int subCount, Connection con) throws SQLException {
        PreparedStatement preStmt = null;

        String insertSql = "INSERT INTO resource VALUES(?, ?, ?, null, 'System', ?, 'System', ?)";
        int resourceId = 0;
        // set the project phase id to NULL
        try {
            preStmt = con.prepareStatement(insertSql);

            // 1000 submissions
            for (int i = 0; i < subCount; i++) {
                // step the resource id
                resourceId = projectId * (subCount + 1) + i;

                preStmt.setLong(1, resourceId);
                preStmt.setLong(2, 1); // sets the role to 'submitter'
                preStmt.setLong(3, projectId); // sets the project id

                preStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                preStmt.executeUpdate();

                // set resource info for each resources including the primary screener
                prepareResourceInfo(resourceId, con);

                prepareUpload(projectId, resourceId, con);

                prepareSubmission(projectId, resourceId, con);
            }
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }

        insertSql = "INSERT INTO resource VALUES(?, ?, ?, ?, 'System', ?, 'System', ?)";

        try {
            preStmt = con.prepareStatement(insertSql);

            resourceId = resourceId + 1;

            preStmt.setLong(1, resourceId);
            preStmt.setLong(2, 2); // sets the role to 'Primary Screener'
            preStmt.setLong(3, projectId); // sets the project id
            preStmt.setLong(4, projectId * 3 + 2); // set the screener to the late project phase id
            preStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
            preStmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            preStmt.executeUpdate();

            // set resource info for each resources including the primary screener
            prepareResourceInfo(resourceId, con);
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the submission table data.
     * </p>
     *
     * @param projectId
     *            the project id to set
     * @param resourceId
     *            the resource id to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareSubmission(int projectId, int resourceId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;
        String insertSql = "INSERT INTO submission VALUES(?, ?, 1, 1, null, null, null, null, 'System', ?, 'System', ?)";
        // submission type is 'Contest Submission'
        // submission status is 'Active'
        // screening_score,initial_score, final_score,placement are all null

        try {
            preStmt = con.prepareStatement(insertSql);

            // make one submission for each submitter and link to each upload
            int submissionId = resourceId;

            preStmt.setLong(1, submissionId);
            preStmt.setLong(2, resourceId); // upload id is the same as the submission id

            preStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis() - MILI_SECONDS_IN_A_DAY * 10));
            preStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis() - MILI_SECONDS_IN_A_DAY * 10));
            preStmt.executeUpdate();
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the resource_info table data.
     * </p>
     *
     * @param resourceId
     *            the resource id to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareResourceInfo(int resourceId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;
        String insertSql = "INSERT INTO resource_info VALUES(?, ?, ?, 'System', ?, 'System', ?)";
        try {
            preStmt = con.prepareStatement(insertSql);

            int resourceInfoTypeCount = 13;

            int resourceInfoTypeId;

            for (int j = 0; j < resourceInfoTypeCount; j++) {
                resourceInfoTypeId = j + 1;

                preStmt.setLong(1, resourceId);
                preStmt.setLong(2, resourceInfoTypeId); // sets the resource info type to the corresponding
                // resource into type id
                String value = RESOURCE_INFO_VALUES_TEMPLATE[j] + resourceInfoTypeId;
                if (resourceInfoTypeId == 3) {
                    value += "@topcoder.com";
                }
                preStmt.setString(3, value); // sets the
                // corresponding value
                // according to the
                // template
                preStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                preStmt.executeUpdate();
            }
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the project_phase table data.
     * </p>
     *
     * @param projectId
     *            the project id to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareProjectPhase(int projectId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;
        String insertSql = "INSERT INTO project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,"
            + "scheduled_start_time, scheduled_end_time, duration,"
            + " create_user, create_date, modify_user, modify_date)"
            + "VALUES (?, ?, ?, ?, ?, ?, ?, 'System', ?, 'System', ?)";
        try {
            long currentTime = System.currentTimeMillis();

            int projectPhaseId = projectId * 3; // make different project phase for project id

            preStmt = con.prepareStatement(insertSql);
            preStmt.setLong(1, projectPhaseId);
            preStmt.setLong(2, projectId); // sets the project id
            preStmt.setLong(3, 1); // set to 'registration' phase
            preStmt.setLong(4, 3); // make sure it's closed

            Timestamp scheduledStart = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 9));
            Timestamp scheduledEnd = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 7));

            preStmt.setTimestamp(5, scheduledStart);
            preStmt.setTimestamp(6, scheduledEnd);
            preStmt.setLong(7, MILI_SECONDS_IN_A_DAY * 2); // two days MILI_SECONDS_IN_A_DAY for registration phase
            preStmt.setTimestamp(8, scheduledStart);
            preStmt.setTimestamp(9, scheduledStart);

            preStmt.executeUpdate();

            projectPhaseId++;

            preStmt.setLong(1, projectPhaseId);
            preStmt.setLong(2, projectId); // sets the project id
            preStmt.setLong(3, 2); // set to 'submission' phase
            preStmt.setLong(4, 3); // make sure it's closed

            scheduledStart = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 7));
            scheduledEnd = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 2));

            preStmt.setTimestamp(5, scheduledStart);
            preStmt.setTimestamp(6, scheduledEnd);
            preStmt.setLong(7, MILI_SECONDS_IN_A_DAY * 5); // five days MILI_SECONDS_IN_A_DAY for registration phase
            preStmt.setTimestamp(8, scheduledStart);
            preStmt.setTimestamp(9, scheduledStart);

            preStmt.executeUpdate();

            projectPhaseId++;

            preStmt.setLong(1, projectPhaseId);
            preStmt.setLong(2, projectId); // sets the project id
            preStmt.setLong(3, 3); // set to 'screening' phase
            preStmt.setLong(4, 2); // make sure it's open

            scheduledStart = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 2));
            scheduledEnd = new Timestamp(currentTime - (MILI_SECONDS_IN_A_DAY * 1)); // this will make the 'screening'
            // phase be
            // late

            preStmt.setTimestamp(5, scheduledStart);
            preStmt.setTimestamp(6, scheduledEnd);
            preStmt.setLong(7, MILI_SECONDS_IN_A_DAY); // one days MILI_SECONDS_IN_A_DAY for registration phase
            preStmt.setTimestamp(8, scheduledStart);
            preStmt.setTimestamp(9, scheduledStart);

            preStmt.executeUpdate();
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the project_info table data.
     * </p>
     *
     * @param projectId
     *            the project id to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareProjectInfo(int projectId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;

        // inserts the required project info 'Track Late Deliverables', 'Project
        // Name', 'Project version'
        String insertSql = "INSERT INTO project_info VALUES(?, ?, ?, 'System', ?, 'System', ?)";
        try {
            preStmt = con.prepareStatement(insertSql);

            preStmt.setLong(1, projectId);
            preStmt.setLong(2, 48); // sets the project info type
            // to
            // 'Track Late Deliverables'
            preStmt.setString(3, "true");
            preStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            preStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            preStmt.executeUpdate();

            preStmt.setLong(2, 6); // sets the project info type to 'Project
            // Name'
            preStmt.setString(3, "Project 1");

            preStmt.executeUpdate();

            preStmt.setLong(2, 7); // sets the project info type to 'Project
            // version'
            preStmt.setString(3, "1.0");

            preStmt.executeUpdate();
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }

    /**
     * <p>
     * Prepares the project table data.
     * </p>
     *
     * @param projectId
     *            the project id to use
     * @param con
     *            the connection to use
     * @throws SQLException
     *             to JUnit
     */
    private static void prepareProject(int projectId, Connection con) throws SQLException {
        PreparedStatement preStmt = null;
        String insertSql = "INSERT INTO project VALUES(?, 1, 1, 'System', ?, 'System', ?)";

        try {
            preStmt = con.prepareStatement(insertSql);

            preStmt.setInt(1, projectId);

            preStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preStmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            preStmt.executeUpdate();
        } finally {
            StressTestUtil.closeStatement(preStmt);
            preStmt = null;
        }
    }
}
