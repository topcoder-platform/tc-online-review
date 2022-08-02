/*
 * Copyright (C) 2011 - 2013 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.util;

import com.cronos.onlinereview.Constants;
import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.management.ProjectValidator;
import com.topcoder.onlinereview.component.project.management.ValidationException;
import com.topcoder.onlinereview.component.project.phase.Phase;
import com.topcoder.onlinereview.component.project.phase.PhaseManager;
import com.topcoder.onlinereview.component.project.phase.PhaseStatus;
import com.topcoder.onlinereview.component.resource.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * The Class StatusProjectValidator.
 * Validates project to check if the current state of project allows setting a specific project status.
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class StatusProjectValidator implements ProjectValidator {

    /**
     * represents the max length of name.
     */
    private static final int MAX_LENGTH_OF_NAME = 64;

    /**
     * represents the max length of property key.
     */
    private static final int MAX_LENGTH_OF_PROPERTY_KEY = 64;

    /**
     * represents the max length of description.
     */
    private static final int MAX_LENGTH_OF_DESCRIPTION = 256;

    /**
     * represents the max length of property value.
     */
    private static final int MAX_LENGTH_OF_PROPERTY_VALUE = 4096;

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

    /*
     * @see com.topcoder.management.project.ProjectValidator#validateProject(com.topcoder.management.project.Project)
     */
    public void validateProject(Project project) throws ValidationException {
        if (project == null) {
            throw new IllegalArgumentException("project can not be null.");
        }
        validateStringLength(project.getProjectStatus().getName(), "project status's name", MAX_LENGTH_OF_NAME);
        validateStringLength(project.getProjectCategory().getName(), "project category's name", MAX_LENGTH_OF_NAME);
        validateStringLength(project.getProjectCategory().getProjectType().getName(), "project type's name",
                MAX_LENGTH_OF_NAME);
        validateStringLength(project.getProjectStatus().getDescription(), "project status's description",
                MAX_LENGTH_OF_DESCRIPTION);
        validateStringLength(project.getProjectCategory().getDescription(), "project category's description",
                MAX_LENGTH_OF_DESCRIPTION);
        validateStringLength(project.getProjectCategory().getProjectType().getDescription(),
                "project type's description", MAX_LENGTH_OF_DESCRIPTION);
        // validate each property.
        for (Object item : project.getAllProperties().entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            validateStringLength((String) entry.getKey(), "property key", MAX_LENGTH_OF_PROPERTY_KEY);
            validateStringLength(entry.getValue().toString(), "property value", MAX_LENGTH_OF_PROPERTY_VALUE);
        }

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

    /**
     * This private method is used to validate the given string.<br>
     * check if given string length is less than or equal to given length.
     *
     * @param str
     *            the string to validate
     * @param name
     *            the name of given string
     * @param length
     *            the max length of given string
     * @throws ValidationException
     *             if name or key length greater than given length
     */
    private void validateStringLength(String str, String name, int length) throws ValidationException {
        if (str.length() > length) {
            throw new ValidationException(name + "length must be less than or equal to " + length);
        }
    }

}

