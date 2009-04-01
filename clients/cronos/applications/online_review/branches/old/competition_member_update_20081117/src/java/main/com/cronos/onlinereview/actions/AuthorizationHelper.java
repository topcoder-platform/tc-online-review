/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
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
 * @author George1
 * @author real_vg
 * @version 1.0
 */
public class AuthorizationHelper {

    /**
     * This member variable is an integer constant that specifies the value which is used to denote
     * that no user is logged into application.
     */
    public static final long NO_USER_LOGGED_IN_ID = -1;

    public static final String REDIRECT_BACK_URL_ATTRIBUTE = "redirectBackUrl";

    /**
     * Private construcor, just to prevent instantiation of the class.
     */
    private AuthorizationHelper() {
        // Just do nothing
    }

    /**
     * This static method puts into session an attribute which specifies the address of the page to
     * return to after successful login.
     *
     * @param request
     *            an <code>HttpServletRequest</code> object containing additional information, and
     *            which session will receive the newly set attribute (if the address can be
     *            determined).
     * @param getFromReferer
     *            determines whether the method should use Referer request header, or record current
     *            URI the user is trying to access. This is required for save type of actions, since
     *            these actions often contain additional information passed as form's fields in
     *            request, and so returning to such kind of page by just its URI will lead to error.
     */
    public static void setLoginRedirect(HttpServletRequest request, boolean getFromReferer) {
        if (getFromReferer) {
            final String referer = request.getHeader("Referer");

            if (referer != null && referer.trim().length() != 0) {
                request.getSession().setAttribute(REDIRECT_BACK_URL_ATTRIBUTE, referer);
            }
            return;
        }

        StringBuffer redirectBackUrl = new StringBuffer();

        redirectBackUrl.append(request.getScheme());
        redirectBackUrl.append("://");
        redirectBackUrl.append(request.getServerName());
        redirectBackUrl.append(':');
        redirectBackUrl.append(request.getServerPort());
        redirectBackUrl.append(request.getRequestURI());
        if (request.getQueryString() != null && request.getQueryString().trim().length() != 0) {
            redirectBackUrl.append('?');
            redirectBackUrl.append(request.getQueryString());
        }

        request.getSession().setAttribute(REDIRECT_BACK_URL_ATTRIBUTE, redirectBackUrl.toString());
    }
    
    /**
     * This static method removes the attribute that specifies the address of the page to return to.
     * This is needed in case the user did not use the Login page he was redirected to, but went to
     * some page which he was allowed to see. Then, (possibly after some time) they decide to login
     * and click on Login link. In this case, after logging in, they get redirected to the previous
     * page of failure, since redirect attribute is already in the session. This method needs to be
     * called by every action that gets past the check whether the user is logged in.
     * 
     * @param request
     *            an <code>HttpServletRequest</code> object that possibly contains redirect
     *            attribute to be removed.
     */
    public static void removeLoginRedirect(HttpServletRequest request) {
        request.getSession().removeAttribute(REDIRECT_BACK_URL_ATTRIBUTE);
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
        Long userId = (Long) request.getSession().getAttribute(ConfigHelper.getUserIdAttributeName());
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

        // Place the set into the request.
        // It will be populated with the roles a little bit later in this method
        request.setAttribute("roles", roles);

        // Add "Public" role, as all users will have one out of project's context
        roles.add(Constants.PUBLIC_ROLE_NAME);

        // If the user is not logged in, he cannot have any other roles
        if (!isUserLoggedIn(request)) {
            return;
        }

        // If this function is called the first time after the user has logged in,
        // obtain and store in the session the handle of the user
        if (request.getSession().getAttribute("userHandle") == null) {
            // Obtain an instance of the User Retrieveal object
            UserRetrieval usrMgr = ActionsHelper.createUserRetrieval(request);
            // Get External User object for the currently logged in user
            ExternalUser extUser = usrMgr.retrieveUser(getLoggedInUserId(request));
            // Place handle of the user into session as attribute
            request.getSession().setAttribute("userHandle", extUser.getHandle());
        }

        // Prepare filter to select resources by the External ID of currently logged in user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
        Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                String.valueOf(getLoggedInUserId(request)));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);
        // Prepare filter to select resources that do not have any project assigned
//        Filter filterNoProject = ResourceFilterBuilder.createNoProjectFilter();
        // Prepare filterr to select resources that do not have any phase assigned
//        Filter filterNoPhase = ResourceFilterBuilder.createNoPhaseFilter();

        // The list that will contain all the individual
        // filters that will later be combined by the AndFilter
        List filters = new ArrayList();

        // Add individual filters to list
        filters.add(filterExtID);
