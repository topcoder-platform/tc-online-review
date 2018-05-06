/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.project;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.persistence.PersistenceResourceManager;
import com.topcoder.management.review.application.ReviewApplication;
import com.topcoder.management.review.application.ReviewApplicationResourceRole;
import com.topcoder.management.review.application.ReviewApplicationRole;
import com.topcoder.management.review.application.ReviewAuction;
import com.topcoder.management.review.application.ReviewAuctionCategory;
import com.topcoder.management.review.application.ReviewAuctionType;
import com.topcoder.management.review.assignment.BaseTestCase;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.management.review.assignment.ReviewAssignmentConfigurationException;
import com.topcoder.management.review.assignment.algorithm.MaxSumOfRatingReviewAssignmentAlgorithm;
import com.topcoder.web.ejb.forums.Forums;

/**
 * Testcases for DefaultReviewAssignmentProjectManager class.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class UnitTestDefaultReviewAssignmentProjectManager extends BaseTestCase {
    /**
     * Represents DefaultReviewAssignmentProjectManager instance to test.
     */
    private DefaultReviewAssignmentProjectManager instance;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * The <code>{@link ReviewAuction}</code> instance used for testing.
     */
    private ReviewAuction reviewAuction;

    /**
     * The <code>{@link Project}</code> instance used for testing.
     */
    private Project project;

    /**
     * The map of <code>{@link ReviewApplication}</code> and <code>{@link ReviewApplicationRole}</code>
     * instance used for testing.
     */
    private Map<ReviewApplication, ReviewApplicationRole> assignment;

    /**
     * setUp the test environment.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new DefaultReviewAssignmentProjectManager();

        config = getConfigurationObject("test_files/config/config.properties",
            "DefaultReviewAssignmentProjectManager", DefaultReviewAssignmentProjectManager.class.getName());
        instance.configure(config);

        reviewAuction = new ReviewAuction();

        ReviewApplication reviewApplication = new ReviewApplication();

        List<ReviewApplicationResourceRole> resourceRoles = produceReviewApplicationResourceRoles();
        ReviewApplicationRole reviewApplicationRole = new ReviewApplicationRole(1, "Reviewer", 2,
            resourceRoles);

        assignment = new HashMap<ReviewApplication, ReviewApplicationRole>();
        assignment.put(reviewApplication, reviewApplicationRole);

        ProjectStatus projectStatus = new ProjectStatus(1, "developement");
        project = new Project(100024, ProjectCategory.BANNERS_ICONS, projectStatus);
    }

    /**
     * Produce an ReviewApplicationResourceRole list for testing.
     *
     * @return an ReviewApplicationResourceRole list for testing.
     */
    private List<ReviewApplicationResourceRole> produceReviewApplicationResourceRoles() {
        List<ReviewApplicationResourceRole> resourceRoles = new ArrayList<ReviewApplicationResourceRole>();
        ReviewApplicationResourceRole resourceRole = new ReviewApplicationResourceRole(1, true);
        resourceRoles.add(resourceRole);
        return resourceRoles;
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
     * Accuracy testcase for <code>DefaultReviewAssignmentProjectManager()</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDefaultReviewAssignmentProjectManagerAccuracy() throws Exception {
        assertNotNull("DefaultReviewAssignmentProjectManager() error.", instance);
    }

    /**
     * Failure tests <code>addReviewersToProject</code> method. with null reviewAuction,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewersToProjectNullReviewAuction() throws Exception {
        try {
            instance.addReviewersToProject(null, project, assignment);
            fail("testAddReviewersToProjectNullReviewAuction is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>addReviewersToProject</code> method. with null project, IllegalArgumentException
     * should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewersToProjectNullProject() throws Exception {
        try {
            instance.addReviewersToProject(reviewAuction, null, assignment);
            fail("testAddReviewersToProjectNullProject is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Failure tests <code>addReviewersToProject</code> method. while the configure method is not called,
     * IllegalStateException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewersToProject_NoConfigCalled() throws Exception {
        try {
            new DefaultReviewAssignmentProjectManager().addReviewersToProject(reviewAuction, project,
                assignment);
            fail("testAddReviewersToProject_NoConfigCalled is failure.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>addReviewersToProject</code> method. The assignment is empty, so it will
     * return directly.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewersToProjectAccuracy1() throws Exception {
        assignment.clear();
        instance.addReviewersToProject(reviewAuction, project, assignment);
        PersistenceResourceManager resourceManager = (PersistenceResourceManager) getField(instance,
            "resourceManager");
        Resource resource = resourceManager.getResource(project.getId());
        assertNotNull("The operator should be updated to the resource.", resource);
    }

    /**
     * Accuracy testcase for <code>addReviewersToProject</code> method. The forum is not assigned since the
     * forum id isn't set.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAddReviewersToProjectAccuracy2() throws Exception {

        List<ReviewApplication> reviewApplications = new ArrayList<ReviewApplication>();
        ReviewApplication app1 = new ReviewApplication();
        app1.setUserId(1);
        app1.setApplicationRoleId(1);
        ReviewApplication app2 = new ReviewApplication();
        app2.setUserId(2);
        app2.setApplicationRoleId(1);
        ReviewApplication app3 = new ReviewApplication();
        app3.setUserId(3);
        app3.setApplicationRoleId(1);
        reviewApplications.add(app1);
        reviewApplications.add(app2);
        reviewApplications.add(app3);

        List<ReviewApplicationResourceRole> resourceRoles = new ArrayList<ReviewApplicationResourceRole>();
        ReviewApplicationRole reviewApplicationRole = new ReviewApplicationRole(1, "Reviewer", 2,
            resourceRoles);

        assignment.clear();
        assignment.put(app1, reviewApplicationRole);
        assignment.put(app2, reviewApplicationRole);
        assignment.put(app3, reviewApplicationRole);

        // Create ReviewAuctionType
        ReviewAuctionCategory auctionCategory = new ReviewAuctionCategory(1, "Contest Review");

        List<ReviewApplicationRole> applicationRoles = new ArrayList<ReviewApplicationRole>();

        ReviewApplicationRole rar = new ReviewApplicationRole(1, "Reviewer", 2,
            new ArrayList<ReviewApplicationResourceRole>());
        applicationRoles.add(rar);

        ReviewAuctionType auctionType = new ReviewAuctionType(1, "developement contest", auctionCategory,
            applicationRoles);

        // Set the attributes for reviewAuction
        reviewAuction.setProjectId(1);
        reviewAuction.setAuctionType(auctionType);

        List<Long> openPositions = new ArrayList<Long>();
        openPositions.add(new Long(1));
        openPositions.add(new Long(2));

        reviewAuction.setOpenPositions(openPositions);

        config = getConfigurationObject("test_files/config/config.properties", "algorithm",
            MaxSumOfRatingReviewAssignmentAlgorithm.class.getName());
        MaxSumOfRatingReviewAssignmentAlgorithm alg = new MaxSumOfRatingReviewAssignmentAlgorithm();
        alg.configure(config);

        assignment = alg.assign(reviewAuction, reviewApplications);

        instance.addReviewersToProject(reviewAuction, project, assignment);
        PersistenceResourceManager resourceManager = (PersistenceResourceManager) getField(instance,
            "resourceManager");
        Resource resource = resourceManager.getResource(project.getId());

        assertEquals("The project should be updated to the resource.", 100006, resource.getProject()
            .longValue());
        assertEquals("The user id should be 0.", resource.getProperty("External Reference ID"), "132458");
        assertEquals("The handle should be set.", resource.getProperty("Handle"), "user");
    }

    /**
     * Failure tests <code>configure(ConfigurationObject config)</code> method. with null config,
     * IllegalArgumentException should be thrown.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail1() throws Exception {
        try {
            instance.configure(null);
            fail("testConfigureFail1 is failure.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager#configure(ConfigurationObject)}
     * method.
     * </p>
     * <p>
     * The objectFactoryConfig property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail2() throws Exception {
        config.removeChild("objectFactoryConfig");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail3() throws Exception {
        config.removeProperty("userRetrievalKey");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail4() throws Exception {
        config.setPropertyValue("userRetrievalKey", "notfind");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail5() throws Exception {
        config.setPropertyValue("userRetrievalKey", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseManagerKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail6() throws Exception {
        config.removeProperty("phaseManagerKey");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail7() throws Exception {
        config.setPropertyValue("phaseManagerKey", "notfind");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseManagerKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail8() throws Exception {
        config.setPropertyValue("phaseManagerKey", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail9() throws Exception {
        config.removeProperty("resourceManagerKey");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is not in OF in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail10() throws Exception {
        config.setPropertyValue("resourceManagerKey", "notfind");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail11() throws Exception {
        config.setPropertyValue("resourceManagerKey", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The registrationDateFormatString property is empty in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail13() throws Exception {
        config.setPropertyValue("registrationDateFormatString", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanUrl property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail15() throws Exception {
        config.removeProperty("forumsBeanUrl");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanUrl property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail16() throws Exception {
        config.setPropertyValue("forumsBeanUrl", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanFactory property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail17() throws Exception {
        config.removeProperty("forumsBeanFactory");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanFactory property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail18() throws Exception {
        config.setPropertyValue("forumsBeanFactory", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanFactory property is invalid in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail19() throws Exception {
        config.setPropertyValue("forumsBeanFactory", "invalid");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanName property is lost in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail20() throws Exception {
        config.removeProperty("forumsBeanName");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanName property is empty in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail21() throws Exception {
        config.setPropertyValue("forumsBeanName", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The forumsBeanName property is invalid in config, <code>ReviewAssignmentConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail22() throws Exception {
        config.setPropertyValue("forumsBeanName", "invalid");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseTypeExtensionRules property is lost in config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail23() throws Exception {
        config.removeChild("phaseTypeExtensionRules");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseTypeExtensionRules property has invalid key in phaseTypeExtensionRules config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail26() throws Exception {
        ConfigurationObject mapConfig = Helper.getChildConfig(config, "phaseTypeExtensionRules");
        mapConfig.setPropertyValue("1a", "2");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseTypeExtensionRules property has negative in phaseTypeExtensionRules config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail27() throws Exception {
        ConfigurationObject mapConfig = Helper.getChildConfig(config, "phaseTypeExtensionRules");
        mapConfig.setPropertyValue("1", "-2");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The phaseTypeExtensionRules property has invalid value in phaseTypeExtensionRules config,
     * <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail28() throws Exception {
        ConfigurationObject mapConfig = Helper.getChildConfig(config, "phaseTypeExtensionRules");
        mapConfig.setPropertyValue("1", "2a");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The operator property is lost in config, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail29() throws Exception {
        config.removeProperty("operator");
        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link DefaultReviewAssignmentProjectManager(ConfigurationObject)} method.
     * </p>
     * <p>
     * The operator property is empty in config, <code>ReviewAssignmentConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureFail30() throws Exception {
        config.setPropertyValue("operator", "");

        try {
            instance.configure(config);
            fail("should have thrown ReviewAssignmentConfigurationException");
        } catch (ReviewAssignmentConfigurationException e) {
            // pass
        }
    }

    /**
     * Accuracy testcase for <code>configure(ConfigurationObject config)</code> method.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureAccuracy() throws Exception {
        instance.configure(config);
        assertNotNull("userRetrieval field should not be null", getField(instance, "userRetrieval"));
        assertNotNull("phaseManager field should not be null", getField(instance, "phaseManager"));
        assertNotNull("resourceManager field should not be null", getField(instance, "resourceManager"));
        assertNotNull("resourceRoleById field should not be null", getField(instance, "resourceRoleById"));
        assertNotNull("phaseTypeExtensionRules field should not be null",
            getField(instance, "phaseTypeExtensionRules"));
        assertNotNull("log field should not be null", getField(instance, "log"));

        assertEquals("operator field should be 4", "4", getField(instance, "operator"));
        assertEquals("registrationDateFormatString field should not be null", "MM.dd.yyyy hh:mm a",
            getField(instance, "registrationDateFormatString"));

    }

    /**
     * Accuracy testcase for <code>configure(ConfigurationObject config)</code> method. The optional property
     * is not set.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testConfigureAccuracy1() throws Exception {
        config.removeProperty("loggerName");
        config.removeProperty("registrationDateFormatString");
        instance.configure(config);
        assertNotNull("userRetrieval field should not be null", getField(instance, "userRetrieval"));
        assertNotNull("phaseManager field should not be null", getField(instance, "phaseManager"));
        assertNotNull("resourceManager field should not be null", getField(instance, "resourceManager"));
        assertNotNull("resourceRoleById field should not be null", getField(instance, "resourceRoleById"));
        assertNotNull("phaseTypeExtensionRules field should not be null",
            getField(instance, "phaseTypeExtensionRules"));
        assertNotNull("configuration field should not be null", getField(instance, "configuration"));
        assertNull("log field should not be null", getField(instance, "log"));

        assertEquals("operator field should be 4", "4", getField(instance, "operator"));
        assertEquals("registrationDateFormatString field should not be null", "MM.dd.yyyy hh:mm a",
            getField(instance, "registrationDateFormatString"));
    }

}
