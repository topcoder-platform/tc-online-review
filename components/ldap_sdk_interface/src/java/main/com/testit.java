/*
 * TCS Universal Registration System 1.0
 *
 * testit.java
 *
 * 1.0 06/10/2004
 */
package com;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.Values;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory;

import java.util.Map;
import java.util.HashMap;

/**
 *
 *
 * @author  TCSDESIGNER
 * @version 1.0 06/20/2004
 */
public class testit {

    /**
     * A <code>String</code> containing the hostname where the LDAP server is running. This string should not be started
     * with "ldap://" protocol scheme, just a host name.
     */
    private static final String VALID_LDAP_HOST = "localhost";

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
    private static final String CONNECT_DN = "manager";

    /**
     * A <code>String</code> passowrd used to connect to LDAP server.
     */
    private static final String CONNECT_PASSWORD = "secret";

    /**
     * A <code>String</code> name of a sample LDAP entry to be used in testing.
     */
    private static final String ENTRY_NAME = "cn=TCSDEVELOPER8888";

    /**
     * A <code>String</code> new name of a sample LDAP entry to be used in testing.
     */
    private static final String NEW_ENTRY_NAME = "cn=isv556545";

    /**
     * A <code>String</code> value of "cn" attribute of LDAP entry to be used for testing.
     */
    private static final String CN_VALUE = "TCSDEVELOPER44444";

    /**
     * A <code>String</code> value of "sn" attribute of LDAP entry to be used for testing.
     */
    private static final String SN_VALUE = "Istomin";

    /**
     * A <code>String</code> base DN to be used for testing.
     */
    private static final String BASE_DN = "o=TopCoder";

    public static void main(String[] args) throws Exception {
        LDAPSDK sdk = new LDAPSDK(NetscapeFactory.class.getName());
        LDAPSDKConnection con = sdk.createConnection();

        con.connect("127.0.0.1", 389);

        con.authenticate("manager","secret");

        Map attributes = new HashMap();
        attributes.put("objectClass", new Values("person"));
        attributes.put("cn", new Values(CN_VALUE));
        attributes.put("sn", new Values(SN_VALUE));

        Entry entry = new Entry(ENTRY_NAME + "," + BASE_DN);
        entry.setAttributes(attributes);

        con.addEntry(entry);

        con.renameEntry(entry.getDn(), NEW_ENTRY_NAME, false);

        entry = con.readEntry(NEW_ENTRY_NAME+","+BASE_DN);

        System.out.println(entry.getDn());
        System.out.println(entry.getValues("cn").getTextValues().size());

        for (int i = 0; i < entry.getValues("cn").getTextValues().size(); i++) {
            System.out.println(entry.getValues("cn").getTextValues().get(i));
        }

        entry = con.readEntry(ENTRY_NAME+","+BASE_DN);

        System.out.println(entry);

        con.disconnect();

    }
}
