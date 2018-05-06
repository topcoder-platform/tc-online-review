/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.Upload;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogFactory;

/**
 * Tests the version 1.4 newly added or updated methods for <code>PhasesHelper</code> class.
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>Change arePhaseDependenciesMet checkPhaseDependenciesMet.</li>
 * <li>As removing the method getScreeningTasks, remove its test too.</li>
 * </ul>
 * @author myxgyy, microsky
 * @version 1.6.1
 * @since 1.4
 */
public class PhasesHelperTest extends BaseTest {
    /**
     * The log instance.
     */
    private static Log log;

    /**
     * The manager helper.
     */
    private static ManagerHelper helper;

    /**
     * sets up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void setUp() throws Exception {
        super.setUp();

        ConfigManager configManager = ConfigManager.getInstance();

        configManager.add(PHASE_HANDLER_CONFIG_FILE);

        configManager.add(MANAGER_HELPER_CONFIG_FILE);

        // add the component configurations as well
        for (int i = 0; i < COMPONENT_FILE_NAMES.length; i++) {
            configManager.add(COMPONENT_FILE_NAMES[i]);
        }
        helper = new ManagerHelper();
        log = LogFactory.getLog(SpecificationReviewPhaseHandler.class.getName());
    }

    /**
     * cleans up the environment required for test cases for this class.
     * @throws Exception
     *             not under test.
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests the checkPhaseDependenciesMet method. Note only changed functions are tested.
     * <p>
     * If phase B is configured to start when phase A starts (i.e. has a dependency on phase A) the code checks if
     * the phase A is open. if the phase A is already closed. Phase B should start in this case.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckPhaseDependenciesMet1() throws Exception {
        Phase specReview = super.createPhase(2, 1, "Sheduled", 2,
                "Specification Review");
        Phase specSub = createPhase(specReview.getProject(), 1, 1, "Closed", 1,
            "Specification Submission");
        Dependency d = new Dependency(specSub, specReview, true, true, 0);
        specReview.getProject().addPhase(specSub);
        specReview.addDependency(d);

        assertTrue("should return true", PhasesHelper.checkPhaseDependenciesMet(
                specReview, true).isSuccess());
    }

    /**
     * Helper method to create a phase instance.
     * @param project the project.
     * @param phaseId phase id.
     * @param phaseStatusId phase Status Id.
     * @param phaseStatusName phase Status Name.
     * @param phaseTypeId phase Type Id.
     * @param phaseTypeName phase Type Name.
     * @return phase instance.
     */
    private static Phase createPhase(Project project, long phaseId, long phaseStatusId,
        String phaseStatusName, long phaseTypeId,
        String phaseTypeName) {
        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));

        return phase;
    }

    /**
     * Tests the checkPhaseDependenciesMet method. Note only changed functions are tested.
     * <p>
     * If phase B is configured to end when phase A starts. It should end if the phase A is already closed.
     * </p>
     * @throws Exception
     *             to JUnit.
     */
    public void testCheckPhaseDependenciesMet2() throws Exception {
        Phase specReview = super.createPhase(2, 1, "Sheduled", 2,
                "Specification Review");
        Phase specSub = createPhase(specReview.getProject(), 1, 1, "Closed", 1, "Specification Submission");
        Dependency d = new Dependency(specSub, specReview, false, false, 0);
        specReview.getProject().addPhase(specSub);
        specReview.addDependency(d);

        assertTrue("should return true", PhasesHelper.checkPhaseDependenciesMet(
                specReview, false).isSuccess());
    }

    /**
     * Tests the hasOneSpecificationSubmission method. Submission exists and it will return in this case.
     * @throws Exception
     *             to JUnit.
     */
    public void testHasOneSpecificationSubmission1() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            assertNotNull("should have one submission", PhasesHelper.hasOneSpecificationSubmission(phase,
                    helper, getConnection(), log));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the hasOneSpecificationSubmission method. Submission not exists and it will null in this case.
     * @throws Exception
     *             to JUnit.
     */
    public void testHasOneSpecificationSubmission2() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            assertNull("should have one submission", PhasesHelper.hasOneSpecificationSubmission(phase,
                    helper, getConnection(), log));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests the hasOneSpecificationSubmission method. Two submission exist and PhaseHandlingException expected..
     * @throws Exception
     *             to JUnit.
     */
    public void testHasOneSpecificationSubmission_TwoSubmission() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission1 = super.createSubmission(1, upload.getId(), 1, 2);
            Submission submission2 = super.createSubmission(2, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission1, submission2 });

            PhasesHelper.hasOneSpecificationSubmission(phase, helper, getConnection(), log);
            fail("Should have thrown PhaseHandlingException.");
        } catch (PhaseHandlingException e) {
            // pass
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests searchSubmissionsForProject method. Submission with given type should be fetched.
     * @throws Exception to JUnit.
     */
    public void testSearchSubmissionsForProject() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            assertEquals("one submission found", 1,
                    PhasesHelper.searchSubmissionsForProject(helper.getUploadManager(), 1, 2).length);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests searchActiveSubmissions method. Submission with given submission type should be fetched.
     * @throws Exception to JUnit.
     */
    public void testSearchActiveSubmissions() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            // test with scheduled status.
            phase.setPhaseStatus(PhaseStatus.OPEN);

            // insert a submission with specification submission
            Connection conn = getConnection();
            Resource resource = createResource(4, 101L, 1, 17);
            super.insertResources(conn, new Resource[] {resource });

            Upload upload = super.createUpload(1, project.getId(), 4, 1, 1, "parameter");
            super.insertUploads(conn, new Upload[] {upload });

            Submission submission = super.createSubmission(1, upload.getId(), 1, 2);
            super.insertSubmissions(conn, new Submission[] {submission });

            assertEquals("one submission found", 1,
                    PhasesHelper.searchActiveSubmissions(helper.getUploadManager(),
                        conn, 1, "Specification Submission").length);
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests isFirstPhase method. The given phase is not the first phase. False expected.
     * @throws Exception to JUnit.
     */
    public void testiIFirstPhase1() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            assertFalse("The phase is not first phase", PhasesHelper.isFirstPhase(phase));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests isFirstPhase method. The given phase is first phase. True expected.
     * @throws Exception to JUnit.
     */
    public void testiIFirstPhase2() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[0];

            assertTrue("The phase is first phase", PhasesHelper.isFirstPhase(phase));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests insertSpecSubmissionSpecReview method.
     * @throws Exception to JUnit.
     */
    public void testInsertSpecSubmissionSpecReview() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            phase.setPhaseStatus(PhaseStatus.OPEN);

            PhasesHelper.insertSpecSubmissionSpecReview(phase, helper.getPhaseManager(), "123");
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests areParentProjectsCompleted method. The parent project is not completed, false expected.
     * @throws Exception to JUnit.
     */
    @SuppressWarnings("deprecation")
    public void testAreParentProjectsCompleted1() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(false);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            phase.setPhaseStatus(PhaseStatus.OPEN);

            assertFalse("parent project not completed",
                PhasesHelper.areParentProjectsCompleted(phase,
                    new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName()).createConnection(),
                    helper, log));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests areParentProjectsCompleted method. The parent project is completed, true expected.
     * @throws Exception to JUnit.
     */
    @SuppressWarnings("deprecation")
    public void testAreParentProjectsCompleted2() throws Exception {
        try {
            cleanTables();

            Project project = super.setupPhasesForSpec(true);
            Phase[] phases = project.getAllPhases();
            Phase phase = phases[1];

            phase.setPhaseStatus(PhaseStatus.OPEN);

            assertTrue("parent project completed",
                PhasesHelper.areParentProjectsCompleted(phase,
                    new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName()).createConnection(),
                    helper, log));
        } finally {
            cleanTables();
            closeConnection();
        }
    }

    /**
     * Tests getIntegerAttribute method, the phase attribute with given name is null, PhaseHandlingException
     * expected.
     * @throws Exception to JUnit.
     */
    public void testGetIntegerAttribute_NullValue() throws Exception {
        try {
            PhasesHelper.getIntegerAttribute(createPhase(1, 1, "Closed", 1, "Specification Submission"),
                "not exist");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getIntegerAttribute method, the phase attribute with given name can not be parse to integer,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetIntegerAttribute_NotInteger() throws Exception {
        Phase phase = createPhase(1, 1, "Closed", 1, "Specification Submission");
        phase.setAttribute("name", "asss");

        try {
            PhasesHelper.getIntegerAttribute(phase, "name");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getIntegerProp method, the resource property with given name is null, PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetIntegerProp_NullValue() throws Exception {
        try {
            PhasesHelper.getIntegerProp(super.createResource(1L, 1L, 1L, 1L), "not exist");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getIntegerProp method, the resource property with given name can not be parse to integer,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetIntegerProp_NotInteger() throws Exception {
        Resource resource = super.createResource(1L, 1L, 1L, 1L);
        resource.setProperty("name", "asss");

        try {
            PhasesHelper.getIntegerProp(resource, "name");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getIntegerProp method, the resource property with given name is not type of String,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetIntegerProp_NotString() throws Exception {
        Resource resource = super.createResource(1L, 1L, 1L, 1L);
        resource.setProperty("name", new Exception());

        try {
            PhasesHelper.getIntegerProp(resource, "name");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getSubmissionStatus method, the status can not be found,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetSubmissionStatus_StatusNotFound() throws Exception {
        try {
            PhasesHelper.getSubmissionStatus(helper.getUploadManager(), "not found");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getPhaseType method, the phase type can not be found,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetPhaseType_TypeNotFound() throws Exception {
        try {
            PhasesHelper.getPhaseType(helper.getPhaseManager(), "not found");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getPhaseStatus method, the phase status can not be found,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetPhaseStatus_StatusNotFound() throws Exception {
        try {
            PhasesHelper.getPhaseStatus(helper.getPhaseManager(), "not found");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getUploadStatus method, the upload status can not be found,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetUploadStatus_StatusNotFound() throws Exception {
        try {
            PhasesHelper.getUploadStatus(helper.getUploadManager(), "not found");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }

    /**
     * Tests getCommentType method, the comment type can not be found,
     * PhaseHandlingException expected.
     * @throws Exception to JUnit.
     */
    public void testGetCommentType_TypeNotFound() throws Exception {
        try {
            PhasesHelper.getCommentType(helper.getReviewManager(), "not found");
            fail("should have thrown PhaseHandlingException");
        } catch (PhaseHandlingException e) {
            // pass
        }
    }
}