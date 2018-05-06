/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.ProjectPilot;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.phase.autopilot.impl.DefaultProjectPilot;

import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;


/**
 * <p>
 * Failure test cases for <code>AutoPilot</code>.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class AutoPilotFailureTests extends FailureTestsHelper {
    /** The AutoPilot instance to be tested. */
    private AutoPilot autoPilot = null;

    /**
     * Sets up the test environment. Configurations are loaded and new instance of <code>AutoPilot</code> is created.
     *
     * @throws Exception pass to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        loadConfig();

        autoPilot = new AutoPilot();
    }

    /**
     * Tests constructor without the configuration loaded. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor1NoConfig() throws Exception {
        try {
            unloadConfig();
            new AutoPilot();
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
            new AutoPilot(null, "b", "c");
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
            new AutoPilot("a", null, "c");
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
    public void testConstructor2Null3() throws Exception {
        try {
            new AutoPilot("a", "b", null);
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
            new AutoPilot("", "b", "c");
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
            new AutoPilot("  ", "b", "c");
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
            new AutoPilot("a", "", "c");
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
            new AutoPilot("a", "  ", "c");
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
    public void testConstructor2EmptyString5() throws Exception {
        try {
            new AutoPilot("a", "b", "");
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
    public void testConstructor2EmptyString6() throws Exception {
        try {
            new AutoPilot("a", "b", " ");
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
            new AutoPilot("BadNamespace", "b", "c");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor with AutoPilotSource key that does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoAutoPilotSource()
        throws Exception {
        try {
            new AutoPilot(AutoPilot.class.getName(), "no", ProjectPilot.class.getName());
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof InvalidClassSpecificationException);
        }
    }

    /**
     * Tests constructor with ProjectPilot key that does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoProjectPilot()
        throws Exception {
        try {
            new AutoPilot(AutoPilot.class.getName(), AutoPilotSource.class.getName(), "no");
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
            new AutoPilot(AutoPilot.class.getName() + ".Loop", AutoPilotSource.class.getName(),
                ProjectPilot.class.getName());
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
            new AutoPilot(AutoPilot.class.getName() + ".WrongType", AutoPilotSource.class.getName(),
                ProjectPilot.class.getName());
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
            new AutoPilot(null, new DefaultProjectPilot());
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
            new AutoPilot(new ActiveAutoPilotSource(), null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects1Null() throws Exception {
        try {
            autoPilot.advanceProjects(null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects1EmptyString1() throws Exception {
        try {
            autoPilot.advanceProjects("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects1EmptyString2() throws Exception {
        try {
            autoPilot.advanceProjects("          ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method. AutoPilotSource.getProjectIds fails, AutoPilotSourceException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects1AutoPilotSourceFail()
        throws Exception {
        try {
            MockProjectManager.setSearchProjectsException(true);
            autoPilot.advanceProjects("operator");
            fail("AutoPilotSourceException should be thrown");
        } catch (AutoPilotSourceException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method. ProjectPilot fails.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects1ProjectPilotFail()
        throws Exception {
        MockProjectManager.setSearchProjectsException(false);
        MockPhaseManager.setGetPhasesException(true);
        autoPilot.advanceProjects("operator");
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }

    /**
     * Tests advanceProjects method with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects2Null1() throws Exception {
        try {
            autoPilot.advanceProjects(null, "operator");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects2Null2() throws Exception {
        try {
            autoPilot.advanceProjects(new long[] {2, 4, 8 }, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects2EmptyString1() throws Exception {
        try {
            autoPilot.advanceProjects(new long[] {2, 4, 8 }, "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects2EmptyString2() throws Exception {
        try {
            autoPilot.advanceProjects(new long[] {2, 4, 8 }, "        ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProject method with a null argument. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjectNull() throws Exception {
        try {
            autoPilot.advanceProject(2, null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProjects method. ProjectPilot fails.
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjects2ProjectPilotFail()
        throws Exception {
        MockProjectManager.setSearchProjectsException(false);
        MockPhaseManager.setGetPhasesException(true);
        autoPilot.advanceProjects(new long[] {2, 4 }, "operator");
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }

    /**
     * Tests advanceProject method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjectEmptyString1() throws Exception {
        try {
            autoPilot.advanceProject(2, "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProject method with an empty string. IllegalArgumentException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjectEmptyString2() throws Exception {
        try {
            autoPilot.advanceProject(2, "   ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests advanceProject method. ProjectPilot.advancePhases fails
     * PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testAdvanceProjectProjectPilotFail() throws Exception {
        MockPhaseManager.setGetPhasesException(true);
        autoPilot.advanceProject(2, "operator");
        // PhaseOperationException ignored in DefaultProjectPilot#advancePhases, so no exception thrown.
    }
}
