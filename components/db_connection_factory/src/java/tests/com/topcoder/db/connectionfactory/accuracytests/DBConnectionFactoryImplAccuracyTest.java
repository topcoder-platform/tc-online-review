/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducer;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;

import com.topcoder.util.config.ConfigManager;

import junit.framework.TestCase;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * <p>
 * Accuracy test for DBConnectionFactoryImpl class.
 * </p>
 *
 * @author cosherx
 * @version 1.0
 */
public class DBConnectionFactoryImplAccuracyTest extends TestCase {
    /** The producer name for testing. */
    private final static String testName = "test name";

    /**
     * <p>
     * <code>DBConnectionFactoryImpl</code> instance used in this test.
     * </p>
     */
    private DBConnectionFactoryImpl factory;

    /** The ConnectionProducer for testing. */
    private ConnectionProducer producer = new DataSourceConnectionProducer(AccuracyTestHelper.getDataSource());

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    protected void setUp() throws Exception {
        factory = new DBConnectionFactoryImpl();
    }

    /**
     * <p>
     * Test the accuracy of the constructor <code>DBConnectionFactoryImpl</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor1() throws Exception {
        assertNotNull("Fails to instaniated the factory.", factory);
    }

    /**
     * <p>
     * Test the accuracy of the constructor <code>DBConnectionFactoryImpl</code> with namespace argument.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor2() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();

        try {
            if (cm.existsNamespace(AccuracyTestHelper.CONFIG_NAMESPACE)) {
                cm.removeNamespace(AccuracyTestHelper.CONFIG_NAMESPACE);
            }

            cm.add(AccuracyTestHelper.CONFIG_NAMESPACE, new File(AccuracyTestHelper.CONFIG_FILE).getAbsolutePath(), ConfigManager.CONFIG_XML_FORMAT);

            factory = new DBConnectionFactoryImpl(AccuracyTestHelper.CONFIG_NAMESPACE);

            // testify if all configuration had been loaded
            assertNotNull("Fails to instaniated the factory.", factory);
            assertNotNull("Fails to retrieve jdbc producer.", factory.get(AccuracyTestHelper.JDBC));
            assertNotNull("Fails to retrieve jndi producer.", factory.get(AccuracyTestHelper.JNDI));
            assertNotNull("Fails to retrieve reflecting producer.", factory.get(AccuracyTestHelper.REFLECTING));
            assertEquals("Fails to retrieve the default setting.", AccuracyTestHelper.REFLECTING, factory.getDefault());
        } finally {
            if (cm.existsNamespace(AccuracyTestHelper.CONFIG_NAMESPACE)) {
                cm.removeNamespace(AccuracyTestHelper.CONFIG_NAMESPACE);
            }
        }
    }

    /**
     * <p>
     * Test accuracy of <code>add(String, ConnectionProducer)</code>.
     * </p>
     */
    public void testAddAccuracy() {
        assertTrue("Failed to add a new ConnectionProducer", factory.add(testName, producer));
        assertEquals("Fails to add ConnectionProducer", producer, factory.get(testName));
        assertFalse("Failed to return false to add a existing ConnectionProducer", factory.add(testName, producer));
    }

    /**
     * <p>
     * Test accuracy of <code>clear()</code>.
     * </p>
     */
    public void testClearAccuracy() {
        factory.add(testName, producer);
        assertTrue("Fails to add producer", factory.listConnectionProducerNames().hasNext());
        factory.clear();

        // After clear
        assertFalse("Should be empty mapping after clear", factory.listConnectionProducerNames().hasNext());
        assertNull("Default name should be null after clear", factory.getDefault());
    }

    /**
     * <p>
     * Test accuracy of <code>contains(String)</code>.
     * </p>
     */
    public void testContainsAccuracy() {
        assertFalse("Should not contain any producer at the start.", factory.contains(testName));

        factory.add(testName, producer);

        String anotherName = "another name";
        factory.add(anotherName, producer);
        assertTrue("Should contain 'test name'", factory.contains(testName));
        assertTrue("Should contain 'another name'", factory.contains(anotherName));
    }

