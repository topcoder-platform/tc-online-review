/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao;

import java.util.List;

import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseType;

/**
 * <p>
 * This interface defines the dao for manipulating the TermsOfUse entities together with their dependencies, and
 * TermsOfUseType entities. It simply provides CRUD operations on this entity and retrieval operation by the terms of
 * use type. It also provides retrieval and update operation for terms of use type, and create/retrieve/delete
 * operations for terms of use dependencies.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added methods for managing terms of use dependencies.</li>
 * <li>Updated throws doc of createTermsOfUse() and updateTermsOfUse() methods to support newly added
 * TermsOfUse#agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The implementations are required to be thread safe.
 * </p>
 *
 * @author faeton, sparemax, saarixx
 * @version 1.1
 */
public interface TermsOfUseDao {
    /**
     * <p>
     * Creates terms of use entity with the terms text.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Throws IllegalArgumentException when termsOfUse.getAgreeabilityType() is null.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUse
     *            a TermsOfUse containing required information for creation.
     * @param termsText
     *            the terms text to create.
     *
     * @return a TermsOfUse with created id attribute.
     *
     * @throws IllegalArgumentException
     *             if termsOfUse or termsOfUse.getAgreeabilityType() is null.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse createTermsOfUse(TermsOfUse termsOfUse, String termsText) throws TermsOfUsePersistenceException;

    /**
     * <p>
     * Updates terms of use entity.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Throws IllegalArgumentException when termsOfUse.getAgreeabilityType() is null.</li>
     * </ol>
     * </p>
     *
     * @param termsOfUse
     *            a TermsOfUse containing required information for update.
     *
     * @return a TermsOfUse with updated id attribute.
     *
     * @throws IllegalArgumentException
     *             if termsOfUseor termsOfUse.getAgreeabilityType() is null.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse updateTermsOfUse(TermsOfUse termsOfUse) throws EntityNotFoundException,
        TermsOfUsePersistenceException;

    /**
     * Retrieves a terms of use entity from the database.
     *
     * @param termsOfUseId
     *            a long containing the terms of use id to retrieve.
     *
     * @return a TermsOfUse with the requested terms of use or null if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUse getTermsOfUse(long termsOfUseId) throws TermsOfUsePersistenceException;

    /**
     * Deletes a terms of use entity from the database.
     *
     * @param termsOfUseId
     *            a long containing the terms of use id to delete.
     *
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public void deleteTermsOfUse(long termsOfUseId) throws EntityNotFoundException, TermsOfUsePersistenceException;

    /**
     * Retrieves a terms of use entities by the terms of use type id from the database.
     *
     * @param termsOfUseTypeId
     *            an int containing the terms of use type id to retrieve.
     *
     * @return a list of TermsOfUse entities with the requested terms of use or empty list if not found.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<TermsOfUse> getTermsOfUseByTypeId(int termsOfUseTypeId) throws TermsOfUsePersistenceException;

    /**
     * Retrieves all terms of use entities from the database.
     *
     * @return a list of all TermsOfUse entities.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public List<TermsOfUse> getAllTermsOfUse() throws TermsOfUsePersistenceException;

    /**
     * Gets terms of use type by id.
     *
     * @param termsOfUseTypeId
     *            terms of use type id.
     *
     * @return terms of use type.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUseType getTermsOfUseType(int termsOfUseTypeId) throws TermsOfUsePersistenceException;

    /**
     * Update terms of use type.
     *
     * @param termsType
     *            the terms of use type to be updated.
     *
     * @return updated terms of use type.
     *
     * @throws IllegalArgumentException
     *             if termsType is null.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public TermsOfUseType updateTermsOfUseType(TermsOfUseType termsType) throws EntityNotFoundException,
        TermsOfUsePersistenceException;

    /**
     * Gets terms of use text by terms of use id.
     *
     * @param termsOfUseId
     *            terms of use id.
     *
     * @return text of terms of use.
     *
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     */
    public String getTermsOfUseText(long termsOfUseId) throws EntityNotFoundException, TermsOfUsePersistenceException;

    /**
     * Sets terms of use text.
     *
     * @param termsOfUseId
     *            terms of use id.
     * @param text
     *            text of terms of use.
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     * @throws EntityNotFoundException
     *             if the entity was not found in the database.
     */
    public void setTermsOfUseText(long termsOfUseId, String text) throws TermsOfUsePersistenceException,
        EntityNotFoundException;

    /**
     * Creates a dependency relationship between two terms of use. This method ensures that a dependency loop is not
     * created.
     *
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUseCyclicDependencyException
     *             if creation of the specified relationship will lead to a dependency loop.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void createDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)
        throws TermsOfUseCyclicDependencyException, TermsOfUsePersistenceException;

    /**
     * Retrieves all dependency terms of use for the dependent terms of use with the given ID. Returns an empty list
     * if none found.
     *
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @return the retrieved dependency terms of use (not null; doesn't contain null)
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public List<TermsOfUse> getDependencyTermsOfUse(long dependentTermsOfUseId) throws TermsOfUsePersistenceException;

    /**
     * Retrieves all dependent terms of use for the dependency terms of use with the given ID. Returns an empty list
     * if none found.
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     *
     * @return the retrieved dependent terms of use (not null; doesn't contain null)
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public List<TermsOfUse> getDependentTermsOfUse(long dependencyTermsOfUseId) throws TermsOfUsePersistenceException;

    /**
     * Deletes the dependency relationship between the dependent and dependency terms of use with the specified IDs.
     *
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUseDependencyNotFoundException
     *             if dependency relationship between the specified dependency and dependent terms of use doesn't
     *             exist.
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteDependencyRelationship(long dependentTermsOfUseId, long dependencyTermsOfUseId)
        throws TermsOfUseDependencyNotFoundException, TermsOfUsePersistenceException;

    /**
     * Deletes all dependency relationships in which terms of use with the specified ID is a dependent.
     *
     * @param dependentTermsOfUseId
     *            the ID of the dependent terms of use
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteAllDependencyRelationshipsForDependent(long dependentTermsOfUseId)
        throws TermsOfUsePersistenceException;

    /**
     * Deletes all dependent relationships in which terms of use with the specified ID is a dependency.
     *
     * @param dependencyTermsOfUseId
     *            the ID of the dependency terms of use
     *
     * @throws TermsOfUsePersistenceException
     *             if any persistence error occurs.
     *
     * @since 1.1
     */
    public void deleteAllDependencyRelationshipsForDependency(long dependencyTermsOfUseId)
        throws TermsOfUsePersistenceException;
}
