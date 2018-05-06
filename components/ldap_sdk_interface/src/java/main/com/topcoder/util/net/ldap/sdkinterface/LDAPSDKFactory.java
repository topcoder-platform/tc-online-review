/*
 * TCS LDAP SDK Interface 1.0
 *
 * LDAPSDKFactory.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface;

/**
 * As the <em>Abstract Factory</em> of the Abstract Factory Pattern, it defines the products that each <em>Concrete
 * Factory</em> (for example NetscapeFactory) must provide.&nbsp; The defined products are connections and SSL
 * connections.</p>
 * <p>As the <em>Product</em> of the Factory Method Pattern, it defines the interface for the type of objects that the
 * factory method produces.</p>
 * <p>In other words, the Factory Method Pattern is used to produce different factories depending on the LDAP
 * implementation. Then, the factory can produce different types of connections.</p>
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public interface LDAPSDKFactory {

    /**
     * The implementing classes of this interface (plugin classes) must return an object inherited from
     * LDAPSDKConnection that represent a LDAP connection for the plugin.
     *
     * @return a LDAPSDKConnection
     */
    public LDAPSDKConnection createConnection();

    /**
     * The implementing classes of this interface (plugin classes) must return an object inherited from
     * LDAPSDKConnection that represent a LDAP connection with SSL for the plugin.
     *
     * @return a LDAPSDKConnection
     */
    public LDAPSDKConnection createSSLConnection();
}
