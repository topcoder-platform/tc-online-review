/**
 * TCS Heartbeat
 *
 * HeartBeatManagerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.util.List;

/**
 * Tests functionality of HeartBeatManager.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class HeartBeatManagerTestCase extends TestCase {

    /**
     * The HeartBeatManager to test.
     */
    private HeartBeatManager manager = null;

    /**
     * Set up testing environment.
     */
    public void setUp() {
        manager = new HeartBeatManager();
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
        manager.removeAllHeartBeats();
        manager = null;
    }

    /**
     * Test create HeartBeatManager.
     */
    public void testCreateHeartBeatManager() {
        assertNotNull(new HeartBeatManager());
    }

    /**
     * Test add 1.
     */
    public void testAdd1() {
        manager.add(new MyHeartBeat(),
                    10000);

        try {
            manager.add(null,
                        10000);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            manager.add(new MyHeartBeat(),
                        0);
            fail();
        } catch (IllegalArgumentException iae) {
            ;
        }
    }

    /**
     * Test add logic.
     */
    public void testAddLogic() {
        // fire according to period
        MyHeartBeat heartBeat = new MyHeartBeat();
        manager.add(heartBeat,
                    100);
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 1);
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 2);
    }

    /**
     * Test add 2.
     */
    public void testAdd2() {
        manager.add(new ManualTrigger());
        manager.add(new ManualTrigger());

        try {
            manager.add(null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test getHeartBeats.
     */
    public void testGetHeartBeats() {
        HeartBeat heartBeat1 = new MyHeartBeat();
        HeartBeat heartBeat2 = new MyHeartBeat();
        manager.add(heartBeat1,
                    10000);
        ManualTrigger trigger = new ManualTrigger();
        trigger.add(heartBeat2);
        manager.add(trigger);

        List heartBeats = manager.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));

        heartBeats = manager.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));
    }

    /**
     * Test getHeartBeatTriggers.
     */
    public void testGetHeartBeatTriggers() {
        ManualTrigger trigger1 = new ManualTrigger();
        ManualTrigger trigger2 = new ManualTrigger();
        manager.add(trigger1);
        manager.add(trigger2);

        List triggers = manager.getHeartBeatTriggers();
        assertTrue(triggers.size() == 3);
        assertTrue(triggers.contains(trigger1));
        assertTrue(triggers.contains(trigger2));

        triggers = manager.getHeartBeatTriggers();
        assertTrue(triggers.size() == 3);
        assertTrue(triggers.contains(trigger1));
        assertTrue(triggers.contains(trigger2));
    }

    /**
     * Test remove 1.
     */
    public void testRemove1() {
        HeartBeat heartBeat1 = new MyHeartBeat();
        HeartBeat heartBeat2 = new MyHeartBeat();
        manager.add(heartBeat1,
                    10000);
        ManualTrigger trigger = new ManualTrigger();
        trigger.add(heartBeat2);
        manager.add(trigger);

        assertTrue(manager.remove(heartBeat1));
        assertFalse(manager.remove(heartBeat1));
        assertTrue(manager.remove(heartBeat2));
        assertFalse(manager.remove(heartBeat2));

        try {
            manager.remove((HeartBeat) null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test remove 2.
     */
    public void testRemove2() {
        ManualTrigger trigger1 = new ManualTrigger();
        ManualTrigger trigger2 = new ManualTrigger();
        manager.add(trigger1);
        manager.add(trigger2);

        assertTrue(manager.remove(trigger1));
        assertFalse(manager.remove(trigger1));
        assertTrue(manager.remove(trigger2));
        assertFalse(manager.remove(trigger2));

        try {
            manager.remove((HeartBeatTrigger) null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test removeAllHeartBeats.
     */
    public void testRemoveAllHeartBeats() {
        HeartBeat heartBeat1 = new MyHeartBeat();
        HeartBeat heartBeat2 = new MyHeartBeat();
        manager.add(heartBeat1,
                    10000);
        ManualTrigger trigger = new ManualTrigger();
        trigger.add(heartBeat2);
        manager.add(trigger);

        List heartBeats = manager.removeAllHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));

        heartBeats = manager.removeAllHeartBeats();
        assertTrue(heartBeats.size() == 0);
    }

    /**
     * Test removeAllHeartBeatTriggers.
     */
    public void testRemoveAllHeartBeatTriggers() {
        ManualTrigger trigger1 = new ManualTrigger();
        ManualTrigger trigger2 = new ManualTrigger();
        manager.add(trigger1);
        manager.add(trigger2);

        List triggers = manager.removeAllHeartBeatTriggers();
        assertTrue(triggers.size() == 2);
        assertTrue(triggers.contains(trigger1));
        assertTrue(triggers.contains(trigger2));

        triggers = manager.removeAllHeartBeatTriggers();
        assertTrue(triggers.size() == 0);
    }

    /**
     * Test contains 1.
     */
    public void testContains1() {
        HeartBeat heartBeat1 = new MyHeartBeat();
        HeartBeat heartBeat2 = new MyHeartBeat();
        manager.add(heartBeat1,
                    10000);
        ManualTrigger trigger = new ManualTrigger();
        trigger.add(heartBeat2);
        manager.add(trigger);

        assertTrue(manager.contains(heartBeat1));
        assertTrue(manager.contains(heartBeat2));
        assertFalse(manager.contains(new MyHeartBeat()));

        try {
            manager.contains((HeartBeat) null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test contains 2.
     */
    public void testContains2() {
        ManualTrigger trigger1 = new ManualTrigger();
        ManualTrigger trigger2 = new ManualTrigger();
        manager.add(trigger1);
        manager.add(trigger2);

        assertTrue(manager.contains(trigger1));
        assertTrue(manager.contains(trigger2));
        assertFalse(manager.contains(new ManualTrigger()));

        try {
            manager.contains((HeartBeatTrigger) null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

}
