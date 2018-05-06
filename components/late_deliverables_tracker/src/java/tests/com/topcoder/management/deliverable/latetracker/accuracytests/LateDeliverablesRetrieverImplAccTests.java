/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import java.util.List;
import java.util.Set;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;

/**
 * Accuracy tests for LateDeliverablesRetrieverImpl.
 * Changes in 1.3: add reject final fix type.
 *
 * @author mumujava, victorsam, gjw99
 * @version 1.3
 */
public class LateDeliverablesRetrieverImplAccTests extends AccuracyHelper {
    /** Represents the LateDeliverablesRetrieverImpl instance to test. */
    private LateDeliverablesRetrieverImpl instance;

    /**
     * <p>Sets up the unit tests.</p>
     */
    public void setUp() throws Exception {
        super.setUp();
        instance = new LateDeliverablesRetrieverImpl();
    }

    /**
     * <p>Cleans up the unit tests.</p>
     * @throws Exception to junit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Accuracy test for method LateDeliverablesRetrieverImpl.
     */
    public void test_LateDeliverablesRetrieverImpl() {
        assertNotNull(instance);
    }

    /**
     * Accuracy test for method retrieve.
     *
     *
     * @throws Exception to junit
     */
    public void test_retrieve() throws Exception {
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 0, res.size());
    }

    /**
     * Accuracy test for method retrieve.
     * Changes in 1.3: add reject final fix type.
     *
     * @throws Exception to junit
     * @version 1.3
     */
    public void test_retrieve2() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 3, res.size());

        assertEquals("the return value should be correct", 1, res.get(0).getProject().getId());
        assertEquals("the return value should be correct", 2, res.get(1).getProject().getId());
        assertEquals("the return value should be correct", 2, res.get(2).getProject().getId());
    }

    /**
     * Accuracy test for method retrieve.  The project is closed and the Deliverable is late.
     *
     *
     * @throws Exception to junit
     */
    public void test_retrieve3() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test_project_closed.sql");

        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 0, res.size());
    }

    /**
     * Accuracy test for method retrieve. The project is under_Scheduled and the Deliverable is late.
     *
     *
     * @throws Exception to junit
     */
    public void test_retrieve4() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test_project_under_Scheduled.sql");

        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 0, res.size());
    }

    /**
     * Accuracy test for method retrieve.  The deliverable is not late.
     *
     *
     * @throws Exception to junit
     */
    public void test_retrieve5() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test_not_late.sql");

        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 0, res.size());
    }

    /**
     * Accuracy test for method retrieve.  Two Deliverable is late.
     *
     *
     * @throws Exception to junit
     */
    public void test_retrieve6() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test_double_late.sql");

        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        List<LateDeliverable> res = instance.retrieve(getTypes());
        assertEquals("the return value should be correct", 2, res.size());

        LateDeliverable deliverable = res.get(0);
        assertEquals("the return value should be correct", 1, deliverable.getProject().getId());
        deliverable = res.get(1);
        assertEquals("the return value should be correct", 1, deliverable.getProject().getId());
    }

    /**
     * Accuracy test for method retrieve.
     *
     * Of type REJECTED_FINAL_FIX, the "Rejected Final Fix" late deliverable is created for the subsequent final fix.
     *
     *
     * @throws Exception to JUnit
     * @since 1.3
     */
    public void test_retrieve7() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        instance = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        instance.configure(config);
        Set<LateDeliverableType> types = getTypes();
        types.remove(LateDeliverableType.MISSED_DEADLINE);
        List<LateDeliverable> result = instance.retrieve(types);
        for (LateDeliverable deliverable : result) {
            assertEquals("the late deliverable should be found for project 2", 2, deliverable.getProject().getId());
            assertEquals("the late deliverable should be found only for subsequenct final fix", 113, deliverable.getPhase().getId());
        }
    }
}
