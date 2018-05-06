/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver.accuracytests;

import com.topcoder.processor.ipserver.Handler;
import com.topcoder.processor.ipserver.IPServer;
import com.topcoder.processor.ipserver.IPServerManager;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;

import java.util.Set;
import java.util.Iterator;


/**
 * Accuracy test of the IPServerManager class.
 *
 * @author air2cold, WishingBone
 * @version 2.0
 */
public class IPServerManagerAccuracyTest extends TestCase {
    /**
     * The namespace to load the config file.
     */
    public static final String NAMESPACE = "com.topcoder.processor.ipserver";

    /**
     * The namespace for the MessageFactory.
     */
    public static final String MESSAGE_FACTORY_NAMESPACE = "com.topcoder.processor.ipserver.message";

    /**
     * The configuration file for accuracy test.
     */
    private static final String CONFIG_FILE = "accuracytests/accuracy.properties";

    /**
     * The IPServerManager instance to test.
     */
    private static IPServerManager manager = null;

    /**
     * Create instances to test.
     */
    public void setUp() throws Exception {
        clearConfiguration();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add(NAMESPACE, CONFIG_FILE, ConfigManager.CONFIG_PROPERTIES_FORMAT);
        cm.add("accuracytests/MessageFactory.xml");

        if (manager == null) {
            Constructor constructor = IPServerManager.class
                .getDeclaredConstructor(new Class[0]);
            constructor.setAccessible(true);
            manager = (IPServerManager) constructor.newInstance(new Object[0]);
        }
    }

    /**
     * Unload all the configuration files.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        clearConfiguration();
    }

    /**
     * Remove all the config files from the Configuration Manager.
     *
     * @throws Exception to JUnit.
     */
    private void clearConfiguration() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        for (Iterator itr = cm.getAllNamespaces(); itr.hasNext();) {
            cm.removeNamespace((String) itr.next());
        }
    }

    /**
     * Test getIPServerNames method, all the ip servers configured should be
     * loaded.
     */
    public void testGetIPServerNames() {
        Set names = manager.getIPServerNames();

        assertEquals("The number of server names configured is incorrect.", 3,
            names.size());
        assertTrue("server1 is not loaded", names.contains("server1"));
        assertTrue("server2 is not loaded", names.contains("server2"));
        assertTrue("server3 is not loaded", names.contains("server3"));
    }

    /**
     * Test getIPServer method, all the ip servers configured should be loaded.
     */
    public void testGetIPServer() {
        //  check server1
        IPServer server1 = manager.getIPServer("server1");
        assertEquals("serve1's backlog is incorrect", 1000, server1.getBacklog());

        Set handlers1 = server1.getHandlerIds();
        assertEquals("The number of server1's handler is incorrect", 3,
            handlers1.size());

        Handler handler = server1.getHandler("handler1");
        assertEquals("The max requests of handler1 is incorrect", 30,
            handler.getMaxConnections());
        assertTrue("Handler1 is an MaxHandler instance",
            handler instanceof MaxHandler);

        // checker server3
        IPServer server3 = manager.getIPServer("server3");
        assertEquals("server3's port is incorrect", 12342, server3.getPort());
    }

    /**
     * Test add/remove ip server from the the manager.
     *
     * @throws Exception to JUnit.
     */
    public void testAddRemoveIPServer() throws Exception {
        manager.addIPServer("a1", new IPServer("127.0.0.1", 111, 20, 20, MESSAGE_FACTORY_NAMESPACE));
        assertTrue("The ipserver a1 is not added",
            manager.containsIPServer("a1"));
        manager.removeIPServer("a1");
        assertFalse("The ipserver a1 is not removed",
            manager.containsIPServer("a1"));
    }
}
