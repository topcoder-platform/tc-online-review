/**
 * Copyright (c) 2005, TopCoder, Inc. All rights reserved
 */
package com.topcoder.search.builder.failuretests;

import com.topcoder.search.builder.*;
import com.topcoder.search.builder.filter.*;
import com.topcoder.search.builder.database.*;
import com.topcoder.search.builder.ldap.*;
import com.topcoder.util.config.ConfigManager;
import com.topcoder.db.connectionfactory.DBConnectionFactoryImpl;
import com.topcoder.db.connectionfactory.ConnectionProducer;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;

/**
 * Helper class.
 *
 * @author WishingBone
 * @version 1.1
 */
class TestHelper {

    /**
     * A test connection producer.
     */
    private static ConnectionProducer cp = null;

    /**
     * Get a valid connection producer.
     *
     * @return a valid connection producer.
     */
    public static ConnectionProducer getConnectionProducer() {
        if (cp == null) {
            try {
            	ConfigManager cm = ConfigManager.getInstance();
            	if (cm.existsNamespace("ConnectionFactory")) {
                    cm.removeNamespace("ConnectionFactory");
                }
                
                cm.add("failuretests.xml");
                cp = new DBConnectionFactoryImpl("ConnectionFactory").get("conn");
                
                cm.removeNamespace("ConnectionFactory");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return cp;
    }

    /**
     * A test LDAP SDK.
     */
    private static LDAPSDK ldapsdk = null;

    /**
     * Get a valid LDAP SDK.
     *
     * @return a valid LDAP SDK.
     */
    public static LDAPSDK getLDAPSDK() {
        if (ldapsdk == null) {
            try {
                ldapsdk = new LDAPSDK("com.topcoder.util.net.ldap.sdkinterface.netscape.NetscapeFactory");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return ldapsdk;
    }

}
