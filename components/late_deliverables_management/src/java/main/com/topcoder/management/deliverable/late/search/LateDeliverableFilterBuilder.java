/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.search;

import java.util.Date;

import com.topcoder.management.deliverable.late.Helper;
import com.topcoder.search.builder.filter.EqualToFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.GreaterThanOrEqualToFilter;
import com.topcoder.search.builder.filter.LessThanOrEqualToFilter;
import com.topcoder.search.builder.filter.NotFilter;
import com.topcoder.search.builder.filter.NullFilter;

/**
 * <p>
 * This is a static helper class that provides factory methods for creating filters that can be used when searching
 * for late deliverables using LateDeliverableManagerImpl and possibly other implementations of
 * LateDeliverableManager.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added createLateDeliverableTypeIdFilter() method.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> This class is immutable and thread safe.
 * </p>
 *
 * @author saarixx, sparemax
 * @version 1.0.6
 */
public final class LateDeliverableFilterBuilder {
    /**
     * <p>
     * Empty private constructor.
     * </p>
     */
    private LateDeliverableFilterBuilder() {
        // Empty
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables for the project with the given ID.
     * </p>
     *
     * @param projectId
     *            the ID of the project.
     *
     * @return the filter that selects late deliverables for the project with the given ID (not null).
     *
     * @throws IllegalArgumentException
     *             if projectId &lt;= 0.
     */
    public static Filter createProjectIdFilter(long projectId) {
        Helper.checkPositive(projectId, "projectId");

        return new EqualToFilter("projectId", projectId);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables for all projects with the specified status.
     * </p>
     *
     * @param projectStatusId
     *            the ID of the project status.
     *
     * @return the filter that selects late deliverables for all projects with the specified status (not null).
     *
     * @throws IllegalArgumentException
     *             if projectStatusId &lt;= 0.
     */
    public static Filter createProjectStatusIdFilter(long projectStatusId) {
        Helper.checkPositive(projectStatusId, "projectStatusId");

        return new EqualToFilter("projectStatusId", projectStatusId);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables for all projects from the specified category.
     * </p>
     *
     * @param projectCategoryId
     *            the ID of the project category.
     *
     * @return the filter that selects late deliverables for all projects from the specified category (not null).
     *
     * @throws IllegalArgumentException
     *             if projectCategoryId &lt;= 0.
     */
    public static Filter createProjectCategoryIdFilter(long projectCategoryId) {
        Helper.checkPositive(projectCategoryId, "projectCategoryId");

        return new EqualToFilter("projectCategoryId", projectCategoryId);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables for specific deliverable types.
     * </p>
     *
     * @param deliverableId
     *            the ID of the deliverable type.
     *
     * @return the filter that selects late deliverables for specific deliverable types (not null).
     *
     * @throws IllegalArgumentException
     *             if deliverableId &lt;= 0.
     */
    public static Filter createDeliverableIdFilter(long deliverableId) {
        Helper.checkPositive(deliverableId, "deliverableId");

        return new EqualToFilter("deliverableId", deliverableId);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables with forgiven flag equal to the specified value.
     * </p>
     *
     * @param forgiven
     *            the value of the forgiven flag of late deliverables to be selected.
     *
     * @return the filter that selects late deliverables with forgiven flag equal to the specified value (not null).
     */
    public static Filter createForgivenFilter(boolean forgiven) {
        return new EqualToFilter("forgiven", forgiven ? 1 : 0);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables assigned to the user with the specified handle.
     * </p>
     *
     * @param userHandle
     *            the handle of the user.
     *
     * @return the filter that selects late deliverables assigned to the user with the specified handle (not null).
     *
     * @throws IllegalArgumentException
     *             if userHandle is null/empty.
     */
    public static Filter createUserHandleFilter(String userHandle) {
        Helper.checkNull(userHandle, "userHandle");
        if (userHandle.trim().length() == 0) {
            throw new IllegalArgumentException("'userHandle' should not be an empty string.");
        }

        return new EqualToFilter("userHandle", userHandle);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables for the TC Direct project with the given ID.
     * </p>
     *
     * @param tcDirectProjectId
     *            the ID of the cockpit project (null if filter should match deliverables associated with no TC Direct
     *            project)
     *
     * @return the filter that selects late deliverables for the TC Direct project with the given ID.
     *
     * @throws IllegalArgumentException
     *             if tcDirectProjectId != null and tcDirectProjectId &lt;= 0.
     */
    public static Filter createCockpitProjectIdFilter(Long tcDirectProjectId) {
        if (tcDirectProjectId != null) {
            Helper.checkPositive(tcDirectProjectId, "tcDirectProjectId");

            return new EqualToFilter("tcDirectProjectId", tcDirectProjectId);
        }

        return new NullFilter("tcDirectProjectId");
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables that have or have not explanation specified.
     * </p>
     *
     * @param explanationPresent
     *            true if filter should match late deliverables with explanation specified; false if filter should
     *            match late deliverables without explanation specified.
     *
     * @return the filter that selects late deliverables that have or have not explanation specified.
     */
    public static Filter createHasExplanationFilter(boolean explanationPresent) {
        if (explanationPresent) {
            return new NotFilter(new NullFilter("explanation"));
        }
        return new NullFilter("explanation");
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables that have or have not response specified.
     * </p>
     *
     * @param responsePresent
     *            true if filter should match late deliverables with response specified, false if filter should match
     *            late deliverables without response specified.
     *
     * @return the filter that selects late deliverables that have or have not response specified.
     */
    public static Filter createHasResponseFilter(boolean responsePresent) {
        if (responsePresent) {
            return new NotFilter(new NullFilter("response"));
        }
        return new NullFilter("response");
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables with deadline equal to or greater than the specified value.
     * </p>
     *
     * @param minDeadline
     *            the minimum deadline of late deliverables to be selected.
     *
     * @return the filter that selects late deliverables with deadline equal to or greater than the specified value.
     *
     * @throws IllegalArgumentException
     *             if minDeadline is null.
     */
    public static Filter createMinimumDeadlineFilter(Date minDeadline) {
        Helper.checkNull(minDeadline, "minDeadline");

        return new GreaterThanOrEqualToFilter("deadline", minDeadline);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables with deadline equal to or less than the specified value.
     * </p>
     *
     * @param maxDeadline
     *            the maximum deadline of late deliverables to be selected.
     *
     * @return the filter that selects late deliverables with deadline equal to or less than the specified value.
     *
     * @throws IllegalArgumentException
     *             if maxDeadline is null.
     */
    public static Filter createMaximumDeadlineFilter(Date maxDeadline) {
        Helper.checkNull(maxDeadline, "maxDeadline");

        return new LessThanOrEqualToFilter("deadline", maxDeadline);
    }

    /**
     * <p>
     * Creates a filter that selects late deliverables of type with the given ID.
     * </p>
     *
     * @param lateDeliverableTypeId
     *            the ID of the late deliverable type.
     *
     * @return the filter that selects late deliverables of type with the given ID (not null).
     *
     * @throws IllegalArgumentException
     *             if lateDeliverableTypeId &lt;= 0.
     *
     * @since 1.0.6
     */
    public static Filter createLateDeliverableTypeIdFilter(long lateDeliverableTypeId) {
        Helper.checkPositive(lateDeliverableTypeId, "lateDeliverableTypeId");

        return new EqualToFilter("lateDeliverableTypeId", lateDeliverableTypeId);
    }
}
