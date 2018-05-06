/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.accuracytests;

import java.util.List;
import java.util.Map;

import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;

/**
 * <p>
 * This class contains Accuracy tests for ProjectTermsOfUseDaoImpl.
 * </p>
 *
 * <p>
 * Change in 1.1:
 * <ul>
 * <li>Add test case to verify the supporting for Non-agreeable type.
 * <li>Update test cases to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
 * <li>Update test cases to support adding of TermsOfUse#agreeabilityType property.
 * </ul>
 * </p>
 *
 * @author sokol, fish_ship
 * @version 1.1
 */
public class ProjectTermsOfUseDaoImplAccuracyTest extends BaseAccuracyTest {

    /**
     * <p>
     * Represents constant when query parameter is not specified.
     * </p>
     */
    private static final int NOT_SPECIFIED = -1;

    /**
     * <p>
     * Represents query for retrieving project role terms of use.
     * </p>
     */
    private static final String SELECT_QUERY = "SELECT * FROM project_role_terms_of_use_xref";

    /**
     * <p>
     * Represents ProjectTermsOfUseDaoImpl for testing.
     * </p>
     */
    private ProjectTermsOfUseDaoImpl dao;

    /**
     * <p>
     * Represents sort order for testing.
     * </p>
     */
    private int sortOrder;

    /**
     * <p>
     * Represents project id for testing.
     * </p>
     */
    private int projectId;

    /**
     * <p>
     * Represents group index for testing.
     * </p>
     */
    private int groupIndex;

    /**
     * <p>
     * Represents terms of use id for testing.
     * </p>
     */
    private long termsOfUseId;

    /**
     * <p>
     * Represents resource role id for testing.
     * </p>
     */
    private int resourceRoleId;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void setUp() throws Exception {
        super.setUp();
        dao = new ProjectTermsOfUseDaoImpl(getConfiguration(PROJECT_TERMS_OF_USE_CONFIGURATION));
    }

    /**
     * <p>
     * Tears down the test environment.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void tearDown() throws Exception {
        super.tearDown();
        dao = null;
    }

    /**
     * <p>
     * Tests ProjectTermsOfUseDaoImpl constructor.
     * </p>
     * <p>
     * ProjectTermsOfUseDaoImpl instance should be created successfully. No exception is expected.
     * </p>
     */
    public void testConstructor() {
        assertNotNull("ProjectTermsOfUseDaoImpl instance should be created successfully", dao);
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#createProjectRoleTermsOfUse(int, int, long, int, int)} with valid arguments
     * passed.
     * </p>
     * <p>
     * Project role terms of use should be created successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testCreateProjectRoleTermsOfUse() throws Exception {
        // create new terms of use for project 3 and role 3 and terms 3
        sortOrder = 1;
        projectId = 3;
        groupIndex = 1;
        termsOfUseId = 4;
        resourceRoleId = 3;
        dao.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);
        assertEquals("Project role terms of use should be created successfully.", 1,
                getQueryRowCount(prepareQuery(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex)));
    }

    /**
     * <p>
     * Creates query for retrieving project role terms of use for given values.
     * </p>
     *
     * @param projectId
     *            the project id
     * @param resourceRoleId
     *            the resource role id
     * @param termsOfUseId
     *            the terms of use id
     * @param sortOrder
     *            the sort order
     * @param groupIndex
     *            the group index
     * @return SQL query for retrieving project role terms of use for given values
     */
    private static String prepareQuery(int projectId, int resourceRoleId, long termsOfUseId, int sortOrder,
            int groupIndex) {
        StringBuilder sb = new StringBuilder(SELECT_QUERY);
        StringBuilder clause = new StringBuilder();
        appendCondition(clause, "project_id", projectId);
        appendCondition(clause, "resource_role_id", resourceRoleId);
        appendCondition(clause, "terms_of_use_id", termsOfUseId);
        appendCondition(clause, "sort_order", sortOrder);
        appendCondition(clause, "group_ind", groupIndex);
        if (clause.length() > 0) {
            sb.append(" WHERE ").append(clause);
        }
        return sb.toString();
    }

