/**
 *
 * Copyright (c) 2007, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * This test case aggregates all Accuracy test cases.
 * </p>
 *
 * @author TopCoder
 * @version 3.1
 */
public class AccuracyTests extends TestCase {

    /**
     * <p>
     * return the aggregated Accuracy test cases.
     * </p>
     *
     * @return the aggregated Accuracy test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();
        suite.addTest(DayOfMonthTest.suite());
        suite.addTest(DayOfYearTest.suite());
        suite.addTest(DaysOfWeekTest.suite());
        suite.addTest(DayTest.suite());
        suite.addTest(DependenceTest.suite());
        suite.addTest(EmailEventHandlerTest.suite());
        suite.addTest(HourTest.suite());
        suite.addTest(JobGroupTest.suite());
        suite.addTest(JobTest.suite());
        suite.addTest(MinuteTest.suite());
        suite.addTest(MonthTest.suite());
        suite.addTest(SecondTest.suite());
        suite.addTest(WeekMonthOfYearTest.suite());
        suite.addTest(WeekOfMonthTest.suite());
        suite.addTest(WeekTest.suite());
        suite.addTest(YearTest.suite());
        suite.addTest(ConfigManagerSchedulerTest.suite());
        suite.addTest(DBSchedulerTest.suite());
        return suite;
    }

}
