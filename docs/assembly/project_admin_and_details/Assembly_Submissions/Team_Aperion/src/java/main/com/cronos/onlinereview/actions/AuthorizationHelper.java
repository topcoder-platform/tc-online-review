/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class provides helper methods that can be used to determine if the user has particular role,
 * or to check if user is authorized to perform particular action.  The methods are designed to
 * check directly the <code>HttpServletRequest</code>, as it simplifies their usage.
 * <p>
 * This class is thread safe as it contains only static methods and no inner state.
 * </p>
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
public class AuthorizationHelper {

    /**
     * This member variable is an integer constant that specifies the value which is used to denote
     * that no user is logged into application.
     */
    public static final long NO_USER_LOGGED_IN_ID = 0;

    /**
     * Private construcor, just to prevent instantiation of the class.
     */
    private AuthorizationHelper() {
        // Just do nothing
    }

    /**
     * This static method returns the ID of the user currently logged in.
     *
     * @return the ID of the currently logged in user, or <code>NO_USER_LOGGED_IN_ID</code> if no
     * user has been logged in.
     * @param request
     *            an <code>HttpServletRequest</code> object containing the information about
     *            the user possibly logged in.
     */
    public static long getLoggedInUserId(HttpServletRequest request) {
        Long userId = (Long)request.getSession().getAttribute(ConfigHelper.getUserIdAttributeName());
        return (userId != null) ? userId.longValue() : NO_USER_LOGGED_IN_ID;
    }

    /**
     * This static method sets (stores into session attribute) the ID of the user currently logged
     * in.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object which the information about the logged
     *            in user will be stored to.
     * @param userId
     *            the ID of the user that should be logged into the application, or
     *            <code>NO_USER_LOGGED_IN_ID</code> if no user should be logged in.
     */
    public static void setLoggedInUserId(HttpServletRequest request, long userId) {
        request.getSession().setAttribute(ConfigHelper.getUserIdAttributeName(),
                (userId != NO_USER_LOGGED_IN_ID) ? new Long(userId) : null);
    }

    /**
     * This static method checks whether any user has been logged in into the application.
     *
     * @return <code>true</code> if there is a user logged in,
     *         <code>false</code> if there isn't.
     * @param request
     *            an <code>HttpServletRequest</code> object containing the information about the
     *            possibly logged in user.
     */
    public static boolean isUserLoggedIn(HttpServletRequest request) {
        return (getLoggedInUserId(request) != NO_USER_LOGGED_IN_ID);
    }

    /**
     * Logs out the user from the application.  If there has been no user logged in, this function
     * has no effect.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object which the information about the logged
     *            in user will be stored to.
     */
    public static void logoutTheUser(HttpServletRequest request) {
        setLoggedInUserId(request, NO_USER_LOGGED_IN_ID);
    }

    /**
     * This static method gathers the Resource Roles that the currently logged in user has.  It then
     * places the gathered roles into the object specified by the <code>request</code> parameter.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information such
     *            as the ID of the currently logged in user that will help gathering Resource Roles.
     *            <p>On return, the object specified by this parameter will receive the list of all
     *            gathered roles.</p>
     * @throws BaseException
     *             if any error occurs.
     */
    public static void gatherUserRoles(HttpServletRequest request) throws BaseException {
        // Prepare the set which will contain all the roles the user has
        Set roles = new HashSet();

        // Add "Public" role
        roles.add(Constants.PUBLIC_ROLE_NAME);

        // Prepare filter to select resources by the External ID of currently logged in user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
        Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                String.valueOf(getLoggedInUserId(request)));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Perform search for resources
        Resource[] resources = resMgr.searchResources(filterExtID);

        // Iterate over all resources and retrieve their roles
        for (int i = 0; i < resources.length; ++i) {
            // Get the role this resource has
            ResourceRole role = resources[i].getResourceRole();
            // Add the name of the role to the roles set (gather the role)
            roles.add(role.getName());
        }

        // Place the set with gathered roles into request
        request.setAttribute("roles", roles);
    }

    /**
     * This static method gathers the Resource Roles that the currently logged in user has based on
     * the context of the Project specified by its ID.  It then places the gathered roles into the
     * object specified by the <code>request</code> parameter.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information such
     *            as the ID of the currently logged in user that will help gathering Resource Roles.
     *            <p>On return, the object specified by this parameter will receive the list of all
     *            gathered roles.</p>
     * @param projectId
     *            an ID of the project which context should be used to gather the Resource Roles.
     * @throws BaseException
     *             if any error occurs.
     */
    public static void gatherUserRoles(HttpServletRequest request, long projectId) throws BaseException {
        // Prepare the set which will contain all the roles the user has
        Set roles = new HashSet();

        // Create an instance of Project Manager as some properties for the project should be checked
        ProjectManager projMgr = new ProjectManagerImpl();
        // Retrieve the project by the specified project ID
        Project project = projMgr.getProject(projectId);

        // If project with the specified id does not exist,
        // simply gather the roles as if shoter version of this funtion has been called
        if (project == null) {
            gatherUserRoles(request);
            return;
        }

        // If this project is public, add "Public" role to the set
        if ("Yes".equalsIgnoreCase((String)project.getProperty("Public"))) {
            roles.add(Constants.PUBLIC_ROLE_NAME);
        }

        // Prepare filter to select resources by the External ID of currently logged in user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
        Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                String.valueOf(getLoggedInUserId(request)));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);

        // Create filter to filter only the resources for the project in question
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(projectId);
        // Create combined filter
        Filter filterCombined = new AndFilter(filterExtID, filterProject);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager();
        // Perform search for resources
        Resource[] resources = resMgr.searchResources(filterCombined);

        // Iterate over all resources and retrieve their roles
        for (int i = 0; i < resources.length; ++i) {
            // Get the role this resource has
            ResourceRole role = resources[i].getResourceRole();
            // Add the name of the role to the roles set (gather the role)
            roles.add(role.getName());
        }

        // Place the set with gathered roles into request
        request.setAttribute("roles", roles);
    }

    /**
     * This static method checks whether the logged in user has the specified role.  The information
     * about logged in user is taken from the session associated with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called prior making a
     * call to this method, or the result of this call will be <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has the specified role in current context,
     *         <code>false</code> if he doesn't.
     * @param request
     *            the <code>HttpServletRequest</code> to take the information about roles
     *            currently logged in user has in current context.
     * @param role
     *            the role to check for belonging to user.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserRole(HttpServletRequest request, String role) {
        // Retrieve set with roles that the user has in the current context from the request
        Set roles = (Set)request.getAttribute("roles");

        // If nothing has been retrieved, or the set is empty
        // (no permissions, how sad), return false immediately
        if (roles == null || roles.isEmpty()) {
            return false;
        }
        // Otherwise allow set to test if it contains the value under question
        return roles.contains(role);
    }

    /**
     * This static method checks whether the logged in user has the permission to perform the
     * specified action.  The information about logged in user is taken from the session associated
     * with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called prior making a
     * call to this method, or the result of this call will be <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has the permission to perform the specified
     *         action, <code>false</code> if he doesn't have.
     * @param request
     *            the <code>HttpServletRequest</code> to take the information about logged in user
     *            from (actually it is taken from session associated with this request).
     * @param permissionName
     *            the name of Permission to check if the user has.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserPermission(HttpServletRequest request, String permissionName) {
        String[] roles = ConfigHelper.getRolesForPermission(permissionName);

        for (int i = 0; i < roles.length; ++i) {
            if (hasUserRole(request, roles[i])) {
                return true;
            }
        }
        return false;
    }
}
