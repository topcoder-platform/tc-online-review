/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */

package com.cronos.onlinereview.phases.failuretests;

import com.topcoder.project.phases.PhaseStatus;
import com.topcoder.project.phases.Phase;
import com.topcoder.project.phases.Project;
import com.topcoder.project.phases.PhaseType;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.cronos.onlinereview.phases.failuretests.mock.MockConnection;

import java.util.Date;
import java.sql.Connection;

/**
 * <p>A factory producing the sample data which could be used for testing purposes. This class provides a set of static
 * constants and a set of static methods producing the sample data. Note that the methods produce a new copy of sample
 * data on each method call.</p>
 *
 * @author  isv
 * @version 1.0
 */
public class TestDataFactory {

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
    public static final String UNKNOWN_LOOKUP_VALUE = "cvwoub3vfubvoqwebv980234t23g20v8";

    /**
     *
     */
    public static final String NAMESPACE = "com.cronos.onlinereview.phases.failuretests";

    /**
     *
     */
    public static final String MANAGER_HELPER_NAMESPACE = "com.cronos.onlinereview.phases.ManagerHelper";

    /**
     *
     */
    public static final String OPERATOR = "FailureTest";

    /**
     *
     */
    public static final String UNKNOWN_NAMESPACE = "cwkfwifwbfqiwfiqwvuvqiebvqebi";

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getRegistrationPhaseType() {
        return new PhaseType(1, "Registration");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getSubmissionPhaseType() {
        return new PhaseType(2, "Submission");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getScreeningPhaseType() {
        return new PhaseType(3, "Screening");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getReviewPhaseType() {
        return new PhaseType(4, "Review");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getAppealsPhaseType() {
        return new PhaseType(5, "Appeals");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getAppealsResponsePhaseType() {
        return new PhaseType(6, "Appeals Response");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getAggregationPhaseType() {
        return new PhaseType(7, "Aggregation");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getAggregationReviewPhaseType() {
        return new PhaseType(8, "Aggregation Review");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getFinalFixPhaseType() {
        return new PhaseType(9, "Final Fix");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getFinalReviewPhaseType() {
        return new PhaseType(10, "Final Review");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getSpecificationSubmissionPhaseType() {
        return new PhaseType(11, "Specification Submission");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getSpecificationReviewPhaseType() {
        return new PhaseType(13, "Specification Review");
    }

    /**
     * <p>Generates a new instance of <code>PhaseType</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseType</code> instance.
     */
    public static PhaseType getApprovalPhaseType() {
        return new PhaseType(11, "Approval");
    }

    /**
     * <p>Generates a new instance of <code>PhaseStatus</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseStatus</code> instance.
     */
    public static PhaseStatus getScheduledPhaseStatus() {
        return new PhaseStatus(1, "Scheduled");
    }

    /**
     * <p>Generates a new instance of <code>PhaseStatus</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseStatus</code> instance.
     */
    public static PhaseStatus getOpenPhaseStatus() {
        return new PhaseStatus(2, "Open");
    }

    /**
     * <p>Generates a new instance of <code>PhaseStatus</code> type initialized with random data.</p>
     *
     * @return a new <code>PhaseStatus</code> instance.
     */
    public static PhaseStatus getClosedPhaseStatus() {
        return new PhaseStatus(3, "Closed");
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getRegistrationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getRegistrationPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenRegistrationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getRegistrationPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getScreeningPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getScreeningPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenScreeningPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getScreeningPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getAppealsPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getAppealsResponsePhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsResponsePhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getAggregationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getAggregationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getFinalFixPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalFixPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getFinalReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getApprovalPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getApprovalPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedRegistrationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getRegistrationPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedScreeningPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getScreeningPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getReviewPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedAppealsPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedAppealsResponsePhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsResponsePhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedAggregationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedAggregationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationReviewPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedFinalFixPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalFixPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedFinalReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalReviewPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedApprovalPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getApprovalPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getRegistrationPhaseWithNoRegistrationNumberAttribute() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getRegistrationPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getRegistrationPhaseWithNonIntegerRegistrationNumberAttribute() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getRegistrationPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setAttribute("Registration Number", "abc");
        result.setFixedStartDate(new Date(System.currentTimeMillis() - 2000));
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSubmissionPhaseWithNoSubmissionNumberAttribute() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSubmissionPhaseWithNonIntegerSubmissionNumberAttribute() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setAttribute("Submission Number", "abc");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSubmissionPhaseWithManualScreening() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSubmissionPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setAttribute("Manual Screening", "Yes");
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getScreeningPhaseWithPrimaryScreener() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getScreeningPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenAppealsResponsePhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAppealsResponsePhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenAggregationPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenAggregationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getAggregationReviewPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenFinalFixPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalFixPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenFinalReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getFinalReviewPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenApprovalPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getApprovalPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Project</code> type initialized with random data.</p>
     *
     * @return a new <code>Project</code> instance.
     */
    public static Project getProject() {
        Project result = new Project(new Date(), new DefaultWorkdays());
        result.setId(2);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Connection</code> type initialized with random data.</p>
     *
     * @return a new <code>Connection</code> instance.
     */
    @SuppressWarnings("deprecation")
    public static Connection getConnection() throws Exception {
        DBConnectionFactory factory = new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");
        return factory.createConnection();
    }

    /**
     * <p>Generates a new instance of <code>Connection</code> type initialized with random data.</p>
     *
     * @return a new <code>Connection</code> instance.
     */
    public static Connection getMockConnection() {
        return new MockConnection();
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSpecificationSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationSubmissionPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenSpecificationSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationSubmissionPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedSpecificationSubmissionPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationSubmissionPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSpecificationSubmissionPhaseWithManualScreening() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationSubmissionPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Manual Screening", "Yes");
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSpecificationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getOpenSpecificationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationReviewPhaseType());
        result.setPhaseStatus(getOpenPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getClosedSpecificationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationReviewPhaseType());
        result.setPhaseStatus(getClosedPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getScheduledSpecificationReviewPhase() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

    /**
     * <p>Generates a new instance of <code>Phase</code> type initialized with random data.</p>
     *
     * @return a new <code>Phase</code> instance.
     */
    public static Phase getSpecificationReviewPhaseWithManualScreening() {
        Phase result = new Phase(getProject(), 1000);
        result.setPhaseType(getSpecificationReviewPhaseType());
        result.setPhaseStatus(getScheduledPhaseStatus());
        result.setAttribute("Manual Screening", "Yes");
        result.setAttribute("Submission Number", "1");
        result.setFixedStartDate(new Date());
        result.setId(1);
        return result;
    }

}
