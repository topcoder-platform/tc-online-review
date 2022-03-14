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
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to allow the user to complete the appeals early.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class EarlyAppealsAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -4721732535834248862L;

    /**
     * Creates a new instance of the <code>EarlyAppealsAction</code> class.
     */
    public EarlyAppealsAction() {
    }

    /**
     * This method is an implementation of &quot;EarlyAppeals&quot; Struts Action defined for
     * this assembly, which allows the logged in submitter from a project (denoted by <code>pid</code>
     * parameter) to mark his appeals completed or to resume appealing. This action gets executed twice &#x96;
     * once to display the page with the confirmation, and once to process the confirmed request to
     * actually perform the action.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to /jsp/confirmEarlyAppeals.jsp
     *         If this action was called during the post back (the second time), then this method
     *         verifies if everything is correct, and proceeds with the unregistration.
     *         After this it returns a forward to the View Project Details page.
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                this, request, Constants.VIEW_PROJECT_DETAIL_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Check the user has submitter role and appeals phase is open
        boolean isSubmitter = AuthorizationHelper.hasUserRole(request,
                Constants.SUBMITTER_ROLE_NAME);

        if (!isSubmitter) {
            return ActionsHelper.produceErrorReport(this,
                    request, "Early Appeals", "Error.NoPermission", Boolean.FALSE);
        }

        PhaseManager phaseMgr = ActionsHelper.createPhaseManager(false);
        com.topcoder.project.phases.Project phProj = phaseMgr.getPhases(verification.getProject().getId());
        Phase[] phases = phProj.getAllPhases(new Comparators.ProjectPhaseComparer());

        // Obtain an array of all active phases of the project
        Phase[] activePhases = ActionsHelper.getActivePhases(phases);

        // check if appeals phase is open
        boolean appealsOpen = false;
        for (int i = 0; i < activePhases.length && !appealsOpen; i++) {
            if (activePhases[i].getPhaseType().getName().equalsIgnoreCase(Constants.APPEALS_PHASE_NAME)) {
                appealsOpen = true;
            }
        }

        if (!appealsOpen) {
            return ActionsHelper.produceErrorReport(this,
                    request, "Early Appeals", "Error.AppealsClosed", null);
        }

        // At this point, redirect-after-login attribute should be removed (if it exists)
        AuthorizationHelper.removeLoginRedirect(request);

        Resource submitter = ActionsHelper.getMyResourceForRole(request, "Submitter");
        if (submitter == null) {
            throw new BaseException("Unable to find the Submitter resource " +
                    "associated with the current user for project " + verification.getProject().getId());
        }

        // get appeals completed early property value
        boolean appealsCompletedFlag = false;
        String value = (String) submitter.getProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY);
        if (value != null && value.equals(Constants.YES_VALUE)) {
            appealsCompletedFlag = true;
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("perform") != null);
        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);

            request.setAttribute("complete", !appealsCompletedFlag);

            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }

        // Get the name (id) of the user performing the operations
        String operator = Long.toString(AuthorizationHelper.getLoggedInUserId(request));

        // Obtain the instance of the Resource Manager
        ResourceManager resourceManager = ActionsHelper.createResourceManager();

        // set appeals completed early property
        submitter.setProperty(Constants.APPEALS_COMPLETED_EARLY_PROPERTY_KEY,
                appealsCompletedFlag ? Constants.NO_VALUE : Constants.YES_VALUE);

        // update resource
        resourceManager.updateResource(submitter, operator);

        setPid(verification.getProject().getId());
        return Constants.SUCCESS_FORWARD_NAME;
    }
}

