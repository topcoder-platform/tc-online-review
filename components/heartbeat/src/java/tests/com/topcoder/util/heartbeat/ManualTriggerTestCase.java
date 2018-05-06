/**
 * TCS Heartbeat
 *
 * ManualTriggerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.util.List;

/**
 * Tests functionality of ManualTrigger.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class ManualTriggerTestCase extends TestCase {

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
     * Test add.
     */
    public void testAdd() {
        ManualTrigger trigger = new ManualTrigger();
        trigger.add(new MyHeartBeat());
        trigger.add(new MyHeartBeat());

        try {
            trigger.add(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test remove.
     */
    public void testRemove() {
        ManualTrigger trigger = new ManualTrigger();
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();
        trigger.add(heartBeat1);
        trigger.add(heartBeat2);

        assertTrue(trigger.remove(heartBeat1));
        assertFalse(trigger.remove(heartBeat1));
        assertTrue(trigger.remove(heartBeat2));
        assertFalse(trigger.remove(heartBeat2));

        try {
            trigger.remove(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test removeAll.
     */
    public void testRemoveAll() {
        ManualTrigger trigger = new ManualTrigger();
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();
        trigger.add(heartBeat1);
        trigger.add(heartBeat2);

        List heartBeats = trigger.removeAll();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));

        heartBeats = trigger.removeAll();
        assertTrue(heartBeats.size() == 0);
    }

    /**
     * Test contains.
     */
    public void testContains() {
        ManualTrigger trigger = new ManualTrigger();
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();
        trigger.add(heartBeat1);
        trigger.add(heartBeat2);

        assertTrue(trigger.contains(heartBeat1));
        assertTrue(trigger.contains(heartBeat2));
        assertFalse(trigger.contains(new MyHeartBeat()));

        try {
            trigger.contains(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getHeartBeats.
     */
    public void testGetHeartBeats() {
        ManualTrigger trigger = new ManualTrigger();
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();
        trigger.add(heartBeat1);
        trigger.add(heartBeat2);

        List heartBeats = trigger.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));

        heartBeats = trigger.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));
    }

    /**
     * Test fireKeepAlive.
     */
    public void testFireKeepAlive() {
        ManualTrigger trigger = new ManualTrigger();
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        trigger.add(heartBeat1);
        trigger.fireKeepAlive();
        MyHeartBeat heartBeat2 = new MyHeartBeat();
        trigger.add(heartBeat2);
        trigger.fireKeepAlive();
        trigger.fireKeepAlive();

        assertTrue(heartBeat1.getFiredCount() == 3);
        assertTrue(heartBeat2.getFiredCount() == 2);
    }

}

/**
 * A trivial HeartBeat implementation for testing.
 */
class MyHeartBeat implements HeartBeat {

    /**
     * Number of fired count.
     */
    private int numFired = 0;

    /**
     * Get fired count.
     *
     * @return fired count
     */
    public int getFiredCount() {
        return numFired;
    }

    /**
     * Keep heartbeat.
     */
    public void keepAlive() {
        ++numFired;
    }

    /**
     * Get last exception.
     *
     * @return null
     */
    public Exception getLastException() {
        return null;
    }

}
