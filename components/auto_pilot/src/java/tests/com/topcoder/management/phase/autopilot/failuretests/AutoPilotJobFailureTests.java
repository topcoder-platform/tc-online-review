/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotJob;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;

import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;


/**
 * <p>
 * Failure test cases for <code>AutoPilotJob</code>.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class AutoPilotJobFailureTests extends FailureTestsHelper {
    /**
     * Sets up the test environment. Configurations are loaded.
     *
     * @throws Exception pass to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        loadConfig();
    }

    /**
     * Tests constructor without the configuration loaded. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor1NoConfig() throws Exception {
        try {
            unloadConfig();
            new AutoPilotJob();
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null1() throws Exception {
        try {
            new AutoPilotJob((String) null, "b");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2Null2() throws Exception {
        try {
            new AutoPilotJob("a", null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString1() throws Exception {
        try {
            new AutoPilotJob("", "b");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString2() throws Exception {
        try {
            new AutoPilotJob("       ", "b");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString3() throws Exception {
        try {
            new AutoPilotJob("a", "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2EmptyString4() throws Exception {
        try {
            new AutoPilotJob("a", "      ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with namespace that does not exist. ConfigurationException should be thrown.
     */
    public void testConstructor2ConfigNoNamespace() {
        try {
            new AutoPilotJob("BadNamespace", AutoPilot.class.getName());
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor with AutoPilot key that does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoAutoPilot() throws Exception {
        try {
            new AutoPilotJob(AutoPilotJob.class.getName(), "no");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof InvalidClassSpecificationException);
        }
    }

    /**
     * Tests constructor with a namespace that contains loop. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigLoop() throws Exception {
        try {
            new AutoPilotJob(AutoPilotJob.class.getName() + ".Loop", AutoPilot.class.getName());
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof IllegalReferenceException);
        }
    }

    /**
     * Tests constructor with a namespace that contains wrong type. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigWrongType() throws Exception {
        try {
            new AutoPilotJob(AutoPilotJob.class.getName() + ".WrongType", AutoPilot.class.getName());
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor3Null1() throws Exception {
        try {
            new AutoPilotJob((AutoPilot) null, "a");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor3Null2() throws Exception {
        try {
            new AutoPilotJob(new AutoPilot(), null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor3EmptyString1() throws Exception {
        try {
            new AutoPilotJob(new AutoPilot(), "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor3EmptyString2() throws Exception {
        try {
            new AutoPilotJob(new AutoPilot(), "            ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a null argument. IllegalArgumentException will be thrown.
     */
    public void testCreateJobNull() {
        try {
            AutoPilotJob.createJob(null, 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     */
    public void testCreateJobEmptyString1() {
        try {
            AutoPilotJob.createJob("", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     */
    public void testCreateJobEmptyString2() {
        try {
            AutoPilotJob.createJob("       ", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with negative interval. IllegalArgumentException will be thrown.
     */
    public void testCreateJobNegativeInterval() {
        try {
            AutoPilotJob.createJob("job", -100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with zero interval. IllegalArgumentException will be thrown.
     */
    public void testCreateJobZeroInterval() {
        try {
            AutoPilotJob.createJob("job", 0);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a null argument. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleNull1() throws Exception {
        try {
            AutoPilotJob.schedule(null, "job", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a null argument. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleNull2() throws Exception {
        try {
            AutoPilotJob.schedule("name", null, 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleEmptyString1() throws Exception {
        try {
            AutoPilotJob.schedule("", "job", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleEmptyString2() throws Exception {
        try {
            AutoPilotJob.schedule("      ", "job", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleEmptyString3() throws Exception {
        try {
            AutoPilotJob.schedule("name", "", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a empty string. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleEmptyString4() throws Exception {
        try {
            AutoPilotJob.schedule("name", "      ", 100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a negative interval. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleNegativeInterval() throws Exception {
        try {
            AutoPilotJob.schedule("name", "job", -100);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method with a zero interval. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleZeroInterval() throws Exception {
        try {
            AutoPilotJob.schedule("name", "job", 0);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests createJob method. Call the method with bad namespace, ConfigurationException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testScheduleBadNamespace() throws Exception {
        try {
            AutoPilotJob.schedule("utoPilotJob", "AutoPilotJob", 100);
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * Tests main method with a null argument. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainNull() throws Exception {
        try {
            AutoPilotJob.main(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with value of poll switch not integer. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainPollValidationFailure() throws Exception {
        try {
            String[] args = {"-poll", "11a" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with poll switch with too many values. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainPollTooManyValues() throws Exception {
        try {
            String[] args = {"-poll", "1,2" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with poll switch with negative value. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainPollNegativeValue() throws Exception {
        try {
            String[] args = {"-poll", "-100" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with value of project switch not integer. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainProjectValidationFailure() throws Exception {
        try {
            String[] args = {"-project", "1,2,a" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with config switch with too many values. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainConfigTooManyValues() throws Exception {
        try {
            String[] args = {"-config", "baby,girl", "-poll" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with namespace switch with too many values. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainNamespaceTooManyValues() throws Exception {
        try {
            String[] args = {"-namespace", "baby,girl", "-project" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with autopilot switch with too many values. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainAutopilotTooManyValues() throws Exception {
        try {
            String[] args = {"-autopilot", "baby,girl", "-poll", "11" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with jobname switch with too many values. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainJobnameTooManyValues() throws Exception {
        try {
            String[] args = {"-jobname", "baby,girl", "-poll" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with config switch with a file not exist. ConfigurationException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainConfigFileNotExist() throws Exception {
        try {
            String[] args = {"-config", "NoSuchFile", "-project", "33" };
            AutoPilotJob.main(args);
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * Tests main method with both poll switch and project switch specified. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainPollAndProject() throws Exception {
        try {
            String[] args = {"-poll", "1", "-project" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with neither poll switch nor project switch specified. IllegalArgumentException will be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainNoPollAndProject() throws Exception {
        try {
            String[] args = {"-namespace", "AutoPilotJob", "-autopilot", "AutoPilotJob" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with namespace switch specified but no autopilot switch in project mode.
     * IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainWithNamespaceNoAutoPilotProjectMode()
        throws Exception {
        try {
            String[] args = {"-namespace", "AutoPilotJob", "-project", "1" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests main method with autopilot switch specified but no namespace switch. IllegalArgumentException will be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testMainWithAutoPilotNoNamespace() throws Exception {
        try {
            String[] args = {"-autopilot", "AutoPilotJob", "-project" };
            AutoPilotJob.main(args);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests run method. AutoPilotSource fails, RuntimeException will be thrown, with cause an instance of
     * AutoPilotSourceException.
     *
     * @throws Exception pass to JUnit.
     */
    public void testRun1AutoPilotSourceFail() throws Exception {
        try {
            MockProjectManager.setSearchProjectsException(true);
            new AutoPilotJob().run();
            fail("RuntimeException should be thrown");
        } catch (RuntimeException e) {
            assertTrue("cause not as expected", e.getCause() instanceof AutoPilotSourceException);
        }
    }

    /**
     * Tests run method. ProjectPilot fails.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testRun1ProjectPilotFail() throws Exception {
        MockProjectManager.setSearchProjectsException(false);
        MockPhaseManager.setGetPhasesException(true);
        new AutoPilotJob().run();
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }

    /**
     * Tests run(long[]) method with a null argument. IllegalArgumentException will be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testRun2Null() throws Exception {
        try {
            new AutoPilotJob().run(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests run(long[]) method. ProjectPilot fails.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     * @throws Exception pass to JUnit.
     */
    public void testRun2ProjectPilotFail() throws Exception {
        MockProjectManager.setSearchProjectsException(false);
        MockPhaseManager.setGetPhasesException(true);
        new AutoPilotJob().run(new long[] {1 });
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }
}
