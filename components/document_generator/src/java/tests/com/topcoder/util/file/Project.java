/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This is a data class used to populate data for <code>NodeList</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class Project {
    /**
     * <p>
     * Represents the project type.
     * </p>
     */
    private String projectType;

    /**
     * <p>
     * Represents the project dependencies.
     * </p>
     */
    private final List dependencies = new ArrayList();

    /**
     * <p>
     * Gets the project type.
     * </p>
     *
     * @return the project type.
     */
    public String getProjectType() {
        return projectType;
    }

    /**
     * <p>
     * Sets the project type.
     * </p>
     *
     * @param projectType the new project type
     */
    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    /**
     * <p>
     * Gets the dependencies for the project.
     * </p>
     *
     * @return the dependencies for the project.
     */
    public Component[] getDependencies() {
        return (Component[]) dependencies.toArray(new Component[dependencies.size()]);
    }

    /**
     * <p>
     * Adds a dependency for the project.
     * </p>
     *
     * @param component a dependency of the project
     */
    public void addDependency(Component component) {
        if (component != null) {
            this.dependencies.add(component);
        }
    }

    /**
     * <p>
     * Sets the dependencies for the project.
     * </p>
     *
     * @param components the dependencies of the project
     */
    public void setDependencies(Component[] components) {
        dependencies.clear();

        if (components != null) {
            for (int i = 0; i < components.length; i++) {
                addDependency(components[i]);
            }
        }
    }
}
