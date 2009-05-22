package com.cronos.onlinereview.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.DynaBean;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectLinkInfo;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;

public class EditProjectLinksActions extends DispatchAction {
    
    private static final List<Long> SELECTABLE_PROJECT_STATUS_IDS = Arrays.asList(1L, 2L, 4L, 5L, 6L, 7L, 8L);
    
    public ActionForward viewProjectLinks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        
        LoggingHelper.logAction(request);
        // Verify that certain requirements are met before processing with the Action
        CorrectnessCheckResult verification = ActionsHelper.checkForCorrectProjectId(
                mapping, getResources(request), request, Constants.EDIT_PROJECT_DETAILS_PERM_NAME, false);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getForward();
        }

        // Retrieve a review to view
        Project project = verification.getProject();
        
        ProjectManager pMgr = ActionsHelper.createProjectManager(request);

        request.setAttribute("linkTypes", pMgr.getAllProjectLinkTypes());
        
        request.setAttribute("selectableProjects", getSelectableProjects(pMgr, project.getId()));
        
        request.setAttribute("linkInfos", pMgr.getLinkedProjects(project.getId()));
        
        return mapping.findForward(Constants.DISPLAY_PAGE_FORWARD_NAME);
    }
    
    public ActionForward saveProjectLinks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
        throws BaseException{
        
        DynaBean dynaForm = (DynaBean)form;
        
        Long srcPrjId = (Long) dynaForm.get("pId");
        Long[] projectIds = (Long[])dynaForm.get("prjIds");
        Long[] linkTypes = (Long[])dynaForm.get("lnkTypes");
        
        List<ProjectLinkInfo> infos = new ArrayList<ProjectLinkInfo>();
        for (int i=0; i<projectIds.length;i++){
            Long prjId = projectIds[i];
            Long type = linkTypes[i];
            
            if (prjId != null && prjId != -1 && type!= null && type != -1 && srcPrjId!=prjId){
                infos.add(new ProjectLinkInfo(prjId, type));
            }
        }
        
        ProjectManager pMgr = ActionsHelper.createProjectManager(request);
        
        pMgr.saveProjectLinkInfo(srcPrjId, infos.toArray(new ProjectLinkInfo[infos.size()]));
        
        // Return success forward
        return ActionsHelper.cloneForwardAndAppendToPath(
                mapping.findForward(Constants.SUCCESS_FORWARD_NAME),"&pid=" + srcPrjId);
    }
    
    private Project[] getSelectableProjects(ProjectManager mgr, long currentPrjId) throws PersistenceException{
        Filter projectsFilter = ProjectFilterUtility.buildStatusIdInFilter(SELECTABLE_PROJECT_STATUS_IDS);
        
        Project[] result = mgr.searchProjects(projectsFilter);
        
        List<Project> tmp = new ArrayList<Project>();
        for (Project prj:result){
            //exclude current project
            if (prj.getId() != currentPrjId){
                tmp.add(prj);
            }
        }
        
        return tmp.toArray(new Project[tmp.size()]);
    }
}
