/**
 * TCS Heartbeat
 *
 * TimerTriggerTaskTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

/**
 * Tests functionality of TimerTriggerTask.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class TimerTriggerTaskTestCase extends TestCase {

    /**
     * Set up testing environment.
     */
    public void setUp() {
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
    }

    /**
     * Test create TimerTriggerTask.
     */
    public void testCreateTimerTriggerTask() {
        assertNotNull(new TimerTriggerTask(new MyHeartBeat()));

        try {
            new TimerTriggerTask(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getHeartBeat.
     */
    public void testGetHeartBeat() {
        MyHeartBeat heartBeat = new MyHeartBeat();
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        assertTrue(task.getHeartBeat() == heartBeat);
    }

    /**
     * Test run.
     */
    public void testRun() {
        MyHeartBeat heartBeat = new MyHeartBeat();
        TimerTriggerTask task = new TimerTriggerTask(heartBeat);
        task.run();
        task.run();
        assertTrue(heartBeat.getFiredCount() == 2);
    }

}
