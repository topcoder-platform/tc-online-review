package com.topcoder.util.heartbeat.functionaltests;

import java.util.List;
import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;

/**
 * Test cases for the HeartBeatManager class
 */
public class HeartBeatManagerTest extends TestCase {

    /**
     * Test cases for the constructor
     */
	public void testHeartBeatManager() {
	    // Normal constructor
	    HeartBeatManager manager = new HeartBeatManager();
	    
	    // Validate constructor
	    assertEquals(manager.getHeartBeatTriggers().size(), 1);
	}

	/**
	 * Test cases for add(HeartBeat, long) method
	 */
	public void testAddHeartBeatJ() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    TestHeartBeat beat = new TestHeartBeat();

        // Null tests
        try {
            manager.add(null, 1);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    

        // IllegalArg tests
        try {
            manager.add(beat, 0);
            fail("Did not throw a IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }	    
        
        // Add the beat for every 3 seconds
        manager.add(beat, 3000);
        
		// Wait 2 seconds
		try {
			synchronized(this) { wait(2000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Reset it
		beat.gotit = false;
		
		// Wait 2 more seconds
		try {
			synchronized(this) { wait(2000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Cleanup
		manager.remove(beat);
        
	}

	/**
	 * Test cases for add(HeartBeatTrigger) method
	 */
	public void testAddHeartBeatTrigger() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger = new ManualTrigger();
	    
	    // Add it
	    manager.add(trigger);
	    
	    // Verify
	    assertTrue(manager.contains(trigger));

        // Null tests
        try {
            manager.add(null);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    
	}

    /**
     * Test cases for getHeartBeats() method
     */
	public void testGetHeartBeats() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger = new ManualTrigger();
	    TestHeartBeat beat1 = new TestHeartBeat();
	    TestHeartBeat beat2 = new TestHeartBeat();
	    TestHeartBeat beat3 = new TestHeartBeat();
	    
	    // Add the beats to the trigger
	    manager.add(beat1, 20000);
	    trigger.add(beat2);
	    trigger.add(beat3);
	    
	    // Add the trigger
	    manager.add(trigger);
	    
	    // Get the heartbeats
	    List list = manager.getHeartBeats();
	    
	    // Verify
	    assertEquals(list.size(), 3);
	    assertTrue(list.contains(beat1));
	    assertTrue(list.contains(beat2));
	    assertTrue(list.contains(beat3));
	    
	    // Cleanup
	    manager.removeAllHeartBeats();
	}

    /**
     * Test cases for getHeartBeatTriggers() method
     */
	public void testGetHeartBeatTriggers() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger1 = new ManualTrigger();
	    ManualTrigger trigger2 = new ManualTrigger();
	    
	    // Add the triggers
	    manager.add(trigger1);
	    manager.add(trigger2);
	    
	    // Get the triggers
	    List list = manager.getHeartBeatTriggers();
	    assertEquals(list.size(), 3);
	    assertTrue(list.contains(trigger1));
	    assertTrue(list.contains(trigger2));
	}

	/**
	 * Test cases for remove(HeartBeat) method
	 */
	public void testRemoveHeartBeat() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger = new ManualTrigger();
	    TestHeartBeat beat1 = new TestHeartBeat();
	    TestHeartBeat beat2 = new TestHeartBeat();
	    TestHeartBeat beat3 = new TestHeartBeat();
	    
	    // Add the beats to the trigger
	    manager.add(beat1, 20000);
	    manager.add(beat3, 20000);
	    trigger.add(beat2);
	    trigger.add(beat3);
	    
	    // Add the trigger
	    manager.add(trigger);
	    
	    // Verify removes
	    assertTrue(manager.remove(beat1));
	    assertFalse(manager.remove(beat1));
	    assertTrue(manager.remove(beat2));
	    assertFalse(manager.remove(beat2));
	    assertTrue(manager.remove(beat3));
	    assertFalse(manager.remove(beat3));

        // Null tests
        try {
            manager.remove((HeartBeat)null);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    
	}

	/**
	 * Test cases for remove(HeartBeatTrigger) method
	 */
	public void testRemoveHeartBeatTrigger() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger1 = new ManualTrigger();
	    
	    // Add the triggers
	    manager.add(trigger1);

        // Verify the removes	    
        assertTrue(manager.remove(trigger1));
        assertFalse(manager.remove(trigger1));

        // Null tests
        try {
            manager.remove((HeartBeatTrigger)null);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    
	}

    /**
     * Test cases for removeAllHeartBeats() method
     */
	public void testRemoveAllHeartBeats() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger = new ManualTrigger();
	    TestHeartBeat beat1 = new TestHeartBeat();
	    TestHeartBeat beat2 = new TestHeartBeat();
	    TestHeartBeat beat3 = new TestHeartBeat();
	    
	    // Add the beats to the trigger
	    manager.add(beat1, 20000);
	    manager.add(beat3, 20000);
	    trigger.add(beat2);
	    trigger.add(beat3);
	    
	    // Add the trigger
	    manager.add(trigger);

        // Remove all the heart beats
        List list = manager.removeAllHeartBeats();
        
        // Verify
        assertEquals(list.size(), 4); // 4 because beat3 added twice
        assertEquals(manager.getHeartBeats().size(), 0);
        assertTrue(list.contains(beat1));
        assertTrue(list.contains(beat2));
        assertTrue(list.contains(beat3));
	}

    /**
     * Test cases for removeAllHeartBeatTriggers() method
     */
	public void testRemoveAllHeartBeatTriggers() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger1 = new ManualTrigger();
	    ManualTrigger trigger2 = new ManualTrigger();
	    
	    // Add the triggers
	    manager.add(trigger1);
	    manager.add(trigger2);
	    
	    // Get the triggers
	    List list = manager.removeAllHeartBeatTriggers();
	    assertEquals(list.size(), 2);
	    assertEquals(manager.getHeartBeatTriggers().size(), 1);
	    assertTrue(list.contains(trigger1));
	    assertTrue(list.contains(trigger2));
	}

	/**
	 * Test cases for contains(HeartBeat) method
	 */
	public void testContainsHeartBeat() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger = new ManualTrigger();
	    TestHeartBeat beat1 = new TestHeartBeat();
	    TestHeartBeat beat2 = new TestHeartBeat();
	    TestHeartBeat beat3 = new TestHeartBeat();
	    
	    // Add the beats to the trigger
	    manager.add(beat1, 20000);
	    trigger.add(beat2);
	    
	    // Add the trigger
	    manager.add(trigger);

        // Verify
        assertTrue(manager.contains(beat1));
        assertTrue(manager.contains(beat2));
        assertFalse(manager.contains(beat3));
        
        // Cleanup
        manager.removeAllHeartBeats();

        // Null tests
        try {
            manager.contains((HeartBeat)null);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    
	}

	/**
	 * Test cases for contains(HeartBeatTrigger) method
	 */
	public void testContainsHeartBeatTrigger() {
	    // Setup the tests
	    HeartBeatManager manager = new HeartBeatManager();
	    ManualTrigger trigger1 = new ManualTrigger();
	    ManualTrigger trigger2 = new ManualTrigger();
	    
	    // Add the triggers
	    manager.add(trigger1);
	    
	    // Verify
	    assertTrue(manager.contains(trigger1));
	    assertFalse(manager.contains(trigger2));

        // Null tests
        try {
            manager.remove((HeartBeatTrigger)null);
            fail("Did not throw a NullPointerException");
        } catch (NullPointerException e) {
        }	    
	}

	/** 
	 * Test class used 
	 */
	private class TestHeartBeat implements HeartBeat {

		public boolean gotit = false;
		public void keepAlive() {
			gotit = true;
		}
		public Exception getLastException() { 
		    return null;
		}
	}
}
