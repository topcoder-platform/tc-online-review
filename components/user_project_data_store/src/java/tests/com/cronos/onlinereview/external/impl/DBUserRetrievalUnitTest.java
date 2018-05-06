/*
 * Copyright (C) 2007 TopCoder Inc., All Rights Reserved.
 */
package com.cronos.onlinereview.external.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.cronos.onlinereview.external.ConfigException;
import com.cronos.onlinereview.external.ExternalUser;
import com.cronos.onlinereview.external.RetrievalException;
import com.cronos.onlinereview.external.UnitTestHelper;
import com.topcoder.db.connectionfactory.DBConnectionFactory;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;

import junit.framework.TestCase;

/**
 * <p>
 * Tests the DBUserRetrieval class.
 * </p>
 *
 * @author oodinary
 * @author FireIce
 * @version 1.1
 * @since 1.0
 */
public class DBUserRetrievalUnitTest extends TestCase {

    /**
     * <p>
     * Represents the configuration file.
     * </p>
     */
    private static final String CONFIG_FILE = "SampleConfig.xml";

    /**
     * <p>
     * The name of the namespace that the calling program can populate which contains DBConnectionFactory and other
     * configuration values.
     * </p>
     */
    private static final String NAMESPACE = "com.cronos.onlinereview.external";

    /**
     * <p>
     * The default ConnName which is defined in the configure file.
     * </p>
     */
    private static final String DEFAULT_CONN_NAME = "UserProjectDataStoreConnection";

    /**
     * <p>
     * The default DB connection factory.
     * </p>
     */
    private DBConnectionFactory defaultConnFactory = null;

    /**
     * <p>
     * An DBUserRetrieval instance for testing.
     * </p>
     */
    private MockDBUserRetrieval defaultDBUserRetrieval = null;

    /**
     * <p>
     * The default connection used for db operations.
     * </p>
     */
    private Connection defaultConnection = null;

