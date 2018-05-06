/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.notification;

import com.cronos.onlinereview.external.RetrievalException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.BaseTestCase;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.MockDeliverablePersistence;
import com.topcoder.management.deliverable.latetracker.MockLateDeliverableManager;
import com.topcoder.management.deliverable.latetracker.MockPhasePersistence;
import com.topcoder.management.deliverable.latetracker.MockProjectManager;
import com.topcoder.management.deliverable.latetracker.MockResourceManager;
import com.topcoder.management.deliverable.latetracker.MockUserRetrieval;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.PhasePersistenceException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.search.builder.SearchBuilderException;

/**
 * Unit tests for <code>{@link NotRespondedLateDeliverablesNotifier}</code> class.
 *
 * @author sparemax
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotifierTests extends BaseTestCase {
    /**
     * The <code>{@link NotRespondedLateDeliverablesNotifier}</code> instance used for testing.
     */
    private NotRespondedLateDeliverablesNotifier target;

    /**
     * The <code>{@link LateDeliverablesTracker}</code> instance used for testing.
     */
    private LateDeliverablesTracker tracker;

    /**
     * The <code>{@link LateDeliverablesRetrieverImpl}</code> instance used for testing.
     */
    private LateDeliverablesRetrieverImpl retriever;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void setUp() throws Exception {
        super.setUp();

        config = getConfigurationObject("config/LateDeliverablesTracker.xml", LateDeliverablesTracker.class.getName());
        tracker = new LateDeliverablesTracker(config);

        retriever = new LateDeliverablesRetrieverImpl();
        config = getConfigurationObject("config/LateDeliverablesRetrieverImpl.xml",
            LateDeliverablesRetrieverImpl.class.getName());
        retriever.configure(config);

        config = getConfigurationObject("config/NotRespondedLateDeliverablesNotifier.xml",
            NotRespondedLateDeliverablesNotifier.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             throws exception if any.
     */
    protected void tearDown() throws Exception {
        super.tearDown();

        MockResourceManager.setResult(null);
        MockProjectManager.setResult(null);
        MockPhasePersistence.setResult(null);
        MockDeliverablePersistence.setResult(null);
        MockUserRetrieval.setResult(null);
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor.
     * </p>
     * <p>
     * Verifies the values of all class fields.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_1() throws Exception {
        target = new NotRespondedLateDeliverablesNotifier(config);

        assertNotNull("'log' should be correct.", getField(target, "log"));
        assertNotNull("'lateDeliverableManager' should be correct.", getField(target, "lateDeliverableManager"));
        assertNotNull("'managerResourceRoleIds' should be correct.", getField(target, "managerResourceRoleIds"));
        assertNotNull("'resourceManager' should be correct.", getField(target, "resourceManager"));
        assertNotNull("'userRetrieval' should be correct.", getField(target, "userRetrieval"));
        assertNotNull("'emailSubjectTemplateText' should be correct.", getField(target, "emailSubjectTemplateText"));
        assertNotNull("'emailBodyTemplatePath' should be correct.", getField(target, "emailBodyTemplatePath"));
        assertNotNull("'emailSendingUtility' should be correct.", getField(target, "emailSendingUtility"));
        assertNotNull("'timestampFormat' should be correct.", getField(target, "timestampFormat"));
        assertNotNull("'projectManager' should be correct.", getField(target, "projectManager"));
        assertNotNull("'phasePersistence' should be correct.", getField(target, "phasePersistence"));
        assertNotNull("'deliverablePersistence' should be correct.", getField(target, "deliverablePersistence"));
    }

    /**
     * <p>
     * Accuracy test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor.
     * </p>
     * <p>
     * Verifies the values of all class fields.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_2() throws Exception {
        config.removeProperty("loggerName");
        config.setPropertyValue("emailSubjectTemplateText", "");
        config.removeProperty("timestampFormat");
        target = new NotRespondedLateDeliverablesNotifier(config);

        assertNull("'log' should be correct.", getField(target, "log"));
        assertNotNull("'lateDeliverableManager' should be correct.", getField(target, "lateDeliverableManager"));
        assertNotNull("'managerResourceRoleIds' should be correct.", getField(target, "managerResourceRoleIds"));
        assertNotNull("'resourceManager' should be correct.", getField(target, "resourceManager"));
        assertNotNull("'userRetrieval' should be correct.", getField(target, "userRetrieval"));
        assertNotNull("'emailSubjectTemplateText' should be correct.", getField(target, "emailSubjectTemplateText"));
        assertNotNull("'emailBodyTemplatePath' should be correct.", getField(target, "emailBodyTemplatePath"));
        assertNotNull("'emailSendingUtility' should be correct.", getField(target, "emailSendingUtility"));
        assertNotNull("'timestampFormat' should be correct.", getField(target, "timestampFormat"));
        assertNotNull("'projectManager' should be correct.", getField(target, "projectManager"));
        assertNotNull("'phasePersistence' should be correct.", getField(target, "phasePersistence"));
        assertNotNull("'deliverablePersistence' should be correct.", getField(target, "deliverablePersistence"));
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with config is <code>null</code>.
     * </p>
     * <p>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_configNull() throws Exception {
        config = null;

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'loggerName' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_loggerNameEmpty() throws Exception {
        config.setPropertyValue("loggerName", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'objectFactoryConfig' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_objectFactoryConfigMissing() throws Exception {
        config.removeChild("objectFactoryConfig");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'lateDeliverableManagerKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_lateDeliverableManagerKeyMissing() throws Exception {
        config.removeProperty("lateDeliverableManagerKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'lateDeliverableManagerKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_lateDeliverableManagerKeyEmpty() throws Exception {
        config.setPropertyValue("lateDeliverableManagerKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'lateDeliverableManagerKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_lateDeliverableManagerKeyNotString() throws Exception {
        config.setPropertyValue("lateDeliverableManagerKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'managerResourceRoleIds' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_managerResourceRoleIdsMissing() throws Exception {
        config.removeProperty("managerResourceRoleIds");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'managerResourceRoleIds' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_managerResourceRoleIdsEmpty() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new String[0]);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'managerResourceRoleIds' is not a string array.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_managerResourceRoleIdsNotStringArray() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new Object[]{1, "0"});

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'managerResourceRoleIds' contains non-positive long.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_managerResourceRoleIdsContainsNonPositive() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new Object[]{"1", "0"});

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'managerResourceRoleIds' contains invalid number.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_managerResourceRoleIdsContainsInvalidNumber() throws Exception {
        config.setPropertyValues("managerResourceRoleIds", new Object[]{"1", "invalid_num"});

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'resourceManagerKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_resourceManagerKeyMissing() throws Exception {
        config.removeProperty("resourceManagerKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'resourceManagerKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_resourceManagerKeyEmpty() throws Exception {
        config.setPropertyValue("resourceManagerKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'resourceManagerKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_resourceManagerKeyNotString() throws Exception {
        config.setPropertyValue("resourceManagerKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'userRetrievalKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_userRetrievalKeyMissing() throws Exception {
        config.removeProperty("userRetrievalKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'userRetrievalKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_userRetrievalKeyEmpty() throws Exception {
        config.setPropertyValue("userRetrievalKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'userRetrievalKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_userRetrievalKeyNotString() throws Exception {
        config.setPropertyValue("userRetrievalKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailSubjectTemplateText' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailSubjectTemplateTextMissing() throws Exception {
        config.removeProperty("emailSubjectTemplateText");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailSubjectTemplateText' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailSubjectTemplateTextNotString() throws Exception {
        config.setPropertyValue("emailSubjectTemplateText", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailBodyTemplatePath' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailBodyTemplatePathMissing() throws Exception {
        config.removeProperty("emailBodyTemplatePath");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailBodyTemplatePath' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailBodyTemplatePathEmpty() throws Exception {
        config.setPropertyValue("emailBodyTemplatePath", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailBodyTemplatePath' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailBodyTemplatePathNotString() throws Exception {
        config.setPropertyValue("emailBodyTemplatePath", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailSender' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailSenderMissing() throws Exception {
        config.removeProperty("emailSender");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailSender' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailSenderEmpty() throws Exception {
        config.setPropertyValue("emailSender", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'emailSender' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_emailSenderNotString() throws Exception {
        config.setPropertyValue("emailSender", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'timestampFormat' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_timestampFormatEmpty() throws Exception {
        config.setPropertyValue("timestampFormat", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'timestampFormat' is invalid.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_timestampFormatInvalid() throws Exception {
        config.setPropertyValue("timestampFormat", "yyyy-MYM-dd HH:mm");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'timestampFormat' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_timestampFormatNotString() throws Exception {
        config.setPropertyValue("timestampFormat", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'projectManagerKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_projectManagerKeyMissing() throws Exception {
        config.removeProperty("projectManagerKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'projectManagerKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_projectManagerKeyEmpty() throws Exception {
        config.setPropertyValue("projectManagerKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'projectManagerKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_projectManagerKeyNotString() throws Exception {
        config.setPropertyValue("projectManagerKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'lateDeliverableManagerKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_phasePersistenceKeyMissing() throws Exception {
        config.removeProperty("phasePersistenceKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
 phasePersistenceKeyateDeliverableManagerKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_phasePersistenceKeyEmpty() throws Exception {
        config.setPropertyValue("phasePersistenceKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'phasePersistenceKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_phasePersistenceKeyNotString() throws Exception {
        config.setPropertyValue("phasePersistenceKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'lateDeliverableManagerKey' is missing.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testdeliverablePersistenceKeygerKeyMissing() throws Exception {
        config.removeProperty("deliverablePersistenceKey");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'deliverablePersistenceKey' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_deliverablePersistenceKeyEmpty() throws Exception {
        config.setPropertyValue("deliverablePersistenceKey", " \t ");

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}
     * constructor with 'deliverablePersistenceKey' is not a string.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor_deliverablePersistenceKeyNotString() throws Exception {
        config.setPropertyValue("deliverablePersistenceKey", 1);

        try {
            target = new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Late deliverable table has one record and one email should be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_1() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        target.execute();

        // Manually check the email
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Late deliverable table has no record and no email should be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_2() throws Exception {
        cleanTables();

        config.setPropertyValues("managerResourceRoleIds", new Object[] {"1000"});
        target = new NotRespondedLateDeliverablesNotifier(config);

        target.execute();

        // No email
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTracker#execute()} method with an unexpected error
     * occurred.
     * </p>
     * <p>
     * The exception will be logged.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_UnexpectedError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockUserRetrieval.setResult(new RuntimeException("RuntimeException for testing."));

        config.getChild("objectFactoryConfig").getChild("userRetrieval")
            .setPropertyValue("type", MockUserRetrieval.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);

        target.execute();
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a late deliverable management
     * error occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_LateDeliverableManagementError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        config.getChild("objectFactoryConfig").getChild("lateDeliverableManager")
            .setPropertyValue("type", MockLateDeliverableManager.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a search builder error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_SearchBuilderError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockResourceManager.setResult(new SearchBuilderException("SearchBuilderException for testing."));

        config.getChild("objectFactoryConfig").getChild("resourceManager")
            .setPropertyValue("type", MockResourceManager.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a resource persistence error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_ResourcePersistenceError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockResourceManager.setResult(new ResourcePersistenceException("ResourcePersistenceException for testing."));

        config.getChild("objectFactoryConfig").getChild("resourceManager")
            .setPropertyValue("type", MockResourceManager.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a project persistence error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_ProjectPersistenceError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockProjectManager.setResult(new PersistenceException("PersistenceException for testing."));

        config.getChild("objectFactoryConfig").getChild("projectManager")
            .setPropertyValue("type", MockProjectManager.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a phase persistence error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_PhasePersistenceError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockPhasePersistence.setResult(new PhasePersistenceException("PhasePersistenceException for testing."));

        config.getChild("objectFactoryConfig").getChild("phasePersistence")
            .setPropertyValue("type", MockPhasePersistence.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a deliverable persistence error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_DeliverablePersistenceError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockDeliverablePersistence.setResult(
            new DeliverablePersistenceException("DeliverablePersistenceException for testing."));

        config.getChild("objectFactoryConfig").getChild("deliverablePersistence")
            .setPropertyValue("type", MockDeliverablePersistence.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with there is no matching
     * deliverable in the persistence.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_DeliverablePersistenceNoResult() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        config.getChild("objectFactoryConfig").getChild("deliverablePersistence")
            .setPropertyValue("type", MockDeliverablePersistence.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with a user detail Retrieval error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_UserRetrievalError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        MockUserRetrieval.setResult(new RetrievalException("RetrievalException for testing."));

        config.getChild("objectFactoryConfig").getChild("userRetrieval")
            .setPropertyValue("type", MockUserRetrieval.class.getName());
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method with an email sending error
     * occurred.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_EmailSendingError() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        tracker.execute();
        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        setColumn("resource_info", "resource_info_type_id", 2);

        config.setPropertyValue("emailSender", "invalid_\n_address@com");
        target = new NotRespondedLateDeliverablesNotifier(config);
        try {
            target.execute();
            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (NotRespondedLateDeliverablesNotificationException e) {
            // Good
        }
    }
}