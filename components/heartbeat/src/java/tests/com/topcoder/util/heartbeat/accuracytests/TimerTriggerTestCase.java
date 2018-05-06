/**
 * TCS Heartbeat
 *
 * TimerTriggerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.util.*;

/**
 * Tests functionality of TimerTrigger.
 *
 * @author valeriy
 * @version 1.0
 */
public class TimerTriggerTestCase extends TestCase {

    /**
     * Tests schedule(HeartBeat, long), contains(HeartBeat) and
     * remove(HeartBeat) method.
     */
    public void testSchedule1() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, 1000000);
        assertEquals(trig.getHeartBeats().size(), 4);
        assertTrue(trig.contains(beat));
        assertTrue(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests schedule(HeartBeat, long, long), contains(HeartBeat)
     * and remove(HeartBeat) method.
     */
    public void testSchedule2() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, 1000000, 1000000);
        assertEquals(trig.getHeartBeats().size(), 4);
        assertTrue(trig.contains(beat));
        assertTrue(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests schedule(HeartBeat, 0), contains(HeartBeat) and
     * remove(HeartBeat) method.
     */
    public void testSchedule3() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, 0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        assertEquals(beat.getFiredCount(), 1);
        assertEquals(trig.getHeartBeats().size(), 3);// !!! see specs
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests schedule(HeartBeat, Date), contains(HeartBeat) and remove(HeartBeat) method.
     */
    public void testSchedule4() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, new Date(System.currentTimeMillis()+1000000));
        assertEquals(trig.getHeartBeats().size(), 4);
        assertTrue(trig.contains(beat));
        assertTrue(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests schedule(HeartBeat, Date, long), contains(HeartBeat) and remove(HeartBeat) method.
     */
    public void testSchedule5() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, new Date(System.currentTimeMillis()+1000000), 1000000);
        assertEquals(trig.getHeartBeats().size(), 4);
        assertTrue(trig.contains(beat));
        assertTrue(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests schedule(HeartBeat, Date in past), contains(HeartBeat) and remove(HeartBeat) method.
     */
    public void testSchedule6() {
        TimerTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.schedule(beat, new Date(System.currentTimeMillis()-100));
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {}
        assertEquals(beat.getFiredCount(), 1);
        assertEquals(trig.getHeartBeats().size(), 3);// !!! see specs
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests consistency between getHeartBeats() and
     * contains(HeartBeat) methods.
     */
    public void testContains() {
        TimerTrigger trig = createTrigger();
        List beats = trig.getHeartBeats();
        assertEquals(beats.size(), 3);
        for (int i = 0; i < beats.size(); i++) {
            if (!trig.contains((HeartBeat)beats.get(i))) {
                fail("Method contains(HeartBeat) is broken");
            }
        }
    }

    /**
     * Tests remove(HeartBeat) method.
     */
    public void testRemove() {
        TimerTrigger trig = createTrigger();
        List beats = trig.getHeartBeats();
        int removed = 0;
        for (int i = 0; i < beats.size(); i++) {
            if (trig.remove((HeartBeat)beats.get(i))) {
                removed++;
            }
        }
        assertEquals("Method remove(HeartBeat) is broken", beats.size(), removed);
        assertEquals("Method remove(HeartBeat) is broken", trig.getHeartBeats().size(), 0);
    }

    /**
     * Tests removeAll() method.
     */
    public void testRemoveAll() {
        TimerTrigger trig = createTrigger();
        List beats = trig.getHeartBeats();
        List removed = trig.removeAll();
        assertEquals("Method removeAll() is broken",
            beats.size(), removed.size());
        assertEquals("Method removeAll() is broken",
            trig.getHeartBeats().size(), 0);
    }

    /** Creates TimerTrigger for testing
     */
    private TimerTrigger createTrigger() {
        TimerTrigger trig = new TimerTrigger();
        trig.schedule(new MyHeartBeat(), 1000000);
        trig.schedule(new MyHeartBeat(), 1000000);
        trig.schedule(new MyHeartBeat(), 1000000);
        return trig;
    }

}
