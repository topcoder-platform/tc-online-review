/*
 * Copyright (C) 2013 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.management.payment.calculator.impl;

import java.io.IOException;
import java.sql.Connection;

import com.topcoder.commons.utils.LoggingWrapperUtility;
import com.topcoder.commons.utils.ParameterCheckUtility;
import com.topcoder.configuration.ConfigurationObject;
import com.topcoder.configuration.persistence.ConfigurationFileManager;
import com.topcoder.configuration.persistence.ConfigurationParserException;
import com.topcoder.configuration.persistence.NamespaceConflictException;
import com.topcoder.configuration.persistence.UnrecognizedFileTypeException;
import com.topcoder.configuration.persistence.UnrecognizedNamespaceException;
import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorConfigurationException;
import com.topcoder.management.payment.calculator.ProjectPaymentCalculatorException;
import com.topcoder.util.log.Log;
import com.topcoder.util.log.LogManager;


/**
 * <p>
 * This is the base class of the payment calculators defined in this component. It can be extended by classes that
 * need to gain access to the logger instance to be used to perform logging, it provides
 * {@link #createConnection()} method to be used by subclasses to create a connection to the database.
 * </p>
 * <p>
 * This class is configured using Configuration API <code>ConfigurationObject</code> either from configuration file
 * or directly via <code>ConfigurationObject</code> instance. Here are the basic configuration parameters (angle
 * brackets are used for identifying child configuration objects):
 * </p>
 * <table border="1" cellspacing="2" cellpadding="3">
 * <tr>
 * <th>Parameter name</th>
 * <th>Description</th>
 * <th>Value</th>
 * </tr>
 * <tr>
 * <td>logger_name</td>
 * <td>The name of the logger to be used to perform logging. When not provided, logging is not performed.</td>
 * <td>String. Optional. When empty, it is treated as it is not provided.</td>
 * </tr>
 * <tr>
 * <td>connection_name</td>
 * <td>The database connection name to be used by this class. When not specified the default connection is used.</td>
 * <td>String. Optional. When empty, it is treated as it is not specified.</td>
 * </tr>
 * <tr>
 * <td>&lt;db_connection_factory_config&gt;</td>
 * <td>The configuration for <code>DBConnectionFactoryImpl</code> instance to be used by this class.</td>
 * <td><code>ConfigurationObject</code>. Not <code>null</code>. Required.</td>
 * </tr>
 * </table>
 * <p>
 * It holds a reference to Logging Wrapper <code>Log</code> instance that can be retrieved by subclasses via
 * {@link #getLogger()} getter and uses <code>DBConnectionFactory</code> from DB connection Factory component to
 * create connections to the database.
 * </p>
 * <p>
 * <b>Thread Safety:</b><br/>
 * This class is thread safe, since all its fields are initialized in the constructor and never changed after that.
 * Implementations of the aggregated <code>DBConnectionFactory</code> and <code>Log</code> are expected to be
 * thread safe.
 * </p>
 *
 * @author Schpotsky, TCSDEVELOPER
 * @version 1.0
 */
public abstract class BaseProjectPaymentCalculator {
    /**
     * <p>
     * The Logging Wrapper Log implementation instance to be used to perform logging.
     * </p>
     * <p>
     * It is initialized in the constructor and never changed after that. It can be any value (if it is null, this
     * means that it is not required to perform logging).
     * </p>
     * <p>
     * It is used to perform logging.
     * </p>
     */
    private final Log logger;

    /**
     * <p>
     * The DBConnectionFactory instance to be used by this class to create a connection using the configured or
     * default connection name.
     * </p>
     * <p>
     * It is initialized in the constructor and never changed after that. Can not be null after initialization. (A
     * configuration exception should be thrown from the constructor if this variable can not be initialized
     * properly).
     * </p>
     * <p>
     * It is used in createConnection() method to create a connection to the database.
     * </p>
     */
    private final DBConnectionFactory dbConnectionFactory;

    /**
     * <p>
     * The connection name to be used to create a connection.
     * </p>
     * <p>
     * It is initialized in the constructor and never changed after that. Can be any value. If it is null or empty
     * then the default connection should be used instead.
     * </p>
     * <p>
     * It is used in createConnection().
     * </p>
     */
    private final String connectionName;

