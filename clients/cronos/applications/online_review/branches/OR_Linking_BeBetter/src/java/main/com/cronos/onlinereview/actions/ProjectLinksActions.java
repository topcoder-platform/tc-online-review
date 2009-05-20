/*
 * Copyright (C) 2006-2007 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.topcoder.util.errorhandling.BaseException;

/**
 * <p>
 * This class contains Struts Actions that are meant to deal with Projects. There are following Actions defined in
 * this class.
 * <ul>
 * <li>Edit Project Link</li>
 * <li>Save Project Link</li>
 * </ul>
 * </p>
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 *
 * @author TCSDEVELOPER
 * @version 1.0
 * @since OR Project Linking Assembly
 */
public class ProjectLinksActions extends DispatchAction {

    /**
     * <p>
     * Edits project links for the given project.
     * </p>
     *
     * @param mapping action mapping.
     * @param form action form.
     * @param request the http request.
     * @param response the http response.
     * @return the action forward
     * @throws BaseException when any error happens while processing in TCS components.
     */
    public ActionForward editProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }



        return mapping.findForward(Constants.SUCCESS_FORWARD_NAME);
    }

}
