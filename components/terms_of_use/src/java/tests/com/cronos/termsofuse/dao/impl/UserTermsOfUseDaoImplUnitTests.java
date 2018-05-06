/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;
import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.UserBannedException;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Unit tests for {@link UserTermsOfUseDaoImpl} class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Updated test cases to support removal of memberAgreeable and electronicallySignable properties of
 *     TermsOfUse.</li>
 * <li>Updated test cases to support adding of TermsOfUse#agreeabilityType property.</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class UserTermsOfUseDaoImplUnitTests extends BaseUnitTests {
    /**
     * <p>
     * Represents the <code>UserTermsOfUseDaoImpl</code> instance used in tests.
     * </p>
     */
    private UserTermsOfUseDaoImpl instance;

    /**
     * <p>
     * Represents the configuration object used in tests.
     * </p>
     */
    private ConfigurationObject configurationObject;

    /**
     * <p>
     * Represents the user id used in tests.
     * </p>
     */
    private long userId = 3;

    /**
     * <p>
     * Represents the terms of use id used in tests.
     * </p>
     */
    private long termsOfUseId = 2;

    /**
     * <p>
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(UserTermsOfUseDaoImplUnitTests.class);
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

        configurationObject = TestsHelper.getConfig(TestsHelper.CONFIG_USER_TERMS);

        instance = new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject
     * configurationObject)</code>.<br>
     * Instance should be correctly created.
     * </p>
     */
    @Test
    public void testCtor() {
        instance = new UserTermsOfUseDaoImpl(configurationObject);

        assertNotNull("'dbConnectionFactory' should be correct.", instance.getDBConnectionFactory());
        assertNotNull("'log' should be correct.", instance.getLog());
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code>
     * with configurationObject is <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCtor_configurationObjectNull() {
        configurationObject = null;

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
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

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
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

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
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

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
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

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Failure test for the constructor <code>UserTermsOfUseDaoImpl(ConfigurationObject configurationObject)</code> with
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

        new UserTermsOfUseDaoImpl(configurationObject);
    }

    /**
     * <p>
     * Accuracy test for the method <code>createUserTermsOfUse(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createUserTermsOfUse() throws Exception {
        instance.createUserTermsOfUse(userId, termsOfUseId);

        assertTrue("'createUserTermsOfUse' should be correct.", instance.hasTermsOfUse(userId, termsOfUseId));
    }

    /**
     * <p>
     * Failure test for the method <code>createUserTermsOfUse(long userId, long termsOfUseId)</code> with the user
     * is banned to create terms of use.<br>
     * <code>UserBannedException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = UserBannedException.class)
    public void test_createUserTermsOfUse_UserBannedError() throws Exception {
        userId = 1;
        termsOfUseId = 3;

        instance.createUserTermsOfUse(userId, termsOfUseId);
    }

    /**
     * <p>
     * Failure test for the method <code>createUserTermsOfUse(long userId, long termsOfUseId)</code> with
     * a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_createUserTermsOfUse_PersistenceError() throws Exception {
        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.createUserTermsOfUse(userId, termsOfUseId);
    }

    /**
     * <p>
     * Accuracy test for the method <code>removeUserTermsOfUse(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_removeUserTermsOfUse() throws Exception {
        instance.createUserTermsOfUse(userId, termsOfUseId);

        instance.removeUserTermsOfUse(userId, termsOfUseId);

        assertFalse("'removeUserTermsOfUse' should be correct.", instance.hasTermsOfUse(userId, termsOfUseId));
    }

    /**
     * <p>
     * Failure test for the method <code>removeUserTermsOfUse(long userId, long termsOfUseId)</code> with the entity
     * was not found in the database.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_removeUserTermsOfUse_EntityNotFoundError() throws Exception {
        userId = Long.MAX_VALUE;

        instance.removeUserTermsOfUse(userId, termsOfUseId);
    }

    /**
     * <p>
     * Failure test for the method <code>removeUserTermsOfUse(long userId, long termsOfUseId)</code> with
     * a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_removeUserTermsOfUse_PersistenceError() throws Exception {
        instance.createUserTermsOfUse(userId, termsOfUseId);

        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.removeUserTermsOfUse(userId, termsOfUseId);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByUserId(long userId)</code>.<br>
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
    public void test_getTermsOfUseByUserId_1() throws Exception {
        List<TermsOfUse> res = instance.getTermsOfUseByUserId(userId);

        assertEquals("'getTermsOfUseByUserId' should be correct.", 1, res.size());
        TestsHelper.checkTermsOfUse("getTermsOfUseByUserId",
            res.get(0), new Object[] {3L, 1, "t3", "url3", 1L, "Non-agreeable", "Non-agreeable"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByUserId(long userId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseByUserId_2() throws Exception {
        List<TermsOfUse> res = instance.getTermsOfUseByUserId(1);

        assertEquals("'getTermsOfUseByUserId' should be correct.", 2, res.size());
    }

    /**
     * <p>
     * Accuracy test for the method <code>getTermsOfUseByUserId(long userId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getTermsOfUseByUserId_3() throws Exception {
        List<TermsOfUse> res = instance.getTermsOfUseByUserId(Integer.MAX_VALUE);

        assertEquals("'getTermsOfUseByUserId' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>getTermsOfUseByUserId(long userId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUseByUserId_PersistenceError() throws Exception {
        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getTermsOfUseByUserId(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>getUsersByTermsOfUseId(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getUsersByTermsOfUseId_1() throws Exception {
        List<Long> res = instance.getUsersByTermsOfUseId(termsOfUseId);

        assertEquals("'getUsersByTermsOfUseId' should be correct.", 1, res.size());
        assertTrue("'getUsersByTermsOfUseId' should be correct.", res.contains(1L));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getUsersByTermsOfUseId(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getUsersByTermsOfUseId_2() throws Exception {
        instance.createUserTermsOfUse(userId, termsOfUseId);
        List<Long> res = instance.getUsersByTermsOfUseId(termsOfUseId);

        assertEquals("'getUsersByTermsOfUseId' should be correct.", 2, res.size());
        assertTrue("'getUsersByTermsOfUseId' should be correct.", res.contains(1L));
        assertTrue("'getUsersByTermsOfUseId' should be correct.", res.contains(3L));
    }

    /**
     * <p>
     * Accuracy test for the method <code>getUsersByTermsOfUseId(long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_getUsersByTermsOfUseId_3() throws Exception {
        List<Long> res = instance.getUsersByTermsOfUseId(Integer.MAX_VALUE);

        assertEquals("'getUsersByTermsOfUseId' should be correct.", 0, res.size());
    }

    /**
     * <p>
     * Failure test for the method <code>getUsersByTermsOfUseId(long termsOfUseId)</code> with a persistence error has
     * occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getUsersByTermsOfUseId_PersistenceError() throws Exception {
        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.getUsersByTermsOfUseId(1);
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUse(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUse_1() throws Exception {
        instance.createUserTermsOfUse(userId, termsOfUseId);

        assertTrue("'hasTermsOfUse' should be correct.", instance.hasTermsOfUse(userId, termsOfUseId));
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUse(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUse_2() throws Exception {
        assertFalse("'hasTermsOfUse' should be correct.", instance.hasTermsOfUse(Long.MAX_VALUE, termsOfUseId));
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUse(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUse_3() throws Exception {
        assertFalse("'hasTermsOfUse' should be correct.", instance.hasTermsOfUse(userId, Long.MAX_VALUE));
    }

    /**
     * <p>
     * Failure test for the method <code>hasTermsOfUse(long userId, long termsOfUseId)</code> with
     * a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_hasTermsOfUse_PersistenceError() throws Exception {
        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.hasTermsOfUse(userId, termsOfUseId);
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUseBan(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUseBan_1() throws Exception {
        termsOfUseId = 4;

        assertTrue("'hasTermsOfUseBan' should be correct.", instance.hasTermsOfUseBan(userId, termsOfUseId));
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUseBan(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUseBan_2() throws Exception {
        assertFalse("'hasTermsOfUseBan' should be correct.", instance.hasTermsOfUseBan(Long.MAX_VALUE, termsOfUseId));
    }

    /**
     * <p>
     * Accuracy test for the method <code>hasTermsOfUseBan(long userId, long termsOfUseId)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_hasTermsOfUseBan_3() throws Exception {
        assertFalse("'hasTermsOfUseBan' should be correct.", instance.hasTermsOfUseBan(userId, Long.MAX_VALUE));
    }

    /**
     * <p>
     * Failure test for the method <code>hasTermsOfUseBan(long userId, long termsOfUseId)</code> with
     * a persistence error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_hasTermsOfUseBan_PersistenceError() throws Exception {
        instance = new UserTermsOfUseDaoImpl(TestsHelper.getConfig(TestsHelper.CONFIG_INVALID));

        instance.hasTermsOfUseBan(userId, termsOfUseId);
    }
}
