/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import java.io.File;
import java.util.Arrays;
import java.util.GregorianCalendar;

import com.topcoder.util.file.Template;
import com.topcoder.util.file.xslttemplate.XsltTemplate;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.scheduler.scheduling.persistence.DBScheduler;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * The unit test class is used for component demonstration.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class DemoTests extends TestCase {
    /**
     * <p>
     * This constant represents the namespace to be used by this component.
     * </p>
     */
    private static final String NAMESPACE = "com.topcoder.util.scheduler.scheduling.persistence.DBScheduler";

    /**
     * <p>
     * This constant represents the file to be used by this component.
     * </p>
     */
    private static final String CONFIGFILE = "test_files" + File.separator + "sample_db_persistence.xml";

    /**
     * <p>
     * Setup test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        TestHelper.loadXMLConfig(CONFIGFILE);
        TestHelper.setUpDataBase();
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.tearDownDataBase();
        TestHelper.clearConfig();
    }

    /**
     * <p>
     * Return all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DemoTests.class);
    }

    /**
     * <p>
     * This test case demonstrates the usage of the DBScheduler.
     * </p>
     *
     * @throws Exception to JUnit
     */
    public void testDemo() throws Exception {
        // Instantiate the DBScheduler, passing it the name of the config file
        // containing job data.
        Scheduler myScheduler = new DBScheduler(NAMESPACE);

        // add new jobs.
        // This job will start at 1 am on the 10th of March (GregorianCalendar months
        // run from 0 to 11), and will run once a day, at 1 am, everyday until
        // the 10th of March 2004 (inclusive).
        Job deleteFiles = new Job("Nightly file cleanup", JobType.JOB_TYPE_EXTERNAL, "erase *.tmp");
        deleteFiles.setStartDate(new GregorianCalendar(2003, 04, 10, 01, 00, 00));
        deleteFiles.setStopDate(new GregorianCalendar(2004, 04, 10, 01, 00, 00));
        deleteFiles.setIntervalUnit(new Day());
        myScheduler.addJob(deleteFiles);

        // Add a job dependent on another job regarding the execution time.
        // jobA has a specific schedule time. jobB is dependent on jobA and
        // jobC is dependent on jobB
        Job jobA = new Job("jobA", JobType.JOB_TYPE_JAVA_CLASS, "com.topcoder.util.scheduler.scheduling.MyJob");
        // Calendar representing the date
        jobA.setStartDate(new GregorianCalendar(2003, 04, 10, 01, 00, 00));
        // long representing a time of day
        jobA.setStartTime(580);
        // Calendar representing the date
        jobA.setStopDate(new GregorianCalendar(2004, 04, 10, 01, 00, 00));
        jobA.setIntervalUnit(new Day());
        jobA.setIntervalValue(5);

        myScheduler.addJob(jobA);

        // another job with name jobB
        Job jobB = new Job("jobB", JobType.JOB_TYPE_EXTERNAL, "dir");
        // the delay is 10s
        jobB.setDependence(new Dependence("jobA", EventHandler.SUCCESSFUL, 10000));
        jobB.setIntervalUnit(new Week());
        jobB.setIntervalValue(1);

        myScheduler.addJob(jobB);

        // another job, dependent on jobB, and configured for no delay
        Job jobC = new Job("jobC", JobType.JOB_TYPE_EXTERNAL, "java -version");
        jobC.setDependence(new Dependence("jobB", EventHandler.SUCCESSFUL, 0));
        jobC.setIntervalUnit(new Month());
        jobC.setIntervalValue(5);

        // add email alert event handler to jobC, if the jobC executed
        // unsuccessfully, an email will be sent to name1@topcoder.com
        // the typical messageTemplate is like
        //
        // The Job %JobName% is %JobStatus%...
        //
        Log logger = LogManager.getLog();
        Template template = new XsltTemplate();
        template.setTemplate("The Job %JobName% is %JobStatus%...");
        EmailEventHandler handler1 = new EmailEventHandler(template, Arrays.asList(new String[] {"name1@topcoder.com",
            "service@topcoder.com", "Failure of Job"}), EventHandler.FAILED, "admin@topcoder.com", "Notification",
            logger);
        jobC.addHandler(handler1);

        myScheduler.addJob(jobC);

        // add a job group to scheduler
        JobGroup group = new JobGroup("group_1", Arrays.asList(new Job[] {jobA, jobB, jobC}));

        myScheduler.addGroup(group);

        // add an email Event Handler to the group
        // the following code means if any one of the jobs in the group executed
        // successfully, an email alert will be sent to "name2@topcoder.com" and
        // "name3@topcoder.com"
        EmailEventHandler handler2 = new EmailEventHandler(template, Arrays.asList(new String[] {"name2@topcoder.com",
            "name3@topcoder.com", "service@topcoder.com", "Success of Job"}), EventHandler.SUCCESSFUL,
            "admin@topcoder.com", "Notification", logger);
        group.addHandler(handler2);

        // you can remove the handler from job and group, but you then have to
        // update each
        jobC.removeHandler(handler1);
        group.removeHandler(handler2);

        myScheduler.updateJob(jobC);
        myScheduler.updateGroup(group);

        // you also can remove job and group from the scheduler
        myScheduler.deleteGroup(group.getName());
        myScheduler.deleteJob(jobA);
    }
}
