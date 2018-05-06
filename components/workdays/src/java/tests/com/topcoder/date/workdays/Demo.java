/*
 * Copyright (C) 2010 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.date.workdays;

import junit.framework.TestCase;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * This class show all the usage of workdays component, getting a Workdays instance from a WorkdaysFactory, directly
 * constructing a DefaultWorkdays instance, initializing a Workdays instance with properties of XML configuration file,
 * adding non-workdays to the instance, setting the work time, and the most importantly, adding amount of unitOfTimes to
 * a specified start date to get the end date.
 *
 * @author TCSDEVELOPER
 * @version 1.0
 */
public class Demo extends TestCase {
    /**
     * Test all functionality in this method.
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void testDemo1() throws Exception {
        // create an instance of DefaultWorkdays using Configuration API / Configuration Persistence
        String configFile = "test_files/sample.properties";
        DefaultWorkdays workdays = new DefaultWorkdays(configFile);

        // create a Workdays factory using Configuration Manager
        // Note that this constructor is now deprecated
        WorkdaysFactory factory = new DefaultWorkdaysFactory();

        // create a Workdays factory using Configuration API / Configuration Persistence
        factory = new DefaultWorkdaysFactory(true);

        // use the Workdays factory to create instances of Workdays
        Workdays w1 = factory.createWorkdaysInstance();
        Workdays w2 = factory.createWorkdaysInstance();

        // Another way to get a Workdays instance, construct it directly.
        // A default configurated instance.
        workdays = new DefaultWorkdays();

        // Or construct it with file_name and file_format, a properties type file
        workdays = new DefaultWorkdays("test_files/workdays_unittest.properties",
                DefaultWorkdays.PROPERTIES_FILE_FORMAT);

        // Or a XML type file
        workdays = new DefaultWorkdays("test_files/workdays_unittest.xml", DefaultWorkdays.XML_FILE_FORMAT);

        workdays.setSaturdayWorkday(true);
        workdays.setSundayWorkday(true);

        // query if weekend days are normal work-days
        boolean isSaturdayWorkday = workdays.isSaturdayWorkday();
        boolean isSundayWorkday = workdays.isSundayWorkday();

        // change the start and end time hours and minutes
        workdays.setWorkdayStartTimeHours(8);
        workdays.setWorkdayStartTimeMinutes(0);
        workdays.setWorkdayEndTimeHours(16);
        workdays.setWorkdayEndTimeMinutes(30);

        // query the start and end time hours and minutes
        int startTimeHours = workdays.getWorkdayStartTimeHours();
        int startTimeMinutes = workdays.getWorkdayStartTimeMinutes();
        int endTimeHours = workdays.getWorkdayEndTimeHours();
        int endTimeMinutes = workdays.getWorkdayEndTimeMinutes();

        // You also can save the change
        workdays.save();

        // calculate the end date, knowing the start date, the amount of time and the unit of time
        Date startDate = new Date();
        Date endDate = workdays.add(startDate, WorkdaysUnitOfTime.HOURS, 40);

        // calculate the end date by subtracting 40 hours for the specified start date
        endDate = workdays.add(startDate, WorkdaysUnitOfTime.HOURS, -40);

        // refresh the configuration from the file
        workdays.refresh();
    }

    /**
     * Test all functionality in this method.
     *
     * @throws Exception
     *             if any error occurs.
     */
    public void testDemo2() throws Exception {
        DefaultWorkdays workdays = new DefaultWorkdays();
        // change the file format to XML
        // Note that this method is now deprecated
        workdays.setFileFormat(DefaultWorkdays.XML_FILE_FORMAT);
        // Another way to get a Workdays instance, construct it directly.
        // A default configurated instance.
        workdays = new DefaultWorkdays();

        // Or construct it with file_name and file_format, a properties type file
        workdays = new DefaultWorkdays("test_files/workdays_unittest.properties",
                DefaultWorkdays.PROPERTIES_FILE_FORMAT);

        // Or a XML type file
        workdays = new DefaultWorkdays("test_files/workdays_unittest.xml", DefaultWorkdays.XML_FILE_FORMAT);

        // add a non-work day
        Date date = DateFormat.getDateInstance(DateFormat.SHORT, Locale.US).parse("12/12/10");
        workdays.addNonWorkday(date);

        // remove a non-work day
        workdays.removeNonWorkday(date);

        // Then, you can also get all non-workdays as a set.
        Set<Date> allNonWorkdays = workdays.getNonWorkdays();

        // You can refresh the configuration from config file, but it's DefaultWorkdays's method
        DefaultWorkdays defaultWorkdays = (DefaultWorkdays) workdays;
        defaultWorkdays.refresh();

        // or maybe you want to save as another file,
        // first you should set the file_name and file_format
        defaultWorkdays.setFileName("test_files/save_as_demo.XML");
        defaultWorkdays.setFileFormat(DefaultWorkdays.XML_FILE_FORMAT);

        // now save as the file you set
        defaultWorkdays.save();

        // the most important, this component can help you calculate the end date that there are amount units of time
        // between the start time and end time.
        Date endDate = workdays.add(new Date(), WorkdaysUnitOfTime.DAYS, 100);

        // or
        endDate = workdays.add(new Date(), WorkdaysUnitOfTime.HOURS, 10);

        // or
        endDate = workdays.add(new Date(), WorkdaysUnitOfTime.MINUTES, 10000);
    }
}
