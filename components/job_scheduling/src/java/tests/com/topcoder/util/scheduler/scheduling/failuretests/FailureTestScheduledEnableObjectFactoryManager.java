/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.failuretests;

import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactory;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactoryManager;

import junit.framework.TestCase;

/**
 * This class contains unit tests for ScheduledEnableObjectFactoryManager class.
 *
 * @author King_Bette
 * @version 3.1
 */
public class FailureTestScheduledEnableObjectFactoryManager extends TestCase {

    /**
     * Tests <code>getScheduledEnableObjectFactory(String name)</code> method
     * for failure with null name. IllegalArgumentException should be thrown.
     */
    public void testGetScheduledEnableObjectFactoryNullName() {
        try {
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory(null);
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>getScheduledEnableObjectFactory(String name)</code> method
     * for failure with empty name. IllegalArgumentException should be thrown.
     */
    public void testGetScheduledEnableObjectFactoryEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager.getScheduledEnableObjectFactory("   ");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>addScheduledEnableObjectFactory(String name, ScheduledEnableObjectFactory factory)</code>
     * method for failure with null name. IllegalArgumentException should be
     * thrown.
     */
    public void testSetScheduledEnableObjectFactoryNullName() {
        try {
            ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory(null,
                new ScheduledEnableObjectFactory() {
                    public ScheduledEnable createScheduledEnableObject() {
                        return null;
                    }
                });
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>addScheduledEnableObjectFactory(String name, ScheduledEnableObjectFactory factory)</code>
     * method for failure with empty name. IllegalArgumentException should be
     * thrown.
     */
    public void testSetScheduledEnableObjectFactoryEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("   ",
                new ScheduledEnableObjectFactory() {
                    public ScheduledEnable createScheduledEnableObject() {
                        return null;
                    }
                });
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests
     * <code>addScheduledEnableObjectFactory(String name, ScheduledEnableObjectFactory factory)</code>
     * method for failure with null value. IllegalArgumentException should be
     * thrown.
     */
    public void testSetScheduledEnableObjectFactoryNullValue() {
        try {
            ScheduledEnableObjectFactoryManager.addScheduledEnableObjectFactory("test", null);
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeScheduledEnableObjectFactory(String name)</code>
     * method for failure with null name. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveScheduledEnableObjectFactoryNullName() {
        try {
            ScheduledEnableObjectFactoryManager.removeScheduledEnableObjectFactory(null);
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }

    /**
     * Tests <code>removeScheduledEnableObjectFactory(String name)</code>
     * method for failure with empty name. IllegalArgumentException should be
     * thrown.
     */
    public void testRemoveScheduledEnableObjectFactoryEmptyName() {
        try {
            ScheduledEnableObjectFactoryManager.removeScheduledEnableObjectFactory("   ");
        } catch (IllegalArgumentException iae) {
            // pass
        }
    }
}
