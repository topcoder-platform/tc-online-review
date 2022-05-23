/*
 * Copyright (C) 2010 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.onlinereview.component.project.management.ProjectStatus;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceManager;
import com.topcoder.onlinereview.component.resource.ResourceRole;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.topcoder.onlinereview.component.util.CommonUtils.getDate;
import static com.topcoder.onlinereview.component.util.CommonUtils.getLong;
import static com.topcoder.onlinereview.component.util.CommonUtils.getString;

/**
 * <p>A simple DAO for project resources backed up by Query Tool.</p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ResourceDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>ResourceDataAccess</code> instance. This implementation does nothing.</p>
     */
    public ResourceDataAccess() {
    }

    /**
     * <p>Searches the resources for specified user for projects of specified status.
     * If status parameter is null it will search for the 'global' resources with no
     * project associated.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param status a <code>ProjectStatus</code> specifying the status of the projects.
     * @param resourceManager a <code>ResourceManager</code> to be used for searching.
     * @return a <code>Resource</code> array providing the details for found resources.
     * @throws DataAccessException if an unexpected error occurs.
     */
    public Resource[] searchUserResources(long userId, ProjectStatus status, ResourceManager resourceManager) {
        
        // Get the list of existing resource roles and build a cache
        ResourceRole[] resourceRoles = resourceManager.getAllResourceRoles();
        Map<Long, ResourceRole> cachedRoles = new HashMap<Long, ResourceRole>();
        for (ResourceRole role : resourceRoles) {
            cachedRoles.put(role.getId(), role);
        }

        // Get resources details by user ID using Query Tool
        Map<String, List<Map<String, Object>>> results;
        if (status == null) {
            results = runQuery("tcs_global_resources_by_user", "uid", String.valueOf(userId));
        } else {
            results = runQuery("tcs_resources_by_user_and_status", new String[] {"uid", "stid"},
                               new String[] {String.valueOf(userId), String.valueOf(status.getId())});
        }

        // Convert returned data into Resource objects
        List<Map<String, Object>> resourcesData;
        if (status == null) {
            resourcesData = results.get("tcs_global_resources_by_user");
        } else {
            resourcesData = results.get("tcs_resources_by_user_and_status");
        }
        Map<Long, Resource> cachedResources = new HashMap<Long, Resource>();
        int recordNum = resourcesData.size();
        Resource[] resources = new Resource[recordNum];
        for (int i = 0; i < recordNum; i++) {
            long resourceId = getLong(resourcesData.get(i), "resource_id");
            long resourceRoleId = getLong(resourcesData.get(i), "resource_role_id");
            Long projectId = null;
            if (resourcesData.get(i).get("project_id") != null) {
                projectId = getLong(resourcesData.get(i), "project_id");
            }
            Long phaseId = null;
            if (resourcesData.get(i).get("phase_id") != null) {
                phaseId = getLong(resourcesData.get(i), "phase_id");
            }
            String createUser = getString(resourcesData.get(i), "create_user");
            Date createDate = getDate(resourcesData.get(i), "create_date");
            String modifyUser = getString(resourcesData.get(i), "modify_user");
            Date modifyDate = getDate(resourcesData.get(i), "modify_date");

            Resource resource = new Resource(resourceId, cachedRoles.get(resourceRoleId));
            resource.setProject(projectId);
            resource.setPhase(phaseId);
            resource.setUserId(userId);
            resource.setCreationUser(createUser);
            resource.setCreationTimestamp(createDate);
            resource.setModificationUser(modifyUser);
            resource.setModificationTimestamp(modifyDate);

            resources[i] = resource;
            cachedResources.put(resourceId, resource);
        }

        // Fill resources with resource info records
        List<Map<String, Object>> resourceInfosData;
        if (status == null) {
            resourceInfosData = results.get("tcs_global_resource_infos_by_user");
        } else {
            resourceInfosData = results.get("tcs_resource_infos_by_user_and_status");
        }
        recordNum = resourceInfosData.size();

        for (int i = 0; i < recordNum; i++) {
            long resourceId = getLong(resourceInfosData.get(i), "resource_id");
            String propName = getString(resourceInfosData.get(i), "resource_info_type_name");
            String value = getString(resourceInfosData.get(i), "value");
            Resource resource = cachedResources.get(resourceId);
            resource.setProperty(propName, value);
        }

        return resources;
    }
}
