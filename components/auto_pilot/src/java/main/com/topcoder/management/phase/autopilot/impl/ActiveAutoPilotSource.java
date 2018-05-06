/*
 * Copyright (C) 2006-2012 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase.autopilot.impl;

import com.topcoder.management.phase.autopilot.AutoPilot;
import com.topcoder.management.phase.autopilot.AutoPilotSource;
import com.topcoder.management.phase.autopilot.AutoPilotSourceException;
import com.topcoder.management.phase.autopilot.ConfigurationException;
import com.topcoder.management.phase.autopilot.logging.LogMessage;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectFilterUtility;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.search.builder.filter.Filter;
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
 * An implementation of AutoPilotSource that retrieves all currently active projects that have auto
 * pilot switch on in its extended property. It uses Project Management component to search all
 * projects which are active and have auto pilot switch on in its extended property.
 * buildFilter/processProject are protected so that it can be extended/customized easily in the
 * future.
 * </p>
 * <p>
 * This class is immutable, however it's not guaranteed to be thread-safe because the underlying
 * ProjectManager instance that is used by this class may not be thread-safe. In a multiple thread
 * situation, application is advised to synchronize on the AutoPilot instance to ensure that only a
 * single thread is retrieving/advancing project phases at one time.
 * </p>
 * @author sindu, abelli
 * @version 1.0.2
 */
public class ActiveAutoPilotSource implements AutoPilotSource {

    /**
     * <p>
     * Represents the default project status name for an active status. Referenced in ctor().
     * </p>
     */
    public static final String DEFAULT_ACTIVE_STATUS_NAME = "Active";

    /**
     * <p>
     * Represents the default extended property name for the auto pilot switch. Referenced in
     * ctor().
     * </p>
     */
    public static final String DEFAULT_EXTPROP_AUTOPILOTSWITCH = "Autopilot Option";

    /**
     * <p>
     * Represents the default extended property value for the auto pilot switch. Referenced in
     * ctor().
     * </p>
     */
    public static final String DEFAULT_EXTPROP_AUTOPILOTSWITCH_VALUE = "On";

    /**
     * Zero length <code>long</code> array, which can be returned by {@link #processProject(Project[])}.
     */
    private static final long[] ZERO_LONG_ARRAY = new long[0];

    /**
     * <p>The log used by this class for logging errors and debug information.</p>
     */
    private final Log log;
    
    /**
     * <p>
     * Represents the ProjectManager instance that is used to search project based on its status and
     * extended property. This variable is initially null, initialized in constructor using object
     * factory and immutable afterwards. It is referenced by getProjectId. It can be retrieved with
     * the getter.
     * </p>
     */
    private final ProjectManager projectManager;

    /**
     * <p>
     * Represents the project status name of an active status. This will be used to compare a
     * project status name. This variable is initially null, set in the constructor and immutable
     * afterwards. It is referenced by getProjectId. It can be retrieved with the getter.
     * </p>
     */
    private final String activeStatusName;

    /**
     * <p>
     * Represents the extended property name for auto pilot switch. This will be used to search
     * projects. This variable is initially null, set in the constructor and immutable afterwards.
     * It is referenced by getProjectId. It can be retrieved with the getter.
     * </p>
     */
    private final String extPropAutoPilotSwitch;

    /**
     * <p>
     * Represents the extended property value for auto pilot switch. This will be used to search
     * projects. This variable is initially null, set in the constructor and immutable afterwards.
     * It is referenced by getProjectId. It can be retrieved with the getter.
     * </p>
     */
    private final String extPropAutoPilotSwitchValue;

    /**
     * <p>
     * Constructs a new instance of ActiveAutoPilotSource class. This will initialize the project
     * manager instance using object factory. The object factory is initialized with this class'
     * full name as its configuration namespace. Inside this namespace, a property with the key of
     * ProjectManager's full name is used to retrieve project manager instance.<br>
     * {@link #DEFAULT_ACTIVE_STATUS_NAME} will be used as active status name;
     * {@link #DEFAULT_EXTPROP_AUTOPILOTSWITCH} will be used as extProp;
     * {@link #DEFAULT_EXTPROP_AUTOPILOTSWITCH_VALUE} will be used as extPropValue.
     * </p>
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             project manager instance
     */
    public ActiveAutoPilotSource() throws ConfigurationException {
        this(ActiveAutoPilotSource.class.getName(), ProjectManager.class.getName(),
            DEFAULT_ACTIVE_STATUS_NAME, DEFAULT_EXTPROP_AUTOPILOTSWITCH,
            DEFAULT_EXTPROP_AUTOPILOTSWITCH_VALUE);
    }

