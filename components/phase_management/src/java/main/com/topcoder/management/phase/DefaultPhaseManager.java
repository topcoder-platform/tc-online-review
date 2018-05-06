/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import com.topcoder.management.phase.validation.DefaultPhaseValidator;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.UnknownNamespaceException;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.idgenerator.IDGenerationException;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;
import com.topcoder.util.objectfactory.InvalidClassSpecificationException;
import com.topcoder.util.objectfactory.ObjectFactory;
import com.topcoder.util.objectfactory.impl.ConfigManagerSpecificationFactory;
import com.topcoder.util.objectfactory.impl.IllegalReferenceException;
import com.topcoder.util.objectfactory.impl.SpecificationConfigurationException;

/**
 * <p>
 * Default implementation of the PhaseManager interface. The purpose of this class is to have a facade which will allow
 * a user to manage phase information (backed by a data store). Phases can be started or ended. The logic to check the
 * feasibility of the status change as well as to move the status is pluggable through the PhaseHandler registration
 * API (registerHandler, unregisterHandler, etc.). Applications can provide the plug-ins on a per phase type/operation
 * basis if extra logic needs to be integrated.
 * </p>
 * <p>
 * In addition, a phase validator can be provided that will ensure that all phases that are subject to persistent
 * storage operations are validated before they are persisted. This is a pluggable option.
 * </p>
 * <p>
 * API usage:
 * <pre>
 * // set up the config manager
 * ConfigManager.getInstance().add(&quot;config.xml&quot;);
 * // create a manager using configuration
 * PhaseManager manager = new DefaultPhaseManager(&quot;test.default&quot;);
 * // set up a simple project with a single phase
 * final Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
 * final PhaseType phaseTypeOne = new PhaseType(1, &quot;one&quot;);
 * final Phase phaseOne = new Phase(project, 1);
 * phaseOne.setPhaseType(phaseTypeOne);
 * phaseOne.setFixedStartDate(new Date());
 * phaseOne.setPhaseStatus(PhaseStatus.SCHEDULED);
 * project.addPhase(phaseOne);
 * // create some of the pluggable components
 * DemoIdGenerator idgen = new DemoIdGenerator();
 * DemoPhaseValidator validator = new DemoPhaseValidator();
 * DemoPhaseHandler handler = new DemoPhaseHandler();
 * PhasePersistence persistence = new DemoPhasePersistence() {
 *     public PhaseType[] getAllPhaseTypes() {
 *         return new PhaseType[] {phaseTypeOne};
 *     }
 *     public PhaseStatus[] getAllPhaseStatuses() {
 *         return new PhaseStatus[] {phaseOne.getPhaseStatus()};
 *     }
 *     public Project getProjectPhases(long projectId) {
 *         return project;
 *     }
 * };
 * // create manager programmatically
 * manager = new DefaultPhaseManager(persistence, idgen);
 * // set the validator
 * manager.setPhaseValidator(validator);
 * // register a phase handler for dealing with canStart()
 * manager.registerHandler(handler, phaseTypeOne, PhaseOperationEnum.START);
 * // do some operations
 * // check if phaseOne can be started
 * OperationCheckResult checkResult = manager.canStart(phaseOne);
 * // start
 * if (checkResult.isSuccess()) {
 *     manager.start(phaseOne, &quot;ivern&quot;);
 * } else {
 *     // print out a reason why phase cannot be started
 *     System.out.println(checkResult.getMessage());
 * }
 * // check if phaseOne can be ended
 * checkResult = manager.canEnd(phaseOne);
 * // end
 * if (checkResult.isSuccess()) {
 *     manager.end(phaseOne, &quot;sokol&quot;);
 * } else {
 *     // print out a reason why phase cannot be ended
 *     System.out.println(checkResult.getMessage());
 * }
 * // get all phase types
 * PhaseType[] allTypes = manager.getAllPhaseTypes();
 * // get all phase statuses
 * PhaseStatus[] allStatuses = manager.getAllPhaseStatuses();
 * // update the project
 * manager.updatePhases(project, &quot;ivern&quot;);
 * </pre>
 * </p>
 * <p>
 * Changes in 1.1:
 * <ol>
 * <li>Removed canCancel() and cancel() methods.</li>
 * <li>Changed return type of canStart() and canEnd() methods from boolean to OperationCheckResult.</li>
 * </ol>
 * </p>
 * <p>
 * Thread Safety: DefaultPhaseManager is not thread safe since it is mutable and its state (data such as handlers) can
 * be compromised through race condition issues. To make this thread-safe we would have to ensure that all the methods
 * that use the internal handlers map have their access synchronized.
 * </p>
 * @author AleaActaEst, saarixx, RachaelLCook, sokol
 * @version 1.1
 */
