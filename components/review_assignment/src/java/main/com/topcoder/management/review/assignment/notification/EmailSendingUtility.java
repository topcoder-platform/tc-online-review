/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.review.assignment.notification;

import java.util.Map;
import java.util.Map.Entry;

import com.topcoder.management.review.assignment.ReviewAssignmentNotificationException;
import com.topcoder.management.review.assignment.Helper;
import com.topcoder.message.email.AddressException;
import com.topcoder.message.email.EmailEngine;
import com.topcoder.message.email.SendingException;
import com.topcoder.message.email.TCSEmailMessage;
import com.topcoder.util.config.ConfigManagerException;
import com.topcoder.util.errorhandling.ExceptionUtils;
import com.topcoder.util.file.DocumentGenerator;
import com.topcoder.util.file.Template;
import com.topcoder.util.file.TemplateDataFormatException;
import com.topcoder.util.file.TemplateFormatException;
import com.topcoder.util.file.fieldconfig.Node;
import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.file.fieldconfig.NodeListUtility;
import com.topcoder.util.file.fieldconfig.TemplateFields;
import com.topcoder.util.file.templatesource.FileTemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This is a helper utility class that is used by EmailReviewAssignmentNotificationManager for sending warning
 * email messages. This class supports constructing email message subjects and bodies from templates.
 * </p>
 * <p>
 * It uses Document Generator component for this. This class uses Email Engine component to perform the actual
 * sending of email messages.
 * </p>
 * <p>
 * Thread Safety: This class is immutable and thread safe. It uses thread safe EmailEngine class.
 * </p>
 *
 * @author gevak, zhongqiangzhang
 * @version 1.0
 */
public class EmailSendingUtility {
    /**
     * <p>
     * Represent the class name.
     * </p>
     */
    private static final String CLASS_NAME = EmailSendingUtility.class.getName();

    /**
     * <p>
     * The address of the email sender.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that. Cannot be null or empty after
     * initialization. Is used in sendEmail().
     * </p>
     */
    private final String emailSender;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that. If is null after initialization,
     * logging is not performed. Is used in sendEmail().
     * </p>
     */
    private final Log log;

    /**
     * Creates an instance of EmailSendingUtility.
     *
     * @param emailSender
     *            the address of the email sender
     * @param log
     *            the logger used by this class for logging errors and debug information (null if logging is
     *            not required to be performed)
     *
     * @throws IllegalArgumentException
     *             if emailSender is null or empty
     */
    public EmailSendingUtility(String emailSender, Log log) {
        ExceptionUtils.checkNullOrEmpty(emailSender, null, null,
            "The parameter 'emailSender' should not be null or empty.");
        this.emailSender = emailSender;
        this.log = log;
    }

    /**
     * Sends an email message generated from templates to the specified recipients. The email message is
     * assumed to have HTML content.
     *
     * @param subjectTemplateText
     *            the template text of the email message subject
     * @param bodyTemplatePath
     *            the resource or file path of the email message body template
     * @param recipient
     *            the email address of recipient
     * @param params
     *            the template parameters (String, List and Map values are supported)
     *
     * @throws IllegalArgumentException
     *             if subjectTemplateText is null, bodyTemplatePath is null/empty, recipient is null/empty,
     *             params is null or contains null/empty key or null value
     * @throws ReviewAssignmentNotificationException
     *             if some error occurred when sending an email message
     */
    public void sendEmail(String subjectTemplateText, String bodyTemplatePath, String recipient,
        Map<String, Object> params) throws ReviewAssignmentNotificationException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME + ".sendEmail(String, String, String, Map<String, Object>)";
        Helper.logEntrance(log, signature, new String[] { "subjectTemplateText", "bodyTemplatePath",
            "recipient", "params" },
            new Object[] { subjectTemplateText, bodyTemplatePath, recipient, params });

