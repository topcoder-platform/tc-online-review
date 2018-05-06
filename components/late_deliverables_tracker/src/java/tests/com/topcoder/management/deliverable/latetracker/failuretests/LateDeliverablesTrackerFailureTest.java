/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableProcessor;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesProcessingException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesRetrievalException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesRetriever;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTracker;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.project.phases.Phase;

/**
 * Failure test for LateDeliverablesTracker class.
 *
 * @author mumujava
 * @version 1.3
 */
public class LateDeliverablesTrackerFailureTest extends TestCase {
    /**
     * Represents the instance of LateDeliverablesTracker used in test.
     */
    private LateDeliverablesTracker tracker;

    /**
     * The instance of LateDeliverablesRetriever used to initializing LateDeliverablesTracker.
     */
    private LateDeliverablesRetriever retriever;

    /**
     * The instance of LateDeliverableProcessor used to initializing LateDeliverablesTracker.
     */
    private LateDeliverableProcessor processor;

    /**
     * The ConfigurationObject used to initializing LateDeliverablesTracker.
     */
    private ConfigurationObject config;

    /**
     * Set up for each test.
     *
     * @throws Exception
     *             to jUnit.
     */
    protected void setUp() throws Exception {
        TestHelper.addConfig();
        TestHelper.executeSqlFile("test_files/failure/insert.sql");

        ConfigurationObject lateDeliverablesRetrieverConfig = TestHelper.getConfigurationObject(
            "failure/Retriever.xml", "failuretests");

        ConfigurationObject lateDeliverableProcessorConfig = TestHelper.getConfigurationObject("failure/Processor.xml",
            "failuretests");

        retriever = new LateDeliverablesRetrieverImpl();
        retriever.configure(lateDeliverablesRetrieverConfig);

        processor = new LateDeliverableProcessorImpl();
        processor.configure(lateDeliverableProcessorConfig);

        Set<LateDeliverableType> set = new HashSet<LateDeliverableType>();
        set.add(LateDeliverableType.MISSED_DEADLINE);
        set.add(LateDeliverableType.REJECTED_FINAL_FIX);
        // Create LateDeliverablesTracker
        tracker = new LateDeliverablesTracker(retriever, processor, set, null);

        config = TestHelper.getConfigurationObject("failure/LateDeliverablesTracker.xml", LateDeliverablesTracker.class
            .getName());
    }

    /**
     * Tear down for each test case.
     *
     * @throws Exception
     *             to jUnit.
     */
    protected void tearDown() throws Exception {
        TestHelper.cleanTables();
        TestHelper.clearNamespace();
    }

