/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.topcoder.search.builder.SearchBuilderConfigurationException;
import com.topcoder.search.builder.SearchBuilderException;
import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.SearchBundleManager;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.datavalidator.IntegerValidator;
import com.topcoder.util.datavalidator.LongValidator;
import com.topcoder.util.datavalidator.StringValidator;
import com.topcoder.util.sql.databaseabstraction.CustomResultSet;

/**
 * <p>
 * This is the manager class of this component. It loads persistence implementation using settings in the configuration
 * namespace. Then use the persistence implementation to create/update/retrieve/search projects. This is the main class
 * of the component, which is used by client to perform the above project operations. Searching projects and getting
 * project associated with and external user id are two operations which the logic reside in this class. The remaining
 * operations are delegated to persistence implementation.
 * </p>
 * <p>
 * The default configuration namespace for this class is: &quot;com.topcoder.management.project&quot;. It can accept a
 * custom namespace as well. Apart from the persistence settings, it also initialize a SearchBundle instance to use in
 * projects searching and a ProjectValidator instance to validate projects.
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
 * <strong>Thread safety:</strong> The implementation is not thread safe in that two threads running the same method
 * will use the same statement and could overwrite each other's work.
 * </p>
 *
 * @author tuenm, iamajia, flytoj2ee, VolodymyrK
 * @version 1.2.2
 * @since 1.0
 */
public class ProjectManagerImpl implements ProjectManager {
    /**
     * The default namespace of this component. It will be used in the default constructor.
     */
    public static final String NAMESPACE = "com.topcoder.management.project";

    /**
     * Represents the ProjectSearchBundle.
     */
    private static final String PROJECT_SEARCH_BUNDLE = "ProjectSearchBundle";
    /**
     * Represents the persistence class property name.
     */
    private static final String PERSISTENCE_CLASS = "PersistenceClass";
    /**
     * Represents the persistence namespace property name.
     */
    private static final String PERSISTENCE_NAMESPACE = "PersistenceNamespace";
    /**
     * Represents the Validator class property name.
     */
    private static final String VALIDATOR_CLASS = "ValidatorClass";
    /**
     * Represents the Validator namespace property name.
     */
    private static final String VALIDATOR_NAMESPACE = "ValidatorNamespace";
    /**
     * Represents the SearchBuilderNamespace namespace property name.
     */
    private static final String SEARCH_BUILDER_NAMESPACE = "SearchBuilderNamespace";
    /**
     * represents the max length of name.
     */
    private static final int MAX_LENGTH_OF_NAME = 64;
    /**
     * represents the max length of value.
     */
    private static final int MAX_LENGTH_OF_VALUE = 4096;
    /**
     * The persistence instance. It is initialized in the constructor using reflection and never changed after that. It
     * is used in the create/update/retrieve/search project methods.
     */
    private final ProjectPersistence persistence;

    /**
     * The search bundle instance in SearchBuilder component. It is initialized in the constructor and never changed
     * after that. It is used in the search project method.
     */
    private final SearchBundle searchBundle;

    /**
     * The project validator instance. It is initialized in the constructor using reflection and never changed after
     * that. It is used to validate projects before create/update them.
     */
    private final ProjectValidator validator;

    /**
     * Create a new instance of ProjectManagerImpl using the default configuration namespace. First it load the
     * 'PersistenceClass' and 'PersistenceNamespace' properties to initialize the persistence plug-in implementation.
     * The 'PersistenceNamespace' is optional and if it does not present, value of 'PersistenceClass' property will be
     * used. Then it load the 'SearchBuilderNamespace' property to initialize SearchBuilder component.
     * <p>
     *
     * @throws ConfigurationException
     *             if error occurs while loading configuration settings, or required configuration parameter is missing.
     */
    public ProjectManagerImpl() throws ConfigurationException {
        this(NAMESPACE);
    }

