/*
 * Copyright (C) 2013 - 2017 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.interceptors;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.struts2.ServletActionContext;

import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.model.FormFile;
import org.apache.struts2.ActionContext;
import org.apache.struts2.ActionInvocation;
import org.apache.struts2.ModelDriven;
import org.apache.struts2.interceptor.Interceptor;
import org.apache.struts2.dispatcher.HttpParameters;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;
import org.apache.struts2.dispatcher.multipart.StrutsUploadedFile;
import org.apache.struts2.dispatcher.multipart.UploadedFile;



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
 * <p>
 * v2.2 - Fixed in Migrate Struts 2.5 to 7.0.0 For Online Review
 * - Fixed file upload functionality by creating a temporary file from StrutsUploadedFile content
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.2
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
        HttpServletRequest currentRequest = invocation.getInvocationContext().getServletRequest();
        MultiPartRequestWrapper multiWrapper = currentRequest instanceof HttpServletRequestWrapper wrapper
            ? findMultipartRequestWrapper(wrapper)
            : null;

        // Check if model is Dynamic Model
        Object action = invocation.getAction();

        if (action instanceof ModelDriven
                && ((ModelDriven<?>) action).getModel() instanceof DynamicModel) {
            // Populate the parameters:
            HttpParameters params = ActionContext.getContext().getParameters();
            Map<String, Object> filteredParams = new HashMap<String, Object>();

            if (multiWrapper == null) {
                for (String key : params.keySet()) {
                    System.out.println("Dynamic model key: " + key + " Value: " + params.get(key).getObject().toString());
                    if (!key.contains(".") || key.matches("^.*[(].+\\..+[)]*$")) {
                        filteredParams.put(key, params.get(key).getObject());
                    }
                }
            } else {
                // normal fields
                Enumeration<String> paramNames = multiWrapper.getParameterNames();
                while (paramNames != null && paramNames.hasMoreElements()) {
                    String key = paramNames.nextElement();
                    if (!key.contains(".") || key.matches("^.*[(].+\\..+[)]*$")) {
                        filteredParams.put(key, params.get(key).getObject());
                    }
                }

                // file fields
                Enumeration<String> fileParameterNames = multiWrapper.getFileParameterNames();
                while (fileParameterNames != null && fileParameterNames.hasMoreElements()) {
                    String key = fileParameterNames.nextElement();
                    if (!key.contains(".") || key.matches("^.*[(].+\\..+[)]*$")) {
                        UploadedFile[] uploadedFiles = multiWrapper.getFiles(key);

                        if (uploadedFiles != null && uploadedFiles.length > 0) {
                            UploadedFile uploadedFile = uploadedFiles[0];

                            String fileName = uploadedFile.getOriginalName();
                            String contentType = uploadedFile.getContentType();

                            FormFile formFile = new FormFile(fileName, ((StrutsUploadedFile) uploadedFile).getContent(), contentType);
                            filteredParams.put(key, formFile);
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

    /**
      * Find multipart request wrapper
      * @param request current request
      * @return the wrapper
      */
    private MultiPartRequestWrapper findMultipartRequestWrapper(HttpServletRequestWrapper request) {
        if (request instanceof MultiPartRequestWrapper multiPartRequestWrapper) {
            return multiPartRequestWrapper;
        } else if (request.getRequest() instanceof HttpServletRequestWrapper wrappedRequest) {
            return findMultipartRequestWrapper(wrappedRequest);
        }
        return null;
    }
}
