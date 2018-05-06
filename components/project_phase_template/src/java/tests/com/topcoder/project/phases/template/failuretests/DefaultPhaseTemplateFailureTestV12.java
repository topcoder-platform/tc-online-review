/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.failuretests;

import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.template.DefaultPhaseTemplate;
import com.topcoder.project.phases.template.PhaseTemplatePersistence;
import com.topcoder.project.phases.template.StartDateGenerator;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;
import com.topcoder.project.phases.template.startdategenerator.RelativeWeekTimeStartDateGenerator;

/**
 * Failure test for DefaultPhaseTemplate that changed in version 1.2
 *
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class DefaultPhaseTemplateFailureTestV12 extends TestCase {
    /**
     * The phaseTemplatePersistence used to initialize defaultPhaseTemplate.
     */
    private PhaseTemplatePersistence phaseTemplatePersistence;

    /**
     * The startDateGenerator used to initialize defaultPhaseTemplate.
     */
    private StartDateGenerator startDateGenerator;

    /**
     * The workdays.
     */
    private Workdays workdays;

    /**
     * The template name.
     */
    private String templateName;

    /**
     * The defaultPhaseTemplate used in test.
     */
    private DefaultPhaseTemplate defaultPhaseTemplate;

    /**
     * Set up for each test.
     *
     * @throws Exception to jUnit
     */
    protected void setUp() throws Exception {
        FailureTestsHelper.loadAllConfig();
        phaseTemplatePersistence = new XmlPhaseTemplatePersistence(
        	new String[] { "test_files/failuretests/template_file1.xml" });
        startDateGenerator = new RelativeWeekTimeStartDateGenerator(1, 0, 0, 0, 0);
        workdays = new DefaultWorkdays();
        templateName = "Design";
        defaultPhaseTemplate = new DefaultPhaseTemplate(
        	phaseTemplatePersistence, startDateGenerator, workdays);
    }

    /**
     * Clear for each test.
     *
     * @throws Exception to jUnit
     */
    protected void tearDown() throws Exception {
        FailureTestsHelper.clearTestConfig();
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[])'.
     * Null templateName.
     */
    public void testApplyTemplate1_TemplateNameIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("", (long[])null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[])'.
     * Empty templateName.
     */
    public void testApplyTemplate1_TemplateNameIsEmpty() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("    ", (long[])null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[])'.
     * unknown phase id.
     */
    public void testApplyTemplate1_PhaseIdIsUnknown() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 90001 });
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[])'.
     * unknown phase id.
     */
    public void testApplyTemplate1_PhaseIdIsDuplicated() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 1, 1 });
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], Date)'.
     * Null templateName.
     */
    public void testApplyTemplate2_TemplateNameIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("", (long[])null, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], Date)'.
     * Empty templateName.
     */
    public void testApplyTemplate2_TemplateNameIsEmpty() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("    ", (long[])null, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate2_PhaseIdIsUnknown() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 90001 }, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate2_PhaseIdIsDuplicated() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 1, 1 }, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], Date)'.
     * null Date.
     */
    public void testApplyTemplate2_StartDateIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 1, 1 }, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * Null templateName.
     */
    public void testApplyTemplate3_TemplateNameIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("", (long[])null, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * Empty templateName.
     */
    public void testApplyTemplate3_TemplateNameIsEmpty() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("    ", (long[])null, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate3_PhaseIdIsUnknown() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 90001 }, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate3_PhaseIdIsDuplicated() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, null, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * null Date.
     */
    public void testApplyTemplate3_DateIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 3, 3 }, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * leftOutPhaseIds contains varPhaseId.
     */
    public void testApplyTemplate3_LeftOutPhaseIdsContainsVarPhaseId() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 1, 3 }, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date)'.
     * leftOutPhaseIds contains fixedPhaseId.
     */
    public void testApplyTemplate3_LeftOutPhaseIdsContainsFixedPhaseId() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 2, 3 }, 1, 2, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * Null templateName.
     */
    public void testApplyTemplate4_TemplateNameIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("", (long[])null, 1, 2, new Date(System.currentTimeMillis() + 10000000),
            	new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * Empty templateName.
     */
    public void testApplyTemplate4_TemplateNameIsEmpty() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate("    ", (long[])null, 1, 2, new Date(System.currentTimeMillis() + 10000000),
            	new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate4_PhaseIdIsUnknown() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 90001 }, 1, 2,
            	new Date(System.currentTimeMillis() + 10000000), new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * unknown phase id.
     */
    public void testApplyTemplate4_PhaseIdIsDuplicated() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, null, 1, 2,
            	new Date(System.currentTimeMillis() + 10000000), new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * null Date.
     */
    public void testApplyTemplate4_DateIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 3, 3 }, 1, 2,
            	new Date(System.currentTimeMillis() + 10000000), null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * null fixedPhaseStartDate.
     */
    public void testApplyTemplate4_FixedPhaseStartDateIsNull() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 3, 3 }, 1, 2,
            	null, new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * fixedPhaseStartDate is before startDate.
     */
    public void testApplyTemplate4_FixedPhaseStartDateBeforeStartDate() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 3, 3 }, 1, 2,
            	new Date(System.currentTimeMillis() - 10000000), new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * leftOutPhaseIds contains varPhaseId.
     */
    public void testApplyTemplate4_LeftOutPhaseIdsContainsVarPhaseId() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 1, 3 }, 1, 2,
            	new Date(System.currentTimeMillis() + 10000000), new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Test method for
     * 'com.topcoder.project.phases.template.DefaultPhaseTemplate.applyTemplate(String, long[], long, long, Date, Date)'.
     * leftOutPhaseIds contains fixedPhaseId.
     */
    public void testApplyTemplate4_LeftOutPhaseIdsContainsFixedPhaseId() throws Exception {
        try {
            defaultPhaseTemplate.applyTemplate(templateName, new long[] { 2, 3 }, 1, 2,
            	new Date(System.currentTimeMillis() + 10000000), new Date());
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}