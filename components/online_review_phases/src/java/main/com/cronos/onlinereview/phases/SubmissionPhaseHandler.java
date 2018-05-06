/*
 * Copyright (C) 2009 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.OperationCheckResult;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.project.ProjectManager;
import com.topcoder.management.project.PersistenceException;
import com.topcoder.management.project.ValidationException;

import com.topcoder.project.phases.Phase;

/**
 * <p>
 * This class implements PhaseHandler interface to provide methods to check if a phase can be executed and to add
 * extra logic to execute a phase. It will be used by Phase Management component. It is configurable using an input
 * namespace. The configurable parameters include database connection, email sending and the required number of
 * submissions that pass screening. This class handle the submission phase. If the input is of other phase types,
 * PhaseNotSupportedException will be thrown.
 * </p>
 * <p>
 * The submission phase can start as soon as the dependencies are met and start time is reached, and can stop when
 * the following conditions met:
 * <ul>
 * <li>The dependencies are met</li>
 * <li>The phase's end time is reached.</li>
 * <li>If manual screening is absent, the number of submissions that have passed auto-screening meets the required
 * number;</li>
 * <li>If manual screening is required, the number of submissions that have passed manual screening meets the
 * required number.</li>
 * </ul>
 * </p>
 * <p>
 * Note that screening phase can be started during a submission phase.
 * </p>
 * <p>
 * There is no additional logic for executing this phase.
 * </p>
 * <p>
 * Thread safety: This class is thread safe because it is immutable.
 * </p>
 * <p>
 * Version 1.1 changes note:
 * <li>Insert a post-mortem phase if there is no submission in <code>perform</code>.</li>
 * </p>
 * <p>
 * Version 1.2 changes note:
 * <ul>
 * <li>Added capability to support different email template for different role (e.g. Submitter, Reviewer, Manager,
 * etc).</li>
 * <li>Support for more information in the email generated: for stop, the number of submission and info about
 * Submitter.</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.3 (Online Review End Of Project Analysis Assembly 1.0) Change notes:
 * <ol>
 * <li>Updated {@link #perform(Phase, String)} method to use updated PhasesHelper#insertPostMortemPhase(Project,
 * Phase, ManagerHelper, String) method for creating <code>Post-Mortem</code> phase.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.4 Change notes:
 * <ol>
 * <li>Updated {@link #getAutoScreeningPasses(Phase)} method to create connection before calling
 * PhasesHelper#getScreeningTasks(ManagerHelper, Connection, Phase) method.</li>
 * </ol>
 * </p>
 * <p>
 * Version 1.6.1 changes note:
 * <ul>
 * <li>canPerform() method was updated to return not only true/false value, but additionally an explanation message
 * in case if operation cannot be performed</li>
 * <li>Removed private methods arePassedSubmissionsEnough(), getManualScreeningPasses() and
 * getAutoScreeningPasses().</li>
 * </ul>
 * </p>
 * <p>
 * Version 1.6.2 changes note:
 * <ul>
 * <li>Sets the 'Rated Timestamp' property when the submission phase ends.</li>
 * </ul>
 * </p>
 * @author tuenm, bose_java, argolite, bramandia, saarixx, myxgyy, microsky, ywu
 * @version 1.6.2
 * @since 1.0
 */
public class SubmissionPhaseHandler extends AbstractPhaseHandler {
    /**
     * Represents the default namespace of this class. It is used in the default
     * constructor to load configuration settings.
     */
    public static final String DEFAULT_NAMESPACE = "com.cronos.onlinereview.phases.SubmissionPhaseHandler";

    /**
     * Represents the 'Rated Timestamp' constant as property name.
     */
    private static final String RATED_TIMESTAMP = "Rated Timestamp";

    /**
     * Represents the date formatter used to format the 'Rated Timestamp'.
     */
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("MM.dd.yyyy hh:mm a", Locale.US);

