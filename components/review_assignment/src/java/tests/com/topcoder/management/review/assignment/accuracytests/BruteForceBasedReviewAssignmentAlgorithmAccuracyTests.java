/**
 * Copyright (c) 2013, TopCoder, Inc. All rights reserved
 */
package com.topcoder.management.review.assignment.accuracytests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.assignment.ReviewAssignmentAlgorithmException;
import com.topcoder.management.review.assignment.algorithm.BruteForceBasedReviewAssignmentAlgorithm;
import com.topcoder.util.log.Log;

/**
 * <p>
 * Unit tests for {@link BruteForceBasedReviewAssignmentAlgorithm} class. <br/>
 * </p>
 *
 * @author KennyAlive
 * @version 1.0
 */
public class BruteForceBasedReviewAssignmentAlgorithmAccuracyTests {
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION_LOGGER = "Logger";
    /**
     * Constant for configuration namespace for accuracy tests.
     */
    private static final String CONFIGURATION_NOLOGGER = "NoLogger";

    /**
     * The {@code MockBruteForceBasedReviewAssignmentAlgorithm} instance used for testing.
     */
    private MockBruteForceBasedReviewAssignmentAlgorithm instance;

    /**
     * Creates a suite with all test methods for JUnix3.x runner.
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(BruteForceBasedReviewAssignmentAlgorithmAccuracyTests.class);
    }

    /**
     * Sets up the test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    @Before
    public void setUp() throws Exception {
        instance = new MockBruteForceBasedReviewAssignmentAlgorithm();
    }

    /**
     * Accuracy test for no-arg constructor. The logger should be null.
     */
    @Test
    public void test_constructor_BruteForceBasedReviewAssignmentAlgorithm() {
        assertNull("The logger should be null", Helper.getSuperField(instance, "log"));
    }

