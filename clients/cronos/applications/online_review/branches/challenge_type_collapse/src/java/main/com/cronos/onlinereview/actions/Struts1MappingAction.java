/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions;

import java.util.Map;

/**
 * This class provides support for the old-style Struts1 requests. It redirects these requests to appropriate
 * Struts2 actions. Only GET requests are supported.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class Struts1MappingAction extends BaseServletAwareAction {
    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = -2929958981318117044L;

    /**
     * Used for dynamic result.
     */
    private String url;

    /**
     * Default constructor.
     */
    public Struts1MappingAction() {
    }

    /**
     * This method redirects old-style Struts1 requests to corresponding Struts2 actions.
     *
     * @return &quot;dynamic&quot; result, which forwards to dynamically constructed URL. If the request method is not
     * "GET", it returns &quot;errorPage&quot; result.
     *
     */
    public String execute() {
        String method = request.getMethod();
        if (!method.equalsIgnoreCase("GET")) {
            return "errorPage";
        }
        Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
        
        int count = 0;
        StringBuilder urlBuilder = new StringBuilder(request.getRequestURI().replace(".do",""));
        for(String key : parameters.keySet()) {
            if (!key.equalsIgnoreCase("method")) {
                for(String value : parameters.get(key)) {
                    urlBuilder.append((count == 0 ? "?" : "&")).append(key+"="+value);
                    count++;
                }
            }
        }
        
        url = urlBuilder.substring(urlBuilder.indexOf("/actions/"));
        return "dynamic";
    }

    /**
     * Getter of url.
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter of url.
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

}
