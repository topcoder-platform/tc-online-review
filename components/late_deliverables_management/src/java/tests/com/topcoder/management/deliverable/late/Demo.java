/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late;

import java.sql.Connection;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * Shows usage for the component.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Updated demo.</li>
 * </ol>
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public class Demo {
    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(Demo.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        TestsHelper.unloadConfig();
        TestsHelper.loadConfig("SearchBundleManager.xml");

        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        TestsHelper.unloadConfig();

        TestsHelper.clearDB(connection);
        TestsHelper.closeConnection(connection);
        connection = null;
    }

    /**
     * <p>
     * Demo API usage of this component.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void testDemo() throws Exception {
        // Create an instance of LateDeliverableManagerImpl using custom configuration
        ConfigurationObject configuration = TestsHelper.getConfig();
        LateDeliverableManagerImpl lateDeliverableManager = new LateDeliverableManagerImpl(configuration);

        // Create an instance of LateDeliverableManagerImpl using custom config file and namespace
        lateDeliverableManager = new LateDeliverableManagerImpl(
            LateDeliverableManagerImpl.DEFAULT_CONFIG_FILENAME,
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);

        // Create an instance of LateDeliverableManagerImpl using default config file
        lateDeliverableManager = new LateDeliverableManagerImpl();

        // Retrieve the late deliverable with ID=1
        LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(1);
        // lateDeliverable.getId() must be 1
        // lateDeliverable.getProjectPhaseId() must be 101
        // lateDeliverable.getResourceId() must be 1001
        // lateDeliverable.getDeliverableId() must be 4
        // lateDeliverable.isForgiven() must be false
        // lateDeliverable.getExplanation() must be null
        // lateDeliverable.getType().getId() must be 1
        // lateDeliverable.getType().getName() must be "Missed Deadline"
        // lateDeliverable.getType().getDescription() must be "Missed Deadline"

        // Update the late deliverable by changing its forgiven flag and explanation
        lateDeliverable.setForgiven(true);
        lateDeliverable.setExplanation("OR didn't work");
        lateDeliverableManager.update(lateDeliverable);

        // Search for all forgiven late deliverables for project with ID=100000
        Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(true);
        Filter projectIdFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100000);
        Filter compositeFilter = new AndFilter(forgivenFilter, projectIdFilter);

        List<LateDeliverable> lateDeliverables = lateDeliverableManager.searchAllLateDeliverables(compositeFilter);
        // lateDeliverables.size() must be 1
        // lateDeliverables.get(0).getId() must be 1
        // lateDeliverables.get(0).getProjectPhaseId() must be 101
        // lateDeliverables.get(0).getResourceId() must be 1001
        // lateDeliverables.get(0).getDeliverableId() must be 4
        // lateDeliverables.get(0).isForgiven() must be true
        // lateDeliverables.get(0).getExplanation() must be "OR didn't work"
        // lateDeliverables.get(0).getType().getId() must be 1
        // lateDeliverables.get(0).getType().getName() must be "Missed Deadline"
        // lateDeliverables.get(0).getType().getDescription() must be "Missed Deadline"

        // Search for all late deliverables from design category for all active projects
        // to which user with ID=3 has a manager/copilot access
        Filter categoryFilter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1);
        Filter activeProjectFilter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(1);
        compositeFilter = new AndFilter(categoryFilter, activeProjectFilter);
        lateDeliverables = lateDeliverableManager.searchRestrictedLateDeliverables(compositeFilter, 3);
        // lateDeliverables.size() must be 1
        // lateDeliverables.get(0).getId() must be 2
        // lateDeliverables.get(0).getProjectPhaseId() must be 102
        // lateDeliverables.get(0).getResourceId() must be 1002
        // lateDeliverables.get(0).getDeliverableId() must be 3
        // lateDeliverables.get(0).isForgiven() must be false
        // lateDeliverables.get(0).getExplanation() must be null
        // lateDeliverables.get(0).getType().getId() must be 1
        // lateDeliverables.get(0).getType().getName() must be "Missed Deadline"
        // lateDeliverables.get(0).getType().getDescription() must be "Missed Deadline"

        // Retrieve all late deliverable types
        List<LateDeliverableType> lateDeliverableTypes = lateDeliverableManager.getLateDeliverableTypes();
        // lateDeliverableTypes.size() must be 2
        // lateDeliverableTypes.get(0).getId() must be 1
        // lateDeliverableTypes.get(0).getName() must be "Missed Deadline"
        // lateDeliverableTypes.get(0).getDescription() must be "Missed Deadline"
        // lateDeliverableTypes.get(1).getId() must be 2
        // lateDeliverableTypes.get(1).getName() must be "Rejected Final Fix"
        // lateDeliverableTypes.get(1).getDescription() must be "Rejected Final Fix"
    }
}
