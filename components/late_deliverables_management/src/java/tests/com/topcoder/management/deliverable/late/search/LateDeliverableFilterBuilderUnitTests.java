/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Date;

import junit.framework.JUnit4TestAdapter;

import org.junit.Test;

import com.topcoder.management.deliverable.late.TestsHelper;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;

/**
 * <p>
 * Unit tests for {@link LateDeliverableFilterBuilder} class.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added test cases for createLateDeliverableTypeIdFilter() method.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.0.6
 */
public class LateDeliverableFilterBuilderUnitTests {
    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LateDeliverableFilterBuilderUnitTests.class);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createProjectIdFilter(long projectId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createProjectIdFilter() {
        long value = 1;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createProjectIdFilter(value);

        assertEquals("'createProjectIdFilter' should be correct.", "projectId", filter.getName());
        assertEquals("'createProjectIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectIdFilter(long projectId)</code> with projectId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectIdFilter_projectIdNegative() {
        long value = -1;

        LateDeliverableFilterBuilder.createProjectIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectIdFilter(long projectId)</code> with projectId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectIdFilter_projectIdZero() {
        long value = 0;

        LateDeliverableFilterBuilder.createProjectIdFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createProjectStatusIdFilter(long projectStatusId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createProjectStatusIdFilter() {
        long value = 1;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createProjectStatusIdFilter(value);

        assertEquals("'createProjectStatusIdFilter' should be correct.", "projectStatusId", filter.getName());
        assertEquals("'createProjectStatusIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectStatusIdFilter(long projectStatusId)</code> with projectStatusId
     * is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectStatusIdFilter_projectStatusIdNegative() {
        long value = -1;

        LateDeliverableFilterBuilder.createProjectStatusIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectStatusIdFilter(long projectStatusId)</code> with projectStatusId
     * is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectStatusIdFilter_projectStatusIdZero() {
        long value = 0;

        LateDeliverableFilterBuilder.createProjectStatusIdFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createProjectCategoryIdFilter(long projectCategoryId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createProjectCategoryIdFilter() {
        long value = 1;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createProjectCategoryIdFilter(value);

        assertEquals("'createProjectCategoryIdFilter' should be correct.", "projectCategoryId", filter.getName());
        assertEquals("'createProjectCategoryIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectCategoryIdFilter(long projectCategoryId)</code> with
     * projectCategoryId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectCategoryIdFilter_projectCategoryIdNegative() {
        long value = -1;

        LateDeliverableFilterBuilder.createProjectCategoryIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectCategoryIdFilter(long projectCategoryId)</code> with
     * projectCategoryId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createProjectCategoryIdFilter_projectCategoryIdZero() {
        long value = 0;

        LateDeliverableFilterBuilder.createProjectCategoryIdFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createDeliverableIdFilter(long deliverableId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createDeliverableIdFilter() {
        long value = 1;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createDeliverableIdFilter(value);

        assertEquals("'createDeliverableIdFilter' should be correct.", "deliverableId", filter.getName());
        assertEquals("'createDeliverableIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createDeliverableIdFilter(long deliverableId)</code> with deliverableId is
     * negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createDeliverableIdFilter_deliverableIdNegative() {
        long value = -1;

        LateDeliverableFilterBuilder.createDeliverableIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createDeliverableIdFilter(long deliverableId)</code> with deliverableId is
     * zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createDeliverableIdFilter_deliverableIdZero() {
        long value = 0;

        LateDeliverableFilterBuilder.createDeliverableIdFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createForgivenFilter(boolean forgiven)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createForgivenFilter_1() {
        boolean value = true;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createForgivenFilter(value);

        assertEquals("'createForgivenFilter' should be correct.", "forgiven", filter.getName());
        assertEquals("'createForgivenFilter' should be correct.", 1, filter.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createForgivenFilter(boolean forgiven)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createForgivenFilter_2() {
        boolean value = false;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createForgivenFilter(value);

        assertEquals("'createForgivenFilter' should be correct.", "forgiven", filter.getName());
        assertEquals("'createForgivenFilter' should be correct.", 0, filter.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createUserHandleFilter(String userHandle)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createUserHandleFilter() {
        String value = "user";
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createUserHandleFilter(value);

        assertEquals("'createUserHandleFilter' should be correct.", "userHandle", filter.getName());
        assertEquals("'createUserHandleFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createUserHandleFilter(String userHandle)</code> with userHandle is null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createUserHandleFilter_userHandleNull() {
        String value = null;

        LateDeliverableFilterBuilder.createUserHandleFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createUserHandleFilter(String userHandle)</code> with userHandle is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createUserHandleFilter_userHandleEmpty() {
        String value = TestsHelper.EMPTY_STRING;

        LateDeliverableFilterBuilder.createUserHandleFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createCockpitProjectIdFilter(Long tcDirectProjectId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createCockpitProjectIdFilter1() {
        Long value = 1L;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createCockpitProjectIdFilter(value);

        assertEquals("'createCockpitProjectIdFilter' should be correct.", "tcDirectProjectId", filter.getName());
        assertEquals("'createCockpitProjectIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createCockpitProjectIdFilter(Long tcDirectProjectId)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createCockpitProjectIdFilter2() {
        Long value = null;
        NullFilter filter = (NullFilter) LateDeliverableFilterBuilder.createCockpitProjectIdFilter(value);

        assertEquals("'createCockpitProjectIdFilter' should be correct.", "tcDirectProjectId", filter.getName());
        assertNull("'createCockpitProjectIdFilter' should be correct.", filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createCockpitProjectIdFilter(Long tcDirectProjectId)</code> with
     * tcDirectProjectId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createCockpitProjectIdFilter_tcDirectProjectIdNegative() {
        Long value = -1L;

        LateDeliverableFilterBuilder.createCockpitProjectIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createCockpitProjectIdFilter(Long tcDirectProjectId)</code> with
     * tcDirectProjectId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createCockpitProjectIdFilter_tcDirectProjectIdZero() {
        Long value = 0L;

        LateDeliverableFilterBuilder.createCockpitProjectIdFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createHasExplanationFilter(boolean explanationPresent)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createHasExplanationFilter1() {
        boolean value = true;
        NotFilter filter = (NotFilter) LateDeliverableFilterBuilder.createHasExplanationFilter(value);

        NullFilter filterToNegate = (NullFilter) filter.getFilter();
        assertEquals("'createHasExplanationFilter' should be correct.", "explanation", filterToNegate.getName());
        assertNull("'createHasExplanationFilter' should be correct.", filterToNegate.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createHasExplanationFilter(boolean explanationPresent)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createHasExplanationFilter2() {
        boolean value = false;
        NullFilter filter = (NullFilter) LateDeliverableFilterBuilder.createHasExplanationFilter(value);

        assertEquals("'createHasExplanationFilter' should be correct.", "explanation", filter.getName());
        assertNull("'createHasExplanationFilter' should be correct.", filter.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createHasResponseFilter(boolean responsePresent)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createHasResponseFilter1() {
        boolean value = true;
        NotFilter filter = (NotFilter) LateDeliverableFilterBuilder.createHasResponseFilter(value);

        NullFilter filterToNegate = (NullFilter) filter.getFilter();
        assertEquals("'createHasResponseFilter' should be correct.", "response", filterToNegate.getName());
        assertNull("'createHasResponseFilter' should be correct.", filterToNegate.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createHasResponseFilter(boolean responsePresent)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createHasResponseFilter2() {
        boolean value = false;
        NullFilter filter = (NullFilter) LateDeliverableFilterBuilder.createHasResponseFilter(value);

        assertEquals("'createHasResponseFilter' should be correct.", "response", filter.getName());
        assertNull("'createHasResponseFilter' should be correct.", filter.getValue());
    }

    /**
     * <p>
     * Accuracy test for the method <code>createMinimumDeadlineFilter(Date minDeadline)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createMinimumDeadlineFilter() {
        Date value = new Date();
        GreaterThanOrEqualToFilter filter =
            (GreaterThanOrEqualToFilter) LateDeliverableFilterBuilder.createMinimumDeadlineFilter(value);

        assertEquals("'createMinimumDeadlineFilter' should be correct.", "deadline", filter.getName());
        assertEquals("'createMinimumDeadlineFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createMinimumDeadlineFilter(Date minDeadline)</code> with minDeadline is
     * null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createMinimumDeadlineFilter_minDeadlineNull() {
        Date value = null;

        LateDeliverableFilterBuilder.createMinimumDeadlineFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createMaximumDeadlineFilter(Date maxDeadline)</code>.<br>
     * The result should be correct.
     * </p>
     */
    @Test
    public void test_createMaximumDeadlineFilter() {
        Date value = new Date();
        LessThanOrEqualToFilter filter =
            (LessThanOrEqualToFilter) LateDeliverableFilterBuilder.createMaximumDeadlineFilter(value);

        assertEquals("'createMaximumDeadlineFilter' should be correct.", "deadline", filter.getName());
        assertEquals("'createMaximumDeadlineFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createMaximumDeadlineFilter(Date maxDeadline)</code> with maxDeadline is
     * null.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createMaximumDeadlineFilter_maxDeadlineNull() {
        Date value = null;

        LateDeliverableFilterBuilder.createMaximumDeadlineFilter(value);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createLateDeliverableTypeIdFilter(long lateDeliverableTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @since 1.0.6
     */
    @Test
    public void test_createLateDeliverableTypeIdFilter() {
        long value = 1;
        EqualToFilter filter = (EqualToFilter) LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(value);

        assertEquals("'createLateDeliverableTypeIdFilter' should be correct.",
            "lateDeliverableTypeId", filter.getName());
        assertEquals("'createLateDeliverableTypeIdFilter' should be correct.", value, filter.getValue());
    }

    /**
     * <p>
     * Failure test for the method <code>createLateDeliverableTypeIdFilter(long lateDeliverableTypeId)</code> with
     * lateDeliverableTypeId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @since 1.0.6
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createLateDeliverableTypeIdFilter_lateDeliverableTypeIdNegative() {
        long value = -1;

        LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(value);
    }

    /**
     * <p>
     * Failure test for the method <code>createLateDeliverableTypeIdFilter(long lateDeliverableTypeId)</code> with
     * lateDeliverableTypeId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @since 1.0.6
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createLateDeliverableTypeIdFilter_lateDeliverableTypeIdZero() {
        long value = 0;

        LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(value);
    }
}
