package com.topcoder.util.heartbeat.functionaltests;

import java.util.Date;
import java.util.List;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * TestCases for the TimerTrigger Class
 */
public class TimerTriggerTest extends TestCase {

	/**
	 * Test cases for the constructor
	 */
	public void testTimerTrigger() {
		// Normal construction
		TimerTrigger timer = new TimerTrigger();
		
		// Validate construction
		assertEquals(timer.getHeartBeats().size(), 0);
	}

	/**
	 * Test cases for schedule(HeartBeat, Date)
	 */
	public void testScheduleHeartBeatDate() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Null tests
		try {
			timer.schedule(null, new Date());
			fail("Should have thrown a NullPointerException for null heartbeat");
		} catch (NullPointerException e) {
		}
		
		try {
			timer.schedule(beat, null);
			fail("Should have thrown a NullPointerException for null date");
		} catch (NullPointerException e) {
		}

		// Schedule the beat for one second from now
		timer.schedule(beat, new Date(System.currentTimeMillis() + 1000));	
		
		// Wait 3 seconds to be sure
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
			
	}

	/**
	 * Test cases for schedule(HeartBeat, Date, long)
	 */
	public void testScheduleHeartBeatDateJ() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Null tests
		try {
			timer.schedule(null, new Date(), 2000);
			fail("Should have thrown a NullPointerException for null heartbeat");
		} catch (NullPointerException e) {
		}
		
		try {
			timer.schedule(beat, null, 2000);
			fail("Should have thrown a NullPointerException for null date");
		} catch (NullPointerException e) {
		}

		// Illegal tests
		try {
			timer.schedule(beat, new Date(), 0);
			fail("Should have thrown a IllegalArg for zero period");
		} catch (IllegalArgumentException e) {
		}

		// Schedule the beat for one second from now repeating every 4 seconds
		timer.schedule(beat, new Date(System.currentTimeMillis() + 1000), 4000);	
		
		// Wait 3 seconds to be sure
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Reset the beat
		beat.gotit = false;
		
		// Wait 3 more seconds
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Cleanup
		timer.remove(beat);			
	}

	/**
	 * Test cases for schedule(HeartBeat, long)
	 */
	public void testScheduleHeartBeatJ() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Null tests
		try {
			timer.schedule(null, 2000);
			fail("Should have thrown a NullPointerException for null heartbeat");
		} catch (NullPointerException e) {
		}
		
		// Illegal tests
		try {
			timer.schedule(beat, -1);
			fail("Should have thrown a IllegalArg for negative delay");
		} catch (IllegalArgumentException e) {
		}

		// Schedule the beat for one second delay
		timer.schedule(beat, 1000);	
		
		// Wait 3 seconds to be sure
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
	}

	/**
	 * Test cases for schedule(HeartBeat, long, long)
	 */
	public void testScheduleHeartBeatJJ() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Null tests
		try {
			timer.schedule(null, 22, 2000);
			fail("Should have thrown a NullPointerException for null heartbeat");
		} catch (NullPointerException e) {
		}
		
		// Illegal tests
		try {
			timer.schedule(beat, -1, 2000);
			fail("Should have thrown a IllegalArg for negative delay");
		} catch (IllegalArgumentException e) {
		}

		try {
			timer.schedule(beat, 1000, 0);
			fail("Should have thrown a IllegalArg for zero period");
		} catch (IllegalArgumentException e) {
		}
		
		// Schedule the beat for one second from now repeating every 4 seconds
		timer.schedule(beat, 1000, 4000);	
		
		// Wait 3 seconds to be sure
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Reset the beat
		beat.gotit = false;
		
		// Wait 3 more seconds
		try {
			synchronized(this) { wait(3000); }
		} catch (InterruptedException e) {
			fail("wait interrupted - don't know if test will succeed");
		}
		
		// Verify it was done
		assertTrue(beat.gotit);
		
		// Cleanup
		timer.remove(beat);			
	}

	/**
	 * Test cases for the remove() method
	 */
	public void testRemove() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat = new TestHeartBeat();
		
		// Add the beat (with a delay of 20 seconds to prevent execution
		timer.schedule(beat, 20000);
		
		// Validate the remove
		assertTrue(timer.remove(beat));
		assertFalse(timer.remove(beat));
		
		// Null test
		try {
			timer.remove(null);
			fail("Should have thrown a NullPointerException");
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Test cases for the removeAll() method
	 */
	public void testRemoveAll() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat (with a delay of 20 seconds to prevent execution
		timer.schedule(beat1, 20000);
		timer.schedule(beat2, 20000);
		
		// Remove all
		List list = timer.removeAll();
		
		// Validate the remove
		assertEquals(list.size(), 2);
		assertEquals(timer.getHeartBeats().size(), 0);
		assertTrue(list.contains(beat1));
		assertTrue(list.contains(beat2));
		
	}

	/**
	 * Test cases for the contains() method
	 */
	public void testContains() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat (with a delay of 20 seconds to prevent execution
		timer.schedule(beat1, 20000);
		
		// Validate the contains
		assertTrue(timer.contains(beat1));
		assertFalse(timer.contains(beat2));
		
		// Remove beat1 to cleanup
		timer.remove(beat1);
		
		// Null test
		try {
			timer.contains(null);
			fail("Should have thrown a NullPointerException");
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Test cases for the getHeartBeats() method
	 */
	public void testGetHeartBeats() {
		// Setup the test
		TimerTrigger timer = new TimerTrigger();
		TestHeartBeat beat1 = new TestHeartBeat();
		TestHeartBeat beat2 = new TestHeartBeat();
		
		// Add the beat (with a delay of 20 seconds to prevent execution
		timer.schedule(beat1, 20000);
		timer.schedule(beat2, 20000);
		
		// Get the heart beats
		List list = timer.getHeartBeats();
		
		// Validate the remove
		assertEquals(list.size(), 2);
		assertTrue(list.contains(beat1));
		assertTrue(list.contains(beat2));
		
		// Remove the beats to cleanup
		timer.remove(beat1);
		timer.remove(beat2);
		
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
