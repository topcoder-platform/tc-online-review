/*
 * Copyright (C) 2010-2011 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.deliverable.latetracker.failuretests;

import java.util.Date;

import junit.framework.TestCase;

import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.date.workdays.DefaultWorkdays;
import com.topcoder.management.deliverable.Deliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverable;
import com.topcoder.management.deliverable.latetracker.LateDeliverableType;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesProcessingException;
import com.topcoder.management.deliverable.latetracker.LateDeliverablesTrackerConfigurationException;
import com.topcoder.management.deliverable.latetracker.processors.LateDeliverableProcessorImpl;
import com.topcoder.management.project.Project;
import com.topcoder.management.project.ProjectCategory;
import com.topcoder.management.project.ProjectStatus;
import com.topcoder.management.project.ProjectType;
import com.topcoder.project.phases.Phase;

/**
 * Failure test cases <code>LateDeliverableProcessorImpl</code>.
 *
 * @author gjw99, jmn, mumujava
 * @version 1.3
 * @since 1.0
 */
public class LateDeliverableProcessorImplFailureTests extends TestCase {
    /** Represents the instance to be tested. */
    private LateDeliverableProcessorImpl instance;

    /**
     * Represents the ConfigurationObject to configure.
     */
    private ConfigurationObject config;

    /**
     * Set up for test cases.
     *
     * @throws Exception
     *             to jUnit.
     */
    public void setUp() throws Exception {
        TestHelper.addConfig();
        TestHelper.executeSqlFile("test_files/failure/insert.sql");
        this.instance = new LateDeliverableProcessorImpl();
        config = TestHelper.getConfigurationObject("test_files/failure/Processor.xml", "failuretests");
    }

