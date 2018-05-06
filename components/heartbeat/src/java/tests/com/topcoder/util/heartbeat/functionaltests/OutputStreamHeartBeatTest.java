package com.topcoder.util.heartbeat.functionaltests;

import java.io.*;
import java.util.*;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Test cases for the OutputStreamHeartBeat class
 */
public class OutputStreamHeartBeatTest extends TestCase {

    /** Message */
    private static final byte[] MESSAGE = "message".getBytes();
    
    /**
     * Test cases for the constructor
     */
	public void testOutputStreamHeartBeat() {
	    // Null tests
	    try {
	        new OutputStreamHeartBeat(null, MESSAGE);
	        fail("Did not throw a NullPointerException for null stream");
	    } catch (NullPointerException e) {
	    }

	    try {
	        new OutputStreamHeartBeat(new MyTester(), null);
	        fail("Did not throw a NullPointerException for null message");
	    } catch (NullPointerException e) {
	    }
	    
	    // Normal Constructor
	    MyTester tester = new MyTester();
	    OutputStreamHeartBeat output = new OutputStreamHeartBeat(tester, MESSAGE);
	    
	    // Verify constructor
	    assertTrue(output.getOutputStream()==tester);
	    assertTrue(output.getMessage()==MESSAGE);
	}

    /**
     * Test cases for the getOutputStream() method
     */
	public void testGetOutputStream() {
	    // Setup the test
	    MyTester tester = new MyTester();
	    OutputStreamHeartBeat output = new OutputStreamHeartBeat(tester, MESSAGE);
	    
	    // Verify
	    assertTrue(output.getOutputStream()==tester);
	}

    /**
     * Test cases for the getMessage() method
     */
	public void testGetMessage() {
	    // Setup the test
	    OutputStreamHeartBeat output = new OutputStreamHeartBeat(new MyTester(), MESSAGE);
	    
	    // Verify
	    assertTrue(output.getMessage()==MESSAGE);
	}

    /**
     * Test cases for the getLastException() method
     */
	public void testGetLastException() {
	    // Setup the test
	    MyTester goodtester = new MyTester();
	    MyTester badtester = new MyTester(true);
	    OutputStreamHeartBeat goodbeat = new OutputStreamHeartBeat(goodtester, MESSAGE);
	    OutputStreamHeartBeat badbeat = new OutputStreamHeartBeat(badtester, MESSAGE);
	    
	    // Fire keep alive
	    goodbeat.keepAlive();
	    badbeat.keepAlive();
	    
	    // Validate
	    assertNull(goodbeat.getLastException());
	    assertNotNull(badbeat.getLastException());
	}

    /**
     * Test cases for the keepAlive() method
     */
	public void testKeepAlive() {
	    // Setup the test
	    MyTester tester = new MyTester();
	    OutputStreamHeartBeat beat = new OutputStreamHeartBeat(tester, MESSAGE);
	    
	    // Fire keep alive
	    beat.keepAlive();
	    
	    // Validate
	    assertTrue(Arrays.equals(tester.toByteArray(), MESSAGE));
	}

    /**
     * Test stream
     */
    private class MyTester extends OutputStream {
        private ByteArrayOutputStream stream = new ByteArrayOutputStream();
        public boolean throwError = false;
        public MyTester() { this(false); }
        public MyTester(boolean throwError) { 
            this.throwError = throwError; 
        }
        
        public void close() throws IOException { stream.close(); }
        public void flush() throws IOException { stream.flush(); }
        
        public byte[] toByteArray() { 
            try {
                ByteArrayInputStream in = new ByteArrayInputStream(stream.toByteArray());
                ObjectInputStream oin = new ObjectInputStream(in);
                return (byte[])oin.readObject();
            } catch (Exception e) {
                e.printStackTrace();
                // Return 0 length array to force a no match
                return new byte[0];
            }
        }
        
        public void write(int b) throws IOException {
            if(throwError) throw new IOException("error");
            stream.write(b);
        }

        public void write(byte[] b) throws IOException {
            if(throwError) throw new IOException("error");
            stream.write(b);
        }

        public void write(byte[] b, int off, int len) throws IOException {
            if(throwError) throw new IOException("error");
            stream.write(b, off, len);
        }
    }
}
