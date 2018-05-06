/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.impl.persistence.DatabaseLateDeliverablePersistence;
import org.junit.Assert;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.Date;
import java.util.List;

import static com.topcoder.management.deliverable.late.accuracytests.TestHelper.getDate;
import static com.topcoder.management.deliverable.late.accuracytests.TestHelper.getPrivateField;
import static org.junit.Assert.assertNotNull;

/**
 * <p> Accuracy test for <code>DatabaseLateDeliverablePersistence</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class DatabaseLateDeliverablePersistenceTest extends PersistenceTestBase {

    /**
     * <p> Accuracy test for the constructor. </p>
     *
     * <p> Test the constructor can construct the instance correctly.</p>
     *
     * @throws Exception to jUnit.
     */
    @Test
    public void testCtor() throws Exception {
        assertNotNull(lateDeliverablePersistence);
    }

    /**
     * <p> Accuracy test for the method <code>configure(ConfigurationObject)</code>. </p>
     */
    @Test
    public void testConfigure_Accuracy() {
        lateDeliverablePersistence.configure(persistenceConfig);

        Assert.assertNotNull(getPrivateField(lateDeliverablePersistence, "log"));
        Assert.assertNotNull(getPrivateField(lateDeliverablePersistence, "dbConnectionFactory"));
        Assert.assertEquals(getPrivateField(lateDeliverablePersistence, "connectionName"), "myConnection");
    }

    /**
     * <p> Accuracy test for the method <code>update(LateDeliverable)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testUpdate_Accuracy() throws Exception {

        lateDeliverablePersistence = new DatabaseLateDeliverablePersistence();
        lateDeliverablePersistence.configure(persistenceConfig);

        final Date deadline = getDate(2010, 10, 1, 10, 1, 1);
        final Date createDate = getDate(2010, 9, 25, 10, 1, 1);
        final Date lastNotify = getDate(2010, 10, 2, 10, 1, 1);

        final long delay = 10000000L;
        final String explanation = "explaination";
        final String response = "response";

        final long projectPhaseId = 101L;
        final long resourceId = 1003L;
        final long deliverableId = 4L;
        LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(2);
        LateDeliverable entity =
            getUpdatedEntity(lateDeliverable, deadline, createDate, delay, lastNotify, true, explanation, response,
                             projectPhaseId, resourceId, deliverableId);
        entity.getType().setId(3);
        lateDeliverablePersistence.update(entity);

        ResultSet rs = null;
        try {
            rs = connection.prepareStatement("select * from late_deliverable where late_deliverable_id = 2")
                .executeQuery();

            // Should have next record.
            Assert.assertTrue(rs.next());

            Assert.assertEquals(rs.getInt("forgive_ind"), 1);
            Assert.assertEquals(rs.getBigDecimal("delay").longValue(), delay);
            Assert.assertEquals(rs.getLong("project_phase_id"), projectPhaseId);
            Assert.assertEquals(rs.getLong("resource_id"), resourceId);
            Assert.assertEquals(rs.getLong("deliverable_id"), deliverableId);
            Assert.assertEquals(rs.getString("explanation"), explanation);
            Assert.assertEquals(rs.getString("response"), response);

            Assert.assertEquals(rs.getTimestamp("deadline").getTime(), deadline.getTime());
            Assert.assertEquals(rs.getTimestamp("create_date").getTime(), createDate.getTime());
            Assert.assertEquals(rs.getTimestamp("last_notified").getTime(), lastNotify.getTime());
            Assert.assertEquals(rs.getInt("late_deliverable_type_id"), 3);

            // Should not have second record.
            Assert.assertFalse(rs.next());
        } finally {
            if (rs != null) {
                rs.close();
            }
        }
    }

    /**
     * Update the given entity with given values.
     *
     * @param lateDeliverable entity to update.
     * @param deadLine        dead line.
     * @param createDate      create date.
     * @param delay           delay.
     * @param lastNotify      last notify date.
     * @param forgiven        forgiven value.
     * @param explanation     explaination.
     * @param response        response.
     *
     * @param projectPhaseId project phase id.
     * @param resourceId  Resource id.
     * @param deliverableId deliverable Id.
     * @return updated entity for test.
     */
    private static LateDeliverable getUpdatedEntity(LateDeliverable lateDeliverable, Date deadLine, Date createDate,
                                                    long delay, Date lastNotify, boolean forgiven, String explanation,
                                                    String response, long projectPhaseId, long resourceId,
                                                    long deliverableId) {
        lateDeliverable.setForgiven(forgiven);
        lateDeliverable.setCreateDate(createDate);
        lateDeliverable.setDeadline(deadLine);
        lateDeliverable.setDelay(delay);
        lateDeliverable.setLastNotified(lastNotify);
        lateDeliverable.setExplanation(explanation);
        lateDeliverable.setResponse(response);
        lateDeliverable.setProjectPhaseId(projectPhaseId);
        lateDeliverable.setResourceId(resourceId);
        lateDeliverable.setDeliverableId(deliverableId);

        return lateDeliverable;
    }


    /**
     * <p> Accuracy test for the method <code>getLateDeliverableTypes(LateDeliverable)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testgetLateDeliverableTypes_Accuracy() throws Exception {

        lateDeliverablePersistence = new DatabaseLateDeliverablePersistence();
        lateDeliverablePersistence.configure(persistenceConfig);

        List<LateDeliverableType> list = lateDeliverablePersistence.getLateDeliverableTypes();
        Assert.assertEquals("The result is incorrect", list.size(), 3);
        Assert.assertEquals("The result is incorrect", list.get(0).getId(), 1);
        Assert.assertEquals("The result is incorrect", list.get(0).getDescription(), "des1");
        Assert.assertEquals("The result is incorrect", list.get(0).getName(), "Late Review Phase");
        Assert.assertEquals("The result is incorrect", list.get(1).getId(), 2);
        Assert.assertEquals("The result is incorrect", list.get(1).getDescription(), "des2");
        Assert.assertEquals("The result is incorrect", list.get(1).getName(), "Late Submission Phase");
        Assert.assertEquals("The result is incorrect", list.get(2).getId(), 3);
        Assert.assertEquals("The result is incorrect", list.get(2).getDescription(), "des3");
        Assert.assertEquals("The result is incorrect", list.get(2).getName(), "Late Final fix Phase");
    }
}
