/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.failure;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.impl.ProjectTermsOfUseDaoImpl;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Failure test for {@link ProjectTermsOfUseDaoImpl}.
 * </p>
 * <p>
 * Changes in 1.1: modify the test cases due to the methods' signatures change.
 * </p>
 * @author mumujava, gjw99
 * @version 1.1
 * @since 1.0
 */
public class ProjectTermsOfUseDaoImplFailureTest extends BaseFailureTest {
    
    /**
     * <p>
     * Represents the ProjectTermsOfUseDaoImpl instance to test against.
     * </p>
     */
    private ProjectTermsOfUseDaoImpl instance;
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(ProjectTermsOfUseDaoImplFailureTest.class);
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
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "termsOfUseDao");
		instance = new ProjectTermsOfUseDaoImpl(configurationObject );
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
     * Failure test for method ProjectTermsOfUseDaoImpl() with null input.
     * Expects IllegalArgumentException.
     */
    public void test_ProjectTermsOfUseDaoImpl_Null() throws Exception {
        try {
            new ProjectTermsOfUseDaoImpl(null);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method ProjectTermsOfUseDaoImpl() with empty config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_ProjectTermsOfUseDaoImpl_2() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "emptyConfig");
        try {
            new ProjectTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method ProjectTermsOfUseDaoImpl() with invalid config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_ProjectTermsOfUseDaoImpl_3() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "invalidConfig");
        try {
            new ProjectTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method ProjectTermsOfUseDaoImpl() with null logger name.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_ProjectTermsOfUseDaoImpl_4() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "loggerNotFoundConfig");
        try {
            new ProjectTermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method createProjectRoleTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createProjectRoleTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new ProjectTermsOfUseDaoImpl(configurationObject);
        try {
            instance.createProjectRoleTermsOfUse(1, 1, 1, 1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createProjectRoleTermsOfUse() with termsOfUsenot found.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createProjectRoleTermsOfUse_2() throws Exception {
        try {
            instance.createProjectRoleTermsOfUse(1, 1, 10000, 1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createProjectRoleTermsOfUse() for duplicate records.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_createProjectRoleTermsOfUse_3() throws Exception {
        instance.createProjectRoleTermsOfUse(1, 1, 1, 1, 1);
        try {
            instance.createProjectRoleTermsOfUse(1, 1, 1, 1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method removeProjectRoleTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_removeProjectRoleTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new ProjectTermsOfUseDaoImpl(configurationObject);
        try {
            instance.removeProjectRoleTermsOfUse(1, 1, 1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method removeProjectRoleTermsOfUse() with termsOfUsenot found.
     * Expects EntityNotFoundException.
     */
    public void test_removeProjectRoleTermsOfUse_2() throws Exception {
        try {
            instance.removeProjectRoleTermsOfUse(1, 1, 10000, 1);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method removeAllProjectRoleTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_removeAllProjectRoleTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new ProjectTermsOfUseDaoImpl(configurationObject);
        try {
            instance.removeAllProjectRoleTermsOfUse(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method getTermsOfUse() with wrong database password.
     * Expects EntityNotFoundException.
     * Changes in 1.1: update the passed in parameters of the method
     * @throws Exception if any error
     */
    public void test_getTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new ProjectTermsOfUseDaoImpl(configurationObject);
        try {
            instance.getTermsOfUse(1, 1, new int[]{1});
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method getTermsOfUse() with empty agreeabilityTypeIds.
     * Expects IllegalArgumentException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_getTermsOfUse_2() throws Exception {
        try {
            instance.getTermsOfUse(1, 1, new int[]{});
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
}
