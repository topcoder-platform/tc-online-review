/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template;

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
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;

/**
 * The Class DemoVersion12.
 *
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class DemoVersion12 {

    /**
     * <p>
     * Template file for "Design" template.
     * </p>
     */
    private static final String TEMPLATE_FILE_DESIGN = "test_files/new_template_12/new_file.xml";

    /**
     * <p>
     * Configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "test_files/new_template_12/Project_Phase_Template_Config.xml";

    /**
     * <p>
     * Template name for "Design" template.
     * </p>
     */
    private static final String TEMPLATE_DESIGN = "new_file";

    /**
     * <p>
     * Instance of <code>XmlPhaseTemplatePersistence</code> used in this test.
     * </p>
     */
    private PhaseTemplatePersistence persistence2 = null;

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

        return new JUnit4TestAdapter(DemoVersion12.class);
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Before
    public void setUp() throws Exception {

        Helper.loadConfig(CONFIG_FILE);
        // create persistence instances
        persistence2 = new XmlPhaseTemplatePersistence(new String[]{TEMPLATE_FILE_DESIGN});

        // create needed objects
        startDate = Calendar.getInstance().getTime();
        workdays = new DefaultWorkdays();
        project = new Project(startDate, (DefaultWorkdays) workdays);
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    @After
    public void tearDown() throws Exception {

        Helper.clearConfig();
    }

    /**
     * Tests for the demo usage of XmlPhaseTemplatePersistence and DefaultPhaseTemplate's new method.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testDemo() throws Exception {

        // dependency[Name(Id)]: A(1),B(2),C(3),D(4),E(5),F(6),G(7)
        // Assume that we have phases A, B, C, D, E and F with the following dependencies:
        // Phase C depends on phases A and B.
        // Phase D depends on phase C.
        // Phase E depends on phase B, D, C.
        // Phase F depends on phase D, E.
        // Phase G depends on phase E, C.
        // In this case if phases C and E are left out, this component must create phases A, B, D, F and G
        // with the following dependencies:
        // Phase F depends on phases A, D and B.

        // generate phases
        persistence2.generatePhases(TEMPLATE_DESIGN, project, new long[]{3, 5});

        Assert.assertEquals("The array size is 5", 2 + 2 + 1, project.getAllPhases().length);

        Helper.assertDependencies(project, 6, 4L, 2L, 1L);
    }

    /**
     * Test demo1.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testDemo1() throws Exception {

        // dependency[Name(Id)]: A(1),B(2),C(3),D(4),E(5),F(6),G(7)
        // Assume that we have phases A, B, C, D, E and F with the following dependencies:
        // Phase C depends on phases A and B.
        // Phase D depends on phase C.
        // Phase E depends on phase B, D, C.
        // Phase F depends on phase D, E.
        // Phase G depends on phase E, C.
        // In this case if phases C and E are left out, this component must create phases A, B, D, F and G
        // with the following dependencies:
        // Phase F depends on phases A, D and B.

        PhaseTemplate persistence = new DefaultPhaseTemplate(
            "com.topcoder.project.phases.template.DefaultPhaseTemplate");
        project = persistence.applyTemplate("new_file", new long[]{3, 5});
        Assert.assertEquals("The array size is 5", 2 + 2 + 1, project.getAllPhases().length);

        Helper.assertDependencies(project, 6, 4L, 2L, 1L);
    }

}
