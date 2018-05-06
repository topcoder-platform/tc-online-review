/*
 * Copyright (c) 2010, TopCoder, Inc. All rights reserved
 */
package com.topcoder.date.workdays.accuracytests;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.date.workdays.WorkdaysUnitOfTime;
import com.topcoder.util.config.ConfigManager;

/**
 * Accuracy test for class DefaultWorkdays in version 1.1.
 *
 * @author extra
 * @version 1.1
 */
public class DefaultWorkdaysTestV11 extends TestCase {

    /**
     * The default workdays instance for testing.
     */
    private DefaultWorkdays wd = null;

    /**
     * The config file for testing.
     */
    private String fileName = "test_files/accuracytests/workdays2.properties";

    /**
     * <p>
     * Creates a test suite of the tests contained in this class.
     * </p>
     *
     * @return a test suite of the tests contained in this class.
     */
    public static Test suite() {
        return new TestSuite(DefaultWorkdaysTestV11.class);
    }

    /**
     * Sets up test environment.
     *
     * @throws Exception
     *             to JUnit
     *
     */
    protected void setUp() throws Exception {
        super.setUp();
        clear();
        this.wd = new DefaultWorkdays(fileName);
    }

    /**
     * Tears down test environment.
     *
     * @throws Exception
     *             to JUnit
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        clear();
    }

    /**
     * <p>
     * Tests accuracy of the constructor method DefaultWorkdays(String fileName). All the fields should be set
     * correctly.
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testDefaultWorkdaysAccuracy1() throws Exception {
        DefaultWorkdays instance = new DefaultWorkdays("test_files/accuracytests/workdays2.xml");
        assertEquals("test_files/accuracytests/workdays2.xml", instance.getFileName());
        assertEquals(16, instance.getWorkdayEndTimeHours());
        assertEquals(30, instance.getWorkdayEndTimeMinutes());
        assertEquals(8, instance.getWorkdayStartTimeHours());
        assertEquals(0, instance.getWorkdayStartTimeMinutes());
        assertEquals(4, instance.getNonWorkdays().size());
        assertEquals(true, instance.isSaturdayWorkday());
        assertEquals(false, instance.isSundayWorkday());
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for add 24 hours.
     * </p>
     */
    public void testAddAccuracy7() {
        Date startDate = getDate(2005, 0, 1, 8, 30);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, 24 * 60);
        assertEquals(getDate(2005, 0, 4, 15, 30), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for add 212 hours.
     * </p>
     */
    public void testAddAccuracy71() {
        Date startDate = getDate(2005, 0, 1, 8, 30);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, 212 * 60);
        assertEquals(getDate(2005, 1, 2, 16, 30), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for add 212 hours.
     * </p>
     */
    public void testAddAccuracy72() {
        Date startDate = getDate(2005, 0, 1, 8, 00);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, 212 * 60);
        assertEquals(getDate(2005, 1, 2, 16, 0), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for add 212 hours.
     * </p>
     */
    public void testAddAccuracy73() {
        Date startDate = getDate(2005, 0, 1, 7, 00);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, 212 * 60);
        assertEquals(getDate(2005, 1, 2, 16, 0), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for sub 24 hours.
     * </p>
     */
    public void testAddAccuracy8() {
        Date startDate = getDate(2005, 0, 4, 15, 30);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, -24 * 60);
        assertEquals(getDate(2005, 0, 1, 8, 30), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for sub 212 hours.
     * </p>
     */
    public void testAddAccuracy81() {
        Date startDate = getDate(2005, 1, 2, 16, 30);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, -212 * 60);
        assertEquals(getDate(2005, 0, 1, 8, 30), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method add(Date startDate, WorkdaysUnitOfTime unitOfTime, int amount). The result should be
     * correct for sub 212 hours.
     * </p>
     */
    public void testAddAccuracy82() {
        Date startDate = getDate(2005, 1, 2, 16, 00);
        Date endDate = this.wd.add(startDate, WorkdaysUnitOfTime.MINUTES, -212 * 60);
        assertEquals(getDate(2005, 0, 1, 8, 00), endDate);
    }

    /**
     * <p>
     * Tests accuracy of the method setFileName(String fileName) and refresh(). The result should be correct after
     * refresh().
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSetFileNameAccuracy1() throws Exception {
        DefaultWorkdays instance = new DefaultWorkdays("test_files/accuracytests/workdays2.xml");

        instance.setFileName("test_files/accuracytests/workdays4.properties");

        // before refresh, only the filename updated
        assertEquals("fileName updated", "test_files/accuracytests/workdays4.properties", instance.getFileName());
        assertEquals(16, instance.getWorkdayEndTimeHours());
        assertEquals(30, instance.getWorkdayEndTimeMinutes());
        assertEquals(8, instance.getWorkdayStartTimeHours());
        assertEquals(0, instance.getWorkdayStartTimeMinutes());
        assertEquals(4, instance.getNonWorkdays().size());
        assertEquals(true, instance.isSaturdayWorkday());
        assertEquals(false, instance.isSundayWorkday());

        instance.refresh();

        // after refresh, the fields should be updated

        assertEquals(17, instance.getWorkdayEndTimeHours());
        assertEquals(30, instance.getWorkdayEndTimeMinutes());
        assertEquals(9, instance.getWorkdayStartTimeHours());
        assertEquals(0, instance.getWorkdayStartTimeMinutes());
        assertEquals(5, instance.getNonWorkdays().size());
        assertEquals(true, instance.isSaturdayWorkday());
        assertEquals(false, instance.isSundayWorkday());
    }

    /**
     * <p>
     * Tests accuracy of the method save(). The result reload should be correct after save().
     * </p>
     *
     * @throws Exception
     *             to JUnit
     */
    public void testSaveAccuracy1() throws Exception {
        DefaultWorkdays instance = new DefaultWorkdays("test_files/accuracytests/workdays3.properties");

        // set the fields with random value
        int hour = 1 + new Random().nextInt(13);
        Date date = new Date();
        Locale.setDefault(new Locale("en", "US", "MAC"));
        instance.setSaturdayWorkday(true);
        instance.setSundayWorkday(true);
        instance.setWorkdayStartTimeHours(hour);
        instance.setWorkdayStartTimeMinutes(hour + 20);
        instance.setWorkdayEndTimeHours(hour + 6);
        instance.setWorkdayEndTimeMinutes(hour + 30);

        instance.clearNonWorkdays();
        instance.addNonWorkday(date);
        instance.addNonWorkday(date);
        instance.addNonWorkday(new SimpleDateFormat("yyyy-MM-dd").parse("2010-03-14"));
        instance.addNonWorkday(new SimpleDateFormat("yyyy-MM-dd").parse("2010-06-24"));
        instance.addNonWorkday(new SimpleDateFormat("yyyy-MM-dd").parse("2010-06-17"));
        instance.addNonWorkday(new SimpleDateFormat("yyyy-MM-dd").parse("2010-06-23"));

        instance.save();

        // load the config file after save, verify the fields

        instance = new DefaultWorkdays("test_files/accuracytests/workdays3.properties");

        assertEquals(hour, instance.getWorkdayStartTimeHours());
        assertEquals(hour + 20, instance.getWorkdayStartTimeMinutes());
        assertEquals(hour + 6, instance.getWorkdayEndTimeHours());
        assertEquals(hour + 30, instance.getWorkdayEndTimeMinutes());
        assertEquals(5, instance.getNonWorkdays().size());
        assertEquals(true, instance.isSaturdayWorkday());
        assertEquals(true, instance.isSundayWorkday());

    }

    /**
     * <p>
     * an instance of Date with the specified year, month, day, and hour, and min.
     * </p>
     *
     * @param year
     *            an int represents the year
     * @param month
     *            an int represents the month
     * @param day
     *            an int represents the day
     * @param hour
     *            an int represents the hour
     * @param min
     *            an int represents the minute
     * @return an instance of Date with the specified year, month, day, and hour, and min.
     */
    private static Date getDate(int year, int month, int day, int hour, int min) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        return calendar.getTime();
    }

    /**
     * Clear the config manager.
     *
     * @throws Exception
     *             to JUnit
     */
    private static void clear() throws Exception {
        ConfigManager manager = ConfigManager.getInstance();
        for (Iterator iterator = manager.getAllNamespaces(); iterator.hasNext();) {
            String namespace = (String) iterator.next();
            manager.removeNamespace(namespace);
        }
    }
}