public class DefaultPhaseManager implements PhaseManager {

    /**
     * <p>
     * This is an inner class of DefaultPhaseManager. It is a comparator that compares Phase instances.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Added generic parameter for implemented Comparator&lt;T&gt; interface.</li>
     * <li>Added default constructor.</li>
     * <li>Updated compare() method to use Phase instead of Object parameters.</li>
     * </ol>
     * </p>
     * <p>
     * Thread Safety: This class is thread safe by virtue of not having any state.
     * </p>
     * @author sokol
     * @version 1.1
     */
    private class PhaseComparator implements Comparator < Phase > {

        /**
         * Creates an instance of PhaseComparator.
         * @since 1.1
         */
        public PhaseComparator() {
        }

        /**
         * <p>
         * Returns -1, 0, or 1 if the ID of the first Phase argument is less than, equal to, or greater than the ID of
         * the second Phase, respectively.
         * </p>
         * <p>
         * Changes in 1.1:
         * <ol>
         * <li>Changed parameter types and names</li>
         * </ol>
         * </p>
         * @param phase1 the first Phase to compare
         * @param phase2 the second Phase to compare
         * @return -1, 0, or 1 if the ID of the first Phase argument is less than, equal to, or greater than the ID of
         *         the second Phase, respectively
         * @throws IllegalArgumentException if phase1 or phase2 is null
         */
        public int compare(Phase phase1, Phase phase2) {
            // check arguments
            Helper.checkState(phase1 == null, "phase1 should not be null.");
            Helper.checkState(phase2 == null, "phase2 should not be null.");
            long p1 = phase1.getId(); // MODIFIED in 1.1
            long p2 = phase2.getId(); // MODIFIED in 1.1
            if (p1 < p2) {
                return -1;
            } else if (p1 > p2) {
                return 1;
            }
            return 0;
        }
    }

    /**
     * <p>
     * Mapping from HandlerRegistryInfo keys to PhaseHandler values. This is used to look up phase handlers when
     * performing operations. Collection instance is initialized during construction and never changed after that.
     * Cannot be null, cannot contain null key or value.
     * </p>
     * <p>
     * Is used in constructor, registerHandler(), unregisterHandler(), getAllHandlers(), getHandlerRegistrationInfo()
     * and getPhaseHandler().
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Added generic parameters to the collection types.</li>
     * </ol>
     * </p>
     */
    private final Map < HandlerRegistryInfo, PhaseHandler > handlers =
            new HashMap < HandlerRegistryInfo, PhaseHandler >();

    /**
     * The phase persistence. This member is initialized by the constructor and does not change over the life of the
     * object.
     */
    private final PhasePersistence persistence;

    /**
     * The ID generator. This member is initialized by the constructor and does not change over the life of the object.
     */
    private final IDGenerator idGenerator;

    /**
     * The phase validator.
     * @see #getPhaseValidator
     * @see #setPhaseValidator
     */
    private PhaseValidator phaseValidator = new DefaultPhaseValidator();

