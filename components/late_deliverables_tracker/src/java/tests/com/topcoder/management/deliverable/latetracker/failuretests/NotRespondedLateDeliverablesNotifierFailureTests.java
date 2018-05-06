/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotificationException;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;
import com.topcoder.util.config.ConfigManager;
import junit.framework.TestCase;

/**
 * Failure test cases for <code>NotRespondedLateDeliverablesNotifierFailure</code> class.
 *
 * @author jmn
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotifierFailureTests extends TestCase {

    /**
     * Represents the instance to be tested.
     */
    private NotRespondedLateDeliverablesNotifier instance;

    /**
     * Represents the ConfigurationObject to configure.
     */
    private ConfigurationObject config;

    /**
     * Set up for test cases.
     *
     * @throws Exception to jUnit.
     */
    public void setUp() throws Exception {

        TestHelper.addConfig();
        TestHelper.executeSqlFile("test_files/failure/insert.sql");

        ConfigManager.getInstance().add("failure/SearchBundleManager.xml");
        ConfigManager.getInstance().add("failure/InformixPhasePersistence.xml");
        // read the ConfigurationObject configObject
        config = TestHelper.getConfigurationObject(
                "failure/LateDeliverablesTrackingUtility.xml",
                LateDeliverablesTrackingUtility.class.getName());

        config = config.getChild("notificationJobConfig");
        // create the instance
        instance = new NotRespondedLateDeliverablesNotifier(config);
    }

    /**
     * Tear down for test cases.
     *
     * @throws Exception to jUnit.
     */
    public void tearDown() throws Exception {
        TestHelper.cleanTables();
        TestHelper.clearNamespace();
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     */
    public void testCtor() {

        assertNotNull("Instance hasn't been created.", instance);
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    public void testCtorError() {

        try {
            new NotRespondedLateDeliverablesNotifier(null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link IllegalArgumentException} is expected.</p>
     */
    public void testCtorError1() {

        try {
            new NotRespondedLateDeliverablesNotifier(new DefaultConfigurationObject("invalid_config"));
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError2() throws Exception {

        config.setPropertyValue("loggerName", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError3() throws Exception {

        config.removeChild("objectFactoryConfig");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError4() throws Exception {

        config.removeChild("objectFactoryConfig");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError5() throws Exception {

        config.removeChild("objectFactoryConfig");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError6() throws Exception {

        config.removeProperty("lateDeliverableManagerKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError7() throws Exception {

        config.setPropertyValue("lateDeliverableManagerKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError8() throws Exception {

        config.removeProperty("managerResourceRoleIds");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError9() throws Exception {

        config.setPropertyValues("managerResourceRoleIds", new Object[]{" ", " "});

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError10() throws Exception {

        config.setPropertyValues("managerResourceRoleIds", new Object[]{"a", "b"});

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError11() throws Exception {

        config.removeProperty("resourceManagerKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError12() throws Exception {

        config.setPropertyValue("resourceManagerKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError13() throws Exception {

        config.removeProperty("userRetrievalKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError14() throws Exception {

        config.setPropertyValue("userRetrievalKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError15() throws Exception {

        config.removeProperty("emailSubjectTemplateText");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError16() throws Exception {

        config.removeProperty("emailBodyTemplatePath");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError17() throws Exception {

        config.setPropertyValue("emailBodyTemplatePath", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError18() throws Exception {

        config.removeProperty("emailSender");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError19() throws Exception {

        config.setPropertyValue("emailSender", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError20() throws Exception {

        config.setPropertyValue("timestampFormat", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError21() throws Exception {

        config.removeProperty("projectManagerKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError22() throws Exception {

        config.setPropertyValue("projectManagerKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError23() throws Exception {

        config.removeProperty("phasePersistenceKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError24() throws Exception {

        config.setPropertyValue("phasePersistenceKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError25() throws Exception {

        config.removeProperty("deliverablePersistenceKey");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests constructor of {@link NotRespondedLateDeliverablesNotifier} class.</p>
     *
     * <p>{@link LateDeliverablesTrackerConfigurationException} is expected.</p>
     *
     * @throws Exception if any error occurs
     */
    public void testCtorError26() throws Exception {

        config.setPropertyValue("deliverablePersistenceKey", " ");

        try {
            new NotRespondedLateDeliverablesNotifier(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * <p>Tests the {@link NotRespondedLateDeliverablesNotifier#execute()} method.</p>
     */
    public void testExecute() throws NotRespondedLateDeliverablesNotificationException {

        instance.execute();
    }
}
