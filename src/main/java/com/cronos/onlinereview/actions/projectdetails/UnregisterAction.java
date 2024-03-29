/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.Comparators;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseManager;
import com.topcoder.onlinereview.component.resource.Resource;

/**
 * This class is the struts action class which is used for unregistering from a project.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class UnregisterAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 5779728620125539072L;

    /**
     * Creates a new instance of the <code>UnregisterAction</code> class.
     */
    public UnregisterAction() {
    }

    /**
     * This method is an implementation of &quot;Unregister&quot; Struts Action defined for
     * this assembly, which is supposed to unregister the logged in submitter from a project
     * (denoted by <code>pid</code> parameter). This action gets executed twice &#x96; once to
     * display the page with the confirmation, and once to process the confirmed request to
     * actually perform the unregistration.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmUnregistration.jsp
     *         If this action was called during the post back (the second time), then this method
     *         verifies if everything is correct, and proceeds with the unregisration.
     *         After this it returns a forward to the View Project Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                this, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Check the user has submitter role and registration phase is open
        boolean isSubmitter = AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME);

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(this,
                    request, "Unregistration", "Error.NoPermission", Boolean.FALSE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(false);
        com.topcoder.onlinereview.component.project.phase.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
        Phase[] phases = phProj.getAllPhases(new Comparators.ProjectPhaseComparer());

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);

        // check if registration phase is open
        boolean registrationOpen = false;
        for (int i = 0; i < activePhases.length && !registrationOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.REGISTRATION_PHASE_NAME)) {
                registrationOpen = true;
            }
        }

        if (!registrationOpen) {
            return ActionsHelper.produceErrorReport(this,
                    request, "Unregistration", "Error.RegistrationClosed", null);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("unregister") != null);

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);

            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        Resource[] allProjectResources = ActionsHelper.getAllResourcesForProject(verification.getProject());

        boolean found = false;
        boolean hasOtherRoles = false;
        for (int i = 0; i < allProjectResources.length && (!found || !hasOtherRoles); i++) {
            Long userId = allProjectResources[i].getUserId();

            if (userId != null && userId.equals(AuthorizationHelper.getLoggedInUserId(request))) {
                if (allProjectResources[i].getResourceRole().getName().equalsIgnoreCase(Constants.SUBMITTER_ROLE_NAME)) {
                    ActionsHelper.deleteProjectResult(verification.getProject(), userId, allProjectResources[i].getResourceRole().getId());
                    ActionsHelper.createResourceManager().removeResource(allProjectResources[i],
                            Long.toString(AuthorizationHelper.getLoggedInUserId(request)));

                    found = true;
                } else {
                    hasOtherRoles = true;
                }
            }
        }

        this.setPid(verification.getProject().getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

