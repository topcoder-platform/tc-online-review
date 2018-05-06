/*
 * Copyright (C) 2006, TopCoder Inc. All rights reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests.impl;

import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.impl.ActiveAutoPilotSource;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.search.builder.filter.Filter;

/**
 * <p>A subclass of <code>ActiveAutoPilotSource</code> class to be used to test the protected methods of the super
 * class. Overrides the protected methods declared by a super-class. The overridden methods are declared with package
 * private access so only the test cases could invoke them. The overridden methods simply call the corresponding method
 * of a super-class.
 *
 * @author isv
 * @version 1.0
 */
class ActiveAutoPilotSourceSubclass extends ActiveAutoPilotSource {

    /**
     * <p>Constructs new <code>ActiveAutoPilotSourceSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException if any error occurs instantiating the object factory or the project manager
     * instance
     */
    public ActiveAutoPilotSourceSubclass() throws ConfigurationException {
        super();
    }

    /**
     * <p>Constructs new <code>ActiveAutoPilotSourceSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param namespace the namespace to initialize object factory with
     * @param projectManagerKey the key defining the ProjectManager instance
     * @param activeStatusName A non-null string representing a project status of active
     * @param extProp A non-null string representing the extended property name for auto pilot switch
     * @param extPropVal A non-null string representing the extended property value for auto pilot switch
     * @throws IllegalArgumentException if any of the argument is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the project manager
     * instance
     */
    public ActiveAutoPilotSourceSubclass(String namespace, String projectManagerKey, String activeStatusName,
                                         String extProp, String extPropVal) throws ConfigurationException {
        super(namespace, projectManagerKey, activeStatusName, extProp, extPropVal);
    }

    /**
     * <p>Constructs new <code>ActiveAutoPilotSourceSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param projectManager the ProjectManager instance to use
     * @param activeStatusName A non-null string representing a project status of active
     * @param extProp A non-null string representing the extended property name for auto pilot switch
     * @param extPropVal A non-null string representing the extended property value for auto pilot switch
     * @throws IllegalArgumentException if any of the argument is null or the string arguments are empty (trimmed)
     */
    public ActiveAutoPilotSourceSubclass(ProjectManager projectManager, String activeStatusName, String extProp, String extPropVal) {
        super(projectManager, activeStatusName, extProp, extPropVal);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public ProjectManager getProjectManager() {
        return super.getProjectManager();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public String getActiveStatusName() {
        return super.getActiveStatusName();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public String getExtPropAutoPilotSwitch() {
        return super.getExtPropAutoPilotSwitch();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public String getExtPropAutoPilotSwitchValue() {
        return super.getExtPropAutoPilotSwitchValue();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @throws AutoPilotSourceException if an error occurs retrieving the project ids
     */
    public long[] getProjectIds() throws AutoPilotSourceException {
        return super.getProjectIds();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public Filter buildFilter() {
        return super.buildFilter();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param projects an array of Project[] whose id to return (could be null).
     */
    public long[] processProject(Project[] projects) {
        return super.processProject(projects);
    }
}
