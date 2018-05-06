/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */

package com.topcoder.util.scheduler.scheduling;

import java.util.Map;

import junit.framework.TestCase;

/**
 * Unit test cases for class <code>ScheduledEnableObjectFactoryManager</code>.
 * @author fuyun
 * @version 3.1
 */
public class ScheduledEnableObjectFactoryManagerTests extends TestCase {

    /**
     * Represents one <code>ScheduledEnableObjectFactory</code> instance for
     * testing.
     */
    private ScheduledEnableObjectFactory factory1 = null;

    /**
     * Represents another <code>ScheduledEnableObjectFactory</code> instance
     * for testing.
     */
    private ScheduledEnableObjectFactory factory2 = null;

    /**
     * Represents the reference to the private factories map of
     * <code>ScheduledEnableObjectFactoryManager</code>.
     */
    private Map factories = null;

    /**
     * <p>
     * Sets up the test environment.
     * </p>
     * <p>
     * It initializes the manager's internal factory map with two
     * <code>SimpleScheduledEnableObjectFactory</code> instances.
     * </p>
     * @throws Exception if there is any problem.
     */
    protected void setUp() throws Exception {
        factory1 = new CustomScheduledEnableObjectFactory();
        factory2 = new CustomScheduledEnableObjectFactory();

        // get the factories member.
        factories = (Map) TestHelper.getPrivateFieldValue(
                ScheduledEnableObjectFactoryManager.class,
                ScheduledEnableObjectFactoryManager.class, "FACTORIES");

        factories.clear();
        // put the two factories to the map.
        factories.put("key1", factory1);
        factories.put("key2", factory2);
    }

    /**
     * <p>
     * Cleans up the test environment.
     * </p>
     * <p>
     * Clears the internal factory map in the manager.
     * </p>
     */
    protected void tearDown() {
        factories.clear();
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>getScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the expected factory could be returned properly. And if the
     * given name is not set before, it will return <code>null</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryAccuracy() {
        assertSame("Fails to get the ScheduledEnableObjectFactory", factory1,
                ScheduledEnableObjectFactoryManager
                        .getScheduledEnableObjectFactory("key1"));
        assertSame("Fails to get the ScheduledEnableObjectFactory", factory2,
                ScheduledEnableObjectFactoryManager
                        .getScheduledEnableObjectFactory("key2"));
        assertNull("Fails to get the ScheduledEnableObjectFactory",
                ScheduledEnableObjectFactoryManager
                        .getScheduledEnableObjectFactory("key"));
    }

    /**
     * <p>
     * Failure test for method
     * <code>getScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is <code>null</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryFailureNullName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .getScheduledEnableObjectFactory(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method
     * <code>getScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is empty string.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryFailureEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .getScheduledEnableObjectFactory(" ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     * <p>
     * It tests that the factory could be added into the manager successfully.
     * </p>
     */
    public void testAddScheduledEnableObjectFactoryAccuracy() {
        ScheduledEnableObjectFactory factory = new CustomScheduledEnableObjectFactory();
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(
                "key", factory);
        assertEquals("Fails to add ScheduledEnableObjectFactory.", 3, factories
                .size());
        assertSame("Fails to add ScheduledEnableObjectFactory.", factory,
                factories.get("key"));
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     * <p>
     * It tests that the new factory will replace the old one if the name
     * exists.
     * </p>
     */
    public void testAddScheduledEnableObjectFactoryAccuracyDuplicateName() {
        ScheduledEnableObjectFactory factory = new CustomScheduledEnableObjectFactory();
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(
                "key1", factory);
        assertEquals("Fails to add ScheduledEnableObjectFactory.", 2, factories
                .size());
        assertSame("Fails to add ScheduledEnableObjectFactory.", factory,
                factories.get("key1"));
    }

    /**
     * <p>
     * Failure test for method
     * <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is <code>null</code>.
     * </p>
     */
    public void testAddScheduledEnableObjectFactoryFailureNullName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .addScheduledEnableObjectFactory(null, factory1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method
     * <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is empty string.
     * </p>
     */
    public void testAddScheduledEnableObjectFactoryFailureEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .addScheduledEnableObjectFactory(" ", factory1);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method
     * <code>addScheduledEnableObjectFactory(String, ScheduledEnableObjectFactory)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>factory</code> is <code>null</code>.
     * </p>
     */
    public void testAddScheduledEnableObjectFactoryFailureNullFactory() {
        try {
            ScheduledEnableObjectFactoryManager
                    .addScheduledEnableObjectFactory("key", null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>removeScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the specified factory could be removed successfully.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactoryAccuracy() {
        ScheduledEnableObjectFactoryManager
                .removeScheduledEnableObjectFactory("key1");
        assertNull("Fails to remove ScheduledEnableObjectFactory.", factories
                .get("key1"));
        ScheduledEnableObjectFactoryManager
                .removeScheduledEnableObjectFactory("key2");
        assertNull("Fails to remove ScheduledEnableObjectFactory.", factories
                .get("key2"));
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>removeScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that nothing will happen if the name does not exist.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactoryAccuracyNameNotExist() {
        ScheduledEnableObjectFactoryManager
                .removeScheduledEnableObjectFactory("key");
        assertEquals("Fails to remove ScheduledEnableObjectFactory.", 2,
                factories.size());
    }

    /**
     * <p>
     * Failure test for method
     * <code>removeScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is <code>null</code>.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactoryFailureNullName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .removeScheduledEnableObjectFactory(null);
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Failure test for method
     * <code>removeScheduledEnableObjectFactory(String)</code>.
     * </p>
     * <p>
     * It tests that the <code>IllegalArgumentException</code> will be thrown
     * if the argument <code>name</code> is empty string.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactoryFailureEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager
                    .removeScheduledEnableObjectFactory(" ");
            fail("IllegalArgumentException is expected.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>getScheduledEnableObjectFactoryNames</code>.
     * </p>
     * <p>
     * It tests that all the names could be retrieved successfully.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryNamesAccuracy() {
        String[] names = ScheduledEnableObjectFactoryManager
                .getScheduledEnableObjectFactoryNames();
        assertEquals("Fails to get the ScheduledEnableObjectFactory names.", 2,
                names.length);
        for (int i = 0; i < names.length; i++) {
            assertTrue("Fails to get the ScheduledEnableObjectFactory names.",
                    names[i].equals("key1") || names[i].equals("key2"));
        }
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>getScheduledEnableObjectFactoryNames()</code>.
     * </p>
     * <p>
     * It tests that all the names could be retrieved successfully.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryNamesAccuracyEmptyArray() {
        factories.clear();
        String[] names = ScheduledEnableObjectFactoryManager
                .getScheduledEnableObjectFactoryNames();
        assertEquals("Fails to get the ScheduledEnableObjectFactory names.", 0,
                names.length);
    }

    /**
     * <p>
     * Accuracy test for method
     * <code>clearScheduledEnableObjectFactories()</code>.
     * </p>
     * <p>
     * It tests that the factories map is cleared.
     * </p>
     */
    public void testClearScheduledEnableObjectFactoriesAccuracy() {
        ScheduledEnableObjectFactoryManager
                .clearScheduledEnableObjectFactories();
        assertEquals("Fails to clear the ScheduledEnableObjectFactory.", 0,
                factories.size());
    }
}
