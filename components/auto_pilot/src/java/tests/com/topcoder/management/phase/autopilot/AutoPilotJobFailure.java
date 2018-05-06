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
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Failure tests for <code>AutoPilotJob</code>.
 * </p>
 * @author abelli
 * @version 1.0
 */
public class AutoPilotJobFailure extends TestCase {

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
     * Create auto pilot instance for test cases.
     * </p>
     * @return the AutoPilot instance.
     * @throws Exception - to JUnit.
     */
    private AutoPilot createAutoPilot() throws Exception {
        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        cfg.add(AutoPilot.class.getName(), "auto_pilot.xml", ConfigManager.CONFIG_XML_FORMAT);
        return new AutoPilot();
    }

    /**
     * <p>
     * Create auto pilot job for test cases.
     * </p>
     * @return the AutoPilotJob instance.
     * @throws Exception - to JUnit.
     * @see junit.framework.TestCase#setUp()
     */
    private AutoPilotJob createAutoPilotJob() throws Exception {
        // Release AutoPilotJob.scheduler.
        TestHelper.releaseSingletonInstance(AutoPilotJob.class, "scheduler");

        ConfigManager cfg = ConfigManager.getInstance();
        cfg.add(ActiveAutoPilotSource.class.getName(), "active_auto_source_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(DefaultProjectPilot.class.getName(), "project_pilot.xml",
            ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("logging.xml");
        cfg.add(AutoPilot.class.getName(), "auto_pilot.xml", ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(AutoPilotJob.class.getName(), "auto_pilot_job.xml", ConfigManager.CONFIG_XML_FORMAT);
        cfg.add(AutoPilotJob.class.getName() + AutoPilotJob.OBJECT_FACTORY_POSTFIX,
            "auto_pilot_job_factory.xml", ConfigManager.CONFIG_XML_FORMAT);
        cfg.add("scheduler", "scheduler.xml", ConfigManager.CONFIG_XML_FORMAT);
        return new AutoPilotJob();
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob()}.
     * Fails if bad configuration.
     */
    public void testAutoPilotJob() {
        try {
            new AutoPilotJob();
            fail("no namespace");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(String, String)}.
     * Fails if null namespace.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobStringStringNullNs() throws Exception {
        try {
            new AutoPilotJob((String) null, "apkey");
            fail("null namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(String, String)}.
     * Fails if empty namespace.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobStringStringEmptyNs() throws Exception {
        try {
            new AutoPilotJob(" \r\t\n", "apkey");
            fail("empty namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(String, String)}.
     * Fails if null auto pilot key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobStringStringNullApkey() throws Exception {
        try {
            new AutoPilotJob("namespace", null);
            fail("null apkey");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(String, String)}.
     * Fails if empty auto pilot key.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobStringStringEmptyApkey() throws Exception {
        try {
            new AutoPilotJob("test", " \r\t\n");
            fail("empty apkey");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(AutoPilot, String)}.
     * Fails if null auto pilot.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobAutoPilotStringNullAutoPilot() throws Exception {
        try {
            new AutoPilotJob((AutoPilot) null, "operator");
            fail("null auto pilot");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(AutoPilot, String)}.
     * Fails if null operator.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobAutoPilotStringNullOperator() throws Exception {
        try {
            new AutoPilotJob(createAutoPilot(), null);
            fail("null operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#AutoPilotJob(AutoPilot, String)}.
     * Fails if empty operator.
     * @throws Exception - to JUnit.
     */
    public void testAutoPilotJobAutoPilotStringEmptyOperator() throws Exception {
        try {
            new AutoPilotJob(createAutoPilot(), " \r\t\n");
            fail("empty operator");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#run()}.
     * Fails if auto pilot reports AutoPilotSourceException.
     * @throws Exception - to JUnit.
     */
    public void testRunVoidAutoPilotSourceException() throws Exception {
        try {
            // Load config.
            createAutoPilotJob();
            AutoPilot autoPilot = new AutoPilot() {
                public AutoPilotResult[] advanceProjects(String operator)
                    throws AutoPilotSourceException, PhaseOperationException {
                    throw new AutoPilotSourceException("test");
                }
            };
            AutoPilotJob autoPilotJob = new AutoPilotJob(autoPilot, "tester");

            // Call run().
            autoPilotJob.run();
            fail("auto pilot reports AutoPilotSourceException");
        } catch (RuntimeException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#run()}.
     * Fails if auto pilot reports PhaseOperationException.
     * @throws Exception - to JUnit.
     */
    public void testRunVoidPhaseOperationException() throws Exception {
        try {
            // Load config.
            createAutoPilotJob();
            AutoPilot autoPilot = new AutoPilot() {
                public AutoPilotResult[] advanceProjects(String operator)
                    throws AutoPilotSourceException, PhaseOperationException {
                    throw new PhaseOperationException(-1, null, "test");
                }
            };
            AutoPilotJob autoPilotJob = new AutoPilotJob(autoPilot, "tester");

            // Call run().
            autoPilotJob.run();
            fail("auto pilot reports AutoPilotSourceException");
        } catch (RuntimeException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#run(long[])}.
     * Fails if null projectId.
     * @throws Exception - to JUnit.
     */
    public void testRunLongArray() throws Exception {
        try {
            createAutoPilotJob().run(null);
            fail("null projectId");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if bad config specified.
     * @throws Exception - to JUnit.
     */
    public void testMainBadConfig() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-config", "test"});
            fail("fail to load config file");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if namespace not specified.
     * @throws Exception - to JUnit.
     */
    public void testMainNoNs() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace"});
            fail("fail cause of no namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if namespace not exist.
     * @throws Exception - to JUnit.
     */
    public void testMainBadNs() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "ns", "-project"});
            fail("fail cause of no namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if both poll and project omitted.
     * @throws Exception - to JUnit.
     */
    public void testMainNoPollProject() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test"});
            fail("both poll and project omitted");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if both poll and project exist.
     * @throws Exception - to JUnit.
     */
    public void testMainBothPollProject() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-poll", "-project"});
            fail("both poll and project specified");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if poll not number.
     * @throws Exception - to JUnit.
     */
    public void testMainBadPollA() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-poll", "abc"});
            fail("poll cannot be parsed to integer");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if poll <= 0.
     * @throws Exception - to JUnit.
     */
    public void testMainBadPollB() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-poll", "0"});
            fail("poll cannot <= 0");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if project not number.
     * @throws Exception - to JUnit.
     */
    public void testMainBadProjectA() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-autopilot", "apkey",
                "-project", "abc"});
            fail("project must be integer");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if project not number.
     * @throws Exception - to JUnit.
     */
    public void testMainBadProjectB() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-autopilot", "apkey",
                "-project", "1,", "2,", "abc"});
            fail("project must be integer");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if namespace not pre-loaded.
     * @throws Exception - to JUnit.
     */
    public void testMainBadProjectC() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-autopilot", "apkey",
                "-project", "1,", "2,", "0"});
            fail("namespace not pre-loaded");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if namespace not pre-loaded.
     * @throws Exception - to JUnit.
     */
    public void testMainBadProjectD() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-namespace", "test", "-autopilot", "apkey",
                "-project", "1,2,0"});
            fail("namespace not loaded");
        } catch (ConfigurationException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#main(String[])}.
     * Fails if jobname specified without poll.
     * @throws Exception - to JUnit.
     */
    public void testMainProjectJobName() throws Exception {
        try {
            AutoPilotJob.main(new String[] {"-project", "1,2,0", "-jobname", "jobname"});
            fail("jobname specified without poll");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#createJob(String, int)}.
     * Fails if job name is null.
     */
    public void testCreateJobNullJobName() {
        try {
            AutoPilotJob.createJob(null, 60 * 1000);
            fail("null job name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#createJob(String, int)}.
     * Fails if job name is empty.
     */
    public void testCreateJobEmptyJobName() {
        try {
            AutoPilotJob.createJob(" \r\t\n", 60 * 1000);
            fail("empty job name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#createJob(String, int)}.
     * Fails if interval <= 0.
     */
    public void testCreateJobNonPositive() {
        try {
            AutoPilotJob.createJob("testJob", 0);
            fail("non-positive interval");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#schedule(String, String, int)}.
     * Fails if null namespace.
     * @throws Exception - to JUnit.
     */
    public void testScheduleNullNs() throws Exception {
        try {
            AutoPilotJob.schedule(null, "jobName", 1);
            fail("null namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#schedule(String, String, int)}.
     * Fails if empty namespace.
     * @throws Exception - to JUnit.
     */
    public void testScheduleEmptyNs() throws Exception {
        try {
            AutoPilotJob.schedule(" \r\t\n", "jobName", 1);
            fail("empty namespace");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#schedule(String, String, int)}.
     * Fails if null job name.
     * @throws Exception - to JUnit.
     */
    public void testScheduleNullJobName() throws Exception {
        try {
            AutoPilotJob.schedule("ns", null, 1);
            fail("null job name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#schedule(String, String, int)}.
     * Fails if empty job name.
     * @throws Exception - to JUnit.
     */
    public void testScheduleEmptyJobName() throws Exception {
        try {
            AutoPilotJob.schedule("ns", " \r\t\n", 1);
            fail("empty job name");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Test method for {@link AutoPilotJob#schedule(String, String, int)}.
     * Fails if interval <= 0.
     * @throws Exception - to JUnit.
     */
    public void testScheduleNonPositiveInterval() throws Exception {
        try {
            AutoPilotJob.schedule("ns", "jobName", 0);
            fail("non positive interval");
        } catch (IllegalArgumentException e) {
            // Good.
        }
    }

    /**
     * Aggregates the test suite.
     * @return the test suite.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTestSuite(AutoPilotJobFailure.class);

        return suite;
    }

}
