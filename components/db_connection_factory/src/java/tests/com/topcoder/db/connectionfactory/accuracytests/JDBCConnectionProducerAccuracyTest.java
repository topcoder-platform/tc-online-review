/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.producers.JDBCConnectionProducer;

import junit.framework.TestCase;

import java.util.Properties;


/**
 * <p>
 * Accuracy test for JDBCConnectionProducer class.
 * </p>
 *
 * @author cosherx
 * @version 1.0
 */
public class JDBCConnectionProducerAccuracyTest extends TestCase {
    /**
     * <p>
     * <code>JDBCConnectionProducer</code> instance used in test.
     * </p>
     */
    private JDBCConnectionProducer producer;

    /**
     * <p>
     * Test accuracy of constructor <code>JDBCConnectionProducer(Property)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor1() throws Exception {
        producer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(AccuracyTestHelper.JDBC));
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JDBCConnectionProducer(URL)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor2() throws Exception {
        producer = new JDBCConnectionProducer(AccuracyTestHelper.getDBUrl());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JDBCConnectionProducer(URL, Properties)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor3() throws Exception {
        producer = new JDBCConnectionProducer(AccuracyTestHelper.getDBUrl(), new Properties());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JDBCConnectionProducer(URL, User, Password)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor4() throws Exception {
        producer = new JDBCConnectionProducer(AccuracyTestHelper.getDBUrl(), AccuracyTestHelper.getDBUserName(),
                AccuracyTestHelper.getDBPassword());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of <code>createConnection()</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateConnection() throws Exception {
        producer = new JDBCConnectionProducer(AccuracyTestHelper.getProperty(AccuracyTestHelper.JDBC));
        assertNotNull("Failed to create instance with property.", producer);
        assertNotNull("Failed to create connection.", producer.createConnection());
    }
}