    /**
     * Creates a new instance of ProjectManagerImpl using the given configuration namespace. First it load the
     * 'PersistenceClass' and 'PersistenceNamespace' properties to initialize the persistence plug-in implementation.
     * The 'PersistenceNamespace' is optional and if it does not present, value of 'PersistenceClass' property will be
     * used. Then it load the 'SearchBuilderNamespace' property to initialize SearchBuilder component.
     * <p>
     *
     * @param ns
     *            The namespace to load configuration settings from.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     * @throws ConfigurationException
     *             if error occurs while loading configuration settings, or required configuration parameter is missing.
     */
    public ProjectManagerImpl(String ns) throws ConfigurationException {
        Helper.checkStringNotNullOrEmpty(ns, "ns");
        // get config manager instance.
        ConfigManager cm = ConfigManager.getInstance();
        try {
            // get PersistenceClass property.
            String persistenceClass = cm.getString(ns, PERSISTENCE_CLASS);
            // assert perisitenceClass not null.
            Helper.checkObjectNotNull(persistenceClass, "persistenceClass");

            // get PersistenceNamespace property.
            String persistenceNamespace = cm.getString(ns, PERSISTENCE_NAMESPACE);

            // if PersistenceNamespace property is not exist, use
            // persistenceClass instead.
            if (persistenceNamespace == null) {
                persistenceNamespace = persistenceClass;
            }
            // get ValidatorClass property.
            String validatorClass = cm.getString(ns, VALIDATOR_CLASS);
            // assert validatorClass not null.
            Helper.checkObjectNotNull(validatorClass, "validatorClass");

            // get ValidatorNamespace property.
            String validatorNamespace = cm.getString(ns, VALIDATOR_NAMESPACE);

            // if ValidatorNamespace property is not exist, use validatorClass
            // instead.
            if (validatorNamespace == null) {
                validatorNamespace = validatorClass;
            }
            // create persistence and validator
            persistence = (ProjectPersistence) createObject(persistenceClass, persistenceNamespace);
            validator = (ProjectValidator) createObject(validatorClass, validatorNamespace);
            String searchBuilderNamespace = cm.getString(ns, SEARCH_BUILDER_NAMESPACE);
            Helper.checkObjectNotNull(searchBuilderNamespace, "searchBuilderNamespace");

            // create SearchBundleManager.
            SearchBundleManager manager = new SearchBundleManager(searchBuilderNamespace);
            // create searchBundle
            searchBundle = manager.getSearchBundle(PROJECT_SEARCH_BUNDLE);
            Helper.checkObjectNotNull(searchBundle, "searchBundle");
        } catch (IllegalArgumentException iae) {
            throw new ConfigurationException("some property is missed", iae);
        } catch (UnknownNamespaceException une) {
            throw new ConfigurationException(ns + " namespace is unknown.", une);
        } catch (ClassCastException cce) {
            throw new ConfigurationException("error occurs", cce);
        } catch (SearchBuilderConfigurationException sbce) {
            throw new ConfigurationException("error occurs when creating SearchBundleManager", sbce);
        }
        // set validation map.
        setValidationMap();
    }

    /**
     * Creates the project in the database using the given project instance. The project information is stored to
     * 'project' table, while its properties are stored in 'project_info' table. For the project, its properties, the
     * operator parameter is used as the creation/modification user and the creation date and modification date will be
     * the current date time when the project is created. The given project instance will be validated before
     * persisting.
     *
     * @param project
     *            The project instance to be created in the database.
     * @param operator
     *            The creation user of this project.
     * @throws IllegalArgumentException
     *             if any input is null or the operator is empty string.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     * @throws ValidationException
     *             if error occurred while validating the project instance.
     */
    public void createProject(Project project, String operator) throws PersistenceException, ValidationException {
        validator.validateProject(project);
        persistence.createProject(project, operator);
    }

    /**
     * Updates the given project instance into the database. The project information is stored to 'project' table, while
     * its properties are stored in 'project_info' table. All related items in these tables will be updated. If items
     * are removed from the project, they will be deleted from the persistence. Likewise, if new items are added, they
     * will be created in the persistence. For the project, its properties, the operator parameter is used as the
     * modification user and the modification date will be the current date time when the project is updated. An update
     * reason is provided with this method. Update reason will be stored in the persistence for future references. The
     * given project instance will be validated before persisting.
     * <p>
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
     * @throws ValidationException
     *             if error occurred while validating the project instance.
     */
    public void updateProject(Project project, String reason, String operator) throws PersistenceException,
            ValidationException {
        validator.validateProject(project);
        persistence.updateProject(project, reason, operator);
    }

