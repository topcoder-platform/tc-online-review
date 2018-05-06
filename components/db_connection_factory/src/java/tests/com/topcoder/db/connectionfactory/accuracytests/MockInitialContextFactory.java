/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * Mock initial context factory for unit testing purposes only. Currently the
 * only thing in the context is the driver for a configuration file, required
 * for the IDGenerator.
 *
 * @author cosherx
 * @version 1.0
 */
public class MockInitialContextFactory implements InitialContextFactory {
    /**
     * Returns an instance of our mock context that merely points at my
     * MySql database for unit testing purposes only.
     *
     * @param parm1 ignored.
     *
     * @return an initial context that points at the MySql database.
     *
     * @throws NamingException Only if InitialContext's constructor does.
     */
    public Context getInitialContext(final Hashtable parm1)
        throws NamingException {
        return new MockContext();
    }
}


