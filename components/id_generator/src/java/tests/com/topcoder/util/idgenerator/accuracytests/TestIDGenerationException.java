/**
 * Copyright (C) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.idgenerator.accuracytests;

import junit.framework.TestCase;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.util.idgenerator.*;

/**
 * This tests the situation that results in IDGenerationException thrown</p>
 *
 * @author garyk
 * @version 2.0
 */
public class TestIDGenerationException extends TestCase {

    /* Drop the test row */
    private static final String DELETE_TEST_ROW = 
        "DELETE FROM id_sequences WHERE name = ?";

    /* The id name 1 */
    private static final String ID_NAME1 = "accuracytests_gen_ex1";

    /* The id name 2 */
    private static final String ID_NAME2 = "accuracytests_gen_ex2";

    /**
     * Lookup the registered DataSource in the JNDI, then return
     * the Connection associated with it.
     */
    private Connection getConnection() throws Exception {
        Connection conn =
            new DBConnectionFactoryImpl("com.topcoder.db.connectionfactory.DBConnectionFactoryImpl").createConnection();
        return conn;
    }

    public void testIDGeneratorFactory() throws Exception {
        IDGenerator gen = null;

        
        gen = IDGeneratorFactory.getIDGenerator(ID_NAME1);

        // delete the row of the table from the database
        Connection conn = getConnection();
        PreparedStatement st = conn.prepareStatement(DELETE_TEST_ROW);

        st.setString(1, ID_NAME1);
        st.executeUpdate();

        st.close();
        conn.close();   

        try {
             gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDGenerationException");
        } catch (IDGenerationException e) {
            // good, caught as expected            
        }
    }

    public void testIDGeneratorImpl() throws Exception {
        IDGenerator gen = null;

        gen = new IDGeneratorImpl(ID_NAME2);

        // delete the row of the table from the database
        Connection conn = getConnection();
        PreparedStatement st = conn.prepareStatement(DELETE_TEST_ROW);

        st.setString(1, ID_NAME2);
        st.executeUpdate();

        st.close();
        conn.close();   

        try {
             gen.getNextID();

            // bad, exception not thrown
            fail("Should have thrown IDGenerationException");
        } catch (IDGenerationException e) {
            // good, caught as expected            
        }
    }

}