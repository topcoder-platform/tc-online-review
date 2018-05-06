/*
 * Copyright (C) 2006 - 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.io.Serializable;

/**
 * <p>
 * This class represents a project category from the persistence. Each project category must belong to a project type.
 * Project type are stored in 'project_type_lu' table, project category in 'project_category_lu'. A project category
 * instance contains id, name and description and a reference to project type. This class is used in Project class to
 * specify the project category of a project. This class implements Serializable interface to support serialization.
 * </p>
 * <p>
 * Thread safety: This class is not thread safe.
 * </p>
 *
 * @author tuenm, iamajia, flytoj2ee, TCSDEVELOPER
 * @version 1.2.2
 * @since 1.0
 */
@SuppressWarnings("serial")
public class ProjectCategory implements Serializable {
    /**
     * Represents Web Page Design project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory WEB_DESIGN = new ProjectCategory(17, "Web Design", ProjectType.STUDIO);

    /**
     * Represents Logo Design project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory LOGO_DESIGN = new ProjectCategory(20, "Logo Design", ProjectType.STUDIO);

    /**
     * Represents Banners and Icons project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory BANNERS_ICONS = new ProjectCategory(16, "Banners/Icons", ProjectType.STUDIO);

    /**
     * Represents Application Front End Design project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory APPLICATION_FRONT_END_DESIGN = new ProjectCategory(19,
        "Application Front End Design", ProjectType.STUDIO);

    /**
     * Represents Widget or Mobile Screen project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory WIDGET_OR_MOBILE_SCREEN_DESIGN = new ProjectCategory(30,
        "Windget or Mobile Screen Design", ProjectType.STUDIO);

    /**
     * Represents Front End Flash project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory FRONT_END_FLASH
        = new ProjectCategory(31, "Front End Flash", ProjectType.STUDIO);

    /**
     * Represents Print or Presentation project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory PRINT_OR_PRESENTATION = new ProjectCategory(21, "Print/Presentation",
        ProjectType.STUDIO);

    /**
     * Represents Other project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory OTHER = new ProjectCategory(34, "Other", ProjectType.STUDIO);

    /**
     * Represents Wireframes project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory WIREFRAMES = new ProjectCategory(18, "Wireframes", ProjectType.STUDIO);

    /**
     * Represents Idea Generation project category.
     *
     * @since 1.2
     */
    public static final ProjectCategory IDEA_GENERATION
        = new ProjectCategory(22, "Idea Generation", ProjectType.STUDIO);
    /**
     * Represents spec review category.
     *
     * @since 1.2
     */
    public static final long PROJECT_CATEGORY_SPEC_REVIEW = 27;
    /**
     * Represents copilot posting category.
     *
     * @since 1.2
     */
    public static final ProjectCategory COPILOT_POSTING = new ProjectCategory(29, "Copilot Posting", ProjectType.APPLICATION);

    /**
     * Represents the id of this instance. Only values greater than zero is allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private long id = 0;

    /**
     * Represents the name of this instance. Null or empty values are not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private String name = null;

    /**
     * Represents the description of this instance. Null value is not allowed. This variable is initialized in the
     * constructor and can be accessed in the corresponding getter/setter method.
     */
    private String description = null;

    /**
     * The project type instance associated with this instance. Null value is not allowed. This variable is initialized
     * in the constructor and can be accessed in the corresponding getter/setter method.
     */
    private ProjectType projectType = null;

    /**
     * Create a new ProjectCategory instance with the given id and name. The two fields are required for a this instance
     * to be persisted.
     *
     * @param id
     *            The project category id.
     * @param name
     *            The project category name.
     * @param projectType
     *            The project type of this instance.
     * @throws IllegalArgumentException
     *             If id is less than or equals to zero, any parameter is null or name is empty string.
     */
    public ProjectCategory(long id, String name, ProjectType projectType) {
        this(id, name, "", projectType);
    }

    /**
     * Create a new ProjectCategory instance with the given id, name and description. The two first fields are required
     * for a this instance to be persisted.
     *
     * @param id
     *            The project category id.
     * @param name
     *            The project category name.
     * @param description
     *            The project category description.
     * @param projectType
     *            The project type of this instance.
     * @throws IllegalArgumentException
     *             If id is less than or equals to zero, any parameter is null or name is empty string.
     */
    public ProjectCategory(long id, String name, String description, ProjectType projectType) {
        setId(id);
        setName(name);
        setDescription(description);
        setProjectType(projectType);
    }

    /**
     * Sets the id for this project category instance. Only positive values are allowed.
     *
     * @param id
     *            The id of this project category instance.
     * @throws IllegalArgumentException
     *             If project category id is less than or equals to zero.
     */
    public void setId(long id) {
        Helper.checkNumberPositive(id, "id");
        this.id = id;
    }

    /**
     * Gets the id of this project category instance.
     *
     * @return the id of this project category instance.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the name for this project category instance. Null or empty values are not allowed.
     *
     * @param name
     *            The name of this project category instance.
     * @throws IllegalArgumentException
     *             If project category name is null or empty string.
     */
    public void setName(String name) {
        Helper.checkStringNotNullOrEmpty(name, "name");
        this.name = name;
    }

    /**
     * Gets the name of this project category instance.
     *
     * @return the name of this project category instance.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the description for this project category instance. Null value are not allowed.
     *
     * @param description
     *            The description of this project category instance.
     * @throws IllegalArgumentException
     *             If project category description is null.
     */
    public void setDescription(String description) {
        Helper.checkObjectNotNull(description, "description");
        this.description = description;
    }

    /**
     * Gets the description of this project category instance.
     *
     * @return the description of this project category instance.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the project type for this project category instance. Null value is not allowed.
     *
     * @param projectType
     *            The project type instance to set.
     * @throws IllegalArgumentException
     *             If input is null.
     */
    public void setProjectType(ProjectType projectType) {
        Helper.checkObjectNotNull(projectType, "projectType");
        this.projectType = projectType;
    }

    /**
     * Gets the project type of this project category instance.
     *
     * @return The project type of this project category instance.
     */
    public ProjectType getProjectType() {
        return projectType;
    }
}
