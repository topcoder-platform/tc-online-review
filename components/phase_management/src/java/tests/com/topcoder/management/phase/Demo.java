/*
 * Copyright (C) 2006-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase;

import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.date.workdays.DefaultWorkdaysFactory;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * Demonstrates the usage of the phase management component. Adapted from the component specification.
 * @author saarixx, RachaelLCook, sokol
 * @version 1.1
 */
public class Demo extends TestCase {

    /**
     * A sample ID generator which simply returns a sequence of increasing <code>longs</code>.
     */
    private class DemoIdGenerator extends NullIdGenerator {

        /**
         * Returns the next <code>long</code> in the sequence.
         * @return the next <code>long</code> in the sequence
         */
        public long getNextID() {
            return getNext();
        }
    }

    /**
     * A sample phase validator that does nothing.
     */
    private class DemoPhaseValidator implements PhaseValidator {

        /**
         * Does nothing.
         * @param phase the phase to validate
         */
        public void validate(Phase phase) {
        }
    }

    /**
     * A sample phase handler that lets all operations perform and has no special actions.
     */
    private class DemoPhaseHandler implements PhaseHandler {

        /**
         * Returns <code>OperationCheckResult.SUCCESS</code>.
         * @param phase the phase on which an operation is being performed
         * @return <code>OperationCheckResult.SUCCESS</code>
         */
        public OperationCheckResult canPerform(Phase phase) {
            return OperationCheckResult.SUCCESS;
        }

        /**
         * Does nothing.
         * @param phase the phase on which an operation is being performed
         * @param operator the operator
         */
        public void perform(Phase phase, String operator) {
        }
    }

    /**
     * A sample phase persistence class that does nothing.
     */
    private class DemoPhasePersistence extends NullPhasePersistence {

        /**
         * Does nothing.
         * @param phase the phase to update
         * @param operator the operator
         */
        public void updatePhase(Phase phase, String operator) {
        }

        /**
         * Returns <code>true</code>.
         * @param phase the phase to test for being new
         * @return <code>true</code>
         */
        public boolean isNewPhase(Phase phase) {
            return true;
        }

        /**
         * Does nothing.
         * @param phases the phases to update
         * @param operator the operator
         */
        public void updatePhases(Phase[] phases, String operator) {
        }
    }

    /**
     * Demonstrates the usage of the phase management component. Adapted from the component specification.
     * @throws Exception if an unexpected exception occurs while executing the demo
     */
    public void testDemo() throws Exception {
        // set up the config manager
        ConfigManager.getInstance().add("config.xml");
        // create a manager using configuration
        PhaseManager manager = new DefaultPhaseManager("test.default");
        // set up a simple project with a single phase
        final Project project = new Project(new Date(), new DefaultWorkdaysFactory(false).createWorkdaysInstance());
        final PhaseType phaseTypeOne = new PhaseType(1, "one");
        final Phase phaseOne = new Phase(project, 1);
        phaseOne.setPhaseType(phaseTypeOne);
        phaseOne.setFixedStartDate(new Date());
        phaseOne.setPhaseStatus(PhaseStatus.SCHEDULED);
        project.addPhase(phaseOne);
        // create some of the pluggable components
        DemoIdGenerator idgen = new DemoIdGenerator();
        DemoPhaseValidator validator = new DemoPhaseValidator();
        DemoPhaseHandler handler = new DemoPhaseHandler();
        PhasePersistence persistence = new DemoPhasePersistence() {

            public PhaseType[] getAllPhaseTypes() {
                return new PhaseType[] {phaseTypeOne};
            }

            public PhaseStatus[] getAllPhaseStatuses() {
                return new PhaseStatus[] {phaseOne.getPhaseStatus()};
            }

            public Project getProjectPhases(long projectId) {
                return project;
            }
        };
        // create manager programmatically
        manager = new DefaultPhaseManager(persistence, idgen);
        // set the validator
        manager.setPhaseValidator(validator);
        // register a phase handler for dealing with canStart()
        manager.registerHandler(handler, phaseTypeOne, PhaseOperationEnum.START);
        // NEW in 1.1
        // check if phaseOne can be started
        OperationCheckResult checkResult = manager.canStart(phaseOne);
        // start
        if (checkResult.isSuccess()) {
            manager.start(phaseOne, "ivern");
        } else {
            // print out a reason why phase cannot be started
            System.out.println(checkResult.getMessage());
        }
        // check if phaseOne can be ended
        checkResult = manager.canEnd(phaseOne);
        // end
        if (checkResult.isSuccess()) {
            manager.end(phaseOne, "sokol");
        } else {
            // print out a reason why phase cannot be ended
            System.out.println(checkResult.getMessage());
        }
        // get all phase types
        PhaseType[] allTypes = manager.getAllPhaseTypes();
        // get all phase statuses
        PhaseStatus[] allStatuses = manager.getAllPhaseStatuses();
        // update the project
        manager.updatePhases(project, "ivern");
    }
}
