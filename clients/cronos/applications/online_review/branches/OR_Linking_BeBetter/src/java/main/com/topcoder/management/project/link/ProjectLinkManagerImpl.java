/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project.link;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.link.Helper.DataType;
import com.topcoder.management.project.persistence.logging.LogMessage;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * <p>
 * Project link manager. It handles persistence operations for project link as well as project link type. It currently
 * relies on the project manager to get <code>Project</code> entities.
 * </p>
 * <p>
 * It is created for "OR Project Linking" assembly.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class ProjectLinkManagerImpl implements ProjectLinkManager {
    /**
     * <p>
     * Logger instance using the class name as category.
     * </p>
     */
    private static final Log LOGGER = LogFactory.getLog(ProjectLinkManagerImpl.class.getName());

    /**
     * <p>
     * Represents the name of connection name parameter in configuration.
     * </p>
     */
    private static final String CONNECTION_NAME_PARAMETER = "ConnectionName";

    /**
     * <p>
     * Represents the name of connection factory namespace parameter in configuration.
     * </p>
     */
    private static final String CONNECTION_FACTORY_NAMESPACE_PARAMETER = "ConnectionFactoryNS";

    /**
     * <p>
     * The SQL to get all project link types.
     * </p>
     */
    private static final String QUERY_ALL_PROJECT_LINK_TYPES_SQL = "SELECT link_type_id, link_type_name "
        + " FROM link_type_lu";

    /**
     * <p>
     * Column types for return row of above SQL.
     * </p>
     */
    private static final DataType[] QUERY_ALL_PROJECT_LINK_TYPES_COLUMN_TYPES = new DataType[] {Helper.LONG_TYPE,
        Helper.STRING_TYPE};

    /**
     * <p>
     * The SQL to get all destination project links given source project id.
     * </p>
     */
    private static final String QUERY_DEST_PROJECT_LINK_SQL = "SELECT dest_project_id, link_type_id "
        + " FROM linked_project_xref WHERE source_project_id = ?";

    /**
     * <p>
     * Column types for return row of above SQL.
     * </p>
     */
    private static final DataType[] QUERY_DEST_PROJECT_LINK_COLUMN_TYPES = new DataType[] {Helper.LONG_TYPE,
        Helper.LONG_TYPE};

    /**
     * <p>
     * The SQL to get all source project links for the given destination project id.
     * </p>
     */
    private static final String QUERY_SOURCE_PROJECT_LINK_SQL = "SELECT source_project_id, link_type_id "
        + " FROM linked_project_xref WHERE dest_project_id = ?";

    /**
     * <p>
     * Column types for return row of above SQL.
     * </p>
     */
    private static final DataType[] QUERY_SOURCE_PROJECT_LINK_COLUMN_TYPES = new DataType[] {Helper.LONG_TYPE,
        Helper.LONG_TYPE};

    /**
     * <p>
     * The SQL to delete project links by source project id.
     * </p>
     */
    private static final String DELETE_PROJECT_LINK_BY_SOURCE_RPOJECT_ID = "DELETE FROM linked_project_xref "
        + "WHERE source_project_id = ?";

    /**
     * <p>
     * The SQL to insert project link.
     * </p>
     */
    private static final String INSERT_PROJECT_LINK = "INSERT INTO linked_project_xref"
        + "(source_project_id,dest_project_id,link_type_id) VALUES(?,?,?)";

    /**
     * <p>
     * The factory instance used to create connection to the database. It is initialized in the constructor using
     * DBConnectionFactory component and never changed after that. It will be used in various persistence methods of
     * this project.
     * </p>
     */
    private final DBConnectionFactory factory;

    /**
     * <p>
     * Represents the database connection name that will be used by DBConnectionFactory. This variable is initialized
     * in the constructor and never changed after that. It will be used in create/update/retrieve method to create
     * connection. This variable can be null, which mean connection name is not defined in the configuration namespace
     * and default connection will be created.
     * </p>
     */
    private final String connectionName;

    /**
     * <p>
     * The project manager. It will delegate some project related queries into project manager.
     * </p>
     */
    private final ProjectManager projectManager;

    /**
     * <p>
     * The default constructor for project link manager.
     * </p>
     *
     * @param projectManager the project manager
     * @throws IllegalArgumentException if the project manager is null
     * @throws ConfigurationException if error occurs while loading configuration settings, or required configuration
     *             parameter is missing.
     */
    public ProjectLinkManagerImpl(ProjectManager projectManager) throws ConfigurationException {
        this(ProjectLinkManagerImpl.class.getName(), projectManager);
    }

    /**
     * <p>
     * The constructor for project link manager.
     * </p>
     *
     * @param namespace the name space value. It should not be null or empty
     * @param projectManager the project manager
     * @throws IllegalArgumentException if the namespace is null or empty string or the project manager is null
     * @throws ConfigurationException if error occurs while loading configuration settings, or required configuration
     *             parameter is missing.
     */
    public ProjectLinkManagerImpl(String namespace, ProjectManager projectManager) throws ConfigurationException {
        Helper.assertStringNotNullNorEmpty(namespace, "namespace");
        Helper.assertObjectNotNull(projectManager, "projectManager");

        this.projectManager = projectManager;

        // get the instance of ConfigManager
        ConfigManager cm = ConfigManager.getInstance();

        // get db connection factory namespace
        String factoryNamespace = Helper.getConfigurationParameterValue(cm, namespace,
            CONNECTION_FACTORY_NAMESPACE_PARAMETER, true, getLogger());

        // try to create a DBConnectionFactoryImpl instance from
        // factoryNamespace
        try {
            factory = new DBConnectionFactoryImpl(factoryNamespace);
        } catch (Exception e) {
            throw new ConfigurationException("Unable to create a new instance of DBConnectionFactoryImpl class"
                + " from namespace [" + factoryNamespace + "].", e);
        }

        // get the connection name
        connectionName = Helper.getConfigurationParameterValue(cm, namespace, CONNECTION_NAME_PARAMETER, false,
            getLogger());

    }

    /**
     * <p>
     * Gets all project link types.
     * </p>
     *
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    public ProjectLinkType[] getAllProjectLinkTypes() throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "Enter getAllProjectLinkTypes method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project link types
            ProjectLinkType[] projectLinkTypes = getAllProjectLinkTypes(conn);
            closeConnection(conn);
            return projectLinkTypes;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getAllProjectLinkTypes.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * Gets all project link types.
     * </p>
     *
     * @param conn the db connection
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    private ProjectLinkType[] getAllProjectLinkTypes(Connection conn) throws PersistenceException {
        // find all project types in the table.
        Object[][] rows = Helper.doQuery(conn, QUERY_ALL_PROJECT_LINK_TYPES_SQL, new Object[] {},
            QUERY_ALL_PROJECT_LINK_TYPES_COLUMN_TYPES);

        // create the ProjectLinkType array.
        ProjectLinkType[] projectLinkTypes = new ProjectLinkType[rows.length];

        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectLinkType class
            projectLinkTypes[i] = new ProjectLinkType(((Long) row[0]).longValue(), (String) row[1]);
        }

        return projectLinkTypes;
    }

    /**
     * <p>
     * Gets all project links based on source project id.
     * </p>
     *
     * @param sourceProjectId source project id
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    public ProjectLink[] getDestProjectLinks(long sourceProjectId) throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "Enter getDestProjectLinks method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project link types
            ProjectLink[] projectLinks = getDestProjectLinks(sourceProjectId, conn);
            closeConnection(conn);
            return projectLinks;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getDestProjectLinks.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * It is internal method and it gets all project links based on source project id.
     * </p>
     *
     * @param sourceProjectId source project id
     * @param conn the db connection
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    private ProjectLink[] getDestProjectLinks(long sourceProjectId, Connection conn) throws PersistenceException {
        Object[][] rows = Helper.doQuery(conn, QUERY_DEST_PROJECT_LINK_SQL, new Object[] {new Long(sourceProjectId)},
            QUERY_DEST_PROJECT_LINK_COLUMN_TYPES);

        ProjectLink[] projectLinks = new ProjectLink[rows.length];

        long[] ids = new long[rows.length + 1];
        ids[0] = sourceProjectId;
        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectLink class
            projectLinks[i] = new ProjectLink();
            // we will only provide link type id
            projectLinks[i].setType(new ProjectLinkType());
            projectLinks[i].getType().setId((Long) row[1]);
            ids[i + 1] = (Long) row[0];
        }

        // get Project objects
        Project[] projects = projectManager.getProjects(ids);
        Project sourceProject = projects[0];
        for (int i = 0; i < projectLinks.length; i++) {
            projectLinks[i].setSourceProject(sourceProject);
            projectLinks[i].setDestProject(projects[i + 1]);
        }

        fillLinkTypes(projectLinks, conn);

        return projectLinks;
    }

    /**
     * <p>
     * Gets all project links based on destination project id.
     * </p>
     *
     * @param destProjectId destination project id
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    public ProjectLink[] getSourceProjectLinks(long destProjectId) throws PersistenceException {
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "Enter getSourceProjectLinks method."));
        try {
            // create the connection
            conn = openConnection();

            // get all the project link types
            ProjectLink[] projectLinks = getSourceProjectLinks(destProjectId, conn);
            closeConnection(conn);
            return projectLinks;
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to getSourceProjectLinks.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * It is internal method and it gets all project links based on destination project id.
     * </p>
     *
     * @param destProjectId destination project id
     * @param conn the db connection
     * @return all project link types
     * @throws PersistenceException if any persistence error occurs
     */
    private ProjectLink[] getSourceProjectLinks(long destProjectId, Connection conn) throws PersistenceException {
        Object[][] rows = Helper.doQuery(conn, QUERY_SOURCE_PROJECT_LINK_SQL, new Object[] {new Long(destProjectId)},
            QUERY_SOURCE_PROJECT_LINK_COLUMN_TYPES);

        ProjectLink[] projectLinks = new ProjectLink[rows.length];

        long[] ids = new long[rows.length + 1];
        ids[0] = destProjectId;
        for (int i = 0; i < rows.length; ++i) {
            Object[] row = rows[i];

            // create a new instance of ProjectLink class
            projectLinks[i] = new ProjectLink();
            // we will only provide link type id
            projectLinks[i].setType(new ProjectLinkType());
            projectLinks[i].getType().setId((Long) row[1]);
            ids[i + 1] = (Long) row[0];
        }

        // get Project objects
        Project[] projects = projectManager.getProjects(ids);
        Project destProject = projects[0];
        for (int i = 0; i < projectLinks.length; i++) {
            projectLinks[i].setSourceProject(projects[i + 1]);
            projectLinks[i].setDestProject(destProject);
        }

        fillLinkTypes(projectLinks, conn);

        return projectLinks;
    }

    /**
     * <p>
     * Fills full blown link type information.
     * </p>
     *
     * @param projectLinks project links to be filled
     * @param conn db the connection
     * @throws PersistenceException if any persistence error occurs
     */
    private void fillLinkTypes(ProjectLink[] projectLinks, Connection conn) throws PersistenceException {
        ProjectLinkType[] allTypes = getAllProjectLinkTypes(conn);

        Map<Long, ProjectLinkType> typeMap = new HashMap<Long, ProjectLinkType>();
        for (ProjectLinkType type : allTypes) {
            typeMap.put(type.getId(), type);
        }

        for (ProjectLink link : projectLinks) {
            link.setType(typeMap.get(link.getType().getId()));
        }
    }

    /**
     * <p>
     * Updates project links for given source project id. It will delete all old links and use passed in project
     * links. There are 2 arrays passed in, one is for destination project ids and other for link type ids. The id at
     * the same position in each array represents a project link information.
     * </p>
     *
     * @param sourceProjectId the source project id
     * @param destProjectIds the destination project ids
     * @param linkTypeIds the type ids
     * @throws IllegalArgumentException if any array is null or it is not equal in length for dest project id array
     *             and link type array
     * @throws PersistenceException if any persistence error occurs
     */
    public void updateProjectLinks(long sourceProjectId, long[] destProjectIds, long[] linkTypeIds)
        throws PersistenceException {
        Helper.assertObjectNotNull(destProjectIds, "destProjectIds");
        Helper.assertObjectNotNull(linkTypeIds, "linkTypeIds");
        if (destProjectIds.length != linkTypeIds.length) {
            throw new IllegalArgumentException("destProjectIds must have same length as linkTypeIds");
        }
        Connection conn = null;

        getLogger().log(Level.INFO, new LogMessage(null, null, "Enter updateProjectLinks method."));
        try {
            // create the connection
            conn = openConnection();

            // refresh links
            updateProjectLinks(sourceProjectId, destProjectIds, linkTypeIds, conn);
            closeConnection(conn);
        } catch (PersistenceException e) {
            getLogger().log(Level.ERROR, new LogMessage(null, null, "Fail to updateProjectLinks.", e));
            if (conn != null) {
                closeConnectionOnError(conn);
            }
            throw e;
        }
    }

    /**
     * <p>
     * It is internal method and it updates the project links for given source project id.
     * </p>
     *
     * @param sourceProjectId the source project id
     * @param destProjectIds the destination project ids
     * @param linkTypeIds the type ids
     * @param conn the db connection
     * @throws PersistenceException if any persistence error occurs
     */
    private void updateProjectLinks(long sourceProjectId, long[] destProjectIds, long[] linkTypeIds, Connection conn)
        throws PersistenceException {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(DELETE_PROJECT_LINK_BY_SOURCE_RPOJECT_ID);
            ps.setLong(1, sourceProjectId);
            ps.executeUpdate();
            Helper.closeStatement(ps);

            ps = conn.prepareStatement(INSERT_PROJECT_LINK);
            for (int i = 0; i < destProjectIds.length; i++) {
                int idx = 1;
                ps.setLong(idx++, sourceProjectId);
                ps.setLong(idx++, destProjectIds[i]);
                ps.setLong(idx++, linkTypeIds[i]);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new PersistenceException("Error occurs while executing queries for update project links. ", e);
        } finally {
            Helper.closeStatement(ps);
        }
    }

    /**
     * Returns the database connection name that will be used by DB Connection Factory.
     *
     * @return a possibly <code>null</code> string representing the connection name used in DB Connection Factory.
     */
    public String getConnectionName() {
        return connectionName;
    }

    /**
     * Returns the <code>DBConnectionFactory</code> instance used to create connection to the database.
     *
     * @return the <code>DBConnectionFactory</code> instance used to create connection to the database.
     */
    public DBConnectionFactory getConnectionFactory() {
        return factory;
    }

    /**
     * <p>
     * It uses the DB connection factory to create the connection to underlying database. If the connection is not
     * configured, the default connection from DB connection factory will be created, otherwise, the connection with
     * the specified name in DB connection factory will be created.
     * </p>
     * <p>
     * Once the connection is retrieved, the auto commit property will be set false to manage the transaction itself.
     * </p>
     *
     * @return an open Connection to underlying database.
     * @throws PersistenceException if there's a problem getting the Connection
     */
    private Connection openConnection() throws PersistenceException {
        if (connectionName == null) {
            getLogger()
                .log(Level.INFO, new LogMessage(null, null, "creating db connection using default connection"));
        } else {
            getLogger().log(Level.INFO,
                new LogMessage(null, null, "creating db connection using connection name: " + connectionName));
        }
        Connection conn = Helper.createConnection(getConnectionFactory(), connectionName);
        try {
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            throw new PersistenceException("Error occurs when setting "
                + (connectionName == null ? "the default connection" : ("the connection '" + connectionName + "'"))
                + " to support transaction.", e);
        }

    }

    /**
     * <p>
     * It is used to commit the transaction and close the connection after an operation successfully completes.
     * </p>
     *
     * @param connection a Connection to close
     * @throws PersistenceException if any problem occurs trying to close the connection
     * @throws IllegalArgumentException if the argument is null
     */
    protected void closeConnection(Connection connection) throws PersistenceException {
        Helper.assertObjectNotNull(connection, "connection");
        try {
            getLogger().log(Level.INFO, "committing transaction");
            Helper.commitTransaction(connection);
        } finally {
            Helper.closeConnection(connection);
        }

    }

    /**
     * <p>
     * It is used to rollback the transaction and close the connection after an operation fails to complete.
     * </p>
     *
     * @param connection a connection to close
     * @throws IllegalArgumentException if the argument is null
     * @throws PersistenceException if any problem occurs trying to close the connection
     */
    protected void closeConnectionOnError(Connection connection) throws PersistenceException {
        Helper.assertObjectNotNull(connection, "connection");
        try {
            getLogger().log(Level.INFO, "rollback transaction");
            Helper.rollBackTransaction(connection);
        } finally {
            Helper.closeConnection(connection);
        }
    }

    /**
     * <p>
     * Returns the logger.
     * </p>
     *
     * @return the <code>Log</code> instance used to take the log message
     */
    private Log getLogger() {
        return LOGGER;
    }

}
