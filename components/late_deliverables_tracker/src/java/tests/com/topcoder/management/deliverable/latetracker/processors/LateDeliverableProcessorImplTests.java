/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.processors;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.deliverable.latetracker.BaseTestCase;
import com.topcoder.management.deliverable.latetracker.EmailSendingException;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableData;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesProcessingException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;

/**
 * Unit tests for <code>{@link LateDeliverableProcessorImpl}</code> class.
 *
 * <p>
 * <em>Change in 1.1:</em>
 * <ol>
 * <li>Updated tests for processLateDeliverable.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Change in 1.2:</em>
 * <ol>
 * <li>Updated tests for explanationDeadlineIntervalInHours.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Added/Updated test cases.</li>
 * </ol>
 * </p>
 *
 * @author myxgyy, sparemax
 * @version 1.3
 */
public class LateDeliverableProcessorImplTests extends BaseTestCase {
    /**
     * The <code>{@link LateDeliverableProcessorImpl}</code> instance used for testing.
     */
    private LateDeliverableProcessorImpl target;

    /**
     * The <code>{@link LateDeliverablesRetrieverImpl}</code> instance used for testing.
     */
    private LateDeliverablesRetrieverImpl retriever;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * The tracked late deliverable types used for testing.
     *
     * @since 1.3
     */
    private Set<LateDeliverableType> trackedLateDeliverableTypes;

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

        retriever = new LateDeliverablesRetrieverImpl();
        config = getConfigurationObject("config/LateDeliverablesRetrieverImpl.xml",
            LateDeliverablesRetrieverImpl.class.getName());
        retriever.configure(config);

        target = new LateDeliverableProcessorImpl();
        config = getConfigurationObject("config/LateDeliverableProcessorImpl.xml",
            LateDeliverableProcessorImpl.class.getName());
        target.configure(config);

