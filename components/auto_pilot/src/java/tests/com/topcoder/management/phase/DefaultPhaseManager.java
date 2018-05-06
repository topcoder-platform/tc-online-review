/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.phase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.phase.validation.DefaultPhaseValidator;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.idgenerator.IDGenerator;

/**
 * <p>
 * Implementation of the PhaseManager interface. DefaultPhaseManager is not thread-safe since it is
 * mutable and its state (data such as handlers) can be compromised through race condition issues.
 * To make this thread-safe we would have to ensure that all the methods that use the internal
 * handlers map have their access synchronized. The purpose of this class is to have a facade which
 * will allow a user to manage phase information (backed by a data store) Phases can be started,
 * ended or cancelled. The logic to check the feasibility of the status change as well as to move
 * the status is pluggable through the PhaseHandler registration API (registerHandler,
 * unregisterHandler, etc...) Applications can provide the plug-ins on a per phase type/operation
 * basis if extra logic needs to be integrated. In addition, a phase validator can be provided that
 * will ensure that all phases that are subject to data store operations such as update, delete,
 * etc... are validated before they are persisted. This is a pluggable option.
 * </p>
 * @author TCSDESIGNER, TCSDEVELOPER
 * @version 1.0
 */
public class DefaultPhaseManager implements PhaseManager {

    /**
     * <p>
     * Represents PhaseHandler registry for this manager. This is basically a mapping of
     * &lt;Phasetype, PhaseOperation&gt; to a handler. Note that &lt;Phasetype, PhaseOperation&gt;
     * is basically a composite key which is represented by the HandlerRegistryInfo class. Since
     * handlers are options this map can be empty. The map can not contain null keys. - initialized
     * at creation to an emprty HashMap(). - modified through register and unregister handler
     * methods.
     * </p>
     */
    private final Map handlers = new HashMap();

    /**
     * <p>
     * Represents persistence implementation plugged into this manager. - initialized through one of
     * the constructors - not modified after initialization.
     * </p>
     */
    private final PhasePersistence persistence;

    /**
     * <p>
     * Represents id generator used by this manager. this will be used to create unique ids for new
     * Phases and Dependencies.. - initialized through one of the constructors - not modified after
     * initialization.
     * </p>
     */
    private final IDGenerator idGenerator;

    /**
     * <p>
     * Represents a genaric validator to be used to scrutinize Phase input. This will be
     * specifically used in the updatePhases method. - initialized through one of the constructors -
     * not modified after initialization.
     * </p>
     */
    private PhaseValidator phaseValidator = new DefaultPhaseValidator();

    /** Mock data. */
    private Project[] projects;

    /**
     * <p>
     * Creates a new instance of teh manager. initialize it with the following data from
     * configuration: 1. Persistence class (required) 2. ID Generator class info (required) 3. DB
     * Connection factory info. (required) 4. If a validator is provided also initialize it
     * (optional)
     * </p>
     * <p>
     * @throws ConfigurationException If any of the required items are not set or if there is an
     *             issue with configuration
     * @throws IllegalArgumentException if input is null or an empty string
     *             </p>
     */
    public DefaultPhaseManager() throws ConfigurationException {
        this(null);
    }

    /**
     * <p>
     * Creates a new instance of teh manager. initialize it with the following data from
     * configuration: 1. Persistence class (required) 2. ID Generator class info (required) 3. DB
     * Connection factory info. (required) 4. If a validator is provided also initialize it
     * (optional)
     * </p>
     * <p>
     * @throws ConfigurationException If any of the required items are not set or if there is an
     *             issue with configuration
     * @throws IllegalArgumentException if input is null or an empty string
     *             </p>
     * @param namespace configuration namespace
     */
    public DefaultPhaseManager(String namespace) throws ConfigurationException {
        this.persistence = null;
        this.idGenerator = null;
        // your code here

        createPhases();
    }

