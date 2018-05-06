/**
 * Copyright &copy; TopCoder Inc, 2004. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.failuretests;

import com.topcoder.util.net.ldap.sdkinterface.*;
import com.topcoder.util.net.ldap.sdkinterface.netscape.*;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import junit.framework.TestCase;

/**
 * Failure test for NetscapeConnection class
 *
 * @author Standlove
 */
public class NetscapeConnectionFailureTest extends TestCase {
    /** The host and port the OpenLDAP runs. */
    private static String HOST = "127.0.0.1";//"192.168.0.233";
    private static int PORT = 389;    

    private static String DN = "dc=my-domain,dc=com";

    private NetscapeFactory factory = null;

    /** The connection to test. */
    private NetscapeConnection conn = null;

    /** Create instance to test. */
    public void setUp() throws Exception {
        factory = new NetscapeFactory();
        conn = (NetscapeConnection) factory.createConnection();
        conn.connect(HOST, PORT);
        conn.authenticate("cn=Manager,dc=my-domain,dc=com", "secret");
    }

    /** Disconnet to the LDAP server. */
    public void tearDown() throws Exception {
        conn.disconnect();
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test connect with bad argument. */
    public void testConnect_BadArgument() throws Exception {
        NetscapeConnection connection = (NetscapeConnection) factory.createConnection();
        try {
            connection.connect(null, PORT);
            fail("the given host is null");
        } catch (NullPointerException e) {
        }

        try {
            connection.connect(" ", PORT);
            fail("the given host is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            connection.connect(HOST, -1);
            fail("the given port is invalid");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test the connect with bad host / port. */
    public void testConnect_BadHostPort() throws Exception {  
        NetscapeConnection connection = (NetscapeConnection) factory.createConnection(); 
        // connect to the wrong port
        try {
            connection.connect(HOST, 180);
            fail("the given port should be 389");
        } catch (LDAPSDKCommunicationException e) {
        }

        // connect to the host without running LDAP server
        try {
            connection.connect("localhost1", PORT);
            fail("the given host does not run LDAP server");
        } catch (LDAPSDKCommunicationException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test addEntry with bad argument. */
    public void testAddEntryFailure() throws Exception {   
        try {
            conn.addEntry(null);
            fail("the entry is null");
        } catch (NullPointerException e) {
        }

        Entry entry = new Entry("cn=Standlove,dc=my-domain1,dc=com");
        entry.setAttribute("sn", new Values("nothing"));
        entry.setAttribute("cn", new Values("cn"));
        entry.setAttribute("objectClass", new Values("person"));
        try {
            conn.addEntry(entry);
            fail("the dn is invalid");
        } catch (Exception e) {
        }

        entry = new Entry("cn=Standlove,dc=my-domain,dc=com");
       // entry.setAttribute("cn", new Values("Standlove"));
        entry.setAttribute("sn", new Values("sn"));
        entry.setAttribute("objectClass", new Values("person"));
        try {
            conn.addEntry(entry);
            fail("the attribute is invalid");
        } catch (LDAPSDKSchemaViolationException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test deleteEntry with bad arguments. */
    public void testDeleteEntryFailure() throws Exception {
        try {
            conn.deleteEntry(null);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.deleteEntry(" ");
            fail("the given dn is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.deleteEntry("cn=ff,dc=my-domain,dc=com");
            fail("the entry does not exist");
        } catch (LDAPSDKNoSuchObjectException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test renameEntry with bad argument. */
    public void testRenameEntryFailure() throws Exception {
        String oldDN = "cn=Loveyou,dc=my-domain,dc=com";
        String newDN = "cn=Standlove,dc=my-domain,dc=com";
        try {
            conn.renameEntry(null, newDN, false);
            fail("the given oldDN is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.renameEntry(oldDN, null, false);
            fail("The given newDN is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.renameEntry(" ", newDN, false);
            fail("The given oldDN is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.renameEntry(oldDN, " ", false);
            fail("the given newDN is empty string");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.renameEntry(oldDN, newDN, false);
            fail("the oldDN does not exist");
        } catch (Exception e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test updateEntry with bad argument. */
    public void testUpdateEntry() throws Exception {
        Update update = new Update();
        update.add("description02023", new Values("java"));
        
        try {
            conn.updateEntry(null, update);
            fail("The given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.updateEntry(" ", update);
            fail("the given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.updateEntry(DN, null);
            fail("the given Update is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.updateEntry(DN, update);
            fail("the given attribute does not exist in the DN");
        } catch (LDAPSDKSchemaViolationException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test readEntry with bad argument. */
    public void testReadEntryFailure() throws Exception {
        try {
            conn.readEntry(null);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.readEntry("   ");
            fail("the given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.readEntry("cn=noperson, o=dev, c=CN");
            fail("The entry does not exist");
        } catch (LDAPSDKNoSuchObjectException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test readEntry with attr with bad argument. */
    public void testReadEntryAttrFailure() throws Exception {
        try {
            conn.readEntry(null, new String[0]);
            fail("The given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.readEntry(" ", new String[0]);
            fail("The given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.readEntry(DN, null);
            fail("the given attrs is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.readEntry(DN, new String[] { null });
            fail("the given attrs contains null elements.");
        } catch (NullPointerException e) {
        }

        try {
            Entry entry = new Entry(DN);
            Map attributes = new HashMap();
            attributes.put("objectClass", new Values("person"));
            attributes.put("cn", new Values("cn_value"));
            attributes.put("sn", new Values("sn_value"));
            entry.setAttributes(attributes);

            conn.addEntry(entry);
            conn.readEntry(DN, new String[] { "noattr" }); // by isv - the entry does not exist
            fail("The attr does not exist");
        } catch (LDAPSDKSchemaViolationException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test search with bad arguments. */
    public void testSearchFailure() throws Exception {
        String baseDN = "o=dev, c=CN";
        String filter = "(objectclass=*)";

        try {
            conn.search(null, LDAPSDKConnection.SCOPE_BASE, filter);
            fail("the given base dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.search(" ", LDAPSDKConnection.SCOPE_BASE, filter);
            fail("the given base dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, null);
            fail("the given filter is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, " ");
            fail("the given filter is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.search(baseDN, -100, filter);
            fail("the given scope is invalid");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test search attr with bad arguments. */
    public void testSearchAttrFailure() throws Exception {
        String baseDN = "o=dev, c=CN";
        String filter = "(objectclass=*)";
        String[] attrs = new String[] { "cn" };

        try {
            conn.search(null, LDAPSDKConnection.SCOPE_BASE, filter, attrs);
            fail("the given base dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.search(" ", LDAPSDKConnection.SCOPE_BASE, filter, attrs);
            fail("the given base dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, null, attrs);
            fail("the given filter is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, " ", attrs);
            fail("the given filter is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.search(baseDN, -100, filter, attrs);
            fail("the given scope is invalid");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, " ", null);
            fail("the attrs is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.search(baseDN, LDAPSDKConnection.SCOPE_BASE, " ", new String[] { null });
            fail("the attrs contains null element");
        } catch (NullPointerException e) {
        } catch(IllegalArgumentException e) {}  // by isv

    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test authenticate with bad argument. */
    public void testAuthenticateFailure() throws Exception {
        try {
            conn.authenticate(null, "pwd");
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.authenticate(" ", "pwd");
            fail("the given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.authenticate("standlove", null);
            fail("the given pwd is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.authenticate("standlove", " ");
            fail("the given pwd is empty");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test authenticateSASL with bad arguments.*/
    public void testAuthenticateSASLFailure() throws Exception {
        String dn = "o=dev, c=CN";
        String[] mechanisms = new String[0];
        Hashtable props = new Hashtable();
        Object cbh = new Integer(0);

        try {
            conn.authenticateSASL(null, mechanisms, props, cbh);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.authenticateSASL(" ", mechanisms, props, cbh);
            fail("the given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.authenticateSASL(dn, null, props, cbh);
            fail("the given mechanisms is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.authenticateSASL(dn, mechanisms, null, cbh);
            fail("the given props is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.authenticateSASL(dn, mechanisms, props, null);
            fail("the given cbh is null");
        } catch (NullPointerException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test bind with bad argument. */
    public void testBindFailure() throws Exception {
        try {
            conn.bind(null, "pwd");
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.bind(" ", "pwd");
            fail("The given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.bind("dn", null);
            fail("the given pwd is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.bind("dn", " ");
            fail("the given pwd is empty");
        } catch (IllegalArgumentException e) {
        }
    }

    /** com.topcoder.util.net.ldap.sdkinterface.netscape.Test bindSASL with bad argument. */
    public void testBindSASLFailure() throws Exception {
        String dn = "o=dev, c=CN";
        String[] mechanisms = new String[0];
        Hashtable props = new Hashtable();
        Object cbh = new Integer(0);

        try {
            conn.bindSASL(null, mechanisms, props, cbh);
            fail("the given dn is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.bindSASL(" ", mechanisms, props, cbh);
            fail("the given dn is empty");
        } catch (IllegalArgumentException e) {
        }

        try {
            conn.bindSASL(dn, null, props, cbh);
            fail("the given mechanisms is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.bindSASL(dn, mechanisms, null, cbh);
            fail("the given props is null");
        } catch (NullPointerException e) {
        }

        try {
            conn.bindSASL(dn, mechanisms, props, null);
            fail("the given cbh is null");
        } catch (NullPointerException e) {
        }
    }
}