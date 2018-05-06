/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import com.topcoder.util.file.templatesource.TemplateSource;

/**
 * <p>
 * This class implements TemplateSource interface. It is only used for testing.
 * </p>
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class MockTemplateSource implements TemplateSource {
    /**
     * <p>
     * Not implemented.
     * </p>
     * @param templateName
     *            not used
     * @param templateText
     *            not used
     */
    public void addTemplate(String templateName, String templateText) {
        // empty
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     * @param templateName
     *            not used
     * @return always return null
     */
    public String getTemplate(String templateName) {
        return null;
    }

    /**
     * <p>
     * Not implemented.
     * </p>
     * @param templateName
     *            not used
     */
    public void removeTemplate(String templateName) {
        // empty

    }

}