    /**
     * <p>
     * Constructs a new instance of ActiveAutoPilotSource class using the given
     * namespace/projectManagerKey to get ProjectManager instance with object factory. The projects
     * will be searched whose status are the given activeStatusName having the given extended
     * property key/value (extProp/extPropVal). This will initialize the project manager instance
     * using object factory. The object factory is initialized with namespace. Inside this
     * namespace, a property with the key of projectManagerKey is used to retrieve project manager
     * instance.
     * </p>
     * @param namespace the namespace to initialize object factory with
     * @param projectManagerKey the key defining the ProjectManager instance
     * @param activeStatusName A non-null string representing a project status of active
     * @param extProp A non-null string representing the extended property name for auto pilot
     *            switch
     * @param extPropVal A non-null string representing the extended property value for auto pilot
     *            switch
     * @throws IllegalArgumentException if any of the argument is null or empty (trimmed) string
     * @throws ConfigurationException if any error occurs instantiating the object factory or the
     *             project manager instance
     */
    public ActiveAutoPilotSource(String namespace, String projectManagerKey,
        String activeStatusName, String extProp, String extPropVal) throws ConfigurationException {
        // Check arguments.
        checkArgumentsForCtor(namespace, projectManagerKey, activeStatusName, extProp, extPropVal);

        this.log = LogManager.getLog("AutoPilot");

        log.log(Level.DEBUG, "Create ActiveAutoPilotSource instance with namespace:" + namespace
        		+ ", projectManagerkey:" + projectManagerKey + ",activeStatusName:"
        		+ activeStatusName + ", extProp:" + extProp + ", extPropVal:" + extPropVal);
        // Create object factory.
        ObjectFactory of;
        Object objProjectManager;
        try {
            of = new ObjectFactory(new ConfigManagerSpecificationFactory(namespace));

            log.log(Level.DEBUG, "create Objectfactory from namespace: " + namespace);
            
            // Create project manager from object factory.
            objProjectManager = of.createObject(projectManagerKey);
            if (!ProjectManager.class.isInstance(objProjectManager)) {
            	log.log(Level.FATAL, "fail to create ProjectManager object caused of bad type:" + objProjectManager);
                throw new ConfigurationException(
                    "fail to create ProjectManager object caused of bad type:" + objProjectManager);
            }
            log.log(Level.DEBUG, "create ProjectManager from objectfactory with key:" + projectManagerKey);
        } catch (InvalidClassSpecificationException e) {
        	log.log(Level.FATAL,
                    "fail to create ProjectManager cause of invalid class specification exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create project manager cause of invalid class specification", e);
        } catch (SpecificationConfigurationException e) {
        	log.log(Level.FATAL,
                    "fail to create object factory cause of specification configuration exception \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create object factory cause of specification configuration exception", e);
        } catch (IllegalReferenceException e) {
        	log.log(Level.FATAL,
                    "fail to create object factory cause of illegal reference \n"
        			+ LogMessage.getExceptionStackTrace(e));
            throw new ConfigurationException(
                "fail to create object factory cause of illegal reference", e);
        }

        this.projectManager = (ProjectManager) objProjectManager;
        this.activeStatusName = activeStatusName;
        this.extPropAutoPilotSwitch = extProp;
        this.extPropAutoPilotSwitchValue = extPropVal;
    }

    /**
     * <p>
     * Check arguments for {@link #ActiveAutoPilotSource(String, String, String, String, String)}.
     * </p>
     * @param namespace namespace argument of constructor.
     * @param projectManagerKey projectManagerKey argument of constructor.
     * @param activeStatusName activeStatusName argument of constructor.
     * @param extProp extProp argument of constructor.
     * @param extPropVal extPropVal argument of constructor.
     */
    private void checkArgumentsForCtor(String namespace, String projectManagerKey,
                    String activeStatusName, String extProp, String extPropVal) {
        if (null == namespace) {
            throw new IllegalArgumentException("namespace cannot be null");
        }
        if (namespace.trim().length() < 1) {
            throw new IllegalArgumentException("namespace cannot be empty");
        }
        if (null == projectManagerKey) {
            throw new IllegalArgumentException("projectManagerKey cannot be null");
        }
        if (projectManagerKey.trim().length() < 1) {
            throw new IllegalArgumentException("projectManagerKey cannot be empty");
        }
        if (null == activeStatusName) {
            throw new IllegalArgumentException("activeStatusName cannot be null");
        }
        if (activeStatusName.trim().length() < 1) {
            throw new IllegalArgumentException("activeStatusName cannot be empty");
        }
        if (null == extProp) {
            throw new IllegalArgumentException("extProp cannot be null");
        }
        if (extProp.trim().length() < 1) {
            throw new IllegalArgumentException("extProp cannot be empty");
        }
        if (null == extPropVal) {
            throw new IllegalArgumentException("extPropVal cannot be null");
        }
        if (extPropVal.trim().length() < 1) {
            throw new IllegalArgumentException("extPropVal cannot be empty");
        }
    }

