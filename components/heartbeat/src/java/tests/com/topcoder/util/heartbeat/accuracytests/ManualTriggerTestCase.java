/**
 * TCS Heartbeat
 *
 * ManualTriggerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.util.List;

/**
 * Tests functionality of ManualTrigger.
 *
 * @author valeriy
 * @version 1.0
 */
public class ManualTriggerTestCase extends TestCase {

    /**
     * Tests add(HeartBeat), contains(HeartBeat) and remove(HeartBeat) method.
     */
    public void testAdd() {
        ManualTrigger trig = createTrigger();
        MyHeartBeat beat = new MyHeartBeat();
        trig.add(beat);
        assertEquals(trig.getHeartBeats().size(), 4);
        assertTrue(trig.contains(beat));
        assertTrue(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
        assertFalse(trig.contains(beat));
        assertFalse(trig.remove(beat));
        assertEquals(trig.getHeartBeats().size(), 3);
    }

    /**
     * Tests consistency between getHeartBeats() and
     * contains(HeartBeat) methods.
     */
    public void testContains() {
        ManualTrigger trig = createTrigger();
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
        ManualTrigger trig = createTrigger();
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
        ManualTrigger trig = createTrigger();
        List beats = trig.getHeartBeats();
        List removed = trig.removeAll();
        assertEquals("Method removeAll() is broken",
            beats.size(), removed.size());
        assertEquals("Method removeAll() is broken",
            trig.getHeartBeats().size(), 0);
    }

    /**
     * Tests fireKeepAlive() method.
     */
    public void testFireKeepAlive() {
        ManualTrigger trig = createTrigger();
        trig.fireKeepAlive();
        trig.fireKeepAlive();
        trig.fireKeepAlive();
        trig.fireKeepAlive();
        List beats = trig.getHeartBeats();
        for (int i = 0; i < beats.size(); i++) {
            MyHeartBeat beat = (MyHeartBeat)beats.get(i);
            assertEquals("Method fireKeepAlive() is broken", beat.getFiredCount(), 4);
        }
    }

    /** Creates ManualTrigger for testing
     */
    private ManualTrigger createTrigger() {
        ManualTrigger trig = new ManualTrigger();
        trig.add(new MyHeartBeat());
        trig.add(new MyHeartBeat());
        trig.add(new MyHeartBeat());
        return trig;
    }

}
