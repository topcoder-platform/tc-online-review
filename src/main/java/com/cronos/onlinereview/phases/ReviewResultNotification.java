/*
 * Copyright (C) 2011 - 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.topcoder.onlinereview.component.deliverable.Submission;
import com.topcoder.onlinereview.component.deliverable.UploadManager;
import com.topcoder.onlinereview.component.document.DocumentGenerator;
import com.topcoder.onlinereview.component.document.Template;
import com.topcoder.onlinereview.component.document.fieldconfig.Field;
import com.topcoder.onlinereview.component.document.fieldconfig.Node;
import com.topcoder.onlinereview.component.document.fieldconfig.TemplateFields;
import com.topcoder.onlinereview.component.document.templatesource.FileTemplateSource;
import com.topcoder.onlinereview.component.email.EmailEngine;
import com.topcoder.onlinereview.component.email.TCSEmailMessage;
import com.topcoder.onlinereview.component.exception.BaseException;
import com.topcoder.onlinereview.component.external.ExternalUser;
import com.topcoder.onlinereview.component.project.management.Project;
import com.topcoder.onlinereview.component.project.phase.ManagerHelper;
import com.topcoder.onlinereview.component.project.phase.PhaseHandlingException;
import com.topcoder.onlinereview.component.project.phase.PhaseManagementException;
import com.topcoder.onlinereview.component.resource.Resource;
import com.topcoder.onlinereview.component.resource.ResourceFilterBuilder;
import com.topcoder.onlinereview.component.resource.ResourceRole;
import com.topcoder.onlinereview.component.search.filter.AndFilter;
import com.topcoder.onlinereview.component.search.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * This class is used to send email notification for winners.
 *
 * <p>
 * Thread-safety: This class is thread-safe as it does not contain any mutable inner state.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ReviewResultNotification {

    private static final Logger log = LoggerFactory.getLogger(ReviewResultNotification.class.getName());

    /** constant for "Project Name" project info. */
    private static final String PROJECT_NAME = "Project Name";

    /** winners email template name */
    private String winnersEmailTemplateName;

    /** subject for the winners email */
    private String winnersEmailSubject;

    /** sender address of the winners email */
    private String winnersEmailFromAddress;

    /**
     * helper class for obtaining several managers
     */
    private ManagerHelper managerHelper;

    public void sendMailForWinners(Project project) throws Exception {
        log.debug( "we're in the send email method");

        String winnerId = (String) project.getProperty("Winner External Reference ID");
        String runnerUpId = (String) project.getProperty("Runner-up External Reference ID");

        String sendWinnerEmail = (String) project.getProperty("Send Winner Emails");

        if (sendWinnerEmail == null || !sendWinnerEmail.equalsIgnoreCase("true")) {
            return;
        }

        if (winnerId!=null) {
            sendWinnersEmailForUser(project,
                    managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(winnerId)), "1st");
        }
        if (runnerUpId!=null) {
            sendWinnersEmailForUser(project,
                    managerHelper.getUserRetrieval().retrieveUser(Long.parseLong(runnerUpId)), "2nd");
        }
    }

    private void sendWinnersEmailForUser(Project project, ExternalUser user, String position) throws Exception {
        DocumentGenerator docGenerator = new DocumentGenerator();
        Template template = getEmailTemplate();
        log.debug( "sending winner email for projectId: " + project.getId() + " handle: " + user.getHandle() + " position: " + position);
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
            ResourceRole[] roles = managerHelper.getResourceManager().getAllResourceRoles();
            log.debug( "roles size: " + roles.length);
            for (ResourceRole role : roles) {
                log.debug( "roles id: " + role.getId() + ", name: " + role.getName());
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
            Resource[] submitters = managerHelper.getResourceManager().searchResources(filter);
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
                    UploadManager uploadManager = managerHelper.getUploadManager();
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

    public void setWinnersEmailTemplateName(String winnersEmailTemplateName) {
        this.winnersEmailTemplateName = winnersEmailTemplateName;
    }

    public void setWinnersEmailSubject(String winnersEmailSubject) {
        this.winnersEmailSubject = winnersEmailSubject;
    }

    public void setWinnersEmailFromAddress(String winnersEmailFromAddress) {
        this.winnersEmailFromAddress = winnersEmailFromAddress;
    }

    public void setManagerHelper(ManagerHelper managerHelper) {
        this.managerHelper = managerHelper;
    }
}
