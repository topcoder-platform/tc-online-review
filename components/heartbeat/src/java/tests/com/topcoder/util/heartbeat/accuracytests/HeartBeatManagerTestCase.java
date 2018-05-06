/**
 * TCS Heartbeat
 *
 * HeartBeatManagerTestCase.java
 *
 * Copyright (c) 2003, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.heartbeat.accuracytests;

import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;
import java.util.List;

/**
 * Tests functionality of HeartBeatManager.
 *
 * @author valeriy
 * @version 1.0
 */
public class HeartBeatManagerTestCase extends TestCase {

    /**
     * Tests add(HeartBeat), contains(HeartBeat) and
     * remove(HeartBeat) method.
     */
    public void testAdd1() {
        HeartBeatManager man = createManager();
        MyHeartBeat beat = new MyHeartBeat();
        man.add(beat, 1000000);
        assertEquals(man.getHeartBeats().size(), 10);
        assertTrue(man.contains(beat));
        assertTrue(man.remove(beat));
        assertEquals(man.getHeartBeats().size(), 9);
        assertFalse(man.contains(beat));
        assertFalse(man.remove(beat));
        assertEquals(man.getHeartBeats().size(), 9);
    }

    /**
     * Tests add(HeartBeatTrigger), contains(HeartBeatTrigger) and
     * remove(HeartBeatTrigger) method.
     */
    public void testAdd2() {
        HeartBeatManager man = createManager();
        HeartBeatTrigger trig = new ManualTrigger();
        man.add(trig);
        assertEquals(man.getHeartBeatTriggers().size(), 4);//+timerTrigger
        assertTrue(man.contains(trig));
        assertTrue(man.remove(trig));
        assertEquals(man.getHeartBeatTriggers().size(), 3);
        assertFalse(man.contains(trig));
        assertFalse(man.remove(trig));
        assertEquals(man.getHeartBeatTriggers().size(), 3);
    }

    /**
     * Tests consistency between getHeartBeats() and
     * contains(HeartBeat) methods.
     */
    public void testContains1() {
        HeartBeatManager man = createManager();
        List beats = man.getHeartBeats();
        assertEquals(beats.size(), 9);
        for (int i = 0; i < beats.size(); i++) {
            if (!man.contains((HeartBeat)beats.get(i))) {
                fail("Method contains(HeartBeat) is broken");
            }
        }
    }

    /**
     * Tests consistency between getHeartBeatTriggers() and
     * contains(HeartBeatTrigger) methods.
     */
    public void testContains2() {
        HeartBeatManager man = createManager();
        List trigs = man.getHeartBeatTriggers();
        assertEquals(trigs.size(), 3);//+timerTrigger
        for (int i = 0; i < trigs.size(); i++) {
            if (!man.contains((HeartBeatTrigger)trigs.get(i))) {
                fail("Method contains(HeartBeatTrigger) is broken");
            }
        }
    }
    
    /**
     * Tests remove(HeartBeat) method.
     */
    public void testRemove1() {
        HeartBeatManager man = createManager();
        List beats = man.getHeartBeats();
        int removed = 0;
        for (int i = 0; i < beats.size(); i++) {
            if (man.remove((HeartBeat)beats.get(i))) {
                removed++;
            }
        }
        assertEquals("Method remove(HeartBeat) is broken", beats.size(), removed);
        assertEquals("Method remove(HeartBeat) is broken", man.getHeartBeats().size(), 0);
    }

    /**
     * Tests remove(HeartBeatTrigger) method.
     */
    public void testRemove2() {
        HeartBeatManager man = createManager();
        List trigs = man.getHeartBeatTriggers();
        int removed = 0;
        for (int i = 0; i < trigs.size(); i++) {
            if (man.remove((HeartBeatTrigger)trigs.get(i))) {
                removed++;
            }
        }
        assertEquals("Method remove(HeartBeatTrigger) is broken", man.getHeartBeatTriggers().size(), trigs.size()-removed);
    }

    /**
     * Tests removeAllHeartBeats() method.
     */
    public void testRemoveAll1() {
        HeartBeatManager man = createManager();
        List beats = man.getHeartBeats();
        List removed = man.removeAllHeartBeats();
        assertEquals("Method removeAllHeartBeats() is broken",
            beats.size(), removed.size());
        assertEquals("Method removeAllHeartBeats() is broken",
            man.getHeartBeats().size(), 0);
    }

    /**
     * Tests removeAllHeartBeatTriggers() method.
     */
    public void testRemoveAll2() {
        HeartBeatManager man = createManager();
        List trigs = man.getHeartBeatTriggers();
        List removed = man.removeAllHeartBeatTriggers();
        assertEquals("Method removeAllHeartBeatTriggers() is broken",
            man.getHeartBeatTriggers().size(), trigs.size()-removed.size());
    }

    /** Creates HeartBeatManager for testing
     */
    private HeartBeatManager createManager() {
        HeartBeatManager man = new HeartBeatManager();
        man.add(new MyHeartBeat(), 1000000);
        man.add(new MyHeartBeat(), 1000000);
        ManualTrigger trig = new ManualTrigger();
        trig.add(new MyHeartBeat());
        trig.add(new MyHeartBeat());
        trig.add(new MyHeartBeat());
        ManualTrigger trig2 = new ManualTrigger();
        trig2.add(new MyHeartBeat());
        trig2.add(new MyHeartBeat());
        trig2.add(new MyHeartBeat());
        trig2.add(new MyHeartBeat());
        man.add(trig);
        man.add(trig2);
        return man;
    }
    
}

