/*
 * Copyright (C) 2013 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.interceptors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;

import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.Interceptor;


/**
 * This is the interceptor class which is used to intercept any ModelDriven
 * action classes, it will populate the parameters to the model.
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class DynamicModelPopulationInterceptor implements Interceptor {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 8166183568925763406L;

    /**
     * The destroy method, currently do nothing.
     */
    public void destroy() {
        // do nothing
    }

    /**
     * The init method, currently do nothing.
     */
    public void init() {
        // do nothing
    }

    /**
     * This will intercept the request, and populate the parameters to the
     * model, if the action implements ModelDriven interface and the model is a
     * DynamicModel.
     *
     * In addition, it sets the model to the request attribute with the model
     * name, so that the JSP can access the model via JSP EL.
     *
     * @param invocation
     *            the current action invocation
     * @return the intercept result
     * @throws Exception if any error
     *
     */
    public String intercept(ActionInvocation invocation)
        throws Exception {
        // Check if model is Dynamic Model
        Object action = invocation.getAction();

        if (action instanceof ModelDriven
                && ((ModelDriven<?>) action).getModel() instanceof DynamicModel) {
            // Populate the parameters:
            Map<String, Object> params = ActionContext.getContext()
                                                      .getParameters();
            Map<String, Object> filteredParams = new HashMap<String, Object>();

            for (String key : params.keySet()) {
                if (!key.contains(".") || key.matches("^.*[(].+\\..+[)]*$")) {
                    Object value = ((Object[]) params.get(key))[0];

                    if (value instanceof File) {
                        String fileName = (String) ((Object[]) params.get(key
                                + "FileName"))[0];
                        String contentType = (String) ((Object[]) params.get(key
                                + "ContentType"))[0];

                        FormFile formFile = new FormFile(fileName,
                                (File) value, contentType);

                        filteredParams.put(key, formFile);
                    } else {
                        if (!key.contains("FileName") && !key.contains("ContentType")) {
                            filteredParams.put(key, params.get(key));
                        }
                    }
                }
            }

            DynamicModel model = (DynamicModel) ((ModelDriven<?>) action).getModel();

            BeanUtils.populate(model, filteredParams);

            // / set model to the request:
            HttpServletRequest request = ServletActionContext.getRequest();
            request.setAttribute(model.getName(), model);
        }

        return invocation.invoke();
    }
}
