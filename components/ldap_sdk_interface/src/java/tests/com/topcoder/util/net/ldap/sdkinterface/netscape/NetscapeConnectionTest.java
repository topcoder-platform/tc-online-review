/*
 * TCS LDAP SDK Interface 1.0
 *
 * NetscapeConnectionTest.java
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 *
 */
package com.topcoder.util.net.ldap.sdkinterface.netscape;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.framework.Test;

import com.topcoder.util.net.ldap.sdkinterface.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Hashtable;

/**
 * A test case testing the behavior of <code>Netscape</code> class. In order to run this test the running LDAP server is
 * required. The values of <code>VALID_LDAP_HOST</code> and <code>VALID_LDAP_PORT</code> should be set to form the URL
 * of such a LDAP server. The machine running the test should be SSL-enabled.
 * As for now some tests testing the SSL-connections fail with unexpected error due to abscence of classes from
 * <code>netscape.net</code> package. These tests should execute successfully if such classes are available. The
 * corresponding lines should be uncommented.
 *
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class NetscapeConnectionTest extends TestCase {

    /**
     * A <code>String</code> containing the hostname where the LDAP server is running. This string should not be started
     * with "ldap://" protocol scheme, just a host name.
     */
    private static final String VALID_LDAP_HOST ="127.0.0.1";// "192.168.0.233";

    /**
     * An <code>int</code> containing the port which the LDAP server is listening to.
     */
    private static final int VALID_LDAP_PORT = 389;

    /**
     * A <code>String</code> containing the hostname of non-existing LDAP server is running. This string should not be
     * started with "ldap://" protocol scheme, just a host name.
     */
    private static final String INVALID_LDAP_HOST = "acme.acme";

    /**
     * An <code>int</code> containing the port which the LDAP server is not listening to.
     */
    private static final int INVALID_LDAP_PORT = 1389;

    /**
     * A <code>String</code> DN used to connect to LDAP server.
     */
    private static final String CONNECT_DN = "cn=Manager,dc=my-domain,dc=com";//"cn=jinlimin,c=CN";

    /**
     * A <code>String</code> passowrd used to connect to LDAP server.
     */
    private static final String CONNECT_PASSWORD ="secret";// "qazwsx";

    /**
     * A <code>String</code> name of a sample LDAP entry to be used in testing.
     */
    private static final String ENTRY_NAME = "cn=TCSDEVELOPER";

    /**
     * A <code>String</code> new name of a sample LDAP entry to be used in testing.
     */
    private static final String NEW_ENTRY_NAME = "cn=foo";

    /**
     * A <code>String</code> value of "cn" attribute of LDAP entry to be used for testing.
     */
    private static final String CN_VALUE = "TCSDEVELOPER";

    /**
     * A new <code>String</code> value of "cn" attribute of LDAP entry to be used for testing after the entry is
     * renamed.
     */
    private static final String NEW_CN_VALUE = "foo";

    /**
     * A <code>String</code> value of "sn" attribute of LDAP entry to be used for testing.
     */
    private static final String SN_VALUE = "Istomin";

    /**
     * A <code>String</code> base DN to be used for testing.
     */
    private static final String BASE_DN = "dc=my-domain,dc=com";//"o=TopCoder,c=CN";

    /**
     * A connection to LDAP server that may be used to perform the LDAP operations within test cases.
     */
    private NetscapeConnection con = null;

    /**
     * Constructs new <code>NetscapeConnectionTest</code> with specified name.
     *
     * @param testName
     */
    public NetscapeConnectionTest(String testName) {
        super(testName);
    }

    /**
     * Tests the non-argument default constructor.  Since this is a default constructor the test simply verifies that
     * nothing prevents the instantiation of <code>NetscapeConnection</code> object.
     */
    public void testConstructor() {
        NetscapeConnection conn = null;
        try {
            conn = new NetscapeConnection();
        } catch (Exception e) {
            fail("There shouldn't be any reasons preventing the successful instantiation of NetscapeConneciton");
        }
    }

    /**
     * Tests the <code>connect(String, int)</code> method. Verifies that the connection to active LDAP server can be
     * successfully established. In fact this is verified by successful execution of <code>setUp()</code> method where
     * the NetscapeConnection object is created and the connection to running LDAP server is established.
     *
     * Also tests the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodConnect_String_int() throws LDAPSDKException {

        // Tests the invalid arguments handling
        try {
            con.connect((String) null, 389);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.connect(VALID_LDAP_HOST, -1);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.connect(VALID_LDAP_HOST, 90000);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.connect("", 389);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.connect("    ", 389);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.connect(INVALID_LDAP_HOST, INVALID_LDAP_PORT);
            fail("A LDAPSDKCommuuncationException should be thrown ");
        } catch (LDAPSDKCommunicationException e) {
        }
    }

    /**
     * Tests the <code>createLDAPConnection(boolean)</code> method. Simply verifies that the method always returns
     * successfully. Performs the test for non-SSL and SSL-connections.
     */
    public void testMethodCreateLDAPConnection_boolean() {

        try {
            con.createLDAPConnection(false);
        } catch (Exception e) {
            fail("Nothing should prevent the creation of LDAP SDK Connection");
        }

        try {
            con.createLDAPConnection(true);
        } catch (Exception e) {
            fail("Nothing should prevent the creation of LDAP SDK Connection");
        }

        try {
            con.createLDAPConnection(false);
        } catch (Exception e) {
            fail("Nothing should prevent the creation of LDAP SDK Connection");
        }

        try {
            con.createLDAPConnection(true);
        } catch (Exception e) {
            fail("Nothing should prevent the creation of LDAP SDK Connection");
        }
    }

    /**
     * Tests the <code>rename(String, String, boolean)</code> method. Adds a sample entry to the LDAP server, then
     * renames it, reads it back using the new DN and verifies that the entry under new name exists but the entry with
     * old name does not exist. Also tests the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodRenameEntry_String_String_boolean() throws LDAPSDKException {

        // Tests the behavior of target method
        con.renameEntry(ENTRY_NAME + "," + BASE_DN, NEW_ENTRY_NAME, true);

        Entry entry = null;

        try {
            entry = con.readEntry(NEW_ENTRY_NAME + "," + BASE_DN);
        } catch (LDAPSDKNoSuchObjectException e) {
            fail("The entry should be successfully renamed : " + e);
        }

        // Tries to read the old entry back from the server and checks that the entry no longer exist
        try {
            entry = con.readEntry(ENTRY_NAME + "," + BASE_DN);
            fail("LDAPSDKNoSuchObjectException should be thrown");
        } catch(LDAPSDKNoSuchObjectException e) {}

        // Tests the invalid arguments handling
        try {
            con.renameEntry((String) null, "a", false);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry("a", (String) null, false);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry((String) null, (String) null, false);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry((String) null, "a", true);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry("a", (String) null, true);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry((String) null, (String) null, true);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.renameEntry("", "a", false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("     ", "a", false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("a", "", false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("a", "    ", false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("", "     ", false);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("", "a", true);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("     ", "a", true);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("a", "", true);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("a", "    ", true);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.renameEntry("", "     ", true);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests the <code>deleteEntry(String)</code> method. Verifies that existing entry can be successfully deleted
     * and does not exist after deletion. First creates and adds an entry to LDAP server and then deletes it
     *
     * Also tests the invalid arguments handing.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodDeleteEntry_String() throws LDAPSDKException {

        con.deleteEntry(NEW_ENTRY_NAME + "," + BASE_DN);

        // Tries to read the entry back from the server and checks that the entry was deleted
        try {
            Entry entry = con.readEntry(NEW_ENTRY_NAME);
            fail("LDAPSDKNoSuchObjectException should be thrown");
        } catch(LDAPSDKNoSuchObjectException e) {}

        // Tests the invalid arguments handling
        try {
            con.deleteEntry((String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.deleteEntry("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.deleteEntry("                ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests the <code>readEntry(String, String[])</code> method. Verifies that existing entries can be read and contain
     * correct attribute values. Adds a new entry to the LDAP server and then reads it back providing the lists of
     * requested attributes and verifies that attribute values are correct. Verifies that entry contains only an
     * attributes containing within attribute names array. Also tests the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodReadEntry_String_StringArray() throws LDAPSDKException {
        Entry entry = getTestEntry();

        // Reads the entry back and verifies that specified attributes are equal and non-requested attributes are not
        // read from the server
        try {
            String[] attributeNames = new String[]{"cn"};
            Entry sameEntry = con.readEntry(ENTRY_NAME + "," + BASE_DN, attributeNames);
            assertEquals("The DN of the entry should be the same", entry.getDn(), sameEntry.getDn());
            assertEquals("The attribute value should be the same",
                    entry.getValues("cn").getTextValues(), sameEntry.getValues("cn").getTextValues());
            try {
                sameEntry.getValues("sn");
                fail("IllegalArgumentException should be thrown since the attribute was not requested for retrieval.");
            } catch (IllegalArgumentException e) {
            }

            attributeNames = new String[]{"sn"};
            sameEntry = con.readEntry(ENTRY_NAME + "," + BASE_DN, attributeNames);
            assertEquals("The DN of the entry should be the same", entry.getDn(), sameEntry.getDn());
            assertEquals("The attribute value should be the same",
                    entry.getValues("sn").getTextValues(), sameEntry.getValues("sn").getTextValues());
            try {
                sameEntry.getValues("cn");
                fail("IllegalArgumentException should be thrown since the attribute was not requested for retrieval.");
            } catch (IllegalArgumentException e) {
            }

        } catch (LDAPSDKNoSuchObjectException e) {
            fail("The existing entry should be read successfully : " + e);
        }

        // Tests invalid arguments handling
        String[] attributeNames = new String[]{"sn", "cn"};

        try {
            con.readEntry((String) null, attributeNames);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.readEntry("", attributeNames);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.readEntry("                ", attributeNames);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.readEntry(ENTRY_NAME, (String[]) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Tests the <code>readEntry(String)</code> method. Verifies that existing entries can be read and contain
     * correct attribute values. Adds a new entry to the LDAP server and then reads it back and verifies that attribute
     * values are correct. Also tests the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodReadEntry_String() throws LDAPSDKException {

        Entry entry = getTestEntry();

        // Reads the entry back and verifies that specified attributes are equal
        try {
            Entry sameEntry = con.readEntry(ENTRY_NAME + "," + BASE_DN);
            assertEquals("The DN of the entry should be the same", entry.getDn(), sameEntry.getDn());
            assertEquals("The attribute value should be the same",
                    entry.getValues("cn").getTextValues(), sameEntry.getValues("cn").getTextValues());
            assertEquals("The attribute value should be the same",
                    entry.getValues("sn").getTextValues(), sameEntry.getValues("sn").getTextValues());

        } catch (LDAPSDKNoSuchObjectException e) {
            fail("The existing entry should be read successfully : " + e);
        }

        // Tests invalid arguments handling
        try {
            con.readEntry((String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.readEntry("");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.readEntry("                ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests the <code>authenticate(String, String)</code> method. Verifies that given a valid DN and password the
     * connection to the running LDAP server can be established.  In fact this is verified by successful execution of
     * <code>setUp()</code> method where the NetscapeConnection object is created and the connection to running LDAP
     * server is established. Also verifies the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodAuthenticate_String_String() throws LDAPSDKException {
        NetscapeConnection con = null;

        con = new NetscapeConnection();

        con.createLDAPConnection(false);
        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
        con.bind(CONNECT_DN, CONNECT_PASSWORD);
        con.disconnect();


        /* Following section should be uncommented once the classes from netscape.net package become available */
