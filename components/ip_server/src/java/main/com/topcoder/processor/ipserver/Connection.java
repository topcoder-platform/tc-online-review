/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import java.net.InetSocketAddress;
import java.net.Socket;


/**
 * <p>
 * The Connection class encapsulates information about a client connection on the server side. This class is only used
 * to provide the user with convenient information about the client connection.
 * </p>
 *
 * <p>
 * <em>Thread Safety: </em>this class has been made thread safe by being immutable.
 * </p>
 *
 * @author visualage, zsudraco
 * @version 1.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class Connection {
    /**
     * <p>
     * The unique id of the connection.
     * </p>
     */
    private final String id;

    /**
     * <p>
     * The IP server that created this connection. It is possible for one handler to be registered to more servers, so
     * the handler needs to know which server called it.
     * </p>
     */
    private final IPServer ipServer;

    /**
     * <p>
     * The socket for the connection (will be used to get the connection details from).
     * </p>
     */
    private final Socket socket;

    /**
     * <p>
     * Construct a new Connection with given id, ipServer, socket.
     * </p>
     *
     * @param id the id of the connection.
     * @param ipServer the IP server instance that creates the connection.
     * @param socket the socket for the connection.
     *
     * @throws NullPointerException if any argument is null.
     */
    Connection(String id, IPServer ipServer, Socket socket) {
        if (id == null) {
            throw new NullPointerException("connection id cannot be null.");
        }

        if (ipServer == null) {
            throw new NullPointerException("ipServer cannot be null.");
        }

        if (socket == null) {
            throw new NullPointerException("socket cannot be null.");
        }

        this.id = id;
        this.ipServer = ipServer;
        this.socket = socket;
    }

    /**
     * <p>
     * Gets the id of the connection.
     * </p>
     *
     * @return the id of the connection.
     */
    public String getId() {
        return id;
    }

    /**
     * <p>
     * Gets the IP server that created the connection.
     * </p>
     *
     * @return the IP server that created the connection.
     */
    public IPServer getIPServer() {
        return ipServer;
    }

    /**
     * <p>
     * Gets the socket for the connection (may be used for raw writing).
     * </p>
     *
     * @return the socket for the connection.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * <p>
     * Returns the client address (as name, www.topcoder.com).
     * </p>
     *
     * @return the client address.
     */
    public String getClientNameAddress() {
        return ((InetSocketAddress) this.socket.getRemoteSocketAddress()).getAddress().getHostName();
    }

    /**
     * <p>
     * Returns the client address (as IP, 192.168.0.1).
     * </p>
     *
     * @return the client IP address.
     */
    public String getClientIPAddress() {
        return ((InetSocketAddress) socket.getRemoteSocketAddress()).getAddress().getHostAddress();
    }

    /**
     * <p>
     * Returns the client port.
     * </p>
     *
     * @return the client port.
     */
    public int getClientPort() {
        return ((InetSocketAddress) socket.getRemoteSocketAddress()).getPort();
    }
}
