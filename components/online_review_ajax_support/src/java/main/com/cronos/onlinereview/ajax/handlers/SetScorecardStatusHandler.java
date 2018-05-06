/*
 * Copyright (C) 2006-2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.ajax.handlers;

import com.cronos.onlinereview.ajax.AjaxRequest;
import com.cronos.onlinereview.ajax.AjaxResponse;
import com.cronos.onlinereview.ajax.AjaxSupportHelper;
import com.cronos.onlinereview.ajax.ConfigurationException;
import com.topcoder.management.scorecard.PersistenceException;
import com.topcoder.management.scorecard.ScorecardManager;
import com.topcoder.management.scorecard.data.Scorecard;
import com.topcoder.management.scorecard.data.ScorecardStatus;

/**
 * <p>
 * Ajax request handler class which handles the "Set Scorecard Status" Ajax operation.
 * This class extends the CommonHandler abstract class, and uses the resource manager defined in its parent class,
 * plus an instance of the ScorecardManager class in order to implement the "Set Scorecard Status" operation.
 *
 * The scorecard must be not in use when the scorecard status to be set is "Inactive",
 * otherwise an Ajax response will be returned with an error status.<br><br>
 * Any successful or failed operation is logged using the Logging Wrapper component.
 * </p>
 * <p>
 * <strong>Thread Safety:</strong>
 * This class is immutable an thread safe. Any manager class used by this handler is supposed to be thread safe.
 * </p>
 *
 * @author topgear
 * @author assistant
 * @version 1.1
 */
public class SetScorecardStatusHandler extends CommonHandler {

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
     * Represents the status of invalid scorecard error.
     */
    private static final String INVALID_SCORECARD_ERROR = "Invalid scorecard error";


    /**
     * <p>
     * The scorecard manager used to get scorecard data
     * This variable is immutable, it is initialized by the constructor to a not null ScorecardManager object,
     * and used by the service method.
     * </p>
     */
    private final ScorecardManager scorecardManager;

    /**
     * <p>
     * Represents scorecard status with name "Active", it is used to assign the "Active" status to a scorecard.
     * This variable is immutable, it is initialized by the constructor to a not null ScorecardStatus
     * obtained from the ScorecardManager of the parent class, and used by the service method.
     * </p>
     */
    private final ScorecardStatus activeScorecardStatus;

    /**
     * <p>
     * Represents scorecard status with name "Inactive", it is used to assign the "Inactive" status to a scorecard.
     * This variable is immutable, it is initialized by the constructor to a not null ScorecardStatus
     * obtained from the ScorecardManager of the parent class, and used by the service method.
     * </p>
     */
    private final ScorecardStatus inactiveScorecardStatus;

    /**
     * <p>
     * Creates an instance of this class and initialize its internal state.
     * </p>
     *
     * @throws ConfigurationException if there is a configuration exception
     */
    public SetScorecardStatusHandler() throws ConfigurationException {

        try {
            scorecardManager = (ScorecardManager) AjaxSupportHelper.createObject(ScorecardManager.class);

            // get all the score cards
            ScorecardStatus[] statuses = scorecardManager.getAllScorecardStatuses();
            // get the active and inactive statuses
            ScorecardStatus active = null;
            ScorecardStatus inactive = null;
            for (ScorecardStatus status : statuses) {
                if (status.getName().equals("Active")) {
                    active = status;
                } else if (status.getName().equals("Inactive")) {
                    inactive = status;
                }
            }

            if (active == null) {
                throw new ConfigurationException("No active status found.");
            }
            if (inactive == null) {
                throw new ConfigurationException("No inactive status found.");
            }

            activeScorecardStatus = active;
            inactiveScorecardStatus = inactive;
        } catch (Exception e) {
            if (e instanceof ConfigurationException) {
                throw (ConfigurationException) e;
            }
            throw new ConfigurationException("Something error when loading configurations.");
        }
    }

    /**
     * <p>
     * Performs the "Set Scorecard Status" operation and return the success or failure Ajax response.
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
        long scorecardId;
        String status;

        // ScorecardId
        try {
            scorecardId = request.getParameterAsLong("ScorecardId");
        } catch (NumberFormatException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The scorecard id should be a long value.", "SetScorecardStatus. " + "User id : " + userId, e);
        }

        status = request.getParameter("Status");
        if (status == null || (!status.equals("Active") && !status.equals("Inactive"))) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_PARAMETER_ERROR,
                    "The status must be Active or Inactive.", "SetScorecardStatus. " + "User id : " + userId);
        }

        // check the userId for validation
        if (userId == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), LOGIN_ERROR,
                    "Doesn't login or expired.", "SetScorecardStatus. " + "User id : " + userId);
        }

        // check the user has global manager role
        try {
            if (!checkUserHasGlobalManagerRole(userId)) {
                return AjaxSupportHelper.createAndLogError(request.getType(), ROLE_ERROR,
                        "The user should have global manager role.", "SetScorecardStatus. " + "User id : " + userId);
            }
        } catch (ResourceException e) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Error when check user has gloable manager role.", "SetScorecardStatus. " + "User id : " + userId,
                    e);
        }

        // get the scorecard from the manager
        Scorecard card;
        try {
            card = this.scorecardManager.getScorecard(scorecardId);
        } catch (PersistenceException e1) {
            return AjaxSupportHelper.createAndLogError(request.getType(), BUSINESS_ERROR,
                    "Error when get scorecard.", "SetScorecardStatus. " + "User id : " + userId
                    + "\tscorecard : " + scorecardId, e1);
        }
        if (card == null) {
            return AjaxSupportHelper.createAndLogError(request.getType(), INVALID_SCORECARD_ERROR,
                    "Can't find the scorecard.", "SetScorecardStatus. " + "User id : " + userId
                    + "\tcard id : " + scorecardId);
        }

        // set the status
        if (status.equals("Active")) {
            card.setScorecardStatus(activeScorecardStatus);
        } else {
            card.setScorecardStatus(inactiveScorecardStatus);
        }

        // upload the scorecard
        try {
            this.scorecardManager.updateScorecard(card, userId.toString());
        } catch (Exception e) {
            return AjaxSupportHelper.createAndLogError(request.getType(),
                    BUSINESS_ERROR, "Can't update review.",
                    "SetScorecardStatus. User id : " + userId + "\tscorecard id : "
                    + scorecardId + "\tstatus" + status, e);
        }

        return AjaxSupportHelper.createAndLogSuccess(request.getType(), SUCCESS, "", null,
                "SetScorecardStatus. User id : " + userId + "\tscorecard id : " + scorecardId);
    }

}