    /**
     * <p>
     * Creates a new manager configured based on the specified configuration namespace. The configuration parameters
     * are as follows.
     * <ul>
     * <li><code>PhasePersistence.className</code> - the name of the class handling phase persistence (this class must
     * have a constructor that accepts a single <code>String</code> argument)</li>
     * <li>PhaseValidator.className - the name of the class handling phase validation (<i>optional</i>)</li>
     * <li><code>Idgenerator.className</code> - the class handling ID generation</li>
     * <li><code>Idgenerator.sequenceName</code> - the name of the ID sequence</li>
     * <ul>
     * <code>Handlers</code> - the handlers to be registered
     * <li><code>handler[x]</code> - the handler[x]</li>
     * </ul>
     * <ul>
     * handler[x]
     * <li><code>phaseType</code> - the type of the phase to handle
     * <li>
     * <li><code>operation</code> - the operation of the handler to perform, must be one of start or end
     * <li><code>handlerDef</code> - the handler definition name in configuration of Object Factory
     * </ul>
     * </ul>
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Just added generic parameters for the collection types in the code.</li>
     * </ol>
     * </p>
     * @param namespace the configuration namespace
     * @throws ConfigurationException if any required configuration parameter is missing, or if any of the supplied
     *             parameters are invalid
     * @throws IllegalArgumentException if the argument is <code>null</code> or the empty string
     */
    public DefaultPhaseManager(String namespace) throws ConfigurationException {
        if (namespace == null) {
            throw new IllegalArgumentException("namespace must be a non-null string");
        }
        if (namespace.trim().length() == 0) {
            throw new IllegalArgumentException("namespace must be a non-empty string");
        }
        ConfigManager manager = ConfigManager.getInstance();
        try {
            String persistenceClass = manager.getString(namespace, "PhasePersistence.className");
            if (persistenceClass == null) {
                throw new ConfigurationException("PhasePersistence.className does not exist");
            }
            // create phase persistence
            this.persistence =
                    DefaultPhaseManager.createObj(PhasePersistence.class, persistenceClass,
                            new Class < ? >[] {String.class}, new Object[] {namespace}); // UPDATED in 1.1
            String validatorClass = manager.getString(namespace, "PhaseValidator.className");
            if (validatorClass != null) {
                // create phase validator
                // UPDATED in 1.1
                this.phaseValidator =
                        DefaultPhaseManager.createObj(PhaseValidator.class, validatorClass, new Class < ? >[0],
                                new Object[0]);
            }
            String idSequence = manager.getString(namespace, "Idgenerator.sequenceName");
            String idClass = manager.getString(namespace, "Idgenerator.className");
            this.idGenerator = instantiateIDGenerator(idSequence, idClass);
            // load the handlers
            // create all the handlers defined in the configuration file
            // this is an optional property
            String[] handlerNames = manager.getStringArray(namespace, "Handlers");
            if (handlerNames != null && handlerNames.length > 0) {
                // first get the namespace for object factory
                // it's optional and if it's missed, the default one is the same with the current namespace
                String factoryNamespace = manager.getString(namespace, "ObjectFactoryNamespace");
                if (factoryNamespace == null || factoryNamespace.trim().length() == 0) {
                    throw new ConfigurationException("The namespace for object factory is missing.");
                }
                // create the object factory
                ObjectFactory factory =
                        new ObjectFactory(new ConfigManagerSpecificationFactory(factoryNamespace), ObjectFactory.BOTH);
                // for each handler defined, get its registry info and handler object
                // then register it
                // both info and handler key are required
                for (int i = 0; i < handlerNames.length; i++) {
                    // get registry info
                    PhaseType type =
                            (PhaseType) factory.createObject(getRequiredValue(namespace, handlerNames[i]
                                    + ".phaseType"));
                    PhaseOperationEnum op = getOperation(getRequiredValue(namespace, handlerNames[i] + ".operation"));
                    if (op == null) {
                        throw new ConfigurationException("There is an error in the configurations,"
                                + "operation is not set or set incorrectly. It should be one of start and end.");
                    }
                    // get handler object
                    PhaseHandler handler =
                            (PhaseHandler) factory.createObject(getRequiredValue(namespace, handlerNames[i]
                                    + ".handler"));
                    // register the handler
                    this.registerHandler(handler, type, op);
                }
            }
        } catch (UnknownNamespaceException ex) {
            throw new ConfigurationException("no such namespace '" + namespace + "'", ex);
        } catch (SpecificationConfigurationException ex) {
            throw new ConfigurationException("bad specification in configurations", ex);
        } catch (IllegalReferenceException ex) {
            throw new ConfigurationException("illegal references", ex);
        } catch (InvalidClassSpecificationException ex) {
            throw new ConfigurationException("invalid class specification", ex);
        }
    }

    /**
     * <p>
     * Get operation by name.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made static.</li>
     * <li>Removed support of CANCEL phase operation enum value.</li>
     * </ol>
     * </p>
     * @param name the name of the operation
     * @return the enum value or null, if operation is not found for given name
     */
    private static PhaseOperationEnum getOperation(String name) {
        // UPDATED in 1.1
        // iterate over all find operation with given name
        for (PhaseOperationEnum phaseOperation : PhaseOperationEnum.values()) {
            if (phaseOperation.getName().equals(name)) {
                return phaseOperation;
            }
        }
        return null;
    }

