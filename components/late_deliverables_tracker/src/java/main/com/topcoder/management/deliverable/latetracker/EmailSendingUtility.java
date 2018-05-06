/*
 * Copyright (C) 2010, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker;


import java.text.MessageFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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
import com.topcoder.util.file.templatesource.TemplateSource;
import com.topcoder.util.file.templatesource.TemplateSourceException;
import com.topcoder.util.log.Log;

/**
 * <p>
 * This is a helper utility class that is used by <code>LateDeliverableProcessorImpl</code> and
 * <code>NotRespondedLateDeliverablesNotifier</code> for sending warning email messages to the users who have late
 * deliverables and the managers who need to provide responses for late deliverables respectively. This class supports
 * constructing email message subjects and bodies from templates. It uses Document Generator component for this. This
 * class uses Email Engine component to perform the actual sending of email messages.
 * </p>
 *
 * <p>
 * <em>Changes in 1.2:</em>
 * <ol>
 * <li>Moved from com.topcoder.management.deliverable.latetracker.processors to
 * com.topcoder.management.deliverable.latetracker package.</li>
 * <li>Updated to support loops in email message templates.</li>
 * </ol>
 * </p>
 *
 * <p>
 * Thread Safety: This class is immutable and thread safe. It uses thread safe EmailEngine class.
 * </p>
 *
 * @author saarixx, myxgyy, sparemax
 * @version 1.2
 */
public class EmailSendingUtility {
    /**
     * <p>
     * Represents the name of this class used for logging.
     * </p>
     */
    private static final String CLASS_NAME = EmailSendingUtility.class.getName();

    /**
     * <p>
     * Represents the message of sending email.
     * </p>
     */
    private static final String EMAIL_SEND_MESSAGE = "email sent to {0} at {1}.";

    /**
     * <p>
     * The address of the email sender.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * Cannot be null or empty after initialization.
     * </p>
     * <p>
     * Is used in {@link #sendEmail(String, String, String, Map)}.
     * </p>
     */
    private final String emailSender;

    /**
     * <p>
     * The logger used by this class for logging errors and debug information.
     * </p>
     * <p>
     * Is initialized in the constructor and never changed after that.
     * </p>
     * <p>
     * If is null after initialization, logging is not performed.
     * </p>
     * <p>
     * Is used in {@link #sendEmail(String, String, String, Map)}.
     * </p>
     */
    private final Log log;

    /**
     * Creates an instance of <code>EmailSendingUtility</code>.
     *
     * @param emailSender
     *            the address of the email sender.
     * @param log
     *            the logger used by this class for logging errors and debug information
     *            (null if logging is not required to be performed).
     * @throws IllegalArgumentException
     *             if <code>emailSender</code> is <code>null</code> or empty.
     */
    public EmailSendingUtility(String emailSender, Log log) {
        ExceptionUtils.checkNullOrEmpty(emailSender, null, null,
            "The parameter 'emailSender' should not be null or empty.");
        this.emailSender = emailSender;
        this.log = log;
    }

