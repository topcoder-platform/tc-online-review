/*
 * Copyright 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.management.deliverable.*;
import com.topcoder.management.deliverable.persistence.UploadPersistenceException;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.review.ReviewManager;
import com.topcoder.management.review.ReviewManagementException;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.ResourceManager;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.persistence.ResourcePersistenceException;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.PhaseStatus;

/**
 * <p>
 * A helper class used for retrieving various lookup entities.
 * </p>
 * @author VolodymyrK
 * @version 1.7.4
 */
final class LookupHelper {

    /**
     * Utility method to create a UploadStatus instance with given name.
     * @param uploadManager
     *            UploadManager instance used to search for upload status.
     * @param statusName
     *            upload status name.
     * @return UploadStatus instance.
     * @throws com.topcoder.management.phase.PhaseHandlingException
     *             if upload status could not be found.
     */
    static UploadStatus getUploadStatus(UploadManager uploadManager, String statusName) throws PhaseHandlingException {
        UploadStatus[] statuses;

        try {
            statuses = uploadManager.getAllUploadStatuses();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding upload status with name: " + statusName, e);
        }

        for (UploadStatus status : statuses) {
            if (statusName.equals(status.getName())) {
                return status;
            }
        }

        throw new PhaseHandlingException("Could not find upload status with name: " + statusName);
    }

    /**
     * Utility method to get an UploadType object for the given type name.
     * @param uploadManager
     *            UploadManager instance used to search for upload type.
     * @param typeName
     *            upload type name.
     * @return a UploadType object for the given type name.
     * @throws PhaseHandlingException
     *             if upload type could not be found.
     */
    static UploadType getUploadType(UploadManager uploadManager, String typeName) throws PhaseHandlingException {
        UploadType[] types;

        try {
            types = uploadManager.getAllUploadTypes();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding upload type with name: " + typeName, e);
        }

        for (UploadType type : types) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }

        throw new PhaseHandlingException("Could not find upload type with name: " + typeName);
    }

    /**
     * Utility method to get a SubmissionStatus object for the given status name.
     * @param uploadManager
     *            UploadManager instance used to search for submission status.
     * @param statusName
     *            submission status name.
     * @return a SubmissionStatus object for the given status name.
     * @throws PhaseHandlingException
     *             if submission status could not be found.
     */
    static SubmissionStatus getSubmissionStatus(UploadManager uploadManager, String statusName)
        throws PhaseHandlingException {
        SubmissionStatus[] statuses;

        try {
            statuses = uploadManager.getAllSubmissionStatuses();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding submission status with name: " + statusName, e);
        }

        for (SubmissionStatus status : statuses) {
            if (statusName.equals(status.getName())) {
                return status;
            }
        }

        throw new PhaseHandlingException("Could not find submission status with name: " + statusName);
    }

    /**
     * Utility method to get a SubmissionType object for the given type name.
     * @param uploadManager
     *            UploadManager instance used to search for submission type.
     * @param typeName
     *            submission type name.
     * @return a SubmissionType object for the given type name.
     * @throws PhaseHandlingException
     *             if submission type could not be found.
     */
    static SubmissionType getSubmissionType(UploadManager uploadManager, String typeName)
        throws PhaseHandlingException {
        SubmissionType[] types;

        try {
            types = uploadManager.getAllSubmissionTypes();
        } catch (UploadPersistenceException e) {
            throw new PhaseHandlingException("Error finding submission type with name: " + typeName, e);
        }

        for (SubmissionType type : types) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }

        throw new PhaseHandlingException("Could not find submission type with name: " + typeName);
    }

    /**
     * Utility method to create a PhaseType instance with given name.
     * @param phaseManager
     *            PhaseManager instance used to search for phase type.
     * @param typeName
     *            phase type name.
     * @return PhaseType instance.
     * @throws PhaseHandlingException
     *             if phase type could not be found.
     */
    static PhaseType getPhaseType(PhaseManager phaseManager, String typeName)
        throws PhaseHandlingException {
        PhaseType[] types;

        try {
            types = phaseManager.getAllPhaseTypes();
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Error finding phase type with name: " + typeName, e);
        }

        for (PhaseType type : types) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }

        throw new PhaseHandlingException("Could not find phase type with name: " + typeName);
    }

    /**
     * Utility method to create a PhaseStatus instance with given name.
     * @param phaseManager
     *            PhaseManager instance used to search for phase status.
     * @param statusName
     *            phase status name.
     * @return PhaseStatus instance.
     * @throws PhaseHandlingException
     *             if phase status could not be found.
     */
    static PhaseStatus getPhaseStatus(PhaseManager phaseManager, String statusName)
        throws PhaseHandlingException {
        PhaseStatus[] statuses;

        try {
            statuses = phaseManager.getAllPhaseStatuses();
        } catch (PhaseManagementException e) {
            throw new PhaseHandlingException("Error finding phase status with name: " + statusName, e);
        }

        for (PhaseStatus status : statuses) {
            if (statusName.equals(status.getName())) {
                return status;
            }
        }

        throw new PhaseHandlingException("Could not find phase status with name: " + statusName);
    }

    /**
     * Utility method to create a CommentType instance with given name.
     * @param reviewManager
     *            ReviewManager instance used to search for comment type.
     * @param typeName
     *            comment type name.
     * @return CommentType instance.
     * @throws PhaseHandlingException
     *             if comment type could not be found.
     */
    static CommentType getCommentType(ReviewManager reviewManager, String typeName)
        throws PhaseHandlingException {
        CommentType[] types;

        try {
            types = reviewManager.getAllCommentTypes();
        } catch (ReviewManagementException e) {
            throw new PhaseHandlingException("Error finding comment type with name: " + typeName, e);
        }

        for (CommentType type : types) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }

        throw new PhaseHandlingException("Could not find comment type with name: " + typeName);
    }

    /**
     * Utility method to create a NotificationType instance with given name.
     * @param resourceManager
     *            ResourceManager instance used to search for notification type.
     * @param typeName
     *            comment type name.
     * @return NotificationType instance.
     * @throws PhaseHandlingException
     *             if notification type could not be found.
     */
    static NotificationType getNotificationType(ResourceManager resourceManager, String typeName)
        throws PhaseHandlingException {
        NotificationType[] types;

        try {
            types = resourceManager.getAllNotificationTypes();
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Error finding notification type with name: " + typeName, e);
        }

        for (NotificationType type : types) {
            if (typeName.equals(type.getName())) {
                return type;
            }
        }

        throw new PhaseHandlingException("Could not find notification type with name: " + typeName);
    }

    /**
     * Utility method to create a ResourceRole instance with given name.
     * @param resourceManager
     *            ResourceManager instance used to search for resource role.
     * @param roleName
     *            resource role name.
     * @return ResourceRole instance.
     * @throws PhaseHandlingException
     *             if resource role could not be found.
     */
    static ResourceRole getResourceRole(ResourceManager resourceManager, String roleName)
        throws PhaseHandlingException {
        ResourceRole[] roles;

        try {
            roles = resourceManager.getAllResourceRoles();
        } catch (ResourcePersistenceException e) {
            throw new PhaseHandlingException("Error finding resource role with name: " + roleName, e);
        }

        for (ResourceRole role : roles) {
            if (roleName.equals(role.getName())) {
                return role;
            }
        }

        throw new PhaseHandlingException("Could not find resource role with name: " + roleName);
    }

}
