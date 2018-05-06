/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.processor.ipserver;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.File;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * <p>
 * Unit test cases for IPServerManager. Test ipServers management methods (add IPServer, remove IPServer, get IPServer
 * with name, get all ipServer names, check if one ipServer name exists and so on)
 * </p>
 *
 * @author zsudraco
 * @version 2.0
 * @copyright Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
public class IPServerManagerTest extends TestCase {
    /** The Configuration file path for the manager. */
    private static final String CONFIG_FILE_PATH = "test_files/ipserver.properties";

    /** The ConfigHelper instance used for testing. */
    private ConfigHelper helper;

    /** The IPServerManager used for testing. */
    private IPServerManager manager = null;

    /** The IPServer used for testing. */
    private IPServer ipServer;

    /** The IPServerManager constructor used for private IPServerManager Constructor testing. */
    private Constructor constructor = null;

    /** The configManager used for tesing. */
    private final ConfigManager cm = ConfigManager.getInstance();

    /** The field used to reset the instance field of IPServerManager for testing. */
    private Field field = null;

    /** This ipServer instances set used to stop all start ipServer for other testing. */
    private Set ipServers = new HashSet();

    /**
     * <p>
     * setUp() routine.
     * </p>
     *
     * <p>
     * An IPServerManager instance is get for testing.
     * </p>
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        ConfigHelper.loadNamespaces();
        helper = ConfigHelper.getInstance();
        ipServer = helper.getIPServer();

        if (this.constructor == null) {
            try {
                this.constructor = IPServerManager.class.getDeclaredConstructor(new Class[0]);
                this.constructor.setAccessible(true);
                this.field = IPServerManager.class.getDeclaredField("instance");
                this.field.setAccessible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        setNamespaceWithConfigFile(CONFIG_FILE_PATH);
        this.manager = IPServerManager.getInstance();

        // Gathers all ipServer for stopping by tearDown method
        Iterator iter = this.manager.getIPServerNames().iterator();
        this.ipServers.clear();

        while (iter.hasNext()) {
            ipServers.add(manager.getIPServer((String) iter.next()));
        }
    }

    /**
     * Clear all testing results (test config file for IPServerManager namespace, started ipServer, instance field of
     * IPServerManager etc.
     * @throws Exception to JUnit
     */
    protected void tearDown() throws Exception {
        // Set instance of IPServerManager to null for other testing
        this.field.set(null, null);

        // Remove the testing namespace set in test method
        ConfigHelper.releaseNamespaces();

        if (cm.existsNamespace(IPServerManager.NAMESPACE)) {
            cm.removeNamespace(IPServerManager.NAMESPACE);
        }

        // Stop all test ipServer for other testing
        Iterator iter = ipServers.iterator();

        while (iter.hasNext()) {
            IPServer ipserver = (IPServer) iter.next();

            if (ipserver.isStarted()) {
                ConfigHelper.stopServer(ipserver);
            }
        }
    }

    /**
     * Test of constructor with config which miss server names properties, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorMissingServerNames() throws Exception {
        setNamespaceWithConfigFile("test_files/missing_servername.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which miss server names property.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException while missing server names property.");
            }
        }
    }

    /**
     * Test of constructor with config which miss message factory namespace properties, ConfigurationException
     * should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorMissingMessageFactoryNamespace()
        throws Exception {
        setNamespaceWithConfigFile("test_files/missing_message_factory_namespace.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which miss message factory namespace property.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrap ConfigurationException while missing message factory namespace property.");
            }
        }
    }

    /**
     * Test of constructor with config which invalid port, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidPort()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_port.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with invalid port.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with invalid port.");
            }
        }
    }

    /**
     * Test of constructor with config file which has out of range small port, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorOutOfRangeSmallPort()
        throws Exception {
        setNamespaceWithConfigFile("test_files/out_of_range_small_port.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with out of range small port.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with out of range small port.");
            }
        }
    }

    /**
     * Test of constructor with config file which has out of range large port, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorOutOfRangeLargePort()
        throws Exception {
        setNamespaceWithConfigFile("test_files/out_of_range_large_port.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with out of range large port.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with out of range large port.");
            }
        }
    }

    /**
     * Test of constructor with config file which has invalid server started, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidStarted()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_server_started.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with invalid server started.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with invalid server started.");
            }
        }
    }

    /**
     * Test of constructor with config file which has invalid maxconnections, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidMaxconnections()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_maxconnections.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with invalid maxconnections.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with invalid maxconnections.");
            }
        }
    }

    /**
     * Test of constructor with config file which has negative maxconnections, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorNegativeMaxconnections()
        throws Exception {
        setNamespaceWithConfigFile("test_files/negative_maxconnections.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with negative maxconnections.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with negative maxconnections.");
            }
        }
    }

    /**
     * Test of constructor with config file which has invalid backlog, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidBacklog()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_backlog.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with invalid backlog.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with invalid backlog.");
            }
        }
    }

    /**
     * Test of constructor with config file which missing handler, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorMissingHandler()
        throws Exception {
        setNamespaceWithConfigFile("test_files/missing_handler.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which missing handler.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file which missing handler.");
            }
        }
    }

    /**
     * Test of constructor with config file which missing handler class, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorMissingHandlerClass()
        throws Exception {
        setNamespaceWithConfigFile("test_files/missing_handler_class.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which missing handler class.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file which missing handler class.");
            }
        }
    }

    /**
     * Test of constructor with config file with invalid handler class name, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidHandlerClassName()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_handler_class_name.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which invalid handler class name.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file which invalid handler class name.");
            }
        }
    }

    /**
     * Test of constructor with config file which missing handler value, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorMissingHandlerValue()
        throws Exception {
        setNamespaceWithConfigFile("test_files/missing_handler_value.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file which missing handler value.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file which missing handler value.");
            }
        }
    }

    /**
     * Test of constructor with config file with invalid handler maxrequests, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorInvalidHandlerMaxRequests()
        throws Exception {
        setNamespaceWithConfigFile("test_files/invalid_handler_maxrequest.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with invalid handler maxrequests.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with invalid handler maxrequests.");
            }
        }
    }

    /**
     * Test of constructor with config file with negative handler maxrequests, ConfigurationException should be thrown.
     *
     * @throws Exception to JUnit
     */
    public void testConstructorNegativeHandlerMaxRequests()
        throws Exception {
        setNamespaceWithConfigFile("test_files/negative_handler_maxrequest.properties");

        try {
            this.constructor.newInstance(new Object[0]);
            fail("Fails to process config file with negative handler maxrequests.");
        } catch (InvocationTargetException e) {
            // The ConfigurationException should be wrapped by InvocationTargetException
            if (!(e.getCause() instanceof ConfigurationException)) {
                fail("Fails to wrapp ConfigurationException for config file with negative handler maxrequests.");
            }
        }
    }

