/*
 * TCS LDAP SDK Interface 1.0
 *
 * NetscapeFactory.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.netscape;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKConnection;
import com.topcoder.util.net.ldap.sdkinterface.LDAPSDKFactory;

/**
 * As a <em>Concrete Product</em> from the Method Factory Pattern, it is one of the classes that the <em>Creator</em>
 * (LDAPSDK) can instantiate.&nbsp;</p>
 * <p>Once this <em>Concrete Product</em> is chosen, it serves as a <em>Concrete Factory</em> from the Abstract Factory
 * Pattern.&nbsp; This <em>Concrete Factory</em> is used to create the corresponding <em>Products</em> (connections and
 * SSL connections), that are actually instances of <em>Netscape Connection.</em>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class NetscapeFactory implements LDAPSDKFactory {

    /**
     * Create a connection using Netscape Directory LDAP. It should instantiate a NetscapeConnection object, call his
     * createLDAPConnection method with ssl set to false and return it.
     *
     * @return a connection using Netscape Directory
     */
    public LDAPSDKConnection createConnection() {
        NetscapeConnection connection = new NetscapeConnection();
        connection.createLDAPConnection(false);
        return connection;
    }

    /**
     * Create a SSL connection using Netscape Directory LDAP. It should instantiate a NetscapeConnection object, call
     * his createLDAPConnection method with ssl set to true and return it.
     *
     * @return an SSL connection using Netscape Directory
     */
    public LDAPSDKConnection createSSLConnection() {
        NetscapeConnection connection = new NetscapeConnection();
        connection.createLDAPConnection(true);
        return connection;
    }
}