    /**
     * <p>
     * Creates a new instance and initializes it from the given configuration object.
     * </p>
     *
     * @param configurationObject
     *            The configuration object for this class.
     * @throws IllegalArgumentException
     *             If <code>configurationObject</code> is <code>null</code>.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred during configuration (required configuration parameter not found or has an
     *             invalid value or if there is any configuration exception thrown from the DB connection factory).
     */
    protected BaseProjectPaymentCalculator(ConfigurationObject configurationObject) {
        ParameterCheckUtility.checkNotNull(configurationObject, "configurationObject");

        // get the logger name
        String loggerName = Helper.getStringProperty(configurationObject, "logger_name", false);
        logger = loggerName == null ? null : LogManager.getLog(loggerName);

        // get the connection name from the configuration
        // and assign it to the namesake field
        connectionName = Helper.getStringProperty(configurationObject, "connection_name", false);

        // Get the database configuration from the configuration object
        ConfigurationObject dbConnFactoryConfig =
            Helper.getChildConfiguration(configurationObject, "db_connection_factory_config");

        // create the database connection factory
        try {
            dbConnectionFactory = new DBConnectionFactoryImpl(dbConnFactoryConfig);
        } catch (UnknownConnectionException e) {
            throw new ProjectPaymentCalculatorConfigurationException(
                "Unknown connection is found when creating DBConnectionFactory instance", e);
        } catch (ConfigurationException e) {
            throw new ProjectPaymentCalculatorConfigurationException("Fails to configure DBConnectionFactory instance",
                e);
        }

    }

    /**
     * <p>
     * Gets the logger instance field used to perform the logging.
     * </p>
     *
     * @return The logger instance field.
     */
    protected Log getLogger() {
        return logger;
    }

    /**
     * <p>
     * Creates a Database connection.
     * </p>
     *
     * @return The created database connection.
     * @throws ProjectPaymentCalculatorException
     *             if there is any problem when creating the connection.
     */
    protected Connection createConnection() throws ProjectPaymentCalculatorException {
        String signature = getClass().getName() + "#createConnection";
        LoggingWrapperUtility.logEntrance(logger, signature, null, null);
        Connection connection;

        try {
            if (connectionName == null) {
                // create the default connection
                connection = dbConnectionFactory.createConnection();
            } else {
                // create a connection with the configured name
                connection = dbConnectionFactory.createConnection(connectionName);
            }
        } catch (DBConnectionException e) {
            throw LoggingWrapperUtility.logException(logger, signature, new ProjectPaymentCalculatorException(
                "Failed to create connection to database", e));
        }

        LoggingWrapperUtility.logExit(logger, signature, new Object[] {connection});
        return connection;
    }

    /**
     * <p>
     * This protected method is a helper method to be used by subclasses to get the
     * <code>ConfigurationObject</code> instance by reading the configuration data from the specified configuration
     * file and namespace.
     * </p>
     *
     * @param configFilePath
     *            The configuration file path from which to read the configuration.
     * @param namespace
     *            The namespace to be used to load the configuration.
     * @return The <code>ConfigurationObject</code> instance created using the specified configuration file and
     *         namespace.
     * @throws IllegalArgumentException
     *             If any or input parameters is <code>null</code> or empty.
     * @throws ProjectPaymentCalculatorConfigurationException
     *             If any error occurred when creating the <code>ConfigurationObject</code> instance from the
     *             specified config file and namespace.
     */
    protected static ConfigurationObject getConfigurationObject(String configFilePath, String namespace) {
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming(configFilePath, "configFilePath");
        ParameterCheckUtility.checkNotNullNorEmptyAfterTrimming(namespace, "namespace");

        try {
            // create configuration file manager using the specified configuration file
            ConfigurationFileManager configFileManager = new ConfigurationFileManager(configFilePath);

            // Get the configuration object from the configuration file manager using the specified namespace.
            ConfigurationObject configObject = configFileManager.getConfiguration(namespace);

            // Get the configuration of this class
            ConfigurationObject config = Helper.getChildConfiguration(configObject, namespace);

            return config;
        } catch (ConfigurationParserException e) {
            throw new ProjectPaymentCalculatorConfigurationException("Fails to parse configuration file '"
                + configFilePath + "'", e);
        } catch (NamespaceConflictException e) {
            throw new ProjectPaymentCalculatorConfigurationException(
                "Namespace conflict is encountered while loading configuration file '" + configFilePath
                    + "' to namespace '" + namespace + "'", e);
        } catch (UnrecognizedFileTypeException e) {
            throw new ProjectPaymentCalculatorConfigurationException("The type of configuration file '"
                + configFilePath + "' is not supported", e);
        } catch (IOException e) {
            throw new ProjectPaymentCalculatorConfigurationException(
                "An I/O error occurs while reading configuration file '" + configFilePath + "'", e);
        } catch (UnrecognizedNamespaceException e) {
            throw new ProjectPaymentCalculatorConfigurationException("The specified namespace '" + namespace
                + "' is not found in configuration file '" + configFilePath + "'", e);
        }
    }
}
