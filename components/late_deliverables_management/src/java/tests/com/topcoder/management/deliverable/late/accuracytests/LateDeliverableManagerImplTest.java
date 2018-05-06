/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.management.deliverable.late.accuracytests;

import static com.topcoder.management.deliverable.late.accuracytests.TestHelper.getDate;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.OrFilter;

/**
 * <p> Accuracy test for <code>LateDeliverableManagerImpl</code>. </p>
 *
 * @author yuanyeyuanye, TCSDEVELOPER
 * @version 1.0
 */
public class LateDeliverableManagerImplTest extends PersistenceTestBase {

    /**
     * <p> Accuracy test for the constructor <code>LateDeliverableManagerImpl(Configuration)</code>. </p>
     *
     * @throws Exception to Junit.
     */
    @Test
    public void testCtorConfiguration_Accuracy() throws Exception {
        assertNotNull(lateDeliverableManager);
    }

    /**
     * <p> Accuracy test for <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testLateDeliverableManagerImpl_accuracy() throws Exception {
        assertNotNull("test_files/accuracy/LateDeliverableManagerImpl.xml",
                      "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl");
    }

    /**
     * <p> Accuracy test for the method <code>retrieve(long lateDeliverableId)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testRetrieve_Accuracy() throws Exception {
        LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(1);
        final int id = 1;
        final int deliverableId = 4;
        final int resourceId = 1001;
        final int projectPhaseId = 101;
        final boolean forgiven = false;
        final String explaination = "explaination";
        final String response = "response";
        final Date creatDate = getDate(2010, 10, 20, 9, 5, 0);
        final Date deadLine = getDate(2010, 10, 22, 9, 5, 0);
        final Date lastNotified = getDate(2010, 10, 20, 9, 5, 0);
        final long delay = 10000L;
        LateDeliverableType type = new LateDeliverableType();
        type.setId(2);
        type.setName("Late Submission Phase");
        type.setDescription("des2");

        verifyData(lateDeliverable,
                   id,
                   deliverableId,
                   resourceId,
                   projectPhaseId,
                   forgiven,
                   explaination,
                   response,
                   creatDate,
                   deadLine,
                   lastNotified,
                   delay,
                   type);
    }

    /**
     * <p> Accuracy test for the method <code>retrieve(long lateDeliverableId)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testRetrieve_if_id_do_not_exists() throws Exception {
        final long notExistedId = Long.MAX_VALUE;
        LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(notExistedId);
        Assert.assertNull(lateDeliverable);
    }

    /**
     * <p> Accuracy test for the method <code>retrieve(long lateDeliverableId)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testRetrieve_Accuracy2() throws Exception {
        // Gets the record with id = 2.
        // The data is
        // INSERT INTO tcs_catalog:'informix'.late_deliverable(late_deliverable_id, project_phase_id, resource_id,
        // deliverable_id, deadline, create_date, forgive_ind, last_notified) VALUES(2, 102, 1002, 3,
        // TO_DATE('2010-11-25 12:00:00', '%Y-%m-%d %H:%M:%S'), TO_DATE('2010-11-23 12:00:00', '%Y-%m-%d %H:%M:%S'),
        // 0, TO_DATE('2010-11-23 12:00:00', '%Y-%m-%d %H:%M:%S'));
        LateDeliverable lateDeliverable = lateDeliverableManager.retrieve(2);

        final int id = 2;
        final int projectPhaseId = 102;
        final int resourceId = 1002;
        final int deliverableId = 3;
        final boolean forgiven = false;
        final String explaination = null;
        final String response = null;
        final Date deadLine = getDate(2010, 10, 25, 12, 0, 0);
        final Date creatDate = getDate(2010, 10, 23, 12, 0, 0);
        final Date lastNotified = getDate(2010, 10, 23, 12, 0, 0);
        final Long delay = null;
        LateDeliverableType type = new LateDeliverableType();
        type.setId(2);
        type.setName("Late Submission Phase");
        type.setDescription("des2");

        verifyData(lateDeliverable,
                   id,
                   deliverableId,
                   resourceId,
                   projectPhaseId,
                   forgiven,
                   explaination,
                   response,
                   creatDate,
                   deadLine,
                   lastNotified,
                   delay,
                   type);
    }

    /**
     * Verify the data is the same with data in "insert.sql".
     *
     * @param lateDeliverable data to be verified.
     * @param id              id of the record.
     * @param deliverableId   deliverable Id.
     * @param resourceId      resource id.
     * @param projectPhaseId  projectPhase id.
     * @param forgiven        value for forgiven.
     * @param explaination    value of explaination.
     * @param response        value of response.
     * @param creatDate       value of creation date.
     * @param deadLine        value of deadline.
     * @param lastNotified    value of last notified.
     * @param delay           value for delay.
     * @param type           value for LateDeliverableType.
     */
    private void verifyData(LateDeliverable lateDeliverable, int id, int deliverableId, int resourceId,
                            int projectPhaseId, boolean forgiven, String explaination, String response,
                            Date creatDate, Date deadLine, Date lastNotified, Long delay, LateDeliverableType type) {
        assertEquals(id, lateDeliverable.getId());
        assertEquals(projectPhaseId, lateDeliverable.getProjectPhaseId());
        assertEquals(resourceId, lateDeliverable.getResourceId());
        assertEquals(deliverableId, lateDeliverable.getDeliverableId());
        assertEquals(forgiven, lateDeliverable.isForgiven());
        assertEquals(explaination, lateDeliverable.getExplanation());
        assertEquals(response, lateDeliverable.getResponse());
        assertEquals(creatDate, lateDeliverable.getCreateDate());
        assertEquals(deadLine, lateDeliverable.getDeadline());
        assertEquals(lastNotified, lateDeliverable.getLastNotified());
        assertEquals(delay, lateDeliverable.getDelay());
        assertEquals(type.getId(), lateDeliverable.getType().getId());
        assertEquals(type.getDescription(), lateDeliverable.getType().getDescription());
        assertEquals(type.getName(), lateDeliverable.getType().getName());
    }