    /**
     * Accuracy test for {@code configure} method. Checks that logger is initialized properly.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_1() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        Log log = (Log) Helper.getSuperField(instance, "log");
        assertNotNull("The logger should not be null", log);
    }

    /**
     * Accuracy test for {@code configure} method. Checks that logger initialized to null when it's not configured.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_configure_2() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_NOLOGGER);
        instance.configure(configuration);

        Log log = (Log) Helper.getSuperField(instance, "log");
        assertNull("The logger should be null", log);
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction without roles, no review applications.
     *
     * Expected behavior: empty assignment
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_1_noRolesNoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_noRoles();
        List<ReviewApplication> applications = new ArrayList<ReviewApplication>();

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction, applications);

        assertNotNull("Assignment map should not be null", assignment);
        assertTrue("Assignment map should be empty", assignment.isEmpty());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction without roles, single application.
     *
     * Expected behavior: empty assignment
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_2_noRolesSingleApplication() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_noRoles();
        List<ReviewApplication> applications = Arrays.asList(Helper.createReviewApplication());

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction, applications);

        assertNotNull("Assignment map should not be null", assignment);
        assertTrue("Assignment map should be empty", assignment.isEmpty());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with single role, no applications.
     *
     * Expected behavior: empty assignment
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_3_singleRoleNoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        List<ReviewApplication> applications = new ArrayList<ReviewApplication>();

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction, applications);

        assertNotNull("Assignment map should not be null", assignment);
        assertTrue("Assignment map should be empty", assignment.isEmpty());
    }


    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with single role, single application for the same role.
     *
     * Expected behavior: the application is selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_4_singleRoleSingleApplication() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        ReviewApplication application = Helper.createReviewApplication();

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Single application should be selected", 1, assignment.size());

        assertTrue("The application should be corrrect", assignment.keySet().contains((application)));

        ReviewApplicationRole applicationRole = assignment.get(application);
        assertSame("The application role should be correct", application.getApplicationRoleId(),
                applicationRole.getId());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with single role, single application for different role.
     *
     * Expected behavior: the application is not selected.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_5_singleRoleSingleApplication() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        ReviewApplication application = Helper.createReviewApplication(1L, 1L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application));

        assertNotNull("Assignment map should not be null", assignment);
        assertTrue("There should be no assignments", assignment.isEmpty());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with a single role and single position, two applications from different users for the
     * auction's role.
     *
     * Expected behavior: only single application is selected.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_6_singleRoleTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Single application should be selected", 1, assignment.size());

        assertTrue("The application should be corrrect", assignment.keySet().contains((application2)));

        ReviewApplicationRole applicationRole = assignment.get(application2);
        assertSame("The application role should be correct", application2.getApplicationRoleId(),
                applicationRole.getId());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with a single role and single position, two applications: the first for auction's role,
     * the second for different role.
     *
     * Expected behavior: the first application is selected.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_7_singleRoleTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("There should be single application selected", 1, assignment.size());

        assertTrue("The application1 should be assigned", assignment.keySet().contains((application1)));

        ReviewApplicationRole applicationRole = assignment.get(application1);
        assertSame("The application role should be correct", application1.getApplicationRoleId(),
                applicationRole.getId());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles, two applications for auction's roles but from the same user
     *
     * Expected behavior: only single application is selected.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_8_twoRolesTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 1L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("There should be single application selected", 1, assignment.size());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles, two applications for different auction's roles from two different
     * users.
     *
     * Expected behavior: two applications are selected.
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_9_twoRolesTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 2L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Two applications should be selected", 2, assignment.size());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles, two applications from different users for the same auction's role with
     * single position.
     *
     * Expected behavior: only single application is selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_10_twoRolesTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("There should be single application selected", 1, assignment.size());

        assertTrue("The application should be corrrect", assignment.keySet().contains((application2)));

        ReviewApplicationRole applicationRole = assignment.get(application2);
        assertSame("The application role should be correct", application2.getApplicationRoleId(),
                applicationRole.getId());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles, two applications from different users for the same auction's role with
     * two positions.
     *
     * Expected behavior: two applications are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_11_twoRolesTwoApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 2L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 2L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Two applications should be selected", 2, assignment.size());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles and three positions in total, three applications from different users: 2
     * users for role with 2 positions, 1 user for role with 1 position
     *
     * Expected behavior: all 3 applications are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_12_twoRolesThreeApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 2L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 2L);
        ReviewApplication application3 = Helper.createReviewApplication(3L, 3L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2, application3));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Three applications should be selected", 3, assignment.size());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two roles and three positions in total, three applications from different users: 2
     * users for role with 1 position, 1 user for role with 2 positions
     *
     * Expected behavior: 2 applications are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_13_twoRolesThreeApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 2L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);
        ReviewApplication application3 = Helper.createReviewApplication(3L, 3L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2, application3));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Two applications should be selected", 2, assignment.size());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with one role and two positions in total, four applications from different users
     *
     * Expected behavior: 2 applications are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_14_twoRolesFourApplications() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_singleRole(2L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 1L);
        ReviewApplication application3 = Helper.createReviewApplication(3L, 3L, 1L);
        ReviewApplication application4 = Helper.createReviewApplication(4L, 4L, 1L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2, application3, application4));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Two applications should be selected", 2, assignment.size());

        assertTrue("The application should be corrrect", assignment.keySet().contains((application3)));
        assertTrue("The application should be corrrect", assignment.keySet().contains((application4)));

        ReviewApplicationRole applicationRole = assignment.get(application3);
        assertSame("The application role should be correct", application3.getApplicationRoleId(),
                applicationRole.getId());

        applicationRole = assignment.get(application4);
        assertSame("The application role should be correct", application4.getApplicationRoleId(),
                applicationRole.getId());
    }

    /**
     * Accuracy test for {@code assign} method.
     *
     * Preconditions: auction with two single-position roles, four applications from three different users
     *
     * Expected behavior: 2 applications are selected
     *
     * @throws Exception
     *             to JUnit
     */
    @Test
    public void test_assign_15_twoRolesFourApplication() throws Exception {
        ConfigurationObject configuration = Helper.getConfiguration(CONFIGURATION_LOGGER);
        instance.configure(configuration);

        ReviewAuction auction = Helper.createReviewAuction_twoRoles(1L, 1L);
        ReviewApplication application1 = Helper.createReviewApplication(1L, 1L, 1L);
        ReviewApplication application2 = Helper.createReviewApplication(2L, 2L, 2L);
        ReviewApplication application3 = Helper.createReviewApplication(3L, 3L, 1L);
        ReviewApplication application4 = Helper.createReviewApplication(4L, 3L, 2L);

        Map<ReviewApplication, ReviewApplicationRole> assignment = instance.assign(auction,
                Arrays.asList(application1, application2, application3, application4));

        assertNotNull("Assignment map should not be null", assignment);
        assertEquals("Two applications should be selected", 2, assignment.size());

        assertTrue("The application should be corrrect", assignment.keySet().contains((application2)));
        assertTrue("The application should be corrrect", assignment.keySet().contains((application3)));
    }

    /**
     * The mock class for {@code BruteForceBasedReviewAssignmentAlgorithm} abstract class. Used to test concrete
     * methods of {@code BruteForceBasedReviewAssignmentAlgorithm} class.
     *
     * @author KennyAlive
     * @version 1.0
     */
    private static class MockBruteForceBasedReviewAssignmentAlgorithm extends BruteForceBasedReviewAssignmentAlgorithm {
        /**
         * For testing purposes user with greater userId wins...
         */
        @Override
        protected double measureQuality(ReviewAuction reviewAuction,
                Map<ReviewApplication, ReviewApplicationRole> assignment) throws ReviewAssignmentAlgorithmException {
            double quality = 0.0;
            for (ReviewApplication application : assignment.keySet()) {
                quality += application.getUserId();
            }
            return quality;
        }
    }
}
