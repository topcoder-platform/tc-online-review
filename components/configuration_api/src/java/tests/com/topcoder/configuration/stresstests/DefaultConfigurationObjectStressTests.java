/*
 * Copyright (C) 2007, 2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.configuration.stresstests;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.defaults.DefaultConfigurationObject;

import junit.framework.TestCase;

/**
 * <p>
 * Stress tests of {@link DefaultConfigurationObject} class.
 * </p>
 *
 * @author TCSDEVELOPER, victorsam
 * @version 1.1
 */
public class DefaultConfigurationObjectStressTests extends TestCase {
    /**
     * This instance is used in the test.
     */
    private DefaultConfigurationObject root = new DefaultConfigurationObject("root");

    /**
     * Set up the fixture.
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {

    }

    /**
     * Run getAllDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getAllDescendants();
            assertEquals("result is incorrect, root should have 1000 descendants.", 1000, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getAllDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getAllDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetAllDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getAllDescendants();
            assertEquals("result is incorrect, root should have 1000 descendants.", 1000, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getAllDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run findDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFindDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.findDescendants("child0/child1/child2");
            assertEquals("result is incorrect, 1 descendants should be found.", 1, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run findDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run findDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testFindDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.findDescendants("child*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run findDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDescendantsCase1() throws Exception {
        DefaultConfigurationObject previous = root;
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            previous.addChild(child);
            previous = child;
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getDescendants("child.*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testGetDescendantsCase2() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.getDescendants("child.*");
            assertEquals("result is incorrect, 1000 descendants should be found.", 1000, configurationObjects.length);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run deleteDescendants 1000 times.
     *
     * @throws Exception
     *             to JUnit.
     */
    public void testDeleteDescendantsCase() throws Exception {
        for (int i = 0; i < 1000; i++) {
            DefaultConfigurationObject child = new DefaultConfigurationObject("child" + i);
            root.addChild(child);
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            ConfigurationObject[] configurationObjects = root.deleteDescendants("child" + i);
            assertEquals("result is incorrect, 1 descendants should be deleted.", 1, configurationObjects.length);
        }
        assertEquals("result is incorrect, there should no descendants exist.", 0, root.getAllDescendants().length);
        long endTime = System.currentTimeMillis();
        System.out.println("Run deleteDescendants 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getIntegerProperty 1000 times.
     *
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testGetIntegerPropertyCase() throws Exception {
        for (int i = 0; i < 1000; i++) {
            root.setPropertyValue("child" + i, new Integer(i));
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int value = root.getIntegerProperty("child" + i, true).intValue();
            assertEquals("Failed to get integer property.", i, value);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getIntegerProperty 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getDateProperty 1000 times.
     *
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testGetDatePropertyCase() throws Exception {
        for (int i = 0; i < 1000; i++) {
            root.setPropertyValue("child" + i, "2011-05-11");
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Date date = root.getDateProperty("child" + i, "yyyy-MM-dd", true);
            assertEquals("Failed to get date property.", "2011-05-11", new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getDateProperty 1000 times costs " + (endTime - startTime) + "ms");
    }

    /**
     * Run getClassProperty 1000 times.
     *
     * @throws Exception to JUnit.
     * @since 1.1
     */
    public void testGetClassProperty() throws Exception {
        for (int i = 0; i < 1000; i++) {
            root.setPropertyValue("child" + i, "java.lang.String");
        }
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            Class<?> clazz = root.getClassProperty("child" + i, true);
            assertEquals("Failed to get class property.", String.class, clazz);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Run getClassProperty 1000 times costs " + (endTime - startTime) + "ms");
    }
}
