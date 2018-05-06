/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.TestsHelper;
import com.topcoder.management.deliverable.late.search.LateDeliverableFilterBuilder;
import com.topcoder.search.builder.filter.AndFilter;
import com.topcoder.search.builder.filter.Filter;
import com.topcoder.search.builder.filter.InFilter;
import com.topcoder.search.builder.filter.LikeFilter;
import com.topcoder.search.builder.filter.NotFilter;

/**
 * <p>
 * Unit tests for {@link LateDeliverableManagerImpl} class.
 * </p>
 *
 * <p>
 * <em>Changes in version 1.0.6:</em>
 * <ol>
 * <li>Added/updated test cases for getLateDeliverableTypes(), update(), retrieve, searchAllLateDeliverables and
 * searchRestrictedLateDeliverables methods.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.0.6
 */
public class LateDeliverableManagerImplUnitTests {
    /**
     * <p>
     * Represents the <code>LateDeliverableManagerImpl</code> instance used in tests.
     * </p>
     */
    private LateDeliverableManagerImpl instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject config;

    /**
     * <p>
     * Represents the connection used in tests.
     * </p>
     */
    private Connection connection;


    /**
     * <p>
     * Represents the late deliverable id used in tests.
     * </p>
     */
    private long lateDeliverableId = 1;

    /**
     * <p>
     * Represents the late deliverable used in tests.
     * </p>
     */
    private LateDeliverable lateDeliverable;

    /**
     * <p>
     * Represents the filter used in tests.
     * </p>
     */
    private Filter filter;

