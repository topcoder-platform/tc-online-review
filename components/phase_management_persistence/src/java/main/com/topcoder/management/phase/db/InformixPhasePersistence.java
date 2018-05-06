/*
 * Copyright (c) 2006-2013, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.management.phase.db;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.phase.ConfigurationException;
import com.topcoder.management.phase.PhasePersistence;
import com.topcoder.management.phase.PhasePersistenceException;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

/**
 * <p>
 * This is an actual implementation of database persistence that is geared for Informix database.
 * </p>
 *
 * <p>
 * All the methods uses the transactions to ensure database integrity. If any error will happen in the batch
 * operations, whole transaction will be replaced. Because the batch operation may involve lot of queries, it is
 * important to properly configure the database to avoid too long transaction error.
 * </p>
 *
 * <p>
 * Auditing Operator audit is based on simply filling in the create_user, create_date, modify_user, and modify_date
 * field for each create and update operation on any of the provided tables. When creating an entry the above
 * columns will be fill out with actual data.
 *
 * When updating, only the update user and update timestamp will be set.
 * </p>
 *
 * <p>
 * Thread Safety InformixPhasePersistence acts like a stateless bean with utility-like functionality where function
 * calls retain no state from one call to the next. Separate connections are created each time a call is made and
 * thus (assuming the connections are different) there is no contention for a connection from competing threads.
 * This class is thread-safe.
 * </p>
 *
 * <p>
 *   Version 1.0.3 (Online Review Miscellaneous Improvements ) change notes:
 *     <ol>
 *       <li>Remove <code>AUDIT_DELETE_TYPEM</code> field.</li>
 *       <li>Updated {@link #deletePhase(Phase)} method to fix the bug of phase audit. Phase audit can't exist
 *       for the phases that has been deleted because of the foreign key constraints.</li>
 *     </ol>   
 * </p>
 *
 * <p>
 *   Version 1.0.4 (Online Review - Project Payments Integration Part 1 v1.0) change notes:
 *     <ol>
 *       <li>Updated {@link #SELECT_PHASE_FOR_PROJECTS} to populate phase modify date.</li>
 *       <li>Updated {@link #createPhasesImpl(Connection, Phase[], String, Map)},
 *       {@link #populatePhase(ResultSet, Project)}, {@link #updatePhases(Phase[], String)} methods to set the
 *       phase modify timestamp.</li>
 *     </ol>
 * </p>
 *
 * @author AleaActaEst
 * @author kr00tki
 * @author flexme
 * @version 1.0.4
 */
public class InformixPhasePersistence implements PhasePersistence {

    /**
     * Checks if the dependency exists in the database.
     */
    private static final String CHECK_DEPENDENCY = "SELECT 1 FROM phase_dependency WHERE dependency_phase_id = ? "
            + "AND dependent_phase_id = ?";

    /**
     * The second part of the delete query for the dependencies.
     */
    private static final String DELETE_FROM_PHASE_DEPENDENCY_OR = " OR dependent_phase_id IN ";

    /**
     * Delete the dependencies for particular phase.
     */
    private static final String DELETE_FROM_PHASE_DEPENDENCY = "DELETE FROM phase_dependency WHERE "
            + "dependency_phase_id IN ";

    /**
     * Delete the phase criteria for the given phases.
     */
    private static final String DELETE_PHASE_CRITERIA_FOR_PHASES = "DELETE FROM phase_criteria "
            + "WHERE project_phase_id IN ";

    /**
     * Delete the project phases given by id list.
     */
    private static final String DELETE_PROJECT_PHASE = "DELETE FROM project_phase WHERE project_phase_id IN ";

    /**
     * Select the complete phase criteria for a phases.
     */
    private static final String SELECT_PHASE_CRITERIA_FOR_PHASE = "SELECT phase_criteria.phase_criteria_type_id, "
            + "name, parameter FROM phase_criteria JOIN phase_criteria_type_lu "
            + "ON phase_criteria_type_lu.phase_criteria_type_id = phase_criteria.phase_criteria_type_id "
            + "WHERE project_phase_id = ?";

    /**
     * Update the phase criteria.
     */
    private static final String UPDATE_PHASE_CRITERIA = "UPDATE phase_criteria SET parameter = ?, "
            + "modify_user = ?, modify_date = ? WHERE project_phase_id = ? AND phase_criteria_type_id = ?";

    /**
     * Delete the criteria for phase.
     */
    private static final String DELETE_PHASE_CRITERIA = "DELETE FROM phase_criteria "
            + "WHERE project_phase_id = ? AND phase_criteria_type_id IN ";

    /**
     * Deletes the concrete dependecies for a phase.
     */
    private static final String DELETE_PHASE_DEPENDENCY = "DELETE FROM phase_dependency "
            + "WHERE dependent_phase_id = ? AND dependency_phase_id IN ";

    /**
     * Updates the phases dependencies.
     */
    private static final String UPDATE_PHASE_DEPENDENCY = "UPDATE phase_dependency "
            + "SET dependency_start = ?, dependent_start = ?, lag_time = ?, modify_user = ?, modify_date = ? "
            + "WHERE dependency_phase_id = ? AND dependent_phase_id = ?";

    /**
     * Selects the phase criteria for phases.
     */
    private static final String SELECT_PHASE_CRITERIA_FOR_PROJECTS = "SELECT phase_criteria.project_phase_id, name, parameter "
            + "FROM phase_criteria JOIN phase_criteria_type_lu "
            + "ON phase_criteria_type_lu.phase_criteria_type_id = phase_criteria.phase_criteria_type_id "
            + "JOIN project_phase ON phase_criteria.project_phase_id = project_phase.project_phase_id "
            + "WHERE project_id IN ";

    /**
     * Select the project id - phase id mappings.
     */
    private static final String SELECT_PROJECT_PHASE_ID = "SELECT project_phase_id, project_id FROM project_phase "
            + "WHERE project_phase_id IN ";

    /**
     * Select all the criteria id and name.
     */
    private static final String SELECT_PHASE_CRITERIA = "SELECT phase_criteria_type_id, name "
            + "FROM phase_criteria_type_lu";

    /**
     * Inserts the phase criteria into table.
     */
    private static final String INSERT_PHASE_CRITERIA = "INSERT INTO phase_criteria(project_phase_id, "
            + "phase_criteria_type_id, parameter, create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * Updates the phase table.
     */
    private static final String UPDATE_PHASE = "UPDATE project_phase SET project_id = ?, phase_type_id = ?, "
            + "phase_status_id = ?, fixed_start_time = ?, scheduled_start_time = ?, scheduled_end_time = ?, "
            + "actual_start_time = ?, actual_end_time = ?, duration = ?, modify_user = ?, modify_date = ? "
            + "WHERE project_phase_id = ?";

    /**
     * Selects all depedencies for phase.
     */
    private static final String SELECT_DEPENDENCY = "SELECT dependency_phase_id, dependent_phase_id, "
        + "dependency_start, dependent_start, lag_time FROM phase_dependency WHERE dependent_phase_id = ?";

    /**
     * Selects all depedencies for projects.
     */
    private static final String SELECT_DEPENDENCY_FOR_PROJECTS = "SELECT dependency_phase_id, dependent_phase_id, "
            + "dependency_start, dependent_start, lag_time FROM phase_dependency "
            + "JOIN project_phase ON dependent_phase_id = project_phase_id "
            + "WHERE project_id IN ";

    /**
     * Selects phase data.
     */
    private static final String SELECT_PHASE_FOR_PROJECTS = "SELECT project_phase_id, project_id, fixed_start_time, "
            + "scheduled_start_time, scheduled_end_time, actual_start_time, actual_end_time, duration, "
            + "project_phase.modify_date, "
            + "phase_type_lu.phase_type_id, phase_type_lu.name phase_type_name, "
            + "phase_status_lu.phase_status_id, phase_status_lu.name phase_status_name "
            + "FROM project_phase JOIN phase_type_lu ON phase_type_lu.phase_type_id = project_phase.phase_type_id "
            + "JOIN phase_status_lu ON phase_status_lu.phase_status_id = "
            + "project_phase.phase_status_id WHERE project_id IN ";

