/*
 * Copyright (C) 2013-2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.project;

import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.grpcclient.GrpcHelper;

/**
 * This class is the struts action class which is used for listing all projects.
 * <p>
 * Struts 2 Action objects are instantiated for each request, so there are no
 * thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SyncProjectAction extends BaseProjectAction {

    /**
     * Default constructor.
     */
    public SyncProjectAction() {
    }

    public String execute() throws BaseException {
        String projectId = request.getParameter("projectId");

        GrpcHelper.getSyncServiceRpc().saveProjectSync(Long.valueOf(projectId), false, false,
                false, false, true, false, false, false);

        // Signal about successful execution of the Action
        return "syncResult";
    }
}
