/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;
import com.topcoder.project.phases.Phase;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.scheduler.Job;
import com.topcoder.util.scheduler.Scheduler;

/**
 * <p>
 * Demonstration the usage of auto pilot. For command line utility, please see test_files/README for
 * reference.
 * </p>
 * @author sindu, abelli
 * @version 1.0
 */
public class DemoTest extends TestCase {

    /**
     * <p>
     * Setup the test fixture. Load namespaces used to create AutoPilot & AutoPilotJob instance.
     * </p>
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        cfg.add(AutoPilot.class.getName(), "auto_pilot.xml", ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("scheduler", "demo_scheduler.xml", ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("AutoPilotDemo", "demo_scheduler.xml", ConfigManager.CONFIG_XML_FORMAT);
    }

    /**
     * <p>
     * Tear down the test fixture. Remove all namespaces added for test cases.
     * </p>
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        for (Iterator it = cfg.getAllNamespaces(); it.hasNext();) {
            cfg.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Auto-pilot projects (advance project phases) programmatically.
     * </p>
     * @throws Exception - to JUnit.
     */
    public void testProgramatically() throws Exception {
        AutoPilot autoPilot = new AutoPilot();

        // advance phases for all projects that are active and
        // have Auto pilot switch on in its extended property
        AutoPilotResult[] result = autoPilot.advanceProjects("TCSUser");

        // print result
        System.out.println("Projects processed: " + result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println("  ID: " + result[i].getProjectId() + " ¨C " + " ended: "
                + result[i].getPhaseEndedCount() + " started: " + result[i].getPhaseStartedCount());
        }

        // advance phases for the given project ids
        result = autoPilot.advanceProjects(new long[] {1, 5}, "TCSDESIGNER");

        // advance phases for one project id
        AutoPilotResult r = autoPilot.advanceProject(1, "TCSDEVELOPER");
    }

    /**
     * <p>
     * Auto-pilot projects using scheduler at some configurable interval.
     * </p>
     * @throws Throwable - to JUnit.
     */
    public void testConfig() throws Throwable {
        // poll every 30 minutes (this uses the internal scheduler)
        AutoPilotJob.schedule("AutoPilotDemo", "AutoPilotJob", 30);

        // do some other tasks

        // stop internal scheduler
        // application cannot exit if scheduler is not stopped
        AutoPilotJob.getScheduler().stop();

        // create a job to do AutoPilot every 15 minutes
        Job pilotJob = AutoPilotJob.createJob("AutoPilotJob", 15);

        // use our own Job Scheduler
        Scheduler scheduler = new Scheduler("AutoPilotDemo");
        scheduler.addJob(pilotJob);
        scheduler.start();

        // do some other processing

        // stop jobs
        scheduler.stop();
    }

    /**
     * <p>
     * Custom project pilot, override auditing.
     * </p>
     * @author TCSDESIGNER, TCSDEVELOPER
     * @version 1.0
     */
    public class NewPilot extends DefaultProjectPilot {
        /**
         * <p>
         * Empty ctor.
         * </p>
         * @throws ConfigurationException -
         */
        public NewPilot() throws ConfigurationException {
        }

        /**
         * <p>
         * Simple impl.
         * </p>
         * @param phase the Phase.
         * @param isEnd if the phase end.
         * @param operator the operator.
         * @see DefaultProjectPilot#doAudit(Phase, boolean, java.lang.String)
         */
        protected void doAudit(Phase phase, boolean isEnd, String operator) {
            // do some auditing here
        }
    }

    /**
     * <p>
     * Advanced usage (override auditing & pluggable demo).
     * </p>
     * @throws Exception - to JUnit.
     */
    public void testCustomPilot() throws Exception {
        // create autopilot
        AutoPilot pilot = new AutoPilot(new ActiveAutoPilotSource(), new NewPilot());

        // advance phase for all active projects
        // with auto pilot switch on in its extended property
        pilot.advanceProjects("AUDIT");
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(DemoTest.class);

        return suite;
    }
}
