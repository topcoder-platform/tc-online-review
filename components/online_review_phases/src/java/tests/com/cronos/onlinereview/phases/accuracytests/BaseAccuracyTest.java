/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;
import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

/**
 * This is a base class for all accuracy test cases. It loads configuration settings, database connection
 * and clean up for the test cases.
 *
 * @author tuenm, assistant
 * @version 1.2
 * @since 1.0
 */
public class BaseAccuracyTest extends TestCase {
    /**
     * The accuracy test configuration file.
     */
    public static final String CONNECTION_FACTORY_NS = "accuracy/ConnectionFactory.xml";

    /**
     * The log configuration file.
     */
    public static final String LOG_CONFIGURATION = "accuracy/Logging_Wrapper.xml";

    /**
     * The configuration namespace for phase handlers
     */
    public static final String PHASE_HANDLER_NS = "com.cronos.onlinereview.phases.AbstractPhaseHandler";

    /**
     * Represents the DBConnectionFactory instance.
     */
    private DBConnectionFactory dbFactory;

    /**
     * Represents the database connection instance.
     */
    private Connection conn;

    /**
     * <p/>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        // clear all configuration namespaces
        clearAllNamespace();

        // load the configuration file
        configManager.add(CONNECTION_FACTORY_NS);
        configManager.add(LOG_CONFIGURATION);

        // initialize DBConnectionFactory
        this.dbFactory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
    }

    /**
     * <p/>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // clear all configuration namespaces
        clearAllNamespace();

        closeConnection();
        conn = null;
        dbFactory = null;
    }

    /**
     * Clear all cofiguration namepsaces.
     *
     * @throws Exception to JUnit.
     */
    protected void clearAllNamespace() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * Returns a connection instance.
     *
     * @return a connection instance.
     * @throws Exception not for this test case.
     */
    protected Connection getConnection() throws Exception {
        if (conn == null) {
            conn = dbFactory.createConnection();
        }
        return conn;
    }


    /**
     * Closes the connection.
     *
     * @throws Exception not for this test case.
     */
    private void closeConnection() throws Exception {
        if (conn != null) {
            conn.close();
        }
    }