    /**
     * Test the constructor LateDeliverablesTracker(LateDeliverablesRetriever retriever, LateDeliverableProcessor
     * processor, Log log). When retriever is null.
     */
    public void testCtor1_RetrieverIsNull() {
        Set<LateDeliverableType> set = new HashSet<LateDeliverableType>();
        set.add(LateDeliverableType.MISSED_DEADLINE);
        set.add(LateDeliverableType.REJECTED_FINAL_FIX);
        try {
            new LateDeliverablesTracker(null, processor, set, null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(LateDeliverablesRetriever retriever, LateDeliverableProcessor
     * processor, Log log). When processor is null.
     */
    public void testCtor1_ProcessorIsNull() {
        Set<LateDeliverableType> set = new HashSet<LateDeliverableType>();
        set.add(LateDeliverableType.MISSED_DEADLINE);
        set.add(LateDeliverableType.REJECTED_FINAL_FIX);
        try {
            new LateDeliverablesTracker(retriever, null, set, null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When config is null.
     */
    public void testCtor2_ConfigIsNull() {
        try {
            new LateDeliverablesTracker(null);
            fail("Cannot go here.");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the loggerName property is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LoggerNameIsEmpty() throws Exception {
        config.setPropertyValue("loggerName", "");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the loggerName property is invalid
     * type.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LoggerNameIsInvalidType() throws Exception {
        config.setPropertyValue("loggerName", 1);
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the objectFactoryConfig is not
     * existed.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_ObjectFactoryConfigIsNotExisted() throws Exception {
        config.removeChild("objectFactoryConfig");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverablesRetrieverKey
     * property is missed.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverablesRetrieverKeyIsMissed() throws Exception {
        config.removeProperty("lateDeliverablesRetrieverKey");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverablesRetrieverKey
     * property is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverablesRetrieverKeyIsEmpty() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", " ");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverablesRetrieverKey
     * property is invalid type.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverablesRetrieverKeyIsInvalidType() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", 1);
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverablesRetrieverKey
     * property is invalid key.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverablesRetrieverKeyIsInvalidKey() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", "a");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the
     * lateDeliverablesRetrieverConfig is missed.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverablesRetrieverConfigIsMissed() throws Exception {
        config.removeChild("lateDeliverablesRetrieverConfig");

        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverableProcessorKey
     * property is missed.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverableProcessorKeyIsMissed() throws Exception {
        config.removeProperty("lateDeliverableProcessorKey");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverableProcessorKey
     * property is empty.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverableProcessorKeyIsEmpty() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", " ");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverableProcessorKey
     * property is invalid type.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverableProcessorKeyIsInvalidType() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", 1);
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverableProcessorKey
     * property is invalid key.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverableProcessorKeyIsInvalidKey() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", "a");
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }

    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). When the lateDeliverableProcessorConfig
     * is missed.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testCtor2_LateDeliverableProcessorConfigIsMissed() throws Exception {
        config.removeChild("lateDeliverableProcessorConfig");

        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }
    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). lateDeliverableTypes is empty.
     *
     * @throws Exception
     *             to JUnit.
     * @since 1.3
     */
    public void testCtor20() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new String[0]);
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }
    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). lateDeliverableTypes contains duplicate.
     *
     * @throws Exception
     *             to JUnit.
     * @since 1.3
     */
    public void testCtor21() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new String[]{"Missed Deadline","Missed Deadline"});
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }
    /**
     * Test the constructor LateDeliverablesTracker(ConfigurationObject config). lateDeliverableTypes contains empty.
     *
     * @throws Exception
     *             to JUnit.
     * @since 1.3
     */
    public void testCtor22() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new String[]{"Missed Deadline"," "});
        try {
            new LateDeliverablesTracker(config);
            fail("Cannot go here.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // OK
        }
    }
    /**
     * Test the method execute().
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecute_RetrieveError() throws Exception {
        retriever.configure(TestHelper.getConfigurationObject("failure/Retriever1.xml",
            LateDeliverablesRetrieverImpl.class.getName()));

        try {
            tracker.execute();
            fail("Cannot go here.");
        } catch (LateDeliverablesRetrievalException e) {
            // OK
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Fails to process the late deliverable, <code>LateDeliverablesProcessingException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testExecute_ProcessException() throws Exception {
        TestHelper.setPrivateField(LateDeliverablesTracker.class, tracker, "lateDeliverablesRetriever",
            new LateDeliverablesRetriever() {

                /**
                 * Gets the list of LateDeliverable.
                 *
                 * @return the list of LateDeliverable.
                 */
                public List<LateDeliverable> retrieve(
                        Set<LateDeliverableType> types) throws LateDeliverablesRetrievalException {
                    List<LateDeliverable> list = new ArrayList<LateDeliverable>();

                    LateDeliverable entity = new LateDeliverable();
                    entity.setCompensatedDeadline(new Date());
                    entity.setDeliverable(new Deliverable(1, 1, 1, new Long(1), false));
                    entity.setProject(new Project(1, new ProjectCategory(1, "n", new ProjectType(1, "1")),
                        new ProjectStatus(1, "1")));
                    entity.setPhase(new Phase(
                        new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 1));
                    entity.getPhase().setScheduledEndDate(new Date());
                    entity.setType(LateDeliverableType.MISSED_DEADLINE);
                    list.add(entity);
                    return list;
                }

                /**
                 * Do nothing.
                 *
                 * @param config
                 *            the configuration object.
                 */
                public void configure(ConfigurationObject config) {
                }
            });

        processor.configure(TestHelper.getConfigurationObject("failure/processor1.xml",
            LateDeliverableProcessorImpl.class.getName()));

        try {
            tracker.execute();
            fail("Cannot go here.");
        } catch (LateDeliverablesProcessingException e) {
            // OK
        }
    }
}