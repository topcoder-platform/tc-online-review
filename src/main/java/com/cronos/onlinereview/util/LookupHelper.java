/*
 * Copyright (C) 2012 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.topcoder.onlinereview.component.deliverable.SubmissionStatus;
import com.topcoder.onlinereview.component.deliverable.SubmissionType;
import com.topcoder.onlinereview.component.deliverable.UploadPersistenceException;
import com.topcoder.onlinereview.component.deliverable.UploadStatus;
import com.topcoder.onlinereview.component.deliverable.UploadType;
import com.topcoder.onlinereview.component.project.management.PrizeType;
import com.topcoder.onlinereview.component.project.management.ProjectCategory;
import com.topcoder.onlinereview.component.project.management.ProjectStatus;
import com.topcoder.onlinereview.component.project.phase.PhaseManagementException;
import com.topcoder.onlinereview.component.project.phase.PhaseStatus;
import com.topcoder.onlinereview.component.project.phase.PhaseType;
import com.topcoder.onlinereview.component.resource.NotificationType;
import com.topcoder.onlinereview.component.resource.ResourcePersistenceException;
import com.topcoder.onlinereview.component.resource.ResourceRole;
import com.topcoder.onlinereview.component.review.CommentType;
import com.topcoder.onlinereview.component.review.ReviewManagementException;
import com.topcoder.onlinereview.component.scorecard.PersistenceException;
import com.topcoder.onlinereview.component.scorecard.ScorecardType;

/**
 * <p>
 * This class contains handy lookup-methods for various entities.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 **/
public class LookupHelper {

