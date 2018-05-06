/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.topcoder.util.scheduler.scheduling.persistence.ConfigManagerSchedulerTests;
import com.topcoder.util.scheduler.scheduling.persistence.ConfigurationObjectSchedulerTests;
import com.topcoder.util.scheduler.scheduling.persistence.DBSchedulerTests;
import com.topcoder.util.scheduler.scheduling.persistence.SchedulerHelperTests;

/**
 * <p>This test case aggregates all Unit test cases.</p>
 *
 * @author TCSDEVELOPER
 * @version 3.0
 */
public class UnitTests extends TestCase {
    /**
     * <p>
     * This test case aggregates all Unit test cases.
     * </p>
     *
     * @return all Unit test cases.
     */
    public static Test suite() {
        final TestSuite suite = new TestSuite();

        // tests for package com.topcoder.util.scheduler.scheduling
        suite.addTest(DaysOfWeekTests.suite());
        suite.addTest(WeekOfMonthTests.suite());
        suite.addTest(DependenceTests.suite());
        suite.addTest(JobTests.suite());
        suite.addTest(JobGroupTests.suite());
        suite.addTest(HourTests.suite());
        suite.addTest(MinuteTests.suite());
        suite.addTest(WeekTests.suite());
        suite.addTest(ConfigurationExceptionTests.suite());
        suite.addTest(YearTests.suite());
        suite.addTest(UtilTests.suite());
        suite.addTest(SecondTests.suite());
        suite.addTest(DayOfMonthTests.suite());
        suite.addTest(SchedulingExceptionTests.suite());
        suite.addTest(WeekMonthOfYearTests.suite());
        suite.addTest(EmailEventHandlerTests.suite());
        suite.addTest(MonthTests.suite());
        suite.addTest(DayOfYearTests.suite());
        suite.addTest(DayTests.suite());
        suite.addTest(JobTypeTests.suite());

        // tests for package com.topcoder.util.scheduler.scheduling.persistence
        suite.addTest(ConfigManagerSchedulerTests.suite());
        suite.addTest(DBSchedulerTests.suite());
        suite.addTest(SchedulerHelperTests.suite());

        // tests for the demo
        suite.addTest(DemoTests.suite());

        // tests for version 3.1
        suite.addTestSuite(DemoV31Tests.class);
        suite.addTestSuite(JobV31Tests.class);
        suite.addTestSuite(ScheduledEnableObjectCreationExceptionTests.class);
        suite.addTestSuite(ScheduledEnableObjectFactoryManagerTests.class);
        suite.addTestSuite(ConfigurationObjectSchedulerTests.class);

        return suite;
    }

}
