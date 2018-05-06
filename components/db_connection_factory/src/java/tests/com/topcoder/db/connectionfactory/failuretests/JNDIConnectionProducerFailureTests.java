/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.failuretests;

import java.util.Hashtable;

import javax.naming.NamingException;

import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducer;
import com.topcoder.util.config.Property;

import junit.framework.TestCase;

/**
 * Failure test cases for <code>JNDIConnectionProducer</code>.
 *
 * @author roma
 * @version 1.0
 */
public class JNDIConnectionProducerFailureTests extends TestCase {

    /**
     * Test <code>constructor(String)</code> with <code>null</code>.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithNull() throws Exception {
        try {
            new JNDIConnectionProducer((String) null);
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
            new JNDIConnectionProducer("");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String)</code> with non-exist name.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringWithNonExist() throws Exception {
        try {
            new JNDIConnectionProducer("throwNamingException");
            fail("Should throw NamingException.");
        } catch (NamingException e) {
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
            new JNDIConnectionProducer(null, "user", "pass");
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
            new JNDIConnectionProducer("", "user", "pass");
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, String, String)</code> with non-exist jndi name.
     *
     * @throws Exception should not throw
     */
    public void testConstructorStringStringStringWithNonExist() throws Exception {
        try {
            new JNDIConnectionProducer("throwNamingException", "user", "pass");
            fail("Should throw NamingException.");
        } catch (NamingException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, Hashtable)</code> with first <code>null</code>.
     * @throws Exception should not throw
     */
    public void testConstructorStringHashtableWithFirstNull() throws Exception {
        try {
            new JNDIConnectionProducer(null, new Hashtable());
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(String, Hashtable)</code> with first empty string.
     * @throws Exception should not throw
     */
    public void testConstructorStringHashtableWithFirstEmpty() throws Exception {
        try {
            new JNDIConnectionProducer("", new Hashtable());
            fail("Should throw IAE.");
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
            new JNDIConnectionProducer((Property) null);
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
            new JNDIConnectionProducer(new Property(""));
            fail("Should throw IAE.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
