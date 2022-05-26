/*
 * Copyright (C) 2014 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.phases;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.topcoder.onlinereview.component.project.management.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static com.topcoder.onlinereview.component.util.SpringUtils.getPropertyValue;

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
     * The logger instance.
     */
    private static final Logger log = LoggerFactory.getLogger(AmazonSNSHelper.class.getName());

    /**
     * Empty private constructor.
     */
    private AmazonSNSHelper() {
        // Do nothing.
    }

    private static String getTopCoderApiBaseUrl() {
        return getPropertyValue(TOPCODER_API_BASE_URL_PROP);
    }

    private static String getAmazonSnsArn() {
        return getPropertyValue(AMAZON_SNS_ARN_PROP);
    }

    private static String getAmazonSnsMessageSubject() {
        return getPropertyValue(AMAZON_SNS_MESSAGE_SUBJECT_PROP);
    }

    private static String getAmazonSnsMessageTemplate() {
        return getPropertyValue(AMAZON_SNS_MESSAGE_TEMPLATE_PROP);
    }

    private static AmazonSNSClient getAmazonSNSClient() {
        ClassLoader loader = AmazonSNSHelper.class.getClassLoader();
        URL credentialURL = loader.getResource(AWS_CREDENTIALS_FILE);
        try {
            return new AmazonSNSClient(new PropertiesCredentials(new File(credentialURL.getFile())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize AmazonSNS.", e);
        }
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
        String topCoderApiBaseUrl = getTopCoderApiBaseUrl();

        String apiURL = "";
        if("Studio".equals(project.getProjectCategory().getProjectType().getName())) {
            apiURL = topCoderApiBaseUrl + "/design/challenges/" + String.valueOf(project.getId());
        } else {
            apiURL = topCoderApiBaseUrl + "/develop/challenges/" + String.valueOf(project.getId());
        }
        String message = getAmazonSnsMessageTemplate().
                replace("%CHALLENGE_ID%", String.valueOf(project.getId())).
                replace("%CMC_TASK_ID%", cmcTaskId).
                replace("%API_URL%", apiURL);

        String subject = getAmazonSnsMessageSubject().replace("%CMC_TASK_ID%", cmcTaskId);

        PublishRequest request = new PublishRequest().
                withSubject(subject).
                withMessage(message).
                withTopicArn(getAmazonSnsArn());

        try {
            PublishResult snsResult = getAmazonSNSClient().publish(request);
            log.debug("Published project change event to Amazon SNS , project ID : " + project.getId() + ", result : " +
                            (snsResult == null ? "null" : snsResult.getMessageId()));
        } catch (Throwable e) {
            // Just log the error, do not rethrow any exception.
            log.error("Failed to publish project change event to Amazon SNS, project ID : " + project.getId());
        }
    } 

}
