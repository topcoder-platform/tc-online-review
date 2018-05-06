package com.cronos.onlinereview.phases.accuracytests;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>This class contains helper methods for accuracy tests.</p>
 *
 * @author tuenm
 * @version 1.0
 */
public class AccuracyTestHelper {
    /**
     * The configuration file for ManagerHelper
     */
    public static final String MANAGER_HELPER_CONFIG_FILE = "accuracy/ManagerHelperConfig.xml";

    /**
     * Represents the connection to the database.
     */
    private static Connection connection = null;

    /**
     * an array of table names to be cleaned in setup() and tearDown() methods.
     */
    private static final String[] TABLE_NAMES = new String[]{"screening_result", "screening_task", "notification",
                                                             "project_audit", "phase_dependency", "project_phase", "review_item_comment", "review_comment", "review_item",
                                                             "review", "resource_submission",
                                                             "submission", "upload", "resource_info", "resource", "phase_criteria",
                                                             "project_scorecard", "project_info", "project", "scorecard_question", "scorecard_section", "scorecard_group",
                                                             "scorecard"};

    /**
     * Create a phase instance with the input parameters.
     *
     * @param phaseId         phase id.
     * @param phaseStatusId   phase Status Id.
     * @param phaseStatusName phase Status Name.
     * @param phaseTypeId     phase Type Id.
     * @param phaseTypeName   phase Type Name.
     * @return phase instance.
     */
    public static Phase createPhase(long phaseId, long phaseStatusId, String phaseStatusName, long phaseTypeId, String phaseTypeName) {
        Project project = new Project(new Date(), new DefaultWorkdays());
        project.setId(1);
        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));

        return phase;
    }

    /**
     * <p/>
     * Load the necessary configuration files for testing.
     * </p>
     *
     * @throws Exception if any unexpected error occurred.
     */
    public static void loadTestConfiguration() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // load the configuration file of the managers
        configManager.add("accuracy/ProjectManagement.xml");
        configManager.add("accuracy/PhaseManagement.xml");
        configManager.add("accuracy/ScorecardManagement.xml");
        configManager.add("accuracy/ResourceUploadSearchBundleManager.xml");
        configManager.add("accuracy/AutoScreeningManagement.xml");
        configManager.add("accuracy/UserProjectDataStore.xml");
        configManager.add("accuracy/ReviewScorecardAggregator.xml");
        configManager.add("accuracy/ReviewManagement.xml");

        configManager.add("accuracy/SearchBuilderCommon.xml");
        configManager.add("accuracy/Phase_Handler.xml");
    }

    /**
     * <p/>
     * Clear all configuration namespaces.
     * </p>
     *
     * @throws Exception if any unexpected error occurred.
     */
    @SuppressWarnings("unchecked")
	public static void clearAllConfigNS() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>A helper method to be used to <code>nullify</code> the singleton instance. The method uses a <code>Java
     * Reflection API</code> to access the field and initialize the field with <code>null</code> value. The operation
     * may fail if a <code>SecurityManager</code> prohibits such sort of accessing.</p>
     *
     * @param clazz        a <code>Class</code> representing the class of the <code>Singleton</code> instance.
     * @param instanceName a <code>String</code> providing the name of the static field holding the reference to the
     *                     singleton instance.
     */
    @SuppressWarnings("unchecked")
	public static final void releaseSingletonInstance(Class clazz, String instanceName) throws Exception {
        try {
            Field instanceField = clazz.getDeclaredField(instanceName);
            boolean accessibility = instanceField.isAccessible();
            instanceField.setAccessible(true);

            if (Modifier.isStatic(instanceField.getModifiers())) {
                instanceField.set(null, null);
            } else {
                System.out.println("An error occurred while trying to release the singleton instance - the "
                        + " '" + instanceName + "' field is not static");
            }

            instanceField.setAccessible(accessibility);
        } catch (Exception e) {
            System.out.println("An error occurred while trying to release the singleton instance : " + e);
        }
    }


    /**
     * Inserts a project into the database for testing. Inserts records into the project table.
     *
     * @param conn connection to use.
     * @throws Exception if any error occurred.
     */
    public static void insertProject(Connection conn) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            //insert a project
            String insertProject = "insert into project(project_id, project_status_id, project_category_id,"
                    + "create_user, create_date, modify_user, modify_date) values "
                    + "(1, 1, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProject);
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
     * inserts a project and the standard phases into the database.
     *
     * @return project instance with phases populated.
     * @throws Exception if any error occurred.
     */
    public static Project setupPhases() throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        Project project = null;

        try {
            project = new Project(new Date(), new DefaultWorkdays());
            project.setId(1);

            //insert project first
            insertProject(conn);

            String insertPhase = "insert into project_phase(project_phase_id, project_id, phase_type_id, phase_status_id,"
                    + "scheduled_start_time, scheduled_end_time, duration,"
                    + " create_user, create_date, modify_user, modify_date)"
                    + "values (?, 1, ?, 1, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertPhase);

            //insert all standard phases
            long[] phaseIds = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
            long[] phaseTypeIds = new long[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
            String[] phaseTypeNames = new String[]{"Registration", "Submission", "Screening", "Review", "Appeals",
                                                   "Appeals Response", "Aggregation", "Aggregation Review",
                                                   "Final Fix", "Final Review", "Approval"};
            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now);
            long duration = 24 * 60 * 60 * 1000; //one day
            Timestamp scheduledEnd = new Timestamp(now + duration);

            for (int i = 0; i < phaseIds.length; i++) {
                //insert into db
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setTimestamp(3, scheduledStart);
                preparedStmt.setTimestamp(4, scheduledEnd);
                preparedStmt.setLong(5, duration);
                preparedStmt.setTimestamp(6, new Timestamp(now));
                preparedStmt.setTimestamp(7, new Timestamp(now));
                preparedStmt.executeUpdate();

                //create phase intance
                Phase phase = new Phase(project, duration);
                phase.setId(phaseIds[i]);
                phase.setPhaseType(new PhaseType(phaseTypeIds[i], phaseTypeNames[i]));
                phase.setPhaseStatus(PhaseStatus.SCHEDULED);
                phase.setActualStartDate(scheduledStart);
                phase.setActualEndDate(scheduledEnd);
                phase.setScheduledStartDate(scheduledStart);
                phase.setScheduledEndDate(scheduledEnd);

                project.addPhase(phase);
            }
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }

        return project;
    }

    /**
     * Add dependency.
     *
     * @param dependent the dependent string
     * @param dependency the dependency
     * @param project the project to be added
     * @param depClosed if the dependency is closed.
     * @return the added project
     * @throws Exception if any error occurs
     */
    public static Project addDependency(String dependent, String dependency, Project project, boolean depClosed) throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;
        try {
            Phase dependentPhase = getPhase(dependent, project.getAllPhases());
            Phase dependencyPhase = getPhase(dependency, project.getAllPhases());
            if (depClosed) {
                dependencyPhase.setPhaseStatus(new PhaseStatus(3, "Closed"));
            } else {
                dependencyPhase.setPhaseStatus(new PhaseStatus(2, "Open"));
            }

            //insert dependencies
            String insertDependency = "INSERT INTO phase_dependency "
                    + "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, lag_time,"
                    + " create_user, create_date, modify_user, modify_date)"
                    + "VALUES (?, ?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertDependency);

            long now = System.currentTimeMillis();

            preparedStmt.setLong(1, dependencyPhase.getId());
            preparedStmt.setLong(2, dependentPhase.getId());
            preparedStmt.setBoolean(3, false);
            preparedStmt.setBoolean(4, true);
            preparedStmt.setLong(5, 0);
            preparedStmt.setTimestamp(6, new Timestamp(now));
            preparedStmt.setTimestamp(7, new Timestamp(now));
            preparedStmt.executeUpdate();
            Dependency dep = new Dependency(dependencyPhase, dependentPhase, false, true, 0);
            dependentPhase.addDependency(dep);

            return project;
        } finally {
            closeStatement(preparedStmt);
            closeConnection();
        }
    }

    /**
     * Get phase.
     *
     * @param type the phase type
     * @param phases all phases
     * @return the phase got
     */
    public static Phase getPhase(String type, Phase[] phases) {
        for (int i = 0; i < phases.length; i++) {
            if (phases[i].getPhaseType().getName().equals(type)) {
                return phases[i];
            }
        }

        return null;
    }

    /**
     * Returns a connection instance.
     *
     * @return a connection instance.
     * @throws Exception not for this test case.
     */
    @SuppressWarnings("deprecation")
	public static Connection getConnection() throws Exception {
        DBConnectionFactory dbFactory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());

        if (connection == null) {
            connection = dbFactory.createConnection();
        }
        return connection;
    }

    /**
     * Closes the connection.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                //do nothing.
            }
        }
        connection = null;
    }

    /**
     * Helper method to close a statement.
     *
     * @param stmt statement to close.
     */
    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                // ignore
            }
        }
    }

    /**
     * Cleans up records in the given table names.
     *
     * @throws Exception if any error occurred.
     */
    public static void cleanTables() throws Exception {
        Connection conn = null;
        Statement stmt = null;
        try {
            conn = getConnection();
            stmt = conn.createStatement();
            for (int i = 0; i < TABLE_NAMES.length; i++) {
                stmt.addBatch("delete from " + TABLE_NAMES[i]);
            }

            stmt.executeBatch();
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }
}
