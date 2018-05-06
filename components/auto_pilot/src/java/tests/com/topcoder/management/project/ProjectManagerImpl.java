/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.project;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topcoder.search.builder.SearchBundle;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>
 * This is the manager class of this component. It loads persistence implementation using settings
 * in the configuration namespace. Then use the persistence implementation to
 * create/update/retrieve/search projects. This is the main class of the component, which is used by
 * client to perform the above project operations. Searching projects and getting project associated
 * with and external user id are two operations which the logic reside in this class. The remaning
 * operations are delegated to persistence implementation.
 * </p>
 * The default configuration namespace for this class is:
 * &quot;com.topcoder.management.project&quot;. It can accept a custom namespace as well. Apart from
 * the persistence settings, it also initialize a SearchBundle instance to use in projects searching
 * and a ProjectValidator instance to validate projects.
 * <p>
 * Thread Safety: The implementation is not thread safe in that two threads running the same method
 * will use the same statement and could overwrite each other's work.
 * </p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class ProjectManagerImpl implements ProjectManager {

    /**
     * The default namespace of this component. It will be used in the default constructor.
     */
    public static final String NAMESPACE = "com.topcoder.management.project";

    /**
     * The persistence instance. It is initialized in the constructor using refelction and never
     * changed after that. It is used in the create/update/retrieve/search project methods.
     */
    private final ProjectPersistence persistence = null;

    /**
     * The search bundle instance in SearchBuilder component. It is initialized in the constructor
     * and never changed after that. It is used in the search project method.
     */
    private final SearchBundle searchBundle = null;

    /**
     * The project validator instance. It is initialized in the constructor using reflection and
     * never changed after that. It is used to validate projects before create/update them.
     */
    private final ProjectValidator validator = null;

    /** Mock data.*/
    private Project[] projects;
    /** Mock data.*/
    private ProjectType[] types;
    /** Mock data.*/
    private ProjectStatus[] stats;
    /** Mock data.*/
    private ProjectCategory[] categories;
    /** Mock data.*/
    private ProjectPropertyType[] propertyTypes;

    /**
     * Create a new instance of ProjectManagerImpl using the default configuration namespace. First
     * it load the 'PersistenceClass' and 'PersistenceNamespace' properties to initialize the
     * persistence plug-in implementation. The 'PersistenceNamespace' is optional and if it does not
     * present, value of 'PersistenceClass' property will be used. Then it load the
     * 'SearchBuilderNamespace' property to initialize SearchBuilder component.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>Load 'PersistenceClass' property, required</li>
     * <li>Load 'PersistenceNamespace' property, optional. If not exist, use the 'PersistenceClass'
     * value.</li>
     * <li>Using 'PersistenceClass' property, create a new instance of ProjectPersistence use
     * reflection,</li>
     * the 'PersistenceNamespace' value will be passed into the constructor of the persistence
     * implementation class during creation. The created instance is set to the 'persistence' member
     * variable.
     * <li>Repeat the above steps with 'ValidatorClass' and 'ValidatorNamespace' property.</li>
     * <li>Initialize the 'searchBundle' instance to use in PersistenceException searching. See
     * Component Specification, algorithm section for this part.</li>
     * </ul>
     * @throws ConfigurationException if error occurs while loading configuration settings, or
     *             required configuration parameter is missing.
     */
    public ProjectManagerImpl() throws ConfigurationException {
        // your code here
        projects = new Project[5];
        types = new ProjectType[5];
        stats = new ProjectStatus[5];
        categories = new ProjectCategory[5];
        propertyTypes = new ProjectPropertyType[5];

        for (int i = 1; i <= 5; i++) {
            types[i - 1] = new ProjectType(i, "Type#" + i);
            stats[i - 1] = new ProjectStatus(i, "Status#" + i);
            categories[i - 1] = new ProjectCategory(i, "Category#" + i, types[i - 1]);
            propertyTypes[i - 1] = new ProjectPropertyType(i, "PropertyType#" + i);

            projects[i - 1] = new Project(i, categories[i - 1], stats[i - 1]);
            Date d = new Date();
            projects[i - 1].setCreationTimestamp(d);
            projects[i - 1].setModificationTimestamp(d);
            projects[i - 1].setCreationUser("foo");
            projects[i - 1].setModificationUser("foo");
        }
    }

    /**
     * Create a new instance of ProjectManagerImpl using the given configuration namespace. First it
     * load the 'PersistenceClass' and 'PersistenceNamespace' properties to initialize the
     * persistence plug-in implementation. The 'PersistenceNamespace' is optional and if it does not
     * present, value of 'PersistenceClass' property will be used. Then it load the
     * 'SearchBuilderNamespace' property to initialize SearchBuilder component.
     * <p>
     * Implementation notes:
     * </p> - Similar to the default constructor.
     * @param ns The namespace to load configuration settings from.
     * @throws IllegalArgumentException if the input is null or empty string.
     * @throws ConfigurationException if error occurs while loading configuration settings, or
     *             required configuration parameter is missing.
     */
    public ProjectManagerImpl(String ns) throws ConfigurationException {
        // your code here
        this();
    }

    /**
     * Sets the project status for this project instance. Null value is not allowed.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>Set the corresponding member variables.</li>
     * </ul>
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string.
     * @throws PersistenceException if error occurred while accessing the database.
     * @throws ValidationException if error occurred while validating the project instance.
     */
    public void createProject(Project project, String operator) throws PersistenceException,
        ValidationException {
        // your code here
    }

    /**
     * Update the given project instance into the database. The project information is stored to
     * 'project' table, while its properties are stored in 'project_info' table. The project's
     * associating scorecards are stored in 'project_scorecard' table. All related items in these
     * tables will be updated. If items are removed from the project, they will be deleted from the
     * persistence. Likewise, if new items are added, they will be created in the persistence. For
     * the project, its properties and associating scorecards, the operator parameter is used as the
     * modification user and the modification date will be the current date time when the project is
     * updated. An update reason is provided with this methiod. Update reason will be stored in the
     * persistence for future references. The given project instance will be validated before
     * persisting.
     * <p>
     * Implementation notes:
     * </p> - Validate the project instance by calling validator.validateProject(project) - Call
     * persistence.updateProject(project, operator)
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for future references.
     * @param operator The modification user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string.
     * @throws PersistenceException if error occurred while accessing the database.
     * @throws ValidationException if error occurred while validating the project instance.
     */
    public void updateProject(Project project, String reason, String operator)
        throws PersistenceException, ValidationException {
        // your code here
        project.setModificationUser("foo");
        project.setModificationTimestamp(new Date());
    }

    /**
     * Retrieves the project instance from the persistence given its id. The project instance is
     * retrieved with its related items, such as properties and scorecards.
     * <p>
     * Implementation notes:
     * </p> - return persistence.getProject(id, true)
     * @return The project instance.
     * @param id The id of the project to be retrieved.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project getProject(long id) throws PersistenceException {
        if (id < 1) {
            throw new IllegalArgumentException("id cannot be 0 or negative");
        }

        if (id > projects.length) {
            return null;
        } else {
            return projects[(int) id - 1];
        }
    }

    /**
     * <p>
     * Retrieves an array of project instance from the persistence given their ids. The project
     * instances are retrieved with their properties.
     * </p>
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>See the Component Specification, Algorithm section.</li>
     * </ul>
     * <p>
     * </p>
     * @param ids The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException {
        // your code here
        List ret = new ArrayList();
        for (int i = 0; i < ids.length; i++) {
            Project proj = getProject(ids[i]);
            if (null != proj) {
                ret.add(proj);
            }
        }
        return (Project[]) ret.toArray(new Project[0]);
    }

    /**
     * <p>
     * Searchs project instances using the given filter parameter. The filter parameter decides the
     * condition of searching. This method use the Search Builder component to perform searching.
     * The search condition can be the combination of any of the followings:
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
     * The filter is created using the ProjectFilterUtility class. This class provide static methods
     * to create filter of the above conditions and any combination of them. Implementation note: -
     * See the Component Specification, Algorithm section.
     * @return An array of project instance as the search result.
     * @param filter The filter to search for projects.
     * @throws IllegalArgumentException if the filter is null.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] searchProjects(Filter filter) throws PersistenceException {
        return projects;
    }

    /**
     * Gets the projects associated with an external user id. The user id is defined as a property
     * of of a resource that belong to the project. The resource property name is 'External
     * Reference ID'. and the property value is the given user id converted to string.
     * <p>
     * Implementation notes:
     * </p>
     * <p>- See the Component Specification, Algorithm section.<br/>
     * </p>
     * @return An array of project instances associated with the given user id.
     * @param user The user id to search for projects.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getUserProjects(long user) throws PersistenceException {
        return (user == 1) ? projects : null;
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in
     * 'project_type_lu' table.
     * <p>
     * Implementation notes:
     * </p> - return persistence.getAllProjectTypes();
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        return types;
    }

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored
     * in 'project_category_lu' table.
     * <p>
     * Implementation notes:
     * </p> - return persistence.getAllProjectCategories();
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException {
        return categories;
    }

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in
     * 'project_status_lu' table.
     * <p>
     * Implementation notes:
     * </p> - return persistence.getAllProjectStatuses();
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        return stats;
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are
     * stored in 'project_info_type_lu' table.
     * <p>
     * Implementation notes:
     * </p> - return persistence.getAllProjectPropertyTypes();
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        return propertyTypes;
    }
}
