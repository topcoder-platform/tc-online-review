/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;

import com.topcoder.configuration.ConfigurationObject;

import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;

import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Unit tests for <code>{@link LateDeliverablesTracker}</code> class.
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
public class LateDeliverablesTrackerTests extends BaseTestCase {
    /**
     * The <code>{@link LateDeliverablesTracker}</code> instance used for testing.
     */
    private LateDeliverablesTracker target;

    /**
     * The <code>{@link LateDeliverablesRetriever}</code> instance used for testing.
     */
    private LateDeliverablesRetriever lateDeliverablesRetriever;

    /**
     * The <code>{@link LateDeliverableProcessor}</code> instance used for testing.
     */
    private LateDeliverableProcessor lateDeliverableProcessor;

    /**
     * The <code>{@link Log}</code> instance used for testing.
     */
    private Log log;

    /**
     * The <code>ConfigurationObject</code> instance used for testing.
     */
    private ConfigurationObject config;

    /**
     * The tracked late deliverable types used for testing.
     *
     * @since 1.3
     */
    private Set<LateDeliverableType> lateDeliverableTypes;

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

        // Prepare configuration for LateDeliverablesRetriever
        ConfigurationObject lateDeliverablesRetrieverConfig = getConfigurationObject(
            "config/LateDeliverablesRetrieverImpl.xml", LateDeliverablesRetrieverImpl.class.getName());

        // Prepare configuration for LateDeliverableProcessor
        ConfigurationObject lateDeliverableProcessorConfig = getConfigurationObject(
            "config/LateDeliverableProcessorImpl.xml", LateDeliverableProcessorImpl.class.getName());

        // Create an instance of LateDeliverablesRetrieverImpl and configure it
        lateDeliverablesRetriever = new LateDeliverablesRetrieverImpl();
        lateDeliverablesRetriever.configure(lateDeliverablesRetrieverConfig);

        // Create an instance of LateDeliverableProcessorImpl and configure it
        lateDeliverableProcessor = new LateDeliverableProcessorImpl();
        lateDeliverableProcessor.configure(lateDeliverableProcessorConfig);

        lateDeliverableTypes = EnumSet.allOf(LateDeliverableType.class);

        // Get logger
        log = LogFactory.getLog("my_logger");

        // Create LateDeliverablesTracker
        target = new LateDeliverablesTracker(lateDeliverablesRetriever, lateDeliverableProcessor,
            lateDeliverableTypes, log);

