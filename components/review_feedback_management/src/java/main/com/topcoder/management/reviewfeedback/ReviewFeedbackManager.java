/*
 * Copyright (C) 2012, 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.reviewfeedback;

import java.util.List;

/**
 * <p>
 * This is a DAO for ReviewFeedback. It provides CRUD operations for managing the ReviewFeedback entities in
 * persistence. Please note that ReviewFeedbackDetail entities, aggregated by ReviewFeedback entity, are also managed by
 * this DAO. However, the interface of all CRUD operations of this DAO works with the ReviewFeedback class (the data for
 * ReviewFeedbackDetail class will be accessed through ReviewFeedback entity).
 * </p>
 *
 * <p>
 * <em>Changes in 2.0:</em>
 * <ol>
 * <li>Updated class documentation.</li>
 * <li>New "operator:String" argument is added to create() and update() methods in order to support auditing, and return
 * value is added to update() method.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread safety</strong>: Implementations are required to be thread-safe with assumption that caller uses
 * method arguments thread safely.
 * </p>
 *
 * @author gevak, amazingpig, hesibo, sparemax
 * @version 2.0
 */
public interface ReviewFeedbackManager {
    /**
     * <p>
     * Creates given entity (along with its details records) in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Documentation is updated according to the new data model and new audit requirements.</li>
     * <li>New "operator:String" argument is added in order to support auditing.</li>
     * </ol>
     * </p>
     *
     * @param entity
     *            Entity to be created in persistence. Its original Id property will be ignored and after successful
     *            execution, it will be populated with newly generated identity. Its original audit-related properties
     *            (CreateUser, CreateDate, ModifyUser, ModifyDate) will be ignored, they all will be populated by this
     *            method. It must be not null. Its Comment property but be not empty (but may be null). Its Details
     *            property must be not null (but may be empty) and each of its elements (if any) must conform to all of
     *            the following validation rules:
     *            <ol>
     *            <li>Must be not null.</li>
     *            <li>Its ReviewerUserId property must contain not more than 10 significant decimal digits and must be
     *            unique across all entity.Details elements.</li>
     *            <li>Its FeedbackText property must be not null and not empty.</li>
     *            </ol>
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     *
     * @return Entity created in persistence (some of its properties will be populated as per method argument
     *         documentation). Not null.
     *
     * @throws IllegalArgumentException
     *             if any argument is invalid (as per argument description above).
     * @throws ReviewFeedbackManagementPersistenceException
     *             if any issue occurs with persistence.
     * @throws ReviewFeedbackManagementException
     *             if any other error occurs.
     */
    public ReviewFeedback create(ReviewFeedback entity, String operator) throws ReviewFeedbackManagementException;

    /**
     * <p>
     * Updates given entity (along with associations to details records) in persistence.
     * </p>
     *
     * <p>
     * <em>Changes in 2.0:</em>
     * <ol>
     * <li>Documentation is updated according to the new data model and new audit requirements.</li>
     * <li>New "operator:String" argument is added in order to support auditing.</li>
     * <li>Return value is added.</li>
     * </ol>
     * </p>
     *
     * @param entity
     *            Entity to be updated in persistence. Its Id property will be used to identify (find) entity in
     *            persistence. Its original audit-related properties (CreateUser, CreateDate, ModifyUser, ModifyDate)
     *            will be ignored, ModifyUser and ModifyDate will be populated by this method. It must be not null. Its
     *            Comment property but be not empty (but may be null). Its Details property must be not null (but may be
     *            empty) and each of its elements (if any) must conform to all of the following validation rules:
     *            <ol>
     *            <li>Must be not null.</li>
     *            <li>Its ReviewerUserId property must contain not more than 10 significant decimal digits and must be
     *            unique across all entity.Details elements.</li>
     *            <li>Its FeedbackText property must be not null and not empty.</li>
     *            </ol>
     * @param operator
     *            Specifies user who is performing this operation. Must be not null and not empty.
     *
     * @return Updated entity. Not null.
     *
     * @throws IllegalArgumentException
     *             if any argument is invalid (as per argument description above).
     * @throws ReviewFeedbackManagementEntityNotFoundException
     *             if review feedback entity with specified identity is not found in persistence.
     * @throws ReviewFeedbackManagementPersistenceException
     *             if any issue occurs with persistence.
     * @throws ReviewFeedbackManagementException
     *             if any other error occurs.
     */
    public ReviewFeedback update(ReviewFeedback entity, String operator) throws ReviewFeedbackManagementException;

    /**
     * Retrieves entity with given ID from persistence.
     *
     * @param id
     *            ID of entity to retrieve.
     * @return Retrieved entity. Null if entity with specified ID is not found in persistence.
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     * @throws ReviewFeedbackManagementException
     *             If any other error occurs.
     */
    public ReviewFeedback get(long id) throws ReviewFeedbackManagementException;

    /**
     * Deletes entity with given ID from persistence.
     *
     * @param id
     *            ID of entity to delete.
     * @return true if entity was found and deleted, false if entity was not found.
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     * @throws ReviewFeedbackManagementException
     *             If any other error occurs.
     */
    public boolean delete(long id) throws ReviewFeedbackManagementException;

    /**
     * Retrieves entities with given project ID from persistence.
     *
     * @param projectId
     *            the project ID
     *
     * @return a list of retrieved entities
     *
     * @throws ReviewFeedbackManagementPersistenceException
     *             If any issue occurs with persistence.
     * @throws ReviewFeedbackManagementException
     *             If any other error occurs.
     */
    public List<ReviewFeedback> getForProject(long projectId) throws ReviewFeedbackManagementException;
}
