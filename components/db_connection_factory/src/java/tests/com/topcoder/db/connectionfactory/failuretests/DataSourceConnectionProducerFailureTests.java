/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.failuretests;

import javax.sql.DataSource;

import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducer;

import junit.framework.TestCase;

/**
 * Failure test cases for <code>DataSourceConnectionProducer</code>.
 *
 * @author roma
 * @version 1.0
 */
public class DataSourceConnectionProducerFailureTests extends TestCase {

    /**
     * Test <code>constructor(DataSource)</code> with <code>null</code>.
     */
    public void testConstructorWithNull() {
        try {
            new DataSourceConnectionProducer((DataSource) null);
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }

    /**
     * Test <code>constructor(DataSource, String, String)</code> with first <code>null</code>.
     */
    public void testConstructorWithFirstNull() {
        try {
            new DataSourceConnectionProducer((DataSource) null, "user", "pass");
            fail("Should throw IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // success
        }
    }
}
