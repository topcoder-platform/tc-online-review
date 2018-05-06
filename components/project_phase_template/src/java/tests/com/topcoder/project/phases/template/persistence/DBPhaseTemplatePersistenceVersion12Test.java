/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.persistence;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.Workdays;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.Helper;
import com.topcoder.project.phases.template.TestHelper;

/**
 * DBPhaseTemplatePersistenceVersion12Test.
 *
 * @author TCSDEVELOPER
 * @version 1.2
 */
public class DBPhaseTemplatePersistenceVersion12Test {

    /**
     * <p>
     * Configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "test_files" + File.separator + "config" + File.separator
        + "Project_Phase_Template_Config.xml";

    /**
     * <p>
     * An instance of DBPhaseTemplatePersistence for testing use.
     * </p>
     */
    private DBPhaseTemplatePersistence persistence;

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

        return new JUnit4TestAdapter(DBPhaseTemplatePersistenceVersion12Test.class);
    }

    /**
     * Sets the up.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Before
    public void setUp() throws Exception {

        TestHelper.initializeDB();
        Helper.loadConfig(CONFIG_FILE);
        persistence = new DBPhaseTemplatePersistence(TestHelper.getDBConnectionFactory(), "test");

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
        TestHelper.clearDB();
    }

    /**
     * Test method for DBPhaseTemplatePersistence#generatePhases(String, Project, long[]).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testGeneratePhases() throws Exception {

        persistence.generatePhases("New_Design", project, new long[]{7});
        Assert.assertEquals("The phases array size is 7", 9, project.getAllPhases().length);

        Helper.assertDependencies(project, 8L, 6L);

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
        this.persistence.generatePhases("New_Design", project, new long[]{3, 3, 5});
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
        this.persistence.generatePhases("New_Design", project, new long[]{3, 11});
    }
}
