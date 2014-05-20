/*
 * Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import java.text.MessageFormat;

import com.cronos.onlinereview.external.ExternalUser;
import com.topcoder.management.deliverable.Submission;
import com.topcoder.management.deliverable.UploadManager;
import com.topcoder.management.phase.PhaseHandlingException;
import com.topcoder.management.phase.PhaseManagementException;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.resource.search.ResourceFilterBuilder;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.project.phases.Phase;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.log.Level;

/**
 * The extend from AppealsResponsePhaseHandler to add on the logic to push data to project_result.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class PRAppealResponsePhaseHandler extends AppealsResponsePhaseHandler {
    
    /**
    * Used for pulling data to project_result table and filling payments.
    */
    private final PRHelper prHelper = new PRHelper();

    private static final com.topcoder.util.log.Log log = com.topcoder.util.log.LogManager
            .getLog(PRAppealResponsePhaseHandler.class.getName());

    /** constant for "Project Name" project info. */
    private static final String PROJECT_NAME = "Project Name";
    
    /** winners email template name */
    private String winnersEmailTemplateName;
    
    /** subject for the winners email */
    private String winnersEmailSubject;
    
    /** sender address of the winners email */
    private String winnersEmailFromAddress;

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the default namespace for loading configuration settings.
     *
     * @throws ConfigurationException if errors occurred while loading configuration settings.
     */
    public PRAppealResponsePhaseHandler() throws ConfigurationException {
        this(DEFAULT_NAMESPACE);
    }

    /**
     * Create a new instance of AppealsResponsePhaseHandler using the given namespace for loading configuration settings.
     *
     * @param namespace the namespace to load configuration settings from.
     * @throws ConfigurationException if errors occurred while loading configuration settings or required properties
     * missing.
     * @throws IllegalArgumentException if the input is null or empty string.
     */
    public PRAppealResponsePhaseHandler(String namespace) throws ConfigurationException {
        super(namespace);
        obtainWinnersEmailConfigProperties(namespace);
    }
    
    private void obtainWinnersEmailConfigProperties(String namespace) throws ConfigurationException {
        this.winnersEmailTemplateName = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailTemplateName",  true);
        this.winnersEmailSubject = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailSubject", true);
        this.winnersEmailFromAddress = PhasesHelper.getPropertyValue(namespace, "WinnersEmail.EmailFromAddress", true);
    }

    /**
     * Provides additional logic to execute a phase. this extension will update valid_submission_ind, submit_timestamp
     * field of project_result table.</p>
     *
     * @param phase The input phase to check.
     * @param operator The operator that execute the phase.
     *
     * @throws PhaseNotSupportedException if the input phase type is not "Submission" type.
     * @throws PhaseHandlingException if there is any error occurred while processing the phase.
     * @throws IllegalArgumentException if the input parameters is null or empty string.
     */
    public void perform(Phase phase, String operator) throws PhaseHandlingException {
        super.perform(phase, operator);
        boolean toStart = PhasesHelper.checkPhaseStatus(phase.getPhaseStatus());
        // Only will perform while submission phase is ended
        try {
            prHelper.processAppealResponsePR(getManagerHelper(), phase, toStart, operator);
            //send winner email as appropriate here
            if (!toStart) {
                sendMailForWinners(getManagerHelper().getProjectManager().getProject(phase.getProject().getId()));
            }
        } catch (Throwable e) {
            throw new PhaseHandlingException(e.getMessage(), e);
        }
    }

    private void sendMailForWinners(Project project) throws Exception {
       log.log(Level.DEBUG, "we're in the send email method");

       String winnerId = (String) project.getProperty("Winner External Reference ID");
       String runnerUpId = (String) project.getProperty("Runner-up External Reference ID");

       String sendWinnerEmail = (String) project.getProperty("Send Winner Emails");

       if (sendWinnerEmail == null || !sendWinnerEmail.equalsIgnoreCase("true")) {
           return;
       }

        if (winnerId!=null) {
            sendWinnersEmailForUser(project,
                    getManagerHelper().getUserRetrieval().retrieveUser(Long.parseLong(winnerId)), "1st");
        }
        if (runnerUpId!=null) {
            sendWinnersEmailForUser(project,
                    getManagerHelper().getUserRetrieval().retrieveUser(Long.parseLong(runnerUpId)), "2nd");
        }
    }

    private void sendWinnersEmailForUser(Project project, ExternalUser user, String position) throws Exception {
        DocumentGenerator docGenerator = new DocumentGenerator();
        Template template = getEmailTemplate();
        log.log(Level.DEBUG, "sending winner email for projectId: " + project.getId() + " handle: " + user.getHandle() + " position: " + position);
        TemplateFields root = setTemplateFieldValues(docGenerator.getFields(template), project, user, position);
        
        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(winnersEmailSubject, "Online Review", project.getProperty(PROJECT_NAME)));
        message.setBody(emailContent);
        message.setFromAddress(winnersEmailFromAddress);
        message.setToAddress(user.getEmail(), TCSEmailMessage.TO);
        EmailEngine.send(message);
    }
    
    private Resource getResourceForProjectAndUser(Project project, Long userId) throws PhaseManagementException {
        try {
            ResourceRole submitterRole = null;
            ResourceRole[] roles = getManagerHelper().getResourceManager().getAllResourceRoles();
            log.log(Level.DEBUG, "roles size: " + roles.length);
            for (ResourceRole role : roles) {
                log.log(Level.DEBUG, "roles id: " + role.getId() + ", name: " + role.getName());
                if ("Submitter".equals(role.getName())) {
                    submitterRole = role;
                    break;
                }
            }
            if (submitterRole == null) {
                throw new PhaseHandlingException("can't find submitter role id");
            }
            // Create filter to filter only the resources for the project in question
            Filter filterProject = ResourceFilterBuilder.createProjectIdFilter(project.getId());
            Filter filterManager = ResourceFilterBuilder.createResourceRoleIdFilter(submitterRole.getId());
            // Create combined final filter
            Filter filter = new AndFilter(filterProject, filterManager);

            // Perform search for resources
            Resource[] submitters = getManagerHelper().getResourceManager().searchResources(filter);
            for (Resource resource : submitters) {
                if (userId != null && userId.equals(resource.getUserId())) {
                    return resource;
                }
            }
        } catch (Exception e) {
            throw new PhaseManagementException(e.getMessage(), e);
        }
        throw new PhaseHandlingException("couldn't found the resource for userId: " + userId + " projectId: " + project.getId());
    }
    
    private TemplateFields setTemplateFieldValues(TemplateFields root, Project project, ExternalUser user, String position) throws BaseException {
        Node[] nodes = root.getNodes();

        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;

                if ("PROJECT_TYPE".equals(field.getName())) {
                    field.setValue(project.getProjectCategory().getDescription());
                } else if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue((String) project.getProperty(PROJECT_NAME));
                } else if ("SCORE".equals(field.getName())) {
                    // get all the submissions for the user in the project
                    Long[] submissions = getResourceForProjectAndUser(project, user.getId()).getSubmissions();
                    int placement = position.equals("1st") ? 1 : 2;
                    UploadManager uploadManager = getManagerHelper().getUploadManager();
                    for (Long submissionId : submissions) {
                        // get the score for the placement depending on the position
                        Submission submission = uploadManager.getSubmission(submissionId);
                        if (submission.getPlacement() != null && submission.getPlacement() == placement) {
                            field.setValue(submission.getFinalScore() + "");
                            break;
                        }
                    }
                } else if ("PLACE".equals(field.getName())) {
                    field.setValue(position);
                }
            }
        }

        return root;
    }

    private Template getEmailTemplate() throws Exception {
        DocumentGenerator generator = new DocumentGenerator();
        generator.setDefaultTemplateSource(new FileTemplateSource());
        return generator.getTemplate(winnersEmailTemplateName);
    }

}
