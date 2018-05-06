/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectScheduler;

/**
 * <p>
 * This is a demo for the Job Scheduling version 3.1 new functionality.
 * </p>
 * <p>
 * It consists of two demos. The first one shows how to work with the two new
 * job types and the newly added
 * <code>ScheduledEnableObjectFactoryManager</code> and
 * <code>ScheduledEnableObjectFactory</code> to create custom runner. The
 * second demo shows how to use the new
 * <code>ConfigurationObjectScheduler</code>.
 * </p>
 * @author TCSDEVELOPER
 * @version 3.1
 */
public class DemoV31Tests extends TestCase {

    /**
     * Represents the configuration file to create the running object from
     * configuration manager.
     */
    private static final String CONFIG_FILE = "V31/SampleConfig.xml";

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * <p>
     * It configures the testing namespace.
     * </p>
     * @throws Exception if there is any problem.
     */
    protected void setUp() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();
        manager.add(CONFIG_FILE);
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     * <p>
     * It clears the testing namespace.
     * </p>
     * @throws Exception if there is any problem.
     */
    protected void tearDown() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();
        Iterator it = manager.getAllNamespaces();
        while (it.hasNext()) {
            manager.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Demonstrates how to use the new job types with custom runner.
     * </p>
     * <p>
     * If the job type is
     * <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE</code>, it will
     * create the job using object factory from the given namespace. If the job
     * type is <code>JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY</code>, it will
     * first get the <code>ScheduledEnableObjectFactory</code> from
     * <code>ScheduledEnableObjectFactoryManager</code> and then create the
     * job from the factory.
     * </p>
     * <p>
     * The custom scheduled job runner is used to show how to deal with the
     * attributes of the job.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testCustomScheduledJobRunnerDemo() throws Exception {

        // create a job with JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE job
        // type. the job will be created by the object factory by the given
        // namespace.
        Job job = new Job("test",
                JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_NAMESPACE,
                "com.topcoder.util.scheduler.scheduling.job");

        // start the job
        job.start();
        // waiting for complete
        Thread.sleep(100);
        // stop the job
        job.stop();

        // get the execution result from job¡¯s attributes
        String value = (String) job.getAttribute("name");

        // the value should be "value"
        System.out.println(value);

        // register CustomScheduledEnableObjectFactory into the manager
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(
                "testFactory", new CustomScheduledEnableObjectFactory());

        // create object with JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY job type
        // where the "testFactory" corresponds the factory name in
        // ScheduledEnableObjectFactoryManager
        job = new Job("test", JobType.JOB_TYPE_SCHEDULED_ENABLE_OBJECT_FACTORY,
                "testFactory");

        // start the job
        job.start();
        Thread.sleep(100);
        // stop the job
        job.stop();

        // get the value, the value should be "user"
        value = (String) job.getAttribute("name");
        System.out.println(value);
    }

    /**
     * <p>
     * Demonstrates how to use the new scheduler
     * <code>ConfigurationObjectScheduler</code>.
     * </p>
     * <p>
     * It only shows how to create the scheduler and add/update job. Its usage
     * is almost the same with other schedulers, so for more deails, please
     * refer to the <code>DemoTests</code>.
     * </p>
     * @throws Exception if there is any problem.
     */
    public void testConfigurationObjectSchedulerDemo() throws Exception {

        // create the ConfigurationObjectScheduler
        ConfigurationObjectScheduler scheduler = new ConfigurationObjectScheduler(
                new DefaultConfigurationObject("test"));

        // create a new job
        Job job = new Job("job", JobType.JOB_TYPE_EXTERNAL, "dir");
        job.setActive(true);
        job.setIntervalUnit(new Week());
        job.setIntervalValue(2);
        job.setRecurrence(10);
        job.setModificationDate(new GregorianCalendar());
        List recipients = new ArrayList();
        recipients.add("recipients@topcoder.com");
        GregorianCalendar startDate = new GregorianCalendar();
        GregorianCalendar stopDate = (GregorianCalendar) startDate.clone();
        stopDate.add(Calendar.DATE, 10);
        job.setStartDate(startDate);
        job.setStartTime(3000000);
        job.setStopDate(stopDate);

        // add job
        scheduler.addJob(job);

        // change the job.
        job.setActive(false);

        // update the job
        scheduler.updateJob(job);

        // get back the job
        Job result = scheduler.getJob("test");

    }
}
