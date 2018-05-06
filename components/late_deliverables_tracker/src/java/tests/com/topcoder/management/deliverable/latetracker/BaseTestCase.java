/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * The base test case for Unit tests.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Updated code.</li>
 * </ol>
 * </p>
 *
 * @author myxgyy, sparemax
 * @version 1.3
 */
public abstract class BaseTestCase extends TestCase {
    /**
     * Constant for one day time.
     */
    public static final long DAY = 24 * 60 * 60 * 1000;

    /**
     * Represents the file contains statement to insert look up data.
     */
    private static final String INSERT_SQL = "/insert.sql";

    /**
     * Constant for database connection factory configuration file.
     */
    private static final String DB_FACTORY_CONFIG_FILE = "config/DB_Factory.xml";

    /**
     * Array of all the config file names for various dependency components.
     */
    private static final String[] COMPONENT_FILE_NAMES = new String[] {"config/Project_Management.xml",
        "config/Phase_Management.xml", "config/Upload_Resource_Search.xml", "config/InformixPhasePersistence.xml",
        "config/SearchBuilderCommon.xml", "config/SearchBundleManager.xml", "config/LateDeliverableManagerImpl.xml",
        "invalid_config/Project_Management.xml", "invalid_config/Phase_Management.xml",
        "invalid_config/Deliverable_Search.xml"};

    /**
     * An array of table names to be cleaned.
     */
    private static final String[] ALL_TABLE_NAMES = new String[] {"phase_dependency", "resource_info", "resource",
        "project_phase", "project_info", "project", "comp_versions", "comp_catalog", "user_reliability",
        "user_rating", "user", "email", "id_sequences", "project_category_lu", "project_type_lu",
        "project_status_lu", "project_info_type_lu", "deliverable_lu", "phase_status_lu",
        "phase_criteria_type_lu", "resource_role_lu", "resource_info_type_lu", "comment_type_lu",
        "phase_type_lu", "submission_type_lu", "late_deliverable", "late_deliverable_type_lu"};

    /**
     * Holds database connection factory instance.
     */
    private DBConnectionFactory dbFactory;

    /**
     * Holds database connection.
     */
    private Connection connection;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    @SuppressWarnings("deprecation")
    protected void setUp() throws Exception {
        addConfig();

        dbFactory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
        cleanTables();
        executeSqlFile(INSERT_SQL);
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     *
     * @throws Exception
     *             pass any unexpected exception to JUnit.
     */
    protected void tearDown() throws Exception {
        clearNamespace();

        cleanTables();
        closeConnection();
    }

    /**
     * Adds all required configuration to config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void addConfig() throws Exception {
        clearNamespace();

        ConfigManager configManager = ConfigManager.getInstance();

        // init db factory
        configManager.add(DB_FACTORY_CONFIG_FILE);

        // add all dependencies config
        for (String config : COMPONENT_FILE_NAMES) {
            configManager.add(config);
        }
    }

    /**
     * Clears all namespace under config manager.
     *
     * @throws Exception
     *             to JUnit.
     */
    public static void clearNamespace() throws Exception {
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator<?> iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            String ns = (String) iter.next();
            if (!"com.topcoder.util.log".equals(ns)) {
                configManager.removeNamespace(ns);
            }
        }
    }

