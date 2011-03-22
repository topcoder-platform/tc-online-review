/**
 * Copyright (C) 2005 - 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.sql.Connection;
import java.sql.SQLException;

import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.project.phases.Phase;

/**
 * The extend from AppealsResponsePhaseHandler to add on the logic to push data to project_result.
 * 
 * <p>
 * Version 1.1 (Online Review Update Review Management Process assembly 2) Change notes:
 * This class has been totally refactor, all the logic to send winner emails are moved to <code>PRHelper.sendMailForWinners</code> method
 * because <code>PRPrimaryReviewAppealResponsePhaseHandler</code> class also need to call this method.
 * </p>
 *
 * @author brain_cn, TCSASSEMBER
 * @version 1.1
 */
public class PRAppealResponsePhaseHandler extends AppealsResponsePhaseHandler {
    /** winners email template source type */
    private String winnersEmailTemplateSource;

    /** winners email template name */
    private String winnersEmailTemplateName;

    /** subject for the winners email */
    private String winnersEmailSubject;

    /** sender address of the winners email */
    private String winnersEmailFromAddress;

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default namespace for loading configuration
     * settings.
     * 
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings.
     */
    public PRAppealResponsePhaseHandler() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given namespace for loading configuration
     * settings.
     * 
     * @param namespace
     *            the namespace to load configuration settings from.
     * @throws ConfigurationException
     *             if errors occurred while loading configuration settings or required properties missing.
     * @throws IllegalArgumentException
     *             if the input is null or empty string.
     */
    public PRAppealResponsePhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        this.winnersEmailTemplateSource = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateSource",
                true);
        this.winnersEmailTemplateName = PhasesHelper
                .getPropertyValue(namespace, "WinnersEmail.EmailTemplateName", true);
        this.winnersEmailSubject = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailSubject", true);
        this.winnersEmailFromAddress = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailFromAddress", true);
    }

    /**
     * Provides additional logic to execute a phase. this exetension will update valid_submission_ind, submit_timestamp
     * field of project_result table.</p>
     * 
     * @param phase
     *            The input phase to check.
     * @param operator
     *            The operator that execute the phase.
     * 
     * @throws PhaseNotSupportedException
     *             if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException
     *             if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException
     *             if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        long projectId = phase.getProject().getId();
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        // Only will perform while submission phase is ended
        Connection conn = this.createConnection();
        try {
            processPR(projectId, conn, toStart);
            // send winner email as appropriate here
            if (!toStart) {
                PRHelper.sendMailForWinners(getManagerHelper(),
                        getManagerHelper().getProjectManager().getProject(projectId), winnersEmailSubject,
                        winnersEmailFromAddress, winnersEmailTemplateSource, winnersEmailTemplateName);
            }
        } catch (Throwable e) {
            throw new PhaseHandlingException(e.getMessage(), e);
        } finally {
            PRHelper.close(conn);
        }
    }

    /**
     * Pull data to project_result.
     * 
     * @param projectId
     *            the projectId
     * @throws PhaseHandlingException
     *             if error occurs
     */
    public void processPR(long projectId, Connection conn, boolean toStart) throws PhaseHandlingException {
        try {
            PRHelper.processAppealResponsePR(projectId, conn, toStart);
        } catch (SQLException e) {
            throw new PhaseHandlingException("Failed to push data to project_result", e);
        }
    }
}
