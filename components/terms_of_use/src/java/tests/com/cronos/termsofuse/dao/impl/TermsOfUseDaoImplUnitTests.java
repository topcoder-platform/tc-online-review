/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;
import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUseDependencyNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Unit tests for {@link TermsOfUseDaoImpl} class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Added test cases for methods for managing terms of use dependencies.</li>
 * <li>Updated test cases to support removal of memberAgreeable and electronicallySignable properties
 *     of TermsOfUse.</li>
 * <li>Updated test cases to support adding of TermsOfUse#agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class TermsOfUseDaoImplUnitTests extends BaseUnitTests {
    /**
     * <p>
     * Represents the <code>TermsOfUseDaoImpl</code> instance used in tests.
     * </p>
     */
    private TermsOfUseDaoImpl instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject configurationObject;

    /**
     * <p>
     * Represents the TermsOfUse instance used in tests.
     * </p>
     */
    private TermsOfUse termsOfUse;

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
        return new JUnit4TestAdapter(TermsOfUseDaoImplUnitTests.class);
    }

    /**
     * <p>
     * Sets up the unit tests.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Before
    public void setUp() throws Exception {
        super.setUp();
        connection = getConnection();

        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_TERMS);

        instance = new TermsOfUseDaoImpl(configurationObject);

        termsOfUse = new TermsOfUse();
        termsOfUse.setTermsOfUseTypeId(3);
        termsOfUse.setTitle("t5");
        termsOfUse.setUrl("url5");
        TermsOfUseAgreeabilityType nonAgreeableType = new TermsOfUseAgreeabilityType();
        nonAgreeableType.setTermsOfUseAgreeabilityTypeId(1);
        termsOfUse.setAgreeabilityType(nonAgreeableType);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new TermsOfUseDaoImpl(configurationObject);

        assertNotNull("'dbConnectionFactory' should be correct.", instance.getDBConnectionFactory());
        assertNotNull("'log' should be correct.", instance.getLog());
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with configurationObject is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_configurationObjectNull() {
        configurationObject = null;

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'dbConnectionFactoryConfig' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigMissing() throws Exception {
        configurationObject.removeChild("dbConnectionFactoryConfig");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid1() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig")
            .removeChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * failed to create the db connection factor.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_dbConnectionFactoryConfigInvalid2() throws Exception {
        configurationObject.getChild("dbConnectionFactoryConfig")
            .getChild("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl")
            .getChild("connections").setPropertyValue("default", "not_exist");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'loggerName' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameMissing() throws Exception {
        configurationObject.removeProperty("loggerName");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'loggerName' is not a String.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_loggerNameNotString() throws Exception {
        configurationObject.setPropertyValue("loggerName", 1);

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'idGeneratorName' is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_idGeneratorNameMissing() throws Exception {
        configurationObject.removeProperty("idGeneratorName");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'idGeneratorName' is not a String.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_idGeneratorNameNotString() throws Exception {
        configurationObject.setPropertyValue("idGeneratorName", 1);

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>TermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
     * 'idGeneratorName' is invalid.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void testCtor_idGeneratorNameInvalid() throws Exception {
        configurationObject.setPropertyValue("idGeneratorName", "not_exist");

        new TermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createTermsOfUse_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        TermsOfUse res = getTermsOfUse(connection, termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("createTermsOfUse", res,
            new Object[] {5L, 3, "t5", "url5", 1L, "Non-agreeable", "Non-agreeable"});

        assertEquals("'createTermsOfUse' should be correct.",
            "text5", getTermsOfUseText(connection, termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Accuracy test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createTermsOfUse_2() throws Exception {
        termsOfUse.setUrl(null);
        instance.createTermsOfUse(termsOfUse, null);

        TermsOfUse res = getTermsOfUse(connection, termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("createTermsOfUse",
            res, new Object[] {5L, 3, "t5", null, 1L, "Non-agreeable", "Non-agreeable"});

        assertNull("'createTermsOfUse' should be correct.",
            getTermsOfUseText(connection, termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Accuracy test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createTermsOfUse_3() throws Exception {
        instance.createTermsOfUse(termsOfUse, "");

        TermsOfUse res = getTermsOfUse(connection, termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("createTermsOfUse",
            res, new Object[] {5L, 3, "t5", "url5", 1L, "Non-agreeable", "Non-agreeable"});

        assertEquals("'createTermsOfUse' should be correct.",
            "", getTermsOfUseText(connection, termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Failure test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code> with
     * termsOfUse is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createTermsOfUse_termsOfUseNull() throws Exception {
        termsOfUse = null;

        instance.createTermsOfUse(termsOfUse, "text5");
    }

    /**
     * <p>
     * Failure test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code> with
     * termsOfUse.getAgreeabilityType() is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_createTermsOfUse_termsOfUseAgreeabilityTypeNull() throws Exception {
        termsOfUse.setAgreeabilityType(null);

        instance.createTermsOfUse(termsOfUse, "text5");
    }

    /**
     * <p>
     * Failure test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code> with
     * failed to generate a new ID.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_createTermsOfUse_IDGenerationError() throws Exception {
        TestsHelper.clearDB(connection);

        setField(TestsHelper.getField(instance, "idGenerator"), "idsLeft", 0);

        instance.createTermsOfUse(termsOfUse, "text5");
    }

    /**
     * <p>
     * Failure test for the method <code>createTermsOfUse(TermsOfUse termsOfUse, String termsText)</code> with
     * a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_createTermsOfUse_PersistenceError() throws Exception {
        termsOfUse.setTitle(null);

        instance.createTermsOfUse(termsOfUse, "text5");
    }

    /**
     * <p>
     * Accuracy test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_updateTermsOfUse_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        termsOfUse.setTermsOfUseTypeId(4);
        termsOfUse.setTitle("t6");
        termsOfUse.setUrl("url6");
        TermsOfUseAgreeabilityType nonAgreeableType = new TermsOfUseAgreeabilityType();
        nonAgreeableType.setTermsOfUseAgreeabilityTypeId(2);
        termsOfUse.setAgreeabilityType(nonAgreeableType);

        instance.updateTermsOfUse(termsOfUse);

        TermsOfUse res = getTermsOfUse(connection, termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("updateTermsOfUse", res,
            new Object[] {5L, 4, "t6", "url6", 2L, "Non-electronically-agreeable", "Non-electronically-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_updateTermsOfUse_2() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        termsOfUse.setUrl(null);

        instance.updateTermsOfUse(termsOfUse);

        TermsOfUse res = getTermsOfUse(connection, termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("updateTermsOfUse", res,
            new Object[] {5L, 3, "t5", null, 1L, "Non-agreeable", "Non-agreeable"});
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code> with
     * termsOfUse is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_updateTermsOfUse_termsOfUseNull() throws Exception {
        termsOfUse = null;

        instance.updateTermsOfUse(termsOfUse);
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code> with
     * termsOfUse.getAgreeabilityType() is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_updateTermsOfUse_termsOfUseAgreeabilityTypeNull() throws Exception {
        termsOfUse.setAgreeabilityType(null);

        instance.updateTermsOfUse(termsOfUse);
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code> with the entity was not
     * found in the database. <br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_updateTermsOfUse_EntityNotFoundError() throws Exception {
        termsOfUse.setTermsOfUseId(Long.MAX_VALUE);

        instance.updateTermsOfUse(termsOfUse);
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUse(TermsOfUse termsOfUse)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_updateTermsOfUse_PersistenceError() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        termsOfUse.setTitle(null);

        instance.updateTermsOfUse(termsOfUse);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUse(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUse_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        TermsOfUse res = instance.getTermsOfUse(termsOfUse.getTermsOfUseId());

        TestsHelper.checkTermsOfUse("getTermsOfUse", res,
            new Object[] {5L, 3, "t5", "url5", 1L, "Non-agreeable", "Non-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUse(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUse_2() throws Exception {
        TermsOfUse res = instance.getTermsOfUse(Long.MAX_VALUE);

        assertNull("'getTermsOfUse' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUse(long termsOfUseId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUse_PersistenceError() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUse(termsOfUse.getTermsOfUseId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteTermsOfUse(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_deleteTermsOfUse() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        instance.deleteTermsOfUse(termsOfUse.getTermsOfUseId());

        assertNull("'deleteTermsOfUse' should be correct.",
            getTermsOfUse(connection, termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Failure test for the method <code>deleteTermsOfUse(long termsOfUseId)</code> with the entity was not found in
     * the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_deleteTermsOfUse_EntityNotFoundError() throws Exception {
        instance.deleteTermsOfUse(Long.MAX_VALUE);
    }

    /**
     * <p>
     * Failure test for the method <code>deleteTermsOfUse(long termsOfUseId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_deleteTermsOfUse_PersistenceError() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.deleteTermsOfUse(termsOfUse.getTermsOfUseId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByTypeId(int termsOfUseTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseByTypeId_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        List<TermsOfUse> res = instance.getTermsOfUseByTypeId(termsOfUse.getTermsOfUseTypeId());

        assertEquals("'getTermsOfUseByTypeId' should be correct.", 1, res.size());
        TestsHelper.checkTermsOfUse("getTermsOfUseByTypeId",
            res.get(0), new Object[] {5L, 3, "t5", "url5", 1L, "Non-agreeable", "Non-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByTypeId(int termsOfUseTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseByTypeId_2() throws Exception {
        List<TermsOfUse> res = instance.getTermsOfUseByTypeId(1);

        assertEquals("'getTermsOfUseByTypeId' should be correct.", 3, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByTypeId(int termsOfUseTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseByTypeId_3() throws Exception {
        List<TermsOfUse> res = instance.getTermsOfUseByTypeId(Integer.MAX_VALUE);

        assertEquals("'getTermsOfUseByTypeId' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUseByTypeId(int termsOfUseTypeId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUseByTypeId_PersistenceError() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUseByTypeId(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getAllTermsOfUse()</code>.<br>
     * The result should be correct.
     * </p>
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getAllTermsOfUse_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        List<TermsOfUse> res = instance.getAllTermsOfUse();

        assertEquals("'getAllTermsOfUse' should be correct.", 5, res.size());
        for (TermsOfUse terms : res) {
            if (terms.getTermsOfUseId() == 5) {
                TestsHelper.checkTermsOfUse("getAllTermsOfUse",
                    terms, new Object[] {5L, 3, "t5", "url5", 1L, "Non-agreeable", "Non-agreeable"});
            }
        }
    }

    /**
     * <p>
     * Accuracy test for the method <code>getAllTermsOfUse()</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getAllTermsOfUse_2() throws Exception {
        List<TermsOfUse> res = instance.getAllTermsOfUse();

        assertEquals("'getAllTermsOfUse' should be correct.", 4, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getAllTermsOfUse()</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getAllTermsOfUse_3() throws Exception {
        TestsHelper.clearDB(connection);

        List<TermsOfUse> res = instance.getAllTermsOfUse();

        assertEquals("'getAllTermsOfUse' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>getAllTermsOfUse()</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getAllTermsOfUse_PersistenceError() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getAllTermsOfUse();
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseType(int termsOfUseTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseType_1() throws Exception {
        TermsOfUseType res = instance.getTermsOfUseType(1);

        assertEquals("'getTermsOfUseType' should be correct.", "type1", res.getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseType(int termsOfUseTypeId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseType_2() throws Exception {
        TermsOfUseType res = instance.getTermsOfUseType(Integer.MAX_VALUE);

        assertNull("'getTermsOfUseType' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUseType(int termsOfUseTypeId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUseType_PersistenceError() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUseType(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>updateTermsOfUseType(TermsOfUseType termsType)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_updateTermsOfUseType_1() throws Exception {
        TermsOfUseType termsType = new TermsOfUseType();
        termsType.setTermsOfUseTypeId(1);
        termsType.setDescription("new");

        instance.updateTermsOfUseType(termsType);

        assertEquals("'updateTermsOfUseType' should be correct.",
            "new", instance.getTermsOfUseType(1).getDescription());
    }

    /**
     * <p>
     * Accuracy test for the method <code>updateTermsOfUseType(TermsOfUseType termsType)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_updateTermsOfUseType_2() throws Exception {
        TermsOfUseType termsType = new TermsOfUseType();
        termsType.setTermsOfUseTypeId(1);
        termsType.setDescription(null);

        instance.updateTermsOfUseType(termsType);

        assertNull("'updateTermsOfUseType' should be correct.",
            instance.getTermsOfUseType(1).getDescription());
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUseType(TermsOfUseType termsType)</code> with termsType is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_updateTermsOfUseType_termsTypeNull() throws Exception {
        instance.updateTermsOfUseType(null);
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUseType(TermsOfUseType termsType)</code> with the entity was
     * not found in the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_updateTermsOfUseType_EntityNotFoundError() throws Exception {
        TermsOfUseType termsType = new TermsOfUseType();
        termsType.setTermsOfUseTypeId(Integer.MAX_VALUE);

        instance.updateTermsOfUseType(termsType);
    }

    /**
     * <p>
     * Failure test for the method <code>updateTermsOfUseType(TermsOfUseType termsType)</code> with a persistence
     * error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_updateTermsOfUseType_PersistenceError() throws Exception {
        TermsOfUseType termsType = new TermsOfUseType();
        termsType.setTermsOfUseTypeId(1);

        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.updateTermsOfUseType(termsType);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseText(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseText_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        String res = instance.getTermsOfUseText(termsOfUse.getTermsOfUseId());

        assertEquals("'getTermsOfUseText' should be correct.", "text5", res);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseText(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseText_2() throws Exception {
        instance.createTermsOfUse(termsOfUse, null);

        String res = instance.getTermsOfUseText(termsOfUse.getTermsOfUseId());

        assertNull("'getTermsOfUseText' should be correct.", res);
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUseText(long termsOfUseId)</code> with the entity was not found
     * in the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_getTermsOfUseText_EntityNotFoundError() throws Exception {
        instance.getTermsOfUseText(Long.MAX_VALUE);
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUseText(long termsOfUseId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUseText_PersistenceError() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUseText(termsOfUse.getTermsOfUseId());
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTermsOfUseText(long termsOfUseId, String text)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_setTermsOfUseText_1() throws Exception {
        instance.createTermsOfUse(termsOfUse, "");

        instance.setTermsOfUseText(termsOfUse.getTermsOfUseId(), "text5");

        assertEquals("'setTermsOfUseText' should be correct.",
            "text5", instance.getTermsOfUseText(termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Accuracy test for the method <code>setTermsOfUseText(long termsOfUseId, String text)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_setTermsOfUseText_2() throws Exception {
        instance.createTermsOfUse(termsOfUse, "text5");

        instance.setTermsOfUseText(termsOfUse.getTermsOfUseId(), null);

        assertNull("'setTermsOfUseText' should be correct.",
            instance.getTermsOfUseText(termsOfUse.getTermsOfUseId()));
    }

    /**
     * <p>
     * Failure test for the method <code>setTermsOfUseText(long termsOfUseId, String text)</code> with the entity
     * was not found in the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_setTermsOfUseText_EntityNotFoundError() throws Exception {
        instance.setTermsOfUseText(Long.MAX_VALUE, "text5");
    }

    /**
     * <p>
     * Failure test for the method <code>setTermsOfUseText(long termsOfUseId, String text)</code> with a persistence
     * error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_setTermsOfUseText_PersistenceError() throws Exception {
        instance.createTermsOfUse(termsOfUse, "");

        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.setTermsOfUseText(termsOfUse.getTermsOfUseId(), "text5");
    }

    /**
     * <p>
     * Accuracy test for the method <code>createDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with 1 dependent/dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_createDependencyRelationship_1() throws Exception {
        instance.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = getDependencyTermsOfUse(connection, 1);

        assertEquals("'createDependencyRelationship' should be correct.", 1, res.size());

        TestsHelper.checkTermsOfUse("createDependencyRelationship", res.get(0),
            new Object[] {2L, 1, "t2", "url2", 1L, "Non-agreeable", "Non-agreeable"});

        res = getDependentTermsOfUse(connection, 2);

        assertEquals("'createDependencyRelationship' should be correct.", 1, res.size());

        TestsHelper.checkTermsOfUse("createDependencyRelationship", res.get(0),
            new Object[] {1L, 1, "t1", "url1", 2L, "Non-electronically-agreeable", "Non-electronically-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>createDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with 2 dependents/dependencies.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_createDependencyRelationship_2() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(1, 3);
        instance.createDependencyRelationship(3, 2);

        List<TermsOfUse> res = getDependencyTermsOfUse(connection, 1);

        assertEquals("'createDependencyRelationship' should be correct.", 2, res.size());
        List<Long> ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(2L));
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(3L));

        res = getDependentTermsOfUse(connection, 2);
        ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(1L));
        assertTrue("'createDependencyRelationship' should be correct.", ids.contains(3L));
    }

    /**
     * <p>
     * Failure test for the method <code>createDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with creation of the specified relationship will lead
     * to a dependency loop.<br>
     * <code>TermsOfUseCyclicDependencyException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUseCyclicDependencyException.class)
    public void test_createDependencyRelationship_CyclicDependencyError1() throws Exception {
        instance.createDependencyRelationship(1, 1);
    }

    /**
     * <p>
     * Failure test for the method <code>createDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with creation of the specified relationship will lead
     * to a dependency loop.<br>
     * <code>TermsOfUseCyclicDependencyException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUseCyclicDependencyException.class)
    public void test_createDependencyRelationship_CyclicDependencyError2() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(1, 3);
        instance.createDependencyRelationship(2, 4);
        instance.createDependencyRelationship(4, 1);
    }

    /**
     * <p>
     * Failure test for the method <code>createDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_createDependencyRelationship_Error() throws Exception {
        TestsHelper.clearDB(connection);

        setField(TestsHelper.getField(instance, "idGenerator"), "idsLeft", 0);

        instance.createDependencyRelationship(1, 2);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependencyTermsOfUse(long dependentTermsOfUseId)</code>
     * with no dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependencyTermsOfUse_1() throws Exception {
        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependencyTermsOfUse(long dependentTermsOfUseId)</code>
     * with 1 dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependencyTermsOfUse_2() throws Exception {
        instance.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 1, res.size());

        TestsHelper.checkTermsOfUse("getDependencyTermsOfUse", res.get(0),
            new Object[] {2L, 1, "t2", "url2", 1L, "Non-agreeable", "Non-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependencyTermsOfUse(long dependentTermsOfUseId)</code>
     * with 2 dependencies.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependencyTermsOfUse_3() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(1, 3);

        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'getDependencyTermsOfUse' should be correct.", 2, res.size());

        List<Long> ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());

        assertTrue("'getDependencyTermsOfUse' should be correct.", ids.contains(2L));
        assertTrue("'getDependencyTermsOfUse' should be correct.", ids.contains(3L));
    }

    /**
     * <p>
     * Failure test for the method <code>getDependencyTermsOfUse(long dependentTermsOfUseId)</code> with a
     * persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getDependencyTermsOfUse_Error() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getDependencyTermsOfUse(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependentTermsOfUse(long dependentTermsOfUseId)</code>
     * with no dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependentTermsOfUse_1() throws Exception {
        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependentTermsOfUse(long dependentTermsOfUseId)</code>
     * with 1 dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependentTermsOfUse_2() throws Exception {
        instance.createDependencyRelationship(1, 2);

        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 1, res.size());

        TestsHelper.checkTermsOfUse("getDependentTermsOfUse", res.get(0),
            new Object[] {1L, 1, "t1", "url1", 2L, "Non-electronically-agreeable", "Non-electronically-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>getDependentTermsOfUse(long dependentTermsOfUseId)</code>
     * with 2 dependencies.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_getDependentTermsOfUse_3() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(3, 2);

        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'getDependentTermsOfUse' should be correct.", 2, res.size());

        List<Long> ids = Arrays.asList(res.get(0).getTermsOfUseId(), res.get(1).getTermsOfUseId());

        assertTrue("'getDependentTermsOfUse' should be correct.", ids.contains(1L));
        assertTrue("'getDependentTermsOfUse' should be correct.", ids.contains(3L));
    }

    /**
     * <p>
     * Failure test for the method <code>getDependentTermsOfUse(long dependentTermsOfUseId)</code> with a
     * persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getDependentTermsOfUse_Error() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getDependentTermsOfUse(2);
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteDependencyRelationship() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.deleteDependencyRelationship(1, 2);

        List<TermsOfUse> res = getDependencyTermsOfUse(connection, 1);

        assertEquals("'deleteDependencyRelationship' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>deleteDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with dependency relationship between the specified dependency
     * and dependent terms of use doesn't exist.<br>
     * <code>TermsOfUseDependencyNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUseDependencyNotFoundException.class)
    public void test_deleteDependencyRelationship_DependencyNotFoundError() throws Exception {
        instance.deleteDependencyRelationship(1, 2);
    }

    /**
     * <p>
     * Failure test for the method <code>deleteDependencyRelationship(long dependentTermsOfUseId,
     * long dependencyTermsOfUseId)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_deleteDependencyRelationship_Error() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.deleteDependencyRelationship(1, 2);
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependent(long
     * dependentTermsOfUseId)</code> with no dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependent_1() throws Exception {
        instance.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependent(long
     * dependentTermsOfUseId)</code> with 1 dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependent_2() throws Exception {
        instance.createDependencyRelationship(1, 2);

        instance.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependent(long
     * dependentTermsOfUseId)</code> with 2 dependencies.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependent_3() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(1, 3);

        instance.deleteAllDependencyRelationshipsForDependent(1);

        List<TermsOfUse> res = instance.getDependencyTermsOfUse(1);

        assertEquals("'deleteAllDependencyRelationshipsForDependent' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>deleteAllDependencyRelationshipsForDependent(long
     * dependentTermsOfUseId)</code> with a
     * persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_deleteAllDependencyRelationshipsForDependent_Error() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.deleteAllDependencyRelationshipsForDependent(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependency(long
     * dependentTermsOfUseId)</code> with no dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependency_1() throws Exception {
        instance.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependency(long
     * dependentTermsOfUseId)</code> with 1 dependency.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependency_2() throws Exception {
        instance.createDependencyRelationship(1, 2);

        instance.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>deleteAllDependencyRelationshipsForDependency(long
     * dependentTermsOfUseId)</code> with 2 dependencies.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_deleteAllDependencyRelationshipsForDependency_3() throws Exception {
        instance.createDependencyRelationship(1, 2);
        instance.createDependencyRelationship(3, 2);

        instance.deleteAllDependencyRelationshipsForDependency(2);

        List<TermsOfUse> res = instance.getDependentTermsOfUse(2);

        assertEquals("'deleteAllDependencyRelationshipsForDependency' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>deleteAllDependencyRelationshipsForDependency(long
     * dependentTermsOfUseId)</code> with a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_deleteAllDependencyRelationshipsForDependency_Error() throws Exception {
        instance = new TermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.deleteAllDependencyRelationshipsForDependency(2);
    }

    /**
     * Gets the terms of use text by id.
     *
     * @param connection
     *            the connection.
     * @param termsOfUseId
     *            the terms of use id.
     *
     * @return the terms of use text.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static String getTermsOfUseText(Connection connection, long termsOfUseId) throws Exception {
        PreparedStatement ps = connection
            .prepareStatement("SELECT terms_text FROM terms_of_use WHERE terms_of_use_id=?");
        try {
            ps.setLong(1, termsOfUseId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            byte[] termsTextBytes = rs.getBytes(1);
            return (termsTextBytes == null) ? null : new String(termsTextBytes);
        } finally {
            ps.close();
        }
    }

    /**
     * Gets all dependency terms.
     *
     * @param connection
     *            the connection.
     * @param termsOfUseId
     *            the terms of use id.
     *
     * @return the dependency terms.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    private static List<TermsOfUse> getDependencyTermsOfUse(Connection connection, long termsOfUseId) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT terms_of_use_id, terms_of_use_type_id, title, url, tou.terms_of_use_agreeability_type_id,"
            + " touat.name as terms_of_use_agreeability_type_name,"
            + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use_dependency d"
            + " INNER JOIN terms_of_use tou ON tou.terms_of_use_id = d.dependency_terms_of_use_id"
            + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
            + " = tou.terms_of_use_agreeability_type_id WHERE d.dependent_terms_of_use_id = ?");
        try {
            ps.setLong(1, termsOfUseId);

            ResultSet rs = ps.executeQuery();
            List<TermsOfUse> result = new ArrayList<TermsOfUse>();
            while (rs.next()) {
                result.add(Helper.getTermsOfUse(rs, null, null));
            }
            return result;
        } finally {
            ps.close();
        }
    }

    /**
     * Gets all dependent terms.
     *
     * @param connection
     *            the connection.
     * @param termsOfUseId
     *            the terms of use id.
     *
     * @return the dependent terms.
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    private static List<TermsOfUse> getDependentTermsOfUse(Connection connection, long termsOfUseId) throws Exception {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT terms_of_use_id, terms_of_use_type_id, title, url, tou.terms_of_use_agreeability_type_id,"
            + " touat.name as terms_of_use_agreeability_type_name,"
            + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use_dependency d"
            + " INNER JOIN terms_of_use tou ON tou.terms_of_use_id = d.dependent_terms_of_use_id"
            + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
            + " = tou.terms_of_use_agreeability_type_id WHERE d.dependency_terms_of_use_id = ?");
        try {
            ps.setLong(1, termsOfUseId);

            ResultSet rs = ps.executeQuery();
            List<TermsOfUse> result = new ArrayList<TermsOfUse>();
            while (rs.next()) {
                result.add(Helper.getTermsOfUse(rs, null, null));
            }
            return result;
        } finally {
            ps.close();
        }
    }

    /**
     * Gets a terms of use by id.
     *
     * <p>
     * <em>Changes in 1.1:</em>
     * <ol>
     * <li>Updated code to support removal of memberAgreeable and electronicallySignable properties.</li>
     * <li>Updated code to support adding of agreeabilityType property.</li>
     * </ol>
     * </p>
     *
     * @param connection
     *            the connection.
     * @param termsOfUseId
     *            the terms of use id.
     *
     * @return the terms of use entity.
     *
     * @throws Exception
     *             to JUnit.
     */
    private static TermsOfUse getTermsOfUse(Connection connection, long termsOfUseId) throws Exception {
        PreparedStatement ps = connection.prepareStatement("SELECT terms_of_use_type_id, title, url,"
            + " tou.terms_of_use_agreeability_type_id, touat.name as terms_of_use_agreeability_type_name,"
            + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use tou"
            + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
            + " = tou.terms_of_use_agreeability_type_id WHERE terms_of_use_id=?");
        try {
            ps.setLong(1, termsOfUseId);

            ResultSet rs = ps.executeQuery();
            TermsOfUse terms = null;
            if (rs.next()) {
                terms = Helper.getTermsOfUse(rs, termsOfUseId, null);
            }
            return terms;
        } finally {
            ps.close();
        }
    }

    /**
     * <p>
     * Sets value to the field of given object.
     * </p>
     *
     * @param obj
     *            the given object.
     * @param field
     *            the field name.
     * @param value
     *            the field value.
     */
    private static void setField(Object obj, String field, Object value) {
        try {
            Field declaredField = obj.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);

            declaredField.set(obj, value);

            declaredField.setAccessible(false);
        } catch (IllegalAccessException e) {
            // Ignore
        } catch (NoSuchFieldException e) {
            // Ignore
        }
    }
}
