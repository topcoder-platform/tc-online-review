/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.failuretests;

import java.io.File;
import java.util.Date;
import java.util.Iterator;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableManagementException;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceException;
import com.topcoder.util.config.ConfigManager;

/**
 * <p>
 * Failure test for {@link LateDeliverableManagerImpl}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class LateDeliverableManagerImplFailureTest extends TestCase {
    
    /**
     * <p>
     * Represents the LateDeliverableManagerImpl instance to test against.
     * </p>
     */
    private LateDeliverableManagerImpl instance;
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(LateDeliverableManagerImplFailureTest.class);
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
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<?> it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();

            if (!"com.topcoder.util.log".equals(ns)) {
                cm.removeNamespace(ns);
            }
        }
        cm.add("failure/SearchBundleManager.xml");
		String filePath = "failure/LateDeliverableManagerImpl.properties";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
			
        instance = new LateDeliverableManagerImpl(filePath, namespace);
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
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_0() throws Exception {
		String filePath = "   ";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
        try {
			new LateDeliverableManagerImpl(filePath, namespace);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_1() throws Exception {
		String filePath = "failure/LateDeliverableManagerImpl.xml";
		String namespace = "  ";
        try {
			new LateDeliverableManagerImpl(filePath, namespace);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_2() throws Exception {
		String filePath = null;
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
        try {
			new LateDeliverableManagerImpl(filePath, namespace);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid config.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_8() throws Exception {
        try {
        	//the configuration with namespace failure1 could not be found
			new LateDeliverableManagerImpl("failure/LateDeliverableManagerImpl.properties", "failure1");
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_3() throws Exception {
		String filePath = "not exist file";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
        try {
			new LateDeliverableManagerImpl(filePath, namespace);
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_4() throws Exception {
		String filePath = "failure/LateDeliverableManagerImpl2.xml";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
        try {
			new LateDeliverableManagerImpl(filePath, namespace);
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_5() throws Exception {
		String filePath = "failure/LateDeliverableManagerImpl.properties";
        try {
			new LateDeliverableManagerImpl(filePath, "invalid namespace");
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }

    /**
     * Failure test for method ctor() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_LateDeliverableManagerImpl_6() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl");
        obj.setPropertyValue("nonRestrictedSearchBundleName", "notexist");
        try {
			new LateDeliverableManagerImpl(obj);
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method update() with invalid config.
     * Expects LateDeliverableManagementConfigurationException.
     */
    public void test_update_2() throws Exception {
		String filePath = "failure/LateDeliverableManagerImpl.properties";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
			
        instance = new LateDeliverableManagerImpl(filePath, namespace);

    	LateDeliverable lateDeliverable = new LateDeliverable();
    	lateDeliverable.setId(1);
    	lateDeliverable.setCreateDate(new Date());
    	lateDeliverable.setDeadline(new Date());
    	lateDeliverable.setDelay(2L);
    	lateDeliverable.setDeliverableId(1);
    	lateDeliverable.setExplanation("invalid");
    	lateDeliverable.setLastNotified(new Date());
    	lateDeliverable.setProjectPhaseId(1);
    	lateDeliverable.setResourceId(1);
    	lateDeliverable.setResponse("reply");
    	LateDeliverableType type = new LateDeliverableType();
    	type.setId(1);
        lateDeliverable.setType(type );
        try {
			instance.update(lateDeliverable );
            fail("Expects LateDeliverablePersistenceException");
        } catch (LateDeliverablePersistenceException e) {
            // good
        }
    }

    /**
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_4() throws Exception {
    	LateDeliverable lateDeliverable = null;
        try {
			instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_5() throws Exception {
    	LateDeliverable lateDeliverable = new LateDeliverable();
    	lateDeliverable.setId(1);
    	lateDeliverable.setCreateDate(null);
    	lateDeliverable.setDeadline(new Date());
    	lateDeliverable.setDelay(2L);
    	lateDeliverable.setDeliverableId(1);
    	lateDeliverable.setExplanation("invalid");
    	lateDeliverable.setLastNotified(new Date());
    	lateDeliverable.setProjectPhaseId(1);
    	lateDeliverable.setResourceId(1);
    	lateDeliverable.setResponse("reply");
        LateDeliverableType type = new LateDeliverableType();
        type.setId(1);
        lateDeliverable.setType(type );
        try {
			instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }

    /**
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_6() throws Exception {
    	LateDeliverable lateDeliverable = new LateDeliverable();
    	lateDeliverable.setId(1);
    	lateDeliverable.setCreateDate(new Date());
    	lateDeliverable.setDeadline(null);
    	lateDeliverable.setDelay(2L);
    	lateDeliverable.setDeliverableId(1);
    	lateDeliverable.setExplanation("invalid");
    	lateDeliverable.setLastNotified(new Date());
    	lateDeliverable.setProjectPhaseId(1);
    	lateDeliverable.setResourceId(1);
    	lateDeliverable.setResponse("reply");
        LateDeliverableType type = new LateDeliverableType();
        type.setId(1);
        lateDeliverable.setType(type );
        try {
			instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_7() throws Exception {
        LateDeliverable lateDeliverable = new LateDeliverable();
        lateDeliverable.setId(1);
        lateDeliverable.setCreateDate(new Date());
        lateDeliverable.setDeadline(new Date());
        lateDeliverable.setDelay(2L);
        lateDeliverable.setDeliverableId(1);
        lateDeliverable.setExplanation("invalid");
        lateDeliverable.setLastNotified(new Date());
        lateDeliverable.setProjectPhaseId(1);
        lateDeliverable.setResourceId(1);
        lateDeliverable.setResponse("reply");
        LateDeliverableType type = new LateDeliverableType();
        type.setId(1);
        lateDeliverable.setType(null );
        try {
            instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_8() throws Exception {
        LateDeliverable lateDeliverable = new LateDeliverable();
        lateDeliverable.setId(1);
        lateDeliverable.setCreateDate(new Date());
        lateDeliverable.setDeadline(new Date());
        lateDeliverable.setDelay(2L);
        lateDeliverable.setDeliverableId(1);
        lateDeliverable.setExplanation("invalid");
        lateDeliverable.setLastNotified(new Date());
        lateDeliverable.setProjectPhaseId(1);
        lateDeliverable.setResourceId(1);
        lateDeliverable.setResponse("reply");
        LateDeliverableType type = new LateDeliverableType();
        type.setId(0);
        lateDeliverable.setType(type );
        try {
            instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method retrieve() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_retrieve_1() throws Exception {
        try {
			instance.retrieve(-1 );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method searchRestrictedLateDeliverables() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_searchRestrictedLateDeliverables_1() throws Exception {
        try {
			instance.searchRestrictedLateDeliverables(null, -1 );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    

    /**
     * Failure test for method retrieve() with invalid input.
     * Expects LateDeliverableManagementException.
     */
    public void test_retrieve_2() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<?> it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();

            if (!"com.topcoder.util.log".equals(ns)) {
                cm.removeNamespace(ns);
            }
        }
        cm.add("failure/SearchBundleManager1.xml");
		String filePath = "failure/LateDeliverableManagerImpl.properties";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
			
        instance = new LateDeliverableManagerImpl(filePath, namespace);
        try {
			instance.retrieve(1);
            fail("Expects LateDeliverableManagementException");
        } catch (LateDeliverableManagementException e) {
            // good
        }
    }
    /**
     * Failure test for method searchRestrictedLateDeliverables() with invalid config for search bundle.
     * Expects LateDeliverableManagementException.
     */
    public void test_searchRestrictedLateDeliverables_2() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        for (Iterator<?> it = cm.getAllNamespaces(); it.hasNext();) {
            String ns = it.next().toString();

            if (!"com.topcoder.util.log".equals(ns)) {
                cm.removeNamespace(ns);
            }
        }
        cm.add("failure/SearchBundleManager1.xml");
		String filePath = "failure/LateDeliverableManagerImpl.properties";
		String namespace = LateDeliverableManagerImpl.DEFAULT_CONFIG_NAMESPACE;
			
        instance = new LateDeliverableManagerImpl(filePath, namespace);
        try {
			instance.searchRestrictedLateDeliverables(null, 1);
            fail("Expects LateDeliverableManagementException");
        } catch (LateDeliverableManagementException e) {
            // good
        }
    }
    /**
     * Failure test for method getLateDeliverableTypes() with invalid database configuration.
     * Expects LateDeliverablePersistenceException.
     */
    public void test_getLateDeliverableTypes_8() throws Exception {
        try {
            instance.getLateDeliverableTypes();
            fail("Expects LateDeliverablePersistenceException");
        } catch (LateDeliverablePersistenceException e) {
            // good
        }
    }
}
