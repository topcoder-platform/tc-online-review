/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.producers.DataSourceConnectionProducer;

import junit.framework.TestCase;

import javax.sql.DataSource;


/**
 * <p>
 * Accuracy test for DataSourceConnectionProducer class.
 * </p>
 *
 * @author cosherx
 * @version 1.0
 */
public class DataSourceConnectionProducerAccuracyTest extends TestCase {
    /**
     * <p>
     * <code>DataSourceConnectionProducer</code> instance used in test.
     * </p>
     */
    private DataSourceConnectionProducer producer;

    /**
     * <p>
     * <code>DataSourceConnectionProducer</code> instance used in test.
     * </p>
     */
    private DataSource datasource = AccuracyTestHelper.getDataSource();

    /**
     * <p>
     * Test accuracy of constructor <code>DataSourceConnectionProducer(DataSource)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateDataSource1() throws Exception {
        producer = new DataSourceConnectionProducer(datasource);

        // Verify that data source is correct
        assertEquals("Data source should be identical", datasource, producer.getDataSource());
        assertNotNull("Failed to create connection from underlying datasource", producer.createConnection());
    }

    /**
     * <p>
     * Test accuracy of constructor <code>DataSourceConnectionProducer(DataSource, username, password)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testCreateDataSource2() throws Exception {
        producer = new DataSourceConnectionProducer(datasource, "user", "password");

        // Verify that data source is correct
        assertEquals("Data source should be identical", datasource, producer.getDataSource());
        assertNotNull("Failed to create connection from underlying datasource", producer.createConnection());
    }
}
