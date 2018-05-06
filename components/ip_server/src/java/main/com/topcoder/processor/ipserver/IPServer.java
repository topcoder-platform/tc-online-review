/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.util.generator.guid.Generator;
import com.topcoder.util.generator.guid.UUIDType;
import com.topcoder.util.generator.guid.UUIDUtility;

import com.topcoder.processor.ipserver.message.MessageFactory;
import com.topcoder.processor.ipserver.message.Message;
import com.topcoder.processor.ipserver.message.DefaultMessageFactory;
import com.topcoder.processor.ipserver.message.MessageSerializationException;

import java.io.IOException;

import java.net.InetSocketAddress;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * <p>
 * The IPServer class represents the server-side of this component.
 * </p>
 *
 * <p>
 * This class implements a non-blocking server that uses modern java.nio features for socket connection management
 * instead of the clasic multithreaded techniques. This allows far greater scalability because the socket accepting
 * and request reads are done using a single thread (a non java.nio implementation will use one plus number of
 * accepted connections threads).
 * </p>
 *
 * <p>
 * Note that this does not mean threads aren't used at all. The java.nio takes care that everything that is socket
 * related, including request reading, is done using one thread. But the actual request processing must be executed
 * multithreaded. The number of threads per handler can be configured at the Handler level.
 * </p>
 *
 * <p>
 * The server only takes care of reading requests and writing responses. The actual requests are handled by the Handler
 * subclasses. The server dispatches the requests to the handlers according to the handler configuration.
 * </p>
 *
 * <p>
 * The API exposed by this class allows server configuration (host, port, maximum number of connections), request
 * handler configuration and methods to start and stop the server.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by making access to the handlers map synchronized on
 * the map itself. The access to the started attribute is protecting using a lock on the IPServer instance. The
 * configuration methods run inside the lock for the started attribute.
 * </p>
 *
 * @author visualage, zsudraco, FireIce
 * @version 2.0.1
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPServer implements Runnable {
    /** It represents the timeout when selector to select the keys from the channel. */
    private static final int SELECT_TIMEOUT = 1000;

    /**
     * The address to bind to (useful for machines with more network adapters). Can be null, in which case the wildcard
     * address is used.
     */
    private String address = null;

    /** The port to listen to. */
    private int port = 0;

    /** Maximum connections that can be serviced. */
    private int maxConnections = 0;

    /**
     * <p>
     * The size of the TCP queue. If more connections arrive than IPServer is capable of accepting, they are queued at
     * the TCP level. backlog is the dimension of the queue. If it is full, all connections are rejected.
     * </p>
     *
     * <p>
     * The backlog argument must be a positive value greater than 0. If the value passed if equal or less than 0, then
     * the default value will be assumed.
     * </p>
     */
    private int backlog = 0;

    /**
     * Map with the handlers for the IP server.  Maps handlers ids (Strings) to handler instances (Handler) A HashMap
     * is recommended.
     */
    private Map handlers = null;

    /** Indicates whether the server is started or stopped. */
    private boolean started = false;

    /** The ServerSocketChannel used for this IPServer. */
    private ServerSocketChannel server = null;

    /** The Selector used for this IPServer. */
    private Selector selector = null;

    /**
     * Indicates whether the server should stop. Used to stop the server. The stop method sets this value to true and
     * the run loop will break at the first iteration.
     */
    private boolean shouldStop = false;

    /**
     * GUID generator that is used for connection id generation. The uniqueness is ensured without using any persistent
     * storage.
     */
    private Generator generator = null;

    /**
     * Lookup table that maps connection ids to socket channels. Useful in the sendResponse method to locate the socket
     * channel for a given connection id.
     */
    private Map connectionIdToChannel = null;

    /** Represents the last exception that occured in run. */
    private Exception lastException = null;

    /**
     * <p>Represents the message factory to create various types of messages. This message factory is shared by all
     * handlers belong to this server.</p>
     */
    private MessageFactory messageFactory = null;

    /**
     * Constructor that sets the basic configuration parameters. The backlog is the max num of ServerSocket can accept,
     * and MaxConnections is the max num client connection of IPServer can service.
     *
     * @param address the address to bind to (useful for machines with more network adapters); can be null, in which
     *        case the wildcard address is used
     * @param port the port to listen to.
     * @param maxConnections maxConnections the maximum number of connections (0 means unlimited)
     * @param backlog the backlog used for ServerSocket
     * @param namespace the namespace used to create message factory
     *
     * @throws NullPointerException if the namespace is null.
     * @throws IllegalArgumentException if port in not within 0..65535 or if the maximum number of connections is
     *         negative or namespace is an empty string.
     * @throws ConfigurationException if message factory cannot be created due to configuration error.
     */
    public IPServer(String address, int port, int maxConnections, int backlog, String namespace)
        throws ConfigurationException {
        // The port must be between 0 and 65535, inclusive.
        if ((port < 0) || (port > IOHelper.MAX_PORT)) {
            throw new IllegalArgumentException("The given port is not within 0..65535: " + port);
        }

        // max request cannot be negative
        if (maxConnections < 0) {
            throw new IllegalArgumentException("The given max connections cannot be negative.");
        }

        this.address = address;
        this.port = port;
        this.maxConnections = maxConnections;
        this.backlog = backlog;
        this.handlers = new HashMap();
        this.connectionIdToChannel = new HashMap();
        this.generator = UUIDUtility.getGenerator(UUIDType.TYPEINT32);
        // The constructor of DefaultMessageFactory will validate the namespace and throw ConfigurationException
        // due to configuration error.
        this.messageFactory = new DefaultMessageFactory(namespace);
    }

    /**
     * <p>
     * Gets the address the server binds to.
     * </p>
     *
     * @return returns the server address
     */
    public synchronized String getAddress() {
        return this.address;
    }

    /**
     * <p>
     * Sets the address the server binds to. Can only be called if not started.
     * </p>
     *
     * @param address the address to bind to (useful for machines with more network adapters); can be null, in which
     *        case the wildcard address is used
     *
     * @throws IllegalStateException if the server is started
     */
    public synchronized void setAddress(String address) {
        // you cann't reset address while server is started
        if (isStarted()) {
            throw new IllegalStateException("The server is started.");
        }

        this.address = address;
    }

    /**
     * <p>
     * Gets the listening port of the server.
     * </p>
     *
     * @return returns the listening port
     */
    public synchronized int getPort() {
        return this.port;
    }

    /**
     * <p>
     * Sets the listening port of the server. Can only be called if not started.
     * </p>
     *
     * @param port the port of the server
     *
     * @throws IllegalArgumentException if the port is not within 0..65535
     * @throws IllegalStateException if the server is started
     */
    public void setPort(int port) {
        // The port must be between 0 and 65535, inclusive.
        if ((port < 0) || (port > IOHelper.MAX_PORT)) {
            throw new IllegalArgumentException("The given port is not within 0..65535: " + port);
        }

        synchronized (this) {
            // you cann't reset port while server is started
            if (isStarted()) {
                throw new IllegalStateException("The server is started.");
            }

            this.port = port;
        }
    }

    /**
     * <p>
     * Gets the maximum number of connections.
     * </p>
     *
     * @return the maximum number of connections.
     */
    public synchronized int getMaxConnections() {
        return this.maxConnections;
    }

    /**
     * <p>
     * Sets the maximum number of connections.
     * </p>
     *
     * @param maxConnections maxConnections the maximum number of connections (0 means unlimited)
     *
     * @throws IllegalArgumentException if the number is negative
     * @throws IllegalStateException if the server is started
     */
    public void setMaxConnections(int maxConnections) {
        // max request cannot be negative
        if (maxConnections < 0) {
            throw new IllegalArgumentException("The given max connections cannot be negative.");
        }

        synchronized (this) {
            // you cann't reset maxConnections while server is started
            if (isStarted()) {
                throw new IllegalStateException("The server is started.");
            }

            this.maxConnections = maxConnections;
        }
    }

    /**
     * <p>
     * Adds a handler to the IP server. The handler is added to the map of handlers under the given id as key. If the
     * handler id is used (the key already exists) then no action is taken and false is returned.
     * </p>
     *
     * <p>
     * Can be called only if the server is not started.
     * </p>
     *
     * @param id the handler id
     * @param handler handler the handler
     *
     * @return whether the handler was added or not
     *
     * @throws NullPointerException if any argument is null
     * @throws IllegalStateException if the server is started
     */
    public boolean addHandler(String id, Handler handler) {
        if (id == null) {
            throw new NullPointerException("The given handler id cannot be null.");
        }

        if (handler == null) {
            throw new NullPointerException("The given handler cannot be null.");
        }

        synchronized (this) {
            if (isStarted()) {
                throw new IllegalStateException("The server is started.");
            }

            // the key already exists, then no action is taken and false is returned.
            if (this.handlers.containsKey(id)) {
                return false;
            }

            this.handlers.put(id, handler);
        }

        return true;
    }

    /**
     * <p>
     * Removes a handler from the IP server. The handler is removed from the map of handlers given its id. If the
     * handler does not exist (the key does not exist) then no action is performed and false is returned.
     * </p>
     *
     * <p>
     * Can be called only if the server is not started.
     * </p>
     *
     * @param id the id of the handler to remove
     *
     * @return whether the handler was removed or not
     *
     * @throws NullPointerException if the argument is null
     * @throws IllegalStateException if the server is started
     */
    public boolean removeHandler(String id) {
        if (id == null) {
            throw new NullPointerException("The given handler id cannot be null.");
        }

        synchronized (this) {
            if (isStarted()) {
                throw new IllegalStateException("The server is started.");
            }

            return this.handlers.remove(id) != null;
        }
    }

    /**
     * <p>
     * The clearHandlers method clears all the handlers from the IP server. It does so by clearing the map storing
     * them.
     * </p>
     *
     * <p>
     * Can be called only if the server is not started.
     * </p>
     *
     * @throws IllegalStateException if the server if started
     */
    public synchronized void clearHandlers() {
        if (isStarted()) {
            throw new IllegalStateException("The server is started.");
        }

        this.handlers.clear();
    }

    /**
     * <p>
     * Returns whether a handler id is registered in this IP server.
     * </p>
     *
     * @param id the id of the handler to be tested
     *
     * @return whether the handler id is registered or not
     *
     * @throws NullPointerException if the argument is null
     */
    public boolean containsHandler(String id) {
        if (id == null) {
            throw new NullPointerException("The given handler id cannot be null.");
        }

        synchronized (this) {
            return this.handlers.containsKey(id);
        }
    }

    /**
     * <p>
     * Returns the list of the handler ids contained in the IP server. It actually returns a set of keys contained in
     * the handler map. Note that a copy is returned, not the set obtained from Map.keySet, nor a unmodifiable set
     * (because it is backed by the inner key set that can change).
     * </p>
     *
     * @return the list of handler ids (list of Strings)
     */
    public synchronized Set getHandlerIds() {
        return new HashSet(this.handlers.keySet());
    }

    /**
     * <p>
     * Gets the handler that is registered in the IP server given its id. If the id does not exist, null is returned
     * </p>
     *
     * @param id the id of the handler
     *
     * @return the handler with the given id or null if there isn't an instance with that id
     *
     * @throws NullPointerException if the argument is null
     */
    public Handler getHandler(String id) {
        if (id == null) {
            throw new NullPointerException("The given handler id cannot be null.");
        }

        synchronized (this) {
            return (Handler) this.handlers.get(id);
        }
    }

    /**
     * <p>
     * Starts the server. This method sets up the server socket and starts a new thread in which the connection
     * accepting and request reading loop runs (implement in the run method).
     * </p>
     *
     * @throws IOException if an error occurs while setting up the server socket.
     * @throws IllegalStateException if the server is already started.
     */
    public synchronized void start() throws IOException {
        // this method only can be call while server is not started
        if (isStarted()) {
            throw new IllegalStateException("The server is started.");
        }

        // Create the server socket channel
        server = ServerSocketChannel.open();

        // Set non-blocking I/O
        server.configureBlocking(false);

        // Set listening port
        if (this.address != null) {
            server.socket().bind(new InetSocketAddress(address, port), this.backlog);
        } else {
            // wildcard address
            server.socket().bind(new InetSocketAddress(port), this.backlog);
        }

        // Create the selector for server
        selector = Selector.open();

        // register selector with server for OP_ACCEPT
        server.register(selector, SelectionKey.OP_ACCEPT);
        this.started = true;
        this.shouldStop = false;

        // Starts a thread in which the connection accepting and request reading loop runs
        new Thread(this).start();
    }

    /**
     * <p>
     * This method implements the run method from Runnable. This method is essentially a loop that runs while the
     * server is in shouldStop == false state. The loop uses java.nio to accept incoming connections and read requests
     * without using any additional threads.
     * </p>
     *
     * <p>
     * If the loop breaks (shouldStop is true), everything is closed.
     * </p>
     *
     * <p>
     * The only moment when threads kick in is after the socket interaction is over, when the requests are passed to
     * the handlers for processing.
     * </p>
     *
     * <p>
     * Note that any exception occuring in the handling processing (ProcessingException) will cause immediate
     * termination of the loop. Normally handlers will have to take care of exceptions on their own (typically using
     * logging). It is not normal to pass them to the server. The server interprets that as a fatal failure and stops.
     * getLastException can be used to see what happened.
     * </p>
     */
    public void run() {
        try {
            // This map is used to store clientChannel - Connection pair set,
            // store it while accepting a client socket, and retrieve it while reading client request.
            final Map channelToConnection = new HashMap();

            while (!shouldStop) {
                // waiting for events with timeout value
                if (selector.select(SELECT_TIMEOUT) <= 0) {
                    continue;
                }

                Iterator it = selector.selectedKeys().iterator();

                // process every selectionKey
                while (it.hasNext()) {
                    SelectionKey key = (SelectionKey) it.next();

                    // a client required a connection
                    if (key.isAcceptable()) {
                        synchronized (this) {
                            // reach the max connections limit
                            if ((this.maxConnections > 0)
                                    && (this.connectionIdToChannel.size() >= this.maxConnections)) {
                                continue;
                            }
                        }

                        it.remove();

                        // get client socket channel
                        SocketChannel client = server.accept();

                        // Set Non-Blocking mode for select operation
                        client.configureBlocking(false);

                        // recording to the selector (reading)
                        client.register(selector, SelectionKey.OP_READ);

                        Connection connection =
                            new Connection(generator.getNextUUID().toString(), this, client.socket());

                        synchronized (this) {
                            connectionIdToChannel.put(connection.getId(), client);
                        }

                        channelToConnection.put(client, connection);

                        // Notify all handlers to process this connection
                        for (Iterator connIter = this.handlers.values().iterator(); connIter.hasNext();) {
                            ((Handler) connIter.next()).onConnect(connection);
                        }
                    } else if (key.isReadable()) {
                        // if isReadable = true then the server is ready to read
                        it.remove();
                        final SocketChannel client = (SocketChannel) key.channel();

                        try {
                            Message request = null;
                            while ((request = IOHelper.readMessage(client, messageFactory)) != null) {
                                Handler handler = getHandler(request.getHandlerId());

                                // If no handler register for the given request, simply ignore the request.
                                if (handler != null) {
                                    // Handler exists for the request.
                                    handler.handleRequest((Connection) channelToConnection.get(client), request);
                                }
                            }
                        } catch (IOException e) {
                            this.setLastException(e);
                            // Failed to read from client, so close the corresponding connection
                            key.cancel();
                            Connection con = (Connection) channelToConnection.remove(client);
                            this.closeConnection(con.getId());
                        }
                    }
                }
            }
        } catch (ProcessingException e) {
            this.setLastException(e);
        } catch (IOException e) {
            this.setLastException(e);
        } finally {
            synchronized (this) {
                this.started = false;
                this.shouldStop = false;

                // close everything
                try {
                    this.selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    this.server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                this.selector = null;
                this.server = null;
                this.connectionIdToChannel.clear();
            }
        }
    }

    /**
     * <p>
     * Stops the server.
     * </p>
     *
     * <p>
     * It simply sets the shouldStop flag to true. The loop from the run method will not break immediately. It will
     * after select times out. All requests started until the loop break will be serviced.
     * </p>
     *
     * @throws IllegalStateException if server is not started
     */
    public synchronized void stop() {
        if (!isStarted()) {
            throw new IllegalStateException("The server is stopped.");
        }

        // set shouldStop flag to true, The loop from the run method will not break immediately.
        // All requests started until the loop break will be serviced.
        this.shouldStop = true;
    }

    /**
     * <p>
     * Returns whether the served is started or not (the value of the started attribute).
     * </p>
     *
     * @return true if started, false if stopped
     */
    public synchronized boolean isStarted() {
        return this.started;
    }

    /**
     * <p>
     * Gets the last exception that occured in run.
     * </p>
     *
     * <p>
     * Note that any exception occuring in the handling processing (ProcessingException) will cause immediate
     * termination of the loop. Normally handlers will have to take care of exceptions on their own (typically using
     * logging). It is not normal to pass them to the server. The server interprets that as a fatal failure and stops.
     * </p>
     *
     * @return the last exception that occured in run
     */
    public synchronized Exception getLastException() {
        return this.lastException;
    }

    /**
     * Set the lastException. Mostly will be invoking by handleRequest process when a serious error occurs.
     *
     * @param exception the last exception will be set
     */
    synchronized void setLastException(Exception exception) {
        this.lastException = exception;
    }

    /**
     * <p>
     * Sends a response through the connection with the given id. This method is called only by the Handler subclasses.
     * </p>
     *
     * @param connectionId connectionId the connection id
     * @param response response the response to write
     *
     * @throws NullPointerException if any argument is null
     * @throws IllegalStateException if the server is not started
     * @throws IOException if any error occurs while writing to the socket
     * @throws IllegalArgumentException if the connection id does not correspond to an existing connection
     * @throws MessageSerializationException if the message factory fails to serialize the response.
     */
    public void sendResponse(String connectionId, Message response)
        throws IOException, MessageSerializationException {
        if (connectionId == null) {
            throw new NullPointerException("The given connectionId cannot be null.");
        }

        if (response == null) {
            throw new NullPointerException("The given response cannot be null.");
        }

        // Try to get corresponding SocketChannel for the given connectionId
        SocketChannel client = null;

        synchronized (this) {
            if (!isStarted()) {
                throw new IllegalStateException("The server is not started.");
            }

            client = (SocketChannel) connectionIdToChannel.get(connectionId);
        }

        // No correspond connection for the given connection id
        if (client == null) {
            throw new IllegalArgumentException("The connection id is not correspond to an existing connection: "
                    + connectionId);
        }

        // Wraps a byte array into a buffer
        try {
            client.write(IOHelper.wrapMessage(response, messageFactory));
        } catch (IOException e) {
            // client is unavailable.
            closeConnection(connectionId);
            throw e;
        }
    }

    /**
     * This method will mostly be invoking by Handler while hand Request completely.
     *
     * @param connectionId The Id of connectino that will be closed.
     */
    public void closeConnection(String connectionId) {
        if (connectionId == null) {
            throw new NullPointerException("Parameter connectionId is null");
        }

        synchronized (this) {
            // if the connectionId exists in connectionIdToChannel map, remove it
            if (this.connectionIdToChannel.containsKey(connectionId)) {
                SocketChannel channel = (SocketChannel) this.connectionIdToChannel.get(connectionId);
                this.closeChannel(channel);
                this.connectionIdToChannel.remove(connectionId);
            }
        }
    }

    /**
     * <p>
     * Sets the backlog of the server. Can only be called if not started.
     * </p>
     *
     * @param backlog the backlog of the server
     *
     * @throws IllegalStateException if the server is started
     */
    public synchronized void setBacklog(int backlog) {
        // you cann't reset port while server is started
        if (isStarted()) {
            throw new IllegalStateException("The server is started.");
        }

        this.backlog = backlog;
    }

    /**
     * <p>
     * Gets the backlog of the server.
     * </p>
     *
     * @return returns the backlog
     */
    public synchronized int getBacklog() {
        return this.backlog;
    }

    /**
     * Close a socket channel.
     *
     * @param channel the channel to close
     */
    private void closeChannel(SocketChannel channel) {
        try {
            channel.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * <p>Gets the message factory instance shared on the server side.</p>
     *
     * @return the message factory used to create various types of messages.
     */
    public MessageFactory getMessageFactory() {
        return this.messageFactory;
    }
}
