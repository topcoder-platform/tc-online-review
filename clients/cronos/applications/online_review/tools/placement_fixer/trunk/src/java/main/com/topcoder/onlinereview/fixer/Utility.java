package com.topcoder.onlinereview.fixer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.config.ConfigManagerException;

/**
 * Utility class for the OR fixer.
 * 
 * @author hohosky
 */
public class Utility {
    /**
     * DB connection to the online review database.
     */
    private static Connection connection;

    /**
     * Config file of the fixer, which contains the configuration for DB
     * connection factory.
     */
    private static final String CONFIG_FILE = "config.xml";

    /**
     * Config namespace for DB connection factory.
     */
    private static final String DB_CONNECTION_FACTORY_NS = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /**
     * Gets the database connection.
     * 
     * @return the database connection to OR database.
     */
    public static Connection getConnection() {
        if (connection == null) {
            
            try {
                ConfigManager.getInstance().add(CONFIG_FILE);
            } catch (ConfigManagerException cme) {
                throw new OnlineReviewScoreRankFixerException("Fail to load the configuration of Fixer.");
            }
            
            try {
                DBConnectionFactory factory = new DBConnectionFactoryImpl(DB_CONNECTION_FACTORY_NS);
                connection = factory.createConnection();
            } catch (Exception ex) {
                throw new OnlineReviewScoreRankFixerException("Fail to get Database Connection.", ex);
            }
        }

        return connection;
    }
    
    /**
     * Realse all the resources.
     * 
     * @param statement the sql statement.
     * @param resultSet the resultset.
     * @param connection the db connection.
     */
    public static void releaseResource(Statement statement, ResultSet resultSet, Connection connection) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            
            if (statement != null) {
                statement.close();
            }
            
            if (connection != null) {
                connection.close();
            }
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
