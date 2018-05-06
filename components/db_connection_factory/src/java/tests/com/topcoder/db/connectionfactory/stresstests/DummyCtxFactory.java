/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.stresstests;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.spi.InitialContextFactory;


/**
 * <p>
 * Dummy initial context factory used in <code>JNDIConnectionProducer</code> test.
 * </p>
 *
 * @author magicpig
 * @version 1.0
 */
public final class DummyCtxFactory implements InitialContextFactory {
    /**
     * <p>
     * Get the initial context, which is an instance of <code>DummyContext</code>.
     * </p>
     *
     * @param environment environment properties
     *
     * @return an instance of <code>DummyContext</code>
     */
    public Context getInitialContext(Hashtable environment) {
        return new DummyContext(environment);
    }
}







