/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.event;

import com.cronos.onlinereview.actions.BaseServletAwareAction;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.topcoder.util.errorhandling.BaseException;

/**
 * EventBusHandleAppealAction is used to fire the appeal submit event to
 * the event bus when the appeal is submitted by the user
 * 
 * It's added in Topcoder - Online Review Update - Post to Event Bus Part 2 v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class EventBusHandleAppealAction extends BaseServletAwareAction {

    /**
     * Execute the action
     *
     * @throws BaseException if any error occurs
     * @return the execution result
     */
    public String execute() throws BaseException {
        String challengeId = request.getParameter("challengeId");
        long userId = AuthorizationHelper.getLoggedInUserId(request);
        EventBusServiceClient.fireSubmissionCreateEvent(Long.parseLong(challengeId), userId, null, null, 0L, 
                EventBusServiceClient.SUBMISSION_TYPE_ID_FOR_APPEAL);
        return NONE;
    }
}
