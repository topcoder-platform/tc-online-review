/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;

/**
 * @author AdamSelene This class tests XsltTemplate for proper failure.
 */
public class XmlTemplateDataTest extends TestCase {

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(XmlTemplateDataTest.class);
    }

    /**
     * Tests setTemplateData for null arg failure.
     */
    public void testSetTemplate() {
        XmlTemplateData test = new XmlTemplateData();

        try {
            test.setTemplateData((String) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception.");
        }
    }

    /**
     * Tests setTemplateData for empty arg failure.
     */
    public void testSetTemplateES() {
        XmlTemplateData test = new XmlTemplateData();

        try {
            test.setTemplateData("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception.");
        }
    }

    /**
     * Tests setTemplateData for null arg failure.
     */
    public void testSetTemplate_2() {
        XmlTemplateData test = new XmlTemplateData();

        try {
            test.setTemplateData((TemplateFields) null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception.");
        }
    }

    /**
     * Tests getTemplateData() for unset failure.
     */
    public void testGetTemplate() {
        XmlTemplateData test = new XmlTemplateData();

        try {
            test.getTemplateData();
            fail("Did not fail.");
        } catch (IllegalStateException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception.");
        }
    }

}