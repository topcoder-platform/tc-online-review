package com.cronos.onlinereview.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class MockLoginAction extends Action {

    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        request.getSession(true).setAttribute(
                ScorecardActionsHelper.getInstance()
                        .getUserIdSessionAttributeKey(),
                new Long(((MockLoginForm) form).getUserIdText()));
        return mapping.findForward("welcome");
    }
}
