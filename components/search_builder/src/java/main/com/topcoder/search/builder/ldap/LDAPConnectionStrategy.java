/*
 * Copyright (C) 2005 TopCoder Inc., All Rights Reserved.
 */
package com.topcoder.search.builder.ldap;

import com.topcoder.search.builder.ConnectionInformation;
import com.topcoder.search.builder.ConnectionStrategy;
import com.topcoder.search.builder.PersistenceOperationException;

import com.topcoder.util.net.ldap.sdkinterface.*;

import java.util.Iterator;
import java.util.List;


/**
 * It holds the Connection object used to conduct search.
 *
 * @author victor_lxd, haozhangr
 * @version 1.1
 */
public class LDAPConnectionStrategy extends ConnectionStrategy {
    /**
     * It will hold a LDAPSDK object, used to create a LDAP connection.
     *
     */
    private LDAPSDKConnection connection = null;

    /**
     * It will hold the context used in the LDAP search. It is the context, such as "country=USA"
     *
     */
    private final String context;

    /**
     * <p>
     * Create a new instance by setting the connectionInfo and context.
     * The connection is initialized to be null.
     * </p>
     *
     *
     * @param connectionInfo a ConnectionInformation object
     * @param context the context used in the LDAP search
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException if the context parameter is an empty string
     */
    public LDAPConnectionStrategy(ConnectionInformation connectionInfo,
        String context) {
        super(connectionInfo);

        if (context == null) {
            throw new NullPointerException(
                "The context should not be null to construct the LDAPConnectionStrategy.");
        }

        if (context.length() == 0) {
            throw new IllegalArgumentException(
                "The context should not be empty to construct the LDAPConnectionStrategy.");
        }

        this.connection = null;

        this.context = context;
    }

    /**
     * <p>
     * Search LDAP directory and return specified fields ,all the fields are in the fields list.
     * The search is based on the searchString.
     * </p>
     *
     *
     *
     * @param searchString a search string used to conduct the search
     * @param fields names of fileds should be returned
     * @return an Iterator of Entry objects holding search result
     * @throws LDAPSDKException
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException if any paramter is empty or any element in the fields parameter is empty
     * @throws PersistenceOperationException if any error when operating over data store
     */
    public synchronized Object search(String searchString, List fields)
        throws PersistenceOperationException {
        //check searchString null
        if (searchString == null) {
            throw new NullPointerException(
                "The searchString should not be null to search.");
        }

        //check searchString empty
        if ((searchString.length() == 0) || (fields.size() == 0)) {
            throw new IllegalArgumentException(
                "The param should not be empty to search in LDAPConnectionStrategy.");
        }

        if (fields == null) {
            throw new NullPointerException(
                "The fields should not be null to search in LDAPConnectionStrategy.");
        }

        Iterator it = fields.iterator();
        String[] array = new String[fields.size()];
        int index = 0;

        while (it.hasNext()) {
            Object nextItem = it.next();

            if ((nextItem == null) || !(nextItem instanceof String)) {
                throw new IllegalArgumentException(
                    "The field should be instance of String to search in LDAPConnectionStrategy.");
            }

            String filed = (String) nextItem;

            if ((filed.length() == 0)) {
                throw new IllegalArgumentException(
                    "The field should not be null to search.");
            }

            array[index++] = filed;
        }

        try {
            if (connection == null) {
                connect();
            }

            return connection.search(context,
                ((LDAPConnectionInformation) connectionInfo).getScope(),
                searchString, array);
        } catch (LDAPSDKException e) {
            //wrap the LDAPSDKException with PersistenceOperationException
            throw new PersistenceOperationException("LDAPSDKException occurs while do search.",
                e);
        }
    }

    /**
     * <p>Close the connection.</p>
     *
     * @throws PersistenceOperationException is any error occurs.
     *
     */
    public synchronized void close() throws PersistenceOperationException {
        try {
            if (connection != null) {
                connection.disconnect();
            }
        } catch (LDAPSDKException e) {
            //wrap the LDAPSDKException with PersistenceOperationException
            throw new PersistenceOperationException("LDAPSDKException occues while close in LDAPConnectionStrategy.",
                e);
        }
    }

    /**
     * <p>
     * Search LDAP server and return the result.
     * The result is returned as a iterator of entrys.
     * </p>
     *
     *
     * @param searchString a search string used to conduct the search
     * @return an Iterator of Entry objects holding search result
     * @throws PersistenceOperationException
     * @throws NullPointerException if any parameter is Null
     * @throws IllegalArgumentException if the searchString parameter is an empty string
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected synchronized Object searchDataStore(String searchString)
        throws PersistenceOperationException {
        //check searchString null
        if (searchString == null) {
            throw new NullPointerException(
                "The searchString should not be null to searchDataStore in LDAPConnectionStrategy.");
        }

        //check searchString empty
        if (searchString.length() == 0) {
            throw new IllegalArgumentException(
                "The searchString should not be empty to searchDataStore in LDAPConnectionStrategy.");
        }

        try {
            return connection.search(context,
                ((LDAPConnectionInformation) connectionInfo).getScope(),
                searchString);
        } catch (LDAPSDKException e) {
            //wrap the LDAPSDKException with PersistenceOperationException
            throw new PersistenceOperationException("LDAPSDKException occurs while do searchDataStore.",
                e);
        }
    }

    /**
     * <p>
     * If the connection has not been created,then
     * connect to ldap server via the component LDAP-SDK.
     * Also the bind to the server is also done after the connection.
     * </p>
     *
     *
     * @throws PersistenceOperationException if any error when operating over data store
     */
    protected synchronized void connect() throws PersistenceOperationException {
        if (connection != null) {
            return;
        }

        try {
            LDAPConnectionInformation ldapInformation = (LDAPConnectionInformation) connectionInfo;

            LDAPSDK factory = (LDAPSDK) ldapInformation.getFactory();

            if (ldapInformation.isSecure()) {
                connection = factory.createSSLConnection();
            } else {
                connection = factory.createConnection();
            }

            connection.connect(ldapInformation.getHost(),
                ldapInformation.getPort());

            //bind the connection to the server
            String dnroot = ldapInformation.getDnroot();
            String password = ldapInformation.getPassword();
            //if it is not anonymous, bind the connection
            if ((dnroot != null) && (dnroot.length() > 0)
                    && (password != null) && (password.length() > 0)) {
                connection.authenticate(3, ldapInformation.getDnroot(),
                    ldapInformation.getPassword());
            } else {
                connection.authenticateAnonymous(3);
            }
        } catch (LDAPSDKException e) {
            //wrap the LDAPSDKException with PersistenceOperationException
            throw new PersistenceOperationException("LDAPSDKException occurs whilt connect.",
                e);
        }
    }
}
