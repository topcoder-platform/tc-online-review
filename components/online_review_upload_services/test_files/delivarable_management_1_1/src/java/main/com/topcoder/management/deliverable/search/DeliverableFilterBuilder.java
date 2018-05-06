/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.search;

import com.topcoder.management.deliverable.DeliverableHelper;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p> The DeliverableFilterBuilder class supports building filters for searching for Deliverables. </p>
 *
 * <p><strong>Thread Safety:</strong> This class has only final static fields/methods, so mutability is not an
 * issue.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class DeliverableFilterBuilder {

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * Deliverable. </p>
     */
    private static final String DELIVERABLE_ID_FIELD_NAME = "deliverable_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the name of the
     * Deliverable. </p>
     */
    private static final String NAME_FIELD_NAME = "name";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * project of the Deliverable. </p>
     */
    private static final String PROJECT_ID_FIELD_NAME = "project_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the phase the
     * Deliverable falls in. </p>
     */
    private static final String PHASE_ID_FIELD_NAME = "phase_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the submission the
     * Deliverable is associated with. </p>
     */
    private static final String SUBMISSION_ID_FIELD_NAME = "submission_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the resource id of
     * the Deliverable. </p>
     */
    private static final String RESOURCE_ID_FIELD_NAME = "resource_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on whether the
     * Deliverable is required or not. </p>
     */
    private static final String REQUIRED_FIELD_NAME = "required";

    /**
     * <p>Private constructor to prevent instantiation.</p>
     */
    private DeliverableFilterBuilder() {
        // no operation.
    }

    /**
     * <p>Creates a filter that selects deliverables with the given id.</p>
     *
     * @param deliverableId The id of the deliverable
     *
     * @return A filter for selecting deliverables by id
     *
     * @throws IllegalArgumentException If deliverableId is <= 0
     */
    public static Filter createDeliverableIdFilter(long deliverableId) {
        DeliverableHelper.checkGreaterThanZero(deliverableId, "deliverableId");
        return SearchBundle.buildEqualToFilter(DELIVERABLE_ID_FIELD_NAME, new Long(deliverableId));
    }

    /**
     * <p>Creates a filter that selects deliverables that fall have the given name.</p>
     *
     * @param name The name to filter on
     *
     * @return A filter for selecting deliverables with the given name
     *
     * @throws IllegalArgumentException If name is null
     */
    public static Filter createNameFilter(String name) {
        DeliverableHelper.checkObjectNotNull(name, "name");
        return SearchBundle.buildEqualToFilter(NAME_FIELD_NAME, name);
    }

    /**
     * <p>Creates a filter that selects deliverables related to the project with the given id.</p>
     *
     * @param projectId The project id to filter on
     *
     * @return A filter for selecting deliverables related to the given project
     *
     * @throws IllegalArgumentException If projectId is <= 0
     */
    public static Filter createProjectIdFilter(long projectId) {
        DeliverableHelper.checkGreaterThanZero(projectId, "projectId");
        return SearchBundle.buildEqualToFilter(PROJECT_ID_FIELD_NAME, new Long(projectId));
    }

    /**
     * <p>Creates a filter that selects deliverables that fall in the phase with the given id.</p>
     *
     * @param phaseId The phase id to filter on
     *
     * @return A filter for selecting deliverables for the given phase
     *
     * @throws IllegalArgumentException If phaseId is <= 0
     */
    public static Filter createPhaseIdFilter(long phaseId) {
        DeliverableHelper.checkGreaterThanZero(phaseId, "phaseId");
        return SearchBundle.buildEqualToFilter(PHASE_ID_FIELD_NAME, new Long(phaseId));
    }

    /**
     * <p>Creates a filter that selects deliverables that are related to the given submission.</p>
     *
     * @param submissionId The submission id to filter on
     *
     * @return A filter for selecting deliverables for the given submission
     *
     * @throws IllegalArgumentException If submissionId is <= 0
     */
    public static Filter createSubmissionIdFilter(long submissionId) {
        DeliverableHelper.checkGreaterThanZero(submissionId, "submissionId");
        return SearchBundle.buildEqualToFilter(SUBMISSION_ID_FIELD_NAME, new Long(submissionId));
    }

    /**
     * <p>Creates a filter that selects deliverables that are related to the given resource.</p>
     *
     * @param resourceId The resource id to filter on
     *
     * @return A filter for selecting deliverables for the given resource
     *
     * @throws IllegalArgumentException If resourceId is <= 0
     */
    public static Filter createResourceIdFilter(long resourceId) {
        DeliverableHelper.checkGreaterThanZero(resourceId, "resourceId");
        return SearchBundle.buildEqualToFilter(RESOURCE_ID_FIELD_NAME, new Long(resourceId));
    }

    /**
     * <p>Creates a filter that selects deliverables that fall on whether or not the deliverable is required.</p>
     *
     * @param isRequired Whether or not deliverable is required
     *
     * @return A filter for selecting deliverables that are (or are not) required
     */
    public static Filter createRequiredFilter(boolean isRequired) {
        // Build the filter according to the fact that SQL Boolean false = 0 and SQL Boolean true != 0
        return (isRequired)
                ? SearchBundle.buildNotFilter(SearchBundle.buildEqualToFilter(REQUIRED_FIELD_NAME, new Integer(0)))
                : SearchBundle.buildEqualToFilter(REQUIRED_FIELD_NAME, new Integer(0));
    }

    /**
     * <p>Creates a filter that selects deliverables that are related to the given id and resource.</p>
     *
     * @param deliverableId The id of the deliverable
     * @param resourceId    The resource id to filter on
     *
     * @return A filter for selecting deliverables for the given id and resource
     *
     * @throws IllegalArgumentException If resourceId or deliverableId is <= 0
     */
    public static Filter createDeliverableIdResourceIdFilter(long deliverableId, long resourceId) {
        DeliverableHelper.checkGreaterThanZero(deliverableId, "deliverableId");
        DeliverableHelper.checkGreaterThanZero(resourceId, "resourceId");
        return SearchBundle.buildAndFilter(createDeliverableIdFilter(deliverableId),
                createResourceIdFilter(resourceId));
    }
}
