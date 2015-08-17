/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectreview;

import java.io.PrintWriter;
import java.io.StringWriter;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used to view the scorecard.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ViewScorecardAction extends BaseViewOrExportGenericReviewAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -879839954394387197L;


    /**
     * This method is an implementation of &quot;View Scorecard&quot; Struts Action.
     *
     * @return &quot;success&quot;, which forwards to the /jsp/viewScorecard.jsp page (as
     *         defined in struts.xml file), or &quot;userError&quot; forward, which forwards
     *         to the /jsp/userError.jsp page, which displays information about the error).
     * @throws BaseException
     *             if any error occurs.
     */
    public String execute() throws BaseException {

        try {
            // At this point, redirect-after-login attribute should be removed (if it exists)
            AuthorizationHelper.removeLoginRedirect(request);

            LoggingHelper.logAction(request);

            CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
            if (!verification.isSuccessful()) {
                return verification.getResult();
            }

            // Gather the roles the user has for current request
            AuthorizationHelper.gatherUserRoles(request);

            // Verify that Scorecard ID was specified and denotes correct scorecard
            String scidParam = request.getParameter("scid");
            if (scidParam == null || scidParam.trim().length() == 0) {
                return (ActionsHelper.produceErrorReport(
                    this, request, null, "Error.ScorecardIdNotSpecified", null));
            }

            long scid;

            try {
                // Try to convert specified scid parameter to its integer representation
                scid = Long.parseLong(scidParam, 10);
            } catch (NumberFormatException e) {
                return (ActionsHelper.produceErrorReport(
                    this, request, null, "Error.ScorecardIdInvalid", null));
            }

            // Obtain an instance of Scorecard Manager
            ScorecardManager scrMgr = ActionsHelper.createScorecardManager();
            Scorecard scorecardTemplate = null;
            try {
                // Get Scorecard by its id
                scorecardTemplate = scrMgr.getScorecard(scid);
            } catch (PersistenceException e) {
                // Eat the exception
            }

            // Verify that scorecard with specified ID exists
            if (scorecardTemplate == null) {
                return (ActionsHelper.produceErrorReport(
                    this, request, null, "Error.ScorecardNotFound", null));
            }

            // Verify the scorecard is active
            if (scorecardTemplate.getScorecardStatus().getId() != ACTIVE_SCORECARD) {
                return (ActionsHelper.produceErrorReport(
                    this, request, null, "Error.ScorecardNotActive", null));
            }

            // Place Scorecard template in the request
            request.setAttribute("scorecardTemplate", scorecardTemplate);

            // Signal about successful execution of the Action
            return Constants.SUCCESS_FORWARD_NAME;
        } catch (Throwable e) {
            StringWriter buf = new StringWriter();
            e.printStackTrace(new PrintWriter(buf));
            LoggingHelper.logError(buf.toString());
            throw new BaseException(e);
        }
    }
}

