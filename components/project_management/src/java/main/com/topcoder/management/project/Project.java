/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * <p>
 * This class represents a project from the persistence. Each project must belong to a project category and must have a
 * status. Project also have contains some properties. Projects are stored in 'project' table, project category in
 * 'project_category_lu' table, project status in 'project_status_lu' table. project properties are stored in
 * 'project_info' table. This class is used by ProjectPersistence implementors to store projects in the persistence.
 * </p>
 * <p>
 * This class implements Serializable interface to support serialization.
 * </p>
 * <p>
 * Updated in version 1.2: Added prizes / projectStudioSpecification / projectFileTypes attributes.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author tuenm, iamajia, flytoj2ee, VolodymyrK
 * @version 1.2.2
 * @since 1.0
 */
@SuppressWarnings("serial")
public class Project extends AuditableObject implements Serializable {

    /**
     * Represents the id of this instance. Only values greater than or equal to zero is allowed. This variable is
     * initialized in the constructor and can be accessed in the corresponding getter/setter method.
     */
    private long id = 0;

    /**
     * Represents the project category of this instance. Null values are not allowed. This variable is initialized in
     * the constructor and can be accessed in the corresponding getter/setter method.
     */
    private ProjectCategory projectCategory = null;

    /**
     * Represents the project status of this instance. Null values are not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private ProjectStatus projectStatus = null;

    /**
     * Represents the properties of this instance. Null values are not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter/getAll method. The map key is property name
     * and map value is property value. Key is string type and value is Object type.
     */
    @SuppressWarnings("unchecked")
    private Map properties = new HashMap();

    /**
     * Represents the prizes of Project. The default value is null. It's changeable. It could not contain null. It's
     * accessed in setter and getter.
     *
     * @since 1.2
     */
    private List<Prize> prizes;

    /**
     * Represents the projectStudioSpecification of Project. The default value is null. It's changeable. It could not be
     * null if project type is studio. It's accessed in setter and getter.
     *
     * @since 1.2
     */
    private ProjectStudioSpecification projectStudioSpecification;

    /**
     * Represents the projectFileTypes of Project. The default value is null. It's changeable. It could not contain
     * null. It's accessed in setter and getter.
     *
     * @since 1.2
     */
    private List<FileType> projectFileTypes;

    /**
     * Represents the direct project id of Project. The default value is null. It's changeable. It's accessed in setter
     * and getter.
     *
     * @since 1.2
     */
    private long tcDirectProjectId;

    /**
     * Represents the name of the associated cockpit (tc direct) project. The default value is null. It's changeable. It's accessed in setter
     * and getter.
     */
    private String tcDirectProjectName;

    /**
     * <p>
     * Create a new Project instance with the given project type and project status. These fields are required for a
     * project to be created. Project id will be zero which indicates that the project instance is not yet created in
     * the database.
     * </p>
     *
     * @param projectCategory
     *            The project category instance of this project.
     * @param projectStatus
     *            The project status instance of this project.
     * @throws IllegalArgumentException
     *             If any parameter is null.
     */
    public Project(ProjectCategory projectCategory, ProjectStatus projectStatus) {
        this(0, projectCategory, projectStatus);
    }

    /**
     * Create a new Project instance with the given project id, project type and project status. This method is supposed
     * to use by persistence implementation to load project from the persistence when the project id is already set.
     *
     * @param projectId
     *            The project id.
     * @param projectCategory
     *            The project category instance of this project.
     * @param projectStatus
     *            The project status instance of this project.
     * @throws IllegalArgumentException
     *             If id is less than zero, or any parameter is null.
     */
    @SuppressWarnings("unchecked")
    public Project(long projectId, ProjectCategory projectCategory, ProjectStatus projectStatus) {
        this(projectId, projectCategory, projectStatus, new HashMap());
    }

    /**
     * Create a new Project instance with the given project id, project type id, project status id and the associated
     * properties. The input properties map should contains String/Object as key/value pairs. This method is supposed to
     * use by persistence implementation to load project from the persistence when the project id is already set.
     *
     * @param projectId
     *            The project id.
     * @param projectCategory
     *            The project category instance of this project.
     * @param projectStatus
     *            The project status instance of this project.
     * @param properties
     *            A map of properties of this project.
     * @throws IllegalArgumentException
     *             If id is less than zero, or any parameter is null. Or if in the 'properties' map, the key/value type
     *             is not String/Object, Or key is null or empty, Object is null.
     */
    @SuppressWarnings("unchecked")
    public Project(long projectId, ProjectCategory projectCategory, ProjectStatus projectStatus, Map properties) {
        if (projectId < 0) {
            throw new IllegalArgumentException("id can not less than zero.");
        }
        this.id = projectId;
        setProjectCategory(projectCategory);
        setProjectStatus(projectStatus);
        Helper.checkObjectNotNull(properties, "properties");
        // check all entry in properties.
        for (Object item : properties.entrySet()) {
            Entry entry = (Entry) item;
            if (!(entry.getKey() instanceof String)) {
                throw new IllegalArgumentException("properties contains some key that is not String.");
            }
            Helper.checkStringNotNullOrEmpty((String) entry.getKey(), "properties's key");
            Helper.checkObjectNotNull(entry.getValue(), "properties's value");
        }
        this.properties = new HashMap(properties);
    }

    /**
     * Sets the id for this project instance. Only positive values are allowed.
     *
     * @param id
     *            The id of this project instance.
     * @throws IllegalArgumentException
     *             If project id is less than or equals to zero.
     */
    public void setId(long id) {
        Helper.checkNumberPositive(id, "id");
        this.id = id;
    }

