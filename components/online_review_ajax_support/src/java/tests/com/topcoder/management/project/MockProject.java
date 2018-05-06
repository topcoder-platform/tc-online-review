/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.project;

import java.util.HashMap;
import java.util.Map;

/**
 * Mock subclass of Project.
 *
 * @author assistant
 * @version 1.0
 */
public class MockProject extends Project {

    public MockProject(ProjectCategory arg0, ProjectStatus arg1) {
        super(arg0, arg1);
    }

    /**
     * The properties.
     */
    private Map props = new HashMap();

    /**
     * Represents the id.
     */
    private long id;

    /**
     * Get the id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Set the id.
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Get property.
     * @param name property name
     * @return value
     */
    public Object getProperty(String name) {
        return props.get(name);
    }

    /**
     * Set property.
     * @param name the name
     * @param value the value
     */
    public void setProperty(String name, Object value) {
        props.put(name, value);
    }
}
