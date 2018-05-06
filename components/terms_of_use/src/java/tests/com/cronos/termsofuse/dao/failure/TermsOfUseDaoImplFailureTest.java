/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.failure;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.cronos.termsofuse.dao.EntityNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUseCyclicDependencyException;
import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.cronos.termsofuse.dao.TermsOfUseDependencyNotFoundException;
import com.cronos.termsofuse.dao.TermsOfUsePersistenceException;
import com.cronos.termsofuse.dao.impl.TermsOfUseDaoImpl;
import com.cronos.termsofuse.model.TermsOfUse;
import com.cronos.termsofuse.model.TermsOfUseAgreeabilityType;
import com.cronos.termsofuse.model.TermsOfUseType;
import com.topcoder.configuration.ConfigurationObject;

/**
 * <p>
 * Failure test for {@link TermsOfUseDaoImpl}.
 * </p>
 * <p>
 * Changes in 1.1: modify the test cases due to the methods' signatures change, and add test cases to the newly added methods.
 * </p>
 *
 * @author mumujava, gjw99
 * @version 1.1
 * @since 1.0
 */
public class TermsOfUseDaoImplFailureTest extends BaseFailureTest {
    
    /**
     * <p>
     * Represents the TermsOfUseDaoImpl instance to test against.
     * </p>
     */
    private TermsOfUseDaoImpl instance;
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(TermsOfUseDaoImplFailureTest.class);
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
        clearDB();
		initDB();
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "termsOfUseDao");
		instance = new TermsOfUseDaoImpl(configurationObject );
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
     * Failure test for method TermsOfUseDaoImpl() with null input.
     * Expects IllegalArgumentException.
     */
    public void test_TermsOfUseDaoImpl_Null() throws Exception {
        try {
            new TermsOfUseDaoImpl(null);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method TermsOfUseDaoImpl() with empty config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_TermsOfUseDaoImpl_2() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "emptyConfig");
        try {
            new TermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method TermsOfUseDaoImpl() with invalid config.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_TermsOfUseDaoImpl_3() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "invalidConfig");
        try {
            new TermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method TermsOfUseDaoImpl() with null logger name.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_TermsOfUseDaoImpl_4() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "loggerNotFoundConfig");
        try {
            new TermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method TermsOfUseDaoImpl() with null generator name.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_TermsOfUseDaoImpl_5() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "projectTermsOfUseDao");
        try {
            new TermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method TermsOfUseDaoImpl() with wrong generator name.
     * Expects TermsOfUseDaoConfigurationException.
     */
    public void test_TermsOfUseDaoImpl_6() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "wronggenerator");
        try {
            new TermsOfUseDaoImpl(configurationObject);
            fail("Expects TermsOfUseDaoConfigurationException");
        } catch (TermsOfUseDaoConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_createTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(1);
        termOfUse.setTitle("title");
        termOfUse.setUrl("url");
        try {
			instance.createTermsOfUse(termOfUse, "text");
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with null termofuse.
     * Expects IllegalArgumentException.
     */
    public void test_createTermsOfUse_2() throws Exception {
        try {
			instance.createTermsOfUse(null, "text");
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with null title.
     * Expects TermsOfUsePersistenceException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_createTermsOfUse_3() throws Exception {
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(1);
        termOfUse.setTitle(null);
        termOfUse.setUrl("url");
        try {
			instance.createTermsOfUse(termOfUse, "text");
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with TermsOfUseTypeId not found.
     * Expects TermsOfUsePersistenceException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_createTermsOfUse_4() throws Exception {
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(10000);
        termOfUse.setTitle("title");
        termOfUse.setUrl("url");
        try {
			instance.createTermsOfUse(termOfUse, "text");
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with termOfUse null.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createTermsOfUse_5() throws Exception {
        try {
			instance.createTermsOfUse(null, "text");
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method createTermsOfUse() with termsOfUse.getAgreeabilityType() null.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createTermsOfUse_6() throws Exception {
        try {
        	TermsOfUse term = new TermsOfUse();
			instance.createTermsOfUse(term, "text");
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    
    /**
     * Failure test for method updateTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_updateTermsOfUse_1() throws Exception {
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(1);
        termOfUse.setTitle("title");
        termOfUse.setUrl("url");
		instance.createTermsOfUse(termOfUse, "text");
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.updateTermsOfUse(termOfUse);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUse() with null termofuse.
     * Expects IllegalArgumentException.
     */
    public void test_updateTermsOfUse_2() throws Exception {
        try {
			instance.updateTermsOfUse(null);
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUse() with termOfUse not found in persistence.
     * Expects EntityNotFoundException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_updateTermsOfUse_3() throws Exception {
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(1);
        termOfUse.setTitle("title");
        termOfUse.setUrl("url");
        try {
			instance.updateTermsOfUse(termOfUse);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUse() with TermsOfUseTypeId not found.
     * Expects TermsOfUsePersistenceException.
     * Changes in 1.1: modify the entity to be saved due to its change.
     * @throws Exception if any error
     */
    public void test_updateTermsOfUse_5() throws Exception {
        TermsOfUse termOfUse = new TermsOfUse();
        TermsOfUseAgreeabilityType type = new TermsOfUseAgreeabilityType();
        type.setTermsOfUseAgreeabilityTypeId(1);
        type.setDescription("desc1");
        type.setName("name1");
        termOfUse.setAgreeabilityType(type);
        termOfUse.setTermsOfUseTypeId(1);
        termOfUse.setTitle("title");
        termOfUse.setUrl("url");
		instance.createTermsOfUse(termOfUse, "text");
        termOfUse.setTermsOfUseTypeId(10000);
        try {
			instance.updateTermsOfUse(termOfUse);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    
    /**
     * Failure test for method getTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getTermsOfUse(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method deleteTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_deleteTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.deleteTermsOfUse(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method deleteTermsOfUse().
     * Expects EntityNotFoundException if termsOfUse does not exist.
     */
    public void test_deleteTermsOfUse_2() throws Exception {
        try {
			instance.deleteTermsOfUse(10000);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method getTermsOfUseByTypeId() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getTermsOfUseByTypeId_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getTermsOfUseByTypeId(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method getAllTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getAllTermsOfUse_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getAllTermsOfUse();
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method getTermsOfUseType() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getTermsOfUseType_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getTermsOfUseType(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUseType() with null type.
     * Expects IllegalArgumentException.
     */
    public void test_updateTermsOfUseType_1() throws Exception {
        try {
			instance.updateTermsOfUseType(null);
            fail("Expects an error");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUseType() with type not exist.
     * Expects EntityNotFoundException.
     */
    public void test_updateTermsOfUseType_2() throws Exception {
		TermsOfUseType type = new TermsOfUseType();
		type.setTermsOfUseTypeId(1000);
		type.setDescription("new");
        try {
			instance.updateTermsOfUseType(type);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }
    /**
     * Failure test for method updateTermsOfUseType() with large description.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_updateTermsOfUseType_3() throws Exception {
		TermsOfUseType type = new TermsOfUseType();
		type.setTermsOfUseTypeId(1);
		type.setDescription(LARGE_TEXT);
        try {
			instance.updateTermsOfUseType(type);
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method updateTermsOfUseType() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_updateTermsOfUseType_4() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
		TermsOfUseType type = new TermsOfUseType();
		type.setTermsOfUseTypeId(1);
		type.setDescription("des");
        try {
			instance.updateTermsOfUseType(type);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method getTermsOfUseText() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_getTermsOfUseText_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getTermsOfUseText(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method getTermsOfUseText() with text not found.
     * Expects EntityNotFoundException.
     */
    public void test_getTermsOfUseText_2() throws Exception {
        try {
			instance.getTermsOfUseText(10000);
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }

    /**
     * Failure test for method setTermsOfUseText() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     */
    public void test_setTermsOfUseText_1() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.setTermsOfUseText(1, "text");
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method setTermsOfUseText() with text not found.
     * Expects EntityNotFoundException.
     */
    public void test_setTermsOfUseText_2() throws Exception {
        try {
			instance.setTermsOfUseText(10000, "text");
            fail("Expects an error");
        } catch (EntityNotFoundException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship() with equal ids.
     * Expects TermsOfUseCyclicDependencyException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_1() throws Exception {
        try {
			instance.createDependencyRelationship(1, 1);
            fail("Expects an error");
        } catch (TermsOfUseCyclicDependencyException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_2() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.createDependencyRelationship(1, 2);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship() which will cause a dependency loop.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_3() throws Exception {
        try {
			instance.createDependencyRelationship(1, 2);
            fail("Expects an error");
        } catch (TermsOfUseCyclicDependencyException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship() which will cause primary key violation.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_4() throws Exception {
        try {
			instance.createDependencyRelationship(2, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship(), the dependentTermsOfUseId doesn't exist.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_5() throws Exception {
        try {
			instance.createDependencyRelationship(10, 2);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship(), the dependencyTermsOfUseId doesn't exist.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_6() throws Exception {
        try {
			instance.createDependencyRelationship(1, 10);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method createDependencyRelationship(), the dependentTermsOfUseId and
     * dependencyTermsOfUseId doesn't exist.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_createDependencyRelationship_7() throws Exception {
        try {
			instance.createDependencyRelationship(1, 10);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method getDependencyTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_getDependencyTermsOfUse() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getDependencyTermsOfUse(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method getDependentTermsOfUse() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_getDependentTermsOfUse() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.getDependentTermsOfUse(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method deleteDependencyRelationship() while the dependency is not found.
     * Expects TermsOfUseDependencyNotFoundException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_deleteDependencyRelationship_1() throws Exception {
        try {
			instance.deleteDependencyRelationship(1, 10);
            fail("Expects an error");
        } catch (TermsOfUseDependencyNotFoundException e) {
            // good
        }
    }

    /**
     * Failure test for method deleteDependencyRelationship() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_deleteDependencyRelationship_2() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.deleteDependencyRelationship(1, 1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method deleteAllDependencyRelationshipsForDependent() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_deleteAllDependencyRelationshipsForDependent() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.deleteAllDependencyRelationshipsForDependent(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method deleteAllDependencyRelationshipsForDependency() with wrong database password.
     * Expects TermsOfUsePersistenceException.
     * @throws Exception if any error
     * @since 1.1
     */
    public void test_deleteAllDependencyRelationshipsForDependency() throws Exception {
        ConfigurationObject configurationObject = getConfigurationObject("test_files/failure/config.xml", "failureConfig");
        instance = new TermsOfUseDaoImpl(configurationObject);
        try {
			instance.deleteAllDependencyRelationshipsForDependency(1);
            fail("Expects an error");
        } catch (TermsOfUsePersistenceException e) {
            // good
        }
    }
}
