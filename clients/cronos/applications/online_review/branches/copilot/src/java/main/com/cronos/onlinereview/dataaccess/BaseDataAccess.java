/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.shared.dataAccess.DataAccess;
import com.topcoder.shared.dataAccess.Request;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;
import com.topcoder.shared.util.DBMS;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A base class for DAO implementations backed up by Query Tool.</p>
 *
 * <p>
 * Version 1.1 (Impersonation Login Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Renamed <code>PROJECT_STATUS_INACTIVE_ID</code> constant to <code>PROJECT_STATUS_DRAFT_ID</code> constant.
 *     </li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (TopCoder Online Review Switch To Local Calls Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #getTCSCatalogDBConnection()} method.</li>
 *   </ol>
 * </p>
 *
 * @author isv
 * @version 1.2
 */
public abstract class BaseDataAccess {

    /**
     * <p>A <code>long</code> providing the ID of project status <code>Active</code>. </p>
     */
    protected static final long PROJECT_STATUS_ACTIVE_ID = 1;

    /**
     * <p>A <code>long</code> providing the ID of project status <code>Draft</code>. </p>
     */
    protected static final long PROJECT_STATUS_DRAFT_ID = 2;

    /**
     * <p>Constructs new <code>BaseDataAccess</code> instance. This implementation does nothing.</p>
     */
    protected BaseDataAccess() {
    }

    /**
     * <p>Builds the map to be used for looking up the project categories by IDs.</p>
     *
     * @param categories a <code>ProjectCategory</code> array listing existing project categories.
     * @return a <code>Map</code> mapping the category IDs to categories.
     */
    protected static Map<Long, ProjectCategory> buildProjectCategoriesLookupMap(ProjectCategory[] categories) {
        Map<Long, ProjectCategory> categoriesMap = new HashMap<Long, ProjectCategory>(categories.length);
        for (ProjectCategory category : categories) {
            categoriesMap.put(category.getId(), category);
        }
        return categoriesMap;
    }

    /**
     * <p>Builds the map to be used for looking up the phase types by IDs.</p>
     *
     * @param phaseTypes a <code>PhaseType</code> array listing existing phase types.
     * @return a <code>Map</code> mapping the type IDs to phase types.
     */
    protected Map<Long, PhaseType> buildPhaseTypesLookupMap(PhaseType[] phaseTypes) {
        Map<Long, PhaseType> typesMap = new HashMap<Long, PhaseType>(phaseTypes.length);
        for (PhaseType type : phaseTypes) {
            typesMap.put(type.getId(), type);
        }
        return typesMap;
    }

    /**
     * <p>Builds the map to be used for looking up the phase statuses by IDs.</p>
     *
     * @param phaseStatuses a <code>PhaseType</code> array listing existing phase statuses.
     * @return a <code>Map</code> mapping the status IDs to phase statuses.
     */
    protected Map<Long, PhaseStatus> buildPhaseStatusesLookupMap(PhaseStatus[] phaseStatuses) {
        Map<Long, PhaseStatus> statusesMap = new HashMap<Long, PhaseStatus>(phaseStatuses.length);
        for (PhaseStatus phaseStatus : phaseStatuses) {
            statusesMap.put(phaseStatus.getId(), phaseStatus);
        }
        return statusesMap;
    }

    /**
     * <p>Builds the map to be used for looking up the project statuses by IDs.</p>
     *
     * @param projectStatuses a <code>PhaseType</code> array listing existing project statuses.
     * @return a <code>Map</code> mapping the status IDs to project statuses.
     */
    protected Map<Long, ProjectStatus> buildProjectStatusesLookupMap(ProjectStatus[] projectStatuses) {
        Map<Long, ProjectStatus> statusesMap = new HashMap<Long, ProjectStatus>(projectStatuses.length);
        for (ProjectStatus status : projectStatuses) {
            statusesMap.put(status.getId(), status);
        }
        return statusesMap;
    }

    /**
     * <p>Builds the map to be used for looking up the project info types by IDs.</p>
     *
     * @param projectInfoTypes a <code>PhaseType</code> array listing existing project info types.
     * @return a <code>Map</code> mapping the info type IDs to project info types.
     */
    protected Map<Long, ProjectPropertyType> buildProjectPropertyTypesLookupMap(ProjectPropertyType[] projectInfoTypes) {
        Map<Long, ProjectPropertyType> typesMap = new HashMap<Long, ProjectPropertyType>(projectInfoTypes.length);
        for (ProjectPropertyType infoType : projectInfoTypes) {
            typesMap.put(infoType.getId(), infoType);
        }
        return typesMap;
    }

    /**
     * <p>Builds the map to be used for looking up the resource roles by IDs.</p>
     *
     * @param resourceRoles a <code>ResourceRole</code> array listing existing resource roles.
     * @return a <code>Map</code> mapping the info type IDs to project info types.
     */
    protected Map<Long, ResourceRole> buildResourceRolesLookupMap(ResourceRole[] resourceRoles) {
        Map<Long, ResourceRole> rolesMap = new HashMap<Long, ResourceRole>(resourceRoles.length);
        for (ResourceRole role : resourceRoles) {
            rolesMap.put(role.getId(), role);
        }
        return rolesMap;
    }

    /**
     * <p>Executes the specified query using Query Tool. The query is customized with provided value of specified
     * parameter.</p>
     *
     * @param queryName a <code>String</code> providing the name of the query to be run.
     * @param paramName a <code>String</code> providing the name of the query parameter for customization.
     * @param paramValue a <code>String</code> providing the value of the query parameter for customization.
     * @return a <code>Map</code> providing the query results. Maps query names to query results.
     * @throws DataAccessException if an unexpected error occurs while running the query.
     */
    protected Map<String, ResultSetContainer> runQuery(String queryName, String paramName, String paramValue) {
        DataAccess dataAccess = new DataAccess(DBMS.TCS_OLTP_DATASOURCE_NAME);
        Request request = new Request();
        request.setContentHandle(queryName);
        if (paramName != null) {
            request.setProperty(paramName, paramValue);
        }
        try {
            return dataAccess.getData(request);
        } catch (Exception e) {
            throw new DataAccessException("Failed to run " + queryName + " query via Query Tool", e);
        }
    }

    /**
     * <p>Executes the specified query using Query Tool. The query is customized with provided value of specified
     * parameter.</p>
     *
     * @param queryName a <code>String</code> providing the name of the query to be run.
     * @param paramNames a <code>String</code> array providing the names of the query parameters for customization.
     * @param paramValues a <code>String</code> array providing the values of the query parameters for customization.
     * @return a <code>Map</code> providing the query results. Maps query names to query results.
     * @throws DataAccessException if an unexpected error occurs while running the query.
     */
    protected Map<String, ResultSetContainer> runQuery(String queryName, String[] paramNames, String[] paramValues) {
        DataAccess dataAccess = new DataAccess(DBMS.TCS_OLTP_DATASOURCE_NAME);
        Request request = new Request();
        request.setContentHandle(queryName);
        for (int i = 0; i < paramNames.length; i++) {
            request.setProperty(paramNames[i], paramValues[i]);
        }
        try {
            return dataAccess.getData(request);
        } catch (Exception e) {
            throw new DataAccessException("Failed to run " + queryName + " query via Query Tool", e);
        }
    }

    /**
     * <p>Gets a connection to TCS Catalog database.</p>
     *
     * @return a <code>Connection</code> providing the connection to TCS Catalog database.
     * @throws DataAccessException if an SQL error occurs while establishing connection to TCS Catalog database.
     * @since 1.2
     */
    protected Connection getTCSCatalogDBConnection() {
        try {
            return DBMS.getConnection(DBMS.TCS_OLTP_DATASOURCE_NAME);
        } catch (SQLException e) {
            throw new DataAccessException("Failed to connect to TCS Catalog database", e);
        }
    }

    /**
     * <p>Closes the specified statement. If an SQL error occurs while closing the statement it is ignored.</p>
     *
     * @param statement a <code>Statement</code> to be closed.
     */
    protected void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }

    /**
     * <p>Closes the specified connection. If an SQL error occurs while closing the connection it is ignored.</p>
     *
     * @param connection a <code>Connection</code> to be closed.
     */
    protected void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Ignore
            }
        }
    }
}
