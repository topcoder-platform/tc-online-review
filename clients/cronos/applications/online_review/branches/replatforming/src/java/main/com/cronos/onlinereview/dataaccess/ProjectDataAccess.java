/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.dataaccess;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.shared.dataAccess.resultSet.ResultSetContainer;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>A simple DAO for projects backed up by Query Tool.</p>
 *
 * <p>
 * Version 1.1 (Impersonation Login Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #getCockpitProjectName(long)} method.</li>
 *     <li>Added {@link #isCockpitProjectUser(long, long)} method.</li>
 *     <li>Renamed <code>searchInactiveProjects</code> method to <code>searchDraftProjects</code> method.</li>
 *   </ol>
 * </p>
 *
 * <p>
 * Version 1.2 (Online Review Late Deliverables Edit Assembly 1.0) Change notes:
 *   <ol>
 *     <li>Added {@link #getAllCockpitProjects()} method.</li>
 *     <li>Added {@link #getCockpitProjectsForUser(long)} method.</li>
 *   </ol>
 * </p>
 *
 * @author isv
 * @version 1.2
 */
public class ProjectDataAccess extends BaseDataAccess {

    /**
     * <p>Constructs new <code>ProjectDataAccess</code> instance. This implementation does nothing.</p>
     */
    public ProjectDataAccess() {
    }

    /**
     * <p>Gets all active projects.</p>
     *
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> listing available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchActiveProjects(ProjectStatus[] projectStatuses, ProjectCategory[] projectCategories,
                                          ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_status", "tcs_project_infos_by_status", "stid",
                                         String.valueOf(PROJECT_STATUS_ACTIVE_ID),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets all draft projects.</p>
     *
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> listing available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchDraftProjects(ProjectStatus[] projectStatuses, ProjectCategory[] projectCategories,
                                            ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_status", "tcs_project_infos_by_status", "stid",
                                         String.valueOf(PROJECT_STATUS_DRAFT_ID),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets all active projects assigned to specified user.</p>
     *
     * @param userId a <code>long</code> providing the user ID.
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> listing available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     */
    public Project[] searchUserActiveProjects(long userId, ProjectStatus[] projectStatuses,
                                              ProjectCategory[] projectCategories,
                                              ProjectPropertyType[] projectInfoTypes) {
        return searchProjectsByQueryTool("tcs_projects_by_user", "tcs_project_infos_by_user", "uid",
                                         String.valueOf(userId),
                                         projectStatuses, projectCategories, projectInfoTypes);
    }

    /**
     * <p>Gets the name for <code>Cockpit</code> project which might have been associated with the specified project.
     * </p>
     *
     * @param projectId a <code>long</code> providing the ID of a project.
     * @return a <code>String</code> providing the name for <code>Cockpit</code> project associated with specified
     *         project or <code>null</code> if there is no such <code>Cockpit</code> project.
     * @since 1.1
     */
    public String getCockpitProjectName(long projectId) {
        Map<String, ResultSetContainer> results = runQuery("cockpit_project", "pj", String.valueOf(projectId));
        ResultSetContainer result = results.get("cockpit_project");
        if (!result.isEmpty()) {
            return result.getStringItem(0, "name");
        } else {
            return null;
        }
    }

    /**
     * <p>Checks if the specified user has access to the specified cockpit project.</p>
     *
     * @param projectId a <code>long</code> providing the ID of a project.
     * @param userId a <code>long</code> providing the ID of a user.
     * @return <code>true</code> if specified user is granted <code>Cockpit Project User</code> role for specified
     *         project; <code>false</code> otherwise.
     * @since 1.1
     */
    public boolean isCockpitProjectUser(long projectId, long userId) {
        Map<String, ResultSetContainer> results = runQuery("cockpit_project_user",
                new String[] {"pj", "uid"}, new String[] {String.valueOf(projectId), String.valueOf(userId)});
        ResultSetContainer result = results.get("cockpit_project_user");
        if (!result.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Gets the list of all existing <code>Cockpit</code> projects.</p>
     * 
     * @return a <code>Map</code> mapping IDs to names for all existing <code>Cockpit</code> projects.
     * @since 1.2 
     */
    public Map<Long, String> getAllCockpitProjects() {
        Map<String, ResultSetContainer> results = runQuery("cockpit_projects", (String) null, (String) null);
        ResultSetContainer projectsResultContainer = results.get("cockpit_projects");
        Map<Long, String> result = new LinkedHashMap<Long, String>();
        for (ResultSetContainer.ResultSetRow row : projectsResultContainer) {
            long tcDirectProjectId = row.getLongItem("tc_direct_project_id");
            String tcDirectProjectName = row.getStringItem("tc_direct_project_name");
            result.put(tcDirectProjectId, tcDirectProjectName);
        }
        return result;
    }

    /**
     * <p>Gets the list of all existing <code>Cockpit</code> projects which the specified user is granted access 
     * permission for.</p>
     * 
     * @param userId a <code>long</code> providing the ID of a user to get the list of accessible <code>Cockpit</code> 
     *        projects for. 
     * @return a <code>Map</code> mapping IDs to names for all existing <code>Cockpit</code> projects accessible to
     *         specified user.
     * @since 1.2 
     */
    public Map<Long, String> getCockpitProjectsForUser(long userId) {
        Map<String, ResultSetContainer> results = runQuery("direct_my_projects", "uid", String.valueOf(userId));
        ResultSetContainer projectsResultContainer = results.get("direct_my_projects");
        Map<Long, String> result = new LinkedHashMap<Long, String>();
        for (ResultSetContainer.ResultSetRow row : projectsResultContainer) {
            long tcDirectProjectId = row.getLongItem("tc_direct_project_id");
            String tcDirectProjectName = row.getStringItem("tc_direct_project_name");
            result.put(tcDirectProjectId, tcDirectProjectName);
        }
        return result;
    }

    /**
     * <p>Gets the list of projects of specified status.</p>
     *
     * @param projectQuery a <code>String</code> providing the name of the query to be run for getting the project
     *        details.
     * @param projectInfoQuery a <code>String</code> providing the name of the query to be run for getting the project
     *        info details.
     * @param paramName a <code>String</code> providing the name of the query parameter for customization.
     * @param paramValue a <code>String</code> providing the value of the query parameter for customization.
     * @param projectStatuses a <code>ProjectStatus</code> array listing the available project statuses.
     * @param projectCategories a <code>ProjectCategory</code> array listing available project categories.
     * @param projectInfoTypes a <code>ProjectPropertyType</code> listing available project info types.
     * @return a <code>Project</code> array listing the projects of specified status.
     * @throws DataAccessException if an unexpected error occurs while running the query via Query Tool.
     * @since 1.7
     */
    private Project[] searchProjectsByQueryTool(String projectQuery, String projectInfoQuery, String paramName,
                                                String paramValue, ProjectStatus[] projectStatuses,
                                                ProjectCategory[] projectCategories,
                                                ProjectPropertyType[] projectInfoTypes) {

        // Build the cache of project categories and project statuses for faster lookup by ID
        Map<Long, ProjectCategory> categoriesMap = buildProjectCategoriesLookupMap(projectCategories);
        Map<Long, ProjectStatus> statusesMap = buildProjectStatusesLookupMap(projectStatuses);

        // Get project details by status using Query Tool
        Map<String, ResultSetContainer> results = runQuery(projectQuery, paramName, paramValue);

        // Convert returned data into Project objects
        ResultSetContainer projectsData = results.get(projectQuery);
        Map<Long, Project> projects = new HashMap<Long, Project>(projectsData.size());
        int recordNum = projectsData.size();
        Project[] resultingProjects = new Project[recordNum];
        for (int i = 0; i < recordNum; i++) {
            long projectId = projectsData.getLongItem(i, "project_id");
            long projectCategoryId = projectsData.getLongItem(i, "project_category_id");
            long projectStatusId = projectsData.getLongItem(i, "project_status_id");
            String createUser = projectsData.getStringItem(i, "create_user");
            Timestamp createDate = projectsData.getTimestampItem(i, "create_date");
            String modifyUser = projectsData.getStringItem(i, "modify_user");
            Timestamp modifyDate = projectsData.getTimestampItem(i, "modify_date");

            Project project = new Project(projectId, categoriesMap.get(projectCategoryId),
                                          statusesMap.get(projectStatusId));
            project.setCreationUser(createUser);
            project.setCreationTimestamp(createDate);
            project.setModificationUser(modifyUser);
            project.setModificationTimestamp(modifyDate);

            resultingProjects[i] = project;
            projects.put(projectId, project);
        }

        // Build the cache of project info types for faster lookup by ID
        Map<Long, ProjectPropertyType> infoTypesMap = buildProjectPropertyTypesLookupMap(projectInfoTypes);

        ResultSetContainer projectInfosData = results.get(projectInfoQuery);
        recordNum = projectInfosData.size();
        for (int i = 0; i < recordNum; i++) {
            long projectId = projectInfosData.getLongItem(i, "project_id");
            long projectInfoTypeId = projectInfosData.getLongItem(i, "project_info_type_id");
            String value = projectInfosData.getStringItem(i, "value");
            Project project = projects.get(projectId);
            project.setProperty(infoTypesMap.get(projectInfoTypeId).getName(), value);
        }

        return resultingProjects;
    }
}
