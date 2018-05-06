/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.project.persistence;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.management.project.ConfigurationException;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectPersistence;
import com.topcoder.management.project.ProjectPropertyType;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.util.idgenerator.IDGenerator;

/**
 * <p>
 * This class contains operations to create/update/retrieve project instances from the Informix
 * database. It also provides methods to retrieve database look up table values. It implements the
 * ProjectPersistence interface to provide a plug-in persistence for Informix database. It is used
 * by the ProjectManagerImpl class. The constructor takes a namespace parameter to initialize
 * database connection.
 * </p>
 * <p>
 * Note that in this class, delete operation is not supported. To delete a project, its status is
 * set to 'Deleted'. Create and update operations here work on the project and its related items as
 * well. It means creating/updating a project would involve creating/updating its properties and
 * associated scorecards.
 * </p>
 * Thread Safety: The implementation is not thread safe in that two threads running the same method
 * will use the same statement and could overwrite each other's work.
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class InformixProjectPersistence implements ProjectPersistence {

    /**
     * <p>
     * Represents the default value for Project Id sequence name. It is used to create id generator
     * for project. This value will be overrided by '<!--StartFragment--><span
     * style='font-size:10.0pt;font-family:Arial; mso-fareast-font-family:"Times New
     * Roman";mso-ansi-language:EN-US;mso-fareast-language:
     * EN-US;mso-bidi-language:AR-SA'>ProjectIdSequenceName</span><!--EndFragment-->' configuraton
     * parameter if it exist.
     * </p>
     */
    public static final String PROJECT_ID_SEQUENCE_NAME = "project_id_seq";

    /**
     * <p>
     * Represents the default value for project audit id sequence name. It is used to create id
     * generator for project audit. This value will be overrided by '<!--StartFragment--><span
     * style='font-size:10.0pt;font-family:Arial; mso-fareast-font-family:"Times New
     * Roman";mso-ansi-language:EN-US;mso-fareast-language:
     * EN-US;mso-bidi-language:AR-SA'>ProjectAuditIdSequenceName</span><!--EndFragment-->'
     * configuraton parameter if it exist.
     * </p>
     */
    public static final String PROJECT_AUDIT_ID_SEQUENCE_NAME = "project_audit_id_seq";

    /**
     * The factory instance used to create connection to the database. It is initialized in the
     * constructor using DBConnectionFactory component and never changed after that. It will be used
     * in various persistence methods of this project.
     */
    private final DBConnectionFactory factory = null;

    /**
     * <p>
     * Represents the database connection name that will be used by DBConnectionFactory. This
     * variable is initialized in the constructor and never changed after that. It will be used in
     * create/update/retrieve method to create connection. This variable can be null, which mean
     * connection name is not defined in the configuration nanespace and default connection will be
     * created.
     * </p>
     */
    private final String connectionName = null;

    /**
     * <p>
     * Represents the IDGenerator for project table. This variable is initiazlied in the constructor
     * and never change after that. It will be used in createProject() method to generate new Id for
     * project..
     * </p>
     */
    private final IDGenerator projectIdGenerator = null;

    /**
     * <p>
     * Represents the IDGenerator for project audit table. This variable is initiazlied in the
     * constructor and never change after that. It will be used in updateProject() method to store
     * udpate reason.
     * </p>
     */
    private final IDGenerator projectAuditIdGenerator = null;

    /**
     * Create a new instance of InformixProjectPersistence. The passing namespace parameter will be
     * used to get the namespace to initialize DBConnectionFactory component. ConfigurationException
     * will be thrown if the 'ConnectionFactoryNS' property is missing in the given namespace.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>Get the value of the 'ConnectionFactoryNS' property in the given namespace.</li>
     * <li>Create a new instance of DBConnectionFactoryImpl using the above value and assign to the
     * 'factory' member.</li>
     * <li>Get the value of the 'ConnectionName' property in the given namespace if it exist and
     * assign it to 'connectionName' member variable.</li>
     * <li>Get the value of '<!--StartFragment--><span style='font-size:10.0pt;font-family:Arial;
     * mso-fareast-font-family:"Times New Roman";mso-ansi-language:EN-US;mso-fareast-language:
     * EN-US;mso-bidi-language:AR-SA'>ProjectIdSequenceName</span><!--EndFragment-->' and '<!--StartFragment--><span
     * style='font-size:10.0pt;font-family:Arial; mso-fareast-font-family:"Times New
     * Roman";mso-ansi-language:EN-US;mso-fareast-language:
     * EN-US;mso-bidi-language:AR-SA'>ProjectAuditIdSequenceName</span><!--EndFragment-->'
     * properties if exist and create IDGenerator instances from them. Assign the instances to the
     * corresponding member variable.</li>
     * </ul>
     * @param namespace The namespace to load connection setting.
     * @throws IllegalArgumentException if the input is null or empty string.
     * @throws ConfigurationException if error occurrs while loading configuration settings, or
     *             required configuration parameter is missing.
     * @throws PersistenceException if cannot initialize the connection to the database.
     */
    public InformixProjectPersistence(String namespace) throws ConfigurationException,
        PersistenceException {
        // your code here
    }

    /**
     * Create the project in the database using the given project instance. The project information
     * is stored to 'project' table, while its properties are stored in 'project_info' table. The
     * project's associating scorecards are stored in 'project_scorecard' table. For the project,
     * its properties and associating scorecards, the operator parameter is used as the
     * creation/modification user and the creation date and modification date will be the current
     * date time when the project is created.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>See the Component Specification, Algorithm section.</li>
     * </ul>
     * @param project The project instance to be created in the database.
     * @param operator The creation user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public void createProject(Project project, String operator) throws PersistenceException {
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
     * updated. An update reason is provided with this method. Update reason will be stored in the
     * persistence for future references.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>See the Component Specification, Algorithm section.</li>
     * </ul>
     * @param project The project instance to be updated into the database.
     * @param reason The update reason. It will be stored in the persistence for future references.
     * @param operator The modification user of this project.
     * @throws IllegalArgumentException if any input is null or the operator is empty string. Or
     *             project id is zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public void updateProject(Project project, String reason, String operator)
        throws PersistenceException {
        // your code here
    }

    /**
     * Retrieves the project instance from the persistence given its id. The project instance is
     * retrieved with its related items, such as properties and scorecards.
     * <p>
     * Implementation notes:
     * </p>
     * <ul>
     * <li>See the Component Specification, Algorithm section.</li>
     * </ul>
     * @return The project instance.
     * @param id The id of the project to be retrieved.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project getProject(long id) throws PersistenceException {
        return null;
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
     * @param ids ids The ids of the projects to be retrieved.
     * @return An array of project instances.
     * @throws IllegalArgumentException if the input id is less than or equal to zero.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public Project[] getProjects(long[] ids) throws PersistenceException {
        // your code here
        return null;
    }

    /**
     * Gets an array of all project types in the persistence. The project types are stored in
     * 'project_type_lu' table.
     * <p>
     * Implementation notes:
     * </p>
     * <li> SELECT project_type_id, name, description FROM project_type_lu </li>
     * <li> Populate an array of ProjectType and return</li>
     * <li> Wrap any error using PersistenceException</li>
     * @return An array of all project types in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectType[] getAllProjectTypes() throws PersistenceException {
        return new ProjectType[0];
    }

    /**
     * Gets an array of all project categories in the persistence. The project categories are stored
     * in 'project_category_lu' table.
     * <p>
     * Implementation notes:
     * </p>
     * <li> Get an array of all project types using getAllProjectTypes() method.</li>
     * <li> SELECT project_category_id, project_type_id, name, description FROM project_category_lu</li>
     * <li> Get the ProjectType instance from the array using project_type_id</li>
     * <li> Populate an array of ProjectCategory and return</li>
     * <li> Wrap any error using PersistenceException</li>
     * @return An array of all project categories in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectCategory[] getAllProjectCategories() throws PersistenceException {
        return new ProjectCategory[0];
    }

    /**
     * Gets an array of all project statuses in the persistence. The project statuses are stored in
     * 'project_status_lu' table.
     * <p>
     * Implementation notes:
     * </p>
     * <li> SELECT project_status_id, name, description FROM project_status_lu</li>
     * <li> Populate an array of ProjectStatus and return</li>
     * <li> Wrap any error using PersistenceException</li>
     * @return An array of all project statuses in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectStatus[] getAllProjectStatuses() throws PersistenceException {
        return new ProjectStatus[0];
    }

    /**
     * Gets an array of all project property type in the persistence. The project property types are
     * stored in 'project_info_type_lu' table.
     * <p>
     * Implementation notes:
     * </p>
     * <li> SELECT project_info_type_id, name, description FROM project_info_type_lu</li>
     * <li> Populate an array of ProjectPropertyType and return</li>
     * <li> Wrap any error using PersistenceException</li>
     * @return An array of all scorecard assignments in the persistence.
     * @throws PersistenceException if error occurred while accessing the database.
     */
    public ProjectPropertyType[] getAllProjectPropertyTypes() throws PersistenceException {
        return new ProjectPropertyType[0];
    }
}