    /**
     * <p>
     * Constructs a new instance of ActiveAutoPilotSource class using the ProjectManager instance
     * and parameter values.
     * </p>
     * @param projectManager the ProjectManager instance to use
     * @param activeStatusName A non-null string representing a project status of active
     * @param extProp A non-null string representing the extended property name for auto pilot
     *            switch
     * @param extPropVal A non-null string representing the extended property value for auto pilot
     *            switch
     * @param log the Log instance
     * @throws IllegalArgumentException if any of the argument is null or the string arguments are
     *             empty (trimmed)
     */
    public ActiveAutoPilotSource(ProjectManager projectManager, String activeStatusName,
        String extProp, String extPropVal, Log log) {
        // Check arguments.
        if (null == projectManager) {
            throw new IllegalArgumentException("projectManager cannot be null");
        }
        if (null == activeStatusName) {
            throw new IllegalArgumentException("activeStatusName cannot be null");
        }
        if (activeStatusName.trim().length() < 1) {
            throw new IllegalArgumentException("activeStatusName cannot be empty");
        }
        if (null == extProp) {
            throw new IllegalArgumentException("extProp cannot be null");
        }
        if (extProp.trim().length() < 1) {
            throw new IllegalArgumentException("extProp cannot be empty");
        }
        if (null == extPropVal) {
            throw new IllegalArgumentException("extPropVal cannot be null");
        }
        if (extPropVal.trim().length() < 1) {
            throw new IllegalArgumentException("extPropVal cannot be empty");
        }
        if (null == log) {
            throw new IllegalArgumentException("log cannot be null");
        }

        this.log = log;
        this.projectManager = projectManager;
        this.activeStatusName = activeStatusName;
        this.extPropAutoPilotSwitch = extProp;
        this.extPropAutoPilotSwitchValue = extPropVal;

        log.log(Level.DEBUG, "Instantiate ActiveAutoPilotSource with ProjectManager, activeStatusName:"
                + activeStatusName + ",extProp:" + extProp + ", extPropVal:" + extPropVal);
    }

    /**
     * <p>
     * Return the project manager instance used by this class.
     * </p>
     * @return the project manager instance used by this class.
     */
    protected ProjectManager getProjectManager() {
        return this.projectManager;
    }

    /**
     * <p>
     * Returns the name of a project status that represents an active status.
     * </p>
     * @return A non-null string representing the active project status name
     */
    public String getActiveStatusName() {
        return this.activeStatusName;
    }

    /**
     * <p>
     * Returns the extended property name for auto pilot switch.
     * </p>
     * @return A non-null string representing the extended property name for auto pilot switch
     */
    public String getExtPropAutoPilotSwitch() {
        return this.extPropAutoPilotSwitch;
    }

    /**
     * <p>
     * Returns the extended property value for auto pilot switch.
     * </p>
     * @return A non-null string representing the extended property value for auto pilot switch
     */
    public String getExtPropAutoPilotSwitchValue() {
        return this.extPropAutoPilotSwitchValue;
    }

    /**
     * <p>
     * Retrieves all project ids that are active and have Autopilot option switch on in its extended
     * property. This will use {@link ProjectManager#searchProjects(Filter)}. buildFilter is used to build
     * the filter. The return Project[] is then processed by processProject() to extract its id,
     * it's also possible to do more programmatically filtering here.
     * </p>
     * @return a long[] representing project ids to auto pilot (could be empty, but never null)
     * @throws AutoPilotSourceException if an error occurs retrieving the project ids
     */
    public long[] getProjectIds() throws AutoPilotSourceException {
        Filter f = buildFilter();
        try {
            Project[] proj = projectManager.searchProjects(f);

            return processProject(proj);
        } catch (PersistenceException e) {
        	log.log(Level.ERROR,
        			"Fail to get projects from projectManager.\n" + LogMessage.getExceptionStackTrace(e));
            throw new AutoPilotSourceException(
                "fail to search projects cause of persistence exception", e);
        }
    }

    /**
     * <p>
     * Builds the filter that is to be passed to {@link ProjectManager#searchProjects(Filter)}. The
     * filters must filter based on the following search criteria:<br>
     * <ul>
     * <li> - Project status = Active</li>
     * <li> - Extended property name: AutopilotOption = On</li>
     * </ul>
     * <br>
     * </p>
     * @return A non-null filter representing the search criteria
     */
    protected Filter buildFilter() {
        Filter stat = ProjectFilterUtility.buildStatusNameEqualFilter(getActiveStatusName());
        Filter extProp = ProjectFilterUtility.buildProjectPropertyEqualFilter(
            getExtPropAutoPilotSwitch(), getExtPropAutoPilotSwitchValue());
        return ProjectFilterUtility.buildAndFilter(stat, extProp);
    }

    /**
     * <p>
     * Extracts project ids into array. Future implementation may want to override this to do
     * additional filtering programmatically.
     * </p>
     * @param project an array of Project[] whose id to return (could be null)
     * @return a long[] representing project id (never null, but could be empty)
     */
    protected long[] processProject(Project[] project) {
        if (null == project || project.length < 1) {
            return ZERO_LONG_ARRAY;
        }

        long[] ids = new long[project.length];
        for (int i = 0; i < project.length; i++) {
            ids[i] = project[i].getId();
        }

        return ids;
    }
}
