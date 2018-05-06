/*
 * TCS LDAP 1.0 
 *
 * SDK
 *
 * Copyright (c), 2004. TopCoder, Inc. All rights reserved.
 */
/**
 * Copyright ?2004, TopCoder, Inc. All rights reserved
 */
package com.topcoder.util.net.ldap.sdkinterface.stresstests;

import junit.framework.*;
import com.topcoder.util.net.ldap.sdkinterface.*;
import com.topcoder.util.net.ldap.sdkinterface.netscape.*;
import netscape.ldap.*;
import java.util.*;

/**
 * Stress tests for LDAP SDK Interface.
 *
 * This component adds a layer of abstraction to accessing any SDK, so any operation will be slower
 * than executing it directly using the corresponding libraries.  Anyway, in most cases the overhead
 * should be negligible, because the time that takes all the LDAP execution, including sending data
 * through a network, is usually big in comparison to the time it takes to call some methods and to
 * copy some data in memory.
 *
 * The time of executing operations with the component can be compared with the time to do the same
 * thing using the corresponding libraries.  This can be done reading entries with different sizes to
 * test if bigger entries produce more overhead.  Although it should take longer to do the translation
 * between the specific LDAP objects and the component object, also the operation will be slower, so
 * it's very likely for the overhead to be still negligible.
 *
 * Also some other times for operations like deleting and updating entries should be measured.  The
 * expected result is a very little overhead.  For a normal situation with a not very slow computer
 * as the client and not an ideal network and server, less than 1% of overhead will be very good,
 * between 1% and 5% will be good, and more than 5% will be too much.
 *
 * @author  WishingBone
 * @version 1.0
 */
public class LDAPStressTest extends TestCase {
    private final static String BASE_DN = "dc=my-domain,dc=com";

    /**
     * Get the test suite. The tests depend on each other and is executed in order!
     *
     * @return the test suite.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new LDAPStressTest("testAddEntry"));
        suite.addTest(new LDAPStressTest("testReadEntry"));
        suite.addTest(new LDAPStressTest("testUpdateEntry"));
        suite.addTest(new LDAPStressTest("testSearch"));
        suite.addTest(new LDAPStressTest("testRenameEntry"));
        suite.addTest(new LDAPStressTest("testDeleteEntry"));
        return suite;
    }

    /**
     * Constructor.
     *
     * @param name name of the test case.
     */
    public LDAPStressTest(String name) {
        super(name);
    }

    /**
     * The LDAP host.
     */
    public static String HOST = "127.0.0.1";//"192.168.0.233";

    /**
     * The LDAP listening port.
     */
    public static int PORT = 389;

    /**
     * The DN.
     */
    public static String DN = "cn=Manager,dc=my-domain,dc=com";//"cn=jinlimin,c=CN";

    /**
     * The password for specified DN.
     */
    public static String PASSWORD ="secret";// "qazwsx";

    /**
     * The number of iteration for each operation to go. Adjust this to suit your service connection speed.
     */
    public static int ITERATION = 3;

    /**
     * The sdk connection to benchmark. Created in setup routine.
     */
    private LDAPSDKConnection sdkConnection = null;

    /**
     * The raw connection to compare with. Created in setup routine.
     */
    private LDAPConnection connection = null;

    /**
     * Connect to LDAP server.
     *
     * @throws Exception to JUnit.
     */
    protected void setUp() throws Exception {
        // create the sdk connection
        LDAPSDK sdk = new LDAPSDK("com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory");
        sdkConnection = sdk.createConnection();
        sdkConnection.connect(HOST, PORT);
        sdkConnection.authenticate(DN, PASSWORD);
        // create the raw connection
        connection = new LDAPConnection();
        connection.connect(HOST, PORT);
        connection.authenticate(DN, PASSWORD);
        System.out.println("setUp() exited");
    }

    /**
     * Disconnect.
     *
     * @throws Exception to JUnit.
     */
    protected void tearDown() throws Exception {
        sdkConnection.disconnect();
        connection.disconnect();
        System.out.println("tearDown() exited");
    }

    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test add entry to the LDAP server.
     *
     * @throws Exception to JUnit.
     */

