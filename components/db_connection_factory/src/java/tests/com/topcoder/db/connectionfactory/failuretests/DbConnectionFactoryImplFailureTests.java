/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.failuretests;

import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.NoDefaultConnectionException;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;

import junit.framework.TestCase;

/**
 * Failure test cases for <code>DBConnectionFactoryImpl</code>.
 *
 * @author roma
 * @version 1.0
 */
public class DbConnectionFactoryImplFailureTests extends TestCase {
    /**
     * The folder which the config file is in.
     */
    private static final String CONFIG_DIR = "test_files/failure/";

    /**
     * The DBConnectionFactoryImpl instance is used to call its
     * methods and it will be initialized in setUp().
     */
    private DBConnectionFactoryImpl factory = null;

    /**
     * The ConnectionProducer instatnce is used to test
     * add method. It will be initialized in setUp().
     */
    private ConnectionProducer producer = null;


    /**
     * <p>Initialize test environment.</p>
     *
     * @throws Exception to JUnit
     */
    protected void setUp() throws Exception {
        // Initialize the DBConnectionFactoryImpl
        // and ConnectionProducer instance
        factory = new DBConnectionFactoryImpl();
        producer = new JDBCConnectionProducer("jdbc");
    }

    /**
     * Test <code>constructor</code> with <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstrustorStringWithNull() throws Exception {
        try {
            new DBConnectionFactoryImpl((String)null);
            fail("Should throw IllegalArgumentException for null namespace.");
        } catch (IllegalArgumentException iee) {
            // success
        }
    }

    /**
     * Test <code>constructor</code> with empty namespace.
     *
     * @throws Exception should not throw
     */
    public void testConstrustorStringWithEmpty() throws Exception {
        try {
            new DBConnectionFactoryImpl("");
            fail("Should throw IAE for empty string as namespace.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>createConnection</code> without default.
     *
     * @throws Exception should not throw
     */
    public void testCreateConnectionWihoutDefault() throws Exception {
        try {
            factory.createConnection();
            fail("Should throw NoDefualtConnectionException");
        } catch (NoDefaultConnectionException e) {
            // success
        }
    }

    /**
     * Test <code>createConnection(String)</code> with <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testCreateConnectionStringWithNull() throws Exception {
        try {
            factory.createConnection(null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
            // success
        }
    }

    /**
     * Test <code>createConnection(String)</code> with empty string.
     *
     * @throws Exception should not throw
     */
    public void testCreateConnectionStringWithEmpty() throws Exception {
        try {
            factory.createConnection("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>createConnection(String)</code> with unknown connection.
     *
     * @throws Exception should not throw
     */
    public void testCreateConnectionStringWithUnknownConnection() throws Exception {
        try {
            factory.createConnection("UnknownConnection");
            fail("Should throw UnknownConnectionException.");
        } catch (UnknownConnectionException e) {
            // success
        }
    }

    /**
     * Test <code>add</code> with <code>null</code> as name.
     */
    public void testAddFirstNull() {
        try {
            factory.add(null, producer);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException npe) {
            // success
        }
    }

    /**
     * Test <code>add</code> with <code>null</code> as producer.
     */
    public void testAddSecodnNull() {
        try {
            factory.add("name", null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * Test <code>add</code> with empty name.
     */
    public void testAddEmptyName() {
        try {
            factory.add("", producer);
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>remove</code> with <code>null</code> as name.
     */
    public void testRemoveWithNull() {
        try {
            factory.remove(null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * Test <code>remove</code> with empty name.
     */
    public void testRemoveEmptyName() {
        try {
            factory.remove("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>contains</code> with <code>null</code> as name.
     */
    public void testContainsWithNull() {
        try {
            factory.contains(null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * Test <code>remove</code> with empty name.
     */
    public void testContainsEmptyName() {
        try {
            factory.contains("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>setDefualt</code> with empty string.
     *
     * @throws Exception should not throw
     */
    public void testSetDefaultWithEmpty() throws Exception {
        try {
            factory.setDefault("");
            fail("Should throw IAE");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>setDefualt</code> with unknown.
     */
    public void testSetDefaultWithUnknown() {
        try {
            factory.setDefault("UnknownConnection");
            fail("Should throw 'UnknownConnectionException'");
        } catch (UnknownConnectionException e) {
            // success
        }
    }

    /**
     * Test <code>contains</code> with <code>null</code> as name.
     */
    public void testGetWithNull() {
        try {
            factory.contains(null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException iae) {
            // success
        }
    }

    /**
     * Test <code>remove</code> with empty name.
     */
    public void testGetEmptyName() {
        try {
            factory.contains("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
