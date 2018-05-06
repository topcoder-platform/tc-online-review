/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.accuracytests.AccuracyHelper;

import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;


/**
 * <p>
 * Tests the DBUserRetrieval class.
 * </p>
 *
 * @author lyt, restarter
 * @version 1.1
 */
public class DBUserRetrievalAccuracyTest extends BaseDBRetrievalAccuracyTest {
    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * An DBUserRetrieval instance for testing.
     * </p>
     */
    private DBUserRetrieval userRetrieval = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();

        defaultConnFactory = new DBConnectionFactoryImpl(NAMESPACE);
        defaultConnection = defaultConnFactory.createConnection(DEFAULT_CONNECTION_NAME);

        userRetrieval = new DBUserRetrieval(defaultConnFactory, DEFAULT_CONN_NAME);

        baseDBRetrievalByString = new DBUserRetrieval(NAMESPACE);
        baseDBRetrieval = userRetrieval;

        // Inserts.
        AccuracyHelper.insertIntoEmail(defaultConnection);
        AccuracyHelper.insertIntoUser(defaultConnection);
        AccuracyHelper.insertIntoUserRating(defaultConnection);
        AccuracyHelper.insertIntoUserReliability(defaultConnection);
    }

    /**
     * <p>
     * Set defaultDBUserRetrieval to null.
     * </p>
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        // Cleans up.
        AccuracyHelper.cleanupTable(defaultConnection, "user_reliability");
        AccuracyHelper.cleanupTable(defaultConnection, "user_rating");
        AccuracyHelper.cleanupTable(defaultConnection, "user");
        AccuracyHelper.cleanupTable(defaultConnection, "email");

        defaultConnection.close();
        userRetrieval = null;
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(String).
     * </p>
     *
     * <p>
     * The defaultDBProjectRetrieval instance should be created successfully.
     * </p>
     *
     * @throws ConfigException to junit
     */
    public void testConstructor1_String() throws ConfigException {
        userRetrieval = new DBUserRetrieval(NAMESPACE);
        assertTrue("defaultDBUserRetrieval should be instance of DBUserRetrieval.",
            userRetrieval instanceof DBUserRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, userRetrieval, "connFactory");
        Object connName = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, userRetrieval, "connName");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
    }

    /**
     * <p>
     * Tests the accuracy of the Constructor(DBConnectionFactory, String).
     * </p>
     *
     * <p>
     * The instance should be created successfully.
     * </p>
     *
     * @throws ConfigException this exception would never be thrown in this test case.
     */
    public void testConstructor2_Accuracy() throws ConfigException {
        assertTrue("defaultDBUserRetrieval should be instance of DBUserRetrieval.",
            userRetrieval instanceof DBUserRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, userRetrieval, "connFactory");
        Object connName = AccuracyHelper.getPrivateField(BaseDBRetrieval.class, userRetrieval, "connName");

        // Asserts the set.
        assertEquals("connFactory should be set correctly.", defaultConnFactory, connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(long).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_Long() throws RetrievalException {
        ExternalUser user = userRetrieval.retrieveUser(1002);

        // Validate the emails
        String[] emails = user.getAlternativeEmails();
        Arrays.sort(emails);
        AccuracyHelper.assertEquals("Tests the accuracy of the retrieveUser(long) failed.",
            new String[] { "User2@hotmail.com", "User2@yahoo.com" }, emails);

        assertEquals("There is no design rating of the user.", 0, user.getDesignNumRatings());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignRating());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignReliability());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignVolatility());
        assertEquals("The dev number ratings should be the same.", 3, user.getDevNumRatings());
        assertEquals("The dev rating should be the same.", "2108", user.getDevRating());
        assertEquals("The dev reliability should be the same.", "76.54 %", user.getDevReliability());
        assertEquals("The dev volatility should be the same.", "131", user.getDevVolatility());
        assertEquals("The email address should be the same.", "User2@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First B", user.getFirstName());
        assertEquals("The last name should be the same.", "Last B", user.getLastName());
        assertEquals("The handle should be the same.", "Handle B", user.getHandle());
        assertEquals("The user id should be the same.", 1002, user.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(long).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_Long_NotFound() throws RetrievalException {
        assertNull("Tests the accuracy of the retrieveUser(long) failed.", userRetrieval.retrieveUser(9999));
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray() throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new long[] { 1001, 1002, 1005 });

        // Asserts.
        assertEquals("Two records would be got.", 2, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_NotFound()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new long[] { 9998, 9999 });
        assertEquals("Two records would be got.", 0, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(long[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_Duplicate()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new long[] { 1001, 1001 });
        assertEquals("Two records would be got.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(String).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_String_Accuracy() throws RetrievalException {
        ExternalUser user = userRetrieval.retrieveUser("Handle B");

        // Validate the emails
        String[] emails = user.getAlternativeEmails();
        Arrays.sort(emails);
        AccuracyHelper.assertEquals("Tests the accuracy of the retrieveUser(long) failed.",
            new String[] { "User2@hotmail.com", "User2@yahoo.com" }, emails);

        assertEquals("There is no design rating of the user.", 0, user.getDesignNumRatings());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignRating());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignReliability());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignVolatility());
        assertEquals("The dev number ratings should be the same.", 3, user.getDevNumRatings());
        assertEquals("The dev rating should be the same.", "2108", user.getDevRating());
        assertEquals("The dev reliability should be the same.", "76.54 %", user.getDevReliability());
        assertEquals("The dev volatility should be the same.", "131", user.getDevVolatility());
        assertEquals("The email address should be the same.", "User2@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First B", user.getFirstName());
        assertEquals("The last name should be the same.", "Last B", user.getLastName());
        assertEquals("The handle should be the same.", "Handle B", user.getHandle());
        assertEquals("The user id should be the same.", 1002, user.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(String).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_String_NotFound() throws RetrievalException {
        ExternalUser user = userRetrieval.retrieveUser("Handle BXXX");
        assertNull("Tests the accuracy of the retrieveUser(String) failed.", user);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray() throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new String[] { "Handle A", "Handle C", "Handle Z" });
        assertEquals("Two records would be got.", 2, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_NotFound()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new String[] { "Handle AXXX", "Handle CXXX", "Handle ZXXX" });
        assertEquals("Two records would be got.", 0, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_Duplicate()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsers(new String[] { "Handle A", "Handle A", "Handle A" });
        assertEquals("Two records would be got.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersIgnoreCase(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCase_StringArray()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersIgnoreCase(new String[] { "hAndLe a" });

        assertEquals("There is only one user.", 1, users.length);

        ExternalUser user = users[0];

        // Asserts.
        assertEquals("There is only one alternative email.", 1, user.getAlternativeEmails().length);
        assertEquals("The email address should be the same.", "User1@163.com", user.getAlternativeEmails()[0]);
        assertEquals("The design number ratings should be the same.", 10, user.getDesignNumRatings());
        assertEquals("The design rating should be the same.", "1563", user.getDesignRating());
        assertEquals("The design reliability should be the same.", "1.00 %", user.getDesignReliability());
        assertEquals("The design volatility should be the same.", "431", user.getDesignVolatility());
        assertEquals("There is no dev rating of the user.", 0, user.getDevNumRatings());
        assertEquals("There is no dev rating of the user.", "N/A", user.getDevRating());
        assertEquals("There is no dev rating of the user.", "N/A", user.getDevReliability());
        assertEquals("There is no dev rating of the user.", "N/A", user.getDevVolatility());
        assertEquals("The email address should be the same.", "User1@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First A", user.getFirstName());
        assertEquals("The last name should be the same.", "Last A", user.getLastName());
        assertEquals("The handle should be the same.", "Handle A", user.getHandle());
        assertEquals("The user id should be the same.", 1001, user.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersIgnoreCase(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCase_StringArray2()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersIgnoreCase(new String[] { "hAndLe a", "HaNdle A", "HANDLE A" });

        assertEquals("There is only one user.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersIgnoreCase(String[]).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCase_StringArray3()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersIgnoreCase(new String[] { "handle b" });

        // Here both user 1002 and 2004's handle_lower are "handle b"
        assertEquals("Tests the accuracy of the retrieveUsersIgnoreCase(String[]) failed.", 2, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     *
     * <p>
     * There do have some records can be found.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString1()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersByName("First B", "Last B");

        assertEquals("There is only one user.", 1, users.length);

        ExternalUser user = users[0];

        // Validate the emails
        String[] emails = user.getAlternativeEmails();
        Arrays.sort(emails);
        AccuracyHelper.assertEquals("Tests the accuracy of the retrieveUser(long) failed.",
            new String[] { "User2@hotmail.com", "User2@yahoo.com" }, emails);

        assertEquals("There is no design rating of the user.", 0, user.getDesignNumRatings());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignRating());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignReliability());
        assertEquals("There is no design rating of the user.", "N/A", user.getDesignVolatility());
        assertEquals("The dev number ratings should be the same.", 3, user.getDevNumRatings());
        assertEquals("The dev rating should be the same.", "2108", user.getDevRating());
        assertEquals("The dev reliability should be the same.", "76.54 %", user.getDevReliability());
        assertEquals("The dev volatility should be the same.", "131", user.getDevVolatility());
        assertEquals("The email address should be the same.", "User2@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First B", user.getFirstName());
        assertEquals("The last name should be the same.", "Last B", user.getLastName());
        assertEquals("The handle should be the same.", "Handle B", user.getHandle());
        assertEquals("The user id should be the same.", 1002, user.getId());
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     *
     * <p>
     * Blur search.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString2()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersByName("    ", "Last B");

        assertEquals("There is only one user.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     *
     * <p>
     * No record can be found.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString3()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersByName("    ", "AAAA");

        assertEquals("There is no user.", 0, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     *
     * <p>
     * No record can be found.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString4()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersByName("First A", " ");

        assertEquals("There is no user.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     *
     * <p>
     * No record can be found.
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString5()
        throws RetrievalException {
        ExternalUser[] users = userRetrieval.retrieveUsersByName("    First A    ", " ");

        assertEquals("There is no user.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the createObject(ResultSet).
     * </p>
     *
     * @throws RetrievalException this exception would never be thrown in this test case.
     * @throws SQLException this exception would never be thrown in this test case.
     */
    public void testCreateObject_ResultSet() throws RetrievalException, SQLException {
        // Prepares the ps and rs.
        PreparedStatement ps = defaultConnection.prepareStatement("Select " +
                " u.user_id id, u.first_name, u.last_name, u.handle, e.address " + " from user u, email e " +
                " where u.user_id = 1002 and e.primary_ind = 1 and u.user_id = e.user_id");
        ResultSet rs = ps.executeQuery();

        // Creates object.
        ExternalUser user = null;

        while (rs.next()) {
            user = (ExternalUser) userRetrieval.createObject(rs);
        }

        // Asserts.
        assertEquals("The email address should be the same.", "User2@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First B", user.getFirstName());
        assertEquals("The last name should be the same.", "Last B", user.getLastName());
        assertEquals("The handle should be the same.", "Handle B", user.getHandle());
        assertEquals("The user id should be the same.", 1002, user.getId());
    }
}