    public void testAddEntry() throws Exception {
        System.out.println("Adding " + ITERATION + " entries");
        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            Map attributes = new HashMap();
            attributes.put("objectClass", new Values("person"));
            attributes.put("cn", new Values("stress_sdk" + i));
            attributes.put("sn", new Values("sn" + i));
            Entry entry = new Entry("cn=stress_sdk" + i + "," + BASE_DN);
            entry.setAttributes(attributes);
            sdkConnection.addEntry(entry);
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            LDAPAttributeSet attributes = new LDAPAttributeSet();
            attributes.add(new LDAPAttribute("objectClass", "person"));
            attributes.add(new LDAPAttribute("cn", "stress_raw" + i));
            attributes.add(new LDAPAttribute("sn", "sn" + i));
            LDAPEntry entry = new LDAPEntry("cn=stress_raw" + i + "," + BASE_DN, attributes);
            connection.add(entry);
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");
        System.out.println("--");
    }


    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test read entry from the LDAP server.
     *
     * @throws Exception to JUnit.
     */



    public void testReadEntry() throws Exception {
        System.out.println("Reading " + ITERATION + " entries");
        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            sdkConnection.readEntry("cn=stress_sdk" + i + "," + BASE_DN);
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            connection.read("cn=stress_raw" + i + "," + BASE_DN);
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");
        System.out.println("--");
    }



    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test update entry on the LDAP server.
     *
     * @throws Exception to JUnit.
     */


    public void testUpdateEntry() throws Exception {
        System.out.println("Updating " + ITERATION + " entries");
        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            Update update = new Update();
            update.add("description", new Values("desc"));
            update.replace("description", new Values("new desc"));
            update.delete("description");
            sdkConnection.updateEntry("cn=stress_sdk" + i + "," + BASE_DN, update);
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            connection.modify("cn=stress_raw" + i + "," + BASE_DN,
                    new LDAPModification[] {
                            new LDAPModification(LDAPModification.ADD, new LDAPAttribute("description", "desc")),
                            new LDAPModification(LDAPModification.REPLACE, new LDAPAttribute("description", "new desc")),
                            new LDAPModification(LDAPModification.DELETE, new LDAPAttribute("description"))});
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");
        System.out.println("--");
    }



    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test search on the LDAP server.
     *
     * @throws Exception to JUnit.
     */


    public void testSearch() throws Exception {
        System.out.println("Searching " + ITERATION + " entries");
        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            sdkConnection.search(BASE_DN, LDAPv3.SCOPE_SUB, "(sn=sn" + i + ")");
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            connection.search(BASE_DN, LDAPv3.SCOPE_SUB, "(sn=sn" + i + ")", null, false);
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");
        System.out.println("--");
    }



    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test rename entry on the LDAP server.
     *
     * @throws Exception to JUnit.
     */

    public void testRenameEntry() throws Exception {
        System.out.println("Renaming " + ITERATION + " entries");

        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            connection.rename("cn=stress_raw" + i + "," + BASE_DN, "cn=new_stress_raw" + i, true);
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        

        System.out.println("the raw entry is renamed");

        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            sdkConnection.renameEntry("cn=stress_sdk" + i + "," + BASE_DN, "cn=new_stress_sdk" + i, true);
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");
        
        System.out.println("--");
    }

    /**
     * com.topcoder.util.net.ldap.sdkinterface.netscape.Test delete entry from the LDAP server.
     *
     * @throws Exception to JUnit.
     */
    public void testDeleteEntry() throws Exception {
        System.out.println("Deleting " + ITERATION + " entries");

        // with raw connection
        long rawStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            connection.delete("cn=new_stress_raw" + i + "," + BASE_DN);
        }
        long rawEnd = System.currentTimeMillis();
        double rawTime = (rawEnd - rawStart) / 1000.0;
        System.out.println("with raw connection: " + rawTime + " seconds");
        
        System.out.println("The raw entry is deleted");

        // with wrapped connection
        long sdkStart = System.currentTimeMillis();
        for (int i = 0; i < ITERATION; ++i) {
            sdkConnection.deleteEntry("cn=new_stress_sdk" + i + "," + BASE_DN);
        }
        long sdkEnd = System.currentTimeMillis();
        double sdkTime = (sdkEnd - sdkStart) / 1000.0;
        System.out.println("with wrapped connection: " + sdkTime + " seconds");
        System.out.println("The average overhead is: " + (sdkTime - rawTime) / rawTime * 100.0 + "%");

        
        System.out.println("--");
    }

}
