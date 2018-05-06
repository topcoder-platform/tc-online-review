/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file.failuretests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.file.TemplateData;
import com.topcoder.util.file.TemplateDataFormatException;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.xslttemplate.XmlTemplateData;
import com.topcoder.util.file.xslttemplate.XsltTemplate;

/**
 * @author AdamSelene
 *
 * This class tests XsltTemplate for proper failure.
 *
 * @author Chenhong
 * @version 2.1
 *
 * @since 2.0
 */
public class XsltTemplateTest extends TestCase {

    /**
     * Creates a attachment point for this testcase.
     *
     * @return a wonderful testsuite for this case.
     */
    public static Test suite() {
        return new TestSuite(XsltTemplateTest.class);
    }

    /**
     * Tests setTemplate for null arg failure.
     *
     */
    public void testSetTemplate() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate(null);
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for empty arg failure.
     *
     */
    public void testSetTemplateES() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("");
            fail("Did not fail.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }


    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_1() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%{}%");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_2() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%This line is bad");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_3() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%This line is bad");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_4() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%{%");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_5() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%}%");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests setTemplate for bad arg failure.
     *
     */
    public void testSetTemplateBad_6() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.setTemplate("%=%");
            fail("Did not fail.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for failure if the template has not been set before data is applied. This behavior is not
     * properly specified by the javadocs, so I am assuming TemplateFormatException is the closest declared exception.
     *
     */
    public void testApplyTemplateUnset() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("<DATA></DATA>");

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad XML data.
     *
     */
    public void testApplyTemplateBad() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("<DATA/>>>></DATA>");

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad XML data.
     *
     */
    public void testApplyTemplateBad2() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("</DATA>");

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad XML data.
     *
     */
    public void testApplyTemplateBad3() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("<DATA><TAG></DATA>");

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad XML data.
     *
     */
    public void testApplyTemplateBad5() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("<DATA><TAG><GREG></TAG></GREG></DATA>");

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad XML data.
     *
     */
    public void testApplyTemplateBad4() {
        XsltTemplate test = new XsltTemplate();
        XmlTemplateData xd = new XmlTemplateData();
        xd.setTemplateData("<DATA></TAG><TAG></DATA>");

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(xd);
            fail("Did not crash.");
        } catch (TemplateDataFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests applyTemplate for bad data failure.
     */
    public void testApplyTemplateWrongType() {
        XsltTemplate test = new XsltTemplate();
        TestTemplateData ttd = new TestTemplateData();

        try {
            test.setTemplate("# yeah yeah yeah");
        } catch (Exception e) {
            fail("Non dev exception");
        }

        try {
            test.applyTemplate(ttd);
            fail("Did not crash.");
        } catch (IllegalArgumentException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

    /**
     * Tests getTemplate for failure if the template has not been set before call. This behavior is not properly
     * specified by the javadocs, so I am assuming TemplateFormatException is the closest declared exception.
     */
    public void testGetFieldsUnset() {
        XsltTemplate test = new XsltTemplate();

        try {
            test.getFields();
            fail("Did not crash.");
        } catch (TemplateFormatException e) { /* expected */
        } catch (Exception e) {
            fail("Wrong exception - " + e.toString());
        }
    }

}

/**
 * A mocked class for testing.
 */
class TestTemplateData implements TemplateData {

    /**
     * @see com.topcoder.util.file.TemplateData#setTemplateData(java.lang.String)
     */
    public void setTemplateData(String templateData) throws IllegalArgumentException, TemplateDataFormatException {
        // Does nothing.
    }

    /**
     * @see com.topcoder.util.file.TemplateData#setTemplateData(com.topcoder.util.file.fieldconfig.TemplateFields)
     */
    public void setTemplateData(TemplateFields fields) throws IllegalArgumentException {
        // Does nothing.
    }

    /**
     *
     * @see com.topcoder.util.file.TemplateData#getTemplateData()
     */
    public String getTemplateData() {
        return null;
    }
}