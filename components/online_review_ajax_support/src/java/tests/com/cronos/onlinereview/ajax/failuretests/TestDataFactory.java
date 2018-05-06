/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.ajax.failuretests;

import com.cronos.onlinereview.ajax.failuretests.mock.MockReader;
import com.cronos.onlinereview.ajax.failuretests.mock.MockServletConfig;
import com.topcoder.management.resource.NotificationType;
import com.topcoder.management.resource.ResourceRole;
import com.topcoder.management.review.data.CommentType;
import com.topcoder.management.scorecard.data.ScorecardStatus;
import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.PhaseType;

import javax.servlet.ServletConfig;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>A factory producing the sample data which could be used for testing purposes. This class provides a set of static
 * constants and a set of static methods producing the sample data. Note that the methods produce a new copy of sample
 * data on each method call.</p>
 *
 * @author isv
 * @version 1.0
 */
public class TestDataFactory {

    /**
     *
     */
    public static final Object NULL = null;

    /**
     *
     */
    public static final String ZERO_LENGTH_STRING = "";

    /**
     *
     */
    public static final String WHITESPACE_ONLY_STRING = " \t \n \t ";

    /**
     *
     */
    public static final String VALID_AJAX_REQUEST_TYPE = "TODO";

    /**
     *
     */
    public static final Map VALID_AJAX_REQUEST_PARAMETERS = getValidAjaxRequestParameters();

    /**
     *
     */
    public static final String AJAX_RESPONSE_SUCCESS_STATUS = "success";

    /**
     *
     */
    public static final String MANAGER_ROLE = "Manager";


    /**
     * <p>Generates a new instance of <code>Reader</code> type initialized with random data.</p>
     *
     * @return a new <code>Reader</code> instance.
     */
    public static Reader getAjaxRequestMalformedXmlReader() {
        return (Reader) new StringReader("<?xml version=\"1.0\"?><request type=\"SetScorecardStatus\"><parameters>"
                                         + "</request></parameters>");
    }

    /**
     * <p>Generates a new instance of <code>Reader</code> type initialized with random data.</p>
     *
     * @return a new <code>Reader</code> instance.
     */
    public static Reader getAjaxRequestInvalidXmlReader() {
        return (Reader) new StringReader("This is not a well-formed XML document");
    }

