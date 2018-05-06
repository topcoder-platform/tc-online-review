/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.phase.db.failuretests;

import java.util.Date;
import java.util.Iterator;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.phase.ConfigurationException;
import com.topcoder.management.phase.PhasePersistenceException;
import com.topcoder.management.phase.db.InformixPhasePersistence;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.idgenerator.IDGenerator;
import com.topcoder.util.idgenerator.IDGeneratorFactory;

import junit.framework.TestCase;

/**
 * <p>
 * Failure test for <code>InformixPhasePersistence</code> class.
 * </p>
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class InformixPhasePersistenceFailureTest extends TestCase {
    /**
     * <p>
     * The config file for db connection factory.
     * </p>
     */
    private static String DB_CONFIG = "failuretests/dbconnectionfactory.xml";

    /**
     * <p>
     * The config file for db connection factory.
     * </p>
     */
    private static String CONFIG = "failuretests/dbpersistence.xml";

    /**
     * <p>
     * The connection name for failure test.
     * </p>
     */
    private static final String CONNECTION_NAME = "informix";

    /**
     * <p>
     * The connection factory.
     * </p>
     */
    private DBConnectionFactory factory;

    /**
     * <p>
     * An instance of <code>InformixPhasePersistence</code> for testing.
     * </p>
     */
    private InformixPhasePersistence tester;

    /**
     * <p>
     * An invalid instance of <code>InformixPhasePersistence</code> for testing.
     * </p>
     */
    private InformixPhasePersistence invalidPersistence;

    /**
     * <p>
     * The id generator.
     * </p>
     */
    private IDGenerator generator = null;

    /**
     * <p>
     * Load config file and intializes instance for test.
     * </p>
     */
    protected void setUp() throws Exception {
        super.setUp();
        clearNamespace();
        loadConfig();

        generator = IDGeneratorFactory.getIDGenerator("phase_id_seq");
        factory = new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
        tester = new InformixPhasePersistence(factory, CONNECTION_NAME, generator);
        invalidPersistence = new InformixPhasePersistence(factory, "invalid", generator);
    }

    /**
     * <p>
     * Removes all namespace.
     * </p>
     */
    protected void tearDown() throws Exception {
        clearNamespace();
        super.tearDown();
    }

    /**
     * <p>
     * Remove all namespace.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    private static void clearNamespace() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();
        while (it.hasNext()) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * <p>
     * Load config file for test.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    private static void loadConfig() throws Exception {
        ConfigManager.getInstance().add(DB_CONFIG);
        ConfigManager.getInstance().add(CONFIG);
    }


    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when namespace is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_NamespaceIsNull() throws Exception {
        try {
            new InformixPhasePersistence(null);
            fail("when namespace is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when namespace is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_NamespaceIsEmpty() throws Exception {
        try {
            new InformixPhasePersistence("  \t ");
            fail("when namespace is empty, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when connection name is empty, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionNameIsEmpty() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid1");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when connectionFactory is missed, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryIsMissed() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid2");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when ConnectionFactory.className is missed, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryClassNameIsMissed() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid3");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when ConnectionFactory.namespace is missed, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryNamespaceIsMissed() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid4");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when ConnectionFactory.className is empty , ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryClassNameIsEmpty() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid5");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when ConnectionFactory.className is invalid , ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryClassNameIsInvalid() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid6");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when ConnectionFactory.namespace is invalid, ConfigurationException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit
     */
    public void testCtor1_ConnectionFactoryNamespaceIsInvalid() throws Exception {
        innerTestCtor1("com.topcoder.management.phase.db.invalid7");
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(String namespace),
     * when namespace is invalid, ConfigurationException is expected.
     * </p>
     *
     * @param namespace the namespace to construct InformixPhasePersistence.
     */
    private void innerTestCtor1(String namespace) {
        try {
            new InformixPhasePersistence(namespace);
            fail("when config file is invalid, ConfigurationException is expected.");
        } catch (ConfigurationException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(DBConnectionFactory connectionFactory, String connectionName,
     * IDGenerator idGen), when connectionFactory is null, IllegalArgumentException is expected.
     * </p>
     */
    public void testCtor2_ConnectionFactoryIsNull() {
        try {
            new InformixPhasePersistence(null, CONNECTION_NAME, generator);
            fail("when connectionFactory is null, IllegalArgumentException is expected.");
        }catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(DBConnectionFactory connectionFactory, String connectionName,
     * IDGenerator idGen), when connectionName is empty, IllegalArgumentException is expected.
     * </p>
     */
    public void testCtor2_ConnectionNameIsEmpty() {
        try {
            new InformixPhasePersistence(factory, "  \t ", generator);
            fail("when connectionName is empty, IllegalArgumentException is expected.");
        }catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test ctor InformixPhasePersistence(DBConnectionFactory connectionFactory, String connectionName,
     * IDGenerator idGen), when idGen is null, IllegalArgumentException is expected.
     * </p>
     */
    public void testCtor2_GeneratorIsEmpty() {
        try {
            new InformixPhasePersistence(factory, CONNECTION_NAME, null);
            fail("when connectionName is empty, IllegalArgumentException is expected.");
        }catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getProjectPhases(long projectId),
     * when connection Name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testGetProjectPhases1_ConnectionNameIsInvalid() {
        try {
            invalidPersistence.getProjectPhases(1);
            fail("when connection Name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getProjectPhases(long[] projectIds),
     * when projectIds is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testGetProjectPhases2_ProjectIdsIsNull() throws Exception {
        try {
            tester.getProjectPhases(null);
            fail("when projectIds is null, IllegalArgumentException is expected. ");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getProjectPhases(long[] projectIds),
     * when connection Name is invalid, PhasePersistenceException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testGetProjectPhases2_ConnectionNameIsInvalid() {
        try {
            invalidPersistence.getProjectPhases(new long[] {1});
            fail("when connection Name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAllPhaseTypes(),
     * when connection Name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testGetAllPhaseTypes_ConnectionNameIsInvalid() {
        try {
            invalidPersistence.getAllPhaseTypes();
            fail("when connection Name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getAllPhaseStatuses(),
     * when connection Name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testGetAllPhaseStatuses_ConnectionNameIsInvalid() {
        try {
            invalidPersistence.getAllPhaseStatuses();
            fail("when connection Name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhase(Phase phase, String operator),
     * when phase is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhase_PhaseIsNull() throws Exception {
        try {
            tester.createPhase(null, "tester");
            fail("when phase is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhase(Phase phase, String operator),
     * when operator is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhase_OperatorIsNull() throws Exception {
        Phase phase = createPhase();
        try {
            tester.createPhase(phase, null);
            fail("when operator is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhase(Phase phase, String operator),
     * when operator is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhase_OperatorIsEmpty() throws Exception {
        Phase phase = createPhase();
        try {
            tester.createPhase(phase, "  \t ");
            fail("when operator is empty, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhase(Phase phase, String operator),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testCreatePhase_ConnectionNameIsInvalid() {
        Phase phase = createPhase();
        try {
            invalidPersistence.createPhase(phase, "failureTester");
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhases(Phase[] phases, String operator),
     * when phases is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhases_PhasesIsNull() throws Exception {
        try {
            tester.createPhases(null, "FailureTester");
            fail("when phases is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhases(Phase[] phases, String operator),
     * when phases contains null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhases_PhasesContainNull() throws Exception {
        Phase[] phases = new Phase[] {null};
        try {
            tester.createPhases(phases, "FailureTester");
            fail("when phases contains null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhases(Phase[] phases, String operator),
     * when operator is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhases_OperatorIsNull() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            tester.createPhases(phases, null);
            fail("when operator is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhases(Phase[] phases, String operator),
     * when operator is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhases_OperatorIsEmpty() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            tester.createPhases(phases, "  \t ");
            fail("when operator is empty, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test createPhases(Phase[] phases, String operator),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testCreatePhases_ConnectionNameIsInvalid() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            invalidPersistence.createPhases(phases, "FailureTester");
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test getPhase(long phaseId),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testGetPhase_ConnectionNameIsInvalid() {
        try {
            invalidPersistence.getPhase(1);
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e){
            // good
        }
    }

    /**
     * <p>
     * Test getPhases(long[] phaseIds),
     * when phaseIds is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testGetPhases_PhaseIdsIsNull() throws Exception {
        try {
            tester.getPhases(null);
            fail("when phaseIds is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e){
            // good
        }
    }

    /**
     * <p>
     * Test getPhases(long[] phaseIds),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testGetPhases_ConnectionNameIsInvalid() throws Exception {
        try {
            invalidPersistence.getPhases(new long[] {1});
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e){
            // good
        }
    }

    /**
     * <p>
     * Test updatePhase(Phase phase, String operator),
     * when phase is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhase_PhaseIsNull() throws Exception {
        try {
            tester.updatePhase(null, "FailureTester");
            fail("when phase is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhase(Phase phase, String operator),
     * when operator is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhase_OperatorIsNull() throws Exception {
        Phase phase = createPhase();
        try {
            tester.updatePhase(phase, null);
            fail("when operator is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhase(Phase phase, String operator),
     * when operator is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhase_OperatorIsEmpty() throws Exception {
        Phase phase = createPhase();
        try {
            tester.updatePhase(phase, "  \t ");
            fail("when operator is empty, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhase(Phase phase, String operator),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhase_ConnectionNameIsInvalid() throws Exception {
        Phase phase = createPhase();
        try {
            invalidPersistence.updatePhase(phase, "FailureTester");
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhases(Phase[] phases, String operator),
     * when phases is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhases_PhasesIsNull() throws Exception {
        try {
            tester.updatePhases(null, "failuretester");
            fail("when phases is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhases(Phase[] phases, String operator),
     * when phases contains null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhases_PhasesContainsNull() throws Exception {
        try {
            tester.updatePhases(new Phase[] { null }, "failuretester");
            fail("when phases contains null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhases(Phase[] phases, String operator),
     * when operator is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhases_OperatorIsNull() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            tester.updatePhases(phases, null);
            fail("when operator is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhases(Phase[] phases, String operator),
     * when operator is empty, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhases_OperatorIsEmpty() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            tester.updatePhases(phases, "  \t ");
            fail("when operator is empty, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test updatePhases(Phase[] phases, String operator),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testUpdatePhases_ConnectionNameIsInvalid() throws Exception {
        Phase[] phases = new Phase[] { createPhase() };
        try {
            invalidPersistence.updatePhases(phases, "failuretester");
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test deletePhase(Phase phase),
     * when phase is null, IllegalArgumentException is expected.
     * </p>
     *
     * @throws Exception Exception to JUnit.
     */
    public void testdeletePhase_PhaseIsNull() throws Exception {
        try {
            tester.deletePhase(null);
            fail("when phase is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test deletePhase(Phase phase),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testdeletePhase_ConnectionNameIsInvalid() {
        Phase phase = createPhase();
        try {
            invalidPersistence.deletePhase(phase);
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test deletePhases(Phase[] phases),
     * when phases is null, IllegalArgumentException is expected.
     * </p>
     */
    public void testDeletePhases_PhasesIsNull() throws Exception {
        try {
            tester.deletePhases(null);
            fail("when phases is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test deletePhases(Phase[] phases),
     * when phases contains null, IllegalArgumentException is expected.
     * </p>
     */
    public void testDeletePhases_PhasesContainsNull() throws Exception {
        try {
            tester.deletePhases(new Phase[] { null });
            fail("when phases contains null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test deletePhases(Phase[] phases),
     * when connection name is invalid, PhasePersistenceException is expected.
     * </p>
     */
    public void testDeletePhases_ConnectionNameIsInvalid() throws Exception {
        try {
            invalidPersistence.deletePhases(new Phase[] { createPhase() });
            fail("when connection name is invalid, PhasePersistenceException is expected.");
        } catch (PhasePersistenceException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isNewPhase(Phase phase),
     * when phase is null, IllegalArgumentException is expected.
     * </p>
     */
    public void testIsNewPhase_PhaseIsNull() {
        try{
            tester.isNewPhase(null);
            fail("when phase is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Test isNewDependency(Dependency dependency),
     * when dependency is null, IllegalArgumentException is expected.
     * </p>
     */
    public void testIsNewDependency_DependencyIsNull() throws Exception {
        try{
            tester.isNewDependency(null);
            fail("when dependency is null, IllegalArgumentException is expected.");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * <p>
     * Create a phase for test.
     * </p>
     *
     * @return a phase.
     */
    private Phase createPhase() {
        Project project = new Project(new Date(), new MockWorkDays());
        Phase phase = new Phase(project, 1000 * 3600 * 24);
        phase.setId(1);
        return phase;
    }
}