        trackedLateDeliverableTypes = EnumSet.allOf(LateDeliverableType.class);
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
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#LateDeliverableProcessorImpl()} method.
     * </p>
     * <p>
     * Verifies the default value of all class fields.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor() throws Exception {
        target = new LateDeliverableProcessorImpl();
        assertNull("defaultMissedDeadlineEmailSubjectTemplateText field should be null", getField(target,
            "defaultMissedDeadlineEmailSubjectTemplateText"));
        assertNull("defaultMissedDeadlineEmailBodyTemplatePath field should be null",
            getField(target, "defaultMissedDeadlineEmailBodyTemplatePath"));
        assertNull("missedDeadlineEmailBodyTemplatePaths field should be null",
            getField(target, "missedDeadlineEmailBodyTemplatePaths"));
        assertNull("missedDeadlineEmailSubjectTemplateTexts field should be null",
            getField(target, "missedDeadlineEmailSubjectTemplateTexts"));
        assertNull("missedDeadlineNotificationDeliverableIds field should be null",
            getField(target, "missedDeadlineNotificationDeliverableIds"));
        assertNull("dbConnectionFactory field should be null", getField(target, "dbConnectionFactory"));
        assertNull("log field should be null", getField(target, "log"));
        assertNull("connectionName field should be null", getField(target, "connectionName"));
        assertNull("emailSendingUtility field should be null", getField(target, "emailSendingUtility"));
        assertNull("resourceManager field should be null", getField(target, "resourceManager"));
        assertNull("userRetrieval field should be null", getField(target, "userRetrieval"));
        assertNull("timestampFormat field should be null", getField(target, "timestampFormat"));
        assertEquals("notificationInterval field should be zero",
            new Long(0), getField(target, "notificationInterval"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * <p>
     * <em>Change in 1.2:</em>
     * <ol>
     * <li>Updated tests for explanationDeadlineIntervalInHours.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void test_Configure_1() throws Exception {
        assertEquals("defaultMissedDeadlineEmailSubjectTemplateText field should be correct",
            "WARNING\\: You are late when providing a deliverable for %PROJECT_NAME%", getField(target,
                "defaultMissedDeadlineEmailSubjectTemplateText"));
        assertEquals("defaultMissedDeadlineEmailBodyTemplatePath field should be correct",
            "test_files/warn_email_template.html", getField(target, "defaultMissedDeadlineEmailBodyTemplatePath"));

        Map<?, ?> body = (Map) getField(target, "missedDeadlineEmailBodyTemplatePaths");
        assertEquals("missedDeadlineEmailBodyTemplatePaths field wrong", 1, body.size());
        assertEquals("missedDeadlineEmailBodyTemplatePaths field wrong",
            "test_files/warn_email_template.html", body.get(new Long(3)));

        Map<?, ?> subject = (Map) getField(target, "missedDeadlineEmailSubjectTemplateTexts");
        assertEquals("missedDeadlineEmailSubjectTemplateTexts field wrong", 1, subject.size());
        assertEquals("missedDeadlineEmailSubjectTemplateTexts field wrong",
            "WARNING\\: You are late when providing a deliverable for %PROJECT_NAME%", subject.get(new Long(3)));

        Set<?> missedDeadlineNotificationDeliverableIds =
            (Set) getField(target, "missedDeadlineNotificationDeliverableIds");
        assertEquals("missedDeadlineNotificationDeliverableIds field wrong",
            1, missedDeadlineNotificationDeliverableIds.size());
        assertTrue("missedDeadlineNotificationDeliverableIds field wrong",
            missedDeadlineNotificationDeliverableIds.contains(new Long(4)));
        assertNotNull("dbConnectionFactory field should be correct", getField(target, "dbConnectionFactory"));
        assertNotNull("log field should be correct", getField(target, "log"));
        assertEquals("connectionName field should be correct", "informix_connection",
            getField(target, "connectionName"));
        assertNotNull("emailSendingUtility field should be correct", getField(target, "emailSendingUtility"));
        assertNotNull("resourceManager field should be correct", getField(target, "resourceManager"));
        assertNotNull("userRetrieval field should be correct", getField(target, "userRetrieval"));
        assertNotNull("timestampFormat field should be correct", getField(target, "timestampFormat"));
        assertEquals("notificationInterval field should be correct", new Long(10),
            getField(target, "notificationInterval"));

        assertEquals("explanationDeadlineIntervalInHours field should be correct", 24,
            getField(target, "explanationDeadlineIntervalInHours"));

        assertEquals("rejectedFinalFixEmailSubjectTemplateText field should be correct",
            "WARNING\\: You need to explain why your Final Fix for %PROJECT_NAME% was rejected", getField(target,
                "rejectedFinalFixEmailSubjectTemplateText"));
        assertEquals("rejectedFinalFixEmailBodyTemplatePath field should be correct",
            "test_files/rejected_ff_email_template.html", getField(target, "rejectedFinalFixEmailBodyTemplatePath"));

        Map<LateDeliverableType, Long> lateDeliverableTypeIds =
            (Map<LateDeliverableType, Long>) getField(target, "lateDeliverableTypeIds");
        assertEquals("lateDeliverableTypeIds field wrong",
            2, lateDeliverableTypeIds.size());

        assertTrue("sendRejectedFinalFixNotifications field should be correct",
            (Boolean) getField(target, "sendRejectedFinalFixNotifications"));
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The given config is <code>null</code>, <code>IllegalArgumentException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_2() throws Exception {
        try {
            target.configure(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_3() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is not type of String in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_4() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineNotificationDeliverableIds property is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_6() throws Exception {
        config.setPropertyValue("missedDeadlineNotificationDeliverableIds", "x,3");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineNotificationDeliverableIds property is negative in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_7() throws Exception {
        config.setPropertyValue("missedDeadlineNotificationDeliverableIds", "-5");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineNotificationDeliverableIds property is not type of String in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_8() throws Exception {
        config.setPropertyValue("missedDeadlineNotificationDeliverableIds", new Exception());

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The child config to create database connection factory does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_9() throws Exception {
        config.removeChild("dbConnectionFactoryConfig");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The emailSender property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_10() throws Exception {
        config.setPropertyValue("emailSender", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The emailSender property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_11() throws Exception {
        config.removeProperty("emailSender");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The child config to create object factory does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_12() throws Exception {
        config.removeChild("objectFactoryConfig");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_13() throws Exception {
        config.setPropertyValue("resourceManagerKey", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_14() throws Exception {
        config.removeProperty("resourceManagerKey");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The resourceManagerKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_15() throws Exception {
        config.setPropertyValue("resourceManagerKey", "not_exist");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_16() throws Exception {
        config.setPropertyValue("userRetrievalKey", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_17() throws Exception {
        config.removeProperty("userRetrievalKey");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The userRetrievalKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_18() throws Exception {
        config.setPropertyValue("userRetrievalKey", "not_exist");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The timestampFormat property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_19() throws Exception {
        config.setPropertyValue("timestampFormat", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The timestampFormat property value is invalid,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_20() throws Exception {
        config.setPropertyValue("timestampFormat", "vgfgfg");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The notificationInterval property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_21() throws Exception {
        config.setPropertyValue("notificationInterval", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The notificationInterval property value is negative in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_22() throws Exception {
        config.setPropertyValue("notificationInterval", "-10");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The notificationInterval property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_23() throws Exception {
        config.setPropertyValue("notificationInterval", "ccc");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The defaultMissedDeadlineEmailSubjectTemplateText property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_24() throws Exception {
        config.removeProperty("defaultMissedDeadlineEmailSubjectTemplateText");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The defaultMissedDeadlineEmailBodyTemplatePath property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_25() throws Exception {
        config.removeProperty("defaultMissedDeadlineEmailBodyTemplatePath");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The defaultMissedDeadlineEmailBodyTemplatePath property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_26() throws Exception {
        config.setPropertyValue("defaultMissedDeadlineEmailBodyTemplatePath", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineEmailSubjectForDeliverable3 property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_27() throws Exception {
        config.setPropertyValue("missedDeadlineEmailSubjectForDeliverable3", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineEmailBodyForDeliverable3 property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_28() throws Exception {
        config.setPropertyValue("missedDeadlineEmailBodyForDeliverable3", "");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineEmailBodyForDeliverable in config has invalid suffix,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_29() throws Exception {
        config.setPropertyValue("missedDeadlineEmailBodyForDeliverableX", "value");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineEmailSubjectForDeliverable in config has invalid suffix,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_30() throws Exception {
        config.setPropertyValue("missedDeadlineEmailSubjectForDeliverableX", "value");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * All optional values do not exist and the default email subject text is empty in
     * config.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void test_Configure_31() throws Exception {
        config.removeProperty("notificationInterval");
        config.removeProperty("timestampFormat");
        config.removeProperty("missedDeadlineEmailSubjectForDeliverable3");
        config.removeProperty("missedDeadlineEmailBodyForDeliverable3");
        config.removeProperty("missedDeadlineNotificationDeliverableIds");
        config.removeProperty("loggerName");
        config.removeProperty("connectionName");
        config.setPropertyValue("defaultMissedDeadlineEmailSubjectTemplateText", "");
        target.configure(config);
        assertNull("log field should be null", getField(target, "log"));
        assertNull("connectionName field should be null", getField(target, "connectionName"));
        assertEquals("defaultMissedDeadlineEmailSubjectTemplateText field should be null", "", getField(target,
            "defaultMissedDeadlineEmailSubjectTemplateText"));

        Map<?, ?> body = (Map) getField(target, "missedDeadlineEmailBodyTemplatePaths");
        assertEquals("missedDeadlineEmailBodyTemplatePaths field wrong", 0, body.size());

        Map<?, ?> subject = (Map) getField(target, "missedDeadlineEmailSubjectTemplateTexts");
        assertEquals("missedDeadlineEmailSubjectTemplateTexts field wrong", 0, subject.size());

        Set<?> missedDeadlineNotificationDeliverableIds =
            (Set) getField(target, "missedDeadlineNotificationDeliverableIds");
        assertEquals("missedDeadlineNotificationDeliverableIds field wrong",
            0, missedDeadlineNotificationDeliverableIds.size());
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The configuration to create database connection factory is invalid,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_32() throws Exception {
        try {
            target.configure(getConfigurationObject("invalid_config/LateDeliverableProcessorImpl2.xml",
                LateDeliverableProcessorImpl.class.getName()));
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The default connection in database connection factory configuration is unknown,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_33() throws Exception {
        try {
            target.configure(getConfigurationObject("invalid_config/LateDeliverableProcessorImpl3.xml",
                LateDeliverableProcessorImpl.class.getName()));
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The Object configured under key 'userRetrievalKey' in ObjectFactory configuration
     * has wrong type., <code>LateDeliverablesTrackerConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_34() throws Exception {
        try {
            target.configure(getConfigurationObject("invalid_config/LateDeliverableProcessorImpl4.xml",
                LateDeliverableProcessorImpl.class.getName()));
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The Object configured under key 'resourceManagerKey' in ObjectFactory configuration
     * has wrong type., <code>LateDeliverablesTrackerConfigurationException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_35() throws Exception {
        try {
            target.configure(getConfigurationObject("invalid_config/LateDeliverableProcessorImpl5.xml",
                LateDeliverableProcessorImpl.class.getName()));
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedFinalFixEmailSubjectTemplateText property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_36() throws Exception {
        config.removeProperty("rejectedFinalFixEmailSubjectTemplateText");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedFinalFixEmailBodyTemplatePath property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_37() throws Exception {
        config.removeProperty("rejectedFinalFixEmailBodyTemplatePath");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The rejectedFinalFixEmailBodyTemplatePath property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_38() throws Exception {
        config.setPropertyValue("rejectedFinalFixEmailBodyTemplatePath", " ");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_39() throws Exception {
        config.removeProperty("lateDeliverableTypeIds");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_40() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", " ");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_41() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_42() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=invalid_num");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_43() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=-1");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_44() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=0");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypeIds property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_45() throws Exception {
        config.setPropertyValue("lateDeliverableTypeIds", "invalid=1");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The sendRejectedFinalFixNotifications property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_46() throws Exception {
        config.setPropertyValue("sendRejectedFinalFixNotifications", " ");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The sendRejectedFinalFixNotifications property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_47() throws Exception {
        config.setPropertyValue("sendRejectedFinalFixNotifications", "True");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_48() throws Exception {
        config.removeProperty("sendRejectedFinalFixNotifications");

        target.configure(config);

        assertTrue("sendRejectedFinalFixNotifications field should be correct", (Boolean) getField(target,
            "sendRejectedFinalFixNotifications"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverableProcessorImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Configure_49() throws Exception {
        config.setPropertyValue("sendRejectedFinalFixNotifications", "false");

        target.configure(config);

        assertFalse("sendRejectedFinalFixNotifications field should be correct", (Boolean) getField(target,
            "sendRejectedFinalFixNotifications"));
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method with
     * 'explanationDeadlineIntervalInHours' is empty.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.2
     */
    public void test_Configure_explanationDeadlineIntervalInHoursEmpty() throws Exception {
        config.setPropertyValue("explanationDeadlineIntervalInHours", " \t ");

        try {
            target.configure(config);

            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method with
     * 'explanationDeadlineIntervalInHours' is invalid.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.2
     */
    public void test_Configure_explanationDeadlineIntervalInHoursInvalid() throws Exception {
        config.setPropertyValue("explanationDeadlineIntervalInHours", "invalid_num");

        try {
            target.configure(config);

            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method with
     * 'explanationDeadlineIntervalInHours' is non-positive.
     * </p>
     * <p>
     * <code>LateDeliverablesTrackerConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.2
     */
    public void test_Configure_explanationDeadlineIntervalInHoursNonPositive() throws Exception {
        config.setPropertyValue("explanationDeadlineIntervalInHours", "0");

        try {
            target.configure(config);

            fail("LateDeliverablesTrackerConfigurationException is expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // Good
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The user is late for the first time, one record should be written to late
     * deliverable table, and the notification email should be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_1() throws Exception {
        config.removeProperty("explanationDeadlineIntervalInHours");
        target.configure(config);

        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        LateDeliverable lateDeliverable = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        lateDeliverable.setCompensatedDeadline(new Date());

        target.processLateDeliverable(lateDeliverable);

        // check database record
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());

        LateDeliverableData data = datas.get(0);
        assertFalse("forgive should be false", data.isForgive());
        assertTrue("last notified time wrong", (System.currentTimeMillis() - data.getLastNotified().getTime()) < 5000);
        assertEquals("should be equal", data.getCreateDate(), data.getLastNotified());
        assertEquals("should be equal", retriever.retrieve(trackedLateDeliverableTypes).get(0).getPhase()
            .getScheduledEndDate(), data.getDeadline());
        assertEquals("deliverable id wrong", 4, data.getDeliverableId());

        // please manually check the email
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)}
     * method with "Rejected Final Fix".
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_processLateDeliverable_New() throws Exception {
        config.removeProperty("explanationDeadlineIntervalInHours");
        target.configure(config);

        setupPhases(new long[] {101L, 102L}, new long[] {10L, 9L}, new long[] {3L, 2L}, true);
        createDependency(101L, 102L);

        LateDeliverable lateDeliverable = retriever.retrieve(trackedLateDeliverableTypes).get(0);

        target.processLateDeliverable(lateDeliverable);

        // check database record
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());

        LateDeliverableData data = datas.get(0);
        assertFalse("forgive should be false", data.isForgive());
        assertEquals("should be equal", 102, data.getProjectPhaseId());

        // please manually check the email
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The user is late again, and the notification interval time past, two email should
     * be sent. The last notified time should be updated.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_2() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
        // sleep to pass the interval
        Thread.sleep(15000);

        setColumn("late_deliverable", "explanation", "Dog ate my laptop.");
        target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));

        // verify the update
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());

        LateDeliverableData data = datas.get(0);
        assertTrue("last notified date not update", data.getCreateDate().before(data.getLastNotified()));

        // manually check two email
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The user is late again, but the notification interval time does not past, one email
     * should be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_3() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
        target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
        // verify the update
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());

        LateDeliverableData data = datas.get(0);
        assertEquals("should be equal", data.getCreateDate(), (data.getLastNotified()));

        // manually check the email
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The deliverable id does not exist in configured ids, the database should have one record with
     * null last notified time, email should not be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_4() throws Exception {
        setupPhases(new long[] {112L}, new long[] {3L}, new long[] {2L}, true);
        target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());
        assertNull("should be null", datas.get(0).getLastNotified());
        // no email send here
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Verifies the delay time in this case. The delay time should be 1 day 12 hours.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_5() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        LateDeliverable d = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        // delay time : two days
        target.processLateDeliverable(d);
        // extends the deadline
        // delay time : 1 day 12 hours
        d.getPhase().setScheduledEndDate(new Date(d.getPhase().getScheduledEndDate().getTime() + (DAY / 2)));
        target.processLateDeliverable(d);

        checkResult(getLateDeliverable(), new Date(d.getPhase().getScheduledEndDate().getTime() - (DAY / 2)),
            d.getPhase().getScheduledEndDate());
    }

    /**
     * Checks the retrieved late deliverable data.
     *
     * @param result the retrieved late deliverable data.
     * @param first the expected deadline of first record in given result.
     * @param second the expected deadline of second record in given result.
     */
    private static void checkResult(List<LateDeliverableData> result, Date first, Date second) {
        assertEquals("should have one record", 2, result.size());

        LateDeliverableData data = result.get(0);
        assertFalse("forgive should be false", data.isForgive());
        assertTrue("last notified time wrong", (System.currentTimeMillis() - data.getLastNotified().getTime()) < 5000);
        assertEquals("should be equal", data.getCreateDate(), data.getLastNotified());
        assertEquals("should be equal", first, data.getDeadline());
        assertEquals("deliverable id wrong", 4, data.getDeliverableId());

        data = result.get(1);
        assertFalse("forgive should be false", data.isForgive());
        assertTrue("last notified time wrong", (System.currentTimeMillis() - data.getLastNotified().getTime()) < 5000);
        assertEquals("should be equal", data.getCreateDate(), data.getLastNotified());
        assertEquals("should be equal", second, data.getDeadline());
        assertEquals("deliverable id wrong", 4, data.getDeliverableId());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Verifies the delay time in this case. The delay time should be 1 minute.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_6() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        LateDeliverable d = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        target.processLateDeliverable(d);
        // extends the deadline
        // delay time : 1 minute
        d.getPhase().setScheduledEndDate(new Date(d.getPhase().getScheduledEndDate().getTime() + (DAY * 2)));
        target.processLateDeliverable(d);

        checkResult(getLateDeliverable(), new Date(d.getPhase().getScheduledEndDate().getTime() - (DAY * 2)),
            d.getPhase().getScheduledEndDate());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Verifies the delay time in this case. The delay time should be 20 minutes.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_7() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        LateDeliverable d = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        target.processLateDeliverable(d);
        // extends the deadline
        // delay time : 20 minutes
        d.getPhase().setScheduledEndDate(
            new Date(d.getPhase().getScheduledEndDate().getTime() + ((DAY * 2) - 1200000)));
        target.processLateDeliverable(d);

        checkResult(getLateDeliverable(),
            new Date(d.getPhase().getScheduledEndDate().getTime() - ((DAY * 2) - 1200000)),
            d.getPhase().getScheduledEndDate());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Verifies the delay time in this case. The delay time should be 1 hour.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_8() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        LateDeliverable d = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        target.processLateDeliverable(d);
        // extends the deadline
        // delay time : 1 hour
        d.getPhase().setScheduledEndDate(
            new Date(d.getPhase().getScheduledEndDate().getTime() + ((DAY * 2) - (60 * 60 * 1000))));
        target.processLateDeliverable(d);

        checkResult(getLateDeliverable(), new Date(d.getPhase().getScheduledEndDate().getTime()
            - ((DAY * 2) - (60 * 60 * 1000))), d.getPhase().getScheduledEndDate());
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Uses the email subject and body template from two inner map in this case.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_9() throws Exception {
        config.setPropertyValue("missedDeadlineEmailSubjectForDeliverable4", "Subject");
        config.setPropertyValue("missedDeadlineEmailBodyForDeliverable4", "test_files/warn_email_template.html");
        target.configure(config);

        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        LateDeliverable d = retriever.retrieve(trackedLateDeliverableTypes).get(0);
        target.processLateDeliverable(d);

        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The connection name is unknown, <code>LateDeliverablesProcessingException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_10() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        config.setPropertyValue("connectionName", "unknown");
        target.configure(config);

        try {
            target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The default connection does not exist,
     * <code>LateDeliverablesProcessingException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_11() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        // force to use default connection
        config.removeProperty("connectionName");
        target.configure(config);

        try {
            target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The email subject template text is invalid, <code>EmailSendingException</code>
     * expected. And changes made into database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_12() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        // invalid template
        config.setPropertyValue("defaultMissedDeadlineEmailSubjectTemplateText", "Subject:");
        target.configure(config);

        try {
            target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
            fail("should have thrown EmailSendingException");
        } catch (EmailSendingException e) {
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The resource does not exist, <code>LateDeliverablesProcessingException</code>
     * expected. And changes made into database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_13() throws Exception {
        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * External Reference ID property value of resource can not parse to long,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_14() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "invalid");

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * External user with specified id does not exist,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_15() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "3000");

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * Error occurred when getting resource,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_16() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        // load failure config
        target.configure(getConfigurationObject("invalid_config/LateDeliverableProcessorImpl1.xml",
            LateDeliverableProcessorImpl.class.getName()));

        try {
            target.processLateDeliverable(retriever.retrieve(trackedLateDeliverableTypes).get(0));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The property value of [Project Name] is null,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_17() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "1");

        try {
            target.processLateDeliverable(createLateDeliverable(1));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The property value of [Project Version] is null,,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_18() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "1");

        try {
            target.processLateDeliverable(createLateDeliverable(2));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The name of deliverable is null,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_19() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "1");

        try {
            target.processLateDeliverable(createLateDeliverable(3));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The property value of [Project Name] is not type of <code>String</code>,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_20() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "1");

        try {
            target.processLateDeliverable(createLateDeliverable(4));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The phase type of phase is null,
     * <code>LateDeliverablesProcessingException</code> expected. And changes made into
     * database should be undone.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_21() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        insertResources(getConnection(), new Resource[] {createResource(1000L, 112L, 1L, 2L)});
        insertResourceInfo(getConnection(), 1000, 1, "1");

        try {
            target.processLateDeliverable(createLateDeliverable(5));
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // verify roll back
            assertEquals("no record", 0, getLateDeliverable().size());
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>timestampFormat</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_22() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "timestampFormat", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>dbConnectionFactory</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_23() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "dbConnectionFactory", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>emailSendingUtility</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_24() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "emailSendingUtility", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>missedDeadlineNotificationDeliverableIds</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_25() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "missedDeadlineNotificationDeliverableIds", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>missedDeadlineEmailSubjectTemplateTexts</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_26() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "missedDeadlineEmailSubjectTemplateTexts", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>missedDeadlineEmailBodyTemplatePaths</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_27() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "missedDeadlineEmailBodyTemplatePaths", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>defaultMissedDeadlineEmailSubjectTemplateText</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_28() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "defaultMissedDeadlineEmailSubjectTemplateText", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>defaultMissedDeadlineEmailBodyTemplatePath</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_29() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "defaultMissedDeadlineEmailBodyTemplatePath", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>resourceManager</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_30() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "resourceManager", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The <code>userRetrieval</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_31() throws Exception {
        setField(LateDeliverableProcessorImpl.class, target, "userRetrieval", null);

        try {
            target.processLateDeliverable(createLateDeliverable(0));
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The phase of lateDeliverable is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_32() throws Exception {
        LateDeliverable d = createLateDeliverable(0);
        d.setPhase(null);

        try {
            target.processLateDeliverable(d);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The project of lateDeliverable is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_33() throws Exception {
        LateDeliverable d = createLateDeliverable(0);
        d.setProject(null);

        try {
            target.processLateDeliverable(d);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The deliverable of lateDeliverable is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_34() throws Exception {
        LateDeliverable d = createLateDeliverable(0);
        d.setDeliverable(null);

        try {
            target.processLateDeliverable(d);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverableProcessorImpl#processLateDeliverable(LateDeliverable)} method.
     * </p>
     * <p>
     * The deliverable of lateDeliverable is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_processLateDeliverable_35() throws Exception {
        LateDeliverable d = createLateDeliverable(0);
        d.setPhase(new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 100));

        try {
            target.processLateDeliverable(d);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }
}