    /**
     * <p>
     * Appends condition to given string builder.
     * </p>
     *
     * @param sb
     *            the string builder to append
     * @param propertyName
     *            the property name
     * @param propertyValue
     *            the property value
     */
    private static void appendCondition(StringBuilder sb, String propertyName, long propertyValue) {
        if (propertyValue != NOT_SPECIFIED) {
            if (sb.length() > 0) {
                sb.append(" AND ");
            }
            sb.append(propertyName).append("=").append(propertyValue);
        }
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#removeProjectRoleTermsOfUse(int, int, long, int)} with valid arguments
     * passed.
     * </p>
     * <p>
     * Project role terms of use should be removed successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testRemoveProjectRoleTermsOfUse() throws Exception {
        // remove term of use 1 for project 3 and role 2
        projectId = 1;
        groupIndex = 1;
        termsOfUseId = 1;
        resourceRoleId = 2;
        dao.removeProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, groupIndex);
        assertEquals("Project role terms of use should be deleted successfully.", 0,
                getQueryRowCount(prepareQuery(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex)));
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#removeAllProjectRoleTermsOfUse(int)} with valid arguments passed.
     * </p>
     * <p>
     * All project role terms of use should be removed successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testRemoveAllProjectRoleTermsOfUse() throws Exception {
        // remove all terms of use for project 1
        projectId = 1;
        dao.removeAllProjectRoleTermsOfUse(projectId);
        assertEquals("Project role terms of use should be deleted successfully.", 0,
                getQueryRowCount(prepareQuery(projectId, NOT_SPECIFIED, NOT_SPECIFIED, NOT_SPECIFIED, NOT_SPECIFIED)));
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with valid arguments passed with
     * including non-agreeable terms.
     * </p>
     * <p>
     * Mapping for each group to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse1() throws Exception {
        // retrieve agreeable and non-agreeable terms of use for each group of project 1 and resource roles 1,2
        projectId = 1;
        Map<Integer, List<TermsOfUse>> actual = dao.getTermsOfUse(projectId, 1, null);
        assertEquals("Role 1 should have 1 group of terms of use.", 1, actual.size());

        // check the group of terms for role 1
        List<TermsOfUse> terms = actual.get(0);
        assertEquals("Group 0 for Role 1 should have 3 terms of use.", 3, terms.size());

        // check that terms correctly sorted
        for (int i = 0; i < terms.size(); i++) {
            assertEquals("Correct terms of use should be placed on position " + i + " in resulted list.",
                    i + 1, terms.get(i).getTermsOfUseId());
        }
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with valid arguments passed with
     * including non-agreeable terms only for one resource role.
     * </p>
     * <p>
     * Mapping for group 2 to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse2() throws Exception {
        projectId = 1;
        Map<Integer, List<TermsOfUse>> actual = dao.getTermsOfUse(projectId, 2,
                new int[] { 2 });   // retrieve the electronically-agreeable terms
        assertEquals("Role 2 should have 1 group of terms of use.", 1, actual.size());

        // check the group of terms for role 2
        List<TermsOfUse> terms = actual.get(1);
        assertEquals("Group 1 for Role 2 should have 1 terms of use.", 1, terms.size());
        TermsOfUseAgreeabilityType agreeabilityType = createTermsOfUseAgreeabilityType(2, "Electronically-agreeable",
                "Electronically-agreeable");
        assertTrue("Group 1 should have 1 terms of use.",
                compareTermsOfUse(createTermsOfUse(1, 1, "t1", "url1", agreeabilityType), terms.get(0)));

    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with valid arguments passed without
     * including non-agreeable terms.
     * </p>
     * <p>
     * Empty mapping for group to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     *
     * @since 1.1
     */
    public void testGetTermsOfUse_NonAgreeable() throws Exception {
        projectId = 1;
        Map<Integer, List<TermsOfUse>> actual = dao.getTermsOfUse(projectId, 3,
                new int[] { 1 });   // retrieve the non-agreeable terms
        assertEquals("Role 3 should have 1 group of terms of use.", 1, actual.size());

        // check the group of terms for role 3
        List<TermsOfUse> terms = actual.get(2);
        assertEquals("Group 2 for Role 3 should have 1 terms of use.", 1, terms.size());
        TermsOfUseAgreeabilityType agreeabilityType = createTermsOfUseAgreeabilityType(1, "Non-agreeable",
                "Non-agreeable");
        assertTrue("Group 2 should have 1 terms of use.",
                compareTermsOfUse(createTermsOfUse(2, 1, "t2", "url2", agreeabilityType), terms.get(0)));
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with valid arguments passed without
     * including non-agreeable terms and only for resource role 1.
     * </p>
     * <p>
     * Empty mapping for group to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse_Agreeable_Empty() throws Exception {
        // retrieve only agreeable terms of use for each group of project 1 and resource roles 1
        projectId = 1;
        Map<Integer, List<TermsOfUse>> actual
            = dao.getTermsOfUse(projectId, 2, new int[] {1});
        assertEquals("No roles should present in mapping.", 0, actual.size());
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with id for non-existing project role
     * terms of use passed.
     * </p>
     * <p>
     * Empty mapping for group to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse_NoProject() throws Exception {
        // retrieve only agreeable terms of use for each group of project 3 and resource roles 1
        projectId = 3;
        Map<Integer, List<TermsOfUse>> actual = dao.getTermsOfUse(projectId, 1, new int[] {2 });
        assertEquals("No groups should present in mapping.", 0, actual.size());
    }

    /**
     * <p>
     * Tests {@link ProjectTermsOfUseDaoImpl#getTermsOfUse(int, int[], int[])} with resource id for non-existing project
     * role passed.
     * </p>
     * <p>
     * Empty mapping for group to project role terms of use should be retrieved successfully. No exception is expected.
     * </p>
     *
     * <p>
     * Change in 1.1:
     * <ul>
     * <li>Update to support removal of memberAgreeable and electronicallySignable properties of TermsOfUse.
     * <li>Update to support adding of TermsOfUse#agreeabilityType property.
     * </ul>
     * </p>
     *
     * @throws Exception
     *             if any error occurs
     */
    public void testGetTermsOfUse_NoResource() throws Exception {
        // retrieve only agreeable terms of use for each group of project 3 and resource roles 1,2
        projectId = 2;
        Map<Integer, List<TermsOfUse>> actual = dao.getTermsOfUse(projectId, 2, new int[] {2 });
        assertEquals("No groups should present in mapping.", 0, actual.size());
    }
}