    /**
     * <p>
     * Get a required value from configuration manager. If the value is missed (null/empty after trimmed),
     * ConfigurationException will be thrown.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made static.</li>
     * </ol>
     * </p>
     * @param namespace the namespace
     * @param name the name of the property
     * @return the value
     * @throws UnknownNamespaceException if the namespace doesn't exist
     * @throws ConfigurationException if the property missed
     */
    private static String getRequiredValue(String namespace, String name) throws UnknownNamespaceException,
        ConfigurationException {
        ConfigManager manager = ConfigManager.getInstance();
        String value = manager.getString(namespace, name);
        if (value == null || value.trim().length() == 0) {
            throw new ConfigurationException("The '" + name + "' is required.");
        }
        return value;
    }

    /**
     * Creates a new <code>DefaultPhaseManager</code> with the specified persistent storage and ID generator.
     * @throws IllegalArgumentException if either argument is <code>null</code>
     * @param persistence the persistent storage manager for this phase manager
     * @param idGenerator the ID generator for this phase manager
     */
    public DefaultPhaseManager(PhasePersistence persistence, IDGenerator idGenerator) {
        if (persistence == null) {
            throw new IllegalArgumentException("persistence must be non-null");
        }
        if (idGenerator == null) {
            throw new IllegalArgumentException("idGenerator must be non-null");
        }
        this.persistence = persistence;
        this.idGenerator = idGenerator;
    }

    /**
     * <p>
     * Instantiates the ID generator and wrap the exceptions.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made static.</li>
     * </ol>
     * </p>
     * @param idSequence the ID sequence
     * @param idClass the ID class
     * @return the ID generator
     * @throws ConfigurationException if ID generator cannot be instantiated for any reason
     */
    private static IDGenerator instantiateIDGenerator(String idSequence, String idClass) throws ConfigurationException {
        if (idSequence == null) {
            throw new ConfigurationException("Idgenerator.sequenceName does not exist");
        }
        if (idClass == null) {
            throw new ConfigurationException("Idgenerator.className does not exist");
        }
        try {
            return IDGeneratorFactory.getIDGenerator(idSequence, idClass);
        } catch (BaseException ex) {
            throw new ConfigurationException("failed to instantiate ID generator: " + idClass, ex);
        } catch (IllegalAccessException ex) {
            throw new ConfigurationException("failed to instantiate ID generator: " + idClass, ex);
        } catch (IllegalStateException ex) {
            throw new ConfigurationException("failed to instantiate ID generator: " + idClass, ex);
        } catch (ClassNotFoundException ex) {
            throw new ConfigurationException("failed to instantiate ID generator: " + idClass, ex);
        } catch (NoSuchMethodException ex) {
            throw new ConfigurationException("class " + idClass + " does not have the correct constructor", ex);
        } catch (InvocationTargetException ex) {
            throw new ConfigurationException("error instantiating class " + idClass, ex);
        } catch (InstantiationException ex) {
            throw new ConfigurationException("class " + idClass + " does not have the correct constructor", ex);
        } catch (ClassCastException ex) {
            throw new ConfigurationException("error casting ID generator class " + idClass, ex);
        }
    }

