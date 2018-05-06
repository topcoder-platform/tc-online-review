/*
 * Copyright (C) 2007-2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.project.phases.template.accuracytests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.template.Helper;
import com.topcoder.project.phases.template.TestHelper;
import com.topcoder.project.phases.template.persistence.DBPhaseTemplatePersistence;
import com.topcoder.util.config.ConfigManager;

/**
 * Accuracy tests on class <code>DBPhaseTemplatePersistence</code>.
 *
 * @author onsky
 * @version 1.2
 */
public class DBPhaseTemplatePersistenceAccuracyTests extends TestCase {

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
     * Set up environment.
     * </p>
     *
     *
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        TestHelper.clearDB();
        TestHelper.initializeDB();
        ConfigManager.getInstance().add("accuracytests/DBPersistence.xml");
        persistence = new DBPhaseTemplatePersistence(TestHelper.getDBConnectionFactory(), "test");

        // create needed objects
        Date startDate = Calendar.getInstance().getTime();
        DefaultWorkdays workdays = new DefaultWorkdays();
        project = new Project(startDate, workdays);

    }

    /**
     * <p>
     * Clear the environment.
     * </p>
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        Helper.clearConfig();
        TestHelper.clearDB();
    }

    /**
     * <p>
     * Returns all tests.
     * </p>
     *
     * @return all tests
     */
    public static Test suite() {
        return new TestSuite(DBPhaseTemplatePersistenceAccuracyTests.class);
    }

    /**
     * Accuracy test for the generatePhases method.
     *
     * @throws Exception if any error
     *
     */
    public void test_generatePhases() throws Exception {
        persistence.generatePhases("New_Design", project, new long[] {7});

        Phase[] phases = project.getAllPhases();

        assertNotNull("the array should not be null.", phases);
        assertEquals("the array should contains 9 elements.", 9, phases.length);
    }

    /**
     * Run the generatePhases(String, Project) method test, with no left out phase.
     *
     * @throws Exception pass any unexpected exception to JUnit.
     */
    public void testGeneratePhasesAccuracy1() throws Exception {
        persistence.generatePhases("New_Design", project, null);

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
        persistence.generatePhases("New_Design", project, new long[] { 1, 2, 3 });

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
        persistence.generatePhases("New_Design", project, new long[] { 2 });

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
}
