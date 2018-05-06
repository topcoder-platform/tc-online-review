package com.topcoder.management.deliverable.latetracker.failuretests;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackingJobRunner;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotificationJobRunner;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;
import com.topcoder.util.config.ConfigManager;

/**
 * Failure test cases for <code>NotRespondedLateDeliverablesNotificationJobRunner</code> class.
 *
 * @author jmn
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotificationJobRunnerFailureTests extends TestCase {

    /**
     * Represents the instance of NotRespondedLateDeliverablesNotificationJobRunner used in test.
     */
    private NotRespondedLateDeliverablesNotificationJobRunner instance;

    /**
     * The ConfigurationObject used to initializing LateDeliverablesTracker.
     */
    private ConfigurationObject config;

    /**
     * Set up for each test.
     *
     * @throws Exception to jUnit.
     */
    protected void setUp() throws Exception {

        TestHelper.addConfig();
        TestHelper.executeSqlFile("test_files/failure/insert.sql");

        ConfigManager.getInstance().add("failure/SearchBundleManager.xml");
        ConfigManager.getInstance().add("failure/InformixPhasePersistence.xml");
        // read the ConfigurationObject configObject

        config = TestHelper.getConfigurationObject("failure/LateDeliverablesTrackingUtility.xml",
                LateDeliverablesTrackingUtility.class.getName());

        config = config.getChild("notificationJobConfig");

        instance = new NotRespondedLateDeliverablesNotificationJobRunner();
    }

    /**
     * Tear down for each test case.
     *
     * @throws Exception to jUnit.
     */
    protected void tearDown() throws Exception {

        TestHelper.clearNamespace();
    }

    /**
     * Failure tests for configure(ConfigurationObject config). When config is null.
     *
     * @throws Exception to JUnit.
     */
    public void testConfigure_ConfigIsNull() throws Exception {
        try {
            instance.configure(null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }


    /**
     * Failure test for configure(ConfigurationObject config). When loggerName is empty.
     *
     * @throws Exception to JUnit.
     */
    public void testConfigure_LoggerNameIsEmpty() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * Failure test for configure(ConfigurationObject config). When loggerName is empty.
     *
     * @throws Exception to JUnit.
     */
    public void testConfigure_LoggerNameIsInvalidType() throws Exception {
        config.setPropertyValue("loggerName", 1);

        try {
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException was expected.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // test passed
        }
    }

    /**
     * Test setJob(Job job). When job is null.
     *
     * @throws Exception to JUnit.
     */
    public void testSetJob_JobIsNull() throws Exception {
        try {
            instance.setJob(null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    /**
     * Failure test for run(). When not configured.
     *
     * @throws Exception to JUnit.
     */
    public void testRun_NoConfigured() throws Exception {
        try {
            new LateDeliverablesTrackingJobRunner().run();
            fail("IllegalStateException was expected.");
        } catch (IllegalStateException e) {
            // test passed
        }
    }

    /**
     * Failure test for setJobName(String jobName). When jobName is null.
     *
     * @throws Exception to JUnit.
     */
    public void testSetJobName_JobNameIsNull() throws Exception {
        try {
            instance.setJobName(null);
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }

    /**
     * Failure test for setJobName(String jobName). When jobName is empty.
     *
     * @throws Exception to JUnit.
     */
    public void testSetJobName_JobNameIsEmpty() throws Exception {
        try {
            instance.setJobName(" ");
            fail("IllegalArgumentException was expected.");
        } catch (IllegalArgumentException e) {
            // test passed
        }
    }
}
