/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * Tests the demo of this component.
 *
 * <p>
 * <em>Changes in version 1.3:</em>
 * <ol>
 * <li>Updated demo.</li>
 * </ol>
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.3
 */
public class Demo extends BaseTestCase {
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
        new File("guard.tmp").delete();

        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
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

        new File("guard.tmp").delete();
    }

    /**
     * Tests the API usage of this component.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Added usage of "NotRespondedLateDeliverablesNotifier".</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testAPIUsage() throws Exception {
        // Prepare configuration for LateDeliverablesRetriever
        ConfigurationObject lateDeliverablesRetrieverConfig = getConfigurationObject(
            "config/LateDeliverablesRetrieverImpl.xml", LateDeliverablesRetrieverImpl.class.getName());

        // Prepare configuration for LateDeliverableProcessor
        ConfigurationObject lateDeliverableProcessorConfig = getConfigurationObject(
            "config/LateDeliverableProcessorImpl.xml", LateDeliverableProcessorImpl.class.getName());

        // Create an instance of LateDeliverablesRetrieverImpl and configure it
        LateDeliverablesRetriever lateDeliverablesRetriever = new LateDeliverablesRetrieverImpl();
        lateDeliverablesRetriever.configure(lateDeliverablesRetrieverConfig);

        // Create an instance of LateDeliverableProcessorImpl and configure it
        LateDeliverableProcessor lateDeliverableProcessor = new LateDeliverableProcessorImpl();
        lateDeliverableProcessor.configure(lateDeliverableProcessorConfig);

        // Get logger
        Log log = LogFactory.getLog("my_logger");

        // Create LateDeliverablesTracker
        Set<LateDeliverableType> trackedLateDeliverableTypes = EnumSet.allOf(LateDeliverableType.class);
        LateDeliverablesTracker lateDeliverablesTracker = new LateDeliverablesTracker(
            lateDeliverablesRetriever, lateDeliverableProcessor, trackedLateDeliverableTypes, log);
        // Track for late deliverables
        lateDeliverablesTracker.execute();

        // Prepare configuration for NotRespondedLateDeliverablesNotifier
        ConfigurationObject notRespondedLateDeliverablesNotifierConfig = getConfigurationObject(
            "config/NotRespondedLateDeliverablesNotifier.xml", NotRespondedLateDeliverablesNotifier.class.getName());

        // Create an instance of NotRespondedLateDeliverablesNotifier
        NotRespondedLateDeliverablesNotifier notRespondedLateDeliverablesNotifier =
            new NotRespondedLateDeliverablesNotifier(notRespondedLateDeliverablesNotifierConfig);

        // Send notifications for explained but not responded late deliverables
        notRespondedLateDeliverablesNotifier.execute();
    }

    /**
     * Tests the command line usage of this component.
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>"interval" switch was renamed to "trackingInterval".</li>
     * <li>Added switch "notificationInterval".</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCommandLineUsage() throws Exception {
        // This command line can be used to print out the usage string
        LateDeliverablesTrackingUtility.main(new String[] {"-help"});
        // If configuration for the utility is stored in the default namespace
        // of the default configuration file, then the application can be
        // executed without additional arguments
        runMain(new String[0]);
        // To use the custom configuration file the user can provide "-c" switch
        // The user can specify custom import files utility configuration file name and
        // namespace
        runMain(new String[] {"-c=config/LateDeliverablesTrackingUtility.properties",
            "-ns=com.topcoder.management.deliverable.latetracker.utility.LateDeliverablesTrackingUtility",
            "-guardFile=guard.tmp", "-background=true"});
        // The user can specify the interval between late deliverable checks
        // and interval between sending PM notifications in the command line
        // (in this example deliverables will be checked every 5 minutes,
        // and notifications will be sent every hour)
        runMain(new String[] {"-trackingInterval =300", "-notificationInterval =3600", "-guardFile=guard.tmp",
            "-background=true"});
    }
}