    /**
     * helper method to close a statement.
     *
     * @param stmt statement to close.
     */
    protected void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                // do nothing
            }
        }
    }

    /**
     * inserts a project into the database. Inserts records into the project, comp_catalog and comp_versions tables.
     *
     * @param conn connection to use.
     *
     * @throws Exception not under test.
     */
    protected void insertProject(Connection conn) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project
            String insertProject = "insert into project(project_id, project_status_id, project_category_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 1, 1, 'user', ?, 'user', ?)";
            System.out.println(insertProject);
            preparedStmt = conn.prepareStatement(insertProject);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertCatalog = "insert into comp_catalog(component_id, current_version, component_name," +
                "description, create_time, status_id) values " +
                "(1, 1, 'Online Review Phases', 'Online Review Phases', ?, 1)";
            System.out.println(insertCatalog);
            preparedStmt = conn.prepareStatement(insertCatalog);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertVersion = "insert into comp_versions(comp_vers_id, component_id, version,version_text," +
                "create_time, phase_id, phase_time, price, comments) values " +
                "(1, 1, 1, '1.0', ?, 112, ?, 500, 'Comments')";
            System.out.println(insertVersion);
            preparedStmt = conn.prepareStatement(insertVersion);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Set up phases include a new post-mortem phase. This method is brought in in version 1.1.
     *
     * @return the created project
     *
     * @throws Exception to JUnit.
     *
     * @since 1.1
     */
    protected Project setupPhasesWithPostMortem() throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        Project project = null;

        try {
            project = new Project(new Date(), new DefaultWorkdays());
            project.setId(1);

            // insert project first
            insertProject(conn);

            String insertPhase =
                "insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id," +
                "scheduled_start_time, scheduled_end_time, duration," +
                " create_user, create_date, modify_user, modify_date)" +
                "values (?, 1, ?, 1, ?, ?, ?, 'user', ?, 'user', ?)";

            preparedStmt = conn.prepareStatement(insertPhase);

            // insert all standard phases
            long[] phaseIds = new long[] { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112 };
            long[] phaseTypeIds = new long[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12 };
            String[] phaseTypeNames = new String[] {
                    "Registration", "Submission", "Screening", "Review", "Appeals", "Appeals Response", "Aggregation",
                    "Aggregation Review", "Final Fix", "Final Review", "Approval", "Post-Mortem"
                };
            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now);
            long duration = 24 * 60 * 60 * 1000; // one day
            Timestamp scheduledEnd = new Timestamp(now + duration);

            for (int i = 0; i < phaseIds.length; i++) {
                // insert into db
                System.out.println(insertPhase);
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setTimestamp(3, scheduledStart);
                preparedStmt.setTimestamp(4, scheduledEnd);
                preparedStmt.setLong(5, duration);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                // create phase intance
                Phase phase = new Phase(project, duration);
                phase.setId(phaseIds[i]);
                phase.setPhaseType(new PhaseType(phaseTypeIds[i], phaseTypeNames[i]));
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
                phase.setActualStartDate(scheduledStart);
                phase.setActualEndDate(scheduledEnd);
                phase.setScheduledStartDate(scheduledStart);
                phase.setScheduledEndDate(scheduledEnd);

                project.addPhase(phase);

                // re-calculate scheduled start and end.
                scheduledStart = new Timestamp(scheduledEnd.getTime());
                scheduledEnd = new Timestamp(scheduledStart.getTime() + duration);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert dependencies
            String insertDependency = "INSERT INTO phase_dependency " +
                "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time," +
                " create_user, create_date, modify_user, modify_date)" +
                "VALUES (?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertDependency);

            long[] dependencyPhaseIds = new long[] { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 102 };
            long[] dependentPhaseIds = new long[] { 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112 };
            Phase[] phases = project.getAllPhases();

            for (int i = 0; i < dependencyPhaseIds.length; i++) {
                System.out.println(insertDependency);
                preparedStmt.setLong(1, dependencyPhaseIds[i]);
                preparedStmt.setLong(2, dependentPhaseIds[i]);
                preparedStmt.setBoolean(3, false);
                preparedStmt.setBoolean(4, true);
                preparedStmt.setLong(5, 0);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                Dependency dependency = new Dependency(phases[i], phases[i + 1], false, true, 0);
                phases[i + 1].addDependency(dependency);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            project.getAllPhases()[11].setAttribute("Reviewer Number", "1");
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }

        return project;
    }

    /**
     * inserts project properties into the database. Inserts records into the project_info table.
     *
     * @param conn connection to use.
     * @param projectId project id.
     * @param infoTypes array of project info type ids.
     * @param infoValues array of corresponding project info values.
     *
     * @throws Exception not under test.
     */
    protected void insertProjectInfo(Connection conn, long projectId, long[] infoTypes, String[] infoValues)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project info
            String insertProjectInfo = "insert into project_info(project_id, project_info_type_id, value," +
                "create_user, create_date, modify_user, modify_date) values " + "(?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProjectInfo);

            for (int i = 0; i < infoTypes.length; i++) {
                System.out.println(insertProjectInfo + " for " + infoTypes[i]);
                preparedStmt.setLong(1, projectId);
                preparedStmt.setLong(2, infoTypes[i]);
                preparedStmt.setString(3, infoValues[i]);
                preparedStmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                preparedStmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Inserts a project and the standard phases into the database.
     *
     * @return project instance with phases populated.
     *
     * @throws Exception not under test.
     */
    protected Project setupPhases() throws Exception {
        return setupPhases("All");
    }

    /**
     * inserts a project and the standard phases into the database.
     *
     * @return project instance with phases populated.
     *
     * @throws Exception not under test.
     */
    protected Project setupPhases(String stepPhase) throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        Project project = null;

        try {
            project = new Project(new Date(), new DefaultWorkdays());
            project.setId(1);

            // insert project first
            insertProject(conn);

            String insertPhase =
                "insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id," +
                "scheduled_start_time, scheduled_end_time, duration," +
                " create_user, create_date, modify_user, modify_date)" +
                "values (?, 1, ?, 1, ?, ?, ?, 'user', ?, 'user', ?)";

            preparedStmt = conn.prepareStatement(insertPhase);

            // insert all standard phases
            long[] phaseIds = new long[] { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 };
            long[] phaseTypeIds = new long[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };
            String[] phaseTypeNames = new String[] {
                    "Registration", "Submission", "Screening", "Review", "Appeals", "Appeals Response", "Aggregation",
                    "Aggregation Review", "Final Fix", "Final Review", "Approval"
                };
            int step = phaseIds.length - 1;

            for (int i = 0; i < phaseTypeNames.length; i++) {
                if (phaseTypeNames[i].equalsIgnoreCase(stepPhase)) {
                    step = i;

                    break;
                }
            }
            long duration = 24 * 60 * 60 * 1000; // one day
            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now-duration*2);
            Timestamp scheduledEnd = new Timestamp(scheduledStart.getTime() + duration);

            for (int i = 0; i < (step + 1); i++) {
                // insert into db
                System.out.println(insertPhase);
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setTimestamp(3, scheduledStart);
                preparedStmt.setTimestamp(4, scheduledEnd);
                preparedStmt.setLong(5, duration);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                // create phase intance
                Phase phase = new Phase(project, duration);
                phase.setId(phaseIds[i]);
                phase.setPhaseType(new PhaseType(phaseTypeIds[i], phaseTypeNames[i]));
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
                phase.setActualStartDate(scheduledStart);
                phase.setActualEndDate(scheduledEnd);
                phase.setScheduledStartDate(scheduledStart);
                phase.setScheduledEndDate(scheduledEnd);

                project.addPhase(phase);

                // re-calculate scheduled start and end.
                scheduledStart = new Timestamp(scheduledEnd.getTime());
                scheduledEnd = new Timestamp(scheduledStart.getTime() + duration);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert dependencies
            String insertDependency = "INSERT INTO phase_dependency " +
                "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time," +
                " create_user, create_date, modify_user, modify_date)" +
                "VALUES (?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertDependency);

            long[] dependencyPhaseIds = new long[] { 101, 102, 103, 104, 105, 106, 107, 108, 109, 110 };
            long[] dependentPhaseIds = new long[] { 102, 103, 104, 105, 106, 107, 108, 109, 110, 111 };
            Phase[] phases = project.getAllPhases();

            for (int i = 0; i < step; i++) {
                System.out.println(insertDependency);
                preparedStmt.setLong(1, dependencyPhaseIds[i]);
                preparedStmt.setLong(2, dependentPhaseIds[i]);
                preparedStmt.setBoolean(3, false);
                preparedStmt.setBoolean(4, true);
                preparedStmt.setLong(5, 0);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                Dependency dependency = new Dependency(phases[i], phases[i + 1], false, true, 0);
                phases[i + 1].addDependency(dependency);
            }

            closeStatement(preparedStmt);
            preparedStmt = null;

            if (project.getAllPhases().length > 3) {
                //insert phase_criteria
                Statement stmt = conn.createStatement();
                stmt.execute(
                    "insert into phase_criteria(project_phase_id, phase_criteria_type_id, parameter,create_user," +
                    " create_date, modify_user, modify_date) values(104, 6, '1', user, sysdate, user, sysdate)");
                project.getAllPhases()[3].setAttribute("Reviewer Number", "1");
                stmt.close();
            }
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }

        return project;
    }

    /**
     * Setup project, phases, resources and project-notifications.
     *
     * @param step which phase to create
     * @param postMorterm create phases with post-morterm phase, if false, only create phases based on the step
     *
     * @return The created Project
     *
     * @throws Exception into JUnit
     * @since 1.2
     */
    protected Project setupProjectResourcesNotification(String step, boolean postMorterm)
        throws Exception {
        Connection conn = getConnection();
        Statement stmt = null;
        PreparedStatement preparedStmt = null;

        try {
            Project project = postMorterm ? this.setupPhasesWithPostMortem() : setupPhases(step);

            conn = getConnection();

            // insert project info for "Project Name" and "Project Version" info ids.
            insertProjectInfo(conn, 1, new long[] { 6, 7 }, new String[] { "Online Review Phases", "1.2" });

            // insert into notification
            String insertNotification = "insert into notification(project_id, external_ref_id, notification_type_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 1, 1, 'user', ?, 'user', ?)";
            System.out.println(insertNotification);
            preparedStmt = conn.prepareStatement(insertNotification);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);

            insertNotification = "insert into notification(project_id, external_ref_id, notification_type_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 2, 1, 'user', ?, 'user', ?)";
            System.out.println(insertNotification);
            preparedStmt = conn.prepareStatement(insertNotification);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);

            insertNotification = "insert into notification(project_id, external_ref_id, notification_type_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 3, 1, 'user', ?, 'user', ?)";
            System.out.println(insertNotification);
            preparedStmt = conn.prepareStatement(insertNotification);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);

            insertNotification = "insert into notification(project_id, external_ref_id, notification_type_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 4, 1, 'user', ?, 'user', ?)";
            System.out.println(insertNotification);
            preparedStmt = conn.prepareStatement(insertNotification);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);

            insertNotification = "insert into notification(project_id, external_ref_id, notification_type_id," +
                "create_user, create_date, modify_user, modify_date) values " + "(1, 5, 1, 'user', ?, 'user', ?)";
            System.out.println(insertNotification);
            preparedStmt = conn.prepareStatement(insertNotification);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into 'user'
            stmt = conn.createStatement();

            String sql = "insert into user(user_id, first_name, last_name, handle, status)" +
                "values (1, 'babut', 'guy', 'babut', 'ON')";
            System.out.println(sql);
            stmt.addBatch(sql);

            // insert into 'user_rating'
            sql = "insert into user_rating(user_id, phase_id) values (1, 112)";
            stmt.addBatch(sql);
            System.out.println(sql);

            // insert into email
            sql = "insert into email(user_id, email_id, address, primary_ind)" +
                " values (1, 1, 'topcoder_smtp@126.com', 1)";
            stmt.addBatch(sql);
            System.out.println(sql);

            sql = "insert into user(user_id, first_name, last_name, handle, status)" +
                "values (2, 'abc', 'xyz', 'wishingbone', 'ON')";
            System.out.println(sql);
            stmt.addBatch(sql);

            // insert into 'user_rating'
            sql = "insert into user_rating(user_id, phase_id) values (2, 112)";
            stmt.addBatch(sql);
            System.out.println(sql);

            // insert into email
            sql = "insert into email(user_id, email_id, address, primary_ind)" +
                " values (2, 2, 'topcoder_smtp@126.com', 1)";
            stmt.addBatch(sql);
            System.out.println(sql);

            sql = "insert into user(user_id, first_name, last_name, handle, status)" +
                "values (3, 'abc', 'xyz', 'developer', 'ON')";
            System.out.println(sql);
            stmt.addBatch(sql);

            // insert into 'user_rating'
            sql = "insert into user_rating(user_id, phase_id) values (3, 112)";
            stmt.addBatch(sql);
            System.out.println(sql);

            // insert into email
            sql = "insert into email(user_id, email_id, address, primary_ind)" +
                " values (3, 3, 'topcoder_smtp@126.com', 1)";
            stmt.addBatch(sql);
            System.out.println(sql);

            sql = "insert into user(user_id, first_name, last_name, handle, status)" +
                "values (4, 'Allen', 'Iverson', 'I3', 'ON')";
            System.out.println(sql);
            stmt.addBatch(sql);

            // insert into 'user_rating'
            sql = "insert into user_rating(user_id, phase_id) values (4, 112)";
            stmt.addBatch(sql);
            System.out.println(sql);

            // insert into email
            sql = "insert into email(user_id, email_id, address, primary_ind)" +
                " values (4, 4, 'iverns@topcoder.com', 1)";
            stmt.addBatch(sql);
            System.out.println(sql);

            sql = "insert into user(user_id, first_name, last_name, handle, status)" +
                "values (5, 'John', 'Lennon', 'lennon', 'ON')";
            System.out.println(sql);
            stmt.addBatch(sql);

            // insert into 'user_rating'
            sql = "insert into user_rating(user_id, phase_id) values (5, 112)";
            stmt.addBatch(sql);
            System.out.println(sql);

            // insert into email
            sql = "insert into email(user_id, email_id, address, primary_ind)" +
                " values (5, 5, 'iverns@topcoder.com', 1)";
            stmt.addBatch(sql);
            System.out.println(sql);
            stmt.executeBatch();

            //insert manager resource
            Resource manager = createResource(100000, null, 1, 13);
            Resource freviewer = createResource(100008, null, 1, 9);
            Resource reviewer = createResource(100001, null, 1, 4);
            Resource observer = createResource(100002, null, 1, 12);
            insertResources(conn, new Resource[] {manager, reviewer, freviewer, observer});

            //insert resource info
            insertResourceInfo(conn, manager.getId(), 1, "1");
            insertResourceInfo(conn, reviewer.getId(), 1, "2");
            insertResourceInfo(conn, freviewer.getId(), 1, "2");
            insertResourceInfo(conn, observer.getId(), 1, "1");

            return project;
        } finally {
            closeStatement(stmt);
            closeStatement(preparedStmt);
            closeConnection();
        }
    }

    /**
     * A helper method to insert a winning submitter for the given project id with given resource id.
     *
     * @param conn connection to use.
     * @param resourceId resource id.
     * @param resourceInfoTypeId resource info type id.
     * @param resourceInfo resource info value.
     *
     * @throws Exception not under test.
     */
    protected void insertResourceInfo(Connection conn, long resourceId, long resourceInfoTypeId, String resourceInfo)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertInfo = "insert into resource_info" + "(resource_id, resource_info_type_id, value," +
                "create_user, create_date, modify_user, modify_date) " + "VALUES (?, ?, ?, 'user', ?, 'user', ?)";
            System.out.println(insertInfo);
            preparedStmt = conn.prepareStatement(insertInfo);

            Timestamp now = new Timestamp(System.currentTimeMillis());
            preparedStmt.setLong(1, resourceId);
            preparedStmt.setLong(2, resourceInfoTypeId);
            preparedStmt.setString(3, resourceInfo);
            preparedStmt.setTimestamp(4, now);
            preparedStmt.setTimestamp(5, now);
            preparedStmt.executeUpdate();

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * inserts resources required by test cases into the db.
     *
     * @param resources resources to insert.
     * @param conn connection to use.
     *
     * @throws Exception not under test.
     */
    protected void insertResources(Connection conn, Resource[] resources)
        throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertResource = "INSERT INTO resource " +
                "(resource_id, resource_role_id, project_id, project_phase_id," +
                "create_user, create_date, modify_user, modify_date) " + "VALUES (?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertResource);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < resources.length; i++) {
                System.out.println(insertResource);
                preparedStmt.setLong(1, resources[i].getId());
                preparedStmt.setLong(2, resources[i].getResourceRole().getId());
                preparedStmt.setLong(3, resources[i].getProject().longValue());

                if (resources[i].getPhase() != null) {
                    preparedStmt.setLong(4, resources[i].getPhase());
                } else {
                    preparedStmt.setNull(4, java.sql.Types.INTEGER);
                }

                preparedStmt.setTimestamp(5, now);
                preparedStmt.setTimestamp(6, now);
                preparedStmt.executeUpdate();
            }

            closeStatement(preparedStmt);
            preparedStmt = null;
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Helper method to create Resource instance.
     *
     * @param resourceId resource Id.
     * @param phaseId phase Id.
     * @param projectId project Id.
     * @param resourceRoleId resource Role Id.
     *
     * @return Resource instance.
     */
    protected Resource createResource(long resourceId, Long phaseId, long projectId, long resourceRoleId) {
        Resource resource2 = new Resource();
        resource2.setId(resourceId);
        resource2.setPhase(phaseId);
        resource2.setProject(new Long(projectId));
        resource2.setResourceRole(new ResourceRole(resourceRoleId));

        return resource2;
    }
}
