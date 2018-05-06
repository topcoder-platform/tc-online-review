package com.topcoder.util.heartbeat.functionaltests;

import java.io.*;
import java.net.*;
import com.topcoder.util.heartbeat.*;

import junit.framework.TestCase;

/**
 * Test cases for SocketHeartBeat
 */
public class SocketHeartBeatTest extends TestCase {

    /** The port communicates occurs on */
    public static int PORT1 = 55005;
    public static int PORT2 = 55006;
    
    /** Message */
    private static final byte[] MESSAGE = "message".getBytes();
    
    /**
     * Test cases for the constructor
     */
	public void testSocketHeartBeat() {
	    // Null tests
	    try {
	        new SocketHeartBeat(null, MESSAGE);
	        fail("Did not throw a NullPointerException for null socket");
	    } catch (NullPointerException e) {
	    }

	    try {
	        new SocketHeartBeat(createSocket(PORT1), null);
	        fail("Did not throw a NullPointerException for null message");
	    } catch (NullPointerException e) {
	    }
	    
	    // IllegalArg test
	    try {
	        new SocketHeartBeat(createSocket(PORT1), new byte[0]);
	        fail("Did not throw a IllegalArgumentException for zero length message");
	    } catch (IllegalArgumentException e) {
	    }
	    
	    // Normal Constructor
	    Socket socket = createSocket(PORT1);
	    SocketHeartBeat beat = new SocketHeartBeat(socket, MESSAGE);
	    
	    // Verify constructor
	    assertTrue(beat.getSocket()==socket);
	    assertTrue(beat.getMessage()==MESSAGE);
	}

    /**
     * Test cases for the getSocket() method
     */
	public void testGetSocket() {
	    // Setup the test
	    Socket socket = createSocket(PORT1);
	    SocketHeartBeat beat = new SocketHeartBeat(socket, MESSAGE);
	    
	    // Verify
	    assertTrue(beat.getSocket()==socket);
	}

    /**
     * Test cases for the getMessage() method
     */
	public void testGetMessage() {
	    // Setup the test
	    SocketHeartBeat beat = new SocketHeartBeat(createSocket(PORT1), MESSAGE);
	    
	    // Verify
	    assertTrue(beat.getMessage()==MESSAGE);
	}

    /**
     * Test cases for the getLastException() method
     */
	public void testGetLastException() {
	    // Setup the servers
	    startServer(PORT1, false);
	    startServer(PORT2, true);
	    
	    // Setup the test
	    SocketHeartBeat goodbeat = new SocketHeartBeat(createSocket(PORT1), MESSAGE);
	    SocketHeartBeat badbeat = new SocketHeartBeat(createSocket(PORT2), MESSAGE);
	    
	    // Fire keepAlive
	    goodbeat.keepAlive();
	    badbeat.keepAlive();
	    
	    // Verify
	    assertNull(goodbeat.getLastException());
	    assertNotNull(badbeat.getLastException());
	}

    /**
     * Test cases for the keepAlive() method
     */
	public void testKeepAlive() {
	    // Setup the servers
	    TestServer server = startServer(PORT1, false);

	    // Setup the test
	    SocketHeartBeat beat = new SocketHeartBeat(createSocket(PORT1), MESSAGE);
	    
	    // Fire keepAlive
	    beat.keepAlive();
	    
	    // Validate
	    assertEquals(server.stream.toByteArray(), MESSAGE);
	}

    /**
     * Helper class to return the socket
     */
    private Socket createSocket(int port) {
        try {
            return new Socket("127.0.0.1", port);
        } catch (UnknownHostException e) {
            fail("127.0.0.1 is unknown - impossible");
        } catch (IOException e) {
            fail("Exception creating local socket : " + e);
        }
        return null;
    }

    /**
     * Helper class to start the socket server
     */
    private TestServer startServer(int port, boolean throwError) {
        TestServer testServer = new TestServer(port, throwError);
        testServer.start();
        return testServer;
    }
     
    /**
     * The test socket server (one connect server!)
     */
    private class TestServer extends Thread {
        private boolean throwError = false;
        private int port = 0;
        public ByteArrayOutputStream stream = new ByteArrayOutputStream();
        
        public TestServer(int port, boolean throwError) {
            this.port = port;
            this.throwError = throwError;
        }
        
        public void run() {
            ServerSocket server = null;
            Socket socket = null;
            try {
                // Establish listener on port
                server = new ServerSocket(port);
                
                // Listen
                socket = server.accept();
                
                // If we should throw error- immediately close
                if(throwError) {
                    socket.close();
                    return;
                }

                // Get the input stream
                InputStream in = socket.getInputStream();
                
                // Read until end
                while(true) {
                    int b = in.read();
                    if(b==-1) break;
                    stream.write(b);
                }
                
            } catch (Exception e) {
                fail("Error in test server: " + e);
                e.printStackTrace();
            } finally {
                try {
                    if(socket!=null) socket.close();
                    if(server!=null) server.close();
                } catch (Exception e) {}
            }
        }
    }
}
