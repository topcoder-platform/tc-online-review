/**
 * TCS Heartbeat
 *
 * TimerTriggerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat;

import junit.framework.TestCase;

import java.util.List;
import java.util.Date;

/**
 * Tests functionality of TimerTrigger.
 *
 * @author TCSDeveloper
 * @version 1.0
 */
public class TimerTriggerTestCase extends TestCase {

    /**
     * The TimerTrigger to test.
     */
    private TimerTrigger trigger = null;

    /**
     * Set up testing environment.
     */
    public void setUp() {
        trigger = new TimerTrigger();
    }

    /**
     * Tear down testing environment.
     */
    public void tearDown() {
        trigger.removeAll();
        trigger = null;
    }

    /**
     * Test create TimerTrigger.
     */
    public void testCreateTimerTrigger() {
        assertNotNull(new TimerTrigger());
    }

    /**
     * Test schedule 1.
     */
    public void testSchedule1() {
        trigger.schedule(new MyHeartBeat(),
                         new Date());

        try {
            trigger.schedule(null,
                             new Date());
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             null);
            fail();
        } catch (NullPointerException npe) {
            ;
        }
    }

    /**
     * Test schedule logic 1.
     */
    public void testScheduleLogic1() {
        // in the past, will be fired immediately
        MyHeartBeat heartBeat = new MyHeartBeat();
        trigger.schedule(heartBeat,
                         new Date(System.currentTimeMillis() - 100));
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
        assertTrue(heartBeat.getFiredCount() == 1);
    }

    /**
     * Test schedule logic 2.
     */
    public void testScheduleLogic2() {
        // in the future, fire on time
        MyHeartBeat heartBeat = new MyHeartBeat();
        trigger.schedule(heartBeat,
                         new Date(System.currentTimeMillis() + 100));
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 0);
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 1);
    }

    /**
     * Test schedule 2.
     */
    public void testSchedule2() {
        trigger.schedule(new MyHeartBeat(),
                         new Date(),
                         10000);

        try {
            trigger.schedule(null,
                             new Date(),
                             10000);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             null,
                             10000);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             new Date(),
                             0);
            fail();
        } catch (IllegalArgumentException iae) {
            ;
        }
    }

    /**
     * Test schedule logic 3.
     */
    public void testScheduleLogic3() {
        // fire according to period
        MyHeartBeat heartBeat = new MyHeartBeat();
        trigger.schedule(heartBeat,
                         new Date(System.currentTimeMillis() + 100),
                         100);
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 0);
        synchronized (this) {
            try {
                wait(100);
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
     * Test schedule 3.
     */
    public void testSchedule3() {
        trigger.schedule(new MyHeartBeat(),
                         10000);

        try {
            trigger.schedule(null,
                             10000);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             -1);
            fail();
        } catch (IllegalArgumentException iae) {
            ;
        }
    }

    /**
     * Test schedule logic 4.
     */
    public void testScheduleLogic4() {
        // fire at specified delay
        MyHeartBeat heartBeat = new MyHeartBeat();
        trigger.schedule(heartBeat,
                         100);
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 0);
        synchronized (this) {
            try {
                wait(100);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 1);
    }

    /**
     * Test schedule 4.
     */
    public void testSchedule4() {
        trigger.schedule(new MyHeartBeat(),
                         10000,
                         10000);

        try {
            trigger.schedule(null,
                             10000,
                             10000);
            fail();
        } catch (NullPointerException npe) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             -1,
                             10000);
            fail();
        } catch (IllegalArgumentException iae) {
            ;
        }

        try {
            trigger.schedule(new MyHeartBeat(),
                             10000,
                             0);
            fail();
        } catch (IllegalArgumentException iae) {
            ;
        }
    }

    /**
     * Test schedule logic 5.
     */
    public void testScheduleLogic5() {
        // fire according to period
        MyHeartBeat heartBeat = new MyHeartBeat();
        trigger.schedule(heartBeat,
                         100,
                         100);
        synchronized (this) {
            try {
                wait(50);
            } catch (InterruptedException ie) {
                ;
            }
        }
        assertTrue(heartBeat.getFiredCount() == 0);
        synchronized (this) {
            try {
                wait(100);
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
     * Test remove.
     */
    public void testRemove() {
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();

        trigger.schedule(heartBeat1,
                         10000);
        trigger.schedule(heartBeat2,
                         10000);

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
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();

        trigger.schedule(heartBeat1,
                         10000);
        trigger.schedule(heartBeat2,
                         10000);

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
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();

        trigger.schedule(heartBeat1,
                         10000);
        trigger.schedule(heartBeat2,
                         10000);

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
        MyHeartBeat heartBeat1 = new MyHeartBeat();
        MyHeartBeat heartBeat2 = new MyHeartBeat();

        trigger.schedule(heartBeat1,
                         10000);
        trigger.schedule(heartBeat2,
                         10000);

        List heartBeats = trigger.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));

        heartBeats = trigger.getHeartBeats();
        assertTrue(heartBeats.size() == 2);
        assertTrue(heartBeats.contains(heartBeat1));
        assertTrue(heartBeats.contains(heartBeat2));
    }

}
