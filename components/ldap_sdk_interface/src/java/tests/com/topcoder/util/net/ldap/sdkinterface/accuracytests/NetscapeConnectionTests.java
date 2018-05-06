/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.accuracytests;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKNoSuchObjectException;
import com.topcoder.util.net.ldap.sdkinterface.Values;
import com.topcoder.util.net.ldap.sdkinterface.Update;
import com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Tests NetscapeConnection implementation of LDAPSDKConnection
 *
 * Note: need to replace constants before running these tests
 *
 * @author BryanChen
 * @version 1.0
 */
public class NetscapeConnectionTests extends TestCase {

    /** our LDAP connection */
    private LDAPSDKConnection con;

    /** DN to login to our LDAP server */
    private static final String DN = "cn=Manager,dc=my-domain,dc=com";//"cn=root";

    /** password to use to login */
    private static final String PASSWORD = "secret";

    /** the base DN to use to add our test entries to */
    private static final String BASEDN = "dc=my-domain,dc=com";//"dc=austin,dc=rr,dc=com";

    /** hostname of our LDAP server */
    private static final String HOST = "localhost";

    /** port the LDAP server is listening on for connections */
    private static final int PORT = 389;//17353;

    /**
     * Sets up the connection
     * @throws Exception exception is propogated to JUnit
     */
    protected void setUp() throws Exception {
        con = new NetscapeFactory().createConnection();
        con.connect(HOST, PORT);
        con.authenticate(DN, PASSWORD);
        
       
    }

    /**
     * Tests that disconnection behaves without problems
     * @throws Exception exception is propogated to JUnit
     */
    public void testDisconnection() throws Exception {
        con.disconnect();
        // should be fine as long as it doesn't throw any exceptions
    }

