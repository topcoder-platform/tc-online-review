/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.late.failuretests;

import java.io.File;
import java.util.Date;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.XMLFilePersistence;
import com.topcoder.management.deliverable.late.LateDeliverable;
import com.topcoder.management.deliverable.late.LateDeliverableManagementConfigurationException;
import com.topcoder.management.deliverable.late.LateDeliverableType;
import com.topcoder.management.deliverable.late.impl.LateDeliverablePersistenceException;
import com.topcoder.management.deliverable.late.impl.persistence.DatabaseLateDeliverablePersistence;

/**
 * <p>
 * Failure test for {@link DatabaseLateDeliverablePersistence}.
 * </p>
 *
 * @author mumujava
 * @version 1.0
 */
public class DatabaseLateDeliverablePersistenceFailureTest extends TestCase {
    
    /**
     * <p>
     * Represents the DatabaseLateDeliverablePersistence instance to test against.
     * </p>
     */
    private DatabaseLateDeliverablePersistence instance;
    /**
     * <p>
     * Creates a test suite for the tests in this test case.
     * </p>
     *
     * @return a TestSuite for this test case.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite(DatabaseLateDeliverablePersistenceFailureTest.class);
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
        instance = new DatabaseLateDeliverablePersistence();
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
     * Failure test for method configure() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_configure_1() throws Exception {
        ConfigurationObject obj = null;
        try {
			instance.configure(obj);
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method configure() with invalid input.
     * Expects LateDeliverableManagementConfigurationException.
     */
    public void test_configure_3() throws Exception {
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl2.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
        obj.setPropertyValue("connectionName", "notexist");
        try {
			instance.configure(obj);
            fail("Expects LateDeliverableManagementConfigurationException");
        } catch (LateDeliverableManagementConfigurationException e) {
            // good
        }
    }
    /**
     * Failure test for method configure() with invalid input.
     * Expects LateDeliverableManagementConfigurationException.
     */
    public void test_configure_5() throws Exception {
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl4.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
        try {
			instance.configure(obj);
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
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
		instance.configure(obj);

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
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
		instance.configure(obj);

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
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
		instance.configure(obj);

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
    	XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
		instance.configure(obj);

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
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
        instance.configure(obj);

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
     * Failure test for method update() with invalid input.
     * Expects IllegalArgumentException.
     */
    public void test_update_8() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
        instance.configure(obj);

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
        lateDeliverable.setType(null);
        try {
            instance.update(lateDeliverable );
            fail("Expects IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // good
        }
    }
    /**
     * Failure test for method getLateDeliverableTypes() with invalid database configuration.
     * Expects LateDeliverablePersistenceException.
     */
    public void test_getLateDeliverableTypes_8() throws Exception {
        XMLFilePersistence persistence = new XMLFilePersistence();

        // Get configuration
        ConfigurationObject obj = persistence.loadFile(
            "com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl", new File("test_files/failure/LateDeliverableManagerImpl.xml"));

        obj = obj.getChild("com.topcoder.management.deliverable.late.impl.LateDeliverableManagerImpl").getChild("persistenceConfig");
        instance.configure(obj);
        try {
            instance.getLateDeliverableTypes();
            fail("Expects LateDeliverablePersistenceException");
        } catch (LateDeliverablePersistenceException e) {
            // good
        }
    }
}
