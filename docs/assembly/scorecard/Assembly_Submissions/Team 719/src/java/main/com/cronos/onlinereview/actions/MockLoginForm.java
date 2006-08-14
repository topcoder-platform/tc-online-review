package com.cronos.onlinereview.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class MockLoginForm extends ActionForm {
    private String userIdText;
    private String userName;
    private String password;
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
//        try {
//            new Long(this.userIdText);
//        } catch (Exception e) {
//            ActionErrors errors = new ActionErrors();
//            errors.add("userIdText",
//                    new ActionMessage("Invalid user Id", false));
//            return errors;
//        }
        if (this.userName != null && this.userName.trim().length() > 0
                && this.password != null && this.password.trim().length() > 0) {
            this.userIdText = "10";
            return null;
        }
        ActionErrors errors = new ActionErrors();
        errors.add("userName", new ActionMessage("Login error.", false));
        return errors;
    }

    public String getUserIdText() {
        return userIdText;
    }

    public void setUserIdText(String userIdText) {
        this.userIdText = userIdText;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
