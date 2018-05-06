/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.autopilot.failuretests.impl;

import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.failuretests.FailureTestsHelper;
import com.topcoder.management.phase.autopilot.failuretests.MockProjectManager;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ProjectManager;

import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;


/**
 * <p>
 * Failure test cases for <code>ActiveAutoPilotSource</code>.
 * </p>
 *
 * @author skatou
 * @version 1.0
 */
public class ActiveAutoPilotSourceFailureTests extends FailureTestsHelper {
    /**
     * Tests constructor without the configuration loaded. ConfigurationException should be thrown.
     */
    public void testConstructor1NoConfig() {
        try {
            new ActiveAutoPilotSource();
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
            new ActiveAutoPilotSource(null, "b", "c", "d", "e");
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
            new ActiveAutoPilotSource("a", null, "c", "d", "e");
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
            new ActiveAutoPilotSource("a", "b", null, "d", "e");
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
    public void testConstructor2Null4() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", null, "e");
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
    public void testConstructor2Null5() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", "d", null);
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
            new ActiveAutoPilotSource("", "b", "c", "d", "e");
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
            new ActiveAutoPilotSource("  ", "b", "c", "d", "e");
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
            new ActiveAutoPilotSource("a", "", "c", "d", "e");
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
            new ActiveAutoPilotSource("a", "  ", "c", "d", "e");
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
            new ActiveAutoPilotSource("a", "b", "", "d", "e");
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
            new ActiveAutoPilotSource("a", "b", " ", "d", "e");
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
    public void testConstructor2EmptyString7() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", "", "e");
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
    public void testConstructor2EmptyString8() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", "   ", "e");
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
    public void testConstructor2EmptyString9() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", "d", "");
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
    public void testConstructor2EmptyString10() throws Exception {
        try {
            new ActiveAutoPilotSource("a", "b", "c", "d", " ");
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
            new ActiveAutoPilotSource("BadNamespace", "b", "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof SpecificationConfigurationException);
        }
    }

    /**
     * Tests constructor with project manager key that does not exist. ConfigurationException should be thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testConstructor2ConfigNoProjectManagerKey()
        throws Exception {
        try {
            loadConfig();
            new ActiveAutoPilotSource(ActiveAutoPilotSource.class.getName(), "b", "c", "d", "e");
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
            loadConfig();
            new ActiveAutoPilotSource(ActiveAutoPilotSource.class.getName() + ".Loop", ProjectManager.class.getName(),
                "c", "d", "e");
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
            loadConfig();
            new ActiveAutoPilotSource(ActiveAutoPilotSource.class.getName() + ".WrongType",
                ProjectManager.class.getName(), "c", "d", "e");
            fail("ConfigurationException should be thrown");
        } catch (ConfigurationException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null1() {
        try {
            new ActiveAutoPilotSource(null, "a", "b", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null2() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, null, "b", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null3() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", null, "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a null argument. IllegalArgumentException should be thrown.
     */
    public void testConstructor3Null4() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", "b", null);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString1() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "", "b", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString2() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "  ", "b", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString3() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", "", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString4() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", "  ", "c");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString5() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", "b", "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests constructor with a empty string. IllegalArgumentException should be thrown.
     */
    public void testConstructor3EmptyString6() {
        try {
            ProjectManager projectManager = new MockProjectManager();
            new ActiveAutoPilotSource(projectManager, "a", "b", "   ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
            // ok
        }
    }

    /**
     * Tests getProjectIds method. <code>MockProjectManager</code> is used so AutoPilotSourceException should be
     * thrown.
     *
     * @throws Exception pass to JUnit.
     */
    public void testGetProjectIdsFailure() throws Exception {
        try {
            loadConfig();
            MockProjectManager.setSearchProjectsException(true);
            new ActiveAutoPilotSource().getProjectIds();
            fail("AutoPilotSourceException should be thrown");
        } catch (AutoPilotSourceException e) {
            assertTrue("Cause not as expected", e.getCause() instanceof PersistenceException);
        }
    }
}
