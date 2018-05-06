/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;
import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Unit tests for {@link ProjectTermsOfUseDaoImpl} class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Updated/added test cases for getTermsOfUse() method to support filtering of terms of use groups by custom
 * agreeability types instead of member agreeable flag.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class ProjectTermsOfUseDaoImplUnitTests extends BaseUnitTests {
    /**
     * <p>
     * Represents the <code>ProjectTermsOfUseDaoImpl</code> instance used in tests.
     * </p>
     */
    private ProjectTermsOfUseDaoImpl instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject configurationObject;

    /**
     * <p>
     * Represents the project id used in tests.
     * </p>
     */
    private int projectId = 2;

    /**
     * <p>
     * Represents the resource role id used in tests.
     * </p>
     */
    private int resourceRoleId = 2;

    /**
     * <p>
     * Represents the terms of use id used in tests.
     * </p>
     */
    private long termsOfUseId = 3;

    /**
     * <p>
     * Represents the sort order used in tests.
     * </p>
     */
    private int sortOrder = 2;

    /**
     * <p>
     * Represents the group index used in tests.
     * </p>
     */
    private int groupIndex = 0;

    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(ProjectTermsOfUseDaoImplUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();

        connection = getConnection();

        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_PROJECT_TERMS);

        instance = new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject
     * configurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new ProjectTermsOfUseDaoImpl(configurationObject);

        assertNotNull("'dbConnectionFactory' should be correct.", instance.getDBConnectionFactory());
        assertNotNull("'log' should be correct.", instance.getLog());
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with configurationObject is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_configurationObjectNull() {
        configurationObject = null;

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with 'dbConnectionFactoryConfig' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigMissing() throws Exception {
        configurationObject.removeChild("dbConnectionFactoryConfig");

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid1() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig").removeChild(
            "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid2() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig").getChild(
            "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl").getChild("connections").setPropertyValue(
            "default", "not_exist");

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with 'loggerName' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameMissing() throws Exception {
        configurationObject.removeProperty("loggerName");

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>ProjectTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with 'loggerName' is not a String.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameNotString() throws Exception {
        configurationObject.setPropertyValue("loggerName", 1);

        new ProjectTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createProjectRoleTermsOfUse(int projectId, int resourceRoleId,
     * long termsOfUseId, int sortOrder, int groupIndex)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createProjectRoleTermsOfUse() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        assertTrue("'createProjectRoleTermsOfUse' should be correct.",
            hasProjectRoleTermsOfUse(connection, projectId, resourceRoleId, termsOfUseId, groupIndex));
    }

    /**
     * <p>
     * Failure test for the method <code>createProjectRoleTermsOfUse(int projectId, int resourceRoleId,
     * long termsOfUseId, int sortOrder, int groupIndex)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_createProjectRoleTermsOfUse_PersistenceError() throws Exception {
        instance = new ProjectTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);
    }

    /**
     * <p>
     * Accuracy test for the method <code>removeProjectRoleTermsOfUse(int projectId, int resourceRoleId,
     * long termsOfUseId, int groupIndex)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_removeProjectRoleTermsOfUse() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        instance.removeProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, groupIndex);

        assertFalse("'removeProjectRoleTermsOfUse' should be correct.",
            hasProjectRoleTermsOfUse(connection, projectId, resourceRoleId, termsOfUseId, groupIndex));
    }

    /**
     * <p>
     * Failure test for the method <code>removeProjectRoleTermsOfUse(int projectId, int resourceRoleId,
     * long termsOfUseId, int groupIndex)</code> with the entity was not found in the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_removeProjectRoleTermsOfUse_EntityNotFoundError() throws Exception {
        instance.removeProjectRoleTermsOfUse(Integer.MAX_VALUE, resourceRoleId, termsOfUseId, groupIndex);
    }

    /**
     * <p>
     * Failure test for the method <code>removeProjectRoleTermsOfUse(int projectId, int resourceRoleId,
     * long termsOfUseId, int groupIndex)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_removeProjectRoleTermsOfUse_PersistenceError() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        instance = new ProjectTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.removeProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, groupIndex);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUse(int projectId, int[] resourceRoleIds,
     * int[] agreeabilityTypeIds)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support replaced includeNonMemberAgreeable:boolean parameter with
     * agreeabilityTypeIds:int[].</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUse_1() throws Exception {
        Map<Integer, List<TermsOfUse>> res = instance.getTermsOfUse(1, 1, null);

        assertEquals("'getTermsOfUse' should be correct.", 3, res.get(0).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUse(int projectId, int[] resourceRoleIds,
     * int[] agreeabilityTypeIds)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support replaced includeNonMemberAgreeable:boolean parameter with
     * agreeabilityTypeIds:int[].</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUse_2() throws Exception {
        Map<Integer, List<TermsOfUse>> res = instance.getTermsOfUse(1, 2, new int[] {2, 3});

        assertEquals("'getTermsOfUse' should be correct.", 1, res.size());
        assertEquals("'getTermsOfUse' should be correct.", 1, res.get(1).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUse(int projectId, int[] resourceRoleIds,
     * int[] agreeabilityTypeIds)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support replaced includeNonMemberAgreeable:boolean parameter with
     * agreeabilityTypeIds:int[].</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUse_3() throws Exception {
        Map<Integer, List<TermsOfUse>> res= instance.getTermsOfUse(Integer.MAX_VALUE, 1, null);

        assertEquals("'getTermsOfUse' should be correct.", 0, res.size());

    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUse(int projectId, int[] resourceRoleIds,
     * int[] agreeabilityTypeIds)</code>
     * with agreeabilityTypeIds is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_getTermsOfUse_agreeabilityTypeIdsEmpty() throws Exception {
        instance.getTermsOfUse(projectId, 1, new int[0]);
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUse(int projectId, int[] resourceRoleIds,
     * int[] agreeabilityTypeIds)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support replaced includeNonMemberAgreeable:boolean parameter with
     * agreeabilityTypeIds:int[].</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUse_PersistenceError() throws Exception {
        instance = new ProjectTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUse(1, 1, null);
    }

    /**
     * <p>
     * Accuracy test for the method <code>removeAllProjectRoleTermsOfUse(int projectId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_removeAllProjectRoleTermsOfUse_1() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        instance.removeAllProjectRoleTermsOfUse(projectId);

        assertFalse("'removeAllProjectRoleTermsOfUse' should be correct.",
            hasProjectRoleTermsOfUse(connection, projectId, resourceRoleId, termsOfUseId, groupIndex));
        resourceRoleId = 1;
        termsOfUseId = 2;
        groupIndex = 2;
        assertFalse("'removeAllProjectRoleTermsOfUse' should be correct.",
            hasProjectRoleTermsOfUse(connection, projectId, resourceRoleId, termsOfUseId, groupIndex));
    }

    /**
     * <p>
     * Accuracy test for the method <code>removeAllProjectRoleTermsOfUse(int projectId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_removeAllProjectRoleTermsOfUse_2() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        instance.removeAllProjectRoleTermsOfUse(Integer.MAX_VALUE);

        assertTrue("'removeAllProjectRoleTermsOfUse' should be correct.",
            hasProjectRoleTermsOfUse(connection, projectId, resourceRoleId, termsOfUseId, groupIndex));

    }

    /**
     * <p>
     * Failure test for the method <code>removeAllProjectRoleTermsOfUse(int projectId)</code> with a persistence
     * error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_removeAllProjectRoleTermsOfUse_PersistenceError() throws Exception {
        instance.createProjectRoleTermsOfUse(projectId, resourceRoleId, termsOfUseId, sortOrder, groupIndex);

        instance = new ProjectTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.removeAllProjectRoleTermsOfUse(projectId);
    }

    /**
     * Checks if there is a project role terms of use association.
     *
     * @param connection
     *            the connection.
     * @param groupIndex
     *            the group index to associate.
     * @param resourceRoleId
     *            the role id to associate.
     * @param termsOfUseId
     *            the terms of use id to associategroupIndex groupIndex to associate.
     * @param projectId
     *            the project id to associate.
     *
     * @return <code>true</code> if there is a project role terms of use association; <code>false</code>
     *         otherwise.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static boolean hasProjectRoleTermsOfUse(Connection connection, int projectId, int resourceRoleId,
        long termsOfUseId, int groupIndex) throws Exception {
        PreparedStatement ps = connection.prepareStatement("SELECT '1' FROM project_role_terms_of_use_xref"
            + " WHERE project_id = ? and resource_role_id = ? and terms_of_use_id = ? and group_ind = ?");
        try {
            ps.setInt(1, projectId);
            ps.setInt(2, resourceRoleId);
            ps.setLong(3, termsOfUseId);
            ps.setInt(4, groupIndex);

            ResultSet rs = ps.executeQuery();
            return rs.next();
        } finally {
            ps.close();
        }
    }
}