    /**
     * Retrieves the project instance from the persistence given its id. The project instance is retrieved with its
     * related items, such as properties.
     *
     * @return The project instance.
     * @param id
     *            The id of the project to be retrieved.
     * @throws IllegalArgumentException
     *             if the input id is less than or equal to zero.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public Project getProject(long id) throws PersistenceException {
        return persistence.getProject(id);
    }

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
    public Project[] getProjects(long[] ids) throws PersistenceException {
        return persistence.getProjects(ids);
    }

    /**
     * <p>
     * Searches project instances using the given filter parameter. The filter parameter decides the condition of
     * searching. This method use the Search Builder component to perform searching. The search condition can be the
     * combination of any of the followings:
     * </p>
     * <ul>
     * <li>Project type id</li>
     * <li>Project type name</li>
     * <li>Project category id</li>
     * <li>Project category name</li>
     * <li>Project status id</li>
     * <li>Project status name</li>
     * <li>Project property name</li>
     * <li>Project property value</li>
     * <li>Project resource property name</li>
     * <li>Project resource property value</li>
     * </ul>
     * The filter is created using the ProjectFilterUtility class. This class provide static methods to create filter of
     * the above conditions and any combination of them.
     *
     * @return An array of project instance as the search result.
     * @param filter
     *            The filter to search for projects.
     * @throws IllegalArgumentException
     *             if the filter is null.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public Project[] searchProjects(Filter filter) throws PersistenceException {
        Helper.checkObjectNotNull(filter, "filter");
        try {
            // search depend the filter
            CustomResultSet result = (CustomResultSet) searchBundle.search(filter);

            return persistence.getProjects(result);
        } catch (SearchBuilderException sbe) {
            throw new PersistenceException("error occurs when getting search result.", sbe);
        } catch (ClassCastException cce) {
            throw new PersistenceException("error occurs when trying to get ids.", cce);
        }
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in 'project_type_lu' table.
     *
     * @return An array of all project types in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        return persistence.getAllProjectTypes();
    }

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored in
     * 'project_category_lu' table.
     *
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException {
        return persistence.getAllProjectCategories();
    }

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in 'project_status_lu'
     * table.
     *
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        return persistence.getAllProjectStatuses();
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are stored in
     * 'project_info_type_lu' table.
     *
     * @return An array of all property assignments in the persistence.
     * @throws PersistenceException
     *             if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        return persistence.getAllProjectPropertyTypes();
    }

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
    public Project[] getProjectsByDirectProjectId(long directProjectId) throws PersistenceException {
        return persistence.getProjectsByDirectProjectId(directProjectId);
    }

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
    public FileType[] getProjectFileTypes(long projectId) throws PersistenceException {
        return persistence.getProjectFileTypes(projectId);
    }

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
        throws PersistenceException {
        persistence.updateProjectFileTypes(projectId, fileTypes, operator);
    }

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
    public Prize[] getProjectPrizes(long projectId) throws PersistenceException {
        return persistence.getProjectPrizes(projectId);
    }

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
     *             if projectId is non-positive, prizes contains null, or if operator is null or empty.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public void updateProjectPrizes(long projectId, List<Prize> prizes, String operator) throws PersistenceException {
        persistence.updateProjectPrizes(projectId, prizes, operator);
    }

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
    public FileType createFileType(FileType fileType, String operator) throws PersistenceException {
        return persistence.createFileType(fileType, operator);
    }

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
    public void updateFileType(FileType fileType, String operator) throws PersistenceException {
        persistence.updateFileType(fileType, operator);
    }

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
    public void removeFileType(FileType fileType, String operator) throws PersistenceException {
        persistence.removeFileType(fileType, operator);
    }

    /**
     * Gets all FileType entities.
     *
     * @return the found FileType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public FileType[] getAllFileTypes() throws PersistenceException {
        return persistence.getAllFileTypes();
    }

    /**
     * Gets all PrizeType entities.
     *
     * @return the found PrizeType entities, return empty if cannot find any.
     * @throws PersistenceException
     *             if there are any exceptions.
     * @since 1.2
     */
    public PrizeType[] getPrizeTypes() throws PersistenceException {
        return persistence.getPrizeTypes();
    }

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
    public Prize createPrize(Prize prize, String operator) throws PersistenceException {
        return persistence.createPrize(prize, operator);
    }

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
    public void updatePrize(Prize prize, String operator) throws PersistenceException {
        persistence.updatePrize(prize, operator);
    }

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
    public void removePrize(Prize prize, String operator) throws PersistenceException {
        persistence.removePrize(prize, operator);
    }

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
        throws PersistenceException {
        return persistence.createProjectStudioSpecification(spec, operator);
    }

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
        throws PersistenceException {
        persistence.updateProjectStudioSpecification(spec, operator);
    }

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
        throws PersistenceException {
        persistence.removeProjectStudioSpecification(spec, operator);
    }

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
    public ProjectStudioSpecification getProjectStudioSpecification(long projectId) throws PersistenceException {
        return persistence.getProjectStudioSpecification(projectId);
    }

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
        throws PersistenceException {
        persistence.updateStudioSpecificationForProject(spec, projectId, operator);
    }

