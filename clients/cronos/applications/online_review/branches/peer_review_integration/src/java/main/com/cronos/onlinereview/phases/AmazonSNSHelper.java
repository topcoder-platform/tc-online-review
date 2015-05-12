/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.topcoder.management.project.Project;
import com.topcoder.util.log.Level;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

import java.io.File;
import java.net.URL;

/**
 * <p>
 * This class contains methods for publishing events to Amazon SNS service.
 * </p>
 *
 * @author TCSASSEMBLER
 * @version 2.0
 **/
public class AmazonSNSHelper {

    /**
     * The default configuration namespace of this class. It is used in the default constructor.
     */
    private static final String NAMESPACE = "com.cronos.OnlineReview";

    /**
     * The AWS credentials file.
     */
    private final static String AWS_CREDENTIALS_FILE = "AwsCredentials.properties";

    /**
     * <p>A <code>String</code> providing the name for Amazon SNS arn.</p>
     */
    private static final String AMAZON_SNS_ARN_PROP = "amazon_sns_arn";

    /**
     * <p>A <code>String</code> providing the name for Amazon SNS message subject.</p>
     */
    private static final String AMAZON_SNS_MESSAGE_SUBJECT_PROP = "amazon_sns_message_subject";

    /**
     * <p>A <code>String</code> providing the name for Amazon SNS message template.</p>
     */
    private static final String AMAZON_SNS_MESSAGE_TEMPLATE_PROP = "amazon_sns_message_template";

    /**
     * <p>A <code>String</code> providing the TopCoder API base URL.</p>
     */
    private static final String TOPCODER_API_BASE_URL_PROP = "topcoder_api_base_url";

    /**
     * The amazon SNS client.
     */
    private static AmazonSNS amazonSNSClient;

    /**
     * The arn used to publish events to amazon SNS.
     */
    private static String amazonSnsArn;

    /**
     * The subject used to publish events to amazon SNS.
     */
    private static String amazonSnsMessageSubject;

    /**
     *  The message template used to publish events to amazon SNS.
     */
    private static String amazonSnsMessageTemplate;

    /**
     *  Root URL for TopCoder API.
     */
    private static String topCoderApiBaseUrl;

    /**
     * The logger instance.
     */
    private static final Log log = LogManager.getLog(AmazonSNSHelper.class.getName());

    /**
     * Static section.
     */
    static {
        try {
            amazonSnsArn = PhasesHelper.getPropertyValue(NAMESPACE, AMAZON_SNS_ARN_PROP, false);
            amazonSnsMessageSubject = PhasesHelper.getPropertyValue(NAMESPACE, AMAZON_SNS_MESSAGE_SUBJECT_PROP, false);
            amazonSnsMessageTemplate = PhasesHelper.getPropertyValue(NAMESPACE, AMAZON_SNS_MESSAGE_TEMPLATE_PROP, false);
            topCoderApiBaseUrl = PhasesHelper.getPropertyValue(NAMESPACE, TOPCODER_API_BASE_URL_PROP, false);

            ClassLoader loader = AmazonSNSHelper.class.getClassLoader();
            URL credentialURL = loader.getResource(AWS_CREDENTIALS_FILE);
            amazonSNSClient = new AmazonSNSClient(new PropertiesCredentials(new File(credentialURL.getFile())));
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize AmazonSNS.", e);
        }
    }

    /**
     * Empty private constructor.
     */
    private AmazonSNSHelper() {
        // Do nothing.
    }

    /**
     * Publishes events to Amazon SNS service signalling about a change with the project.
     * @param project Project that has changed.
     */
    public static void publishProjectUpdateEvent(Project project) {
        // Get the CloudSpokes CMC Task from the project property. If absent, do not publish event.
        String cmcTaskId = (String) project.getProperty("CloudSpokes CMC Task");
        if (cmcTaskId == null || cmcTaskId.trim().length() == 0) {
            return;
        }

        String apiURL = "";
        if("Studio".equals(project.getProjectCategory().getProjectType().getName())) {
            apiURL = topCoderApiBaseUrl + "/design/challenges/" + String.valueOf(project.getId());
        } else {
            apiURL = topCoderApiBaseUrl + "/develop/challenges/" + String.valueOf(project.getId());
        }
        String message = amazonSnsMessageTemplate.
                replace("%CHALLENGE_ID%", String.valueOf(project.getId())).
                replace("%CMC_TASK_ID%", cmcTaskId).
                replace("%API_URL%", apiURL);

        String subject = amazonSnsMessageSubject.replace("%CMC_TASK_ID%", cmcTaskId);

        PublishRequest request = new PublishRequest().
                withSubject(subject).
                withMessage(message).
                withTopicArn(amazonSnsArn);

        try {
            PublishResult snsResult = amazonSNSClient.publish(request);
            log.log(Level.DEBUG,
                    "Published project change event to Amazon SNS , project ID : " + project.getId() + ", result : " +
                            (snsResult == null ? "null" : snsResult.getMessageId()));
        } catch (Throwable e) {
            // Just log the error, do not rethrow any exception.
            log.log(Level.ERROR, e,
                    "Failed to publish project change event to Amazon SNS, project ID : " + project.getId());
        }
    } 

}
