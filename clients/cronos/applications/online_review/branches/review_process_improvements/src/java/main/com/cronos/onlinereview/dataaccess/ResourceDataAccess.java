/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.shared.dataAccess.DataAccess;
import com.topcoder.shared.dataAccess.Request;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;
import com.topcoder.shared.util.DBMS;
import com.topcoder.util.errorhandling.BaseException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A simple DAO for project resources backed up by Query Tool.</p>
 *
 * @author isv
 * @version 1.0
 */
public class ResourceDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>ResourceDataAccess</code> instance. This implementation does nothing.</p>
     */
    public ResourceDataAccess() {
    }

    /**
     * <p>Searches the resources for specified user for projects of specified status.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param status a <code>ProjectStatus</code> specifying the status of the projects.
     * @param resourceManager a <code>ResourceManager</code> to be used for searching.
     * @return a <code>Resource</code> array providing the details for found resources.
     * @throws ResourcePersistenceException if an error occurs while retrieveing resource roles.
     * @throws DataAccessException if an unexpected error occurs.
     * @since 1.6
     */
    public Resource[] searchUserResources(long userId, ProjectStatus status, ResourceManager resourceManager)
        throws ResourcePersistenceException {
        
        // Get the list of existing resource roles and build a cache
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
        Map<Long, ResourceRole> cachedRoles = new HashMap<Long, ResourceRole>();
        for (ResourceRole role : resourceRoles) {
            cachedRoles.put(role.getId(), role);
        }

        // Get resources details by user ID using Query Tool
        Map<String, ResultSetContainer> results;
        if (status == null) {
            results = runQuery("tcs_resources_by_user", "uid", String.valueOf(userId));
        } else {
            results = runQuery("tcs_resources_by_user_and_status", new String[] {"uid", "stid"},
                               new String[] {String.valueOf(userId), String.valueOf(status.getId())});
        }

        // Convert returned data into Resource objects
        ResultSetContainer resourcesData;
        if (status == null) {
            resourcesData = results.get("tcs_resources_by_user");
        } else {
            resourcesData = results.get("tcs_resources_by_user_and_status");
        }
        Map<Long, Resource> cachedResources = new HashMap<Long, Resource>();
        int recordNum = resourcesData.size();
        Resource[] resources = new Resource[recordNum];
        for (int i = 0; i < recordNum; i++) {
            long resourceId = resourcesData.getLongItem(i, "resource_id");
            long resourceRoleId = resourcesData.getLongItem(i, "resource_role_id");
            Long projectId = null;
            if (resourcesData.getItem(i, "project_id").getResultData() != null) {
                projectId = resourcesData.getLongItem(i, "project_id");
            }
            Long phaseId = null;
            if (resourcesData.getItem(i, "phase_id").getResultData() != null) {
                phaseId = resourcesData.getLongItem(i, "phase_id");
            }
            String createUser = resourcesData.getStringItem(i, "create_user");
            Timestamp createDate = resourcesData.getTimestampItem(i, "create_date");
            String modifyUser = resourcesData.getStringItem(i, "modify_user");
            Timestamp modifyDate = resourcesData.getTimestampItem(i, "modify_date");

            Resource resource = new Resource(resourceId, cachedRoles.get(resourceRoleId));
            resource.setProject(projectId);
            resource.setPhase(phaseId);
            resource.setCreationUser(createUser);
            resource.setCreationTimestamp(createDate);
            resource.setModificationUser(modifyUser);
            resource.setModificationTimestamp(modifyDate);

            resources[i] = resource;
            cachedResources.put(resourceId, resource);
        }

        // Fill resources with resource info records
        ResultSetContainer resourceInfosData;
        if (status == null) {
            resourceInfosData = results.get("tcs_resource_infos_by_user");
        } else {
            resourceInfosData = results.get("tcs_resource_infos_by_user_and_status");
        }
        recordNum = resourceInfosData.size();

        for (int i = 0; i < recordNum; i++) {
            long projectId = resourceInfosData.getLongItem(i, "resource_id");
            String propName = resourceInfosData.getStringItem(i, "resource_info_type_name");
            String value = resourceInfosData.getStringItem(i, "value");
            Resource resource = cachedResources.get(projectId);
            resource.setProperty(propName, value);
        }

        return resources;
    }
}
