/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.event;

import com.cronos.onlinereview.util.ConfigHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcoder.management.review.data.Review;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * The client to communicate with TC Event Bus With the REST API.
 * 
 * It's added in Topcoder - Online Review Update - Post to Event BUS v1.0
 * 
 * @author TCCoder
 * @version 1.0
 *
 */
public class EventBusServiceClient {
    /**
     * The logger used to log events
     */
    private static final Logger LOGGER = Logger.getLogger(EventBusServiceClient.class);

    /**
     * The CREATE field
     */
    public static final String CREATE = "CREATE";
    
    /**
     * The UPDATE field
     */
    public static final String UPDATE = "UPDATE";
    
    /**
     * The REVIEW_TYPES_MAP field maps the review type to review type id
     */
    public static Map<String, Long> REVIEW_TYPES_MAP = new HashMap<String, Long>() {
        {
            put("Screening", 1L);
            put("Review", 2L);
            put("Specification Review", 5L);
            put("Checkpoint Screening", 6L);
            put("Checkpoint Review", 7L);
            put("Iterative Review", 8L);
            put("Aggregation Review", 9L);
            put("Final Review", 10L);
            put("Approval", 11L);
            put("Post-Mortem", 12L);
        }
    };

    /**
     * Constructor
     *
     */
    public EventBusServiceClient() {
    }

    /**
     * Fire review create event
     *
     * @param review the review to use
     * @param reviewerId the reviewerId to use
     * @param reviewType the reviewType to use
     */
    public static void fireReviewCreate(Review review, long reviewerId, String reviewType) {
        if (review!= null && reviewType != null && REVIEW_TYPES_MAP.keySet().contains(reviewType.trim())) {
            EventMessage msg = EventMessage.getDefaultReviewEvent();
            msg.setPayload("score", review.getScore());
            msg.setPayload("reviewId", review.getId());
            msg.setPayload("scorecardId", review.getScorecard());
            msg.setPayload("reviewerId", reviewerId);
            msg.setPayload("reviewTypeId", REVIEW_TYPES_MAP.get(reviewType));
            msg.setPayload("eventType", CREATE);
            EventBusServiceClient.fireEvent(msg);
        }
    }

    /**
     * Fire review update event
     *
     * @param review the review to use
     * @param reviewerId the reviewerId to use
     * @param userId the userId to use
     * @param reviewType the reviewType to use
     */
    public static void fireReviewUpdate(Review review, long reviewerId, long userId, String reviewType) {
        if (review!= null && reviewType != null && REVIEW_TYPES_MAP.keySet().contains(reviewType.trim())) {
            EventMessage msg = EventMessage.getDefaultReviewEvent();
            msg.setPayload("score", review.getScore());
            msg.setPayload("reviewId", review.getId());
            msg.setPayload("scorecardId", review.getScorecard());
            msg.setPayload("reviewerId", reviewerId);
            msg.setPayload("userId", Long.toString(userId));
            msg.setPayload("reviewTypeId", REVIEW_TYPES_MAP.get(reviewType));
            msg.setPayload("eventType", UPDATE);
            EventBusServiceClient.fireEvent(msg);
        }
    }

    /**
     * Fire event
     *
     * @param eventMessage the eventMessage to use
     */
    public static void fireEvent(EventMessage eventMessage) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ConfigHelper.getEventBusEndpoint());
            StringEntity entity = new StringEntity(new ObjectMapper().writeValueAsString(eventMessage.getData()), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + ConfigHelper.getEventBusAuthToken());
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
                LOGGER.info("Unable to fire event:" + response.getStatusLine().getReasonPhrase());
            } else {
                LOGGER.debug("successfully fired the event");
            }

        } catch (Exception e) {
            LOGGER.error("Fail to fire event", e);
        }
    }
}
