/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * The servlet of web demo.
 * </p>
 * @author flexme
 * @version 3.1
 * @since 3.1
 */
public class DocumentServlet extends HttpServlet {
    /**
     * <p>
     * Represents the serial version unique id.
     * </p>
     */
    private static final long serialVersionUID = -8981275298065576222L;

    /**
     * <p>
     * Process the web post request.
     * </p>
     *
     * @param request
     *          the HTTP servlet request
     * @param response
     *          the HTTP servlet response
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String configFile = (String) request.getParameter("config_file");
        String namespace  = (String) request.getParameter("config_namespace");
        String sourceId = (String) request.getParameter("source_id");
        String templateFile = (String) request.getParameter("template_file");
        String dataFile = (String) request.getParameter("data_file");
        try {
            Util.checkString(configFile, "configuration file");
            Util.checkString(namespace, "configuration namespace");
            Util.checkString(sourceId, "source id");
            Util.checkString(templateFile, "template file");
            Util.checkString(dataFile, "xml data file");
        } catch (IllegalArgumentException e) {
            writeResult(response, e.getMessage());
        }
        try {
            ConfigurationObject config = TestHelper.createConfigurationObject(configFile, namespace);
            // Create DocumentGenerator from factory
            DocumentGenerator docGen = DocumentGeneratorFactory.getDocumentGenerator(config);
            Template buildTemplate = docGen.getTemplate(sourceId, templateFile);
            String res = docGen.applyTemplate(buildTemplate, Util.getBufferedReaderFromFileOrResource(dataFile));
            writeResult(response, "<pre>" + res + "</pre>");
        } catch (Exception e) {
            writeResult(response, "<pre>" + e.getMessage() + "</pre>");
        }
    }

    /**
     * <p>
     * Write result to client.
     * </p>
     *
     * @param response
     *          the HTTP servlet response
     * @param result
     *          the message to send to client
     */
    private static void writeResult(HttpServletResponse response, String result) {
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            // igonre
        }
    }
}
