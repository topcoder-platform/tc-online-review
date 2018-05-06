/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.database;

import com.topcoder.db.connectionfactory.ConnectionProducer;

import com.topcoder.search.builder.ConnectionInformation;


/**
 * <p>
 * The class implements ConnectionInformation interface.
 * This class includes all the information needed to connect to a database.
 * </p>
 *
 * <p>
 * The class is not thread safe, since the only attribute it has is immutable
 * </p>
 * @author victor_lxd, haozhangr
 * @version 1.1
 */
public class DatabaseConnectionInformation implements ConnectionInformation {
    /**
     * It will hold a ConnnectionProducer object, used to create database connection.
     * It is immutable. It will not be changed by this class during the life time of the instance
     *
     */
    private final ConnectionProducer factory;

    /**
     * <p>
     * Create a new instance,by providing the ConnectionProducer instance.
     * </p>
     *
     * @param factory an object used to create a data store connection
     * @throws NullPointerException if any parameter is Null
     */
    public DatabaseConnectionInformation(ConnectionProducer factory) {
        //check factory null
        if (factory == null) {
            throw new NullPointerException(
                "The ConnectionProducer should not be null to construct the DatabaseConnectionInformation.");
        }

        this.factory = factory;
    }

    /**
     * <p>
     * Get the factory object used to create a database connection.
     * </p>
     *
     *
     * @return an object used to create a data store connection
     */
    public Object getFactory() {
        return this.factory;
    }
}