    /**
     * Tests adding an entry to the LDAP server
     * Uses:
     *   readEntry, deleteEntry, addEntry
     * @throws Exception
     */
    public void testAddEntry() throws Exception {
        Entry entry;
        String str = "ou=blah1";

        // check if the entry exists, if so, delete it so we can add
        try {
            entry = con.readEntry(str + "," + BASEDN);
            con.deleteEntry(str + "," + BASEDN);
        } catch (Exception e) {
            // we're good to add
        }
        entry = new Entry();
        Values value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"blah1"});
        entry.setAttribute("ou", value);
        entry.setDn(str + "," + BASEDN);
        con.addEntry(entry);

        Entry entry2 = con.readEntry(str + "," + BASEDN);
        assertEquals("ou=blah1," + BASEDN, entry2.getDn());
        assertEquals("blah1", entry2.getValues("ou").getTextValues().get(0));
        assertTrue(entry2.getValues("objectClass").getTextValues().contains("organizationalUnit"));
        assertTrue(entry2.getValues("objectClass").getTextValues().contains("top"));
        con.deleteEntry(str + "," + BASEDN);
    }

    /**
     * Tests adding multiple entries to an LDAP tree
     *   uses: readEntry, deleteEntry, addEntry
     * @throws Exception exception is propogated to JUnit
     */
    public void testAddMultipleEntries() throws Exception {
        Entry entry;
        String str = "ou=blah";

        // check if the entry exists, if so, delete it so we can add
        try {
            entry = con.readEntry(str + "," + BASEDN);
            
            con.deleteEntry("ou=foo," + str + "," + BASEDN);
            con.deleteEntry("ou=bar," + str + "," + BASEDN);
            con.deleteEntry(str + "," + BASEDN);
        } catch (LDAPSDKNoSuchObjectException e) {
            // we're good to add
        }
        entry = new Entry();
        Values value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"blah"});
        entry.setAttribute("ou", value);
        entry.setDn(str + "," + BASEDN);
        con.addEntry(entry);

        entry = new Entry();
        value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"foo"});
        entry.setAttribute("ou", value);
        entry.setDn("ou=foo," + str + "," + BASEDN);
        con.addEntry(entry);

        entry = new Entry();
        value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"bar"});
        entry.setAttribute("ou", value);
        entry.setDn("ou=bar," + str + "," + BASEDN);
        con.addEntry(entry);

        entry = con.readEntry(str + "," + BASEDN);
        assertEquals("ou=blah," + BASEDN, entry.getDn());
        assertEquals("blah", entry.getValues("ou").getTextValues().get(0));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("organizationalUnit"));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("top"));

        entry = con.readEntry("ou=foo," + str + "," + BASEDN);
        assertEquals("ou=foo," + str + "," + BASEDN, entry.getDn());
        assertEquals("foo", entry.getValues("ou").getTextValues().get(0));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("organizationalUnit"));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("top"));

        entry = con.readEntry("ou=bar," + str + "," + BASEDN);
        assertEquals("ou=bar," + str + "," + BASEDN, entry.getDn());
        assertEquals("bar", entry.getValues("ou").getTextValues().get(0));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("organizationalUnit"));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("top"));

        entry = con.readEntry(str + "," + BASEDN);
        assertEquals("ou=blah," + BASEDN, entry.getDn());
        assertEquals("blah", entry.getValues("ou").getTextValues().get(0));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("organizationalUnit"));
        assertTrue(entry.getValues("objectClass").getTextValues().contains("top"));

        // clean up
        con.deleteEntry("ou=foo," + str + "," + BASEDN);
        con.deleteEntry("ou=bar," + str + "," + BASEDN);
        con.deleteEntry(str + "," + BASEDN);
    }

    /**
     * Tests the ability to update an entry in the LDAP tree
     *   uses: updateEntry, readEntry, deleteEntry, addEntry
     * @throws Exception
     */
    public void testUpdateEntry() throws Exception {
        Entry entry;
        String str = "ou=blah";

        // check if the entry exists, if so, delete it so we can add
        try {
            entry = con.readEntry(str + "," + BASEDN);
            con.deleteEntry("ou=foo," + str + "," + BASEDN);
            con.deleteEntry("ou=bar," + str + "," + BASEDN);
            con.deleteEntry(str + "," + BASEDN);
        } catch (LDAPSDKNoSuchObjectException e) {
            // we're good to add
        }
        entry = new Entry();
        Values value = new Values();
        value.setTextValues(new String[]{"organizationalUnit"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"blah"});
        entry.setAttribute("ou", value);
        entry.setDn(str + "," + BASEDN);
        con.addEntry(entry);

        entry = con.readEntry(str + "," + BASEDN);
        assertEquals("blah", entry.getValues("ou").getTextValues().get(0));

        Update update = new Update();
        value = new Values();
        value.setTextValues(new String[]{"this is an object"});
        update.add("description", value);
        con.updateEntry(str + "," + BASEDN, update);
        entry = con.readEntry(str + "," + BASEDN);
        assertEquals("this is an object", entry.getValues("description").getTextValues().get(0));
        con.deleteEntry(str + "," + BASEDN);
    }

    /**
     * Tests the search functionality of the LDAP component
     *   uses: search, readEntry, deleteEntry, addEntry
     */
    public void testSearch() throws Exception {

        Entry entry;
        String str = "ou=blah";

        // check if the entry exists, if so, delete it so we can add
        try {
            entry = con.readEntry(str + "," + BASEDN);
            con.deleteEntry("ou=foo," + str + "," + BASEDN);
            con.deleteEntry("ou=bar," + str + "," + BASEDN);
            con.deleteEntry(str + "," + BASEDN);
            
        } catch (LDAPSDKNoSuchObjectException e) {
            // we're good to add
        }
        // create our 3 entries
        entry = new Entry();
        Values value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"blah"});
        entry.setAttribute("ou", value);
        entry.setDn(str + "," + BASEDN);
        con.addEntry(entry);

        entry = new Entry();
        value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"foo"});
        entry.setAttribute("ou", value);
        entry.setDn("ou=foo," + str + "," + BASEDN);
        con.addEntry(entry);

        entry = new Entry();
        value = new Values();
        value.setTextValues(new String[]{"organizationalUnit","top"});
        entry.setAttribute("objectClass", value);
        value = new Values();
        value.setTextValues(new String[]{"bar"});
        entry.setAttribute("ou", value);
        entry.setDn("ou=bar," + str + "," + BASEDN);
        con.addEntry(entry);

        Iterator it = con.search(str + "," + BASEDN, LDAPSDKConnection.SCOPE_ONE, "(ou=*)", new String[]{"objectClass", "ou"});

        // check that it has 3 items
        assertTrue(it.hasNext());
        it.next();
        assertTrue(it.hasNext());
        it.next();
       // assertTrue(it.hasNext());
       // it.next();
        assertFalse(it.hasNext());

        // clean up
        con.deleteEntry("ou=foo," + str + "," + BASEDN);
        con.deleteEntry("ou=bar," + str + "," + BASEDN);
        con.deleteEntry(str + "," + BASEDN);
    }

    /**
     * Returns the suite of tests
     * @return the suite of tests
     */
    public static Test suite() {
        return new TestSuite(NetscapeConnectionTests.class);
    }
}
