/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.topcoder.management.phase.autopilot.logging.LogMessage;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * This is the main class which performs auto-pilot for projects. Auto-piloting a project means
 * automating project phases execution. There are two kinds of project phases execution:<br>
 * <br> - ending a project phase (if it's open and certain conditions are met)<br>
 * <br> - starting a project phase (if it's scheduled and certain conditions are met)<br>
 * This class delegates the project phase execution to ProjectPilot interface. The projects' ids to
 * auto-pilot can be supplied programmatically or automatically searched from all projects who meet
 * certain criteria. The task of searching projects is delegated to AutoPilotSource implementation.
 * Note, this class doesn't poll/execute phase change at certain intervals. See AutoPilotJob for
 * that.<br>
 * This class is immutable, however it's not guaranteed to be thread-safe. Thread-safety will depend
 * on the instance of AutoPilotSource & ProjectPilot. Calling advanceProjects from multiple thread
 * may confuse ProjectPilot because phase status may be changed from other thread. Multi-threaded
 * applications are advised to lock on the AutoPilot instance to ensure thread-safety.
 * </p>
 * @author sindu, abelli
 * @version 1.0.2
 */
public class AutoPilot {

    /**
     * <p>The log used by this class for logging errors and debug information.</p>
     */
    private final Log log;

    /**
     * The collection which hold ids of processing project.
     */
    private static final Set processingProjectIds = new HashSet();

    /**
     * Zero length <code>AutoPilotResult</code> array, which can be returned by
     * {@link #advanceProjects(long[], String)}.
     */
    private static final AutoPilotResult[] ZERO_AUTO_PILOT_RESULT_ARRAY = new AutoPilotResult[0];

    /**
     * <p>
     * Represents the AutoPilotSource instance that is used to retrieve a list of project ids to
     * auto-pilot. This variable is initially null, initialized in constructor using object factory
     * and immutable afterwards. It can be retrieved with the getter. It's used in
     * advanceProjects(String) to retrieve project ids which are to advance.
     * </p>
     */
    private final AutoPilotSource autoPilotSource;

    /**
     * <p>
     * Represents the ProjectPilot instance that is used to pilot a project (start/end project
     * phases). This variable is initially null, initialized in constructor using object factory and
     * immutable afterwards. It can be retrieved with the getter. It's used in advanceProject(long,
     * String) to advance the given project id phases.
     * </p>
     */
    private final ProjectPilot projectPilot;

    /**
     * <p>
     * Constructs a new instance of AutoPilot class. The object factory is initialized with this
     * class' fullname as its configuration namespace. Inside this namespace, properties with the
     * keys of AutoPilotSource and ProjectPilot's full names are used to retrieve the corresponding
     * instances.
     * </p>
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             auto pilot source or project pilot instance
     */
    public AutoPilot() throws ConfigurationException {
        this(AutoPilot.class.getName(), AutoPilotSource.class.getName(),
            ProjectPilot.class.getName());
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilot class using the given autoPilotSourceKey /
     * projectPilotKey to get AutoPilotSource/ProjectPilot instance with object factory. The object
     * factory is initialized with namespace as its configuration namespace. Inside this namespace,
     * properties with the keys of autoPilotSourceKey and projectPilotKey are used to retrieve the
     * corresponding instances.<br>
     * </p>
     * @param namespace the namespace to initialize object factory with
     * @param autoPilotSourceKey the key defining the AutoPilotSource instance
     * @param projectPilotKey the key defining the ProjectPilot instance
     * @throws IllegalArgumentException if any of the argument is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             auto pilot source or project pilot instance
     */
    public AutoPilot(String namespace, String autoPilotSourceKey, String projectPilotKey)
        throws ConfigurationException {
        // Check arguments.
        checkArguments(namespace, autoPilotSourceKey, projectPilotKey);
        this.log = LogManager.getLog("AutoPilot");
        
        log.log(Level.DEBUG, "Instantiate AutoPilot with namespace:" + namespace
        		+ " ,autoPilotSourceKey:" + autoPilotSourceKey + " and projectPilotKey:" + projectPilotKey);
        // Create object factory.
        ObjectFactory of;
        try {
            of = new ObjectFactory(new ConfigManagerSpecificationFactory(namespace));
        } catch (SpecificationConfigurationException e) {
        	log.log(Level.FATAL,
        			"fail to create object factory instance cause of specification configuration exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create object factory instance cause of specification configuration exception",
                e);
        } catch (IllegalReferenceException e) {
        	log.log(Level.FATAL,
        			"fail to create object factory instance cause of illegal reference exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create object factory instance cause of illegal reference exception", e);
        }

        log.log(Level.DEBUG, "create Objectfactory from namespace: " + namespace);
        
        // Create auto pilot source from object factory.
        Object objAutoPilotSource;
        try {
            objAutoPilotSource = of.createObject(autoPilotSourceKey);
            if (!AutoPilotSource.class.isInstance(objAutoPilotSource)) {
            	log.log(Level.FATAL, "fail to create AutoPilotSource object cause of bad type:" + objAutoPilotSource);
                throw new ConfigurationException(
                    "fail to create AutoPilotSource object cause of bad type:" + objAutoPilotSource);
            }
            log.log(Level.DEBUG, "create AutoPilotSource from objectfactory with autoPilotSourceKey:" + autoPilotSourceKey);
        } catch (InvalidClassSpecificationException e) {
        	log.log(Level.FATAL,
        			"fail to create auto pilot source cause of invalid class specification exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create auto pilot source cause of invalid class specification exception",
                e);
        }

        // Create project pilot from object factory.
        Object objProjectPilot;
        try {
            objProjectPilot = of.createObject(projectPilotKey);
            if (!ProjectPilot.class.isInstance(objProjectPilot)) {
            	log.log(Level.FATAL, "fail to create ProjectPilot object cause of bad type:" + objProjectPilot);
                throw new ConfigurationException(
                    "fail to create ProjectPilot object cause of bad type:" + objProjectPilot);
            }
            log.log(Level.DEBUG, "create ProjectPilot from objectfactory with projectPilotKey:" + projectPilotKey);
        } catch (InvalidClassSpecificationException e) {
        	log.log(Level.FATAL,
        			"fail to create project pilot cause of invalid class specification exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create project pilot cause of invalid class specification exception", e);
        }

        // Assign to fields.
        this.autoPilotSource = (AutoPilotSource) objAutoPilotSource;
        this.projectPilot = (ProjectPilot) objProjectPilot;
    }

    /**
     * <p>
     * Constructs a new instance of AutoPilot class using the given AutoPilotSource/ProjectPilot
     * instances.
     * </p>
     * @param autoPilotSource the AutoPilotSource instance to use
     * @param projectPilot the ProjectPilot instance to use
     * @param log the Log instance
     * @throws IllegalArgumentException if any of the parameter is null
     */
    public AutoPilot(AutoPilotSource autoPilotSource, ProjectPilot projectPilot, Log log) {
        // Check arguments.
        if (null == autoPilotSource) {
            throw new IllegalArgumentException("autoPilotSource cannot be null");
        }
        if (null == projectPilot) {
            throw new IllegalArgumentException("projectPilot cannot be null");
        }
        if (null == log) {
            throw new IllegalArgumentException("log cannot be null");
        }

        this.log = log;
        this.autoPilotSource = autoPilotSource;
        this.projectPilot = projectPilot;
        log.log(Level.DEBUG, "Instantiate AutoPilot with AutoPilot and ProjectPilot");
    }

    /**
     * <p>
     * Check arguments for {@link #AutoPilot(String, String, String)}.
     * </p>
     * @param namespace namespace for the constructor.
     * @param autoPilotSourceKey autoPilotSourceKey for the constructor.
     * @param projectPilotKey projectPilotKey for the constructor.
     * @throws IllegalArgumentException - if any of the argument is null or empty (trimmed) string
     */
    private void checkArguments(String namespace, String autoPilotSourceKey, String projectPilotKey) {
        if (null == namespace) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (namespace.trim().length() < 1) {
            throw new IllegalArgumentException("namespace cannot be empty");
        }
        if (null == autoPilotSourceKey) {
            throw new IllegalArgumentException("autoPilotSourceKey cannot be null");
        }
        if (autoPilotSourceKey.trim().length() < 1) {
            throw new IllegalArgumentException("autoPilotSourceKey cannot be empty");
        }
        if (null == projectPilotKey) {
            throw new IllegalArgumentException("projectPilotKey cannot be null");
        }
        if (projectPilotKey.trim().length() < 1) {
            throw new IllegalArgumentException("projectPilotKey cannot be empty");
        }
    }

    /**
     * <p>
     * Return the auto pilot source instance used by this class.
     * </p>
     * @return the auto pilot source instance used by this class
     */
    protected AutoPilotSource getAutoPilotSource() {
        return autoPilotSource;
    }

    /**
     * <p>
     * Return the project pilot instance used by this class.
     * </p>
     * @return the project pilot instance used by this class
     */
    protected ProjectPilot getProjectPilot() {
        return projectPilot;
    }

    /**
     * <p>
     * The main method which will retrieve all projects to auto-pilot using AutoPilotSource. Each
     * project is then advanced using ProjectPilot. This is repeated until no more phase changes can
     * be made for all projects.
     * </p>
     * @param operator the operator (used for auditing)
     * @return an array of AutoPilotResult representing result of auto-pilot (never null, but can be
     *         empty)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string
     * @throws AutoPilotSourceException if any error occurs retrieving project ids from
     *             AutoPilotSource
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] advanceProjects(String operator) throws AutoPilotSourceException,
        PhaseOperationException {
        // Check arguments.
        if (null == operator) {
            throw new IllegalArgumentException("operator cannot be null");
        }
        if (operator.trim().length() < 1) {
            throw new IllegalArgumentException("operator cannot be empty");
        }

        long[] projIds = autoPilotSource.getProjectIds();
        return advanceProjects(projIds, operator);
    }

    /**
     * <p>
     * Another convenient method to auto-pilot given project id list. Each project is advanced using
     * ProjectPilot. This is repeated until no more phase changes can be made for all projects.
     * </p>
     * @param projectId a list of project id to auto-pilot
     * @param operator the operator (used for auditing)
     * @return an array of AutoPilotResult representing result of auto-pilot (never null, but can be
     *         empty)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string or project id
     *             is null
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult[] advanceProjects(long[] projectId, String operator) throws PhaseOperationException {
        // Check arguments.
        if (null == operator) {
            throw new IllegalArgumentException("operator cannot be null");
        }
        if (operator.trim().length() < 1) {
            throw new IllegalArgumentException("operator cannot be empty");
        }
        if (null == projectId) {
            throw new IllegalArgumentException("projectId cannot be null");
        }

        if (projectId.length < 1) {
            return ZERO_AUTO_PILOT_RESULT_ARRAY;
        }
        log.log(Level.DEBUG, new LogMessage(null, operator, "Checking active projects: " + getIdString(projectId)));

        // Map key is Long (project id). Map value is AutoPilotResult instance.
        Map resMap = new HashMap();
        for (int i = 0; i < projectId.length; i++) {
            AutoPilotResult result = null;
            Long longProjectId = new Long(projectId[i]);

            // Check if the project is processing by another thread
            synchronized (processingProjectIds) {
                if (processingProjectIds.contains(longProjectId)) {
                    continue;
                } else {
                    processingProjectIds.add(longProjectId);
                }
            }

            try {
                result = advanceProject(projectId[i], operator);
                // store/aggregate into Map
                if (resMap.containsKey(longProjectId)) {
                    // Aggregate the result only if at least one of counters > 0.
                    if (result.getPhaseEndedCount() > 0 || result.getPhaseStartedCount() > 0) {
                        ((AutoPilotResult) resMap.get(longProjectId)).aggregate(result);
                    }
                } else {
                    resMap.put(longProjectId, result);
                }
            } finally {
                // Make sure this project can be processed by next thread
                synchronized (processingProjectIds) {
                    processingProjectIds.remove(longProjectId);
                }
            }
        }

        return (AutoPilotResult[]) resMap.values().toArray(new AutoPilotResult[resMap.size()]);
    }

    /**
     * <p>
     * Another convenient method to auto-pilot a given project id. The project is advanced using
     * ProjectPilot.
     * </p>
     * @param projectId the project id to auto-pilot
     * @param operator the operator (used for auditing)
     * @return AutoPilotResult representing result of auto-pilot (never null)
     * @throws IllegalArgumentException if operator is null or empty (trimmed) string
     * @throws PhaseOperationException if any error occurs while ending/starting a phase
     */
    public AutoPilotResult advanceProject(long projectId, String operator) throws PhaseOperationException {
    	log.log(Level.DEBUG, new LogMessage(new Long(projectId), operator, "Checking project phases."));
        return projectPilot.advancePhases(projectId, operator);
    }
    /**
     * Get id string spereated by comma for the ids.
     * @param ids the id array
     * @return string seperated by comma
     */
	private String getIdString(long[] ids) {
		if (ids == null || ids.length == 0) {
			return "";
		}
		StringBuffer idString = new StringBuffer();
        for(int i = 0 ; i < ids.length; i++) {
        	idString.append(',').append(ids[i]);
        }
		return idString.substring(1);
	}
}

