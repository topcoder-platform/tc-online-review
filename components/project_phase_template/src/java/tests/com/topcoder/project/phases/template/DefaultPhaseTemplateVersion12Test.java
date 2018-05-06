/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.date.workdays.Workdays;
import com.topcoder.date.workdays.WorkdaysUnitOfTime;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.persistence.DBPhaseTemplatePersistence;

/**
 * DefaultPhaseTemplateVersion12Test.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class DefaultPhaseTemplateVersion12Test {

    /**
     * <p>
     * Configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "test_files" + File.separator + "config" + File.separator
        + "Project_Phase_Template_Config.xml";

    /**
     * <p>
     * Bad configuration file.
     * </p>
     */
    private static final String BAD_CONFIG_FILE = "test_files" + File.separator + "bad_config"
        + File.separator + "Default_Phase_Template_Bad_Config.xml";

    /**
     * <p>
     * Configuration namespace for <code>DefaultPhaseTemplate</code>.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.project.phases.template.DefaultPhaseTemplate";

    /**
     * <p>
     * Represent an instance of DefaultPhaseTemplate for testing use.
     * </p>
     */
    private DefaultPhaseTemplate template;

    /**
     * <p>
     * Represent an instance Calendar for testing use.
     * </p>
     */
    private Calendar calendar = null;

    /**
     * <p>
     * Represent an instance Date for testing use.
     * </p>
     */
    private Date fixedDate = null;

    /**
     * Suite.
     *
     * @return the junit.framework. test
     */
    public static junit.framework.Test suite() {

        return new JUnit4TestAdapter(DefaultPhaseTemplateVersion12Test.class);
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
        Helper.loadConfig(BAD_CONFIG_FILE);

        // create the template
        template = new DefaultPhaseTemplate(NAMESPACE);

        // set the db persistence
        template.setPersistence(new DBPhaseTemplatePersistence(TestHelper.getDBConnectionFactory(), "test"));

        // initializeDB
        TestHelper.initializeDB();

        calendar = Calendar.getInstance();
        calendar.set(2002, 2, 10);
        fixedDate = calendar.getTime();
        calendar.set(2002, 1, 11);
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
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[]).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testApplyTemplateStringLongArray() throws Exception {

        Project project = template.applyTemplate("New_Design", new long[]{3, 4});
        Assert.assertEquals("The project's phases number is 8", 8, project.getAllPhases().length);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[]).
     * Failure test, the leftOutPhaseIds contains unknown id
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyTemplateFailure() throws Exception {

        template.applyTemplate("New_Design", new long[]{11});
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], Date).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testApplyTemplateStringLongArrayDate() throws Exception {

        Project project = template.applyTemplate("New_Design", new long[]{3, 4}, new Date());
        Assert.assertEquals("The project's phases number is 8", 8, project.getAllPhases().length);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], Date).
     * Failure test. the Data parameter is null.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyTemplateStringLongArrayDateFailure() throws Exception {

        template.applyTemplate("New_Design", new long[]{3, 4}, null);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], long, long, Date).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testApplyTemplateStringLongArrayLongLongDate() throws Exception {

        long workTime = 604800000;
        Workdays workDays = template.getWorkdays();
        fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

        template.applyTemplate("New_Design", new long[]{5}, 1, 2, fixedDate);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], long, long, Date).
     * Failure test. The fixed date must be larger than the start date.
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyTemplateStringLongArrayLongLongDateFailure() throws Exception {

        template.applyTemplate("New_Design", new long[]{5}, 1, 2, fixedDate);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], long, long, Date).
     * Failure test. The leftOutPhaseIds contains varPhaseId
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyTemplateStringLongArrayLongLongDateFailure1() throws Exception {

        long workTime = 604800000;
        Workdays workDays = template.getWorkdays();
        fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);
        template.applyTemplate("New_Design", new long[]{5, 1}, 1, 2, fixedDate);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], long, long, Date).
     * Failure test. The leftOutPhaseIds contains fixedPhaseId
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testApplyTemplateStringLongArrayLongLongDateFailure2() throws Exception {

        long workTime = 604800000;
        Workdays workDays = template.getWorkdays();
        fixedDate = workDays.add(new Date(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);
        template.applyTemplate("New_Design", new long[]{5, 2}, 1, 2, fixedDate);
    }

    /**
     * Test method for DefaultPhaseTemplate#applyTemplate(String, long[], long, long, Date, Date).
     *
     * @throws Exception
     *             the exception occurs
     */
    @Test
    public void testApplyTemplateStringLongArrayLongLongDateDate() throws Exception {

        // set the fixeDate to the start date of phase 2
        long workTime = 604800000;
        Workdays workDays = template.getWorkdays();
        fixedDate = workDays.add(calendar.getTime(), WorkdaysUnitOfTime.MINUTES, (int) workTime / 60000);

        Project project = template.applyTemplate("New_Design", new long[]{7}, 1, 2, fixedDate,
            calendar.getTime());
        // verify the start date is adjustied
        Phase phase = getPhase(project, 2);
        Assert.assertEquals("Should be equal.", 0,
            (phase.calcStartDate().getTime() - fixedDate.getTime()) / 60000);
    }

    /**
     * <p>
     * Get a phase from the project with the given id.
     * </p>
     *
     * @param project
     *            the project to get the phase
     * @param phaseId
     *            the phase id
     * @return the Phase with given id
     */
    private Phase getPhase(Project project, long phaseId) {

        Phase[] phases = project.getAllPhases();
        for (int i = 0; i < phases.length; ++i) {
            if (phases[i].getId() == phaseId) {
                return phases[i];
            }
        }
        return null;
    }

}
