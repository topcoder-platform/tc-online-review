/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.io.IOException;

import java.util.LinkedList;
import java.util.List;

import com.topcoder.processor.ipserver.message.Message;

/**
 * <p>
 * The handler class abstracts the server side notification and request processing API. The notifications cover
 * successful connection and request arrival. The request arrival is not only a notification method because the
 * implementations are expected to respond to it.
 * </p>
 *
 * <p>
 * This class also defines a maximum connection number, indicating how many requests can be handled simultaneously. The
 * meaning of this number is that at most that many threads can be executing the onRequest method at one time. If more
 * requests arrive, then they are queued until a thread is available to handle it.
 * </p>
 *
 * <p>
 * The users are expected to subclass this class and override some of or all three notification methods (onConnect,
 * onRequest). The default implementations of these methods do nothing (except argument validation)
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>The subclasses should implement onConnect and onRequest in a thread safe manner. The
 * handleRequest method is delicate as thread safety is regarded. This method implements essentially a waiting queue
 * for the onRequest method. It ensures that only the maximum number of threads can be inside the method at one time.
 * This waiting mechanism is implemented used wait and notify. The access to the counter (currentRequests) need to be
 * synchronized on the instance itself. Note that the test in the while and the incrementation must be executed
 * together, atomically.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public abstract class Handler {
    /**
     * <p>
     * The maximum number of requests that can be serviced by onRequest at one time. 0 means no limit.
     * </p>
     */
    private final int maxRequests;

    /**
     * <p>
     * The current number of requests being serviced by onRequest. This count attribute will be incremented when
     * entering the method and decremented afterwards.
     * </p>
     */
    private int currentRequests = 0;

    /**
     * <p>
     * The request queue. If requests arrive when the maximum number of threads are running older request, then the new
     * requests are queued here until a thread is available.
     * </p>
     *
     * <p>
     * Both the connection object and request object need to be queued. This element of this list use a 2 element
     * object array, the type are Connection, Message type.
     * </p>
     */
    private List requestQueue = null;

    /**
     * <p>
     * Constructor with maxRequests. Should be called by subclasses. If a handler does not wish to have maximum request
     * limitation it should simply pass 0 as argument.
     * </p>
     *
     * @param maxRequests maximum number of requests that can be serviced by onRequest at one time (0 means no limit)
     *
     * @throws IllegalArgumentException if the argument is less than 0.
     */
    protected Handler(int maxRequests) {
        if (maxRequests < 0) {
            throw new IllegalArgumentException("The given maxRequests cannot be less than 0.");
        }

        this.maxRequests = maxRequests;
        this.requestQueue = new LinkedList();
    }

    /**
     * <p>
     * Get the maximum number of requests that can be serviced by onRequest at one time (0 means no limit).
     * </p>
     *
     * @return the maximum number of requests that can be serviced by onRequest at one time (0 means no limit)
     */
    public synchronized int getMaxConnections() {
        return this.maxRequests;
    }

    /**
     * <p>
     * Method that is called by the IP server when a request arrives.
     * </p>
     *
     * @param connection the connection on which the request arrived
     * @param request the request message
     *
     * @throws NullPointerException if any argument is null
     * @throws IllegalStateException if the server is stopped, when trying to send a response or if the connection is
     *         closed
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     *         reported to the user by wrapping them in the response message, only fatal exceptions that should
     *         terminate the server should throw this exception)
     * @throws IOException if a socket exception occurs while sending the response to the client
     */
    void handleRequest(Connection connection, Message request)
        throws ProcessingException, IOException {
        if (connection == null) {
            throw new NullPointerException("The given connection cannot be null.");
        }

        if (request == null) {
            throw new NullPointerException("The given request cannot be null.");
        }

        synchronized (this) {
            if (!connection.getIPServer().isStarted()) {
                throw new IllegalStateException("The server is not running.");
            }

            this.requestQueue.add(new Object[] {connection, request});

            // simple ignore if reach maxRequests
            if ((this.maxRequests > 0) && (this.currentRequests >= this.maxRequests)) {
                return;
            }
        }

        Thread newThread = new Thread() {
                public void run() {
                    processRequest();
                }
            };
        newThread.start();
    }

    /**
     * Process requests from requests queue one by one, until the queue is empty.
     *
     */
    void processRequest() {
        synchronized (requestQueue) {
            // one request will be processed
            currentRequests++;
        }

        Connection connection = null;

        try {
            while (true) {
                Object[] objects = null;

                synchronized (requestQueue) {
                    if (requestQueue.isEmpty()) {
                        return;
                    }

                    objects = (Object[]) requestQueue.remove(0);
                }

                connection = (Connection) objects[0];

                Message request = (Message) objects[1];
                onRequest(connection, request);
            }
        } catch (ProcessingException e) {
            // It is a serious exception, so we will stop the server and record the exception
            IPServer server = connection.getIPServer();
            server.setLastException(e);
            server.stop();
        } catch (IOException e) {
            // Basically, handler should handle this kind of exception silently, just record it
            connection.getIPServer().setLastException(e);
            connection.getIPServer().closeConnection(connection.getId());
        } finally {
            synchronized (requestQueue) {
                // one process thread exit
                currentRequests--;
            }
        }
    }

    /**
     * <p>
     * Notification method that is called when a connection is established with a client.
     * </p>
     *
     * @param connection the new connection
     *
     * @throws NullPointerException if the argument is null
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     *         reported to the user by wrapping them in the response message, only fatal exceptions that should
     *         terminate the server should throw this exception)
     */
    protected void onConnect(Connection connection) throws ProcessingException {
        if (connection == null) {
            throw new NullPointerException("The given connection cannot be null.");
        }
    }

    /**
     * <p>
     * Notification method that is called when a new client request arrives. The subclasses should implement this
     * method. They should do whatever processing is required and then use connection.getIPServer().sendResponse (zero
     * to many times as needed) to send the response back to the client.
     * </p>
     *
     * @param connection the connection on which the request arrived
     * @param request the request message
     *
     * @throws NullPointerException if any argument is null
     * @throws IllegalStateException if the server is stopped, when trying to send a response or if the connection is
     *         closed
     * @throws ProcessingException wraps a fatal application specific exception (note that normal exception should be
     *         reported to the user by wrapping them in the response message, only fatal exceptions that should
     *         terminate the server should throw this exception)
     * @throws IOException if a socket exception occurs while sending the response to the client
     */
    protected void onRequest(Connection connection, Message request)
        throws ProcessingException, IOException {
        if (connection == null) {
            throw new NullPointerException("The given connection cannot be null.");
        }

        if (request == null) {
            throw new NullPointerException("The given request cannot be null.");
        }

        if (!connection.getIPServer().isStarted()) {
            throw new IllegalStateException("The server is not running.");
        }

        // Do nothing else than argument validation in this default implementation
    }
}
