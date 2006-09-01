/**
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.onlinereview.migration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
 * 
 * @author brain_cn
 * @version 1.0
 */
public class BaseDataTestCases extends TestCase {
	protected void tearDown() throws Exception {
		close(this.connection);
	}

	protected void close(Object object) throws Exception {
		if (object instanceof Connection) {
			((Connection) object).close();
		}
		if (object instanceof Statement) {
			((Statement) object).close();
		}
		if (object instanceof ResultSet) {
			((ResultSet) object).close();
		}
	}

	protected void verifyProjectCreated(long projectId) {
    	String tableName = "project";
    	tableName = "project_info";
    	tableName = "project_phase";
    	tableName = "phase_criteria";
    	tableName = "phase_criteria";
    	tableName = "phase_dependency";
    	tableName = "resource";
    	tableName = "resource_info";
	}

    protected long getProjectId(String compVersId, String typeId, String projectVersion) throws Exception {
    	String sql = "select project_id from project p " +
    				"	inner join project_info pi_vi " +
    				"	on p.project_id = pi_vi.project_id and pi_vi.project_info_type_id = 1 " +
    				"	and pi_vi.value = ?" +
    				"	inner join project_info pi_vt " +
    				"	on p.project_id = pi_vt.project_id and pi_vt.project_info_type_id = 7 " +
    				"	and pi_vt.value = ?" +
    				"	where p.project_category_id = ?";
    	Connection conn = this.getConnection().getConnection();
    	PreparedStatement pstmt = conn.prepareStatement(sql);
    	pstmt.setString(1, compVersId);
    	pstmt.setString(2, projectVersion);
    	pstmt.setString(3, typeId);
    	ResultSet rs = pstmt.executeQuery();
    	try {
	    	if (rs.next()) {
	    		return rs.getLong("project_id");
	    	} else {
	    		return 0;
	    	}
    	} finally {
    		close(rs);
    		close(pstmt);
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
        ITable actualData = getConnection().createQueryTable(tableName, query);

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
     * <p>Gets the connection to database.</p>
     *
     * @return an <code>IDatabaseConnection</code> providing the connection to database.
     * @throws Exception if an unexpected error occurs.
     */
    protected IDatabaseConnection getConnection() throws Exception {
        return new DatabaseConnection(getJDBCConnection());
    }

    protected Connection getJDBCConnection() throws Exception {
    	if (connection == null) {
    		connection = getDBConnectionFactory().createConnection("tcs_catalog_new");
    	}
    	return connection;
    }

    private Connection connection = null;
    private boolean isDebug = true;
    protected boolean propertyPending(String name, String value) {
    	if (!"TODO".equals(value)) {
    		return false;
    	}
    	if (!isDebug) {
    		fail("property " + name + " is not configured");
    	}
    	return true;
    }
}