    /**
     * <p>
     * Test accuracy of <code>createConnection()</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateConnectionAccuracy() throws Exception {
        factory.add(testName, producer);
        assertNotNull("Failed to create connection", factory.createConnection(testName));

        // Set default
        factory.setDefault(testName);
        assertNotNull("Failed to create default connection.", factory.createConnection());
    }

    /**
     * <p>
     * Test accuracy of <code>get(String)</code>.
     * </p>
     *
     * @throws ClassNotFoundException to JUnit
     */
    public void testGetAccuracy() throws ClassNotFoundException {
        assertNull("Should return null while the factory which contains no producer", factory.get(testName));

        factory.add(testName, producer);
        assertEquals("Failed to retrieve the correct producer for 'test name'", producer, factory.get(testName));

        ConnectionProducer anotherProducer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(
                    AccuracyTestHelper.JDBC));
        String anotherName = "another name";
        factory.add(anotherName, anotherProducer);
        assertEquals("Failed to retrieve the correct producer for 'another name'", anotherProducer,
            factory.get(anotherName));
    }

    /**
     * <p>
     * Test accuracy of <code>listConnectionProducerNames()</code>.
     * </p>
     *
     * @throws ClassNotFoundException to JUnit
     */
    public void testListConnectionProducerNames() throws ClassNotFoundException {
        String anotherName = "another name";
        ConnectionProducer anotherProducer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(
                    AccuracyTestHelper.JDBC));
        factory.add(testName, producer);
        factory.add(anotherName, anotherProducer);

        // Set to be compared with
        Set set = new HashSet();
        set.add(testName);
        set.add(anotherName);

        Iterator iter = factory.listConnectionProducerNames();
        
        while (iter.hasNext()) {
            assertTrue("Should contains in the set", set.remove(iter.next()));
        }

        assertTrue("Should have all elements in set existing in the iterator", set.isEmpty());
    }

    /**
     * <p>
     * Test of <code>remove(String)</code>.
     * </p>
     *
     * @throws ClassNotFoundException to JUnit
     * @throws UnknownConnectionException
     */
    public void testRemove() throws ClassNotFoundException, UnknownConnectionException {
        assertNull("Fails to return null while remove a non-existing producer.", factory.remove(testName));

        String anotherName = "another name";
        ConnectionProducer anotherProducer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(
                    AccuracyTestHelper.JDBC));
        factory.add(testName, producer);
        factory.add(anotherName, anotherProducer);

        // Remove an existing producer
        assertEquals("The removed ConnectionProducer should be correct", producer, factory.remove(testName));

        // Remove the default name
        factory.setDefault(anotherName);
        assertEquals("The removed ConnectionProducer should be correct", anotherProducer, factory.remove(anotherName));
        assertNull("After removing the default name, default name should be null", factory.getDefault());
    }
    /**
     * <p>
     * Test accuracy of <code>setDefault(String)</code> and <code>getDefault</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testSetDefaultGetDefaultAccuracy() throws Exception {
        String anotherName = "another name";
        ConnectionProducer anotherProducer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(
                    AccuracyTestHelper.JDBC));
        factory.add(testName, producer);
        factory.add(anotherName, anotherProducer);

        assertNull("Default should be null at the start", factory.getDefault());

        // Set null is allowed here
        factory.setDefault(testName);
        assertEquals("Failed to get correct default name.", testName, factory.getDefault());

        // Set to another default name
        factory.setDefault(anotherName);
        assertEquals("Failed to get correct default name.", anotherName, factory.getDefault());

        // Set to null
        factory.setDefault(null);
        assertNull("Failed to get null after setting default to null.", factory.getDefault());
    }
}
