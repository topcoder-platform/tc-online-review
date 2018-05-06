/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactoryManager;

/**
 * <p>
 * Accuracy test for ScheduledEnableObjectFactoryManager.
 * </p>
 *
 *
 * @author peony
 * @version 3.1
 */
public class ScheduledEnableObjectFactoryManagerTest extends TestCase {
    /**
     * <p>
     * The ScheduledEnableObjectFactory instance for testing.
     * </p>
     */
    private AccuracyScheduledEnableObjectFactory factory = new AccuracyScheduledEnableObjectFactory();

    /**
     * <p>
     * Sets up test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        // empty
    }

    /**
     * <p>
     * Tears down test environment.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        ScheduledEnableObjectFactoryManager.clearScheduledEnableObjectFactories();
    }

    /**
     * <p>
     * Accuracy Test for <code>addScheduledEnableObjectFactory(String name,
     * ScheduledEnableObjectFactory factory)</code>.
     * </p>
     */
    public void testAddScheduledEnableObjectFactory() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name", factory);
        assertEquals("Failed to add ScheduledEnableObjectFactory correctly.", factory,
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory("name"));
        AccuracyScheduledEnableObjectFactory factory2 = new AccuracyScheduledEnableObjectFactory();
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name", factory2);
        assertEquals("Failed to add ScheduledEnableObjectFactory correctly.", factory2,
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory("name"));
    }

    /**
     * <p>
     * Accuracy Test for <code>clearScheduledEnableObjectFactories()</code>.
     * </p>
     */
    public void testClearScheduledEnableObjectFactories() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("test", factory);
        ScheduledEnableObjectFactoryManager.clearScheduledEnableObjectFactories();
        assertEquals("Failed to clear ScheduledEnableObjectFactories correctly.", 0,
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactoryNames().length);
    }

    /**
     * <p>
     * Accuracy Test for <code>getScheduledEnableObjectFactory(String name)</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactory() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name", factory);
        assertEquals("Failed to get ScheduledEnableObjectFactory correctly.", factory,
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory("name"));
    }

    /**
     * <p>
     * Accuracy Test for <code>getScheduledEnableObjectFactoryNames()</code>.
     * </p>
     */
    public void testGetScheduledEnableObjectFactoryNames() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name1", factory);
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name2", factory);
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name3", factory);
        String[] names = ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactoryNames();
        List rs = Arrays.asList(names);
        assertEquals("Failed to get ScheduledEnableObjectFactory names correctly.", 3, names.length);

        assertTrue("Failed to get ScheduledEnableObjectFactory names correctly.", rs.contains("name1"));
        assertTrue("Failed to get ScheduledEnableObjectFactory names correctly.", rs.contains("name2"));
        assertTrue("Failed to get ScheduledEnableObjectFactory names correctly.", rs.contains("name3"));
    }

    /**
     * <p>
     * Accuracy Test for <code>removeScheduledEnableObjectFactory(String name)</code>.
     * </p>
     */
    public void testRemoveScheduledEnableObjectFactory() {
        ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("name", factory);
        ScheduledEnableObjectFactoryManager.removeScheduledEnableObjectFactory("name");
        assertNull("Failed to remove ScheduledEnableObjectFactory correctly.",
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory("name"));
    }
}