    /**
     * <p>
     * Executes the sql file.
     * </p>
     *
     * @param file
     *            The sql file to be executed.
     * @throws Exception
     *             to JUnit
     */
    private void executeSqlFile(String file) throws Exception {
        String sql = getSql(file);

        Connection conn = null;
        Statement stmt = null;

        StringTokenizer st = new StringTokenizer(sql, ";");

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            for (int count = 1; st.hasMoreTokens(); count++) {
                String statement = st.nextToken().trim();

                if (statement.length() > 0) {
                    stmt.addBatch(statement);
                }
            }

            stmt.executeBatch();
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    /**
     * <p>
     * Sets value to a column of a table.
     * </p>
     *
     * @param table
     *            the table name.
     * @param column
     *            the column name.
     * @param value
     *            the value.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void setColumn(String table, String column, Object value) throws Exception {
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement("UPDATE  " + table + " SET " + column + "=?");
            stmt.setObject(1, value);

            stmt.executeUpdate();
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
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
    private static String getSql(String file) throws Exception {
        StringBuilder sql = new StringBuilder();
        InputStream is = BaseTestCase.class.getResourceAsStream(file);

        if (is == null) {
            throw new FileNotFoundException("Not found: " + file);
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        try {
            for (String s = in.readLine(); s != null; s = in.readLine()) {
                int commentIndex = s.indexOf("--");

                if (commentIndex >= 0) { // Remove any SQL comment
                    s = s.substring(0, commentIndex);
                }

                sql.append(s).append(' '); // The BufferedReader drops newlines so insert
                // whitespace
            }
        } finally {
            in.close();
        }

        return sql.toString();
    }

    /**
     * <p>
     * Sets the field value to bean instance.
     * </p>
     *
     * @param <T>
     *            The generic type.
     * @param clazz
     *            the class type.
     * @param instance
     *            the instance to be set.
     * @param fieldName
     *            The field name.
     * @param value
     *            The field value to set.
     * @throws Exception
     *             to JUnit.
     */
    protected static <T> void setField(Class<T> clazz, T instance, String fieldName, Object value)
        throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
        field.setAccessible(false);
    }

    /**
     * <p>
     * Gets value for field of given object.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     * @return the field value.
     * @throws Exception
     *             to JUnit.
     */
    public static Object getField(Object obj, String field) throws Exception {
        Object value = null;
        Field declaredField = obj.getClass().getDeclaredField(field);
        declaredField.setAccessible(true);

        value = declaredField.get(obj);
        declaredField.setAccessible(false);

        return value;
    }

