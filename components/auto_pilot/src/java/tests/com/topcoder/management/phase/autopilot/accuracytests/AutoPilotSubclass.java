/*
 * Copyright (C) 2006, TopCoder Inc. All rights reserved.
 */

package com.topcoder.management.phase.autopilot.accuracytests;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotResult;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.PhaseOperationException;
import com.topcoder.management.phase.autopilot.ProjectPilot;

/**
 * <p>A subclass of <code>AutoPilot</code> class to be used to test the protected methods of the super class. Overrides
 * the protected methods declared by a super-class. The overridden methods are declared with package private access so
 * only the test cases could invoke them. The overridden methods simply call the corresponding method of a super-class.
 *
 * @author isv
 * @version 1.0
 */
class AutoPilotSubclass extends AutoPilot {

    /**
     * <p>Constructs new <code>AutoPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @throws ConfigurationException if any error occurs instantiating the object factory or the auto pilot source or
     * project pilot instance
     */
    public AutoPilotSubclass() throws ConfigurationException {
        super();
    }

    /**
     * <p>Constructs new <code>AutoPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param namespace the namespace to initialize object factory with
     * @param autoPilotSourceKey the key defining the AutoPilotSource instance
     * @param projectPilotKey the key defining the ProjectPilot instance
     * @throws IllegalArgumentException if any of the argument is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the auto pilot source or
     * project pilot instance
     */
    public AutoPilotSubclass(String namespace, String autoPilotSourceKey, String projectPilotKey)
        throws ConfigurationException {
        super(namespace, autoPilotSourceKey, projectPilotKey);
    }

    /**
     * <p>Constructs new <code>AutoPilotSubclass</code> instance. Nothing special occurs here.</p>
     *
     * @param autoPilotSource the AutoPilotSource instance to use
     * @param projectPilot the ProjectPilot instance to use
     * @throws IllegalArgumentException if any of the parameter is null
     */
    public AutoPilotSubclass(AutoPilotSource autoPilotSource, ProjectPilot projectPilot) {
        super(autoPilotSource, projectPilot);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public AutoPilotSource getAutoPilotSource() {
        return super.getAutoPilotSource();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     */
    public ProjectPilot getProjectPilot() {
        return super.getProjectPilot();
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param operator the operator (used for auditing)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string
     * @throws AutoPilotSourceException if any error occurs retrieving project ids from AutoPilotSource
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] advanceProjects(String operator) throws AutoPilotSourceException, PhaseOperationException {
        return super.advanceProjects(operator);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param projectId a list of project id to auto-pilot
     * @param operator the operator (used for auditing)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string or projectid is null
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] advanceProjects(long[] projectId, String operator) throws PhaseOperationException {
        return super.advanceProjects(projectId, operator);
    }

    /**
     * <p>Calls corresponding method of a super class providing the specified argument. Nothing special occurs
     * here.</p>
     *
     * @param projectId the project id to auto-pilot
     * @param operator the operator (used for auditing)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult advanceProject(long projectId, String operator) throws PhaseOperationException {
        return super.advanceProject(projectId, operator);
    }

}
