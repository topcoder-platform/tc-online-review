/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.latedeliverables;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cronos.onlinereview.Constants;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.UserRetrieval;
import com.cronos.onlinereview.functions.Functions;
import com.cronos.onlinereview.model.DynamicModel;
import com.cronos.onlinereview.util.ActionsHelper;
import com.cronos.onlinereview.util.AuthorizationHelper;
import com.cronos.onlinereview.util.ConfigHelper;
import com.cronos.onlinereview.util.CorrectnessCheckResult;
import com.cronos.onlinereview.util.LoggingHelper;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManager;
import com.topcoder.management.project.Project;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.util.errorhandling.BaseException;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.fieldconfig.Condition;
import com.topcoder.util.file.fieldconfig.Field;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;

/**
 * This class is the struts action class which is used for saving the late deliverable.
 * <p>
 * <b>Thread Safety:</b>Struts 2 Action objects are instantiated for each request, so there are no thread-safety issues.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 */
public class SaveLateDeliverableAction extends BaseLateDeliverableAction {

    /**
     * Represents the serial version id.
     */
    private static final long serialVersionUID = 804444705366245206L;
    /**
     * Represents the late deliverable id.
     */
    private long id;

    /**
     * Creates a new instance of the <code>SaveLateDeliverableAction</code> class.
     */
    public SaveLateDeliverableAction() {
    }

