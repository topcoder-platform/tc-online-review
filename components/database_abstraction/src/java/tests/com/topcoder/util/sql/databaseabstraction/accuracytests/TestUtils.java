package com.topcoder.util.sql.databaseabstraction.accuracytests;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.DriverManager;

import junit.framework.TestCase;


/**
 * A the superclass for the JUnit test cases of this accuracy test suite.
 * it defines some common methods and variables used in all the tests.
 *
 * @author Tomson
 * @version 1.0
 */
public class TestUtils extends TestCase {
    /** driver name of the database */
    protected static final String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
    
    /** the url of the data source */
    protected static final String sourceURL = "jdbc:odbc:Tester"; 
    
    /** the connection to the test data base */
    protected Connection con;
    
    protected Statement state;
     
    public void setUp() {
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(sourceURL);
    	    state = con.createStatement();
    	} catch(Exception e) {
    	    fail(e.toString());
    	}
    }
    
    /**
     * close the database connection
     */
    public void tearDown() throws Exception {
        con.close();
    }
}