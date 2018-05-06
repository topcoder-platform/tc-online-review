/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.failure;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.UserBannedException;
import com.cronos.termsofuse.dao.impl.UserTermsOfUseDaoImpl;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Failure test for {@link UserTermsOfUseDaoImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class UserTermsOfUseDaoImplFailureTest extends BaseFailureTest {
    
    /**
     * <p>
     * Represents the UserTermsOfUseDaoImpl instance to test against.
     * </p>
     */
    private UserTermsOfUseDaoImpl instance;
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(UserTermsOfUseDaoImplFailureTest.class);
        return suite;
    }
    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    protected void setUp() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "userTermsOfUseDao");
		instance = new UserTermsOfUseDaoImpl(configurationObject);
        clearDB();
		initDB();
    }
	/**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to jUnit.
     */
    protected void tearDown() throws Exception {
        instance = null;
        clearDB();
    }
    /**
     * Failure test for method UserTermsOfUseDaoImpl() with null input.
     * Expects IllegalArgumentException.
     */
    public void test_UserTermsOfUseDaoImpl_Null() throws Exception {
        try {
            new UserTermsOfUseDaoImpl(null);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method UserTermsOfUseDaoImpl() with empty config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_UserTermsOfUseDaoImpl_2() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "emptyConfig");
        try {
            new UserTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method UserTermsOfUseDaoImpl() with invalid config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_UserTermsOfUseDaoImpl_3() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "invalidConfig");
        try {
            new UserTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method UserTermsOfUseDaoImpl() with null logger name.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_UserTermsOfUseDaoImpl_4() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "loggerNotFoundConfig");
        try {
            new UserTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method createUserTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createUserTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.createUserTermsOfUse(1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createUserTermsOfUse() with termsOfUse not found.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createUserTermsOfUse_2() throws Exception {
        try {
            instance.createUserTermsOfUse(1, 10000);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createUserTermsOfUse() with user not found.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createUserTermsOfUse_6() throws Exception {
        try {
            instance.createUserTermsOfUse(10000, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createUserTermsOfUse() for duplicate records.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createUserTermsOfUse_3() throws Exception {
        instance.createUserTermsOfUse(1, 1);
        try {
            instance.createUserTermsOfUse(1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createUserTermsOfUse() for the user is banned.
     * Expects UserBannedException.
     */
    public void test_createUserTermsOfUse_4() throws Exception {
        try {
            instance.createUserTermsOfUse(2, 1);
            fail("Expects an error");
        } catch (UserBannedException e) {
            // good
        }
    }
    
    /**
     * Failure test for method removeUserTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_removeUserTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.removeUserTermsOfUse(2, 2);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method removeUserTermsOfUse() with termsOfUse not found.
     * Expects EntityNotFoundException.
     */
    public void test_removeUserTermsOfUse_2() throws Exception {
        try {
            instance.removeUserTermsOfUse(2,1);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method removeUserTermsOfUse() with user not found.
     * Expects EntityNotFoundException.
     */
    public void test_removeUserTermsOfUse_3() throws Exception {
        try {
            instance.removeUserTermsOfUse(100000,1);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method getTermsOfUseByUserId() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getTermsOfUseByUserId_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.getTermsOfUseByUserId(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method getUsersByTermsOfUseId() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getUsersByTermsOfUseId_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.getUsersByTermsOfUseId(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    
    /**
     * Failure test for method hasTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_hasTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.hasTermsOfUse(1,1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method hasTermsOfUseBan() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_hasTermsOfUseBan_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new UserTermsOfUseDaoImpl(configurationObject);
        try {
            instance.hasTermsOfUseBan(1,1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
}
