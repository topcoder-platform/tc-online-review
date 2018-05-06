/*
 * Copyright (C) 2008 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.util.file.templatesource.TemplateSource;

/**
 * A TemplateSource class that actually does nothing. Used to identify the created template sources in accuracy tests.
 * @author peony
 * @version 3.0
 */
public class MockTemplateSource2 implements TemplateSource {
    /**
     * An empty constructors that does nothing.
     * @param id
     *            useless
     * @param configuration
     *            useless
     */
    public MockTemplateSource2(String id, ConfigurationObject configuration) {
    }

    /**
     * Does nothing.
     * @param templateName
     *            useless
     * @return null
     */
    public String getTemplate(String templateName) {
        return "mock2";
    }

    /**
     * Does nothing.
     * @param templateName
     *            useless
     * @param templateText
     *            useless
     */
    public void addTemplate(String templateName, String templateText) {
    }

    /**
     * Does nothing.
     * @param templateName
     *            useless
     */
    public void removeTemplate(String templateName) {
    }
}
