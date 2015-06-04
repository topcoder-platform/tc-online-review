/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import java.util.ArrayList;
import java.util.List;

import com.cronos.onlinereview.Constants;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.phase.PhaseManager;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ValidationException;
import com.topcoder.management.project.validation.DefaultProjectValidator;
import com.topcoder.management.resource.Resource;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.util.errorhandling.BaseException;


/**
 * The Class StatusProjectValidator.
 * Validates project to check if the current state of project allows setting a specific project status.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class StatusProjectValidator implements com.topcoder.management.project.ProjectValidator{

    /** The default validator - performs basic validation, if there's no violation,
     * then StatusProjectValidator performs status validation. */
    private final DefaultProjectValidator defaultValidator;

    /** Message for exception related to invalid draft status.*/
    private final String ExcMsgDraftViol = "Can't set status: 'Draft'";
    /** key for invalid draft status exception.*/
    private final String KeyDraftViol = "StatusValidation.Draft";

    /** Message for exception related to invalid Cancelled Failed Review status.*/
    private final String ExcMsgCancelledFailedReviewViol = "Can't set status: 'Cancelled - Failed Review'";
    /** key for exception related to invalid Cancelled Failed Review status.*/
    private final String KeyCancelledFailedReviewViol  = "StatusValidation.CancelledFailedReview";

    /** Message for exception related to invalid Cancelled Failed Screening status.*/
    private final String ExcMsgCancelledFailedScreeningViol = "Can't set status: 'Cancelled - Failed Screening'";
    /** key for exception related to invalid Cancelled Failed Screening status.*/
    private final String KeyCancelledFailedScreeningViol  = "StatusValidation.CancelledFailedScreening";

    /** Message for exception related to invalid Cancelled Zero Submissions status.*/
    private final String ExcMsgCancelledZeroSubmissionsViol = "Can't set status: 'Cancelled - Zero Submissions'";
    /** key for exception related to invalid Cancelled Zero Submissions status.*/
    private final String KeyCancelledZeroSubmissionsViol  = "StatusValidation.CancelledZeroSubmissions";

    /** Message for exception related to invalid Cancelled Winner Unresponsive status.*/
    private final String ExcMsgCancelledWinnerUnresponsiveViol = "Can't set status: 'Cancelled - Winner Unresponsive'";
    /** key for exception related to invalid Cancelled Winner Unresponsive status.*/
    private final String KeyCancelledWinnerUnresponsiveViol  = "StatusValidation.CancelledWinnerUnresponsive";

    /** Message for exception related to invalid Cancelled Zero Registrations status.*/
    private final String ExcMsgCancelledZeroRegistrationsViol = "Can't set status: 'Cancelled - Zero Registrations'";
    /** key for exception related to invalid  Zero Registrations status.*/
    private final String KeyCancelledZeroRegistrationsViol  = "StatusValidation.CancelledZeroRegistrations";

    /** Message for unexpected BaseException thrown during validation.*/
    private final String ExcMsgUnexpectedException = "There was unexpected exception during status validation";
    /** key for unexpected BaseException thrown during validation.*/
    private final String KeyUnexpectedException  = "StatusValidation.UnexpectedException";



    /**
     * Instantiates a new status project validator.
     *
     * @param namespace the namespace
     */
    public StatusProjectValidator(String namespace) {
        defaultValidator = new DefaultProjectValidator(namespace);
    }

    /*
     * @see com.topcoder.management.project.ProjectValidator#validateProject(com.topcoder.management.project.Project)
     */
    public void validateProject(Project project) throws ValidationException {
        defaultValidator.validateProject(project);

        //don't validate new projects
        if(project.getId() == 0) {
            return;
        }

        try {
            Resource[] projectResources =  ActionsHelper.getAllResourcesForProject(project);
            PhaseManager phaseManager = ActionsHelper.createPhaseManager(false);
            Phase[] projectPhases =  ActionsHelper.getPhasesForProject(phaseManager, project);
            String projectStatusName = project.getProjectStatus().getName();

            Submission[] contestSubmissions =  ActionsHelper.getProjectSubmissions(project.getId(),
                    Constants.CONTEST_SUBMISSION_TYPE_NAME, null, false);

            if("Draft".equalsIgnoreCase(projectStatusName)) {
                //make sure phases are added
                if(projectPhases == null || projectPhases.length == 0) {
                    throw new StatusValidationException(ExcMsgDraftViol, KeyDraftViol);
                }
                //make sure all phases scheduled
                for(Phase phase : projectPhases) {
                    if(!arePhaseStatusesEqual(PhaseStatus.SCHEDULED, phase.getPhaseStatus())) {
                        throw new StatusValidationException(ExcMsgDraftViol, KeyDraftViol);
                    }
                }
            }

            if("Cancelled - Failed Review".equalsIgnoreCase(projectStatusName)) {
                //make sure appeals response is closed
                Phase appealsResponsePhase = ActionsHelper.findPhaseByTypeName(projectPhases, Constants.APPEALS_RESPONSE_PHASE_NAME);
                if(appealsResponsePhase != null && !arePhaseStatusesEqual(PhaseStatus.CLOSED, appealsResponsePhase.getPhaseStatus())) {
                    throw new StatusValidationException(ExcMsgCancelledFailedReviewViol, KeyCancelledFailedReviewViol);
                }

                //make sure there's at least one submission
                if(contestSubmissions == null || contestSubmissions.length == 0) {
                    throw new StatusValidationException(ExcMsgCancelledFailedReviewViol, KeyCancelledFailedReviewViol);
                }

                //iterate through submissions and look if all failed
                for(Submission submission : contestSubmissions) {
                    if("Active".equals(submission.getSubmissionStatus().getName())) {
                        throw new StatusValidationException(ExcMsgCancelledFailedReviewViol, KeyCancelledFailedReviewViol);
                    }
                }

            }

            if("Cancelled - Failed Screening".equalsIgnoreCase(projectStatusName)) {
                Phase screeningPhase = ActionsHelper.findPhaseByTypeName(projectPhases, Constants.SCREENING_PHASE_NAME);
                if(screeningPhase == null || !arePhaseStatusesEqual(PhaseStatus.CLOSED,screeningPhase.getPhaseStatus())) {
                    throw new StatusValidationException(ExcMsgCancelledFailedScreeningViol, KeyCancelledFailedScreeningViol);
                }

                //make sure there's at least one submission
                if(contestSubmissions == null || contestSubmissions.length == 0) {
                    throw new StatusValidationException(ExcMsgCancelledFailedScreeningViol, KeyCancelledFailedScreeningViol);
                }

                //iterate through submissions and look if all failed screening
                for(Submission submission : contestSubmissions) {
                    if(!"Failed Screening".equals(submission.getSubmissionStatus().getName()))
                        throw new StatusValidationException(ExcMsgCancelledFailedScreeningViol, KeyCancelledFailedScreeningViol);
                }
            }

            if("Cancelled - Zero Submissions".equalsIgnoreCase(projectStatusName)) {
                Phase submissionPhase = ActionsHelper.findPhaseByTypeName(projectPhases, Constants.SUBMISSION_PHASE_NAME);
                //make sure phase exits and was closed
                if(submissionPhase == null || arePhaseStatusesEqual(PhaseStatus.SCHEDULED, submissionPhase.getPhaseStatus())) {
                    throw new StatusValidationException(ExcMsgCancelledZeroSubmissionsViol, KeyCancelledZeroSubmissionsViol);
                }

                List<Resource> submitterResources = getResourcesByRoleName(projectResources, Constants.SUBMITTER_ROLE_NAME);
                if(submitterResources.size() == 0) {
                    throw new StatusValidationException(ExcMsgCancelledZeroSubmissionsViol, KeyCancelledZeroSubmissionsViol);
                }

                //make sure there are no submissions
                if(contestSubmissions != null && contestSubmissions.length > 0) {
                    throw new StatusValidationException(ExcMsgCancelledZeroSubmissionsViol, KeyCancelledZeroSubmissionsViol);
                }
            }

            if("Cancelled - Winner Unresponsive".equalsIgnoreCase(projectStatusName)) {
                Phase appealsResponsePhase = ActionsHelper.findPhaseByTypeName(projectPhases, Constants.APPEALS_RESPONSE_PHASE_NAME);
                if(appealsResponsePhase == null || !arePhaseStatusesEqual(PhaseStatus.CLOSED, appealsResponsePhase.getPhaseStatus())) {
                    throw new StatusValidationException(ExcMsgCancelledWinnerUnresponsiveViol, KeyCancelledWinnerUnresponsiveViol);
                }
                if(contestSubmissions == null) {
                    throw new StatusValidationException(ExcMsgCancelledWinnerUnresponsiveViol, KeyCancelledWinnerUnresponsiveViol);
                }
                //make sure there's at least one active submission
                boolean existsSubmissionThatPassed = false;
                for(Submission submission : contestSubmissions) {
                    if("Active".equals(submission.getSubmissionStatus().getName())) {
                        existsSubmissionThatPassed = true;
                    }
                }
                if(!existsSubmissionThatPassed) {
                    throw new StatusValidationException(ExcMsgCancelledWinnerUnresponsiveViol, KeyCancelledWinnerUnresponsiveViol);
                }
            }

            if("Cancelled - Zero Registrations".equalsIgnoreCase(projectStatusName)) {
                Phase registrationPhase = ActionsHelper.findPhaseByTypeName(projectPhases, Constants.REGISTRATION_PHASE_NAME);
                //make sure phase exits and was closed
                if(registrationPhase == null || !arePhaseStatusesEqual(PhaseStatus.CLOSED, registrationPhase.getPhaseStatus())) {
                    throw new StatusValidationException(ExcMsgCancelledZeroRegistrationsViol, KeyCancelledZeroRegistrationsViol);
                }
                //make sure there are no registrations
                List<Resource> submitterResources = getResourcesByRoleName(projectResources, Constants.SUBMITTER_ROLE_NAME);
                if(submitterResources.size() > 0) {
                    throw new StatusValidationException(ExcMsgCancelledZeroRegistrationsViol, KeyCancelledZeroRegistrationsViol);
                }
            }
        } catch(StatusValidationException validationException) {
            throw validationException;
        } catch(BaseException exc) {
            //only StatusValidationException can be thrown
            throw new StatusValidationException(ExcMsgUnexpectedException, exc, KeyUnexpectedException);
        }
    }

    /**
     * Are phase statuses equal.
     *
     * @param status1 status instance
     * @param status2 status instance
     * @return true, if equal
     */
    private boolean arePhaseStatusesEqual(PhaseStatus status1, PhaseStatus status2) {
        return status1.getName().equals(status2.getName());
    }

    /**
     * Gets the list of resources by role name.
     *
     * @param resources the list of resources
     * @param roleName the role name for whom list of resources should be returned
     * @return the resources by role name
     */
    private List<Resource> getResourcesByRoleName(Resource[] resources, String roleName) {
        List<Resource> filteredResources = new ArrayList<Resource>();
        for(Resource resource : resources) {
            if(roleName.equals(resource.getResourceRole().getName())) {
                filteredResources.add(resource);
            }
        }
        return filteredResources;
    }

}

