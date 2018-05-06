/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.search;

import com.topcoder.management.deliverable.DeliverableHelper;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p> The SubmissionFilterBuilder class supports building filters for searching for Submissions. </p>
 *
 * <p>Changes in 1.1: Added createSubmissionTypeIdFilter method.</p>
 *
 * <p><strong>Thread Safety:</strong> This class has only final static fields/methods, so mutability is not an issue.
 * </p>
 *
 * @author aubergineanode, saarixx, singlewood, TCSDEVELOPER
 * @version 1.1
 */
public class SubmissionFilterBuilder {

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * Submission. </p>
     */
    private static final String SUBMISSION_ID_FIELD_NAME = "submission_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * Upload that is associated with the Submission. </p>
     */
    private static final String UPLOAD_ID_FIELD_NAME = "upload_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the project the
     * submission is associated with. </p>
     */
    private static final String PROJECT_ID_FIELD_NAME = "project_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * resource the Submission is associated with. </p>
     */
    private static final String RESOURCE_ID_FIELD_NAME = "resource_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the submission
     * status of the Submissions. </p>
     */
    private static final String SUBMISSION_STATUS_ID_FIELD_NAME = "submission_status_id";

    /**
     * <p>The name of the field under which SearchBuilder Filters can be built in order to filter on the submission type
     * of the Submissions.</p>
     *
     * <p>Added in version 1.1.</p>
     */
    private static final String SUBMISSION_TYPE_ID_FIELD_NAME = "submission_type_id";

    /**
     * <p>Private constructor to prevent instantiation.</p>
     */
    private SubmissionFilterBuilder() {
        // no operation.
    }

    /**
     * <p>Creates a filter that selects the submission with the given id.</p>
     *
     * @param submissionId The submission id to filter on
     *
     * @return A filter for selecting submissions with the given id
     *
     * @throws IllegalArgumentException If submissionId is <= 0
     */
    public static Filter createSubmissionIdFilter(long submissionId) {
        DeliverableHelper.checkGreaterThanZero(submissionId, "submissionId");
        return SearchBundle.buildEqualToFilter(SUBMISSION_ID_FIELD_NAME, new Long(submissionId));
    }

    /**
     * <p>Creates a filter that selects submissions related to the given upload.</p>
     *
     * @param uploadId The upload id to filter on
     *
     * @return A filter for selecting submissions related to the given upload
     *
     * @throws IllegalArgumentException If uploadId is <= 0
     */
    public static Filter createUploadIdFilter(long uploadId) {
        DeliverableHelper.checkGreaterThanZero(uploadId, "uploadId");
        return SearchBundle.buildEqualToFilter(UPLOAD_ID_FIELD_NAME, new Long(uploadId));
    }

    /**
     * <p>Creates a filter that selects submissions related to the project with the given id.</p>
     *
     * @param projectId The project id to filter on
     *
     * @return A filter for selecting submissions related to the given project
     *
     * @throws IllegalArgumentException If projectId is <= 0
     */
    public static Filter createProjectIdFilter(long projectId) {
        DeliverableHelper.checkGreaterThanZero(projectId, "projectId");
        return SearchBundle.buildEqualToFilter(PROJECT_ID_FIELD_NAME, new Long(projectId));
    }

    /**
     * <p>Creates a filter that selects submissions related to the given resource id.</p>
     *
     * @param resourceId The resource id to filter on
     *
     * @return A filter for selecting submissions related to the given resource
     *
     * @throws IllegalArgumentException If resourceId is <= 0
     */
    public static Filter createResourceIdFilter(long resourceId) {
        DeliverableHelper.checkGreaterThanZero(resourceId, "resourceId");
        return SearchBundle.buildEqualToFilter(RESOURCE_ID_FIELD_NAME, new Long(resourceId));
    }

    /**
     * <p>Creates a filter that selects submissions related having the given submission status id.</p>
     *
     * @param submissionStatusId The submission status id to filter on
     *
     * @return A filter for selecting submissions with the given status
     *
     * @throws IllegalArgumentException If submissionStatusId is <= 0
     */
    public static Filter createSubmissionStatusIdFilter(long submissionStatusId) {
        DeliverableHelper.checkGreaterThanZero(submissionStatusId, "submissionStatusId");
        return SearchBundle.buildEqualToFilter(SUBMISSION_STATUS_ID_FIELD_NAME, new Long(submissionStatusId));
    }

    /**
     * <p>Creates a filter that selects submissions related having the given submission type id.</p>
     *
     * <p>Added in version 1.1.</p>
     *
     * @param submissionTypeId The submission type id to filter on
     *
     * @return A filter for selecting submissions with the given type
     *
     * @throws IllegalArgumentException If submissionTypeId is <= 0
     * @since 1.1
     */
    public static Filter createSubmissionTypeIdFilter(long submissionTypeId) {
        DeliverableHelper.checkGreaterThanZero(submissionTypeId, "submissionTypeId");
        return SearchBundle.buildEqualToFilter(SUBMISSION_TYPE_ID_FIELD_NAME, new Long(submissionTypeId));
    }
}
