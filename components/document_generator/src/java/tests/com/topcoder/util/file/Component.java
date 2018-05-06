/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.file;

/**
 * <p>
 * This is a data class used to populate data for <code>NodeList</code>.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 2.1
 */
public class Component {
    /**
     * <p>
     * Represents the component name.
     * </p>
     */
    private String name;

    /**
     * <p>
     * Represents the component version.
     * </p>
     */
    private String version;

    /**
     * <p>
     * Represents the component long name.
     * </p>
     */
    private String longName;

    /**
     * <p>
     * Sets the component name.
     * </p>
     *
     * @param name the new component name
     */
    public void setComponentName(String name) {
        this.name = name;
    }

    /**
     * <p>
     * Sets the component long name.
     * </p>
     *
     * @param longName the new component long name
     */
    public void setComponentLongName(String longName) {
        this.longName = longName;
    }

    /**
     * <p>
     * Sets the component version.
     * </p>
     *
     * @param version the new component version
     */
    public void setComponentVersion(String version) {
        this.version = version;
    }

    /**
     * <p>
     * Gets the component name.
     * </p>
     *
     * @return the component name
     */
    public String getComponentName() {
        return name;
    }

    /**
     * <p>
     * Gets the component long name.
     * </p>
     *
     * @return the component long name
     */
    public String getComponentLongName() {
        return longName;
    }

    /**
     * <p>
     * Gets the component version.
     * </p>
     *
     * @return the component version
     */
    public String getComponentVersion() {
        return version;
    }

}