/* Awaiting fixes in Resource Management component
        filters.add(filterNoProject);
        filters.add(filterNoPhase);*/

        // Create the main filter for this role-gathering operaion
        Filter filter = new AndFilter(filters);
        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Perform search for resources
        Resource[] resources = resMgr.searchResources(filter);

        // Iterate over all resources retrieved and take into
        // consideration only those ones that have Manager role
        for (int i = 0; i < resources.length; ++i) {
            // Temporary workaround until Resource Management component is fixed
            if (resources[i].getProject() != null || resources[i].getPhase() != null) {
                continue;
            }

            // Get the role this resource has
            ResourceRole role = resources[i].getResourceRole();
            // If this resource has the Manager role and no projects associated with it
            if (role.getName().equalsIgnoreCase(Constants.MANAGER_ROLE_NAME)) {
                // Add Global Manager role to the roles set
                roles.add(Constants.GLOBAL_MANAGER_ROLE_NAME);
                request.setAttribute("global_resource", resources[i]);
                // No need to iterate any further
                break;
            }
        }

        // Determine some common permissions
        request.setAttribute("isAllowedToViewInactiveProjects",
                new Boolean(hasUserPermission(request, Constants.VIEW_PROJECTS_INACTIVE_PERM_NAME)));
        request.setAttribute("isAllowedToCreateProject",
                new Boolean(hasUserPermission(request, Constants.CREATE_PROJECT_PERM_NAME)));
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
        // Call shorter version of this function first
        gatherUserRoles(request);

        // At this moment the request should have "roles" attribute
        Set roles = (Set)request.getAttribute("roles");

        // Create an instance of Project Manager
        ProjectManager projMgr = ActionsHelper.createProjectManager(request);
        // Retrieve the project with specified project ID
        Project project = projMgr.getProject(projectId);

        // If the project with specified ID does not exist, return
        if (project == null) {
            return;
        }

        // If this project is not public, remove "Public" role from the set
        if (!("Yes".equalsIgnoreCase((String)project.getProperty("Public")))) {
            roles.remove(Constants.PUBLIC_ROLE_NAME);
        }

        // Prepare filter to select resources by the External ID of currently logged in user
        Filter filterExtIDname = ResourceFilterBuilder.createExtensionPropertyNameFilter("External Reference ID");
        Filter filterExtIDvalue = ResourceFilterBuilder.createExtensionPropertyValueFilter(
                String.valueOf(getLoggedInUserId(request)));
        Filter filterExtID = new AndFilter(filterExtIDname, filterExtIDvalue);

        // Create filter to filter only the resources for the project in question
        Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(projectId);
        // Create combined final filter
        Filter filter = new AndFilter(filterExtID, filterProject);

        // Obtain an instance of Resource Manager
        ResourceManager resMgr = ActionsHelper.createResourceManager(request);
        // Perform search for resources
        Resource[] resources = resMgr.searchResources(filter);
        for (int i = 0; i < resources.length; i++) {
            ActionsHelper.populateEmailProperty(request, resources[i]);
        }
        // Plase resources for currently logged in user into the request
        request.setAttribute("myResources", resources);

        // Iterate over all resources and retrieve their roles
        for (int i = 0; i < resources.length; ++i) {
            // Get the role this resource has
            ResourceRole role = resources[i].getResourceRole();
            // Add the name of the role to the roles set (gather the role)
            roles.add(role.getName());
        }
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
     * This static method checks whether the logged in user has at least one role from the specified
     * array of resource roles. The information about logged in user is taken from the session
     * associated with the specified request.
     * <p>
     * Note, one of the <code>gatherUserRoles</code> static methods must be called prior making a
     * call to this method, or the result of this call will be <code>false</code>.
     * </p>
     *
     * @return <code>true</code> if the logged in user has at least one of the specified roles in
     *         current context, <code>false</code> if he doesn't.
     * @param request
     *            the <code>HttpServletRequest</code> to take the information about roles
     *            currently logged in user has in current context.
     * @param roles
     *            an array of roles to check.
     * @see #gatherUserRoles(HttpServletRequest)
     * @see #gatherUserRoles(HttpServletRequest, long)
     */
    public static boolean hasUserRole(HttpServletRequest request, String[] roles) {
        for (int i = 0; i < roles.length; ++i) {
            if (hasUserRole(request, roles[i])) {
                return true;
            }
        }

        return false;
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
