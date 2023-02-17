/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.event;

import com.cronos.onlinereview.actions.BaseServletAwareAction;
import com.topcoder.onlinereview.component.exception.BaseException;

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
        return NONE;
    }
}
