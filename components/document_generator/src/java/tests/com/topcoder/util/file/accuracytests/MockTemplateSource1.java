/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.accuracytests;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.TemplateSource;

/**
 * A TemplateSource class that actually does nothing. Used to identify the created template sources in accuracy tests.
 * @author peony
 * @version 3.0
 */
public class MockTemplateSource1 implements TemplateSource {
    /**
     * An empty constructors that does nothing.
     * @param id
     *            useless
     * @param configuration
     *            useless
     */
    public MockTemplateSource1(String id, ConfigurationObject configuration) {
    }

    /**
     * Does nothing.
     * @param templateName
     *            useless
     * @return "mock1"
     */
    public String getTemplate(String templateName) {
        return "mock1";
    }

    /**
     * Simply write a string "mock1_add" to a file in name of the given templateName.
     * @param templateName
     *            the file name
     * @param templateText
     *            useless
     */
    public void addTemplate(String templateName, String templateText) {
        try {
            PrintWriter p = new PrintWriter(new FileWriter(templateName));
            p.print("mock1_add");
            p.close();
        } catch (Exception e) {
            // Ignore it
        }
    }

    /**
     * Simply write a string "mock1_remove" to a file in name of the given templateName.
     * @param templateName
     *            the file name
     */
    public void removeTemplate(String templateName) {
        try {
            PrintWriter p = new PrintWriter(new FileWriter(templateName));
            p.print("mock1_remove");
            p.close();
        } catch (Exception e) {
            // Ignore it
        }
    }
}