    /**
     * <p>
     * Initialization.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void setUp() throws Exception {
        super.setUp();
        UnitTestHelper.addConfig(CONFIG_FILE);

        defaultDBUserRetrieval = new MockDBUserRetrieval(NAMESPACE);

        // Retrieves DBConnectionFactory.
        defaultConnection = defaultDBUserRetrieval.getConnection();
        defaultConnFactory = new DBConnectionFactoryImpl(NAMESPACE);

        // Inserts.
        UnitTestHelper.insertIntoEmail(defaultConnection);
        UnitTestHelper.insertIntoUser(defaultConnection);
        UnitTestHelper.insertIntoUserRating(defaultConnection);
        UnitTestHelper.insertIntoUserReliability(defaultConnection);
    }

    /**
     * <p>
     * Set defaultDBUserRetrieval to null.
     * </p>
     *
     * @throws Exception
     *             to JUnit.
     */
    protected void tearDown() throws Exception {

        // Cleans up.
        UnitTestHelper.cleanupTable(defaultConnection, "user_reliability");
        UnitTestHelper.cleanupTable(defaultConnection, "user_rating");
        UnitTestHelper.cleanupTable(defaultConnection, "user");
        UnitTestHelper.cleanupTable(defaultConnection, "email");

        defaultDBUserRetrieval = null;
        UnitTestHelper.clearConfig();
        defaultConnection.close();
        super.tearDown();
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(String).
     * </p>
     * <p>
     * The defaultDBProjectRetrieval instance should be created successfully.
     * </p>
     */
    public void testCtor_String() {

        assertNotNull("DBUserRetrieval should be accurately created.", defaultDBUserRetrieval);
        assertTrue("defaultDBUserRetrieval should be instance of DBUserRetrieval.",
                defaultDBUserRetrieval instanceof DBUserRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBUserRetrieval,
                "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBUserRetrieval, "connName");

        // Asserts the set.
        assertNotNull("connFactory should be set correctly.", connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If the parameter is null. Then IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_String_NullNamespace() throws ConfigException {

        try {
            new MockDBUserRetrieval(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If the parameter is empty after trimmed. Then IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_String_EmptyNamespace() throws ConfigException {

        try {
            new MockDBUserRetrieval("  ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * The connName which is got from the configure file, is not contained in the connFactoryImpl. Then ConfigException
     * should be thrown.
     * </p>
     */
    public void testCtor_String_ConnNameNotInclude() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBUserRetrieval("com.cronos.onlinereview.external.connNameNotInclude");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * The connName which is got from the configure file, is defined empty. Then ConfigException should be thrown.
     * </p>
     */
    public void testCtor_String_ConnNameEmpty() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            new MockDBUserRetrieval("com.cronos.onlinereview.external.connNameEmpty");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(String).
     * </p>
     * <p>
     * If gives an unknown namespace. Then ConfigException should be thrown.
     * </p>
     */
    public void testCtor_String_UnknownNamespace() {

        try {
            new MockDBUserRetrieval("Unknown");
            fail("ConfigException should be thrown.");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * The instance should be created successfully.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString() throws ConfigException {

        defaultDBUserRetrieval = new MockDBUserRetrieval(defaultConnFactory, DEFAULT_CONN_NAME);

        assertNotNull("DBUserRetrieval should be accurately created.", defaultDBUserRetrieval);
        assertTrue("defaultDBUserRetrieval should be instance of DBUserRetrieval.",
                defaultDBUserRetrieval instanceof DBUserRetrieval);

        // Uses the reflection to test the field setting.
        Object connFactory = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBUserRetrieval,
                "connFactory");
        Object connName = UnitTestHelper.getPrivateField(BaseDBRetrieval.class, defaultDBUserRetrieval, "connName");

        // Asserts the set.
        assertEquals("connFactory should be set correctly.", defaultConnFactory, connFactory);
        assertEquals("connName should be set correctly.", DEFAULT_CONN_NAME, connName);
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If DBConnectionFactory given is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString_NullDBConnectionFactory() throws ConfigException {

        try {
            new MockDBUserRetrieval(null, DEFAULT_CONN_NAME);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If connName given is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws ConfigException
     *             this exception would never be thrown in this test case.
     */
    public void testCtor_DBConnectionFactoryString_NullConnName() throws ConfigException {

        try {
            new MockDBUserRetrieval(defaultConnFactory, null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the ctor(DBConnectionFactory, String).
     * </p>
     * <p>
     * If connName doesn't correspond to a connection the factory knows about, ConfigException should be thrown.
     * </p>
     */
    public void testCtor_DBConnectionFactoryString_UnknownConnName() {

        try {
            new MockDBUserRetrieval(defaultConnFactory, "UnknownConnName");
        } catch (ConfigException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(long[]).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_1() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new long[] {1001, 1002, 1005});

        // Asserts.
        assertEquals("Two records would be got.", 2, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(long[]).
     * </p>
     * <p>
     * If given an empty array as the parameter, empty array would be returned.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_2() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new long[] {});

        // Asserts.
        assertEquals("Empty array would be returned.", 0, users.length);
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(long[]).
     * </p>
     * <p>
     * If ids is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_NullIds() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsers((long[]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(long[]).
     * </p>
     * <p>
     * If any entry is not positive in ids, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_LongArray_NotPositiveEntry() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsers(new long[] {1, 0, 4});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(long[]).
     * </p>
     * <p>
     * If there is no default Connection and connName, connection could not be created, RetrievalException would be
     * thrown.
     * </p>
     */
    public void testRetrieveUsers_LongArray_NoDefaultConnectionAndConnName() {

        try {
            UnitTestHelper.addConfig("FailureConfig.xml");
        } catch (Exception e) {
            // Will never happen.
        }

        try {
            defaultDBUserRetrieval = new MockDBUserRetrieval(
                    "com.cronos.onlinereview.external.NoDefaultConnAndConnName");
        } catch (ConfigException e) {
            // Will never happen.
        }

        try {
            defaultDBUserRetrieval.retrieveUsers(new long[] {1001, 1002});
            fail("RetrievalException should be thrown.");
        } catch (RetrievalException e1) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(long).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_Long() throws RetrievalException {

        ExternalUser user = defaultDBUserRetrieval.retrieveUser(1002);

        // Asserts.
        assertEquals("There are two alternative email.", 2, user.getAlternativeEmails().length);
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
     * Tests the failure of the retrieveUser(long).
     * </p>
     * <p>
     * If id is not positive, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_Long_NotPositive() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUser(-1);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(String[]).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_1() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new String[] {"Handle A", "Handle C", "Handle Z"});

        // Asserts.
        assertEquals("Two records would be got.", 2, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsers(String[]).
     * </p>
     * <p>
     * If given an empty array as the parameter, empty array would be returned.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_2() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsers(new String[] {});

        // Asserts.
        assertEquals("Empty array would be returned.", 0, users.length);
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(String[]).
     * </p>
     * <p>
     * If the array is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_NullArray() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsers((String[]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(String[]).
     * </p>
     * <p>
     * If any entry in the array is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArray_NullEntry() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsers(new String[] {null, "Handle C"});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsers(String[]).
     * </p>
     * <p>
     * If any entry in the array is empty, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_StringArrayStringArray_EmptyEntry() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsers(new String[] {"  ", "Handle C"});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUser(String).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_String() throws RetrievalException {

        ExternalUser user = defaultDBUserRetrieval.retrieveUser("Handle B");

        // Asserts.
        assertEquals("There are two alternative emails.", 2, user.getAlternativeEmails().length);
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
     * Tests the failure of the retrieveUser(String).
     * </p>
     * <p>
     * If the string parameter is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUser_String_NullHandle() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUser(null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUser(String).
     * </p>
     * <p>
     * If the string parameter is empty, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsers_String_EmptyHandle() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUser("  ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersIgnoreCase(String[]).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCase_StringArray_1() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersIgnoreCase(new String[] {"hAndLe a"});

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
     * <p>
     * If given an empty array as the parameter, empty array would be returned.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCase_StringArray_2() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersIgnoreCase(new String[] {});

        // Asserts.
        assertEquals("Empty array would be returned.", 0, users.length);
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsersIgnoreCase(String[]).
     * </p>
     * <p>
     * If the array is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCaseStringArray_NullArray() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsersIgnoreCase((String[]) null);
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsersIgnoreCase(String[]).
     * </p>
     * <p>
     * If any entry in the array is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCaseStringArray_NullEntry() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsersIgnoreCase(new String[] {null, "haNdlE C"});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsersIgnoreCase(String[]).
     * </p>
     * <p>
     * If any entry in the array is empty, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersIgnoreCaseStringArrayStringArray_EmptyEntry() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsersIgnoreCase(new String[] {"  ", "HAnDle c"});
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     * <p>
     * There do have some records can be found.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString1() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersByName("First B", "Last B");

        assertEquals("There is only one user.", 1, users.length);
        ExternalUser user = users[0];

        // Asserts.
        assertEquals("There are two alternative emails.", 2, user.getAlternativeEmails().length);
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
     * <p>
     * Blur search.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString2() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersByName("    ", "Last B");

        assertEquals("There is only one user.", 1, users.length);
    }

    /**
     * <p>
     * Tests the accuracy of the retrieveUsersByName(String, String).
     * </p>
     * <p>
     * No record can be found.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString3() throws RetrievalException {

        ExternalUser[] users = defaultDBUserRetrieval.retrieveUsersByName("    ", "AAAA");

        assertEquals("There is no user.", 0, users.length);
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsersByName(String, String).
     * </p>
     * <p>
     * If the first name given is null, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString_NullFirstName() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsersByName(null, "C");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the failure of the retrieveUsersByName(String, String).
     * </p>
     * <p>
     * If the two names given are both empty, IllegalArgumentException should be thrown.
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     */
    public void testRetrieveUsersByName_StringString_AllNameEmpty() throws RetrievalException {

        try {
            defaultDBUserRetrieval.retrieveUsersByName(" ", "   ");
            fail("IllegalArgumentException should be thrown.");
        } catch (IllegalArgumentException e) {
            // Success.
        }
    }

    /**
     * <p>
     * Tests the accuracy of the createObject(ResultSet).
     * </p>
     *
     * @throws RetrievalException
     *             this exception would never be thrown in this test case.
     * @throws SQLException
     *             this exception would never be thrown in this test case.
     */
    public void testCreateObject_ResultSet() throws RetrievalException, SQLException {

        // Prepares the ps and rs.
        PreparedStatement ps = defaultConnection.prepareStatement("Select "
                + " u.user_id id, u.first_name, u.last_name, u.handle, e.address " + " from user u, email e "
                + " where u.user_id = 1002 and e.primary_ind = 1 and u.user_id = e.user_id");
        ResultSet rs = ps.executeQuery();

        // Creates object.
        ExternalUser user = null;
        while (rs.next()) {
            user = (ExternalUser) defaultDBUserRetrieval.createObject(rs);
        }

        // Asserts.
        assertEquals("The email address should be the same.", "User2@gmail.com", user.getEmail());
        assertEquals("The first name should be the same.", "First B", user.getFirstName());
        assertEquals("The last name should be the same.", "Last B", user.getLastName());
        assertEquals("The handle should be the same.", "Handle B", user.getHandle());
        assertEquals("The user id should be the same.", 1002, user.getId());
    }
}