    /**
     * Test that getInstance will return a non-null manager instance in setUp.
     *
     * @throws Exception to JUnit
     */
    public void testGetInstance() throws Exception {
        assertNotNull("Fails to get non-null instance that get in setUp method.", manager);

        assertEquals("Fails to get singleton instance for IPServerManager.", manager, IPServerManager.getInstance());
    }

    /**
     * Test of addIPServer will return true if add an non-existing ipServerName.
     */
    public void testAddIPServerNonExistingIPServerName() {
        String ipServerName = "ipServerName";

        // Make sure the test ipServerName does not exist
        this.manager.removeIPServer(ipServerName);

        assertTrue("Fails to add the ipServer.", this.manager.addIPServer(ipServerName, this.ipServer));
    }

    /**
     * Test of addIPServer will return false if adding an existing ipServerName, and don't change the existing
     * ipServer.
     */
    public void testAddIPServerExistingIPServerName() {
        String ipServerName = "ipServerName";

        // Make sure the test ipServerName exist in manager
        this.manager.addIPServer(ipServerName, this.ipServer);
        assertFalse("Fails to return false to add existing ipServer.",
            this.manager.addIPServer(ipServerName, this.ipServer));

        assertEquals("Fails to keep the existing ipServer.", this.ipServer, this.manager.getIPServer(ipServerName));
    }

