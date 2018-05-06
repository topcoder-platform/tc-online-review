/*
 * TCS LDAP SDK Interface 1.0
 *
 * NetscapeConnection.java
 *
 * Copyright (c) 2004, TopCoder Inc. All rights reserved.
 */
package com.topcoder.util.net.ldap.sdkinterface.netscape;

import com.topcoder.util.net.ldap.sdkinterface.*;
import netscape.ldap.*;

import java.util.*;

/**
 * As a <em>Product</em> from the Abstract Factory Pattern, this class does the actual work of executing the operations
 * in the Netscape Directory LDAP.
 *
 * @author  cucu
 * @author  isv
 * @version 1.0 05/18/2004
 */
public class NetscapeConnection implements LDAPSDKConnection {

    /**
     * This is an instance of netscape.ldap.LDAPConnection. It should be instantiated in the createLDAPConnection method
     * and it will be used in all the methods of this class to do the real work with the Netscape LDAP.
     */
    private LDAPConnection connection = null;

    /**
     * <p>Connects to the LDAP using the host and port indicated.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions: </p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  host a host name.
     * @param  port a port number.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>host</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>port</code> is negative or greater than 65535.
     * @throws IllegalArgumentException if <code>host</code> is an empty string.
     */
    public void connect(String host, int port) throws LDAPSDKException {
        if (host == null) {
            throw new NullPointerException("Null host is prohibited.");
        }

        if (host.trim().length() == 0) {
            throw new IllegalArgumentException("Empty host is prohibited.");
        }

        if ((port < 0) || (port > 65535)) {
            throw new IllegalArgumentException("Invalid port number : " + port);
        }

        // TODO : Check if this should be replaced with authenticate
        try {
            connection.connect(host, port);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Disconnects from the LDAP server.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     */
    public void disconnect() throws LDAPSDKException {
        try {
            connection.disconnect();
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Adds an entry to the LDAP store.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKInvalidAttributeSyntaxException:The attribute sytnax is invalid</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax of the Entry is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  entry the entry to be added.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>entry</code> is <code>null</code>.
     */
    public void addEntry(Entry entry) throws LDAPSDKException {
        if (entry == null) {
            throw new NullPointerException("Null entries are prohibited.");
        }
        LDAPEntry netscapeEntry = null;
        netscapeEntry = buildLDAPEntry(entry);

        try {
            connection.add(netscapeEntry);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Deletes an entry from the LDAP store.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKOperationNotAllowedException:The operation is not allowed in the entry specified. For example, a
     *     non leaf entry can not be deleted</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN of the entry to be deleted.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public void deleteEntry(String dn) throws LDAPSDKException {
        checkDn(dn);
        try {
            connection.delete(dn);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Renames an entry in the LDAP. The entry at dn will be now at newDN, preserving all its attributes.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid for dn or newDN</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKOperationNotAllowedException:The operation is not allowed in the entry specified.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be renamed.
     * @param newDN the new DN for the entry.
     * @param deleteOldDN a <code>boolean</code> indicating whether the old name is not retained as an attribute value
     *        or not. If false, the old name is retained as an attribute value (for example, the entry might now have
     *        two values for the cn attribute: "cn=oldName" and "cn=newName").
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>newDN</code> is <code>null</code>
     * @throws IllegalArgumentException if any of given parameters is an empty string.
     */
    public void renameEntry(String dn, String newDN, boolean deleteOldDN) throws LDAPSDKException {
        checkDn(dn);
        checkDn(newDN);
        try {
            connection.rename(dn, newDN, deleteOldDN);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Update the entry in the Distinguished Name specified with the modifications provided in the update field </p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKSchemaViolationException:The operation could no be completed because it would violate the LDAP
     *     schema.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry to be updated was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param dn the DN of the entry to be updated.
     * @param update the changes to be applied.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>update</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public void updateEntry(String dn, Update update) throws LDAPSDKException {
        checkDn(dn);

        if (update == null) {
            throw new NullPointerException("Null updates are prohibited.");
        }

        LDAPModificationSet mods = null;
        mods = buildLDAPModificationSet(update);

        try {
            connection.modify(dn, mods);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Read an entry from the LDAP and retreive all the attributes </p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry dn was not found.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN of the entry to be read.
     * @return the entry read.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public Entry readEntry(String dn) throws LDAPSDKException {
        checkDn(dn);

        Entry entry = null;
        LDAPEntry netscapeEntry = null;

        try {
            netscapeEntry = connection.read(dn);
        } catch (LDAPException e) {
            rethrow(e);
        }

        // Note: the LDAPConnection Netscape LDAP SDK returns null if the entry is not found
        if (netscapeEntry != null) {
            entry = buildEntry(netscapeEntry);
        }

        if (entry == null) {
            throw new LDAPSDKNoSuchObjectException("The entry could not be located : DN = "  + dn, 32);
        }

        return entry;
    }

    /**
     * <p>Read an entry from the LDAP and retreive the specified attributes </p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKNoSuchAttributeException:The attribute was not found.</li>
     *     <li>LDAPSDKNoSuchObjectException:The entry was not found.</li>
     *     <li>LDAPSDKInvalidAttributeSyntaxException:The attribute sytnax is invalid</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN of the entry to be read.
     * @param  attrs the attributes to be retrieved.
     * @return the entry read.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>attrs</code> are <code>null</code>
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public Entry readEntry(String dn, String[] attrs) throws LDAPSDKException {
        checkDn(dn);
        checkAttributeNames(attrs);

        Entry entry = null;
        LDAPEntry netscapeEntry = null;

        try {
            netscapeEntry = connection.read(dn, attrs);
        } catch (LDAPException e) {
            rethrow(e);
        }

        if (netscapeEntry != null) {
            entry = buildEntry(netscapeEntry);
        }

        if (entry == null) {
            throw new LDAPSDKNoSuchObjectException("The entry could not be located : DN = "  + dn, 32);
        }

        return entry;
    }

    /**
     * <p>Search the LDAP for Entries. Many entries can be found, and they can be retrieved iterating through the
     * Iterator returned. </p>
     * <p>The scope must be one of:</p>
     * <ul type="disc">
     *     <li>SCOPE_BASE. Only the entry specified as the search base is include in the search. This is used when you
     *     already know the DN of the object and you would like to read its attributes. (The read method may also be
     *     used to read the values of a single entry).</li>
     *     <li>SCOPE_ONE. Objects one level below the base (but not including the base) are included in the search.</li>
     *     <li>SCOPE_SUB. All objects below the base, including the base itself, are included in the search.</li>
     * </ul>
     * <p>The filter string must have the format specified in rfc2254 (http://www.faqs.org/rfcs/rfc2254.html) <br/>
     * For example, some posible filters are: </p>
     * <ul type="disc">
     *     <li>(cn=Babs Jensen)</li>
     *     <li>(!(cn=Tim Howes))</li>
     *     <li>(&amp;(objectClass=Person)(|(sn=Jensen)(cn=Babs J*)))</li>
     *     <li>(o=univ*of*mich*)</li>
     * </ul>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidFilterException:The filter for the search is invalid.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax for the base is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  base the base DN to search.
     * @param  scope scope of the search.
     * @param  filter filter for the search.
     * @return an Iterator over a collection of Entry objects.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>base</code> or <code>filter</code> is <code>null</code>
     * @throws IllegalArgumentException if any of specified strings is empty.
     * @throws IllegalArgumentException if the scope is not one of the constants defined for this purpose.
     */
    public Iterator search(String base, int scope, String filter) throws LDAPSDKException {
        return search(base, scope, filter, new String[] {});
    }

    /**
     * <p>Search the LDAP for Entries, and retrieve the specified attributes for each matching Entry. Many entries can
     * be found, and they can be retrieved iterating through the Iterator returned. </p>
     * <p>The scope must be one of:</p>
     * <ul type="disc">
     * <li>SCOPE_BASE. Only the entry specified as the search base is include in the search. This is used when you
     * already know the DN of the object and you would like to read its attributes. (The read method may also be used
     * to read the values of a single entry).</li>
     * <li>SCOPE_ONE. Objects one level below the base (but not including the base) are included in the search. </li>
     * <li>SCOPE_SUB. All objects below the base, including the base itself, are included in the search.</li>
     * </ul>
     *
     * @param  base the base DN to search.
     * @param  scope scope of the search.
     * @param  filter filter for the search.
     * @param  attrs the attributes to be retreived.
     * @return an Iterator to a collection of Entry objects.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>base</code> or <code>filter</code> or <code>attrs</code> is <code>null
     *         </code>.
     * @throws IllegalArgumentException if any of specified strings is empty.
     * @throws IllegalArgumentException if the scope is not one of the constants defined for this purpose.
     */
    public Iterator search(String base, int scope, String filter, String[] attrs) throws LDAPSDKException {
        LDAPSearchResults results = null;

        checkAttributeNames(attrs);

        if (base == null) {
            throw new NullPointerException("Null base is prohibited.");
        }

        if (base.trim().length() == 0) {
            throw new IllegalArgumentException("Empty base is prohibited.");
        }

        if ((scope != LDAPSDKConnection.SCOPE_BASE) && (scope != LDAPSDKConnection.SCOPE_ONE)
                && (scope != LDAPSDKConnection.SCOPE_SUB)) {
            throw new IllegalArgumentException("Invalid scope of the search request : " + scope);
        }

        if (filter == null) {
            throw new NullPointerException("Null filter is prohibited.");
        }

        if (filter.trim().length() == 0) {
            throw new IllegalArgumentException("Empty filter is prohibited.");
        }


        try {
            results = connection.search(base, scope, filter, attrs, false);
        } catch (LDAPException e) {
            rethrow(e);
        }

        return new LDAPSearchResultsIterator(results);
    }

    /**
     * <p>Authenticate to the LDAP server using the specified dn and password</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN to be authenticated with.
     * @param  password the password for authenticating to that DN.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>password</code> is <code>null</code>.
     * @throws IllegalArgumentException if one or both are empty strings.
     */
    public void authenticate(String dn, String password) throws LDAPSDKException {
        checkDn(dn);
        checkPassword(password);
        try {
            connection.authenticate(dn, password);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Authenticate to the LDAP server using the specified version, dn and password</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  version the LDAP protocol version
     * @param  dn the DN to be authenticated with.
     * @param  password the password for authenticating to that DN.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>password</code> is <code>null</code>.
     * @throws IllegalArgumentException if one or both are empty strings.
     */
    public void authenticate(int version, String dn, String password) throws LDAPSDKException {
        checkDn(dn);
        checkPassword(password);
        try {
            connection.authenticate(version, dn, password);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Authenticate as anonymous to the LDAP server.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *      bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     */
    public void authenticateAnonymous() throws LDAPSDKException {
        try {
            connection.authenticate(null, null);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Authenticate as anonymous to the LDAP server using the specified version</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *      bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  version the LDAP protocol version
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     */
    public void authenticateAnonymous(int version) throws LDAPSDKException {
        try {
            connection.authenticate(version, null, null);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Authenticate to the LDAP server using the specified dn, the configuration properties in the props hashtable
     * and the callback object cbh</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN to be authenticated with.
     * @param  mechanisms the SASL mechanisms to be used.
     * @param  props properties for the SASL.
     * @param  cbh a class which the SASL framework can call to obtain additional required information.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code>, <code>props</code> or <code>cbh</code> is <code>null</code>.
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public void authenticateSASL(String dn, String[] mechanisms, Hashtable props, Object cbh) throws LDAPSDKException {
        checkDn(dn);
        if ((cbh == null) || (props == null) || (mechanisms == null)) {
            throw new NullPointerException("Null parameters are prohibited.");
        }

        try {
            connection.authenticate(dn, mechanisms, props, cbh);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }


    /**
     * <p>Bind to the LDAP server using the specified dn and password</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN to be authenticated with.
     * @param  password the password for authenticating to that DN.
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code> or <code>password</code> is <code>null</code>
     * @throws IllegalArgumentException if any of specified parameters is empty string.
     */
    public void bind(String dn, String password) throws LDAPSDKException {
        checkDn(dn);
        checkPassword(password);
        try {
            connection.bind(dn, password);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Binds as anonymous to the LDAP server.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *      bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     */
    public void bindAnonymous() throws LDAPSDKException {
        try {
            connection.bind(null, null);
        } catch (LDAPException e) {
            rethrow(e);
        }
    }

    /**
     * <p>Bind to the LDAP server using the specified dn, the configuration properties in the props hashtable and the
     * callback object cbh.</p>
     * <p>Throws an LDAPSDKException or one of its inherited exceptions:</p>
     * <ul type="disc">
     *     <li>LDAPSDKCommunicationException:this exception is thrown when an operation was unsuccesfull because of an
     *     error in the communication with the server or in the server</li>
     *     <li>LDAPSDKAuthenticationMethodException:The server is expecting a different authentication method or there
     *     is an error in the way of authenticating.</li>
     *     <li>LDAPSDKAccesDeniedException: the operation could not be completed due insuficient access rights.</li>
     *     <li>LDAPSDKLimitsExceededException:Processing time or maximum entries found in the server exceeded its
     *     limits.</li>
     *     <li>LDAPSDKInvalidDNSyntaxException:The DN sytnax is invalid</li>
     *     <li>LDAPSDKCallErrorException:The call to an LDAP service was unsuccesfull due to a protocol difference, a
     *     bad parameter or not supported functionality.</li>
     *     <li>LDAPSDKException:An unknow error was found.</li>
     * </ul>
     *
     * @param  dn the DN to be authenticated with.
     * @param  mechanisms the SASL mechanisms to be used.
     * @param  props properties for the SASL.
     * @param  cbh a class which the SASL framework can call to obtain additional required information
     * @throws LDAPSDKException if any LDAP exception occurs while performing the requested operation. See details above
     *         for specific subclasses of <code>LDAPSDKException</code> that may be thrown by this method.
     * @throws NullPointerException if <code>dn</code>, <code>props</code> or <code>cbh</code> is <code>null</code>
     * @throws IllegalArgumentException if <code>dn</code> is an empty string.
     */
    public void bindSASL(String dn, String[] mechanisms, Hashtable props, Object cbh) throws LDAPSDKException {
        authenticateSASL(dn, mechanisms, props, cbh);
    }

    /**
     * This method creates an object of type LDAPConnection and stores it on the connection member variable. That
     * object will do the actual work on the LDAP. </p>
     * <p>It is declared as package because only the NetscapeFactory can call him.
     *
     * @param isSSL true if the connection is SSL
     */
    void createLDAPConnection(boolean isSSL) {
        if (isSSL) {
            this.connection = new LDAPConnection(new LDAPSSLSocketFactory());
        } else {
            this.connection = new LDAPConnection();
        }
    }

    /**
     * A helper method converting the <code>LDAPException</code> thrown by <code>Netscape LDAP SDK</code> to instance of
     * corresponding subclass of <code>LDAPSDKException</code> depending on the value of LDAP result code. Uses <code>
     * LDAPSDKExceptionFactory</code> to convert the original exception to custom <code>LDAPSDKException</code>.
     *
     * @param  e an original <code>LDAPException</code> thrown by <code>Netscape LDAP SDK</code>.
     * @throws LDAPSDKException
     */
    private void rethrow(LDAPException e) throws LDAPSDKException {
        throw LDAPSDKExceptionFactory.createException(e.getMessage(), e.getLDAPResultCode(), e);
    }

    /**
     * A helper method building the <code>LDAPAttributeSet</code> object accepted by <code>Netscape LDAP SDK</code> from
     * specified <code>Entry</code> object.
     *
     * @param  entry an original <code>Entry</code> object.
     * @return a <code>LDAPAttributeSet</code> created from data provided with original <code>Entry</code> object.
     */
    private LDAPAttributeSet buildAttributeSet(Entry entry) {
        LDAPAttributeSet attributeSet = new LDAPAttributeSet();
        LDAPAttribute attribute = null;

        String attributeName = null;
        Values values = null;

        Map attributes = entry.getAttributes();
        Map.Entry mapEntry = null;
        Iterator iterator = attributes.entrySet().iterator();

        while (iterator.hasNext()) {
            mapEntry = (Map.Entry) iterator.next();
            attributeName = (String) mapEntry.getKey();
            values = (Values) mapEntry.getValue();
            attribute = buildLDAPAttribute(attributeName, values);
            attributeSet.add(attribute);
        }

        return attributeSet;
    }

    /**
     * A helper method building the <code>LDAPAttribute</code> object accepted by <code>Netscape LDAP SDK</code> from
     * specified attribute name and <code>Value</code> object.
     *
     * @param  attributeName a <code>String</code> representing the attribute name.
     * @param  values an original <code>Values</code> object.
     * @return a <code>LDAPAttribute</code> created from data provided with original <code>Values</code> object with
     *         specified name.
     */
    private LDAPAttribute buildLDAPAttribute(String attributeName, Values values) {
        LDAPAttribute attribute = new LDAPAttribute(attributeName);
        List textValues = null;
        List binaryValues = null;

        if (values != null) {
            textValues = values.getTextValues();
            for (int i = 0; i < textValues.size(); i++) {
                attribute.addValue((String) textValues.get(i));
            }

            binaryValues = values.getBinaryValues();
            for (int i = 0; i < binaryValues.size(); i++) {
                attribute.addValue((byte[]) binaryValues.get(i));
            }
        }

        return attribute;
    }

    /**
     * Builds a Netscape's <code>LDAPEntry</code> object from TC LDAP SDK <code>Entry</code> object.
     *
     * @param  entry an <code>Entry</code> object representing the source <code>TC LDAP SDK Entry</code> to build
     *         Netscape <code>LDAPEntry</code> from.
     * @return an <code>LDAPEntry</code> created from original <code>Entry</code> object.
     */
    private LDAPEntry buildLDAPEntry(Entry entry) {
        LDAPAttributeSet attributeSet = buildAttributeSet(entry);
        LDAPEntry netscapeEntry = new LDAPEntry(entry.getDn(), attributeSet);
        return netscapeEntry;
    }

    /**
     * A helper method building the <code>Entry</code> object accepted by <code>LDAP SDK Interface</code> component from
     * specified <code>LDAPEntry</code> object provided by <code>Netscape LDAP SDK</code>.
     *
     * @param  netscapeEntry an original <code>LDAPEntry</code> object.
     * @return a <code>Entry</code> created from data provided with original <code>LDAPEntry</code> object.
     */
    private Entry buildEntry(LDAPEntry netscapeEntry) {
        Entry entry = new Entry(netscapeEntry.getDN());

        LDAPAttributeSet netscapeAttributes = netscapeEntry.getAttributeSet();
        Enumeration attributeEnum = netscapeAttributes.getAttributes();

        String attributeName = null;
        Values values = null;

        while (attributeEnum.hasMoreElements()) {
            LDAPAttribute netscapeAttribute = (LDAPAttribute) attributeEnum.nextElement();
            attributeName = netscapeAttribute.getName();
            values = new Values();
            values.add(netscapeAttribute.getByteValueArray());
            values.add(netscapeAttribute.getStringValueArray());
            entry.setAttribute(attributeName, values);
        }

        return entry;
    }

    /**
     * A helper method building the <code>LDAPModificationSet</code> object accepted by <code>Netscape LDAP SDK</code>
     * from specified <code>Update</code> object.
     *
     * @param  updates an original <code>Update</code> object.
     * @return a <code>LDAPModificationSet</code> created from data provided with original <code>Update</code> object.
     */
    private LDAPModificationSet buildLDAPModificationSet(Update updates) {
        LDAPModificationSet netscapeModifications = new LDAPModificationSet();
        LDAPAttribute netscapeAttribute = null;
        Modification sdkModification = null;

        String attributeName = null;
        Values values = null;

        List sdkModifications = updates.getModifications();

        for (int i = 0; i < sdkModifications.size(); i++) {
            sdkModification = (Modification) sdkModifications.get(i);
            attributeName = sdkModification.getAttributeName();
            values = sdkModification.getValues();
            netscapeAttribute = buildLDAPAttribute(attributeName, values);
            netscapeModifications.add(getNetscapeModificationType(sdkModification.getType()), netscapeAttribute);
        }

        return netscapeModifications;
    }

    /**
     * A helper method converting the LDAP modification types declared by <code>LDAP SDK Interface</code> component to
     * corresponding LDAP modification types declared by <code>Netscape LDAP SDK</code>.
     *
     * @param  type an <code>int</code> representing the type of modification declared by <code>LDAP SDK Interface
     *         </code> component.
     * @return an <code>int</code> representing the type of modification declared by <code>Netscape LDAP SDK</code>
     *         or -1 if corresponding Netscape's type can not be mapped.
     */
    private int getNetscapeModificationType(int type) {
        switch (type) {
            case (Modification.ADD):
                {
                    return LDAPModification.ADD;
                }
            case (Modification.REPLACE):
                {
                    return LDAPModification.REPLACE;
                }
            case (Modification.DELETE_ATTRIBUTE):
            case (Modification.DELETE_VALUE):
                {
                    return LDAPModification.DELETE;
                }
        }
        return -1;
    }

    /**
     * A helper class providing an <code>Iterator</code> view for search results returned by <code>Netscape LDAP SDK
     * </code>.
     *
     * @author  isv
     * @version 1.0 05/18/2004
     */
    private class LDAPSearchResultsIterator implements Iterator {

        /**
         * A <code>LDAPSearchResults</code> containing the search results returned by <code>Netscape LDAP SDK</code>.
         */
        private LDAPSearchResults results = null;

        /**
         * Constructs new <code>LDAPSearchResultsIterator</code> to iterate over specified search results.
         *
         * @param results a <code>LDAPSearchResult</code> containing the search results returned by <code>Netscape LDAP
         *        SDK</code>.
         */
        private LDAPSearchResultsIterator(LDAPSearchResults results) {
            this.results = results;
        }

        /**
         * Returns true if the iteration has more elements. (In other words, returns true if next would return an
         * element rather than throwing an exception.)
         *
         * @return true if the iterator has more elements.
         */
        public boolean hasNext() {
            return results.hasMoreElements();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration.
         * @throws NoSuchElementException if iteration has no more elements.
         */
        public Object next() {
            Object next = results.nextElement();

            if (next instanceof LDAPEntry) {
                Entry nextEntry = null;
                nextEntry = buildEntry((LDAPEntry) next);
                return nextEntry;
            }

            return next;
        }

        /**
         * Does nothing.
         */
        public void remove() {
        }
    }

    /**
     * A helper "guard" method rejecting the invalid distinguished names throwing the appropriate runtime exception
     * (either <code>NulPointerException</code> or <code>IllegalArgumentException</code>).
     *
     * @param  dn a <code>String</code> representing the DN to check.
     * @throws NullPointerException if specified <code>dn</code> is <code>null</code>.
     * @throws IllegalArgumentException if specified argument is empty.
     */
    private void checkDn(String dn) {
        if (dn == null) {
            throw new NullPointerException("Null DNs are prohhibited.");
        }

        if (dn.trim().length() == 0) {
            throw new IllegalArgumentException("Empty DNs are prohibited.");
        }
    }

    /**
     * A helper "guard" method rejecting the invalid arrays of attribute names throwing the appropriate runtime
     * exception (either <code>NulPointerException</code> or <code>IllegalArgumentException</code>).
     *
     * @param  attrs a <code>String</code> array with attribute names.
     * @throws NullPointerException if specified <code>array</code> is <code>null</code>.
     */
    private void checkAttributeNames(String[] attrs) {
        if (attrs == null) {
            throw new NullPointerException("Null array of attribute names is prohibited");
        }

        for (int i = 0; i < attrs.length; i++) {
            if (attrs[i] == null) {
                throw new NullPointerException("Null attribute name is prohibited");
            }
        }
    }

    /**
     * A helper "guard" method rejecting the invalid password values throwing the appropriate runtime exception (either
     * <code>NulPointerException</code> or <code>IllegalArgumentException</code>).
     *
     * @param  password a <code>String</code> representing the password to check.
     * @throws NullPointerException if specified <code>password</code> is <code>null</code>.
     * @throws IllegalArgumentException if specified argument is empty.
     */
    private void checkPassword(String password) {
        if (password == null) {
            throw new NullPointerException("Null password is prohhibited.");
        }

        if (password.trim().length() == 0) {
            throw new IllegalArgumentException("Empty password is prohibited.");
        }
    }
}