    /**
     * this private method is used to create object via reflection.
     *
     * @param className
     *            className to use
     * @param namespace
     *            namespace to use
     * @throws ConfigurationException
     *             if any error occurs
     * @return the object created.
     */
    @SuppressWarnings("unchecked")
    private static Object createObject(String className, String namespace) throws ConfigurationException {
        try {
            // get constructor
            Constructor constructor = Class.forName(className).getConstructor(new Class[]{String.class});
            // create object
            return constructor.newInstance(new Object[]{namespace});
        } catch (ClassNotFoundException cnfe) {
            throw new ConfigurationException("error occurs when trying to create object via reflection.", cnfe);
        } catch (NoSuchMethodException nsme) {
            throw new ConfigurationException("error occurs when trying to create object via reflection.", nsme);
        } catch (InstantiationException ie) {
            throw new ConfigurationException("error occurs when trying to create object via reflection.", ie);
        } catch (IllegalAccessException iae) {
            throw new ConfigurationException("error occurs when trying to create object via reflection.", iae);
        } catch (InvocationTargetException ite) {
            throw new ConfigurationException("error occurs when trying to create object via reflection.", ite);
        }
    }

    /**
     * This private method is used to set validationMap of searchBundle.
     */
    @SuppressWarnings("unchecked")
    private void setValidationMap() {
        // Create a map with the following key/value pairs, this is required by SearchBundle
        Map validationMap = new HashMap();
        validationMap.put(ProjectFilterUtility.PROJECT_TYPE_ID, LongValidator.isPositive());
        validationMap.put(ProjectFilterUtility.PROJECT_CATEGORY_ID, LongValidator.isPositive());
        validationMap.put(ProjectFilterUtility.PROJECT_STATUS_ID, LongValidator.isPositive());
        validationMap.put(ProjectFilterUtility.PROJECT_TYPE_NAME,
                StringValidator.hasLength(IntegerValidator.lessThanOrEqualTo(MAX_LENGTH_OF_NAME)));
        validationMap.put(ProjectFilterUtility.PROJECT_CATEGORY_NAME,
                StringValidator.hasLength(IntegerValidator.lessThanOrEqualTo(MAX_LENGTH_OF_NAME)));
        validationMap.put(ProjectFilterUtility.PROJECT_STATUS_NAME,
                StringValidator.hasLength(IntegerValidator.lessThanOrEqualTo(MAX_LENGTH_OF_NAME)));
        validationMap.put(ProjectFilterUtility.PROJECT_PROPERTY_NAME,
                StringValidator.hasLength(IntegerValidator.lessThanOrEqualTo(MAX_LENGTH_OF_NAME)));
        validationMap.put(ProjectFilterUtility.PROJECT_PROPERTY_VALUE,
                StringValidator.hasLength(IntegerValidator.lessThanOrEqualTo(MAX_LENGTH_OF_VALUE)));
        validationMap.put(ProjectFilterUtility.PROJECT_TC_DIRECT_PROJECT_ID, LongValidator.isPositive());
        // set the validation map
        searchBundle.setSearchableFields(validationMap);
    }
}
