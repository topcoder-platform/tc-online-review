/*
 * Copyright (C) 2006 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.util.MessageResources;

import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.external.impl.DBUserRetrieval;
import com.topcoder.management.phase.DefaultPhaseManager;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.ProjectManagerImpl;
import com.topcoder.management.resource.Notification;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.search.NotificationFilterBuilder;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseDateComparator;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class contains Struts Actions that are meant to deal with Project's details. There are
 * following Actions defined in this class:
 * <ul>
 * <li>View Project Details</li>
 * <li>Contact Manager</li>
 * <li>Upload Submission</li>
 * <li>Download Submission</li>
 * <li>Upload Final Fix</li>
 * <li>Download Final Fix</li>
 * <li>Upload Test Case</li>
 * <li>Download Test Case</li>
 * <li>Download Document</li>
 * <li>Delete Submission</li>
 * <li>View Auto Screening</li>
 * </ul>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSAssemblyTeam
 * @version 1.0
 */
public class ProjectDetailsActions extends DispatchAction {

    /**
     * Creates a new instance of the <code>ProjectDetailsActions</code> class.
     */
    public ProjectDetailsActions() {
    }

    /**
     * This method is an implementation of "View project Details" Struts Action defined for this
     * assembly, which is supposed to gather all possible information about the project and display
     * it to user.
     *
     * @return an action forward to the appropriate page. If no error has occured, the forward will
     *         be to viewProjectDetails.jsp page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewProjectDetails(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before processing with the Action
        List verification = checkForCorrectProjectId(mapping, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME);
        // The first object in the list denotes whether any error has occured
        if (verification.get(0) != null) {
            return (ActionForward)verification.get(0);
        }

        // Otherwise, retrieve all other objects from the list
        MessageResources messages = (MessageResources)verification.get(1);
        Project project = (Project)verification.get(3);

        // Retrieve some basic project info (such as icons' names) and place it into request
        retrieveAndStoreBaseProjectInfo(request, project, messages);

        PhaseManager phaseMgr = new DefaultPhaseManager("com.topcoder.management.phase");

        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(project.getId());
        Phase[] phases = phProj.getAllPhases(new PhaseDateComparator());

        request.setAttribute("phases", phases);

        String[] displayedStart = new String[phases.length];
        String[] displayedEnd = new String[phases.length];
        String[] originalStart = new String[phases.length];
        String[] originalEnd = new String[phases.length];
        Format format = new SimpleDateFormat("MM.dd.yyyy hh:mm a");
        String strOrigStartTime = messages.getMessage("viewProjectDetails.Timeline.OriginalStartTime") + " ";
        String strOrigEndTime = messages.getMessage("viewProjectDetails.Timeline.OriginalEndTime") + " ";

        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getActualStartDate() != null) {
                displayedStart[i] = format.format(phases[i].getActualStartDate());
                originalStart[i] = strOrigStartTime + format.format(phases[i].getScheduledStartDate());
            } else {
                displayedStart[i] = format.format(phases[i].getScheduledStartDate());
            }
            if (phases[i].getActualEndDate() != null) {
                displayedEnd[i] = format.format(phases[i].getActualEndDate());
                originalEnd[i] = strOrigEndTime + format.format(phases[i].getScheduledEndDate());
            } else {
                displayedEnd[i] = format.format(phases[i].getScheduledEndDate());
            }
        }

        request.setAttribute("displayedStart", displayedStart);
        request.setAttribute("displayedEnd", displayedEnd);
        request.setAttribute("originalStart", originalStart);
        request.setAttribute("originalEnd", originalEnd);

        ResourceManager resMgr = ActionsHelper.createResourceManager();

        if (AuthorizationHelper.hasUserPermission(request, Constants.VIEW_PROJECT_RESOURCES_PERM_NAME)) {
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
            Resource[] resources = resMgr.searchResources(filterProject);

            UserRetrieval usrMgr = new DBUserRetrieval("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
            ExternalUser[] users = new ExternalUser[resources.length];

            for (int i = 0; i < resources.length; ++i) {
                String userID = (String)resources[i].getProperty("External Reference ID");
                users[i] = usrMgr.retrieveUser(Long.valueOf(userID).longValue());
            }

            request.setAttribute("resources", resources);
            request.setAttribute("users", users);
        }

        boolean sendTLNotifications = false;

        if (AuthorizationHelper.isUserLoggedIn(request)) {
            Filter filterTNproject = NotificationFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterTNuser = NotificationFilterBuilder.createExternalRefIdFilter(
                    AuthorizationHelper.getLoggedInUserId(request));
            Filter filterTNtype = NotificationFilterBuilder.createNotificationTypeIdFilter(1);
            Filter filterTN = new AndFilter(filterTNproject, new AndFilter(filterTNuser, filterTNtype));

            Notification[] notifications = resMgr.searchNotifications(filterTN);
            sendTLNotifications = (notifications.length != 0);
        }

        request.setAttribute("sendTLNotifications", (sendTLNotifications) ? "On" : "Off");

        return mapping.findForward("success");
    }

    /**
     * This method is an implementation of "Contact Manager" Struts Action defined for this
     * assembly, which is supposed to send a message entered by user to the manager of some project.
     * This action gets executed twice -- once to display the page with the form, and once to
     * process the message entered by user on that form.
     *
     * @return an action forward to the appropriate page. If no error has occured and this action
     *         was called the the first time, the forward will be to contactManager.jsp page, which
     *         displays the form where user can enter his message. If this action was called during
     *         the post back (the second time), then the request should contain the message to send
     *         entered by user. In this case, this method verifies if everything is correct, sends
     *         the message to manager and returns the forward to the View Project Details page.
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward contactManager(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException {
        // Verify that certain requirements are met before processing with the Action
        List verification = checkForCorrectProjectId(mapping, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME);
        // The first object in the list denotes whether any error has occured
        if (verification.get(0) != null) {
            return (ActionForward)verification.get(0);
        }

        // Otherwise, retrieve all other objects from the list
        MessageResources messages = (MessageResources)verification.get(1);
        Project project = (Project)verification.get(3);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            retrieveAndStoreBaseProjectInfo(request, project, messages);
        }

        return mapping.findForward((postBack) ? "success" : "displayPage");
    }

    /**
     * TODO: Write sensible description for method uploadSubmission here
     *
     * @return TODO: Write sensible description of return value for method uploadSubmission
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward uploadSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method newMethod here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadSubmission here
     *
     * @return TODO: Write sensible description of return value for method downloadSubmission
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadSubmission here
        return null;
    }

    /**
     * TODO: Write sensible description for method uploadFinalFix here
     *
     * @return TODO: Write sensible description of return value for method uploadFinalFix
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward uploadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method uploadFinalFix here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadFinalFix here
     *
     * @return TODO: Write sensible description of return value for method downloadFinalFix
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadFinalFix(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadFinalFix here
        return null;
    }

    /**
     * TODO: Write sensible description for method uploadTestCase here
     *
     * @return TODO: Write sensible description of return value for method uploadTestCase
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward uploadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method uploadTestCase here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadTestCase here
     *
     * @return TODO: Write sensible description of return value for method downloadTestCase
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadTestCase(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadTestCase here
        return null;
    }

    /**
     * TODO: Write sensible description for method deleteSubmission here
     *
     * @return TODO: Write sensible description of return value for method deleteSubmission
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward deleteSubmission(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method deleteSubmission here
        return null;
    }

    /**
     * TODO: Write sensible description for method downloadDocument here
     *
     * @return TODO: Write sensible description of return value for method downloadDocument
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward downloadDocument(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method downloadDocument here
        return null;
    }

    /**
     * TODO: Write sensible description for method viewAutoScreening here
     *
     * @return TODO: Write sensible description of return value for method viewAutoScreening
     * @param mapping
     *            action mapping.
     * @param form
     *            action form.
     * @param request
     *            the http request.
     * @param response
     *            the http response.
     */
    public ActionForward viewAutoScreening(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) {
        // TODO: Add implementation of method viewAutoScreening here
        return null;
    }

