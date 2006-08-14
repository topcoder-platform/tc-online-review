package com.cronos.onlinereview.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class MockLoginForm extends ActionForm {
    private String userIdText;

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        try {
            new Long(this.userIdText);
        } catch (Exception e) {
            ActionErrors errors = new ActionErrors();
            errors.add("userIdText",
                    new ActionMessage("Invalid user Id", false));
            return errors;
        }
        return null;
    }

    public String getUserIdText() {
        return userIdText;
    }

    public void setUserIdText(String userIdText) {
        this.userIdText = userIdText;
    }

}
