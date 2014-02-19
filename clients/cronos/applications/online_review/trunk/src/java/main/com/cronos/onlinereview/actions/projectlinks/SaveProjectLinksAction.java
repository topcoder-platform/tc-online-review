/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectlinks;

import java.util.ArrayList;
import java.util.List;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.link.ProjectLinkManager;
import com.topcoder.util.errorhandling.BaseException;

/**
 * This class is the struts action class which is used for saving project links page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveProjectLinksAction extends DynamicModelDrivenAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 4579747864497179019L;

    /**
     * <p>
     * Constant for link action of "delete".
     * </p>
     */
    private static final String LINK_ACTION_DELETE = "delete";

    /**
     * Represents the project id.
     */
    private long pid;

    /**
     * Creates a new instance of the <code>SaveProjectLinksAction</code> class.
     */
    public SaveProjectLinksAction() {
    }

    /**
     * <p>
     * Saves project links for the given project.
     * </p>
     *
     * @return the action result
     * @throws BaseException when any error happens while processing in TCS components.
     */
    public String execute() throws BaseException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(this,
            request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        Project project = verification.getProject();

        Long[] destIds = (Long[]) getModel().get("link_dest_id");
        Long[] typeIds = (Long[]) getModel().get("link_type_id");
        String[] actions = (String[]) getModel().get("link_action");

        List<Long> destList = new ArrayList<Long>();
        List<Long> typeList = new ArrayList<Long>();
        for (int i = 1; i < destIds.length; i++) {
            if (!LINK_ACTION_DELETE.equals(actions[i])) {
                destList.add(destIds[i]);
                typeList.add(typeIds[i]);
            }
        }

        ProjectLinkManager linkManager = ActionsHelper.createProjectLinkManager();

        long[] paramDestProjectIds = new long[destList.size()];
        long[] paramTypeIds = new long[typeList.size()];
        for (int i = 0; i < paramDestProjectIds.length; i++) {
            paramDestProjectIds[i] = destList.get(i);
            paramTypeIds[i] = typeList.get(i);
        }
        linkManager.updateProjectLinks(project.getId(), paramDestProjectIds, paramTypeIds);

        setPid(project.getId());
        // Return success forward
        return Constants.SUCCESS_FORWARD_NAME;
    }

    /**
     * Getter of pid.
     * @return the pid
     */
    public long getPid() {
        return pid;
    }

    /**
     * Setter of pid.
     * @param pid the pid to set
     */
    public void setPid(long pid) {
        this.pid = pid;
    }
}

