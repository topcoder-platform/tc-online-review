/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.projectdetails;

import java.util.List;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.functions.Functions;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.project.Project;
import com.topcoder.management.resource.Resource;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;

/**
 * This class is the struts action class which is used for contacting manager page.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class ContactManagerAction extends BaseProjectDetailsAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 613970015458046770L;

    /**
     * Creates a new instance of the <code>ContactManagerAction</code> class.
     */
    public ContactManagerAction() {
    }

    /**
     * This method is an implementation of &quot;Contact Manager&quot; Struts Action defined for
     * this assembly, which is supposed to send a message entered by user to the manager of some
     * project. This action gets executed twice &#x96; once to display the page with the form, and
     * once to process the message entered by user on that form.
     *
     * @return a string forward to the appropriate page. If no error has occurred and this action
     *         was called the first time, the forward will be to contactManager.jsp page, which
     *         displays the form where user can enter his message. If this action was called during
     *         the post back (the second time), then the request should contain the message to send
     *         entered by user. In this case, this method verifies if everything is correct, sends
     *         the message to manager and returns a forward to the View Project Details page.
     * @throws ConfigManagerException
     *             if any error occurs while loading the document generator's configuration.
     * @throws BaseException
     *             if any other error occurs.
     */
    public String execute() throws BaseException, ConfigManagerException {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // Determine if this request is a post back
        boolean postBack = (request.getParameter("postBack") != null);

        // Verify that certain requirements are met before processing with the Action
        verification = ActionsHelper.checkForCorrectProjectId(
                this, request, Constants.CONTACT_PM_PERM_NAME, postBack);
        // If any error has occurred, return action forward contained in the result bean
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        if (!postBack) {
            // Retrieve some basic project info (such as icons' names) and place it into request
            ActionsHelper.retrieveAndStoreBasicProjectInfo(request, verification.getProject(), this);
            return Constants.DISPLAY_PAGE_FORWARD_NAME;
        }
        // Get the ID of the sender
        long senderId = AuthorizationHelper.getLoggedInUserId(request);

        // Obtain an instance of User Retrieval
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        // Retrieve information about an external user by its ID
        ExternalUser sender = userRetrieval.retrieveUser(senderId);

        // Get current project from the verification result bean
        Project project = verification.getProject();

        // Obtain an instance of Document Generator
        DocumentGenerator docGenerator = new DocumentGenerator();
        docGenerator.setDefaultTemplateSource(new FileTemplateSource());

        // Get the template of email
        Template docTemplate = docGenerator.getTemplate(ConfigHelper.getContactManagerEmailTemplate());

        TemplateFields fields = docGenerator.getFields(docTemplate);
        Node[] nodes = fields.getNodes();

        // Construct the body of the email
        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;

                if ("USER_FIRST_NAME".equals(field.getName())) {
                    field.setValue(sender.getFirstName());
                } else if ("USER_LAST_NAME".equals(field.getName())) {
                    field.setValue(sender.getLastName());
                } else if ("USER_HANDLE".equals(field.getName())) {
                    field.setValue(sender.getHandle());
                } else if ("PROJECT_NAME".equals(field.getName())) {
                    field.setValue("" + project.getProperty("Project Name"));
                } else if ("PROJECT_VERSION".equals(field.getName())) {
                    field.setValue("" + project.getProperty("Project Version"));
                } else if ("QUESTION_TYPE".equals(field.getName())) {
                    field.setValue(request.getParameter("cat"));
                } else if ("TEXT".equals(field.getName())) {
                    field.setValue(Functions.htmlEncode(request.getParameter("msg")));
                } else if ("OR_LINK".equals(field.getName())) {
                    field.setValue(ConfigHelper.getProjectDetailsBaseURL() + project.getId());
                } else if ("LIST_OF_ROLES".equals(field.getName())) {
                    StringBuilder roleList = new StringBuilder();
                    Resource[] myResources = (Resource[]) request.getAttribute("myResources");

                    for (Resource resource : myResources) {
                        if (roleList.length() != 0) {
                            roleList.append(", ");
                        }
                        roleList.append(resource.getResourceRole().getName());
                    }
                    field.setValue(roleList.toString());
                }
            }
        }

        // Compose a message to send
        TCSEmailMessage message = new TCSEmailMessage();

        // Add 'TO' addresses to message
        List<Long> managerUsrIds = ActionsHelper.getUserIDsByRoleNames(new String[]{"Manager", "Copilot"}, project.getId());
        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUsrIds);
        for (String managerEmail : managerEmails) {
            message.addToAddress(managerEmail, TCSEmailMessage.TO);
        }

        // Add 'BCC' addresses to message (Client Managers wish to keep their email addresses private)
        List<Long> clientManagerUsrIds = ActionsHelper.getUserIDsByRoleNames(new String[]{"Client Manager"}, project.getId());
        List<String> clientManagerEmails = ActionsHelper.getEmailsByUserIDs(request, clientManagerUsrIds);
        for (String clientManagerEmail : clientManagerEmails) {
            // Don't duplicate addressee.
            if (!managerEmails.contains(clientManagerEmail)) {
                message.addToAddress(clientManagerEmail, TCSEmailMessage.BCC);
            }
        }

        //The sender is CC'd in the mail
        message.addToAddress(sender.getEmail(), TCSEmailMessage.CC);

        // Add 'From' address
        message.setFromAddress(sender.getEmail());
        // Set message's subject
        message.setSubject(project.getProperty("Project Name") + " - " + sender.getHandle());
        // Insert a body into the message
        message.setBody(docGenerator.applyTemplate(fields));

        message.setContentType("text/html");

        // Send an email
        EmailEngine.send(message);
        setPid(project.getId());

        return Constants.SUCCESS_FORWARD_NAME;
    }
}

