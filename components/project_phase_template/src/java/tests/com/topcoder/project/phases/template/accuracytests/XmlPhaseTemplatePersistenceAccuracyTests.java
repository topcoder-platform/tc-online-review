/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.accuracytests;

import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence;
import com.topcoder.util.config.ConfigManager;


/**
 * The class <code>XmlPhaseTemplatePersistenceAccuracyTests</code> contains tests for the class
 * {@link<code>XmlPhaseTemplatePersistence</code>}.
 *
 * @author FireIce, onsky
 * @version 1.0
 */
public class XmlPhaseTemplatePersistenceAccuracyTests extends TestCase {
    /** Represents the <code>XmlPhaseTemplatePersistence</code> instance used in tests. */
    private XmlPhaseTemplatePersistence xmlPhaseTemplatePersistence;

    /**
     * Run the XmlPhaseTemplatePersistence(String) constructor test.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testXmlPhaseTemplatePersistence1Accuracy()
        throws Exception {
        String namespace = "com.topcoder.project.phases.template.persistence.XmlPhaseTemplatePersistence";
        xmlPhaseTemplatePersistence = new XmlPhaseTemplatePersistence(namespace);
    }

    /**
     * Run the XmlPhaseTemplatePersistence(String[]) constructor test.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testXmlPhaseTemplatePersistence2Accuracy()
        throws Exception {
        String[] fileNames = new String[] {"test_files/accuracytests/sample.xml" };
        xmlPhaseTemplatePersistence = new XmlPhaseTemplatePersistence(fileNames);
    }

    /**
     * Run the getAllTemplateNames() method tests.
     */
    public void testGetAllTemplateNamesAccuracy() {
        String[] templateNames = xmlPhaseTemplatePersistence.getAllTemplateNames();
        assertNotNull(templateNames);
        assertEquals(1, templateNames.length);
        assertEquals("TCS Component Project", templateNames[0]);
    }

    /**
     * Run the generatePhases(String, Project) method test, with no left out phase.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy1() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays("accuracytests/workdays.xml", "XML"));

        xmlPhaseTemplatePersistence.generatePhases("TCS Component Project", project, null);

        Phase[] phases = project.getAllPhases();

        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 10 elements.", 10, phases.length);
    }

    /**
     * Run the generatePhases(String, Project) method test, all the leftOutPhaseIds exists.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy2() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays("accuracytests/workdays.xml", "XML"));

        xmlPhaseTemplatePersistence.generatePhases("TCS Component Project", project, new long[] { 1, 2, 3 });

        Phase[] phases = project.getAllPhases();

        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 7 elements.", 7, phases.length);
    }

    /**
     * Run the generatePhases(String, Project) method test, dependencies should be correct.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy3() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays("accuracytests/workdays.xml", "XML"));

        xmlPhaseTemplatePersistence.generatePhases("TCS Component Project", project, new long[] { 2 });

        Phase[] phases = project.getAllPhases();
        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 9 elements.", 9, phases.length);

        Phase phase1 = null;
        Phase phase2 = null;

        for (Phase phase : phases) {
            if (phase.getId() == 1) {
                phase1 = phase;
            } else if (phase.getId() == 3) {
                phase2 = phase;
            }
        }

        Dependency[] dependencies = phase2.getAllDependencies();
        assertEquals("should depend on the first phase", phase1.getId(),
            dependencies[0].getDependency().getId());
    }

    /**
     * Run the generatePhases(String, Project) method test, dependencies should be correct.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy4() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays("accuracytests/workdays.xml", "XML"));

        xmlPhaseTemplatePersistence.generatePhases("TCS Component Project", project, new long[] { 1 });

        Phase[] phases = project.getAllPhases();
        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 9 elements.", 9, phases.length);

        Phase phase1 = null;

        for (Phase phase : phases) {
            if (phase.getId() == 2) {
                phase1 = phase;
            }
        }

        Dependency[] dependencies = phase1.getAllDependencies();
        assertEquals("dependencies should be removed", 0, dependencies.length);
    }

    /**
     * Run the generatePhases(String, Project) method test, dependencies should be correct.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy5() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays("accuracytests/workdays.xml", "XML"));

        xmlPhaseTemplatePersistence.generatePhases("TCS Component Project", project, new long[] { 9 });

        Phase[] phases = project.getAllPhases();
        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 9 elements.", 9, phases.length);

        Phase phase1 = null;

        for (Phase phase : phases) {
            if (phase.getId() == 10) {
                phase1 = phase;
            }
        }

        Dependency[] dependencies = phase1.getAllDependencies();
        assertEquals("no need to add dependency as it already exists", 1, dependencies.length);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception if the initialization fails for some reason
     *
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        ConfigManager.getInstance().add("accuracytests/XmlPhaseTemplatePersistence.xml");

        String[] fileNames = new String[] {"test_files/accuracytests/sample.xml" };
        xmlPhaseTemplatePersistence = new XmlPhaseTemplatePersistence(fileNames);
    }

    /**
     * Perform post-test clean-up.
     *
     * @throws Exception if the clean-up fails for some reason
     *
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        ConfigManager configManager = ConfigManager.getInstance();

        for (Iterator iter = configManager.getAllNamespaces(); iter.hasNext();) {
            configManager.removeNamespace((String) iter.next());
        }
    }

    /**
     * <p>Aggregates all tests in this class.</p>
     *
     * @return test suite aggregating all tests.
     */
    public static Test suite() {
        return new TestSuite(XmlPhaseTemplatePersistenceAccuracyTests.class);
    }
}
