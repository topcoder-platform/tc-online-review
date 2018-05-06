/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved.
 */
package com.topcoder.util.idgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;


/**
 * <p>
 * A Helper class for IDGenerator Component.
 * </p>
 *
 * <p>
 * This class provides some useful methods, such as close jdbc resource, etc.
 * </p>
 *
 * @author gua
 * @version 3.0
 */
class IDGeneratorHelper {
    /**
     * Private constructor to prevent this class be instantiated.
     */
    private IDGeneratorHelper() {
    }


    /**
     * <p>
     * Close the given Statement instance.
     * </p>
     *
     * @param stmt the Statement instance to close.
     */
    static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException ignored) {
                // Ignore
            }
        }
    }


    /**
     * <p>
     * Close the given ResultSet instance.
     * </p>
     *
     * @param rs the ResultSet instance to close.
     */
    static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException ignored) {
                // Ignore
            }
        }
    }

    /**
     * <p>
     * Close the given connection.
     * </p>
     *
     * @param connection the connection to close
     */
    static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // Just ignore
            }
        }
    }
    
    /**
     * Gets the configuration setting of an item with the specific key. It tries to read from configuration, and if
     * fails the default value will be used.
     *
     * @param namespace the configuration namespace
     * @param key the key to the configuration item
     * @param defaultValue the default value of the configurtion item
     *
     * @return the value of the configuration item
     *
     * @throws ConfigManagerException if failed to retreive configuration settings.
     */
    static String getConfigurationSetting(String namespace, String key, String defaultValue)
        throws ConfigManagerException {
        String value = ConfigManager.getInstance().getString(namespace, key);

        if (value == null) {
            value = defaultValue;
        }

        return value;
    }
}