        // check parameters
        Helper.checkNullIAE(this.log, signature, subjectTemplateText, "subjectTemplateText");
        Helper.checkNullEmptyIAE(this.log, signature, bodyTemplatePath, "bodyTemplatePath");
        Helper.checkNullEmptyIAE(this.log, signature, recipient, "recipient");
        Helper.checkNullIAE(this.log, signature, params, "params");
        for (Entry<String, Object> entry : params.entrySet()) {
            Helper.checkNullIAE(this.log, signature, entry.getValue(), "params|value");
            Helper.checkNullEmptyIAE(this.log, signature, entry.getKey(), "params|key");
        }

        try {
            DocumentGenerator documentGenerator = new DocumentGenerator();
            documentGenerator.setDefaultTemplateSource(new FileTemplateSource());

            // Parse email title template
            Template emailSubjectTemplate;

            try {
                emailSubjectTemplate = documentGenerator.parseTemplate(subjectTemplateText);
            } catch (TemplateFormatException e) {
                throw new ReviewAssignmentNotificationException("Fails to parse email subject template.", e);
            }

            // Read and parse email body template
            Template emailBodyTemplate;

            try {
                emailBodyTemplate = documentGenerator.getTemplate(bodyTemplatePath);
            } catch (TemplateSourceException e) {
                throw new ReviewAssignmentNotificationException("Fails to read email body template.", e);
            } catch (TemplateFormatException e) {
                throw new ReviewAssignmentNotificationException("Fails to parse email body template.", e);
            }

            // Generate email title from template
            String emailSubject = applyTemplate(documentGenerator, emailSubjectTemplate, params);

            // Generate email body from template
            String emailBody = applyTemplate(documentGenerator, emailBodyTemplate, params);

            // send mail
            sendEmail(emailSubject, emailBody, recipient);
        } catch (ReviewAssignmentNotificationException e) {
            throw Helper.logException(log, signature, e);
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * <p>
     * Applies the template with given parameters.
     * </p>
     *
     * @param documentGenerator
     *            document generator to use.
     * @param template
     *            the template.
     * @param params
     *            the template parameters.
     *
     * @return the content after apply the template data.
     *
     * @throws ReviewAssignmentNotificationException
     *             if any error occurs when applying template.
     */
    private static String applyTemplate(DocumentGenerator documentGenerator, Template template,
        Map<String, Object> params) throws ReviewAssignmentNotificationException {

        try {
            // Get template fields:
            TemplateFields fields = template.getFields();

            // Create a node list from the obtained template nodes:
            Node[] nodes = fields.getNodes();
            NodeList nodeList = new NodeList(nodes);

            // Populate node list with data:
            NodeListUtility.populateNodeList(nodeList, params);

            return documentGenerator.applyTemplate(fields);
        } catch (IllegalArgumentException e) {
            throw new ReviewAssignmentNotificationException(
                "The data does not contain the proper elements .", e);
        } catch (TemplateFormatException e) {
            throw new ReviewAssignmentNotificationException(
                "Template format error occurs when applying template.", e);
        } catch (TemplateDataFormatException e) {
            throw new ReviewAssignmentNotificationException(
                "Template data format error occurs when applying template. ", e);
        }
    }

    /**
     * Constructs the email and sends it.
     *
     * @param emailSubject
     *            the email subject.
     * @param emailBody
     *            the email body.
     * @param recipient
     *            the email recipient.
     * @throws ReviewAssignmentNotificationException
     *             if any error occurs.
     */
    private void sendEmail(String emailSubject, String emailBody, String recipient)
        throws ReviewAssignmentNotificationException {
        // Create email message
        TCSEmailMessage message = new TCSEmailMessage();

        // Set message subject and body
        message.setSubject(emailSubject);
        message.setBody(emailBody);

        try {
            // Set message sender address
            message.setFromAddress(emailSender);
            // Add the recipient to the message
            message.addToAddress(recipient, TCSEmailMessage.TO);
        } catch (AddressException e) {
            throw new ReviewAssignmentNotificationException("Invalid address found.", e);
        }

        // Set content type of the message
        message.setContentType("text/html");

        // Send email message
        try {
            EmailEngine.send(message);
        } catch (ConfigManagerException e) {
            throw new ReviewAssignmentNotificationException(
                "Config error occurs when sending email via email engine.", e);
        } catch (SendingException e) {
            throw new ReviewAssignmentNotificationException("Error occurred when sending email.", e);
        }
    }
}
