/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.persistence;

import java.util.Calendar;
import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.Helper;

/**
 * XmlPhaseTemplatePersistenceVersion12Test.
 *
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class XmlPhaseTemplatePersistenceVersion12Test {

    /**
     * <p>
     * Template file for "Design" template.
     * </p>
     */
    private static final String TEMPLATE_FILE_DESIGN = "test_files/new_template_12/new_file.xml";

    /**
     * <p>
     * Template file for "Design" template.
     * </p>
     */
    private static final String TEMPLATE_FILE_DESIGN2 = "test_files/new_template_12/new_file_2.xml";

    /**
     * <p>
     * Template name for "Design" template.
     * </p>
     */
    private static final String TEMPLATE_DESIGN = "new_file";

    /**
     * <p>
     * Configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "test_files/config/Project_Phase_Template_Config.xml";

    /**
     * <p>
     * Instance of <code>XmlPhaseTemplatePersistence</code> used in this test.
     * </p>
     */
    private XmlPhaseTemplatePersistence persistence2 = null;

    /**
     * <p>
     * Instance of <code>Project</code> used in this test.
     * </p>
     */
    private Project project = null;

    /**
     * <p>
     * Instance of <code>Workdays</code> used in this test.
     * </p>
     */
    private Workdays workdays = null;

    /**
     * <p>
     * Instance of <code>Date</code> as the start date used in this test.
     * </p>
     */
    private Date startDate = null;

    /**
     * Suite.
     *
     * @return the junit.framework. test
     */
    public static junit.framework.Test suite() {

        return new JUnit4TestAdapter(XmlPhaseTemplatePersistenceVersion12Test.class);
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Before
    public void setUp() throws Exception {

        // load configurations
        Helper.loadConfig(CONFIG_FILE);

        // create persistence instances
        persistence2 = new XmlPhaseTemplatePersistence(new String[]{TEMPLATE_FILE_DESIGN});

        // create needed objects
        startDate = Calendar.getInstance().getTime();
        workdays = new DefaultWorkdays();
        project = new Project(startDate, (DefaultWorkdays) workdays);
    }

    /**
     * Tear down.
     *
     * @throws Exception
     *             the exception occurs
     */
    @After
    public void tearDown() throws Exception {

        Helper.clearConfig();
    }

    /**
     * Test method for XmlPhaseTemplatePersistence#generatePhases(String, Project, long[]).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testGeneratePhases() throws Exception {

        // generate phases
        persistence2.generatePhases(TEMPLATE_DESIGN, project, new long[]{3, 5});
        // check the generated phases
        // Helper.checkSamplePhases();

        Assert.assertEquals("The array size is 5", 2 + 2 + 1, project.getAllPhases().length);

        Helper.assertDependencies(project, 6, 4L, 2L, 1L);
    }

    /**
     * Test method for XmlPhaseTemplatePersistence#generatePhases(String, Project, long[]).
     * Failure test. the left out array has the duplicate value
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePhasesFailure() throws Exception {

        // generate phases
        this.persistence2.generatePhases(TEMPLATE_DESIGN, project, new long[]{3, 3, 5});
    }

    /**
     * Test method for XmlPhaseTemplatePersistence#generatePhases(String, Project, long[]).
     * Failure test. the left out array do not has the valid phase id
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGeneratePhasesFailure2() throws Exception {

        // generate phases
        this.persistence2.generatePhases(TEMPLATE_DESIGN, project, new long[]{3, 8});
    }

    /**
     * Test method for XmlPhaseTemplatePersistence#generatePhases(String, Project, long[]).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testGeneratePhases2() throws Exception {

        persistence2 = new XmlPhaseTemplatePersistence(new String[]{TEMPLATE_FILE_DESIGN2});
        // generate phases
        persistence2.generatePhases(TEMPLATE_DESIGN, project, new long[]{1, 2});
        // check the generated phases
        // Helper.checkSamplePhases();

        Assert.assertEquals("The array size is 0", 0, project.getAllPhases().length);

    }
}
