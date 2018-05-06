/*
 * Copyright (C) 2006 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.util.net.ldap.sdkinterface.LDAPSDK;

import java.util.Properties;


/**
 * <p>
 * This class includes all the information needed to create a LDAP connection.
 * </p>
 *
 * <p>
 * The class is thread safe, since it is immutable
 * </p>
 * @author ShindouHikaru, TCSDEVELOPER
 * @version 1.3
 *
 */
public class LDAPConnectionInformation {
    /**
     * It will hold a LDAPSDK object, used to create LDAP connection.
     *
     */
    private final LDAPSDK factory;

    /**
     * It will hold the name of the host.
     *
     */
    private final String host;

    /**
     * It will hold the port number of the host.
     *
     */
    private final int port;

    /**
     * It will hold a boolean indicating the connection is secure or not.
     * If it is true, a ssl connection should be created. Otherwise a regular connection should be created.
     *
     */
    private final boolean isSSL;

    /**
     * It will hold the a number indicating the search scope.
     *
     */
    private final int scope;

    /**
     * It will hold the dnroot of the ldapserver to bind the server.
     *
     */
    private final String dnroot;

    /**
     * It will hold the password of the ldapserver to bind the server.
     *
     */
    private final String password;

    /**
     * <p>Create a new instance by setting all the members in the class.</p>
     *
     *
     *
     * @param factory a LDAPSDK object used to create a LDAP connection
     * @param host host name
     * @param port port number of the host
     * @param isSecure a boolean indicating whether we need to create a ssl connection or regular connection
     * @param scope the search scope
     * @param dnroot the dnroot(user) to bind the ldapserver it can be null or empyt,denote anonymous search.
     * @param password the password to bind the ldapserver it can be null or empyt,denote anonymous search.
     * @throws IllegalArgumentException if any parameter is Null
     * or the host is an empty string,
     * or port has 0 or negative integers
     */
    public LDAPConnectionInformation(LDAPSDK factory, String host, int port,
        boolean isSecure, int scope, String dnroot, String password) {
        if(factory == null) {
            throw new IllegalArgumentException("The factory should not be null.");
        }
        if(host == null) {
            throw new IllegalArgumentException("The host should not be null.");
        }
        if(dnroot == null) {
            throw new IllegalArgumentException("The dnroot should not be null.");
        }
        if(password == null) {
            throw new IllegalArgumentException("The password should not be null.");
        }

        if (host.trim().length() == 0) {
            throw new IllegalArgumentException(
                "The String param should not be rmpty to construct the LDAPConnectionInformation.");
        }
        
        if(port <= 0) {
            throw new IllegalArgumentException("The port should be > 0.");
        }

        this.factory = factory;
        this.host = host;
        this.port = port;
        this.isSSL = isSecure;
        this.scope = scope;
        this.dnroot = dnroot;
        this.password = password;
    }

    /**
     * <p>
     * Create a new instance via the properties.
     * The properties should include all the members the class required.
     * Also the mothed check the members in the properties whether they are valid.
     * </p>
     *
     *
     * @param properties Property used to construct the instance
     * @throws IllegalArgumentException if param is null
     * @throws IllegalArgumentException if any member in the properties is illegal
     */
    public LDAPConnectionInformation(Properties properties) {
        if (properties == null) {
            throw new IllegalArgumentException(
                "The Property should not be null to construct the LDAPConnectionInformation.");
        }

        this.factory = (LDAPSDK) properties.get("factory");

        if (factory == null) {
            //wrap the exception by IllegalArgumentException
            throw new IllegalArgumentException(
                "The factory in the Properties is illagal.");
        }

        this.host = properties.getProperty("host");
        this.dnroot = properties.getProperty("dnroot");
        this.password = properties.getProperty("password");
        if(host == null) {
            throw new IllegalArgumentException("The host should not be null.");
        }
        if (host.length() == 0) {
            throw new IllegalArgumentException(
                "The String host in the Properties is illagal.");
        }

        try {
            this.port = Integer.parseInt(properties.getProperty("port"));
            this.isSSL = Boolean.getBoolean(properties.getProperty("isSSL"));
            this.scope = Integer.parseInt(properties.getProperty("scope"));
        } catch (NumberFormatException e) {
            //wrap the exception by IllegalArgumentException
            throw new IllegalArgumentException(
                "The integer value in the properties is illegal.");
        }
    }

    /**
     * <p> get the factory object used to create a LDAP connection.</p>
     *
     * This method is updatd in version 1.3.
     *
     * @return LDAPSDK used to create a LDAP connection
     */
    public LDAPSDK getFactory() {
        return this.factory;
    }

    /**
     * <p> get the host name.</p>
     *
     *
     * @return host name
     */
    public String getHost() {
        return this.host;
    }

    /**
     * <p> get the port number of the host.</p>
     *
     *
     * @return port number of host
     */
    public int getPort() {
        return this.port;
    }

    /**
     * <p> get a boolean indicating whether we need to create a ssl connection or regular connection.</p>
     *
     *
     * @return a boolean indicating whether we need to create a ssl connection or regular connection
     */
    public boolean isSecure() {
        return this.isSSL;
    }

    /**
     * <p> get the scope of the search.</p>
     *
     * @return the search scope
     */
    public int getScope() {
        return this.scope;
    }

    /**
     <p> get the dnroot of the server.</p>
     *
     * @return the dnroot to bind the ldapserver
     */
    public String getDnroot() {
        return this.dnroot;
    }

    /**
    * <p> get the dnroot of the server.</p>
    *
    * @return the password to bind the ldapserver
    */
    public String getPassword() {
        return this.password;
    }
}
