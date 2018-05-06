/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder;

import java.util.Iterator;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.Values;


/**
 * Defines helper methods used in tests.
 *
 * @author TCSDEVELOPER
 * @version 1.3
 */
public final class TestHelper {
    /** Represents the namespace to load manager configuration. */
    static final String NAMESPACE = "com.topcoder.searchbuilder";

    /** Represents the namespace to load DB connection factory configuration. */
    static final String DB_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** Reperesents the classname of ldap. */
    static final String LDAP_CLASS = "com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory";

    /**
     * Emtpy private constructor to make this class can not be instanced.
     */
    private TestHelper() {
    }

    /**
     * Remove all the namespace.
     *
     * @throws Exception to JUnit
     */
    public static void clearConfig() throws Exception {
        ConfigManager cm = ConfigManager.getInstance();
        Iterator it = cm.getAllNamespaces();

        while (it.hasNext()) {
            cm.removeNamespace((String) it.next());
        }
    }

    /**
     * Add the namespace.
     *
     * @throws Exception to JUnit
     */
    public static void addConfig() throws Exception {
        clearConfig();

        ConfigManager cm = ConfigManager.getInstance();
        cm.add("config.xml");
        cm.add("DataBase.xml");
        cm.add("ldapObjectFactory.xml");
        cm.add("ldapSearchStrategyConfig.xml");
        cm.add("dbSearchStrategyConfig.xml");
    }

