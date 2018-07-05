/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.event;

import com.cronos.onlinereview.actions.BaseServletAwareAction;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.data.Review;
import com.topcoder.management.scorecard.data.ScorecardType;
import com.topcoder.util.errorhandling.BaseException;

/**
 * EventBusHandleAppealResponseAction is used to fire the review update event to
 * the event bus when the appeal response is submitted
 * 
 * It's added in Topcoder - Online Review Update - Post to Event BUS v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class EventBusHandleAppealResponseAction extends BaseServletAwareAction {

    /**
     * Execute the action
     *
     * @throws BaseException if any error occurs
     * @return the execution result
     */
    public String execute() throws BaseException {
        String reviewId = request.getParameter("reviewId");
        long userId = AuthorizationHelper.getLoggedInUserId(request);

        ReviewManager revManager = ActionsHelper.createReviewManager();
        Review rev = revManager.getReview(Long.parseLong(reviewId));
        ScorecardType scType = ActionsHelper.createScorecardManager().getScorecard(rev.getScorecard()).getScorecardType();
        
        EventBusServiceClient.fireReviewUpdate(rev, Long.parseLong(rev.getCreationUser()), userId, scType.getName());

        return NONE;
    }
}
