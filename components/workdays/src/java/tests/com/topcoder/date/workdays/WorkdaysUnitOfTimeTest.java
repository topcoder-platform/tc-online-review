/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import junit.framework.TestCase;

/**
 * This class is used to check the behavior of the <code>WorkdaysUnitOfTime</code> class, including tests of MINUTES,
 * HOURS and DAYS.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class WorkdaysUnitOfTimeTest extends TestCase {
    /**
     * Test name and value of MINUTES instance.
     */
    public void testMinutes() {
        WorkdaysUnitOfTime minutes = WorkdaysUnitOfTime.MINUTES;
        assertEquals("Test of MINUTES", "MINUTES", minutes.toString());
        assertEquals("Test of MINUTES", 0, minutes.getValue());
    }

    /**
     * Test name and value of HOURS instance.
     */
    public void testHours() {
        WorkdaysUnitOfTime hours = WorkdaysUnitOfTime.HOURS;
        assertEquals("Test of HOURS", "HOURS", hours.toString());
        assertEquals("Test of HOURS", 1, hours.getValue());
    }

    /**
     * Test name and value of DAYS instance.
     */
    public void testDays() {
        WorkdaysUnitOfTime days = WorkdaysUnitOfTime.DAYS;
        assertEquals("Test of DAYS", "DAYS", days.toString());
        assertEquals("Test of DAY", 2, days.getValue());
    }

    /**
     * Test the construct when the file name is empty.
     *
     * @throws Exception
     *            to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void testConstruct1() throws Exception {
        try {
            Class clazz = Class.forName(WorkdaysUnitOfTime.class.getName());
            Constructor con = clazz.getDeclaredConstructor(java.lang.String.class, int.class);
            con.setAccessible(true);
            con.newInstance("\n", 3);
            fail("should throw NullPointerException");
        } catch (InvocationTargetException e) {
            assertEquals(IllegalArgumentException.class.getName(), e.getCause().getClass().getName());
        }
    }

    /**
     * Test the construct when the file name is <code>null</code>.
     *
     * @throws Exception
     *            to JUnit.
     */
    @SuppressWarnings("unchecked")
    public void testConstruct2() throws Exception {
        try {
            Class clazz = Class.forName(WorkdaysUnitOfTime.class.getName());
            Constructor con = clazz.getDeclaredConstructor(java.lang.String.class, int.class);
            con.setAccessible(true);
            con.newInstance(null, 3);
            fail("should throw NullPointerException");
        } catch (InvocationTargetException e) {
            assertEquals(NullPointerException.class.getName(), e.getCause().getClass().getName());
        }
    }
}