    /**
     * <p>
     * Creates a new instance of teh manager. initialize it with the input data.
     * </p>
     * <p>
     * Exception Handling
     * @throws IllegalArgumentException if input is null.
     *             </p>
     * @param persistence pluggable persistence
     * @param idGenerator id generator
     */
    public DefaultPhaseManager(PhasePersistence persistence, IDGenerator idGenerator) {
        // your code here
        this.persistence = persistence;
        this.idGenerator = idGenerator;
        createPhases();
    }

    /**
     * <p>
     * Create mock data.
     * </p>
     */
    private void createPhases() {
        projects = new Project[5];
        for (int i = 1; i <= 5; i++) {
            Project proj = new Project(new Date(), new DefaultWorkdays());
            proj.setId(i);
            proj.setStartDate(new Date());

            Phase phase = new Phase(proj, 24 * 3600 * 1000);
            phase.setId(5 * i + 1);
            phase.setPhaseType(new PhaseType(1, "PhaseType#1"));
            phase.setPhaseStatus(PhaseStatus.CLOSED);
            proj.addPhase(phase);

            phase = new Phase(proj, 8 * 3600 * 1000);
            phase.setId(5 * i + 2);
            phase.setPhaseType(new PhaseType(2, "PhaseType#2"));
            phase.setPhaseStatus(PhaseStatus.OPEN);
            proj.addPhase(phase);

            phase = new Phase(proj, 12 * 3600 * 1000);
            phase.setPhaseType(new PhaseType(3, "PhaseType#3"));
            phase.setPhaseStatus(PhaseStatus.SCHEDULED);
            phase.setId(5 * i + 3);
            proj.addPhase(phase);

            projects[i - 1] = proj;
        }
    }

    /**
     * <p>
     * Will update this project
     * </p>
     * <p>
     * Implementation details<br>
     * Step 1. walk through all the phases in the project<br>
     * Step 2. For each phase test if it needs to have an id generated (call isNewPhase(phase))<br>
     * <br> - for each dependency in a phase we test if the dependency needs an id generated ((call
     * isNewDependency(phase))<br>
     * <br> - If an id is needed then use id generator and set the id for the object.<br>
     * Step 3. Delegate the rest to persistence.updatePhases(project) Also, in Step 2 we validate
     * each phase and if the validation fails we throw a new PhaseManagementException with the
     * validation exception from validation wrapped.
     * </p>
     * @param project project to update phases for
     * @param operator the operator.
     * @throws IllegalArgumentException if input is null.
     * @throws PhaseManagementException with the validation exception from validation wrapped if
     *             there was a validation problem.
     */
    public void updatePhases(Project project, String operator) throws PhaseManagementException {
        // your code here
    }

    /**
     * <p>
     * Get the Project with the input id. If the Project doesn;t exist we return null.
     * </p>
     * <p>
     * Implementation details Delegate to persistence.getPhases(project)
     * </p>
     * @param project id of the project to fetch
     * @return the project with the project id.
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     */
    public Project getPhases(long project) throws PhaseManagementException {
        // your code here
        if ((int) project <= projects.length) {
            return projects[(int) (project - 1)];
        }

        return null;
    }

    /**
     * <p>
     * Get the Projects with the input id from the input array.
     * </p>
     * <p>
     * Implementation details Delegate to persistence.getPhases(...)
     * </p>
     * <p>
     * Exception Handling
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     *             </p>
     * @param projects an array of project ids to fetch
     * @return an array of Project instances for the input ids.
     */
    public Project[] getPhases(long[] projects) throws PhaseManagementException {
        // your code here
        Project[] projs = new Project[projects.length];
        for (int i = 0; i < projects.length; i++) {
            projs[i] = getPhases(projects[i]);
        }

        return projs;
    }

    /**
     * <p>
     * Return all available Phase Types.
     * </p>
     * <p>
     * Implementation details Delegate to persistence.getAllPhaseTypes(...)
     * </p>
     * <p>
     * Exception Handling
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     *             </p>
     * @return all the PhaseTypes as a list.
     */
    public PhaseType[] getAllPhaseTypes() throws PhaseManagementException {
        // your code here
        return new PhaseType[] {new PhaseType(1, "PhaseType#1"), new PhaseType(2, "PhaseType#2"),
            new PhaseType(3, "PhaseType#3")};
    }