    /**
     * Synchronizes the current state of the specified project with persistent storage. This method first validates all
     * of the phases in the project, then generates IDs for any new phases. Finally, the phases of the specified input
     * project are compared to the phases already in the database. If any new phases are encountered, they are added to
     * the persistent store via {@link PhasePersistence#createPhases createPhases}. If any phases are missing from the
     * input, they are deleted using {@link PhasePersistence#deletePhases deletePhases}. All other phases are updated
     * using {@link PhasePersistence#updatePhases updatePhases}.
     * @throws IllegalArgumentException if either argument is <code>null</code> or the empty string
     * @throws PhaseManagementException if a phase fails validation, or if an error occurs while persisting the updates
     *             or generating the IDs
     * @param project project for which to update phases
     * @param operator the operator performing the action
     */
    public void updatePhases(Project project, String operator) throws PhaseManagementException {
        checkUpdatePhasesArguments(project, operator);
        Phase[] phases = project.getAllPhases();
        PhaseValidator validator = this.getPhaseValidator();
        // first, validate all the phases if a validator exists
        if (validator != null) {
            for (int i = 0; i < phases.length; ++i) {
                try {
                    validator.validate(phases[i]);
                } catch (PhaseValidationException ex) {
                    throw new PhaseManagementException("validation failure for phase " + phases[i].getId(), ex);
                }
            }
        }
        try {
            // next, set the ID for any phases that need it
            for (int i = 0; i < phases.length; ++i) {
                if (persistence.isNewPhase(phases[i])) {
                    phases[i].setId(this.idGenerator.getNextID());
                }
            }
            // separate the phases into three batches: additions, deletions, and updates
            TreeSet < Phase > delete = new TreeSet < Phase >(new PhaseComparator()); // UPDATED in 1.1
            TreeSet < Phase > add = new TreeSet < Phase >(new PhaseComparator()); // UPDATED in 1.1
            TreeSet < Phase > update = new TreeSet < Phase >(new PhaseComparator()); // UPDATED in 1.1
            // initially, mark all of the currently existing phases for deletion
            Project existingProject = getPhases(project.getId());
            if (existingProject != null) {
                Phase[] existingPhases = existingProject.getAllPhases();
                for (int i = 0; i < existingPhases.length; ++i) {
                    delete.add(existingPhases[i]);
                }
            }
            // for each phase in the input project, determine whether it's a new phase or an existing phase
            // if it's an existing phase, remove it from the set of phases to delete
            for (int i = 0; i < phases.length; ++i) {
                if (delete.remove(phases[i])) {
                    // the phase already exists, so update it
                    update.add(phases[i]);
                } else {
                    // the phase doesn't already exist, so add it
                    add.add(phases[i]);
                }
            }
            // anything left over in the delete set at this point does not exist in the input project
            // finally, perform the creations, updates, and deletions
            if (add.size() > 0) {
                persistence.createPhases(add.toArray(new Phase[0]), operator);
            }
            if (update.size() > 0) {
                persistence.updatePhases(update.toArray(new Phase[0]), operator);
            }
            if (delete.size() > 0) {
                persistence.deletePhases(delete.toArray(new Phase[0]));
            }
        } catch (IDGenerationException ex) {
            throw new PhaseManagementException("cannot generate phase IDs", ex);
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * <p>
     * Validates the arguments to <code>updatePhases</code>. A non-exceptional return indicates success.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made static.</li>
     * </ol>
     * </p>
     * @param project project for which to update phases
     * @param operator the operator performing the action
     * @throws IllegalArgumentException if either argument is <code>null</code> or the empty string
     */
    private static void checkUpdatePhasesArguments(Project project, String operator) {
        if (project == null) {
            throw new IllegalArgumentException("project must be non-null");
        }
        if (operator == null) {
            throw new IllegalArgumentException("operator must be non-null");
        }
        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("operator must be non-empty");
        }
    }

    /**
     * Returns the <code>Project</code> corresponding to the specified ID. If no such project exists, returns
     * <code>null</code>.
     * @throws PhaseManagementException if an error occurred querying the project from the persistent store
     * @param project id of the project to fetch
     * @return the project corresponding to the specified ID, or <code>null</code> if no such project exists
     */
    public Project getPhases(long project) throws PhaseManagementException {
        try {
            return persistence.getProjectPhases(project);
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * Similar to {@link #getPhases(long) getPhases(long)}, except this method queries multiple projects in one call.
     * Indices in the returned array correspond to indices in the input array. If a specified project cannot be found,
     * a <code>null</code> will be returned in the corresponding array position.
     * @throws PhaseManagementException if an error occurred querying the projects from the persistent store
     * @throws IllegalArgumentException if <code>projects</code> is <code>null</code>
     * @param projects the project IDs to look up
     * @return the <code>Project</code> instances corresponding to the specified project IDs
     */
    public Project[] getPhases(long[] projects) throws PhaseManagementException {
        if (projects == null) {
            throw new IllegalArgumentException("arguments to DefaultPhaseManager#getPhases must be non-null");
        }
        try {
            return persistence.getProjectPhases(projects);
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * Returns an array of all phase types by calling the {@link PhasePersistence#getAllPhaseStatuses getAllPhaseTypes}
     * method of this manager's configured persistence object.
     * @throws PhaseManagementException if an error occurred retrieving the types from persistent storage
     * @return an array of all the phase types
     */
    public PhaseType[] getAllPhaseTypes() throws PhaseManagementException {
        try {
            return persistence.getAllPhaseTypes();
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * Returns an array of all phase statuses by calling the {@link PhasePersistence#getAllPhaseStatuses
     * getAllPhaseStatuses} method of this manager's configured persistence object.
     * @throws PhaseManagementException if an error occurred retrieving the statuses from persistent storage
     * @return an array of all the phase statuses
     */
    public PhaseStatus[] getAllPhaseStatuses() throws PhaseManagementException {
        try {
            return persistence.getAllPhaseStatuses();
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * <p>
     * Determines whether it is possible to start the specified phase. If a PhaseHandler phase handler has been
     * registered for the start operation of the given phase type, its canPerform method will be called to determine
     * whether the phase can be started. If no handler is registered, this method returns OperationCheckResult.SUCCESS
     * if the phase's start date is less than or equal to the current date.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Changed return type from boolean to OperationCheckResult.</li>
     * <li>Added step for creating OperationCheckResult instance with reasoning message.</li>
     * </ol>
     * </p>
     * @param phase phase to test for starting
     * @return the validation result indicating whether phase can be started, and if not, providing a reasoning message
     *         (not null)
     * @throws IllegalArgumentException if phase is <code>null</code>
     * @throws PhaseHandlingException propagated from the phase handler (if any)
     */
    public OperationCheckResult canStart(Phase phase) throws PhaseHandlingException {
        if (phase == null) {
            throw new IllegalArgumentException("null argument to DefaultPhaseManager#canStart");
        }
        PhaseHandler handler = getPhaseHandler(phase, PhaseOperationEnum.START);
        if (handler != null) {
            return handler.canPerform(phase);
        }
        boolean success = phase.calcStartDate().compareTo(new Date()) <= 0; // UPDATED in 1.1
        return success ? OperationCheckResult.SUCCESS
                : new OperationCheckResult("Phase start time is not yet reached"); // NEW in 1.1
    }

    /**
     * Starts the specified phase. If a PhaseHandler phase handler is set for the start operation of the phase's type,
     * the handler's perform method is invoked first. Next, the phase's status is set to OPEN and the phase's actual
     * start date is set to the current date. Finally, the changes are persisted by delegating to the configured phase
     * persistence object.
     * @param phase the phase to start
     * @param operator the operator starting the phase
     * @throws IllegalArgumentException if either argument is null or an empty string
     * @throws PhaseHandlingException if an exception occurs while starting the phase
     * @throws PhaseManagementException if an error occurs while persisting the change
     */
    public void start(Phase phase, String operator) throws PhaseManagementException {
        if (phase == null) {
            throw new IllegalArgumentException("phase must be non-null");
        }
        if (operator == null) {
            throw new IllegalArgumentException("operator must be non-null");
        }
        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("operator must be non-empty");
        }
        if (phase.getPhaseType() != null) {
            PhaseHandler handler = getPhaseHandler(phase, PhaseOperationEnum.START);
            if (handler != null) {
                handler.perform(phase, operator);
            }
        }
        phase.setPhaseStatus(new PhaseStatus(PhaseStatusEnum.OPEN.getId(), PhaseStatusEnum.OPEN.getName()));
        phase.setActualStartDate(new Date());
        Phase[] allPhases = phase.getProject().getAllPhases();
        recalculateScheduledDates(allPhases);
        try {
            persistence.updatePhases(allPhases, operator);
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * <p>
     * Determines whether it is possible to end the specified phase. If a PhaseHandler phase handler has been
     * registered for the end operation of the given phase type, its canPerform method will be called to determine
     * whether the phase can be ended. If no handler is registered, this method returns OperationCheckResult.SUCCESS if
     * the phase's end date is less than or equal to the current date.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Changed return type from boolean to OperationCheckResult.</li>
     * <li>Added step for creating OperationCheckResult instance with reasoning message.</li>
     * </ol>
     * </p>
     * @param phase phase to test for ending
     * @return the validation result indicating whether phase can be ended, and if not, providing a reasoning message
     *         (not null)
     * @throws IllegalArgumentException if phase is <code>null</code>
     * @throws PhaseHandlingException propagated from the phase handler (if any)
     */
    public OperationCheckResult canEnd(Phase phase) throws PhaseHandlingException {
        if (phase == null) {
            throw new IllegalArgumentException("null argument to DefaultPhaseManager#canEnd");
        }
        PhaseHandler handler = getPhaseHandler(phase, PhaseOperationEnum.END);
        if (handler != null) {
            return handler.canPerform(phase);
        }
        boolean success = phase.calcEndDate().compareTo(new Date()) <= 0;
        // NEW in 1.1
        return success ? OperationCheckResult.SUCCESS : new OperationCheckResult("Phase end time is not yet reached");
    }

    /**
     * Ends the specified phase. If a PhaseHandler phase handler is set for the end operation of the phase's type, the
     * handler's perform method is invoked first. Next, the phase's status is set to CLOSED and the phase's actual end
     * date is set to the current date. Finally, the changes are persisted by delegating to the configured phase
     * persistence object.
     * @param phase the phase to end
     * @param operator the operator ending the phase
     * @throws IllegalArgumentException if either argument is null or an empty string
     * @throws PhaseHandlingException if an exception occurs while starting the phase
     * @throws PhaseManagementException if an error occurs while persisting the change
     */
    public void end(Phase phase, String operator) throws PhaseManagementException {
        if (phase == null) {
            throw new IllegalArgumentException("phase must be non-null");
        }
        if (operator == null) {
            throw new IllegalArgumentException("operator must be non-null");
        }
        if (operator.trim().length() == 0) {
            throw new IllegalArgumentException("operator must be non-empty");
        }
        if (phase.getPhaseType() != null) {
            PhaseHandler handler = getPhaseHandler(phase, PhaseOperationEnum.END);
            if (handler != null) {
                handler.perform(phase, operator);
            }
        }
        phase.setPhaseStatus(new PhaseStatus(PhaseStatusEnum.CLOSED.getId(), PhaseStatusEnum.CLOSED.getName()));
        phase.setActualEndDate(new Date());
        Phase[] allPhases = phase.getProject().getAllPhases();
        recalculateScheduledDates(allPhases);
        try {
            persistence.updatePhases(allPhases, operator);
        } catch (PhasePersistenceException ex) {
            throw new PhaseManagementException("phase persistence error", ex);
        }
    }

    /**
     * <p>
     * Registers a custom handler for the specified phase type and operation. If present, handlers override the default
     * behavior for determining whether a given operation can be performed on a given phase. If a handler already
     * exists for the specified type/operation combination, it will be replaced by the specified handler.
     * </p>
     * <p>
     * Note that <code>type</code> is stored in the registry by reference (rather than copied) so the caller should
     * take care not to subsequently modify the type. Doing so may cause the registry to become inconsistent.
     * </p>
     * @throws IllegalArgumentException if any argument is null
     * @param handler the handler
     * @param type the phase type to associate with the handler
     * @param operation the operation to associate with the handler
     */
    public void registerHandler(PhaseHandler handler, PhaseType type, PhaseOperationEnum operation) {
        if (handler == null) {
            throw new IllegalArgumentException("handler must be non-null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must be non-null");
        }
        if (operation == null) {
            throw new IllegalArgumentException("operation must be non-null");
        }
        handlers.put(new HandlerRegistryInfo(type, operation), handler);
    }

    /**
     * Unregisters the handler (if any) associated with the specified phase type and operation and returns a reference
     * to the handler. Returns <code>null</code> if no handler is associated with the specified type/operation
     * combination.
     * @throws IllegalArgumentException if either argument is <code>null</code>
     * @param type the phase type associated with the handler to unregister
     * @param operation the operation associated with the handler to unregister
     * @return the previously registered handler, or <code>null</code> if no handler was registered
     */
    public PhaseHandler unregisterHandler(PhaseType type, PhaseOperationEnum operation) {
        if (type == null) {
            throw new IllegalArgumentException("type must be non-null");
        }
        if (operation == null) {
            throw new IllegalArgumentException("operation must be non-null");
        }
        return handlers.remove(new HandlerRegistryInfo(type, operation));
    }

    /**
     * <p>
     * Returns an array of all the currently registered phase handlers. If a handler is registered more than one (for
     * different phase/operation combinations), it will appear only once in the array.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Just added generic parameters for the collection types in the code.</li>
     * </ol>
     * </p>
     * @return all of the currently registered phase handlers
     */
    public PhaseHandler[] getAllHandlers() {
        // Copying the values into a set will remove duplicate values
        Set < PhaseHandler > allHandlers = new HashSet < PhaseHandler >(handlers.values()); // UPDATED in 1.1
        return allHandlers.toArray(new PhaseHandler[0]);
    }

    /**
     * <p>
     * Returns the phase type(s) and operation(s) associated with the specified handler in the handler registry.
     * Returns an empty array if the handler is not registered.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Just added generic parameters for the collection types in the code.</li>
     * </ol>
     * </p>
     * @throws IllegalArgumentException if <code>handler</code> is <code>null</code>
     * @param handler handler of interest
     * @return the registration entries associated with the handler
     */
    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler handler) {
        if (handler == null) {
            throw new IllegalArgumentException("handler must not be null");
        }
        final HashSet < HandlerRegistryInfo > hri = new HashSet < HandlerRegistryInfo >(); // UPDATED in 1.1
        for (Iterator < Entry < HandlerRegistryInfo, PhaseHandler >> it = handlers.entrySet().iterator();
        it.hasNext();) {
            Entry < HandlerRegistryInfo, PhaseHandler > entry = it.next();
            if (entry.getValue() == handler) {
                hri.add(entry.getKey());
            }
        }
        return hri.toArray(new HandlerRegistryInfo[0]);
    }

    /**
     * Sets the current phase validator for this manager.
     * @throws IllegalArgumentException if the validator is null
     * @param phaseValidator the validator to use for this manager
     */
    public void setPhaseValidator(PhaseValidator phaseValidator) {
        if (phaseValidator == null) {
            throw new IllegalArgumentException("phase validator cannot be set to null");
        }
        this.phaseValidator = phaseValidator;
    }

    /**
     * Returns the current phase validator. If no phase validator has been configured or set for this manager, an
     * instance of {@link DefaultPhaseValidator DefaultPhaseValidator} will be used instead.
     * @return the current phase validator
     */
    public PhaseValidator getPhaseValidator() {
        return this.phaseValidator;
    }

    /**
     * Returns the phase handler associated with the specified phase and operation, or <code>null</code> if no such
     * handler exists.
     * @param phase the phase
     * @param operation the phase operation
     * @return the phase handler associated with the specified phase and operation
     */
    private PhaseHandler getPhaseHandler(Phase phase, PhaseOperationEnum operation) {
        HandlerRegistryInfo hri = new HandlerRegistryInfo(phase.getPhaseType(), operation);
        return handlers.get(hri);
    }

    /**
     * <p>
     * Creates new instance of given class name created via reflection.
     * </p>
     * @param <T> the type of returned object
     * @param targetClass the target class that our class name should be assignable from
     * @param className the class that should be created name
     * @param argumentsClasses the constructor arguments classes, empty if default constructor is used
     * @param argumentsValues the constructor arguments values, empty if default constructor is used
     * @return new instance of given class name created via reflection
     * @throws ConfigurationException if any error occurs while creating object of given class
     */
    @SuppressWarnings("unchecked")
    private static < T > T createObj(Class < T > targetClass, String className, Class < ? > argumentsClasses[],
            Object argumentsValues[]) throws ConfigurationException {
        // get class name
        try {
            Class < ? > objClass = Class.forName(className);
            if (!targetClass.isAssignableFrom(objClass)) {
                throw new ConfigurationException("The class '" + className + "' is not of type '"
                        + targetClass.getName() + "'.");
            }
            // Instantiate the class reflectively:
            return (T) objClass.getDeclaredConstructor(argumentsClasses).newInstance(argumentsValues);
        } catch (ClassNotFoundException e) {
            throw new ConfigurationException("The class '" + className + "' cannot be found.", e);
        } catch (NoSuchMethodException e) {
            throw new ConfigurationException("The constructor cannot be found.", e);
        } catch (InstantiationException e) {
            throw new ConfigurationException("The class is an abstract class.", e);
        } catch (IllegalAccessException e) {
            throw new ConfigurationException("The constructor is inaccessible.", e);
        } catch (InvocationTargetException e) {
            throw new ConfigurationException("Failed to create an object.", e);
        } catch (LinkageError e) {
            throw new ConfigurationException("Linkage failed.", e);
        } catch (SecurityException e) {
            throw new ConfigurationException("A security error occurred.", e);
        }
    }

    /**
     * <p>
     * Recalculate scheduled start date and end date for all phases when a phase is moved.
     * </p>
     * <p>
     * Changes in 1.1:
     * <ol>
     * <li>Made static.</li>
     * </ol>
     * </p>
     * @param allPhases all the phases for the project.
     */
    private static void recalculateScheduledDates(Phase[] allPhases) {
        for (int i = 0; i < allPhases.length; ++i) {
            Phase phase = allPhases[i];
            phase.setScheduledStartDate(phase.calcStartDate());
            phase.setScheduledEndDate(phase.calcEndDate());
        }
    }
}
