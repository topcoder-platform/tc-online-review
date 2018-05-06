/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.search;

import com.topcoder.management.deliverable.DeliverableHelper;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p> The UploadFilterBuilder class supports building filters for searching for Uploads. </p>
 *
 * <p><strong>Thread Safety:</strong> This class has only final static fields/methods, so mutability is not an
 * issue.</p>
 *
 * @author aubergineanode, singlewood
 * @version 1.1
 */
public class UploadFilterBuilder {

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the id of the
     * Upload. </p>
     */
    private static final String UPLOAD_ID_FIELD_NAME = "upload_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the type id of the
     * Upload. </p>
     */
    private static final String UPLOAD_TYPE_ID_FIELD_NAME = "upload_type_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the status id of
     * the Upload. </p>
     */
    private static final String UPLOAD_STATUS_ID_FIELD_NAME = "upload_status_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the project the
     * Upload is associated with. </p>
     */
    private static final String PROJECT_ID_FIELD_NAME = "project_id";

    /**
     * <p> The name of the field under which SearchBuilder Filters can be built in order to filter on the resource the
     * Upload is associated with. </p>
     */
    private static final String RESOURCE_ID_FIELD_NAME = "resource_id";

    /**
     * <p>Private constructor to prevent instantiation.</p>
     */
    private UploadFilterBuilder() {
        // no operation
    }

    /**
     * <p>Creates a filter that selects uploads with the given id.</p>
     *
     * @param uploadId The upload id to filter on
     *
     * @return A filter for selecting uploads with the given id.
     *
     * @throws IllegalArgumentException If uploadId is <= 0
     */
    public static Filter createUploadIdFilter(long uploadId) {
        DeliverableHelper.checkGreaterThanZero(uploadId, "uploadId");
        return SearchBundle.buildEqualToFilter(UPLOAD_ID_FIELD_NAME, new Long(uploadId));
    }

    /**
     * <p>Creates a filter that selects uploads with the given type id.</p>
     *
     * @param uploadTypeId The upload type id to filter on
     *
     * @return A filter for selecting uploads with the given type id.
     *
     * @throws IllegalArgumentException If uploadTypeId is <= 0
     */
    public static Filter createUploadTypeIdFilter(long uploadTypeId) {
        DeliverableHelper.checkGreaterThanZero(uploadTypeId, "uploadTypeId");
        return SearchBundle.buildEqualToFilter(UPLOAD_TYPE_ID_FIELD_NAME, new Long(uploadTypeId));
    }

    /**
     * <p>Creates a filter that selects uploads with the given status id.</p>s
     *
     * @param uploadStatusId The upload id to filter on
     *
     * @return A filter for selecting uploads with the given status id.
     *
     * @throws IllegalArgumentException If uploadStatusId is <= 0
     */
    public static Filter createUploadStatusIdFilter(long uploadStatusId) {
        DeliverableHelper.checkGreaterThanZero(uploadStatusId, "uploadStatusId");
        return SearchBundle.buildEqualToFilter(UPLOAD_STATUS_ID_FIELD_NAME, new Long(uploadStatusId));
    }

    /**
     * <p>Creates a filter that selects uploads related to the project with the given id.</p>
     *
     * @param projectId The project id to filter on
     *
     * @return A filter for selecting uploads related to the given project
     *
     * @throws IllegalArgumentException If projectId is <= 0
     */
    public static Filter createProjectIdFilter(long projectId) {
        DeliverableHelper.checkGreaterThanZero(projectId, "projectId");
        return SearchBundle.buildEqualToFilter(PROJECT_ID_FIELD_NAME, new Long(projectId));
    }

    /**
     * <p>Creates a filter that selects uploads related to the given resource id.</p>
     *
     * @param resourceId The resource id to filter on
     *
     * @return A filter for selecting uploads related to the given resource
     *
     * @throws IllegalArgumentException If resourceId is <= 0
     */
    public static Filter createResourceIdFilter(long resourceId) {
        DeliverableHelper.checkGreaterThanZero(resourceId, "resourceId");
        return SearchBundle.buildEqualToFilter(RESOURCE_ID_FIELD_NAME, new Long(resourceId));
    }
}