    /**
     * <p>
     * add the entries to ldap database for testing.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void addEntries() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");

            Entry entry1 = new Entry("cn=test1,dc=guessant,dc=org");
            entry1.setAttribute("objectClass", new Values("person"));
            entry1.setAttribute("cn", new Values("test1"));
            entry1.setAttribute("sn", new Values("1"));
            con.addEntry(entry1);
            Entry entry2 = new Entry("cn=test2,dc=guessant,dc=org");
            entry2.setAttribute("objectClass", new Values("person"));
            entry2.setAttribute("cn", new Values("test2"));
            entry2.setAttribute("sn", new Values("2"));
            con.addEntry(entry2);
            Entry entry3 = new Entry("cn=test3,dc=guessant,dc=org");
            entry3.setAttribute("objectClass", new Values("person"));
            entry3.setAttribute("cn", new Values("test3"));
            entry3.setAttribute("sn", new Values("3"));
            con.addEntry(entry3);
            Entry entry4 = new Entry("cn=test4,dc=guessant,dc=org");
            entry4.setAttribute("objectClass", new Values("person"));
            entry4.setAttribute("cn", new Values("test4"));
            entry4.setAttribute("sn", new Values("4"));
            con.addEntry(entry4);
            Entry entry5 = new Entry("cn=test5,dc=guessant,dc=org");
            entry5.setAttribute("objectClass", new Values("person"));
            entry5.setAttribute("cn", new Values("test5"));
            entry5.setAttribute("sn", new Values("5"));
            con.addEntry(entry5);
            Entry entry6 = new Entry("cn=s1,dc=guessant,dc=org");
            entry6.setAttribute("objectClass", new Values("role"));
            entry6.setAttribute("cn", new Values("s1"));
            entry6.setAttribute("searchbuild", new Values("sb1"));
            con.addEntry(entry6);
            Entry entry7 = new Entry("cn=s2,dc=guessant,dc=org");
            entry7.setAttribute("objectClass", new Values("role"));
            entry7.setAttribute("cn", new Values("s2"));
            entry7.setAttribute("searchbuild", new Values("sb2"));
            con.addEntry(entry7);
            Entry entry8 = new Entry("cn=s3,dc=guessant,dc=org");
            entry8.setAttribute("objectClass", new Values("role"));
            entry8.setAttribute("cn", new Values("s3"));
            entry8.setAttribute("searchbuild", new Values("sb3"));
            con.addEntry(entry8);
            Entry entry9 = new Entry("cn=s4,dc=guessant,dc=org");
            entry9.setAttribute("objectClass", new Values("role"));
            entry9.setAttribute("cn", new Values("s4"));
            entry9.setAttribute("searchbuild", new Values("sb4"));
            con.addEntry(entry9);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }

    /**
     * <p>
     * Add the entries to ldap database for testing the LikeFilter.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void addEntriesForLikeFilter() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");

            Entry entry1 = new Entry("cn=s1,dc=guessant,dc=org");
            entry1.setAttribute("objectClass", new Values("role"));
            entry1.setAttribute("cn", new Values("s1"));
            entry1.setAttribute("searchbuild", new Values("likeFilter)"));
            con.addEntry(entry1);
            Entry entry2 = new Entry("cn=s2,dc=guessant,dc=org");
            entry2.setAttribute("objectClass", new Values("role"));
            entry2.setAttribute("cn", new Values("s2"));
            entry2.setAttribute("searchbuild", new Values("likeFilter("));
            con.addEntry(entry2);
            Entry entry3 = new Entry("cn=s3,dc=guessant,dc=org");
            entry3.setAttribute("objectClass", new Values("role"));
            entry3.setAttribute("cn", new Values("s3"));
            entry3.setAttribute("searchbuild", new Values("*"));
            con.addEntry(entry3);
            Entry entry4 = new Entry("cn=s4,dc=guessant,dc=org");
            entry4.setAttribute("objectClass", new Values("role"));
            entry4.setAttribute("cn", new Values("s4"));
            entry4.setAttribute("searchbuild", new Values("\\"));
            con.addEntry(entry4);
            Entry entry5 = new Entry("cn=s5,dc=guessant,dc=org");
            entry5.setAttribute("objectClass", new Values("role"));
            entry5.setAttribute("cn", new Values("s5"));
            entry5.setAttribute("searchbuild", new Values("r5"));
            con.addEntry(entry5);
            Entry entry6 = new Entry("cn=s6,dc=guessant,dc=org");
            entry6.setAttribute("objectClass", new Values("role"));
            entry6.setAttribute("cn", new Values("s6"));
            entry6.setAttribute("searchbuild", new Values("r6"));
            con.addEntry(entry6);
            Entry entry7 = new Entry("cn=s7,dc=guessant,dc=org");
            entry7.setAttribute("objectClass", new Values("role"));
            entry7.setAttribute("cn", new Values("s7"));
            entry7.setAttribute("searchbuild", new Values("r7"));
            con.addEntry(entry7);
            Entry entry8 = new Entry("cn=s8,dc=guessant,dc=org");
            entry8.setAttribute("objectClass", new Values("role"));
            entry8.setAttribute("cn", new Values("s8"));
            entry8.setAttribute("searchbuild", new Values("likeFi*lter"));
            con.addEntry(entry8);
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    /**
     * <p>
     * Delete the entries to ldap database for testing the LikeFilter.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void delEntriesForLikeFilter() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");
            con.deleteEntry("cn=s1,dc=guessant,dc=org");
            con.deleteEntry("cn=s2,dc=guessant,dc=org");
            con.deleteEntry("cn=s3,dc=guessant,dc=org");
            con.deleteEntry("cn=s4,dc=guessant,dc=org");
            con.deleteEntry("cn=s5,dc=guessant,dc=org");
            con.deleteEntry("cn=s6,dc=guessant,dc=org");
            con.deleteEntry("cn=s7,dc=guessant,dc=org");
            con.deleteEntry("cn=s8,dc=guessant,dc=org");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    /**
     * <p>
     * delete the entries to ldap database for testing.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    public static void delEntries() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");
            con.deleteEntry("cn=test1,dc=guessant,dc=org");
            con.deleteEntry("cn=test2,dc=guessant,dc=org");
            con.deleteEntry("cn=test3,dc=guessant,dc=org");
            con.deleteEntry("cn=test4,dc=guessant,dc=org");
            con.deleteEntry("cn=test5,dc=guessant,dc=org");
            con.deleteEntry("cn=s1,dc=guessant,dc=org");
            con.deleteEntry("cn=s2,dc=guessant,dc=org");
            con.deleteEntry("cn=s3,dc=guessant,dc=org");
            con.deleteEntry("cn=s4,dc=guessant,dc=org");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
    /**
     * Get the number of items in the Iterator.
     *
     * @param it depend which to get items.
     * @return the number if items in the Iterator.
     */
    public static int getItemNumber(Iterator it) {
        int sum = 0;

        while (it.hasNext()) {
            sum++;
            it.next();
        }

        return sum;
    }
}
