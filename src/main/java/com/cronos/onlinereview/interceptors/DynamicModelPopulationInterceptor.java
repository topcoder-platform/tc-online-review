/*
 * Copyright (C) 2013 - 2017 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.interceptors;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;

import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionInvocation;
import org.apache.struts2.ModelDriven;
import org.apache.struts2.interceptor.Interceptor;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.dispatcher.Parameter;
import org.apache.struts2.dispatcher.multipart.StrutsUploadedFile;



/**
 * This is the interceptor class which is used to intercept any ModelDriven
 * action classes, it will populate the parameters to the model.
 * <p>
 * This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * <p>
 * v2.1 - Changed in Migrate Struts 2.3 to 2.5 For Online Review
 * - the uploaded file in intercept method is instance of StrutsUploadedFile now.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.1
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
            HttpParameters params = ActionContext.getContext()
                                                      .getParameters();
            Map<String, Object> filteredParams = new HashMap<String, Object>();

            for (String key : params.keySet()) {
                System.out.println("Dynamic model key: " + key + " Value: " + params.get(key).getObject().toString());
                if (!key.contains(".") || key.matches("^.*[(].+\\..+[)]*$")) {
                    Object value = params.get(key).getObject();
                    if (value != null && value.getClass().isArray()) {
                        Object[] values = (Object[]) value;
                        if (values.length > 0) {
                            value = values[0];
                        }
                    }

                    if (value instanceof StrutsUploadedFile) {
                        String fileName = params.get(key + "FileName").getValue();
                        String contentType = params.get(key + "ContentType").getValue();

                        FormFile formFile = new FormFile(fileName,
                                 ((StrutsUploadedFile) value).getContent(), contentType);

                        filteredParams.put(key, formFile);
                    } else {
                        if (!key.contains("FileName") && !key.contains("ContentType")) {
                            filteredParams.put(key, params.get(key).getObject());
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
