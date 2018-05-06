package com.topcoder.util.heartbeat.functionaltests;

import java.net.MalformedURLException;
import java.net.URL;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Test Cases for URLHeartBeat class
 */
public class URLHeartBeatTest extends TestCase {

	/**
	 * Test cases for the constructor
	 */
	public void testURLHeartBeat() {
		// NullPointer
		try {
			new URLHeartBeat(null);
			fail("Did not throw a NullPointerException");
		} catch (NullPointerException e) {
		}
		
		// Normal
		URL url = createURL("http://127.0.0.1"); 
		URLHeartBeat beat = new URLHeartBeat(url);
		
		// Verify constructor
		assertNull(beat.getLastException());
		assertTrue(beat.getURL()==url);
		
	}

	/**
	 * Test cases for the getURL() method
	 */
	public void testGetURL() {
		// Normal
		URL url = createURL("http://127.0.0.1"); 
		URLHeartBeat beat = new URLHeartBeat(url);
		
		// Verify constructor
		assertTrue(beat.getURL()==url);
		
	}

	/**
	 * Test cases for the getLastException() method
	 */
	public void testGetLastException() {
		// Normal
		URL url = createURL("http://127.0.0.1/doesnotexist.html"); 
		URLHeartBeat beat = new URLHeartBeat(url);
		
		// Verify constructor
		assertNull(beat.getLastException());
		
		// Do the keep alive (which should create an error)
		beat.keepAlive();
		
		// Make sure we have the error
		assertNotNull(beat.getLastException());
	}

	/**
	 * Test cases for the keepAlive() method
	 */
	public void testKeepAlive() {
		// Normal
		URL url = createURL("http://www.topcoder.com"); // URL must exist and be reachable 
		URLHeartBeat beat = new URLHeartBeat(url);
		
		// Do the keep alive (should be fine)
		beat.keepAlive();
		
		// Make sure we did not get an error
		assertNull(beat.getLastException());
	}

	/**
	 * Helper method to setup a URL
	 */
	private URL createURL(String urlString) {
		try {
			return new URL(urlString);
		} catch (MalformedURLException e) {
			fail("Cannot setup test url");
			return null;
		}
	}
}
