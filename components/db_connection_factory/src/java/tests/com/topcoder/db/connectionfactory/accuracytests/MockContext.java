/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.db.connectionfactory.accuracytests;

import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Extension of initial context that always only ever returns a DataSource to connect to the database used for personal
 * unit testing.
 *
 * @author cosherx
 * @version 1.0
 */
class MockContext extends InitialContext {
    /**
     * Default ctor tells the parent to only lazily instnatiate itself.
     *
     * @throws NamingException if InitialContext does
     */
    MockContext() throws NamingException {
        super(true);
    }

    /**
     * In this simple mock implementation, this method only ever returns a DataSource object pointing at the database
     * used for unit testing.
     *
     * @param key Ignored
     *
     * @return a DataSource object.
     */
    public Object lookup(final String key) {
        return new MockDataSource();
    }
}