    /**
     * Inserts data into phase depedencies.
     */
    private static final String INSERT_PHASE_DEPENDENCY = "INSERT INTO phase_dependency "
            + "(dependency_phase_id, dependent_phase_id, dependency_start, dependent_start, "
            + "lag_time, create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ";

    /**
     * Inserts the phase.
     */
    private static final String INSERT_PHASE = "INSERT INTO project_phase (project_phase_id, project_id, "
            + "phase_type_id, " + "phase_status_id, fixed_start_time, scheduled_start_time, scheduled_end_time, "
            + "actual_start_time, actual_end_time, duration, create_user, create_date, modify_user, modify_date) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Selects all phase statuses.
     */
    private static final String SELECT_PHASE_STATUS_IDS = "SELECT phase_status_id, name phase_status_name "
            + "FROM phase_status_lu";

    /**
     * Selects all phases types.
     */
    private static final String SELECT_PHASE_TYPES = "SELECT phase_type_id, name phase_type_name FROM phase_type_lu";

    /**
     * Selects all projects - checks if all exists in database.
     */
    private static final String SELECT_PROJECT_IDS = "SELECT project_id FROM project WHERE project_id IN ";
    
    /**
     * <p>
     * Represents the audit creation type.
     * </p>
     *
     * @since 1.0.2
     */
    private static final int AUDIT_CREATE_TYPE = 1;
    
    /**
     * <p>
     * Represents the audit update type.
     * </p>
     *
     * @since 1.0.2
     */
    private static final int AUDIT_UPDATE_TYPE = 3;
    
    /**
     * Represents the SQL statement to audit project info.
     * 
     * @since 1.0.2
     */
    private static final String PROJECT_PHASE_AUDIT_INSERT_SQL = "INSERT INTO project_phase_audit "
    	+ "(project_phase_id, scheduled_start_time, scheduled_end_time, audit_action_type_id, action_date, action_user_id) "
    	+ "VALUES (?, ?, ?, ?, ?, ?)";

    /**
     * Represents the SQL statement to delete the audio info.
     * 
     * @since 1.0.3
     */
    private static final String PROJECT_PHASE_AUDIT_DELETE_SQL = "DELETE FROM project_phase_audit WHERE project_phase_id IN ";

    /**
     * DBConnectionFactory constructor parameters.
     */
    private static final Class[] CTOR_PARAMS = new Class[] {String.class};

    /**
     * <p>
     * This represents the connection factory from which we will obtain a pre-configured connection for our data
     * base access. This is initialized in one of the constructors and once initialized cannot be changed. Cannot
     * be null.
     * <p>
     *
     */
    private final DBConnectionFactory connectionFactory;

    /**
     * <p>
     * This represents a connection name (an alias) that is used to fetch a connection instance from the connection
     * factory. This is initialized in one of the constructors and once initialized cannot be changed. Can be null
     * or an empty string, upon which it will try to use the default connection.
     * </p>
     *
     */
    private final String connectionName;

    /**
     * This IdGenerator will be used to generate the new ids in the create and update methods. It is initialized in
     * the constructor and never changed after that.
     */
    private final IDGenerator idGenenerator;

