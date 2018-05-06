/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.failuretests;

import com.topcoder.db.connectionfactory.producers.ReflectingConnectionProducer;
import com.topcoder.util.config.Property;

import junit.framework.TestCase;

/**
 * Failure test cases for <code>ReflectingConnectionProducer</code>.
 *
 * @author roma
 * @version 1.0
 */
public class ReflectingConnectionProducerFailureTests extends TestCase {

    /**
     * Mock DataSource.
     */
    private final String dataSourceClass = "com.topcoder.db.connectionfactory.failuretests.MockDataSource";

    /**
     * Test <code>constructor(String)</code> with <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithNull() throws Exception {
        try {
            new ReflectingConnectionProducer((String) null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with empty string.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithEmpty() throws Exception {
        try {
            new ReflectingConnectionProducer("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with non-exist class.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithNonExist() throws Exception {
        try {
            new ReflectingConnectionProducer("NonExist");
            fail("Should throw ClassNotFoundException.");
        } catch (ClassNotFoundException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with not bad class.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithBadClass() throws Exception {
        try {
            new ReflectingConnectionProducer("java.lang.String");
            fail("Should throw ClassCastException.");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with first <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithFirstNull() throws Exception {
        try {
            new ReflectingConnectionProducer(null, "user", "pass");
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with first empty string.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithFirstEmpty() throws Exception {
        try {
            new ReflectingConnectionProducer("", "user", "pass");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with non-exist class.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithNonExist() throws Exception {
        try {
            new ReflectingConnectionProducer("NonExist", "user", "pass");
            fail("Should throw ClassNotFoundException.");
        } catch (ClassNotFoundException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with not bad class.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithBadClass() throws Exception {
        try {
            new ReflectingConnectionProducer("java.lang.String", "user", "pass");
            fail("Should throw ClassCastException.");
        } catch (ClassCastException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with second <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithSecondNull() throws Exception {
        try {
            new ReflectingConnectionProducer(dataSourceClass, null, "pass");
            fail("Should thrwo IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with third <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithThirdNull() throws Exception {
        try {
            new ReflectingConnectionProducer(dataSourceClass, "user", null);
            fail("Should thrwo IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(Property)</code> with <code>null</code>.
     * @throws Exception should not throw
     */
    public void testConstructorPropertyWithNull() throws Exception {
        try {
            new ReflectingConnectionProducer((Property) null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(Property)</code> with empty.
     * @throws Exception should not throw
     */
    public void testConstructorPropertyWithEmpty() throws Exception {
        try {
            new ReflectingConnectionProducer(new Property(""));
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
