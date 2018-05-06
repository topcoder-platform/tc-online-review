/*
 * Copyright (C) 2009 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases.accuracytests;

import com.cronos.onlinereview.phases.AbstractPhaseHandler;
import com.cronos.onlinereview.phases.EmailOptions;

import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;

import com.topcoder.project.phases.Phase;

import java.util.HashMap;
import java.util.Map;


/**
 * Accuracy tests for V1.3 <code>AbstractPhaseHandler</code>.
 *
 * @author assistant, gjw99
 * @version 1.2
 */
public class AbstractPhaseHandlerTestV13 extends BaseTestCase {
    /** Instance to test. */
    private AbstractPhaseHandler instance;

    /**
     * Sets up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        super.setUp();
        instance = new AbstractPhaseHandler("com.cronos.onlinereview.phases.AbstractPhaseHandler") {
                    public OperationCheckResult canPerform(Phase arg0)
                        throws PhaseHandlingException {
                        return new OperationCheckResult(false, "Some requirements are not met");
                    }

                    public void perform(Phase arg0, String arg1)
                        throws PhaseHandlingException {
                    }
                };
    }

    /**
     * Cleans up the environment.
     *
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.AbstractPhaseHandler
     * #AbstractPhaseHandler(java.lang.String)}.
     */
    @SuppressWarnings("unchecked")
    public void testAbstractPhaseHandler() {
        // check whether the configuration about email is loaded
        Map<String, EmailOptions> startPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(AbstractPhaseHandler.class,
                instance, "startPhaseEmailOptions");
        assertNotNull("The configuration is not correct.", startPhaseEmailOptions);
        assertEquals("The configuration is not correct.", 2, startPhaseEmailOptions.size());
        assertEquals("The value is invalid.", "Phase Start", startPhaseEmailOptions.get("Manager").getSubject());
        assertNotNull("The value is invalid.", startPhaseEmailOptions.get("Reviewer"));
        assertEquals("The value is invalid.", 0, startPhaseEmailOptions.get("Reviewer").getPriority());

        // check end phase email options
        Map<String, EmailOptions> endPhaseEmailOptions = (Map<String, EmailOptions>) getPrivateField(AbstractPhaseHandler.class,
                instance, "endPhaseEmailOptions");
        assertNotNull("The configuration is not correct.", endPhaseEmailOptions);
        assertEquals("The configuration is not correct.", 2, endPhaseEmailOptions.size());
        assertEquals("The value is invalid.", "Phase End", endPhaseEmailOptions.get("Manager").getSubject());
        assertNotNull("The value is invalid.", endPhaseEmailOptions.get("Reviewer"));
        assertEquals("The value is invalid.", 0, endPhaseEmailOptions.get("Reviewer").getPriority());
    }

    /**
     * Test method for {@link com.cronos.onlinereview.phases.AbstractPhaseHandler
     * #sendEmail(com.topcoder.project.phases.Phase, java.util.Map)}. Because it's hard to test the email sent, the
     * email content is checked manually.
     *
     * @throws Exception to JUnit
     */
    @SuppressWarnings("unchecked")
    public void testSendEmail() throws Exception {
        // set up the notification resources. roles. projects. phases
        setupProjectResourcesNotification("All", false);

        Phase phase = createPhase(1, 2, "Open", 1, "Registration");

        try {
            instance.sendEmail(phase, new HashMap());

            // pass
        } catch (Exception e) {
        	throw e;
        }
    }
}