    /**
     * <p>
     * Return all available Phase Statuses.
     * </p>
     * <p>
     * Implementation details Delegate to persistence.getAllPhaseStatuses(...)
     * </p>
     * @return an array of all the phase statuses.
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     */
    public PhaseStatus[] getAllPhaseStatuses() throws PhaseManagementException {
        // your code here
        return new PhaseStatus[] {PhaseStatus.CLOSED, PhaseStatus.OPEN, PhaseStatus.SCHEDULED};
    }

    /**
     * <p>
     * Test if the phase can be put into a start. if a handler exists use handler.canPerform(),
     * otherwise check phase.calcStart() against the current timestamp.
     * </p>
     * <p>
     * Implementation details - To find if handler exists use the phase.getPhaseStatus() and
     * PhaseOperation.START to create an instance of HandlerRegistryInfo. this is our KEY. - Use teh
     * KEY to get a handler ofrm the handlers map. - If found then call the handlers
     * canPerform(phase) method and return result - if NOT found then check phase.calcStart()
     * against the current timestamp.
     * </p>
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     * @param phase phase to tests
     * @return true if we can start. After that.
     */
    public boolean canStart(Phase phase) throws PhaseManagementException {
        // your code here
        return false;
    }

    /**
     * <p>
     * Will cancel this phase. The operation will set the phase to the Open status and also persist
     * the actual end timestamp.
     * </p>
     * <p>
     * Implementation details Step 1. Set the phase object's status id to
     * integer.parseInt(PhaseStatus.OPEN.getValue()); Delegate to persistence.updatePhase(phase);
     * </p>
     * @param phase phase to start
     * @param operator operator starting the phase
     */
    public void start(Phase phase, String operator) {
        // your code here
    }

    /**
     * <p>
     * Test if the phase can be put into an end state. if a handler exists use handler.canPerform(),
     * otherwise check phase.calcEnd() against the current timestamp.
     * </p>
     * <p>
     * Implementation details - To find if handler exists use the phase.getPhaseStatus() and
     * PhaseOperation.START to create an instance of HandlerRegistryInfo. this is our KEY. - Use teh
     * KEY to get a handler ofrm the handlers map. - If found then call the handlers
     * canPerform(phase) method and return result - if NOT found then check phase.calcEnd() against
     * the current timestamp.
     * </p>
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     * @param phase phase to be tested.
     * @return true if the provided phase can properly be ended; false otherwise.
     */
    public boolean canEnd(Phase phase) throws PhaseManagementException {
        // your code here
        return false;
    }

    /**
     * <p>
     * Will end this phase. The operation will set the phase to the Closed status and also persist
     * the actual end timestamp.
     * </p>
     * <p>
     * Implementation details Step 1. Set the phase object's status id to
     * integer.parseInt(PhaseStatus.CLOSED.getValue()); Delegate to persistence.updatePhase(phase);
     * </p>
     * @param phase phase to be eneded
     * @param operator operator ending it.
     */
    public void end(Phase phase, String operator) {
        // your code here
    }

    /**
     * <p>
     * Test if the phase can be put into a cancel state. if a handler exists use
     * handler.canPerform(), otherwise return true.
     * </p>
     * <p>
     * Implementation details - To find if handler exists use the phase.getPhaseStatus() and
     * PhaseOperation.START to create an instance of HandlerRegistryInfo. this is our KEY. - Use teh
     * KEY to get a handler ofrm the handlers map. - If found then call the handlers
     * canPerform(phase) method and return result - if NOT found then return true;
     * </p>
     * @throws PhaseManagementException if there were any issues from persistence. Wrapped.
     * @param phase phase to test (can we cancel)
     * @return treu if we can cancel; False otherwise.
     */
    public boolean canCancel(Phase phase) throws PhaseManagementException {
        // your code here
        return false;
    }

