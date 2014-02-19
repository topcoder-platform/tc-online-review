/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.latedeliverables;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.actions.DynamicModelDrivenAction;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;

/**
 * This is the base class for project late deliverables actions classes.
 * It provides the basic functions which will be used by all project late deliverables actions.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public abstract class BaseLateDeliverableAction extends DynamicModelDrivenAction {

    /**
     * <p>Gets the late deliverable matching the specified ID.</p>
     *
     * @param lateDeliverableId a <code>long</code> providing the ID of late deliverable to retrieve.
     * @return a <code>LateDeliverable</code> matching the specified ID or <code>null</code> if requested late
     *         deliverable could not be found.
     * @throws LateDeliverableManagementException if any error occurs.
     */
    protected LateDeliverable getLateDeliverable(long lateDeliverableId)
        throws LateDeliverableManagementException {
        LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();
        return lateDeliverableManager.retrieve(lateDeliverableId);
    }

    /**
     * <p>Gets the ID for a user associated with the resource assigned to specified late deliverable.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> providing the data for late deliverable.
     * @return a <code>long</code> providing the ID of a user who is associated with the resource set for specified
     *         late deliverable.
     * @throws ResourcePersistenceException if an unexpected error occurs.
     */
    protected long getLateDeliverableUserId(LateDeliverable lateDeliverable)
        throws ResourcePersistenceException {
        ResourceManager resourceManager = ActionsHelper.createResourceManager();
        Resource lateDeliverableResource = resourceManager.getResource(lateDeliverable.getResourceId());
        return Long.parseLong(String.valueOf(lateDeliverableResource.getProperty("External Reference ID")));
    }

    /**
     * <p>Sets the request with various attributes for specified late deliverable to be consumed by the JSP.</p>
     *
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @param lateDeliverable a <code>LateDeliverable</code> providing the data for late deliverable.
     * @param lateDeliverableOwner <code>true</code> if current user is late member associated with late deliverable;
     *        <code>false</code> otherwise.
     * @throws PersistenceException if an unexpected error occurs.
     */
    protected void setEditLateDeliverableRequest(HttpServletRequest request, LateDeliverable lateDeliverable,
                                               boolean lateDeliverableOwner) throws PersistenceException {
        boolean canEditLateDeliverables
            = AuthorizationHelper.hasUserPermission(request, Constants.EDIT_LATE_DELIVERABLE_PERM_NAME);

        request.setAttribute("lateDeliverable", lateDeliverable);
        request.setAttribute("project",
                             ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId()));

        boolean explanationEditable = lateDeliverableOwner && lateDeliverable.getExplanation() == null &&
            ActionsHelper.explanationDeadline(lateDeliverable).compareTo(new Date()) > 0;
        request.setAttribute("isExplanationEditable", explanationEditable);

        request.setAttribute("isResponseEditable",
                             canEditLateDeliverables && !lateDeliverableOwner
                             && (lateDeliverable.getResponse() == null)
                             && (lateDeliverable.getExplanation() != null));
        request.setAttribute("isJustifiedEditable", canEditLateDeliverables && !lateDeliverableOwner);
        request.setAttribute("isFormSubmittable",
                             (canEditLateDeliverables && !lateDeliverableOwner) || explanationEditable);
    }
}