    /**
     * Helper method to create a phase instance.
     *
     * @param phaseId
     *            phase id.
     * @param phaseStatusId
     *            phase Status Id.
     * @param phaseStatusName
     *            phase Status Name.
     * @param phaseTypeId
     *            phase Type Id.
     * @param phaseTypeName
     *            phase Type Name.
     * @return phase instance.
     */
    protected Phase createPhase(long phaseId, long phaseStatusId, String phaseStatusName,
        long phaseTypeId, String phaseTypeName) {
        Project project = new Project(new Date(), new DefaultWorkdays());
        project.setId(1);

        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));

        return phase;
    }

    /**
     * Helper method to create Resource instance.
     *
     * @param resourceId
     *            resource Id.
     * @param phaseId
     *            phase Id.
     * @param projectId
     *            project Id.
     * @param resourceRoleId
     *            resource Role Id.
     * @return Resource instance.
     */
    protected Resource createResource(long resourceId, Long phaseId, long projectId, long resourceRoleId) {
        Resource resource = new Resource();
        resource.setId(resourceId);
        resource.setPhase(phaseId);
        resource.setProject(new Long(projectId));
        resource.setResourceRole(new ResourceRole(resourceRoleId));

        return resource;
    }

    /**
     * Returns a connection instance.
     *
     * @return a connection instance.
     * @throws Exception
     *             not for this test case.
     */
    protected Connection getConnection() throws Exception {
        if (connection == null) {
            connection = dbFactory.createConnection();
        }

        return connection;
    }

    /**
     * Closes the connection.
     */
    protected void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                // do nothing.
            }
        }

        connection = null;
    }

    /**
     * helper method to close a statement.
     *
     * @param stmt
     *            statement to close.
     */
    protected static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ex) {
                // do nothing
            }
        }
    }

    /**
     * Inserts a project into the database. Inserts records into the project, comp_catalog
     * and comp_versions tables.
     *
     * @param conn
     *            connection to use.
     * @throws Exception
     *             to JUnit.
     */
    protected void insertProject(Connection conn) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project
            String insertProject = "insert into project(project_id, project_status_id, project_category_id,"
                + "create_user, create_date, modify_user, modify_date) values "
                + "(1, 1, 1, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProject);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertCatalog = "insert into comp_catalog(component_id, current_version, component_name,"
                + "description, create_time, status_id) values "
                + "(1, 1, 'Online Review Phases', 'Online Review Phases', ?, 1)";
            preparedStmt = conn.prepareStatement(insertCatalog);
            preparedStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            preparedStmt.executeUpdate();
            closeStatement(preparedStmt);
            preparedStmt = null;

            // insert into comp_catalog
            String insertVersion = "insert into comp_versions(comp_vers_id, component_id, version,version_text,"
                + "create_time, phase_id, phase_time, price, comments) values "
                + "(1, 1, 1, '1.0', ?, 112, ?, 500, 'Comments')";
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
     * Inserts project properties into the database. Inserts records into the project_info
     * table.
     *
     * @param conn
     *            connection to use.
     * @param projectId
     *            project id.
     * @param infoTypes
     *            array of project info type ids.
     * @param infoValues
     *            array of corresponding project info values.
     * @throws Exception
     *             not under test.
     */
    protected void insertProjectInfo(Connection conn, long projectId, long[] infoTypes,
        String[] infoValues) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            // insert a project info
            String insertProjectInfo = "insert into project_info(project_id, project_info_type_id, value,"
                + "create_user, create_date, modify_user, modify_date) values "
                + "(?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertProjectInfo);

            for (int i = 0; i < infoTypes.length; i++) {
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
     * Inserts resources required by test cases into the db.
     *
     * @param conn
     *            connection to use.
     * @param resources
     *            resources to insert.
     * @throws Exception
     *             not under test.
     */
    protected void insertResources(Connection conn, Resource[] resources) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertResource = "INSERT INTO resource "
                + "(resource_id, resource_role_id, project_id, project_phase_id,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, 'user', ?, 'user', ?)";
            preparedStmt = conn.prepareStatement(insertResource);

            Timestamp now = new Timestamp(System.currentTimeMillis());

            for (int i = 0; i < resources.length; i++) {
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
     * A helper method to insert a resource info.
     *
     * @param conn
     *            connection to use.
     * @param resourceId
     *            resource id.
     * @param resourceInfoTypeId
     *            resource info type id.
     * @param resourceInfo
     *            resource info value.
     * @throws Exception
     *             not under test.
     */
    protected void insertResourceInfo(Connection conn, long resourceId, long resourceInfoTypeId,
        String resourceInfo) throws Exception {
        PreparedStatement preparedStmt = null;

        try {
            String insertInfo = "insert into resource_info"
                + "(resource_id, resource_info_type_id, value,"
                + "create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, 'user', ?, 'user', ?)";
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
     * Cleans up records in the given table names.
     *
     * @throws Exception
     *             not under test.
     */
    protected void cleanTables() throws Exception {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            for (int i = 0; i < ALL_TABLE_NAMES.length; i++) {
                String sql = "delete from " + ALL_TABLE_NAMES[i];
                stmt.addBatch(sql);
            }

            stmt.executeBatch();
        } finally {
            closeStatement(stmt);
            closeConnection();
        }
    }

    /**
     * Creates the dependency.
     *
     * @param dependencyId
     *            the dependency phase id.
     * @param dependentId
     *            the dependent phase id.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    protected void createDependency(long dependencyId, long dependentId) throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;

        try {
            String insertSQL = "INSERT INTO phase_dependency "
                + "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, "
                + "lag_time, create_user, create_date, modify_user, modify_date) "
                + "VALUES (?, ?, ?, ?, ?, 'user', current, 'user', current)";

            preparedStmt = conn.prepareStatement(insertSQL);

            // insert into db
            preparedStmt.setLong(1, dependencyId);
            preparedStmt.setLong(2, dependentId);
            preparedStmt.setBoolean(3, true);
            preparedStmt.setBoolean(4, true);
            preparedStmt.setLong(5, 5);

            preparedStmt.executeUpdate();
        } finally {
            closeStatement(preparedStmt);
        }

    }

    /**
     * Inserts a project and the standard phases into the database.
     *
     * @param phaseIds
     *            the phase ids.
     * @param phaseTypeIds
     *            the phase type ids
     * @param phaseStatusIds
     *            the phase status ids.
     * @param late
     *            whether the phase is late or not.
     * @throws Exception
     *             to JUnit.
     */
    protected void setupPhases(long[] phaseIds, long[] phaseTypeIds, long[] phaseStatusIds, boolean late)
        throws Exception {
        Connection conn = getConnection();
        PreparedStatement preparedStmt = null;

        try {
            insertProject(conn);
            insertProjectInfo(conn, 1, new long[] {6, 7, 45}, new String[] {"Late Deliverable Tracker",
                "1.0.0", "true"});

            String insertPhase = "insert into project_phase(project_phase_id, project_id,"
                + " phase_type_id, phase_status_id,"
                + "scheduled_start_time, scheduled_end_time, duration,"
                + " create_user, create_date, modify_user, modify_date)"
                + "values (?, 1, ?, ?, ?, ?, ?, 'user', ?, 'user', ?)";

            preparedStmt = conn.prepareStatement(insertPhase);

            long now = System.currentTimeMillis();
            Timestamp scheduledStart = new Timestamp(now - (DAY * 3));
            Timestamp scheduledEnd = new Timestamp(late ? (now - (DAY * 2)) : (now + DAY * 2));

            for (int i = 0; i < phaseIds.length; i++) {
                // insert into db
                preparedStmt.setLong(1, phaseIds[i]);
                preparedStmt.setLong(2, phaseTypeIds[i]);
                preparedStmt.setLong(3, phaseStatusIds[i]);
                preparedStmt.setTimestamp(4, scheduledStart);
                preparedStmt.setTimestamp(5, scheduledEnd);
                preparedStmt.setLong(6, DAY);
                preparedStmt.setTimestamp(7, scheduledStart);
                preparedStmt.setTimestamp(8, scheduledEnd);
                preparedStmt.executeUpdate();
            }

            Resource[] resources = getResources(phaseIds, phaseTypeIds);

            insertResources(conn, resources);

            for (int i = 0; i < resources.length; i++) {
                insertResourceInfo(conn, resources[i].getId(), 1, "1");
            }

            insertUsers(conn);
        } finally {
            closeStatement(preparedStmt);
        }
    }

    /**
     * Creates the resources.
     *
     * @param phaseIds
     *            the phase ids.
     * @param phaseTypeIds
     *            the phase type ids.
     *
     * @return the resources.
     *
     * @since 1.3
     */
    private Resource[] getResources(long[] phaseIds, long[] phaseTypeIds) {
        Resource[] resources = new Resource[phaseIds.length];

        for (int i = 0; i < phaseIds.length; i++) {
            long phaseTypeId = phaseTypeIds[i];
            long resourceRoleId;
            if (phaseTypeId == 9) {
                resourceRoleId = 1;
            } else if (phaseTypeId == 10) {
                resourceRoleId = 9;
            } else {
                resourceRoleId = phaseTypeId;
            }
            resources[i] = createResource(i + 1, phaseIds[i], 1, resourceRoleId);
        }

        return resources;
    }

    /**
     * Inserts external user required by test cases into the db.
     *
     * @param connection
     *            the database connection.
     * @throws Exception
     *             to JUnit.
     */
    private void insertUsers(Connection connection) throws Exception {
        Statement stmt = null;

        try {
            stmt = connection.createStatement();

            stmt.addBatch("insert into user(user_id, first_name, last_name, handle, status)"
                + "values (1, 'abc', 'xyz', 'wishingbone', 'ON')");

            // insert into 'user_rating'
            stmt.addBatch("insert into user_rating(user_id, phase_id) values (1, 112)");

            // insert into email
            stmt.addBatch("insert into email(user_id, email_id, address, primary_ind)"
                + " values (1, 1, 'wishingbone@topcoder.com', 1)");

            stmt.executeBatch();
        } finally {
            closeStatement(stmt);
        }
    }

    /**
     * Gets the configuration object from the given file with the given namespace.
     *
     * @param file
     *            the configuration file.
     * @param namespace
     *            the namespace.
     * @return the configuration object.
     * @throws Exception
     *             to JUnit.
     */
    public static ConfigurationObject getConfigurationObject(String file, String namespace)
        throws Exception {
        ConfigurationFileManager manager = new ConfigurationFileManager();

        manager.loadFile("root", file);

        ConfigurationObject config = manager.getConfiguration("root");

        return config.getChild(namespace);
    }

    /**
     * Gets all records from late deliverable table.
     *
     * @return all records in late deliverable table.
     * @throws Exception
     *             to JUnit.
     */
    protected List<LateDeliverableData> getLateDeliverable() throws Exception {
        List<LateDeliverableData> ret = new ArrayList<LateDeliverableData>();

        PreparedStatement statement = null;
        ResultSet rs = null;
        Connection conn = getConnection();

        try {
            statement = conn.prepareStatement("select project_phase_id, resource_id, deliverable_id,"
                + " deadline, create_date, forgive_ind, last_notified from late_deliverable");
            rs = statement.executeQuery();

            while (rs.next()) {
                LateDeliverableData data = new LateDeliverableData();
                data.setProjectPhaseId(rs.getLong(1));
                data.setResourceId(rs.getLong(2));
                data.setDeliverableId(rs.getLong(3));
                data.setDeadline(rs.getTimestamp(4));
                data.setCreateDate(rs.getTimestamp(5));
                data.setForgive(rs.getBoolean(6));
                data.setLastNotified(rs.getTimestamp(7));
                ret.add(data);
            }

            return ret;
        } finally {
            if (rs != null) {
                rs.close();
            }

            closeStatement(statement);
        }
    }

    /**
     * Creates <code>LateDeliverable</code> instance for tests.
     *
     * @param flag
     *            the flag to set the required value.
     * @return the <code>LateDeliverable</code> instance.
     */
    protected static LateDeliverable createLateDeliverable(int flag) {
        LateDeliverable d = new LateDeliverable();
        Deliverable deliverable = new Deliverable(1, 112, 1000, null, false);
        deliverable.setId(4);

        Phase phase = new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 100);
        phase.setScheduledEndDate(new Date());

        com.topcoder.management.project.Project project = new com.topcoder.management.project.Project(1,
            new ProjectCategory(1, "Dev", new ProjectType(1, "type")), new ProjectStatus(2, "Active"));

        project.setProperty("Project Name", "name");
        project.setProperty("Project Version", "version");
        deliverable.setName("Screening scorecard");
        phase.setPhaseType(new PhaseType(1, "review"));

        if (flag == 1) {
            project.setProperty("Project Name", null);
        } else if (flag == 2) {
            project.setProperty("Project Version", null);
        } else if (flag == 3) {
            deliverable.setName(null);
        } else if (flag == 4) {
            project.setProperty("Project Name", new Exception());
        } else if (flag == 5) {
            phase.setPhaseType(null);
        }

        d.setDeliverable(deliverable);
        d.setPhase(phase);
        d.setProject(project);
        d.setType(LateDeliverableType.MISSED_DEADLINE);

        return d;
    }

    /**
     * Runs the main method with given arguments.
     *
     * @param args
     *            the arguments.
     * @throws Exception to JUnit.
     */
    protected void runMain(String[] args) throws Exception {
        final PipedOutputStream out = new PipedOutputStream();
        InputStream in = new PipedInputStream(out);
        InputStream org = System.in;
        System.setIn(in);

        try {
            class MainThread extends Thread {
                /**
                 * <p>
                 * The arguments.
                 * </p>
                 */
                private String[] args;

                /**
                 * <p>
                 * Creates an instance of MainThread.
                 * </p>
                 *
                 * @param args
                 *            the arguments.
                 */
                public MainThread(String[] args) {
                    this.args = args;
                }

                /**
                 * Runs this thread. It will sleep 2 minutes then write a char to stop the job.
                 */
                @Override
                public void run() {
                    LateDeliverablesTrackingUtility.main(args);

                    try {
                        // sleep to make sure the job run
                        Thread.sleep(90 * 1000);
                    } catch (InterruptedException e) {
                        // ignore
                    }

                    // write a char to stop
                    try {
                        out.write(1);
                    } catch (IOException e) {
                        // ignore
                    }
                }
            }

            Thread t = new MainThread(args);
            t.start();

            Thread.sleep(1000);
        } finally {
            // set back
            System.setIn(org);
        }
    }
}