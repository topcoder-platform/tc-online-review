/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.accuracytests;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl;

/**
 * Accuracy tests for LateDeliverableProcessorImpl.
 * @author mumujava, victorsam, KLW, gjw99
 * @version 1.3
 */
public class LateDeliverableProcessorImplAccTests extends AccuracyHelper {
    /**
     * <p>
     * Represents one minute time in long.
     * </p>
     */
    public static final long MINUTE = 60000;
    /**
     * <p>
     * Represents constant value 60.
     * </p>
     */
    public static final int SIXTY = 60;
    
    /** Represents the LateDeliverableProcessorImpl instance to test. */
    private LateDeliverableProcessorImpl instance;

    /**
     * <p>Sets up the unit tests.</p>
     * @throws Exception to JUnit
     */
    public void setUp() throws Exception {
        super.setUp();
        instance = new LateDeliverableProcessorImpl();
    }

    /**
     * <p>Cleans up the unit tests.</p>
     * @throws Exception to JUnit
     */
    public void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    /**
     * Accuracy test for method LateDeliverableProcessorImpl.
     */
    public void test_LateDeliverableProcessorImpl() {
        assertNotNull(instance);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable() throws Exception {
        //addTrackingRecord = true, canSendNotification = true and needToNotify = true
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        LateDeliverable result = retriever.retrieve(getTypes()).get(0);
        // test the new functionality
        Date compensatedDeadline = new Date(new Date().getTime()-MINUTE);
       result.setCompensatedDeadline(compensatedDeadline);

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(result);
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);
        
        int delay = Integer.valueOf(lateDeliverable[3].toString());
        System.out.println(delay);
        //update in version 1.2
        //Delay is computed based on the compensated deadline if exists (previously was always computed based on the real deadline). 
        //check the dalay  
         assertTrue("The delay is inocrrect.",delay<SIXTY+1);
    }

