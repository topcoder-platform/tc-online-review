/*
 * Copyright (C) 2006-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.search.ResourceFilterBuilder;

/**
 * <p>
 * Ajax request handler class which handles the "Set Timeline Notification" Ajax operation.
 * This class extends the CommonHandler abstract class, and uses the resource manager defined in its parent class,
 * plus an instance of the ProjectManager class in order to implement the "Set Timeline Notification" operation.
 *
 * Any successful or failed operation is logged using the Logging Wrapper component.
 * </p>
 *
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe. any manager class used by this handler is supposed to be thread safe.
 * </p>
 *
 * @author topgear, assistant, VolodymyrK
 * @version 1.1.2
 */
public class SetTimelineNotificationHandler extends CommonHandler {

    /**
     * Represents the status of success.
     */
    private static final String SUCCESS = "Success";

    /**
     * Represents the status of business error.
     */
    private static final String BUSINESS_ERROR = "Business error";

    /**
     * Represents the status of role error.
     */
    private static final String ROLE_ERROR = "Role error";

    /**
     * Represents the status of login error.
     */
    private static final String LOGIN_ERROR = "Login error";

    /**
     * Represents the status of invalid parameter error.
     */
    private static final String INVALID_PARAMETER_ERROR = "Invalid parameter error";

    /**
     * <p>
     * The project manager used to get project data
     * This variable is immutable, it is initialized by the constructor to a not null ProjectManager object,
     * and used by the service method.
     * </p>
     */
    private final ProjectManager projectManager;

    /**
     * <p>
     * The id of the timeline notification type with name "Timeline Notification".
     * It is used when adding or removing a timeline notification.
     * This variable is immutable, it is initialized by the constructor to a negative/0/positive long number,
     * and used by the service method.
     * </p>
     */
    private final long timelineNotificationId;

    /**
     * <p>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public  SetTimelineNotificationHandler() throws ConfigurationException {

        try {
            projectManager = (ProjectManager) AjaxSupportHelper.createObject(ProjectManager.class);

            // get all notification types
            NotificationType[] types = getResourceManager().getAllNotificationTypes();
            boolean found = false;
            long id = 0;
            for (NotificationType type : types) {
                if (type.getName().equals("Timeline Notification")) {
                    id = type.getId();
                    found = true;
                    break;
                }
            }
            if (!found) {
                throw new ConfigurationException("The timeline notification id can't be found.");
            }

            timelineNotificationId = id;
        } catch (Exception e) {
            if (e instanceof ConfigurationException) {
                throw (ConfigurationException) e;
            }
            throw new ConfigurationException("Something error in loading configuration.", e);
        }
    }

    /**
     * <p>
     * Performs the "Set Timeline Notification" operation and return the success or failure Ajax response.
     * </p>
     *
     * @return the response to the request
     * @param request the request to service
     * @param userId the id of user who issued this request
     * @throws IllegalArgumentException if the request is null
     */
    public AjaxResponse service(AjaxRequest request, Long userId) {
        if (request == null) {
            throw new IllegalArgumentException("The request should not be null.");
        }

        // check the request parameters
        long projectId;
        String status;

        try {
            projectId = request.getParameterAsLong("ProjectId");
        } catch (NumberFormatException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The project id should be a long value.", "SetTimelineNotification. User id : " + userId, e);
        }

        // status
        status = request.getParameter("Status");
        if (!status.equals("On") && !status.equals("Off")) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The status must be On or Off.", "SetTimelineNotification. User id : " + userId
                    + "\tStatus : " + status);
        }

        // check the userId for validation
        if (userId == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), LOGIN_ERROR,
                    "Doesn't login or expired.", "SetTimelineNotification. User id : " + userId);
        }

        // check whether the project is : one of the user's projects or the user has global manager role
        // if doesn't fit any of the two, create and return a "Role error"
        boolean accessible = false;

        Project project;
        try {
            project = this.projectManager.getProject(projectId);
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Can't get the project.", "SetTimelineNotification. User id : " + userId
                    + "\tproject id : " + projectId, e);
        }
        if (project == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Can't get the project.", "SetTimelineNotification. User id : " + userId
                    + "\tproject id : " + projectId);
        }

        // check whether is one of the user's projects
        try {
            // get the project's resources
            Resource[] resources = getResourceManager().searchResources(ResourceFilterBuilder.createProjectIdFilter(projectId));
            for (Resource resource : resources) {
                if (userId != null && userId.equals(resource.getUserId())) {
                    accessible = true;
                    break;
                }
            }
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Can't get the user's projects.", "SetTimelineNotification. User id : " + userId, e);
        }

        // check whether the user has global manager role
        if (!accessible) {
            try {
                if (checkUserHasGlobalManagerRole(userId)) {
                    accessible = true;
                }
            } catch (ResourceException e) {
                return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                        "Can't check the user's role : " + e, "SetTimelineNotification. User id : " + userId, e);
            }
        }

        if (!accessible) {
            return AjaxSupportHelper.createAndLogError(request.getType(), ROLE_ERROR,
                    "Can't access this project.", "SetTimelineNotification. User id : " + userId);
        }

        // add/remove the timeline notification status
        // if the status is on
        try {
            if (status.equals("On")) {
                getResourceManager().addNotifications(
                        new long[] {userId},
                        projectId,
                        timelineNotificationId,
                        userId.toString());
            } else {
                getResourceManager().removeNotifications(
                        new long[] {userId},
                        projectId,
                        timelineNotificationId,
                        userId.toString());
            }
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Can't add/remove notification : " + e, "SetTimelineNotification. User id : "
                    + userId + "\tproject id : " + projectId + "\ttimelineNotifcationId : " + timelineNotificationId, e);
        }

        return AjaxSupportHelper.createAndLogSuccess(request.getType(), SUCCESS,
                "Succeeded to set timeline notification.", null, "SetTimelineNotification. User id : "
                + userId + "\tproject id : " + projectId + "\ttimelineNotificationId : " + timelineNotificationId);

    }
}
