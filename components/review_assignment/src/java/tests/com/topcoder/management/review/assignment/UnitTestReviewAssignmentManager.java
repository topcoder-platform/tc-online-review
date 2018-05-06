/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment;

import java.util.ArrayList;
import java.util.Date;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationManager;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewApplicationStatus;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionManager;
import com.topcoder.management.review.application.ReviewAuctionType;

/**
 * Testcases for ReviewAssignmentManager class.
 *
 * @author TCSDEVELOPER
 * @version 1.0.1
 */
public class UnitTestReviewAssignmentManager extends BaseTestCase {
    /**
     * Represents ReviewAssignmentManager instance to test.
     */
    private ReviewAssignmentManager instance;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * setUp the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        config = getConfigurationObject("test_files/config/config.properties", "assignment",
            ReviewAssignmentManager.class.getName());

        instance = new ReviewAssignmentManager(config);
    }

    /**
     * tearDown the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Failure tests <code>ReviewAssignmentManager(ConfigurationObject config)</code> method. with null
     * config, IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerNullConfig() throws Exception {
        try {
            new ReviewAssignmentManager(null);
            fail("ReviewAssignmentConfigurationException should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>ReviewAssignmentManager(ConfigurationObject config)</code> method.
     * </p>
     * <p>
     * The objectFactoryConfig property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail2() throws Exception {
        config.removeChild("objectFactoryConfig");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewApplicationManagerKey property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail3() throws Exception {
        config.removeProperty("reviewApplicationManagerKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewApplicationManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail4() throws Exception {
        config.setPropertyValue("reviewApplicationManagerKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewApplicationManagerKey property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail5() throws Exception {
        config.setPropertyValue("reviewApplicationManagerKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAuctionManagerKey property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail6() throws Exception {
        config.removeProperty("reviewAuctionManagerKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAuctionManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail7() throws Exception {
        config.setPropertyValue("reviewAuctionManagerKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAuctionManagerKey property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail8() throws Exception {
        config.setPropertyValue("reviewAuctionManagerKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The projectManagerKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail9() throws Exception {
        config.removeProperty("projectManagerKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The projectManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail10() throws Exception {
        config.setPropertyValue("projectManagerKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The projectManagerKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail11() throws Exception {
        config.setPropertyValue("projectManagerKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentAlgorithmKey property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail12() throws Exception {
        config.removeProperty("reviewAssignmentAlgorithmKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentAlgorithmKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail13() throws Exception {
        config.setPropertyValue("reviewAssignmentAlgorithmKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentAlgorithmKey property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail14() throws Exception {
        config.setPropertyValue("reviewAssignmentAlgorithmKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentProjectManagerKey property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail15() throws Exception {
        config.removeProperty("reviewAssignmentProjectManagerKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentProjectManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail16() throws Exception {
        config.setPropertyValue("reviewAssignmentProjectManagerKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentProjectManagerKey property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail17() throws Exception {
        config.setPropertyValue("reviewAssignmentProjectManagerKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentNotificationManagerKey property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail18() throws Exception {
        config.removeProperty("reviewAssignmentNotificationManagerKey");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentNotificationManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail19() throws Exception {
        config.setPropertyValue("reviewAssignmentNotificationManagerKey", "notfind");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentNotificationManagerKey property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail20() throws Exception {
        config.setPropertyValue("reviewAssignmentNotificationManagerKey", "");

        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentAlgorithmConfig property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail21() throws Exception {
        config.removeChild("reviewAssignmentAlgorithmConfig");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentProjectManagerConfig property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail22() throws Exception {
        config.removeChild("reviewAssignmentProjectManagerConfig");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The reviewAssignmentNotificationManagerConfig property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail23() throws Exception {
        config.removeChild("reviewAssignmentNotificationManagerConfig");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The pendingReviewApplicationStatusId property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail24() throws Exception {
        config.removeProperty("pendingReviewApplicationStatusId");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The pendingReviewApplicationStatusId property is not long in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail25() throws Exception {
        config.setPropertyValue("pendingReviewApplicationStatusId", "a");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The approvedReviewApplicationStatusId property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail26() throws Exception {
        config.removeProperty("approvedReviewApplicationStatusId");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The approvedReviewApplicationStatusId property is not long in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail27() throws Exception {
        config.setPropertyValue("approvedReviewApplicationStatusId", "a");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The rejectedReviewApplicationStatusId property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail28() throws Exception {
        config.removeProperty("rejectedReviewApplicationStatusId");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The rejectedReviewApplicationStatusId property is not long in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail29() throws Exception {
        config.setPropertyValue("rejectedReviewApplicationStatusId", "a");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The rejectedReviewApplicationStatusId property and pendingReviewApplicationStatusId are equals in
     * config, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail30() throws Exception {
        config.setPropertyValue("rejectedReviewApplicationStatusId", "1000019");
        config.setPropertyValue("pendingReviewApplicationStatusId", "1000019");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The approvedReviewApplicationStatusId property and pendingReviewApplicationStatusId are equals in
     * config, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail31() throws Exception {
        config.setPropertyValue("approvedReviewApplicationStatusId", "1000019");
        config.setPropertyValue("pendingReviewApplicationStatusId", "1000019");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the ReviewAssignmentManager(ConfigurationObject config) method.
     * </p>
     * <p>
     * The rejectedReviewApplicationStatusId property and approvedReviewApplicationStatusId are equals in
     * config, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerFail32() throws Exception {
        config.setPropertyValue("rejectedReviewApplicationStatusId", "1000019");
        config.setPropertyValue("approvedReviewApplicationStatusId", "1000019");
        try {
            new ReviewAssignmentManager(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
            e.printStackTrace();
        }
    }

    /**
     * Accuracy testcase for <code>ReviewAssignmentManager(ConfigurationObject config)</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testReviewAssignmentManagerAccuracy() throws Exception {
        assertNotNull("ReviewAssignmentManager(config) error.", instance);
    }

    /**
     * Accuracy testcase for <code>execute()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecuteAccuracy() throws Exception {

        ReviewAuctionManager auctionManager = (ReviewAuctionManager) getField(instance,
                "reviewAuctionManager");

        long projectId = 300001L;
        ReviewAuction reviewAuction = new ReviewAuction();
        reviewAuction.setId(1L);
        reviewAuction.setProjectId(projectId);

        ReviewAuctionCategory category = new ReviewAuctionCategory(1L, "Contest Review");

        ReviewAuctionType type = new ReviewAuctionType(1L, "Spec Review", category,
                new ArrayList<ReviewApplicationRole>());
        reviewAuction.setAuctionType(type);
        reviewAuction.setOpen(true);
        reviewAuction.setOpenPositions(new ArrayList<Long>());
        reviewAuction.setAssignmentDate(new Date(new Date().getTime() + DAY));

        reviewAuction.setProjectId(projectId);
        auctionManager.createAuction(reviewAuction);

        ReviewApplicationManager applicationManager = (ReviewApplicationManager) getField(instance,
                "reviewApplicationManager");

        ReviewApplication application = new ReviewApplication();
        application.setId(1L);
        application.setUserId(1L);
        application.setAuctionId(1L);
        application.setApplicationRoleId(1L);

        ReviewApplicationStatus status = new ReviewApplicationStatus(1L, "Pending");
        application.setStatus(status);
        application.setCreateDate(new Date());

        application.setAuctionId(reviewAuction.getId());
        applicationManager.createApplication(application);

        instance.execute();
        long cnt = getResourcesCount(projectId);
        assertEquals("The reviewers count should be correct", 3, cnt);
    }
}