	/**
     * Accuracy test for method processLateDeliverable.
     *
     * "Screening Scorecard" deliverable is late. (No need to notify)
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable2() throws Exception {
        //addTrackingRecord = true, canSendNotification = true and needToNotify = false
        AccuracyHelper.executeSqlFile("test_files/accuracy/test_Screening_late_noneed_notify.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNull(lateDeliverable[1]);
        //should not be notified
        assertEquals(lateDeliverable[2], 3);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     * The deadline is extended, however it's late again.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable3() throws Exception {

        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable[0], false);
        assertEquals(lateDeliverable.length, 4);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);

        //extend the deadline
        AccuracyHelper.executeSqlFile("test_files/accuracy/extendDeadline1.sql");

        res = retriever.retrieve(getTypes());
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        //double records is obtained.
        lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 8);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);
        assertEquals(lateDeliverable[4], false);
        assertNotNull(lateDeliverable[5]);
        //should be notified
        assertEquals(lateDeliverable[6], 4);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     * The deadline is extended and it's no longer late.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable4() throws Exception {

        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable[0], false);
        assertEquals(lateDeliverable.length, 4);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);

        //extend the deadline
        AccuracyHelper.executeSqlFile("test_files/accuracy/extendDeadline2.sql");

        instance.processLateDeliverable(res.get(0));
        //db is unchanged.
        Object[] lateDeliverable2 = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable2.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     * 10 seconds later, need to notify again.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to junit
     */
    public void test_processLateDeliverable5() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);

        Thread.sleep(10 * 1000);

        instance.processLateDeliverable(res.get(0));
        //db is unchanged.
        Object[] lateDeliverable2 = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable2.length, 4);
        assertEquals(lateDeliverable2[0], false);
        assertNotNull(lateDeliverable2[1]);
        //should be notified
        assertEquals(lateDeliverable2[2], 4);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     * 1 seconds later, do not need to notify again.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable6() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);

        Thread.sleep(1 * 1000);

        instance.processLateDeliverable(res.get(0));
        //db is unchanged.
        Object[] lateDeliverable2 = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable2.length, 4);
        assertEquals(lateDeliverable2[0], false);
        assertNotNull(lateDeliverable2[1]);
        //should be notified
        assertEquals(lateDeliverable2[2], 4);

        //should update the notification datetime
        assertTrue(((Comparable) lateDeliverable2[1]).compareTo(lateDeliverable[1]) == 0);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     * 11 seconds later, the late is forgiven, do not need to notify again.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable7() throws Exception {
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        List<LateDeliverable> res = retriever.retrieve(getTypes());

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(res.get(0));
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);

        Thread.sleep(11 * 1000);
        //forgive it
        AccuracyHelper.executeSqlFile("test_files/accuracy/forgiveAll.sql");

        instance.processLateDeliverable(res.get(0));
        //db is unchanged.
        Object[] lateDeliverable2 = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable2.length, 4);
        assertNotNull(lateDeliverable2[1]);
        //should be notified
        assertEquals(lateDeliverable2[2], 4);

        //should update the notification datetime
        assertTrue(((Comparable) lateDeliverable2[1]).compareTo(lateDeliverable[1]) == 0);
    }
    /**
     * Accuracy test for method processLateDeliverable.
     *
     * "Review Scorecard" deliverable is late.
     *
     * Manually check the mails.
     *
     *
     * @throws Exception to JUnit
     */
    public void test_processLateDeliverable8() throws Exception {
        //addTrackingRecord = true, canSendNotification = true and needToNotify = true
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        LateDeliverable result = retriever.retrieve(getTypes()).get(0);
       //not set the compensatedDeadline
       //result.setCompensatedDeadline(compensatedDeadline);

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(result);
        //an email is expected to sent to wuyb@topcoder.com, manually check the mails please

        Object[] lateDeliverable = getLateDeliverable(112, 1);
        assertEquals(lateDeliverable.length, 4);
        assertEquals(lateDeliverable[0], false);
        assertNotNull(lateDeliverable[1]);
        //should be notified
        assertEquals(lateDeliverable[2], 4);
        
        int delay = Integer.valueOf(lateDeliverable[3].toString());
        //update in version 1.2
        //Delay is computed based on the compensated deadline if exists (previously was always computed based on the real deadline). 
        //check the dalay  
         assertTrue("The delay is inocrrect.",delay>SIXTY);
    }

    /**
     * Accuracy test for method processLateDeliverable.
     *
     * Of type REJECTED_FINAL_FIX.
     * It will check 2 conditions:
     * 1. new late deliverable is created.
     * 2. the late deliverable is already tracked.
     *
     *
     * @throws Exception to JUnit
     * @since 1.3
     */
    public void test_processLateDeliverable9() throws Exception {
        //addTrackingRecord = true, canSendNotification = true and needToNotify = true
        AccuracyHelper.executeSqlFile("test_files/accuracy/test.sql");

        LateDeliverablesRetrieverImpl retriever = new LateDeliverablesRetrieverImpl();
        ConfigurationObject config = getConfigurationObject("accuracy/config/LateDeliverablesRetrieverImpl.xml",
            "com.topcoder.management.deliverable.latetracker.retrievers.LateDeliverablesRetrieverImpl");
        retriever.configure(config);
        LateDeliverable result = retriever.retrieve(getTypes()).get(1);

        config = getConfigurationObject("accuracy/config/LateDeliverableProcessorImpl.xml",
            "com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl");
        instance.configure(config);
        instance.processLateDeliverable(result);

        Object[] lateDeliverable = getLateDeliverable(113, 2);
        // check the late deliverables found, should be only one
        assertEquals("one late deliverable should exist", 1, lateDeliverable.length/4);
        assertTrue("the deliverable id must be positive", (Integer)lateDeliverable[2] > 0);
        Timestamp timstamp = (Timestamp) lateDeliverable[1];
        // alreadyTracked = true
        instance.processLateDeliverable(result);
        Timestamp afterProcessed = (Timestamp) getLateDeliverable(113, 2)[1];
        assertEquals("the already tracked rejected final fix should not be processed", timstamp, afterProcessed);
    }

    /**
     * <p>
     * Gets the late Deliverable.
     * </p>
     *
     * @param projectPhaseId the project phase id
     * @param resourceId the resource id
     * @return the late Deliverable
     * @throws SQLException to JUnit
     */
    private Object[] getLateDeliverable(int projectPhaseId, int resourceId) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = connection.prepareStatement("select forgive_ind, last_notified, deliverable_id, delay from late_deliverable where "
                + "project_phase_id = ? and resource_id = ?");
            ps.setInt(1, projectPhaseId);
            ps.setInt(2, resourceId);

            rs = ps.executeQuery();
            List<Object> res = new ArrayList<Object>();
            while (rs.next()) {
                res.add(rs.getBoolean(1));
                res.add(rs.getTimestamp(2));
                res.add(rs.getInt(3));
                res.add(rs.getInt(4));
            }
            return res.toArray();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
        }
    }
}
