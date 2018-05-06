/*
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.stresstests;

import com.topcoder.util.config.ConfigManager;
import com.topcoder.util.net.ldap.sdkinterface.Entry;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.Values;


/**
 * Defines helper methods used in tests.
 *
 * @author fuyun, brain
 * @version 1.3
 */
class StressTestHelper {
    /** Represents the configuration file. */
    private static final String CONFIG_FILE = "stresstests/SampleConfig.xml";
    
    /** The stress loads used for testing. */
    static final int STRESS_LOADS = 100;
    
    /** The stress loads used for testing. */
    static final int THREAD_COUNT = 1;

    /** Represents the namespace to load manager configuration. */
    static final String NAMESPACE = "com.topcoder.searchbuilder";

    /** Represents the namespace to load DB connection factory configuration. */
    static final String DB_NAMESPACE = "com.topcoder.db.connectionfactory.DBConnectionFactoryImpl";

    /** Reperesents the classname of ldap. */
    static final String LDAP_CLASS = "com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory";

    /** Represents the all namespaces. */
    private static final String[] NAMESPACES = new String[] {NAMESPACE,
    	DB_NAMESPACE,
    	"com.topcoder.search.builder.validator.factory",
    	"com.topcoder.search.builder.strategy.factory",
    	"DBSearchStrategy",
    	"LDAPSearchStrategy",
    	"com.topcoder.search.builder.ldap.connInfo.factory",
    	"com.topcoder.search.builder.database.stresstest.factory",
    	"com.topcoder.search.builder.ldap.stresstest.factory"
    	};

    /**
     * Emtpy private constructor to make this class can not be instanced.
     */
    private StressTestHelper() {
    }

    /**
     * <p>
     * clear the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    static void clearConfig() throws Exception {
        ConfigManager config = ConfigManager.getInstance();

        for (int i = 0; i < NAMESPACES.length; i++) {
            if (config.existsNamespace(NAMESPACES[i])) {
                config.removeNamespace(NAMESPACES[i]);
            }
        }
    }

    /**
     * <p>
     * add the config.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    static void addConfig() throws Exception {
        clearConfig();

        ConfigManager.getInstance().add(CONFIG_FILE);
    }

    /**
     * <p>
     * add the entries to ldap database for testing.
     * </p>
     *
     * @throws Exception unexpected exception.
     */
    static void addEntries() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");

            Entry entry = new Entry("cn=test,dc=guessant,dc=org");
            entry.setAttribute("objectClass", new Values("person"));
            entry.setAttribute("cn", new Values("test"));

            String[] sn = { "1", "2", "3", "4", "5" };
            entry.setAttribute("sn", new Values(sn));
            con.addEntry(entry);

            /*Entry e = con.readEntry("cn=test,dc=guessant,dc=org");
            System.out.println("sn:");

            List list = e.getValues("sn").getTextValues();

            for (Iterator it = list.iterator(); it.hasNext();)
                System.out.println((String) it.next()); */
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
    static void delEntries() throws Exception {
        LDAPSDKConnection con = null;

        try {
            con = new LDAPSDK(LDAP_CLASS).createConnection();
            con.connect("localhost", 389);
            con.authenticate(3, "cn=Manager,dc=guessant,dc=org", "secret");
            con.deleteEntry("cn=test,dc=guessant,dc=org");
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }
}