    /**
     * <p>
     * An simple constructor which will populate the connectionFactory and connectionName information from
     * configuration. It will also read the IDGenerator sequence name and implementation class (optional).
     * </p>
     *
     * @param namespace config namespace.
     * @throws IllegalArgumentException if namespace is an empty string or a null.
     * @throws ConfigurationException if any of the required configuration parameters are missing or are incorrect.
     */
    public InformixPhasePersistence(String namespace) throws ConfigurationException {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace cannot be null.");
        }

        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("namespace cannot be empty String.");
        }

        String className = getProperty(namespace, "ConnectionFactory.className", true);
        String ns = getProperty(namespace, "ConnectionFactory.namespace", true);
        connectionName = getProperty(namespace, "connectionName", false);
        connectionFactory = createConnectionFactory(className, ns);
        // create the idgenerator
        className = getProperty(namespace, "Idgenerator.className", false);
        String sequence = getProperty(namespace, "Idgenerator.sequenceName", true);
        idGenenerator = createIdGenerator(sequence, className);
    }

    /**
     * Creates the IDGenerator instance using the sequence name and the generator class.
     *
     * @param sequence the name of the id sequence.
     * @param className the name of the generator class.
     * @return the created IDGenerator.
     * @throws ConfigurationException if error occurs while creating the IdGenerator.
     */
    private static IDGenerator createIdGenerator(String sequence, String className) throws ConfigurationException {
        try {
            if (className == null) {
                return IDGeneratorFactory.getIDGenerator(sequence);
            }
            return IDGeneratorFactory.getIDGenerator(sequence, className);
        } catch (IDGenerationException ex) {
            throw new ConfigurationException("Error occurs while creation IdGenerator.", ex);
        } catch (ClassNotFoundException ex) {
            throw new ConfigurationException("No generator class.", ex);
        } catch (NoSuchMethodException ex) {
            throw new ConfigurationException("Missing public constructor.", ex);
        } catch (InstantiationException ex) {
            throw new ConfigurationException("Error occurs while creation IdGenerator.", ex);
        } catch (IllegalAccessException ex) {
            throw new ConfigurationException("Error occurs while creation IdGenerator.", ex);
        } catch (InvocationTargetException ex) {
            throw new ConfigurationException("Error occurs while creation IdGenerator.", ex);
        }
    }

    /**
     * Creates the database connection factory instance.
     *
     * @param className the class name of the factory.
     * @param ns the factory namespace.
     *
     * @return the DbConnectionFactory instance.
     * @throws ConfigurationException if any error occurs.
     */
    private static DBConnectionFactory createConnectionFactory(String className, String ns)
        throws ConfigurationException {

        try {
            Class cl = Class.forName(className);
            if (!DBConnectionFactory.class.isAssignableFrom(cl)) {
                throw new ConfigurationException("The class is not DbConnectionFactory.");
            }
            return (DBConnectionFactory) cl.getConstructor(CTOR_PARAMS).newInstance(new Object[] {ns});
        } catch (ConfigurationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ConfigurationException("Error occurs while creating the factory.", ex);
        }
    }

    /**
     * Returns the configuration property from ConfigManager.
     *
     * @param namespace the configuration namespace.
     * @param name the name of the property.
     * @param required flag indicating if the property is required.
     * @return the configuration property.
     * @throws ConfigurationException if any error was thrown from ConfigManager, or there is no require property,
     *         or the property has empty value.
     */
    private static String getProperty(String namespace, String name, boolean required)
        throws ConfigurationException {

        ConfigManager cm = ConfigManager.getInstance();
        try {
            String value = cm.getString(namespace, name);
            if (required && (value == null)) {
                throw new ConfigurationException("Missing required property: " + name + " in namespace: "
                        + namespace);
            }

            // check if the value is empty
            if ((value != null) && (value.trim().length() == 0)) {
                throw new ConfigurationException("Empty value for property: " + name + " in namespace: "
                        + namespace);
            }

            return value;
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("Miising config namespace: " + namespace, ex);
        }
    }

    /**
     * <p>
     * A simple constructor which will populate the connectionFactory and connectionName information from input
     * parameters.
     * </p>
     *
     * @param connectionFactory connection factory instance
     * @param connectionName connection name.
     * @param idGen the IdGenerator that will be used to create the new ids for entities.
     * @throws IllegalArgumentException if the connectionFactory is <code>null</code> or the connectionName is
     *         empty String.
     */
    public InformixPhasePersistence(DBConnectionFactory connectionFactory, String connectionName, IDGenerator idGen) {
        if (connectionFactory == null) {
            throw new IllegalArgumentException("connectionFactory cannot be null.");
        }

        if ((connectionName != null) && (connectionName.trim().length() == 0)) {
            throw new IllegalArgumentException("The connectionName cannot be empty.");
        }

        if (idGen == null) {
            throw new IllegalArgumentException("idGen cannot be null.");
        }

        this.connectionFactory = connectionFactory;
        this.connectionName = connectionName;
        this.idGenenerator = idGen;
    }

    /**
     * <p>
     * Will return project instance for the given id. If the project can not be found then a null is returned. The
     * project will have all the depedencies filled out. If the project exists in the database, but has no phases,
     * the empty Project instance will be returned.
     * </p>
     *
     * @param projectId project id to find by.
     * @return Project for this id.
     * @throws PhasePersistenceException if any database error happen.
     */
    public Project getProjectPhases(long projectId) throws PhasePersistenceException {
        return getProjectPhases(new long[] {projectId})[0];
    }

    /**
     * <p>
     * Will return an array of project instances for the given input array of project ids. If the project can not
     * be found then a null is returned in the return array. Returns are positional thus id at index 2 of input is
     * represented at index 2 of output. The project will have all the depedencies filled out. If the project
     * exists in the database, but has no phases, the empty Project instance will be returned.
     * </p>
     *
     * @param projectIds an array of project ids
     * @return and array of Projects for the ids
     *
     * @throws IllegalArgumentException if the input array is null.
     * @throws PhasePersistenceException if an database error occurs.
     */
    public Project[] getProjectPhases(long[] projectIds) throws PhasePersistenceException {
        if (projectIds == null) {
            throw new IllegalArgumentException("projectIds cannot be null.");
        }

        // empty array empty result
        if (projectIds.length == 0) {
            return new Project[0];
        }

        Connection conn = createConnection(false);

        try {
            return getProjectPhasesImpl(conn, projectIds);
        } catch (SQLException ex) {
            throw new PhasePersistenceException("Error occurs while retrieving the projects.", ex);
        } finally {
            close(conn);
        }
    }

    /**
     * This the current implementation of the {@link #getProjectPhases(long[])} method. It will first check which
     * project for the given ids exists and which have no phases. The it selects all the phases for the projects,
     * create them and return. If any project from the givenlist not exists, null will be returned in that place.
     *
     * @param conn the database connection to use.
     * @param projectIds the ids of the projects to retrieve.
     * @return the array of projects with one to one mapping between project and it id.
     *
     * @throws SQLException if any database error occurs.
     * @throws PhasePersistenceException if other error happen.
     */
    private Project[] getProjectPhasesImpl(Connection conn, long[] projectIds) throws SQLException,
        PhasePersistenceException {
        // if no ids - return empty array
        if (projectIds.length == 0) {
            return new Project[0];
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // create workdays to be used to create the project
        Workdays workdays = new DefaultWorkdaysFactory().createWorkdaysInstance();

        try {
            pstmt = conn.prepareStatement(SELECT_PROJECT_IDS + createQuestionMarks(projectIds.length));
            for (int i = 0; i < projectIds.length; i++) {
                pstmt.setLong(i + 1, projectIds[i]);
            }

            rs = pstmt.executeQuery();

            Map projectsMap = new HashMap();

            // create all the projects that exists and store them in helper map
            while (rs.next()) {
                long projectId = rs.getLong(1);
                Project project = new Project(new Date(Long.MAX_VALUE), workdays);
                project.setId(projectId);
                projectsMap.put(new Long(projectId), project);
            }

            // closes resources
            close(rs);
            close(pstmt);

            Map phasesMap = new HashMap();

            // prepare the query to retrieve the phases .
            pstmt = conn.prepareStatement(SELECT_PHASE_FOR_PROJECTS + createQuestionMarks(projectIds.length));
            for (int i = 0; i < projectIds.length; i++) {
                pstmt.setLong(i + 1, projectIds[i]);
            }

            rs = pstmt.executeQuery();

            // for each phase in the response create the Phase object and add it to the internal list
            while (rs.next()) {
                long projectId = rs.getLong("project_id");

                Project project = (Project) projectsMap.get(new Long(projectId));

                Phase phase = populatePhase(rs, project);
                phasesMap.put(new Long(phase.getId()), phase);
            }

            // fill the phases depedencies and criteria for them
            if (phasesMap.size() > 0) {
                fillDependencies(conn, phasesMap, projectIds);
                fillCriteria(conn, phasesMap, projectIds);
            }

            // this comparator is used to get the lowest start date
            Comparator phasesComparator = new PhaseStartDateComparator();
            // set the correct date for each project
            for (Iterator it = projectsMap.values().iterator(); it.hasNext();) {
                Project project = (Project) it.next();
                Phase[] phases = project.getAllPhases(phasesComparator);
                // if project has any phases - get the first one
                if (phases.length > 0) {
                    project.setStartDate(phases[0].getScheduledStartDate());
                }
            }

            // create the result array
            Project[] result = new Project[projectIds.length];
            for (int i = 0; i < projectIds.length; i++) {
                result[i] = (Project) projectsMap.get(new Long(projectIds[i]));
            }

            return result;
        } finally {
            close(rs);
            close(pstmt);
        }
    }

    /**
     * <p>
     * Returns all the PhaseTypes from the datastore. The returned array might be empty if no types exists, but it
     * won't be null.
     * </p>
     *
     * @return all available phase types stored in the database.
     * @throws PhasePersistenceException if any error occurs while accessing the database or creating PhaseTypes.
     */
    public PhaseType[] getAllPhaseTypes() throws PhasePersistenceException {
        Connection conn = createConnection(true);
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // create statement
            stmt = conn.createStatement();
            // select all phase types
            rs = stmt.executeQuery(SELECT_PHASE_TYPES);

            // create the types
            List result = new ArrayList();
            while (rs.next()) {
                result.add(populatePhaseType(rs));
            }

            // convert list to array and return
            return (PhaseType[]) result.toArray(new PhaseType[result.size()]);
        } catch (SQLException ex) {
            throw new PhasePersistenceException("Error occurs while retrieving phase types.", ex);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

    }

    /**
     * Creates the PhaseType instance using the data from the ResultSet.
     *
     * @param rs the source ResultSet for PhaseType object.
     * @return the PhaseType instance created from the ResultSet row.
     * @throws SQLException if errors occurs while accessing the ResultSet.
     */
    private static PhaseType populatePhaseType(ResultSet rs) throws SQLException {
        long id = rs.getLong("phase_type_id");
        String name = rs.getString("phase_type_name");
        return new PhaseType(id, name);
    }

    /**
     * Creates the database connection. If the <code>connectionName</code> is <code>null</code>, the default
     * connecton will be returned; otherwise the connection configured for the name will be returned.
     *
     * @param autoCommit indicates the state of the <code>autoCommit</code> flag for the connection.
     * @return the database connection.
     * @throws PhasePersistenceException if error occurs while creating the connection.
     */
    private Connection createConnection(boolean autoCommit) throws PhasePersistenceException {
        try {
            Connection conn = null;
            if (connectionName == null) {
                conn = connectionFactory.createConnection();
            } else {
                conn = connectionFactory.createConnection(connectionName);
            }

            conn.setAutoCommit(autoCommit);
            return conn;
        } catch (DBConnectionException ex) {
            throw new PhasePersistenceException("Error occurs while creating connection.", ex);
        } catch (SQLException ex) {
            throw new PhasePersistenceException("Error occurs while creating connection.", ex);
        }
    }

    /**
     * <p>
     * Returns all the PhaseStatuses from the datastore. The returned array might be empty if no statuses exists,
     * but it won't be null.
     * </p>
     *
     *
     * @return all available phase statuses stored in the database.
     * @throws PhasePersistenceException if any error occurs while accessing the database or creating PhaseStatus.
     */
    public PhaseStatus[] getAllPhaseStatuses() throws PhasePersistenceException {
        Connection conn = createConnection(true);
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // create the statement
            stmt = conn.createStatement();
            // select all the statuses
            rs = stmt.executeQuery(SELECT_PHASE_STATUS_IDS);

            // create the statuses
            List result = new ArrayList();
            while (rs.next()) {
                result.add(populatePhaseStatus(rs));
            }

            // convert result to array and return
            return (PhaseStatus[]) result.toArray(new PhaseStatus[result.size()]);
        } catch (SQLException ex) {
            throw new PhasePersistenceException("Error occurs while database operation.", ex);
        } finally {
            close(rs);
            close(stmt);
            close(conn);
        }

    }

    /**
     * Creates the PhaseStatus instance using the values from ResultSet.
     *
     * @param rs the source result set for the PhaseStatus object.
     * @return the PhaseStatus created from the ResultSet.
     *
     * @throws SQLException if error occurs while accessing the ResultSet.
     */
    private static PhaseStatus populatePhaseStatus(ResultSet rs) throws SQLException {
        long id = rs.getLong("phase_status_id");
        String name = rs.getString("phase_status_name");
        return new PhaseStatus(id, name);
    }

    /**
     * <p>
     * Creates the provided phase in persistence. All the phase depedencies will be also created. The phase should
     * have the unique id already generated and set.
     * </p>
     *
     * @param phase phase to be created.
     * @param operator the creation operator for audit proposes.
     * @throws IllegalArgumentException if phase or operator is null or if operator is am empty string.
     * @throws PhasePersistenceException if database relates error occurs.
     */
    public void createPhase(Phase phase, String operator) throws PhasePersistenceException {
        if (phase == null) {
            throw new IllegalArgumentException("phase cannot be null.");
        }

        createPhases(new Phase[] {phase}, operator);
    }

    /**
     * <p>
     * Creates the provided phase in persistence. All the phases depedencies will be also created. Each phase
     * should have the unique id already generated and set.
     * </p>
     *
     * @param phases an array of phases to create in persistence
     * @param operator the creation operator for audit proposes.
     * @throws IllegalArgumentException if phase or operator is null or if operator is an empty string or the array
     *         contains null value.
     * @throws PhasePersistenceException if database relates error occurs.
     */
    public void createPhases(Phase[] phases, String operator) throws PhasePersistenceException {
        checkPhases(phases);
        if (operator == null) {
            throw new IllegalArgumentException("operator cannot be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("operator cannot be empty String.");
        }

        if (phases.length == 0) {
            return;
        }

        PreparedStatement pstmt = null;
        // create transactioned connection
        Connection conn = createConnection(false);

        try {
            Map lookUp = getCriteriaTypes(conn);
            createPhasesImpl(conn, phases, operator, lookUp);
            // if all ok - commit transaction
            conn.commit();
        } catch (SQLException ex) {
            rollback(conn);
            throw new PhasePersistenceException("Error occurs while creating the phases.", ex);
        } catch (PhasePersistenceException ex) {
            rollback(conn);
            throw ex;
        } finally {
            close(pstmt);
            close(conn);
        }
    }

    /**
     * This is the internal implementation of the {@link #createPhase(Phase, String)} method. It creates the given
     * array of the Phases in the persistence.
     *
     * @param conn the connection to be used.
     * @param phases the phases array to be created.
     * @param operator the creation audit operator.
     * @param lookUp the lookup map for the criteria name - id.
     *
     * @throws SQLException if any database error occurs.
     * @throws PhasePersistenceException if other error occurs.
     */
    private void createPhasesImpl(Connection conn, Phase[] phases, String operator, Map lookUp)
        throws SQLException, PhasePersistenceException {
        // the list of phases depedencies
        List depedencies = new ArrayList();
        PreparedStatement pstmt = null;

        try {

            // create insert statement
            pstmt = conn.prepareStatement(INSERT_PHASE);
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // set the date and operator - this values are constant for all phases
            pstmt.setString(11, operator);
            pstmt.setTimestamp(12, now);
            pstmt.setString(13, operator);
            pstmt.setTimestamp(14, now);

            // iterate over the phases array
            for (int i = 0; i < phases.length; i++) {
                // generate the new id for phase
                phases[i].setId(nextId());
                phases[i].setModifyDate(now);
                // add all phase depedencies to the list
                depedencies.addAll(Arrays.asList(phases[i].getAllDependencies()));

                // set the phase data to statement
                pstmt.setLong(1, phases[i].getId());
                pstmt.setLong(2, phases[i].getProject().getId());
                pstmt.setLong(3, phases[i].getPhaseType().getId());
                pstmt.setLong(4, phases[i].getPhaseStatus().getId());
                insertValueOrNull(pstmt, 5, phases[i].getFixedStartDate());
                Timestamp scheduledStartTime = new Timestamp(phases[i].getScheduledStartDate().getTime());
                pstmt.setTimestamp(6, scheduledStartTime);
                Timestamp scheduledEndTime = new Timestamp(phases[i].getScheduledEndDate().getTime());
                pstmt.setTimestamp(7, scheduledEndTime);
                insertValueOrNull(pstmt, 8, phases[i].getActualStartDate());
                insertValueOrNull(pstmt, 9, phases[i].getActualEndDate());
                pstmt.setLong(10, phases[i].getLength());

                // insert the phase
                pstmt.executeUpdate();
                
                auditProjectPhase(conn, phases[i], AUDIT_CREATE_TYPE, scheduledStartTime, scheduledEndTime,
                		Long.parseLong(operator), now);
                
                // create the criteria for phase
                createPhaseCriteria(conn, phases[i], filterAttributes(phases[i].getAttributes()), operator, lookUp);
            }

            // create the depedencies for phases
            createDependency(conn, depedencies, operator);
        } finally {
            close(pstmt);
        }
    }

    /**
     * Returns the new unique ID.
     *
     * @return the unique ID.
     * @throws PhasePersistenceException if error occurs while generating the id.
     */
    private long nextId() throws PhasePersistenceException {
        try {
            return idGenenerator.getNextID();
        } catch (IDGenerationException ex) {
            throw new PhasePersistenceException("Error occurs while generating new ids.", ex);
        }
    }

    /**
     * Returns all the criteria types for lookup proposes. This will speed up the creation process.
     *
     * @param conn the connection to use.
     * @return a map where the key is the criteria name and the value is the criteria type id.
     *
     * @throws SQLException if any database error occurs.
     */
    private static Map getCriteriaTypes(Connection conn) throws SQLException {
        Map result = new HashMap();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(SELECT_PHASE_CRITERIA);
            while (rs.next()) {
                result.put(rs.getString("name"), new Long(rs.getLong("phase_criteria_type_id")));
            }

        } finally {
            close(rs);
            close(stmt);
        }

        return result;
    }

    /**
     * Creates the phase criteria in the database. It uses helper lookup map to reduce database call for all the
     * criteria types.
     *
     * @param conn the database connection to use.
     * @param phase the phase to which the criteria belongs.
     * @param attribs the phases criteria and parameters. The key is the name of criteria, the vale is the
     *        parameter.
     * @param operator the operator for audit proposes.
     * @param lookUp the lookup values for the criteria types.
     *
     * @throws SQLException thrown if any database error occurs.
     */
    private void createPhaseCriteria(Connection conn, Phase phase, Map attribs, String operator, Map lookUp)
        throws SQLException {

        // no work to do, return
        if (attribs.size() == 0) {
            return;
        }

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // create the query
            pstmt = conn.prepareStatement(INSERT_PHASE_CRITERIA);
            Timestamp time = new Timestamp(System.currentTimeMillis());

            // set the value that do not change
            pstmt.setLong(1, phase.getId());
            pstmt.setString(4, operator);
            pstmt.setTimestamp(5, time);
            pstmt.setString(6, operator);
            pstmt.setTimestamp(7, time);

            // iterate over all attributes and persist only those who are the criteria
            for (Iterator it = attribs.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                Long id = (Long) lookUp.get(entry.getKey());
                // if such criteria exists - add it
                if (id != null) {
                    pstmt.setLong(2, id.longValue());
                    pstmt.setString(3, (String) entry.getValue());
                    pstmt.executeUpdate();
                }
            }

        } finally {
            close(pstmt);
            close(rs);
        }

    }

    /**
     * This helper method removes all attributes that have non-String hey.
     *
     * @param attributes the attributes to be filtered.
     *
     * @return the filtered attributes map.
     */
    private static Map filterAttributes(Map attributes) {
        // make the copy because the input attributes are not modifiable
        Map attribs = new HashMap(attributes);
        for (Iterator it = attribs.entrySet().iterator(); it.hasNext();) {
            Map.Entry el = (Map.Entry) it.next();
            if (!(el.getKey() instanceof String)) {
                it.remove();
            }
        }
        return attribs;
    }

    /**
     * Inserts date into PreparedStatement. If the date is null, null will be set according to JDBC specification.
     *
     * @param pstmt the statement where value will be set.
     * @param idx the position is statement where values need to be set.
     * @param date the value to be set.
     * @throws SQLException if error occurs while setting the date.
     */
    private static void insertValueOrNull(PreparedStatement pstmt, int idx, Date date) throws SQLException {
        if (date == null) {
            pstmt.setNull(idx, Types.DATE);
        } else {
            pstmt.setTimestamp(idx, new Timestamp(date.getTime()));
        }
    }

    /**
     * Creates the given list of depedencies in the database. All the phases must already be persisted.
     *
     * @param conn the database connection to be used.
     * @param depedencies the phases depedencies to.
     * @param operator the creation operator for audit proposes.
     *
     * @throws SQLException if any database error happens.
     */
    private void createDependency(Connection conn, Collection depedencies, String operator) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            // create the statemnt
            pstmt = conn.prepareStatement(INSERT_PHASE_DEPENDENCY);
            Timestamp time = new Timestamp(System.currentTimeMillis());
            // set the operator and timestamps
            pstmt.setString(6, operator);
            pstmt.setTimestamp(7, time);
            pstmt.setString(8, operator);
            pstmt.setTimestamp(9, time);

            // iterate over the list of depedencies
            for (Iterator iter = depedencies.iterator(); iter.hasNext();) {
                Dependency dependency = (Dependency) iter.next();

                // set the depedencies values.
                pstmt.setLong(1, dependency.getDependency().getId());
                pstmt.setLong(2, dependency.getDependent().getId());
                pstmt.setBoolean(3, dependency.isDependencyStart());
                pstmt.setBoolean(4, dependency.isDependentStart());
                pstmt.setLong(5, dependency.getLagTime());

                // create the depedency
                pstmt.executeUpdate();
            }
        } finally {
            close(pstmt);
        }

    }

    /**
     * <p>
     * Reads a specific phase from the data store. If the phase with given id doesn't exist, <code>null</code>
     * value will be returned. The returned phase will have all its depedencies set.
     * </p>
     *
     * @param phaseId id of phase to fetch
     * @return the Phase object with given id, or <code>null</code> if not found.
     * @throws PhasePersistenceException if database related error occurs.
     */
    public Phase getPhase(long phaseId) throws PhasePersistenceException {
        return getPhases(new long[] {phaseId})[0];
    }

    /**
     * <p>
     * Batch version of the {@link #getPhase(long) getPhase} method. For each entry in the input array at index i
     * of the Phase is not found then we return a null in the corresponding index in the output array. All the
     * phases dependencies will be satisfied.
     * </p>
     *
     * @param phaseIds an array of phase ids to fetch phases with
     * @return a non-null array of Phases.
     * @throws PhasePersistenceException if any database related error occurs.
     * @throws IllegalArgumentException if phaseIds array is null.
     */
    public Phase[] getPhases(long[] phaseIds) throws PhasePersistenceException {
        if (phaseIds == null) {
            throw new IllegalArgumentException("phaseIds cannot be null.");
        }

        if (phaseIds.length == 0) {
            return new Phase[0];
        }
        Connection conn = createConnection(true);

        try {
            return getPhasesImpl(conn, phaseIds);
        } catch (SQLException ex) {
            throw new PhasePersistenceException("Error occurs while retrieving phases.", ex);
        } finally {
            close(conn);
        }
    }

    /**
     * This is the internal version of the {@link #checkPhases(Phase[])} method. It retrieves the project ids for
     * those phases and that delegates to the {@link #getProjectPhasesImpl(Connection, long[])} method. Than it
     * gets the phases fom appropriate projects and returns them.
     *
     * @param conn the database connection.
     * @param phaseIds the phases ids that need to be retrieved.
     * @param projects this map will hold the project instances. Key - project id, value - project instance.
     * @return the Phase array.
     *
     * @throws SQLException if any database error occurs.
     * @throws PhasePersistenceException if the phase depedencies cannot be met.
     */
    private Phase[] getPhasesImpl(Connection conn, long[] phaseIds) throws SQLException, PhasePersistenceException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // this map will hold current phases. the key is phase id, value - phase instance
        Map phases = new HashMap();

        try {
            // get all the phase - project mapping from the database - just in case the actual value were outdated
            pstmt = conn.prepareStatement(SELECT_PROJECT_PHASE_ID + createQuestionMarks(phaseIds.length));
            for (int i = 0; i < phaseIds.length; i++) {
                pstmt.setLong(i + 1, phaseIds[i]);
            }

            // execute the query
            rs = pstmt.executeQuery();
            List projectIds = new ArrayList();

            // create the phase - project mapping
            while (rs.next()) {
                Long projectId = new Long(rs.getLong("project_id"));
                Long phaseId = new Long(rs.getLong("project_phase_id"));

                phases.put(phaseId, projectId);
                if (!projectIds.contains(projectId)) {
                    projectIds.add(projectId);
                }
            }

            // get the projects
            Project[] projects = getProjectPhasesImpl(conn, listToArray(projectIds));
            // for each phase in the phases map
            for (Iterator it = new HashSet(phases.entrySet()).iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                // get the phase id
                long phaseId = ((Long) entry.getKey()).longValue();

                // get the phases from project for that phase id.
                Phase[] phasesTemp = projects[projectIds.indexOf(entry.getValue())].getAllPhases();
                // find the phase object
                for (int i = 0; i < phasesTemp.length; i++) {
                    if (phasesTemp[i].getId() == phaseId) {
                        phases.put(entry.getKey(), phasesTemp[i]);
                        break;
                    }
                }
            }

        } finally {
            close(rs);
            close(pstmt);
        }

        // prepare the result array - if any phase not exists - set the result for it to null
        Phase[] result = new Phase[phaseIds.length];
        for (int i = 0; i < phaseIds.length; i++) {
            result[i] = (Phase) phases.get(new Long(phaseIds[i]));
        }

        return result;

    }

    /**
     * This method set the phase criteria into the phases from the given map.
     *
     * @param conn the database connection to be used.
     * @param phases the Phases to which criteria will ba add. Key should be Long phase id, value - Phase object.
     * @param projectIds all the project ids.
     *
     * @throws SQLException if any database error occurs.
     */
    private void fillCriteria(Connection conn, Map phases, long[] projectIds) throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // create the statement
            pstmt = conn.prepareStatement(SELECT_PHASE_CRITERIA_FOR_PROJECTS + createQuestionMarks(projectIds.length));

            // set the id to the statement
            for (int i = 0; i < projectIds.length; ++i) {
                pstmt.setLong(i + 1, projectIds[i]);
            }

            // execute query
            rs = pstmt.executeQuery();

            // create the phase criteria
            while (rs.next()) {
                // get the phase id
                Long id = new Long(rs.getLong("project_phase_id"));
                // get criteria name and parameter
                String name = rs.getString("name");
                String parameter = rs.getString("parameter");

                // get the phase and add criteria
                Phase phase = (Phase) phases.get(id);
                phase.setAttribute(name, parameter);
            }
        } finally {
            close(rs);
            close(pstmt);
        }
    }

    /**
     * This method selects all the depedencies for phases.
     *
     * @param conn the database connection.
     * @param phases the map of already retrieved phases.
     * @param projectIds all the project ids.
     *
     * @throws SQLException if database error occures.
     * @throws PhasePersistenceException if the phase depedencies cannot be filled.
     */
    private void fillDependencies(Connection conn, Map phases, long[] projectIds) throws SQLException,
        PhasePersistenceException {
        // get the phase
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // create the statement
            pstmt = conn.prepareStatement(SELECT_DEPENDENCY_FOR_PROJECTS + createQuestionMarks(projectIds.length));

            // set the id to the statement
            for (int i = 0; i < projectIds.length; ++i) {
                pstmt.setLong(i + 1, projectIds[i]);
            }

            // execte the query
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // get the depedency
                Long dependentId = new Long(rs.getLong("dependent_phase_id"));
                Long dependencyId = new Long(rs.getLong("dependency_phase_id"));
                // if the phase exists - create dependecy
                if (phases.containsKey(dependentId) && phases.containsKey(dependencyId)) {
                    Phase phase = (Phase) phases.get(dependentId);
                    Dependency dependency = createDependency(rs, phases, phase);
                    phase.addDependency(dependency);
                } else {
                    // because we have retrieved all the phases for project before, this should never happen
                    throw new PhasePersistenceException("Missing dependecy: " + dependencyId
                            + " for phase: " + dependentId);
                }
            }

        } finally {
            close(rs);
            close(pstmt);
        }
    }

    /**
     * Create the Dependency instance from given result set.
     *
     * @param rs the source result set.
     * @param phases the retrieved phases.
     * @param dependantPhase the dependant phase.
     * @return the Dependency instance.
     *
     * @throws SQLException if database error occures.
     */
    private static Dependency createDependency(ResultSet rs, Map phases, Phase dependantPhase) throws SQLException {
        Phase dependencyPhase = (Phase) phases.get(new Long(rs.getLong("dependency_phase_id")));
        long lagTime = rs.getLong("lag_time");

        return new Dependency(dependencyPhase, dependantPhase, rs.getBoolean("dependency_start"), rs
                .getBoolean("dependent_start"), lagTime);
    }

    /**
     * Creates the Phase instance from the given ResultSet.
     *
     * @param rs the source result set.
     * @param project the project for phase.
     * @return the Phase instance.
     * @throws SQLException if database error occurs.
     */
    private static Phase populatePhase(ResultSet rs, Project project) throws SQLException {
        long duration = rs.getLong("duration");
        Phase phase = new Phase(project, duration);
        phase.setActualEndDate(rs.getTimestamp("actual_end_time"));
        phase.setActualStartDate(rs.getTimestamp("actual_start_time"));
        phase.setFixedStartDate(rs.getTimestamp("fixed_start_time"));
        phase.setId(rs.getLong("project_phase_id"));

        phase.setPhaseStatus(populatePhaseStatus(rs));
        phase.setPhaseType(populatePhaseType(rs));
        phase.setScheduledEndDate(rs.getTimestamp("scheduled_end_time"));
        phase.setScheduledStartDate(rs.getTimestamp("scheduled_start_time"));
        phase.setModifyDate(rs.getTimestamp("modify_date"));

        return phase;
    }

    /**
     * <p>
     * Update the provided phase in persistence. All the phases depedencies will be updated as well.
     * </p>
     *
     * @param phase update the phase in persistence
     * @param operator operator.
     * @throws PhasePersistenceException if any database related error occurs.
     * @throws IllegalArgumentException if phase or operator is null or the the operator is empty string.
     */
    public void updatePhase(Phase phase, String operator) throws PhasePersistenceException {
        if (phase == null) {
            throw new IllegalArgumentException("phase cannot be null.");
        }

        updatePhases(new Phase[] {phase}, operator);
    }

    /**
     * <p>
     * Update the provided phases in persistence. All the phases depedencies will be updated as well.
     * If any of the phases is not in the database, it will be create with the new id.
     * </p>
     *
     *
     * @param phases an array of phases to update.
     * @param operator audit operator.
     *
     * @throws PhasePersistenceException if any database related error occurs.
     * @throws IllegalArgumentException if phaseIds array is null or operator is null or the the operator is empty
     *         string.
     */
    public void updatePhases(Phase[] phases, String operator) throws PhasePersistenceException {
        checkPhases(phases);
        if (operator == null) {
            throw new IllegalArgumentException("operator cannot be null.");
        }

        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("operator cannot be empty String.");
        }

        // if not phases to update - just return
        if (phases.length == 0) {
            return;
        }
        
        // Retrieve the original phases to check for modifications.
        long[] phaseIds = new long[phases.length];
        for (int i = 0; i < phases.length; ++i) {
        	phaseIds[i] = phases[i].getId();
        }
        Phase[] oldPhases = getPhases(phaseIds);
        Map<Long, Phase> oldPhasesMap = new HashMap<Long, Phase>();
        for (Phase p : oldPhases) {
        	oldPhasesMap.put(p.getId(), p);
        }

        Connection conn = createConnection(false);
        PreparedStatement pstmt = null;
        // it will contain the new phases that should be created
        List toCreate = new ArrayList();

        try {
            // get the phases criteria lookups
            Map lookUps = getCriteriaTypes(conn);
            // create the statement
            pstmt = conn.prepareStatement(UPDATE_PHASE);

            // set audit values
            pstmt.setString(10, operator);
            Timestamp updateTime = new Timestamp(System.currentTimeMillis());
            pstmt.setTimestamp(11, updateTime);

            // iterate over all phases
            for (int i = 0; i < phases.length; i++) {
                phases[i].setModifyDate(updateTime);
                // check if is a new phase - if so add it to list
                if (isNewPhase(phases[i])) {
                    toCreate.add(phases[i]);
                } else {
                    // set the update values
                    pstmt.setLong(1, phases[i].getProject().getId());
                    pstmt.setLong(2, phases[i].getPhaseType().getId());
                    pstmt.setLong(3, phases[i].getPhaseStatus().getId());
                    insertValueOrNull(pstmt, 4, phases[i].getFixedStartDate());
                    Timestamp scheduledStartTime = new Timestamp(phases[i].getScheduledStartDate().getTime());
                    pstmt.setTimestamp(5, scheduledStartTime);
                    Timestamp scheduledEndTime = new Timestamp(phases[i].getScheduledEndDate().getTime());
                    pstmt.setTimestamp(6, scheduledEndTime);

                    insertValueOrNull(pstmt, 7, phases[i].getActualStartDate());
                    insertValueOrNull(pstmt, 8, phases[i].getActualEndDate());
                    pstmt.setLong(9, phases[i].getLength());
                    pstmt.setLong(12, phases[i].getId());

                    // if phase not exists add it to the new list
                    if (pstmt.executeUpdate() == 0) {
                        toCreate.add(phases[i]);
                    } else {
                        // if phase exists - update the criteria and dependencies
                        updatePhaseCriteria(conn, phases[i], operator, lookUps);
                        updateDependencies(conn, phases[i], operator);
                        
                        Phase oldPhase = oldPhasesMap.get(phases[i].getId());
                        Timestamp auditScheduledStartTime =
                        	oldPhase.getScheduledStartDate().equals(scheduledStartTime) ? null : scheduledStartTime;
                        Timestamp auditScheduledEndTime =
                        	oldPhase.getScheduledEndDate().equals(scheduledEndTime) ? null : scheduledEndTime;
                        
                        // only audit if one of the scheduled times changed
                        if (auditScheduledStartTime != null || auditScheduledEndTime != null) {
                        	auditProjectPhase(conn, phases[i], AUDIT_UPDATE_TYPE, auditScheduledStartTime,
                        			auditScheduledEndTime, Long.parseLong(operator), updateTime);
                        }
                    }
                }
            }

            // create the new phases
            createPhasesImpl(conn, (Phase[]) toCreate.toArray(new Phase[toCreate.size()]), operator, lookUps);

            conn.commit();
        } catch (SQLException ex) {
            rollback(conn);
            throw new PhasePersistenceException("Error occurs while updating phases.", ex);
        } finally {
            close(pstmt);
            close(conn);
        }
    }

    /**
     * This method will update the phase depedencies in the persistence. Because the table has new audit columns,
     * we cannot just remove depedencies and create the again.
     *
     * @param conn the database connection.
     * @param phase the phase with depedencies to update.
     * @param operator the update operator for audit proposes.
     *
     * @throws SQLException if any database error occurs.
     */
    private void updateDependencies(Connection conn, Phase phase, String operator) throws SQLException {
        // put all the depedencies to the map: key: dependecy id, value: Dependency object.
        Map depedencies = new HashMap();
        Dependency[] deps = phase.getAllDependencies();
        for (int i = 0; i < deps.length; i++) {
            depedencies.put(new Long(deps[i].getDependency().getId()), deps[i]);
        }

        PreparedStatement selectStatement = null;
        ResultSet rs = null;

        // this list will keep dependecies to remove and to update
        List depsToRemove = new ArrayList();
        List depsToUpdate = new ArrayList();
        try {
            // select dependecies for the phase
            selectStatement = conn.prepareStatement(SELECT_DEPENDENCY);
            selectStatement.setLong(1, phase.getId());

            rs = selectStatement.executeQuery();
            // for each row
            while (rs.next()) {
                // get the dependency phase id
                Long dependencyId = new Long(rs.getLong("dependency_phase_id"));
                // check if such dependency exists
                if (depedencies.containsKey(dependencyId)) {
                    // if yes
                    // check to update (we don't need another db call of the row not need to be updated.
                    Dependency dep = (Dependency) depedencies.get(dependencyId);
                    // check the value
                    if ((dep.isDependencyStart() != rs.getBoolean("dependency_start"))
                            || (dep.isDependentStart() != rs.getBoolean("dependent_start"))
                            || (dep.getLagTime() != rs.getLong("lag_time"))) {

                        // if any is different - update the dependency
                        depsToUpdate.add(dep);
                    }
                    // remove the dependecy from map
                    depedencies.remove(dependencyId);
                } else {
                    // if not exists, delete the dependency from database
                    depsToRemove.add(dependencyId);
                }
            }

            // if still some dependecies left - this means they are new - create them
            if (depedencies.size() > 0) {
                // create new deps
                createDependency(conn, depedencies.values(), operator);
            }

            // if there is something to remove
            if (depsToRemove.size() > 0) {
                deleteDependencies(conn, depsToRemove, phase.getId());
            }

            // if there is something to update
            if (depsToUpdate.size() > 0) {
                updateDependencies(conn, depsToUpdate, operator);
            }

        } finally {
            close(rs);
            close(selectStatement);
        }
    }

    /**
     * Updates the dependecies in the database.
     *
     * @param conn the database connection to use.
     * @param deps the dependencies list.
     * @param operator the update operator for autid proposes.
     *
     * @throws SQLException if any database error occurs.
     */
    private static void updateDependencies(Connection conn, List deps, String operator) throws SQLException {
        PreparedStatement pstmt = null;

        try {
            // create the statement
            pstmt = conn.prepareStatement(UPDATE_PHASE_DEPENDENCY);

            pstmt.setString(4, operator);
            pstmt.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

            // for eachd ependency - update it in the datastore
            for (Iterator it = deps.iterator(); it.hasNext();) {
                Dependency dep = (Dependency) it.next();

                pstmt.setBoolean(1, dep.isDependencyStart());
                pstmt.setBoolean(2, dep.isDependentStart());
                pstmt.setLong(3, dep.getLagTime());
                pstmt.setLong(6, dep.getDependency().getId());
                pstmt.setLong(7, dep.getDependent().getId());

                pstmt.executeUpdate();
            }

        } finally {
            close(pstmt);
        }
    }

    /**
     * Deletes the dependecies rom database.
     *
     * @param conn the connection to use.
     * @param ids the ids of the dependecies.
     * @param dependantId the dependant phase id.
     *
     * @throws SQLException if any database error occurs.
     */
    private static void deleteDependencies(Connection conn, List ids, long dependantId) throws SQLException {
        PreparedStatement pstmt = null;
        try {
            // create the statement
            pstmt = conn.prepareStatement(DELETE_PHASE_DEPENDENCY + createQuestionMarks(ids.size()));
            pstmt.setLong(1, dependantId);
            for (int i = 0; i < ids.size(); i++) {
                pstmt.setLong(i + 2, ((Long) ids.get(i)).longValue());
            }

            // execute update
            pstmt.executeUpdate();

        } finally {
            close(pstmt);
        }
    }

    /**
     * Updates the phase criteria for the given phase. It will create new ones, modify existsing and delete the
     * old.
     *
     * @param conn the database connection to be used.
     * @param phase the phase to update.
     * @param operator the modification operator for audit proposes.
     * @param lookUp the lookup value for the criteria type ids.
     *
     * @throws SQLException if any database error occurs.
     */
    private void updatePhaseCriteria(Connection conn, Phase phase, String operator, Map lookUp)
        throws SQLException {

        PreparedStatement selectStatement = null;
        PreparedStatement updateStatement = null;
        PreparedStatement deleteStatement = null;
        ResultSet rs = null;

        // get all the properties that only string key - they are potential criteria
        Map newCriteria = filterAttributes(phase.getAttributes());
        // the map for the old criteria
        Map oldCriteria = new HashMap();

        try {
            // current update time
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // get all criteria for phase from the persistence
            selectStatement = conn.prepareStatement(SELECT_PHASE_CRITERIA_FOR_PHASE);
            selectStatement.setLong(1, phase.getId());

            rs = selectStatement.executeQuery();

            // put the old criteria to map
            while (rs.next()) {
                String name = rs.getString("name");
                String value = rs.getString("parameter");

                oldCriteria.put(name, value);
            }

            // create the update statement for all criteria
            updateStatement = conn.prepareStatement(UPDATE_PHASE_CRITERIA);
            updateStatement.setString(2, operator);
            updateStatement.setTimestamp(3, now);
            updateStatement.setLong(4, phase.getId());

            // iterate over the new attributes taken from the updated phase
            for (Iterator it = newCriteria.entrySet().iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                // check if the value from new set is in the one from persistence
                String oldValue = (String) oldCriteria.remove(entry.getKey());
                if ((oldValue != null) && !oldValue.equals(entry.getValue())) {
                    // update if the values are different - update the criteria
                    updateStatement.setString(1, (String) entry.getValue());
                    updateStatement.setLong(5, ((Long) lookUp.get(entry.getKey())).longValue());
                    updateStatement.executeUpdate();
                }
                if (oldValue != null) {
                    // remove the criteria from list
                    it.remove();
                }
            }

            // if left any new values - create them
            if (newCriteria.size() > 0) {
                createPhaseCriteria(conn, phase, newCriteria, operator, lookUp);
            }

            // if any value left in old criteria - they need to be removed.
            if (oldCriteria.size() > 0) {
                deleteStatement = conn.prepareStatement(DELETE_PHASE_CRITERIA
                        + createQuestionMarks(oldCriteria.size()));
                deleteStatement.setLong(1, phase.getId());

                int i = 1;
                for (Iterator it = oldCriteria.keySet().iterator(); it.hasNext();) {
                    String name = (String) it.next();
                    deleteStatement.setLong(i++, ((Long) lookUp.get(name)).longValue());
                }

                deleteStatement.executeUpdate();
            }

        } finally {
            close(rs);
            close(selectStatement);
            close(deleteStatement);
            close(updateStatement);
        }

    }

    /**
     * <p>
     * Deletes the provided phase from the persistence. All phase depedencies will be removed as well. If phase is
     * not in the persistence, nothing will happen.
     * </p>
     *
     * @param phase phase to delete.
     * @throws PhasePersistenceException if database error occurs.
     * @throws IllegalArgumentException if phase is null.
     */
    public void deletePhase(Phase phase) throws PhasePersistenceException {
        if (phase == null) {
            throw new IllegalArgumentException("phase cannot be null.");
        }

        deletePhases(new Phase[] {phase});
    }

    /**
     * <p>
     * Deletes the provided phases from the persistence. All phases depedencies and phase audit records
     *  will be removed as well. If any phase is not in the persistence, nothing will happen.
     * </p>
     *
     * @param phases an array of phases to delete
     * @throws PhasePersistenceException if database error occurs.
     * @throws IllegalArgumentException if array is null or contains null.
     */
    public void deletePhases(Phase[] phases) throws PhasePersistenceException {
        checkPhases(phases);
        // if no phases to delete - just return
        if (phases.length == 0) {
            return;
        }

        String inSet = createQuestionMarks(phases.length);
        Connection conn = createConnection(false);
        PreparedStatement pstmt = null;
        PreparedStatement pstmt2 = null;
        PreparedStatement pstmt3 = null;
        PreparedStatement pstmt4 = null;

        try {
            // create the staments for all 3 tables (phase, dependecies and criteria)
            pstmt = conn.prepareStatement(DELETE_FROM_PHASE_DEPENDENCY + inSet + DELETE_FROM_PHASE_DEPENDENCY_OR
                    + inSet);
            pstmt2 = conn.prepareStatement(DELETE_PROJECT_PHASE + inSet);
            pstmt3 = conn.prepareStatement(DELETE_PHASE_CRITERIA_FOR_PHASES + inSet);
            pstmt4 = conn.prepareStatement(PROJECT_PHASE_AUDIT_DELETE_SQL + inSet);

            // set the id values
            for (int i = 0; i < phases.length; i++) {
                pstmt.setLong(i + 1, phases[i].getId());
                pstmt.setLong(i + 1 + phases.length, phases[i].getId());
                pstmt2.setLong(i + 1, phases[i].getId());
                pstmt3.setLong(i + 1, phases[i].getId());
                pstmt4.setLong(i + 1, phases[i].getId());
            }

            // delete depedencies
            pstmt.executeUpdate();
            // delete criteria
            pstmt3.executeUpdate();
            // delete audit
            pstmt4.executeUpdate();
            // delete phases
            pstmt2.executeUpdate();
            
            conn.commit();
        } catch (SQLException ex) {
            rollback(conn);
            throw new PhasePersistenceException("Error occurs while deleting phases.", ex);
        } finally {
            close(pstmt);
            close(pstmt2);
            close(pstmt3);
            close(conn);
        }

    }

    /**
     * Checks the phases array.
     *
     * @param phases the phases array.
     * @throws IllegalArgumentException if phases is null or contains null.
     */
    private static void checkPhases(Phase[] phases) {
        if (phases == null) {
            throw new IllegalArgumentException("phases cannot be null.");
        }

        for (int i = 0; i < phases.length; i++) {
            if (phases[i] == null) {
                throw new IllegalArgumentException("The phases array contains null element at index: " + i);
            }
        }
    }

    /**
     * <p>
     * Tests if the input phase is a new phase (i.e. needs its id to be set).
     * </p>
     *
     * @param phase Phase object to tests if it is new
     * @return true if the applied test woked; true otherwise.
     * @throws IllegalArgumentException if dependency is null.
     */
    public boolean isNewPhase(Phase phase) {
        if (phase == null) {
            throw new IllegalArgumentException("phase cannot be null.");
        }

        return phase.getId() == 0;
    }

    /**
     * <p>
     * Tests if the input dependency is a new depoendency (i.e. needs its id to be set) .
     * </p>
     *
     * <p>
     * Implementation details As per PM comments in the forums. This logic is not currently known and will be
     * supplied later.
     * </p>
     *
     *
     * @param dependency Dependency to check for being new.
     * @return true if new; false otherswise.
     * @throws IllegalArgumentException if dependency is null.
     */
    public boolean isNewDependency(Dependency dependency) {
        if (dependency == null) {
            throw new IllegalArgumentException("dependency cannot be null.");
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = createConnection(true);
        } catch (PhasePersistenceException e) {
            return false;
        }

        try {
            // create the statement
            pstmt = conn.prepareStatement(CHECK_DEPENDENCY);
            pstmt.setLong(1, dependency.getDependency().getId());
            pstmt.setLong(2, dependency.getDependent().getId());
            // execute query
            rs = pstmt.executeQuery();

            // if has any result - phase is not new
            return !rs.next();
        } catch (SQLException ex) {
            return false;
        } finally {
            close(rs);
            close(pstmt);
            close(conn);
        }
    }

    /**
     * Closes the connection.
     *
     * @param conn the connection.
     */
    private static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Closes the statement.
     *
     * @param stmt the statement.
     */
    private static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Closes the result set.
     *
     * @param rs the result set.
     */
    private static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * Roll backs the transaction.
     *
     * @param conn connection.
     */
    private static void rollback(Connection conn) {
        try {
            conn.rollback();
        } catch (SQLException ex) {
            // ignore
        }
    }

    /**
     * Converts the give list of Long objects to array.
     *
     * @param ids the ids list.
     * @return the ids array.
     */
    private static long[] listToArray(List ids) {
        long[] result = new long[ids.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = ((Long) ids.get(i)).longValue();
        }

        return result;
    }

    /**
     * Creates the string in the pattern (?,+) where count is the number of question marks. It is used th build
     * prepared statements with IN condition.
     *
     * @param count number of question marks.
     * @return the string of question marks.
     */
    private static String createQuestionMarks(int count) {
        StringBuffer buff = new StringBuffer();
        buff.append("(?");
        for (int i = 1; i < count; i++) {
            buff.append(", ?");
        }

        buff.append(")");
        return buff.toString();
    }

    /**
     * The private comparator of phases. It compares the phases scheduled start dates.
     *
     * @author kr00tki
     * @version 1.0
     */
    private class PhaseStartDateComparator implements Comparator {

        /**
         * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the
         * first start date is less than, equal to, or greater than the second.
         *
         * @param o1 the first phase.
         * @param o2 the second phase.
         *
         * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or
         *         greater than the second.
         */
        public int compare(Object o1, Object o2) {
            return ((Phase) o1).getScheduledStartDate().compareTo(((Phase) o2).getScheduledStartDate());
        }
    }
    
    /**
     * This method will a project phase's scheduled start and end time when they are inserted, deleted, or edited.
     *
     * @param connection the connection to database
     * @param phase the phase being audited
     * @param scheduledStartTime the new scheduled start time for the phase
     * @param scheduledEndTime the new scheduled end time for the phase
     * @param auditType the audit type. Can be AUDIT_CREATE_TYPE, AUDIT_DELETE_TYPE, or AUDIT_UPDATE_TYPE
     * @param auditUser the user initiating the change
     * @param auditTime the timestamp for the audit event
     *
     * @throws PhasePersistenceException if validation error occurs or any error occurs in the underlying layer
     *
     * @since 1.0.2
     */
    private void auditProjectPhase(Connection connection, Phase phase, int auditType, Timestamp scheduledStartTime,
    		Timestamp scheduledEndTime, Long auditUser, Timestamp auditTime) throws PhasePersistenceException {

        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement(PROJECT_PHASE_AUDIT_INSERT_SQL);

            int index = 1;
            statement.setLong(index++, phase.getId());
            statement.setTimestamp(index++, scheduledStartTime);
            statement.setTimestamp(index++, scheduledEndTime);
            statement.setInt(index++, auditType);
            statement.setTimestamp(index++, auditTime);
            statement.setLong(index++, auditUser);

            if (statement.executeUpdate() != 1) {
                throw new PhasePersistenceException("Audit information was not successfully saved.");
            }
        } catch (SQLException e) {
            close(connection);
            throw new PhasePersistenceException("Unable to insert project_info_audit record.", e);
        } finally {
            close(statement);
        }
    }
    
}