    /**
     * Create a new instance of SubmissionPhaseHandler using the default
     * namespace for loading configuration settings.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings.
     */
    public SubmissionPhaseHandler() throws ConfigurationException {
        super(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of SubmissionPhaseHandler using the given namespace
     * for loading configuration settings.
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading
     *             configuration settings or required properties missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public SubmissionPhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
    }

    /**
     * Check if the input phase can be executed or not. This method will check
     * the phase status to see what will be executed. This method will be called
     * by canStart() and canEnd() methods of PhaseManager implementations in
     * Phase Management component.
     * <p>
     * If the input phase status is Scheduled, then it will check if the phase can be started using the following
     * conditions: the dependencies are met and Its start time is reached.
     * </p>
     * <p>
     * If the input phase status is Open, then it will check if the phase can be stopped using the following
     * conditions:
     * <ul>
     * <li>The dependencies are met</li>
     * <li>The phase's end time is reached.</li>
     * </ul>
     * </p>
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.6.1 changes note:
     * <ul>
     * <li>The return changes from boolean to OperationCheckResult.</li>
     * </ul>
     * </p>
     * @param phase The input phase to check.
     * @return the validation result indicating whether the associated operation can be performed, and if not,
     *         providing a reasoning message (not null)
     * @throws PhaseNotSupportedException if the input phase type is not
     *             &quot;Submission&quot; type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input is null.
     */
    public OperationCheckResult canPerform(Phase phase) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SUBMISSION);

        // will throw exception if phase status is neither "Scheduled" nor "Open"
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());

        if (toStart) {
            return PhasesHelper.checkPhaseCanStart(phase);
        } else {
            if (!PhasesHelper.reachedPhaseEndTime(phase)) {
                return new OperationCheckResult("Phase end time is not yet reached");
            }
            // version 1.1 : can stop if there is no submission
            return PhasesHelper.checkPhaseDependenciesMet(phase, false);
        }
    }

    /**
     * Provides additional logic to execute a phase. This method will be called
     * by start() and end() methods of PhaseManager implementations in Phase
     * Management component. This method can send email to a group of users
     * associated with timeline notification for the project. The email can be
     * send on start phase or end phase base on configuration settings.
     * <p>
     * If the input phase status is Closed, then PhaseHandlingException will be thrown.
     * </p>
     * <p>
     * Version 1.1. changes note: insert a post-mortem phase if there is no submission.
     * </p>
     * <p>
     * Update in version 1.2: Support for more information in the email generated: for stop, the number of
     * submission and info about Submitter.
     * </p>
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     * @throws PhaseNotSupportedException if the input phase type is not
     *             "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while
     *             processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty
     *             string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        PhasesHelper.checkNull(phase, "phase");
        PhasesHelper.checkString(operator, "operator");
        PhasesHelper.checkPhaseType(phase, Constants.PHASE_SUBMISSION);

        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        Map<String, Object> values = new HashMap<String, Object>();
        // Check whether we get submissions, if not, insert post-mortem phase
        if (!toStart && !hasAnySubmission(phase, values)) {
            PhasesHelper.insertPostMortemPhase(phase.getProject(), phase, getManagerHelper(), operator);
        }
        // set the 'Rated Timestamp' property when the submission phase ends
        if (!toStart) {
            try {
                ProjectManager projectManager = getManagerHelper().getProjectManager();
                com.topcoder.management.project.Project project = projectManager.getProject(phase.getProject().getId());
                project.setProperty(RATED_TIMESTAMP, DATE_FORMATTER.format(new Date()));
                projectManager.updateProject(project, "Update the rated timestamp.", operator);
            } catch (PersistenceException e) {
                throw new PhaseHandlingException("Cannot save the Rated Timestamp for project : "
                    + phase.getProject().getId(), e);
            }  catch (ValidationException e) {
                throw new PhaseHandlingException("Validation failed for project : "
                    + phase.getProject().getId(), e);
            }
        }

        sendPhaseEmails(phase, values);
    }

    /**
     * This method checks whether there is any submission in submission phase.
     * <p>
     * Update in version 1.2: Support for more information in the email generated: for stop, the number of
     * submission and info about Submitter.
     * </p>
     * @param phase the phase to check.
     * @param values the values map to hold the information for email generation
     * @return true if there is at least one submission, false otherwise.
     * @throws PhaseHandlingException if any error occurs during processing.
     */
    private boolean hasAnySubmission(Phase phase, Map<String, Object> values) throws PhaseHandlingException {
        Submission[] subs = PhasesHelper.getActiveProjectSubmissions(getManagerHelper().getUploadManager(),
                phase.getProject().getId(), Constants.SUBMISSION_TYPE_CONTEST_SUBMISSION);

        // for stop phase, we are going to support more information now from version 1.2
        if (values != null) {
            values.put("N_SUBMITTERS", subs.length);
            values.put("SUBMITTER", PhasesHelper.constructSubmitterValues(
                                    subs, getManagerHelper().getResourceManager(), false));
        }
        return subs.length > 0;
    }

}