    /**
     * <p>Generates a new instance of <code>Reader</code> type initialized with random data.</p>
     *
     * @return a new <code>Reader</code> instance.
     */
    public static Reader getAjaxRequestInvalidIOReader() {
        MockReader result = new MockReader();
        MockReader.throwGlobalException(new IOException());
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Map</code> type initialized with random data.</p>
     *
     * @return a new <code>Map</code> instance.
     */
    public static Map getValidAjaxRequestParameters() {
        Map result = new HashMap();
        result.put("type", "SetScorecardStatus");
        result.put("ScorecardId", "1");
        result.put("Status", "Active");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ResourceRole[]</code> type initialized with random data.</p>
     *
     * @return a new <code>ResourceRole[]</code> instance.
     */
    public static ResourceRole[] getResourceRolesWithoutManagerRole() {
        ResourceRole[] result = new ResourceRole[1];
        result[0] = new ResourceRole();
        result[0].setId(1);
        result[0].setName("MANAGER");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ResourceRole[]</code> type initialized with random data.</p>
     *
     * @return a new <code>ResourceRole[]</code> instance.
     */
    public static ResourceRole[] getResourceRoles() {
        ResourceRole[] result = new ResourceRole[1];
        result[0] = new ResourceRole();
        result[0].setId(1);
        result[0].setName("Manager");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>CommentType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>CommentType[]</code> instance.
     */
    public static CommentType[] getCommentTypes() {
        CommentType[] result = new CommentType[2];
        result[0] = new CommentType();
        result[0].setId(1);
        result[0].setName("Appeal Response");
        result[1] = new CommentType();
        result[1].setId(2);
        result[1].setName("Appeal");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>CommentType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>CommentType[]</code> instance.
     */
    public static CommentType[] getCommentTypesWithoutAppeal() {
        CommentType[] result = new CommentType[1];
        result[0] = new CommentType();
        result[0].setId(1);
        result[0].setName("Appeal Response");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>CommentType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>CommentType[]</code> instance.
     */
    public static CommentType[] getCommentTypesWithoutAppealResponse() {
        CommentType[] result = new CommentType[1];
        result[0] = new CommentType();
        result[0].setId(1);
        result[0].setName("Appeal");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType[]</code> instance.
     */
    public static PhaseType[] getPhaseTypesWithoutReview() {
        PhaseType[] result = new PhaseType[2];
        result[0] = new PhaseType(1, "Appeal");
        result[0].setId(1);
        result[0].setName("Appeal");
        result[1] = new PhaseType(2, "Appeal Response");
        result[1].setId(2);
        result[1].setName("Appeal Response");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType[]</code> instance.
     */
    public static PhaseType[] getPhaseTypesWithoutAppeal() {
        PhaseType[] result = new PhaseType[2];
        result[0] = new PhaseType(1, "Review");
        result[0].setId(1);
        result[0].setName("Review");
        result[1] = new PhaseType(2, "Appeal Response");
        result[1].setId(2);
        result[1].setName("Appeal Response");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType[]</code> instance.
     */
    public static PhaseType[] getPhaseTypesWithoutAppealResponse() {
        PhaseType[] result = new PhaseType[2];
        result[0] = new PhaseType(1, "Review");
        result[0].setId(1);
        result[0].setName("Review");
        result[1] = new PhaseType(2, "Appeal");
        result[1].setId(2);
        result[1].setName("Appeal");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType[]</code> instance.
     */
    public static PhaseType[] getPhaseTypes() {
        PhaseType[] result = new PhaseType[3];
        result[0] = new PhaseType(1, "Review");
        result[0].setId(1);
        result[0].setName("Review");
        result[1] = new PhaseType(2, "Appeal Response");
        result[1].setId(2);
        result[1].setName("Appeal Response");
        result[2] = new PhaseType(3, "Appeals");
        result[2].setId(2);
        result[2].setName("Appeals");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseStatus[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseStatus[]</code> instance.
     */
    public static PhaseStatus[] getPhaseStatusesWithoutOpen() {
        PhaseStatus[] result = new PhaseStatus[2];
        result[0] = new PhaseStatus(1, "Closed");
        result[0].setId(1);
        result[0].setName("Closed");
        result[1] = new PhaseStatus(2, "Cancelled");
        result[1].setId(2);
        result[1].setName("Cancelled");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>PhaseStatus[]</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseStatus[]</code> instance.
     */
    public static PhaseStatus[] getPhaseStatuses() {
        PhaseStatus[] result = new PhaseStatus[2];
        result[0] = new PhaseStatus(1, "Open");
        result[0].setId(1);
        result[0].setName("Open");
        result[1] = new PhaseStatus(2, "Cancelled");
        result[1].setId(2);
        result[1].setName("Cancelled");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ScorecardStatus[]</code> type initialized with random data.</p>
     *
     * @return a new <code>ScorecardStatus[]</code> instance.
     */
    public static ScorecardStatus[] getScorecardStatusesWithoutActive() {
        ScorecardStatus[] result = new ScorecardStatus[1];
        result[0] = new ScorecardStatus();
        result[0].setId(1);
        result[0].setName("Inactive");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ScorecardStatus[]</code> type initialized with random data.</p>
     *
     * @return a new <code>ScorecardStatus[]</code> instance.
     */
    public static ScorecardStatus[] getScorecardStatuses() {
        ScorecardStatus[] result = new ScorecardStatus[2];
        result[0] = new ScorecardStatus();
        result[0].setId(1);
        result[0].setName("Inactive");
        result[1] = new ScorecardStatus();
        result[1].setId(2);
        result[1].setName("Active");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ScorecardStatus[]</code> type initialized with random data.</p>
     *
     * @return a new <code>ScorecardStatus[]</code> instance.
     */
    public static ScorecardStatus[] getScorecardStatusesWithoutInactive() {
        ScorecardStatus[] result = new ScorecardStatus[1];
        result[0] = new ScorecardStatus();
        result[0].setId(1);
        result[0].setName("Active");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>NotificationType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>NotificationType[]</code> instance.
     */
    public static NotificationType[] getNotificationTypesWithoutTimelineNotification() {
        NotificationType[] result = new NotificationType[3];
        result[0] = new NotificationType();
        result[0].setId(1);
        result[0].setName("Notification 1");
        result[1] = new NotificationType();
        result[1].setId(2);
        result[1].setName("Notification 2");
        result[2] = new NotificationType();
        result[2].setId(3);
        result[2].setName("Notification 3");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>NotificationType[]</code> type initialized with random data.</p>
     *
     * @return a new <code>NotificationType[]</code> instance.
     */
    public static NotificationType[] getNotificationTypes() {
        NotificationType[] result = new NotificationType[1];
        result[0] = new NotificationType();
        result[0].setId(1);
        result[0].setName("Timeline Notification");
        return result;
    }

    /**
     * <p>Generates a new instance of <code>ServletConfig</code> type initialized with random data.</p>
     *
     * @return a new <code>ServletConfig</code> instance.
     */
    public static ServletConfig getServletConfig() {
        return (ServletConfig) new MockServletConfig();
    }

    /**
     * <p>Generates a new map which contains a <code>null</code> key among the keys of <code>String</code> type.</p>
     *
     * @return a <code>Map</code> containing <code>null</code> key among the keys of <code>String</code> type.
     */
    public static Map getStringStringMapWithNullKey() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(TestDataFactory.NULL, String.valueOf(System.currentTimeMillis()));
            } else {
                result.put(String.valueOf(System.currentTimeMillis() + 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new map which contains an invalid key (of correct type) among the keys of <code>String</code>
     * type.</p>
     *
     * @return a <code>Map</code> containing an invalid key among the keys of <code>String</code> type.
     */
    public static Map getStringStringMapWithZeroLengthKey() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(TestDataFactory.ZERO_LENGTH_STRING, String.valueOf(System.currentTimeMillis()));
            } else {
                result.put(String.valueOf(System.currentTimeMillis()+ 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new map which contains an invalid key (of correct type) among the keys of <code>String</code>
     * type.</p>
     *
     * @return a <code>Map</code> containing an invalid key among the keys of <code>String</code> type.
     */
    public static Map getStringStringMapWithWhitespaceKey() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(TestDataFactory.WHITESPACE_ONLY_STRING, String.valueOf(System.currentTimeMillis()));
            } else {
                result.put(String.valueOf(System.currentTimeMillis() + 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new map which contains a <code>null</code> value among the values of <code>String</code>
     * type.</p>
     *
     * @return a <code>Map</code> containing <code>null</code> value among the values of <code>String</code> type.
     */
    public static Map getStringStringMapWithNullValue() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(String.valueOf(System.currentTimeMillis()), TestDataFactory.NULL);
            } else {
                result.put(String.valueOf(System.currentTimeMillis() + 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new map which contains a key of incompatbile type among the keys of <code>String</code> type.</p>
     *
     * @return a <code>Map</code> containing a key of incompatible type among the keys of <code>String</code> type.
     */
    public static Map getStringStringMapWithNonSrtringKey() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(new Object(), String.valueOf(System.currentTimeMillis()));
            } else {
                result.put(String.valueOf(System.currentTimeMillis() + 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Generates a new map which contains a value of incompatbile type among the values of <code>String</code>
     * type.</p>
     *
     * @return a <code>Map</code> containing a value of incompatible type among the values of <code>String</code> type.
     */
    public static Map getStringStringMapWithNonSrtringValue() {
        Map result = new HashMap();
        for (int i = 0; i < 10; i++) {
            delay();
            if (i % 5 == 0) {
                result.put(String.valueOf(System.currentTimeMillis()), new Object());
            } else {
                result.put(String.valueOf(System.currentTimeMillis() + 1),
                           String.valueOf(System.currentTimeMillis() + 3));
            }
        }
        return result;
    }

    /**
     * <p>Delays the current execution thread for 10 milliseconds.</p>
     */
    private static void delay() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
    }
}