    /**
     * This method verifies the request for ceratins conditions to be met. This includes verifying
     * if the user has specified an ID of the project he wants to perform an operation on, if the ID
     * of the project specified by user denotes existing project, and whether the user has rights to
     * perform the operation specified by <code>permission</code> parameter.
     *
     * @return the list of items. The first item in the list denotes action mapping the request
     *         should be immediately forwarded to if this item is not <code>null</code>. All
     *         subsequent items will exist only in the case the first item is <code>null</code>.
     *         The second item denotes <code>MessageResources</code> onject. The third item in the
     *         list denotes <code>ProjectManager</code> object. And the fourth item in the list
     *         denotes <code>Project</code> object. This <code>Project</code> object will be the
     *         project retrieved by project Id retrieved from the request as parameter. This list
     *         will always contain at least one (the first) object.
     * @param mapping
     *            action mapping.
     * @param request
     *            the http request.
     * @param permission
     *            permission to check against, or <code>null</code> if no check is requeired.
     * @throws BaseException
     *             if any error occurs.
     */
    private List checkForCorrectProjectId(ActionMapping mapping, HttpServletRequest request, String permission)
        throws BaseException {
        // Prepare list that will be returned as the result
        List result = new ArrayList();
        // It will always contain at least one element
        result.add(null);

        // Place index of the active tab into request.
        // For Action defined in this class this index always equals to zero, so no tab is active
        request.setAttribute("projectTabIndex", new Integer(0));

        // Message Resources to be used for this request
        MessageResources messages = getResources(request);
        // Add MessageResources object as the second returned value
        result.add(messages);

        if (permission == null || permission.trim().length() == 0) {
            permission = null;
        }

        // Verify that Project ID was specified and denotes correct project
        String pidParam = request.getParameter("pid");
        if (pidParam == null || pidParam.trim().length() == 0) {
            // Gather roles, so tabs will be displayed
            AuthorizationHelper.gatherUserRoles(request);
            // Place error title into request
            if (permission == null) {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
            } else {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
            }
            // Place error message (reason) into request
            request.setAttribute("errorMessage", messages.getMessage("Error.ProjectIdNotSpecified"));
            // Find appropriate forward
            result.set(0, mapping.findForward("userError"));
            return result;
        }

        long pid;

        try {
            // Try to convert specified pid parameter to its integer representation
            pid = Long.parseLong(pidParam, 10);
        } catch (NumberFormatException nfe) {
            // Gather roles, so tabs will be displayed
            AuthorizationHelper.gatherUserRoles(request);
            // Place error title into request
            if (permission == null) {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
            } else {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
            }
            // Place error message (reason) into request
            request.setAttribute("errorMessage", messages.getMessage("Error.ProjectIdNotSpecified"));
            // Find appropriate forward
            result.set(0, mapping.findForward("userError"));
            return result;
        }

        // Obtain an instance of Project Manager
        ProjectManager projMgr = new ProjectManagerImpl();
        // Store ProjectManager object as the third returned value
        result.add(projMgr);
        // Get Project by its id
        Project project = projMgr.getProject(pid);
        // Verify that project with given id exists
        if (project == null) {
            // Gather roles, so tabs will be displayed
            AuthorizationHelper.gatherUserRoles(request);
            // Place error title into request
            if (permission == null) {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title.General"));
            } else {
                request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
            }
            // Place error message (reason) into request
            request.setAttribute("errorMessage", messages.getMessage("Error.ProjectIdNotSpecified"));
            // Find appropriate forward
            result.set(0, mapping.findForward("userError"));
            return result;
        }

        // Store Project object as the fourth returned value
        result.add(project);

        // Gather the roles the user has for current request
        AuthorizationHelper.gatherUserRoles(request, pid);

        // If permission parameter was not null or empty string
        if (permission != null) {
            // Verify that this permission is granted for currently logged in user
            if (!AuthorizationHelper.hasUserPermission(request, permission)) {
                // Place error title into request
                request.setAttribute("errorTitle", messages.getMessage("Error.Title." + permission.replaceAll(" ", "")));
                // Place error message (reason) into request
                request.setAttribute("errorMessage", messages.getMessage("Error.ProjectIdNotSpecified"));
                // Find appropriate forward
                result.set(0, mapping.findForward("userError"));
                return result;
            }
        }

        // Place Project object into the request
        request.setAttribute("project", project);

        return result;
    }

    /**
     * This method helps gather some commonly used information about the project. When the
     * information has been gathered, this method places it into the request as attributes.
     *
     * @param request
     *            the http request.
     * @param project
     *            a project to get the info for.
     * @param messages
     *            message resources.
     */
    private static void retrieveAndStoreBaseProjectInfo(
            HttpServletRequest request, Project project, MessageResources messages) {
        // Retrieve the name of the Project Category icon
        String categoryIconName = ConfigHelper.getProjectCategoryIconName(project.getProjectCategory().getName());
        // And place it into request
        request.setAttribute("categoryIconName", categoryIconName);

        String rootCatalogID = (String) project.getProperty("Root Catalog ID");
        // Retrieve Root Catalog icon's filename
        String rootCatalogIcon = ConfigHelper.getRootCatalogIconNameSm(rootCatalogID);
        // Retrieve the name of Root Catalog for this project
        String rootCatalogName = messages.getMessage(ConfigHelper.getRootCatalogAltTextKey(rootCatalogID));

        // Place the filename of the icon for Root Catalog into request
        request.setAttribute("rootCatalogIcon", rootCatalogIcon);
        // Place the name of the Root Catalog for the current project into request
        request.setAttribute("rootCatalogName", rootCatalogName);
    }
}
