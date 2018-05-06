/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.scheduler.scheduling.accuracytests;

import com.topcoder.util.file.fieldconfig.NodeList;
import com.topcoder.util.scheduler.scheduling.ScheduledEnable;
import com.topcoder.util.scheduler.scheduling.ScheduledEnableObjectFactory;

/**
 * <p>
 * This is a simple implementation of ScheduledEnableObjectFactory.
 * </p>
 *
 * @author peony
 * @version 3.1
 */
public class AccuracyScheduledEnableObjectFactory implements ScheduledEnableObjectFactory {

    /**
     * <p>
     * The ScheduledEnable instance.
     * </p>
     */
    private ScheduledEnable scheduledEnable;

    /**
     * <p>
     * Constructor.
     * </p>
     */
    public AccuracyScheduledEnableObjectFactory() {
        scheduledEnable = new ScheduledEnable() {

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @return null
             */
            public NodeList getMessageData() {
                return null;
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @return null
             */
            public String getRunningStatus() {
                return null;
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             */
            public void run() {
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             */
            public void close() {
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @return null
             */
            public String getJobName() {
                return null;
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @return null
             */
            public String getStatus() {
                return null;
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @return false
             */
            public boolean isDone() {
                return false;
            }

            /**
             * <p>
             * Mock stubs.
             * </p>
             *
             * @param jobName the job name
             */
            public void setJobName(String jobName) {
            }
        };
    }

    /**
     * <p>
     * Creates a new ScheduledEnableObject.
     * </p>
     *
     * @return null
     */
    public ScheduledEnable createScheduledEnableObject() {
        return scheduledEnable;
    }
}
