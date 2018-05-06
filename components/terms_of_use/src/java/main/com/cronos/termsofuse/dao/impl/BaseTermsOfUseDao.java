/*
 * Copyright (C) 2011 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.termsofuse.dao.impl;

import com.cronos.termsofuse.dao.TermsOfUseDaoConfigurationException;
import com.topcoder.configuration.ConfigurationAccessException;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;

/**
 * <p>
 * This class is the base class defined for the daos of this component. It simply provides common logger and database
 * connection factory. The subclasses access these fields by protected getters.
 * </p>
 *
 * <p>
 * <strong>Thread Safety: </strong> The class is immutable and thread safe. The configuration is done in a thread safe
 * manner.
 * </p>
 *
 * @author faeton, sparemax
 * @version 1.0
 */
public abstract class BaseTermsOfUseDao {
    /**
     * <p>
     * Represents the child key 'dbConnectionFactoryConfig'.
     * </p>
     */
    private static final String KEY_DBCF_CONFIG = "dbConnectionFactoryConfig";

    /**
     * <p>
     * Represents the property key 'loggerName'.
     * </p>
     */
    private static final String KEY_LOGGER_NAME = "loggerName";

    /**
     * <p>
     * The db connection factory, used to obtain the connection to the database.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed afterwards. It is used in the dao operations. It can not
     * be null. It is accessed by subclasses by protected getter.
     * </p>
     */
    private final DBConnectionFactory dbConnectionFactory;

    /**
     * <p>
     * The log, used to perform logging.
     * </p>
     *
     * <p>
     * It is initialized in the constructor and never changed afterwards. It is used in the dao operations. It can not
     * be null. It is accessed by subclasses by protected getter.
     * </p>
     */
    private final Log log;

    /**
     * This is the constructor with the ConfigurationObject parameter, which initializes the database connection
     * factory and logger.
     *
     * @param configurationObject
     *            the configuration object containing the configuration.
     *
     * @throws IllegalArgumentException
     *             if the configurationObject is null.
     * @throws TermsOfUseDaoConfigurationException
     *             if any exception occurs while initializing the instance.
     */
    protected BaseTermsOfUseDao(ConfigurationObject configurationObject) {
        Helper.checkNull(configurationObject, "configurationObject");

        try {
            ConfigurationObject dbConnectionFactoryConfig = configurationObject.getChild(KEY_DBCF_CONFIG);
            if (dbConnectionFactoryConfig == null) {
                throw new TermsOfUseDaoConfigurationException("The child 'dbConnectionFactoryConfig' is required.");
            }

            dbConnectionFactory = new DBConnectionFactoryImpl(dbConnectionFactoryConfig);

            log = LogManager.getLog(Helper.getRequiredProperty(configurationObject, KEY_LOGGER_NAME));
        } catch (ConfigurationException e) {
            throw new TermsOfUseDaoConfigurationException("Failed to create the db connection factory.", e);
        } catch (UnknownConnectionException e) {
            throw new TermsOfUseDaoConfigurationException("Failed to create the db connection factory.", e);
        } catch (ConfigurationAccessException e) {
            throw new TermsOfUseDaoConfigurationException("An error occurred while accessing the configuration.", e);
        }
    }

    /**
     * Gets the db connection factory, used to obtain the connection to the database.
     *
     * @return the db connection factory, used to obtain the connection to the database.
     */
    protected DBConnectionFactory getDBConnectionFactory() {
        return dbConnectionFactory;
    }

    /**
     * Gets the log, used to perform logging.
     *
     * @return the log, used to perform logging.
     */
    protected Log getLog() {
        return log;
    }
}
