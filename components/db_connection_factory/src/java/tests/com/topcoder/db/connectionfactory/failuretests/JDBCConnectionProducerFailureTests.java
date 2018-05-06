/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.failuretests;

import java.util.Properties;

import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;
import com.topcoder.util.config.Property;

import junit.framework.TestCase;

/**
 * Failure test cases for <code>JDBCConnectionProducer</code>.
 *
 * @author roma
 * @version 1.0
 */
public class JDBCConnectionProducerFailureTests extends TestCase {

    /**
     * Test <code>constructor(String)</code> with null.
     */
    public void testConstructorStringWithNull() {
        try {
            new JDBCConnectionProducer((String) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with empty string.
     */
    public void testConstructorStringWithEmpty() {
        try {
            new JDBCConnectionProducer("");
            fail("Should throw IAE");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with first <code>null</code>.
     */
    public void testConstructorStringStringStringWithNull() {
        try {
            new JDBCConnectionProducer(null, "user", "pass");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with empty string.
     */
    public void testConstructorStringStringStringWithEmpty() {
        try {
            new JDBCConnectionProducer("", "user", "pass");
            fail("Should throw IAE");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with second <code>null</code>.
     */
    public void testConstructorStringStringStringWithSecondNull() {
        try {
            new JDBCConnectionProducer("jdbc", null, "pass");
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with third <code>null</code>.
     */
    public void testConstructorStringStringStringWithThirdNull() {
        try {
            new JDBCConnectionProducer("jdbc", "user", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, Properties)</code> with null.
     */
    public void testConstructorStringPropertiesWithNull() {
        try {
            new JDBCConnectionProducer((String) null, new Properties());
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, Properties)</code> with empty string.
     */
    public void testConstructorStringPropertiesWithEmpty() {
        try {
            new JDBCConnectionProducer("", new Properties());
            fail("Should throw IAE");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, Properties)</code> with second null.
     */
    public void testConstructorStringPropertiesWithSecondNull() {
        try {
            new JDBCConnectionProducer("jdbc", null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(Property)</code> with null.
     *
     * @throws Exception should not throw
     */
    public void testConstructorPropertyWithNull() throws Exception {
        try {
            new JDBCConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(Property)</code> with empty.
     *
     * @throws Exception should not throw
     */
    public void testConstructorPropertyWithEmpty() throws Exception {
        try {
            new JDBCConnectionProducer(new Property(""));
            fail("Should throw IAE");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    public void testCreateConnectionFail() {
        try {
            new JDBCConnectionProducer("jdbc").createConnection();
            fail("Should throw DBConnectionException");
        } catch (DBConnectionException e) {
            // success
        }
    }
}