//        con.createLDAPConnection(true);
//        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
//        con.bind(CONNECT_DN, CONNECT_PASSWORD);
//        con.disconnect();

        // Tests the invalid arguments handling
        try {
            con.authenticate((String) null, "a");
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.authenticate("a", (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.authenticate((String) null, (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.authenticate("", "a");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticate("     ", "a");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticate("a", "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticate("a", "    ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticate("", "     ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

    }

    /**
     * Tests the <code>authenticateSASL(String, String[], Hashtable, Object)</code> method. In order to run this test
     * successfully the following requirements should be met:
     * <ul>
     *    <li>the LDAP server must support at least one SASL mechanism</li>
     *    <li>your client environment must support at least one of the SASL mechanisms supported by the server</li>
     * </ul>
     */
    public void testMethodAuthenticateSASL_String_StringArray_Hashtable_Object() throws LDAPSDKException {

        // Tests invalid arguments handling
        Object obj = new Object();
        String[] mechs = new String[]{"mechanism1", "mechanism2"};
        Hashtable props = new Hashtable();

        try {
            con.authenticateSASL("", mechs, props, obj);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticateSASL(" ", mechs, props, obj);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.authenticateSASL(BASE_DN, (String[]) null, props, obj);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.authenticateSASL(BASE_DN, mechs, (Hashtable) null, obj);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.authenticateSASL(BASE_DN, mechs, props, (Object) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Tests the <code>bind(String, String)</code> method. Verifies that given a valid DN and password the
     * connection to the running LDAP server can be established. Also verifies the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodBind_String_String() throws LDAPSDKException {
        NetscapeConnection con = null;

        con = new NetscapeConnection();

        con.createLDAPConnection(false);
        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
        con.bind(CONNECT_DN, CONNECT_PASSWORD);
        con.disconnect();

        /* Following section should be uncommented once the classes from netscape.net package become available */
//        con.createLDAPConnection(true);
//        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
//        con.bind(CONNECT_DN, CONNECT_PASSWORD);
//        con.disconnect();

        // Tests the invalid arguments handling
        try {
            con.bind((String) null, "a");
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.bind("a", (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.bind((String) null, (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.bind("", "a");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bind("     ", "a");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bind("a", "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bind("a", "    ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bind("", "     ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests the <code>disconnect()</code> method. Simply verifies that nothing prevents the disconnection from LDAP
     * server during normal functioning.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodDisconnect() throws LDAPSDKException {
        NetscapeConnection conn = null;

        conn = new NetscapeConnection();

        conn.createLDAPConnection(false);
        conn.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);

        try {
            conn.disconnect();
        } catch (LDAPSDKException e) {
            fail("Nothing should prevent the disconnection from LDAP server during normal functioning.");
        }

        /* Following section should be uncommented once the classes from netscape.net package become available */
//        conn.createLDAPConnection(true);
//        conn.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
//
//        try {
//            conn.disconnect();
//        } catch(LDAPSDKException e) {
//            fail("Nothing should prevent the disconnection from LDAP server during normal functioning.");
//        }
    }

    /**
     * Tests the <code>updateEntry(String, Update)</code> method.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodUpdateEntry_String_Update() throws LDAPSDKException {

        Entry entry = null;
        Update update = null;

        update = new Update();
        update.add("description", new Values("password"));
        con.updateEntry(NEW_ENTRY_NAME + "," + BASE_DN, update);
        entry = con.readEntry(NEW_ENTRY_NAME + "," + BASE_DN);
        assertEquals("The new attribute should be added",
                entry.getValues("description").getTextValues().get(0), "password");

        update = new Update();
        update.replace("description", new Values("new password"));
        con.updateEntry(NEW_ENTRY_NAME + "," + BASE_DN, update);
        entry = con.readEntry(NEW_ENTRY_NAME + "," + BASE_DN);
        assertEquals("The existing attribute should be replaced",
                entry.getValues("description").getTextValues().get(0), "new password");

        update = new Update();
        update.delete("description");
        con.updateEntry(NEW_ENTRY_NAME + "," + BASE_DN, update);
        entry = con.readEntry(NEW_ENTRY_NAME + "," + BASE_DN);

        try {
            entry.getValues("description");
            fail("The attribute should be removed");
        } catch (IllegalArgumentException e) {
        }

        // Tests invalid arguments handling

        try {
            con.updateEntry((String) null, update);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.updateEntry(" ", update);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.updateEntry(BASE_DN, (Update) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Tests the <code>authenticateAnonymous()</code> method</code>. SImply verifies that the NetscapeConnection can
     * authenticate anonymously to LDAP server after the connection is successfully established. Tests both non-SSL and
     * SSL connections.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodAuthenticateAnonymous() throws LDAPSDKException {
        NetscapeConnection con = null;

        con = new NetscapeConnection();

        con.createLDAPConnection(false);
        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
        con.bindAnonymous();
        con.disconnect();

        /* Following section should be uncommented once the classes from netscape.net package become available */
//        con.createLDAPConnection(true);
//        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
//        con.bindAnonymous();
//        con.disconnect();
    }

    /**
     * Tests the <code>bindAnonymous()</code> method</code>. SImply verifies that the NetscapeConnection can bind
     * anonymously to LDAP server after the connection is successfully established. Tests both non-SSL and
     * SSL connections.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodBindAnonymous() throws LDAPSDKException {
        NetscapeConnection con = null;

        con = new NetscapeConnection();

        con.createLDAPConnection(false);
        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
        con.bindAnonymous();
        con.disconnect();

        /* Following section should be uncommented once the classes from netscape.net package become available */
//        con.createLDAPConnection(true);
//        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
//        con.bindAnonymous();
//        con.disconnect();
    }

    /**
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodBindSASL_String_StringArray_Hashtable_Object() throws LDAPSDKException {

        // Tests invalid arguments handling
        Object obj = new Object();
        String[] mechs = new String[]{"mechanism1", "mechanism2"};
        Hashtable props = new Hashtable();

        try {
            con.bindSASL("", mechs, props, obj);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bindSASL(" ", mechs, props, obj);
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.bindSASL(BASE_DN, (String[]) null, props, obj);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.bindSASL(BASE_DN, mechs, (Hashtable) null, obj);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.bindSASL(BASE_DN, mechs, props, (Object) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }
    }

    /**
     * Tests the <code>search(String, int, String, String[])</code> method. Tests that existing LDAP entries are found
     * and that an <code>Iterator</code> returns an instances of <code>Entry</code> class.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodSearch_String_int_String_StringArray() throws LDAPSDKException {
        Iterator iterator = con.search(NEW_ENTRY_NAME + "," + BASE_DN, LDAPSDKConnection.SCOPE_SUB,
                "(objectClass=*)", new String[]{"cn"});

        while (iterator.hasNext()) {
            Object next = iterator.next();

            assertTrue("The iterator should return the instances of Entry class", next instanceof Entry);

            Entry entry = (Entry) next;
            assertEquals("The search method should return the correct attribute value",
                    NEW_CN_VALUE, entry.getValues("cn").getTextValues().get(0));

            try {
                entry.getValues("sn");
                fail("The search should return only requested attributes");
            } catch (IllegalArgumentException e) {
            }
        }

        iterator = con.search(NEW_ENTRY_NAME + "," + BASE_DN, LDAPSDKConnection.SCOPE_ONE,
                "(objectClass=*)", new String[]{"cn"});

        while (iterator.hasNext()) {
            Object next = iterator.next();

            assertTrue("The iterator should return the instances of Entry class", next instanceof Entry);

            Entry entry = (Entry) next;
            assertEquals("The search method should return the correct attribute value",
                    NEW_CN_VALUE, entry.getValues("cn").getTextValues().get(0));
            try {
                entry.getValues("sn");
                fail("The search should return only requested attributes");
            } catch (IllegalArgumentException e) {
            }
        }

        iterator = con.search(NEW_ENTRY_NAME + "," + BASE_DN, LDAPSDKConnection.SCOPE_BASE,
                "(objectClass=*)", new String[]{"cn"});

        while (iterator.hasNext()) {
            Object next = iterator.next();

            assertTrue("The iterator should return the instances of Entry class", next instanceof Entry);

            Entry entry = (Entry) next;
            assertEquals("The search method should return the correct attribute value",
                    NEW_CN_VALUE, entry.getValues("cn").getTextValues().get(0));
            try {
                entry.getValues("sn");
                fail("The search should return only requested attributes");
            } catch (IllegalArgumentException e) {
            }
        }

        // Tests the invalid arguments handling
        try {
            con.search(BASE_DN, -1, "(sn=sn1)", new String[]{"cn"});
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB + LDAPSDKConnection.SCOPE_BASE
                    + LDAPSDKConnection.SCOPE_ONE, "(sn=sn1)", new String[]{"cn"});
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search((String) null, LDAPSDKConnection.SCOPE_SUB, "(sn=sn1)", new String[]{"cn"});
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search((String) null, LDAPSDKConnection.SCOPE_SUB, (String) null, new String[]{"cn"});
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, (String) null, new String[]{"cn"});
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, "(sn=sn1)", (String[]) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search("", LDAPSDKConnection.SCOPE_SUB, "(sn=sn1)", new String[]{"cn"});
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search("", LDAPSDKConnection.SCOPE_SUB, "", new String[]{"cn"});
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, "        ", new String[]{"cn"});
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Tests the <code>search(String, int, String)</code> method.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodSearch_String_int_String() throws LDAPSDKException {

        Iterator iterator = null;

        iterator = con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, "(objectClass=*)");

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());

        while (iterator.hasNext()) {
            Object next = iterator.next();
            assertTrue("The iterator should return an instance of Entry", next instanceof Entry);
        }

        iterator = con.search(BASE_DN, LDAPSDKConnection.SCOPE_ONE, "(objectClass=*)");

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());

        while (iterator.hasNext()) {
            Object next = iterator.next();
            assertTrue("The iterator should return an instance of Entry", next instanceof Entry);
        }

        iterator = con.search(BASE_DN, LDAPSDKConnection.SCOPE_BASE, "(objectClass=*)");

        assertNotNull(iterator);
        assertTrue(iterator.hasNext());

        while (iterator.hasNext()) {
            Object next = iterator.next();
            assertTrue("The iterator should return an instance of Entry", next instanceof Entry);
        }

        //tests invalid argument handling
        try {
            con.search(BASE_DN, -1, "(sn=sn1)");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search((String) null, LDAPSDKConnection.SCOPE_SUB, "(sn=sn1)");
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search((String) null, LDAPSDKConnection.SCOPE_SUB, (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, (String) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }

        try {
            con.search("", LDAPSDKConnection.SCOPE_SUB, "(sn=sn1)");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search("", LDAPSDKConnection.SCOPE_SUB, "");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

        try {
            con.search(BASE_DN, LDAPSDKConnection.SCOPE_SUB, "        ");
            fail("IllegalArgumentException should be thrown");
        } catch (IllegalArgumentException e) {
        }

    }

    /**
     * Tests the <code>addEntry(Entry)</code> method. Verifies that an entry is successfully added. First adds the entry
     * and then reads it back verifying that the entry exists and contains an attribute values as specified during
     * entry addition. Also checks the invalid arguments handling.
     *
     * @throws LDAPSDKException if an LDAP server is not accessible. This exception is not a result of test failure.
     */
    public void testMethodAddEntry_Entry() throws LDAPSDKException {

        Entry entry = getTestEntry();
        con.addEntry(entry);

        Entry readEntry = con.readEntry(ENTRY_NAME + "," + BASE_DN);
        assertEquals("The added entry should be succesfully read back and both entries should be equal",
                entry.getValues("cn").getTextValues(), readEntry.getValues("cn").getTextValues());
        assertEquals("The added entry should be succesfully read back and both entries should be equal",
                entry.getValues("sn").getTextValues(), readEntry.getValues("sn").getTextValues());


        // Tests the invalid arguments handling
        try {
            con.addEntry((Entry) null);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
        }
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new NetscapeConnectionTest("testConstructor"));
        suite.addTest(new NetscapeConnectionTest("testMethodCreateLDAPConnection_boolean"));
        suite.addTest(new NetscapeConnectionTest("testMethodConnect_String_int"));
        suite.addTest(new NetscapeConnectionTest("testMethodAuthenticate_String_String"));
        suite.addTest(new NetscapeConnectionTest("testMethodAuthenticateAnonymous"));
        suite.addTest(new NetscapeConnectionTest("testMethodAuthenticateSASL_String_StringArray_Hashtable_Object"));
        suite.addTest(new NetscapeConnectionTest("testMethodBind_String_String"));
        suite.addTest(new NetscapeConnectionTest("testMethodBindAnonymous"));
        suite.addTest(new NetscapeConnectionTest("testMethodBindSASL_String_StringArray_Hashtable_Object"));
        suite.addTest(new NetscapeConnectionTest("testMethodAddEntry_Entry"));
        suite.addTest(new NetscapeConnectionTest("testMethodReadEntry_String"));
        suite.addTest(new NetscapeConnectionTest("testMethodReadEntry_String_StringArray"));
        suite.addTest(new NetscapeConnectionTest("testMethodRenameEntry_String_String_boolean"));
        suite.addTest(new NetscapeConnectionTest("testMethodSearch_String_int_String"));
        suite.addTest(new NetscapeConnectionTest("testMethodSearch_String_int_String_StringArray"));
        suite.addTest(new NetscapeConnectionTest("testMethodUpdateEntry_String_Update"));
        suite.addTest(new NetscapeConnectionTest("testMethodDeleteEntry_String"));
        suite.addTest(new NetscapeConnectionTest("testMethodDisconnect"));
        return suite;
    }

    /**
     * The initialization of the test case. A <code>NetscapeConnection</code> object to be used in the test cases is
     * initialized and a connection to
     *
     * @throws Exception
     */
    protected void setUp() throws Exception {
        super.setUp();

        con = new NetscapeConnection();
        con.createLDAPConnection(false);
        con.connect(VALID_LDAP_HOST, VALID_LDAP_PORT);
        con.authenticate(CONNECT_DN, CONNECT_PASSWORD);
    }

    /**
     * Releases the connection to LDAP server after execution of a test.
     *
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        con.disconnect();
    }

    /**
     * A helper method producing the LDAP entry to be used in tests.
     *
     * @return a pre-configured <code>Entry</code> object to be used in tests.
     */
    private Entry getTestEntry() {
        Map attributes = new HashMap();
        attributes.put("objectClass", new Values("person"));
        attributes.put("cn", new Values(CN_VALUE));
        attributes.put("sn", new Values(SN_VALUE));

        Entry entry = new Entry(ENTRY_NAME + "," + BASE_DN);
        entry.setAttributes(attributes);

        return entry;
    }
}
