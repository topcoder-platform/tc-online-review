/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import org.junit.Assert;
import org.junit.Test;

import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.search.builder.filter.EqualToFilter;

/**
 * <p> Accuracy test for <code>LateDeliverableFilterBuilder</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableFilterBuilderTest {

    /**
     * <p> Accuracy test for <code>createProjectIdFilter(long projectId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testCreateProjectIdFilter_accuracy() throws Exception {
        Assert.assertNotNull(LateDeliverableFilterBuilder.createProjectIdFilter(1L));
    }

    /**
     * <p> Accuracy test for <code>createProjectStatusIdFilter(long projectStatusId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testCreateProjectStatusIdFilter_accuracy() throws Exception {
        Assert.assertNotNull(LateDeliverableFilterBuilder.createProjectStatusIdFilter(1L));
    }

    /**
     * <p> Accuracy test for <code>createProjectCategoryIdFilter(long projectCategoryId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testCreateProjectCategoryIdFilter_accuracy() throws Exception {
        Assert.assertNotNull(LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1L));
    }

    /**
     * <p> Accuracy test for <code>createDeliverableIdFilter(long deliverableId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testCreateDeliverableIdFilter_accuracy() throws Exception {
        Assert.assertNotNull(LateDeliverableFilterBuilder.createDeliverableIdFilter(1L));
    }

    /**
     * <p> Accuracy test for <code>createForgivenFilter(boolean forgiven)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testCreateForgivenFilter_accuracy() throws Exception {
        Assert.assertNotNull(LateDeliverableFilterBuilder.createForgivenFilter(false));
    }

    /**
     * <p> Accuracy test for <code>createLateDeliverableTypeIdFilter(long id)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testcreateLateDeliverableTypeIdFilter_accuracy() throws Exception {
        EqualToFilter createLateDeliverableTypeIdFilter = (EqualToFilter)LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(1);
        Assert.assertEquals(createLateDeliverableTypeIdFilter.getName(), "lateDeliverableTypeId");
        Assert.assertEquals(createLateDeliverableTypeIdFilter.getValue(), new Long(1L));
    }
}
