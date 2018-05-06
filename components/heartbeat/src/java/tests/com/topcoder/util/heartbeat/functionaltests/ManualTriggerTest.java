package com.topcoder.util.heartbeat.functionaltests;

import java.util.List;
import com.topcoder.util.heartbeat.*;
import junit.framework.TestCase;

/**
 * Test cases for ManualTrigger class
 */
public class ManualTriggerTest extends TestCase {

    /**
     * Test cases for the add() method
     */
	public void testAdd() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat = new TestHeartBeat();		
		
		// Add the beat
		trigger.add(beat);
		
		// Validate
		assertTrue(trigger.contains(beat));

		// Null test
		try {
			trigger.add(null);
			fail("Should have thrown a NullPointerException");
		} catch (NullPointerException e) {
		}
	}

    /**
     * Test cases for the remove() method
     */
	public void testRemove() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Add the beat
		trigger.add(beat);
		
		// Validate the remove
		assertTrue(trigger.remove(beat));
		assertFalse(trigger.remove(beat));
		
		// Null test
		try {
			trigger.remove(null);
			fail("Should have thrown a NullPointerException");
		} catch (NullPointerException e) {
		}
	}

    /**
     * Test cases for the removeAll() method
     */
	public void testRemoveAll() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat 
		trigger.add(beat1);
		trigger.add(beat2);
		
		// Remove all
		List list = trigger.removeAll();
		
		// Validate the remove
		assertEquals(list.size(), 2);
		assertEquals(trigger.getHeartBeats().size(), 0);
		assertTrue(list.contains(beat1));
		assertTrue(list.contains(beat2));
	}

    /**
     * Test cases for the contains() method
     */
	public void testContains() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat 
		trigger.add(beat1);

        // Validate
        assertTrue(trigger.contains(beat1));		
        assertFalse(trigger.contains(beat2));		

		// Null test
		try {
			trigger.contains(null);
			fail("Should have thrown a NullPointerException");
		} catch (NullPointerException e) {
		}
	}

    /**
     * Test cases for the getHeartBeats() method
     */
	public void testGetHeartBeats() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat 
		trigger.add(beat1);
		trigger.add(beat2);
		
		// Remove all
		List list = trigger.getHeartBeats();
		
		// Validate the remove
		assertEquals(list.size(), 2);
		assertTrue(list.contains(beat1));
		assertTrue(list.contains(beat2));
	}

    /**
     * Test cases for the fireKeepAlive() method
     */
	public void testFireKeepAlive() {
		// Setup the test
		ManualTrigger trigger = new ManualTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		
		// Add the beat
		trigger.add(beat1);

        // Fire it off
        trigger.fireKeepAlive();
        
        // Validate
        assertTrue(beat1.gotit);		
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
