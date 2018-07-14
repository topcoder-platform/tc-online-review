/*
 * Copyright (C) 2018 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.actions.event;

import com.cronos.onlinereview.model.ProjectPaymentsForm;
import com.cronos.onlinereview.util.ConfigHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.topcoder.management.project.Project;
import com.topcoder.management.review.data.Review;
import com.topcoder.project.phases.Dependency;
import com.topcoder.project.phases.Phase;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * The client to communicate with TC Event Bus With the REST API.
 *
 * It's added in Topcoder - Online Review Update - Post to Event BUS v1.0
 *
 * Version 1.1 - Topcoder - Online Review Update - Post to Event Bus Part 2 v1.0
 * - add more util methods to fire the events for the submission/appeal creation, late deliverable and project update.
 * - fix the fireEvent to handle the Date type for the json serialization
 *
 * @author TCCoder
 * @version 1.1
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
     * The F2F_ASSEMBLY_CODE_ID_MAP field, maps the project category id to the submission type id for f2f/assembly/code
     */
    public static Map<Long, Long> F2F_ASSEMBLY_CODE_ID_MAP = new HashMap<Long, Long>() {
        {
            put(14L, 3L);
            put(38L, 2L);
            put(39L, 1L);
        }
    };

    /**
     * The SUBMISSION_TYPE_ID_FOR_CHECKPOINT_SUBMISSION field
     */
    public static final int SUBMISSION_TYPE_ID_FOR_CHECKPOINT_SUBMISSION = 4;

    /**
     * The SUBMISSION_TYPE_ID_FOR_APPEAL field
     */
    public static final int SUBMISSION_TYPE_ID_FOR_APPEAL = 5;

    /**
     * The SUBMISSION_TYPE_ID_FOR_LATE_DELIVERABLE field
     */
    public static final int SUBMISSION_TYPE_ID_FOR_LATE_DELIVERABLE = 6;

    /**
     * The SUBMISSION_TYPE_ID_FOR_PROJECT_UPDATE field
     */
    public static final int SUBMISSION_TYPE_ID_FOR_PROJECT_UPDATE = 7;

    /**
     * The LATE_DELIVERABLE_TYPE_ID_FOR_USER field
     */
    public static final int LATE_DELIVERABLE_TYPE_ID_FOR_USER = 1;

    /**
     * The LATE_DELIVERABLE_TYPE_ID_FOR_MANAGER field
     */
    public static final int LATE_DELIVERABLE_TYPE_ID_FOR_MANAGER = 2;

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
     * Fire submission create event
     *
     * @param challengeId the challengeId to use
     * @param userId the userId to use
     * @param fileName the fileName to use
     * @param fileUrl the fileUrl to use
     * @param legacyId the legacyId to use
     * @param submissionTypeId the submissionTypeId to use
     */
    public static void fireSubmissionCreateEvent(long challengeId, long userId, String fileName, String fileUrl, long legacyId, int submissionTypeId) {
        EventMessage msg = EventMessage.getDefaultReviewEvent();
        msg.setPayload("challengeId", challengeId);
        msg.setPayload("userId", userId);
        msg.setPayload("submissionTypeId", submissionTypeId);
        msg.setPayload("isFileSubmission", SUBMISSION_TYPE_ID_FOR_APPEAL != submissionTypeId);
        if(SUBMISSION_TYPE_ID_FOR_APPEAL != submissionTypeId) {
            msg.setPayload("fileName", fileName);
            msg.setPayload("fileURL", fileUrl);
        }
        msg.setPayload("legacySubmissionId", legacyId);
        EventBusServiceClient.fireEvent(msg);
    }

    /**
     * Fire late deliverable update event
     *
     * @param challengeId the challengeId to use
     * @param userId the userId to use
     * @param lateDeliverableId the lateDeliverableId to use
     * @param deadline the deadline to use
     * @param explanation the explanation to use
     * @param managerId the managerId to use
     * @param justified the justified to use
     * @param response the response to use
     */
    public static void fireLateDeliverableUpdateEvent(long challengeId, long userId, long lateDeliverableId, Date deadline, String explanation,
            long managerId,  boolean justified, String response) {
        EventMessage msg = EventMessage.getDefaultReviewEvent();
        msg.setPayload("challengeId", challengeId);
        msg.setPayload("userId", userId);
        msg.setPayload("submissionTypeId", SUBMISSION_TYPE_ID_FOR_LATE_DELIVERABLE);
        msg.setPayload("isFileSubmission", false);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("lateDeliverableTypeId", managerId <= 0 ? LATE_DELIVERABLE_TYPE_ID_FOR_USER : LATE_DELIVERABLE_TYPE_ID_FOR_MANAGER);
        data.put("explanation", explanation);
        data.put("deadline", deadline);

        if (managerId > 0) {
            data.put("justified", justified);
            data.put("response", response);
        }
        data.put("userId", managerId <= 0 ? userId : managerId);
        msg.setPayload("data", data);
        msg.setPayload("lateDeliverableId", lateDeliverableId);
        EventBusServiceClient.fireEvent(msg);
    }

    /**
     * Fire project update event
     *
     * @param challengeId the challengeId to use
     * @param userId the userId to use
     * @param project the project to use
     * @param phases the phases to use
     */
    public static void fireProjectUpdateEvent(long challengeId, long userId, Project project, List<Phase> phases) {
        List<Map<String, Object>> convertedPhases = null;
        if (phases != null) {
            convertedPhases = new ArrayList<Map<String, Object>>();
            for (Phase phase : phases) {
                Map<String, Object> phaseMap = convertPhaseToMap(phase, false);
                Set<Map<String, Object>> deps = new HashSet<Map<String, Object>>();
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (Dependency dep : phase.getAllDependencies()) {
                    // for the dependency and dependent phases, only id should be included, as the phases are recursively referenced
                    map.put("dependency", convertPhaseToMap(dep.getDependency(), true));
                    map.put("dependent", convertPhaseToMap(dep.getDependent(), true));
                    map.put("dependencyStart", dep.isDependencyStart());
                    map.put("dependentStart", dep.isDependentStart());
                    map.put("lagTime", dep.getLagTime());
                }
                // ignore the empty dependency
                if (!map.keySet().isEmpty()) {
                    deps.add(map);
                }
                phaseMap.put("dependencies", deps);

                convertedPhases.add(phaseMap);
            }
        }

        EventMessage msg = EventMessage.getDefaultReviewEvent();
        msg.setPayload("challengeId", challengeId);
        msg.setPayload("userId", userId);
        msg.setPayload("submissionTypeId", SUBMISSION_TYPE_ID_FOR_PROJECT_UPDATE);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("project", project);
        data.put("phases", convertedPhases);
        msg.setPayload("data", data);
        EventBusServiceClient.fireEvent(msg);
    }

    /**
     * Fire project update event
     *
     * @param challengeId the challengeId to use
     * @param userId the userId to use
     * @param project the project to use
     * @param phases the phases to use
     */
    public static void fireProjectPaymentUpdateEvent(long challengeId, long userId, ProjectPaymentsForm projectPaymentsForm) {
        EventMessage msg = EventMessage.getDefaultReviewEvent();
        msg.setPayload("challengeId", challengeId);
        msg.setPayload("userId", userId);
        msg.setPayload("submissionTypeId", SUBMISSION_TYPE_ID_FOR_PROJECT_UPDATE);
        msg.setPayload("data", projectPaymentsForm);
        EventBusServiceClient.fireEvent(msg);
    }

    /**
     * Convert phase to map
     *
     * If onlyIdInclude is true, only the phase id will be put in the result
     *
     * @param phase the phase to use
     * @param onlyIdInclude the onlyIdInclude to use
     * @return the Map<String,Object> result
     */
    private static Map<String, Object> convertPhaseToMap(Phase phase, boolean onlyIdInclude) {
        if (phase == null) {
            return null;
        }
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("id", phase.getId());
        if (!onlyIdInclude) {
            map.put("length", phase.getLength());
            map.put("phaseType", phase.getPhaseType());
            map.put("phaseStatus", phase.getPhaseStatus());
            map.put("fixedStartDate", phase.getFixedStartDate());
            map.put("scheduledStartDate", phase.getScheduledStartDate());
            map.put("scheduledEndDate", phase.getScheduledEndDate());
            map.put("actualStartDate", phase.getActualStartDate());
            map.put("actualEndDate", phase.getActualEndDate());
            map.put("modifyDate", phase.getModifyDate());
            if (phase.getAttributes() != null) {
                for (Object key : phase.getAttributes().keySet()) {
                    if (key != null) {
                        map.put(key.toString(), phase.getAttributes().get(key));
                    }
                }
            }
        }

        return map;
    }

    /**
     * Fire event
     *
     * @param eventMessage the eventMessage to use
     */
    public static void fireEvent(EventMessage eventMessage) {
        try {
            LOGGER.debug("will fire event to bus API");

            HttpClient client = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(ConfigHelper.getEventBusEndpoint());

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(dateFormat);
            StringEntity entity = new StringEntity(mapper.writeValueAsString(eventMessage.getData()), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            httpPost.setHeader("Authorization", "Bearer " + ConfigHelper.getEventBusAuthToken());
            HttpResponse response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_NO_CONTENT) {
                LOGGER.info("Unable to fire event:" + response.getStatusLine().getReasonPhrase());
            } else {
                LOGGER.debug("successfully fired the event in topic: " + eventMessage.getData().get("topic"));
            }
        } catch (Exception e) {
            LOGGER.error("Fail to fire event", e);
        }
    }
}
