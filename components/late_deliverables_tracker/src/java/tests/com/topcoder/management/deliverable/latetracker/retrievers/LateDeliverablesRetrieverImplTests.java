/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.retrievers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.deliverable.latetracker.BaseTestCase;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesRetrievalException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.persistence.DeliverableCheckingException;
import com.topcoder.management.deliverable.persistence.DeliverablePersistenceException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;

/**
 * Unit tests for <code>{@link LateDeliverablesRetrieverImpl}</code> class.
 *
 * <p>
 * <em>Change in 1.1:</em>
 * <ol>
 * <li>Updated tests for constructor, configure and retrieve.</li>
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
public class LateDeliverablesRetrieverImplTests extends BaseTestCase {
    /**
     * The <code>{@link LateDeliverablesRetrieverImpl}</code> instance used for testing.
     */
    private LateDeliverablesRetrieverImpl target;

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

        target = new LateDeliverablesRetrieverImpl();
        config = BaseTestCase.getConfigurationObject("config/LateDeliverablesRetrieverImpl.xml",
            LateDeliverablesRetrieverImpl.class.getName());
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
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#LateDeliverablesRetrieverImpl()} method.
     * </p>
     * <p>
     * Verifies the default value of all class fields.
     * </p>
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Verifies maxDurationOfPhaseWithCompensatedDeadline.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Constructor() throws Exception {
        target = new LateDeliverablesRetrieverImpl();
        assertNull("projectManager field should be null", getField(target, "projectManager"));
        assertNull("phaseManager field should be null", getField(target, "phaseManager"));
        assertNull("deliverableManager field should be null", getField(target, "deliverableManager"));
        assertNull("log field should be null", getField(target, "log"));
        assertNull("missedDeadlineTrackingDeliverableIds field should be null",
            getField(target, "missedDeadlineTrackingDeliverableIds"));
        assertEquals("maxDurationOfPhaseWithCompensatedDeadline field should be 0",
            0L, getField(target, "maxDurationOfPhaseWithCompensatedDeadline"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Verifies maxDurationOfPhaseWithCompensatedDeadline.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void test_Configure_1() throws Exception {
        assertNotNull("projectManager field should be null", getField(target, "projectManager"));
        assertNotNull("phaseManager field should be null", getField(target, "phaseManager"));
        assertNotNull("deliverableManager field should be null", getField(target, "deliverableManager"));
        assertNotNull("log field should be null", getField(target, "log"));

        Set<?> missedDeadlineTrackingDeliverableIds =
            (Set) getField(target, "missedDeadlineTrackingDeliverableIds");
        assertEquals("missedDeadlineTrackingDeliverableIds field wrong",
            2, missedDeadlineTrackingDeliverableIds.size());
        assertTrue("missedDeadlineTrackingDeliverableIds field wrong",
            missedDeadlineTrackingDeliverableIds.contains(new Long(3)));
        assertTrue("missedDeadlineTrackingDeliverableIds field wrong",
            missedDeadlineTrackingDeliverableIds.contains(new Long(4)));

        assertEquals("maxDurationOfPhaseWithCompensatedDeadline field wrong",
            86400000L, getField(target, "maxDurationOfPhaseWithCompensatedDeadline"));
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * Verifies all class fields have been set by configuration correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @SuppressWarnings("unchecked")
    public void test_Configure_New() throws Exception {
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", "1");
        config.removeProperty("loggerName");

        target = new LateDeliverablesRetrieverImpl();
        target.configure(config);


        assertNotNull("projectManager field should be null", getField(target, "projectManager"));
        assertNotNull("phaseManager field should be null", getField(target, "phaseManager"));
        assertNotNull("deliverableManager field should be null", getField(target, "deliverableManager"));
        assertNull("log field should be null", getField(target, "log"));

        Set<?> missedDeadlineTrackingDeliverableIds =
            (Set) getField(target, "missedDeadlineTrackingDeliverableIds");
        assertEquals("missedDeadlineTrackingDeliverableIds field wrong",
            2, missedDeadlineTrackingDeliverableIds.size());
        assertTrue("missedDeadlineTrackingDeliverableIds field wrong",
            missedDeadlineTrackingDeliverableIds.contains(new Long(3)));
        assertTrue("missedDeadlineTrackingDeliverableIds field wrong",
            missedDeadlineTrackingDeliverableIds.contains(new Long(4)));

        assertEquals("maxDurationOfPhaseWithCompensatedDeadline field wrong",
            1L, getField(target, "maxDurationOfPhaseWithCompensatedDeadline"));
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
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
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
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
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
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
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The missedDeadlineTrackingDeliverableIds property is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_5() throws Exception {
        config.removeProperty("missedDeadlineTrackingDeliverableIds");

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
     * The missedDeadlineTrackingDeliverableIds property is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_6() throws Exception {
        config.setPropertyValue("missedDeadlineTrackingDeliverableIds", "");

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
     * The missedDeadlineTrackingDeliverableIds property is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_7() throws Exception {
        config.setPropertyValue("missedDeadlineTrackingDeliverableIds", "x,3");

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
     * The missedDeadlineTrackingDeliverableIds property is not type of String in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_8() throws Exception {
        config.setPropertyValue("missedDeadlineTrackingDeliverableIds", new Exception());

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
     * The missedDeadlineTrackingDeliverableIds property is invalid in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_9() throws Exception {
        config.setPropertyValue("missedDeadlineTrackingDeliverableIds", ",");

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
     * The child config to create object factory does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_10() throws Exception {
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
     * The projectManagerKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_11() throws Exception {
        config.setPropertyValue("projectManagerKey", "");

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
     * The projectManagerKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_12() throws Exception {
        config.removeProperty("projectManagerKey");

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
     * The projectManagerKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_13() throws Exception {
        config.setPropertyValue("projectManagerKey", "not_exist");

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
     * The phaseManagerKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_14() throws Exception {
        config.setPropertyValue("phaseManagerKey", "");

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
     * The phaseManagerKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_15() throws Exception {
        config.removeProperty("phaseManagerKey");

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
     * The phaseManagerKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_16() throws Exception {
        config.setPropertyValue("phaseManagerKey", "not_exist");

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
     * The deliverablePersistenceKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_17() throws Exception {
        config.setPropertyValue("deliverablePersistenceKey", "");

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
     * The deliverablePersistenceKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_18() throws Exception {
        config.removeProperty("deliverablePersistenceKey");

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
     * The deliverablePersistenceKey property value can not be found in object factory
     * config, <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_19() throws Exception {
        config.setPropertyValue("deliverablePersistenceKey", "not_exist");

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
     * The searchBundleManagerNamespace property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_20() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace", "");

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
     * The searchBundleManagerNamespace property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_21() throws Exception {
        config.removeProperty("searchBundleManagerNamespace");

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
     * The searchBundleManagerNamespace property value is not type of String,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_22() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace", new Exception());

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
     * The searchBundleManagerNamespace property value is invalid to create search bundle
     * manager, <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_23() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace", "not_exist");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            assertTrue("check inner cause", e.getCause() instanceof SearchBuilderConfigurationException);
        }
    }

    /**
     * <p>
     * Failure test case for the {@link
     * LateDeliverablesRetrieverImpl#configure(ConfigurationObject)} method.
     * </p>
     * <p>
     * The child config start with deliverableChecker does not exist,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_24() throws Exception {
        config.removeChild("deliverableChecker1");
        config.removeChild("deliverableChecker2");

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
     * The deliverableName property value is empty,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_25() throws Exception {
        config.getChild("deliverableChecker1").setPropertyValue("deliverableName", "");

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
     * The deliverableName property value is missing,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_26() throws Exception {
        config.getChild("deliverableChecker1").removeProperty("deliverableName");

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
     * The deliverableName property value is not type of String,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_27() throws Exception {
        config.getChild("deliverableChecker1").setPropertyValue("deliverableName", new Exception());

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
     * The deliverableCheckerKey property value is empty in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_28() throws Exception {
        config.getChild("deliverableChecker1").setPropertyValue("deliverableCheckerKey", "");

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
     * The deliverableCheckerKey property value is missing in config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_29() throws Exception {
        config.getChild("deliverableChecker1").removeProperty("deliverableCheckerKey");

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
     * The deliverableCheckerKey property value can not be found in object factory config,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_Configure_30() throws Exception {
        config.getChild("deliverableChecker1").setPropertyValue("deliverableCheckerKey", "not found");

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
     * The maxDurationOfPhaseWithCompensatedDeadline property value is empty,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_Configure_31() throws Exception {
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", " ");

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
     * The maxDurationOfPhaseWithCompensatedDeadline property value is invalid,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_Configure_32() throws Exception {
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", "invalid_num");

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
     * The maxDurationOfPhaseWithCompensatedDeadline property value is negative,
     * <code>LateDeliverablesTrackerConfigurationException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_Configure_33() throws Exception {
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", "-1");

        try {
            target.configure(config);
            fail("should have thrown LateDeliverablesTrackerConfigurationException");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Verifies all late deliverables have been retrieved correctly. In this case, there is only one late phase.
     * </p>
     *
     * <p>
     * <em>Change in 1.1:</em>
     * <ol>
     * <li>Verifies the compensated deadline.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_1() throws Exception {
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, true);

        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 1, result.size());

        LateDeliverable deliverable = result.get(0);
        assertEquals("the project in late deliverable wrong", 1, deliverable.getProject().getId());
        assertEquals("the phase in late deliverable wrong", 101, deliverable.getPhase().getId());
        assertEquals("the deliverable name in late deliverable wrong", "Screening Scorecard",
            deliverable.getDeliverable().getName());

        assertNull("the compensated deadline in late deliverable wrong", deliverable.getCompensatedDeadline());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Verifies all late deliverables have been retrieved correctly. In this case, there are three phases, the first
     * phase is closed, and the other two are late phases.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_2() throws Exception {
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", "86500000");
        target = new LateDeliverablesRetrieverImpl();
        target.configure(config);

        // setup two phases have late deliverable
        setupPhases(new long[] {101L, 102L, 103L}, new long[] {2L, 3L, 4L}, new long[] {3L, 2L, 2L}, true);

        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have two late deliverables", 2, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_retrieve_New1() throws Exception {
        config.setPropertyValue("missedDeadlineTrackingDeliverableIds", "8,9,10");
        config.setPropertyValue("maxDurationOfPhaseWithCompensatedDeadline", "86500000");
        target = new LateDeliverablesRetrieverImpl();
        target.configure(config);

        // setup two phases have late deliverable
        setupPhases(new long[] {101L, 102L, 103L}, new long[] {8L, 9L, 10L}, new long[] {3L, 2L, 2L}, true);

        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have two late deliverables", 0, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_retrieve_New2() throws Exception {
        setupPhases(new long[] {101L, 102L}, new long[] {10L, 9L}, new long[] {3L, 2L}, true);
        createDependency(101L, 102L);

        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 2, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_retrieve_New3() throws Exception {
        trackedLateDeliverableTypes.clear();
        trackedLateDeliverableTypes.add(LateDeliverableType.REJECTED_FINAL_FIX);

        setupPhases(new long[] {101L, 102L}, new long[] {10L, 9L}, new long[] {3L, 2L},
            true);
        createDependency(101L, 102L);

        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 2, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_retrieve_New4() throws Exception {
        setupPhases(new long[] {101L, 102L}, new long[] {10L, 9L}, new long[] {3L, 2L}, true);
        createDependency(101L, 102L);

        trackedLateDeliverableTypes = null;
        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 2, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method with the dependency phase is Approval.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_retrieve_New5() throws Exception {
        setupPhases(new long[] {101L, 102L}, new long[] {11L, 9L}, new long[] {3L, 2L}, true);
        createDependency(101L, 102L);

        trackedLateDeliverableTypes = null;
        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 0, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.3
     */
    public void test_retrieve_New6() throws Exception {
        setupPhases(new long[] {101L, 102L}, new long[] {10L, 9L}, new long[] {3L, 2L}, true);
        createDependency(101L, 102L);

        trackedLateDeliverableTypes.clear();
        trackedLateDeliverableTypes.add(LateDeliverableType.MISSED_DEADLINE);
        List<LateDeliverable> result = target.retrieve(trackedLateDeliverableTypes);
        assertEquals("should have one late deliverable", 0, result.size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * In this case, no active project &quot;Track Late Deliverables&quot; property set to &quot;true&quot;. Empty
     * list expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_3() throws Exception {
        assertEquals("empty list expected", 0, target.retrieve(trackedLateDeliverableTypes).size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * In this case, no phase is late. Empty list expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_4() throws Exception {
        // the phase is not late
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, false);

        assertEquals("empty list expected", 0, target.retrieve(trackedLateDeliverableTypes).size());
    }

    /**
     * <p>
     * Accuracy test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * In this case, the late phase in not with type in tracking list. Empty list expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_5() throws Exception {
        setupPhases(new long[] {101L}, new long[] {5L}, new long[] {2L}, true);

        assertEquals("empty list expected", 0, target.retrieve(trackedLateDeliverableTypes).size());
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * The <code>projectManager</code> field is <code>null</code>, <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_6() throws Exception {
        setField(LateDeliverablesRetrieverImpl.class, target, "projectManager", null);

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * The <code>phaseManager</code> field is <code>null</code>, <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_7() throws Exception {
        setField(LateDeliverablesRetrieverImpl.class, target, "phaseManager", null);

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * The <code>deliverableManager</code> field is <code>null</code>, <code>IllegalStateException</code>
     * expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_8() throws Exception {
        setField(LateDeliverablesRetrieverImpl.class, target, "deliverableManager", null);

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * The <code>missedDeadlineTrackingDeliverableIds</code> field is <code>null</code>,
     * <code>IllegalStateException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_9() throws Exception {
        setField(LateDeliverablesRetrieverImpl.class, target, "missedDeadlineTrackingDeliverableIds", null);

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Error occurred when searching projects, <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_10() throws Exception {
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, true);

        // configure with failure configuration
        target.configure(getConfigurationObject("invalid_config/LateDeliverablesRetrieverImpl1.xml",
            LateDeliverablesRetrieverImpl.class.getName()));

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            assertTrue("check inner cause", e.getCause() instanceof PersistenceException);
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Error occurred when getting phase projects, <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_11() throws Exception {
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, true);

        // configure with failure configuration
        target.configure(getConfigurationObject("invalid_config/LateDeliverablesRetrieverImpl2.xml",
            LateDeliverablesRetrieverImpl.class.getName()));

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            assertTrue("check inner cause", e.getCause() instanceof PhaseManagementException);
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Error occurred when searching deliverables, <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_12() throws Exception {
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, true);

        // configure with failure configuration
        target.configure(getConfigurationObject("invalid_config/LateDeliverablesRetrieverImpl3.xml",
            LateDeliverablesRetrieverImpl.class.getName()));

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            assertTrue("check inner cause", e.getCause() instanceof DeliverablePersistenceException);
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * Error occurred when searching deliverables, <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_13() throws Exception {
        setupPhases(new long[] {101L}, new long[] {3L}, new long[] {2L}, true);

        // configure with failure configuration
        target.configure(getConfigurationObject("invalid_config/LateDeliverablesRetrieverImpl4.xml",
            LateDeliverablesRetrieverImpl.class.getName()));

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            assertTrue("check inner cause", e.getCause() instanceof SearchBuilderException);
        }
    }

    /**
     * <p>
     * Failure test case for the <code>LateDeliverablesRetrieverImpl#retrieve(Set&lt;LateDeliverableType&gt;)</code>
     * method.
     * </p>
     * <p>
     * The deliverable fails to pass the check, <code>LateDeliverablesRetrievalException</code> expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    public void test_retrieve_14() throws Exception {
        setupPhases(new long[] {1000L}, new long[] {3L}, new long[] {2L}, true);

        try {
            target.retrieve(trackedLateDeliverableTypes);
            fail("should have thrown LateDeliverablesRetrievalException");
        } catch (LateDeliverablesRetrievalException e) {
            assertTrue("check inner cause", e.getCause() instanceof DeliverableCheckingException);
        }
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_1() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase = new Phase(project, 86400000);

        Date res = callGetCompensatedDeadline(target, phase);

        assertNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_2() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase = new Phase(project, 86400000 - 1);

        Date res = callGetCompensatedDeadline(target, phase);

        assertNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_3() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase1 = new Phase(project, 86400000 - 1);
        phase1.setActualStartDate(new Date());
        Phase phase2 = new Phase(project, 86400000 - 1);
        phase2.setScheduledEndDate(new Date(System.currentTimeMillis() + (2 * 86400000L)));

        Dependency dependency1 = new Dependency(phase1, phase2, false, true, 1);
        phase2.addDependency(dependency1);
        Dependency dependency2 = new Dependency(phase1, phase2, true, true, 2);
        phase2.addDependency(dependency2);
        Dependency dependency3 = new Dependency(phase1, phase2, false, false, 3);
        phase2.addDependency(dependency3);

        Date res = callGetCompensatedDeadline(target, phase2);

        assertNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_4() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase1 = new Phase(project, 86400000 - 1);
        phase1.setActualStartDate(new Date());
        Phase phase2 = new Phase(project, 86400000 - 1);
        phase2.setScheduledEndDate(new Date());

        Dependency dependency1 = new Dependency(phase1, phase2, false, true, 1);
        phase2.addDependency(dependency1);
        Dependency dependency2 = new Dependency(phase1, phase2, true, true, 2);
        phase2.addDependency(dependency2);
        Dependency dependency3 = new Dependency(phase1, phase2, false, false, 3);
        phase2.addDependency(dependency3);

        Date res = callGetCompensatedDeadline(target, phase2);

        assertNotNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_5() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase1 = new Phase(project, 86400000 - 1);
        phase1.setActualStartDate(new Date());
        Phase phase2 = new Phase(project, 86400000 - 1);
        phase2.setScheduledEndDate(new Date());
        phase2.setFixedStartDate(new Date(System.currentTimeMillis() - (2 * 86400000L)));

        Dependency dependency1 = new Dependency(phase1, phase2, false, true, 1);
        phase2.addDependency(dependency1);
        Dependency dependency2 = new Dependency(phase1, phase2, false, true, 2);
        phase2.addDependency(dependency2);
        Dependency dependency3 = new Dependency(phase1, phase2, false, false, 3);
        phase2.addDependency(dependency3);

        Date res = callGetCompensatedDeadline(target, phase2);

        assertNotNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Accuracy test case for the {@link LateDeliverablesRetrieverImpl#getCompensatedDeadline()} method.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    public void test_getCompensatedDeadline_6() throws Exception {
        Project project = new Project(new Date(), new DefaultWorkdays());
        Phase phase1 = new Phase(project, 86400000 - 1);
        phase1.setActualStartDate(new Date());
        Phase phase2 = new Phase(project, 86400000 - 1);
        phase2.setScheduledEndDate(new Date());
        phase2.setFixedStartDate(new Date(System.currentTimeMillis() + (2 * 86400000L)));

        Dependency dependency1 = new Dependency(phase1, phase2, false, true, 1);
        phase2.addDependency(dependency1);
        Dependency dependency2 = new Dependency(phase1, phase2, true, true, 2);
        phase2.addDependency(dependency2);
        Dependency dependency3 = new Dependency(phase1, phase2, false, false, 3);
        phase2.addDependency(dependency3);

        Date res = callGetCompensatedDeadline(target, phase2);

        assertNotNull("the compensated deadline in late deliverable wrong", res);
    }

    /**
     * <p>
     * Calls the method 'getCompensatedDeadline'.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param phase
     *            the phase.
     *
     * @return the result.
     */
    private static Date callGetCompensatedDeadline(Object obj, Phase phase) {
        Object value = null;
        try {
            Method declaredMethod = obj.getClass().getDeclaredMethod("getCompensatedDeadline", Phase.class);
            declaredMethod.setAccessible(true);

            value = declaredMethod.invoke(obj, phase);

            declaredMethod.setAccessible(false);
        } catch (IllegalArgumentException e) {
            // Ignore
        } catch (SecurityException e) {
            // Ignore
        } catch (NoSuchMethodException e) {
            // Ignore
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (InvocationTargetException e) {
            // Ignore
        }

        return (Date) value;
    }
}
