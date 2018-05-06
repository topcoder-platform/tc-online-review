/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.failuretests;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import com.cronos.onlinereview.phases.PhaseNotSupportedException;
import com.cronos.onlinereview.phases.PostMortemPhaseHandler;
import com.cronos.onlinereview.phases.failuretests.mock.MockResourceManager;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.project.phases.Project;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Test <code>PostMortemPhaseHandler</code>.
 * </p>
 *
 * @author assistant
 * @version 1.1
 * @since 1.1
 */
public class PostMortemPhaseHandlerFailureTest extends AbstractTestCase {

	/**
	 * The instance to test.
	 */
	private PostMortemPhaseHandler instance;

	/**
	 * The configuration file.
	 */
    public static final String[] CONFIGURATION_FILES = new String[] {
        "failure/DB_Factory.xml",
        "failure/Logging_Wrapper.xml",
        "failure/Phase_Handler.xml",
        "failure/Manager_Helper.xml",
        "failure/Email_Engine.xml",
        "failure/Project_Management.xml",
        "failure/Phase_Management.xml",
        "failure/Review_Management.xml",
        "failure/Scorecard_Management.xml",
        "failure/Screening_Management.xml",
        "failure/Upload_Resource_Search.xml",
        "failure/User_Project_Data_Store.xml",
        "failure/Review_Score_Aggregator.xml",
        "failure/SearchBuilderCommon.xml"};

	/**
	 * Sets up the environment.
	 * @throws Exception to JUnit
	 */
	protected void setUp() throws Exception {
		super.setUp();
        ConfigManager configManager = ConfigManager.getInstance();
        Iterator iter = configManager.getAllNamespaces();
        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
        ConfigHelper.loadConfiguration(new File("failure/config.xml"));
        ConfigHelper.loadConfiguration(new File("failure/Failure.xml"));
        ConfigHelper.loadConfiguration(new File("failure/Phase_Handler.xml"));
        instance = new PostMortemPhaseHandler();
	}

	/**
	 * Cleans up the environment.
	 * @throws Exception to JUnit
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
        ConfigManager configManager = ConfigManager.getInstance();

        Iterator iter = configManager.getAllNamespaces();

        while (iter.hasNext()) {
            configManager.removeNamespace((String) iter.next());
        }
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #PostMortemPhaseHandler(java.lang.String)}.
	 * In this case, the input is null and IllegalArgumentException is expected.
	 * @throws Exception to JUnit
	 */
	public void testPostMortemPhaseHandler_NullInput() throws Exception {
		try {
			new PostMortemPhaseHandler(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #PostMortemPhaseHandler(java.lang.String)}.
	 * In this case, the input is empty and IllegalArgumentException is expected.
	 * @throws Exception to JUnit
	 */
	public void testPostMortemPhaseHandler_EmptyInput() throws Exception {
		try {
			new PostMortemPhaseHandler(" \t \n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #canPerform(com.topcoder.project.phases.Phase)}.
	 * In this case, the phase is null.
	 * @throws Exception to JUnit
	 */
	public void testCanPerform_NullPhase() throws Exception {
		try {
			instance.canPerform(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #canPerform(com.topcoder.project.phases.Phase)}.
	 * In this case, the phase type is wrong.
	 * @throws Exception to JUnit
	 */
	public void testCanPerform_WrongPhaseType() throws Exception {
		try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "INVALID");
			instance.canPerform(phase);
			fail("PhaseNotSupportedException expected");
		} catch (PhaseNotSupportedException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
	 * @throws Exception to JUnit
	 */
	public void testPerform_NullPhase() throws Exception {
		try {
			instance.perform(null, "operator");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
	 * In this case, the operator is null.
	 * @throws Exception to JUnit
	 */
	public void testPerform_NullOperator() throws Exception {
		try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "Post-Mortem");
			instance.perform(phase, null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
	 * In this case, the operator is null.
	 * @throws Exception to JUnit
	 */
	public void testPerform_EmptyOperator() throws Exception {
		try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "Post-Mortem");
			instance.perform(phase, "\t \n");
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {
			// good
		}
	}

	/**
	 * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
	 * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
	 * In this case, the phase type is wrong.
	 * @throws Exception to JUnit
	 */
	public void testPerform_WrongPhaseType() throws Exception {
		try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "review");
			instance.perform(phase, "operator");
			fail("PhaseNotSupportedException expected");
		} catch (PhaseNotSupportedException e) {
			// good
		}
	}

    /**
     * Test method for {@link com.cronos.onlinereview.phases.PostMortemPhaseHandler
     * #perform(com.topcoder.project.phases.Phase, java.lang.String)}.
     * In this case, the phase type is wrong.
     * @throws Exception to JUnit
     */
    public void testPerform_WrongReviewerNumber() throws Exception {
        MockResourceManager.setMethodResult("searchResources_Filter", new Resource[0]);
        try {
            Phase phase = createPhase(1, 1, "Scheduled", 12, "Post-Mortem");
            phase.setAttribute("Reviewer Number", "xyz");
            instance.perform(phase, "operator");
            fail("PhaseHandlingException expected");
        } catch (PhaseHandlingException e) {
            // good
        }
    }

    /**
     * Helper method to create a phase instance.
     *
     * @param phaseId phase id.
     * @param phaseStatusId phase Status Id.
     * @param phaseStatusName phase Status Name.
     * @param phaseTypeId phase Type Id.
     * @param phaseTypeName phase Type Name.
     *
     * @return phase instance.
     */
    private Phase createPhase(long phaseId, long phaseStatusId,
                    String phaseStatusName, long phaseTypeId,
                    String phaseTypeName) {
        Project project = new Project(new Date(), new DefaultWorkdays());
        project.setId(1);
        Phase phase = new Phase(project, 2000);
        phase.setId(phaseId);
        phase.setPhaseStatus(new PhaseStatus(phaseStatusId, phaseStatusName));
        phase.setPhaseType(new PhaseType(phaseTypeId, phaseTypeName));
        return phase;
    }

}
