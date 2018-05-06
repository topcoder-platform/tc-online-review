/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import junit.framework.JUnit4TestAdapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cronos.termsofuse.TestsHelper;
import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.model.TermsOfUse;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * Unit tests for {@link Helper} class.
 * </p>
 *
 * <p>
 * <em>Changes in 1.1:</em>
 * <ol>
 * <li>Updated test cases for getTermsOfUse() to support removed and new properties.</li>
 * <li>Added test cases for executeUpdate(Connection, String, Object[]).</li>
 * </ol>
 * </p>
 *
 * @author sparemax
 * @version 1.1
 */
public class HelperUnitTests {
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
     * Adapter for earlier versions of JUnit.
     * </p>
     *
     * @return a test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(HelperUnitTests.class);
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
        connection = TestsHelper.getConnection();
        TestsHelper.clearDB(connection);
        TestsHelper.loadDB(connection);

        config = TestsHelper.getConfig(TestsHelper.CONFIG_TERMS);
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
    }

    /**
     * <p>
     * Tests failure of <code>checkNull(Object value, String name)</code> method with <code>value</code> is
     * <code>null</code>.<br>
     * <code>IllegalArgumentException</code> is expected.
     * </p>
     */
    @Test(expected = IllegalArgumentException.class)
    public void test_checkNull_Null() {
        Object value = null;

        Helper.checkNull(value, "name");
    }

    /**
     * <p>
     * Tests accuracy of <code>checkNull(Object value, String name)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_checkNull() {
        Object value = new Object();

        Helper.checkNull(value, "name");

        // Good
    }

    /**
     * <p>
     * Tests failure of <code>getRequiredProperty(ConfigurationObject config, String key)</code> method with the
     * property is missing.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void test_getRequiredProperty_Missing() {
        Helper.getRequiredProperty(config, "not_exist");
    }

    /**
     * <p>
     * Tests failure of <code>getRequiredProperty(ConfigurationObject config, String key)</code> method with the
     * property is not a String.<br>
     * <code>TermsOfUseDaoConfigurationException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUseDaoConfigurationException.class)
    public void test_getRequiredProperty_NotString() throws Exception {
        config.setPropertyValue("loggerName", 1);

        Helper.getRequiredProperty(config, "loggerName");
    }

    /**
     * <p>
     * Tests accuracy of <code>getRequiredProperty(ConfigurationObject config, String key)</code> method.<br>
     * Result should be correct.
     * </p>
     */
    @Test
    public void test_getRequiredProperty() {
        assertEquals("'getProperty' should be correct.", "loggerName", Helper.getRequiredProperty(config,
            "loggerName"));
    }

    /**
     * <p>
     * Tests accuracy of <code>closeStatement(Statement statement)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_closeStatement_1() throws Exception {
        PreparedStatement ps = connection.prepareStatement("SELECT '1' FROM user_terms_of_use_xref WHERE user_id=?");
        Helper.closeStatement(ps);

        try {
            ps.setLong(1, 1);
        } catch (SQLException e) {
            // Good
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>closeStatement(Statement statement)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_closeStatement_2() throws Exception {
        Helper.closeStatement(null);

        // Good
    }

    /**
     * <p>
     * Tests accuracy of <code>closeConnection(Connection connection)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_closeConnection() throws Exception {
        Connection conn = TestsHelper.getConnection();
        Helper.closeConnection(conn);

        assertTrue("'closeConnection' should be correct.", conn.isClosed());
    }

    /**
     * <p>
     * Tests accuracy of <code>createConnection(DBConnectionFactory dbConnectionFactory)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_createConnection() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        Connection conn = Helper.createConnection(dbConnectionFactory);

        assertNotNull("'createConnection' should be correct.", conn);

        Helper.closeConnection(conn);
    }

    /**
     * <p>
     * Tests accuracy of <code>getTermsOfUse(ResultSet rs, Long termsOfUseId, Integer termsOfUseTypeId)</code>
     * method.<br>
     * Result should be correct.
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
    public void test_getTermsOfUse1() throws Exception {
        PreparedStatement ps = connection.prepareStatement(
            "SELECT terms_of_use_type_id, title, url,"
            + " tou.terms_of_use_agreeability_type_id, touat.name as terms_of_use_agreeability_type_name,"
            + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use tou"
            + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
            + " = tou.terms_of_use_agreeability_type_id WHERE terms_of_use_id=?");

        try {
            ps.setLong(1, 1);

            ResultSet rs = ps.executeQuery();
            rs.next();

            TermsOfUse res = Helper.getTermsOfUse(rs, 1L, null);
            TestsHelper.checkTermsOfUse("getTermsOfUse", res,
                new Object[] {1L, 1, "t1", "url1", 2L, "Non-electronically-agreeable", "Non-electronically-agreeable"});
        } finally {
            ps.close();
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>getTermsOfUse(String signature, Log log, DBConnectionFactory dbConnectionFactory,
     * String sql, Long userId, Integer termsOfUseTypeId)</code> method.<br>
     * Result should be correct.
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
    public void test_getTermsOfUse2() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "SELECT terms_of_use_id, title, url,"
            + " tou.terms_of_use_agreeability_type_id, touat.name as terms_of_use_agreeability_type_name,"
            + " touat.description as terms_of_use_agreeability_type_description FROM terms_of_use tou"
            + " INNER JOIN terms_of_use_agreeability_type_lu touat ON touat.terms_of_use_agreeability_type_id"
            + " = tou.terms_of_use_agreeability_type_id WHERE terms_of_use_type_id=?";

        List<TermsOfUse> res = Helper.getTermsOfUse("getTermsOfUse", LogManager.getLog("loggerName"),
            dbConnectionFactory, sql, null, 1);

        assertEquals("'getTermsOfUse' should be correct.", 3, res.size());
    }

    /**
     * <p>
     * Tests failure of <code>getTermsOfUse(String signature, Log log, DBConnectionFactory dbConnectionFactory,
     * String sql, Long userId, Integer termsOfUseTypeId)</code> method with an error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_getTermsOfUse2_Error() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "invalid";

        Helper.getTermsOfUse("getTermsOfUse", LogManager.getLog("loggerName"),
            dbConnectionFactory, sql, null, 1);
    }

    /**
     * <p>
     * Tests accuracy of <code>executeUpdate(Connection conn, String sql, Object[] values)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test
    public void test_executeUpdate1() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "UPDATE terms_of_use_type SET terms_of_use_type_desc = ?"
            + " WHERE terms_of_use_type_id = ?";

        Connection conn = Helper.createConnection(dbConnectionFactory);

        try {
            Helper.executeUpdate(conn, sql, new Object[] {"new", 1});
        } finally {
            Helper.closeConnection(conn);
        }

        PreparedStatement ps = connection
            .prepareStatement("SELECT terms_of_use_type_desc FROM terms_of_use_type WHERE terms_of_use_type_id=1");
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("'getTermsOfUse' should be correct.", "new", rs.getString(1));
        } finally {
            ps.close();
        }
    }

    /**
     * <p>
     * Tests failure of <code>executeUpdate(Connection conn, String sql, Object[] values)</code> method with an
     * error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     *
     * @since 1.1
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_executeUpdate1_Error() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "invalid";

        Connection conn = Helper.createConnection(dbConnectionFactory);

        try {
            Helper.executeUpdate(conn, sql, new Object[0]);
        } finally {
            Helper.closeConnection(conn);
        }
    }

    /**
     * <p>
     * Tests accuracy of <code>executeUpdate(DBConnectionFactory dbConnectionFactory, String sql,
     * Object[] values)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeUpdate2() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "UPDATE terms_of_use_type SET terms_of_use_type_desc = ?"
            + " WHERE terms_of_use_type_id = ?";

        Helper.executeUpdate(dbConnectionFactory, sql,
            new Object[] {"new", 1});

        PreparedStatement ps = connection
            .prepareStatement("SELECT terms_of_use_type_desc FROM terms_of_use_type WHERE terms_of_use_type_id=1");
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("'getTermsOfUse' should be correct.", "new", rs.getString(1));
        } finally {
            ps.close();
        }
    }

    /**
     * <p>
     * Tests failure of <code>executeUpdate(DBConnectionFactory dbConnectionFactory, String sql,
     * Object[] values)</code> method with an error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_executeUpdate2_Error() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "invalid";

        Helper.executeUpdate(dbConnectionFactory, sql, new Object[0]);
    }

    /**
     * <p>
     * Tests accuracy of <code>executeUpdate(DBConnectionFactory dbConnectionFactory, String sql,
     * Object[] values, String id)</code> method.<br>
     * Result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_executeUpdate3() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "UPDATE terms_of_use_type SET terms_of_use_type_desc = ?"
            + " WHERE terms_of_use_type_id = ?";

        Helper.executeUpdate(dbConnectionFactory, sql,
            new Object[] {"new", 1}, "1");

        PreparedStatement ps = connection
            .prepareStatement("SELECT terms_of_use_type_desc FROM terms_of_use_type WHERE terms_of_use_type_id=1");
        try {
            ResultSet rs = ps.executeQuery();
            rs.next();
            assertEquals("'getTermsOfUse' should be correct.", "new", rs.getString(1));
        } finally {
            ps.close();
        }
    }

    /**
     * <p>
     * Tests failure of <code>executeUpdate(DBConnectionFactory dbConnectionFactory, String sql,
     * Object[] values, String id)</code> method with the entity was not found.<br>
     * <code>EntityNotFoundException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = EntityNotFoundException.class)
    public void test_executeUpdate3_EntityNotFoundError() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "UPDATE terms_of_use_type SET terms_of_use_type_desc = ?"
            + " WHERE terms_of_use_type_id = ?";

        Helper.executeUpdate(dbConnectionFactory, sql,
            new Object[] {"new", Integer.MAX_VALUE}, Integer.toString(Integer.MAX_VALUE));
    }

    /**
     * <p>
     * Tests failure of <code>executeUpdate(DBConnectionFactory dbConnectionFactory, String sql,
     * Object[] values, String id)</code> method with an error has occurred.<br>
     * <code>TermsOfUsePersistenceException</code> is expected.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test(expected = TermsOfUsePersistenceException.class)
    public void test_executeUpdate3_Error() throws Exception {
        DBConnectionFactoryImpl dbConnectionFactory =
            new DBConnectionFactoryImpl(config.getChild("dbConnectionFactoryConfig"));
        String sql = "invalid";

        Helper.executeUpdate(dbConnectionFactory, sql, new Object[0], "1");
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log logger, String signature,
     * String[] paramNames, Object[] paramValues)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance_1() throws Exception {
        Log logger = LogManager.getLog("loggerName");

        Helper.logEntrance(logger, "signature", null, null);
    }

    /**
     * <p>
     * Accuracy test for the method <code>logEntrance(Log logger, String signature,
     * String[] paramNames, Object[] paramValues)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logEntrance_2() throws Exception {
        Log logger = LogManager.getLog("loggerName");

        Helper.logEntrance(logger, "signature", new String[] {"p1", "p2"}, new String[] {"v1", "v2"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log logger, String signature, Object[] value)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit_1() throws Exception {
        Log logger = LogManager.getLog("loggerName");

        Helper.logExit(logger, "signature", null);
    }

    /**
     * <p>
     * Accuracy test for the method <code>logExit(Log logger, String signature, Object[] value)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logExit_2() throws Exception {
        Log logger = LogManager.getLog("loggerName");

        Helper.logExit(logger, "signature", new String[] {"v1", "v2"});
    }

    /**
     * <p>
     * Accuracy test for the method <code>logException(Log log, String signature, T e)</code>.<br>
     * The result should be correct.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    @Test
    public void test_logException() throws Exception {
        Log logger = LogManager.getLog("loggerName");

        Throwable e = new Exception("Test");
        Helper.logException(logger, "signature", e);
    }
}