    /**
     * <p>
     * Will cancel this phase. The operation will set the phase to the Closed status and also
     * persist the actual end timestamp.
     * </p>
     * <p>
     * Implementation details Step 1. Set the phase object's status id to
     * integer.parseInt(PhaseStatus.CLOSED.getValue()); Delegate to persistence.updatePhasephase();
     * </p>
     * @param phase phase to cancel
     * @param operator operator cancelling
     */
    public void cancel(Phase phase, String operator) {
        // your code here
    }

    /**
     * <p>
     * Add this handler to the handlers map. If the handler slot is already occupied simply replace
     * it.
     * </p>
     * <p>
     * Implementation details Step 1. Using the type/operation create an instance of
     * HandlerRegistryInfo this is our key. Step 2. put the value into the map using the created
     * key.
     * </p>
     * <p>
     * Exception Handling
     * @throws IllegalArgumentException if any element of input is null
     *             </p>
     * @param handler handler to register
     * @param type phase type
     * @param operation phase operation
     */
    public void registerHandler(PhaseHandler handler, PhaseType type, PhaseOperationEnum operation) {
        // your code here
    }

    /**
     * <p>
     * this method will remove the handler associated with a specific operation/type combination. if
     * teh handler is not found we return null and remove nothing. If the handler is found we remove
     * it and return it to the caller.
     * </p>
     * <p>
     * Implementation details Step 1. Create a new HandlerRegistryInfo instance with the values
     * provided Step 2. Uuse the created instance (lets call it a KEY) to lookup the handler in the
     * handlers map. - If found then we remove it and return it to user - if not found then simply
     * return null.
     * </p>
     * @throws IllegalArgumentException if any element of input is null
     * @param type phase type
     * @param operation phase operation
     * @return the phase handler.
     */
    public PhaseHandler unregisterHandler(PhaseType type, PhaseOperationEnum operation) {
        // your code here
        return null;
    }

    /**
     * <p>
     * this method will get all the handlers in the registry.
     * </p>
     * <p>
     * Implementation details Step 1. Get the set of all values for this handlers map Step 2.
     * Convert it to an array and return it. We do not need to make a copy.
     * </p>
     * <p>
     * Exception Handling None
     * </p>
     * @return all teh registered handlers.
     */
    public PhaseHandler[] getAllHandlers() {
        // your code here
        return null;
    }

    /**
     * <p>
     * this method will try to return some information about the handler in the registry. if the
     * handler is not found we return null
     * </p>
     * <p>
     * Implementation details Step 1. Get all the keys for this handlers map (get iterator) Step 2.
     * for each key we walk through the map fetching the value for this key until we either - have
     * found it. i.e. handler.get(key) == handler if so we return the key. - not found it. If so we
     * return null
     * </p>
     * <p>
     * Exception Handling
     * @throws IllegalArgumentException if input is null
     *             </p>
     * @param handler handler of interest
     * @return registry info under which this was stored.
     */
    public HandlerRegistryInfo[] getHandlerRegistrationInfo(PhaseHandler handler) {
        // your code here
        return null;
    }

    /**
     * <p>
     * Simple setter for a validator.
     * </p>
     * <p>
     * Implementation details this.phaseValidator = this.phaseValidator;
     * </p>
     * <p>
     * Thread-Safety
     * @throws IllegalArgumentException if the input is null.
     *             </p>
     * @param phaseValidator Assign a validator to this manager
     */
    public void setPhaseValidator(PhaseValidator phaseValidator) {
        // your code here
    }

    /**
     * <p>
     * Simple getter for the currently set validator. this method should never return null.
     * </p>
     * <p>
     * Implementation details return this.phaseValidator();
     * </p>
     * @return get the curent phase validator.
     */
    public PhaseValidator getPhaseValidator() {
        // your code here
        return null;
    }
}
