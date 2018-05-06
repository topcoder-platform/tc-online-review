/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.producers.ReflectingConnectionProducer;

import junit.framework.TestCase;

/**
 * <p>Accuracy test for ReflectingConnectionProducer class.</p>
 *
 * @author cosherx
 * @version 1.0
 */
public class ReflectingConnectionProducerAccuracyTest extends TestCase {
    /**
     * <p>
     * <code>ReflectingConnectionProducer</code> instance used in test.
     * </p>
     */
    private ReflectingConnectionProducer producer;

    /**
     * <p>
     * Test accuracy of constructor <code>ReflectingConnectionProducer(Property)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor1() throws Exception {
        producer = new ReflectingConnectionProducer(AccuracyTestHelper.getProperty(AccuracyTestHelper.REFLECTING));
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>ReflectingConnectionProducer(ClassName)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor2() throws Exception {
        producer = new ReflectingConnectionProducer(AccuracyTestHelper.getReflectingClass());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>ReflectingConnectionProducer(ClassName, User, Password)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor4() throws Exception {
        producer = new ReflectingConnectionProducer(AccuracyTestHelper.getReflectingClass(), AccuracyTestHelper.getDBUserName(),
                AccuracyTestHelper.getDBPassword());
        assertNotNull("Failed to create instance with property.", producer);
    }
}
