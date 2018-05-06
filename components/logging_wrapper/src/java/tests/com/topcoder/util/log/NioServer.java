/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.util.log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;

import com.topcoder.util.format.ObjectFormatMethod;
import com.topcoder.util.log.jdk14.Jdk14LogFactory;

/**
 * <p>
 * This class is an example of the usage of the Logging Wrapper component in a NIO Server.
 * </p>
 *
 * <p>
 * This example demonstrates the setup and API usage of the component.
 * </p>
 *
 * <p>
 * This class will be used in DemoTests.
 * </p>
 *
 *
 * @author TCSDEVELOPER
 * @version 2.0
 */
public class NioServer implements Runnable {

    /**
     * <p>
     * Represents the InetAddress with host:port combination to listen on.
     * </p>
     */
    private InetAddress hostAddress;

    /**
     * <p>
     * Represents the port to listen on.
     * </p>
     */
    private int port;

    /**
     * <p>
     * Represents the channel on which we'll accept connections.
     * </p>
     */
    private ServerSocketChannel serverChannel;

    /**
     * <p>
     * Represents the selector we'll be monitoring.
     * </p>
     */
    private Selector selector;

    /**
     * <p>
     * Represents the logger we’ll be using.
     * </p>
     */
    private Log log = LogManager.getLog("nioserver");

    /**
     * <p>
     * Constructs a NioServer.
     * </p>
     *
     * @param args the arguments to create a NioServer
     */
    public NioServer(String[] args) {
        try {
            // parse arguments and get the initial selector
            this.hostAddress = InetAddress.getByName(args[0]);
            this.port = Integer.parseInt(args[1]);
            this.selector = this.initSelector();
        } catch (UnknownHostException e) {
            // log error with exception stack trace
            log.log(Level.ERROR, e, "Invalid host name: {0}", args[0]);
        } catch (NumberFormatException e) {
            // log error the number parsing error
            log.log(Level.ERROR, "Specified port ‘{0}’ was not a valid number", args[1]);
        } catch (IOException e) {
            // log the IO error
            log.log(Level.ERROR, e, "IOException occurred initializing server");
        }
    }

    /**
     * <p>
     * Creates and initializes the selector.
     * </p>
     *
     * @return the selector which has been created and initialized
     *
     * @throws IOException if fail to create and initialize the selector
     */
    private Selector initSelector() throws IOException {
        // create a new selector
        Selector socketSelector = SelectorProvider.provider().openSelector();

        // create a new non-blocking server socket channel
        this.serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);

        // bind the server socket to the specified address and port
        InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.port);
        serverChannel.socket().bind(isa);

        // register the server socket channel, indicating an interest in
        // accepting new connections
        serverChannel.register(socketSelector, SelectionKey.OP_ACCEPT);

        return socketSelector;
    }

    /**
     * <p>
     * The entry point method of this thread.
     * </p>
     */
    public void run() {
        log.log(Level.INFO, "Server started for {0} listening to port {1}", hostAddress, new Integer(port));

        while (true) {
            try {
                // wait for an event one of the registered channels
                this.selector.select();
                // iterate over the set of keys for which events are available
                Iterator selectedKeys = this.selector.selectedKeys().iterator();
                while (selectedKeys.hasNext()) {
                    SelectionKey key = (SelectionKey) selectedKeys.next();

                    // log the selection event
                    log.log(Level.DEBUG, key);

                    selectedKeys.remove();
                    if (!key.isValid()) {
                        continue;
                    }

                    // check what event is available and deal with it
                    if (key.isAcceptable()) {
                        this.accept(key);
                    }
                }
            } catch (IOException e) {
                // log the exception and break
                log.log(Level.ERROR, e, "Server encountered an exception during selector processing");
                break;
            }
        }
        // log server stopping message
        log.log(Level.INFO, "Server stopped");
    }

    /**
     * <p>
     * Accepts a connections.
     * </p>
     *
     * @param key the selectionKey representing the registration of a SelectableChannel with a Selector.
     */
    public void accept(SelectionKey key) {
        // ... application processing code
    }

    /**
     * <p>
     * The Main entry point.
     * </p>
     *
     * @param args the arguments used for Main entry point
     */
    public static void main(String[] args) {
        // set logging to use the Java Logging API
        LogManager.setLogFactory(new Jdk14LogFactory());

        // create an object formatter for a selection key
        LogManager.getObjectFormatter().setFormatMethodForClass(
                SelectionKey.class,
                new ObjectFormatMethod() {
                    public String format(Object o) {
                        SelectionKey k = (SelectionKey) o;
                        return "InterestOps: " + k.interestOps() + ", ReadyOps: " + k.readyOps();
                    }
                },
                true);
        // start the server
        new Thread(new NioServer(args)).start();
    }
}