        config = getConfigurationObject(
            "config/LateDeliverablesTracker.xml", LateDeliverablesTracker.class.getName());
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
     * Accuracy test case for the <code>LateDeliverablesTracker#LateDeliverablesTracker(LateDeliverablesRetriever,
     * LateDeliverableProcessor, Set&lt;LateDeliverableType&gt;, Log)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor1_1() throws Exception {
        assertNotNull("lateDeliverablesRetriever field should not be null", getField(target,
            "lateDeliverablesRetriever"));
        assertNotNull("lateDeliverableProcessor field should not be null", getField(target,
            "lateDeliverableProcessor"));
        assertNotNull("lateDeliverableTypes field should not be null", getField(target, "lateDeliverableTypes"));
        assertNotNull("log field should not be null", getField(target, "log"));
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesTracker#LateDeliverablesTracker(LateDeliverablesRetriever,
     * LateDeliverableProcessor, Set&lt;LateDeliverableType&gt;, Log)</code> method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Constructor1_2() throws Exception {
        lateDeliverableTypes = null;
        target = new LateDeliverablesTracker(lateDeliverablesRetriever, lateDeliverableProcessor,
            lateDeliverableTypes, log);

        assertNotNull("lateDeliverablesRetriever field should not be null", getField(target,
            "lateDeliverablesRetriever"));
        assertNotNull("lateDeliverableProcessor field should not be null", getField(target,
            "lateDeliverableProcessor"));
        assertNull("lateDeliverableTypes field should be null", getField(target, "lateDeliverableTypes"));
        assertNotNull("log field should not be null", getField(target, "log"));
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesTracker#LateDeliverablesTracker(LateDeliverablesRetriever,
     * LateDeliverableProcessor, Set&lt;LateDeliverableType&gt;, Log)</code> method.
     * </p>
     * <p>
     * The given <code>retriever</code> is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor1_3() throws Exception {
        try {
            new LateDeliverablesTracker(null, lateDeliverableProcessor,
                lateDeliverableTypes, log);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesTracker#LateDeliverablesTracker(LateDeliverablesRetriever,
     * LateDeliverableProcessor, Set&lt;LateDeliverableType&gt;, Log)</code> method.
     * </p>
     * <p>
     * The given <code>processor</code> is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor1_4() throws Exception {
        try {
            new LateDeliverablesTracker(lateDeliverablesRetriever, null,
                lateDeliverableTypes, log);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * All class fields should be set correctly according to configuration.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_1() throws Exception {
        target = new LateDeliverablesTracker(config);
        assertNotNull("lateDeliverablesRetriever field should not be null", getField(target,
            "lateDeliverablesRetriever"));
        assertNotNull("lateDeliverableProcessor field should not be null", getField(target,
            "lateDeliverableProcessor"));
        assertNotNull("log field should not be null", getField(target, "log"));
        assertNotNull("lateDeliverableTypes field should not be null", getField(target, "lateDeliverableTypes"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * All class fields should be set correctly according to configuration.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_2() throws Exception {
        config.removeProperty("lateDeliverableTypes");

        target = new LateDeliverablesTracker(config);
        assertNotNull("lateDeliverablesRetriever field should not be null", getField(target,
            "lateDeliverablesRetriever"));
        assertNotNull("lateDeliverableProcessor field should not be null", getField(target,
            "lateDeliverableProcessor"));
        assertNull("lateDeliverableTypes field should be null", getField(target, "lateDeliverableTypes"));
        assertNotNull("log field should not be null", getField(target, "log"));
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_3() throws Exception {
        config.setPropertyValue("loggerName", "");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The loggerName property is not type of String in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_4() throws Exception {
        config.setPropertyValue("loggerName", new Exception());

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverablesRetrieverKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_5() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", "");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverablesRetrieverKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_6() throws Exception {
        config.removeProperty("lateDeliverablesRetrieverKey");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverablesRetrieverKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_7() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", "not_exist");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverablesRetrieverKey property value is not type of <code>String</code>,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_8() throws Exception {
        config.setPropertyValue("lateDeliverablesRetrieverKey", new Exception());

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableProcessorKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_9() throws Exception {
        config.removeProperty("lateDeliverableProcessorKey");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableProcessorKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_10() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", "");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableProcessorKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_11() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", "not_exist");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableProcessorKey property value is not type of <code>String</code>,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_12() throws Exception {
        config.setPropertyValue("lateDeliverableProcessorKey", new Exception());

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableProcessorConfig child configuration does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_13() throws Exception {
        config.removeChild("lateDeliverableProcessorConfig");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverablesRetrieverConfig child configuration does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_14() throws Exception {
        config.removeChild("lateDeliverablesRetrieverConfig");

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The given configuration object is <code>null</code>,
     * <code>IllegalArgumentException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor2_15() throws Exception {
        try {
            new LateDeliverablesTracker(null);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypes property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Constructor2_16() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new Object[0]);

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypes property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Constructor2_17() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new Object[] {1});

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypes property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Constructor2_18() throws Exception {
        config.setPropertyValues("lateDeliverableTypes", new Object[] {"not_exist"});

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#LateDeliverablesTracker(ConfigurationObject)} method.
     * </p>
     * <p>
     * The lateDeliverableTypes property value is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_Constructor2_19() throws Exception {
        config.setPropertyValues("lateDeliverableTypes",
            new Object[] {"Rejected Final Fix", "Rejected Final Fix"});

        try {
            new LateDeliverablesTracker(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Late deliverable table should have one record and one email should be sent.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_1() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);

        target.execute();

        // verify record
        List<LateDeliverableData> datas = getLateDeliverable();
        assertEquals("should have one record", 1, datas.size());

        LateDeliverableData data = datas.get(0);
        assertFalse("forgive should be false", data.isForgive());
        assertTrue("last notified time wrong", (System.currentTimeMillis() - data.getLastNotified()
            .getTime()) < 5000);
        assertEquals("should be equal", data.getCreateDate(), data.getLastNotified());
        assertEquals("should be equal", lateDeliverablesRetriever.retrieve(lateDeliverableTypes).get(0)
            .getPhase().getScheduledEndDate(), data.getDeadline());
        assertEquals("deliverable id wrong", 4, data.getDeliverableId());

        // manually check the email
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Fails to retrieve the late deliverable,
     * <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_2() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        lateDeliverablesRetriever.configure(getConfigurationObject(
            "invalid_config/LateDeliverablesRetrieverImpl1.xml", LateDeliverablesRetrieverImpl.class
                .getName()));

        try {
            target.execute();
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the {@link LateDeliverablesTracker#execute()} method.
     * </p>
     * <p>
     * Fails to process the late deliverable,
     * <code>LateDeliverablesProcessingException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_execute_3() throws Exception {
        setupPhases(new long[] {112L}, new long[] {4L}, new long[] {2L}, true);
        lateDeliverableProcessor.configure(getConfigurationObject(
            "invalid_config/LateDeliverableProcessorImpl1.xml", LateDeliverableProcessorImpl.class
                .getName()));

        try {
            target.execute();
            fail("should have thrown LateDeliverablesProcessingException");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }
}
