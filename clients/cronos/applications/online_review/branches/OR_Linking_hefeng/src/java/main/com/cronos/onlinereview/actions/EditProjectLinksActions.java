/*
 * Copyright (C) 2009 TopCoder Inc.  All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectLinkInfo;
import com.topcoder.management.project.ProjectManager;

import com.topcoder.search.builder.filter.Filter;

import com.topcoder.util.errorhandling.BaseException;

import org.apache.commons.beanutils.DynaBean;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * This class contains Struts Actions that are meant to deal with Edit Project Links. There are following Actions
 * defined in this class:
 *  <ul>
 *      <li>View Edit Project Links Page</li>
 *      <li>Update Project Links</li>
 *  </ul>
 *  <p>This class is thread-safe as it does not contain any mutable inner state.</p>
 *
 * @author TCSASSEMBLER
 * @version 1.0
 */
public class EditProjectLinksActions extends DispatchAction {
    /**
     * This List defines the status that used to search the projects that can be selected in the drop down
     * list. All projects whose status is not 'deleted'(status id 3) can be selected.
     */
    private static final List<Long> SELECTABLE_PROJECT_STATUS_IDS = Arrays.asList(1L, 2L, 4L, 5L, 6L, 7L, 8L);

    /**
     * This method is used to populate the data that will be used by editProjectLinks.jsp. The data includes:
     * the project currently edited, all selectable  link types, all selectable projects, and the existing links of
     * this project. Note that current project will be excluded from the selectable drop down list.
     *
     * @param mapping the action mapping
     * @param form the action form
     * @param request the http request
     * @param response the http response
     *
     * @return &quot;displayPage&quot; forward, which forwards to the /jsp/editProjectLinks.jsp page (as defined in
     *         struts-config.xml file), or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect user input (such as
     *         absent submission id, or the lack of permissions, etc.).
     *
     * @throws BaseException if any error occurs.
     */
    public ActionForward viewProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws BaseException {
        LoggingHelper.logAction(request);

        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(mapping, getResources(request),
                request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);


        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve current project
        Project project = verification.getProject();

        ProjectManager pMgr = ActionsHelper.createProjectManager(request);

        request.setAttribute("linkTypes", pMgr.getAllProjectLinkTypes());

        request.setAttribute("selectableProjects", getSelectableProjects(pMgr, project.getId()));

        request.setAttribute("linkInfos", pMgr.getLinkedProjects(project.getId()));

        return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
    }

    /**
     * This method is used to save the project links submitted from editProjectLinks.jsp. The form fields
     * include:
     * -pid, Long, the id of the source project
     * -prjIds, Long[], the ids of the target projects
     * -lnkTypes, Long[], the type ids of the links to corresponding prjIds
     * The form is using LazyValidatorForm defined in struts-config.xml named 'projectLinksForm'.
     *
     * @param mapping the action mapping
     * @param form the action form
     * @param request the http request
     * @param response the http response
     *
     * @return &quot;success&quot; forward, which redirect to ViewProjectDetails.do for the given project id(as defined
     *         in struts-config.xml file), or &quot;userError&quot; forward, which forwards to the /jsp/userError.jsp
     *         page, which displays information about an error that is usually caused by incorrect user input (such as
     *         absent submission id, or the lack of permissions, etc.).
     *
     * @throws BaseException if any error occurs.
     */
    public ActionForward saveProjectLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
        HttpServletResponse response) throws BaseException {
        DynaBean dynaForm = (DynaBean) form;

        Long srcPrjId = (Long) dynaForm.get("pid");
        Long[] projectIds = (Long[]) dynaForm.get("prjIds");
        Long[] linkTypes = (Long[]) dynaForm.get("lnkTypes");

        //populate ProjectLinkInfoS
        List<ProjectLinkInfo> infos = new ArrayList<ProjectLinkInfo>();

        for (int i = 0; i < projectIds.length; i++) {
            Long prjId = projectIds[i];
            Long type = linkTypes[i];

            //neither of target project id and link type should be -1(not selected)
            //and the target project should not equal to source project
            //violated data will be omitted
            if ((prjId != null) && (prjId != -1) && (type != null) && (type != -1) && (srcPrjId != prjId)) {
                infos.add(new ProjectLinkInfo(prjId, type));
            }
        }

        ProjectManager pMgr = ActionsHelper.createProjectManager(request);

        pMgr.saveProjectLinkInfo(srcPrjId, infos.toArray(new ProjectLinkInfo[infos.size()]));

        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(mapping.findForward(Constants.SUCCESS_FORWARD_NAME),
            "&pid=" + srcPrjId);
    }

    /**
     * Loads projects that will be displayed in selectable projects drop down list in editProjectLinks.jsp. All
     * non 'deleted' projects excluding current project will be returned.
     *
     * @param mgr the ProjectManager used to load selectable projects.
     * @param currentPrjId id of current project
     *
     * @return an array of selectable projects
     *
     * @throws PersistenceException if any error occurs
     */
    private Project[] getSelectableProjects(ProjectManager mgr, long currentPrjId)
        throws PersistenceException {
        Filter projectsFilter = ProjectFilterUtility.buildStatusIdInFilter(SELECTABLE_PROJECT_STATUS_IDS);

        Project[] result = mgr.searchProjects(projectsFilter);

        List<Project> tmp = new ArrayList<Project>();

        for (Project prj : result) {
            //exclude current project
            if (prj.getId() != currentPrjId) {
                tmp.add(prj);
            }
        }

        return tmp.toArray(new Project[tmp.size()]);
    }
}
