/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.util.List;

import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

/**
 * <p>
 * This interface defines the contract that any project persistence must implement. The implementation classes will be
 * used by ProjectManagerImpl to perform project persistence operations. The implementation classes should have a
 * constructor that receives a namespace string parameter so that they're exchangeable with each other by changing
 * configuration settings in the manager.
 * </p>
 * <p>
 * Updated in version 1.2. Add CRUD methods for following entities, it also maintain the relationships between project
 * and following entities.
 * <ul>
 * <li>FileType</li>
 * <li>Prize</li>
 * <li>ProjectStudioSpecification</li>
 * </ul>
 * It also adds a method to get projects by direct project id.
 * </p>
 * <p>
 * <strong>Thread safety:</strong> The implementations of this interface do not have to be thread safe.
 * </p>
 *
 * @author tuenm, iamajia, flytoj2ee, VolodymyrK
 * @version 1.2.1
 * @since 1.0
 */
public interface ProjectPersistence {
    /**
     * Creates the project in the database using the given project instance. The project information is stored to
     * 'project' table, while its properties are stored in 'project_info' table. The project's associating scorecards
     * are stored in 'project_scorecard' table. For the project, its properties and associating scorecards, the operator
     * parameter is used as the creation/modification user and the creation date and modification date will be the
     * current date time when the project is created.
     *
     * @param project
     *            The project instance to be created in the database.
     * @param operator
     *            The creation user of this project.
     * @throws IllegalArgumentException
     *             if any input is null or the operator is empty string.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public void createProject(Project project, String operator) throws PersistenceException;

    /**
     * Updates the given project instance into the database. The project information is stored to 'project' table, while
     * its properties are stored in 'project_info' table. The project's associating scorecards are stored in
     * 'project_scorecard' table. All related items in these tables will be updated. If items are removed from the
     * project, they will be deleted from the persistence. Likewise, if new items are added, they will be created in the
     * persistence. For the project, its properties and associating scorecards, the operator parameter is used as the
     * modification user and the modification date will be the current date time when the project is updated. An update
     * reason is provided with this method. Update reason will be stored in the persistence for future references.
     *
     * @param project
     *            The project instance to be updated into the database.
     * @param reason
     *            The update reason. It will be stored in the persistence for future references.
     * @param operator
     *            The modification user of this project.
     * @throws IllegalArgumentException
     *             if any input is null or the operator is empty string.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public void updateProject(Project project, String reason, String operator) throws PersistenceException;

    /**
     * Retrieves the project instance from the persistence given its id. The project instances is retrieved with its
     * properties.
     *
     * @return The project instance.
     * @param id
     *            The id of the project to be retrieved.
     * @throws IllegalArgumentException
     *             if the input id is less than or equal to zero.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public Project getProject(long id) throws PersistenceException;

    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their ids. The project instances are retrieved
     * with their properties.
     * </p>
     *
     * @param ids
     *            The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws IllegalArgumentException
     *             if ids is null or empty or contain an id that is less than or equal to zero.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException;

    /**
     * Gets an array of all project types in the persistence. The project types are stored in 'project_type_lu' table.
     *
     * @return An array of all project types in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException;

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored in
     * 'project_category_lu' table.
     *
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException;

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in 'project_status_lu'
     * table.
     *
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException;

    /**
     * Gets an array of all project property type in the persistence. The project property types are stored in
     * 'project_info_type_lu' table.
     *
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException;

    /**
     * Builds {@link Project} directly from the {@link CustomResultSet}.
     *
     * @param resultSet
     *            a {@link CustomResultSet} containing the data for build the {@link Project} instances.
     * @return an array of {@link Project}
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public Project[] getProjects(CustomResultSet resultSet) throws PersistenceException;

    /**
     * Gets Project entities by given directProjectId.
     *
     * @param directProjectId
     *            the given directProjectId to find the Projects.
     * @return the found Project entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if the argument is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Project[] getProjectsByDirectProjectId(long directProjectId) throws PersistenceException;

    /**
     * Gets Project FileType entities by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found FileType entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if the argument is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType[] getProjectFileTypes(long projectId) throws PersistenceException;

    /**
     * Updates FileType entities by given projectId, it also updates the relationship between project and FileType.
     *
     * @param projectId
     *            the given projectId to update the fileTypes entities.
     * @param fileTypes
     *            the given fileTypes entities to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if projectId is non-positive or fileTypes contains null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectFileTypes(long projectId, List<FileType> fileTypes, String operator)
        throws PersistenceException;

    /**
     * Gets Project Prize entities by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found Prize entities, return empty if cannot find any.
     * @throws IllegalArgumentException
     *             if projectId is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Prize[] getProjectPrizes(long projectId) throws PersistenceException;

    /**
     * Updates Prize entities by given projectId, it also updates the relationship between project and Prize.
     *
     * @param projectId
     *            the given projectId to update the prizes entities.
     * @param prizes
     *            the given prizes entities to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if projectId is non-positive, or prizes contains null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectPrizes(long projectId, List<Prize> prizes, String operator) throws PersistenceException;

    /**
     * Creates the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to create.
     * @param operator
     *            the given audit user.
     * @return the created fileType entity.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType createFileType(FileType fileType, String operator) throws PersistenceException;

    /**
     * Updates the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateFileType(FileType fileType, String operator) throws PersistenceException;

    /**
     * Removes the given FileType entity.
     *
     * @param fileType
     *            the given fileType entity to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if fileType is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removeFileType(FileType fileType, String operator) throws PersistenceException;

    /**
     * Gets all FileType entities.
     *
     * @return the found FileType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType[] getAllFileTypes() throws PersistenceException;

    /**
     * Gets all PrizeType entities.
     *
     * @return the found PrizeType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public PrizeType[] getPrizeTypes() throws PersistenceException;

    /**
     * Creates the given Prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @return the created prize entity.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public Prize createPrize(Prize prize, String operator) throws PersistenceException;

    /**
     * Updates the given prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updatePrize(Prize prize, String operator) throws PersistenceException;

    /**
     * Removes the given prize entity.
     *
     * @param prize
     *            the given prize entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if prize is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removePrize(Prize prize, String operator) throws PersistenceException;

    /**
     * Creates the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @return the created spec entity
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public ProjectStudioSpecification createProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException;

    /**
     * Updates the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException;

    /**
     * Removes the given ProjectStudioSpecification entity.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to create.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void removeProjectStudioSpecification(ProjectStudioSpecification spec, String operator)
        throws PersistenceException;

    /**
     * Gets ProjectStudioSpecification entity by given projectId.
     *
     * @param projectId
     *            the given projectId to find the entities.
     * @return the found ProjectStudioSpecification entities, return null if cannot find any.
     * @throws IllegalArgumentException
     *             if projectId is non-positive
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public ProjectStudioSpecification getProjectStudioSpecification(long projectId) throws PersistenceException;

    /**
     * Updates the given ProjectStudioSpecification entity for specified project id.
     *
     * @param spec
     *            the given ProjectStudioSpecification entity to update.
     * @param projectId
     *            the given project id to update.
     * @param operator
     *            the given audit user.
     * @throws IllegalArgumentException
     *             if spec is null, or projectId is non-positive or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateStudioSpecificationForProject(ProjectStudioSpecification spec, long projectId, String operator)
        throws PersistenceException;
}