    /**
     * <p>This method is an implementation of &quot;Edit Late Deliverable&quot; Struts Action, which is supposed to
     * show the form for editing the selected late deliverable.</p>
     *
     * @return &quot;success&quot; string that forwards to the /jsp/editLateDeliverable.jsp page (as defined in
     *         struts-config.xml file) in the case of successfully processing, &quot;notAuthorized&quot; forward in the
     *         case of user not being authorized to perform the action.
     * @throws Exception when any error happens while processing in TCS components
     */
    public String execute() throws Exception {
        LoggingHelper.logAction(request);

        CorrectnessCheckResult verification = ActionsHelper.checkThrottle(false, request, this);
        if (!verification.isSuccessful()) {
            return verification.getResult();
        }

        // check user login
        if (!AuthorizationHelper.isUserLoggedIn(request)) {
            AuthorizationHelper.setLoginRedirect(request, false);
            return Constants.NOT_AUTHORIZED_FORWARD_NAME;
        }

        // remove login redirect
        AuthorizationHelper.removeLoginRedirect(request);

        // Get requested late deliverable ID and raise an exception if it is not found
        Long lateDeliverableId = (Long) getModel().get("late_deliverable_id");
        LateDeliverable lateDeliverable;
        if (lateDeliverableId <= 0 || ((lateDeliverable = getLateDeliverable(lateDeliverableId)) == null)) {
            return ActionsHelper.produceErrorReport(this, request,
                    Constants.VIEW_LATE_DELIVERABLE_PERM_NAME, "Error.UnknownLateDeliverable", null);
        } else {
            // Gather the roles the user has for current request and project
            AuthorizationHelper.gatherUserRoles(request, lateDeliverable.getProjectId());

            // Check if user has a permission to edit the late deliverable. The users can edit late deliverables
            // either if the they are granted Edit Late Deliverable permission or if they are the source of the late
            // deliverable
            long lateDeliverableUserId = getLateDeliverableUserId(lateDeliverable);
            long currentUserId = AuthorizationHelper.getLoggedInUserId(request);
            boolean isLateDeliverableOwner = (currentUserId == lateDeliverableUserId);

            boolean canEditLateDeliverable
                = AuthorizationHelper.hasUserPermission(request, Constants.EDIT_LATE_DELIVERABLE_PERM_NAME);

            if (!canEditLateDeliverable && !isLateDeliverableOwner) {
                return ActionsHelper.produceErrorReport(this, request,
                        Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.NoPermission", Boolean.FALSE);
            }

            // Validate that desired data is submitted based on user's role
            String newResponse = null;
            String newExplanation = null;
            boolean newJustified = false;
            if (isLateDeliverableOwner) {
                newExplanation = validateText(request, getModel(), "explanation");
            } else if (canEditLateDeliverable) {
                newJustified = (Boolean) getModel().get("justified");
                // Response may be skipped if it has been already set in DB or if there is no explanation set so far
                if ((lateDeliverable.getExplanation() != null) && (lateDeliverable.getResponse() == null)) {
                    newResponse = validateText(request, getModel(), "response");
                }
            }

            // If there are validation errors then display them; otherwise update the late deliverable
            if (ActionsHelper.isErrorsPresent(request)) {
                setEditLateDeliverableRequest(request, lateDeliverable, isLateDeliverableOwner);
                return INPUT;
            } else {
                boolean dataHasBeenChanged = isLateDeliverableOwner
                                             || canEditLateDeliverable
                                                && ((lateDeliverable.isForgiven() != newJustified)
                                                    || (newResponse != null));

                // Re-read the late deliverable from DB again to get most recent data for late deliverable before
                // updating
                lateDeliverable = getLateDeliverable(lateDeliverableId);
                boolean alreadySet = false;
                boolean lateForExplanation = ActionsHelper.explanationDeadline(lateDeliverable).compareTo(new Date()) < 0;
                if (isLateDeliverableOwner) {
                    if (!lateForExplanation) {
                        if (lateDeliverable.getExplanation() == null) {
                            lateDeliverable.setExplanation(newExplanation);
                            lateDeliverable.setExplanationDate(new Date());
                        } else {
                            alreadySet = true;
                        }
                    }
                } else if (canEditLateDeliverable) {
                    if (lateDeliverable.isForgiven() != newJustified) {
                        lateDeliverable.setForgiven(newJustified);
                    }
                    boolean newResponseSubmitted = (getModel().get("response") != null
                                                    && ((String) getModel().get("response")).length() > 0);
                    if (newResponseSubmitted) {
                        if (lateDeliverable.getResponse() == null) {
                            lateDeliverable.setResponse(newResponse);
                            lateDeliverable.setResponseUser("" + currentUserId);
                            lateDeliverable.setResponseDate(new Date());
                        } else {
                            alreadySet = true;
                        }
                    }
                }

                if (alreadySet) {
                    // Raise an error since explanation/response has been already set for this late deliverable
                    return ActionsHelper.produceErrorReport(this, request,
                            Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.LateDeliverableAlreadyUpdated",
                            Boolean.FALSE);
                } else if (isLateDeliverableOwner && lateForExplanation) {
                    // Raise an error since time given for the explanation has ended
                    return ActionsHelper.produceErrorReport(this, request,
                            Constants.EDIT_LATE_DELIVERABLE_PERM_NAME, "Error.LateDeliverableExplanationLate",
                            Boolean.FALSE);
                } else {

                    // Update the affected properties of the deliverable
                    LateDeliverableManager lateDeliverableManager = ActionsHelper.createLateDeliverableManager();
                    lateDeliverableManager.update(lateDeliverable);

                    // Send notification emails in case any data of late deliverable has been changed
                    if (dataHasBeenChanged) {
                        if (isLateDeliverableOwner) {
                            sendEmailToManagers(lateDeliverable, request);
                        } else if (canEditLateDeliverable) {
                            sendEmailToLateMember(lateDeliverable, request);
                        }
                    }

                    this.setId(lateDeliverable.getId());
                    return Constants.SUCCESS_FORWARD_NAME;
                }
            }
        }
    }

    /**
     * <p>Validates that specified property of specified form is not null and is not empty. If validation fails then
     * binds the error message to request to indicate on validation error.</p>
     *
     * @param request the http request.
     * @param model the action form.
     * @param propertyName a name of form property to validate.
     * @return a value of specified property of the form if it is set or null of the property is not set.
     */
    private static String validateText(HttpServletRequest request, DynamicModel model, String propertyName) {
        String value = (String) model.get(propertyName);
        if ((value == null) || (value.trim().length() == 0)) {
            ActionsHelper.addErrorToRequest(request, propertyName,
                "error.com.cronos.onlinereview.actions.LateDeliverablesActions.emptyText");
            return null;
        } else {
            return value;
        }
    }


    /**
     * This method sets the values of the nodes.
     *
     * @param nodes the nodes in template
     * @param project a <code>Project</code> providing details for current project.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws BaseException if an unexpected error occurs.
     */
    private void setNodes(Node[] nodes, Project project, LateDeliverable lateDeliverable, HttpServletRequest request)
        throws BaseException {
        for (Node node : nodes) {
            if (node instanceof Field) {
                Field field = (Field) node;
                field.setValue(getFieldValue(field.getName(), project, lateDeliverable, request));
            } else if (node instanceof Condition) {
                Condition condition = ((Condition) node);
                condition.setValue(getFieldValue(condition.getName(), project, lateDeliverable, request));

                setNodes(condition.getSubNodes().getNodes(), project, lateDeliverable, request);
            }
        }
    }

    /**
     * <p>Gets field value.</p>
     *
     * @param fieldName the Name of the field in template
     * @param project a <code>Project</code> providing details for current project.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @return a <code>String</code> providing the value of the template field
     * @throws BaseException if an unexpected error occurs.
     */
    private String getFieldValue(String fieldName, Project project, LateDeliverable lateDeliverable,
                                 HttpServletRequest request) throws BaseException {
        if ("PROJECT_NAME".equals(fieldName)) {
            return "" + project.getProperty("Project Name");
        } else if ("PROJECT_VERSION".equals(fieldName)) {
            return "" + project.getProperty("Project Version");
        } else if ("LATE_DELIVERABLE_LINK".equals(fieldName)) {
            return ConfigHelper.getLateDeliverableBaseURL() + lateDeliverable.getId();
        } else if ("OR_LINK".equals(fieldName)) {
            return ConfigHelper.getProjectDetailsBaseURL() + project.getId();
        } else if ("JUSTIFIED".equals(fieldName)) {
            if (lateDeliverable.isForgiven()) {
                return "Yes";
            } else {
                return "No";
            }
        } else if ("EXPLANATION".equals(fieldName)) {
            String explanation = lateDeliverable.getExplanation();
            if (explanation == null) {
                return "N/A";
            } else {
                return Functions.htmlEncode(explanation);
            }
        } else if ("RESPONSE".equals(fieldName)) {
            String response = lateDeliverable.getResponse();
            if (response == null) {
                return "N/A";
            } else {
                return Functions.htmlEncode(response);
            }
        } else if ("DEADLINE".equals(fieldName)) {
            return Functions.displayDate(request, lateDeliverable.getDeadline());
        } else if ("DELIVERABLE_TYPE".equals(fieldName)) {
            return Functions.getDeliverableName(request, lateDeliverable.getDeliverableId());
        } else if ("LATE_DELIVERABLE_TYPE".equals(fieldName)) {
            return lateDeliverable.getType().getName();
        } else if ("LATE_MEMBER_HANDLE".equals(fieldName)) {
            UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
            ExternalUser lateMember = userRetrieval.retrieveUser(getLateDeliverableUserId(lateDeliverable));
            return lateMember.getHandle();
        } else if ("CURRENT_USER_HANDLE".equals(fieldName)) {
            UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
            ExternalUser currentUser = userRetrieval.retrieveUser(AuthorizationHelper.getLoggedInUserId(request));
            return currentUser.getHandle();
        }
        return "";
    }

    /**
     * <p>Sends email to specified recipients on update of specified late deliverable.</p>
     *
     * @param project a <code>Project</code> providing details for project.
     * @param recipients a <code>String</code> list providing the list of email addresses to send email message to.
     * @param ccRecipients a <code>String</code> list providing the list of email addresses to be CCed on the email message.
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param emailTemplateName a <code>String</code> referencing the template for generating the email message body.
     * @param emailSubjectTemplate a <code>String</code> providing the template for subject for the email message.
     * @param fromAddress a <code>String</code> providing the email address to send email from.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailForUsers(Project project, List<String> recipients, List<String> ccRecipients, LateDeliverable lateDeliverable,
                                   String emailTemplateName, String emailSubjectTemplate,
                                   String fromAddress, HttpServletRequest request) throws Exception {
        if (recipients.size() + ccRecipients.size() == 0) {
            return;
        }

        DocumentGenerator docGenerator = new DocumentGenerator();
        docGenerator.setDefaultTemplateSource(new FileTemplateSource());
        Template template = docGenerator.getTemplate(emailTemplateName);
        TemplateFields root = docGenerator.getFields(template);
        setNodes(root.getNodes(), project, lateDeliverable, request);

        String emailContent = docGenerator.applyTemplate(root);
        TCSEmailMessage message = new TCSEmailMessage();
        message.setSubject(MessageFormat.format(emailSubjectTemplate, project.getProperty("Project Name"),
                                                project.getProperty("Project Version")));
        message.setBody(emailContent);
        message.setFromAddress(fromAddress);
        for (String recipient : recipients) {
            message.addToAddress(recipient, TCSEmailMessage.TO);
        }
        for (String ccRecipient : ccRecipients) {
            message.addToAddress(ccRecipient, TCSEmailMessage.CC);
        }
        message.setContentType("text/html");
        EmailEngine.send(message);
    }

    /**
     * <p>Sends an email to late member associated with the specified late deliverable to notify on updates of
     * the late deliverable by one of the managers. The other managers are CCed on the email.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailToLateMember(LateDeliverable lateDeliverable, HttpServletRequest request) throws Exception {
        Project project = ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId());

        String[] recipientRoleNames = ConfigHelper.getLateDeliverableUpdateByManagerRecipientRoleNames();

        List<Long> managerUserIds = ActionsHelper.getUserIDsByRoleNames(recipientRoleNames, project.getId());

        managerUserIds.remove((long) 22719217); // "Components" dummy user
        managerUserIds.remove((long) 22770213); // "Applications" dummy user
        managerUserIds.remove((long) 22873364); // "LCSUPPORT" dummy user

        // Don't send email to the user who is editing the late deliverable.
        managerUserIds.remove(AuthorizationHelper.getLoggedInUserId(request));

        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUserIds);

        // Get the late member's email.
        List<String> lateMemberEmails = new ArrayList<String>();
        UserRetrieval userRetrieval = ActionsHelper.createUserRetrieval(request);
        lateMemberEmails.add( userRetrieval.retrieveUser(getLateDeliverableUserId(lateDeliverable)).getEmail() );

        sendEmailForUsers(project, lateMemberEmails, managerEmails, lateDeliverable,
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailTemplateName(),
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailTemplateSubject(),
                          ConfigHelper.getLateDeliverableUpdateByManagerEmailFromAddress(),
                          request);
    }

    /**
     * <p>Sends an email to managers associated with the specified late deliverable to notify on updates of the late
     * deliverable by the late member.</p>
     *
     * @param lateDeliverable a <code>LateDeliverable</code> which has been updated.
     * @param request an <code>HttpServletRequest</code> representing incoming request.
     * @throws Exception if an unexpected error occurs.
     */
    private void sendEmailToManagers(LateDeliverable lateDeliverable, HttpServletRequest request) throws Exception {
        Project project = ActionsHelper.createProjectManager().getProject(lateDeliverable.getProjectId());

        String[] recipientRoleNames = ConfigHelper.getLateDeliverableUpdateByMemberRecipientRoleNames();

        List<Long> managerUserIds = ActionsHelper.getUserIDsByRoleNames(recipientRoleNames, project.getId());

        managerUserIds.remove((long) 22719217); // "Components" dummy user
        managerUserIds.remove((long) 22770213); // "Applications" dummy user
        managerUserIds.remove((long) 22873364); // "LCSUPPORT" dummy user

        List<String> managerEmails = ActionsHelper.getEmailsByUserIDs(request, managerUserIds);
        sendEmailForUsers(project, managerEmails, new ArrayList<String>(), lateDeliverable,
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailTemplateName(),
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailTemplateSubject(),
                          ConfigHelper.getLateDeliverableUpdateByMemberEmailFromAddress(),
                          request);
    }

    /**
     * Getter of id.
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Setter of id.
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }
}