    /**
     * Utility method to create a ScorecardType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return ScorecardType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static ScorecardType getScorecardType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        ScorecardType[] entities;
        try {
            entities = ActionsHelper.createScorecardManager().getAllScorecardTypes();
        } catch (PersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (ScorecardType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a ScorecardType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return ScorecardType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static ScorecardType getScorecardType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        ScorecardType[] entities;
        try {
            entities = ActionsHelper.createScorecardManager().getAllScorecardTypes();
        } catch (PersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (ScorecardType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a CommentType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return CommentType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static CommentType getCommentType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        CommentType[] entities;
        try {
            entities = ActionsHelper.createReviewManager().getAllCommentTypes();
        } catch (ReviewManagementException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (CommentType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a CommentType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return CommentType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static CommentType getCommentType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        CommentType[] entities;
        try {
            entities = ActionsHelper.createReviewManager().getAllCommentTypes();
        } catch (ReviewManagementException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (CommentType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a ProjectCategory instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return ProjectCategory instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static ProjectCategory getProjectCategory(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        ProjectCategory[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getAllProjectCategories();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (ProjectCategory entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a ProjectCategory instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return ProjectCategory instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static ProjectCategory getProjectCategory(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        ProjectCategory[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getAllProjectCategories();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (ProjectCategory entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a ProjectStatus instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return ProjectStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static ProjectStatus getProjectStatus(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        ProjectStatus[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getAllProjectStatuses();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (ProjectStatus entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a ProjectStatus instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return ProjectStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static ProjectStatus getProjectStatus(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        ProjectStatus[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getAllProjectStatuses();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (ProjectStatus entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a ResourceRole instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return ResourceRole instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static ResourceRole getResourceRole(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        ResourceRole[] entities;
        try {
            entities = ActionsHelper.createResourceManager().getAllResourceRoles();
        } catch (ResourcePersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (ResourceRole entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a ResourceRole instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return ResourceRole instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static ResourceRole getResourceRole(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        ResourceRole[] entities;
        try {
            entities = ActionsHelper.createResourceManager().getAllResourceRoles();
        } catch (ResourcePersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (ResourceRole entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a PhaseType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return PhaseType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static PhaseType getPhaseType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        PhaseType[] entities;
        try {
            entities = ActionsHelper.createPhaseManager(false).getAllPhaseTypes();
        } catch (PhaseManagementException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (PhaseType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a PhaseType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return PhaseType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static PhaseType getPhaseType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        PhaseType[] entities;
        try {
            entities = ActionsHelper.createPhaseManager(false).getAllPhaseTypes();
        } catch (PhaseManagementException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (PhaseType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a PhaseStatus instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return PhaseStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static PhaseStatus getPhaseStatus(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        PhaseStatus[] entities;
        try {
            entities = ActionsHelper.createPhaseManager(false).getAllPhaseStatuses();
        } catch (PhaseManagementException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (PhaseStatus entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a PhaseStatus instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return PhaseStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static PhaseStatus getPhaseStatus(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        PhaseStatus[] entities;
        try {
            entities = ActionsHelper.createPhaseManager(false).getAllPhaseStatuses();
        } catch (PhaseManagementException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (PhaseStatus entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a SubmissionStatus instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return SubmissionStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static SubmissionStatus getSubmissionStatus(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        SubmissionStatus[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllSubmissionStatuses();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (SubmissionStatus entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a SubmissionStatus instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return SubmissionStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static SubmissionStatus getSubmissionStatus(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        SubmissionStatus[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllSubmissionStatuses();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (SubmissionStatus entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a SubmissionType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return getSubmissionType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static SubmissionType getSubmissionType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        SubmissionType[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllSubmissionTypes();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (SubmissionType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a SubmissionType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return SubmissionType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static SubmissionType getSubmissionType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        SubmissionType[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllSubmissionTypes();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (SubmissionType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a UploadStatus instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return UploadStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static UploadStatus getUploadStatus(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        UploadStatus[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllUploadStatuses();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (UploadStatus entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a UploadStatus instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return UploadStatus instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static UploadStatus getUploadStatus(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        UploadStatus[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllUploadStatuses();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (UploadStatus entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a UploadType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return UploadType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static UploadType getUploadType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        UploadType[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllUploadTypes();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (UploadType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a UploadType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return UploadType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static UploadType getUploadType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        UploadType[] entities;
        try {
            entities = ActionsHelper.createUploadManager().getAllUploadTypes();
        } catch (UploadPersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (UploadType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a PrizeType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return PrizeType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static PrizeType getPrizeType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        PrizeType[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getPrizeTypes();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (PrizeType entity : entities) {
            if (entityName.equals(entity.getDescription())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a PrizeType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return PrizeType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static PrizeType getPrizeType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        PrizeType[] entities;
        try {
            entities = ActionsHelper.createProjectManager().getPrizeTypes();
        } catch (Exception e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (PrizeType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }

    /**
     * Utility method to create a NotificationType instance with given name.
     * @param entityName
     *            Name of the entity to search for.
     * @return NotificationType instance.
     * @throws IllegalArgumentException
     *             if the parameter is null or empty string.
     * @throws LookupException
     *             if the entity with the given name could not be found.
     */
    public static NotificationType getNotificationType(String entityName) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterStringNotEmpty(entityName, "typeName");

        NotificationType[] entities;
        try {
            entities = ActionsHelper.createResourceManager().getAllNotificationTypes();
        } catch (ResourcePersistenceException e) {
            throw new LookupException("Error finding entity with name: " + entityName, e);
        }

        for (NotificationType entity : entities) {
            if (entityName.equals(entity.getName())) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with name: " + entityName);
    }

    /**
     * Utility method to create a NotificationType instance with given ID.
     * @param entityID
     *            ID of the entity to search for.
     * @return NotificationType instance.
     * @throws IllegalArgumentException
     *             if the parameter is zero or negative.
     * @throws LookupException
     *             if the entity with the given ID could not be found.
     */
    public static NotificationType getNotificationType(long entityID) throws LookupException {
        // Validate parameters
        ActionsHelper.validateParameterPositive(entityID, "typeId");

        NotificationType[] entities;
        try {
            entities = ActionsHelper.createResourceManager().getAllNotificationTypes();
        } catch (ResourcePersistenceException e) {
            throw new LookupException("Error finding entity with ID: " + entityID, e);
        }

        for (NotificationType entity : entities) {
            if (entityID == entity.getId()) {
                return entity;
            }
        }
        throw new LookupException("Could not find entity with ID: " + entityID);
    }
}