    /**
     * <p>
     * Represents the user id used in tests.
     * </p>
     */
    private long userId = 3;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(LateDeliverableManagerImplUnitTests.class);
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
        TestsHelper.unloadConfig();
        TestsHelper.loadConfig("SearchBundleManager.xml");

        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);

        config = TestsHelper.getConfig();

        instance = new LateDeliverableManagerImpl(config);

        lateDeliverable = instance.retrieve(lateDeliverableId);

        Filter forgivenFilter = LateDeliverableFilterBuilder.createForgivenFilter(true);
        Filter projectIdFilter = LateDeliverableFilterBuilder.createProjectIdFilter(100000);
        filter = new AndFilter(forgivenFilter, projectIdFilter);
    }

    /**
     * <p>
     * Cleans up the unit tests.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @After
    public void tearDown() throws Exception {
        TestsHelper.clearDB(connection);
        TestsHelper.closeConnection(connection);
        connection = null;
        instance = null;

        TestsHelper.unloadConfig();
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverableManagerImpl()</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor1() {
        instance = new LateDeliverableManagerImpl();

        assertNotNull("'nonRestrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "nonRestrictedSearchBundle"));
        assertNotNull("'restrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "restrictedSearchBundle"));
        assertNotNull("'persistence' should be correct.",
            TestsHelper.getField(instance, "persistence"));
        assertNotNull("'log' should be correct.", TestsHelper.getField(instance, "log"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor2() {
        instance = new LateDeliverableManagerImpl(LateDeliverableManagerImpl.DEFAULT_CONFIG_FILENAME,
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);

        assertNotNull("'nonRestrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "nonRestrictedSearchBundle"));
        assertNotNull("'restrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "restrictedSearchBundle"));
        assertNotNull("'persistence' should be correct.",
            TestsHelper.getField(instance, "persistence"));
        assertNotNull("'log' should be correct.", TestsHelper.getField(instance, "log"));
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with filePath is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_filePathNull() {
        new LateDeliverableManagerImpl(null,
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with filePath is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_filePathEmpty() {
        new LateDeliverableManagerImpl(TestsHelper.EMPTY_STRING,
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with namespace is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_namespaceNull() {
        new LateDeliverableManagerImpl(LateDeliverableManagerImpl.DEFAULT_CONFIG_FILENAME,
            null);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with namespace is empty.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor2_namespaceEmpty() {
        new LateDeliverableManagerImpl(LateDeliverableManagerImpl.DEFAULT_CONFIG_FILENAME,
            TestsHelper.EMPTY_STRING);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with filePath is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor2_filePathInvalid1() {
        new LateDeliverableManagerImpl(TestsHelper.TEST_FILES,
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(String filePath, String namespace)</code>
     * with filePath is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor2_filePathInvalid2() {
        new LateDeliverableManagerImpl(TestsHelper.TEST_FILES + "SearchBundleManager.xml",
            LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor3_1() {
        instance = new LateDeliverableManagerImpl(config);

        assertNotNull("'nonRestrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "nonRestrictedSearchBundle"));
        assertNotNull("'restrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "restrictedSearchBundle"));
        assertNotNull("'persistence' should be correct.", TestsHelper.getField(instance, "persistence"));
        assertNotNull("'log' should be correct.", TestsHelper.getField(instance, "log"));
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>.<br>
     * Instance should be correctly created.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test
    public void testCtor3_2() throws Exception {
        config.removeProperty("loggerName");

        instance = new LateDeliverableManagerImpl(config);

        assertNotNull("'nonRestrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "nonRestrictedSearchBundle"));
        assertNotNull("'restrictedSearchBundle' should be correct.",
            TestsHelper.getField(instance, "restrictedSearchBundle"));
        assertNotNull("'persistence' should be correct.", TestsHelper.getField(instance, "persistence"));
        assertNull("'log' should be correct.", TestsHelper.getField(instance, "log"));
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with configuration is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor3_configurationNull() {
        config = null;

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'searchBundleManagerNamespace' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_searchBundleManagerNamespaceMissing() throws Exception {
        config.removeProperty("searchBundleManagerNamespace");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'searchBundleManagerNamespace' is empty.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_searchBundleManagerNamespaceEmpty() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace", TestsHelper.EMPTY_STRING);

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'searchBundleManagerNamespace' is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_searchBundleManagerNamespaceInvalid() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace", "invalid.searchBundleManagerNamespace");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'nonRestrictedSearchBundleName' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_nonRestrictedSearchBundleNameMissing() throws Exception {
        config.removeProperty("nonRestrictedSearchBundleName");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'nonRestrictedSearchBundleName' is empty.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_nonRestrictedSearchBundleNameEmpty() throws Exception {
        config.setPropertyValue("nonRestrictedSearchBundleName", TestsHelper.EMPTY_STRING);

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'nonRestrictedSearchBundleName' is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_nonRestrictedSearchBundleNameInvalid() throws Exception {
        config.setPropertyValue("nonRestrictedSearchBundleName", "nonRestrictedSearchBundleNameInvalid");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'restrictedSearchBundleName' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_restrictedSearchBundleNameMissing() throws Exception {
        config.removeProperty("restrictedSearchBundleName");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'restrictedSearchBundleName' is empty.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_restrictedSearchBundleNameEmpty() throws Exception {
        config.setPropertyValue("restrictedSearchBundleName", TestsHelper.EMPTY_STRING);

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'restrictedSearchBundleName' is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_restrictedSearchBundleNameInvalid() throws Exception {
        config.setPropertyValue("restrictedSearchBundleName", "restrictedSearchBundleNameInvalid");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'objectFactoryConfig' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_objectFactoryConfigMissing() throws Exception {
        config.removeChild("objectFactoryConfig");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'objectFactoryConfig' is invalid.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_objectFactoryConfigInvalid() throws Exception {
        config.getChild("objectFactoryConfig").getChild("DatabaseLateDeliverablePersistence")
            .setPropertyValue("type", "invalid_class");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'persistenceKey' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_persistenceKeyMissing() throws Exception {
        config.removeProperty("persistenceKey");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'persistenceKey' is empty.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_persistenceKeyEmpty() throws Exception {
        config.setPropertyValue("persistenceKey", TestsHelper.EMPTY_STRING);

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with 'persistenceConfig' is missing.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_persistenceConfigMissing() throws Exception {
        config.removeChild("persistenceConfig");

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with failed to create an instance of LateDeliverablePersistence.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_CreateLateDeliverablePersistenceFailed1() throws Exception {
        config.getChild("objectFactoryConfig").getChild("DatabaseLateDeliverablePersistence")
            .setPropertyValue("type", Integer.class.getName());

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Failure test for the constructor <code>LateDeliverableManagerImpl(ConfigurationObject configuration)</code>
     * with failed to create an instance of LateDeliverablePersistence.<br>
     * <code>LateDeliverableManagementConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUni.
     */
    @Test(expected = LateDeliverableManagementConfigurationException.class)
    public void testCtor3_CreateLateDeliverablePersistenceFailed2() throws Exception {
        config.getChild("objectFactoryConfig").getChild("DatabaseLateDeliverablePersistence")
            .setPropertyValue("type", String.class.getName());

        new LateDeliverableManagerImpl(config);
    }

    /**
     * <p>
     * Accuracy test for the method <code>update(LateDeliverable lateDeliverable)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_update() throws Exception {
        assertFalse("'lateDeliverable' should be correct.", lateDeliverable.isForgiven());
        assertNull("'lateDeliverable' should be correct.", lateDeliverable.getExplanation());
        assertNull("'lateDeliverable' should be correct.", lateDeliverable.getDelay());

        lateDeliverable.setForgiven(true);
        lateDeliverable.setExplanation("OR didn't work");
        lateDeliverable.setDelay(10L);
        instance.update(lateDeliverable);

        LateDeliverable updatedLateDeliverable = instance.retrieve(1);

        assertTrue("'update' should be correct.", updatedLateDeliverable.isForgiven());
        assertEquals("'update' should be correct.",
            "OR didn't work", lateDeliverable.getExplanation().trim());
        assertEquals("'update' should be correct.",
            10L, lateDeliverable.getDelay().longValue());
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with lateDeliverable is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableNull() throws Exception {
        lateDeliverable = null;

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with lateDeliverable.getId()
     * is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableIdNegative() throws Exception {
        lateDeliverable.setId(-1);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with lateDeliverable.getId()
     * is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableIdZero() throws Exception {
        lateDeliverable.setId(0);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with
     * lateDeliverable.getDeadline() is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableDeadlineNull() throws Exception {
        lateDeliverable.setDeadline(null);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with
     * lateDeliverable.getCreateDate() is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableCreateDateNull() throws Exception {
        lateDeliverable.setCreateDate(null);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with lateDeliverable.getType()
     * is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableTypeNull() throws Exception {
        lateDeliverable.setType(null);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with
     * lateDeliverable.getType().getId() is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableTypeIdNegative() throws Exception {
        lateDeliverable.getType().setId(-1);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with
     * lateDeliverable.getType().getId() is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_update_lateDeliverableTypeIdZero() throws Exception {
        lateDeliverable.getType().setId(0);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with late deliverable with ID
     * equal to lateDeliverable.getId() doesn't exist in persistence.<br>
     * <code>LateDeliverableNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverableNotFoundException.class)
    public void test_update_LateDeliverableNotFound() throws Exception {
        lateDeliverable.setId(Long.MAX_VALUE);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Failure test for the method <code>update(LateDeliverable lateDeliverable)</code> with an error occurred.<br>
     * <code>LateDeliverablePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverablePersistenceException.class)
    public void test_update_Error() throws Exception {
        config.getChild("persistenceConfig").getChild("dbConnectionFactoryConfig")
            .getChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl")
            .getChild("connections").getChild("myConnection").getChild("parameters")
            .setPropertyValue("password", "invalid_\n_password");
        instance = new LateDeliverableManagerImpl(config);

        instance.update(lateDeliverable);
    }

    /**
     * <p>
     * Accuracy test for the method <code>retrieve(long lateDeliverableId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_retrieve_1() throws Exception {
        lateDeliverable = instance.retrieve(lateDeliverableId);

        assertEquals("'retrieve' should be correct.", 1, lateDeliverable.getId());
        assertEquals("'retrieve' should be correct.", 101, lateDeliverable.getProjectPhaseId());
        assertEquals("'retrieve' should be correct.", 1001, lateDeliverable.getResourceId());
        assertEquals("'retrieve' should be correct.", 4, lateDeliverable.getDeliverableId());
        assertFalse("'retrieve' should be correct.", lateDeliverable.isForgiven());
        assertNull("'retrieve' should be correct.", lateDeliverable.getExplanation());

        LateDeliverableType type = lateDeliverable.getType();
        assertEquals("'retrieve' should be correct.", 1, type.getId());
        assertEquals("'retrieve' should be correct.", "Missed Deadline", type.getName());
        assertEquals("'retrieve' should be correct.", "Missed Deadline", type.getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>retrieve(long lateDeliverableId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_retrieve_2() throws Exception {
        lateDeliverableId = Long.MAX_VALUE;

        assertNull("'retrieve' should be correct.", instance.retrieve(lateDeliverableId));
    }

    /**
     * <p>
     * Failure test for the method <code>retrieve(long lateDeliverableId)</code> with lateDeliverableId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_retrieve_lateDeliverableIdNegative() throws Exception {
        lateDeliverableId = -1;

        instance.retrieve(lateDeliverableId);
    }

    /**
     * <p>
     * Failure test for the method <code>retrieve(long lateDeliverableId)</code> with lateDeliverableId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_retrieve_lateDeliverableIdZero() throws Exception {
        lateDeliverableId = 0;

        instance.retrieve(lateDeliverableId);
    }

    /**
     * <p>
     * Failure test for the method <code>retrieve(long lateDeliverableId)</code> with an error occurred.<br>
     * <code>LateDeliverablePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverablePersistenceException.class)
    public void test_retrieve_Error1() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid1");
        instance = new LateDeliverableManagerImpl(config);

        instance.retrieve(lateDeliverableId);
    }

    /**
     * <p>
     * Failure test for the method <code>retrieve(long lateDeliverableId)</code> with an error occurred.<br>
     * <code>LateDeliverableManagementException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverableManagementException.class)
    public void test_retrieve_Error2() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid2");
        instance = new LateDeliverableManagerImpl(config);

        instance.retrieve(lateDeliverableId);
    }

    /**
     * <p>
     * Failure test for the method <code>retrieve(long lateDeliverableId)</code> with an error occurred.<br>
     * <code>LateDeliverableManagementException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverableManagementException.class)
    public void test_retrieve_Error3() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid3");
        instance = new LateDeliverableManagerImpl(config);

        instance.retrieve(lateDeliverableId);
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_1() throws Exception {
        lateDeliverable.setForgiven(true);
        lateDeliverable.setExplanation("OR didn't work");
        lateDeliverable.setExplanationDate(new Date());
        lateDeliverable.setResponse("Accepted");
        lateDeliverable.setResponseDate(new Date());
        lateDeliverable.setCompensatedDeadline(new Date());
        lateDeliverable.getType().setId(2);
        instance.update(lateDeliverable);

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchAllLateDeliverables' should be correct.", 1, lateDeliverable.getId());
        assertEquals("'searchAllLateDeliverables' should be correct.", 100000, lateDeliverable.getProjectId());
        assertEquals("'searchAllLateDeliverables' should be correct.", 101, lateDeliverable.getProjectPhaseId());
        assertEquals("'searchAllLateDeliverables' should be correct.", 1001, lateDeliverable.getResourceId());
        assertEquals("'searchAllLateDeliverables' should be correct.", 4, lateDeliverable.getDeliverableId());
        assertTrue("'searchAllLateDeliverables' should be correct.", lateDeliverable.isForgiven());
        assertEquals("'searchAllLateDeliverables' should be correct.",
            "OR didn't work", lateDeliverable.getExplanation().trim());
        assertNotNull("'searchAllLateDeliverables' should be correct.",
            lateDeliverable.getExplanationDate());
        assertEquals("'searchAllLateDeliverables' should be correct.",
            "Accepted", lateDeliverable.getResponse().trim());
        assertNotNull("'searchAllLateDeliverables' should be correct.",
            lateDeliverable.getResponseDate());
        assertNotNull("'searchAllLateDeliverables' should be correct.",
            lateDeliverable.getCompensatedDeadline());
        assertEquals("'searchAllLateDeliverables' should be correct.",
            2, lateDeliverable.getType().getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_2() throws Exception {
        filter = null;
        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 2, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_3() throws Exception {
        filter = LateDeliverableFilterBuilder.createProjectIdFilter(Long.MAX_VALUE);

        assertEquals("'searchAllLateDeliverables' should be correct.",
            0, instance.searchAllLateDeliverables(filter).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_4() throws Exception {
        filter = LateDeliverableFilterBuilder.createProjectIdFilter(Long.MAX_VALUE);

        assertEquals("'searchAllLateDeliverables' should be correct.",
            0, instance.searchAllLateDeliverables(filter).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_5() throws Exception {
        lateDeliverable.setForgiven(true);
        lateDeliverable.setExplanation("OR didn't work");
        instance.update(lateDeliverable);

        filter = new LikeFilter("explanation", "SW:OR");

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchAllLateDeliverables' should be correct.", 1, lateDeliverable.getId());
        assertEquals("'searchAllLateDeliverables' should be correct.",
            "OR didn't work", lateDeliverable.getExplanation().trim());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_6() throws Exception {
        filter = new NotFilter(new InFilter("id", Arrays.asList(1, 3)));

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchAllLateDeliverables' should be correct.", 2, lateDeliverable.getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_7() throws Exception {
        filter = LateDeliverableFilterBuilder.createUserHandleFilter("user1");

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchAllLateDeliverables' should be correct.", 1, lateDeliverable.getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchAllLateDeliverables_8() throws Exception {
        filter = LateDeliverableFilterBuilder.createUserHandleFilter("user_no");

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchAllLateDeliverables(Filter filter)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test
    public void test_searchAllLateDeliverables_9() throws Exception {
        filter = LateDeliverableFilterBuilder.createLateDeliverableTypeIdFilter(1);

        List<LateDeliverable> res = instance.searchAllLateDeliverables(filter);

        assertEquals("'searchAllLateDeliverables' should be correct.", 2, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>searchAllLateDeliverables(Filter filter)</code> with an error occurred.<br>
     * <code>LateDeliverablePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverablePersistenceException.class)
    public void test_searchAllLateDeliverables_Error1() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid1");
        instance = new LateDeliverableManagerImpl(config);

        instance.searchAllLateDeliverables(filter);
    }

    /**
     * <p>
     * Failure test for the method <code>searchAllLateDeliverables(Filter filter)</code> with an error occurred.<br>
     * <code>LateDeliverableManagementException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverableManagementException.class)
    public void test_searchAllLateDeliverables_Error2() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid2");
        instance = new LateDeliverableManagerImpl(config);

        instance.searchAllLateDeliverables(filter);
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchRestrictedLateDeliverables_1() throws Exception {
        Filter categoryFilter = LateDeliverableFilterBuilder.createProjectCategoryIdFilter(1);
        Filter activeProjectFilter = LateDeliverableFilterBuilder.createProjectStatusIdFilter(1);
        filter = new AndFilter(categoryFilter, activeProjectFilter);

        List<LateDeliverable> res = instance.searchRestrictedLateDeliverables(filter, userId);

        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 2, lateDeliverable.getId());
        assertEquals("'searchAllLateDeliverables' should be correct.", 100001, lateDeliverable.getProjectId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 102, lateDeliverable.getProjectPhaseId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1002, lateDeliverable.getResourceId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 3, lateDeliverable.getDeliverableId());
        assertFalse("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.isForgiven());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getExplanation());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getExplanationDate());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getResponse());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getResponseUser());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getResponseDate());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getCompensatedDeadline());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, lateDeliverable.getType().getId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchRestrictedLateDeliverables_2() throws Exception {
        filter = null;
        List<LateDeliverable> res = instance.searchRestrictedLateDeliverables(filter, userId);

        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 2, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchRestrictedLateDeliverables_3() throws Exception {
        filter = null;
        userId = Long.MAX_VALUE;

        assertEquals("'searchRestrictedLateDeliverables' should be correct.",
            0, instance.searchRestrictedLateDeliverables(filter, userId).size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code>. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_searchRestrictedLateDeliverables_4() throws Exception {
        filter = LateDeliverableFilterBuilder.createUserHandleFilter("user1");
        userId = 1;

        List<LateDeliverable> res = instance.searchRestrictedLateDeliverables(filter, userId);

        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, res.size());

        lateDeliverable = res.get(0);
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1, lateDeliverable.getId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 101, lateDeliverable.getProjectPhaseId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 1001, lateDeliverable.getResourceId());
        assertEquals("'searchRestrictedLateDeliverables' should be correct.", 4, lateDeliverable.getDeliverableId());
        assertFalse("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.isForgiven());
        assertNull("'searchRestrictedLateDeliverables' should be correct.", lateDeliverable.getExplanation());
    }

    /**
     * <p>
     * Failure test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code> with
     * userId is negative.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_searchRestrictedLateDeliverables_userIdNegative() throws Exception {
        userId = -1;

        instance.searchRestrictedLateDeliverables(filter, userId);
    }

    /**
     * <p>
     * Failure test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code> with
     * userId is zero.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_searchRestrictedLateDeliverables_userIdZero() throws Exception {
        userId = 0;

        instance.searchRestrictedLateDeliverables(filter, userId);
    }

    /**
     * <p>
     * Failure test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code> with an
     * error occurred.<br>
     * <code>LateDeliverablePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverablePersistenceException.class)
    public void test_searchRestrictedLateDeliverables_Error1() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid1");
        instance = new LateDeliverableManagerImpl(config);

        filter = null;
        instance.searchRestrictedLateDeliverables(filter, userId);
    }

    /**
     * <p>
     * Failure test for the method <code>searchRestrictedLateDeliverables(Filter filter, long userId)</code> with an
     * error occurred.<br>
     * <code>LateDeliverableManagementException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = LateDeliverableManagementException.class)
    public void test_searchRestrictedLateDeliverables_Error2() throws Exception {
        config.setPropertyValue("searchBundleManagerNamespace",
            "LateDeliverableManagerImpl.SearchBuilderManager.Invalid2");
        instance = new LateDeliverableManagerImpl(config);

        filter = null;
        instance.searchRestrictedLateDeliverables(filter, userId);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLateDeliverableTypes()</code> with 2 items. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test
    public void test_getLateDeliverableTypes_1() throws Exception {
        List<LateDeliverableType> res = instance.getLateDeliverableTypes();

        assertEquals("'getLateDeliverableTypes' should be correct.", 2, res.size());
        List<Long> list = Arrays.asList(res.get(0).getId(), res.get(1).getId());
        assertTrue("'getLateDeliverableTypes' should be correct.", list.contains(1L));
        assertTrue("'getLateDeliverableTypes' should be correct.", list.contains(2L));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getLateDeliverableTypes()</code> with no data in the database. <br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test
    public void test_getLateDeliverableTypes_2() throws Exception {
        TestsHelper.clearDB(connection);

        List<LateDeliverableType> res = instance.getLateDeliverableTypes();

        assertEquals("'getLateDeliverableTypes' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>getLateDeliverableTypes()</code> with an error occurred.<br>
     * <code>LateDeliverablePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.0.6
     */
    @Test(expected = LateDeliverablePersistenceException.class)
    public void test_getLateDeliverableTypes_Error() throws Exception {
        config.getChild("persistenceConfig").getChild("dbConnectionFactoryConfig")
            .getChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl").getChild("connections")
            .getChild("myConnection").getChild("parameters").setPropertyValue("password", "invalid_\n_password");
        instance = new LateDeliverableManagerImpl(config);

        instance.getLateDeliverableTypes();
    }
}