    /**
     * Gets the id of this project instance.
     *
     * @return the id of this project instance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the project status for this project instance. Null value is not allowed.
     *
     * @param projectStatus
     *            The project status instance to set.
     * @throws IllegalArgumentException
     *             If input is null.
     */
    public void setProjectStatus(ProjectStatus projectStatus) {
        Helper.checkObjectNotNull(projectStatus, "projectStatus");
        this.projectStatus = projectStatus;
    }

    /**
     * Gets the project status of this project instance.
     *
     * @return The project status of this project instance.
     */
    public ProjectStatus getProjectStatus() {
        return projectStatus;
    }

    /**
     * Sets the project category for this project instance. Null value is not allowed.
     *
     * @param projectCategory
     *            The project category instance to set.
     * @throws IllegalArgumentException
     *             If input is null.
     */
    public void setProjectCategory(ProjectCategory projectCategory) {
        Helper.checkObjectNotNull(projectCategory, "projectCategory");
        this.projectCategory = projectCategory;
    }

    /**
     * Gets the project category of this project instance.
     *
     * @return The project category of this project instance.
     */
    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    /**
     * Sets a property for this project instance. If the property name already exist, its value will be updated.
     * Otherwise, the property will be added to the project instance. If value parameter is null, the property will be
     * removed from the project instance. Value of the property will be saved as string in the persistence using
     * toString() method. Project property value is stored in 'project_info' table, while its name is stored in
     * 'project_info_type_lu' table.
     *
     * @param name
     *            The property name.
     * @param value
     *            The property value.
     * @throws IllegalArgumentException
     *             If name is null or empty string.
     */
    @SuppressWarnings("unchecked")
    public void setProperty(String name, Object value) {
        Helper.checkStringNotNullOrEmpty(name, "name");
        if (value == null) {
            properties.remove(name);
        } else {
            properties.put(name, value);
        }
    }

    /**
     * Gets a property for this project instance. If the property name does not exist in this instance, null value is
     * returned.
     *
     * @return The property value, or null if the property does not exist.
     * @param name
     *            The property name.
     * @throws IllegalArgumentException
     *             If name is null or empty string.
     */
    public Object getProperty(String name) {
        Helper.checkStringNotNullOrEmpty(name, "name");
        return properties.get(name);
    }

    /**
     * Gets a map of property name/value pairs. If there is no property in this project instance, an empty map is
     * returned.
     *
     * @return A map of property name/value pairs. or an empty map if there is no property in this project instance.
     */
    @SuppressWarnings("unchecked")
    public Map getAllProperties() {
        return new HashMap(properties);
    }

    /**
     * Returns the value of prizes attribute.
     *
     * @return the value of prizes.
     * @since 1.2
     */
    public List<Prize> getPrizes() {
        return this.prizes;
    }

    /**
     * Sets the given value to prizes attribute.
     *
     * @param prizes
     *            the given value to set.
     * @throws IllegalArgumentException if the parameter contains null
     * @since 1.2
     */
    public void setPrizes(List<Prize> prizes) {
        if (null != prizes) {
            if (prizes.contains(null)) {
                throw new IllegalArgumentException("The prizes can not contain null.");
            }
        }
        this.prizes = prizes;
    }

    /**
     * Returns the value of projectStudioSpecification attribute.
     *
     * @return the value of projectStudioSpecification.
     * @since 1.2
     */
    public ProjectStudioSpecification getProjectStudioSpecification() {
        return this.projectStudioSpecification;
    }

    /**
     * Sets the given value to projectStudioSpecification attribute.
     *
     * @param projectStudioSpecification
     *            the given value to set.
     * @throws IllegalArgumentException
     *             if the project type is Studio and the parameter is null.
     * @since 1.2
     */
    public void setProjectStudioSpecification(ProjectStudioSpecification projectStudioSpecification) {
        if (projectCategory != null && projectCategory.getProjectType() == ProjectType.STUDIO) {
            Helper.checkObjectNotNull(projectStudioSpecification, "projectStudioSpecification");
        }
        this.projectStudioSpecification = projectStudioSpecification;
    }

    /**
     * Returns the value of projectFileTypes attribute.
     *
     * @return the value of projectFileTypes.
     * @since 1.2
     */
    public List<FileType> getProjectFileTypes() {
        return this.projectFileTypes;
    }

    /**
     * Sets the given value to projectFileTypes attribute.
     *
     * @param projectFileTypes
     *            the given value to set.
     * @throws IllegalArgumentException if the parameter contains null
     * @since 1.2
     */
    public void setProjectFileTypes(List<FileType> projectFileTypes) {
        if (null != projectFileTypes) {
            if (projectFileTypes.contains(null)) {
                throw new IllegalArgumentException("The projectFileTypes can not contain null.");
            }
        }
        this.projectFileTypes = projectFileTypes;
    }

    /**
     * Returns the value of tcDirectProjectId attribute.
     *
     * @return the direct project id
     * @since 1.2
     */
    public long getTcDirectProjectId() {
        return this.tcDirectProjectId;
    }

    /**
     * Sets the given value to tcDirectProjectId attribute.
     *
     * @param tcDirectProjectId
     *            the directProjectId to set
     * @since 1.2
     */
    public void setTcDirectProjectId(long tcDirectProjectId) {
        this.tcDirectProjectId = tcDirectProjectId;
    }

    /**
     * Returns the value of tcDirectProjectName attribute.
     *
     * @return the direct project name
     */
    public String getTcDirectProjectName() {
        return this.tcDirectProjectName;
    }

    /**
     * Sets the given value to tcDirectProjectName attribute.
     *
     * @param tcDirectProjectName
     *            the tcDirectProjectName to set
     */
    public void setTcDirectProjectName(String tcDirectProjectName) {
        this.tcDirectProjectName = tcDirectProjectName;
    }
}
