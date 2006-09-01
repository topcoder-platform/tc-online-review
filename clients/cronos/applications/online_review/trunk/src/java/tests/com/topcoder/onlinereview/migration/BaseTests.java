/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.Assertion;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSet;

import com.topcoder.db.connectionfactory.ConfigurationException;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.UnknownConnectionException;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;

/**
 * The test of BaseTests.
 *
 * @author brain_cn
 * @version 1.0
 */
public class BaseTests extends TestCase {
    /**
     * The configuration interface.
     */
    protected static Configuration config = new Configuration(TCLoadTCSTests.class.getName());
	private static final String SOURCE_CONNECTION_NAME = config.getProperty("source_conn");
	private static final String TARGET_CONNECTION_NAME = config.getProperty("target_conn");
	private Connection sourceConn = null;
	private Connection targetConn = null;
	protected void setUp() throws Exception {
        DBConnectionFactory dbf = getDBConnectionFactory();
        sourceConn = dbf.createConnection(SOURCE_CONNECTION_NAME);
        targetConn = dbf.createConnection(TARGET_CONNECTION_NAME);
	}

	protected static final long PROJECT_ID = Long.parseLong(config.getProperty("project_id", "22454164"));
	protected static final long SCORECARD_TEMPLATE_ID = Long.parseLong(config.getProperty("scorecard_template_id", "1"));
	protected void tearDown() throws Exception {
		close(sourceConn);
		close(targetConn);
	}

	protected void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch(SQLException e) {
				// Ignore
			}
		}
	}

    /**
     * Return DBConnectionFactory to create connection.
     *
     * @return DBConnectionFactory
     *
     * @throws UnknownConnectionException to JUnit
     * @throws ConfigurationException to JUnit
     */
    protected DBConnectionFactory getDBConnectionFactory()
        throws UnknownConnectionException, ConfigurationException {
        return new DBConnectionFactoryImpl(DBConnectionFactoryImpl.class.getName());
    }

    /**
     * <p>Asserts that the data contained in specified database table matches the expected state. The specified SQL
     * query is used to obtain the desired data from the specified database table. The specified filename (relative to
     * classpath) is used to locate the XML file providing the expected data. Neither actual nor expected data is sorted
     * before comparison. Therefore the SQL query must return data in same order as provided by the expected data.</p>
     *
     * @param tableName a <code>String</code> providing the name of database table being verified.
     * @param expectedDataFileName a <code>String</code> providing the name of the XML file providing the expected data
     * for the specified table.
     * @param query a <code>String</code> providing the SQL query to be used to obtain the actual data from the
     * specified database table for purpose of current test.
     * @param assertionMessage a <code>String</code> providing the description of the error in case data assertion
     * fails.
     * @throws Exception if an unexpected error occurs.
     */
    protected void assertData(String tableName, String expectedDataFileName, String query, String assertionMessage)
        throws Exception {

        // Load actual data from specified database table using the provided SQL query
        ITable actualData = getTargetConnection().createQueryTable(tableName, query);

        // Load expected data from XML file
        InputStream is = getClass().getClassLoader().getResourceAsStream(expectedDataFileName);
        IDataSet expectedDataSet = new FlatXmlDataSet(is);
        ITable expectedData = expectedDataSet.getTable(tableName);

        // Verify that actual data matches expected data
        try {
            Assertion.assertEquals(expectedData, actualData);
        } catch (AssertionFailedError e) {
            throw new AssertionFailedError(assertionMessage + " : " + e.getMessage());
        }
    }

    /**
     * <p>Asserts that the data contained in database matches the expected state. The specified filename (relative to
     * classpath) is used to locate the XML file providing the expected data.</p>
     *
     * @param expectedDataFileName a <code>String</code> providing the name of the XML file providing the expected data
     * for entire database.
     * @param assertionMessage a <code>String</code> providing the description of the error in case data assertion
     * fails.
     * @throws Exception if an unexpected error occurs.
     */
    protected void assertData(String expectedDataFileName, String assertionMessage) throws Exception {

        // Load expected data from XML file
        InputStream is = getClass().getClassLoader().getResourceAsStream(expectedDataFileName);
        IDataSet expectedDataSet = new FlatXmlDataSet(is);

        // Load actual data from specified database table using the provided SQL query
        IDataSet actualDataSet = this.getTargetConnection().createDataSet(expectedDataSet.getTableNames());

        // Verify that actual data matches expected data
        try {
            Assertion.assertEquals(expectedDataSet, actualDataSet);
        } catch (AssertionFailedError e) {
            throw new AssertionFailedError(assertionMessage + " : " + e.getMessage());
        }
    }

    /**
     * <p>Gets the connection to database.</p>
     *
     * @return an <code>IDatabaseConnection</code> providing the connection to database.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDatabaseConnection getTargetConnection() throws Exception {
        return new DatabaseConnection(this.getTargetConn());
    }

	/**
	 * @return Returns the sourceConn.
	 */
	public Connection getSourceConn() {
		return sourceConn;
	}

	/**
	 * @param sourceConn The sourceConn to set.
	 */
	public void setSourceConn(Connection sourceConn) {
		this.sourceConn = sourceConn;
	}

	/**
	 * @return Returns the targetConn.
	 */
	public Connection getTargetConn() {
		return targetConn;
	}

	/**
	 * @param targetConn The targetConn to set.
	 */
	public void setTargetConn(Connection targetConn) {
		this.targetConn = targetConn;
	}

}
