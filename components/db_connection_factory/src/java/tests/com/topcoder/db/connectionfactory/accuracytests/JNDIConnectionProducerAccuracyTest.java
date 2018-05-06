/*
 * Copyright (c) 2004, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import com.topcoder.db.connectionfactory.producers.JNDIConnectionProducer;

import junit.framework.TestCase;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * <p>
 * Accuracy test for JNDIConnectionProducer class.
 * </p>
 *
 * @author cosherx
 * @version 1.0
 */
public class JNDIConnectionProducerAccuracyTest extends TestCase {
    /**
     * <p>
     * <code>JNDIConnectionProducer</code> instance used in test.
     * </p>
     */
    private JNDIConnectionProducer producer;

    /**
     * The default initialFactory set by user.
     */
    private static String defaultInitialFactory = null;

    /**
     * <p>
     * Set up the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    protected void setUp() throws NamingException {
        defaultInitialFactory = System.getProperties().getProperty(Context.INITIAL_CONTEXT_FACTORY);
        System.getProperties().put(Context.INITIAL_CONTEXT_FACTORY, AccuracyTestHelper.getContextFactory());      
    }

    /**
     * <p>
     * tear down the test environment.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    protected void tearDown() throws Exception {
        if (defaultInitialFactory == null) {
            System.getProperties().remove(Context.INITIAL_CONTEXT_FACTORY);   
        } else {
            System.getProperties().put(Context.INITIAL_CONTEXT_FACTORY, defaultInitialFactory);
        }
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JNDIConnectionProducer(Property)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor1() throws Exception {
        producer = new JNDIConnectionProducer(AccuracyTestHelper.getProperty(AccuracyTestHelper.JNDI));
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JNDIConnectionProducer(URL)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor2() throws Exception {
        producer = new JNDIConnectionProducer(AccuracyTestHelper.getDBJNDI());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JNDIConnectionProducer(URL, Properties)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor3() throws Exception {
        producer = new JNDIConnectionProducer(AccuracyTestHelper.getDBJNDI(), new Hashtable());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JNDIConnectionProducer(URL, Properties, User, Password)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor4() throws Exception {
        producer = new JNDIConnectionProducer(AccuracyTestHelper.getDBJNDI(), new Hashtable(),
                AccuracyTestHelper.getDBUserName(), AccuracyTestHelper.getDBPassword());
        assertNotNull("Failed to create instance with property.", producer);
    }

    /**
     * <p>
     * Test accuracy of constructor <code>JNDIConnectionProducer(URL, User, Password)</code>.
     * </p>
     *
     * @throws Exception pass any unexpected exception to JUnit
     */
    public void testConstructor5() throws Exception {
        producer = new JNDIConnectionProducer(AccuracyTestHelper.getDBUrl(), AccuracyTestHelper.getDBUserName(),
                AccuracyTestHelper.getDBPassword());
        assertNotNull("Failed to create instance with property.", producer);
    }
}
