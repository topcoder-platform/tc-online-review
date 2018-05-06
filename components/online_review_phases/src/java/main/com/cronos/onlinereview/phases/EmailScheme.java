/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class represents email options. It is a container for the set of options related to email to be send, such as
 * subject, from address, template filename and whether the email is to be sent or not. There is no
 * validation performed in this class.
 * </p>
 *
 * <p>
 * Thread safety: This class is not thread safe as it is mutable.
 * </p>
 *
 * @author VolodymyrK
 * @version 1.6.1
 *
 * @since 1.6.1
 */
public class EmailScheme {
    /**
     * <p>
     * Represents the roles associated with the email scheme. This field is initialized to the empty list.
     * It has getter/setter.
     * </p>
     */
    private List<String> roles = new ArrayList<String>();

    /**
     * <p>
     * Represents the project type IDs associated with the email scheme. This field is initialized to the empty list.
     * It has getter/setter.
     * </p>
     */
    private List<String> projectTypes;

    /**
     * <p>
     * Represents the project category IDs associated with the email scheme. This field is initialized to the empty list.
     * It has getter/setter.
     * </p>
     */
    private List<String> projectCategories;

    /**
     * <p>
     * Represents the priority of the email options. Default value is 0. It has getter/setter.
     * </p>
     */
    private int priority = 0;

    /**
     * <p>
     * Represents the email options for the phase start. Default value is null. It has getter/setter.
     * </p>
     */
    private EmailOptions startEmailOptions = null;

    /**
     * <p>
     * Represents the email options for the phase end. Default value is null. It has getter/setter.
     * </p>
     */
    private EmailOptions endEmailOptions = null;

    /**
     * <p>
     * Default empty Constructor.
     * </p>
     */
    public EmailScheme() {
        // By default all project types are included.
        projectTypes = new ArrayList<String>();
        projectTypes.add("*");

        // By default all project categories are included.
        projectCategories = new ArrayList<String>();
        projectCategories.add("*");
    }

    /**
     * Returns the value of roles.
     *
     * @return the roles
     */
    public List<String> getRoles() {
        return roles;
    }

    /**
     * Set the value to roles field.
     *
     * @param roles the roles to set
     */
    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    /**
     * Returns the value of projectTypes.
     *
     * @return the projectTypes
     */
    public List<String> getProjectTypes() {
        return projectTypes;
    }

    /**
     * Set the value to projectTypes field.
     *
     * @param projectTypes the projectTypes to set
     */
    public void setProjectTypes(List<String> projectTypes) {
        this.projectTypes = projectTypes;
    }

    /**
     * Returns the value of projectCategories.
     *
     * @return the projectCategories
     */
    public List<String> getProjectCategories() {
        return projectCategories;
    }

    /**
     * Set the value to projectCategories field.
     *
     * @param projectCategories the projectCategories to set
     */
    public void setProjectCategories(List<String> projectCategories) {
        this.projectCategories = projectCategories;
    }

    /**
     * Returns the value of priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the value to priority field.
     *
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Returns the value of startEmailOptions.
     *
     * @return the startEmailOptions
     */
    public EmailOptions getStartEmailOptions() {
        return startEmailOptions;
    }

    /**
     * Set the value to startEmailOptions field.
     *
     * @param startEmailOptions the startEmailOptions to set
     */
    public void setStartEmailOptions(EmailOptions startEmailOptions) {
        this.startEmailOptions = startEmailOptions;
    }

    /**
     * Returns the value of endEmailOptions.
     *
     * @return the endEmailOptions
     */
    public EmailOptions getEndEmailOptions() {
        return endEmailOptions;
    }

    /**
     * Set the value to endEmailOptions field.
     *
     * @param endEmailOptions the endEmailOptions to set
     */
    public void setEndEmailOptions(EmailOptions endEmailOptions) {
        this.endEmailOptions = endEmailOptions;
    }
}