    /**
     * <p> Accuracy test for <code>update(LateDeliverable lateDeliverable)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testUpdate_accuracy() throws Exception {
        LateDeliverable entity = lateDeliverableManager.retrieve(1);

        final Long delay = null;
        entity.setDelay(delay);

        final Date createDate = getDate(2010, 11, 1, 12, 0, 0);
        entity.setCreateDate(createDate);

        final Date deadline = getDate(2010, 11, 3, 12, 0, 0);
        entity.setDeadline(deadline);

        final Date lastNotified = getDate(2010, 11, 1, 12, 0, 0);
        entity.setLastNotified(lastNotified);        
        
        final int resourceId = 1002;
        entity.setResourceId(resourceId);
        
        final int projectPhaseId = 102;
        entity.setProjectPhaseId(projectPhaseId);


        final boolean forgiven = true;
        entity.setForgiven(forgiven);

        final String response = "new_response";
        entity.setResponse(response);
        LateDeliverableType type = new LateDeliverableType();
        type.setId(3);
        entity.setType(type);
        lateDeliverableManager.update(entity);

        ResultSet rs = null;
        try {
            rs = connection.prepareStatement("select * from late_deliverable where late_deliverable_id = 1")
                .executeQuery();

            // Should have next record.
            Assert.assertTrue(rs.next());

            Assert.assertEquals(rs.getInt("forgive_ind"), 1);
            Assert.assertEquals(rs.getBigDecimal("delay"), null);
            Assert.assertEquals(rs.getLong("resource_id"), resourceId);
            Assert.assertEquals(rs.getLong("project_phase_id"), projectPhaseId);
            Assert.assertEquals(rs.getString("response"), response);
            
            Assert.assertEquals(rs.getTimestamp("deadline").getTime(), deadline.getTime());
            Assert.assertEquals(rs.getTimestamp("create_date").getTime(), createDate.getTime());
            Assert.assertEquals(rs.getTimestamp("last_notified").getTime(), lastNotified.getTime());
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
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_return_empty_result_accuracy() throws Exception {
        // Search for not existed project id.
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(
            LateDeliverableFilterBuilder.createProjectIdFilter(Long.MAX_VALUE));

        Assert.assertEquals(ret.size(), 0);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_with_null_filter() throws Exception {
        // Search for not existed project id.
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(null);

        // all the records should be retrieved.
        Assert.assertEquals(ret.size(), 10);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L,7L,8L,9L,10L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_forgiven_and_deliverable_id() throws Exception {

        // Search for forgiven and deliverable id.
        final Filter deliverableIdFilter = LateDeliverableFilterBuilder.createDeliverableIdFilter(3);
        final Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(false);
        final AndFilter deliverableAndForgivenFilter = new AndFilter(deliverableIdFilter, forgivenFilter);

        final List<LateDeliverable> ret =
            lateDeliverableManager.searchAllLateDeliverables(deliverableAndForgivenFilter);

        // There should be 4 records with id = 2,3,5,6 meet the condition forgiven = false and deliverableId = 3.
        Assert.assertEquals(ret.size(), 4);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(2L, 3L, 5L, 6L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_project_id() throws Exception {
        Filter projectIdFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100000);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(projectIdFilter);

        // project phase for project {id=100000} is project_phase{id=101}
        // So there are 2 records with id = 1, 3
        Assert.assertEquals(ret.size(), 2);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(1L, 3L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_project_status_filter() throws Exception {
        Filter projectStatusIdFilter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(1);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(projectStatusIdFilter);

        // project id = 100001 has project status "1"
        // project_phase id = 102 is for this project

        // There should be 2 records with id = 2,4 has project_phase{id:102}
        Assert.assertEquals(ret.size(), 2);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(2L, 4L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_category_filter() throws Exception {
        Filter projectCategoryIdFilter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(projectCategoryIdFilter);

        // project id = 100000,100001 has category id "1"
        // project_phase id = 101, 102 is for this project

        // There should be 2 records with id = 1,2,3,4 has project_phase{id:102, 101}
        Assert.assertEquals(ret.size(), 4);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(1L, 2L, 3L, 4L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_project_status_or_forgiven_filter() throws Exception {
        Filter projectStatusIdFilter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(1);
        Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(true);

        final OrFilter statusOrForgivenFilter = new OrFilter(forgivenFilter, projectStatusIdFilter);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(statusOrForgivenFilter);

        // project id = 100001 has project status "1"
        // project_phase id = 102 is for this project
        // There should be 2 records with id = 2,4 has project_phase{id:102} and 1 records with id = 7,8 with
        // forgiven = true.
        Assert.assertEquals(ret.size(), 6);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(2L, 4L, 7L, 8L, 9L, 10L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_search_for_deliverable_id_filter() throws Exception {
        Filter deliverableIdFilter = LateDeliverableFilterBuilder.createDeliverableIdFilter(4);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(deliverableIdFilter);

        // There should be 2 records with id = 1,4 has deliverable id 4
        Assert.assertEquals(ret.size(), 2);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(1L, 4L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchAllLateDeliverables(Filter filter)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchAllLateDeliverables_createLateDeliverableTypeIdFilter() throws Exception {
        Filter typeFilter = LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(3);
        final List<LateDeliverable> ret = lateDeliverableManager.searchAllLateDeliverables(typeFilter);

        // There should be 1 records with id = 5L,6L has type 3
        Assert.assertEquals(ret.size(), 2);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(5L,6L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_unforgiven() throws Exception {
        Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(false);
        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(forgivenFilter, 10);

        // There should be 1 records for user id 10 and forgiven = false.
        // resource 1004 is accessible by user id =10.
        // The result should be the entity with resource id = 1004 and forgiven = false.
        Assert.assertEquals(ret.size(), 1);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(6L));

        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_by_project_id() throws Exception {

        // Test for lateUser
        Filter projectIdFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100004);
        final int lateUserId = 10;
        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(projectIdFilter, lateUserId);

        Assert.assertEquals(ret.size(), 3);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(6L,7L,8L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_by_project_id_and_forgiven() throws Exception {
        Filter projectFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100000);
        Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(true);

        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(new AndFilter(projectFilter, forgivenFilter), 3);

        // no entity should be retrieved.
        Assert.assertEquals(ret.size(), 0);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_for_manager_user_id() throws Exception {

        // User with id 11 is manager user for project 100004.
        Filter projectFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100004);
        final int lateUserId = 11;

        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(projectFilter,
                                                                    lateUserId);

        // entity with id  = 8 can be retrieved.
        Assert.assertEquals(ret.size(), 3);

        // Late user should be able to access the resources because the resource is assigend to him and he also has manager role.
        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(6L,7L,8L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_with_no_manager_access() throws Exception {
        final int notAccessableProjectId = 100000;
        Filter projectFilter = LateDeliverableFilterBuilder.createProjectIdFilter(notAccessableProjectId);

        final int lateUserId = 11;
        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(projectFilter, lateUserId);

        // user with id 11 do not have privilege to access all the records with filter projectId = 100000.
        Assert.assertEquals(ret.size(), 0);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_own_late_deliverables() throws Exception {

        //Test that user has access to late deliverables assigned to him even when he does not have any kind of
        // the manager access. This means that the late deliverable needs to be assigned to a resource A,
        // and the resource_info table should map resource A to his user ID. Also,
        // the resource A should not have a manager role (i.e. with resource_role_id in 13,14,15).
        // Also, the user should not have any cockpit project access. For the latter one just make
        // sure the user ID is not in the user_permission_grant table.
        final int userId = 12;
        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(null, userId);

        Assert.assertEquals(ret.size(), 1);

        // Late user should be able to access the resources.
        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(9L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_with_manager_role_access() throws Exception {

        // Test that user has access to late deliverables which are not assigned to him but which belong
        // to the project in which the user has a manager role. This means that the late deliverable need
        // s to be assigned to some other resource B which is mapped to some other user ID. The user ID
        // passed to the search method should have a manager resource (with role 13,14 or 15) in the same
        // project that the resource B belongs to. Again, make sure that the user has no cockpit access.


        // User with id 13 has the manager role.
        final int lateUserId = 13;
        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(null, lateUserId);

        Assert.assertEquals(ret.size(), 2);

        // Late user should be able to access the resources.
        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(9L,10L));
        verifyIds(ret, ids);
    }

    /**
     * <p> Accuracy test for <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. </p>
     *
     * <p> Test that when the input is valid, the functionality of this method should be performed correctly.</p>
     *
     * @throws Exception to JUnit.
     */
    @Test
    public void testSearchRestrictedLateDeliverables_with_cockpit_project_access() throws Exception {
        // User with user_id = 3 is tc direct user for project 100001, project(id=100001) only has userid=3 as tc direct
        // user and userid=2 as late user.
        Filter projectFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100001);
        final int tcDirectUser = 3;

        final List<LateDeliverable> ret =
            lateDeliverableManager.searchRestrictedLateDeliverables(projectFilter, tcDirectUser);

        // entity with id  = 2,4 can be retrieved.
        Assert.assertEquals(ret.size(), 2);

        final HashSet<Long> ids = new HashSet<Long>(Arrays.asList(2L, 4L));
        verifyIds(ret, ids);
    }

    /**
     * Verify the records has been selected successfully.
     *
     * @param lateDeliverables Collection of LateDeliverable.
     * @param ids              Set of id.
     * @throws Throwable 
     */
    private static void verifyIds(List<LateDeliverable> lateDeliverables, HashSet<Long> ids) {
        for (LateDeliverable lateDeliverable : lateDeliverables) {
            Assert.assertTrue(String.format("id : %s should be in id set", lateDeliverable.getId()),
                              ids.contains(lateDeliverable.getId()));
        }
    }

    /**
     * <p> Accuracy test for the method <code>getLateDeliverableTypes(LateDeliverable)</code>. </p>
     *
     * @throws Exception to JUnit
     */
    @Test
    public void testgetLateDeliverableTypes_Accuracy() throws Exception {
        List<LateDeliverableType> list = lateDeliverableManager.getLateDeliverableTypes();
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