    /**
     * Tear down for test cases.
     *
     * @throws Exception
     *             to jUnit.
     */
    public void tearDown() throws Exception {
        TestHelper.cleanTables();
        TestHelper.clearNamespace();
        this.instance = null;
        config = null;
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure1() throws Exception {
        try {
            instance.configure(null);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure2() throws Exception {
        try {
            config.setPropertyValue("loggerName", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure3() throws Exception {
        try {
            config.setPropertyValue("connectionName", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure4() throws Exception {
        try {
            config.setPropertyValue("missedDeadlineNotificationDeliverableIds", new Exception());
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure5() throws Exception {
        try {
            config.setPropertyValue("missedDeadlineNotificationDeliverableIds", ", ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure6() throws Exception {
        try {
            config.setPropertyValue("missedDeadlineNotificationDeliverableIds", "1,2,-1");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure7() throws Exception {
        try {
            config.setPropertyValue("missedDeadlineNotificationDeliverableIds", "1,2,,3");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure8() throws Exception {
        try {
            config.removeChild("objectFactoryConfig");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure9() throws Exception {
        try {
            config.removeProperty("resourceManagerKey");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure10() throws Exception {
        try {
            config.setPropertyValue("resourceManagerKey", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure11() throws Exception {
        try {
            config.removeProperty("emailSender");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure12() throws Exception {
        try {
            config.setPropertyValue("emailSender", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure13() throws Exception {
        try {
            config.removeProperty("userRetrievalKey");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure14() throws Exception {
        try {
            config.setPropertyValue("userRetrievalKey", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure15() throws Exception {
        try {
            config.setPropertyValue("timestampFormat", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure16() throws Exception {
        try {
            config.setPropertyValue("notificationInterval", "aa");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure17() throws Exception {
        try {
            config.setPropertyValue("explanationDeadlineIntervalInHours", "explanationDeadlineIntervalInHours");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure18() throws Exception {
        try {
            config.setPropertyValue("explanationDeadlineIntervalInHours", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure19() throws Exception {
        try {
            config.setPropertyValue("rejectedFinalFixEmailSubjectTemplateText", null);
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure20() throws Exception {
        try {
            config.setPropertyValue("rejectedFinalFixEmailBodyTemplatePath", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure21() throws Exception {
        try {
            config.setPropertyValue("rejectedFinalFixEmailBodyTemplatePath", null);
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure22() throws Exception {
        try {
            config.setPropertyValue("lateDeliverableTypeIds", " ");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure23() throws Exception {
        try {
            config.setPropertyValue("lateDeliverableTypeIds", null);
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure24() throws Exception {
        try {
            config.setPropertyValue("lateDeliverableTypeIds", "invalid types ids");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure25() throws Exception {
        try {
            config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=XXX,Rejected Final Fix=YYY");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure26() throws Exception {
        try {
            config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=0,Rejected Final Fix=-1");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }
    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_configure27() throws Exception {
        try {
            config.setPropertyValue("sendRejectedFinalFixNotifications", "not a boolean");
            instance.configure(config);
            fail("LateDeliverablesTrackerConfigurationException should be thrown.");
        } catch (LateDeliverablesTrackerConfigurationException e) {
            // pass
        }
    }

    /**
     * Test configure.
     *
     * @throws Exception
     *             if any error
     */
    public void test_configure() throws Exception {
            instance.configure(config);
    }

    /**
     * Test processLateDeliverable.
     *
     * @throws Exception
     *             if any error
     */
    public void test_processLateDeliverable1() throws Exception {
        try {
            instance.processLateDeliverable(null);
            fail("IAE should be thrown.");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    /**
     * Test processLateDeliverable.
     *
     * @throws Exception
     *             if any error
     */
    public void test_processLateDeliverable2() throws Exception {
        LateDeliverable entity = new LateDeliverable();
        entity.setCompensatedDeadline(new Date());
        entity.setType(LateDeliverableType.MISSED_DEADLINE);
        entity.setDeliverable(new Deliverable(1, 1, 1, new Long(1), false));
        entity.setProject(new Project(1, new ProjectCategory(1, "n", new ProjectType(1, "1")),
            new ProjectStatus(1, "1")));
        entity.setPhase(new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 1));
        entity.getPhase().setScheduledEndDate(new Date());

        try {
            instance.processLateDeliverable(entity);
            fail("ISE should be thrown.");
        } catch (IllegalStateException e) {
            // pass
        }
    }

    /**
     * Test processLateDeliverable.
     *
     * @throws Exception
     *             if any error
     */
    public void test_processLateDeliverable3() throws Exception {
        LateDeliverable entity = new LateDeliverable();
        entity.setCompensatedDeadline(new Date());
        entity.setDeliverable(new Deliverable(1, 1, 1, new Long(1), false));
        entity.setProject(new Project(1, new ProjectCategory(1, "n", new ProjectType(1, "1")),
            new ProjectStatus(1, "1")));
        entity.setPhase(new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 1));
        entity.getPhase().setScheduledEndDate(new Date());
        entity.setType(LateDeliverableType.MISSED_DEADLINE);

        try {
            instance.configure(config);

            TestHelper.setPrivateField(LateDeliverableProcessorImpl.class, instance, "connectionName", "wrong name");
            instance.processLateDeliverable(entity);
            fail("LateDeliverablesProcessingException should be thrown.");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }
    /**
     * Test processLateDeliverable. lateDeliverableTypeIds doesn't exist.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_processLateDeliverable4() throws Exception {
        LateDeliverable entity = new LateDeliverable();
        entity.setCompensatedDeadline(new Date());
        entity.setDeliverable(new Deliverable(1, 1, 1, new Long(1), false));
        entity.setProject(new Project(1, new ProjectCategory(1, "n", new ProjectType(1, "1")),
            new ProjectStatus(1, "1")));
        entity.setPhase(new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 1));
        entity.getPhase().setScheduledEndDate(new Date());
        entity.setType(LateDeliverableType.REJECTED_FINAL_FIX);

        config.setPropertyValue("lateDeliverableTypeIds", "Missed Deadline=11110,Rejected Final Fix=10000");
        instance.configure(config);
        try {

            instance.processLateDeliverable(entity);
            fail("LateDeliverablesProcessingException should be thrown.");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }
    /**
     * Test processLateDeliverable. the template doesn't exist.
     *
     * @throws Exception
     *             if any error
     * @since 1.3
     */
    public void test_processLateDeliverable5() throws Exception {
        LateDeliverable entity = new LateDeliverable();
        entity.setCompensatedDeadline(new Date());
        entity.setDeliverable(new Deliverable(1, 1, 1, new Long(1), false));
        entity.setProject(new Project(1, new ProjectCategory(1, "n", new ProjectType(1, "1")),
            new ProjectStatus(1, "1")));
        entity.setPhase(new Phase(new com.topcoder.project.phases.Project(new Date(), new DefaultWorkdays()), 1));
        entity.getPhase().setScheduledEndDate(new Date());
        entity.setType(LateDeliverableType.REJECTED_FINAL_FIX);

        config.setPropertyValue("rejectedFinalFixEmailBodyTemplatePath", "the template doesn't exist");
        instance.configure(config);
        try {

            instance.processLateDeliverable(entity);
            fail("LateDeliverablesProcessingException should be thrown.");
        } catch (LateDeliverablesProcessingException e) {
            // pass
        }
    }
}
