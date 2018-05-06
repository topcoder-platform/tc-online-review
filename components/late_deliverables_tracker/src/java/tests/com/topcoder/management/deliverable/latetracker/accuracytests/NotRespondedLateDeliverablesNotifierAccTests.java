/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker;
import com.topcoder.management.deliverable.latetracker.notification.NotRespondedLateDeliverablesNotifier;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;

/**
 * Accuracy tests for <code>{@link NotRespondedLateDeliverablesNotifier}</code> class.
 *
 * @author KLW
 * @version 1.2
 * @since 1.2
 */
public class NotRespondedLateDeliverablesNotifierAccTests extends AccuracyHelper {
    /**
     * The <code>{@link NotRespondedLateDeliverablesNotifier}</code> instance used for testing.
     */
    private NotRespondedLateDeliverablesNotifier instance;

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

        config = getConfigurationObject("accuracy/config/LateDeliverablesTracker.xml",
                LateDeliverablesTracker.class.getName());
        tracker = new LateDeliverablesTracker(config);

        retriever = new LateDeliverablesRetrieverImpl();
        config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            LateDeliverablesRetrieverImpl.class.getName());
        retriever.configure(config);

        config = getConfigurationObject("accuracy/config/NotRespondedLateDeliverablesNotifier.xml",
            NotRespondedLateDeliverablesNotifier.class.getName());
        instance = new NotRespondedLateDeliverablesNotifier(config);
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
        instance = null;
    }

    /**
     * <p>
     * Accuracy test for constructor method
     * {@link NotRespondedLateDeliverablesNotifier#NotRespondedLateDeliverablesNotifier(ConfigurationObject config)}.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Ctor() throws Exception {
        instance = new NotRespondedLateDeliverablesNotifier(config);
        assertNotNull("the instance should be created.", instance);
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
    public void test_execute() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");
        tracker.execute();

        AccuracyHelper.updateColumn("late_deliverable", "explanation", "some explanation.");
        AccuracyHelper.updateColumn("resource_info", "resource_info_type_id", 2);

        instance.execute();
        // check the email
    }

}