    /**
     * <p>
     * Sends an email message generated from templates to the specified recipients. The email message is assumed to
     * have HTML content.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Changed the type of params parameter.</li>
     * <li>Updated implementation to make it use NodeListUtility (that supports multi-level data maps).</li>
     * </ol>
     * </p>
     *
     * @param subjectTemplateText
     *            the template text of the email message subject.
     * @param bodyTemplatePath
     *            the resource or file path of the email message body template.
     * @param recipient
     *            the email address of recipient.
     * @param params
     *            the template parameters.
     *
     * @throws IllegalArgumentException
     *             if <code>subjectTemplateText</code> is <code>null</code>, <code>bodyTemplatePath</code> is
     *             <code>null</code> or empty, <code>recipient</code> is <code>null</code> or empty,
     *             <code>params</code> is null or contains <code>null</code> or empty key or <code>null</code> value.
     * @throws EmailSendingException
     *             if some error occurred when sending an email message.
     */
    public void sendEmail(String subjectTemplateText, String bodyTemplatePath, String recipient,
        Map<String, Object> params) throws EmailSendingException {
        final long start = System.currentTimeMillis();
        final String signature = CLASS_NAME
            + ".sendEmail(String subjectTemplateText, String bodyTemplatePath,"
            + " String recipient, Map<String, Object> params)";
        Helper.logEntrance(log, signature, new String[] {"subjectTemplateText", "bodyTemplatePath",
            "recipient", "params"}, new Object[] {subjectTemplateText, bodyTemplatePath, recipient, params});

        try {
            // try for logging purpose

            // check parameters
            ExceptionUtils.checkNull(subjectTemplateText, null, null,
                "The parameter 'subjectTemplateText' should not be null.");
            ExceptionUtils.checkNullOrEmpty(bodyTemplatePath, null, null,
                "The parameter 'bodyTemplatePath' should not be null or empty.");
            ExceptionUtils.checkNullOrEmpty(recipient, null, null,
                "The parameter 'recipient' should not be null or empty.");
            ExceptionUtils.checkNull(params, null, null, "The parameter 'params' should not be null.");

            for (Entry<String, Object> entry : params.entrySet()) {
                ExceptionUtils.checkNullOrEmpty(entry.getKey(), null, null,
                    "The parameter 'params' should not contain null or empty key.");
                ExceptionUtils.checkNull(entry.getValue(), null, null,
                    "The parameter 'params' should not contain null value.");
            }

            DocumentGenerator documentGenerator = new DocumentGenerator();
            TemplateSource fileTemplateSource = new FileTemplateSource();
            documentGenerator.setDefaultTemplateSource(fileTemplateSource);

            // Parse email title template
            Template emailSubjectTemplate;

            try {
                emailSubjectTemplate = documentGenerator.parseTemplate(subjectTemplateText);
            } catch (TemplateFormatException e) {
                throw new EmailSendingException("Fails to parse email subject template.", e);
            }

            // Read and parse email body template
            Template emailBodyTemplate;

            try {
                emailBodyTemplate = documentGenerator.getTemplate(bodyTemplatePath);
            } catch (TemplateSourceException e) {
                throw new EmailSendingException("Fails to read email body template.", e);
            } catch (TemplateFormatException e) {
                throw new EmailSendingException("Fails to parse email body template.", e);
            }

            // Generate email title from template
            String emailSubject = applyTemplate(documentGenerator, emailSubjectTemplate, params); // UPDATED in 1.2

            // Generate email body from template
            String emailBody = applyTemplate(documentGenerator, emailBodyTemplate, params); // UPDATED in 1.2

            // send mail
            sendEmail(emailSubject, emailBody, recipient);
        } catch (IllegalArgumentException e) {
            throw Helper.logException(log, signature, e);
        } catch (EmailSendingException e) {
            throw Helper.logException(log, signature, e);
        }

        Helper.logExit(log, signature, null, start);
    }

    /**
     * <p>
     * Applies the template with given parameters.
     * </p>
     *
     * <p>
     * <em>Changes in 1.2:</em>
     * <ol>
     * <li>Updated implementation to make it use NodeListUtility (that supports multi-level data maps).</li>
     * </ol>
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
     * @throws EmailSendingException
     *             if any error occurs when applying template.
     */
    private static String applyTemplate(DocumentGenerator documentGenerator, Template template,
        Map<String, Object> params) throws EmailSendingException {

        try {
            // Get template fields:
            TemplateFields fields = template.getFields(); // NEW in 1.2
            // Get template nodes:
            Node[] nodes = fields.getNodes(); // NEW in 1.2
            // Create a node list from the obtained nodes:
            NodeList nodeList = new NodeList(nodes); // NEW in 1.2
            // Populate node list with data:
            NodeListUtility.populateNodeList(nodeList, params); // NEW in 1.2

            return documentGenerator.applyTemplate(fields); // UPDATED in 1.2
        } catch (IllegalArgumentException e) {
            throw new EmailSendingException("The data does not contain the proper elements .", e);
        } catch (TemplateFormatException e) {
            throw new EmailSendingException("Template format error occurs when applying template.", e);
        } catch (TemplateDataFormatException e) {
            throw new EmailSendingException("Template data format error occurs when applying template. ", e);
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
     * @throws EmailSendingException
     *             if any error occurs.
     */
    private void sendEmail(String emailSubject, String emailBody, String recipient)
        throws EmailSendingException {
        // Create email message
        TCSEmailMessage message = new TCSEmailMessage();
        // Set message subject
        message.setSubject(emailSubject);
        // Set message body
        message.setBody(emailBody);

        try {
            // Set message sender address
            message.setFromAddress(emailSender);
            // Add the recipient to the message
            message.addToAddress(recipient, TCSEmailMessage.TO);
        } catch (AddressException e) {
            throw new EmailSendingException("Invalid address found.", e);
        }

        // Set content type of the message
        message.setContentType("text/html");

        // Send email message
        try {
            EmailEngine.send(message);
            // log timestamp and recipient if success
            Helper.logInfo(log, MessageFormat.format(EMAIL_SEND_MESSAGE, recipient, new Date()));
        } catch (ConfigManagerException e) {
            throw new EmailSendingException("Config error occurs when sending email via email engine.", e);
        } catch (SendingException e) {
            throw new EmailSendingException("Error occurred when sending email.", e);
        }
    }
}