    /**
     * Test of addIPServer with null name, NPE is expected.
     */
    public void testAddIPServerWithNullName() {
        try {
            this.manager.addIPServer(null, this.ipServer);
            fail("The given name is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of addIPServer with null ipServer, NPE is expected.
     */
    public void testAddIPServerWithNullIPServer() {
        try {
            this.manager.addIPServer("test ipserver", null);
            fail("The given ipServer is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of removeIPServer will return true if remove the existing ipServer with the given ipServerName.
     */
    public void testRemoveIPServerExistingIPServer() {
        String ipServerName = "ipServerName";

        // Make sure the ipServer exists
        this.manager.addIPServer(ipServerName, this.ipServer);
        assertTrue("Fails to remove the existing ipServer.", this.manager.removeIPServer(ipServerName));
    }

    /**
     * Test of removeIPServer will return false if remove the non-existing ipServer with the given ipServerName.
     */
    public void testRemoveIPServerNonExistingIPServer() {
        String ipServerName = "ipServerName";

        // Make sure the ipServer does not exist
        this.manager.removeIPServer(ipServerName);
        assertFalse("Fails to return false for removing the non-existing ipServer.",
            this.manager.removeIPServer(ipServerName));
    }

    /**
     * Test of removeIPServer with null name, NPE is expected.
     */
    public void testRemoveIPServerWithNullName() {
        try {
            this.manager.removeIPServer(null);
            fail("The given name is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of clearIPServers will clear all handlers from the manager, by testing the size of return ipServerNames set
     * is 0.
     */
    public void testClearIPServers() {
        // Add handlers for testing
        this.manager.addIPServer("ServerName1", this.ipServer);
        this.manager.addIPServer("ServerName2", this.ipServer);
        this.manager.addIPServer("ServerName3", this.ipServer);

        // clear all ipServers
        this.manager.clearIPServers();
        assertEquals("Fails to clear all ipServers from the test manager.", this.manager.getIPServerNames().size(), 0);
    }

    /**
     * Test of containsIPServer will return true if the ipServer exist with the given ipServerName.
     */
    public void testContainsIPServerExistingIPServer() {
        String ipServerName = "Server name";

        // Make sure the test ipServer exists in manager
        this.manager.addIPServer(ipServerName, this.ipServer);
        assertTrue("Fails to return true for existing ipServer.", this.manager.containsIPServer(ipServerName));
    }

    /**
     * Test of containsIPServer will return false if the ipServer does not exist with the given ipServerName.
     */
    public void testContainsIPServerNonExistingIPServer() {
        String ipServerName = "Server name";

        // Make sure the test ipServer does not exist in manager
        this.manager.removeIPServer(ipServerName);
        assertFalse("Fails to return false for non-existing ipServer.", this.manager.containsIPServer(ipServerName));
    }

    /**
     * Test of containsIPServer with null name, NPE is expected.
     */
    public void testContainsIPServerWithNullName() {
        try {
            this.manager.containsIPServer(null);
            fail("The given name is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of getIPServerNames will return all ipServerNames of the manager, and the return set is a copy.
     */
    public void testGetIPServerNames() {
        String ipServerName1 = "Server 1";
        String ipServerName2 = "Server 2";
        String ipServerName3 = "Server 3";

        this.manager.addIPServer(ipServerName1, this.ipServer);
        this.manager.addIPServer(ipServerName2, this.ipServer);
        this.manager.addIPServer(ipServerName3, this.ipServer);

        // Get the ipServerNames set
        Set set = this.manager.getIPServerNames();
        assertTrue("Fails to contain ipServerName1.", set.contains(ipServerName1));
        assertTrue("Fails to contain ipServerName2.", set.contains(ipServerName2));
        assertTrue("Fails to contain ipServerName3.", set.contains(ipServerName3));
        set.clear();

        // clear the return set will not affect the ipServerNames of the manager
        Set set1 = this.manager.getIPServerNames();
        assertTrue("Fails to return a ipServerNames set copy.", set1.contains(ipServerName1));
        assertTrue("Fails to return a ipServerNames set copy.", set1.contains(ipServerName2));
        assertTrue("Fails to return a ipServerNames set copy.", set1.contains(ipServerName3));
    }

    /**
     * Test of getIPServer will return the IPServer with the given existing ipServer name.
     */
    public void testGetIPServerExistingIPServer() {
        String ipServerName = "server 1";

        // Make sure the test ipServer does not exist in manager for testing
        this.manager.removeIPServer(ipServerName);
        this.manager.addIPServer(ipServerName, this.ipServer);
        assertEquals("Fails to get ipServer.", this.ipServer, this.manager.getIPServer(ipServerName));
    }

    /**
     * Test of getIPServer will return null with the given non-existing ipServer name.
     */
    public void testGetIPServerNonExistingIPServer() {
        String ipServerName = "server 1";

        // Make sure the test ipServer does not exist in manager for testing
        this.manager.removeIPServer(ipServerName);
        assertNull("Fails to return null for non-existing ipServerName.", this.manager.getIPServer(ipServerName));
    }

    /**
     * Test of getIPServer with null name, NPE is expected.
     */
    public void testGetIPServerWithNullName() {
        try {
            this.manager.getIPServer(null);
            fail("The given name is null");
        } catch (NullPointerException npe) {
            // good
        }
    }

    /**
     * Test of IPServerManager getNamespace method, IPServerManager.NAMESPACE will be always returned.
     */
    public void testGetNamespace() {
        assertEquals("Fails to get correct namespace from manager.", IPServerManager.NAMESPACE, manager.getNamespace());
    }

    /**
     * Test of IPServerManager getConfigPropNames method, all properties for IPServerManager.NAMESPACE will be
     * returned.
     *
     * @throws Exception to JUnit.
     */
    public void testGetConfigPropNames() throws Exception {
        // Get propNames from the config manager.
        Enumeration propNames = cm.getPropertyNames(manager.getNamespace());
        List configPropNames = new ArrayList();

        while (propNames.hasMoreElements()) {
            configPropNames.add(propNames.nextElement());
        }

        int size = 0;

        // Get propNames from IPServerManager.
        propNames = manager.getConfigPropNames();

        while (propNames.hasMoreElements()) {
            Object elem = propNames.nextElement();
            assertTrue("Fails to get valid propName from IPServerManager: " + elem, configPropNames.contains(elem));
            size++;
        }

        assertEquals("Fails to match the propName size with config file.", size, configPropNames.size());
    }

    /**
     * Set the new namespace of IPServerManager with the given config file name.
     *
     * @param fileName The config file name will be set
     *
     * @throws Exception to JUnit
     */
    private void setNamespaceWithConfigFile(String fileName)
        throws Exception {
        if (cm.existsNamespace(IPServerManager.NAMESPACE)) {
            // Remove old namespace for IPServerManager
            cm.removeNamespace(IPServerManager.NAMESPACE);
        }

        cm.add(IPServerManager.NAMESPACE, new File(fileName).getAbsolutePath(), ConfigManager.CONFIG_PROPERTIES_FORMAT);
    }
}
